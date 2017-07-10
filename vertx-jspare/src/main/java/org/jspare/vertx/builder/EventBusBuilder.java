/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.vertx.builder;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.MethodAnnotationMatchProcessor;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jspare.core.Environment;
import org.jspare.vertx.utils.ClasspathScannerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class EventBusBuilder.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Accessors(fluent = true)

/*
 * (non-Javadoc)
 *
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(callSuper = false)
public class EventBusBuilder extends AbstractBuilder<Void> {

  /** The Constant NUMBER_CLASSPATH_SCANNER_THREADS. */
  private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

  /**
   * Creates the.
   *
   * @param vertx
   *          the vertx
   * @return the event bus builder
   */
  public static EventBusBuilder create(Vertx vertx) {

    return new EventBusBuilder(vertx);
  }

  /**
   * Vertx.
   *
   * @return the vertx
   */
  @Getter
  private final Vertx vertx;

  /**
   * Scan classpath.
   *
   * @return true, if successful
   */
  @Getter

  /**
   * Scan classpath.
   *
   * @param scanClasspath
   *          the scan classpath
   * @return the event bus builder
   */
  @Setter
  private boolean scanClasspath;

  /**
   * Classes.
   *
   * @return the list
   */
  @Getter

  /**
   * Classes.
   *
   * @param classes
   *          the classes
   * @return the event bus builder
   */
  @Setter
  private List<Class<?>> classes;

  /**
   * Scan specs.
   *
   * @return the list
   */
  @Getter

  /**
   * Scan specs.
   *
   * @param scanSpecs
   *          the scan specs
   * @return the event bus builder
   */
  @Setter
  private List<String> scanSpecs;

  /**
   * Instantiates a new event bus builder.
   *
   * @param vertx
   *          the vertx
   */
  private EventBusBuilder(Vertx vertx) {

    this.vertx = vertx;
    scanClasspath = false;
    classes = new ArrayList<>();
    scanSpecs = new ArrayList<>();
  }

  /*
   * (non-Javadoc)
   *
   * @see org.jspare.vertx.builder.AbstractBuilder#build()
   */
  @Override
  public Void build() {

    if (scanClasspath) {
      scanSpecs.clear();
      scanSpecs.add(".*");
    }

    // Iterate eventBusPackages scannig and adding classes to
    // eventBusClasses

    MethodAnnotationMatchProcessor processor = (c, m) -> classes.add(c);
    scanSpecs.forEach(scanSpec -> {

      ClasspathScannerUtils.scanner(scanSpec)
          .matchClassesWithMethodAnnotation(org.jspare.vertx.annotation.Consumer.class, processor)
          .scan(NUMBER_CLASSPATH_SCANNER_THREADS);
    });

    List<EventBusData> consumers = new ArrayList<>();

    // Iterate eventBusClasses and add consumers to will process
    classes.forEach(c -> consumers.addAll(Environment.my(EventBusCollector.class).collect(c)));

    // Process consumers
    EventBus eventBus = vertx.eventBus();
    consumers.forEach(md -> eventBus.consumer(md.name(), md.wrap()));

    return null;
  }

}

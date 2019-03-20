/*
 * Copyright 2016 Jspare.org.
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

import org.jspare.core.Environment;
import org.jspare.vertx.annotation.Consumer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class EventBusCollector.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class EventBusCollector implements Collector<Collection<EventBusData>> {

  /*
   * (non-Javadoc)
   *
   * @see org.jspare.vertx.builder.Collector#collect(java.lang.Class,
   * java.lang.Object[])
   */
  @Override
  public Collection<EventBusData> collect(Class<?> clazz, Object... args) {

    List<EventBusData> handlers = new ArrayList<>();
    List<Method> methodsCollected = new ArrayList<>();

    Arrays.asList(clazz.getDeclaredMethods()).forEach(m -> {

      if (m.isAnnotationPresent(Consumer.class)) {

        methodsCollected.add(m);
      }
    });

    if (!methodsCollected.isEmpty()) {

      Object instance = getInstance(clazz);
      handlers.addAll(methodsCollected.stream().map(method -> {

        Consumer consumer = method.getAnnotation(Consumer.class);
        return new EventBusData(instance, method, consumer.value());
      }).collect(Collectors.toList()));
    }
    return handlers;
  }

  /**
   * Gets the single instance of EventBusCollector.
   *
   * @param clazz the clazz
   * @return single instance of EventBusCollector
   */
  private Object getInstance(Class<?> clazz) {
    return Environment.my(clazz);
  }
}

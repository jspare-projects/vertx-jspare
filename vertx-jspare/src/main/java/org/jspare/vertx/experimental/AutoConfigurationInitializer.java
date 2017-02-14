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
package org.jspare.vertx.experimental;

import static org.jspare.vertx.builder.ClasspathScannerUtils.ALL_SCAN_QUOTE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jspare.core.annotation.Resource;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.builder.ClasspathScannerUtils;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * Responsible to init {@link AutoConfiguration} life cycle.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Resource
@Slf4j
public class AutoConfigurationInitializer {

  /** The vertx. */
  @VertxInject
  private Vertx vertx;

  /**
   * Initialize.
   *
   * @param verticle
   *          the verticle
   */
  public void initialize(Verticle verticle) {

    if (verticle == null || verticle.getVertx() == null || !verticle.getClass().isAnnotationPresent(AutoConfiguration.class)) {

      return;
    }
    AutoConfiguration cfg = verticle.getClass().getAnnotation(AutoConfiguration.class);

    Map<String, Class<?>> moduleClasses = new HashMap<>();
    ClassAnnotationMatchProcessor processor = (c) -> moduleClasses.put(c.getAnnotation(Module.class).value(), c);
    ClasspathScannerUtils.scanner(ALL_SCAN_QUOTE).matchClassesWithAnnotation(Module.class, processor).scan();

    Arrays.asList(cfg.value()).forEach(m -> {

      try {

        Class<?> mclasse = moduleClasses.get(m.value());
        Configurable mi = (Configurable) mclasse.newInstance();
        if (mi != null) {

          mi.execute(verticle, m.config());
        }
      } catch (Exception e) {

        log.error("Faile init autoconfig", e);
      }
    });
  }
}
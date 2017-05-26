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
package org.jspare.vertx.autoconfiguration;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.jspare.core.MySupport;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Responsible to init {@link AutoConfiguration} life cycle.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Slf4j
public class AutoConfigurationInitializer extends MySupport {

  /** The vertx. */
  @Inject
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
    Arrays.asList(cfg.value()).forEach(m -> {

      try {

        Configurable mi = (Configurable) m.value().newInstance();
        if (mi != null) {

          mi.execute(verticle, m.config());
        }
      } catch (Exception e) {

        log.error("Faile init autoconfig", e);
      }
    });
  }
}

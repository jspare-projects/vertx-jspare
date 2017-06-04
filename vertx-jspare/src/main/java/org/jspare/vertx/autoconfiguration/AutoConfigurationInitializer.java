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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.jspare.core.MySupport;
import org.jspare.vertx.concurrent.ReduceFuture;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Responsible to init {@link AutoConfiguration} life cycle.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Slf4j
public class AutoConfigurationInitializer extends MySupport {

  /**
   * The vertx.
   */
  @Inject
  private Vertx vertx;

  /**
   * Initialize.
   *
   * @param verticle the verticle
   */
  public Future<Void> initialize(Verticle verticle) {

    Future<Void> initFuture = Future.future();

    if (verticle == null || verticle.getVertx() == null || !verticle.getClass().isAnnotationPresent(AutoConfiguration.class)) {
      initFuture.complete();
      return initFuture;
    }

    if (log.isDebugEnabled()) {
      log.debug("Initialize Auto Configuration");
    }
    AutoConfiguration cfg = verticle.getClass().getAnnotation(AutoConfiguration.class);
    final JsonObject config = new JsonObject();
    if (verticle instanceof AbstractVerticle) {
      config.mergeIn(((AbstractVerticle) verticle).config());
    }

    if (log.isDebugEnabled()) {
      log.debug("Verticle: {}", verticle.getClass().getName());
      log.debug("Config: {}", config.encode());
    }
    final List<Supplier<Future>> futures = new ArrayList<>();
    Arrays.asList(cfg.value()).forEach(m -> {
      try {

        AutoConfigurationResource mi = (AutoConfigurationResource) m.value().newInstance();
        synchronized (mi) {

          if (log.isDebugEnabled()) {
            log.debug("Load Module: {}", m.value().getName());
          }
          if (mi != null) {
            futures.add(() -> mi.init(verticle, config));
          }
        }
      } catch (Exception e) {

        log.error("Failed to load {}", m.value().getName(), e);
      }
    });

    ReduceFuture.create(futures).reduce().setHandler(ar -> {
      if (ar.succeeded()) {

        initFuture.complete();
      } else {

        initFuture.fail(ar.cause());
      }
    });
    return initFuture;
  }
}

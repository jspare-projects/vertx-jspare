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
package org.jspare.vertx.internal;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jspare.core.Environment;
import org.jspare.core.MySupport;
import org.jspare.vertx.Modularized;
import org.jspare.vertx.Module;
import org.jspare.vertx.annotation.Modules;
import org.jspare.vertx.concurrent.ReduceFuture;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static org.jspare.core.Environment.inject;

/**
 * Responsible to init {@link Modules} life cycle.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Slf4j
public class ModuleInitializer extends MySupport {

  private List<Class<? extends Module>> loadedModules;

  /**
   * The vertx.
   */
  @Inject
  private Vertx vertx;

  public ModuleInitializer() {
    loadedModules = new ArrayList<>();

  }

  /**
   * Initialize one modularized instance
   * @param modularized the instance
   * @return the future
   */
  public Future<Void> initialize(Modularized modularized) {

    if (log.isDebugEnabled()) {
      log.debug("Initialize Auto Configuration for {}", modularized.getClass().getName());
    }

    Future<Void> initFuture = Future.future();
    if (modularized == null || !modularized.getClass().isAnnotationPresent(Modules.class)) {
      initFuture.complete();
      return initFuture;
    }

    final JsonObject config = new JsonObject();
    config.mergeIn(modularized.getConfig());

    if (log.isDebugEnabled()) {
      log.debug("Config: {}", modularized.getConfig());
    }

    final List<Supplier<Future>> futures = new ArrayList<>();
    try {
      lookupModules(futures, modularized);

    } catch (Throwable e) {

      return Future.failedFuture(e);
    }

    ReduceFuture.create(futures).reduce().setHandler(ar -> {
      if (ar.succeeded()) {

        initFuture.complete();
      } else {

        initFuture.fail(ar.cause());
      }
    });
    return initFuture;
  }

  private void lookupModules(List<Supplier<Future>> futures, Modularized modularized) throws InitilizationException {

    if (!modularized.getClass().isAnnotationPresent(Modules.class)) {
      return;
    }

    Modules modules = modularized.getClass().getAnnotation(Modules.class);
    JsonObject config = modularized.getConfig();

    Arrays.asList(modules.value()).forEach(m -> {

      Class<? extends Module> mClazz = m.value();
      try {

        if (hasLoaded(mClazz) && m.persistent()) {

          futures.add(() -> Future.succeededFuture());
          return;
        }

        if (m.persistent()) {
          loadedModules.add(mClazz);
        }

        Module mi = Environment.provide(mClazz);
        mi.setConfig(config);

        synchronized (mi) {

          lookupModules(futures, mi);

          if (log.isDebugEnabled()) {
            log.debug("Load Module: {}", m.value().getName());
          }
          if (mi != null) {
            futures.add(() -> initModule(modularized, config, mClazz, mi));
          }
        }
      } catch (Exception e) {

        log.error("Failed to load {}", m.value().getName(), e);
      }
    });
  }

  private boolean hasLoaded(Class<? extends Module> mName) {
    return loadedModules.contains(mName);
  }

  private boolean moduleHasDependencies(Module module) {
    return module.getClass().isAnnotationPresent(Modules.class);
  }

  private boolean allDependenciesHasLoaded(Module module) {
    return getDependenciesDisjunction(module).isEmpty();
  }

  private Collection<Class<? extends Module>> getDependenciesDisjunction(Module module) {
    Modules dependencies = module.getClass().getAnnotation(Modules.class);
    return CollectionUtils.disjunction(Arrays.asList(dependencies.value()), loadedModules);
  }

  private Future initModule(Modularized modularized, JsonObject config, Class<? extends Module> mClazz, Module mi) {
    /*if (moduleHasDependencies(mi) && !allDependenciesHasLoaded(mi)) {

      String dependenciesUnresolved = getDependenciesDisjunction(mi).stream().map(c -> c.getSimpleName()).collect(Collectors.joining(","));
      return Future.failedFuture(String.format("Could not load module %s the following dependencies need to be loaded: %s", mClazz.getSimpleName(), dependenciesUnresolved));
    }*/

    if (log.isDebugEnabled()) {
      log.debug("Init Module: {}", mi.getClass().getName());
    }

    inject(modularized);
    inject(mi);

    return mi.init(modularized, config);
  }

}

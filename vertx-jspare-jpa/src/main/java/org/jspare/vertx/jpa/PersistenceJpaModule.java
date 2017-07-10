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
package org.jspare.vertx.jpa;

import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;
import org.jspare.core.MySupport;
import org.jspare.core.internal.ReflectionUtils;
import org.jspare.jpa.PersistenceOptions;
import org.jspare.jpa.PersistenceUnitProvider;
import org.jspare.vertx.Module;
import org.jspare.vertx.DataObjectConverter;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The Class PersistenceJpaModule.
 * <p>
 * Used for load {@link org.springframework.data.jpa.provider.PersistenceProvider } and endpoints to simple rest api.
 * </p>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class PersistenceJpaModule extends MySupport implements Module {

  @Inject
  private PersistenceUnitProvider provider;

  private Map<String, PersistenceOptions> DATA_SOURCES = new HashMap<>();
  private List<String> ANNOTATED_CLASSES = new ArrayList<>();

  /*
   * (non-Javadoc)
   *
   * @see
   * org.jspare.vertx.experimental.Module#init(io.vertx.core.Verticle,
   * java.lang.String[])
   */
  @Override
  public Future<Void> init(Verticle verticle, JsonObject config) {

    Future<Void> loadFuture = Future.future();
    final Vertx vertx = verticle.getVertx();

    vertx.executeBlocking(f -> {
      try {

        load(verticle, config);

        DATA_SOURCES.entrySet().forEach(es -> {
          PersistenceOptions options = es.getValue();
          options.setAnnotatedClasses(ANNOTATED_CLASSES);
          provider.create(es.getKey(), options);
        });
        f.complete();
      } catch (Throwable t) {

        f.fail(t);
      }

    }, loadFuture.completer());
    return loadFuture;
  }

  private void load(Verticle verticle, JsonObject config) {

    if (verticle.getClass().isAnnotationPresent(AnnotatedClasses.class)) {
      AnnotatedClasses ann = verticle.getClass().getAnnotation(AnnotatedClasses.class);
      ANNOTATED_CLASSES.addAll(Arrays.asList(ann.value()));
    }

    ReflectionUtils
      .getMethodsWithAnnotation(verticle.getClass(), PersistenceUnitOptions.class)
      .forEach(m -> setPersistenceOption(verticle, m));

    if (DATA_SOURCES.isEmpty()) {

      JsonObject json = config.getJsonObject("persistence", new JsonObject());
      PersistenceOptions persistenceOptions = DataObjectConverter.fromJson(json, PersistenceOptions.class);
      DATA_SOURCES.put(PersistenceUnitProvider.DEFAULT_DS, persistenceOptions);
    }
  }

  @SneakyThrows
  private void setPersistenceOption(Verticle verticle, Method method) {

    method.setAccessible(true);
    String dataSource = ReflectionUtils.getAnnotation(method, PersistenceUnitOptions.class).value();
    PersistenceOptions options = (PersistenceOptions) method.invoke(verticle);
    DATA_SOURCES.put(dataSource, options);
  }
}

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
package org.jspare.vertx.web.module;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import lombok.SneakyThrows;
import org.jspare.vertx.autoconfiguration.Configurable;
import org.jspare.vertx.web.builder.HttpServerBuilder;
import org.jspare.vertx.web.builder.RouterBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Class HttpServerModule.
 * <p>
 * Used for load {@link HttpServer } and endpoints to simple rest api.
 * </p>
 * <p>This module use follow Handler by default on {@link io.vertx.ext.web.Route}.</p>
 *
 *  <ul>
 *      <li>{@link SessionHandler} with {@link LocalSessionStore}</li>
 *      <li>{@link CookieHandler}</li>
 *      <li>{@link BodyHandler} with FileUploads directory setted to UPLOAD_DIRECTORY</li>
 *      <li>{@link ResponseTimeHandler}</li>
 *      <li>{@link LoggerHandler}</li>
 *  </ul>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class HttpServerModule implements Configurable {

  /** The Constant NAME. */
  public static final String NAME = "httpServer";
  public static final String UPLOAD_DIRECTORY = "file-uploads";

  private Verticle verticle    ;

  /*
   * (non-Javadoc)
   *
   * @see
   * Configurable#execute(io.vertx.core.Verticle,
   * java.lang.String[])
   */
  @Override
  public void execute(Verticle verticle, String[] args) {

    this.verticle = verticle;
    final Vertx vertx = verticle.getVertx();

    HttpServerOptions options = getOptions(verticle);
    Router router = Router.router(vertx);

    if(verticle.getClass().isAnnotationPresent(Cors.class)){

      Cors cors = verticle.getClass().getAnnotation(Cors.class);
      router.route().handler(
        CorsHandler.create(cors.allowedOriginPattern())
          .allowedHeaders(Arrays.asList(cors.allowedHeaders()).stream().collect(Collectors.toSet()))
          .allowedMethods(Arrays.asList(cors.allowedMethods()).stream().collect(Collectors.toSet()))
      );
    }

    setRouter(router);

    HttpServer server = HttpServerBuilder.create(vertx).httpServerOptions(options).router(router).build();
    server.listen();
  }

  /**
   * Gets the options.
   *
   * @param verticle
   *          the verticle
   * @return the options
   */
  @SneakyThrows
  private HttpServerOptions getOptions(Verticle verticle) {

    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods()).stream()
        .filter(m -> HttpServerOptions.class.equals(m.getReturnType())).findFirst();
    if (oMethod.isPresent() && oMethod.get().getParameterCount() == 0) {
      Method method = oMethod.get();
      method.setAccessible(true);
      return (HttpServerOptions) method.invoke(verticle);
    }

    return new HttpServerOptions(verticle.getVertx().getOrCreateContext().config());
  }

  /**
   * Gets the router.
   *
   * @param router
   *          the router
   * @return the router
   */
  @SneakyThrows
  private void setRouter(Router router) {

    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods()).stream()
        .filter(m -> Router.class.equals(m.getReturnType())).findFirst();
    if (oMethod.isPresent() && oMethod.get().getParameterCount() == 0) {
      Method method = oMethod.get();
      method.setAccessible(true);
      method.invoke(verticle, router);
    }

    RouterBuilder.create(verticle.getVertx(), router)
        .addHandler(SessionHandler.create(LocalSessionStore.create(verticle.getVertx())))
        .addHandler(CookieHandler.create())
        .addHandler(BodyHandler.create(UPLOAD_DIRECTORY).setDeleteUploadedFilesOnEnd(true))
        .addHandler(ResponseTimeHandler.create())
        .addHandler(LoggerHandler.create())
        .scanClasspath(true)
        .build();
  }
}

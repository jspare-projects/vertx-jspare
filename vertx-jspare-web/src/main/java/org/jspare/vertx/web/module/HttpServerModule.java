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

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jspare.core.Environment;
import org.jspare.vertx.Module;
import org.jspare.vertx.web.annotation.module.*;
import org.jspare.vertx.web.builder.HttpServerBuilder;
import org.jspare.vertx.web.builder.RouterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The Class HttpServerModule.
 * <p>
 * Used for load {@link HttpServer } and endpoints to simple rest api.
 * </p>
 * <p>This module use follow Handler by default on {@link io.vertx.ext.web.Route}.</p>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Slf4j
public class HttpServerModule implements Module {

  /*
   * (non-Javadoc)
   *
   * @see
   * Module#init(io.vertx.core.Verticle,
   * java.lang.String[])
   */
  @Override
  public Future<Void> init(Verticle verticle, JsonObject config) {

    Future<Void> loadFuture = Future.future();
    final Vertx vertx = verticle.getVertx();

    HttpServerOptions options = getOptions(verticle, config);
    Router router = Router.router(vertx);
    setRouter(verticle, router);

    HttpServer server = HttpServerBuilder.create(vertx).httpServerOptions(options).router(router).build();
    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods())
      .stream()
      .filter(m -> m.isAnnotationPresent(ListenHandler.class))
      .findFirst();

    server.listen(ar -> {

      if (ar.failed()) {
        loadFuture.fail(ar.cause());
        return;
      }

      if (log.isDebugEnabled()) {
        log.debug("HttpServerModule started at port {}", ar.result().actualPort());
      }

      if (oMethod.isPresent()
        && oMethod.get().getParameterCount() == 1
        && oMethod.get().getParameters()[0].getType().equals(AsyncResult.class)) {

        try {

          Method method = oMethod.get();
          method.setAccessible(true);
          method.invoke(verticle, ar);
        } catch (Exception e) {

          loadFuture.fail(e);
          return;
        }
      }
      loadFuture.complete();
    });
    return loadFuture;
  }

  /**
   * Gets the options.
   *
   * @return the options
   */
  @SneakyThrows
  private HttpServerOptions getOptions(Verticle verticle, JsonObject config) {

    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods()).stream()
      .filter(m -> HttpServerOptions.class.equals(m.getReturnType())).findFirst();
    if (oMethod.isPresent() && oMethod.get().getParameterCount() == 0) {
      Method method = oMethod.get();
      method.setAccessible(true);
      return (HttpServerOptions) method.invoke(verticle);
    }

    return new HttpServerOptions(config);
  }

  /**
   * Gets the router.
   *
   * @param router the router
   * @return the router
   */
  @SneakyThrows
  private void setRouter(Verticle verticle, Router router) {

    final RouterBuilder builder = RouterBuilder.create(verticle.getVertx(), router);

    getHandlerAwareAnnotations(verticle).forEach(a -> setHandlerAnnotation(verticle, builder, a));

    buildMethodAware(verticle, builder);

    buildAuthHandler(verticle, builder);

    builderAnnAware(verticle, builder);

    builder.build();
  }

  private void buildMethodAware(Verticle verticle, RouterBuilder builder) throws InvocationTargetException, IllegalAccessException {

    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods())
      .stream()
      .filter(m -> m.isAnnotationPresent(RouterBuilderAware.class))
      .findFirst();

    if (oMethod.isPresent()
      && oMethod.get().getParameterCount() == 1
      && oMethod.get().getParameters()[0].getType().equals(RouterBuilder.class)) {
      Method method = oMethod.get();
      method.setAccessible(true);
      method.invoke(verticle, builder);
    }
    if (verticle.getClass().isAnnotationPresent(org.jspare.vertx.web.annotation.module.Routes.class)) {
      Routes routes = verticle.getClass().getAnnotation(Routes.class);
      builder.scanClasspath(routes.scanClasspath());
      Arrays.asList(routes.routes()).forEach(builder::addRoute);
      Arrays.asList(routes.skipRoutes()).forEach(builder::skipRoute);
      Arrays.asList(routes.scanPackages()).forEach(builder::addRoutePackage);
    }
  }

  private Stream<Annotation> getHandlerAwareAnnotations(Verticle verticle) {
    Class<?> clazz = verticle.getClass();
    return Arrays.asList(clazz.getAnnotations()).stream().filter(a -> a.annotationType().isAnnotationPresent(HandlerAware.class));
  }

  @SneakyThrows
  private <A extends Annotation> void setHandlerAnnotation(Verticle verticle, RouterBuilder builder, A annotation) {

    Class<?> factoryClass = ((Annotation) annotation).annotationType().getDeclaredClasses()[0];
    if (factoryClass == null) {
      if (log.isWarnEnabled()) {
        log.warn("AnnotationAware class {} without AnnotationHandlerFactory", annotation.getClass().getName());
      }
      return;
    }
    AnnotationHandlerFactory<A> factory = (AnnotationHandlerFactory<A>) factoryClass.newInstance();
    io.vertx.core.Handler<RoutingContext> handler = factory.factory(annotation, verticle);
    builder.addHandler(handler);

    if (log.isDebugEnabled()) {
      log.debug("Handler {} created and registered on RouterBuilder by AnnotationHandlerAware", handler.getClass().getName());
    }
  }

  private void buildAuthHandler(Verticle verticle, RouterBuilder builder) throws IllegalAccessException, InstantiationException {

    if (verticle.getClass().isAnnotationPresent(AuthHandler.class)) {
      AuthHandler ann = verticle.getClass().getAnnotation(AuthHandler.class);
      Supplier<io.vertx.ext.web.handler.AuthHandler> supplier = () -> {
        return Environment.provide(ann.value());
      };
      builder.authHandler(supplier);
    }
  }

  private void builderAnnAware(Verticle verticle, RouterBuilder builder) throws IllegalAccessException, InstantiationException {

    if (verticle.getClass().isAnnotationPresent(BuilderAware.class)) {
      for (Class<? extends Handler<RouterBuilder>> clazz : verticle.getClass().getAnnotation(BuilderAware.class).value()) {
        Handler<RouterBuilder> handler = clazz.newInstance();
        handler.handle(builder);
      }
    }
  }
}

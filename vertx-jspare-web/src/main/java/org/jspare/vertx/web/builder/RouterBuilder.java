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
package org.jspare.vertx.web.builder;

import static org.jspare.core.container.Environment.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jspare.vertx.builder.AbstractBuilder;
import org.jspare.vertx.builder.ClasspathScannerUtils;
import org.jspare.vertx.web.handler.DefaultHandler;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.MethodAnnotationMatchProcessor;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Accessors(fluent = true)

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(callSuper = false)
public class RouterBuilder extends AbstractBuilder<Router> {

  /** The Constant NUMBER_CLASSPATH_SCANNER_THREADS. */
  private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

  /** The Constant DEFAULT_AUTH_HANDLER_KEY. */
  public static final String DEFAULT_AUTH_HANDLER_KEY = "default-auth-handler";

  /**
   * Creates the.
   *
   * @param vertx
   *          the vertx
   * @return the router builder
   */
  public static RouterBuilder create(Vertx vertx) {

    return new RouterBuilder(vertx, Router.router(vertx));
  }

  /**
   * Creates the.
   *
   * @param vertx
   *          the vertx
   * @param router
   *          the router
   * @return the router builder
   */
  public static RouterBuilder create(Vertx vertx, Router router) {

    return new RouterBuilder(vertx, router);
  }

  /**
   * Vertx.
   *
   * @return the vertx
   */
  @Getter
  private final Vertx vertx;

  /**
   * Router.
   *
   * @return the router
   */
  @Getter

  /**
   * Router.
   *
   * @param router
   *          the router
   * @return the router builder
   */
  @Setter
  private Router router;

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
   * @return the router builder
   */
  @Setter
  private boolean scanClasspath;

  /**
   * Handlers.
   *
   * @return the sets the
   */
  @Getter

  /**
   * Handlers.
   *
   * @param handlers
   *          the handlers
   * @return the router builder
   */
  @Setter
  private Set<Handler<RoutingContext>> handlers;

  /**
   * Handler class.
   *
   * @return the class<? extends handler< routing context>>
   */
  @Getter

  /**
   * Handler class.
   *
   * @param handlerClass
   *          the handler class
   * @return the router builder
   */
  @Setter
  private Class<? extends Handler<RoutingContext>> handlerClass;

  /**
   * The sock JS handler options. </br>
   * Used for all SockJsHandlers mapped by this RouterBuilder
   *
   */

  /**
   * Sock JS handler options.
   *
   * @return the sock JS handler options
   */
  @Getter

  /**
   * Sock JS handler options.
   *
   * @param sockJSHandlerOptions
   *          the sock JS handler options
   * @return the router builder
   */
  @Setter
  private SockJSHandlerOptions sockJSHandlerOptions;

  /**
   * Auth handler map.
   *
   * @return the map
   */
  @Getter
  private Map<String, Supplier<AuthHandler>> authHandlerMap;

  /**
   * Routes.
   *
   * @return the sets the
   */
  @Getter

  /**
   * Routes.
   *
   * @param routes
   *          the routes
   * @return the router builder
   */
  @Setter
  private Set<Class<?>> routes;

  /**
   * Skip routes.
   *
   * @return the sets the
   */
  @Getter

  /**
   * Skip routes.
   *
   * @param skipRoutes
   *          the skip routes
   * @return the router builder
   */
  @Setter
  private Set<Class<?>> skipRoutes;

  /**
   * Route packages.
   *
   * @return the sets the
   */
  @Getter

  /**
   * Route packages.
   *
   * @param routePackages
   *          the route packages
   * @return the router builder
   */
  @Setter
  private Set<String> routePackages;

  /**
   * Raml.
   *
   * @return true, if successful
   */
  @Getter

  /**
   * Raml.
   *
   * @param raml
   *          the raml
   * @return the router builder
   */
  @Setter
  private boolean raml;

  /**
   * Instantiates a new router builder.
   *
   * @param vertx
   *          the vertx
   * @param router
   *          the router
   */
  private RouterBuilder(Vertx vertx, Router router) {

    this.vertx = vertx;
    this.router = router;
    handlers = new HashSet<>();
    scanClasspath = false;
    routePackages = new HashSet<>();
    routes = new HashSet<>();
    skipRoutes = new HashSet<>();
    handlerClass = DefaultHandler.class;
    sockJSHandlerOptions = new SockJSHandlerOptions();
    authHandlerMap = new HashMap<>();
    raml = false;
  }

  /**
   * Adds the handler.
   *
   * @param handler
   *          the handler
   * @return the router builder
   */
  public RouterBuilder addHandler(Handler<RoutingContext> handler) {
    handlers.add(handler);
    return this;
  }

  /**
   * Adds the route.
   *
   * @param routeClass
   *          the route class
   * @return the router builder
   */
  public RouterBuilder addRoute(Class<?> routeClass) {

    routes.add(routeClass);
    return this;
  }

  /**
   * Adds the route package.
   *
   * @param routePackage
   *          the route package
   * @return the router builder
   */
  public RouterBuilder addRoutePackage(String routePackage) {

    routePackages.add(routePackage);
    return this;
  }

  /**
   * Auth handler.
   *
   * @param identity
   *          the identity
   * @param authHandler
   *          the auth handler
   * @return the router builder
   */
  public RouterBuilder authHandler(String identity, Supplier<AuthHandler> authHandler) {
    authHandlerMap.put(identity, authHandler);
    return this;
  }

  /**
   * Auth handler.
   *
   * @param authHandler
   *          the auth handler
   * @return the router builder
   */
  public RouterBuilder authHandler(Supplier<AuthHandler> authHandler) {

    authHandler(DEFAULT_AUTH_HANDLER_KEY, authHandler);
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jspare.vertx.builder.AbstractBuilder#build()
   */
  @Override
  public Router build() {

    log.debug("Building Router");

    handlers.forEach(h -> {
      log.debug("Routing handler [{}]", h.toString());
      router.route().handler(h);
    });

    collectRoutes();

    List<HandlerData> handlerDataList = new ArrayList<>();
    routes.stream().filter(c -> !skipRoutes.contains(c)).collect(Collectors.toSet())
        .forEach(c -> handlerDataList.addAll(my(RouteCollector.class).collect(c, this)));

    handlerDataList.forEach(hd -> {
      log.debug("Routing handler {}", hd.toStringLine());
      HandlerWrapper.prepareHandler(router, hd);
    });

    if (raml) {

      generateRamlRoute(router);
    }

    return router;
  }

  /**
   * Removes the auth handler.
   *
   * @param identity
   *          the identity
   * @return the router builder
   */
  public RouterBuilder removeAuthHandler(String identity) {
    authHandlerMap.remove(identity);
    return this;
  }

  /**
   * Route.
   *
   * @param builder
   *          the builder
   * @return the router builder
   */
  public RouterBuilder route(RouteBuilder builder) {
    log.debug("Routing custom route [{}]", builder.getClass());
    builder.create(router.route());
    return this;
  }

  /**
   * Skip route.
   *
   * @param routeClass
   *          the route class
   * @return the router builder
   */
  public RouterBuilder skipRoute(Class<?> routeClass) {

    skipRoutes.add(routeClass);
    return this;
  }

  /**
   * Collect routes.
   */
  private void collectRoutes() {

    if (scanClasspath) {
      routePackages.clear();
      routePackages.add(".*");
    }

    // Iterate routePackages scannig and adding classes to
    // routes
    MethodAnnotationMatchProcessor processor = (c, m) -> routes.add(c);
    routePackages.forEach(scanSpec -> {

      ClasspathScannerUtils.scanner(scanSpec)
          .matchClassesWithMethodAnnotation(org.jspare.vertx.web.annotation.handler.Handler.class, processor)
          .matchClassesWithMethodAnnotation(org.jspare.vertx.web.annotation.handler.FailureHandler.class, processor)
          .matchClassesWithMethodAnnotation(org.jspare.vertx.web.annotation.handler.BlockingHandler.class, processor)
          .matchClassesWithMethodAnnotation(org.jspare.vertx.web.annotation.handler.SockJsHandler.class, processor)
          .scan(NUMBER_CLASSPATH_SCANNER_THREADS);
    });
  }

  /**
   * Generate raml route.
   *
   * @param router
   *          the router
   */
  private void generateRamlRoute(Router router) {

  }
}
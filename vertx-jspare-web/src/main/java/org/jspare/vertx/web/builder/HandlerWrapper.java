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

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.jspare.vertx.web.handler.DefaultSockJSHandler;

import java.lang.reflect.InvocationTargetException;

import static org.jspare.vertx.web.handler.DefaultHandler.HANDLER_DATA;

/**
 * Instantiates a new handler wrapper.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@UtilityClass
public class HandlerWrapper {

  /**
   * Prepare handler.
   *
   * @param router      the router
   * @param handlerData the handler data
   */
  public void prepareHandler(Router router, HandlerData handlerData) {

    setHandler(router, handlerData);
  }

  /**
   * Creates the route.
   *
   * @param router the router
   * @param data   the data
   * @return the route
   */
  protected Route createRoute(Router router, HandlerData data) {
    Route route = router.route();

    setOrder(data, route);

    if (StringUtils.isNotEmpty(data.httpMethod())) {

      setMethod(data, route);
    }
    if (StringUtils.isNotEmpty(data.path())) {

      setPath(data, route);
    }
    if (StringUtils.isNotEmpty(data.consumes())) {

      setConsumes(data, route);
    }
    if (StringUtils.isNotEmpty(data.produces())) {

      setProduces(data, route);
    }
    return route;
  }

  /**
   * Sets the order.
   *
   * @param data  the data
   * @param route the route
   */
  private void setOrder(HandlerData data, Route route) {

    if (Integer.MIN_VALUE != data.order()) {

      route.order(data.order());
    }
  }

  /**
   * Sets the consumes.
   *
   * @param data  the data
   * @param route the route
   */
  protected void setConsumes(HandlerData data, Route route) {
    route.consumes(data.consumes());
  }

  /**
   * Sets the handler.
   *
   * @param router the router
   * @param data   the data
   */
  protected void setHandler(Router router, HandlerData data) {

    // Create auth handler if is setted
    if (data.authHandler() != null) {

      Route authRoute = createRoute(router, data);
      authRoute.handler(data.authHandler());
    }

    // Create route handler
    Route hdRegRoute = createRoute(router, data);
    Route route = createRoute(router, data);

    hdRegRoute.order(Integer.MIN_VALUE).handler(ctx -> {
      ctx.put(HANDLER_DATA, data);
      ctx.next();
    });

    if (HandlerType.HANDLER.equals(data.handlerType())) {

      route.handler(prepareHandler(data));
    } else if (HandlerType.FAILURE_HANDLER.equals(data.handlerType())) {

      route.failureHandler(prepareHandler(data));
    } else if (HandlerType.BLOCKING_HANDLER.equals(data.handlerType())) {

      route.blockingHandler(prepareHandler(data), false);
    } else if (HandlerType.SOCKETJS_HANDLER.equals(data.handlerType())) {

      route.handler(prepareSockJsHandler(data));
    }
  }

  /**
   * Sets the method.
   *
   * @param data  the data
   * @param route the route
   */
  protected void setMethod(HandlerData data, Route route) {
    route.method(HttpMethod.valueOf(data.httpMethod()));
  }

  /**
   * Sets the path.
   *
   * @param data  the data
   * @param route the route
   */
  protected void setPath(HandlerData data, Route route) {
    if (data.pathRegex()) {

      route.pathRegex(data.path());
    } else {

      route.path(data.path());
    }
  }

  /**
   * Sets the produces.
   *
   * @param data  the data
   * @param route the route
   */
  protected void setProduces(HandlerData data, Route route) {
    route.produces(data.produces());
  }

  /**
   * Prepare handler.
   *
   * @param handlerData the handler data
   * @return the handler
   */
  @SneakyThrows({InstantiationException.class, IllegalAccessException.class, IllegalArgumentException.class,
    InvocationTargetException.class, NoSuchMethodException.class})
  private Handler<RoutingContext> prepareHandler(HandlerData handlerData) {
    return handlerData.routeHandlerClass().getConstructor(HandlerData.class).newInstance(handlerData);
  }

  /**
   * Prepare sock js handler.
   *
   * @param handlerData the handler data
   * @return the handler
   */
  private Handler<RoutingContext> prepareSockJsHandler(HandlerData handlerData) {

    return handlerData.sockJSHandler().socketHandler(e -> DefaultSockJSHandler.socketHandler(handlerData, e));
  }
}

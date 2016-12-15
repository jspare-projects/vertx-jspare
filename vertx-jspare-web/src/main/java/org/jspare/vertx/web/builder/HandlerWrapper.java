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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public class HandlerWrapper {

	/**
	 * Prepare handler.
	 *
	 * @param router the router
	 * @param handlerData the data
	 */
	public static void prepareHandler(Router router, HandlerData handlerData) {

		log.debug("Mapping handler: {}", handlerData.toStringLine());

		setHandler(router, handlerData);
	}

	protected static Route createRoute(Router router, HandlerData data) {
		Route route = router.route().order(data.order());

		if (StringUtils.isNotEmpty(data.httpMethod())) {

			setMethod(data, route);
		}
		if (StringUtils.isNotEmpty(data.patch())) {

			setPatch(data, route);
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
	 * Sets the consumes.
	 *
	 * @param data the data
	 * @param route the route
	 */
	protected static void setConsumes(HandlerData data, Route route) {
		route.consumes(data.consumes());
	}

	/**
	 * Sets the handler.
	 *
	 * @param data the data
	 * @param route the route
	 */
	protected static void setHandler(Router router, HandlerData data) {

		// Create auth handler if is setted
		if(data.authHandler() != null){
			
			Route authRoute = createRoute(router, data);
			authRoute.handler(data.authHandler());
		}
		
		// Create route handler
		Route route = createRoute(router, data);
		if (HandlerType.HANDLER.equals(data.handlerType())) {

			route.handler(prepareHandler(data));
		} else if (HandlerType.FAILURE_HANDLER.equals(data.handlerType())) {

			route.failureHandler(prepareHandler(data));
		} else if (HandlerType.BLOCKING_HANDLER.equals(data.handlerType())) {

			route.blockingHandler(prepareHandler(data), false);
		}
	}

	/**
	 * Prepare handler.
	 *
	 * @param handlerData the handler data
	 * @return the handler
	 */
	@SneakyThrows({ InstantiationException.class, IllegalAccessException.class, IllegalArgumentException.class,
			InvocationTargetException.class, NoSuchMethodException.class })
	private static Handler<RoutingContext> prepareHandler(HandlerData handlerData) {

		return handlerData.routeHandlerClass().getConstructor(HandlerData.class).newInstance(handlerData);
	}

	/**
	 * Sets the method.
	 *
	 * @param data the data
	 * @param route the route
	 */
	protected static void setMethod(HandlerData data, Route route) {
		route.method(HttpMethod.valueOf(data.httpMethod()));
	}

	/**
	 * Sets the patch.
	 *
	 * @param data the data
	 * @param route the route
	 */
	protected static void setPatch(HandlerData data, Route route) {
		if (data.pathRegex()) {

			route.pathRegex(data.patch());
		} else {

			route.path(data.patch());
		}
	}

	/**
	 * Sets the produces.
	 *
	 * @param data the data
	 * @param route the route
	 */
	protected static void setProduces(HandlerData data, Route route) {
		route.produces(data.produces());
	}
}
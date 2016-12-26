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
package org.jspare.vertx.web.handler;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jspare.core.container.ContainerUtils;
import org.jspare.vertx.web.annotation.handling.ArrayModel;
import org.jspare.vertx.web.annotation.handling.ArrayModelParser;
import org.jspare.vertx.web.annotation.handling.Header;
import org.jspare.vertx.web.annotation.handling.MapModel;
import org.jspare.vertx.web.annotation.handling.MapModelParser;
import org.jspare.vertx.web.annotation.handling.Model;
import org.jspare.vertx.web.builder.HandlerData;
import org.jspare.vertx.web.handling.Handling;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j

/**
 * Instantiates a new default handler.
 *
 * @param handlerData
 *            the handler data
 */
@AllArgsConstructor
public class DefaultHandler implements Handler<RoutingContext> {

	/** The handler data. */
	protected final HandlerData handlerData;

	/*
	 * (non-Javadoc)
	 *
	 * @see io.vertx.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(RoutingContext ctx) {

		try {
			
			
			// Handle unhandled excetion
			ctx.vertx().exceptionHandler(t -> {
				
				ctx.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(ExceptionUtils.getStackTrace(t));
			});

			Object newInstance = instantiateHandler();

			setHandlingParameters(ctx, newInstance);

			// Wrap bodyEndHandler to share routingContext
			handlerData.bodyEndHandler().forEach(h -> ctx.addBodyEndHandler(event -> {

				h.handle(ctx);
			}));

			Object[] parameters = collectParameters(ctx);

			// Call method of handler data
			handlerData.method().invoke(newInstance, parameters);

		} catch (Throwable t) {

			catchInvoke(ctx, t);
		}
	}

	/**
	 * Catch invoke.
	 *
	 * @param routingContext
	 *            the routing context
	 * @param t
	 *            the t
	 */
	protected void catchInvoke(RoutingContext routingContext, Throwable t) {
		// Any server error return internal server error
		while (t.getCause() != null) {

			t = t.getCause();
		}
		log.info("Error: {}", handlerData.toStringLine());
		log.error(t.getMessage(), t);
		routingContext.response().setStatusCode(500).end(t.toString());
	}

	/**
	 * Collect parameters. This method is responsible to collect all parameters
	 * to send on handler method, resolving parameters and dependencies.
	 *
	 * @param routingContext
	 *            the routing context
	 * @return the object[]
	 */
	protected Object[] collectParameters(RoutingContext routingContext) {
		// Prepare parameters to call method of route
		Object[] parameters = new Object[handlerData.method().getParameterCount()];
		int i = 0;
		for (Parameter parameter : handlerData.method().getParameters()) {

			parameters[i] = resolveParameter(parameter, routingContext);
			i++;
		}
		return parameters;
	}

	@SneakyThrows
	protected Object instantiateHandler() {
		// Inject Request and Response if is Available
		Object newInstance = handlerData.clazz().newInstance();
		ContainerUtils.processInjection(newInstance);
		return newInstance;
	}

	/**
	 * Resolve parameter.
	 *
	 * @param parameter
	 *            the parameter
	 * @param routingContext
	 *            the routing context
	 * @return the object
	 */
	protected Object resolveParameter(Parameter parameter, RoutingContext routingContext) {

		if (parameter.getType().equals(RoutingContext.class)) {

			return routingContext;
		}
		if (parameter.getType().equals(HttpServerRequest.class)) {

			return routingContext.request();
		}

		if (parameter.getType().equals(HttpServerResponse.class)) {

			return routingContext.response();
		}
		if (StringUtils.isNotEmpty(routingContext.request().getParam(parameter.getName()))) {

			return routingContext.request().getParam(parameter.getName());
		}

		if (parameter.isAnnotationPresent(ArrayModel.class)) {

			ArrayModel am = parameter.getAnnotation(ArrayModel.class);
			Class<?> clazz = am.value();
			return ArrayModelParser.toList(routingContext.getBody().toString(), clazz);
		}

		if (parameter.isAnnotationPresent(MapModel.class)) {

			MapModel mm = parameter.getAnnotation(MapModel.class);
			Class<?> value = mm.value();
			return MapModelParser.toMap(routingContext.getBody().toString(), value);
		}

		if (parameter.getType().getPackage().getName().endsWith(".model") || parameter.getType().isAnnotationPresent(Model.class)
				|| parameter.isAnnotationPresent(Model.class)) {

			try {
				if (routingContext.getBody() == null) {

					return null;
				}

				return Json.decodeValue(routingContext.getBody().toString(), parameter.getType());
			} catch (SerializationException e) {

				log.debug("Invalid content of body for class [{}] on parameter [{}]", parameter.getClass(), parameter.getName());
				return null;
			}
		}
		if (parameter.isAnnotationPresent(org.jspare.vertx.web.annotation.handling.Parameter.class)) {

			String parameterName = parameter.getAnnotation(org.jspare.vertx.web.annotation.handling.Parameter.class).value();
			// Test types
			Type typeOfParameter = parameter.getType();
			if (typeOfParameter.equals(Integer.class)) {
				return Integer.parseInt(routingContext.request().getParam(parameterName));
			}
			if (typeOfParameter.equals(Double.class)) {
				return Double.parseDouble(routingContext.request().getParam(parameterName));
			}
			if (typeOfParameter.equals(Long.class)) {
				return Long.parseLong(routingContext.request().getParam(parameterName));
			}
			return routingContext.request().getParam(parameterName);
		}
		if (parameter.isAnnotationPresent(Header.class)) {

			String headerName = parameter.getAnnotation(Header.class).value();
			return routingContext.request().getHeader(headerName);
		}

		return null;
	}

	/**
	 * Send status.
	 *
	 * @param routingContext
	 *            the routing context
	 * @param status
	 *            the status
	 */
	protected void sendStatus(RoutingContext routingContext, HttpResponseStatus status) {
		routingContext.response().setStatusCode(status.code()).setStatusMessage(status.reasonPhrase()).end(status.reasonPhrase());
	}

	protected void setHandlingParameters(RoutingContext routingContext, Object newInstance) {
		// If Route is handling by abstract Handling inject some resources
		if (newInstance instanceof Handling) {

			((Handling) newInstance).setReq(routingContext.request());
			((Handling) newInstance).setRes(routingContext.response());
			((Handling) newInstance).setCtx(routingContext);
		}
	}
}
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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.annotation.Resource;
import org.jspare.vertx.builder.Collector;
import org.jspare.vertx.web.annotation.auth.Auth;
import org.jspare.vertx.web.annotation.auth.IgnoreAuth;
import org.jspare.vertx.web.annotation.content.Consumes;
import org.jspare.vertx.web.annotation.content.Produces;
import org.jspare.vertx.web.annotation.handler.BlockingHandler;
import org.jspare.vertx.web.annotation.handler.FailureHandler;
import org.jspare.vertx.web.annotation.handler.SockJsHandler;
import org.jspare.vertx.web.annotation.method.All;
import org.jspare.vertx.web.annotation.raml.Documentation;
import org.jspare.vertx.web.annotation.subrouter.IgnoreSubRouter;
import org.jspare.vertx.web.annotation.subrouter.SubRouter;
import org.jspare.vertx.web.handler.BodyEndHandler;

import io.vertx.core.Handler;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Resource
public class RouteCollector implements Collector<Collection<HandlerData>> {

	@Override
	public Collection<HandlerData> collect(Class<?> clazz, Object... args) {

		if (args.length < 1) {

			throw new IllegalArgumentException(
					String.format("Cannot collect route class [%s] without routeHandlerClass or authHandler", clazz.getName()));
		}

		final RouterBuilder builder = (RouterBuilder) args[0];

		// Retrieve required parameters to collect handlers

		final Class<? extends Handler<RoutingContext>> routeHandlerClass = builder.handlerClass();
		final AuthProvider authProvider = builder.authProvider();
		final Class<? extends AuthHandler> authHandlerClass = builder.authHandlerClass();
		final SockJSHandlerOptions sockJSHandlerOptions = builder.sockJSHandlerOptions();

		// Initialize collected handlers

		List<HandlerData> collectedHandlers = new ArrayList<>();
		List<Annotation> httpMethodsAnnotations = new ArrayList<>(getHttpMethodsPresents(clazz));

		for (Method method : clazz.getDeclaredMethods()) {

			if (!isHandler(method)) {

				continue;
			}

			final List<Annotation> handlerHttpMethodsAnnotations = new ArrayList<>();
			handlerHttpMethodsAnnotations.addAll(httpMethodsAnnotations);

			String consumes = method.isAnnotationPresent(Consumes.class) ? method.getAnnotation(Consumes.class).value() : StringUtils.EMPTY;
			String produces = method.isAnnotationPresent(Produces.class) ? method.getAnnotation(Produces.class).value() : StringUtils.EMPTY;
			List<BodyEndHandler> bodyEndHandler = collectBodyEndHandlers(method);

			HandlerDocumentation hDocumentation = null;

			// Documentation block
			if (method.isAnnotationPresent(Documentation.class)) {
				Documentation documentation = method.getAnnotation(Documentation.class);
				hDocumentation = new HandlerDocumentation();
				hDocumentation.description(documentation.description());
				hDocumentation.status(Arrays.asList(documentation.responseStatus()).stream()
						.map(s -> new HandlerDocumentation.ResponseStatus(s)).collect(Collectors.toList()));
				hDocumentation.queryParameters(Arrays.asList(documentation.queryParameters()).stream()
						.map(q -> new HandlerDocumentation.QueryParameter(q)).collect(Collectors.toList()));
				hDocumentation.requestSchema(documentation.requestClass());
				hDocumentation.responseSchema(documentation.responseClass());
			}

			// Authentication block
			AuthHandler authHandler = null;

			if (hasAuth(clazz, method) && !method.isAnnotationPresent(IgnoreAuth.class)) {

				Optional<AuthHandler> oAuthHandler = createAuthHandler(authProvider, authHandlerClass);

				if (oAuthHandler.isPresent()) {

					if (method.isAnnotationPresent(Auth.class)) {

						Auth auth = method.getAnnotation(Auth.class);
						oAuthHandler.get().addAuthorities(
								Arrays.asList(auth.value()).stream().filter(a -> StringUtils.isNotEmpty(a)).collect(Collectors.toSet()));
					} else if (clazz.isAnnotationPresent(Auth.class)) {

						Auth authClass = clazz.getAnnotation(Auth.class);
						oAuthHandler.get().addAuthorities(Arrays.asList(authClass.value()).stream().filter(a -> StringUtils.isNotEmpty(a))
								.collect(Collectors.toSet()));
					}
				}

				authHandler = oAuthHandler.orElse(null);
			}

			HandlerData defaultHandlerData = new HandlerData().clazz(clazz).method(method).consumes(consumes).produces(produces)
					.bodyEndHandler(bodyEndHandler).authHandler(authHandler).routeHandlerClass(routeHandlerClass)
					.documentation(hDocumentation);

			if (hasHttpMethodsPresents(method)) {

				handlerHttpMethodsAnnotations.clear();
				handlerHttpMethodsAnnotations.addAll(getHttpMethodsPresents(method));
			}

			// Iterate all handlers catched on class
			getHandlersPresents(method).forEach(handlerType -> {

				try {

					HandlerData handlerData = (HandlerData) defaultHandlerData.clone();

					// Set handler type on HandlerData
					if (isHandlerAnnotation(handlerType, SockJsHandler.class)) {

						// If SockJs type set other attributes and add to
						// collection
						handlerData.handlerType(HandlerType.SOCKETJS_HANDLER);
						handlerData.sockJSHandler(SockJSHandler.create(builder.vertx(),
								sockJSHandlerOptions != null ? sockJSHandlerOptions : new SockJSHandlerOptions()));
						handlerData.path(method.getAnnotation(SockJsHandler.class).value());
						collectedHandlers.add(handlerData);
						return;

					} else if (isHandlerAnnotation(handlerType, org.jspare.vertx.web.annotation.handler.Handler.class)) {

						handlerData.handlerType(HandlerType.HANDLER);
					} else if (isHandlerAnnotation(handlerType, FailureHandler.class)) {

						handlerData.handlerType(HandlerType.HANDLER);
					} else if (isHandlerAnnotation(handlerType, BlockingHandler.class)) {

						handlerData.handlerType(HandlerType.BLOCKING_HANDLER);
					}

					// Extract order from Handler, all Hanlder having order()
					int order = annotationMethod(handlerType, "order");

					handlerData.order(order);

					// Iterate methods of handler and registry on collection
					if (handlerHttpMethodsAnnotations.isEmpty()) {

						collectedHandlers.add(handlerData);
					} else {

						collectedHandlers.addAll(collectByMethods(handlerData, handlerHttpMethodsAnnotations));
					}
				} catch (Exception e) {

					log.warn("Ignoring handler class {} method {} - {}", clazz.getName(), method.getName(), e.getCause().toString());
				}
			});
		}
		return collectedHandlers;
	}

	protected boolean hasAuth(Class<?> clazz, Method method) {
		return (clazz.isAnnotationPresent(Auth.class) || method.isAnnotationPresent(Auth.class))
				&& !method.isAnnotationPresent(IgnoreAuth.class);
	}

	protected boolean isHandlerAnnotation(Annotation handlerType, Class<?> element) {
		return handlerType.annotationType().equals(element);
	}

	@SuppressWarnings("unchecked")
	private <T> T annotationMethod(Annotation annotation, String methodRef)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = annotation.annotationType().getMethod(methodRef);
		return (T) method.invoke(annotation);
	}

	private List<BodyEndHandler> collectBodyEndHandlers(Method method) {

		List<BodyEndHandler> handlers = new ArrayList<>();
		if (method.isAnnotationPresent(org.jspare.vertx.web.annotation.handler.BodyEndHandler.class)) {

			Arrays.asList(method.getAnnotation(org.jspare.vertx.web.annotation.handler.BodyEndHandler.class).value()).forEach(c -> {
				try {
					handlers.add(c.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {

					log.error("Cannot add bodyEndHandler [{}] to route instead of [{}]", c.getName(), method.getName());
				}
			});
		}
		return handlers;
	}

	private Collection<HandlerData> collectByMethods(HandlerData handlerSource, List<Annotation> httpMethodsAnnotations) {

		return httpMethodsAnnotations.stream().map(annotationHttpMethod -> {

			try {

				HandlerData handlerData = (HandlerData) handlerSource.clone();

				String prefix = StringUtils.EMPTY;
				String path = annotationMethod(annotationHttpMethod, "value");
				boolean isRegexPath = annotationMethod(annotationHttpMethod, "regex");

				if (handlerData.clazz().isAnnotationPresent(SubRouter.class)
						&& !handlerData.method().isAnnotationPresent(IgnoreSubRouter.class)) {

					prefix = handlerData.clazz().getAnnotation(SubRouter.class).value();
				}
				handlerData.path(String.format("%s%s", prefix, path));
				handlerData.pathRegex(isRegexPath);

				// For All, ignore set httpMethod
				if (All.class.equals(annotationHttpMethod.annotationType())) {

					return handlerData;
				}

				handlerData.httpMethod(annotationHttpMethod.annotationType().getSimpleName().toUpperCase());
				return handlerData;
			} catch (Exception e) {

				log.warn("Ignoring handler class {} method {} - {}", handlerSource.clazz().getName(), handlerSource.method().getName(), e);
			}
			return null;
		}).collect(Collectors.toList());
	}

	@SneakyThrows
	private Optional<AuthHandler> createAuthHandler(AuthProvider authProvider, Class<? extends AuthHandler> authHandlerClass) {

		if (authProvider == null || authHandlerClass == null) {
			return Optional.empty();
		}

		AuthHandler authHandler = (AuthHandler) authHandlerClass.getDeclaredMethod("create", AuthProvider.class).invoke(authProvider,
				authProvider);
		return Optional.of(authHandler);
	}

	@SuppressWarnings("unchecked")
	private List<Annotation> getHandlersPresents(Method method) {

		List<Class<?>> handlerClass = Arrays.asList(HandlerType.values()).stream().map(ht -> ht.getHandlerClass())
				.collect(Collectors.toList());
		return handlerClass.stream().filter(handlerType -> method.isAnnotationPresent((Class<? extends Annotation>) handlerType))
				.map(handlerType -> method.getAnnotation((Class<? extends Annotation>) handlerType)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	private List<Annotation> getHttpMethodsPresents(AnnotatedElement element) {

		List<Class<?>> filteredClazz = new ArrayList<>();
		filteredClazz
				.addAll(Arrays.asList(HttpMethodType.values()).stream().map(ht -> ht.getHttpMethodClass()).collect(Collectors.toList()));
		filteredClazz.removeIf(clazzHttpMethodType -> !element.isAnnotationPresent((Class<? extends Annotation>) clazzHttpMethodType));
		return filteredClazz.stream().map(httpMethodClazz -> element.getAnnotation((Class<? extends Annotation>) httpMethodClazz))
				.collect(Collectors.toList());
	}

	private boolean hasHttpMethodsPresents(Method method) {

		return getHttpMethodsPresents(method).stream().filter(annotation -> method.isAnnotationPresent(annotation.annotationType()))
				.count() >= 1;
	}

	private boolean isHandler(Method method) {

		return getHandlersPresents(method).stream()
				.filter(handlerAnnotation -> method.isAnnotationPresent(handlerAnnotation.annotationType())).count() >= 1;
	}
}
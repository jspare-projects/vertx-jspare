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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspare.vertx.builder.AbstractBuilder;
import org.jspare.vertx.builder.ClasspathScannerUtils;
import org.jspare.vertx.web.handler.DefaultHandler;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.MethodAnnotationMatchProcessor;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class RouterBuilder extends AbstractBuilder<Router> {

	private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

	public static RouterBuilder create(Vertx vertx) {

		return new RouterBuilder(vertx, Router.router(vertx));
	}

	public static RouterBuilder create(Vertx vertx, Router router) {

		return new RouterBuilder(vertx, router);
	}

	@Getter
	private final Vertx vertx;

	@Getter
	@Setter
	private Router router;

	@Getter
	@Setter
	private boolean scanClasspath;

	@Getter
	@Setter
	private Set<Handler<RoutingContext>> handlers;

	@Getter
	@Setter
	private Class<? extends Handler<RoutingContext>> handlerClass;

	/**
	 * The sock JS handler options. </br>
	 * Used for all SockJsHandlers mapped by this RouterBuilder
	 *
	 */
	@Getter
	@Setter
	private SockJSHandlerOptions sockJSHandlerOptions;

	@Getter
	@Setter
	private AuthProvider authProvider;

	@Getter
	@Setter
	private Class<? extends AuthHandler> authHandlerClass;

	@Getter
	@Setter
	private Set<Class<?>> routes;

	@Getter
	@Setter
	private Set<Class<?>> skipRoutes;

	@Getter
	@Setter
	private Set<String> routePackages;
	
	@Getter
	@Setter
	private boolean raml;

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
		authProvider = null;
		authHandlerClass = null;
		raml = false;
	}

	public RouterBuilder addHandler(Handler<RoutingContext> handler) {
		handlers.add(handler);
		return this;
	}

	public RouterBuilder addRoute(Class<?> routeClass) {

		routes.add(routeClass);
		return this;
	}

	public RouterBuilder addRoutePackage(String routePackage) {

		routePackages.add(routePackage);
		return this;
	}

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
		
		if(raml){
			
			generateRamlRoute(router);
		}
		
		return router;
	}

	public RouterBuilder route(RouteBuilder builder) {
		log.debug("Routing custom route [{}]", builder.getClass());
		builder.create(router.route());
		return this;
	}

	public RouterBuilder skipRoute(Class<?> routeClass) {

		skipRoutes.add(routeClass);
		return this;
	}

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
	
	private void generateRamlRoute(Router router) {
		
	}
}
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

	private static final String DEFAULT_ROUTE_PACKAGE_2_SCAN = ".route";

	public static RouterBuilder create(Vertx vertx) {

		return new RouterBuilder(vertx);
	}

	private final Vertx vertx;

	@Getter
	@Setter
	private Router router;

	@Getter
	@Setter
	private Set<Handler<RoutingContext>> handlers;

	@Getter
	@Setter
	private boolean scanClasspath;

	@Getter
	@Setter
	private Class<? extends Handler<RoutingContext>> handlerClass;

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
	private String defaultPackageToScanRoutes;

	@Getter
	@Setter
	private Set<String> routePackages;

	private RouterBuilder(Vertx vertx) {

		this.vertx = vertx;
		handlers = new HashSet<>();
		scanClasspath = true;
		defaultPackageToScanRoutes = DEFAULT_ROUTE_PACKAGE_2_SCAN;
		routePackages = new HashSet<>();
		routes = new HashSet<>();
		handlerClass = DefaultHandler.class;
		authProvider = null;
		authHandlerClass = null;
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

		// Load router instance
		log.debug("Creating Router with Vert.x Instance {}", vertx.toString());
		createRouter();

		handlers.forEach(h -> {
			log.debug("Routing handler [{}]", h.toString());
			router.route().handler(h);
		});

		collectRoutes();

		List<HandlerData> handlerDataList = new ArrayList<>();
		routes.forEach(c -> handlerDataList.addAll(my(RouteCollector.class).collect(c, handlerClass, authProvider, authHandlerClass)));

		handlerDataList.forEach(hd -> {
			log.debug("Routing handler {}", hd.toStringLine());
			HandlerWrapper.prepareHandler(router, hd);
		});
		return router;
	}

	protected void createRouter() {
		if (router == null) {

			router = Router.router(vertx);
		}
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
					.scan(NUMBER_CLASSPATH_SCANNER_THREADS);
		});
	}
}
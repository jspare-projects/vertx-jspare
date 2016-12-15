package org.jspare.vertx.web.builder;

import static org.jspare.core.container.Environment.my;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jspare.vertx.builder.AbstractBuilder;
import org.jspare.vertx.builder.ClasspathScannerUtils;
import org.jspare.vertx.web.handler.DefaultHandler;

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

	private static final String DEFAULT_ROUTE_PACKAGE_2_SCAN = ".route";

	private final Vertx vertx;

	@Getter
	@Setter
	private Router router;

	@Getter
	@Setter
	private Object source;

	@Getter
	@Setter
	private List<Handler<RoutingContext>> handlers;

	@Getter
	@Setter
	private boolean scanDefaultRoutes;

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
	private List<Class<?>> routes;

	@Getter
	@Setter
	private String defaultPackageToScanRoutes;

	@Getter
	@Setter
	private List<String> routePackages;

	@Override
	public Router build() {

		// Load router instance
		createRouter();

		handlers.forEach(router.route()::handler);

		collectRoutes();

		List<HandlerData> handlerDataList = new ArrayList<>();
		routes.forEach(c -> handlerDataList.addAll(my(RouteCollector.class).collect(c, handlerClass, authProvider, authHandlerClass)));

		handlerDataList.forEach(hd -> HandlerWrapper.prepareHandler(router, hd));
		
		return router;
	}
	
	public RouterBuilder addHandler(Handler<RoutingContext> handler){
		this.handlers.add(handler);
		return this;
	}

	private void collectRoutes() {

		if (scanDefaultRoutes && source != null) {
			String cPackage = source.getClass().getPackage().getName().concat(defaultPackageToScanRoutes)
					.concat(ClasspathScannerUtils.ALL_SCAN_QUOTE);

			routePackages.add(cPackage);
		}

		// Iterate routePackages scannig and adding classes to
		// routes
		routePackages.forEach(p -> {

			routes.addAll(ClasspathScannerUtils.listClassesByPackage(p).stream().map(t -> {
				try {
					return Class.forName(t);
				} catch (ClassNotFoundException e) {
					log.error("Class [%s] not founded on classpath.", e);
				}
				return null;
			}).collect(Collectors.toList()));
		});
	}

	protected void createRouter() {
		if (router == null) {

			router = Router.router(vertx);
		}
	}

	public static RouterBuilder create(Vertx vertx) {

		return new RouterBuilder(vertx);
	}

	private RouterBuilder(Vertx vertx) {

		this.vertx = vertx;
		this.handlers = new ArrayList<>();
		this.scanDefaultRoutes = true;
		this.defaultPackageToScanRoutes = DEFAULT_ROUTE_PACKAGE_2_SCAN;
		this.routePackages = new ArrayList<>();
		this.routes = new ArrayList<>();
		this.handlerClass = DefaultHandler.class;
		this.authProvider = null;
		this.authHandlerClass = null;
	}
}
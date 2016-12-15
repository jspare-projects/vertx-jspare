/**
 * Copyright 2016 Senior Sistemas.
 *
 * Software sob Medida
 * 
 */
package org.jspare.vertx.builder;

import static org.jspare.core.container.Environment.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class VertxBuilder extends AbstractBuilder<Vertx> {

	private static final String DEFAULT_EVENTBUS_PACKAGE_2_SCAN = ".eventbus";

	private static final String DEFAULT_VERTICLE_PACKAGE_2_SCAN = ".verticle";

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public static VertxBuilder create(VertxOptions vertxOptions) {

		return new VertxBuilder().options(vertxOptions);
	}

	@Getter
	@Setter
	private Object source;

	@Getter
	@Setter
	private Vertx vertx;

	@Getter
	@Setter
	private VertxOptions options;

	@Getter
	@Setter
	private boolean scanDefaultVerticles;

	@Getter
	@Setter
	private boolean scanDefaultConsumers;

	@Getter
	@Setter
	private String defaultPackageToScanVerticles;

	@Getter
	@Setter
	private String defaultPackageToScanConsumers;

	@Getter
	@Setter
	private List<Class<?>> verticlesClasses;

	@Getter
	@Setter
	private List<String> verticlesPackages;

	private Map<String, DeploymentOptions> sVerticle2deploy;

	private Map<Verticle, DeploymentOptions> iVerticle2deploy;

	@Getter
	@Setter
	private List<Class<?>> eventBusClasses;

	@Getter
	@Setter
	private List<String> eventBusPackages;

	private VertxBuilder() {

		scanDefaultVerticles = true;
		scanDefaultConsumers = true;
		defaultPackageToScanVerticles = DEFAULT_VERTICLE_PACKAGE_2_SCAN;
		defaultPackageToScanConsumers = DEFAULT_EVENTBUS_PACKAGE_2_SCAN;
		verticlesClasses = new ArrayList<>();
		verticlesPackages = new ArrayList<>();
		eventBusClasses = new ArrayList<>();
		eventBusPackages = new ArrayList<>();
		sVerticle2deploy = new HashMap<>();
		iVerticle2deploy = new HashMap<>();
	}

	@Override
	public Vertx build() {

		Future<Vertx> future = Future.future();

		// Load vertx instance
		Consumer<Vertx> runner = vertx -> {
			
			// Registry vertx
			this.vertx = vertx;

			// Collect and registry event bus
			collectAndRegistryEventBusControllers();

			// Collect, create and registry one verticle
			collectAndRegistryVerticles();

			future.complete(vertx);
		};

		if (vertx != null) {

			runner.accept(vertx);
		} else {

			createVertx(runner);
		}

		return future.result();
	}

	protected void createVertx(Consumer<Vertx> runner) {

		if (options == null) {

			options = new VertxOptions();
		}

		if (options.isClustered()) {

			Vertx.clusteredVertx(options, res -> {
				if (res.succeeded()) {
					Vertx vertx = res.result();
					runner.accept(vertx);
				} else {
					res.cause().printStackTrace();
				}
			});
		} else {

			runner.accept(Vertx.vertx(options));
		}
	}

	public VertxBuilder deployVerticle(String deploymentId, DeploymentOptions deploymentOptions) {

		sVerticle2deploy.put(deploymentId, deploymentOptions);
		return this;
	}

	public VertxBuilder deployVerticle(Verticle verticle, DeploymentOptions deploymentOptions) {

		iVerticle2deploy.put(verticle, deploymentOptions);
		return this;
	}

	private void collectAndRegistryVerticles() {

		// Check if default package are available to scan and add to
		// eventBusPackages
		if (scanDefaultVerticles && source != null) {

			String cPackage = source.getClass().getPackage().getName().concat(defaultPackageToScanVerticles)
					.concat(ClasspathScannerUtils.ALL_SCAN_QUOTE);

			verticlesPackages.add(cPackage);
		}

		// Iterate eventBusPackages scannig and adding classes to
		// eventBusClasses
		verticlesPackages.forEach(p -> {

			verticlesClasses.addAll(ClasspathScannerUtils.listClassesByPackage(p).stream().map(t -> {
				try {
					return Class.forName(t);
				} catch (ClassNotFoundException e) {
					log.error("Class [%s] not founded on classpath.", e);
				}
				return null;
			}).collect(Collectors.toList()));
		});

		List<VerticleData> verticles = new ArrayList<>();

		// Iterate verticlesClasses and add verticles to will process
		verticlesClasses.forEach(c -> {
			my(VerticleCollector.class).collect(c).ifPresent(verticles::add);
		});

		// Process and deploy verticles
		verticles.forEach(vd -> vertx.deployVerticle(vd.verticle(), vd.deploymentOptions()));

		sVerticle2deploy.forEach((k, v) -> vertx.deployVerticle(k, v));

		iVerticle2deploy.forEach((k, v) -> vertx.deployVerticle(k, v));
	}

	/**
	 * Collect and registry event bus controllers on {@link Vertx} instance.
	 *
	 * @param vertx
	 *            the vertx
	 */
	private void collectAndRegistryEventBusControllers() {

		// Check if default package are available to scan and add to
		// eventBusPackages
		if (scanDefaultConsumers && source != null) {

			String cPackage = source.getClass().getPackage().getName().concat(defaultPackageToScanConsumers)
					.concat(ClasspathScannerUtils.ALL_SCAN_QUOTE);

			eventBusPackages.add(cPackage);
		}

		// Iterate eventBusPackages scannig and adding classes to
		// eventBusClasses
		eventBusPackages.forEach(p -> {

			eventBusClasses.addAll(ClasspathScannerUtils.listClassesByPackage(p).stream().map(t -> {
				try {
					return Class.forName(t);
				} catch (ClassNotFoundException e) {
					log.error("Class [%s] not founded on classpath.", e);
				}
				return null;
			}).collect(Collectors.toList()));
		});

		List<MessageData> consumers = new ArrayList<>();

		// Iterate eventBusClasses and add consumers to will process
		eventBusClasses.forEach(c -> consumers.addAll(my(EventBusCollector.class).collect(c)));

		// Process consumers
		EventBus eventBus = vertx.eventBus();
		consumers.forEach(md -> eventBus.consumer(md.name(), md.wrap()));
	}
}
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
package org.jspare.vertx.builder;

import static org.jspare.core.container.Environment.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.jspare.core.container.Context;
import org.jspare.vertx.annotation.EventBusController;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.injector.VertxInjectStrategy;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.MethodAnnotationMatchProcessor;
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

@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class VertxBuilder extends AbstractBuilder<Future<Vertx>> {

	private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public static VertxBuilder create(VertxOptions vertxOptions) {

		return new VertxBuilder().options(vertxOptions);
	}

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private Vertx vertx;

	@Getter
	@Setter
	private VertxOptions options;

	@Getter
	@Setter
	private boolean scanClasspath4verticles;

	@Getter
	@Setter
	private boolean scanClasspath4eventbus;

	@Getter
	@Setter
	private Set<Class<?>> verticlesClasses;

	@Getter
	@Setter
	private Set<String> verticlesPackages;

	private Map<String, DeploymentOptions> sVerticle2deploy;

	private Map<Verticle, DeploymentOptions> iVerticle2deploy;

	@Getter
	@Setter
	private List<Class<?>> eventBusClasses;

	@Getter
	@Setter
	private List<String> eventBusPackages;

	private VertxBuilder() {

		name = VertxInject.DEFAULT;
		scanClasspath4verticles = false;
		scanClasspath4eventbus = false;
		verticlesClasses = new HashSet<>();
		verticlesPackages = new HashSet<>();
		eventBusClasses = new ArrayList<>();
		eventBusPackages = new ArrayList<>();
		sVerticle2deploy = new HashMap<>();
		iVerticle2deploy = new HashMap<>();
	}

	public VertxBuilder addEventBusController(Class<? extends EventBusController> controllerClass) {
		eventBusClasses.add(controllerClass);
		return this;
	}

	@Override
	public Future<Vertx> build() {

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

		my(Context.class).put(VertxInjectStrategy.formatInstanceKey(name), vertx);

		return future;
	}

	public VertxBuilder deployVerticle(Class<?> verticle) {

		verticlesClasses.add(verticle);
		return this;
	}

	public VertxBuilder deployVerticle(String deploymentId, DeploymentOptions deploymentOptions) {

		sVerticle2deploy.put(deploymentId, deploymentOptions);
		return this;
	}

	public VertxBuilder deployVerticle(Verticle verticle) {

		iVerticle2deploy.put(verticle, new DeploymentOptions());
		return this;
	}

	public VertxBuilder deployVerticle(Verticle verticle, DeploymentOptions deploymentOptions) {

		iVerticle2deploy.put(verticle, deploymentOptions);
		return this;
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

	/**
	 * Collect and registry event bus controllers on {@link Vertx} instance.
	 *
	 * @param vertx
	 *            the vertx
	 */
	private void collectAndRegistryEventBusControllers() {

		if (scanClasspath4eventbus) {
			eventBusPackages.clear();
			eventBusPackages.add(".*");
		}

		// Iterate eventBusPackages scannig and adding classes to
		// eventBusClasses

		MethodAnnotationMatchProcessor processor = (c, m) -> eventBusClasses.add(c);

		eventBusPackages.forEach(scanSpec -> {

			ClasspathScannerUtils.scanner(scanSpec).matchClassesWithMethodAnnotation(org.jspare.vertx.annotation.Consumer.class, processor)
					.scan(NUMBER_CLASSPATH_SCANNER_THREADS);
		});

		List<MessageData> consumers = new ArrayList<>();

		// Iterate eventBusClasses and add consumers to will process
		eventBusClasses.forEach(c -> consumers.addAll(my(EventBusCollector.class).collect(c)));

		// Process consumers
		EventBus eventBus = vertx.eventBus();
		consumers.forEach(md -> eventBus.consumer(md.name(), md.wrap()));
	}

	private void collectAndRegistryVerticles() {

		// Check if default package are available to scan and add to
		// eventBusPackages
		if (scanClasspath4verticles) {
			verticlesPackages.clear();
			verticlesPackages.add(".*");
		}

		ClassAnnotationMatchProcessor processor = (c) -> verticlesClasses.add(c);

		// Iterate eventBusPackages scannig and adding classes to
		// eventBusClasses
		verticlesPackages.forEach(scanSpec -> {

			ClasspathScannerUtils.scanner(scanSpec).matchClassesWithAnnotation(org.jspare.vertx.annotation.Verticle.class, processor)
					.scan(NUMBER_CLASSPATH_SCANNER_THREADS);
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
}
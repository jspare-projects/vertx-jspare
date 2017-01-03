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
import java.util.List;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.MethodAnnotationMatchProcessor;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class EventBusBuilder extends AbstractBuilder<Void> {

	private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

	public static EventBusBuilder create(Vertx vertx) {

		return new EventBusBuilder(vertx);
	}

	@Getter
	private final Vertx vertx;

	@Getter
	@Setter
	private boolean scanClasspath;

	@Getter
	@Setter
	private List<Class<?>> classes;

	@Getter
	@Setter
	private List<String> scanSpecs;

	private EventBusBuilder(Vertx vertx) {

		this.vertx = vertx;
		scanClasspath = false;
		classes = new ArrayList<>();
		scanSpecs = new ArrayList<>();
	}

	@Override
	public Void build() {

		if (scanClasspath) {
			scanSpecs.clear();
			scanSpecs.add(".*");
		}

		// Iterate eventBusPackages scannig and adding classes to
		// eventBusClasses

		MethodAnnotationMatchProcessor processor = (c, m) -> classes.add(c);
		scanSpecs.forEach(scanSpec -> {

			ClasspathScannerUtils.scanner(scanSpec).matchClassesWithMethodAnnotation(org.jspare.vertx.annotation.Consumer.class, processor)
					.scan(NUMBER_CLASSPATH_SCANNER_THREADS);
		});

		List<EventBusData> consumers = new ArrayList<>();

		// Iterate eventBusClasses and add consumers to will process
		classes.forEach(c -> consumers.addAll(my(EventBusCollector.class).collect(c)));

		// Process consumers
		EventBus eventBus = vertx.eventBus();
		consumers.forEach(md -> eventBus.consumer(md.name(), md.wrap()));

		return null;
	}

}
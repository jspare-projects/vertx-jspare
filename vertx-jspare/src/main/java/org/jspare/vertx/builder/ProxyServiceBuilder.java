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

import java.util.HashSet;
import java.util.Set;

import org.jspare.vertx.annotation.RegisterProxyService;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Accessors(fluent = true)

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(callSuper = false)
public class ProxyServiceBuilder extends AbstractBuilder<Void> {

	/** The Constant NUMBER_CLASSPATH_SCANNER_THREADS. */
	private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

	/**
	 * Creates the.
	 *
	 * @param vertx the vertx
	 * @return the proxy service builder
	 */
	public static ProxyServiceBuilder create(Vertx vertx) {

		return new ProxyServiceBuilder(vertx);
	}

	/** The vertx. */
	private final Vertx vertx;

	/**
	 * Scan classpath.
	 *
	 * @return true, if successful
	 */
	@Getter
	
	/**
	 * Scan classpath.
	 *
	 * @param scanClasspath the scan classpath
	 * @return the proxy service builder
	 */
	@Setter
	private boolean scanClasspath;

	/**
	 * Classes.
	 *
	 * @return the sets the
	 */
	@Getter
	
	/**
	 * Classes.
	 *
	 * @param classes the classes
	 * @return the proxy service builder
	 */
	@Setter
	private Set<Class<?>> classes;

	/**
	 * Scan specs.
	 *
	 * @return the sets the
	 */
	@Getter
	
	/**
	 * Scan specs.
	 *
	 * @param scanSpecs the scan specs
	 * @return the proxy service builder
	 */
	@Setter
	private Set<String> scanSpecs;

	/**
	 * Instantiates a new proxy service builder.
	 *
	 * @param vertx the vertx
	 */
	private ProxyServiceBuilder(Vertx vertx) {

		this.vertx = vertx;
		scanClasspath = false;
		classes = new HashSet<>();
		scanSpecs = new HashSet<>();
	}

	/**
	 * Adds the proxy service.
	 *
	 * @param clazz the clazz
	 * @return the proxy service builder
	 */
	public ProxyServiceBuilder addProxyService(Class<?> clazz) {
		classes.add(clazz);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.jspare.vertx.builder.AbstractBuilder#build()
	 */
	@Override
	public Void build() {

		// Collect, create and registry proxy services
		// Check if default package are available to scan and add to
		if (scanClasspath) {
			scanSpecs.clear();
			scanSpecs.add(".*");
		}

		ClassAnnotationMatchProcessor processor = (c) -> classes.add(c);

		scanSpecs.forEach(scanSpec -> {

			ClasspathScannerUtils.scanner(scanSpec).matchClassesWithAnnotation(RegisterProxyService.class, processor)
					.scan(NUMBER_CLASSPATH_SCANNER_THREADS);
		});

		// Iterate proxyServiceClasses and register service
		classes.forEach(this::registerProxyService);

		return null;
	}

	/**
	 * Register proxy service.
	 *
	 * @param <T> the generic type
	 * @param clazz the clazz
	 */
	private <T> void registerProxyService(Class<T> clazz) {

		if (!clazz.isAnnotationPresent(RegisterProxyService.class)) {

			log.warn(
					"Cannot register service {} with ProxyHelper. One possible cause, the class is not annotated bt ProxyHandler annotation.");
			return;
		}

		RegisterProxyService proxyHandler = clazz.getAnnotation(RegisterProxyService.class);
		String address = ProxyServiceUtils.getAddress(proxyHandler, clazz);
		T service = my(clazz);
		ProxyHelper.registerService(clazz, vertx, service, address);
	}
}
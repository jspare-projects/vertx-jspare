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

import org.jspare.vertx.annotation.ProxyHandler;

import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class ProxyServiceBuilder extends AbstractBuilder<Void> {

	private static final int NUMBER_CLASSPATH_SCANNER_THREADS = 3;

	public static ProxyServiceBuilder create(Vertx vertx) {

		return new ProxyServiceBuilder(vertx);
	}

	private final Vertx vertx;

	@Getter
	@Setter
	private boolean scanClasspath;

	@Getter
	@Setter
	private Set<Class<?>> proxyServiceClasses;

	@Getter
	@Setter
	private Set<String> proxyServicePackages;

	private ProxyServiceBuilder(Vertx vertx) {

		this.vertx = vertx;
		scanClasspath = false;
		proxyServiceClasses = new HashSet<>();
		proxyServicePackages = new HashSet<>();
	}

	public ProxyServiceBuilder addProxyService(Class<?> service) {
		proxyServiceClasses.add(service);
		return this;
	}

	@Override
	public Void build() {

		// Collect, create and registry proxy services
		collectAndRegistryProxyServices();

		return null;
	}

	private void collectAndRegistryProxyServices() {

		// Check if default package are available to scan and add to
		// eventBusPackages
		if (scanClasspath) {
			proxyServicePackages.clear();
			proxyServicePackages.add(".*");
		}

		ClassAnnotationMatchProcessor processor = (c) -> proxyServiceClasses.add(c);

		proxyServicePackages.forEach(scanSpec -> {

			ClasspathScannerUtils.scanner(scanSpec).matchClassesWithAnnotation(ProxyHandler.class, processor)
					.scan(NUMBER_CLASSPATH_SCANNER_THREADS);
		});

		// Iterate proxyServiceClasses and register service
		proxyServiceClasses.forEach(this::registerProxyService);
	}

	private <T> void registerProxyService(Class<T> clazz) {
		
		if(!clazz.isAnnotationPresent(ProxyHandler.class)){
			
			log.warn("Cannot register service {} with ProxyHelper. One possible cause, the class is not annotated bt ProxyHandler annotation.");
			return;
		}

		ProxyHandler proxyHandler = clazz.getAnnotation(ProxyHandler.class);
		String address = ProxyHandlerUtils.getAddress(proxyHandler, clazz);
		T service = my(clazz);
		ProxyHelper.registerService(clazz, vertx, service, address);
	}
}
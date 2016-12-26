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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jspare.core.annotation.Resource;
import org.jspare.core.container.ContainerUtils;
import org.jspare.vertx.annotation.Consumer;
import org.jspare.vertx.annotation.EventBusController;

import lombok.SneakyThrows;

@Resource
public class EventBusCollector implements Collector<Collection<MessageData>> {

	private Map<Class<?>, Object> controllers;

	public EventBusCollector() {

		controllers = new HashMap<>();
	}

	@Override
	public Collection<MessageData> collect(Class<?> clazz, Object... args) {

		// Ignore all classes without EventBusController
		if (!clazz.isAnnotationPresent(EventBusController.class)) {

			return Arrays.asList();
		}

		List<MessageData> handlers = new ArrayList<>();
		List<Method> methodsCollected = new ArrayList<>();

		Arrays.asList(clazz.getDeclaredMethods()).forEach(m -> {

			if (m.isAnnotationPresent(Consumer.class)) {

				methodsCollected.add(m);
			}
		});

		if (!methodsCollected.isEmpty()) {

			Object instance = getInstance(clazz);
			handlers.addAll(methodsCollected.stream().map(method -> {

				Consumer consumer = method.getAnnotation(Consumer.class);
				return new MessageData(instance, method, consumer.value());
			}).collect(Collectors.toList()));
		}
		return handlers;
	}

	@SneakyThrows
	private Object getInstance(Class<?> controllerClass) {

		EventBusController anEventBusController = controllerClass.getAnnotation(EventBusController.class);

		Object instance = controllers.get(controllerClass);
		if (instance != null) {

			return instance;
		}

		instance = controllerClass.newInstance();
		ContainerUtils.processInjection(instance);

		if (anEventBusController.retention()) {

			controllers.put(controllerClass, instance);
		}
		return instance;
	}
}
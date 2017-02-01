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

/**
 * The Class EventBusCollector.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Resource
public class EventBusCollector implements Collector<Collection<EventBusData>> {

  /** The controllers. */
  private Map<Class<?>, Object> controllers;

  /**
   * Instantiates a new event bus collector.
   */
  public EventBusCollector() {

    controllers = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jspare.vertx.builder.Collector#collect(java.lang.Class,
   * java.lang.Object[])
   */
  @Override
  public Collection<EventBusData> collect(Class<?> clazz, Object... args) {

    List<EventBusData> handlers = new ArrayList<>();
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
        return new EventBusData(instance, method, consumer.value());
      }).collect(Collectors.toList()));
    }
    return handlers;
  }

  /**
   * Gets the single instance of EventBusCollector.
   *
   * @param clazz
   *          the clazz
   * @return single instance of EventBusCollector
   */
  private Object getInstance(Class<?> clazz) {

    if (!clazz.isAnnotationPresent(EventBusController.class)) {

      return instantiate(clazz);
    }

    EventBusController anEventBusController = clazz.getAnnotation(EventBusController.class);
    Object instance = controllers.get(clazz);
    if (instance != null) {

      return instance;
    }

    instance = instantiate(clazz);

    if (anEventBusController.retention()) {

      controllers.put(clazz, instance);
    }
    return instance;
  }

  /**
   * Instantiate.
   *
   * @param clazz
   *          the clazz
   * @return the object
   */
  @SneakyThrows
  private Object instantiate(Class<?> clazz) {

    Object instance = clazz.newInstance();
    ContainerUtils.processInjection(instance);
    return instance;
  }
}
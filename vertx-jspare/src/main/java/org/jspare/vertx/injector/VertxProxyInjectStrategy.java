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
package org.jspare.vertx.injector;

import java.lang.reflect.Field;

import org.jspare.core.container.InjectorStrategy;
import org.jspare.core.container.MySupport;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.annotation.VertxProxyInject;
import org.jspare.vertx.builder.ProxyServiceUtils;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxProxyInjectStrategy extends MySupport implements InjectorStrategy {

	@VertxInject
	private Vertx vertx;

	@Override
	public void inject(Object obj, Field field) {

		try {

			VertxProxyInject proxyHandler = field.getAnnotation(VertxProxyInject.class);
			String address = ProxyServiceUtils.getAddress(proxyHandler, field.getType());
			Object value = ProxyHelper.createProxy(field.getType(), vertx, address);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException | IllegalStateException e) {

			log.error("Cannot create proxy to {}", field.getName(), e);
		}
	}
}
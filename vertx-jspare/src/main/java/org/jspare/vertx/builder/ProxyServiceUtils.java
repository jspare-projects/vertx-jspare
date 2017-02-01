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

import org.apache.commons.lang.StringUtils;
import org.jspare.vertx.annotation.RegisterProxyService;
import org.jspare.vertx.annotation.VertxProxyInject;

import lombok.experimental.UtilityClass;

/**
 * Instantiates a new proxy service utils.
 */
@UtilityClass
public class ProxyServiceUtils {

	/**
	 * Gets the address.
	 *
	 * @param vertxProxyInject the vertx proxy inject
	 * @param serviceType the service type
	 * @return the address
	 */
	public String getAddress(VertxProxyInject vertxProxyInject, Class<?> serviceType) {
		String address = vertxProxyInject.value();
		if (StringUtils.isEmpty(address)) {

			address = defaultAddress(serviceType);
		}
		return address;
	}

	/**
	 * Gets the address.
	 *
	 * @param registerProxyService the register proxy service
	 * @param serviceType the service type
	 * @return the address
	 */
	public String getAddress(RegisterProxyService registerProxyService, Class<?> serviceType) {
		String address = registerProxyService.value();
		if (StringUtils.isEmpty(address)) {

			address = defaultAddress(serviceType);
		}
		return address;
	}

	/**
	 * Default address.
	 *
	 * @param clazz the clazz
	 * @return the string
	 */
	public String defaultAddress(Class<?> clazz) {
		return "service." + clazz.getSimpleName().toLowerCase();
	}
}

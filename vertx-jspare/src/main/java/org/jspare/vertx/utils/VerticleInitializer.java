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
package org.jspare.vertx.utils;

import org.jspare.core.container.ContainerUtils;

import io.vertx.core.Verticle;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VerticleInitializer {

	@SneakyThrows
	public Verticle initialize(Class<? extends Verticle> clazz) {
		Verticle verticle = clazz.newInstance();
		ContainerUtils.processInjection(verticle);
		return verticle;
	}

	@SneakyThrows
	public Verticle initialize(Verticle verticle) {
		ContainerUtils.processInjection(verticle);
		return verticle;
	}
	
	@SneakyThrows
	@SuppressWarnings("unchecked")
	public Verticle initialize(String name) {
		Class<? extends Verticle> clazz = (Class<? extends Verticle>) Class.forName(name);
		return initialize(clazz);
	}
}
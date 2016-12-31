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
import java.util.Arrays;
import java.util.Optional;

import org.jspare.core.annotation.Resource;
import org.jspare.vertx.annotation.DeploymentOptionsBuilder;
import org.jspare.vertx.utils.VerticleInitializer;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import lombok.SneakyThrows;

@Resource
public class VerticleCollector implements Collector<Optional<VerticleData>> {

	@Override
	@SneakyThrows
	public Optional<VerticleData> collect(Class<?> clazz, Object... args) {

		// Ignore all classes without EventBusController
		if (!clazz.isAnnotationPresent(org.jspare.vertx.annotation.Verticle.class)) {

			return Optional.empty();
		}

		@SuppressWarnings("unchecked")
		Verticle verticle = VerticleInitializer.initialize((Class<? extends Verticle>) clazz);
		DeploymentOptions deploymentOptions = getDeploymentOptions(clazz, verticle);

		VerticleData data = new VerticleData(verticle, deploymentOptions);

		return Optional.of(data);
	}

	@SneakyThrows
	private DeploymentOptions getDeploymentOptions(Class<?> clazz, Object verticle) {

		// Validate if clazz contains one valid DeploymentOptions
		Optional<Method> oMethod = Arrays.asList(clazz.getDeclaredMethods()).stream()
				.filter(m -> m.isAnnotationPresent(DeploymentOptionsBuilder.class) && m.getReturnType().equals(DeploymentOptions.class))
				.findFirst();

		DeploymentOptions deploymentOptions = null;
		if (oMethod.isPresent()) {

			// Call for deployment options

			Method method = oMethod.get();
			method.setAccessible(true);
			deploymentOptions = (DeploymentOptions) method.invoke(verticle);
		} else {

			deploymentOptions = new DeploymentOptions();
		}

		return deploymentOptions;
	}
}
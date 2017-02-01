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

import static org.jspare.core.container.Environment.my;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.annotation.Inject;
import org.jspare.core.container.InjectorStrategy;
import org.jspare.core.container.MySupport;
import org.jspare.core.exception.EnvironmentException;
import org.jspare.core.exception.Errors;
import org.jspare.vertx.annotation.SharedWorkerExecutor;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.bootstrap.VertxHolder;
import org.jspare.vertx.utils.JsonObjectLoader;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;

/**
 * The Class VertxInjectStrategy.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class VertxInjectStrategy extends MySupport implements InjectorStrategy {

	/** The Constant DEFAULT_WORKER_EXECUTOR_NAME. */
	private static final String DEFAULT_WORKER_EXECUTOR_NAME = "defaultWorkerExecutor";

	/** The Constant VERTX_PATTERN. */
	private static final String VERTX_PATTERN = "vertx:%s";

	/**
	 * Format instance key.
	 *
	 * @param instanceRef the instance ref
	 * @return the string
	 */
	public static String formatInstanceKey(String instanceRef) {

		return String.format(VERTX_PATTERN, instanceRef);
	}

	/** The vertx holder. */
	@Inject
	private VertxHolder vertxHolder;

	/* (non-Javadoc)
	 * @see org.jspare.core.container.InjectorStrategy#inject(java.lang.Object, java.lang.reflect.Field)
	 */
	@Override
	public void inject(Object result, Field field) {

		Vertx vertx = null;

		try {

			VertxInject inject = field.getAnnotation(VertxInject.class);
			Optional<Vertx> oVertx = Optional.ofNullable(vertxHolder.vertx());
			if (oVertx.isPresent()) {

				vertx = oVertx.get();
			} else {

				VertxOptions options = null;
				if (StringUtils.isNotEmpty(inject.vertxOptions())) {

					options = my(JsonObjectLoader.class).loadOptions(inject.vertxOptions(), VertxOptions.class);
				} else {

					options = new VertxOptions();
				}

				vertx = Vertx.vertx(options);
			}

			setField(result, field, vertx);

		} catch (IllegalArgumentException | IllegalAccessException e) {

			throw new EnvironmentException(Errors.INVALID_INJECTION.throwable(e));
		}
	}

	/**
	 * Sets the field.
	 *
	 * @param result the result
	 * @param field the field
	 * @param vertx the vertx
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	protected void setField(Object result, Field field, Vertx vertx)
			throws IllegalArgumentException, IllegalAccessException {

		field.setAccessible(true);

		if (Vertx.class.equals(field.getType())) {

			field.set(result, vertx);
		} else if (io.vertx.core.Context.class.equals(field.getType())) {

			field.set(result, vertx.getOrCreateContext());
		} else if (JsonObject.class.equals(field.getType())) {

			field.set(result, vertx.getOrCreateContext().config());
		} else if (EventBus.class.equals(field.getType())) {

			field.set(result, vertx.eventBus());
		} else if (WorkerExecutor.class.equals(field.getType())) {

			if (field.isAnnotationPresent(SharedWorkerExecutor.class)) {

				SharedWorkerExecutor annWe = field.getAnnotation(SharedWorkerExecutor.class);
				field.set(result,
						vertx.createSharedWorkerExecutor(annWe.name(), annWe.poolSize(), annWe.maxExecuteTime()));
			} else {

				field.set(result, vertx.createSharedWorkerExecutor(DEFAULT_WORKER_EXECUTOR_NAME));
			}
		} else if (FileSystem.class.equals(field.getType())) {

			field.set(result, vertx.fileSystem());
		} else if (SharedData.class.equals(field.getType())) {

			field.set(result, vertx.sharedData());
		}
	}
}
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

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
@Accessors(fluent = true)

/**
 * Instantiates a new event bus data.
 *
 * @param controller the controller
 * @param method the method
 * @param name the name
 */
@AllArgsConstructor
public class EventBusData {

	/** The controller. */
	private Object controller;

	/** The method. */
	private Method method;

	/** The name. */
	private String name;

	/**
	 * Wrap.
	 *
	 * @param <T> the generic type
	 * @return the handler
	 */
	public <T> Handler<Message<T>> wrap() {

		return new Handler<Message<T>>() {

			@Override
			@SneakyThrows
			public void handle(Message<T> event) {

				if (method.getParameterCount() == 1) {

					method().invoke(controller, event);
				} else {

					method.invoke(controller);
				}
			}
		};
	}

}
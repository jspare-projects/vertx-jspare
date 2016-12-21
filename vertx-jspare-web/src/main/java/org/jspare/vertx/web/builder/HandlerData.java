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
package org.jspare.vertx.web.builder;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jspare.vertx.web.handler.BodyEndHandler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HandlerData implements Cloneable {

	private Class<?> clazz;
	private Method method;
	private HandlerType handlerType;
	private Class<? extends Handler<RoutingContext>> routeHandlerClass;
	private SockJSHandler sockJSHandler;
	private String path = StringUtils.EMPTY;
	private int order;
	private boolean pathRegex;
	private String httpMethod;
	private String consumes;
	private String produces;
	private List<BodyEndHandler> bodyEndHandler;
	private AuthHandler authHandler;
	private HandlerDocumentation documentation;

	public String toStringLine() {
		StringBuilder line = new StringBuilder();
		line.append(String.format("[%s.%s]", clazz().getSimpleName(), method().getName()));
		line.append(String.format("[%s]", handlerType));
		if (StringUtils.isNotEmpty(httpMethod())) {
			line.append(String.format("[%s] ", httpMethod()));
		}
		if (StringUtils.isNotEmpty(path())) {
			line.append(String.format("[%s] ", path()));
		} else {
			line.append(String.format("[%s] ", clazz().getSimpleName())).append(String.format("[%s] ", method().getName()));
		}
		return line.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
}
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

import java.util.List;

import org.jspare.vertx.web.builder.route.MultiHandlers;
import org.jspare.vertx.web.builder.route.MultiHttpMethods;
import org.jspare.vertx.web.builder.route.MultiRoutes;
import org.junit.Assert;
import org.junit.Test;

public class HandlerCollectorTest extends AbstractCollectorTest {

	@Test
	public void collectRouteSetTest() {

		List<HandlerData> handlers = collect(MultiRoutes.class);

		Assert.assertEquals(2, handlers.size());

		HandlerData handler1 = handlers.get(0);
		HandlerData handler2 = handlers.get(1);

		Assert.assertEquals(MultiRoutes.class, handler1.clazz());
		Assert.assertEquals("handler1", handler1.method().getName());
		Assert.assertEquals("/multiRoutes/1", handler1.patch());
		Assert.assertEquals("GET", handler1.httpMethod());
		Assert.assertEquals(HandlerType.HANDLER, handler1.handlerType());
		Assert.assertEquals(0, handler1.order());

		Assert.assertEquals("/multiRoutes/2", handler2.patch());
		Assert.assertEquals(HandlerType.BLOCKING_HANDLER, handler2.handlerType());
		Assert.assertEquals("handler2", handler2.method().getName());
		Assert.assertEquals(1, handler2.order());
		Assert.assertTrue(handler2.pathRegex());
		Assert.assertEquals("*/*", handler2.consumes());
		Assert.assertEquals("text/plain", handler2.produces());
	}

	@Test
	public void multiHandlersTest() {

		List<HandlerData> handlers = collect(MultiHandlers.class);
		Assert.assertEquals(3, handlers.size());
	}

	@Test
	public void multiHttpMethodsTest() {

		List<HandlerData> handlers = collect(MultiHttpMethods.class);
		Assert.assertEquals(HttpMethodType.values().length * 2, handlers.size());
	}
}

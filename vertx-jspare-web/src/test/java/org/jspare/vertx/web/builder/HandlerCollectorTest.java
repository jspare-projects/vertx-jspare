/*
 * Copyright 2016 Jspare.org.
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

import io.vertx.ext.unit.TestContext;
import org.jspare.vertx.unit.ext.junit.VertxJspareUnitRunner;
import org.jspare.vertx.web.builder.route.MultiHandlers;
import org.jspare.vertx.web.builder.route.MultiHttpMethods;
import org.jspare.vertx.web.builder.route.MultiRoutes;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * The Class HandlerCollectorTest.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@RunWith(VertxJspareUnitRunner.class)
public class HandlerCollectorTest extends AbstractCollectorTest {

  /**
   * Collect route set test.
   */
  @Test
  public void collectRouteSetTest(TestContext ctx) {

    List<HandlerData> handlers = collect(MultiRoutes.class);

    ctx.assertEquals(2, handlers.size());

    HandlerData handler1 = handlers.get(0);
    HandlerData handler2 = handlers.get(1);

    ctx.assertEquals(MultiRoutes.class, handler1.clazz());
    ctx.assertEquals("handler1", handler1.method().getName());
    ctx.assertEquals("/multiRoutes/1", handler1.path());
    ctx.assertEquals("GET", handler1.httpMethod());
    ctx.assertEquals(HandlerType.HANDLER, handler1.handlerType());
    ctx.assertEquals(Integer.MIN_VALUE, handler1.order());

    ctx.assertEquals("/multiRoutes/2", handler2.path());
    ctx.assertEquals(HandlerType.BLOCKING_HANDLER, handler2.handlerType());
    ctx.assertEquals("handler2", handler2.method().getName());
    ctx.assertEquals(1, handler2.order());
    ctx.assertTrue(handler2.pathRegex());
    ctx.assertEquals("*/*", handler2.consumes());
    ctx.assertEquals("text/plain", handler2.produces());
  }

  /**
   * Multi handlers test.
   */
  @Test
  public void multiHandlersTest(TestContext ctx) {

    List<HandlerData> handlers = collect(MultiHandlers.class);
    ctx.assertEquals(3, handlers.size());
  }

  /**
   * Multi http methods test.
   */
  @Test
  public void multiHttpMethodsTest(TestContext ctx) {

    List<HandlerData> handlers = collect(MultiHttpMethods.class);
    ctx.assertEquals(HttpMethodType.values().length * 2, handlers.size());
  }
}

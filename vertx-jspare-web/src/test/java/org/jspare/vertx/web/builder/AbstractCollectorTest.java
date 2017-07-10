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

import io.vertx.core.Vertx;
import org.jspare.core.Environment;
import org.jspare.vertx.utils.EnvironmentUtils;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class AbstractCollectorTest.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class AbstractCollectorTest {

  /** The builder. */
  protected RouterBuilder builder = RouterBuilder.create(Vertx.vertx());

  /**
   * Load.
   */
  @Before
  public void load() {
    Environment.create();
    Vertx vertx = Vertx.vertx();
    EnvironmentUtils.bindInterfaces(vertx);
    Environment.inject(this);
  }

  /**
   * Collect.
   *
   * @param clazz
   *          the clazz
   * @return the list
   */
  protected List<HandlerData> collect(Class<?> clazz) {

    List<HandlerData> handlers = new ArrayList<>(
      Environment.my(RouteCollector.class).collect(clazz, RouterBuilder.create(Vertx.vertx())));
    Collections.sort(handlers, (o1, o2) -> o1.path().compareTo(o2.path()));
    return handlers;
  }
}

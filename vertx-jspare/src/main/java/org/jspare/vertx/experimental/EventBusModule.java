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
package org.jspare.vertx.experimental;

import org.jspare.vertx.builder.EventBusBuilder;

import io.vertx.core.Verticle;
import io.vertx.core.eventbus.EventBus;

/**
 * The Class EventBusModule.
 * 
 * <p>
 * Used for load {@link EventBus } consumers mapped on application.
 * </p>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Module(EventBusModule.NAME)
public class EventBusModule implements Configurable {

  /** The Constant NAME. */
  public static final String NAME = "eventbus";

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jspare.vertx.experimental.Configurable#execute(io.vertx.core.Verticle,
   * java.lang.String[])
   */
  @Override
  public void execute(Verticle verticle, String[] args) {

    EventBusBuilder.create(verticle.getVertx()).scanClasspath(true).build();
  }
}
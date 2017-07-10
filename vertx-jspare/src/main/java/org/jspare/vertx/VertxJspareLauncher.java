/*
 * Copyright 2017 JSpare.org.
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
package org.jspare.vertx;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.jspare.vertx.utils.EnvironmentUtils;

/**
 * The Class VertxJspareLauncher.
 * <p>The launcher class is used to extend the behavior of the original bootstrao of {@link Launcher}. Using the bootstrap strategy the Jspare {@link org.jspare.core.Environment} is initialized by registering a main instance of the container. After initialization, the main vertx interfaces are registered in the container to be used through IoC by {@link EnvironmentUtils}.</p>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class VertxJspareLauncher extends Launcher {

  /**
   * Main entry point.
   *
   * @param args the user command line arguments.
   */
  public static void main(String[] args) {
    new VertxJspareLauncher().dispatch(args);
  }

  /*
   * (non-Javadoc)
   *
   * @see io.vertx.core.Launcher#afterStartingVertx(io.vertx.core.Vertx)
   */
  @Override
  public void afterStartingVertx(Vertx vertx) {
    EnvironmentUtils.bindInterfaces(vertx);
  }

  /*
   * (non-Javadoc)
   *
   * @see io.vertx.core.Launcher#beforeStartingVertx(io.vertx.core.VertxOptions)
   */
  @Override
  public void beforeStartingVertx(VertxOptions options) {
    EnvironmentUtils.setup();
  }
}

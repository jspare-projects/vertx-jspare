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
package org.jspare.vertx.bootstrap;

import static org.jspare.core.container.Environment.registryResource;

import org.jspare.core.bootstrap.Runner;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * The Class VertxJspareLauncher.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class VertxJspareLauncher extends Launcher implements Runner {

  /**
   * Main entry point.
   *
   * @param args
   *          the user command line arguments.
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

    registryResource(new VertxHolder().vertx(vertx));

    run();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.core.Launcher#beforeStartingVertx(io.vertx.core.VertxOptions)
   */
  @Override
  public void beforeStartingVertx(VertxOptions options) {

    setup();

    mySupport();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jspare.core.bootstrap.Runner#setup()
   */
  @Override
  public void setup() {

    EnvironmentUtils.register();
  }
}
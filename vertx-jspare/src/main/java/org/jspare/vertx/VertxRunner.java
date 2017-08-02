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


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.jspare.core.Runner;
import org.jspare.vertx.builder.VertxBuilder;
import org.jspare.vertx.cdi.EnvironmentLoader;
import org.jspare.vertx.internal.VerticleInitializer;

/**
 * The Class VertxRunner.
 * <p>Auxiliary class for booting and configuring a standalone application using the framework.</p>
 *  @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public abstract class VertxRunner extends AbstractVerticle implements Runner {

  /*
   * (non-Javadoc)
   *
   * @see org.jspare.core.bootstrap.Runner#run()
   */
  @Override
  public void run() {

    setup();

    vertx().setHandler(res -> {

      if (res.succeeded()) {

        EnvironmentLoader.bindInterfaces(vertx);

        mySupport();

      } else {

        throw new RuntimeException("Failed to create Vert.x instance");
      }
    });
  }

  /*
   * (non-Javadoc)
   *
   * @see org.jspare.core.bootstrap.Runner#setup()
   */
  @Override
  public void setup() {
    EnvironmentLoader.setup();
  }

  /**
   * Vertx.
   *
   * @return the future
   */
  protected Future<Vertx> vertx() {

    return VertxBuilder.create().build().compose(vertx -> {

      vertx.deployVerticle(VerticleInitializer.initialize(this));
    }, Future.succeededFuture());
  }
}

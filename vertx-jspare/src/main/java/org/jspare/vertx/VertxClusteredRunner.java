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
package org.jspare.vertx;

import org.jspare.core.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import org.jspare.vertx.builder.VertxBuilder;
import org.jspare.vertx.utils.EnvironmentUtils;

/**
 * The Class VertxClusteredRunner.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public abstract class VertxClusteredRunner extends AbstractVerticle implements Runner {

  /*
   * (non-Javadoc)
   *
   * @see org.jspare.core.bootstrap.Runner#run()
   */
  @Override
  public void run() {

    setup();

    mySupport();

    vertx().setHandler(res -> {

      if (res.succeeded()) {

        EnvironmentUtils.bindInterfaces(res.result());
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

    EnvironmentUtils.setup();
  }

  /**
   * Cluster manager.
   *
   * @return the cluster manager
   */
  protected abstract ClusterManager clusterManager();

  /**
   * Vertx.
   *
   * @return the future
   */
  protected Future<Vertx> vertx() {

    VertxOptions options = new VertxOptions();
    options.setClustered(true);
    options.setClusterManager(clusterManager());

    return VertxBuilder.create().options(options).build().compose(vertx -> {
      vertx.deployVerticle(this);
    }, Future.succeededFuture());
  }
}

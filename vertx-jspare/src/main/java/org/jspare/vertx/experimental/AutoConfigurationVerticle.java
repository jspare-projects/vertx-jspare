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

import org.jspare.vertx.bootstrap.VerticleInitializer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * An abstract base class that you can extend to write your own Verticle classes
 * using {@link AutoConfiguration } feature.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class AutoConfigurationVerticle extends AbstractVerticle {

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.core.AbstractVerticle#start(io.vertx.core.Future)
   */
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    VerticleInitializer.initialize(this);
    start();
    startFuture.complete();
  }
}
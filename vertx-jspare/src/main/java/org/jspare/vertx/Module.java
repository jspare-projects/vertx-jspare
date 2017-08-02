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

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jspare.vertx.annotation.Modules;

/**
 * An interface able to aggregate for auto boot when used
 * {@link Modules } feature.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public interface Module extends Modularized {

  /**
   * Execute process.
   *
   * @param instance the modularized instance
   * @param config      the configuration
   * @return Future the future
   */
  Future<Void> init(Modularized instance, JsonObject config);
}

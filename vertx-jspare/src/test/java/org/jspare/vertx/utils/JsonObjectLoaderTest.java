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
package org.jspare.vertx.utils;


import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.jspare.vertx.unit.ext.junit.VertxJspareUnitRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jspare.core.Environment.my;

/**
 * The Class JsonObjectLoaderTest.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@RunWith(VertxJspareUnitRunner.class)
public class JsonObjectLoaderTest {

  /**
   * Load test.
   */
  @Test
  public void loadTest() {

    JsonObject jsonObject = my(JsonObjectLoader.class).loadOptions("VertxOptions.json");
    Assert.assertNotNull(jsonObject);
    Assert.assertTrue(jsonObject.getBoolean("clustered"));

    VertxOptions vertxOptions = my(JsonObjectLoader.class).loadOptions("VertxOptions.json", VertxOptions.class);
    Assert.assertNotNull(vertxOptions);
    Assert.assertTrue(vertxOptions.isClustered());
  }

}

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
package org.jspare.vertx.web.annotation.handling;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;

/**
 * The Class MapModelParser.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class MapModelParser {

  /**
   * To map.
   *
   * @param <K>
   *          the key type
   * @param <V>
   *          the value type
   * @param json
   *          the json
   * @param value
   *          the value
   * @return the map
   */
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> toMap(String json, Class<V> value) {

    Map<K, V> resultMap = new HashMap<>();
    JsonObject jsonMap = new JsonObject(json);
    jsonMap.forEach(e -> {

      resultMap.put((K) e.getKey(), Json.decodeValue(((JsonObject) e.getValue()).toString(), value));
    });
    return resultMap;
  }
}

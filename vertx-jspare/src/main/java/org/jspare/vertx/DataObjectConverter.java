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

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
@UtilityClass
public class DataObjectConverter {

  /**
   * Serialize Object to JsonObject
   * <p>
   * Merge in another JSON object, this is the equivalent of putting all the
   * entries of the other JSON object into this object.
   * </p>
   *
   * @param obj  the obj
   * @param json the json
   * @return the json object
   */
  public JsonObject toJson(Object obj, JsonObject json) {
    json = json.mergeIn(toJson(obj));
    return json;
  }

  /**
   * Serialize Object to JsonObject
   *
   * @param obj the obj
   * @return the json object
   */
  public JsonObject toJson(Object obj) {
    return new JsonObject(Json.encode(obj));
  }

  /**
   * From json.
   *
   * @param <T>  the generic type
   * @param obj  the obj
   * @param json the json
   * @return the t
   */
  @SneakyThrows
  public <T> T fromJson(JsonObject json, T obj) {
    BeanUtils.copyProperties(obj, Json.decodeValue(json.encode(), (Class<T>) obj.getClass()));
    return obj;
  }

  /**
   * From json.
   *
   * @param <T>   the generic type
   * @param json  the json
   * @param clazz the clazz
   * @return the t
   */
  public <T> T fromJson(JsonObject json, Class<T> clazz) {
    String encode = json.encode();
    return Json.decodeValue(encode, clazz);
  }
}

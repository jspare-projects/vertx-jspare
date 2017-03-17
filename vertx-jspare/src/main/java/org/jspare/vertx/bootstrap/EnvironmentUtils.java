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

import org.jspare.core.container.Environment;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

import io.vertx.core.json.Json;
import lombok.experimental.UtilityClass;
import org.jspare.vertx.ext.jackson.datatype.VertxJsonModule;

/**
 * Instantiates a new environment utils.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@UtilityClass
public class EnvironmentUtils {

  public final String VERTX_HOLDER = "__vertxHolder";

  /**
   * Register.
   */
  protected void register() {

    // Prepare Environment with VertxInject
    Environment.load();

    // Set default Json Mapper options
    Json.mapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
        .setVisibility(PropertyAccessor.ALL, Visibility.ANY).setSerializationInclusion(Include.NON_NULL)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        //Safe register VertxJsonModule on Mapper
        .registerModule(new VertxJsonModule())
        .findAndRegisterModules();
  }
}

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

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import lombok.experimental.UtilityClass;
import org.jspare.core.Environment;
import org.jspare.core.internal.Bind;
import org.jspare.vertx.annotation.SharedWorkerExecutor;
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
  public void setup() {

    // Prepare Environment with VertxInject
    Environment.create();

    // Set default Json Mapper options
    Json.mapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
        .setVisibility(PropertyAccessor.ALL, Visibility.ANY).setSerializationInclusion(Include.NON_NULL)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        //Safe register VertxJsonModule on Mapper
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule())
        .registerModule(new ParameterNamesModule())
        .registerModule(new VertxJsonModule());
  }

  public void bindInterfaces(Vertx vertx){
    Environment.registry(Bind.bind(Vertx.class), vertx);
    Environment.registry(Bind.bind(Context.class), vertx.getOrCreateContext());
    Environment.registry(Bind.bind(EventBus.class), vertx.eventBus());
    Environment.registry(Bind.bind(FileSystem.class), vertx.fileSystem());
    Environment.registry(Bind.bind(SharedData.class), vertx.sharedData());
  }
}

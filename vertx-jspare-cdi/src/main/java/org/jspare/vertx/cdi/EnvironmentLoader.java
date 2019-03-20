package org.jspare.vertx.cdi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.Json;
import io.vertx.core.shareddata.SharedData;
import lombok.experimental.UtilityClass;
import org.jspare.core.Environment;
import org.jspare.core.internal.Bind;
import org.jspare.vertx.ext.jackson.datatype.VertxJsonModule;

/**
 * Created by paulo.ferreira on 01/08/2017.
 */
@UtilityClass
public class EnvironmentLoader {

  public final String VERTX_HOLDER = "__vertxHolder";

  /**
   * Register.
   */
  public void setup() {

    // Prepare Environment with VertxInject
    Environment.create();

    // Set default Json Mapper options
    Json.mapper
      .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY).setSerializationInclusion(JsonInclude.Include.NON_NULL)
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

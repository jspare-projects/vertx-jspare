package org.jspare.vertx.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;
import io.vertx.core.json.Json;
import org.jspare.vertx.ext.jackson.datatype.VertxJsonModule;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by paulo.ferreira on 16/03/2017.
 */
public class VertxJsonModuleTest {

  @Test
  public void testJsonModule() {

    Json.mapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
      .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY).setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .registerModule(new VertxJsonModule())
      .findAndRegisterModules();

    String jsonAsString = "{ \"value\" : { \"value\" : \"teste\" } }";
    Value value = Json.decodeValue(jsonAsString, Value.class);
    Assert.assertEquals("teste", value.getValue().getString("value", ""));
  }
}

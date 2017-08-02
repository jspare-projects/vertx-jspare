package org.jspare.vertx.json;

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
      .registerModule(new VertxJsonModule());

    String jsonAsString = "{ \"value\" : { \"value\" : \"teste\" } }";
    Value value = Json.decodeValue(jsonAsString, Value.class);
    Assert.assertEquals("teste", value.getValue().getString("value", ""));
  }
}

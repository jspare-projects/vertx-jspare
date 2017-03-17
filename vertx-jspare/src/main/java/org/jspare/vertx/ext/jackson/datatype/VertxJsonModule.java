package org.jspare.vertx.ext.jackson.datatype;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.time.ZoneId;

/**
 * Provides Serializers and Deserializers for {@link JsonObject} and {@link JsonArray}.
 *
 * @author Paulo Ferreira
 * @since 1.1.0
 */
public class VertxJsonModule extends SimpleModule {

  /**
   * @see Serializable
   */
  private static final long serialVersionUID = -1;

  private final static String NAME = "VertxJsonModule";

  /**
   * Creates a new module.
   *
   * @since 1.1.0
   */
  public VertxJsonModule() {
    super(NAME, VersionUtil.parseVersion("1.1.0", "org.jspare.vertx", "jackson-datatype-vertx"));
    addDeserializer(JsonArray.class, JsonArrayDeserializer.INSTANCE);
    addDeserializer(JsonObject.class, JsonObjectDeserializer.INSTANCE);
    addSerializer(JsonArraySerializer.INSTANCE);
    addSerializer(JsonObjectSerializer.INSTANCE);
  }
}

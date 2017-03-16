package org.jspare.vertx.ext.jackson.datatype;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * Provides Serializers and Deserializers for {@link JsonObject} and {@link JsonArray}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
public class VertxJsonModule extends SimpleModule {

    /**
     * @see Serializable
     */
    private static final long serialVersionUID = 4038489993817232066L;

    private final static String NAME = "VertxJsonModule";

    /**
     * Creates a new module.
     *
     * @since 2.1
     */
    public VertxJsonModule() {
        super(NAME, VersionUtil.parseVersion("2.1-SNAPSHOT", "de.crunc", "jackson-datatype-vertx"));
        addDeserializer(JsonArray.class, JsonArrayDeserializer.INSTANCE);
        addDeserializer(JsonObject.class, JsonObjectDeserializer.INSTANCE);
        addSerializer(JsonArraySerializer.INSTANCE);
        addSerializer(JsonObjectSerializer.INSTANCE);
    }
}

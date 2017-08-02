package org.jspare.vertx.ext.jackson.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.io.Serializable;

/**
 * Deserializer which produces {@link JsonObject}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
class JsonObjectDeserializer extends StdDeserializer<JsonObject> {

    /**
     * @see Serializable
     */
    private static final long serialVersionUID = 4883042507402315060L;

    /**
     * Singleton instance of {@link JsonObjectDeserializer}
     *
     * @since 2.1
     */
    public final static JsonObjectDeserializer INSTANCE = new JsonObjectDeserializer();

    /**
     * Creates a new deserializer.
     *
     * @since 2.1
     */
    JsonObjectDeserializer() {
        super(JsonObject.class);
    }

    @Override
    public JsonObject deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonObjectBuilder builder = JsonObjectBuilder.object();

        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
            String fieldName = jp.getCurrentName();
            t = jp.nextToken();

            switch (t) {
                case START_ARRAY:
                    builder.put(fieldName, JsonArrayDeserializer.INSTANCE.deserialize(jp, ctxt));
                    continue;
                case START_OBJECT:
                    builder.put(fieldName, deserialize(jp, ctxt));
                    continue;
                case VALUE_STRING:
                    builder.put(fieldName, jp.getText());
                    continue;
                case VALUE_NULL:
                    builder.putNull(fieldName);
                    continue;
                case VALUE_TRUE:
                    builder.put(fieldName, Boolean.TRUE);
                    continue;
                case VALUE_FALSE:
                    builder.put(fieldName, Boolean.FALSE);
                    continue;
                case VALUE_NUMBER_INT:
                    builder.put(fieldName, jp.getNumberValue());
                    continue;
                case VALUE_NUMBER_FLOAT:
                    builder.put(fieldName, jp.getNumberValue());
                    continue;
                default:
                    throw ctxt.mappingException("Unrecognized or unsupported JsonToken type: " + t);
            }
        }

        return builder.build();
    }
}

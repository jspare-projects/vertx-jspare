package org.jspare.vertx.ext.jackson.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Serializes values of type {@link JsonArray}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
class JsonArraySerializer extends JsonBaseSerializer<JsonArray> {

    /**
     * Singleton instance of {@link JsonArraySerializer}
     *
     * @since 2.1
     */
    public final static JsonArraySerializer INSTANCE = new JsonArraySerializer();

    /**
     * Creates a new serializer.
     *
     * @since 2.1
     */
    JsonArraySerializer() {
        super(JsonArray.class);
    }

    @Override
    public void serialize(JsonArray value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeStartArray();
        serializeContents(value, jgen, provider);
        jgen.writeEndArray();
    }

    @Override
    public void serializeWithType(JsonArray value, JsonGenerator jgen, SerializerProvider provider,
                                  TypeSerializer typeSer)
            throws IOException {
        typeSer.writeTypePrefixForArray(value, jgen);
        serializeContents(value, jgen, provider);
        typeSer.writeTypeSuffixForArray(value, jgen);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
            throws JsonMappingException {
        return createSchemaNode("array", true);
    }

    protected void serializeContents(JsonArray array, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        List value = array.getList();

        for (int i = 0, len = value.size(); i < len; ++i) {
            Object ob = value.get(i);
            if (ob == null) {
                jgen.writeNull();
                continue;
            }
            Class<?> cls = ob.getClass();
            if (cls == JsonObject.class) {
                JsonObjectSerializer.INSTANCE.serialize((JsonObject) ob, jgen, provider);
            } else if (cls == JsonArray.class) {
                serialize((JsonArray) ob, jgen, provider);
            } else if (cls == String.class) {
                jgen.writeString((String) ob);
            } else if (cls == Integer.class) {
                jgen.writeNumber(((Integer) ob).intValue());
            } else if (cls == Long.class) {
                jgen.writeNumber(((Long) ob).longValue());
            } else if (cls == Boolean.class) {
                jgen.writeBoolean(((Boolean) ob).booleanValue());
            } else if (cls == Double.class) {
                jgen.writeNumber(((Double) ob).doubleValue());
            } else if (JsonObject.class.isAssignableFrom(cls)) { // sub-class
                JsonObjectSerializer.INSTANCE.serialize((JsonObject) ob, jgen, provider);
            } else if (JsonArray.class.isAssignableFrom(cls)) { // sub-class
                serialize((JsonArray) ob, jgen, provider);
            } else if (JsonArray.class.isAssignableFrom(cls)) { // sub-class
                JsonArraySerializer.INSTANCE.serialize((JsonArray) ob, jgen, provider);
            } else {
                provider.defaultSerializeValue(ob, jgen);
            }
        }
    }
}

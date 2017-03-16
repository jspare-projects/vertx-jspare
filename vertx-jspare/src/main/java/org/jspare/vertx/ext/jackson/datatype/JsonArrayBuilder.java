package org.jspare.vertx.ext.jackson.datatype;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for {@link JsonArray}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
public class JsonArrayBuilder {

    private final List<Object> values;

    private JsonArrayBuilder() {
        values = new ArrayList<Object>();
    }

    /**
     * Adds an object to the end of the array.
     *
     * @param jsonObject The object that will be added. Can be {@code null} in which case {@code null} is added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(JsonObject jsonObject) {
        values.add(jsonObject);
        return this;
    }

    /**
     * Adds an object to the end of the array.
     *
     * @param builder The builder for the object that will be added. Can be {@code null} in which case {@code null} is
     *                added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(JsonObjectBuilder builder) {
        if (builder != null) {
            return add(builder.build());
        }
        return add((JsonObject) null);
    }

    /**
     * Adds an array to the end of the array.
     *
     * @param jsonArray The array that will be added. Can be {@code null} in which case {@code null} is added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(JsonArray jsonArray) {
        values.add(jsonArray);
        return this;
    }

    /**
     * Adds an array to the end of the array.
     *
     * @param builder The builder for the array that will be added. Can be {@code null} in which case {@code null} is
     *                added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(JsonArrayBuilder builder) {
        if (builder != null) {
            return add(builder.build());
        }
        return add((JsonArray) null);
    }

    /**
     * Adds a string to the end of the array.
     *
     * @param string The string that will be added. Can be {@code null} in which case {@code null} is added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(String string) {
        values.add(string);
        return this;
    }

    /**
     * Adds a number to the end of the array.
     *
     * @param number The number that will be added. Can be {@code null} in which case {@code null} is added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(Number number) {
        values.add(number);
        return this;
    }

    /**
     * Adds a boolean value to the end of the array.
     *
     * @param bool The boolean value that will be added. Can be {@code null} in which case {@code null} is added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(Boolean bool) {
        values.add(bool);
        return this;
    }

    /**
     * Adds binary data to the end of the array.
     *
     * @param bytes The binary data that will be added. Can be {@code null} in which case {@code null} is added.
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder add(byte[] bytes) {
        values.add(bytes);
        return this;
    }

    /**
     * Adds {@code null} to the end of the array.
     *
     * @return {@code this}
     * @since 2.1
     */
    public JsonArrayBuilder addNull() {
        values.add(new JsonNull());
        return this;
    }

    /**
     * Builds a new array which contains the values that have been added to this builder so far.
     *
     * @return A new array.
     * @since 2.1
     */
    public JsonArray build() {
        JsonArray array = new JsonArray();

        for (Object value : values) {
            if (value instanceof JsonNull) {
                array.addNull();
            }
            else {
                array.add(value);
            }
        }

        return array;
    }

    /**
     * Builds a new array which contains the values that have been added to this builder so far and encodes it as a
     * JSON string like {@code "[1, true, "foo", {"bar":3}, [2, 4, 6, 8], null]"}
     *
     * @return A JSON array string.
     * @since 2.1
     */
    public String encode() {
        return build().encode();
    }

    /**
     * Factory method for creating a new builder.
     *
     * @return A new builder.
     * @since 2.1
     */
    public static JsonArrayBuilder array() {
        return new JsonArrayBuilder();
    }
}

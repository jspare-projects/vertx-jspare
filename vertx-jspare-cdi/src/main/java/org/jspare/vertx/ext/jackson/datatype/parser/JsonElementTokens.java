package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigInteger;

/**
 * Helps finding {@link JsonToken}s in a tree of {@link JsonObject}s.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
class JsonElementTokens {

    /**
     * Gets the {@link JsonToken} based on the given elements type.
     *
     * @param element The element that's token should be retrieved. Can be {@code null}.
     * @return The token for the element.
     * @since 2.1
     */
    static JsonToken getToken(Object element) {
        if (element == null) {
            return JsonToken.VALUE_NULL;
        } else if (element instanceof JsonObject) {
            return JsonToken.START_OBJECT;
        } else if (element instanceof JsonArray) {
            return JsonToken.START_ARRAY;
        } else if (element instanceof Boolean) {
            if (Boolean.TRUE.equals(element)) {
                return JsonToken.VALUE_TRUE;
            } else {
                return JsonToken.VALUE_FALSE;
            }
        } else if (element instanceof Number) {
            if (element instanceof Integer
                    || element instanceof Long
                    || element instanceof BigInteger
                    || element instanceof Short) {
                return JsonToken.VALUE_NUMBER_INT;
            } else {
                return JsonToken.VALUE_NUMBER_FLOAT;
            }
        } else if (element instanceof String) {
            return JsonToken.VALUE_STRING;
        } else {
            return JsonToken.VALUE_EMBEDDED_OBJECT;
        }
    }
}

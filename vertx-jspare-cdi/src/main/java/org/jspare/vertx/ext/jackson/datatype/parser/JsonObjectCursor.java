package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;

/**
 * Cursor for traversing {@link JsonObject}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
public class JsonObjectCursor extends AbstractObjectCursor<JsonObject, Object> {

    /**
     * Creates a new cursor with the given parent for the given object.
     *
     * @param jsonObject   The object which is traversed by this cursor. Must not be {@code null}.
     * @param parentCursor The parent of this cursor. Can be {@code null}.
     * @throws IllegalArgumentException If the given jsonObject is {@code null}.
     * @since 2.1
     */
    JsonObjectCursor(JsonObject jsonObject, AbstractTreeCursor<Object> parentCursor) {
        super(jsonObject, parentCursor);
    }

    @Override
    protected Iterator<String> getFields(JsonObject object) {
        return object.fieldNames().iterator();
    }

    @Override
    protected Object getValue(String fieldName, JsonObject object) {
        return object.getValue(fieldName);
    }

    @Override
    protected int getNumberOfChildren(Object element) {
        if (element instanceof JsonObject) {
            return ((JsonObject) element).size();
        } else if (element instanceof JsonArray) {
            return ((JsonArray) element).size();
        } else {
            return -1;
        }
    }

    @Override
    protected AbstractTreeCursor<Object> newObjectCursor(Object object) {
        return new JsonObjectCursor((JsonObject)object, this);
    }

    @Override
    protected AbstractTreeCursor<Object> newArrayCursor(Object array) {
        return new JsonArrayCursor((JsonArray)array, this);
    }

    @Override
    protected JsonToken getToken(Object element) {
        return JsonElementTokens.getToken(element);
    }
}

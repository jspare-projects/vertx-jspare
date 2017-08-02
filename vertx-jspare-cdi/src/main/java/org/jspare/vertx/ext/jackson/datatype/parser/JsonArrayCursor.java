package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;

/**
 * Cursor for traversing {@link JsonArray}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
class JsonArrayCursor extends AbstractArrayCursor<JsonArray, Object> {

    /**
     * Creates a new cursor with the given parent. If the parent is {@code null} then this cursor can be considered a
     * root level cursor.
     *
     * @param jsonArray    The array that should be traversed.
     * @param parentCursor The parent of this cursor. Can be {@code null}.
     * @since 2.1
     */
    JsonArrayCursor(JsonArray jsonArray, AbstractTreeCursor<Object> parentCursor) {
        super(jsonArray, parentCursor);
    }

    @Override
    protected Iterator<Object> getElements(JsonArray array) {
        return array.iterator();
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

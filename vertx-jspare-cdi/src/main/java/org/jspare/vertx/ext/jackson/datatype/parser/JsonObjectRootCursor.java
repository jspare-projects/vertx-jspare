package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
/**
 * Cursor for traversing a {@link JsonObject} root.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
public class JsonObjectRootCursor extends AbstractRootCursor<Object, Object> {

    /**
     * Creates a new root level cursor.
     *
     * @param rootElement  The root element of the JSON structure.
     * @since 2.1
     */
    JsonObjectRootCursor(Object rootElement) {
        super(rootElement);
    }

    @Override
    protected JsonToken getRootToken(Object root) {
        if (root instanceof JsonObject) {
            return JsonToken.START_OBJECT;
        } else if (root instanceof JsonArray) {
            return JsonToken.START_ARRAY;
        } else {
            throw new IllegalArgumentException("unknown JSON element type <" + root + ">");
        }
    }

    @Override
    protected Object getRootValue(Object root) {
        return root;
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

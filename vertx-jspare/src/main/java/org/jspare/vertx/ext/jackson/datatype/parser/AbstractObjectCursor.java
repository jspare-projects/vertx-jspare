package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;

import java.util.Iterator;

/**
 * Cursor for traversing JSON object structures.
 *
 * @param <T> The type of the JSON object.
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
abstract class AbstractObjectCursor<T, E> extends AbstractTreeCursor<E> {

    /**
     * The object which is being traversed.
     */
    private final T object;

    /**
     * Iterates over all fields of the object.
     */
    private final SkippableIterator<String> fields;

    /**
     * The token for the current position of the cursor.
     */
    private JsonToken currentToken;

    /**
     * The name of the field this cursor is currently traversing.
     */
    private String currentFieldName;

    /**
     * The element at the current position of the cursor.
     */
    private E currentValue;

    /**
     * Creates a new cursor with the given parent for the given object.
     *
     * @param jsonObject   The object which is traversed by this cursor. Must not be {@code null}.
     * @param parentCursor The parent of this cursor. Can be {@code null}.
     * @throws IllegalArgumentException If the given jsonObject is {@code null}.
     * @since 2.1
     */
    AbstractObjectCursor(T jsonObject, AbstractTreeCursor<E> parentCursor) {
        super(TYPE_OBJECT, parentCursor);

        if (jsonObject == null) {
            throw new IllegalArgumentException("jsonObject must not be null");
        }

        object = jsonObject;
        fields = new SkippableIterator<String>(getFields(object));
        _index = -1;
    }

    /**
     * Retrieve an iterator for all field names of the given object. If there are no fields (empty object) an iterator
     * with no more elements must be returned.
     *
     * @param object The object that's fields should be returned as an iterator.
     * @return A new iterator.
     * @since 2.1
     */
    protected abstract Iterator<String> getFields(T object);

    /**
     * Retrieve the value which is mapped to the given fieldName within the given object.
     *
     * @param fieldName The name of the field that's value should be retrieved.
     * @param object    The object the value should be retrieved from.
     * @return The value of the field or {@code null} if there is no value.
     * @since 2.1
     */
    protected abstract E getValue(String fieldName, T object);

    /**
     * Retrieve the number of children (if any) for the given element. If the element is not a structure which can have
     * children a negative number must be returned.
     *
     * @param element The element that's number of children should be returned.
     * @return The number of children for the given element.
     * @since 2.1
     */
    protected abstract int getNumberOfChildren(E element);

    @Override
    public JsonToken nextToken() {
        if (currentToken == JsonToken.FIELD_NAME) {
            // step from field name to field value
            currentToken = getToken(currentValue);
        } else {
            // step to next field
            if (fields.hasNext()) {
                currentToken = JsonToken.FIELD_NAME;
                currentFieldName = fields.next();
                currentValue = getValue(currentFieldName, object);
                ++_index;
            } else {
                // no more fields
                currentToken = null;
                currentFieldName = null;
                currentValue = null;
            }
        }

        // update currentName
        currentName = currentFieldName;

        return currentToken;
    }

    @Override
    public JsonToken nextValue() {
        JsonToken t = nextToken();
        if (t == JsonToken.FIELD_NAME) {
            t = nextToken();
        }
        return t;
    }

    @Override
    public void skipChildren() {
        fields.skip();
    }

    @Override
    public JsonToken endToken() {
        return JsonToken.END_OBJECT;
    }

    @Override
    public E currentElement() {
        return currentValue;
    }

    @Override
    public boolean currentHasChildren() {
        return getNumberOfChildren(currentValue) > 0;
    }

    @Override
    public AbstractTreeCursor<E> iterateChildren() {
        if (currentToken == JsonToken.START_OBJECT) {
            return newObjectCursor(currentValue);
        } else if (currentToken == JsonToken.START_ARRAY) {
            return newArrayCursor(currentValue);
        } else {
            throw new IllegalStateException("can not iterate children at token <" + currentToken + ">");
        }
    }

    @Override
    public String toString() {
        return "object @ " + currentToken;
    }
}

package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;

import java.util.Iterator;

/**
 * Cursor for traversing JSON array structures.
 *
 * @param <A> The type of the JSON array.
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
abstract class AbstractArrayCursor<A, E> extends AbstractTreeCursor<E> {

    /**
     * The array which is being traversed.
     */
    private final A array;

    /**
     * Iterates over all items of the array.
     */
    private final SkippableIterator<E> elements;

    /**
     * The token for the current position of the cursor.
     */
    private JsonToken currentToken;

    /**
     * The element at the current position of the cursor.
     */
    private E currentElement;

    /**
     * Creates a new cursor with the given parent. If the parent is {@code null} then this cursor can be considered a
     * root level cursor.
     *
     * @param jsonArray    The array that should be traversed.
     * @param parentCursor The parent of this cursor. Can be {@code null}.
     * @since 2.1
     */
    AbstractArrayCursor(A jsonArray, AbstractTreeCursor<E> parentCursor) {
        super(TYPE_ARRAY, parentCursor);

        if (jsonArray == null) {
            throw new IllegalArgumentException("jsonArray must not be null");
        }

        array = jsonArray;
        elements = new SkippableIterator<E>(getElements(array));
        _index = -1;
    }

    /**
     * Retrieve the elements of the given array as an iterator. If there are no elements (empty array) an iterator with
     * no more elements must be returned.
     *
     * @param array The array that's elements should be iterated.
     * @return A new iterator.
     * @since 2.1
     */
    protected abstract Iterator<E> getElements(A array);

    @Override
    public JsonToken nextToken() {
        // step to next element
        if (elements.hasNext()) {
            currentElement = elements.next();
            currentToken = getToken(currentElement);
            ++_index;
        } else {
            // no more fields
            currentToken = null;
            currentElement = null;
        }

        // update currentName
        currentName = String.valueOf(_index);

        return currentToken;
    }

    @Override
    public JsonToken nextValue() {
        return nextToken();
    }

    @Override
    public void skipChildren() {
        elements.skip();
    }

    @Override
    public JsonToken endToken() {
        return JsonToken.END_ARRAY;
    }

    @Override
    public E currentElement() {
        return currentElement;
    }

    @Override
    public boolean currentHasChildren() {
        return getNumberOfChildren(currentElement) > 0;
    }

    @Override
    public AbstractTreeCursor<E> iterateChildren() {
        if (currentToken == JsonToken.START_OBJECT) {
            return newObjectCursor(currentElement);
        } else if (currentToken == JsonToken.START_ARRAY) {
            return newArrayCursor(currentElement);
        } else {
            throw new IllegalStateException("can not iterate children at token <" + currentToken + ">");
        }
    }

    @Override
    public String toString() {
        return "array @ " + currentToken;
    }
}

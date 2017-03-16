package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonToken;

/**
 * Cursor for traversing the root element of JSON structures.
 *
 * @param <S> Common super type of the JSON structures.
 * @param <E> Common super type for all elements in the tree.
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
abstract class AbstractRootCursor<S, E> extends AbstractTreeCursor<E> {

    private final S root;

    private JsonToken currentToken;
    private E currentElement;

    private boolean done = false;

    /**
     * Creates a new cursor with the given parent. If the parent is {@code null} then this cursor can be considered a
     * root level cursor.
     *
     * @param rootElement  The root element of the JSON structure.
     * @since 2.1
     */
    AbstractRootCursor(S rootElement) {
        super(TYPE_ROOT, null);

        if (rootElement == null) {
            throw new IllegalArgumentException("rootElement must not be null");
        }

        root = rootElement;
        _index = -1;
    }

    /**
     * Retrieves the token that represents the given root element.
     *
     * @param root The root element that's token should be retrieved.
     * @return The token for the root element.
     * @since 2.1
     */
    protected abstract JsonToken getRootToken(S root);

    /**
     * Retrieves the value for the given root element. Can be the element itself.
     *
     * @param root The root element that's value should be retrieved.
     * @return The value for the root element.
     * @since 2.1
     */
    protected abstract E getRootValue(S root);

    @Override
    public JsonToken nextToken() {
        if (!done) {
            // 1st step
            done = true;
            currentElement = getRootValue(root);
            currentToken = getRootToken(root);
            _index = 0;
        }
        else {
            // 2nd, 3rd, ... step
            currentElement = null;
            currentToken = null;
        }

        return currentToken;
    }

    @Override
    public JsonToken nextValue() {
        return nextToken();
    }

    @Override
    public void skipChildren() {
        done = true;
    }

    @Override
    public JsonToken endToken() {
        return null;
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
}

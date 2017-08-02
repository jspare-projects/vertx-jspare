package org.jspare.vertx.ext.jackson.datatype.parser;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Keeps track of the current location with the tree of JSON elements.
 *
 * @param <E> Common super type for all elements in the tree.
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
abstract class AbstractTreeCursor<E> extends JsonStreamContext {

    /**
     * The parent of this cursor.
     */
    protected final AbstractTreeCursor<E> parent;

    /**
     * The current name (which can be overridden)
     */
    protected String currentName;

    /**
     * Creates a new cursor with the given parent. If the parent is {@code null} then this cursor can be considered a
     * root level cursor.
     *
     * @param contextType  The context type of this cursor. Must be one of {@link #TYPE_OBJECT}, {@link #TYPE_ARRAY},
     *                     {@link #TYPE_ROOT}
     * @param parentCursor The parent of this cursor. Can be {@code null}.
     * @since 2.1
     */
    AbstractTreeCursor(int contextType, AbstractTreeCursor<E> parentCursor) {
        _type = contextType;
        parent = parentCursor;
    }

    @Override
    public AbstractTreeCursor<E> getParent() {
        return parent;
    }

    @Override
    public String getCurrentName() {
        return currentName;
    }

    /**
     * Overrides the current name of this cursor.
     *
     * @param name The new current name.
     * @since 2.1
     */
    public void overrideCurrentName(String name) {
        currentName = name;
    }

    /**
     * Advance the cursor to the next token and return it. If there are no more token {@code null} must be returned.
     *
     * @return The next token or {@code null}.
     * @since 2.1
     */
    public abstract JsonToken nextToken();

    /**
     * Advance the cursor to the next value token and return it. If there are no more token {@code null} must be
     * returned.
     *
     * @return The next value token or {@code null}.
     * @since 2.1
     */
    public abstract JsonToken nextValue();

    /**
     * Advance the cursor behind the last child.
     *
     * @since 2.1
     */
    public abstract void skipChildren();

    /**
     * Retrieve the end token for the structure this cursor traverses. If this cursor traverses a JSON object structure
     * then {@link JsonToken#END_OBJECT} must be returned. If this cursor traverses a JSON array structure then
     * {@link JsonToken#END_ARRAY} must be returned.
     *
     * @return The end token for the structure this cursor traverses.
     * @since 2.1
     */
    public abstract JsonToken endToken();

    /**
     * Retrieve the element/value this cursor currently points at.
     *
     * @return The current element.
     * @since 2.1
     */
    public abstract E currentElement();

    /**
     * Indicates whether the current element is a structure with one or more child elements.
     *
     * @return {@code true} if the current element is a structure with children, {@code false} otherwise.
     * @since 2.1
     */
    public abstract boolean currentHasChildren();

    /**
     * Retrieve the number of children (if any) for the given element. If the element is not a structure which can have
     * children a negative number must be returned.
     *
     * @param element The element that's number of children should be returned.
     * @return The number of children for the given element.
     * @since 2.1
     */
    protected abstract int getNumberOfChildren(E element);

    /**
     * Creates a new cursor for iterating over the children of the current element. If the current element does not have
     * any children an {@link IllegalStateException} must be thrown.
     *
     * @return A new cursor.
     * @throws IllegalStateException If the current element does not have children.
     * @since 2.1
     */
    public abstract AbstractTreeCursor<E> iterateChildren();

    /**
     * Creates a new cursor for the given object element.
     *
     * @param object The object element that should be traversed.
     * @return A new cursor for the given object.
     * @since 2.1
     */
    protected abstract AbstractTreeCursor<E> newObjectCursor(E object);

    /**
     * Creates a new cursor for the given array element.
     *
     * @param array The array element that should be traversed.
     * @return A new cursor for the given array.
     * @since 2.1
     */
    protected abstract AbstractTreeCursor<E> newArrayCursor(E array);

    /**
     * Retrieves the token for the given element. If the element is a JSON object or JSON array then
     * {@link JsonToken#START_OBJECT} or {@link JsonToken#START_ARRAY} must be returned. If the element is {@code null}
     * then {@link JsonToken#VALUE_NULL} must be returned.
     *
     * @param element The element that's token is required.
     * @return The token for the element.
     * @since 2.1
     */
    protected abstract JsonToken getToken(E element);
}

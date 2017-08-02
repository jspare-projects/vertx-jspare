package org.jspare.vertx.ext.jackson.datatype.parser;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Wraps an iterator and provides a {@code skip} method which causes the iterator to stop as if it had reached it's end.
 * Note that skipping can only be done if the iterator has not yet started iterating. Otherwise skipping will have no
 * effect.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
class SkippableIterator<E> implements Iterator<E> {

    private final Iterator<E> it;
    private boolean started = false;
    private boolean skipped = false;

    /**
     * Wraps the given iterator transforming it to a skippable iterator.
     *
     * @param delegate The iterator which is wrapped by this iterator.
     * @since 2.1
     */
    SkippableIterator(Iterator<E> delegate) {
        it = delegate;
    }

    /**
     * Causes this iterator to skip the remaining iteration, if iteration has not yet started.
     *
     * @since 2.1
     */
    public void skip() {
        if (!started) {
            skipped = true;
        }
    }

    @Override
    public boolean hasNext() {
        return !skipped && it.hasNext();
    }

    @Override
    public E next() {
        if (skipped) {
            throw new NoSuchElementException("no more elements (remaining elements have been skipped)");
        }
        started = true;
        return it.next();
    }

    @Override
    public void remove() {
        if (skipped) {
            throw new IllegalStateException("can not remove item, end of iterator has been reached du to skipping");
        }
        it.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        if (!skipped) {
            started = true;
            it.forEachRemaining(action);
        }
    }
}

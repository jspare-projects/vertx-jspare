package org.jspare.vertx.ext.jackson.datatype;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Common base class for all serializers.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.1
 */
public abstract class JsonBaseSerializer<T> extends StdSerializer<T> {

    /**
     * Creates a new base serializer for the given class.
     *
     * @param cls The type that can be serialized.
     * @since 2.1
     */
    protected JsonBaseSerializer(Class<T> cls) {
        super(cls);
    }
}

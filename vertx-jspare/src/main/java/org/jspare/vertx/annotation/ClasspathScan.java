package org.jspare.vertx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ClasspathScan indicate the components package to be scanned and loaded without any register.
 *
 * <b>Note</b>: Multiple binding will be discarded.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClasspathScan {
    String[] value();
}

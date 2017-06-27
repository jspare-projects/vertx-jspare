/**
 * Copyright 2016 Senior Sistemas.
 *
 * Software sob Medida
 *
 */
package org.jspare.vertx.unit.mock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface AsyncMock.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface AsyncMock {

}

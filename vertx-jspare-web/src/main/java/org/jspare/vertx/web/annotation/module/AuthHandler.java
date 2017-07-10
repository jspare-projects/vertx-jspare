package org.jspare.vertx.web.annotation.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AuthHandler {

  Class<? extends io.vertx.ext.web.handler.AuthHandler> value() default io.vertx.ext.web.handler.AuthHandler.class;
}

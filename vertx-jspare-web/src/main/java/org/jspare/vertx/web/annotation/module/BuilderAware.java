package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import org.jspare.vertx.web.builder.RouterBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BuilderAware {

  Class<? extends Handler<RouterBuilder>>[] value();
}

package org.jspare.vertx.web.module;

import io.vertx.core.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cors {

  String allowedOriginPattern();

  String[] allowedHeaders() default "";

  HttpMethod[] allowedMethods() default {HttpMethod.GET, HttpMethod.POST};
}

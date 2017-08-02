package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import org.jspare.vertx.Modularized;
import org.jspare.vertx.web.module.AnnotationHandlerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@HandlerAware
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cors {

  String allowedOriginPattern();

  String[] allowedHeaders() default "";

  String[] exposedHeaders() default "";

  boolean allowCredentials() default false;

  int maxAgeSeconds() default -1;

  HttpMethod[] allowedMethods() default {HttpMethod.GET, HttpMethod.POST};

  class CorsHandlerFactory implements AnnotationHandlerFactory<Cors> {

    @Override
    public Handler<RoutingContext> factory(Cors cors, Modularized instance) {
      return CorsHandler.create(cors.allowedOriginPattern())
        .allowCredentials(cors.allowCredentials())
        .allowedHeaders(Arrays.asList(cors.allowedHeaders()).stream().collect(Collectors.toSet()))
        .allowedMethods(Arrays.asList(cors.allowedMethods()).stream().collect(Collectors.toSet()))
        .exposedHeaders(Arrays.asList(cors.exposedHeaders()).stream().collect(Collectors.toSet()))
        .maxAgeSeconds(cors.maxAgeSeconds());
    }
  }
}

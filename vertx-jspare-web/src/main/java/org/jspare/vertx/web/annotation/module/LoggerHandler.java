package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.LoggerFormat;
import org.jspare.vertx.web.module.AnnotationHandlerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@HandlerAware
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoggerHandler {

  boolean immediate() default true;

  LoggerFormat loggerFormat() default LoggerFormat.DEFAULT;

  class LoggerHandlerFactory implements AnnotationHandlerFactory<LoggerHandler> {

    @Override
    public Handler<RoutingContext> factory(LoggerHandler logger, Verticle verticle) {
      return io.vertx.ext.web.handler.LoggerHandler.create(logger.immediate(), logger.loggerFormat());
    }
  }
}

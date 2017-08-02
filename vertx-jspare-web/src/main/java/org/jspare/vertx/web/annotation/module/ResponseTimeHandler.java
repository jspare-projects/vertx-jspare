package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jspare.vertx.Modularized;
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
public @interface ResponseTimeHandler {

  class ResponseTimeHandlerFactory implements AnnotationHandlerFactory<ResponseTimeHandler> {

    @Override
    public Handler<RoutingContext> factory(ResponseTimeHandler ann, Modularized instance) {
      return io.vertx.ext.web.handler.ResponseTimeHandler.create();
    }
  }
}

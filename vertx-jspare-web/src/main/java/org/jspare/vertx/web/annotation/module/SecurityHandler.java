package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.ext.web.RoutingContext;
import org.jspare.vertx.web.module.AnnotationHandlerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by paulo.ferreira on 30/06/2017.
 */
@HandlerAware
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SecurityHandler {

  class SecurityHandlerFactory implements AnnotationHandlerFactory<SecurityHandler> {

    @Override
    public Handler<RoutingContext> factory(SecurityHandler securityHandler, Verticle verticle) {
      return new org.jspare.vertx.web.handler.SecurityHandler();
    }
  }
}

package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
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
public @interface SessionHandler {

  boolean clustered() default false;

  class SessionHandlerFactory implements AnnotationHandlerFactory<SessionHandler> {

    @Override
    public Handler<RoutingContext> factory(SessionHandler logger, Verticle verticle) {

      if(logger.clustered()){

        return io.vertx.ext.web.handler.SessionHandler.create(ClusteredSessionStore.create(verticle.getVertx()));
      }else{
        return io.vertx.ext.web.handler.SessionHandler.create(LocalSessionStore.create(verticle.getVertx()));
      }
    }
  }
}

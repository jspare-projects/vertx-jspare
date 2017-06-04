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
 * Created by paulo.ferreira on 15/03/2017.
 */
@HandlerAware
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BodyHandler {

  String uploadDirectory() default "file-uploads";

  class BodyHandlerFactory implements AnnotationHandlerFactory<BodyHandler> {

    @Override
    public Handler<RoutingContext> factory(BodyHandler bodyHandler, Verticle verticle) {

      String uploadDirectory = bodyHandler.uploadDirectory();
      return io.vertx.ext.web.handler.BodyHandler.create(uploadDirectory);
    }
  }
}

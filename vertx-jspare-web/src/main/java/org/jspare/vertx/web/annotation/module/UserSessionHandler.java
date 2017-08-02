package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;
import org.jspare.core.Environment;
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
public @interface UserSessionHandler {

  Class<? extends AuthProvider> value() default AuthProvider.class;

  class UserSessionHandlerFactory implements AnnotationHandlerFactory<UserSessionHandler> {

    @Override
    public Handler<RoutingContext> factory(UserSessionHandler userSessionHandler, Modularized modularized) {
      AuthProvider authProvider = Environment.my(userSessionHandler.value());
      return io.vertx.ext.web.handler.UserSessionHandler.create(authProvider);
    }
  }
}

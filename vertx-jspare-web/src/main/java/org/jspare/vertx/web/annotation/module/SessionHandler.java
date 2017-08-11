package org.jspare.vertx.web.annotation.module;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.jspare.vertx.Modularized;
import org.jspare.vertx.web.module.AnnotationHandlerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.vertx.ext.web.handler.SessionHandler.*;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@HandlerAware
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SessionHandler {

  boolean clustered() default false;

  long sessionTimeout() default DEFAULT_SESSION_TIMEOUT;

  boolean cookieHttpOnlyFlag() default DEFAULT_COOKIE_HTTP_ONLY_FLAG;

  boolean cookieSecureFlag() default DEFAULT_COOKIE_SECURE_FLAG;

  boolean nagHttps() default DEFAULT_NAG_HTTPS;

  String sessionCookieName() default DEFAULT_SESSION_COOKIE_NAME;

  int minLenght() default DEFAULT_SESSIONID_MIN_LENGTH;

  class SessionHandlerFactory implements AnnotationHandlerFactory<SessionHandler> {

    @Override
    public Handler<RoutingContext> factory(SessionHandler ann, Modularized instance) {
      final Vertx vertx = instance.getVertx();
      return io.vertx.ext.web.handler.SessionHandler.create(sessionStore(ann, vertx))
        .setSessionTimeout(ann.sessionTimeout())
        .setCookieHttpOnlyFlag(ann.cookieHttpOnlyFlag())
        .setCookieSecureFlag(ann.cookieSecureFlag())
        .setNagHttps(ann.nagHttps())
        .setSessionCookieName(ann.sessionCookieName())
        .setMinLength(ann.minLenght());
    }

    private SessionStore sessionStore(SessionHandler ann, Vertx vertx) {
      if (ann.clustered()) {

        return ClusteredSessionStore.create(vertx);
      } else {
        return LocalSessionStore.create(vertx);
      }
    }
  }
}

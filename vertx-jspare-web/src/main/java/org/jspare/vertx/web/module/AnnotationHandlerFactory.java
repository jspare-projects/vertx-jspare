package org.jspare.vertx.web.module;

import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.ext.web.RoutingContext;
import org.jspare.vertx.Modularized;

import java.lang.annotation.Annotation;

/**
 * Created by paulo.ferreira on 31/05/2017.
 */
public interface AnnotationHandlerFactory<A extends  Annotation> {

  Handler<RoutingContext> factory(A annotation, Modularized modularized);

}

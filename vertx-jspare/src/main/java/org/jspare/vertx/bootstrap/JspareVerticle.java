package org.jspare.vertx.bootstrap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.jspare.core.Environment;
import org.jspare.vertx.autoconfiguration.AutoConfigurationInitializer;

/**
 * Created by paulo.ferreira on 15/05/2017.
 */
public class JspareVerticle extends AbstractVerticle {

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    Environment.inject(this);
  }

  protected Future<Void> initAutoConfiguration() {

    return Environment.my(AutoConfigurationInitializer.class).initialize(this);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    initAutoConfiguration().setHandler(ar -> {
      if (ar.succeeded()) {

        try {
          start();
          startFuture.complete();
        } catch (Throwable t) {

          startFuture.fail(t);
        }
      } else {

        startFuture.fail(ar.cause());
      }
    });
  }
}

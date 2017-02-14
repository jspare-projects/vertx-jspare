package org.jspare.vertx.bootstrap;

import static org.jspare.core.container.Environment.my;

import org.jspare.core.container.Context;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class BootstrapVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    my(Context.class).put(EnvironmentUtils.VERTX_HOLDER, vertx);
    
    super.start(startFuture);
  }
}
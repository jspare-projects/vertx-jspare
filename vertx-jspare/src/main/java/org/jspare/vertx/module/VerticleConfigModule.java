package org.jspare.vertx.module;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jspare.core.Environment;
import org.jspare.core.internal.Bind;
import org.jspare.vertx.AbstractModule;
import org.jspare.vertx.Modularized;
import org.jspare.vertx.concurrent.FutureSupplier;

/**
 * Created by paulo.ferreira on 31/07/2017.
 */
public class VerticleConfigModule extends AbstractModule {

  public static final String NAME = "config";

  @Override
  public Future<Void> init(Modularized instance, JsonObject config) {
    return FutureSupplier.supply(v -> Environment.registry(Bind.bind(JsonObject.class).name(NAME), config));
  }
}

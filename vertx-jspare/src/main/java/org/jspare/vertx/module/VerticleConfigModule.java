package org.jspare.vertx.module;

import io.vertx.core.json.JsonObject;
import org.jspare.core.internal.Bind;
import org.jspare.vertx.AbstractModule;

import static org.jspare.core.Environment.registry;

/**
 * Created by paulo.ferreira on 31/07/2017.
 */
public class VerticleConfigModule extends AbstractModule {

  public static final String NAME = "_vertxMainConfig";

  @Override
  protected void load() {
    registry(Bind.bind(JsonObject.class).name(NAME), getConfig());
  }
}

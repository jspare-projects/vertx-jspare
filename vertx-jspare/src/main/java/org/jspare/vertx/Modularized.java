package org.jspare.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Created by paulo.ferreira on 31/07/2017.
 */
public interface Modularized {

  Vertx getVertx();

  void setConfig(JsonObject config);

  JsonObject getConfig();
}

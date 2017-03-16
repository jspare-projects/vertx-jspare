package org.jspare.vertx.json;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Value {
  private JsonObject value;

  public JsonObject getValue() {
    return value;
  }

  public void setValue(JsonObject value) {
    this.value = value;
  }
}

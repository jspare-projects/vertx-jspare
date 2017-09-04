package org.jspare.vertx.web.builder;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandlerMap {

  private List<Class<Handler<RoutingContext>>> defaultHandlersClassList = new ArrayList<>();
  private List<HandlerData> handlerDataList = new ArrayList<>();

  public void add(Class<Handler<RoutingContext>> clazz) {
    defaultHandlersClassList.add(clazz);
  }

  public void add(HandlerData handlerData) {
    handlerDataList.add(handlerData);
  }
}

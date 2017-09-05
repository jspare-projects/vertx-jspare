package org.jspare.vertx.web.builder;

import lombok.Getter;

import javax.annotation.Resource;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Handler Holder is used to hold Handlers mapped by RouterBuilder</p>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Resource
@Singleton
public class HandlerHolder {

  @Getter
  private Map<String, HandlerMap> handlerMap;

  public HandlerHolder() {
    this.handlerMap = new HashMap<>();
  }
}

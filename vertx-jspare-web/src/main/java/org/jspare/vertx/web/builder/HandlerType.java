/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.vertx.web.builder;

import org.jspare.vertx.web.annotation.handler.BlockingHandler;
import org.jspare.vertx.web.annotation.handler.FailureHandler;
import org.jspare.vertx.web.annotation.handler.Handler;
import org.jspare.vertx.web.annotation.handler.SockJsHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Enum HandlerType.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@AllArgsConstructor
public enum HandlerType {

  /** The handler. */
  HANDLER(Handler.class),
  /** The blocking handler. */
  BLOCKING_HANDLER(BlockingHandler.class),
  /** The failure handler. */
  FAILURE_HANDLER(FailureHandler.class),
  /** The socketjs handler. */
  SOCKETJS_HANDLER(SockJsHandler.class);

  /**
   * Gets the handler class.
   *
   * @return the handler class
   */
  @Getter
  private Class<?> handlerClass;
}
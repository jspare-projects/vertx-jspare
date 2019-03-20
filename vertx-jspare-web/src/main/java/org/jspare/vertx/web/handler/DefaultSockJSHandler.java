/*
 * Copyright 2016 Jspare.org.
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
package org.jspare.vertx.web.handler;

import java.lang.reflect.Parameter;

import org.jspare.vertx.web.annotation.handling.Header;
import org.jspare.vertx.web.builder.HandlerData;

import io.vertx.core.net.SocketAddress;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * Instantiates a new default sock JS handler.
 */
@UtilityClass
public class DefaultSockJSHandler {

  /**
   * Socket handler.
   *
   * @param handlerData
   *          the handler data
   * @param event
   *          the event
   */
  @SneakyThrows
  public void socketHandler(HandlerData handlerData, SockJSSocket event) {

    Object instance = handlerData.clazz().newInstance();

    setHandlingParameters(event, instance);

    Object[] parameters = collectParameters(handlerData, event);

    // Call method of handler data
    handlerData.method().invoke(instance, parameters);
  }

  /**
   * Collect parameters.
   *
   * @param handlerData
   *          the handler data
   * @param event
   *          the event
   * @return the object[]
   */
  @SneakyThrows
  private Object[] collectParameters(HandlerData handlerData, SockJSSocket event) {

    // Prepare parameters to call method of route
    Object[] parameters = new Object[handlerData.method().getParameterCount()];
    int i = 0;
    for (Parameter parameter : handlerData.method().getParameters()) {

      parameters[i] = resolveParameter(parameter, event);
      i++;
    }
    return parameters;
  }

  /**
   * Resolve parameter.
   *
   * @param parameter
   *          the parameter
   * @param event
   *          the event
   * @return the object
   */
  protected Object resolveParameter(Parameter parameter, SockJSSocket event) {

    if (parameter.getType().equals(SockJSSocket.class)) {

      return event;
    }

    if (parameter.isAnnotationPresent(Header.class)) {

      String headerName = parameter.getAnnotation(Header.class).value();
      return event.headers().get(headerName);
    }

    if (parameter.getType().equals(SocketAddress.class)) {

      return event.localAddress();
    }

    if (parameter.getType().equals(Session.class)) {

      return event.webSession();
    }

    if (parameter.getType().equals(User.class)) {

      return event.webUser();
    }
    return null;
  }

  /**
   * Sets the handling parameters.
   *
   * @param event
   *          the event
   * @param newInstance
   *          the new instance
   */
  protected void setHandlingParameters(SockJSSocket event, Object newInstance) {
    // If Route is handling by abstract Handling my some resources
    if (newInstance instanceof APIHandler) {

      ((APIHandler) newInstance).setSockJSEvent(event);
    }
  }
}

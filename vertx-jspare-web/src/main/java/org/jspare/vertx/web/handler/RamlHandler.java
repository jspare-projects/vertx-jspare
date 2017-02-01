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
package org.jspare.vertx.web.handler;

import java.util.List;

import org.jspare.vertx.web.builder.HandlerData;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

/**
 * Instantiates a new raml handler.
 *
 * @param listData
 *          the list data
 */
@RequiredArgsConstructor
public class RamlHandler implements Handler<RoutingContext> {

  /** The list data. */
  private final List<HandlerData> listData;

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.core.Handler#handle(java.lang.Object)
   */
  @Override
  public void handle(RoutingContext ctx) {

    HttpServerResponse response = ctx.response();
    StringBuilder ramlBuilder = new StringBuilder();
    listData.forEach(hd -> ramlBuilder.append(generateRow(hd)));
    response.setStatusCode(HttpResponseStatus.OK.code()).putHeader("Content-Type", "text/raml")
        .end(ramlBuilder.toString());
  }

  /**
   * Generate row.
   *
   * @param handlerData
   *          the handler data
   * @return the string
   */
  private String generateRow(HandlerData handlerData) {
    return "";
  }
}
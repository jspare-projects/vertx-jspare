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

import org.jspare.vertx.builder.AbstractBuilder;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * The Class HttpServerBuilder.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Accessors(fluent = true)

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(callSuper = false)
public class HttpServerBuilder extends AbstractBuilder<HttpServer> {

  /**
   * Creates the.
   *
   * @param vertx
   *          the vertx
   * @return the http server builder
   */
  public static HttpServerBuilder create(Vertx vertx) {

    return new HttpServerBuilder(vertx);
  }

  /** The vertx. */
  private final Vertx vertx;

  /**
   * Http server.
   *
   * @return the http server
   */
  @Getter

  /**
   * Http server.
   *
   * @param httpServer
   *          the http server
   * @return the http server builder
   */
  @Setter
  private HttpServer httpServer;

  /**
   * Http server options.
   *
   * @return the http server options
   */
  @Getter

  /**
   * Http server options.
   *
   * @param httpServerOptions
   *          the http server options
   * @return the http server builder
   */
  @Setter
  private HttpServerOptions httpServerOptions;

  /**
   * Router.
   *
   * @return the router
   */
  @Getter

  /**
   * Router.
   *
   * @param router
   *          the router
   * @return the http server builder
   */
  @Setter
  private Router router;

  /**
   * Instantiates a new http server builder.
   *
   * @param vertx
   *          the vertx
   */
  private HttpServerBuilder(Vertx vertx) {

    this.vertx = vertx;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jspare.vertx.builder.AbstractBuilder#build()
   */
  @Override
  public HttpServer build() {

    if (httpServerOptions == null) {

      httpServerOptions = new HttpServerOptions();
    }

    HttpServer httpServer = vertx.createHttpServer(httpServerOptions);

    if (router != null) {

      httpServer.requestHandler(router::accept);
    }

    return httpServer;
  }
}
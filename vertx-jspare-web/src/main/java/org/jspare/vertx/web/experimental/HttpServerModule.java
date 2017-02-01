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
package org.jspare.vertx.web.experimental;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.jspare.vertx.experimental.Configurable;
import org.jspare.vertx.experimental.Module;
import org.jspare.vertx.web.builder.HttpServerBuilder;
import org.jspare.vertx.web.builder.RouterBuilder;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import lombok.SneakyThrows;

/**
 * The Class HttpServerModule.
 * <p>
 * Used for load {@link HttpServer } and endpoints to simple rest api.
 * </p>
 * 
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Module(HttpServerModule.NAME)
public class HttpServerModule implements Configurable {

  /** The Constant NAME. */
  public static final String NAME = "httpServer";

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jspare.vertx.experimental.Configurable#execute(io.vertx.core.Verticle,
   * java.lang.String[])
   */
  @Override
  public void execute(Verticle verticle, String[] args) {

    final Vertx vertx = verticle.getVertx();
    HttpServerOptions options = getOptions(verticle);
    Router router = getRouter(verticle);

    HttpServer server = HttpServerBuilder.create(vertx).httpServerOptions(options).router(router).build();
    server.listen();
  }

  /**
   * Gets the options.
   *
   * @param verticle
   *          the verticle
   * @return the options
   */
  @SneakyThrows
  private HttpServerOptions getOptions(Verticle verticle) {

    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods()).stream()
        .filter(m -> HttpServerOptions.class.equals(m.getReturnType())).findFirst();
    if (oMethod.isPresent() && oMethod.get().getParameterCount() == 0) {
      Method method = oMethod.get();
      method.setAccessible(true);
      return (HttpServerOptions) method.invoke(verticle);
    }

    return new HttpServerOptions(verticle.getVertx().getOrCreateContext().config());
  }

  /**
   * Gets the router.
   *
   * @param verticle
   *          the verticle
   * @return the router
   */
  @SneakyThrows
  private Router getRouter(Verticle verticle) {

    Optional<Method> oMethod = Arrays.asList(verticle.getClass().getDeclaredMethods()).stream()
        .filter(m -> Router.class.equals(m.getReturnType())).findFirst();
    if (oMethod.isPresent() && oMethod.get().getParameterCount() == 0) {
      Method method = oMethod.get();
      method.setAccessible(true);
      return (Router) method.invoke(verticle);
    }

    Router router = RouterBuilder.create(verticle.getVertx())
        .addHandler(SessionHandler.create(LocalSessionStore.create(verticle.getVertx())))
        .addHandler(CookieHandler.create()).addHandler(BodyHandler.create()).addHandler(ResponseTimeHandler.create())
        .scanClasspath(true).build();

    return router;
  }

}
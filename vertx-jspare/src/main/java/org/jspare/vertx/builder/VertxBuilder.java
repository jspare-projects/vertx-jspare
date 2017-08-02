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
package org.jspare.vertx.builder;


import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jspare.vertx.cdi.EnvironmentLoader;

import java.util.function.Consumer;

/**
 * The Class VertxBuilder.
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
public class VertxBuilder extends AbstractBuilder<Future<Vertx>> {

  /**
   * Creates the.
   *
   * @return the vertx builder
   */
  public static VertxBuilder create() {

    return new VertxBuilder();
  }

  /**
   * Creates the.
   *
   * @param vertxOptions
   *          the vertx options
   * @return the vertx builder
   */
  public static VertxBuilder create(VertxOptions vertxOptions) {

    return new VertxBuilder().options(vertxOptions);
  }

  /**
   * Name.
   *
   * @return the string
   */
  @Getter

  /**
   * Name.
   *
   * @param name
   *          the name
   * @return the vertx builder
   */
  @Setter
  private String name;

  /**
   * Vertx.
   *
   * @return the vertx
   */
  @Getter

  /**
   * Vertx.
   *
   * @param vertx
   *          the vertx
   * @return the vertx builder
   */
  @Setter
  private Vertx vertx;

  /**
   * Options.
   *
   * @return the vertx options
   */
  @Getter

  /**
   * Options.
   *
   * @param options
   *          the options
   * @return the vertx builder
   */
  @Setter
  private VertxOptions options;

  /**
   * Instantiates a new vertx builder.
   */
  private VertxBuilder() {
  }

  /*
   * (non-Javadoc)
   *
   * @see org.jspare.vertx.builder.AbstractBuilder#build()
   */
  @Override
  public Future<Vertx> build() {

    Future<Vertx> future = Future.future();

    // Load vertx instance
    Consumer<Vertx> runner = vertx -> {

      // Registry vertx
      this.vertx = vertx;

      future.complete(vertx);
    };

    if (vertx != null) {

      runner.accept(vertx);
    } else {

      createVertx(runner);
    }

    // Register vertx on VertxHolder. This interaction allow that the Vertx
    // can be accessed internally by application.
   EnvironmentLoader.bindInterfaces(vertx);

    return future;
  }

  /**
   * Creates the vertx.
   *
   * @param runner
   *          the runner
   */
  protected void createVertx(Consumer<Vertx> runner) {

    if (options == null) {

      options = new VertxOptions();
    }

    if (options.isClustered()) {

      Vertx.clusteredVertx(options, res -> {
        if (res.succeeded()) {
          Vertx vertx = res.result();
          runner.accept(vertx);
        } else {
          res.cause().printStackTrace();
        }
      });
    } else {

      runner.accept(Vertx.vertx(options));
    }
  }
}

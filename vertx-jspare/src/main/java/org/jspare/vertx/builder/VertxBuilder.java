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

import static org.jspare.core.container.Environment.registryResource;

import java.util.function.Consumer;

import org.jspare.vertx.bootstrap.VertxHolder;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class VertxBuilder extends AbstractBuilder<Future<Vertx>> {

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public static VertxBuilder create(VertxOptions vertxOptions) {

		return new VertxBuilder().options(vertxOptions);
	}

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private Vertx vertx;

	@Getter
	@Setter
	private VertxOptions options;

	private VertxBuilder() {
	}

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

		// Register vertx on VertxHolder. This interaction allow that the Vertx can be accessed internally by application.
		registryResource(new VertxHolder().vertx(vertx));

		return future;
	}

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
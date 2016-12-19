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

@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class HttpServerBuilder extends AbstractBuilder<HttpServer> {

	public static HttpServerBuilder create(Vertx vertx) {

		return new HttpServerBuilder(vertx);
	}

	private final Vertx vertx;

	@Getter
	@Setter
	private HttpServer httpServer;

	@Getter
	@Setter
	private HttpServerOptions httpServerOptions;

	@Getter
	@Setter
	private Router router;

	private HttpServerBuilder(Vertx vertx) {

		this.vertx = vertx;
	}

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
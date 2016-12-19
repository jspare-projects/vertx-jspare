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
package org.jspare.vertx.web.builder.route;

import org.jspare.vertx.web.annotation.handler.Handler;
import org.jspare.vertx.web.annotation.method.Get;
import org.jspare.vertx.web.annotation.subrouter.IgnoreSubRouter;
import org.jspare.vertx.web.annotation.subrouter.SubRouter;

import io.vertx.ext.web.RoutingContext;

@SubRouter("/subRouter")
public class SampleSubRouter {

	@Handler
	@Get("/sub/1")
	public void sub1(RoutingContext routingContext) {

		routingContext.response().write("/subRouter/sub/1");
		routingContext.response().end();
	}

	@Handler
	@Get("/sub/2")
	public void sub2(RoutingContext routingContext) {

		routingContext.response().write("/subRouter/sub/2");
		routingContext.response().end();
	}

	@IgnoreSubRouter
	@Handler
	@Get("/sub/3")
	public void sub3(RoutingContext routingContext) {

		routingContext.response().write("sub/3");
		routingContext.response().end();
	}
}
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

import org.jspare.vertx.web.annotation.content.Consumes;
import org.jspare.vertx.web.annotation.content.Produces;
import org.jspare.vertx.web.annotation.handler.BlockingHandler;
import org.jspare.vertx.web.annotation.handler.Handler;
import org.jspare.vertx.web.annotation.method.Get;
import org.jspare.vertx.web.handling.Handling;

public class MultiRoutes extends Handling {

	@Handler
	@Get("/multiRoutes/1")
	public void handler1() {

		res.write("multiRoutes 1\n");
		ctx.next();
	}

	@Consumes("*/*")
	@Produces("text/plain")
	@BlockingHandler(order = 1)
	@Get(value = "/multiRoutes/2", regex = true)
	public void handler2() {

		res.write("multiRoutes 2\n");
		res.end();
	}
}
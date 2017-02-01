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
import org.jspare.vertx.web.handler.APIHandler;

/**
 * The Class StartRoute.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Get("/start")
public class StartRoute extends APIHandler {

	/**
	 * Route 1.
	 */
	@Handler
	public void route1() {

		res.write("route 1\n");
		context.next();
	}

	/**
	 * Route 2.
	 */
	@Handler
	public void route2() {

		res.write("route 2\n");
		res.end();
	}
}
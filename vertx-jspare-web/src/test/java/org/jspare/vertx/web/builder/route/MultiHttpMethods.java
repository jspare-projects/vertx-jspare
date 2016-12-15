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
import org.jspare.vertx.web.annotation.method.All;
import org.jspare.vertx.web.annotation.method.Connect;
import org.jspare.vertx.web.annotation.method.Delete;
import org.jspare.vertx.web.annotation.method.Get;
import org.jspare.vertx.web.annotation.method.Head;
import org.jspare.vertx.web.annotation.method.Options;
import org.jspare.vertx.web.annotation.method.Other;
import org.jspare.vertx.web.annotation.method.Path;
import org.jspare.vertx.web.annotation.method.Post;
import org.jspare.vertx.web.annotation.method.Put;
import org.jspare.vertx.web.annotation.method.Trace;
import org.jspare.vertx.web.handling.Handling;

public class MultiHttpMethods extends Handling {
	@All
	@Connect
	@Delete
	@Get
	@Post
	@Head
	@Options
	@Other
	@Path
	@Put
	@Trace
	@Handler
	public void handler1() {
	}

	@All
	@Connect
	@Delete
	@Get
	@Post
	@Head
	@Options
	@Other
	@Path
	@Put
	@Trace
	@Handler
	public void handler2() {
	}
}
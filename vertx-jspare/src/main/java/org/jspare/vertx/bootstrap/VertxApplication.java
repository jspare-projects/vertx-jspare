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
package org.jspare.vertx.bootstrap;

import org.jspare.core.bootstrap.Application;
import org.jspare.core.bootstrap.EnvironmentBuilder;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.builder.VertxBuilder;
import org.jspare.vertx.injector.VertxInjectStrategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;

public abstract class VertxApplication extends Application {

	@Override
	public void start() {
		
		vertx().setHandler(res -> {
			if(res.failed()) throw new RuntimeException("Failed to create Vert.x instance");
			start(res.result());
		});
	}

	@Override
	protected void setup() {

		// Prepare Environment with VertxInject
		builder(EnvironmentBuilder.create().addInjector(VertxInject.class, new VertxInjectStrategy()));
		
		// Set default Json Mapper options
		Json.mapper
			.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
			.setVisibility(PropertyAccessor.ALL, Visibility.ANY)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.findAndRegisterModules();
	}

	protected void start(Vertx vertx) {
	}

	protected Future<Vertx> vertx() {

		return VertxBuilder.create().build();
	}
}
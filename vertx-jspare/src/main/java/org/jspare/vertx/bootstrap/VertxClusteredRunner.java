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

import static org.jspare.core.container.Environment.registryResource;

import org.jspare.core.bootstrap.EnvironmentBuilder;
import org.jspare.core.bootstrap.Runner;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.annotation.VertxProxyInject;
import org.jspare.vertx.builder.VertxBuilder;
import org.jspare.vertx.injector.VertxInjectStrategy;
import org.jspare.vertx.injector.VertxProxyInjectStrategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.spi.cluster.ClusterManager;

/**
 * The Class VertxClusteredRunner.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public abstract class VertxClusteredRunner extends AbstractVerticle implements Runner {

	/* (non-Javadoc)
	 * @see org.jspare.core.bootstrap.Runner#run()
	 */
	@Override
	public void run() {

		setup();

		mySupport();

		vertx().setHandler(res -> {

			if (res.succeeded()) {

				registryResource(new VertxHolder().vertx(vertx));
			} else {

				throw new RuntimeException("Failed to create Vert.x instance");
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.jspare.core.bootstrap.Runner#setup()
	 */
	@Override
	public void setup() {

		// Prepare Environment with VertxInject
		EnvironmentBuilder.create().addInjector(VertxInject.class, new VertxInjectStrategy()).build();
		EnvironmentBuilder.create().addInjector(VertxProxyInject.class, new VertxProxyInjectStrategy()).build();

		// Set default Json Mapper options
		Json.mapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
				.setVisibility(PropertyAccessor.ALL, Visibility.ANY).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).findAndRegisterModules();
	}

	/**
	 * Cluster manager.
	 *
	 * @return the cluster manager
	 */
	protected abstract ClusterManager clusterManager();

	/**
	 * Vertx.
	 *
	 * @return the future
	 */
	protected Future<Vertx> vertx() {

		VertxOptions options = new VertxOptions();
		options.setClustered(true);
		options.setClusterManager(clusterManager());

		return VertxBuilder.create().options(options).build().compose(vertx -> {

			vertx.deployVerticle(this);
		}, Future.succeededFuture());
	}
}
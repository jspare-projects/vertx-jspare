package org.jspare.vertx.bootstrap;

import org.jspare.core.bootstrap.Application;
import org.jspare.core.bootstrap.EnvironmentBuilder;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.builder.VertxBuilder;
import org.jspare.vertx.injector.VertxInjectStrategy;

import io.vertx.core.Vertx;

public abstract class VertxApplication extends Application {

	@Override
	protected void setup() {

		builder(EnvironmentBuilder.create().addInjector(VertxInject.class, new VertxInjectStrategy()));
	}

	@Override
	public void start() {
		start(vertx());
	}

	protected Vertx vertx() {

		return VertxBuilder.create().source(this).build();
	}

	protected void start(Vertx vertx) {
	}
}
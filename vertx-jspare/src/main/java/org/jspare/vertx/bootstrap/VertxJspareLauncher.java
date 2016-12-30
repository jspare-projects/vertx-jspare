package org.jspare.vertx.bootstrap;

import static org.jspare.core.container.Environment.my;

import org.jspare.core.bootstrap.EnvironmentBuilder;
import org.jspare.core.bootstrap.Runner;
import org.jspare.core.container.Context;
import org.jspare.vertx.annotation.ProxyHandler;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.injector.ProxyHandlerStrategy;
import org.jspare.vertx.injector.VertxInjectStrategy;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;

public class VertxJspareLauncher extends Launcher implements Runner{
	
	@Override
	public void afterStartingVertx(Vertx vertx) {
	
		my(Context.class).put("vertx:main", vertx);
		
		run();
	}
	
	@Override
	public void beforeStartingVertx(VertxOptions options) {

		setup();

		mySupport();
	}
	
	@Override
	public void setup() {

		// Prepare Jspare Container Environment
		// Register VertxInject and hold vertx
		EnvironmentBuilder.create().addInjector(VertxInject.class, new VertxInjectStrategy()).build();
		EnvironmentBuilder.create().addInjector(ProxyHandler.class, new ProxyHandlerStrategy()).build();

		// Set default Json Mapper options
		Json.mapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
			.setVisibility(PropertyAccessor.ALL, Visibility.ANY)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.findAndRegisterModules();
	}
}
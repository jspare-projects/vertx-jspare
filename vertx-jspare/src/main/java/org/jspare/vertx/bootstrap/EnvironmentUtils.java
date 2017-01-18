package org.jspare.vertx.bootstrap;

import org.jspare.core.bootstrap.EnvironmentBuilder;
import org.jspare.vertx.annotation.VertxProxyInject;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.injector.VertxProxyInjectStrategy;
import org.jspare.vertx.injector.VertxInjectStrategy;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

import io.vertx.core.json.Json;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvironmentUtils {

	protected void register() {

		// Prepare Environment with VertxInject
		EnvironmentBuilder.create().addInjector(VertxInject.class, new VertxInjectStrategy()).build();
		EnvironmentBuilder.create().addInjector(VertxProxyInject.class, new VertxProxyInjectStrategy()).build();

		// Set default Json Mapper options
		Json.mapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector())
			.setVisibility(PropertyAccessor.ALL, Visibility.ANY)
			.setSerializationInclusion(Include.NON_NULL)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.findAndRegisterModules();
	}
}

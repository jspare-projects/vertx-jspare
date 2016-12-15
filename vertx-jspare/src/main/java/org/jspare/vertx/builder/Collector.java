package org.jspare.vertx.builder;

public interface Collector<T> {
	
	T  collect(Class<?> source, Object... args);
}
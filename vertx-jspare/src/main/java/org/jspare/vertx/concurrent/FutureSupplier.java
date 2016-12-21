package org.jspare.vertx.concurrent;

import java.util.function.Supplier;

import io.vertx.core.Future;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FutureSupplier {
	
	public <T> Future<T> supply(Supplier<T> event){

		Future<T> future = Future.future();
		try {

			future.complete(event.get());
		} catch (Throwable e) {

			future.fail(e);
		}
		return future;
	}
}

/**
 * Copyright 2016 Senior Sistemas.
 *
 * Software sob Medida
 *
 */
package org.jspare.vertx.unit.mock;

import io.vertx.core.Future;
import org.jspare.unit.mock.Mocked;

/**
 * The Interface AsyncMocked.
 */
public interface AsyncMocked extends Mocked {

	/**
	 * Fix return.
	 *
	 * @param methodName
	 *            the method name
	 * @param result
	 *            the result
	 */
	void fixReturn(String methodName, Object result);

  <T> void fixAsyncHandler(String methodName, Future<T> futureResult);
}

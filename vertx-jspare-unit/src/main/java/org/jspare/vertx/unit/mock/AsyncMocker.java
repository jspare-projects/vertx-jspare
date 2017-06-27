/**
 * Copyright 2016 Senior Sistemas.
 * <p>
 * Software sob Medida
 */
package org.jspare.vertx.unit.mock;

import io.vertx.core.Future;
import org.jspare.unit.mock.Mocked;
import org.jspare.unit.mock.Mocker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The Class AsyncMocker.
 */
public abstract class AsyncMocker extends Mocker {

  /**
   * Do return.
   *
   * @param mocked
   *            the mocked
   * @param methodName
   *            the method name
   * @param futureResult
   *            the result
   */
  public static <T> void whenAsyncResultHandler(Object mocked, String methodName, Supplier<Future<T>> futureResult) {
    ((AsyncMocked) mocked).fixAsyncHandler(methodName, futureResult.get());
  }

  /**
   * Creates the proxy.
   *
   * @param <T>
   *            the generic type
   * @param interfaceClass
   *            the interface class
   * @return the t
   */
  @SuppressWarnings("unchecked")
  public static <T> T createProxy(Class<T> interfaceClass) {

    MOCKED.put(interfaceClass, new HashMap<>());

    return (T) Proxy.newProxyInstance(Mocker.class.getClassLoader(), new Class[]{interfaceClass, Mocked.class},
      new InvocationHandler() {

        private Class<T> clazz = interfaceClass;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

          if (method.getName().equals("fixReturn")) {
            MOCKED.get(clazz).put((String) args[0], args[1]);
            return null;
          }

          Object result = null;

          if (MOCKED.get(clazz).containsKey(method.getName())) {
            result = MOCKED.get(clazz).get(method.getName());
            return result;
          }

          return result;
        }
      });
  }
}

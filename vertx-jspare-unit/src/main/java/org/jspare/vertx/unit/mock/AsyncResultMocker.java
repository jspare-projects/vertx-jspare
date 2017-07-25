package org.jspare.vertx.unit.mock;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by paulo.ferreira on 25/07/2017.
 */
public class AsyncResultMocker {

  public static Function<Object[], Object> mock(Consumer<Future> supplier) {

    return (args) -> {
      if (args.length < 1) return null;

      int lastArgument = args.length - 1;
      try {
        Future future = Future.future();
        Handler<AsyncResult> hAr = (Handler<AsyncResult>) args[lastArgument];
        supplier.accept(future);
        hAr.handle(future);
      } catch (ClassCastException e) {
        throw new RuntimeException(e);
      }
      return null;
    };
  }

  public static Function<Object[], Object> mockSucceeded(Object[] args) {
    return mock(Future::succeededFuture);
  }

  public static Function<Object[], Object> mockSucceeded(Object result) {
    return mock((f) -> f.complete());
  }

  public static Function<Object[], Object> mockFailed(Object[] args) {
    return mock((f) -> f.fail(new Exception()));
  }

  public static Function<Object[], Object> mockFailed(Exception e) {
    return mock((f) -> f.fail(e));
  }
}

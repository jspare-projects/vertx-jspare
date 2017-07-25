package org.jspare.vertx.unit;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import org.jspare.unit.mock.Mock;
import org.jspare.unit.mock.Mocker;
import org.jspare.vertx.unit.ext.junit.VertxJspareUnitRunner;
import org.jspare.vertx.unit.mock.AsyncResultMocker;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Created by paulo.ferreira on 25/07/2017.
 */
@RunWith(VertxJspareUnitRunner.class)
public class UsageWithEnvironmentTest {

  @Mock
  @Inject
  private SomeService someService;

  @Test
  public void testSuccessAssertion(TestContext context) {

    Mocker.whenReturn(someService, "test", args -> {
      int code = (int) args[0];
      Handler<AsyncResult<?>> hAr = (Handler<AsyncResult<?>>) args[1];
      if (code == 0) {
        hAr.handle(Future.succeededFuture());
      } else if (code == 1) {
        hAr.handle(Future.failedFuture("erro"));
      }
      return null;
    });
    someService.test(0, ar -> context.assertTrue(ar.succeeded()));
  }

  @Test
  public void testFalseAssertion(TestContext context) {

    Mocker.whenReturn(someService, "test", args -> {
      int code = (int) args[0];
      Handler<AsyncResult<?>> hAr = (Handler<AsyncResult<?>>) args[1];
      if (code == 0) {
        hAr.handle(Future.succeededFuture());
      } else if (code == 1) {
        hAr.handle(Future.failedFuture("erro"));
      }
      return null;
    });
    someService.test(1, ar -> context.assertFalse(ar.succeeded()));
  }

  @Test
  public void testSuccessAssertionWithHelper(TestContext context) {

    Mocker.whenReturn(someService, "test", AsyncResultMocker::mockSucceeded);
    someService.test(0, ar -> context.assertTrue(ar.succeeded()));
  }

  @Test
  public void testFailedAssertionWithHelper(TestContext context) {

    Mocker.whenReturn(someService, "test", AsyncResultMocker::mockFailed);
    someService.test(0, ar -> context.assertTrue(ar.failed()));
  }

  @Test
  public void testFailedNpeAssertionWithHelper(TestContext context) {

    Mocker.whenReturn(someService, "test", AsyncResultMocker.mockFailed(new NullPointerException()));
    someService.test(0, ar -> context.assertTrue(ar.cause() instanceof NullPointerException));
  }

  private interface SomeService {

    void test(int code, Handler<AsyncResult<Void>> resultHandler);
  }
}

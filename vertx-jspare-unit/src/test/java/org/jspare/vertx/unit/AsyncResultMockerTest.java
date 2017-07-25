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
public class AsyncResultMockerTest {

  @Mock
  @Inject
  private SomeService someService;

  @Test
  public void mockTest(TestContext context) {
    Mocker.whenReturn(someService, "test", AsyncResultMocker.mock(Future::complete));
    someService.test(ar -> context.assertTrue(ar.succeeded()));
  }

  @Test
  public void mockSucceededTest(TestContext context) {
    Mocker.whenReturn(someService, "test", AsyncResultMocker::mockSucceeded);
    someService.test(ar -> context.assertTrue(ar.succeeded()));
  }

  @Test
  public void mockSucceededWithResultTest(TestContext context) {
    Mocker.whenReturn(someService, "test", AsyncResultMocker.mockSucceeded("success"));
    someService.test(ar -> {
      context.assertTrue(ar.succeeded());
      context.assertEquals("success", ar.result());
    });
  }

  @Test
  public void mockFailedTest(TestContext context) {
    Mocker.whenReturn(someService, "test", AsyncResultMocker::mockFailed);
    someService.test(ar -> context.assertTrue(ar.failed()));
  }

  @Test
  public void mockFailedWithThrowableTest(TestContext context) {
    Mocker.whenReturn(someService, "test", AsyncResultMocker.mockFailed(new NullPointerException()));
    someService.test(ar -> context.assertTrue(ar.cause() instanceof NullPointerException));
  }

  private interface SomeService {

    void test(Handler<AsyncResult<String>> resultHandler);
  }
}

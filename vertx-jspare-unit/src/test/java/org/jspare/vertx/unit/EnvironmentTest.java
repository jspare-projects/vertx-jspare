package org.jspare.vertx.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import org.jspare.vertx.unit.ext.junit.VertxJspareUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Created by paulo.ferreira on 25/07/2017.
 */
@RunWith(VertxJspareUnitRunner.class)
public class EnvironmentTest {

  @Inject
  private Vertx vertx;

  @Test
  public void coreInjectionTest(TestContext testContext) {
    testContext.assertNotNull(vertx);
  }
}

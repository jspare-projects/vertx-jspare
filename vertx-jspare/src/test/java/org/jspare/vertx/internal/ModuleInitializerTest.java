package org.jspare.vertx.internal;

import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.unit.TestContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspare.core.Environment;
import org.jspare.vertx.AbstractModule;
import org.jspare.vertx.annotation.Module;
import org.jspare.vertx.annotation.Modules;
import org.jspare.vertx.unit.ext.junit.VertxJspareUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by paulo.ferreira on 01/08/2017.
 */
@Slf4j
@RunWith(VertxJspareUnitRunner.class)
public class ModuleInitializerTest {

  protected final AtomicInteger count = new AtomicInteger();
  @Inject
  private ModuleInitializer initializer;

  @Test
  public void initializationTest(TestContext ctx) {

    ModuleOne m1 = new ModuleOne();
    m1.setConfig(new JsonObject());

    initializer.initialize(m1).setHandler(ar -> {

      ctx.assertTrue(ar.succeeded());

      SharedData sd = Environment.my(SharedData.class);
      sd.getCounter("c1", c -> {
        c.result().get(ar1 -> {
          ctx.assertEquals(2l, ar1.result());
        });
      });
    });
  }

  @Modules({
    @Module(SubModuleOne.class),
    @Module(SubModuleTwo.class)
  })
  class ModuleOne extends AbstractModule {
  }

  @NoArgsConstructor
  class SubModuleOne extends AbstractModule {

    @Override
    protected void load() {
      log.debug(getClass().getSimpleName());
      SharedData sd = Environment.my(SharedData.class);
      sd.getCounter("c1", c -> {
        c.result().addAndGet(1, ar -> {
        });
      });
    }
  }

  @NoArgsConstructor
  class SubModuleTwo extends AbstractModule {

    @Override
    protected void load() {
      log.debug(getClass().getSimpleName());
      SharedData sd = Environment.my(SharedData.class);
      sd.getCounter("c1", c -> {
        c.result().addAndGet(1, ar -> {
        });
      });
    }
  }


}

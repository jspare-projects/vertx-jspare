package org.jspare.vertx;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.jspare.core.MySupport;
import org.jspare.core.internal.Bind;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * A support class for {@link Module}s which reduces repetition and results in a more readable
 * configuration. Simply extend this class, implement {@link #load()}, and call the inherited
 * methods which mirror those found in {@link org.jspare.core.internal.Bind}. For example:
 *
 * <pre>
 * public class MyModule extends AbstractModule {
 *   protected void init() {
 *     bind(Service.class).to(ServiceImpl.class).registry();
 *     bind(CreditCardPaymentService.class).registry();
 *     bind(PaymentService.class).to(CreditCardPaymentService.class).registry();
 *     bindConstant("name", "Paulo");
 *   }
 * }
 * </pre>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public abstract class AbstractModule extends MySupport implements Module {

  @Inject
  @Getter
  private Vertx vertx;

  @Getter
  private Modularized instance;

  @Getter
  @Setter
  private JsonObject config;

  public AbstractModule() {
  }

  public Future<Void> init(Modularized instance, JsonObject config) {
    this.instance = instance;
    this.config = config;
    Future<Void> future = Future.future();
    loadAsync(future);
    return future;
  }

  protected void loadAsync(Future<Void> future) {
    try {

      load();
      future.complete();
    } catch (Throwable t) {

      future.fail(t);
    }
  }

  protected void load() {
  }

  protected <T> Bind<T> bind(Class<T> clazz) {
    return Bind.bind(clazz);
  }

  protected void bindConstant(String name, String value) {
    bind(String.class).name(name).registry(value);
  }

  protected <T> void hookIfPresent(Class<? extends Annotation> ann, Handler<T> execute) {

    if (getClass().isAnnotationPresent(ann)) {

      Object instance = getClass().getAnnotation(ann);
      execute.handle((T) instance);
    }
  }
}

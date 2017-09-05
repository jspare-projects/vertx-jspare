package org.jspare.vertx;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspare.core.MySupport;
import org.jspare.core.internal.Bind;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * A support class for {@link Module}s which reduces repetition and results in a more readable
 * configuration. Simply extend this class, implement {@link #load()}, and call the inherited
 * methods which mirror those found in {@link org.jspare.core.internal.Bind}. For example:
 * <p>
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
@Slf4j
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
    try {

      loadAsync(future);
    } catch (Throwable t) {

      log.error("Failed to load module [{}] - {}", getInstance().getClass().getName(), t.getMessage(), t);
      log.trace(t.getMessage(), t);
      if(!future.isComplete()){

        future.fail(t);
      }
    }
    return future;
  }

  protected void loadAsync(Future<Void> future) {
    load();
    future.complete();
  }

  protected void load() {
  }

  protected <T> Bind<T> bind(Class<T> clazz) {
    return Bind.bind(clazz);
  }

  protected void bindConstant(String name, String value) {
    bind(String.class).name(name).registry(value);
  }

  /**
   * Use instead doHookIfPresent.
   *
   * @param ann     the annotation
   * @param execute the handler
   * @param <T>     the type
   */
  @Deprecated
  protected <T> void hookIfPresent(Class<T> ann, Handler<T> execute) {

    if (instance.getClass().isAnnotationPresent((Class<? extends Annotation>) ann)) {

      Class<? extends Annotation> annClass = (Class<? extends Annotation>) ann;
      Object instance = getClass().getAnnotation(annClass);
      execute.handle((T) instance);
    }
  }

  /**
   * Execute one hook if is present.
   *
   * @param ann     the annotation
   * @param execute the bi consumer
   * @param <T>     the type
   */
  protected <T> void doHookIfPresent(Class<T> ann, BiConsumer<AnnotatedElement, T> execute) {

    Class<? extends Annotation> annClass = (Class<? extends Annotation>) ann;
    Target target = annClass.getAnnotation(Target.class);

    if (isCheckType(target, ElementType.TYPE)) executeHookType(annClass, execute);
    if (isCheckType(target, ElementType.METHOD)) executeHookMethods(annClass, execute);
  }

  /**
   * Execute one hook if is present.
   *
   * @param ann     the annotation
   * @param execute the handler
   * @param <T>     the type
   */
  protected <T> void doHookIfPresent(Class<T> ann, Handler<T> execute) {

    Class<? extends Annotation> annClass = (Class<? extends Annotation>) ann;
    Target target = annClass.getAnnotation(Target.class);

    if (isCheckType(target, ElementType.TYPE)) {
      executeHookType(annClass, (a, t) -> execute.handle((T) t));
    }

    if (isCheckType(target, ElementType.METHOD)) {
      executeHookMethods(annClass, (a, t) -> execute.handle((T) t));
    }
  }

  /**
   * Check has class contains one annotation type
   *
   * @param annElement the ann element
   * @param annClass   the ann class
   * @return boolean
   */
  protected boolean hasAnnotationInType(AnnotatedElement annElement, Class<? extends Annotation> annClass) {
    return annElement.isAnnotationPresent(annClass);
  }

  private boolean isCheckType(Target target, ElementType type) {
    return target != null ? Arrays.asList(target.value()).contains(type) : false;
  }

  private <T> void executeHookMethods(Class<? extends Annotation> annClass, BiConsumer<AnnotatedElement, T> biConsumer) {

    Arrays.stream(instance.getClass().getDeclaredMethods()).forEach(m -> {

      if (hasAnnotationInType(m, annClass)) {
        Object instance = m.getAnnotation(annClass);
        biConsumer.accept(m, (T) instance);
      }
    });
  }

  private <T> void executeHookType(Class<? extends Annotation> annClass, BiConsumer<AnnotatedElement, T> biConsumer) {
    if (hasAnnotationInType(instance.getClass(), annClass)) {
      Object result = instance.getClass().getAnnotation(annClass);
      biConsumer.accept(getClass(), (T) result);
    }
  }
}

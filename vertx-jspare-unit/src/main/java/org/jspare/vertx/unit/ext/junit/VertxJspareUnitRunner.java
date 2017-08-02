package org.jspare.vertx.unit.ext.junit;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.Helper;
import io.vertx.ext.unit.impl.TestContextImpl;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.jspare.core.Environment;
import org.jspare.unit.mock.MockerUtils;
import org.jspare.vertx.cdi.EnvironmentLoader;
import org.junit.*;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runners.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * A JUnit runner for writing asynchronous tests with Vertx and Jspare Environment.
 * <p>
 * Note : a runner is needed because when a rule statement is evaluated, it will run the before/test/after
 * method and then test method is executed even if there are pending Async objects in the before
 * method. The runner gives this necessary fine grained control.
 * <p>
 * Created by paulo.ferreira on 23/06/2017.
 */
public class VertxJspareUnitRunner extends VertxUnitRunner {

  private static final ThreadLocal<VertxUnitRunner> currentRunner = new ThreadLocal<>();
  private static final LinkedList<Context> contextStack = new LinkedList<>();
  private static final LinkedList<Long> timeoutStack = new LinkedList<>();
  private final TestClass testClass;
  private Map<String, Object> classAttributes = new HashMap<>();
  private TestContextImpl testContext;

  public VertxJspareUnitRunner(Class<?> klass) throws InitializationError {
    super(klass);
    this.testClass = new TestClass(klass);
    setup();
  }

  static void pushContext(Context context) {
    contextStack.push(context);
  }

  static void popContext() {
    contextStack.pop();
  }

  static void pushTimeout(long timeout) {
    timeoutStack.push(timeout);
  }

  static void popTimeout() {
    timeoutStack.pop();
  }

  private void setup() {

    EnvironmentLoader.setup();
    EnvironmentLoader.bindInterfaces(Vertx.vertx());
    MockerUtils.initialize(this.testClass.getJavaClass());
  }

  @Override
  protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
    if (annotation == Test.class || annotation == Before.class || annotation == After.class ||
      annotation == BeforeClass.class || annotation == AfterClass.class) {
      List<FrameworkMethod> fMethods = getTestClass().getAnnotatedMethods(annotation);
      for (FrameworkMethod fMethod : fMethods) {
        fMethod.validatePublicVoid(isStatic, errors);
        try {
          validateTestMethod(fMethod);
        } catch (Exception e) {
          errors.add(e);
        }
      }
    } else {
      super.validatePublicVoidNoArgMethods(annotation, isStatic, errors);
    }
  }

  protected void validateTestMethod(FrameworkMethod fMethod) throws Exception {
    Class<?>[] paramTypes = fMethod.getMethod().getParameterTypes();
    if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(TestContext.class)))) {
      throw new Exception("Method " + fMethod.getName() + " should have no parameters or " +
        "the " + TestContext.class.getName() + " parameter");
    }
  }

  @Override
  protected Statement methodInvoker(FrameworkMethod method, Object test) {
    TestContextImpl ctx = testContext;
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        invokeExplosively(ctx, method, test);
      }
    };
  }

  protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context) throws InvocationTargetException, IllegalAccessException {
    Method method = fMethod.getMethod();
    Class<?>[] paramTypes = method.getParameterTypes();
    if (paramTypes.length == 0) {
      method.invoke(test);
    } else {
      method.invoke(test, context);
    }
  }

  private long getTimeout(FrameworkMethod fMethod) {
    long timeout = 2 * 60 * 1000L;
    if (timeoutStack.size() > 0) {
      timeout = timeoutStack.peekLast();
    }
    Test annotation = fMethod.getAnnotation(Test.class);
    if (annotation != null && annotation.timeout() > 0) {
      timeout = annotation.timeout();
    }
    return timeout;
  }

  private void invokeExplosively(TestContextImpl testContext, FrameworkMethod fMethod, Object test) throws Throwable {
    Handler<TestContext> callback = context -> {
      try {
        invokeTestMethod(fMethod, test, context);
      } catch (InvocationTargetException e) {
        Helper.uncheckedThrow(e.getCause());
      } catch (IllegalAccessException e) {
        Helper.uncheckedThrow(e);
      }
    };
    long timeout = getTimeout(fMethod);
    currentRunner.set(this);
    Context ctx = contextStack.peekLast();
    CompletableFuture<Throwable> future = new CompletableFuture<>();
    if (ctx != null) {
      ctx.runOnContext(v -> {
        testContext.run(null, timeout, callback, future::complete);
      });
    } else {
      testContext.run(null, timeout, callback, future::complete);
    }
    Throwable failure;
    try {
      failure = future.get();
    } catch (InterruptedException e) {
      // Should we do something else ?
      Thread.currentThread().interrupt();
      throw e;
    } finally {
      currentRunner.set(null);
    }
    if (failure != null) {
      throw failure;
    }
  }

  @Override
  protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
    return withBefores(testContext, getTestClass().getAnnotatedMethods(Before.class), target, statement);
  }

  @Override
  protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
    List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
    return withAfters(testContext, afters, target, statement);
  }

  @Override
  protected Statement withBeforeClasses(Statement statement) {
    List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
    return withBefores(new TestContextImpl(classAttributes, null), befores, null, statement);
  }

  @Override
  protected Statement withAfterClasses(Statement statement) {
    List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(AfterClass.class);
    return withAfters(new TestContextImpl(classAttributes, null), afters, null, statement);
  }

  @Override
  protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement next) {
    // Need to be a noop since we handle that without a wrapping statement
    return next;
  }

  private Statement withBefores(TestContextImpl testContext, List<FrameworkMethod> befores, Object target, Statement statement) {
    if (befores.isEmpty()) {
      return statement;
    } else {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          for (FrameworkMethod before : befores) {
            invokeExplosively(testContext, before, target);
          }
          statement.evaluate();
        }
      };
    }
  }

  private Statement withAfters(TestContextImpl testContext, List<FrameworkMethod> afters, Object target, Statement statement) {
    if (afters.isEmpty()) {
      return statement;
    } else {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          List<Throwable> errors = new ArrayList<Throwable>();
          try {
            statement.evaluate();
          } catch (Throwable e) {
            errors.add(e);
          } finally {
            for (FrameworkMethod after : afters) {
              try {
                invokeExplosively(testContext, after, target);
              } catch (Throwable e) {
                errors.add(e);
              }
            }
          }
          MultipleFailureException.assertEmpty(errors);
        }
      };
    }
  }

  @Override
  protected Statement methodBlock(FrameworkMethod frameworkMethod) {
    testContext = new TestContextImpl(new HashMap<>(classAttributes), null);
    Object testInstance;
    try {
      testInstance = new ReflectiveCallable() {
        @Override
        protected Object runReflectiveCall() throws Throwable {
          return createTest();
        }
      }.run();
    } catch (Throwable ex) {
      return new Fail(ex);
    }

    Environment.inject(testInstance);

    Statement statement = methodInvoker(frameworkMethod, testInstance);
    statement = possiblyExpectingExceptions(frameworkMethod, testInstance, statement);
    statement = withBefores(frameworkMethod, testInstance, statement);
    statement = withAfters(frameworkMethod, testInstance, statement);
    statement = withPotentialTimeout(frameworkMethod, testInstance, statement);
    testContext = null;
    return statement;
  }
}

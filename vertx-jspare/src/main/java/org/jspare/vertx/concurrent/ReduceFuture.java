package org.jspare.vertx.concurrent;

import io.vertx.core.Future;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by paulo.ferreira on 02/06/2017.
 */
public class ReduceFuture {

  private final List<Supplier<Future>> elements;
  private AtomicInteger count;
  private Future<Void> future;

  public ReduceFuture(List<Supplier<Future>> elements) {
    this.elements = elements;
    init();
  }

  public static ReduceFuture create(List<Supplier<Future>> futures) {
    return new ReduceFuture(futures);
  }

  private void init() {
    this.future = Future.future();
    this.count = new AtomicInteger(0);
  }

  public Future<Void> reduce() {

    perform();

    return future;
  }

  private void perform() {

    if (future.isComplete()) {
      return;
    }

    Future<?> el = elements.get(count.get()).get();
    el.setHandler(ar -> {

      if (ar.succeeded()) {

        if (count.incrementAndGet() == elements.size()) {

          future.complete();
        } else {

          perform();
        }
      } else {

        future.fail(ar.cause());
      }
    });
  }
}

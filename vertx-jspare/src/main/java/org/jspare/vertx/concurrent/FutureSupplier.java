/*
 * Copyright 2016 Jspare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.vertx.concurrent;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.CompositeFutureImpl;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Instantiates a new future supplier.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@UtilityClass
public class FutureSupplier {

  /**
   * Sequence with a list of futures. Transforms a `List[Future[R]]` into a
   * `Future[List[R]]`.
   * <p>
   * When all futures succeed, the result future completes with the list of each
   * result of elements in {@code futures}.
   * </p>
   * The returned future fails as soon as one of the futures in {@code futures}
   * fails. When the list is empty, the returned future will be already
   * completed.
   * <p>
   * Useful for reducing many futures into a single @{link Future}.
   *
   * @param futures a list of {@link Future futures}
   * @param <R>     the type R
   * @return the future
   */
  public static <R> Future<List<R>> sequenceFuture(List<Future<R>> futures) {
    return CompositeFutureImpl.all(futures.toArray(new Future[futures.size()]))
      .map(v -> futures.stream().map(Future::result).collect(Collectors.toList()));
  }

  /**
   * Supply.
   *
   * @param <T>   the generic type
   * @param event the event
   * @return the future
   */
  public <T> Future<T> supply(Supplier<T> event) {

    Future<T> future = Future.future();
    try {

      future.complete(event.get());
    } catch (Throwable e) {

      future.fail(e);
    }
    return future;
  }

  /**
   * Supply.
   *
   * @param <T>   the generic type
   * @param event the event
   * @return the future
   */
  public <T> Future<T> supply(Handler<Void> event) {
    Future<T> future = Future.future();
    try {

      event.handle(null);
      future.complete();
    } catch (Throwable e) {

      future.fail(e);
    }
    return future;
  }
}

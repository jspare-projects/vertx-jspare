/*
 * Copyright 2016 JSpare.org.
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
package org.jspare.vertx.web.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.annotation.Resource;
import org.jspare.vertx.builder.Collector;
import org.jspare.vertx.web.annotation.auth.Auth;
import org.jspare.vertx.web.annotation.auth.IgnoreAuth;
import org.jspare.vertx.web.annotation.content.Consumes;
import org.jspare.vertx.web.annotation.content.Produces;
import org.jspare.vertx.web.annotation.handler.BlockingHandler;
import org.jspare.vertx.web.annotation.handler.FailureHandler;
import org.jspare.vertx.web.annotation.handler.SockJsHandler;
import org.jspare.vertx.web.annotation.method.All;
import org.jspare.vertx.web.annotation.subrouter.IgnoreSubRouter;
import org.jspare.vertx.web.annotation.subrouter.SubRouter;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Resource
public class RouteCollector implements Collector<Collection<HandlerData>> {

  /*
   * (non-Javadoc)
   * 
   * @see org.jspare.vertx.builder.Collector#collect(java.lang.Class,
   * java.lang.Object[])
   */
  @Override
  public Collection<HandlerData> collect(Class<?> clazz, Object... args) {

    if (args.length < 1) {

      throw new IllegalArgumentException(
          String.format("Cannot collect route class [%s] without routeHandlerClass or authHandler", clazz.getName()));
    }

    final RouterBuilder builder = (RouterBuilder) args[0];

    // Retrieve required parameters to collect handlers

    final Class<? extends Handler<RoutingContext>> routeHandlerClass = builder.handlerClass();
    final SockJSHandlerOptions sockJSHandlerOptions = builder.sockJSHandlerOptions();

    // Initialize collected handlers

    List<HandlerData> collectedHandlers = new ArrayList<>();
    List<Annotation> httpMethodsAnnotations = new ArrayList<>(getHttpMethodsPresents(clazz));

    for (Method method : clazz.getDeclaredMethods()) {

      if (!isHandler(method)) {

        continue;
      }

      final List<Annotation> handlerHttpMethodsAnnotations = new ArrayList<>();
      handlerHttpMethodsAnnotations.addAll(httpMethodsAnnotations);

      String consumes = method.isAnnotationPresent(Consumes.class) ? method.getAnnotation(Consumes.class).value()
          : StringUtils.EMPTY;
      String produces = method.isAnnotationPresent(Produces.class) ? method.getAnnotation(Produces.class).value()
          : StringUtils.EMPTY;

      AuthHandler authHandler = null;

      // Validate if route has auth annotation
      if (hasAuth(clazz, method) && !method.isAnnotationPresent(IgnoreAuth.class)) {

        // Retrieve auth metadata
        Auth auth = method.isAnnotationPresent(Auth.class) ? getAuth(method) : getAuth(clazz);

        // Retrieve auth identity
        String identity = auth.authHandler();

        // Get authHandler from RouterBuilder
        Optional<Supplier<AuthHandler>> oAuthHandler = Optional.ofNullable(builder.authHandlerMap().get(identity));

        // Add authorities if is present on metadata
        if (oAuthHandler.isPresent()) {

          Supplier<AuthHandler> authHandlerSupplier = oAuthHandler.get();
          authHandler = authHandlerSupplier.get();
          authHandler.addAuthorities(
              Arrays.asList(auth.value()).stream().filter(a -> StringUtils.isNotEmpty(a)).collect(Collectors.toSet()));
        }
      }

      HandlerData defaultHandlerData = new HandlerData().clazz(clazz).method(method).consumes(consumes)
          .produces(produces).authHandler(authHandler)
          .routeHandlerClass(routeHandlerClass);

      if (hasHttpMethodsPresents(method)) {

        handlerHttpMethodsAnnotations.clear();
        handlerHttpMethodsAnnotations.addAll(getHttpMethodsPresents(method));
      }

      // Iterate all handlers catched on class
      getHandlersPresents(method).forEach(handlerType -> {

        try {

          HandlerData handlerData = (HandlerData) defaultHandlerData.clone();

          // Set handler type on HandlerData
          if (isHandlerAnnotation(handlerType, SockJsHandler.class)) {

            // If SockJs type set other attributes and add to
            // collection
            handlerData.handlerType(HandlerType.SOCKETJS_HANDLER);
            handlerData.sockJSHandler(SockJSHandler.create(builder.vertx(),
                sockJSHandlerOptions != null ? sockJSHandlerOptions : new SockJSHandlerOptions()));
            handlerData.path(method.getAnnotation(SockJsHandler.class).value());
            collectedHandlers.add(handlerData);
            return;

          } else if (isHandlerAnnotation(handlerType, org.jspare.vertx.web.annotation.handler.Handler.class)) {

            handlerData.handlerType(HandlerType.HANDLER);
          } else if (isHandlerAnnotation(handlerType, FailureHandler.class)) {

            handlerData.handlerType(HandlerType.HANDLER);
          } else if (isHandlerAnnotation(handlerType, BlockingHandler.class)) {

            handlerData.handlerType(HandlerType.BLOCKING_HANDLER);
          }

          // Extract order from Handler, all Hanlder having order()
          int order = annotationMethod(handlerType, "order");

          handlerData.order(order);

          // Iterate methods of handler and registry on collection
          if (handlerHttpMethodsAnnotations.isEmpty()) {

            collectedHandlers.add(handlerData);
          } else {

            collectedHandlers.addAll(collectByMethods(handlerData, handlerHttpMethodsAnnotations));
          }
        } catch (Exception e) {

          log.warn("Ignoring handler class {} method {} - {}", clazz.getName(), method.getName(),
              e.getCause().toString());
        }
      });
    }
    return collectedHandlers;
  }

  /**
   * Annotation method.
   *
   * @param <T>
   *          the generic type
   * @param annotation
   *          the annotation
   * @param methodRef
   *          the method ref
   * @return the t
   * @throws NoSuchMethodException
   *           the no such method exception
   * @throws SecurityException
   *           the security exception
   * @throws IllegalAccessException
   *           the illegal access exception
   * @throws IllegalArgumentException
   *           the illegal argument exception
   * @throws InvocationTargetException
   *           the invocation target exception
   */
  @SuppressWarnings("unchecked")
  private <T> T annotationMethod(Annotation annotation, String methodRef) throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method method = annotation.annotationType().getMethod(methodRef);
    return (T) method.invoke(annotation);
  }

  /**
   * Collect by methods.
   *
   * @param handlerSource
   *          the handler source
   * @param httpMethodsAnnotations
   *          the http methods annotations
   * @return the collection
   */
  private Collection<HandlerData> collectByMethods(HandlerData handlerSource, List<Annotation> httpMethodsAnnotations) {

    return httpMethodsAnnotations.stream().map(annotationHttpMethod -> {

      try {

        HandlerData handlerData = (HandlerData) handlerSource.clone();

        String prefix = StringUtils.EMPTY;
        String path = annotationMethod(annotationHttpMethod, "value");
        boolean isRegexPath = annotationMethod(annotationHttpMethod, "regex");

        if (handlerData.clazz().isAnnotationPresent(SubRouter.class)
            && !handlerData.method().isAnnotationPresent(IgnoreSubRouter.class)) {

          prefix = handlerData.clazz().getAnnotation(SubRouter.class).value();
        }
        handlerData.path(String.format("%s%s", prefix, path));
        handlerData.pathRegex(isRegexPath);

        // For All, ignore set httpMethod
        if (All.class.equals(annotationHttpMethod.annotationType())) {

          return handlerData;
        }

        handlerData.httpMethod(annotationHttpMethod.annotationType().getSimpleName().toUpperCase());
        return handlerData;
      } catch (Exception e) {

        log.warn("Ignoring handler class {} method {} - {}", handlerSource.clazz().getName(),
            handlerSource.method().getName(), e);
      }
      return null;
    }).collect(Collectors.toList());
  }

  /**
   * Gets the handlers presents.
   *
   * @param method
   *          the method
   * @return the handlers presents
   */
  @SuppressWarnings("unchecked")
  private List<Annotation> getHandlersPresents(Method method) {

    List<Class<?>> handlerClass = Arrays.asList(HandlerType.values()).stream().map(ht -> ht.getHandlerClass())
        .collect(Collectors.toList());
    return handlerClass.stream()
        .filter(handlerType -> method.isAnnotationPresent((Class<? extends Annotation>) handlerType))
        .map(handlerType -> method.getAnnotation((Class<? extends Annotation>) handlerType))
        .collect(Collectors.toList());
  }

  /**
   * Gets the http methods presents.
   *
   * @param element
   *          the element
   * @return the http methods presents
   */
  @SuppressWarnings("unchecked")
  private List<Annotation> getHttpMethodsPresents(AnnotatedElement element) {

    List<Class<?>> filteredClazz = new ArrayList<>();
    filteredClazz.addAll(Arrays.asList(HttpMethodType.values()).stream().map(ht -> ht.getHttpMethodClass())
        .collect(Collectors.toList()));
    filteredClazz.removeIf(
        clazzHttpMethodType -> !element.isAnnotationPresent((Class<? extends Annotation>) clazzHttpMethodType));
    return filteredClazz.stream()
        .map(httpMethodClazz -> element.getAnnotation((Class<? extends Annotation>) httpMethodClazz))
        .collect(Collectors.toList());
  }

  /**
   * Checks for http methods presents.
   *
   * @param method
   *          the method
   * @return true, if successful
   */
  private boolean hasHttpMethodsPresents(Method method) {

    return getHttpMethodsPresents(method).stream()
        .filter(annotation -> method.isAnnotationPresent(annotation.annotationType())).count() >= 1;
  }

  /**
   * Checks if is handler.
   *
   * @param method
   *          the method
   * @return true, if is handler
   */
  private boolean isHandler(Method method) {

    return getHandlersPresents(method).stream()
        .filter(handlerAnnotation -> method.isAnnotationPresent(handlerAnnotation.annotationType())).count() >= 1;
  }

  /**
   * Gets the auth.
   *
   * @param annotatedElement
   *          the annotated element
   * @return the auth
   */
  protected Auth getAuth(AnnotatedElement annotatedElement) {
    return annotatedElement.getAnnotation(Auth.class);
  }

  /**
   * Checks for auth.
   *
   * @param clazz
   *          the clazz
   * @param method
   *          the method
   * @return true, if successful
   */
  protected boolean hasAuth(Class<?> clazz, Method method) {
    return (clazz.isAnnotationPresent(Auth.class) || method.isAnnotationPresent(Auth.class))
        && !method.isAnnotationPresent(IgnoreAuth.class);
  }

  /**
   * Checks if is handler annotation.
   *
   * @param handlerType
   *          the handler type
   * @param element
   *          the element
   * @return true, if is handler annotation
   */
  protected boolean isHandlerAnnotation(Annotation handlerType, Class<?> element) {
    return handlerType.annotationType().equals(element);
  }
}
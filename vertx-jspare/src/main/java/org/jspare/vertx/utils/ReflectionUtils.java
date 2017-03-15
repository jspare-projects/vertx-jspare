package org.jspare.vertx.utils;

import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@UtilityClass
public class ReflectionUtils {

  public List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> ann) {
    return Arrays.asList(clazz.getDeclaredMethods()).stream().filter(m -> m.isAnnotationPresent(ann)).collect(Collectors.toList());
  }

  public <T> T getAnnotation(AnnotatedElement element, Class<T> ann) {
    return (T) element.getAnnotation((Class<? extends Annotation>) ann);
  }
}

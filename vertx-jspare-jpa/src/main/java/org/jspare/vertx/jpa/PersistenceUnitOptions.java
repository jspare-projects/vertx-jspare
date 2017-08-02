package org.jspare.vertx.jpa;


import org.jspare.jpa.PersistenceUnitProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by paulo.ferreira on 15/03/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PersistenceUnitOptions {

  /**
   * Define name of data source to be registered
   * @return the value
   */
  String value() default PersistenceUnitProvider.DEFAULT_DS;
}

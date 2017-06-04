package org.jspare.vertx.web.annotation.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define RouterBuilderAware
 * <p>
 * The method annotated with this module will be responsible for setting up a web {@link io.vertx.ext.web.Router}
 * </p>
 * <p><b>Note</b>: The method obrigatorily need receive {@link io.vertx.ext.web.Router} as parameter
 * </p>
 * Created by paulo.ferreira on 15/03/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RouterBuilderAware {
}

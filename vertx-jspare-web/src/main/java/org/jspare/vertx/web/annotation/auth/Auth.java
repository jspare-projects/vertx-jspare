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
package org.jspare.vertx.web.annotation.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;
import org.jspare.vertx.web.builder.RouterBuilder;

/**
 * The Annotation Security. <br>
 * It is defined module that is the route to be safe and protected for Token
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Auth {

  /**
   * Auth handler.
   *
   * @return the string
   */
  String authHandler() default RouterBuilder.DEFAULT_AUTH_HANDLER_KEY;

  /**
   * The Skip roles attribute allows any user who is authenticated to access the
   * handler.
   *
   * @return true, if selected
   */
  boolean skipAuthorities() default false;

  /**
   * Define the authority of route for user authentication.
   *
   * @return the string
   */
  String[] value() default StringUtils.EMPTY;
}

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
package org.jspare.vertx.annotation;

import org.jspare.vertx.JspareVerticle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * An module responsible for indicating to {@link Modules } the
 * internal reached to be initialized by {@link JspareVerticle }.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
  Class<? extends org.jspare.vertx.Module> value();
  boolean persistent() default true;
}

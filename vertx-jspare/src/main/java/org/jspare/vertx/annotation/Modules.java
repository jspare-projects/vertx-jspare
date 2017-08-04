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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link Modules } indicate to {@link JspareVerticle}.
 *
 * <code>
 *  &#64;Modules(@Module("eventbus"))
 *  public class SampleVerticle extends AutoConfigurationVerticle
 *  </code>
 *
 * <b>Note</b>: This class is still considered internal because we did not
 * perform all the tests or arrived at a stable version, but we understand that
 * the use of this will be advantageous.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Modules {

  /**
   * Indicates which internal will be loaded.
   *
   * @return the module[]
   */
  Module[] value();
}

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
package org.jspare.vertx.web.annotation.handling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jspare.vertx.web.handler.DefaultHandler;

/**
 * The Interface Model.
 * 
 * <strong>Note</strong>: The {@link Model} isn't more necessary to serialize or
 * deserialize one route handler method. The {@link DefaultHandler } will try
 * parse any object passed by parameter. 
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 * @since 22/04/2016
 * @deprecated since 1.1.0, candidate to be remoced.
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD })
public @interface Model {

}

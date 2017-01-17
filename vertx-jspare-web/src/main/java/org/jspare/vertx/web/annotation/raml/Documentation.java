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
package org.jspare.vertx.web.annotation.raml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

/**
 * The Annotation Documentation. <br>
 * Used for documents the current route
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Documentation {

	/**
	 * Description of this handler.
	 *
	 * @return the string
	 */
	String description() default StringUtils.EMPTY;

	QueryParameter[] queryParameters() default {};

	/**
	 * Request class.
	 *
	 * @return the class
	 */
	Class<?> requestClass() default Object.class;

	/**
	 * Response class.
	 *
	 * @return the class
	 */
	Class<?> responseClass() default Object.class;

	/**
	 * Response status.
	 *
	 * @return the status[]
	 */
	Status[] responseStatus() default { @Status(code = 200, description = "Success") };
}
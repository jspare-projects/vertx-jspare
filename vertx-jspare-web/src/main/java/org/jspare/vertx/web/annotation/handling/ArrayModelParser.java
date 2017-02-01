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

import java.util.Collection;
import java.util.stream.Collectors;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;

/**
 * The Class ArrayModelParser.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class ArrayModelParser {

	/**
	 * To list.
	 *
	 * @param <T> the generic type
	 * @param json the json
	 * @param typeClass the type class
	 * @return the collection
	 */
	public static <T> Collection<T> toList(String json, Class<T> typeClass) {

		JsonArray array = new JsonArray(json);
		Collection<T> collections = array.stream().map(j -> Json.decodeValue(j.toString(), typeClass))
				.collect(Collectors.toList());
		return collections;
	}
}

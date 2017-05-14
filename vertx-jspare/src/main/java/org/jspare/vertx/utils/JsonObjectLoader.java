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
package org.jspare.vertx.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * The Class JsonObjectLoader.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class JsonObjectLoader {

  /** The Constant CONVERTER_PATTERN. */
  private static final String CONVERTER_PATTERN = "%sConverter";

  /**
   * Sets the default charset.
   *
   * @param defaultCharset
   *          the new default charset
   */
  @Setter

  /**
   * Gets the default charset.
   *
   * @return the default charset
   */
  @Getter
  private Charset defaultCharset = StandardCharsets.UTF_8;

  /**
   * Sets the default root path.
   *
   * @param defaultRootPath
   *          the new default root path
   */
  @Setter

  /**
   * Gets the default root path.
   *
   * @return the default root path
   */
  @Getter
  private String defaultRootPath = "vertx";

  /**
   * Load options.
   *
   * @param filename
   *          the filename
   * @return the json object
   */
  public JsonObject loadOptions(String filename) {

    return loadOptions(defaultRootPath, filename);
  }

  /**
   * Load options.
   *
   * @param <T>
   *          the generic type
   * @param filename
   *          the filename
   * @param optionClazz
   *          the option clazz
   * @return the t
   */
  @SneakyThrows
  public <T> T loadOptions(String filename, Class<T> optionClazz) {

    return loadOptions(defaultRootPath, filename, optionClazz);
  }

  /**
   * Load options.
   *
   * @param defaultRootPath
   *          the default root path
   * @param filename
   *          the filename
   * @return the json object
   */
  @SneakyThrows(IOException.class)
  public JsonObject loadOptions(String defaultRootPath, String filename) {

    String json = null;
    Path path = Paths.get(defaultRootPath, filename);
    File file = path.toFile();
    if (file.exists()) {

      json = FileUtils.readFileToString(path.toFile(), defaultCharset);
    } else {

      json = IOUtils.toString(this.getClass().getResourceAsStream(String.format("/%s", path.toString())),
          getDefaultCharset());
    }

    return new JsonObject(json);
  }

  /**
   * Load options.
   *
   * @param <T>
   *          the generic type
   * @param defaultRootPath
   *          the default root path
   * @param filename
   *          the filename
   * @param optionClazz
   *          the option clazz
   * @return the t
   */
  @SneakyThrows
  public <T> T loadOptions(String defaultRootPath, String filename, Class<T> optionClazz) {

    String optionClassName = String.format(CONVERTER_PATTERN, optionClazz.getName());
    Method method = Class.forName(optionClassName).getDeclaredMethod("fromJson", JsonObject.class, optionClazz);
    T instance = optionClazz.newInstance();
    method.invoke(null, loadOptions(defaultRootPath, filename), instance);
    return instance;
  }
}

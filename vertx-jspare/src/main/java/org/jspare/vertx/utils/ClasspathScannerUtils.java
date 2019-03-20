/*
 * Copyright 2016 Jspare.org.
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

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.experimental.UtilityClass;

/**
 * Instantiates a new classpath scanner utils.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
@UtilityClass
public class ClasspathScannerUtils {

  /**
   * The Constant ALL_SCAN_QUOTE.
   */
  public static final String ALL_SCAN_QUOTE = ".*";

  /**
   * Resolve package name.
   *
   * @param cPackage the c package
   * @return the string
   */
  public String resolvePackageName(String cPackage) {

    return cPackage.endsWith(ALL_SCAN_QUOTE) ? cPackage.substring(0, cPackage.length() - 2) : cPackage;
  }

  /**
   * Scanner.
   *
   * @param scanSpec the scan spec
   * @return the fast classpath scanner
   */
  public FastClasspathScanner scanner(String scanSpec) {

    return new FastClasspathScanner(ClasspathScannerUtils.resolvePackageName(scanSpec));
  }
}

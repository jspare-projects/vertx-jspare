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
package org.jspare.vertx.internal;

/**
 * Base Interface for Launcher with Exit Codes.
 *
 * The codes above follow
 * <a href="http://vertx.io/docs/vertx-core/java/#_launcher_and_exit_code">Vertx
 * and launcher exit code</a> specification, and can be extendend implement the
 * codes above follow Vertx Launcher specification, and can be extended
 * implementing this interface.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 *
 */
public interface ExitCode {

  /**
   * The smoothly.
   * <p>
   * 0 if the process ends smoothly, or if an uncaught error is thrown
   * </p>
   */
  int SMOOTHLY = 0;

  /**
   * The general error.
   * <p>
   * 1 for general purpose error
   * </p>
   * /
   */
  int GENERAL_ERROR = 1;

  /**
   * The vertx cannot be initialized.
   * <p>
   * 11 if Vert.x cannot be initialized
   * </p>
   */
  int VERTX_CANNOT_BE_INITIALIZED = 11;

  /**
   * The spawn process error.
   * <p>
   * 12 if a spawn process cannot be started, found or stopped. This error code
   * is used by the start and stop command
   * </p>
   */
  int SPAWN_PROCESS_ERROR = 12;

  /**
   * The system configuration error.
   * <p>
   * 14 if the system configuration is not meeting the system requirement (shc
   * as java not found)
   * </p>
   */
  int SYSTEM_CONFIGURATION_ERROR = 14;

  /**
   * The main verticle error.
   * <p>
   * 15 if the main verticle cannot be deployed
   * </p>
   */
  int MAIN_VERTICLE_ERROR = 15;
}

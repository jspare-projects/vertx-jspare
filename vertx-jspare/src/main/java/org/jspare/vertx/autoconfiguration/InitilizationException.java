package org.jspare.vertx.autoconfiguration;

/**
 * Created by paulo.ferreira on 03/06/2017.
 */
public class InitilizationException extends Exception {

  public InitilizationException() {
  }

  public InitilizationException(String message) {
    super(message);
  }

  public InitilizationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InitilizationException(Throwable cause) {
    super(cause);
  }

  public InitilizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

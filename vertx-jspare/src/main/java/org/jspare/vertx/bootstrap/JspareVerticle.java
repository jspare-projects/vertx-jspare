package org.jspare.vertx.bootstrap;

import io.vertx.core.AbstractVerticle;
import org.jspare.core.Environment;

/**
 * Created by paulo.ferreira on 15/05/2017.
 */
public class JspareVerticle extends AbstractVerticle {

  public JspareVerticle() {
    Environment.inject(this);
  }
}

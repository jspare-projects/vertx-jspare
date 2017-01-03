package org.jspare.vertx.bootstrap;

import org.jspare.core.annotation.Resource;

import io.vertx.core.Vertx;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Resource
public class VertxHolder {

	private Vertx vertx;
}

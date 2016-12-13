package org.jspare.vertx.builder;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class VerticleData {
	
	private Verticle verticle;
	
	private DeploymentOptions deploymentOptions;
}
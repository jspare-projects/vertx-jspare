package org.jspare.vertx;

import org.jspare.vertx.bootstrap.VertxApplication;
import org.junit.Before;

public class AbstractVertxApplicationTest extends VertxApplication {

	
	@Before
	public void load() {
		
		this.run();
	}

	@Override
	public void start() {
	}
}
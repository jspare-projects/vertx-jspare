package org.jspare.vertx.injector;

import org.jspare.vertx.AbstractVertxApplicationTest;
import org.jspare.vertx.annotation.VertxInject;
import org.junit.Assert;
import org.junit.Test;

import io.vertx.core.Vertx;

public class VertxInjectTest extends AbstractVertxApplicationTest {

	@VertxInject
	private Vertx vertx;
	
	@Test
	public void vertxTest() {
		
		Assert.assertNotNull(vertx);
	}
	
	@Test
	public void componentsCreationTest(){
		
	}
}
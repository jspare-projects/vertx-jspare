package org.jspare.vertx.utils;

import static org.jspare.core.container.Environment.my;

import org.jspare.vertx.AbstractVertxApplicationTest;
import org.jspare.vertx.utils.JsonObjectLoader;
import org.junit.Assert;
import org.junit.Test;

import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class JsonObjectLoaderTest extends AbstractVertxApplicationTest {
	
	
	@Test
	public void loadTest(){
		

		JsonObject jsonObject = my(JsonObjectLoader.class).loadOptions("VertxOptions.json");
		Assert.assertNotNull(jsonObject);
		Assert.assertTrue(jsonObject.getBoolean("clustered"));
		
		VertxOptions vertxOptions = my(JsonObjectLoader.class).loadOptions("VertxOptions.json", VertxOptions.class);
		Assert.assertNotNull(vertxOptions);
		Assert.assertTrue(vertxOptions.isClustered());
	}

}

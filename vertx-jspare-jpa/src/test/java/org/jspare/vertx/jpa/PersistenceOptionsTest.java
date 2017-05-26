package org.jspare.vertx.jpa;

import io.vertx.core.json.Json;
import org.jspare.jpa.PersistenceOptions;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by paulo.ferreira on 16/03/2017.
 */
public class PersistenceOptionsTest {

    @Test
    public void parseTest(){

        String json = "{ \"username\" : \"root\"}";
        PersistenceOptions persistenceOptions = Json.decodeValue(json, PersistenceOptions.class);
        Assert.assertEquals("root", persistenceOptions.getUsername());
    }

}

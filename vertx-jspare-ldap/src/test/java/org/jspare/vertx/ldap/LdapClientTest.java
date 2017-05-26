package org.jspare.vertx.ldap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Ignore;
import org.junit.Test;

import com.unboundid.ldap.sdk.SearchScope;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LdapClientTest {

  @Test
  @Ignore
  public void searchTest(){
    
    String json = "{\"server\" : \"interno.senior.com.br\", \"port\" : 389, \"searchBase\" : \"ou=usuarios,ou=senior,dc=interno,dc=senior,dc=com,dc=br\", \"user\" : \"ssm.consultaad@interno.senior.com.br\", \"password\" : \"$er34T58423q\"}";
    LdapClient client = new LdapClientImpl(new LdapOptions(new JsonObject(json)));
    
    client.search(SearchScope.SUB, "(&(objectClass=Person)(!(userAccountControl:1.2.840.113556.1.4.803:=2)))", null, ar -> {
      
      log.debug(ReflectionToStringBuilder.toString(ar.result(), ToStringStyle.MULTI_LINE_STYLE));
    });
  }

}

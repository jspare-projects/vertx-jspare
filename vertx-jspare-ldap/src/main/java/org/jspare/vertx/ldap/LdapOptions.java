package org.jspare.vertx.ldap;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import org.jspare.vertx.utils.DataObjectConverter;

@Data
@DataObject
public class LdapOptions {

  private String server;
  private int port;
  private String searchBase;
  private String user;
  private String password;

  public LdapOptions() {
  }

  public LdapOptions(JsonObject json) {
    DataObjectConverter.fromJson(json, this);
  }
}
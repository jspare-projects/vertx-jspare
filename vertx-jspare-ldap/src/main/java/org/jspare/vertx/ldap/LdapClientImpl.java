package org.jspare.vertx.ldap;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import lombok.Getter;
import lombok.Setter;

public class LdapClientImpl implements LdapClient {

  @Getter
  @Setter
  private LdapOptions config;

  public LdapClientImpl(LdapOptions config) {
    setConfig(config);
  }

  @Override
  public void search(SearchScope scope, String filter, String[] attributes,
      Handler<AsyncResult<SearchResult>> resultHandler) {
    
    if(attributes == null) attributes = new String[] {};

    try {

      SearchRequest request = new SearchRequest(config.getSearchBase(), scope, filter,
          attributes);
      resultHandler.handle(Future.<SearchResult>succeededFuture(getConnection().search(request)));
    } catch (Throwable t) {

      resultHandler.handle(Future.failedFuture(t));
    }
  }

  @Override
  public void validateCredentials(String username, String password, Handler<AsyncResult<Boolean>> resultHandler) {

    try {

      LDAPConnection conn = getConnection(username, password);
      boolean isConn = conn.isConnected();
      conn.close();
      resultHandler.handle(Future.succeededFuture(isConn));
    } catch (Throwable t) {

      resultHandler.handle(Future.failedFuture(t));
    }
  }

  protected LDAPConnection getConnection() throws LDAPException {

    return getConnection(config.getUser(), config.getPassword());
  }

  protected LDAPConnection getConnection(String username, String password) throws LDAPException {

    return new LDAPConnection(config.getServer(), config.getPort(), username, password);
  }
}
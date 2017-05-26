package org.jspare.vertx.ldap;

import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import org.jspare.core.Component;

@Component
public interface LdapClient {

  void search(SearchScope scope, String filter, String[] attributes, Handler<AsyncResult<SearchResult>> resultHandler);

  void validateCredentials(String username, String password, Handler<AsyncResult<Boolean>> resultHandler);
}

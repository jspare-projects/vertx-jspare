package org.jspare.vertx.web.builder;

import org.jspare.vertx.builder.AbstractBuilder;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class HttpServerBuilder extends AbstractBuilder<HttpServer> {

	private final Vertx vertx;
	
	@Getter @Setter
	private HttpServer httpServer;
	
	@Getter @Setter
	private HttpServerOptions httpServerOptions;
	
	@Getter @Setter
	private Router router;
	
	public static HttpServerBuilder create(Vertx vertx) {

		return new HttpServerBuilder(vertx);
	}
	
	private HttpServerBuilder(Vertx vertx) {
		
		this.vertx = vertx;
	}
	
	@Override
	public HttpServer build() {

		if(httpServerOptions == null){
			
			httpServerOptions = new HttpServerOptions();
		}
		
		HttpServer httpServer = vertx.createHttpServer(httpServerOptions);
		
		if(router != null){
			
			httpServer.requestHandler(router::accept);
		}
		
		return httpServer;
	}
}
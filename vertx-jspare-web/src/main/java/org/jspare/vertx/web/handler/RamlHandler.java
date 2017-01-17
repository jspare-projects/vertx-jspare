package org.jspare.vertx.web.handler;

import java.util.List;

import org.jspare.vertx.web.builder.HandlerData;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RamlHandler implements Handler<RoutingContext> {

	private final List<HandlerData> listData;

	@Override
	public void handle(RoutingContext ctx) {

		HttpServerResponse response = ctx.response();
		StringBuilder ramlBuilder = new StringBuilder();
		listData.forEach(hd -> ramlBuilder.append(generateRow(hd)));
		response.setStatusCode(HttpResponseStatus.OK.code()).putHeader("Content-Type", "text/raml").end(ramlBuilder.toString());
	}

	private String generateRow(HandlerData handlerData) {
		return "";
	}
}
/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.vertx.web.handler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspare.vertx.concurrent.FutureSupplier;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;
import lombok.Setter;

public abstract class APIHandler {

	@Setter
	protected Vertx vertx;

	@Setter
	protected HttpServerRequest req;

	@Setter
	protected HttpServerResponse res;

	@Setter
	protected RoutingContext context;

	@Setter
	protected SockJSSocket sockJSEvent;
	
	protected void accepted() {
		status(HttpResponseStatus.ACCEPTED).end();
	}
	
	protected void accepted(Buffer buffer) {
		status(HttpResponseStatus.ACCEPTED).end(buffer);
	}
	
	protected void accepted(Object object) {
		accepted(object, StandardCharsets.UTF_8);
	}
	
	protected void accepted(Object object, Charset charset) {
		status(HttpResponseStatus.ACCEPTED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void accepted(String content) {
		accepted(content, StandardCharsets.UTF_8);
	}

	protected void accepted(String content, Charset charset) {
		status(HttpResponseStatus.ACCEPTED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void badGateway() {
		status(HttpResponseStatus.BAD_GATEWAY).end();
	}

	protected void badGateway(Buffer buffer) {
		status(HttpResponseStatus.BAD_GATEWAY).end(buffer);
	}

	protected void badGateway(Object object) {
		badGateway(object, StandardCharsets.UTF_8);
	}

	protected void badGateway(Object object, Charset charset) {
		status(HttpResponseStatus.BAD_GATEWAY);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void badGateway(String content) {
		badGateway(content, StandardCharsets.UTF_8);
	}

	protected void badGateway(String content, Charset charset) {
		status(HttpResponseStatus.BAD_GATEWAY);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void badRequest() {
		status(HttpResponseStatus.BAD_REQUEST).end();
	}

	protected void badRequest(Buffer buffer) {
		status(HttpResponseStatus.BAD_REQUEST).end(buffer);
	}

	protected void badRequest(Object object) {
		badRequest(object, StandardCharsets.UTF_8);
	}

	protected void badRequest(Object object, Charset charset) {
		status(HttpResponseStatus.BAD_REQUEST);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void badRequest(String content) {
		badRequest(content, StandardCharsets.UTF_8);
	}

	protected void badRequest(String content, Charset charset) {
		status(HttpResponseStatus.BAD_REQUEST);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected String body() {

		return context.getBodyAsString();
	}

	protected void conflict() {
		status(HttpResponseStatus.CONFLICT).end();
	}

	protected void conflict(Buffer buffer) {
		status(HttpResponseStatus.CONFLICT).end(buffer);
	}

	protected void conflict(Object object) {
		conflict(object, StandardCharsets.UTF_8);
	}

	protected void conflict(Object object, Charset charset) {
		status(HttpResponseStatus.CONFLICT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void conflict(String content) {
		conflict(content, StandardCharsets.UTF_8);
	}

	protected void conflict(String content, Charset charset) {
		conflict(HttpResponseStatus.CONFLICT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected HttpServerResponse contentType(String contentType) {
		res.putHeader("content-type", contentType);
		return res;
	}

	protected void continueIt() {
		status(HttpResponseStatus.CONTINUE).end();
	}

	protected void continueIt(Buffer buffer) {
		status(HttpResponseStatus.CONTINUE).end(buffer);
	}

	protected void continueIt(Object object) {
		continueIt(object, StandardCharsets.UTF_8);
	}

	protected void continueIt(Object object, Charset charset) {
		status(HttpResponseStatus.CONTINUE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void continueIt(String content) {
		continueIt(content, StandardCharsets.UTF_8);
	}

	protected void continueIt(String content, Charset charset) {
		status(HttpResponseStatus.CONTINUE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected Set<Cookie> cookies(){

		return context.cookies();
	}

	protected void created() {
		status(HttpResponseStatus.CREATED).end();
	}

	protected void created(Buffer buffer) {
		status(HttpResponseStatus.CREATED).end(buffer);
	}

	protected void created(Object object) {
		created(object, StandardCharsets.UTF_8);
	}

	protected void created(Object object, Charset charset) {
		status(HttpResponseStatus.CREATED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void created(String content) {
		created(content, StandardCharsets.UTF_8);
	}

	protected void created(String content, Charset charset) {
		created(HttpResponseStatus.CREATED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void end() {

		if (!res.ended()) {

			res.end();
		}
	}

	protected void end(Buffer buffer) {

		if (!res.ended()) {

			res.end(buffer);
		}
	}

	protected void end(JsonObject jsonObject) {

		if (!res.ended()) {

			res.end(jsonObject.encode());
		}
	}

	protected void error() {
		internalServerError();
	}

	protected void error(Buffer buffer) {
		internalServerError(buffer);
	}

	protected void error(Object object) {
		internalServerError(object);
	}

	protected void error(Object object, Charset charset) {
		internalServerError(object, charset);
	}

	protected void error(String content) {
		internalServerError(content);
	}

	protected void error(String content, Charset charset) {
		internalServerError(content, charset);
	}

	protected void expectationFailed() {
		status(HttpResponseStatus.EXPECTATION_FAILED).end();
	}

	protected void expectationFailed(Buffer buffer) {
		status(HttpResponseStatus.EXPECTATION_FAILED).end(buffer);
	}

	protected void expectationFailed(Object object) {
		expectationFailed(object, StandardCharsets.UTF_8);
	}

	protected void expectationFailed(Object object, Charset charset) {
		status(HttpResponseStatus.EXPECTATION_FAILED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void expectationFailed(String content) {
		expectationFailed(content, StandardCharsets.UTF_8);
	}

	protected void expectationFailed(String content, Charset charset) {
		status(HttpResponseStatus.EXPECTATION_FAILED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void failedDependency() {
		status(HttpResponseStatus.FAILED_DEPENDENCY).end();
	}

	protected void failedDependency(Buffer buffer) {
		status(HttpResponseStatus.FAILED_DEPENDENCY).end(buffer);
	}

	protected void failedDependency(Object object) {
		failedDependency(object, StandardCharsets.UTF_8);
	}

	protected void failedDependency(Object object, Charset charset) {
		status(HttpResponseStatus.FAILED_DEPENDENCY);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void failedDependency(String content) {
		failedDependency(content, StandardCharsets.UTF_8);
	}

	protected void failedDependency(String content, Charset charset) {
		status(HttpResponseStatus.FAILED_DEPENDENCY);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected Set<FileUpload> fileUploads(){
		
		return context.fileUploads();
	}

	protected void forbidden() {
		status(HttpResponseStatus.FORBIDDEN).end();
	}

	protected void forbidden(Buffer buffer) {
		status(HttpResponseStatus.FORBIDDEN).end(buffer);
	}

	protected void forbidden(Object object) {
		forbidden(object, StandardCharsets.UTF_8);
	}

	protected void forbidden(Object object, Charset charset) {
		status(HttpResponseStatus.FORBIDDEN);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void forbidden(String content) {
		forbidden(content, StandardCharsets.UTF_8);
	}

	protected void forbidden(String content, Charset charset) {
		status(HttpResponseStatus.FORBIDDEN);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void found() {
		status(HttpResponseStatus.FOUND).end();
	}

	protected void found(Buffer buffer) {
		status(HttpResponseStatus.FOUND).end(buffer);
	}

	protected void found(Object object) {
		found(object, StandardCharsets.UTF_8);
	}

	protected void found(Object object, Charset charset) {
		status(HttpResponseStatus.FOUND);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void found(String content) {
		found(content, StandardCharsets.UTF_8);
	}

	protected void found(String content, Charset charset) {
		status(HttpResponseStatus.FOUND);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void gatewayTimeout() {
		status(HttpResponseStatus.GATEWAY_TIMEOUT).end();
	}

	protected void gatewayTimeout(Buffer buffer) {
		status(HttpResponseStatus.GATEWAY_TIMEOUT).end(buffer);
	}

	protected void gatewayTimeout(Object object) {
		gatewayTimeout(object, StandardCharsets.UTF_8);
	}

	protected void gatewayTimeout(Object object, Charset charset) {
		status(HttpResponseStatus.GATEWAY_TIMEOUT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void gatewayTimeout(String content) {
		gatewayTimeout(content, StandardCharsets.UTF_8);
	}

	protected void gatewayTimeout(String content, Charset charset) {
		status(HttpResponseStatus.GATEWAY_TIMEOUT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected Cookie getCookie(String name){

		return context.getCookie(name);
	}

	protected Optional<String> getHeader(String name) {

		return Optional.ofNullable(req.getHeader(name));
	}

	protected String getParameter(String name) {

		return req.getParam(name);
	}

	protected JsonObject getPrincipal() {

		if (context.user() == null) {

			return null;
		}
		return context.user().principal();
	}

	protected User getUser() {

		return context.user();
	}

	protected void gone() {
		status(HttpResponseStatus.GONE).end();
	}

	protected void gone(Buffer buffer) {
		status(HttpResponseStatus.GONE).end(buffer);
	}

	protected void gone(Object object) {
		gone(object, StandardCharsets.UTF_8);
	}

	protected void gone(Object object, Charset charset) {
		status(HttpResponseStatus.GONE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void gone(String content) {
		gone(content, StandardCharsets.UTF_8);
	}

	protected void gone(String content, Charset charset) {
		status(HttpResponseStatus.GONE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void httpVersionNotSupported() {
		status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED).end();
	}

	protected void httpVersionNotSupported(Buffer buffer) {
		status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED).end(buffer);
	}

	protected void httpVersionNotSupported(Object object) {
		httpVersionNotSupported(object, StandardCharsets.UTF_8);
	}

	protected void httpVersionNotSupported(Object object, Charset charset) {
		status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void httpVersionNotSupported(String content) {
		httpVersionNotSupported(content, StandardCharsets.UTF_8);
	}

	protected void httpVersionNotSupported(String content, Charset charset) {
		status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void insufficientStorage() {
		status(HttpResponseStatus.INSUFFICIENT_STORAGE).end();
	}

	protected void insufficientStorage(Buffer buffer) {
		status(HttpResponseStatus.INSUFFICIENT_STORAGE).end(buffer);
	}

	protected void insufficientStorage(Object object) {
		insufficientStorage(object, StandardCharsets.UTF_8);
	}

	protected void insufficientStorage(Object object, Charset charset) {
		status(HttpResponseStatus.INSUFFICIENT_STORAGE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void insufficientStorage(String content) {
		insufficientStorage(content, StandardCharsets.UTF_8);
	}

	protected void insufficientStorage(String content, Charset charset) {
		status(HttpResponseStatus.INSUFFICIENT_STORAGE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void internalServerError() {
		status(HttpResponseStatus.INTERNAL_SERVER_ERROR).end();
	}

	protected void internalServerError(Buffer buffer) {
		status(HttpResponseStatus.INTERNAL_SERVER_ERROR).end(buffer);
	}

	protected void internalServerError(Object object) {
		internalServerError(object, StandardCharsets.UTF_8);
	}

	protected void internalServerError(Object object, Charset charset) {
		status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void internalServerError(String content) {
		internalServerError(content, StandardCharsets.UTF_8);
	}

	protected void internalServerError(String content, Charset charset) {
		status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void isAuthorised(List<String> permissions, Handler<Void> resultHandler) {

		List<Future<Boolean>> futures = permissions.stream().map(p -> {
			Future<Boolean> future = Future.future();
			context.user().isAuthorised(p, future.completer());
			return future;
		}).collect(Collectors.toList());

		FutureSupplier.sequenceFuture(futures).setHandler(resultList -> {

			if (resultList.succeeded()) {

				boolean isAuthorized = resultList.result().stream().reduce(false, (a, b) -> {
					if (a)
						return a;
					return b;
				});

				if (!isAuthorized) {
					forbidden();
					return;
				}
				resultHandler.handle(null);

			} else {

				error(resultList.cause());
			}
		});
	}

	protected void isAuthorised(String authority, Handler<AsyncResult<Boolean>> resultHandler) {

		context.user().isAuthorised(authority, resultHandler);
	}

	protected boolean isValidJson(String content) {
		try {
			Json.decodeValue(content, Object.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected void lengthRequired() {
		status(HttpResponseStatus.LENGTH_REQUIRED).end();
	}

	protected void lengthRequired(Buffer buffer) {
		status(HttpResponseStatus.LENGTH_REQUIRED).end(buffer);
	}

	protected void lengthRequired(Object object) {
		lengthRequired(object, StandardCharsets.UTF_8);
	}

	protected void lengthRequired(Object object, Charset charset) {
		status(HttpResponseStatus.LENGTH_REQUIRED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void lengthRequired(String content) {
		lengthRequired(content, StandardCharsets.UTF_8);
	}

	protected void lengthRequired(String content, Charset charset) {
		status(HttpResponseStatus.LENGTH_REQUIRED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void locked() {
		status(HttpResponseStatus.LOCKED).end();
	}

	protected void locked(Buffer buffer) {
		status(HttpResponseStatus.LOCKED).end(buffer);
	}

	protected void locked(Object object) {
		locked(object, StandardCharsets.UTF_8);
	}

	protected void locked(Object object, Charset charset) {
		status(HttpResponseStatus.LOCKED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void locked(String content) {
		locked(content, StandardCharsets.UTF_8);
	}

	protected void locked(String content, Charset charset) {
		status(HttpResponseStatus.LOCKED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void methodNotAllowed() {
		status(HttpResponseStatus.METHOD_NOT_ALLOWED).end();
	}

	protected void methodNotAllowed(Buffer buffer) {
		status(HttpResponseStatus.METHOD_NOT_ALLOWED).end(buffer);
	}

	protected void methodNotAllowed(Object object) {
		methodNotAllowed(object, StandardCharsets.UTF_8);
	}

	protected void methodNotAllowed(Object object, Charset charset) {
		status(HttpResponseStatus.METHOD_NOT_ALLOWED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void methodNotAllowed(String content) {
		methodNotAllowed(content, StandardCharsets.UTF_8);
	}

	protected void methodNotAllowed(String content, Charset charset) {
		status(HttpResponseStatus.METHOD_NOT_ALLOWED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void misdirectedRequest() {
		status(HttpResponseStatus.MISDIRECTED_REQUEST).end();
	}

	protected void misdirectedRequest(Buffer buffer) {
		status(HttpResponseStatus.MISDIRECTED_REQUEST).end(buffer);
	}

	protected void misdirectedRequest(Object object) {
		misdirectedRequest(object, StandardCharsets.UTF_8);
	}

	protected void misdirectedRequest(Object object, Charset charset) {
		status(HttpResponseStatus.MISDIRECTED_REQUEST);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void misdirectedRequest(String content) {
		misdirectedRequest(content, StandardCharsets.UTF_8);
	}

	protected void misdirectedRequest(String content, Charset charset) {
		status(HttpResponseStatus.MISDIRECTED_REQUEST);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void movedPermanently() {
		status(HttpResponseStatus.MOVED_PERMANENTLY).end();
	}

	protected void movedPermanently(Buffer buffer) {
		status(HttpResponseStatus.MOVED_PERMANENTLY).end(buffer);
	}

	protected void movedPermanently(Object object) {
		movedPermanently(object, StandardCharsets.UTF_8);
	}

	protected void movedPermanently(Object object, Charset charset) {
		status(HttpResponseStatus.MOVED_PERMANENTLY);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void movedPermanently(String content) {
		movedPermanently(content, StandardCharsets.UTF_8);
	}

	protected void movedPermanently(String content, Charset charset) {
		status(HttpResponseStatus.MOVED_PERMANENTLY);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void multipleChoices() {
		status(HttpResponseStatus.MULTIPLE_CHOICES).end();
	}

	protected void multipleChoices(Buffer buffer) {
		status(HttpResponseStatus.MULTIPLE_CHOICES).end(buffer);
	}

	protected void multipleChoices(Object object) {
		multipleChoices(object, StandardCharsets.UTF_8);
	}

	protected void multipleChoices(Object object, Charset charset) {
		status(HttpResponseStatus.MULTIPLE_CHOICES);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void multipleChoices(String content) {
		multipleChoices(content, StandardCharsets.UTF_8);
	}

	protected void multipleChoices(String content, Charset charset) {
		status(HttpResponseStatus.MULTIPLE_CHOICES);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void multiStatus() {
		status(HttpResponseStatus.MULTI_STATUS).end();
	}

	protected void multiStatus(Buffer buffer) {
		status(HttpResponseStatus.MULTI_STATUS).end(buffer);
	}

	protected void multiStatus(Object object) {
		multiStatus(object, StandardCharsets.UTF_8);
	}

	protected void multiStatus(Object object, Charset charset) {
		status(HttpResponseStatus.MULTI_STATUS);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void multiStatus(String content) {
		multiStatus(content, StandardCharsets.UTF_8);
	}

	protected void multiStatus(String content, Charset charset) {
		status(HttpResponseStatus.MULTI_STATUS);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void networkAuthenticationRequired() {
		status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED).end();
	}

	protected void networkAuthenticationRequired(Buffer buffer) {
		status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED).end(buffer);
	}

	protected void networkAuthenticationRequired(Object object) {
		networkAuthenticationRequired(object, StandardCharsets.UTF_8);
	}

	protected void networkAuthenticationRequired(Object object, Charset charset) {
		status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void networkAuthenticationRequired(String content) {
		networkAuthenticationRequired(content, StandardCharsets.UTF_8);
	}

	protected void networkAuthenticationRequired(String content, Charset charset) {
		status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void noContent() {
		status(HttpResponseStatus.NO_CONTENT).end();
	}

	protected void noContent(Buffer buffer) {
		status(HttpResponseStatus.NO_CONTENT).end(buffer);
	}

	protected void noContent(Object object) {
		noContent(object, StandardCharsets.UTF_8);
	}

	protected void noContent(Object object, Charset charset) {
		status(HttpResponseStatus.NO_CONTENT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void noContent(String content) {
		noContent(content, StandardCharsets.UTF_8);
	}

	protected void noContent(String content, Charset charset) {
		status(HttpResponseStatus.NO_CONTENT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void nonAuthoratitativeInformation() {
		status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION).end();
	}

	protected void nonAuthoratitativeInformation(Buffer buffer) {
		status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION).end(buffer);
	}

	protected void nonAuthoratitativeInformation(Object object) {
		nonAuthoratitativeInformation(object, StandardCharsets.UTF_8);
	}

	protected void nonAuthoratitativeInformation(Object object, Charset charset) {
		status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void nonAuthoratitativeInformation(String content) {
		nonAuthoratitativeInformation(content, StandardCharsets.UTF_8);
	}

	protected void nonAuthoratitativeInformation(String content, Charset charset) {
		status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void notAcceptable() {
		status(HttpResponseStatus.NOT_ACCEPTABLE).end();
	}

	protected void notAcceptable(Buffer buffer) {
		status(HttpResponseStatus.NOT_ACCEPTABLE).end(buffer);
	}

	protected void notAcceptable(Object object) {
		notAcceptable(object, StandardCharsets.UTF_8);
	}

	protected void notAcceptable(Object object, Charset charset) {
		status(HttpResponseStatus.NOT_ACCEPTABLE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void notAcceptable(String content) {
		notAcceptable(content, StandardCharsets.UTF_8);
	}

	protected void notAcceptable(String content, Charset charset) {
		status(HttpResponseStatus.NOT_ACCEPTABLE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void notExtended() {
		status(HttpResponseStatus.NOT_EXTENDED).end();
	}

	protected void notExtended(Buffer buffer) {
		status(HttpResponseStatus.NOT_EXTENDED).end(buffer);
	}

	protected void notExtended(Object object) {
		notExtended(object, StandardCharsets.UTF_8);
	}

	protected void notExtended(Object object, Charset charset) {
		status(HttpResponseStatus.NOT_EXTENDED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void notExtended(String content) {
		notExtended(content, StandardCharsets.UTF_8);
	}

	protected void notExtended(String content, Charset charset) {
		status(HttpResponseStatus.NOT_EXTENDED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void notFound() {
		status(HttpResponseStatus.NOT_FOUND).end();
	}

	protected void notFound(Buffer buffer) {
		status(HttpResponseStatus.NOT_FOUND).end(buffer);
	}

	protected void notFound(Object object) {
		notFound(object, StandardCharsets.UTF_8);
	}

	protected void notFound(Object object, Charset charset) {
		status(HttpResponseStatus.NOT_FOUND);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void notFound(String content) {
		notFound(content, StandardCharsets.UTF_8);
	}

	protected void notFound(String content, Charset charset) {
		status(HttpResponseStatus.NOT_FOUND);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void notImplemented() {
		status(HttpResponseStatus.NOT_IMPLEMENTED).end();
	}

	protected void notImplemented(Buffer buffer) {
		status(HttpResponseStatus.NOT_IMPLEMENTED).end(buffer);
	}

	protected void notImplemented(Object object) {
		notImplemented(object, StandardCharsets.UTF_8);
	}

	protected void notImplemented(Object object, Charset charset) {
		status(HttpResponseStatus.NOT_IMPLEMENTED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void notImplemented(String content) {
		notImplemented(content, StandardCharsets.UTF_8);
	}

	protected void notImplemented(String content, Charset charset) {
		status(HttpResponseStatus.NOT_IMPLEMENTED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void notModified() {
		status(HttpResponseStatus.NOT_MODIFIED).end();
	}

	protected void notModified(Buffer buffer) {
		status(HttpResponseStatus.NOT_MODIFIED).end(buffer);
	}

	protected void notModified(Object object) {
		notModified(object, StandardCharsets.UTF_8);
	}

	protected void notModified(Object object, Charset charset) {
		status(HttpResponseStatus.NOT_MODIFIED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void notModified(String content) {
		notModified(content, StandardCharsets.UTF_8);
	}

	protected void notModified(String content, Charset charset) {
		status(HttpResponseStatus.NOT_MODIFIED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void ok() {
		success();
	}

	protected void ok(Buffer buffer) {
		success(buffer);
	}

	protected void ok(Object object) {
		success(object);
	}

	protected void ok(Object object, Charset charset) {
		success(object, charset);
	}

	protected void ok(String content) {
		success(content, StandardCharsets.UTF_8);
	}

	protected void ok(String content, Charset charset) {
		success(content, charset);
	}

	protected void partialContent() {
		status(HttpResponseStatus.PARTIAL_CONTENT).end();
	}

	protected void partialContent(Buffer buffer) {
		status(HttpResponseStatus.PARTIAL_CONTENT).end(buffer);
	}

	protected void partialContent(Object object) {
		partialContent(object, StandardCharsets.UTF_8);
	}

	protected void partialContent(Object object, Charset charset) {
		status(HttpResponseStatus.PARTIAL_CONTENT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void partialContent(String content) {
		partialContent(content, StandardCharsets.UTF_8);
	}

	protected void partialContent(String content, Charset charset) {
		status(HttpResponseStatus.PARTIAL_CONTENT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void paymentRequired() {
		status(HttpResponseStatus.PAYMENT_REQUIRED).end();
	}

	protected void paymentRequired(Buffer buffer) {
		status(HttpResponseStatus.PAYMENT_REQUIRED).end(buffer);
	}

	protected void paymentRequired(Object object) {
		paymentRequired(object, StandardCharsets.UTF_8);
	}

	protected void paymentRequired(Object object, Charset charset) {
		status(HttpResponseStatus.PAYMENT_REQUIRED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void paymentRequired(String content) {
		paymentRequired(content, StandardCharsets.UTF_8);
	}

	protected void paymentRequired(String content, Charset charset) {
		status(HttpResponseStatus.PAYMENT_REQUIRED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void preConditionFailed() {
		status(HttpResponseStatus.PRECONDITION_FAILED).end();
	}

	protected void preConditionFailed(Buffer buffer) {
		status(HttpResponseStatus.PRECONDITION_FAILED).end(buffer);
	}

	protected void preConditionFailed(Object object) {
		preConditionFailed(object, StandardCharsets.UTF_8);
	}

	protected void preConditionFailed(Object object, Charset charset) {
		status(HttpResponseStatus.PRECONDITION_FAILED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void preConditionFailed(String content) {
		preConditionFailed(content, StandardCharsets.UTF_8);
	}

	protected void preConditionFailed(String content, Charset charset) {
		status(HttpResponseStatus.PRECONDITION_FAILED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void preconditionRequired() {
		status(HttpResponseStatus.PRECONDITION_REQUIRED).end();
	}

	protected void preconditionRequired(Buffer buffer) {
		status(HttpResponseStatus.PRECONDITION_REQUIRED).end(buffer);
	}

	protected void preconditionRequired(Object object) {
		preconditionRequired(object, StandardCharsets.UTF_8);
	}

	protected void preconditionRequired(Object object, Charset charset) {
		status(HttpResponseStatus.PRECONDITION_REQUIRED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void preconditionRequired(String content) {
		preconditionRequired(content, StandardCharsets.UTF_8);
	}

	protected void preconditionRequired(String content, Charset charset) {
		status(HttpResponseStatus.PRECONDITION_REQUIRED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void processing() {
		status(HttpResponseStatus.PROCESSING).end();
	}

	protected void processing(Buffer buffer) {
		status(HttpResponseStatus.PROCESSING).end(buffer);
	}

	protected void processing(Object object) {
		processing(object, StandardCharsets.UTF_8);
	}

	protected void processing(Object object, Charset charset) {
		status(HttpResponseStatus.PROCESSING);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void processing(String content) {
		processing(content, StandardCharsets.UTF_8);
	}

	protected void processing(String content, Charset charset) {
		status(HttpResponseStatus.PROCESSING);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void proxyAuthenticationRequired() {
		status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED).end();
	}

	protected void proxyAuthenticationRequired(Buffer buffer) {
		status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED).end(buffer);
	}

	protected void proxyAuthenticationRequired(Object object) {
		proxyAuthenticationRequired(object, StandardCharsets.UTF_8);
	}

	protected void proxyAuthenticationRequired(Object object, Charset charset) {
		status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void proxyAuthenticationRequired(String content) {
		proxyAuthenticationRequired(content, StandardCharsets.UTF_8);
	}

	protected void proxyAuthenticationRequired(String content, Charset charset) {
		status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void requestEntityTooLarge() {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end();
	}

	protected void requestEntityTooLarge(Buffer buffer) {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(buffer);
	}

	protected void requestEntityTooLarge(Object object) {
		requestEntityTooLarge(object, StandardCharsets.UTF_8);
	}

	protected void requestEntityTooLarge(Object object, Charset charset) {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void requestEntityTooLarge(String content) {
		requestEntityTooLarge(content, StandardCharsets.UTF_8);
	}

	protected void requestEntityTooLarge(String content, Charset charset) {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void requestHeaderFieldsTooLarge() {
		status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).end();
	}

	protected void requestHeaderFieldsTooLarge(Buffer buffer) {
		status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).end(buffer);
	}

	protected void requestHeaderFieldsTooLarge(Object object) {
		requestEntityTooLarge(object, StandardCharsets.UTF_8);
	}

	protected void requestHeaderFieldsTooLarge(Object object, Charset charset) {
		status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void requestHeaderFieldsTooLarge(String content) {
		requestEntityTooLarge(content, StandardCharsets.UTF_8);
	}

	protected void requestHeaderFieldsTooLarge(String content, Charset charset) {
		status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void requestRangeNotSatisfiable() {
		status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE).end();
	}

	protected void requestRangeNotSatisfiable(Buffer buffer) {
		status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE).end(buffer);
	}

	protected void requestRangeNotSatisfiable(Object object) {
		requestRangeNotSatisfiable(object, StandardCharsets.UTF_8);
	}

	protected void requestRangeNotSatisfiable(Object object, Charset charset) {
		status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void requestRangeNotSatisfiable(String content) {
		requestRangeNotSatisfiable(content, StandardCharsets.UTF_8);
	}

	protected void requestRangeNotSatisfiable(String content, Charset charset) {
		status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void requestTimeout() {
		status(HttpResponseStatus.REQUEST_TIMEOUT).end();
	}

	protected void requestTimeout(Buffer buffer) {
		status(HttpResponseStatus.REQUEST_TIMEOUT).end(buffer);
	}

	protected void requestTimeout(Object object) {
		requestTimeout(object, StandardCharsets.UTF_8);
	}

	protected void requestTimeout(Object object, Charset charset) {
		status(HttpResponseStatus.REQUEST_TIMEOUT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void requestTimeout(String content) {
		requestTimeout(content, StandardCharsets.UTF_8);
	}

	protected void requestTimeout(String content, Charset charset) {
		status(HttpResponseStatus.REQUEST_TIMEOUT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void requestUtiTooLong() {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end();
	}

	protected void requestUtiTooLong(Buffer buffer) {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(buffer);
	}

	protected void requestUtiTooLong(Object object) {
		requestUtiTooLong(object, StandardCharsets.UTF_8);
	}

	protected void requestUtiTooLong(Object object, Charset charset) {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void requestUtiTooLong(String content) {
		requestUtiTooLong(content, StandardCharsets.UTF_8);
	}

	protected void requestUtiTooLong(String content, Charset charset) {
		status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void resetContent() {
		status(HttpResponseStatus.RESET_CONTENT).end();
	}

	protected void resetContent(Buffer buffer) {
		status(HttpResponseStatus.RESET_CONTENT).end(buffer);
	}

	protected void resetContent(Object object) {
		resetContent(object, StandardCharsets.UTF_8);
	}

	protected void resetContent(Object object, Charset charset) {
		status(HttpResponseStatus.RESET_CONTENT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void resetContent(String content) {
		resetContent(content, StandardCharsets.UTF_8);
	}

	protected void resetContent(String content, Charset charset) {
		status(HttpResponseStatus.RESET_CONTENT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void seeOther() {
		status(HttpResponseStatus.SEE_OTHER).end();
	}

	protected void seeOther(Buffer buffer) {
		status(HttpResponseStatus.SEE_OTHER).end(buffer);
	}

	protected void seeOther(Object object) {
		seeOther(object, StandardCharsets.UTF_8);
	}

	protected void seeOther(Object object, Charset charset) {
		status(HttpResponseStatus.SEE_OTHER);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void seeOther(String content) {
		seeOther(content, StandardCharsets.UTF_8);
	}

	protected void seeOther(String content, Charset charset) {
		status(HttpResponseStatus.SEE_OTHER);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void serviceUnavailable() {
		status(HttpResponseStatus.SERVICE_UNAVAILABLE).end();
	}

	protected void serviceUnavailable(Buffer buffer) {
		status(HttpResponseStatus.SERVICE_UNAVAILABLE).end(buffer);
	}

	protected void serviceUnavailable(Object object) {
		serviceUnavailable(object, StandardCharsets.UTF_8);
	}

	protected void serviceUnavailable(Object object, Charset charset) {
		status(HttpResponseStatus.SERVICE_UNAVAILABLE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void serviceUnavailable(String content) {
		serviceUnavailable(content, StandardCharsets.UTF_8);
	}

	protected void serviceUnavailable(String content, Charset charset) {
		status(HttpResponseStatus.SERVICE_UNAVAILABLE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected HttpServerResponse status(HttpResponseStatus status) {
		res.setStatusCode(status.code());
		res.setStatusMessage(status.reasonPhrase());
		return res;
	}

	protected void success() {
		status(HttpResponseStatus.OK).end();
	}

	protected void success(Buffer buffer) {
		status(HttpResponseStatus.OK).end(buffer);
	}

	protected void success(Object object) {
		success(object, StandardCharsets.UTF_8);
	}

	protected void success(Object object, Charset charset) {
		status(HttpResponseStatus.OK);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void success(String content) {
		success(content, StandardCharsets.UTF_8);
	}

	protected void success(String content, Charset charset) {
		status(HttpResponseStatus.OK);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void switchingProtocols() {
		status(HttpResponseStatus.SWITCHING_PROTOCOLS).end();
	}

	protected void switchingProtocols(Buffer buffer) {
		status(HttpResponseStatus.SWITCHING_PROTOCOLS).end(buffer);
	}

	protected void switchingProtocols(Object object) {
		switchingProtocols(object, StandardCharsets.UTF_8);
	}

	protected void switchingProtocols(Object object, Charset charset) {
		status(HttpResponseStatus.SWITCHING_PROTOCOLS);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void switchingProtocols(String content) {
		switchingProtocols(content, StandardCharsets.UTF_8);
	}

	protected void switchingProtocols(String content, Charset charset) {
		status(HttpResponseStatus.SWITCHING_PROTOCOLS);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void temporaryRedirect() {
		status(HttpResponseStatus.TEMPORARY_REDIRECT).end();
	}

	protected void temporaryRedirect(Buffer buffer) {
		status(HttpResponseStatus.TEMPORARY_REDIRECT).end(buffer);
	}

	protected void temporaryRedirect(Object object) {
		temporaryRedirect(object, StandardCharsets.UTF_8);
	}

	protected void temporaryRedirect(Object object, Charset charset) {
		status(HttpResponseStatus.TEMPORARY_REDIRECT);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void temporaryRedirect(String content) {
		temporaryRedirect(content, StandardCharsets.UTF_8);
	}

	protected void temporaryRedirect(String content, Charset charset) {
		status(HttpResponseStatus.TEMPORARY_REDIRECT);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void tooManyRequest() {
		status(HttpResponseStatus.TOO_MANY_REQUESTS).end();
	}

	protected void tooManyRequest(Buffer buffer) {
		status(HttpResponseStatus.TOO_MANY_REQUESTS).end(buffer);
	}

	protected void tooManyRequest(Object object) {
		tooManyRequest(object, StandardCharsets.UTF_8);
	}

	protected void tooManyRequest(Object object, Charset charset) {
		status(HttpResponseStatus.TOO_MANY_REQUESTS);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void tooManyRequest(String content) {
		tooManyRequest(content, StandardCharsets.UTF_8);
	}

	protected void tooManyRequest(String content, Charset charset) {
		status(HttpResponseStatus.TOO_MANY_REQUESTS);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected String transform(Object object) {

		return Json.encode(object);
	}

	protected void unauthorized() {
		status(HttpResponseStatus.UNAUTHORIZED).end();
	}

	protected void unauthorized(Buffer buffer) {
		status(HttpResponseStatus.UNAUTHORIZED).end(buffer);
	}

	protected void unauthorized(Object object) {
		unauthorized(object, StandardCharsets.UTF_8);
	}

	protected void unauthorized(Object object, Charset charset) {
		status(HttpResponseStatus.UNAUTHORIZED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void unauthorized(String content) {
		unauthorized(content, StandardCharsets.UTF_8);
	}

	protected void unauthorized(String content, Charset charset) {
		status(HttpResponseStatus.UNAUTHORIZED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void unorderedCollection() {
		status(HttpResponseStatus.UNORDERED_COLLECTION).end();
	}

	protected void unorderedCollection(Buffer buffer) {
		status(HttpResponseStatus.UNORDERED_COLLECTION).end(buffer);
	}

	protected void unorderedCollection(Object object) {
		unorderedCollection(object, StandardCharsets.UTF_8);
	}

	protected void unorderedCollection(Object object, Charset charset) {
		status(HttpResponseStatus.UNORDERED_COLLECTION);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void unorderedCollection(String content) {
		unorderedCollection(content, StandardCharsets.UTF_8);
	}

	protected void unorderedCollection(String content, Charset charset) {
		status(HttpResponseStatus.UNORDERED_COLLECTION);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void unprocessableEntity() {
		status(HttpResponseStatus.UNPROCESSABLE_ENTITY).end();
	}

	protected void unprocessableEntity(Buffer buffer) {
		status(HttpResponseStatus.UNPROCESSABLE_ENTITY).end(buffer);
	}

	protected void unprocessableEntity(Object object) {
		unprocessableEntity(object, StandardCharsets.UTF_8);
	}

	protected void unprocessableEntity(Object object, Charset charset) {
		status(HttpResponseStatus.UNPROCESSABLE_ENTITY);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void unprocessableEntity(String content) {
		unprocessableEntity(content, StandardCharsets.UTF_8);
	}

	protected void unprocessableEntity(String content, Charset charset) {
		status(HttpResponseStatus.UNPROCESSABLE_ENTITY);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void unsupportedMediaType() {
		status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE).end();
	}

	protected void unsupportedMediaType(Buffer buffer) {
		status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE).end(buffer);
	}

	protected void unsupportedMediaType(Object object) {
		unsupportedMediaType(object, StandardCharsets.UTF_8);
	}

	protected void unsupportedMediaType(Object object, Charset charset) {
		status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void unsupportedMediaType(String content) {
		unsupportedMediaType(content, StandardCharsets.UTF_8);
	}

	protected void unsupportedMediaType(String content, Charset charset) {
		status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void upgradeRequired() {
		status(HttpResponseStatus.UPGRADE_REQUIRED).end();
	}

	protected void upgradeRequired(Buffer buffer) {
		status(HttpResponseStatus.UPGRADE_REQUIRED).end(buffer);
	}

	protected void upgradeRequired(Object object) {
		upgradeRequired(object, StandardCharsets.UTF_8);
	}

	protected void upgradeRequired(Object object, Charset charset) {
		status(HttpResponseStatus.UPGRADE_REQUIRED);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void upgradeRequired(String content) {
		upgradeRequired(content, StandardCharsets.UTF_8);
	}

	protected void upgradeRequired(String content, Charset charset) {
		status(HttpResponseStatus.UPGRADE_REQUIRED);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void useProxy() {
		status(HttpResponseStatus.USE_PROXY).end();
	}

	protected void useProxy(Buffer buffer) {
		status(HttpResponseStatus.USE_PROXY).end(buffer);
	}

	protected void useProxy(Object object) {
		useProxy(object, StandardCharsets.UTF_8);
	}

	protected void useProxy(Object object, Charset charset) {
		status(HttpResponseStatus.USE_PROXY);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void useProxy(String content) {
		useProxy(content, StandardCharsets.UTF_8);
	}

	protected void useProxy(String content, Charset charset) {
		status(HttpResponseStatus.USE_PROXY);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

	protected void variantAlsoNegotiates() {
		status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES).end();
	}

	protected void variantAlsoNegotiates(Buffer buffer) {
		status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES).end(buffer);
	}

	protected void variantAlsoNegotiates(Object object) {
		variantAlsoNegotiates(object, StandardCharsets.UTF_8);
	}

	protected void variantAlsoNegotiates(Object object, Charset charset) {
		status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES);
		contentType("application/json");
		res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
		end();
	}

	protected void variantAlsoNegotiates(String content) {
		variantAlsoNegotiates(content, StandardCharsets.UTF_8);
	}

	protected void variantAlsoNegotiates(String content, Charset charset) {
		status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES);
		if (isValidJson(content)) {
			contentType("application/json");
		}
		res.setChunked(true);
		res.write(content, charset.name());
		end();
	}

}
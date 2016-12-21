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
package org.jspare.vertx.web.handling;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;
import lombok.Setter;

public abstract class Handling {

	@Setter
	protected HttpServerRequest req;

	@Setter
	protected HttpServerResponse res;

	@Setter
	protected RoutingContext ctx;

	@Setter
	protected SockJSSocket sockJSEvent;

	public HttpServerResponse contentType(String contentType) {
		res.putHeader("content-type", contentType);
		return res;
	}

	public void end() {

		if (!res.ended()) {

			res.end();
		}
	}

	public void end(Buffer buffer) {

		if (!res.ended()) {

			res.end(buffer);
		}
	}

	public Optional<String> getHeader(String name) {

		return Optional.ofNullable(req.getHeader(name));
	}

	public String getParameter(String name) {

		return req.getParam(name);
	}

	public HttpServerResponse status(HttpResponseStatus status) {
		res.setStatusCode(status.code());
		res.setStatusMessage(status.reasonPhrase());
		return res;
	}
	
	public void continueIt() { status(HttpResponseStatus.CONTINUE).end(); }
	public void continueIt(Buffer buffer) { status(HttpResponseStatus.CONTINUE).end(buffer); }
	public void continueIt(Object object,  Charset charset) { status(HttpResponseStatus.CONTINUE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void continueIt(String content, Charset charset) { status(HttpResponseStatus.CONTINUE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void continueIt(Object object) { success(object, StandardCharsets.UTF_8); }
	public void continueIt(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void  switchingProtocols() { status(HttpResponseStatus.SWITCHING_PROTOCOLS).end(); }
	public void switchingProtocols(Buffer buffer) { status(HttpResponseStatus.SWITCHING_PROTOCOLS).end(buffer); }
	public void switchingProtocols(Object object,  Charset charset) { status(HttpResponseStatus.SWITCHING_PROTOCOLS); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void switchingProtocols(String content, Charset charset) { status(HttpResponseStatus.SWITCHING_PROTOCOLS); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void switchingProtocols(Object object) { success(object, StandardCharsets.UTF_8); }
	public void switchingProtocols(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void processing() { status(HttpResponseStatus.PROCESSING).end(); }
	public void processing(Buffer buffer) { status(HttpResponseStatus.PROCESSING).end(buffer); }
	public void processing(Object object,  Charset charset) { status(HttpResponseStatus.PROCESSING); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void processing(String content, Charset charset) { status(HttpResponseStatus.PROCESSING); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void processing(Object object) { success(object, StandardCharsets.UTF_8); }
	public void processing(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void ok() { success(); }
	public void ok(Buffer buffer) { success(buffer); }
	public void ok(Object object,  Charset charset) { success(object, charset); }
	public void ok(String content, Charset charset) { success(content, charset); }
	public void ok(Object object) { success(object); }
	public void ok(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void success() { status(HttpResponseStatus.OK).end(); }
	public void success(Buffer buffer) { status(HttpResponseStatus.OK).end(buffer); }
	public void success(Object object,  Charset charset) { status(HttpResponseStatus.OK); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void success(String content, Charset charset) { status(HttpResponseStatus.OK); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void success(Object object) { success(object, StandardCharsets.UTF_8); }
	public void success(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void created() { status(HttpResponseStatus.CREATED).end(); }
	public void created(Buffer buffer) { status(HttpResponseStatus.CREATED).end(buffer); }
	public void created(Object object,  Charset charset) { status(HttpResponseStatus.CREATED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void created(String content, Charset charset) { status(HttpResponseStatus.CREATED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void created(Object object) { success(object, StandardCharsets.UTF_8); }
	public void created(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void accepted() { status(HttpResponseStatus.ACCEPTED).end(); }
	public void accepted(Buffer buffer) { status(HttpResponseStatus.ACCEPTED).end(buffer); }
	public void accepted(Object object,  Charset charset) { status(HttpResponseStatus.ACCEPTED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void accepted(String content, Charset charset) { status(HttpResponseStatus.ACCEPTED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void accepted(Object object) { success(object, StandardCharsets.UTF_8); }
	public void accepted(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void nonAuthoratitativeInformation() { status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION).end(); }
	public void nonAuthoratitativeInformation(Buffer buffer) { status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION).end(buffer); }
	public void nonAuthoratitativeInformation(Object object,  Charset charset) { status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void nonAuthoratitativeInformation(String content, Charset charset) { status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void nonAuthoratitativeInformation(Object object) { success(object, StandardCharsets.UTF_8); }
	public void nonAuthoratitativeInformation(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void noContent() { status(HttpResponseStatus.NO_CONTENT).end(); }
	public void noContent(Buffer buffer) { status(HttpResponseStatus.NO_CONTENT).end(buffer); }
	public void noContent(Object object,  Charset charset) { status(HttpResponseStatus.NO_CONTENT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void noContent(String content, Charset charset) { status(HttpResponseStatus.NO_CONTENT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void noContent(Object object) { success(object, StandardCharsets.UTF_8); }
	public void noContent(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void resetContent() { status(HttpResponseStatus.RESET_CONTENT).end(); }
	public void resetContent(Buffer buffer) { status(HttpResponseStatus.RESET_CONTENT).end(buffer); }
	public void resetContent(Object object,  Charset charset) { status(HttpResponseStatus.RESET_CONTENT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void resetContent(String content, Charset charset) { status(HttpResponseStatus.RESET_CONTENT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void resetContent(Object object) { success(object, StandardCharsets.UTF_8); }
	public void resetContent(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void partialContent() { status(HttpResponseStatus.PARTIAL_CONTENT).end(); }
	public void partialContent(Buffer buffer) { status(HttpResponseStatus.PARTIAL_CONTENT).end(buffer); }
	public void partialContent(Object object,  Charset charset) { status(HttpResponseStatus.PARTIAL_CONTENT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void partialContent(String content, Charset charset) { status(HttpResponseStatus.PARTIAL_CONTENT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void partialContent(Object object) { success(object, StandardCharsets.UTF_8); }
	public void partialContent(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void multiStatus() { status(HttpResponseStatus.MULTI_STATUS).end(); }
	public void multiStatus(Buffer buffer) { status(HttpResponseStatus.MULTI_STATUS).end(buffer); }
	public void multiStatus(Object object,  Charset charset) { status(HttpResponseStatus.MULTI_STATUS); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void multiStatus(String content, Charset charset) { status(HttpResponseStatus.MULTI_STATUS); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void multiStatus(Object object) { success(object, StandardCharsets.UTF_8); }
	public void multiStatus(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void multipleChoices() { status(HttpResponseStatus.MULTIPLE_CHOICES).end(); }
	public void multipleChoices(Buffer buffer) { status(HttpResponseStatus.MULTIPLE_CHOICES).end(buffer); }
	public void multipleChoices(Object object,  Charset charset) { status(HttpResponseStatus.MULTIPLE_CHOICES); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void multipleChoices(String content, Charset charset) { status(HttpResponseStatus.MULTIPLE_CHOICES); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void multipleChoices(Object object) { success(object, StandardCharsets.UTF_8); }
	public void multipleChoices(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void movedPermanently() { status(HttpResponseStatus.MOVED_PERMANENTLY).end(); }
	public void movedPermanently(Buffer buffer) { status(HttpResponseStatus.MOVED_PERMANENTLY).end(buffer); }
	public void movedPermanently(Object object,  Charset charset) { status(HttpResponseStatus.MOVED_PERMANENTLY); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void movedPermanently(String content, Charset charset) { status(HttpResponseStatus.MOVED_PERMANENTLY); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void movedPermanently(Object object) { success(object, StandardCharsets.UTF_8); }
	public void movedPermanently(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void found() { status(HttpResponseStatus.FOUND).end(); }
	public void found(Buffer buffer) { status(HttpResponseStatus.FOUND).end(buffer); }
	public void found(Object object,  Charset charset) { status(HttpResponseStatus.FOUND); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void found(String content, Charset charset) { status(HttpResponseStatus.FOUND); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void found(Object object) { success(object, StandardCharsets.UTF_8); }
	public void found(String content) { success(content, StandardCharsets.UTF_8); }

	public void seeOther() { status(HttpResponseStatus.SEE_OTHER).end(); }
	public void seeOther(Buffer buffer) { status(HttpResponseStatus.SEE_OTHER).end(buffer); }
	public void seeOther(Object object,  Charset charset) { status(HttpResponseStatus.SEE_OTHER); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void seeOther(String content, Charset charset) { status(HttpResponseStatus.SEE_OTHER); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void seeOther(Object object) { success(object, StandardCharsets.UTF_8); }
	public void seeOther(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void notModified() { status(HttpResponseStatus.NOT_MODIFIED).end(); }
	public void notModified(Buffer buffer) { status(HttpResponseStatus.NOT_MODIFIED).end(buffer); }
	public void notModified(Object object,  Charset charset) { status(HttpResponseStatus.NOT_MODIFIED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void notModified(String content, Charset charset) { status(HttpResponseStatus.NOT_MODIFIED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void notModified(Object object) { success(object, StandardCharsets.UTF_8); }
	public void notModified(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void useProxy() { status(HttpResponseStatus.USE_PROXY).end(); }
	public void useProxy(Buffer buffer) { status(HttpResponseStatus.USE_PROXY).end(buffer); }
	public void useProxy(Object object,  Charset charset) { status(HttpResponseStatus.USE_PROXY); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void useProxy(String content, Charset charset) { status(HttpResponseStatus.USE_PROXY); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void useProxy(Object object) { success(object, StandardCharsets.UTF_8); }
	public void useProxy(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void temporaryRedirect() { status(HttpResponseStatus.TEMPORARY_REDIRECT).end(); }
	public void temporaryRedirect(Buffer buffer) { status(HttpResponseStatus.TEMPORARY_REDIRECT).end(buffer); }
	public void temporaryRedirect(Object object,  Charset charset) { status(HttpResponseStatus.TEMPORARY_REDIRECT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void temporaryRedirect(String content, Charset charset) { status(HttpResponseStatus.TEMPORARY_REDIRECT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void temporaryRedirect(Object object) { success(object, StandardCharsets.UTF_8); }
	public void temporaryRedirect(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void badRequest() { status(HttpResponseStatus.BAD_REQUEST).end(); }
	public void badRequest(Buffer buffer) { status(HttpResponseStatus.BAD_REQUEST).end(buffer); }
	public void badRequest(Object object,  Charset charset) { status(HttpResponseStatus.BAD_REQUEST); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void badRequest(String content, Charset charset) { status(HttpResponseStatus.BAD_REQUEST); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void badRequest(Object object) { success(object, StandardCharsets.UTF_8); }
	public void badRequest(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void unauthorized() { status(HttpResponseStatus.UNAUTHORIZED).end(); }
	public void unauthorized(Buffer buffer) { status(HttpResponseStatus.UNAUTHORIZED).end(buffer); }
	public void unauthorized(Object object,  Charset charset) { status(HttpResponseStatus.UNAUTHORIZED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void unauthorized(String content, Charset charset) { status(HttpResponseStatus.UNAUTHORIZED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void unauthorized(Object object) { success(object, StandardCharsets.UTF_8); }
	public void unauthorized(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void paymentRequired() { status(HttpResponseStatus.PAYMENT_REQUIRED).end(); }
	public void paymentRequired(Buffer buffer) { status(HttpResponseStatus.PAYMENT_REQUIRED).end(buffer); }
	public void paymentRequired(Object object,  Charset charset) { status(HttpResponseStatus.PAYMENT_REQUIRED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void paymentRequired(String content, Charset charset) { status(HttpResponseStatus.PAYMENT_REQUIRED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void paymentRequired(Object object) { success(object, StandardCharsets.UTF_8); }
	public void paymentRequired(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void forbidden() { status(HttpResponseStatus.FORBIDDEN).end(); }
	public void forbidden(Buffer buffer) { status(HttpResponseStatus.FORBIDDEN).end(buffer); }
	public void forbidden(Object object,  Charset charset) { status(HttpResponseStatus.FORBIDDEN); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void forbidden(String content, Charset charset) { status(HttpResponseStatus.FORBIDDEN); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void forbidden(Object object) { success(object, StandardCharsets.UTF_8); }
	public void forbidden(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void notFound() { status(HttpResponseStatus.NOT_FOUND).end(); }
	public void notFound(Buffer buffer) { status(HttpResponseStatus.NOT_FOUND).end(buffer); }
	public void notFound(Object object,  Charset charset) { status(HttpResponseStatus.NOT_FOUND); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void notFound(String content, Charset charset) { status(HttpResponseStatus.NOT_FOUND); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void notFound(Object object) { success(object, StandardCharsets.UTF_8); }
	public void notFound(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void methodNotAllowed() { status(HttpResponseStatus.METHOD_NOT_ALLOWED).end(); }
	public void methodNotAllowed(Buffer buffer) { status(HttpResponseStatus.METHOD_NOT_ALLOWED).end(buffer); }
	public void methodNotAllowed(Object object,  Charset charset) { status(HttpResponseStatus.METHOD_NOT_ALLOWED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void methodNotAllowed(String content, Charset charset) { status(HttpResponseStatus.METHOD_NOT_ALLOWED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void methodNotAllowed(Object object) { success(object, StandardCharsets.UTF_8); }
	public void methodNotAllowed(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void notAcceptable() { status(HttpResponseStatus.NOT_ACCEPTABLE).end(); }
	public void notAcceptable(Buffer buffer) { status(HttpResponseStatus.NOT_ACCEPTABLE).end(buffer); }
	public void notAcceptable(Object object,  Charset charset) { status(HttpResponseStatus.NOT_ACCEPTABLE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void notAcceptable(String content, Charset charset) { status(HttpResponseStatus.NOT_ACCEPTABLE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void notAcceptable(Object object) { success(object, StandardCharsets.UTF_8); }
	public void notAcceptable(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void proxyAuthenticationRequired() { status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED).end(); }
	public void proxyAuthenticationRequired(Buffer buffer) { status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED).end(buffer); }
	public void proxyAuthenticationRequired(Object object,  Charset charset) { status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void proxyAuthenticationRequired(String content, Charset charset) { status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void proxyAuthenticationRequired(Object object) { success(object, StandardCharsets.UTF_8); }
	public void proxyAuthenticationRequired(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void requestTimeout() { status(HttpResponseStatus.REQUEST_TIMEOUT).end(); }
	public void requestTimeout(Buffer buffer) { status(HttpResponseStatus.REQUEST_TIMEOUT).end(buffer); }
	public void requestTimeout(Object object,  Charset charset) { status(HttpResponseStatus.REQUEST_TIMEOUT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void requestTimeout(String content, Charset charset) { status(HttpResponseStatus.REQUEST_TIMEOUT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void requestTimeout(Object object) { success(object, StandardCharsets.UTF_8); }
	public void requestTimeout(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void conflict() { status(HttpResponseStatus.CONFLICT).end(); }
	public void conflict(Buffer buffer) { status(HttpResponseStatus.CONFLICT).end(buffer); }
	public void conflict(Object object,  Charset charset) { status(HttpResponseStatus.CONFLICT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void conflict(String content, Charset charset) { status(HttpResponseStatus.CONFLICT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void conflict(Object object) { success(object, StandardCharsets.UTF_8); }
	public void conflict(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void gone() { status(HttpResponseStatus.GONE).end(); }
	public void gone(Buffer buffer) { status(HttpResponseStatus.GONE).end(buffer); }
	public void gone(Object object,  Charset charset) { status(HttpResponseStatus.GONE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void gone(String content, Charset charset) { status(HttpResponseStatus.GONE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void gone(Object object) { success(object, StandardCharsets.UTF_8); }
	public void gone(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void lengthRequired() { status(HttpResponseStatus.LENGTH_REQUIRED).end(); }
	public void lengthRequired(Buffer buffer) { status(HttpResponseStatus.LENGTH_REQUIRED).end(buffer); }
	public void lengthRequired(Object object,  Charset charset) { status(HttpResponseStatus.LENGTH_REQUIRED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void lengthRequired(String content, Charset charset) { status(HttpResponseStatus.LENGTH_REQUIRED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void lengthRequired(Object object) { success(object, StandardCharsets.UTF_8); }
	public void lengthRequired(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void preConditionFailed() { status(HttpResponseStatus.PRECONDITION_FAILED).end(); }
	public void preConditionFailed(Buffer buffer) { status(HttpResponseStatus.PRECONDITION_FAILED).end(buffer); }
	public void preConditionFailed(Object object,  Charset charset) { status(HttpResponseStatus.PRECONDITION_FAILED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void preConditionFailed(String content, Charset charset) { status(HttpResponseStatus.PRECONDITION_FAILED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void preConditionFailed(Object object) { success(object, StandardCharsets.UTF_8); }
	public void preConditionFailed(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void requestEntityTooLarge() { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(); }
	public void requestEntityTooLarge(Buffer buffer) { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(buffer); }
	public void requestEntityTooLarge(Object object,  Charset charset) { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void requestEntityTooLarge(String content, Charset charset) { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void requestEntityTooLarge(Object object) { success(object, StandardCharsets.UTF_8); }
	public void requestEntityTooLarge(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void requestUtiTooLong() { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(); }
	public void requestUtiTooLong(Buffer buffer) { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(buffer); }
	public void requestUtiTooLong(Object object,  Charset charset) { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void requestUtiTooLong(String content, Charset charset) { status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void requestUtiTooLong(Object object) { success(object, StandardCharsets.UTF_8); }
	public void requestUtiTooLong(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void unsupportedMediaType() { status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE).end(); }
	public void unsupportedMediaType(Buffer buffer) { status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE).end(buffer); }
	public void unsupportedMediaType(Object object,  Charset charset) { status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void unsupportedMediaType(String content, Charset charset) { status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void unsupportedMediaType(Object object) { success(object, StandardCharsets.UTF_8); }
	public void unsupportedMediaType(String content) { success(content, StandardCharsets.UTF_8); }
	
	
	public void requestRangeNotSatisfiable() { status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE).end(); }
	public void requestRangeNotSatisfiable(Buffer buffer) { status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE).end(buffer); }
	public void requestRangeNotSatisfiable(Object object,  Charset charset) { status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void requestRangeNotSatisfiable(String content, Charset charset) { status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void requestRangeNotSatisfiable(Object object) { success(object, StandardCharsets.UTF_8); }
	public void requestRangeNotSatisfiable(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void expectationFailed() { status(HttpResponseStatus.EXPECTATION_FAILED).end(); }
	public void expectationFailed(Buffer buffer) { status(HttpResponseStatus.EXPECTATION_FAILED).end(buffer); }
	public void expectationFailed(Object object,  Charset charset) { status(HttpResponseStatus.EXPECTATION_FAILED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void expectationFailed(String content, Charset charset) { status(HttpResponseStatus.EXPECTATION_FAILED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void expectationFailed(Object object) { success(object, StandardCharsets.UTF_8); }
	public void expectationFailed(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void misdirectedRequest() { status(HttpResponseStatus.MISDIRECTED_REQUEST).end(); }
	public void misdirectedRequest(Buffer buffer) { status(HttpResponseStatus.MISDIRECTED_REQUEST).end(buffer); }
	public void misdirectedRequest(Object object,  Charset charset) { status(HttpResponseStatus.MISDIRECTED_REQUEST); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void misdirectedRequest(String content, Charset charset) { status(HttpResponseStatus.MISDIRECTED_REQUEST); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void misdirectedRequest(Object object) { success(object, StandardCharsets.UTF_8); }
	public void misdirectedRequest(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void unprocessableEntity() { status(HttpResponseStatus.UNPROCESSABLE_ENTITY).end(); }
	public void unprocessableEntity(Buffer buffer) { status(HttpResponseStatus.UNPROCESSABLE_ENTITY).end(buffer); }
	public void unprocessableEntity(Object object,  Charset charset) { status(HttpResponseStatus.UNPROCESSABLE_ENTITY); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void unprocessableEntity(String content, Charset charset) { status(HttpResponseStatus.UNPROCESSABLE_ENTITY); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void unprocessableEntity(Object object) { success(object, StandardCharsets.UTF_8); }
	public void unprocessableEntity(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void locked() { status(HttpResponseStatus.LOCKED).end(); }
	public void locked(Buffer buffer) { status(HttpResponseStatus.LOCKED).end(buffer); }
	public void locked(Object object,  Charset charset) { status(HttpResponseStatus.LOCKED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void locked(String content, Charset charset) { status(HttpResponseStatus.LOCKED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void locked(Object object) { success(object, StandardCharsets.UTF_8); }
	public void locked(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void failedDependency() { status(HttpResponseStatus.FAILED_DEPENDENCY).end(); }
	public void failedDependency(Buffer buffer) { status(HttpResponseStatus.FAILED_DEPENDENCY).end(buffer); }
	public void failedDependency(Object object,  Charset charset) { status(HttpResponseStatus.FAILED_DEPENDENCY); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void failedDependency(String content, Charset charset) { status(HttpResponseStatus.FAILED_DEPENDENCY); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void failedDependency(Object object) { success(object, StandardCharsets.UTF_8); }
	public void failedDependency(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void unorderedCollection() { status(HttpResponseStatus.UNORDERED_COLLECTION).end(); }
	public void unorderedCollection(Buffer buffer) { status(HttpResponseStatus.UNORDERED_COLLECTION).end(buffer); }
	public void unorderedCollection(Object object,  Charset charset) { status(HttpResponseStatus.UNORDERED_COLLECTION); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void unorderedCollection(String content, Charset charset) { status(HttpResponseStatus.UNORDERED_COLLECTION); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void unorderedCollection(Object object) { success(object, StandardCharsets.UTF_8); }
	public void unorderedCollection(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void upgradeRequired() { status(HttpResponseStatus.UPGRADE_REQUIRED).end(); }
	public void upgradeRequired(Buffer buffer) { status(HttpResponseStatus.UPGRADE_REQUIRED).end(buffer); }
	public void upgradeRequired(Object object,  Charset charset) { status(HttpResponseStatus.UPGRADE_REQUIRED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void upgradeRequired(String content, Charset charset) { status(HttpResponseStatus.UPGRADE_REQUIRED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void upgradeRequired(Object object) { success(object, StandardCharsets.UTF_8); }
	public void upgradeRequired(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void preconditionRequired() { status(HttpResponseStatus.PRECONDITION_REQUIRED).end(); }
	public void preconditionRequired(Buffer buffer) { status(HttpResponseStatus.PRECONDITION_REQUIRED).end(buffer); }
	public void preconditionRequired(Object object,  Charset charset) { status(HttpResponseStatus.PRECONDITION_REQUIRED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void preconditionRequired(String content, Charset charset) { status(HttpResponseStatus.PRECONDITION_REQUIRED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void preconditionRequired(Object object) { success(object, StandardCharsets.UTF_8); }
	public void preconditionRequired(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void tooManyRequest() { status(HttpResponseStatus.TOO_MANY_REQUESTS).end(); }
	public void tooManyRequest(Buffer buffer) { status(HttpResponseStatus.TOO_MANY_REQUESTS).end(buffer); }
	public void tooManyRequest(Object object,  Charset charset) { status(HttpResponseStatus.TOO_MANY_REQUESTS); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void tooManyRequest(String content, Charset charset) { status(HttpResponseStatus.TOO_MANY_REQUESTS); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void tooManyRequest(Object object) { success(object, StandardCharsets.UTF_8); }
	public void tooManyRequest(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void requestHeaderFieldsTooLarge() { status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).end(); }
	public void requestHeaderFieldsTooLarge(Buffer buffer) { status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).end(buffer); }
	public void requestHeaderFieldsTooLarge(Object object,  Charset charset) { status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void requestHeaderFieldsTooLarge(String content, Charset charset) { status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void requestHeaderFieldsTooLarge(Object object) { success(object, StandardCharsets.UTF_8); }
	public void requestHeaderFieldsTooLarge(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void internalServerError() { status(HttpResponseStatus.INTERNAL_SERVER_ERROR).end(); }
	public void internalServerError(Buffer buffer) { status(HttpResponseStatus.INTERNAL_SERVER_ERROR).end(buffer); }
	public void internalServerError(Object object,  Charset charset) { status(HttpResponseStatus.INTERNAL_SERVER_ERROR); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void internalServerError(String content, Charset charset) { status(HttpResponseStatus.INTERNAL_SERVER_ERROR); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void internalServerError(Object object) { success(object, StandardCharsets.UTF_8); }
	public void internalServerError(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void error() { internalServerError(); }
	public void error(Buffer buffer) { internalServerError(buffer); }
	public void error(Object object,  Charset charset) { internalServerError(object, charset);}
	public void error(String content, Charset charset) { internalServerError(content, charset); }
	public void error(Object object) { internalServerError(object);}
	public void error(String content) { internalServerError(content); }
	
	public void notImplemented() { status(HttpResponseStatus.NOT_IMPLEMENTED).end(); }
	public void notImplemented(Buffer buffer) { status(HttpResponseStatus.NOT_IMPLEMENTED).end(buffer); }
	public void notImplemented(Object object,  Charset charset) { status(HttpResponseStatus.NOT_IMPLEMENTED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void notImplemented(String content, Charset charset) { status(HttpResponseStatus.NOT_IMPLEMENTED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void notImplemented(Object object) { success(object, StandardCharsets.UTF_8); }
	public void notImplemented(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void badGateway() { status(HttpResponseStatus.BAD_GATEWAY).end(); }
	public void badGateway(Buffer buffer) { status(HttpResponseStatus.BAD_GATEWAY).end(buffer); }
	public void badGateway(Object object,  Charset charset) { status(HttpResponseStatus.BAD_GATEWAY); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void badGateway(String content, Charset charset) { status(HttpResponseStatus.BAD_GATEWAY); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void badGateway(Object object) { success(object, StandardCharsets.UTF_8); }
	public void badGateway(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void serviceUnavailable() { status(HttpResponseStatus.SERVICE_UNAVAILABLE).end(); }
	public void serviceUnavailable(Buffer buffer) { status(HttpResponseStatus.SERVICE_UNAVAILABLE).end(buffer); }
	public void serviceUnavailable(Object object,  Charset charset) { status(HttpResponseStatus.SERVICE_UNAVAILABLE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void serviceUnavailable(String content, Charset charset) { status(HttpResponseStatus.SERVICE_UNAVAILABLE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void serviceUnavailable(Object object) { success(object, StandardCharsets.UTF_8); }
	public void serviceUnavailable(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void gatewayTimeout() { status(HttpResponseStatus.GATEWAY_TIMEOUT).end(); }
	public void gatewayTimeout(Buffer buffer) { status(HttpResponseStatus.GATEWAY_TIMEOUT).end(buffer); }
	public void gatewayTimeout(Object object,  Charset charset) { status(HttpResponseStatus.GATEWAY_TIMEOUT); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void gatewayTimeout(String content, Charset charset) { status(HttpResponseStatus.GATEWAY_TIMEOUT); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void gatewayTimeout(Object object) { success(object, StandardCharsets.UTF_8); }
	public void gatewayTimeout(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void httpVersionNotSupported() { status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED).end(); }
	public void httpVersionNotSupported(Buffer buffer) { status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED).end(buffer); }
	public void httpVersionNotSupported(Object object,  Charset charset) { status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void httpVersionNotSupported(String content, Charset charset) { status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void httpVersionNotSupported(Object object) { success(object, StandardCharsets.UTF_8); }
	public void httpVersionNotSupported(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void variantAlsoNegotiates() { status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES).end(); }
	public void variantAlsoNegotiates(Buffer buffer) { status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES).end(buffer); }
	public void variantAlsoNegotiates(Object object,  Charset charset) { status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void variantAlsoNegotiates(String content, Charset charset) { status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void variantAlsoNegotiates(Object object) { success(object, StandardCharsets.UTF_8); }
	public void variantAlsoNegotiates(String content) { success(content, StandardCharsets.UTF_8); }
	
	
	public void insufficientStorage() { status(HttpResponseStatus.INSUFFICIENT_STORAGE).end(); }
	public void insufficientStorage(Buffer buffer) { status(HttpResponseStatus.INSUFFICIENT_STORAGE).end(buffer); }
	public void insufficientStorage(Object object,  Charset charset) { status(HttpResponseStatus.INSUFFICIENT_STORAGE); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void insufficientStorage(String content, Charset charset) { status(HttpResponseStatus.INSUFFICIENT_STORAGE); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void insufficientStorage(Object object) { success(object, StandardCharsets.UTF_8); }
	public void insufficientStorage(String content) { success(content, StandardCharsets.UTF_8); }
	
	public void notExtended() { status(HttpResponseStatus.NOT_EXTENDED).end(); }
	public void notExtended(Buffer buffer) { status(HttpResponseStatus.NOT_EXTENDED).end(buffer); }
	public void notExtended(Object object,  Charset charset) { status(HttpResponseStatus.NOT_EXTENDED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void notExtended(String content, Charset charset) { status(HttpResponseStatus.NOT_EXTENDED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void notExtended(Object object) { success(object, StandardCharsets.UTF_8); }
	public void notExtended(String content) { success(content, StandardCharsets.UTF_8); }
	
	
	public void networkAuthenticationRequired() { status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED).end(); }
	public void networkAuthenticationRequired(Buffer buffer) { status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED).end(buffer); }
	public void networkAuthenticationRequired(Object object,  Charset charset) { status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED); contentType("application/json"); res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name()); end();}
	public void networkAuthenticationRequired(String content, Charset charset) { status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED); if (isValidJson(content)) { contentType("application/json"); } res.setChunked(true); res.write(content, charset.name()); end();}
	public void networkAuthenticationRequired(Object object) { success(object, StandardCharsets.UTF_8); }
	public void networkAuthenticationRequired(String content) { success(content, StandardCharsets.UTF_8); }
	
	protected String transform(Object object){
		
		return Json.encode(object);
	}
	
	protected String body() {

		return ctx.getBodyAsString();
	}

	protected boolean isValidJson(String content) {
		try {
			Json.decodeValue(content, Object.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
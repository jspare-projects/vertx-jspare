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

/**
 * Use as helper to handle one Route invocation.
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
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

  /**
   * Accepted.
   */
  protected void accepted() {
    status(HttpResponseStatus.ACCEPTED).end();
  }

  /**
   * Accepted.
   *
   * @param buffer
   *          the buffer
   */
  protected void accepted(Buffer buffer) {
    status(HttpResponseStatus.ACCEPTED).end(buffer);
  }

  /**
   * Accepted.
   *
   * @param object
   *          the object
   */
  protected void accepted(Object object) {
    accepted(object, StandardCharsets.UTF_8);
  }

  /**
   * Accepted.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void accepted(Object object, Charset charset) {
    status(HttpResponseStatus.ACCEPTED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Accepted.
   *
   * @param content
   *          the content
   */
  protected void accepted(String content) {
    accepted(content, StandardCharsets.UTF_8);
  }

  /**
   * Accepted.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void accepted(String content, Charset charset) {
    status(HttpResponseStatus.ACCEPTED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Bad gateway.
   */
  protected void badGateway() {
    status(HttpResponseStatus.BAD_GATEWAY).end();
  }

  /**
   * Bad gateway.
   *
   * @param buffer
   *          the buffer
   */
  protected void badGateway(Buffer buffer) {
    status(HttpResponseStatus.BAD_GATEWAY).end(buffer);
  }

  /**
   * Bad gateway.
   *
   * @param object
   *          the object
   */
  protected void badGateway(Object object) {
    badGateway(object, StandardCharsets.UTF_8);
  }

  /**
   * Bad gateway.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void badGateway(Object object, Charset charset) {
    status(HttpResponseStatus.BAD_GATEWAY);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Bad gateway.
   *
   * @param content
   *          the content
   */
  protected void badGateway(String content) {
    badGateway(content, StandardCharsets.UTF_8);
  }

  /**
   * Bad gateway.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void badGateway(String content, Charset charset) {
    status(HttpResponseStatus.BAD_GATEWAY);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Bad request.
   */
  protected void badRequest() {
    status(HttpResponseStatus.BAD_REQUEST).end();
  }

  /**
   * Bad request.
   *
   * @param buffer
   *          the buffer
   */
  protected void badRequest(Buffer buffer) {
    status(HttpResponseStatus.BAD_REQUEST).end(buffer);
  }

  /**
   * Bad request.
   *
   * @param object
   *          the object
   */
  protected void badRequest(Object object) {
    badRequest(object, StandardCharsets.UTF_8);
  }

  /**
   * Bad request.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void badRequest(Object object, Charset charset) {
    status(HttpResponseStatus.BAD_REQUEST);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Bad request.
   *
   * @param content
   *          the content
   */
  protected void badRequest(String content) {
    badRequest(content, StandardCharsets.UTF_8);
  }

  /**
   * Bad request.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void badRequest(String content, Charset charset) {
    status(HttpResponseStatus.BAD_REQUEST);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Body.
   *
   * @return the string
   */
  protected String body() {

    return context.getBodyAsString();
  }

  /**
   * Conflict.
   */
  protected void conflict() {
    status(HttpResponseStatus.CONFLICT).end();
  }

  /**
   * Conflict.
   *
   * @param buffer
   *          the buffer
   */
  protected void conflict(Buffer buffer) {
    status(HttpResponseStatus.CONFLICT).end(buffer);
  }

  /**
   * Conflict.
   *
   * @param object
   *          the object
   */
  protected void conflict(Object object) {
    conflict(object, StandardCharsets.UTF_8);
  }

  /**
   * Conflict.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void conflict(Object object, Charset charset) {
    status(HttpResponseStatus.CONFLICT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Conflict.
   *
   * @param content
   *          the content
   */
  protected void conflict(String content) {
    conflict(content, StandardCharsets.UTF_8);
  }

  /**
   * Conflict.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void conflict(String content, Charset charset) {
    conflict(HttpResponseStatus.CONFLICT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Content type.
   *
   * @param contentType
   *          the content type
   * @return the http server response
   */
  protected HttpServerResponse contentType(String contentType) {
    res.putHeader("content-type", contentType);
    return res;
  }

  /**
   * Continue it.
   */
  protected void continueIt() {
    status(HttpResponseStatus.CONTINUE).end();
  }

  /**
   * Continue it.
   *
   * @param buffer
   *          the buffer
   */
  protected void continueIt(Buffer buffer) {
    status(HttpResponseStatus.CONTINUE).end(buffer);
  }

  /**
   * Continue it.
   *
   * @param object
   *          the object
   */
  protected void continueIt(Object object) {
    continueIt(object, StandardCharsets.UTF_8);
  }

  /**
   * Continue it.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void continueIt(Object object, Charset charset) {
    status(HttpResponseStatus.CONTINUE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Continue it.
   *
   * @param content
   *          the content
   */
  protected void continueIt(String content) {
    continueIt(content, StandardCharsets.UTF_8);
  }

  /**
   * Continue it.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void continueIt(String content, Charset charset) {
    status(HttpResponseStatus.CONTINUE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Cookies.
   *
   * @return the sets the
   */
  protected Set<Cookie> cookies() {

    return context.cookies();
  }

  /**
   * Created.
   */
  protected void created() {
    status(HttpResponseStatus.CREATED).end();
  }

  /**
   * Created.
   *
   * @param buffer
   *          the buffer
   */
  protected void created(Buffer buffer) {
    status(HttpResponseStatus.CREATED).end(buffer);
  }

  /**
   * Created.
   *
   * @param object
   *          the object
   */
  protected void created(Object object) {
    created(object, StandardCharsets.UTF_8);
  }

  /**
   * Created.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void created(Object object, Charset charset) {
    status(HttpResponseStatus.CREATED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Created.
   *
   * @param content
   *          the content
   */
  protected void created(String content) {
    created(content, StandardCharsets.UTF_8);
  }

  /**
   * Created.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void created(String content, Charset charset) {
    created(HttpResponseStatus.CREATED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * End.
   */
  protected void end() {

    if (!res.ended()) {

      res.end();
    }
  }

  /**
   * End.
   *
   * @param buffer
   *          the buffer
   */
  protected void end(Buffer buffer) {

    if (!res.ended()) {

      res.end(buffer);
    }
  }

  /**
   * End.
   *
   * @param jsonObject
   *          the json object
   */
  protected void end(JsonObject jsonObject) {

    if (!res.ended()) {

      res.end(jsonObject.encode());
    }
  }

  /**
   * Error.
   */
  protected void error() {
    internalServerError();
  }

  /**
   * Error.
   *
   * @param buffer
   *          the buffer
   */
  protected void error(Buffer buffer) {
    internalServerError(buffer);
  }

  /**
   * Error.
   *
   * @param object
   *          the object
   */
  protected void error(Object object) {
    internalServerError(object);
  }

  /**
   * Error.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void error(Object object, Charset charset) {
    internalServerError(object, charset);
  }

  /**
   * Error.
   *
   * @param content
   *          the content
   */
  protected void error(String content) {
    internalServerError(content);
  }

  /**
   * Error.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void error(String content, Charset charset) {
    internalServerError(content, charset);
  }

  /**
   * Expectation failed.
   */
  protected void expectationFailed() {
    status(HttpResponseStatus.EXPECTATION_FAILED).end();
  }

  /**
   * Expectation failed.
   *
   * @param buffer
   *          the buffer
   */
  protected void expectationFailed(Buffer buffer) {
    status(HttpResponseStatus.EXPECTATION_FAILED).end(buffer);
  }

  /**
   * Expectation failed.
   *
   * @param object
   *          the object
   */
  protected void expectationFailed(Object object) {
    expectationFailed(object, StandardCharsets.UTF_8);
  }

  /**
   * Expectation failed.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void expectationFailed(Object object, Charset charset) {
    status(HttpResponseStatus.EXPECTATION_FAILED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Expectation failed.
   *
   * @param content
   *          the content
   */
  protected void expectationFailed(String content) {
    expectationFailed(content, StandardCharsets.UTF_8);
  }

  /**
   * Expectation failed.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void expectationFailed(String content, Charset charset) {
    status(HttpResponseStatus.EXPECTATION_FAILED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Failed dependency.
   */
  protected void failedDependency() {
    status(HttpResponseStatus.FAILED_DEPENDENCY).end();
  }

  /**
   * Failed dependency.
   *
   * @param buffer
   *          the buffer
   */
  protected void failedDependency(Buffer buffer) {
    status(HttpResponseStatus.FAILED_DEPENDENCY).end(buffer);
  }

  /**
   * Failed dependency.
   *
   * @param object
   *          the object
   */
  protected void failedDependency(Object object) {
    failedDependency(object, StandardCharsets.UTF_8);
  }

  /**
   * Failed dependency.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void failedDependency(Object object, Charset charset) {
    status(HttpResponseStatus.FAILED_DEPENDENCY);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Failed dependency.
   *
   * @param content
   *          the content
   */
  protected void failedDependency(String content) {
    failedDependency(content, StandardCharsets.UTF_8);
  }

  /**
   * Failed dependency.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void failedDependency(String content, Charset charset) {
    status(HttpResponseStatus.FAILED_DEPENDENCY);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * File uploads.
   *
   * @return the sets the
   */
  protected Set<FileUpload> fileUploads() {

    return context.fileUploads();
  }

  /**
   * Forbidden.
   */
  protected void forbidden() {
    status(HttpResponseStatus.FORBIDDEN).end();
  }

  /**
   * Forbidden.
   *
   * @param buffer
   *          the buffer
   */
  protected void forbidden(Buffer buffer) {
    status(HttpResponseStatus.FORBIDDEN).end(buffer);
  }

  /**
   * Forbidden.
   *
   * @param object
   *          the object
   */
  protected void forbidden(Object object) {
    forbidden(object, StandardCharsets.UTF_8);
  }

  /**
   * Forbidden.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void forbidden(Object object, Charset charset) {
    status(HttpResponseStatus.FORBIDDEN);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Forbidden.
   *
   * @param content
   *          the content
   */
  protected void forbidden(String content) {
    forbidden(content, StandardCharsets.UTF_8);
  }

  /**
   * Forbidden.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void forbidden(String content, Charset charset) {
    status(HttpResponseStatus.FORBIDDEN);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Found.
   */
  protected void found() {
    status(HttpResponseStatus.FOUND).end();
  }

  /**
   * Found.
   *
   * @param buffer
   *          the buffer
   */
  protected void found(Buffer buffer) {
    status(HttpResponseStatus.FOUND).end(buffer);
  }

  /**
   * Found.
   *
   * @param object
   *          the object
   */
  protected void found(Object object) {
    found(object, StandardCharsets.UTF_8);
  }

  /**
   * Found.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void found(Object object, Charset charset) {
    status(HttpResponseStatus.FOUND);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Found.
   *
   * @param content
   *          the content
   */
  protected void found(String content) {
    found(content, StandardCharsets.UTF_8);
  }

  /**
   * Found.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void found(String content, Charset charset) {
    status(HttpResponseStatus.FOUND);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Gateway timeout.
   */
  protected void gatewayTimeout() {
    status(HttpResponseStatus.GATEWAY_TIMEOUT).end();
  }

  /**
   * Gateway timeout.
   *
   * @param buffer
   *          the buffer
   */
  protected void gatewayTimeout(Buffer buffer) {
    status(HttpResponseStatus.GATEWAY_TIMEOUT).end(buffer);
  }

  /**
   * Gateway timeout.
   *
   * @param object
   *          the object
   */
  protected void gatewayTimeout(Object object) {
    gatewayTimeout(object, StandardCharsets.UTF_8);
  }

  /**
   * Gateway timeout.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void gatewayTimeout(Object object, Charset charset) {
    status(HttpResponseStatus.GATEWAY_TIMEOUT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Gateway timeout.
   *
   * @param content
   *          the content
   */
  protected void gatewayTimeout(String content) {
    gatewayTimeout(content, StandardCharsets.UTF_8);
  }

  /**
   * Gateway timeout.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void gatewayTimeout(String content, Charset charset) {
    status(HttpResponseStatus.GATEWAY_TIMEOUT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Gets the cookie.
   *
   * @param name
   *          the name
   * @return the cookie
   */
  protected Cookie getCookie(String name) {

    return context.getCookie(name);
  }

  /**
   * Gets the header.
   *
   * @param name
   *          the name
   * @return the header
   */
  protected Optional<String> getHeader(String name) {

    return Optional.ofNullable(req.getHeader(name));
  }

  /**
   * Gets the parameter.
   *
   * @param name
   *          the name
   * @return the parameter
   */
  protected String getParameter(String name) {

    return req.getParam(name);
  }

  /**
   * Gets the principal.
   *
   * @return the principal
   */
  protected JsonObject getPrincipal() {

    if (context.user() == null) {

      return null;
    }
    return context.user().principal();
  }

  /**
   * Gets the user.
   *
   * @return the user
   */
  protected User getUser() {

    return context.user();
  }

  /**
   * Gone.
   */
  protected void gone() {
    status(HttpResponseStatus.GONE).end();
  }

  /**
   * Gone.
   *
   * @param buffer
   *          the buffer
   */
  protected void gone(Buffer buffer) {
    status(HttpResponseStatus.GONE).end(buffer);
  }

  /**
   * Gone.
   *
   * @param object
   *          the object
   */
  protected void gone(Object object) {
    gone(object, StandardCharsets.UTF_8);
  }

  /**
   * Gone.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void gone(Object object, Charset charset) {
    status(HttpResponseStatus.GONE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Gone.
   *
   * @param content
   *          the content
   */
  protected void gone(String content) {
    gone(content, StandardCharsets.UTF_8);
  }

  /**
   * Gone.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void gone(String content, Charset charset) {
    status(HttpResponseStatus.GONE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Http version not supported.
   */
  protected void httpVersionNotSupported() {
    status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED).end();
  }

  /**
   * Http version not supported.
   *
   * @param buffer
   *          the buffer
   */
  protected void httpVersionNotSupported(Buffer buffer) {
    status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED).end(buffer);
  }

  /**
   * Http version not supported.
   *
   * @param object
   *          the object
   */
  protected void httpVersionNotSupported(Object object) {
    httpVersionNotSupported(object, StandardCharsets.UTF_8);
  }

  /**
   * Http version not supported.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void httpVersionNotSupported(Object object, Charset charset) {
    status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Http version not supported.
   *
   * @param content
   *          the content
   */
  protected void httpVersionNotSupported(String content) {
    httpVersionNotSupported(content, StandardCharsets.UTF_8);
  }

  /**
   * Http version not supported.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void httpVersionNotSupported(String content, Charset charset) {
    status(HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Insufficient storage.
   */
  protected void insufficientStorage() {
    status(HttpResponseStatus.INSUFFICIENT_STORAGE).end();
  }

  /**
   * Insufficient storage.
   *
   * @param buffer
   *          the buffer
   */
  protected void insufficientStorage(Buffer buffer) {
    status(HttpResponseStatus.INSUFFICIENT_STORAGE).end(buffer);
  }

  /**
   * Insufficient storage.
   *
   * @param object
   *          the object
   */
  protected void insufficientStorage(Object object) {
    insufficientStorage(object, StandardCharsets.UTF_8);
  }

  /**
   * Insufficient storage.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void insufficientStorage(Object object, Charset charset) {
    status(HttpResponseStatus.INSUFFICIENT_STORAGE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Insufficient storage.
   *
   * @param content
   *          the content
   */
  protected void insufficientStorage(String content) {
    insufficientStorage(content, StandardCharsets.UTF_8);
  }

  /**
   * Insufficient storage.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void insufficientStorage(String content, Charset charset) {
    status(HttpResponseStatus.INSUFFICIENT_STORAGE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Internal server error.
   */
  protected void internalServerError() {
    status(HttpResponseStatus.INTERNAL_SERVER_ERROR).end();
  }

  /**
   * Internal server error.
   *
   * @param buffer
   *          the buffer
   */
  protected void internalServerError(Buffer buffer) {
    status(HttpResponseStatus.INTERNAL_SERVER_ERROR).end(buffer);
  }

  /**
   * Internal server error.
   *
   * @param object
   *          the object
   */
  protected void internalServerError(Object object) {
    internalServerError(object, StandardCharsets.UTF_8);
  }

  /**
   * Internal server error.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void internalServerError(Object object, Charset charset) {
    status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Internal server error.
   *
   * @param content
   *          the content
   */
  protected void internalServerError(String content) {
    internalServerError(content, StandardCharsets.UTF_8);
  }

  /**
   * Internal server error.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void internalServerError(String content, Charset charset) {
    status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Checks if is authorised.
   *
   * @param permissions
   *          the permissions
   * @param resultHandler
   *          the result handler
   */
  protected void isAuthorised(List<String> permissions, Handler<Void> resultHandler) {

    List<Future<Boolean>> futures = permissions.stream().map(p -> {
      Future<Boolean> future = Future.future();
      context.user().isAuthorised(p, future.completer());
      return future;
    }).collect(Collectors.toList());

    FutureSupplier.sequenceFuture(futures).setHandler(resultList -> {

      if (resultList.succeeded()) {

        boolean isAuthorized = resultList.result().stream().reduce(false, (a, b) -> {
          if (a) {
            return a;
          }
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

  /**
   * Checks if is authorised.
   *
   * @param authority
   *          the authority
   * @param resultHandler
   *          the result handler
   */
  protected void isAuthorised(String authority, Handler<AsyncResult<Boolean>> resultHandler) {

    context.user().isAuthorised(authority, resultHandler);
  }

  /**
   * Checks if is valid json.
   *
   * @param content
   *          the content
   * @return true, if is valid json
   */
  protected boolean isValidJson(String content) {
    try {
      Json.decodeValue(content, Object.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Length required.
   */
  protected void lengthRequired() {
    status(HttpResponseStatus.LENGTH_REQUIRED).end();
  }

  /**
   * Length required.
   *
   * @param buffer
   *          the buffer
   */
  protected void lengthRequired(Buffer buffer) {
    status(HttpResponseStatus.LENGTH_REQUIRED).end(buffer);
  }

  /**
   * Length required.
   *
   * @param object
   *          the object
   */
  protected void lengthRequired(Object object) {
    lengthRequired(object, StandardCharsets.UTF_8);
  }

  /**
   * Length required.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void lengthRequired(Object object, Charset charset) {
    status(HttpResponseStatus.LENGTH_REQUIRED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Length required.
   *
   * @param content
   *          the content
   */
  protected void lengthRequired(String content) {
    lengthRequired(content, StandardCharsets.UTF_8);
  }

  /**
   * Length required.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void lengthRequired(String content, Charset charset) {
    status(HttpResponseStatus.LENGTH_REQUIRED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Locked.
   */
  protected void locked() {
    status(HttpResponseStatus.LOCKED).end();
  }

  /**
   * Locked.
   *
   * @param buffer
   *          the buffer
   */
  protected void locked(Buffer buffer) {
    status(HttpResponseStatus.LOCKED).end(buffer);
  }

  /**
   * Locked.
   *
   * @param object
   *          the object
   */
  protected void locked(Object object) {
    locked(object, StandardCharsets.UTF_8);
  }

  /**
   * Locked.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void locked(Object object, Charset charset) {
    status(HttpResponseStatus.LOCKED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Locked.
   *
   * @param content
   *          the content
   */
  protected void locked(String content) {
    locked(content, StandardCharsets.UTF_8);
  }

  /**
   * Locked.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void locked(String content, Charset charset) {
    status(HttpResponseStatus.LOCKED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Method not allowed.
   */
  protected void methodNotAllowed() {
    status(HttpResponseStatus.METHOD_NOT_ALLOWED).end();
  }

  /**
   * Method not allowed.
   *
   * @param buffer
   *          the buffer
   */
  protected void methodNotAllowed(Buffer buffer) {
    status(HttpResponseStatus.METHOD_NOT_ALLOWED).end(buffer);
  }

  /**
   * Method not allowed.
   *
   * @param object
   *          the object
   */
  protected void methodNotAllowed(Object object) {
    methodNotAllowed(object, StandardCharsets.UTF_8);
  }

  /**
   * Method not allowed.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void methodNotAllowed(Object object, Charset charset) {
    status(HttpResponseStatus.METHOD_NOT_ALLOWED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Method not allowed.
   *
   * @param content
   *          the content
   */
  protected void methodNotAllowed(String content) {
    methodNotAllowed(content, StandardCharsets.UTF_8);
  }

  /**
   * Method not allowed.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void methodNotAllowed(String content, Charset charset) {
    status(HttpResponseStatus.METHOD_NOT_ALLOWED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Misdirected request.
   */
  protected void misdirectedRequest() {
    status(HttpResponseStatus.MISDIRECTED_REQUEST).end();
  }

  /**
   * Misdirected request.
   *
   * @param buffer
   *          the buffer
   */
  protected void misdirectedRequest(Buffer buffer) {
    status(HttpResponseStatus.MISDIRECTED_REQUEST).end(buffer);
  }

  /**
   * Misdirected request.
   *
   * @param object
   *          the object
   */
  protected void misdirectedRequest(Object object) {
    misdirectedRequest(object, StandardCharsets.UTF_8);
  }

  /**
   * Misdirected request.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void misdirectedRequest(Object object, Charset charset) {
    status(HttpResponseStatus.MISDIRECTED_REQUEST);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Misdirected request.
   *
   * @param content
   *          the content
   */
  protected void misdirectedRequest(String content) {
    misdirectedRequest(content, StandardCharsets.UTF_8);
  }

  /**
   * Misdirected request.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void misdirectedRequest(String content, Charset charset) {
    status(HttpResponseStatus.MISDIRECTED_REQUEST);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Moved permanently.
   */
  protected void movedPermanently() {
    status(HttpResponseStatus.MOVED_PERMANENTLY).end();
  }

  /**
   * Moved permanently.
   *
   * @param buffer
   *          the buffer
   */
  protected void movedPermanently(Buffer buffer) {
    status(HttpResponseStatus.MOVED_PERMANENTLY).end(buffer);
  }

  /**
   * Moved permanently.
   *
   * @param object
   *          the object
   */
  protected void movedPermanently(Object object) {
    movedPermanently(object, StandardCharsets.UTF_8);
  }

  /**
   * Moved permanently.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void movedPermanently(Object object, Charset charset) {
    status(HttpResponseStatus.MOVED_PERMANENTLY);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Moved permanently.
   *
   * @param content
   *          the content
   */
  protected void movedPermanently(String content) {
    movedPermanently(content, StandardCharsets.UTF_8);
  }

  /**
   * Moved permanently.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void movedPermanently(String content, Charset charset) {
    status(HttpResponseStatus.MOVED_PERMANENTLY);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Multiple choices.
   */
  protected void multipleChoices() {
    status(HttpResponseStatus.MULTIPLE_CHOICES).end();
  }

  /**
   * Multiple choices.
   *
   * @param buffer
   *          the buffer
   */
  protected void multipleChoices(Buffer buffer) {
    status(HttpResponseStatus.MULTIPLE_CHOICES).end(buffer);
  }

  /**
   * Multiple choices.
   *
   * @param object
   *          the object
   */
  protected void multipleChoices(Object object) {
    multipleChoices(object, StandardCharsets.UTF_8);
  }

  /**
   * Multiple choices.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void multipleChoices(Object object, Charset charset) {
    status(HttpResponseStatus.MULTIPLE_CHOICES);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Multiple choices.
   *
   * @param content
   *          the content
   */
  protected void multipleChoices(String content) {
    multipleChoices(content, StandardCharsets.UTF_8);
  }

  /**
   * Multiple choices.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void multipleChoices(String content, Charset charset) {
    status(HttpResponseStatus.MULTIPLE_CHOICES);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Multi status.
   */
  protected void multiStatus() {
    status(HttpResponseStatus.MULTI_STATUS).end();
  }

  /**
   * Multi status.
   *
   * @param buffer
   *          the buffer
   */
  protected void multiStatus(Buffer buffer) {
    status(HttpResponseStatus.MULTI_STATUS).end(buffer);
  }

  /**
   * Multi status.
   *
   * @param object
   *          the object
   */
  protected void multiStatus(Object object) {
    multiStatus(object, StandardCharsets.UTF_8);
  }

  /**
   * Multi status.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void multiStatus(Object object, Charset charset) {
    status(HttpResponseStatus.MULTI_STATUS);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Multi status.
   *
   * @param content
   *          the content
   */
  protected void multiStatus(String content) {
    multiStatus(content, StandardCharsets.UTF_8);
  }

  /**
   * Multi status.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void multiStatus(String content, Charset charset) {
    status(HttpResponseStatus.MULTI_STATUS);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Network authentication required.
   */
  protected void networkAuthenticationRequired() {
    status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED).end();
  }

  /**
   * Network authentication required.
   *
   * @param buffer
   *          the buffer
   */
  protected void networkAuthenticationRequired(Buffer buffer) {
    status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED).end(buffer);
  }

  /**
   * Network authentication required.
   *
   * @param object
   *          the object
   */
  protected void networkAuthenticationRequired(Object object) {
    networkAuthenticationRequired(object, StandardCharsets.UTF_8);
  }

  /**
   * Network authentication required.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void networkAuthenticationRequired(Object object, Charset charset) {
    status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Network authentication required.
   *
   * @param content
   *          the content
   */
  protected void networkAuthenticationRequired(String content) {
    networkAuthenticationRequired(content, StandardCharsets.UTF_8);
  }

  /**
   * Network authentication required.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void networkAuthenticationRequired(String content, Charset charset) {
    status(HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * No content.
   */
  protected void noContent() {
    status(HttpResponseStatus.NO_CONTENT).end();
  }

  /**
   * No content.
   *
   * @param buffer
   *          the buffer
   */
  protected void noContent(Buffer buffer) {
    status(HttpResponseStatus.NO_CONTENT).end(buffer);
  }

  /**
   * No content.
   *
   * @param object
   *          the object
   */
  protected void noContent(Object object) {
    noContent(object, StandardCharsets.UTF_8);
  }

  /**
   * No content.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void noContent(Object object, Charset charset) {
    status(HttpResponseStatus.NO_CONTENT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * No content.
   *
   * @param content
   *          the content
   */
  protected void noContent(String content) {
    noContent(content, StandardCharsets.UTF_8);
  }

  /**
   * No content.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void noContent(String content, Charset charset) {
    status(HttpResponseStatus.NO_CONTENT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Non authoratitative information.
   */
  protected void nonAuthoratitativeInformation() {
    status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION).end();
  }

  /**
   * Non authoratitative information.
   *
   * @param buffer
   *          the buffer
   */
  protected void nonAuthoratitativeInformation(Buffer buffer) {
    status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION).end(buffer);
  }

  /**
   * Non authoratitative information.
   *
   * @param object
   *          the object
   */
  protected void nonAuthoratitativeInformation(Object object) {
    nonAuthoratitativeInformation(object, StandardCharsets.UTF_8);
  }

  /**
   * Non authoratitative information.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void nonAuthoratitativeInformation(Object object, Charset charset) {
    status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Non authoratitative information.
   *
   * @param content
   *          the content
   */
  protected void nonAuthoratitativeInformation(String content) {
    nonAuthoratitativeInformation(content, StandardCharsets.UTF_8);
  }

  /**
   * Non authoratitative information.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void nonAuthoratitativeInformation(String content, Charset charset) {
    status(HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Not acceptable.
   */
  protected void notAcceptable() {
    status(HttpResponseStatus.NOT_ACCEPTABLE).end();
  }

  /**
   * Not acceptable.
   *
   * @param buffer
   *          the buffer
   */
  protected void notAcceptable(Buffer buffer) {
    status(HttpResponseStatus.NOT_ACCEPTABLE).end(buffer);
  }

  /**
   * Not acceptable.
   *
   * @param object
   *          the object
   */
  protected void notAcceptable(Object object) {
    notAcceptable(object, StandardCharsets.UTF_8);
  }

  /**
   * Not acceptable.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void notAcceptable(Object object, Charset charset) {
    status(HttpResponseStatus.NOT_ACCEPTABLE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Not acceptable.
   *
   * @param content
   *          the content
   */
  protected void notAcceptable(String content) {
    notAcceptable(content, StandardCharsets.UTF_8);
  }

  /**
   * Not acceptable.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void notAcceptable(String content, Charset charset) {
    status(HttpResponseStatus.NOT_ACCEPTABLE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Not extended.
   */
  protected void notExtended() {
    status(HttpResponseStatus.NOT_EXTENDED).end();
  }

  /**
   * Not extended.
   *
   * @param buffer
   *          the buffer
   */
  protected void notExtended(Buffer buffer) {
    status(HttpResponseStatus.NOT_EXTENDED).end(buffer);
  }

  /**
   * Not extended.
   *
   * @param object
   *          the object
   */
  protected void notExtended(Object object) {
    notExtended(object, StandardCharsets.UTF_8);
  }

  /**
   * Not extended.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void notExtended(Object object, Charset charset) {
    status(HttpResponseStatus.NOT_EXTENDED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Not extended.
   *
   * @param content
   *          the content
   */
  protected void notExtended(String content) {
    notExtended(content, StandardCharsets.UTF_8);
  }

  /**
   * Not extended.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void notExtended(String content, Charset charset) {
    status(HttpResponseStatus.NOT_EXTENDED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Not found.
   */
  protected void notFound() {
    status(HttpResponseStatus.NOT_FOUND).end();
  }

  /**
   * Not found.
   *
   * @param buffer
   *          the buffer
   */
  protected void notFound(Buffer buffer) {
    status(HttpResponseStatus.NOT_FOUND).end(buffer);
  }

  /**
   * Not found.
   *
   * @param object
   *          the object
   */
  protected void notFound(Object object) {
    notFound(object, StandardCharsets.UTF_8);
  }

  /**
   * Not found.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void notFound(Object object, Charset charset) {
    status(HttpResponseStatus.NOT_FOUND);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Not found.
   *
   * @param content
   *          the content
   */
  protected void notFound(String content) {
    notFound(content, StandardCharsets.UTF_8);
  }

  /**
   * Not found.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void notFound(String content, Charset charset) {
    status(HttpResponseStatus.NOT_FOUND);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Not implemented.
   */
  protected void notImplemented() {
    status(HttpResponseStatus.NOT_IMPLEMENTED).end();
  }

  /**
   * Not implemented.
   *
   * @param buffer
   *          the buffer
   */
  protected void notImplemented(Buffer buffer) {
    status(HttpResponseStatus.NOT_IMPLEMENTED).end(buffer);
  }

  /**
   * Not implemented.
   *
   * @param object
   *          the object
   */
  protected void notImplemented(Object object) {
    notImplemented(object, StandardCharsets.UTF_8);
  }

  /**
   * Not implemented.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void notImplemented(Object object, Charset charset) {
    status(HttpResponseStatus.NOT_IMPLEMENTED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Not implemented.
   *
   * @param content
   *          the content
   */
  protected void notImplemented(String content) {
    notImplemented(content, StandardCharsets.UTF_8);
  }

  /**
   * Not implemented.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void notImplemented(String content, Charset charset) {
    status(HttpResponseStatus.NOT_IMPLEMENTED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Not modified.
   */
  protected void notModified() {
    status(HttpResponseStatus.NOT_MODIFIED).end();
  }

  /**
   * Not modified.
   *
   * @param buffer
   *          the buffer
   */
  protected void notModified(Buffer buffer) {
    status(HttpResponseStatus.NOT_MODIFIED).end(buffer);
  }

  /**
   * Not modified.
   *
   * @param object
   *          the object
   */
  protected void notModified(Object object) {
    notModified(object, StandardCharsets.UTF_8);
  }

  /**
   * Not modified.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void notModified(Object object, Charset charset) {
    status(HttpResponseStatus.NOT_MODIFIED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Not modified.
   *
   * @param content
   *          the content
   */
  protected void notModified(String content) {
    notModified(content, StandardCharsets.UTF_8);
  }

  /**
   * Not modified.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void notModified(String content, Charset charset) {
    status(HttpResponseStatus.NOT_MODIFIED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Ok.
   */
  protected void ok() {
    success();
  }

  /**
   * Ok.
   *
   * @param buffer
   *          the buffer
   */
  protected void ok(Buffer buffer) {
    success(buffer);
  }

  /**
   * Ok.
   *
   * @param object
   *          the object
   */
  protected void ok(Object object) {
    success(object);
  }

  /**
   * Ok.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void ok(Object object, Charset charset) {
    success(object, charset);
  }

  /**
   * Ok.
   *
   * @param content
   *          the content
   */
  protected void ok(String content) {
    success(content, StandardCharsets.UTF_8);
  }

  /**
   * Ok.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void ok(String content, Charset charset) {
    success(content, charset);
  }

  /**
   * Partial content.
   */
  protected void partialContent() {
    status(HttpResponseStatus.PARTIAL_CONTENT).end();
  }

  /**
   * Partial content.
   *
   * @param buffer
   *          the buffer
   */
  protected void partialContent(Buffer buffer) {
    status(HttpResponseStatus.PARTIAL_CONTENT).end(buffer);
  }

  /**
   * Partial content.
   *
   * @param object
   *          the object
   */
  protected void partialContent(Object object) {
    partialContent(object, StandardCharsets.UTF_8);
  }

  /**
   * Partial content.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void partialContent(Object object, Charset charset) {
    status(HttpResponseStatus.PARTIAL_CONTENT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Partial content.
   *
   * @param content
   *          the content
   */
  protected void partialContent(String content) {
    partialContent(content, StandardCharsets.UTF_8);
  }

  /**
   * Partial content.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void partialContent(String content, Charset charset) {
    status(HttpResponseStatus.PARTIAL_CONTENT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Payment required.
   */
  protected void paymentRequired() {
    status(HttpResponseStatus.PAYMENT_REQUIRED).end();
  }

  /**
   * Payment required.
   *
   * @param buffer
   *          the buffer
   */
  protected void paymentRequired(Buffer buffer) {
    status(HttpResponseStatus.PAYMENT_REQUIRED).end(buffer);
  }

  /**
   * Payment required.
   *
   * @param object
   *          the object
   */
  protected void paymentRequired(Object object) {
    paymentRequired(object, StandardCharsets.UTF_8);
  }

  /**
   * Payment required.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void paymentRequired(Object object, Charset charset) {
    status(HttpResponseStatus.PAYMENT_REQUIRED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Payment required.
   *
   * @param content
   *          the content
   */
  protected void paymentRequired(String content) {
    paymentRequired(content, StandardCharsets.UTF_8);
  }

  /**
   * Payment required.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void paymentRequired(String content, Charset charset) {
    status(HttpResponseStatus.PAYMENT_REQUIRED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Pre condition failed.
   */
  protected void preConditionFailed() {
    status(HttpResponseStatus.PRECONDITION_FAILED).end();
  }

  /**
   * Pre condition failed.
   *
   * @param buffer
   *          the buffer
   */
  protected void preConditionFailed(Buffer buffer) {
    status(HttpResponseStatus.PRECONDITION_FAILED).end(buffer);
  }

  /**
   * Pre condition failed.
   *
   * @param object
   *          the object
   */
  protected void preConditionFailed(Object object) {
    preConditionFailed(object, StandardCharsets.UTF_8);
  }

  /**
   * Pre condition failed.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void preConditionFailed(Object object, Charset charset) {
    status(HttpResponseStatus.PRECONDITION_FAILED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Pre condition failed.
   *
   * @param content
   *          the content
   */
  protected void preConditionFailed(String content) {
    preConditionFailed(content, StandardCharsets.UTF_8);
  }

  /**
   * Pre condition failed.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void preConditionFailed(String content, Charset charset) {
    status(HttpResponseStatus.PRECONDITION_FAILED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Precondition required.
   */
  protected void preconditionRequired() {
    status(HttpResponseStatus.PRECONDITION_REQUIRED).end();
  }

  /**
   * Precondition required.
   *
   * @param buffer
   *          the buffer
   */
  protected void preconditionRequired(Buffer buffer) {
    status(HttpResponseStatus.PRECONDITION_REQUIRED).end(buffer);
  }

  /**
   * Precondition required.
   *
   * @param object
   *          the object
   */
  protected void preconditionRequired(Object object) {
    preconditionRequired(object, StandardCharsets.UTF_8);
  }

  /**
   * Precondition required.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void preconditionRequired(Object object, Charset charset) {
    status(HttpResponseStatus.PRECONDITION_REQUIRED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Precondition required.
   *
   * @param content
   *          the content
   */
  protected void preconditionRequired(String content) {
    preconditionRequired(content, StandardCharsets.UTF_8);
  }

  /**
   * Precondition required.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void preconditionRequired(String content, Charset charset) {
    status(HttpResponseStatus.PRECONDITION_REQUIRED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Processing.
   */
  protected void processing() {
    status(HttpResponseStatus.PROCESSING).end();
  }

  /**
   * Processing.
   *
   * @param buffer
   *          the buffer
   */
  protected void processing(Buffer buffer) {
    status(HttpResponseStatus.PROCESSING).end(buffer);
  }

  /**
   * Processing.
   *
   * @param object
   *          the object
   */
  protected void processing(Object object) {
    processing(object, StandardCharsets.UTF_8);
  }

  /**
   * Processing.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void processing(Object object, Charset charset) {
    status(HttpResponseStatus.PROCESSING);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Processing.
   *
   * @param content
   *          the content
   */
  protected void processing(String content) {
    processing(content, StandardCharsets.UTF_8);
  }

  /**
   * Processing.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void processing(String content, Charset charset) {
    status(HttpResponseStatus.PROCESSING);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Proxy authentication required.
   */
  protected void proxyAuthenticationRequired() {
    status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED).end();
  }

  /**
   * Proxy authentication required.
   *
   * @param buffer
   *          the buffer
   */
  protected void proxyAuthenticationRequired(Buffer buffer) {
    status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED).end(buffer);
  }

  /**
   * Proxy authentication required.
   *
   * @param object
   *          the object
   */
  protected void proxyAuthenticationRequired(Object object) {
    proxyAuthenticationRequired(object, StandardCharsets.UTF_8);
  }

  /**
   * Proxy authentication required.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void proxyAuthenticationRequired(Object object, Charset charset) {
    status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Proxy authentication required.
   *
   * @param content
   *          the content
   */
  protected void proxyAuthenticationRequired(String content) {
    proxyAuthenticationRequired(content, StandardCharsets.UTF_8);
  }

  /**
   * Proxy authentication required.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void proxyAuthenticationRequired(String content, Charset charset) {
    status(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Request model too large.
   */
  protected void requestEntityTooLarge() {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end();
  }

  /**
   * Request model too large.
   *
   * @param buffer
   *          the buffer
   */
  protected void requestEntityTooLarge(Buffer buffer) {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(buffer);
  }

  /**
   * Request model too large.
   *
   * @param object
   *          the object
   */
  protected void requestEntityTooLarge(Object object) {
    requestEntityTooLarge(object, StandardCharsets.UTF_8);
  }

  /**
   * Request model too large.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void requestEntityTooLarge(Object object, Charset charset) {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Request model too large.
   *
   * @param content
   *          the content
   */
  protected void requestEntityTooLarge(String content) {
    requestEntityTooLarge(content, StandardCharsets.UTF_8);
  }

  /**
   * Request model too large.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void requestEntityTooLarge(String content, Charset charset) {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Request header fields too large.
   */
  protected void requestHeaderFieldsTooLarge() {
    status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).end();
  }

  /**
   * Request header fields too large.
   *
   * @param buffer
   *          the buffer
   */
  protected void requestHeaderFieldsTooLarge(Buffer buffer) {
    status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).end(buffer);
  }

  /**
   * Request header fields too large.
   *
   * @param object
   *          the object
   */
  protected void requestHeaderFieldsTooLarge(Object object) {
    requestEntityTooLarge(object, StandardCharsets.UTF_8);
  }

  /**
   * Request header fields too large.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void requestHeaderFieldsTooLarge(Object object, Charset charset) {
    status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Request header fields too large.
   *
   * @param content
   *          the content
   */
  protected void requestHeaderFieldsTooLarge(String content) {
    requestEntityTooLarge(content, StandardCharsets.UTF_8);
  }

  /**
   * Request header fields too large.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void requestHeaderFieldsTooLarge(String content, Charset charset) {
    status(HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Request range not satisfiable.
   */
  protected void requestRangeNotSatisfiable() {
    status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE).end();
  }

  /**
   * Request range not satisfiable.
   *
   * @param buffer
   *          the buffer
   */
  protected void requestRangeNotSatisfiable(Buffer buffer) {
    status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE).end(buffer);
  }

  /**
   * Request range not satisfiable.
   *
   * @param object
   *          the object
   */
  protected void requestRangeNotSatisfiable(Object object) {
    requestRangeNotSatisfiable(object, StandardCharsets.UTF_8);
  }

  /**
   * Request range not satisfiable.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void requestRangeNotSatisfiable(Object object, Charset charset) {
    status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Request range not satisfiable.
   *
   * @param content
   *          the content
   */
  protected void requestRangeNotSatisfiable(String content) {
    requestRangeNotSatisfiable(content, StandardCharsets.UTF_8);
  }

  /**
   * Request range not satisfiable.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void requestRangeNotSatisfiable(String content, Charset charset) {
    status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Request timeout.
   */
  protected void requestTimeout() {
    status(HttpResponseStatus.REQUEST_TIMEOUT).end();
  }

  /**
   * Request timeout.
   *
   * @param buffer
   *          the buffer
   */
  protected void requestTimeout(Buffer buffer) {
    status(HttpResponseStatus.REQUEST_TIMEOUT).end(buffer);
  }

  /**
   * Request timeout.
   *
   * @param object
   *          the object
   */
  protected void requestTimeout(Object object) {
    requestTimeout(object, StandardCharsets.UTF_8);
  }

  /**
   * Request timeout.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void requestTimeout(Object object, Charset charset) {
    status(HttpResponseStatus.REQUEST_TIMEOUT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Request timeout.
   *
   * @param content
   *          the content
   */
  protected void requestTimeout(String content) {
    requestTimeout(content, StandardCharsets.UTF_8);
  }

  /**
   * Request timeout.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void requestTimeout(String content, Charset charset) {
    status(HttpResponseStatus.REQUEST_TIMEOUT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Request uti too long.
   */
  protected void requestUtiTooLong() {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end();
  }

  /**
   * Request uti too long.
   *
   * @param buffer
   *          the buffer
   */
  protected void requestUtiTooLong(Buffer buffer) {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE).end(buffer);
  }

  /**
   * Request uti too long.
   *
   * @param object
   *          the object
   */
  protected void requestUtiTooLong(Object object) {
    requestUtiTooLong(object, StandardCharsets.UTF_8);
  }

  /**
   * Request uti too long.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void requestUtiTooLong(Object object, Charset charset) {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Request uti too long.
   *
   * @param content
   *          the content
   */
  protected void requestUtiTooLong(String content) {
    requestUtiTooLong(content, StandardCharsets.UTF_8);
  }

  /**
   * Request uti too long.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void requestUtiTooLong(String content, Charset charset) {
    status(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Reset content.
   */
  protected void resetContent() {
    status(HttpResponseStatus.RESET_CONTENT).end();
  }

  /**
   * Reset content.
   *
   * @param buffer
   *          the buffer
   */
  protected void resetContent(Buffer buffer) {
    status(HttpResponseStatus.RESET_CONTENT).end(buffer);
  }

  /**
   * Reset content.
   *
   * @param object
   *          the object
   */
  protected void resetContent(Object object) {
    resetContent(object, StandardCharsets.UTF_8);
  }

  /**
   * Reset content.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void resetContent(Object object, Charset charset) {
    status(HttpResponseStatus.RESET_CONTENT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Reset content.
   *
   * @param content
   *          the content
   */
  protected void resetContent(String content) {
    resetContent(content, StandardCharsets.UTF_8);
  }

  /**
   * Reset content.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void resetContent(String content, Charset charset) {
    status(HttpResponseStatus.RESET_CONTENT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * See other.
   */
  protected void seeOther() {
    status(HttpResponseStatus.SEE_OTHER).end();
  }

  /**
   * See other.
   *
   * @param buffer
   *          the buffer
   */
  protected void seeOther(Buffer buffer) {
    status(HttpResponseStatus.SEE_OTHER).end(buffer);
  }

  /**
   * See other.
   *
   * @param object
   *          the object
   */
  protected void seeOther(Object object) {
    seeOther(object, StandardCharsets.UTF_8);
  }

  /**
   * See other.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void seeOther(Object object, Charset charset) {
    status(HttpResponseStatus.SEE_OTHER);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * See other.
   *
   * @param content
   *          the content
   */
  protected void seeOther(String content) {
    seeOther(content, StandardCharsets.UTF_8);
  }

  /**
   * See other.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void seeOther(String content, Charset charset) {
    status(HttpResponseStatus.SEE_OTHER);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Service unavailable.
   */
  protected void serviceUnavailable() {
    status(HttpResponseStatus.SERVICE_UNAVAILABLE).end();
  }

  /**
   * Service unavailable.
   *
   * @param buffer
   *          the buffer
   */
  protected void serviceUnavailable(Buffer buffer) {
    status(HttpResponseStatus.SERVICE_UNAVAILABLE).end(buffer);
  }

  /**
   * Service unavailable.
   *
   * @param object
   *          the object
   */
  protected void serviceUnavailable(Object object) {
    serviceUnavailable(object, StandardCharsets.UTF_8);
  }

  /**
   * Service unavailable.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void serviceUnavailable(Object object, Charset charset) {
    status(HttpResponseStatus.SERVICE_UNAVAILABLE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Service unavailable.
   *
   * @param content
   *          the content
   */
  protected void serviceUnavailable(String content) {
    serviceUnavailable(content, StandardCharsets.UTF_8);
  }

  /**
   * Service unavailable.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void serviceUnavailable(String content, Charset charset) {
    status(HttpResponseStatus.SERVICE_UNAVAILABLE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Status.
   *
   * @param status
   *          the status
   * @return the http server response
   */
  protected HttpServerResponse status(HttpResponseStatus status) {
    res.setStatusCode(status.code());
    res.setStatusMessage(status.reasonPhrase());
    return res;
  }

  /**
   * Success.
   */
  protected void success() {
    status(HttpResponseStatus.OK).end();
  }

  /**
   * Success.
   *
   * @param buffer
   *          the buffer
   */
  protected void success(Buffer buffer) {
    status(HttpResponseStatus.OK).end(buffer);
  }

  /**
   * Success.
   *
   * @param object
   *          the object
   */
  protected void success(Object object) {
    success(object, StandardCharsets.UTF_8);
  }

  /**
   * Success.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void success(Object object, Charset charset) {
    status(HttpResponseStatus.OK);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Success.
   *
   * @param content
   *          the content
   */
  protected void success(String content) {
    success(content, StandardCharsets.UTF_8);
  }

  /**
   * Success.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void success(String content, Charset charset) {
    status(HttpResponseStatus.OK);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Switching protocols.
   */
  protected void switchingProtocols() {
    status(HttpResponseStatus.SWITCHING_PROTOCOLS).end();
  }

  /**
   * Switching protocols.
   *
   * @param buffer
   *          the buffer
   */
  protected void switchingProtocols(Buffer buffer) {
    status(HttpResponseStatus.SWITCHING_PROTOCOLS).end(buffer);
  }

  /**
   * Switching protocols.
   *
   * @param object
   *          the object
   */
  protected void switchingProtocols(Object object) {
    switchingProtocols(object, StandardCharsets.UTF_8);
  }

  /**
   * Switching protocols.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void switchingProtocols(Object object, Charset charset) {
    status(HttpResponseStatus.SWITCHING_PROTOCOLS);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Switching protocols.
   *
   * @param content
   *          the content
   */
  protected void switchingProtocols(String content) {
    switchingProtocols(content, StandardCharsets.UTF_8);
  }

  /**
   * Switching protocols.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void switchingProtocols(String content, Charset charset) {
    status(HttpResponseStatus.SWITCHING_PROTOCOLS);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Temporary redirect.
   */
  protected void temporaryRedirect() {
    status(HttpResponseStatus.TEMPORARY_REDIRECT).end();
  }

  /**
   * Temporary redirect.
   *
   * @param buffer
   *          the buffer
   */
  protected void temporaryRedirect(Buffer buffer) {
    status(HttpResponseStatus.TEMPORARY_REDIRECT).end(buffer);
  }

  /**
   * Temporary redirect.
   *
   * @param object
   *          the object
   */
  protected void temporaryRedirect(Object object) {
    temporaryRedirect(object, StandardCharsets.UTF_8);
  }

  /**
   * Temporary redirect.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void temporaryRedirect(Object object, Charset charset) {
    status(HttpResponseStatus.TEMPORARY_REDIRECT);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Temporary redirect.
   *
   * @param content
   *          the content
   */
  protected void temporaryRedirect(String content) {
    temporaryRedirect(content, StandardCharsets.UTF_8);
  }

  /**
   * Temporary redirect.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void temporaryRedirect(String content, Charset charset) {
    status(HttpResponseStatus.TEMPORARY_REDIRECT);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Too many request.
   */
  protected void tooManyRequest() {
    status(HttpResponseStatus.TOO_MANY_REQUESTS).end();
  }

  /**
   * Too many request.
   *
   * @param buffer
   *          the buffer
   */
  protected void tooManyRequest(Buffer buffer) {
    status(HttpResponseStatus.TOO_MANY_REQUESTS).end(buffer);
  }

  /**
   * Too many request.
   *
   * @param object
   *          the object
   */
  protected void tooManyRequest(Object object) {
    tooManyRequest(object, StandardCharsets.UTF_8);
  }

  /**
   * Too many request.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void tooManyRequest(Object object, Charset charset) {
    status(HttpResponseStatus.TOO_MANY_REQUESTS);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Too many request.
   *
   * @param content
   *          the content
   */
  protected void tooManyRequest(String content) {
    tooManyRequest(content, StandardCharsets.UTF_8);
  }

  /**
   * Too many request.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void tooManyRequest(String content, Charset charset) {
    status(HttpResponseStatus.TOO_MANY_REQUESTS);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Transform.
   *
   * @param object
   *          the object
   * @return the string
   */
  protected String transform(Object object) {

    return Json.encode(object);
  }

  /**
   * Unauthorized.
   */
  protected void unauthorized() {
    status(HttpResponseStatus.UNAUTHORIZED).end();
  }

  /**
   * Unauthorized.
   *
   * @param buffer
   *          the buffer
   */
  protected void unauthorized(Buffer buffer) {
    status(HttpResponseStatus.UNAUTHORIZED).end(buffer);
  }

  /**
   * Unauthorized.
   *
   * @param object
   *          the object
   */
  protected void unauthorized(Object object) {
    unauthorized(object, StandardCharsets.UTF_8);
  }

  /**
   * Unauthorized.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void unauthorized(Object object, Charset charset) {
    status(HttpResponseStatus.UNAUTHORIZED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Unauthorized.
   *
   * @param content
   *          the content
   */
  protected void unauthorized(String content) {
    unauthorized(content, StandardCharsets.UTF_8);
  }

  /**
   * Unauthorized.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void unauthorized(String content, Charset charset) {
    status(HttpResponseStatus.UNAUTHORIZED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Unordered collection.
   */
  protected void unorderedCollection() {
    status(HttpResponseStatus.UNORDERED_COLLECTION).end();
  }

  /**
   * Unordered collection.
   *
   * @param buffer
   *          the buffer
   */
  protected void unorderedCollection(Buffer buffer) {
    status(HttpResponseStatus.UNORDERED_COLLECTION).end(buffer);
  }

  /**
   * Unordered collection.
   *
   * @param object
   *          the object
   */
  protected void unorderedCollection(Object object) {
    unorderedCollection(object, StandardCharsets.UTF_8);
  }

  /**
   * Unordered collection.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void unorderedCollection(Object object, Charset charset) {
    status(HttpResponseStatus.UNORDERED_COLLECTION);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Unordered collection.
   *
   * @param content
   *          the content
   */
  protected void unorderedCollection(String content) {
    unorderedCollection(content, StandardCharsets.UTF_8);
  }

  /**
   * Unordered collection.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void unorderedCollection(String content, Charset charset) {
    status(HttpResponseStatus.UNORDERED_COLLECTION);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Unprocessable model.
   */
  protected void unprocessableEntity() {
    status(HttpResponseStatus.UNPROCESSABLE_ENTITY).end();
  }

  /**
   * Unprocessable model.
   *
   * @param buffer
   *          the buffer
   */
  protected void unprocessableEntity(Buffer buffer) {
    status(HttpResponseStatus.UNPROCESSABLE_ENTITY).end(buffer);
  }

  /**
   * Unprocessable model.
   *
   * @param object
   *          the object
   */
  protected void unprocessableEntity(Object object) {
    unprocessableEntity(object, StandardCharsets.UTF_8);
  }

  /**
   * Unprocessable model.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void unprocessableEntity(Object object, Charset charset) {
    status(HttpResponseStatus.UNPROCESSABLE_ENTITY);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Unprocessable model.
   *
   * @param content
   *          the content
   */
  protected void unprocessableEntity(String content) {
    unprocessableEntity(content, StandardCharsets.UTF_8);
  }

  /**
   * Unprocessable model.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void unprocessableEntity(String content, Charset charset) {
    status(HttpResponseStatus.UNPROCESSABLE_ENTITY);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Unsupported media type.
   */
  protected void unsupportedMediaType() {
    status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE).end();
  }

  /**
   * Unsupported media type.
   *
   * @param buffer
   *          the buffer
   */
  protected void unsupportedMediaType(Buffer buffer) {
    status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE).end(buffer);
  }

  /**
   * Unsupported media type.
   *
   * @param object
   *          the object
   */
  protected void unsupportedMediaType(Object object) {
    unsupportedMediaType(object, StandardCharsets.UTF_8);
  }

  /**
   * Unsupported media type.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void unsupportedMediaType(Object object, Charset charset) {
    status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Unsupported media type.
   *
   * @param content
   *          the content
   */
  protected void unsupportedMediaType(String content) {
    unsupportedMediaType(content, StandardCharsets.UTF_8);
  }

  /**
   * Unsupported media type.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void unsupportedMediaType(String content, Charset charset) {
    status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Upgrade required.
   */
  protected void upgradeRequired() {
    status(HttpResponseStatus.UPGRADE_REQUIRED).end();
  }

  /**
   * Upgrade required.
   *
   * @param buffer
   *          the buffer
   */
  protected void upgradeRequired(Buffer buffer) {
    status(HttpResponseStatus.UPGRADE_REQUIRED).end(buffer);
  }

  /**
   * Upgrade required.
   *
   * @param object
   *          the object
   */
  protected void upgradeRequired(Object object) {
    upgradeRequired(object, StandardCharsets.UTF_8);
  }

  /**
   * Upgrade required.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void upgradeRequired(Object object, Charset charset) {
    status(HttpResponseStatus.UPGRADE_REQUIRED);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Upgrade required.
   *
   * @param content
   *          the content
   */
  protected void upgradeRequired(String content) {
    upgradeRequired(content, StandardCharsets.UTF_8);
  }

  /**
   * Upgrade required.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void upgradeRequired(String content, Charset charset) {
    status(HttpResponseStatus.UPGRADE_REQUIRED);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Use proxy.
   */
  protected void useProxy() {
    status(HttpResponseStatus.USE_PROXY).end();
  }

  /**
   * Use proxy.
   *
   * @param buffer
   *          the buffer
   */
  protected void useProxy(Buffer buffer) {
    status(HttpResponseStatus.USE_PROXY).end(buffer);
  }

  /**
   * Use proxy.
   *
   * @param object
   *          the object
   */
  protected void useProxy(Object object) {
    useProxy(object, StandardCharsets.UTF_8);
  }

  /**
   * Use proxy.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void useProxy(Object object, Charset charset) {
    status(HttpResponseStatus.USE_PROXY);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Use proxy.
   *
   * @param content
   *          the content
   */
  protected void useProxy(String content) {
    useProxy(content, StandardCharsets.UTF_8);
  }

  /**
   * Use proxy.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
  protected void useProxy(String content, Charset charset) {
    status(HttpResponseStatus.USE_PROXY);
    if (isValidJson(content)) {
      contentType("application/json");
    }
    res.setChunked(true);
    res.write(content, charset.name());
    end();
  }

  /**
   * Variant also negotiates.
   */
  protected void variantAlsoNegotiates() {
    status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES).end();
  }

  /**
   * Variant also negotiates.
   *
   * @param buffer
   *          the buffer
   */
  protected void variantAlsoNegotiates(Buffer buffer) {
    status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES).end(buffer);
  }

  /**
   * Variant also negotiates.
   *
   * @param object
   *          the object
   */
  protected void variantAlsoNegotiates(Object object) {
    variantAlsoNegotiates(object, StandardCharsets.UTF_8);
  }

  /**
   * Variant also negotiates.
   *
   * @param object
   *          the object
   * @param charset
   *          the charset
   */
  protected void variantAlsoNegotiates(Object object, Charset charset) {
    status(HttpResponseStatus.VARIANT_ALSO_NEGOTIATES);
    contentType("application/json");
    res.setChunked(true).write(transform(object), StandardCharsets.UTF_8.name());
    end();
  }

  /**
   * Variant also negotiates.
   *
   * @param content
   *          the content
   */
  protected void variantAlsoNegotiates(String content) {
    variantAlsoNegotiates(content, StandardCharsets.UTF_8);
  }

  /**
   * Variant also negotiates.
   *
   * @param content
   *          the content
   * @param charset
   *          the charset
   */
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

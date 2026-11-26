/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.controller.openapi;

import static com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix.OPEN_API_V1_PREFIX;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.console.web.service.openapi.OpenApiIdempotencyService;
import com.clougence.clouddm.console.web.service.openapi.OpenApiRequestAuthenticator;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.openapi.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OpenApiGatewayController {

    private static final int            MAX_BODY_BYTES          = 2 * 1024 * 1024;
    private static final Pattern        IDEMPOTENCY_KEY_PATTERN = Pattern.compile("[!-~]{1,128}");

    @Resource
    private OpenApiRequestAuthenticator authenticator;

    @Resource
    private OpenApiIdempotencyService   idempotencyService;

    @RequestMapping({ OPEN_API_V1_PREFIX, OPEN_API_V1_PREFIX + "/**" })
    public CompletionStage<ResponseEntity<String>> dispatch(HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        OpenApiSpi spi = PluginManager.findSpi(OpenApiSpi.class, OpenApiSpi.NAME);
        if (!PluginManager.isReady() || spi == null) {
            return completedError(HttpStatus.NOT_FOUND, requestId, "NOT_FOUND", "OpenAPI is unavailable.");
        }

        if (!"POST".equals(request.getMethod())) {
            return completedError(HttpStatus.METHOD_NOT_ALLOWED, requestId, "INVALID_ARGUMENT", "OpenAPI requests must use POST.");
        }

        if (!isJson(request.getContentType())) {
            return completedError(HttpStatus.BAD_REQUEST, requestId, "INVALID_ARGUMENT", "OpenAPI requests require application/json.");
        }

        try {
            byte[] body = request.getInputStream().readNBytes(MAX_BODY_BYTES + 1);
            if (body.length == 0 || body.length > MAX_BODY_BYTES) {
                return completedError(HttpStatus.BAD_REQUEST, requestId, "INVALID_ARGUMENT", "Expected a JSON body of at most 2 MiB.");
            }

            String path = RdpWebUtils.getRequestPath(request).value();
            String idempotencyKey = request.getHeader("Idempotency-Key");
            OpenApiCaller caller = authenticator.authenticate(request, path, idempotencyKey, body);
            if (caller == null) {
                return completedError(HttpStatus.UNAUTHORIZED, requestId, "UNAUTHENTICATED", "OpenAPI authentication failed.");
            }

            String jsonBody = StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(body)).toString();
            OpenApiRequest openApiRequest = new OpenApiRequest(request.getMethod(), path, requestId, jsonBody, idempotencyKey);
            OpenApiWriteOperation write = spi.findWrite(path);
            if (write != null) {
                if (idempotencyKey == null || !IDEMPOTENCY_KEY_PATTERN.matcher(idempotencyKey).matches()) {
                    return completedError(HttpStatus.BAD_REQUEST, requestId, "INVALID_ARGUMENT", "Idempotency-Key must contain 1 to 128 visible ASCII characters.");
                }
                ResponseEntity<String> result = idempotencyService.execute(openApiRequest, caller, request.getQueryString(), write)
                    .map(OpenApiGatewayController::response)
                    .orElseGet(() -> error(HttpStatus.CONFLICT, requestId, "STATE_CONFLICT", "Idempotency-Key has already been used with different request content."));
                return CompletableFuture.completedFuture(result);
            }
            return spi.handle(openApiRequest, caller).thenApply(OpenApiGatewayController::response).exceptionally(error -> internalError(requestId, error));
        } catch (IOException e) {
            return completedError(HttpStatus.BAD_REQUEST, requestId, "INVALID_ARGUMENT", "Could not read the UTF-8 request body.");
        } catch (OpenApiException e) {
            String code = e.getCode().name();
            return switch (e.getCode()) {
                case INVALID_ARGUMENT -> completedError(HttpStatus.BAD_REQUEST, requestId, code, "Invalid request parameters.");
                case FORBIDDEN -> completedError(HttpStatus.FORBIDDEN, requestId, code, "The caller lacks permission for this operation.");
                case NOT_FOUND -> completedError(HttpStatus.NOT_FOUND, requestId, code, "The requested resource was not found.");
                case STATE_CONFLICT -> completedError(HttpStatus.CONFLICT, requestId, code, "The operation conflicts with the current resource state.");
            };
        } catch (RuntimeException e) {
            return CompletableFuture.completedFuture(internalError(requestId, e));
        }
    }

    private static boolean isJson(String contentType) {
        if (contentType == null) {
            return false;
        }
        try {
            return MediaType.APPLICATION_JSON.includes(MediaType.parseMediaType(contentType));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static CompletionStage<ResponseEntity<String>> completedError(HttpStatus status, String requestId, String code, String message) {
        return CompletableFuture.completedFuture(error(status, requestId, code, message));
    }

    private static ResponseEntity<String> error(HttpStatus status, String requestId, String code, String message) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("requestId", requestId);
        body.put("code", code);
        body.put("msg", message);
        body.putNull("data");

        ResponseEntity.BodyBuilder response = response(status.value());
        if (status == HttpStatus.METHOD_NOT_ALLOWED) {
            response.allow(HttpMethod.POST);
        }
        return response.body(body.toString());
    }

    private static ResponseEntity.BodyBuilder response(int statusCode) {
        return ResponseEntity.status(statusCode).contentType(MediaType.APPLICATION_JSON).cacheControl(CacheControl.noStore());
    }

    private static ResponseEntity<String> response(OpenApiResponse result) {
        return response(result.getStatusCode()).body(result.getBody());
    }

    private ResponseEntity<String> internalError(String requestId, Throwable error) {
        if (error instanceof DataAccessException) {
            log.error("OpenAPI request {} failed with database error {}.", requestId, error.getClass().getSimpleName());
        } else {
            log.error("OpenAPI request {} failed.", requestId, error);
        }
        return error(HttpStatus.INTERNAL_SERVER_ERROR, requestId, "INTERNAL_ERROR", "OpenAPI request failed.");
    }
}

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
package com.clougence.clouddm.console.web.util;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;

public class SseResponseUtils {
    public static final String  EVENT_ERROR              = "ERROR";
    private static final String FIELD_CODE               = "code";
    private static final String FIELD_MSG                = "msg";
    private static final String FIELD_ERROR              = "error";
    private static final String FIELD_REQUEST_ID         = "requestId";
    private static final String CACHE_CONTROL            = "no-cache, no-transform";
    private static final String HEADER_X_ACCEL_BUFFERING = "X-Accel-Buffering";
    private static final String BUFFERING_DISABLED       = "no";
    private static final String NO_CACHE                 = "no-cache";
    private static final String EXPIRES                  = "0";

    private SseResponseUtils(){
    }

    public static void applyStreamingHeaders(HttpServletResponse response) {
        String contentType = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=" + StandardCharsets.UTF_8.name();
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL);
        response.setHeader(HttpHeaders.PRAGMA, NO_CACHE);
        response.setHeader(HttpHeaders.EXPIRES, EXPIRES);
        response.setHeader(HEADER_X_ACCEL_BUFFERING, BUFFERING_DISABLED);
    }

    public static Map<String, Object> errorPayload(String code, String message, String errorMessage, String requestId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(FIELD_CODE, code);
        payload.put(FIELD_MSG, message);
        payload.put(FIELD_ERROR, errorMessage);
        payload.put(FIELD_REQUEST_ID, requestId);
        return payload;
    }
}

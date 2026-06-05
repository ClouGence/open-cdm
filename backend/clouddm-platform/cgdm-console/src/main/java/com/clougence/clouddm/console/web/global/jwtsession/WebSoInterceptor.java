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
package com.clougence.clouddm.console.web.global.jwtsession;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSoInterceptor implements HandshakeInterceptor {

    public static final String  WS_PUSER_ID     = "WS_PUSER_ID";
    public static final String  WS_USER_ID      = "WS_USER_ID";
    public static final String  WS_USER_ROLE    = "WS_USER_ROLE";
    public static final String  WS_REQUEST_URI  = "WS_REQUEST_URI";
    public static final String  WS_CHECK_RESULT = "WS_CHECK_RESULT";

    @Resource
    private JwtManager          sessionManager;
    @Getter
    private final WebSoMetadata socketMeta      = new WebSoMetadata();

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> map) throws Exception {
        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) response;
        HttpServletRequest httpReq = httpRequest.getServletRequest();
        HttpServletResponse httpRes = httpResponse.getServletResponse();

        String requestURI = httpReq.getRequestURI();
        Class<?> wsHandlerClass = this.socketMeta.getMetaDataByPath(requestURI);
        if (wsHandlerClass == null) {
            map.put(WS_CHECK_RESULT, JwtCheckResult.builder()//
                .success(false)
                .message("Internal Error Maybe, WebSoMetadata is not init.")
                .errorCode(500)
                .build());
            return false;
        }

        JwtCheckResult checkResult = this.sessionManager.preHandle(httpReq, httpRes, () -> wsHandlerClass.getAnnotation(RequestAuth.class), null);

        String puid = (String) httpReq.getAttribute(RdpUserService.PUID);
        String uid = (String) httpReq.getAttribute(RdpUserService.UID);
        Object userRole = httpReq.getAttribute(RdpUserService.USER_ROLE);
        //
        if (StringUtils.isBlank(uid)) {
            checkResult = JwtCheckResult.builder()//
                .success(false)
                .message("No Login.")
                .errorCode(401)
                .build();
        } else {
            map.put(WS_PUSER_ID, puid);
            map.put(WS_USER_ID, uid);
        }
        if (userRole != null) {
            map.put(WS_USER_ROLE, userRole);
        }
        map.put(WS_REQUEST_URI, requestURI);
        map.put(WS_CHECK_RESULT, checkResult);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        //
    }
}

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

import com.clougence.clouddm.console.web.global.session.SessionCheckResult;
import com.clougence.clouddm.console.web.global.session.SessionPrincipal;
import com.clougence.clouddm.console.web.global.session.WebSessionManager;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpLocal;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
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
    public static final String  WS_LOCALE       = "WS_LOCALE";
    public static final String  WS_CHECK_RESULT = "WS_CHECK_RESULT";
    public static final String  WS_SESSION_ID   = "WS_SESSION_ID";

    @Resource
    private WebSessionManager   sessionManager;
    @Getter
    private final WebSoMetadata socketMeta      = new WebSoMetadata();

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> map) throws Exception {
        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) response;
        HttpServletRequest httpReq = httpRequest.getServletRequest();
        HttpServletResponse httpRes = httpResponse.getServletResponse();

        String requestURI = httpReq.getRequestURI().substring(httpReq.getContextPath().length());
        Class<?> wsHandlerClass = this.socketMeta.getMetaDataByPath(requestURI);
        if (wsHandlerClass == null) {
            map.put(WS_CHECK_RESULT, new SessionCheckResult(false, "Internal Error Maybe, WebSoMetadata is not init.", 500));
            RdpWebUtils.cleanLocal();
            RdpLocal.setLocal(null);
            return false;
        }

        try {
            SessionCheckResult checkResult = this.sessionManager.preHandle(httpReq, httpRes, wsHandlerClass.getAnnotation(RequestAuth.class));
            map.put(WS_LOCALE, RdpLocal.getLocal());

            String puid = (String) httpReq.getAttribute(RdpUserService.PUID);
            String uid = (String) httpReq.getAttribute(RdpUserService.UID);
            //
            if (StringUtils.isBlank(uid)) {
                checkResult = new SessionCheckResult(false, "No Login.", 401);
            } else {
                map.put(WS_PUSER_ID, puid);
                map.put(WS_USER_ID, uid);
                SessionPrincipal principal = (SessionPrincipal) httpReq.getAttribute(WebSessionManager.SESSION_PRINCIPAL);
                if (principal != null) {
                    map.put(WS_SESSION_ID, principal.getSessionId());
                }
            }
            map.put(WS_CHECK_RESULT, checkResult);
            return true;
        } finally {
            // A throwing beforeHandshake is not guaranteed to reach afterHandshake.
            RdpWebUtils.cleanLocal();
            RdpLocal.setLocal(null);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        // thread state from session preHandle is already cleared in beforeHandshake
    }
}

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
package com.clougence.clouddm.console.web.global.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator.OverflowStrategy;

import com.alibaba.fastjson.JSONObject;
import com.clougence.clouddm.console.web.component.language.DsLanguageService;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.clouddm.console.web.model.fo.editor.language.WsLanguageFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsResMsg;
import com.clougence.clouddm.console.web.service.editor.query.ConsoleQueryApi;
import com.clougence.clouddm.sdk.language.AbstractResult;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class WsChannelStore {

    private final String                        uid;
    private final ConsoleQueryApi               queryServiceApi;
    private final DsLanguageService             dsLanguageService;
    private final Map<String, WebSocketSession> channelMap = new ConcurrentHashMap<>();

    public WsChannelStore(String uid, ConsoleQueryApi queryServiceApi, DsLanguageService dsLanguageService){
        this.uid = uid;
        this.queryServiceApi = queryServiceApi;
        this.dsLanguageService = dsLanguageService;
    }

    public boolean containsChannel(String channelKey) {
        return this.channelMap.containsKey(channelKey);
    }

    public void acceptChannel(String channelKey, WebSocketSession ws) {
        OverflowStrategy strategy = OverflowStrategy.DROP;
        int sendTimeLimit = 10000;
        int bufferSizeLimit = 4 * 1024 * 1024;

        log.info("WS[" + channelKey + "]:CONNECTED user(" + uid + ")");
        this.channelMap.put(channelKey, new ConcurrentWebSocketSessionDecorator(ws, sendTimeLimit, bufferSizeLimit, strategy));
    }

    public void closeChannel(String channelKey) {
        log.info("WS[" + channelKey + "]:REMOVE user(" + uid + ")");
        this.channelMap.remove(channelKey);
    }

    public void handleMessage(WebSocketSession ws, WsMsg reqMsg) {
        String channelKey = reqMsg.getChannelKey();

        //Objects.requireNonNull(body, "missing request info.");
        log.info("WS[" + channelKey + "]:RECEIVE user(" + uid + "), type:" + reqMsg.getType().getCode());

        switch (reqMsg.getType()) {
            case WS_REQ_ECHO:
                WsUtils.writeToSocket(ws, WsType.WS_RES_ECHO, reqMsg.getObject());
                return;
            case WS_REQ_QUERY:
                WsQueryFO queryFO = JSONObject.parseObject(reqMsg.getObject(), WsQueryFO.class);
                queryFO.setChannelKey(channelKey);
                queryFO.setPrimaryUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_PUSER_ID));
                queryFO.setCurrentUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
                queryFO.setRequestTime(System.currentTimeMillis());
                queryFO.setClientIp(getHost(ws));
                this.queryServiceApi.offerQueryRequest(queryFO, DmGlobalEventBus::triggerQueryResultEvent);
                return;
            case WS_REQ_LANGUAGE:
                handleLanguageMessage(ws, reqMsg.getObject());
                return;
            default:
                throw new UnsupportedOperationException("Request WsType '" + reqMsg.getType() + "' Unsupported.");
        }
    }

    private void handleLanguageMessage(WebSocketSession ws, String object) {
        WsLanguageFO languageFO = JSONObject.parseObject(object, WsLanguageFO.class);
        JSONObject request = languageFO.getRequest();
        AbstractResult result;
        switch (languageFO.getLanguageType()) {
            case COMPLETE:
                result = this.dsLanguageService.complete(ownerUid(ws), parseCompletionRequest(request));
                break;
            case VALIDATE:
                result = this.dsLanguageService.validate(ownerUid(ws), request.toJavaObject(ValidateRequest.class));
                break;
            case SPLIT:
                result = this.dsLanguageService.split(ownerUid(ws), request.toJavaObject(SplitRequest.class));
                break;
            default:
                throw new UnsupportedOperationException("Language WsType '" + languageFO.getLanguageType() + "' Unsupported.");
        }

        JSONObject response = new JSONObject();
        response.put("languageType", languageFO.getLanguageType());
        response.put("requestId", result.getRequestId());
        response.put("result", result);
        WsUtils.writeToSocket(ws, WsType.WS_RES_LANGUAGE, response);
    }

    private CompletionRequest parseCompletionRequest(String object) {
        return parseCompletionRequest(JSONObject.parseObject(object));
    }

    private CompletionRequest parseCompletionRequest(JSONObject json) {
        JSONObject position = json.getJSONObject("position");
        json.remove("position");
        CompletionRequest request = json.toJavaObject(CompletionRequest.class);
        if (position != null) {
            request.setPosition(new CodeLocation(position.getIntValue("lineNumber"), position.getIntValue("columnNumber")));
        }
        return request;
    }

    private String ownerUid(WebSocketSession ws) {
        return (String) ws.getAttributes().get(WebSoInterceptor.WS_PUSER_ID);
    }

    private String getHost(WebSocketSession ws) {
        // for aliyun  slb
        String host = ws.getHandshakeHeaders().getFirst("X-Forwarded-For");
        if (!StringUtils.isEmpty(host)) {
            return host;
        }

        return ws.getRemoteAddress().getHostString();
    }

    public void broadcastWrite(WsType type, Object data) {
        channelMap.forEach((channelKey, ws) -> WsUtils.writeToSocket(ws, type, data));
        log.info("WS[Broadcast]:SEND user(" + uid + "), type:" + type);
    }

    public void directWrite(String channelKey, WsType type, WsResMsg data) {
        WebSocketSession ws = channelMap.get(channelKey);
        if (ws != null) {
            WsUtils.writeToSocket(ws, type, data);
            log.info("WS[" + channelKey + "]:SEND user(" + uid + "), type:" + type);
        } else {
            log.info("WS[" + channelKey + "]:SEND user(" + uid + "), type:" + type + ", Drop.");
        }
    }
}

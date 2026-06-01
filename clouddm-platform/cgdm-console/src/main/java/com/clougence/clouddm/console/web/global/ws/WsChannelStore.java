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
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.clouddm.console.web.model.fo.editor.language.WsLanguageFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.service.editor.DsConsoleEditorService;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class WsChannelStore {

    private final String                        uid;
    private final DsConsoleEditorService        editorService;
    private final Map<String, WebSocketSession> channelMap = new ConcurrentHashMap<>();

    public WsChannelStore(String uid, DsConsoleEditorService editorService){
        this.uid = uid;
        this.editorService = editorService;
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
        if (reqMsg.getType() == null) {
            log.warn("WS[" + channelKey + "]:RECEIVE user(" + uid + "), missing request type.");
            return;
        } else {
            log.info("WS[" + channelKey + "]:RECEIVE user(" + uid + "), type:" + reqMsg.getType().getCode());
        }

        switch (reqMsg.getType()) {
            case WS_REQ_ECHO:
                WsUtils.writeToSocket(ws, WsType.WS_RES_ECHO, reqMsg.getObject());
                return;
            case WS_REQ_QUERY:
                WsQueryFO qfo = JSONObject.parseObject(reqMsg.getObject(), WsQueryFO.class);
                qfo.setChannelKey(channelKey);
                qfo.setPrimaryUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_PUSER_ID));
                qfo.setCurrentUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
                qfo.setRequestTime(System.currentTimeMillis());
                qfo.setClientIp(getHost(ws));
                this.editorService.offerQueryRequest(qfo, DmGlobalEventBus::triggerQueryResultEvent);
                return;
            case WS_REQ_LANGUAGE:
                WsLanguageFO lfo = JSONObject.parseObject(reqMsg.getObject(), WsLanguageFO.class);
                lfo.setChannelKey(channelKey);
                lfo.setPrimaryUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_PUSER_ID));
                lfo.setCurrentUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
                lfo.setRequestTime(System.currentTimeMillis());
                lfo.setClientIp(getHost(ws));
                this.editorService.offerLanguageRequest(lfo, DmGlobalEventBus::triggerLanguageResultEvent);
                return;
            default:
                throw new UnsupportedOperationException("Request WsType '" + reqMsg.getType() + "' Unsupported.");
        }
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

    public void directWrite(String channelKey, WsType type, Object data) {
        WebSocketSession ws = channelMap.get(channelKey);
        if (ws != null) {
            WsUtils.writeToSocket(ws, type, data);
            log.info("WS[" + channelKey + "]:SEND user(" + uid + "), type:" + type);
        } else {
            log.info("WS[" + channelKey + "]:SEND user(" + uid + "), type:" + type + ", Drop.");
        }
    }
}

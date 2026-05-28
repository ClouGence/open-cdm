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
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.jwtsession.JwtCheckResult;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsResMsg;
import com.clougence.clouddm.console.web.model.vo.system.WsSysMsg;
import com.clougence.clouddm.console.web.service.editor.query.ConsoleQueryApi;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.Resource;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequestAuth(strategy = AuthStrategy.RefAnyOnes)
@ServerEndpoint(DmControllerUrlPrefix.CONSOLE_PREFIX + "/ws/channel")
public class WsChannel extends TextWebSocketHandler implements UnifiedPostConstruct {

    private final Map<String, WsChannelStore> sessionMap = new ConcurrentHashMap<>();
    private final AtomicBoolean               inited     = new AtomicBoolean(false);
    @Resource
    private ConsoleQueryApi                   queryServiceApi;

    @Override
    public void init() throws Exception {
        this.inited.set(true);
        // query request & response
        DmGlobalEventBus.addQueryResultEventListen(msg -> this.wsDirectChannel(WsType.WS_RES_QUERY, msg.getCurUserId(), msg));
        // export info
        DmGlobalEventBus.addQueryResultExportListen(exportVO -> this.wsBroadcastChannel(WsType.WS_RES_EXPORT_EVENT, exportVO.getUid(), exportVO));
        // async task
        DmGlobalEventBus.addDmAsyncEventListen(taskDO -> this.wsBroadcastChannel(WsType.WS_RES_ASYNC_EVENT, taskDO.getUid(), DmConvertUtils.convertToDmAsyncTaskVO(taskDO)));
        // driver download
        DmGlobalEventBus.addDriverDownloadEventListen(event -> this.wsBroadcastChannel(WsType.WS_RES_DRIVER_DOWNLOAD_EVENT, event.uid(), event.progressVO()));
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession ws) {
        boolean inited = this.inited.get();
        WsSysMsg status = new WsSysMsg();
        status.setCurUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
        status.setChannelKey(WsUtils.getChannelKey(ws));
        status.setServiceReady(inited);
        WsUtils.writeToSocket(ws, WsType.WS_SYS_STATUS, status);

        if (inited) {
            JwtCheckResult checkResult = (JwtCheckResult) ws.getAttributes().get(WebSoInterceptor.WS_CHECK_RESULT);
            if (checkResult.isSuccess()) {
                this.acceptChannel(ws);
            } else {
                WsUtils.writeToSocket(ws, WsType.WS_RES_ERROR, checkResult.getErrorCode() + " : " + checkResult.getMessage());
                this.closeAndLog(ws, "from local WS_CHECK_RESULT failed.");
            }
        } else {
            this.closeAndLog(ws, "from local service is not ready yet.");
        }
    }

    private WsChannelStore acceptChannel(WebSocketSession ws) {
        if (ws.isOpen()) {
            String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
            String channelKey = WsUtils.getChannelKey(ws);

            WsChannelStore channelStore = this.sessionMap.computeIfAbsent(uid, s -> new WsChannelStore(uid, this.queryServiceApi));
            if (!channelStore.containsChannel(channelKey)) {
                channelStore.acceptChannel(channelKey, ws);
            }
            return channelStore;
        }

        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession ws, CloseStatus closeStatus) {
        String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        if (StringUtils.isNotBlank(uid)) {
            WsChannelStore channelStore = this.sessionMap.get(uid);
            if (channelStore != null) {
                channelStore.closeChannel(WsUtils.getChannelKey(ws));
            }
            this.closeAndLog(ws, "from closeStatus = " + closeStatus);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable e) throws Exception {
        String message = ExceptionUtils.getRootCauseMessage(e);
        log.error(e.getMessage(), message);
        WsUtils.writeToSocket(wss, WsType.WS_RES_ERROR, message);
        wss.close(CloseStatus.NORMAL);
    }

    @Override
    public void handleMessage(WebSocketSession ws, WebSocketMessage<?> msg) {
        String curUser = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        WsChannelStore channelStore = this.acceptChannel(ws);

        if (channelStore == null) {
            this.closeAndLog(ws, "session out of control.");
            return;
        }

        try {
            String textBody = msg.getPayload().toString().trim();
            if (StringUtils.isBlank(textBody) || StringUtils.equals(textBody, "[]") || StringUtils.equals(textBody, "{}")) {
                log.warn("WS[" + WsUtils.getChannelKey(ws) + "]:RECEIVE user(" + curUser + "), empty message.");
                return;
            }

            WsMsg wsMsg = JSONObject.parseObject(textBody, WsMsg.class);
            wsMsg.setChannelKey(WsUtils.getChannelKey(ws));
            channelStore.handleMessage(ws, wsMsg);
        } catch (Exception e) {
            log.error("WS[" + WsUtils.getChannelKey(ws) + "]:RECEIVE user(" + curUser + "), handleError " + e.getMessage(), e);
        }
    }

    private void closeAndLog(WebSocketSession ws, String logMsg) {
        String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        if (StringUtils.isNotBlank(uid)) {
            String channelKey = WsUtils.getChannelKey(ws);
            WsChannelStore channelStore = this.sessionMap.get(uid);
            if (channelStore != null) {
                if (channelStore.containsChannel(channelKey)) {
                    channelStore.closeChannel(channelKey);
                }
                if (channelStore.getChannelMap().isEmpty()) {
                    this.sessionMap.remove(uid);
                }
            }
        }

        try {
            log.info("WS[" + WsUtils.getChannelKey(ws) + "]:CLOSED user(" + uid + "), " + logMsg);
            ws.close(CloseStatus.NORMAL);
        } catch (Exception e) {
            log.error("WS[" + WsUtils.getChannelKey(ws) + "]:CLOSED user(" + uid + "), " + logMsg, e);
            IOUtils.closeQuietly(ws);
        }
    }

    private void wsBroadcastChannel(WsType type, String uid, Object data) {
        WsChannelStore channelStore = this.sessionMap.get(uid);
        if (channelStore == null) {
            log.info("WS[Broadcast]:SEND user(" + uid + "), WsType=" + type + ", Drop.");
        } else {
            channelStore.broadcastWrite(type, data);
        }
    }

    private void wsDirectChannel(WsType type, String uid, WsResMsg data) {
        String channelKey = data.getChannelKey();

        if (StringUtils.isBlank(channelKey)) {
            return;
        }

        WsChannelStore channelStore = this.sessionMap.get(uid);
        if (channelStore == null || !channelStore.containsChannel(channelKey)) {
            log.info("WS[Direct]:SEND user(" + uid + "), WsType=" + type + ", Drop.");
        } else {
            channelStore.directWrite(channelKey, type, data);
        }
    }
}

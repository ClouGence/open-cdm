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

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleInternalClient;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleInternalAction;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleInternalRequest;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleNotification;
import com.clougence.clouddm.console.web.component.config.ConsoleClusterProperties;
import com.clougence.clouddm.console.web.constants.ConsoleInternalPaths;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.clouddm.console.web.global.session.SessionCheckResult;
import com.clougence.clouddm.console.web.model.vo.editor.WsResult;
import com.clougence.clouddm.console.web.model.vo.system.WsSysMsg;
import com.clougence.clouddm.console.web.service.editor.DsConsoleEditorService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.system.DmSysConsoleDO;
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

    private final Map<Long, WsChannelStore> sessionMap = new ConcurrentHashMap<>();
    private final AtomicBoolean             inited     = new AtomicBoolean(false);
    @Resource
    private DsConsoleEditorService          editorApi;
    @Resource
    private WsSessionGuard                  sessionGuard;
    @Resource
    private ConsoleClusterService           cluster;
    @Resource
    private ConsoleClusterProperties        clusterProperties;
    @Resource
    private ConsoleInternalClient           internalClient;
    private ThreadPoolExecutor              notifications;
    private boolean                         stopped;

    @Override
    public synchronized void init() throws Exception {
        if (this.stopped || this.notifications != null) {
            return;
        }

        this.notifications = new ThreadPoolExecutor(2, 8, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(256), r -> {
            Thread thread = new Thread(r, "console-user-notification");
            thread.setDaemon(true);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());

        try {
            // query request & response
            DmGlobalEventBus.addQueryResultEventListen(o -> this.wsDirectChannel(WsType.WS_RES_QUERY, o));
            // language request & response
            DmGlobalEventBus.addLanguageResultEventListen(o -> this.wsDirectChannel(WsType.WS_RES_LANGUAGE, o));
            // export info
            DmGlobalEventBus.addQueryResultExportListen(o -> this.wsBroadcastChannel(WsType.WS_RES_EXPORT_EVENT, o.getUid(), o));
            // operation audit export info
            DmGlobalEventBus.addOpAuditExportListen(o -> this.wsBroadcastChannel(WsType.WS_RES_EXPORT_EVENT, o.getUid(), o));
            // async task
            DmGlobalEventBus.addDmAsyncEventListen(o -> this.wsBroadcastChannel(WsType.WS_RES_ASYNC_EVENT, o.getUid(), DmConvertUtils.convertToDmAsyncTaskVO(o)));
            // driver download
            DmGlobalEventBus.addDriverDownloadEventListen(o -> this.wsBroadcastChannel(WsType.WS_RES_DRIVER_DOWNLOAD_EVENT, o.uid(), o.progressVO()));
            this.inited.set(true);
        } catch (Exception e) {
            this.notifications.shutdownNow();
            throw e;
        }
    }

    @Override
    public synchronized void stop() {
        this.stopped = true;
        this.inited.set(false);
        for (WsChannelStore store : this.sessionMap.values()) {
            for (WebSocketSession ws : store.getChannelMap().values()) {
                this.closeAndLog(ws, "console stopped or lost its lease", CloseStatus.SERVICE_RESTARTED);
            }
        }

        if (this.notifications != null) {
            this.notifications.shutdownNow();
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession ws) {
        if (!this.inited.get() || !this.cluster.isAcceptingWork()) {
            this.closeAndLog(ws, "local service is not ready", CloseStatus.SERVICE_RESTARTED);
            return;
        }

        SessionCheckResult checkResult = (SessionCheckResult) ws.getAttributes().get(WebSoInterceptor.WS_CHECK_RESULT);
        if (checkResult == null || !checkResult.isSuccess()) {
            closeQuietly(ws, CloseStatus.POLICY_VIOLATION, "pre-handshake check failed");
            return;
        }

        if (!this.sessionGuard.register(ws)) {
            return;
        }

        WsSysMsg status = new WsSysMsg();
        status.setCurUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
        status.setChannelKey(WsUtils.getChannelKey(ws));
        status.setServiceReady(true);
        WsUtils.writeToSocket(ws, WsType.WS_SYS_STATUS, status);

        this.acceptChannel(ws);
    }

    private void acceptChannel(WebSocketSession ws) {
        if (!ws.isOpen()) {
            return;
        }

        Long sessionId = (Long) ws.getAttributes().get(WebSoInterceptor.WS_SESSION_ID);
        String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        String channelKey = WsUtils.getChannelKey(ws);

        this.sessionMap.compute(sessionId, (k, old) -> {
            WsChannelStore store = old != null ? old : new WsChannelStore(uid, this.editorApi);
            if (!store.containsChannel(channelKey)) {
                store.acceptChannel(channelKey, ws);
            }
            return store;
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession ws, CloseStatus closeStatus) {
        removeChannel(ws);
        this.sessionGuard.unregister(ws);
    }

    private void removeChannel(WebSocketSession ws) {
        Long sessionId = (Long) ws.getAttributes().get(WebSoInterceptor.WS_SESSION_ID);
        if (sessionId == null) {
            return;
        }
        this.sessionMap.computeIfPresent(sessionId, (k, store) -> {
            store.closeChannel(WsUtils.getChannelKey(ws));
            return store.getChannelMap().isEmpty() ? null : store;
        });
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable e) throws Exception {
        String message = ExceptionUtils.getRootCauseMessage(e);
        log.error("WS[" + WsUtils.getChannelKey(wss) + "]:TRANSPORT_ERROR " + message, e);
        if (wss.isOpen()) {
            WsUtils.writeToSocket(wss, WsType.WS_RES_ERROR, message);
        }
        wss.close(CloseStatus.NORMAL);
    }

    @Override
    public void handleMessage(WebSocketSession ws, WebSocketMessage<?> msg) {
        String curUser = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        Long sessionId = (Long) ws.getAttributes().get(WebSoInterceptor.WS_SESSION_ID);
        // lookup only; a removed connection is never re-registered by inbound traffic
        WsChannelStore channelStore = sessionId == null ? null : this.sessionMap.get(sessionId);

        if (channelStore == null) {
            this.closeAndLog(ws, "session out of control.", CloseStatus.NORMAL);
            return;
        }

        LocaleContext previousContext = LocaleContextHolder.getLocaleContext();
        try {
            LocaleContextHolder.setLocale((Locale) ws.getAttributes().get(WebSoInterceptor.WS_LOCALE));
            String textBody = msg.getPayload().toString().trim();
            if (StringUtils.isBlank(textBody) || StringUtils.equals(textBody, "[]") || StringUtils.equals(textBody, "{}")) {
                log.warn("WS[" + WsUtils.getChannelKey(ws) + "]:RECEIVE user(" + curUser + "), empty message.");
                return;
            }

            WsMsg wsMsg = JSONObject.parseObject(textBody, WsMsg.class);
            wsMsg.setChannelKey(WsUtils.getChannelKey(ws));

            if (wsMsg.getType() == WsType.WS_REQ_QUERY || wsMsg.getType() == WsType.WS_REQ_LANGUAGE) {
                if (!this.sessionGuard.revalidate(ws)) {
                    return;
                }
            }

            channelStore.handleMessage(ws, wsMsg);
        } catch (Exception e) {
            log.error("WS[" + WsUtils.getChannelKey(ws) + "]:RECEIVE user(" + curUser + "), handleError " + e.getMessage(), e);
        } finally {
            LocaleContextHolder.setLocaleContext(previousContext);
        }
    }

    private void closeAndLog(WebSocketSession ws, String logMsg, CloseStatus closeStatus) {
        removeChannel(ws);
        this.sessionGuard.unregister(ws);
        closeQuietly(ws, closeStatus, logMsg);
    }

    private void closeQuietly(WebSocketSession ws, CloseStatus closeStatus, String logMsg) {
        String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        try {
            log.info("WS[" + WsUtils.getChannelKey(ws) + "]:CLOSED user(" + uid + "), " + logMsg);
            ws.close(closeStatus);
        } catch (Exception e) {
            log.error("WS[" + WsUtils.getChannelKey(ws) + "]:CLOSED user(" + uid + "), " + logMsg, e);
            IOUtils.closeQuietly(ws);
        }
    }

    private void wsBroadcastChannel(WsType type, String uid, Object data) {
        broadcastLocal(type, uid, data);
        if (!clusterProperties.isEnabled() || !cluster.isLeaseValid()) {
            return;
        }

        ConsoleNotification notification = new ConsoleNotification();
        notification.setUid(uid);
        notification.setType(type);
        notification.setData(data);
        try {
            notifications.execute(() -> notifyPeerConsoles(notification));
        } catch (RejectedExecutionException e) {
            log.warn("User notification queue is full: uid={}, type={}", uid, type);
        }
    }

    private void notifyPeerConsoles(ConsoleNotification notification) {
        ConsoleInternalRequest request = new ConsoleInternalRequest(ConsoleInternalAction.DELIVER_USER_EVENT, notification);
        try {
            for (DmSysConsoleDO member : cluster.readyMembers()) {
                if (cluster.getInstanceId().equals(member.getInstanceId())) {
                    continue;
                }

                try {
                    internalClient.post(member, ConsoleInternalPaths.DISPATCH, request, System.currentTimeMillis() + 3000);
                } catch (Exception e) {
                    log.warn("User notification delivery failed: target={}, uid={}, type={}", member.getInstanceId(), notification.getUid(), notification.getType(), e);
                }
            }
        } catch (Exception e) {
            log.warn("List notification recipients failed: uid={}, type={}", notification.getUid(), notification.getType(), e);
        }
    }

    public void broadcastLocal(WsType type, String uid, Object data) {
        boolean sent = false;
        for (WsChannelStore channelStore : this.sessionMap.values()) {
            if (uid != null && uid.equals(channelStore.getUid())) {
                channelStore.broadcastWrite(type, data);
                sent = true;
            }
        }
        if (!sent) {
            log.info("WS[Broadcast]:SEND user(" + uid + "), WsType=" + type + ", Drop.");
        }
    }

    private void wsDirectChannel(WsType type, WsResult data) {
        String uid = data.getCurUserId();
        String channelKey = data.getChannelKey();

        if (StringUtils.isBlank(channelKey)) {
            return;
        }

        boolean sent = false;
        for (WsChannelStore channelStore : this.sessionMap.values()) {
            if (uid != null && uid.equals(channelStore.getUid()) && channelStore.containsChannel(channelKey)) {
                channelStore.directWrite(channelKey, type, data);
                sent = true;
            }
        }
        if (!sent) {
            log.info("WS[Direct]:SEND user(" + uid + "), WsType=" + type + ", Drop.");
        }
    }
}

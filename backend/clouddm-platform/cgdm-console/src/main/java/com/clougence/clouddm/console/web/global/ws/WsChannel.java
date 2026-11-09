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
import java.util.concurrent.ConcurrentHashMap;
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
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.session.SessionAccountPolicy;
import com.clougence.clouddm.console.web.global.session.SessionConfig;
import com.clougence.clouddm.console.web.global.session.SessionCheckResult;
import com.clougence.clouddm.console.web.global.session.SessionPrincipal;
import com.clougence.clouddm.console.web.global.session.SessionService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.clouddm.console.web.model.vo.editor.WsResult;
import com.clougence.clouddm.console.web.model.vo.system.WsSysMsg;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.editor.DsConsoleEditorService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpLocal;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
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
    private SessionService                  sessionService;
    @Resource
    private RdpUserService                  userService;
    @Resource
    private RdpRoleService                  roleService;
    @Resource
    private SessionAccountPolicy            accountPolicy;
    @Resource
    private SessionConfig                   sessionConfig;

    @Override
    public void init() throws Exception {
        this.inited.set(true);
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
        SessionCheckResult checkResult = (SessionCheckResult) ws.getAttributes().get(WebSoInterceptor.WS_CHECK_RESULT);

        // no payload may leave the socket before authoritative session registration
        if (!inited || checkResult == null || !checkResult.isSuccess()) {
            closeQuietly(ws, CloseStatus.POLICY_VIOLATION, "pre-handshake check failed");
            return;
        }
        if (!this.sessionGuard.register(ws)) {
            return;
        }

        WsSysMsg status = new WsSysMsg();
        status.setCurUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
        status.setChannelKey(WsUtils.getChannelKey(ws));
        status.setServiceReady(inited);
        WsUtils.writeToSocket(ws, WsType.WS_SYS_STATUS, status);

        this.acceptChannel(ws);
    }

    private WsChannelStore acceptChannel(WebSocketSession ws) {
        if (!ws.isOpen()) {
            return null;
        }

        Long sessionId = (Long) ws.getAttributes().get(WebSoInterceptor.WS_SESSION_ID);
        String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        String channelKey = WsUtils.getChannelKey(ws);

        WsChannelStore channelStore = this.sessionMap.compute(sessionId, (k, old) -> {
            WsChannelStore store = old != null ? old : new WsChannelStore(uid, this.editorApi);
            if (!store.containsChannel(channelKey)) {
                store.acceptChannel(channelKey, ws);
            }
            return store;
        });
        return channelStore;
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
                if (!this.authoritativeInbound(ws, sessionId)) {
                    return;
                }
            }

            channelStore.handleMessage(ws, wsMsg);
        } catch (Exception e) {
            log.error("WS[" + WsUtils.getChannelKey(ws) + "]:RECEIVE user(" + curUser + "), handleError " + e.getMessage(), e);
        } finally {
            LocaleContextHolder.setLocaleContext(previousContext);
            RdpWebUtils.cleanLocal();
            RdpLocal.setLocal(null);
        }
    }

    /**
     * Authoritative per-message re-check for business requests; never trusts the handshake.
     * Any failure revokes the ws state and closes asynchronously without dispatch.
     */
    private boolean authoritativeInbound(WebSocketSession ws, Long sessionId) {
        long startNanos = System.nanoTime();
        try {
            WsSessionState state = (WsSessionState) ws.getAttributes().get(WsSessionGuard.WS_SESSION_STATE);
            if (state == null || state.isRevoked()) {
                this.sessionGuard.asyncClose(ws);
                return false;
            }

            SessionPrincipal principal = this.sessionService.authenticateById(sessionId);
            DmAuthUserDO user = principal == null ? null : this.userService.getUserByUid(principal.getUid());
            if (principal == null || user == null || user.isDisable() || this.accountPolicy.isPasswordExpired(user) || user.getRoleId() == null) {
                failInbound(ws, state, "account invalid");
                return false;
            }

            DmAuthUserDO primaryUser = user;
            if (user.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
                primaryUser = this.userService.getUserById(user.getParentId());
                if (primaryUser == null) {
                    failInbound(ws, state, "primary user missing");
                    return false;
                }
            }
            DmAuthRoleDO role = this.roleService.fetchRoleById(user.getRoleId());
            if (role == null) {
                failInbound(ws, state, "role missing");
                return false;
            }
            // same bypass semantics as HTTP verifyAuth, but business label required for others
            if (!user.isMaintainer() && user.getAccountType() != AccountType.PRIMARY_ACCOUNT
                && (role.getRoleAuthLabels() == null || !role.getRoleAuthLabels().contains(DM_QUERY_CONSOLE))) {
                failInbound(ws, state, "query label missing");
                return false;
            }

            long now = System.currentTimeMillis();
            if (state.isRevoked() || now >= principal.getExpireTimeMs() || now >= this.sessionConfig.absoluteDeadlineMs(principal.getCreatedTimeMs())
                || System.nanoTime() - startNanos >= WsSessionState.FRESH_WINDOW_NANOS) {
                failInbound(ws, state, "validation timeout");
                return false;
            }

            ws.getAttributes().put(WebSoInterceptor.WS_USER_ID, user.getUid());
            ws.getAttributes().put(WebSoInterceptor.WS_PUSER_ID, primaryUser.getUid());
            ws.getAttributes().put(WebSoInterceptor.WS_USER_ROLE, user.getRoleId());

            RdpWebUtils.currentLocal().setCurrentUser(user);
            RdpWebUtils.currentLocal().setCurrentRole(role);
            RdpWebUtils.currentLocal().setPrimaryUser(primaryUser);
            RdpWebUtils.currentLocal().setSessionPrincipal(principal);
            return true;
        } catch (Exception e) {
            log.error("WS[" + WsUtils.getChannelKey(ws) + "]:INBOUND auth check failed", e);
            WsSessionState state = (WsSessionState) ws.getAttributes().get(WsSessionGuard.WS_SESSION_STATE);
            if (state != null) {
                state.markRevoked();
            }
            this.sessionGuard.asyncClose(ws);
            return false;
        }
    }

    private void failInbound(WebSocketSession ws, WsSessionState state, String reason) {
        log.info("WS[" + WsUtils.getChannelKey(ws) + "]:INBOUND reject " + reason);
        if (state != null) {
            state.markRevoked();
        }
        this.sessionGuard.asyncClose(ws);
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

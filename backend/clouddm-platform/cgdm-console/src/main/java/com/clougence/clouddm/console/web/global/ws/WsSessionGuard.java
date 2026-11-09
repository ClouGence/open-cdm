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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.clouddm.console.web.global.session.SessionAccountPolicy;
import com.clougence.clouddm.console.web.global.session.SessionConfig;
import com.clougence.clouddm.console.web.global.session.SessionPrincipal;
import com.clougence.clouddm.console.web.global.session.SessionRevokedEvent;
import com.clougence.clouddm.console.web.global.session.SessionService;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.NamedThreadFactory;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WsSessionGuard implements UnifiedPostConstruct {

    public static final String                WS_SESSION_STATE   = "WS_SESSION_STATE";
    private static final long                 VALIDATE_PERIOD_MS = 10_000L;
    private static final int                  VALIDATE_BATCH     = 500;

    private static volatile WsSessionGuard    INSTANCE;

    private final Map<Long, WsSessionState>   states             = new ConcurrentHashMap<>();
    private volatile ScheduledExecutorService validator;
    private volatile ExecutorService          closeExecutor;

    @Resource
    private SessionService                    sessionService;
    @Resource
    private SessionConfig                     sessionConfig;
    @Resource
    private SessionAccountPolicy              accountPolicy;
    @Resource
    private RdpUserService                    userService;
    @Resource
    private RdpRoleService                    roleService;

    @Override
    public void init() throws Exception {
        if (this.validator != null) {
            return;
        }
        this.validator = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("ws-session-validator", true));
        this.validator.scheduleWithFixedDelay(this::validateAllSafely, VALIDATE_PERIOD_MS, VALIDATE_PERIOD_MS, TimeUnit.MILLISECONDS);
        this.closeExecutor = new ThreadPoolExecutor(2,
            4,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(512),
            new NamedThreadFactory("ws-session-close", true),
            new ThreadPoolExecutor.AbortPolicy());
        INSTANCE = this;
    }

    @Override
    @PreDestroy
    public void stop() {
        INSTANCE = null;
        if (this.validator != null) {
            this.validator.shutdownNow();
        }
        if (this.closeExecutor != null) {
            this.closeExecutor.shutdownNow();
        }
        for (WsSessionState state : this.states.values()) {
            state.markRevoked();
            for (WebSocketSession ws : state.getConnections()) {
                IOUtils.closeQuietly(ws);
            }
        }
        this.states.clear();
    }

    /**
     * Provisional registration BEFORE the DB read so a revoke event can never miss a
     * not-yet-inserted state; never stores raw credentials.
     */
    public boolean register(WebSocketSession ws) {
        Long sessionId = (Long) ws.getAttributes().get(WebSoInterceptor.WS_SESSION_ID);
        if (sessionId == null) {
            asyncClose(ws);
            return false;
        }

        final WsSessionState[] box = new WsSessionState[1];
        this.states.compute(sessionId, (k, old) -> {
            WsSessionState state = old != null ? old : new WsSessionState(k, (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
            state.getConnections().add(ws);
            ws.getAttributes().put(WS_SESSION_STATE, state);
            box[0] = state;
            return state;
        });
        WsSessionState state = box[0];

        long checkedAtNanos = System.nanoTime();
        try {
            SessionPrincipal principal = this.sessionService.authenticateById(sessionId);
            DmAuthUserDO user = principal == null ? null : this.userService.getUserByUid(principal.getUid());
            String roleFingerprint = fingerprint(user);
            synchronized (state) {
                if (state.isRevoked() || principal == null || roleFingerprint == null || System.nanoTime() - checkedAtNanos >= WsSessionState.FRESH_WINDOW_NANOS
                    || (state.getRoleFingerprint() != null && !state.getRoleFingerprint().equals(roleFingerprint))) {
                    state.markRevoked();
                } else {
                    state.refresh(checkedAtNanos, hardExpiry(principal), roleFingerprint);
                }
            }
        } catch (Exception e) {
            log.error("WS session registration failed", e);
            state.markRevoked();
        }
        if (!state.isFresh()) {
            revokeAndClose(state);
            return false;
        }
        return true;
    }

    public void unregister(WebSocketSession ws) {
        WsSessionState state = (WsSessionState) ws.getAttributes().get(WS_SESSION_STATE);
        if (state == null) {
            return;
        }
        this.states.computeIfPresent(state.getSessionId(), (k, cur) -> {
            cur.getConnections().remove(ws);
            return cur.getConnections().isEmpty() ? null : cur;
        });
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void onSessionRevoked(SessionRevokedEvent event) {
        if (event.getSessionId() != null) {
            revokeAndClose(this.states.get(event.getSessionId()));
            return;
        }
        if (event.getUid() != null) {
            for (WsSessionState state : this.states.values()) {
                if (event.getUid().equals(state.getUid())) {
                    revokeAndClose(state);
                }
            }
        }
    }

    private void validateAllSafely() {
        try {
            validateAll();
        } catch (Exception e) {
            log.error("ws session periodic validation failed", e);
        }
    }

    private void validateAll() {
        List<Long> ids = new ArrayList<>(this.states.keySet());
        for (int i = 0; i < ids.size(); i += VALIDATE_BATCH) {
            Map<String, String> fingerprintCache = new HashMap<>();
            List<Long> chunk = ids.subList(i, Math.min(i + VALIDATE_BATCH, ids.size()));
            // stamp taken before the DB round trip so slow queries cannot extend trust
            long stampNanos = System.nanoTime();
            Map<Long, SessionPrincipal> valid;
            try {
                valid = this.sessionService.authenticateByIds(chunk);
            } catch (Exception e) {
                log.error("ws session batch validation failed, revoking " + chunk.size() + " sessions", e);
                valid = Map.of();
            }
            for (Long sessionId : chunk) {
                WsSessionState state = this.states.get(sessionId);
                if (state == null) {
                    continue;
                }
                SessionPrincipal principal = valid.get(sessionId);
                String fingerprint = null;
                try {
                    if (principal != null) {
                        fingerprint = fingerprintCache.computeIfAbsent(principal.getUid(), k -> {
                            DmAuthUserDO user = this.userService.getUserByUid(k);
                            return fingerprint(user);
                        });
                    }
                } catch (Exception e) {
                    log.error("WS account validation failed", e);
                }
                synchronized (state) {
                    if (principal == null || fingerprint == null || !fingerprint.equals(state.getRoleFingerprint())
                        || System.nanoTime() - stampNanos >= WsSessionState.FRESH_WINDOW_NANOS) {
                        // Fence delivery before queuing a potentially slow socket close.
                        state.markRevoked();
                    } else {
                        state.refresh(stampNanos, hardExpiry(principal), fingerprint);
                    }
                }
            }
            for (Long sessionId : chunk) {
                WsSessionState state = this.states.get(sessionId);
                if (state != null && state.isRevoked()) {
                    for (WebSocketSession ws : Set.copyOf(state.getConnections())) {
                        asyncClose(ws);
                    }
                }
            }
        }
    }

    private String fingerprint(DmAuthUserDO user) {
        if (user == null || user.isDisable() || this.accountPolicy.isPasswordExpired(user) || user.getRoleId() == null) {
            return null;
        }
        DmAuthUserDO primaryUser = user;
        if (user.getParentId() != null) {
            primaryUser = this.userService.getUserById(user.getParentId());
            if (primaryUser == null) {
                return null;
            }
        }
        DmAuthRoleDO role = this.roleService.fetchRoleById(user.getRoleId());
        if (role == null) {
            return null;
        }
        TreeSet<String> labels = role.getRoleAuthLabels() == null ? new TreeSet<>() : new TreeSet<>(role.getRoleAuthLabels());
        return user.getRoleId() + "|" + String.join(",", labels);
    }

    private long hardExpiry(SessionPrincipal principal) {
        return Math.min(principal.getExpireTimeMs(), principal.getCreatedTimeMs() + this.sessionConfig.absoluteLifetimeMs());
    }

    private void revokeAndClose(WsSessionState state) {
        if (state == null) {
            return;
        }
        state.markRevoked();
        for (WebSocketSession ws : Set.copyOf(state.getConnections())) {
            asyncClose(ws);
        }
    }

    public void asyncClose(WebSocketSession ws) {
        ExecutorService executor = this.closeExecutor;
        if (executor == null || executor.isShutdown()) {
            // stays revoked; nothing is permitted to send again
            return;
        }
        try {
            executor.execute(() -> closeQuiet1008(ws));
        } catch (RejectedExecutionException e) {
            // fails closed: revoked state already gates sends; next validator pass retries close
            log.warn("WS[" + WsUtils.getChannelKey(ws) + "]:close executor saturated, close deferred");
        }
    }

    private void closeQuiet1008(WebSocketSession ws) {
        try {
            ws.close(CloseStatus.POLICY_VIOLATION);
        } catch (Exception e) {
            IOUtils.closeQuietly(ws);
        }
    }

    /**
     * Cheap producer-side gate; never touches the DB.
     */
    public static boolean allowSend(WebSocketSession ws) {
        WsSessionState state = (WsSessionState) ws.getAttributes().get(WS_SESSION_STATE);
        if (INSTANCE == null || state == null || !state.isFresh()) {
            if (state != null) {
                state.markRevoked();
            }
            WsSessionGuard guard = INSTANCE;
            if (guard != null) {
                guard.asyncClose(ws);
            }
            // payload dropped; never a blocking close on the producer path
            return false;
        }
        return true;
    }
}

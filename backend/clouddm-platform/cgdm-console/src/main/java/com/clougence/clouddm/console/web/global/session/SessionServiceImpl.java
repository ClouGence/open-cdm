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
package com.clougence.clouddm.console.web.global.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.util.NamedThreadFactory;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthSessionDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.PreDestroy;
import java.util.Collection;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService, UnifiedPostConstruct {

    private static final int          AUTH_BY_IDS_BATCH = 500;

    private static final SecureRandom SECURE_RANDOM     = new SecureRandom();
    private static final int          TOKEN_BYTES       = 32;
    private static final int          CLEAN_BATCH       = 500;

    @Resource
    private AuthDal                   authDal;
    @Resource
    private SessionConfig             sessionConfig;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    private ScheduledExecutorService  cleanerExecutor;

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public IssuedSession create(String uid, LoginAuthType loginType) {
        // contract: caller already holds the user row lock; re-assert inside it
        DmAuthUserDO user = authDal.userMapper().lockByUid(uid);
        if (user == null || user.isDisable()) {
            throw new IllegalStateException("can not create session for absent or disabled user.");
        }

        byte[] tokenBytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        long now = System.currentTimeMillis();
        DmAuthSessionDO session = new DmAuthSessionDO();
        session.setUid(uid);
        session.setTokenHash(hashToken(token));
        session.setLoginType(loginType == null ? null : loginType.name());
        session.setGmtCreate(now);
        session.setRenewedTimeMs(now);
        session.setExpireTimeMs(now + sessionConfig.activityWindowMs());
        authDal.sessionMapper().insert(session);

        SessionPrincipal principal = new SessionPrincipal(session.getId(), uid, loginType, now, now, session.getExpireTimeMs());
        return new IssuedSession(principal, token);
    }

    @Override
    public SessionPrincipal authenticate(HttpServletRequest request) {
        String token = SessionCookieService.extractToken(request);
        if (token == null) {
            return null;
        }
        return toValidPrincipal(authDal.sessionMapper().queryByTokenHash(hashToken(token)));
    }

    @Override
    public SessionPrincipal authenticateById(Long sessionId) {
        if (sessionId == null) {
            return null;
        }
        return toValidPrincipal(authDal.sessionMapper().selectById(sessionId));
    }

    private SessionPrincipal toValidPrincipal(DmAuthSessionDO session) {
        if (session == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        if (session.getExpireTimeMs() <= now) {
            return null;
        }
        if (session.getGmtCreate() + sessionConfig.absoluteLifetimeMs() <= now) {
            return null;
        }
        return new SessionPrincipal(session
            .getId(), session.getUid(), LoginAuthType.valueOfCode(session.getLoginType()), session.getGmtCreate(), session.getRenewedTimeMs(), session.getExpireTimeMs());
    }

    @Override
    public Map<Long, SessionPrincipal> authenticateByIds(Collection<Long> sessionIds) {
        Map<Long, SessionPrincipal> valid = new HashMap<>();
        if (sessionIds == null || sessionIds.isEmpty()) {
            return valid;
        }
        List<Long> unique = sessionIds.stream().filter(java.util.Objects::nonNull).distinct().toList();
        for (int i = 0; i < unique.size(); i += AUTH_BY_IDS_BATCH) {
            List<Long> chunk = unique.subList(i, Math.min(i + AUTH_BY_IDS_BATCH, unique.size()));
            for (DmAuthSessionDO session : authDal.sessionMapper().queryByIds(chunk)) {
                SessionPrincipal principal = toValidPrincipal(session);
                if (principal != null) {
                    valid.put(principal.getSessionId(), principal);
                }
            }
        }
        return valid;
    }

    @Override
    public void renewIfNeeded(SessionPrincipal principal) {
        long now = System.currentTimeMillis();
        long absoluteDeadline = principal.getCreatedTimeMs() + sessionConfig.absoluteLifetimeMs();
        if (principal.getExpireTimeMs() <= now || absoluteDeadline <= now) {
            return;
        }
        if (principal.getExpireTimeMs() - now > sessionConfig.renewalThresholdMs()) {
            return;
        }
        long target = Math.min(now + sessionConfig.activityWindowMs(), absoluteDeadline);
        if (target <= principal.getExpireTimeMs()) {
            return;
        }
        authDal.sessionMapper().renewCas(principal.getSessionId(), principal.getExpireTimeMs(), target, now, now, sessionConfig.absoluteLifetimeMs());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void revokeSession(Long sessionId) {
        if (sessionId == null) {
            return;
        }
        authDal.sessionMapper().deleteById(sessionId);
        eventPublisher.publishEvent(new SessionRevokedEvent(sessionId, null));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void revokeUserSessions(String uid) {
        if (StringUtils.isBlank(uid)) {
            return;
        }
        authDal.sessionMapper().deleteByUid(uid);
        authDal.mfaChallengeMapper().deleteByUid(uid);
        eventPublisher.publishEvent(new SessionRevokedEvent(null, uid));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void revokeCurrentSession(HttpServletRequest request) {
        String token = SessionCookieService.extractToken(request);
        if (token == null) {
            return;
        }
        DmAuthSessionDO session = authDal.sessionMapper().queryByTokenHash(hashToken(token));
        if (session == null) {
            return;
        }
        authDal.sessionMapper().deleteById(session.getId());
        eventPublisher.publishEvent(new SessionRevokedEvent(session.getId(), null));
    }

    @Override
    public void init() {
        if (cleanerExecutor != null) {
            return;
        }
        cleanerExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("auth-session-cleaner", true));
        cleanerExecutor.scheduleAtFixedRate(this::cleanExpiredSessions, 60, 60, TimeUnit.SECONDS);
    }

    private void cleanExpiredSessions() {
        try {
            long now = System.currentTimeMillis();
            int deleted = authDal.sessionMapper().deleteExpiredByExpire(now, CLEAN_BATCH);
            int budget = CLEAN_BATCH - deleted;
            if (budget > 0) {
                deleted += authDal.sessionMapper().deleteExpiredByAbsolute(now - sessionConfig.absoluteLifetimeMs(), budget);
            }
            if (deleted > 0) {
                log.info("Deleted {} expired auth sessions.", deleted);
            }
        } catch (Exception e) {
            log.error("Clean expired auth sessions failed.", e);
        }
    }

    @Override
    @PreDestroy
    public void stop() {
        if (cleanerExecutor != null) {
            cleanerExecutor.shutdown();
            cleanerExecutor = null;
        }
    }

    private static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexadecimalUtils.bytes2hex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available.", e);
        }
    }
}

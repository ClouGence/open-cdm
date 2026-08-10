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
package com.clougence.clouddm.console.web.service.login;

import static com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys.LOGIN_INVALID_TOKEN_ERROR;
import static com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys.LOGIN_MFA_PRE_ACTION_TOKEN_ERROR;
import static com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys.LOGIN_MFA_RETRY_LIMIT_EXCEEDED;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.NamedThreadFactory;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthMfaChallengeDO;
import com.clougence.utils.HexadecimalUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MfaChallengeServiceImpl implements MfaChallengeService, UnifiedPostConstruct {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int          TOKEN_BYTES   = 32;

    @Resource
    private AuthDal                  authDal;

    private ScheduledExecutorService cleanerExecutor;

    @Override
    public void init() {
        if (cleanerExecutor != null) {
            return;
        }
        cleanerExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("mfa-challenge-cleaner", true));
        cleanerExecutor.scheduleAtFixedRate(this::cleanExpiredChallenges, 0, 60, TimeUnit.SECONDS);
    }

    private void cleanExpiredChallenges() {
        try {
            int deleted = authDal.mfaChallengeMapper().deleteExpired(System.currentTimeMillis());
            if (deleted > 0) {
                log.info("Deleted {} expired MFA challenges.", deleted);
            }
        } catch (Exception e) {
            log.error("Clean expired MFA challenges failed.", e);
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

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public String createChallenge(String uid, MfaPreActionType actionType, LoginAuthType loginType) {
        byte[] tokenBytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        DmAuthMfaChallengeDO challengeDO = new DmAuthMfaChallengeDO();
        challengeDO.setUid(uid);
        challengeDO.setActionType(actionType.name());
        challengeDO.setLoginType(loginType.name());
        challengeDO.setChallengeTokenHash(hashToken(token));
        challengeDO.setRetryCount(0);
        challengeDO.setExpireTimeMs(System.currentTimeMillis() + RdpUserService.MFA_TOKEN_EXPIRE_SEC * 1000L);
        authDal.mfaChallengeMapper().insert(challengeDO);
        return token;
    }

    @Override
    public DmAuthMfaChallengeDO requirePendingChallenge(String challengeToken, MfaPreActionType actionType) {
        DmAuthMfaChallengeDO challengeDO = authDal.mfaChallengeMapper().queryByTokenHash(hashToken(challengeToken));
        long nowMs = System.currentTimeMillis();
        if (challengeDO == null || !actionType.name().equals(challengeDO.getActionType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(LOGIN_INVALID_TOKEN_ERROR.name()));
        }
        if (challengeDO.getExpireTimeMs() < nowMs) {
            throw new ErrorMessageException(ConsoleErrorCode.LOGIN_MFA_PRE_ACTION_TOKEN_EXPIRED.getCode(),
                DmI18nUtils.getMessage(LOGIN_MFA_PRE_ACTION_TOKEN_ERROR.name(), RdpUserService.MFA_TOKEN_EXPIRE_SEC));
        }
        if (challengeDO.getRetryCount() >= MAX_RETRY_COUNT) {
            throw new ErrorMessageException(ConsoleErrorCode.LOGIN_MFA_RETRY_LIMIT_EXCEEDED.getCode(),
                DmI18nUtils.getMessage(LOGIN_MFA_RETRY_LIMIT_EXCEEDED.name(), MAX_RETRY_COUNT));
        }
        return challengeDO;
    }

    @Override
    public boolean acquireAttempt(DmAuthMfaChallengeDO challengeDO) {
        int updated = authDal.mfaChallengeMapper().acquireAttempt(challengeDO.getId(), challengeDO.getChallengeTokenHash(), challengeDO.getActionType(),
            System.currentTimeMillis(), MAX_RETRY_COUNT);
        return updated == 1;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public boolean consumeChallenge(DmAuthMfaChallengeDO challengeDO) {
        int deleted = authDal.mfaChallengeMapper().deleteForConsume(challengeDO.getId(), challengeDO.getChallengeTokenHash(), challengeDO.getActionType(),
            System.currentTimeMillis(), MAX_RETRY_COUNT);
        return deleted == 1;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexadecimalUtils.bytes2hex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            String msg = "Hash MFA challenge token failed.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}

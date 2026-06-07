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

import static com.clougence.clouddm.console.web.service.auth.RdpUserService.MFA_TOKEN_EXPIRE_SEC;
import static com.clougence.clouddm.console.web.service.auth.RdpUserService.OP_PASSWD_TOEKN_EXPIRE_MS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.login.LoginMFAService;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-21 10:44
 * @since 1.1.3
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final String    issuer = "CloudDM";
    /** default use hmacsha256 */
    @Value("${jwt.secret}")
    private String          secret;
    @Resource
    private DmConsoleConfig rdpConfig;
    private Algorithm       algorithm;

    public Algorithm algorithm() {
        if (this.algorithm == null) {
            this.algorithm = Algorithm.HMAC256(this.secret);
        }
        return this.algorithm;

    }

    @Override
    public DecodedJWT verify(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtTokenName);
        String headerValue = request.getHeader(jwtTokenName);
        Map<String, String> map = StringUtils.toMap(request.getQueryString(), "&", "=");
        String requestParameterValue = map.getOrDefault(jwtTokenName, null);

        String jwtToken;
        if (requestParameterValue != null) {
            jwtToken = requestParameterValue;
        } else if (headerValue != null) {
            jwtToken = headerValue;
        } else if (cookie != null) {
            jwtToken = cookie.getValue();
        } else {
            return null;
        }

        return verifyJwtToken(jwtToken);
    }

    @Override
    public void refreshCookiePeriodOfValidity(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, jwtTokenName);
        if (cookie != null) {
            cookie.setMaxAge(rdpConfig.getLoginExpireTimeSec());
            cookie.setPath("/");

            if (StringUtils.isNotBlank(rdpConfig.getLoginCookieDomain())) {
                cookie.setDomain(rdpConfig.getLoginCookieDomain());
            }

            response.addCookie(cookie);
        }
    }

    @Override
    public void refreshJwtTokenPeriodOfValidity(HttpServletRequest request, HttpServletResponse response, DmAuthUserDO user) {
        Cookie cookie = WebUtils.getCookie(request, jwtTokenName);
        if (cookie != null) {
            DecodedJWT jwt = verifyJwtToken(cookie.getValue());
            LoginAuthType loginType = jwt == null ? null : LoginAuthType.valueOfCode(jwt.getClaim(LOGIN_TYPE).asString());
            cookie.setValue(genJwtToken(user, loginType));
            // same name(path and domain) cookie merge/overwrite
            response.addCookie(cookie);
        }
    }

    @Override
    public DecodedJWT verifyOpToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, opPwdToken);
        String headerValue = request.getHeader(opPwdToken);
        String requestParameterValue = request.getParameter(opPwdToken);
        String jwtToken;
        if (cookie != null) {
            jwtToken = cookie.getValue();
        } else if (headerValue != null) {
            jwtToken = headerValue;
        } else if (requestParameterValue != null) {
            jwtToken = requestParameterValue;
        } else {
            return null;
        }

        return verifyJwtToken(jwtToken);
    }

    @Override
    public DecodedJWT verifyJwtToken(String jwtToken) {
        if (StringUtils.isBlank(jwtToken)) {
            throw new IllegalArgumentException("jwt token can not be empty.");
        }

        try {
            JWTVerifier verifier = JWT.require(algorithm()).withIssuer(issuer).build();
            return verifier.verify(jwtToken);
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return null;
        }
    }

    @Override
    public DecodedJWT verifyMfaActionToken(String mfaActionToken) {
        if (StringUtils.isBlank(mfaActionToken)) {
            throw new IllegalArgumentException("MFA action token can not be empty.");
        }

        try {
            JWTVerifier verifier = JWT.require(algorithm()).withIssuer(issuer).build();
            return verifier.verify(mfaActionToken);
        } catch (IllegalArgumentException | JWTVerificationException e) {
            log.error("JWT verify error,brief msg:" + ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    @Override
    public String genJwtToken(DmAuthUserDO user) {
        return genJwtToken(user, null);
    }

    @Override
    public String genJwtToken(DmAuthUserDO user, LoginAuthType loginType) {
        // token expire time
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(1);
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        long nowMills = zdt.toInstant().toEpochMilli();

        Date issueAt = new Date(nowMills);
        Date expresAt = new Date(nowMills + Math.max(JwtService.minLoginExpireSec * 1000, this.rdpConfig.getLoginExpireTimeSec() * 1000));

        // username used for django-jwt
        return JWT.create()
            .withIssuer(issuer)
            .withIssuedAt(issueAt)
            .withExpiresAt(expresAt)
            .withJWTId(user.getUid())
            .withClaim("email", user.getEmail())
            .withClaim("username", user.getUsername())
            .withClaim(LOGIN_TYPE, loginType == null ? null : loginType.name())
            .withClaim(RdpUserService.ACCESSKEY, user.getAccessKey())
            .sign(algorithm());
    }

    @Override
    public String genOpPwdToken(DmAuthUserDO user) {
        // token expire time
        long nowMills = System.currentTimeMillis();
        Date issueAt = new Date(nowMills);
        Date expresAt = new Date(nowMills + OP_PASSWD_TOEKN_EXPIRE_MS);
        return JWT.create().withIssuer(issuer).withIssuedAt(issueAt).withExpiresAt(expresAt).withJWTId(user.getUid()).sign(algorithm());
    }

    @Override
    public String genMfaActionToken(String uid, MfaPreActionType actionType, String jwtToken) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusSeconds(MFA_TOKEN_EXPIRE_SEC);
        return JWT.create()
            .withIssuer(issuer)
            .withIssuedAt(now.atZone(ZoneId.systemDefault()).toInstant())
            .withExpiresAt(expireTime.atZone(ZoneId.systemDefault()).toInstant())
            .withJWTId(uid)
            .withClaim(LoginMFAService.MFA_PRE_ACTION_TYPE, actionType.name())
            .withClaim(LoginMFAService.MFA_LOGIN_JWT_TOKEN, jwtToken)
            .sign(algorithm());
    }
}

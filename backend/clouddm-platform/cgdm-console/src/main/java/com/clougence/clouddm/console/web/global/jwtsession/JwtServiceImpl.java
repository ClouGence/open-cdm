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

import static com.clougence.clouddm.console.web.service.auth.RdpUserService.OP_PASSWD_TOEKN_EXPIRE_MS;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final String issuer = "CloudDM";
    /** default use hmacsha256 */
    @Value("${jwt.secret}")
    private String       secret;
    private Algorithm    algorithm;

    public Algorithm algorithm() {
        if (this.algorithm == null) {
            this.algorithm = Algorithm.HMAC256(this.secret);
        }
        return this.algorithm;

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

    private DecodedJWT verifyJwtToken(String jwtToken) {
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
    public String genOpPwdToken(DmAuthUserDO user) {
        // token expire time
        long nowMills = System.currentTimeMillis();
        Date issueAt = new Date(nowMills);
        Date expresAt = new Date(nowMills + OP_PASSWD_TOEKN_EXPIRE_MS);
        return JWT.create().withIssuer(issuer).withIssuedAt(issueAt).withExpiresAt(expresAt).withJWTId(user.getUid()).sign(algorithm());
    }

}

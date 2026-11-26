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
package com.clougence.clouddm.console.web.service.openapi;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.console.web.global.session.SessionTokens;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class OpenApiRequestAuthenticator {

    private static final String   SIGNATURE_METHOD      = "HmacSHA256";
    private static final Duration SIGNATURE_WINDOW      = Duration.ofMinutes(5);
    private static final int      MAX_ACCESS_KEY_LENGTH = 128;
    private static final int      MAX_NONCE_LENGTH      = 128;

    @Resource
    private AuthDal               authDal;

    public OpenApiCaller authenticate(HttpServletRequest request, String path, String idempotencyKey, byte[] body) {
        String accessKeyId = request.getHeader("AccessKeyId");
        String nonce = request.getHeader("SignatureNonce");
        String timestamp = request.getHeader("Timestamp");
        String signature = request.getHeader("Signature");
        if (StringUtils.isBlank(accessKeyId) || accessKeyId.length() > MAX_ACCESS_KEY_LENGTH || !SIGNATURE_METHOD.equals(request.getHeader("SignatureMethod"))
            || StringUtils.isBlank(nonce) || nonce.length() > MAX_NONCE_LENGTH || timestamp == null || signature == null) {
            return null;
        }

        Instant signedAt;
        byte[] providedSignature;
        try {
            signedAt = Instant.parse(timestamp);
            providedSignature = Base64.getDecoder().decode(signature);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return null;
        }

        Instant now = Instant.now();
        if (signedAt.isBefore(now.minus(SIGNATURE_WINDOW)) || signedAt.isAfter(now.plus(SIGNATURE_WINDOW))) {
            return null;
        }

        DmAuthUserDO subAccount = authDal.openApiAuthMapper().queryActiveCredential(accessKeyId);
        if (subAccount == null) {
            return null;
        }

        String signingInput = String.join("\n", request.getMethod(), path, Objects.toString(request.getQueryString(), ""), accessKeyId, SIGNATURE_METHOD, timestamp, nonce, Objects
            .toString(idempotencyKey, ""));
        if (!matchesSignature(subAccount.getSecretKey(), signingInput, body, providedSignature) || !consumeNonce(accessKeyId, nonce, signedAt.plus(SIGNATURE_WINDOW))) {
            return null;
        }

        DmAuthUserDO primary = authDal.userMapper().queryById(subAccount.getParentId());
        if (primary == null || primary.isDisable()) {
            return null;
        }
        return new OpenApiCaller(subAccount.getUid(), primary.getUid());
    }

    private boolean matchesSignature(String storedSecretKey, String signingInput, byte[] body, byte[] providedSignature) {
        String secretKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(storedSecretKey);
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(body);
            String canonical = signingInput + "\n" + HexFormat.of().formatHex(digest);
            Mac mac = Mac.getInstance(SIGNATURE_METHOD);
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIGNATURE_METHOD));
            return MessageDigest.isEqual(mac.doFinal(canonical.getBytes(StandardCharsets.UTF_8)), providedSignature);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("OpenAPI signature verification failed.", e);
        }
    }

    private boolean consumeNonce(String accessKeyId, String nonce, Instant expiresAt) {
        try {
            return authDal.openApiAuthMapper().reserveNonce(accessKeyId, SessionTokens.sha256Hex(nonce), expiresAt.toEpochMilli()) == 1;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }
}

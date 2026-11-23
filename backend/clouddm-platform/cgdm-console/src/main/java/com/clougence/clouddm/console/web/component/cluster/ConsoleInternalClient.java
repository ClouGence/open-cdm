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
package com.clougence.clouddm.console.web.component.cluster;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.config.ConsoleClusterProperties;
import com.clougence.clouddm.platform.dal.model.system.DmSysConsoleDO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Component
@Slf4j
public class ConsoleInternalClient implements UnifiedPostConstruct {
    private static final String      HMAC_ALGORITHM   = "HmacSHA256";
    public static final String       HEADER_ORIGIN    = "X-Cdm-Origin";
    public static final String       HEADER_TARGET    = "X-Cdm-Target";
    public static final String       HEADER_TIME      = "X-Cdm-Time";
    public static final String       HEADER_NONCE     = "X-Cdm-Nonce";
    public static final String       HEADER_DEADLINE  = "X-Cdm-Deadline";
    public static final String       HEADER_SIGNATURE = "X-Cdm-Signature";
    private final Map<String, Long>  nonces           = new ConcurrentHashMap<>();
    private final AtomicLong         nextCleanup      = new AtomicLong();
    private Mac                      mac;
    private OkHttpClient             client;
    private boolean                  stopped;
    @Resource
    private ConsoleClusterProperties properties;
    @Resource
    private ConsoleClusterService    cluster;

    @Override
    public synchronized void init() throws GeneralSecurityException {
        if (this.stopped || this.client != null) {
            return;
        }

        if (this.properties.isEnabled()) {
            if (StringUtils.isBlank(this.properties.getSecret())) {
                throw new IllegalArgumentException("console cluster secret is required");
            }

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(this.properties.getSecret().getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            this.mac = mac;
        }

        this.client = new OkHttpClient.Builder().retryOnConnectionFailure(false).followRedirects(false).followSslRedirects(false).build();
    }

    public String post(DmSysConsoleDO target, String path, Object value, long deadline) throws Exception {
        if (!properties.isEnabled() || !cluster.isLeaseValid()) {
            throw new IllegalStateException("console cluster is unavailable");
        }

        long remaining = deadline - System.currentTimeMillis();
        if (remaining <= 0) {
            throw new IllegalStateException("cluster request deadline exceeded");
        }

        String body = JsonUtils.toJson(value);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = UUID.randomUUID().toString();
        String deadlineValue = String.valueOf(deadline);
        String signature = signature(cluster.getInstanceId(), target.getInstanceId(), path, deadlineValue, timestamp, nonce, body);
        String origin = target.getAdvertiseUrl().replaceAll("/$", "");
        Request request = new Request.Builder().url(origin + path)
            .header(HEADER_ORIGIN, cluster.getInstanceId())
            .header(HEADER_TARGET, target.getInstanceId())
            .header(HEADER_TIME, timestamp)
            .header(HEADER_NONCE, nonce)
            .header(HEADER_DEADLINE, deadlineValue)
            .header(HEADER_SIGNATURE, signature)
            .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body))
            .build();
        OkHttpClient bounded = client.newBuilder()
            .callTimeout(remaining, TimeUnit.MILLISECONDS)
            .readTimeout(remaining, TimeUnit.MILLISECONDS)
            .connectTimeout(Math.min(remaining, 3000), TimeUnit.MILLISECONDS)
            .writeTimeout(remaining, TimeUnit.MILLISECONDS)
            .build();
        try (Response response = bounded.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IllegalStateException("cluster peer request failed: " + response.code());
            }
            return response.body().string();
        }
    }

    public String verify(HttpServletRequest request, String body) {
        try {
            if (!properties.isEnabled() || !cluster.isLeaseValid() || !"POST".equals(request.getMethod())) {
                throw new IllegalArgumentException("cluster endpoint is unavailable");
            }

            String origin = request.getHeader(HEADER_ORIGIN);
            String target = request.getHeader(HEADER_TARGET);
            String timestamp = request.getHeader(HEADER_TIME);
            String deadline = request.getHeader(HEADER_DEADLINE);
            String nonce = request.getHeader(HEADER_NONCE);
            long now = System.currentTimeMillis();
            if (hasInvalidRequestIdentity(origin, target, timestamp, deadline, nonce, now)) {
                throw new IllegalArgumentException("invalid cluster request identity");
            }

            byte[] actual = Base64.getDecoder().decode(request.getHeader(HEADER_SIGNATURE));
            byte[] expected = Base64.getDecoder()
                .decode(signature(origin, target, request.getRequestURI().substring(request.getContextPath().length()), deadline, timestamp, nonce, body));
            if (!MessageDigest.isEqual(actual, expected)) {
                throw new IllegalArgumentException("invalid cluster signature");
            }

            long cleanAt = nextCleanup.get();
            if (now >= cleanAt && nextCleanup.compareAndSet(cleanAt, now + 10000)) {
                nonces.entrySet().removeIf(entry -> entry.getValue() < now - 60000);
            }

            if (nonces.size() >= 100000 || nonces.putIfAbsent(origin + ":" + nonce, now) != null) {
                throw new IllegalArgumentException("cluster request replay rejected");
            }
            return origin;
        } catch (Exception e) {
            throw new IllegalArgumentException("cluster authentication failed", e);
        }
    }

    private boolean hasInvalidRequestIdentity(String origin, String target, String timestamp, String deadline, String nonce, long now) {
        if (!cluster.getInstanceId().equals(target) || origin == null) {
            return true;
        }

        if (nonce == null || nonce.length() > 64) {
            return true;
        }

        if (Math.abs(now - Long.parseLong(timestamp)) > 30000 || Long.parseLong(deadline) <= now) {
            return true;
        }
        return !cluster.isAlive(origin);
    }

    private String signature(String origin, String target, String path, String deadline, String time, String nonce, String body) {
        String value = origin + "\n" + target + "\nPOST\n" + path + "\n" + deadline + "\n" + time + "\n" + nonce + "\n" + body;
        byte[] input = value.getBytes(StandardCharsets.UTF_8);
        byte[] signature;
        synchronized (this.mac) {
            signature = this.mac.doFinal(input);
        }
        return Base64.getEncoder().encodeToString(signature);
    }

    @PreDestroy
    @Override
    public synchronized void stop() {
        this.stopped = true;
        if (this.client != null) {
            this.client.dispatcher().cancelAll();
            this.client.dispatcher().executorService().shutdown();
            this.client.connectionPool().evictAll();
        }
    }
}

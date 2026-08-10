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
package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;

import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.execute.ssl.DsSslSupport;
import com.clougence.utils.StringUtils;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.SslOptions;
import redis.clients.jedis.SslVerifyMode;

final class RedisSslFactory {

    private RedisSslFactory(){
    }

    static void apply(DefaultJedisClientConfig.Builder builder, Map<String, String> dsConfig) {
        SslMode mode = DsSslSupport.sslMode(dsConfig.get(JedisKeys.SSL_MODE));
        if (mode == SslMode.DISABLED) {
            builder.ssl(false);
            return;
        }

        builder.ssl(true);
        try {
            switch (mode) {
                case TRUST -> builder.sslOptions(SslOptions.builder().sslVerifyMode(SslVerifyMode.INSECURE).build());
                case CA -> applyCertificateAuthority(builder, dsConfig);
                case TRUSTSTORE -> builder.sslOptions(trustStoreOptions(dsConfig));
                case KEYSTORE_TRUSTSTORE -> builder.sslOptions(keyStoreTrustStoreOptions(dsConfig));
                case CLIENT_CERT -> applyClientCertificate(builder, dsConfig);
                default -> throw new IllegalArgumentException("unsupported Redis SSL mode: " + mode);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException("invalid Redis SSL " + mode + " configuration: " + e.getMessage(), e);
        }
    }

    private static void applyCertificateAuthority(DefaultJedisClientConfig.Builder builder, Map<String, String> dsConfig) throws GeneralSecurityException, IOException {
        File caFile = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "CA certificate");
        SSLContext sslContext = DsSslSupport.sslContext(null, DsSslSupport.trustManagers(DsSslSupport.readCertificates(caFile)));
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.sslParameters(sslParameters(false));
    }

    private static void applyClientCertificate(DefaultJedisClientConfig.Builder builder, Map<String, String> dsConfig) throws GeneralSecurityException, IOException {
        File caFile = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "CA certificate");
        File clientCertFile = requiredFile(dsConfig, JedisKeys.SSL_CLIENT_CERT_FILE, "client certificate");
        File clientKeyFile = requiredFile(dsConfig, JedisKeys.SSL_CLIENT_KEY_FILE, "client private key");
        char[] keyPassword = DsSslSupport.password(dsConfig.get(JedisKeys.SSL_CLIENT_KEY_PASSWORD));

        X509Certificate[] clientCertificates = DsSslSupport.readCertificates(clientCertFile);
        PrivateKey privateKey = DsSslSupport.readPrivateKey(clientKeyFile, keyPassword, clientCertificates[0]);
        DsSslSupport.verifyKeyPair(privateKey, clientCertificates[0]);

        KeyManager[] keyManagers = DsSslSupport.keyManagers(privateKey, clientCertificates, keyPassword);
        TrustManager[] trustManagers = DsSslSupport.trustManagers(DsSslSupport.readCertificates(caFile));
        SSLContext sslContext = DsSslSupport.sslContext(keyManagers, trustManagers);
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.sslParameters(sslParameters(true));
    }

    private static SslOptions trustStoreOptions(Map<String, String> dsConfig) {
        File trustStore = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "TrustStore");
        return SslOptions.builder()
            .trustStoreType(DsSslSupport.keyStoreType(dsConfig.get(JedisKeys.SSL_CA_FORMAT), trustStore, "TrustStore"))
            .truststore(trustStore, DsSslSupport.password(dsConfig.get(JedisKeys.SSL_CA_PASSWORD)))
            .sslVerifyMode(SslVerifyMode.CA)
            .build();
    }

    private static SslOptions keyStoreTrustStoreOptions(Map<String, String> dsConfig) {
        File trustStore = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "TrustStore");
        File keyStore = requiredFile(dsConfig, JedisKeys.SSL_CLIENT_CERT_FILE, "KeyStore");
        return SslOptions.builder()
            .trustStoreType(DsSslSupport.keyStoreType(dsConfig.get(JedisKeys.SSL_CA_FORMAT), trustStore, "TrustStore"))
            .truststore(trustStore, DsSslSupport.password(dsConfig.get(JedisKeys.SSL_CA_PASSWORD)))
            .keyStoreType(DsSslSupport.keyStoreType(dsConfig.get(JedisKeys.SSL_CLIENT_CERT_FORMAT), keyStore, "KeyStore"))
            .keystore(keyStore, DsSslSupport.password(dsConfig.get(JedisKeys.SSL_CLIENT_KEY_PASSWORD)))
            .sslVerifyMode(SslVerifyMode.FULL)
            .build();
    }

    private static SSLParameters sslParameters(boolean verifyHostname) {
        SSLParameters sslParameters = new SSLParameters();
        if (verifyHostname) {
            sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
        }
        return sslParameters;
    }

    private static File requiredFile(Map<String, String> dsConfig, String configKey, String usage) {
        String path = dsConfig.get(configKey);
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("Redis SSL " + usage + " is required");
        }
        File file = new File(path);
        SslOptions.assertFile(usage, file);
        return file;
    }
}

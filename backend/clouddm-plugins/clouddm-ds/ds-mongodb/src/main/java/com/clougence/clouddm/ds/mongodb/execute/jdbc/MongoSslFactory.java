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
package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.execute.ssl.DsSslSupport;
import com.mongodb.MongoClientSettings;

/**
 * Applies SSL/TLS settings to {@link MongoClientSettings} from the datasource config.
 * Mirrors the Redis SSL modes; the shared crypto material building lives in {@link DsSslSupport}.
 */
final class MongoSslFactory {

    private MongoSslFactory(){
    }

    static void apply(MongoClientSettings.Builder builder, Map<String, String> dsConfig) {
        SslMode mode = DsSslSupport.sslMode(dsConfig.get(MongoKeys.SSL_MODE));
        if (mode == SslMode.DISABLED) {
            return;
        }

        SSLContext context = buildContext(mode, dsConfig);
        boolean invalidHostNameAllowed = !verifyHostname(mode);
        builder.applyToSslSettings(ssl -> {
            ssl.enabled(true);
            ssl.context(context);
            ssl.invalidHostNameAllowed(invalidHostNameAllowed);
        });
    }

    private static SSLContext buildContext(SslMode mode, Map<String, String> dsConfig) {
        try {
            switch (mode) {
                case TRUST:
                    return DsSslSupport.sslContext(null, DsSslSupport.trustAllManagers());
                case CA: {
                    File caFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CA_FILE, "CA certificate");
                    TrustManager[] trustManagers = DsSslSupport.trustManagers(DsSslSupport.readCertificates(caFile));
                    return DsSslSupport.sslContext(null, trustManagers);
                }
                case TRUSTSTORE: {
                    File trustStoreFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CA_FILE, "TrustStore");
                    char[] trustStorePassword = DsSslSupport.password(dsConfig.get(MongoKeys.SSL_CA_PASSWORD));
                    String trustStoreType = DsSslSupport.keyStoreType(dsConfig.get(MongoKeys.SSL_CA_FORMAT), trustStoreFile, "TrustStore");
                    KeyStore trustStore = DsSslSupport.loadKeyStore(trustStoreFile, trustStoreType, trustStorePassword);
                    TrustManager[] trustManagers = DsSslSupport.trustManagers(trustStore);
                    return DsSslSupport.sslContext(null, trustManagers);
                }
                case KEYSTORE_TRUSTSTORE: {
                    File trustStoreFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CA_FILE, "TrustStore");
                    char[] trustStorePassword = DsSslSupport.password(dsConfig.get(MongoKeys.SSL_CA_PASSWORD));
                    String trustStoreType = DsSslSupport.keyStoreType(dsConfig.get(MongoKeys.SSL_CA_FORMAT), trustStoreFile, "TrustStore");
                    KeyStore trustStore = DsSslSupport.loadKeyStore(trustStoreFile, trustStoreType, trustStorePassword);
                    File keyStoreFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CLIENT_CERT_FILE, "KeyStore");
                    char[] keyStorePassword = DsSslSupport.password(dsConfig.get(MongoKeys.SSL_CLIENT_KEY_PASSWORD));
                    String keyStoreType = DsSslSupport.keyStoreType(dsConfig.get(MongoKeys.SSL_CLIENT_CERT_FORMAT), keyStoreFile, "KeyStore");
                    KeyStore keyStore = DsSslSupport.loadKeyStore(keyStoreFile, keyStoreType, keyStorePassword);
                    KeyManager[] keyManagers = DsSslSupport.keyManagers(keyStore, keyStorePassword);
                    TrustManager[] trustManagers = DsSslSupport.trustManagers(trustStore);
                    return DsSslSupport.sslContext(keyManagers, trustManagers);
                }
                case CLIENT_CERT: {
                    File caFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CA_FILE, "CA certificate");
                    File clientCertFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CLIENT_CERT_FILE, "client certificate");
                    File clientKeyFile = DsSslSupport.requiredFile(dsConfig, MongoKeys.SSL_CLIENT_KEY_FILE, "client private key");
                    char[] keyPassword = DsSslSupport.password(dsConfig.get(MongoKeys.SSL_CLIENT_KEY_PASSWORD));
                    X509Certificate[] clientCertificates = DsSslSupport.readCertificates(clientCertFile);
                    PrivateKey privateKey = DsSslSupport.readPrivateKey(clientKeyFile, keyPassword, clientCertificates[0]);
                    DsSslSupport.verifyKeyPair(privateKey, clientCertificates[0]);
                    KeyManager[] keyManagers = DsSslSupport.keyManagers(privateKey, clientCertificates, keyPassword);
                    TrustManager[] trustManagers = DsSslSupport.trustManagers(DsSslSupport.readCertificates(caFile));
                    return DsSslSupport.sslContext(keyManagers, trustManagers);
                }
                default:
                    throw new IllegalArgumentException("unsupported MongoDB SSL mode: " + mode);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException("invalid MongoDB SSL " + mode + " configuration: " + e.getMessage(), e);
        }
    }

    // CLIENT_CERT and KEYSTORE_TRUSTSTORE mirror Redis "FULL" / hostname-verifying modes; the others skip hostname checks.
    private static boolean verifyHostname(SslMode mode) {
        return mode == SslMode.CLIENT_CERT || mode == SslMode.KEYSTORE_TRUSTSTORE;
    }
}

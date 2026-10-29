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
package com.clougence.clouddm.ds.es.execute.dsfactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.execute.ssl.DsSslSupport;
import com.clougence.drivers.DsConfigKeys;

final class EsSslFactory {
    private EsSslFactory(){
    }

    static void apply(HttpAsyncClientBuilder builder, Map<String, String> properties) throws IOException, GeneralSecurityException {
        SslMode mode = DsSslSupport.sslMode(properties.get(DsConfigKeys.SSL_MODE.getConfigKey()));
        // HTTPS with DISABLED uses the JVM trust store and the default hostname verifier.
        if (mode == SslMode.DISABLED) {
            return;
        }

        if (mode == SslMode.TRUST) {
            throw new IllegalArgumentException("Trust-all TLS is not supported");
        }

        File ca = DsSslSupport.requiredFile(properties, DsConfigKeys.SSL_CA_FILE.getConfigKey(), "CA / TrustStore");
        TrustManager[] trust;
        if (mode == SslMode.CA || mode == SslMode.CLIENT_CERT) {
            trust = DsSslSupport.trustManagers(DsSslSupport.readCertificates(ca));
        } else {
            String type = DsSslSupport.keyStoreType(properties.get(DataSourceConfig.Fields.sslCaFileFormat), ca, "TrustStore");
            KeyStore store = DsSslSupport.loadKeyStore(ca, type, DsSslSupport.password(properties.get(DataSourceConfig.Fields.sslCaPassword)));
            trust = DsSslSupport.trustManagers(store);
        }

        KeyManager[] keys = null;
        if (mode == SslMode.CLIENT_CERT || mode == SslMode.KEYSTORE_TRUSTSTORE) {
            File cert = DsSslSupport.requiredFile(properties, DsConfigKeys.SSL_CLIENT_CERT_FILE.getConfigKey(), "Client certificate / KeyStore");
            char[] password = DsSslSupport.password(properties.get(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey()));
            if (mode == SslMode.CLIENT_CERT) {
                File key = DsSslSupport.requiredFile(properties, DsConfigKeys.SSL_CLIENT_KEY_FILE.getConfigKey(), "Client private key");
                X509Certificate[] chain = DsSslSupport.readCertificates(cert);
                PrivateKey privateKey = DsSslSupport.readPrivateKey(key, password, chain[0]);
                DsSslSupport.verifyKeyPair(privateKey, chain[0]);
                keys = DsSslSupport.keyManagers(privateKey, chain, password);
            } else {
                String type = DsSslSupport.keyStoreType(properties.get(DataSourceConfig.Fields.sslClientCertFileFormat), cert, "KeyStore");
                keys = DsSslSupport.keyManagers(DsSslSupport.loadKeyStore(cert, type, password), password);
            }
        }
        builder.setSSLContext(DsSslSupport.sslContext(keys, trust));
        // Keep the default hostname verification for every TLS mode.
    }
}

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
package com.clougence.clouddm.ds.hana.execute.dsfactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.hana.i18n.HanaConfigI18nKeys;
import com.clougence.clouddm.dsfamily.execute.ssl.DsSslSupport;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;

final class HanaSslProperties {

    private HanaSslProperties(){
    }

    static void apply(Properties config, Properties props) throws GeneralSecurityException, IOException {
        SslMode mode = DsSslSupport.sslMode(config.getProperty(DsConfigKeys.SSL_MODE.getConfigKey()));
        props.setProperty("encrypt", Boolean.toString(mode != SslMode.DISABLED));
        props.setProperty("validateCertificate", Boolean.toString(mode != SslMode.TRUST));
        // These values are execution-side file metadata until converted to JDBC store types below.
        props.remove("trustStoreType");
        props.remove("keyStoreType");
        props.remove("trustStorePassword");
        String certificateHost = StringUtils.trimToNull(config.getProperty("hostNameInCertificate"));
        props.remove("hostNameInCertificate");
        if (mode == SslMode.DISABLED || mode == SslMode.TRUST) {
            return;
        }

        if (certificateHost == null && Boolean.parseBoolean(config.getProperty(DataSourceConfig.Fields.sshProxyEnabled))) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TLS_HOST_REQUIRED);
        }

        if (certificateHost != null) {
            if (certificateHost.contains("*")) {
                throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TLS_HOST_ERROR);
            }
            props.setProperty("hostNameInCertificate", certificateHost);
        }

        File caFile = requiredFile(config, DsConfigKeys.SSL_CA_FILE);
        if (mode == SslMode.CA || mode == SslMode.CLIENT_CERT) {
            props.setProperty("sslTrustStore", certificatesPem(DsSslSupport.readCertificates(caFile)));
        } else {
            props.setProperty("trustStore", caFile.getAbsolutePath());
            props.setProperty("trustStoreType", DsSslSupport.keyStoreType(config.getProperty("trustStoreType"), caFile, "TrustStore"));
            props.setProperty("trustStorePassword", config.getProperty("trustStorePassword", ""));
        }

        if (mode == SslMode.CLIENT_CERT) {
            X509Certificate[] certificates = DsSslSupport.readCertificates(requiredFile(config, DsConfigKeys.SSL_CLIENT_CERT_FILE));
            PrivateKey privateKey = DsSslSupport.readPrivateKey(requiredFile(config, DsConfigKeys.SSL_CLIENT_KEY_FILE),
                    DsSslSupport.password(config.getProperty(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey())), certificates[0]);
            DsSslSupport.verifyKeyPair(privateKey, certificates[0]);
            String keyPem = "-----BEGIN PRIVATE KEY-----\n" + Base64.getMimeEncoder(64, new byte[] { '\n' }).encodeToString(privateKey.getEncoded())
                + "\n-----END PRIVATE KEY-----\n";
            props.setProperty("sslKeyStore", keyPem + certificatesPem(certificates));
        } else if (mode == SslMode.KEYSTORE_TRUSTSTORE) {
            File keyStore = requiredFile(config, DsConfigKeys.SSL_CLIENT_CERT_FILE);
            props.setProperty("keyStore", keyStore.getAbsolutePath());
            props.setProperty("keyStoreType", DsSslSupport.keyStoreType(config.getProperty("keyStoreType"), keyStore, "KeyStore"));
            props.setProperty("keyStorePassword", config.getProperty(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey(), ""));
        }
    }

    private static File requiredFile(Properties config, DsConfigKeys key) {
        String path = config.getProperty(key.getConfigKey());
        if (StringUtils.isBlank(path)) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TLS_FILE_REQUIRED);
        }
        return new File(path);
    }

    private static String certificatesPem(X509Certificate[] certificates) throws GeneralSecurityException {
        StringBuilder pem = new StringBuilder();
        Base64.Encoder encoder = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII));
        for (X509Certificate certificate : certificates) {
            pem.append("-----BEGIN CERTIFICATE-----\n").append(encoder.encodeToString(certificate.getEncoded())).append("\n-----END CERTIFICATE-----\n");
        }
        return pem.toString();
    }
}

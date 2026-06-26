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
package com.clougence.clouddm.worker.component.resource.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.component.cache.LocalCacheComponent;
import com.clougence.clouddm.sdk.execute.dsconf.SslConfig;
import com.clougence.clouddm.sdk.execute.dsconf.SslFile;
import com.clougence.clouddm.sdk.service.config.ConfigData;

import jakarta.annotation.Resource;

@Service
public class SslConfigManager {

    @Resource
    private FileResourceManager       fileResourceManager;
    @Resource
    private ConfigRService            configRService;
    private final LocalCacheComponent cache = LocalCacheComponent.getInstance();

    public SslConfig fetch(DataSourceConfig dsConfig) throws IOException {
        if (dsConfig == null || dsConfig.getSslMode() == null || dsConfig.getSslMode() == SslMode.DISABLED) {
            return null;
        }

        Map<String, String> sslConfigData = fetchRemoteSslConfigData(dsConfig);
        SslConfig result = new SslConfig();
        result.setMode(dsConfig.getSslMode());
        result.setClientKeyPassword(dsConfig.getSslClientKeyPassword());
        if (dsConfig.getSslMode() == SslMode.CA || dsConfig.getSslMode() == SslMode.CLIENT_CERT) {
            String caData = configData(dsConfig.getSslCaData(), sslConfigData, DataSourceConfig.Fields.sslCaData);
            result.setCaFile(localFile(dsConfig, DataSourceConfig.Fields.sslCaData, caData, "ca-certificate"));
        }
        if (dsConfig.getSslMode() == SslMode.CLIENT_CERT) {
            String clientCertData = configData(dsConfig.getSslClientCertData(), sslConfigData, DataSourceConfig.Fields.sslClientCertData);
            String clientKeyData = configData(dsConfig.getSslClientKeyData(), sslConfigData, DataSourceConfig.Fields.sslClientKeyData);
            result.setClientCertFile(localFile(dsConfig, DataSourceConfig.Fields.sslClientCertData, clientCertData, "client-certificate"));
            result.setClientKeyFile(localFile(dsConfig, DataSourceConfig.Fields.sslClientKeyData, clientKeyData, "client-key"));
        }
        return result;
    }

    private Map<String, String> fetchRemoteSslConfigData(DataSourceConfig dsConfig) {
        List<String> configNames = new ArrayList<>();
        switch (dsConfig.getSslMode()) {
            case CA:
                addRemoteFetchConfigNameIfNecessary(configNames, dsConfig, DataSourceConfig.Fields.sslCaData, dsConfig.getSslCaData());
                break;
            case CLIENT_CERT:
                addRemoteFetchConfigNameIfNecessary(configNames, dsConfig, DataSourceConfig.Fields.sslCaData, dsConfig.getSslCaData());
                addRemoteFetchConfigNameIfNecessary(configNames, dsConfig, DataSourceConfig.Fields.sslClientCertData, dsConfig.getSslClientCertData());
                addRemoteFetchConfigNameIfNecessary(configNames, dsConfig, DataSourceConfig.Fields.sslClientKeyData, dsConfig.getSslClientKeyData());
                break;
            default:
                break;
        }
        if (configNames.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ConfigData> configData = this.configRService.fetchDsConfig(dsConfig.getInstanceId(), configNames);
        Map<String, String> result = new HashMap<>();
        if (configData == null) {
            return result;
        }

        for (ConfigData config : configData) {
            result.put(config.getConfigName(), config.getConfigValue());
        }
        return result;
    }

    private void addRemoteFetchConfigNameIfNecessary(List<String> configNames, DataSourceConfig dsConfig, String configName, String currentData) {
        if (hasText(currentData)) {
            return;
        }
        if (cached(dsConfig, configName)) {
            return;
        }
        configNames.add(configName);
    }

    private SslFile localFile(DataSourceConfig dsConfig, String configName, String data, String fileName) throws IOException {
        if (data == null || data.isBlank()) {
            SslFile cached = cachedFile(dsConfig, configName);
            if (valid(cached)) {
                return cached;
            }
            return null;
        }

        String format = format(data);
        byte[] fileData = decode(data);
        if (fileData.length == 0) {
            throw new IOException("SSL config file data is empty, dsId=" + dsConfig.getInstanceId() + ", configName=" + configName);
        }
        if (DataSourceConfig.Fields.sslCaData.equals(configName)) {
            validateCaCertificate(dsConfig, fileData);
        }

        String dataHash = sha256(fileData);
        SslFile cached = cachedFile(dsConfig, configName, dataHash);
        if (valid(cached)) {
            return cached;
        }

        SslFile file = new SslFile();
        file.setFormat(format);
        file.setLocalPath(this.fileResourceManager.cacheFile(dsConfig, fileName + "-" + dataHash.substring(0, 12) + "." + format, fileData));
        this.cache.cacheAndReturn(cacheKey(dsConfig, configName, dataHash), file);
        this.cache.cacheAndReturn(cacheKey(dsConfig, configName), file);
        return file;
    }

    //

    private String configData(String currentData, Map<String, String> fetchedData, String configName) {
        return hasText(currentData) ? currentData : fetchedData.get(configName);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private boolean cached(DataSourceConfig dsConfig, String configName) {
        return valid(cachedFile(dsConfig, configName));
    }

    private SslFile cachedFile(DataSourceConfig dsConfig, String configName) {
        return (SslFile) this.cache.getObject(cacheKey(dsConfig, configName));
    }

    private String cacheKey(DataSourceConfig dsConfig, String configName) {
        return cacheKey(dsConfig, configName, null);
    }

    private SslFile cachedFile(DataSourceConfig dsConfig, String configName, String dataHash) {
        return (SslFile) this.cache.getObject(cacheKey(dsConfig, configName, dataHash));
    }

    private String cacheKey(DataSourceConfig dsConfig, String configName, String dataHash) {
        String key = dsConfig.getInstanceId() + ":" + configName;
        return dataHash == null ? key : key + ":" + dataHash;
    }

    private boolean valid(SslFile cached) {
        return cached != null && cached.getFile() != null && cached.getFile().exists() && cached.getFile().length() > 0;
    }

    private String format(String data) {
        int index = data.indexOf("://");
        if (index <= 0) {
            return "pem";
        }
        String format = data.substring(0, index).toLowerCase().replaceAll("[^a-z0-9]", "");
        return format.isBlank() ? "pem" : format;
    }

    private byte[] decode(String data) {
        int index = data.indexOf("://");
        String body = index > 0 ? data.substring(index + 3) : data;
        return Base64.getDecoder().decode(body);
    }

    private String sha256(byte[] data) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(data);
            StringBuilder builder = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }

    private void validateCaCertificate(DataSourceConfig dsConfig, byte[] fileData) throws IOException {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(fileData));
            if (certificate.getBasicConstraints() < 0) {
                throw new IOException("SSL CA file is not a CA certificate, dsId=" + dsConfig.getInstanceId() + ", subject=" + certificate.getSubjectX500Principal());
            }
        } catch (CertificateException e) {
            throw new IOException("SSL CA file is not a valid X.509 certificate, dsId=" + dsConfig.getInstanceId(), e);
        }
    }
}

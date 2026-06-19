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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.sdk.execute.dsconf.SslConfig;
import com.clougence.clouddm.sdk.execute.dsconf.SslFile;
import com.clougence.clouddm.sdk.service.config.ConfigData;

import jakarta.annotation.Resource;

@Service
public class SslConfigManager {

    private final Map<String, SslFile> sslFileCache = new ConcurrentHashMap<>();

    @Resource
    private FileResourceManager        fileResourceManager;
    @Resource
    private ConfigRService             configRService;

    public SslConfig fetch(DataSourceConfig dsConfig) throws IOException {
        if (dsConfig == null || dsConfig.getSslMode() == null || dsConfig.getSslMode() == SslMode.DISABLED) {
            return null;
        }

        Map<String, String> sslConfigData = fetchSslConfigData(dsConfig, missingConfigNames(dsConfig));
        SslConfig result = new SslConfig();
        result.setMode(dsConfig.getSslMode());
        result.setClientKeyPassword(dsConfig.getSslClientKeyPassword());
        if (dsConfig.getSslMode() == SslMode.CA || dsConfig.getSslMode() == SslMode.CLIENT_CERT) {
            result.setCaFile(localFile(dsConfig, DataSourceConfig.Fields.sslCaData, sslConfigData.get(DataSourceConfig.Fields.sslCaData), "ca-certificate"));
        }
        if (dsConfig.getSslMode() == SslMode.CLIENT_CERT) {
            result.setClientCertFile(localFile(dsConfig, DataSourceConfig.Fields.sslClientCertData, sslConfigData
                .get(DataSourceConfig.Fields.sslClientCertData), "client-certificate"));
            result.setClientKeyFile(localFile(dsConfig, DataSourceConfig.Fields.sslClientKeyData, sslConfigData.get(DataSourceConfig.Fields.sslClientKeyData), "client-key"));
        }
        return result;
    }

    private List<String> missingConfigNames(DataSourceConfig dsConfig) {
        List<String> result = new ArrayList<>();
        if ((dsConfig.getSslMode() == SslMode.CA || dsConfig.getSslMode() == SslMode.CLIENT_CERT) && !cached(dsConfig, DataSourceConfig.Fields.sslCaData)) {
            result.add(DataSourceConfig.Fields.sslCaData);
        }
        if (dsConfig.getSslMode() == SslMode.CLIENT_CERT) {
            if (!cached(dsConfig, DataSourceConfig.Fields.sslClientCertData)) {
                result.add(DataSourceConfig.Fields.sslClientCertData);
            }
            if (!cached(dsConfig, DataSourceConfig.Fields.sslClientKeyData)) {
                result.add(DataSourceConfig.Fields.sslClientKeyData);
            }
        }
        return result;
    }

    private Map<String, String> fetchSslConfigData(DataSourceConfig dsConfig, List<String> configNames) {
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

    private SslFile localFile(DataSourceConfig dsConfig, String configName, String data, String fileName) throws IOException {
        SslFile cached = cachedFile(dsConfig, configName);
        if (cached != null) {
            return cached;
        }
        if (data == null || data.isBlank()) {
            return null;
        }
        SslFile file = new SslFile();
        String format = format(data);
        file.setFormat(format);
        file.setLocalPath(this.fileResourceManager.cacheFile(dsConfig, fileName + "." + format, decode(data)));
        this.sslFileCache.put(cacheKey(dsConfig, configName), file);
        return file;
    }

    private boolean cached(DataSourceConfig dsConfig, String configName) {
        return cachedFile(dsConfig, configName) != null;
    }

    private SslFile cachedFile(DataSourceConfig dsConfig, String configName) {
        return this.sslFileCache.get(cacheKey(dsConfig, configName));
    }

    private String cacheKey(DataSourceConfig dsConfig, String configName) {
        return dsConfig.getInstanceId() + ":" + dsConfig.getConfigVersion() + ":" + configName;
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
}

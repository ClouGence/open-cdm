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
package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.model.FileExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.GoogleDriveExtraConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chunlin create time is 2025/6/13
 */
@Service
@Slf4j
public class GoogleDriveExtraConfGen extends FileExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfig() {
        return new GoogleDriveExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        FileExtraConfig config = (GoogleDriveExtraConfig) newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        if (rdpConsoleConfig.getRdpDsConfigValidateEnable()) {
            validate(dsDO, config);
        }

        return config;
    }

    @Override
    protected void fillEntry(FileExtraConfig config, String key, String val) {
        if (key.equals(FileExtraConfig.Fields.dbsJson)) {
            config.setDbsJson(val);
        } else if (key.equals(FileExtraConfig.Fields.defaultLineSchemaJson)) {
            config.setDefaultLineSchemaJson(val);
        } else if (key.equals(FileExtraConfig.Fields.fileSuffixArray)) {
            config.setFileSuffixArray(val);
        } else if (key.equals(FileExtraConfig.Fields.enableLLMExtraction)) {
            config.setEnableLLMExtraction(Boolean.parseBoolean(val));
        } else if (key.equals(FileExtraConfig.Fields.llmExtractionPrompt)) {
            config.setLlmExtractionPrompt(val);
        } else if (key.equals(GoogleDriveExtraConfig.Fields.projectId)) {
            ((GoogleDriveExtraConfig) config).setProjectId(val);
        } else if (key.equals(GoogleDriveExtraConfig.Fields.privateKeyId)) {
            ((GoogleDriveExtraConfig) config).setPrivateKeyId(val);
        } else if (key.equals(GoogleDriveExtraConfig.Fields.privateKey)) {
            ((GoogleDriveExtraConfig) config).setPrivateKey(val);
        } else if (key.equals(GoogleDriveExtraConfig.Fields.clientEmail)) {
            ((GoogleDriveExtraConfig) config).setClientEmail(val);
        } else if (key.equals(GoogleDriveExtraConfig.Fields.clientId)) {
            ((GoogleDriveExtraConfig) config).setClientId(val);
        }
    }

    @Override
    protected void validate(DmDsDO dsDo, FileExtraConfig extraConfig) {
        GoogleDriveExtraConfig config = (GoogleDriveExtraConfig) extraConfig;
        String defaultFormatJson = config.getDefaultLineSchemaJson();
        if (StringUtils.isBlank(defaultFormatJson)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " defaultFormatJson can not blank");
        }
        if (StringUtils.isBlank(config.getPrivateKeyId())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " privateKeyId can not blank");
        }
        if (StringUtils.isBlank(config.getPrivateKey())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " privateKey can not blank");
        }
        if (StringUtils.isBlank(config.getClientEmail())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " clientEmail can not blank");
        }
        if (StringUtils.isBlank(config.getClientId())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " clientId can not blank");
        }
    }
}

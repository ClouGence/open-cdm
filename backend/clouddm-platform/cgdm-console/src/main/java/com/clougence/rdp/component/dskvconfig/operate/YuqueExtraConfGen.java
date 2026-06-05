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
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.model.FileExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.YuqueExtraConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chunlin create time is 2025/6/13
 */
@Service
@Slf4j
public class YuqueExtraConfGen extends FileExtraConfGen {

    @Resource
    private DmConsoleConfig rdpConsoleConfig;

    @Override
    public DsExtraConfig newDsExtraConfig() {
        return new YuqueExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        YuqueExtraConfig config = (YuqueExtraConfig) newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        if (rdpConsoleConfig.getRdpDsConfigValidateEnable()) {
            validate(dsDO, config);
        }

        config.deserialize();
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        YuqueExtraConfig config = (YuqueExtraConfig) newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        config.deserialize();
        return config;
    }

    @Override
    public void fillEntry(FileExtraConfig config, String key, String val) {
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
        } else if (key.equals(YuqueExtraConfig.Fields.repository)) {
            ((YuqueExtraConfig) config).setRepository(val);
        } else if (key.equals(YuqueExtraConfig.Fields.publicKey)) {
            ((YuqueExtraConfig) config).setPublicKey(val);
        } else if (key.equals(YuqueExtraConfig.Fields.userAgent)) {
            ((YuqueExtraConfig) config).setUserAgent(val);
        } else if (key.equals(YuqueExtraConfig.Fields.cookie)) {
            ((YuqueExtraConfig) config).setCookie(val);
        } else if (key.equals(YuqueExtraConfig.Fields.cookieExpire)) {
            ((YuqueExtraConfig) config).setCookieExpire(Long.parseLong(val));
        }
    }

    public void validate(DmDsDO dsDo, YuqueExtraConfig config) {
        String defaultFormatJson = config.getDefaultLineSchemaJson();
        if (StringUtils.isBlank(defaultFormatJson)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " defaultFormatJson can not blank");
        }

        if (StringUtils.isBlank(config.getDbsJson())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " dbsJson can not blank");
        }

        if (StringUtils.isBlank(config.getPublicKey())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " publicKey can not blank");
        }

        if (StringUtils.isBlank(config.getUserAgent())) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " userAgent can not blank");
        }

        if (config.getCookieExpire() == null || config.getCookieExpire() <= 0) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " cookieExpire can not blank or less than zero");
        }
    }
}

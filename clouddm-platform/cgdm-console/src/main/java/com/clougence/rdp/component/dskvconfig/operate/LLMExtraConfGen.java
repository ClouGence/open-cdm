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
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.LLMExtraConfig;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public abstract class LLMExtraConfGen implements RdpDsExtraConfGen {

    abstract protected String llmEmbeddingModelConfig();

    abstract protected String llmChatModelConfig();

    @Override
    public LLMExtraConfig newDsExtraConfig() {
        LLMExtraConfig config = new LLMExtraConfig();
        config.setLlmEmbedding(llmEmbeddingModelConfig());
        config.setLlmChat(llmChatModelConfig());
        config.setHttpsEnabled(true);
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        LLMExtraConfig config = new LLMExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        LLMExtraConfig config = new LLMExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(LLMExtraConfig config, String key, String val) {
        if (key.equals(LLMExtraConfig.Fields.llmEmbedding)) {
            config.setLlmEmbedding(val);
        } else if (key.equals(LLMExtraConfig.Fields.llmChat)) {
            config.setLlmChat(val);
        } else if (key.equals(LLMExtraConfig.Fields.httpsEnabled)) {
            config.setHttpsEnabled(Boolean.parseBoolean(val));
        }

        config.deserialize();
    }
}

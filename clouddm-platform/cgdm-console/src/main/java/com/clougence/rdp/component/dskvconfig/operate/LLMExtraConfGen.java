package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.LLMExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;

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
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        LLMExtraConfig config = new LLMExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        LLMExtraConfig config = new LLMExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
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

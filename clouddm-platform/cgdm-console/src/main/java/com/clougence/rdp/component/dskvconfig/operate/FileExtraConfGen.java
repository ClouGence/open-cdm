package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.FileExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/2/27 12:22:33
 */
@Service
@Slf4j
public abstract class FileExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    protected RdpConsoleConfig rdpConsoleConfig;

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        FileExtraConfig config = (FileExtraConfig) newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        if (rdpConsoleConfig.getRdpDsConfigValidateEnable()) {
            validate(dsDO, config);
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        FileExtraConfig config = (FileExtraConfig) newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

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
        }
    }

    protected void validate(RdpDataSourceDO dsDo, FileExtraConfig extraConfig) {
        String defaultFormatJson = extraConfig.getDefaultLineSchemaJson();
        if (StringUtils.isBlank(defaultFormatJson)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " defaultFormatJson can not blank");
        }
    }
}

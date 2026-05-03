package com.clougence.clouddm.base.metadata.ds.tools;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.ds.ToolsConfigSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/6 18:52
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ToolsConfigSerializer.class)
public class FakerPluginConfig extends ToolConfig {

    public static final String TOOL_NAME = "FAKER";

    @ConfigDef(name = "keepLog", defaultValue = "false", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONCURRENT_DESCRIPTION, readOnly = false)
    private Boolean            keepLog;

    @Override
    public void deserialize() {
        this.setToolName(TOOL_NAME);
    }
}

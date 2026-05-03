package com.clougence.clouddm.base.metadata.ds;

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
public class ToolConfig implements DeserializeAble {

    /** DataSourceType */
    @ConfigDef(name = "toolName", descKey = ConfigI18nKey.CONFIG_DS_TYPE_DESCRIPTION)
    private String  toolName;

    @ConfigDef(name = "version", descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION)
    private String  version;

    @ConfigDef(name = "onlineMaxConcurrent", defaultValue = "50", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONCURRENT_DESCRIPTION, readOnly = false)
    private Integer onlineMaxConcurrent;

    @ConfigDef(name = "exportMaxConcurrent", defaultValue = "20", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_DESCRIPTION, readOnly = false)
    private Integer exportMaxConcurrent;

    @Override
    public void deserialize() {
    }

    protected String safeStr(String value) {
        return value == null ? "" : value;
    }
}

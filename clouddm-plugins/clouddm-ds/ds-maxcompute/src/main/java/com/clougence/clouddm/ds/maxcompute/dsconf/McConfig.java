package com.clougence.clouddm.ds.maxcompute.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020/11/5 20:29
 */
@Getter
@Setter
@Serialization(provider = McSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class McConfig extends DataSourceConfig {

    @ConfigDef(name = "interactiveMode", descKey = ConfigI18nKey.CONFIG_MC_INTERACTIVE_MODE_DESCRIPTION, readOnly = false)
    private Boolean interactiveMode;

    @ConfigDef(name = "sdkEndpoint", descKey = ConfigI18nKey.CONFIG_MC_SDK_ENDPOINT_DESCRIPTION, readOnly = true)
    private String  sdkEndpoint;

    @ConfigDef(name = "schemaStyle", defaultValue = "false", descKey = ConfigI18nKey.CONFIG_MC_SCHEMA_STYLE_DESCRIPTION, readOnly = true)
    private Boolean schemaStyle;

    public McConfig(){
        setDataSourceType(DataSourceType.MaxCompute);
    }

    @Override
    public void deserialize() {
        super.deserialize();
    }

    public Properties asDriverProperties() {

        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));

        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(getDefaultDataBase()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(getDefaultSchema()));

        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(getSoTimeoutSec())));
        //properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), safeStr(getz()));

        properties.setProperty(DsConfigKeys.ODPS_INTERACTIVE.getConfigKey(), safeStr(StringUtils.toString(getInteractiveMode())));
        properties.setProperty(DsConfigKeys.ODPS_SCHEMA_STYLE.getConfigKey(), safeStr(StringUtils.toString(getSchemaStyle())));
        return properties;
    }
}

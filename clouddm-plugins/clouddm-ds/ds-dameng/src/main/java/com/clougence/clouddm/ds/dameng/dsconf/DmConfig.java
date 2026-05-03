package com.clougence.clouddm.ds.dameng.dsconf;

import java.util.Properties;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/5 20:29
 */
@Getter
@Setter
@Serialization(provider = DmSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DmConfig extends DataSourceConfig {

    private String sslFilePath;

    public DmConfig(){
        setDataSourceType(DataSourceType.Dameng);
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
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(getDefaultSchema()));
        //properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(schema)); // unsupported
        properties.setProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey(), "0");
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), "");
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), "true");
        properties.setProperty(DsConfigKeys.DM_SSL_FILE_PATH.getConfigKey(), safeStr(sslFilePath));
        properties.setProperty(DsConfigKeys.DM_SSL_PASSWORD.getConfigKey(), safeStr(this.getStorePassword()));
        return properties;
    }
}

package com.clougence.clouddm.ds.db2zos.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Serialization(provider = Db2ForZosSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Db2ForZosConfig extends DataSourceConfig {

    @JsonIgnore
    public static final Integer DEFAULT_PORT = 50000;

    @JsonIgnore
    private String              ip;

    @JsonIgnore
    private int                 port;

    public Db2ForZosConfig(){
        setDataSourceType(DataSourceType.Db2);
    }

    @Override
    public void deserialize() {
        super.deserialize();

        if (StringUtils.isNotBlank(getHost())) {
            String[] ipPort = getHost().split(":");
            if (ipPort.length == 2) {
                this.ip = ipPort[0];
                if (StringUtils.isNotBlank(ipPort[1])) {
                    this.port = Integer.parseInt(ipPort[1]);
                } else {
                    this.port = DEFAULT_PORT;
                }
            } else if (ipPort.length == 1) {
                this.ip = ipPort[0];
                this.port = DEFAULT_PORT;
            } else {
                throw new IllegalArgumentException("unsupported DB2 for z/OS host format:" + getHost());
            }
        }
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(this.getDefaultDataBase()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), "true");
        properties.setProperty("db2.jcc.progressiveStreaming", "1");
        properties.setProperty("db2.jcc.override.progressiveStreaming", "1");
        return properties;
    }
}

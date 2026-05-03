package com.clougence.clouddm.ds.clickhouse.dsconf;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class ChConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new ChConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        ChConfig config = (ChConfig) dsConfig;
        // it will be modified in the future
        ConnectType connectType = (ConnectType) dsConfigMap.getRdpDsBean().get(RDP_DS_KEY_CONNECT_TYPE);
        if (connectType == ConnectType.CLICKHOUSE_TCP) {
            config.setDriverVersion("[\"Native JDBC\",\"/2.7.1\"]");
        } else {
            config.setDriverVersion("[\"ClickHouse JDBC\",\"/0.9.8\"]");
        }
        return dsConfig;
    }
}

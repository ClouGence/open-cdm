package com.clougence.clouddm.ds.db2zos.dsconf;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class Db2ForZosConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new Db2ForZosConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        ((Db2ForZosConfig) dsConfig).setDefaultDataBase((String) dsConfigMap.getRdpDsBean().get(RDP_DS_KEY_DB_NAME));
        return dsConfig;
    }
}

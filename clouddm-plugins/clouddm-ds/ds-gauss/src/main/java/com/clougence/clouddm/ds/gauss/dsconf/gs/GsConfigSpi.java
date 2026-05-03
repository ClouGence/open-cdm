package com.clougence.clouddm.ds.gauss.dsconf.gs;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class GsConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new GsConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        ((GsConfig) dsConfig).setDefaultDataBase((String) dsConfigMap.getRdpDsBean().get(RDP_DS_KEY_DB_NAME));
        return dsConfig;
    }
}

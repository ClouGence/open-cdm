package com.clougence.clouddm.ds.hologres.dsconf;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class HgConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new HgConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        // dsConfig
        ((HgConfig) dsConfig).setUserName((String) dsConfigMap.getRdpDsBean().get(ConfigKeys.RDP_DS_KEY_ACCESS_KEY));
        ((HgConfig) dsConfig).setPassword((String) dsConfigMap.getRdpDsBean().get(ConfigKeys.RDP_DS_KEY_SECRET_KEY));
        ((HgConfig) dsConfig).setDefaultDataBase((String) dsConfigMap.getRdpDsBean().get(ConfigKeys.RDP_DS_KEY_DB_NAME));

        return dsConfig;
    }}

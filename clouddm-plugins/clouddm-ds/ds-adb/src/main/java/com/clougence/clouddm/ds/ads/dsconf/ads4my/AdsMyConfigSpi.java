package com.clougence.clouddm.ds.ads.dsconf.ads4my;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class AdsMyConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new AdsMyConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        return dsConfig;
    }
}

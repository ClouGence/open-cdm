package com.clougence.clouddm.ds.gauss.dsconf.gsog;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class GsogConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new GsogConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        return dsConfig;
    }
}

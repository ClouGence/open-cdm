package com.clougence.clouddm.sdk.execute.dsconf;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.Spi;

public interface DsConfigSpi extends Spi {

    DataSourceConfig newConfig(Map<String, String> configMap);

    DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap);
}

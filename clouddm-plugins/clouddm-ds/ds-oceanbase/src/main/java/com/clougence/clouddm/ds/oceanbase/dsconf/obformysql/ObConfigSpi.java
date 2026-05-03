package com.clougence.clouddm.ds.oceanbase.dsconf.obformysql;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class ObConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new ObConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        ((ObConfig) dsConfig).setTenant((String) dsConfigMap.getRdpExtraBean().get(ConfigKeys.RDP_EXTRA_OB_TENANT));
        ((ObConfig) dsConfig).setCluster((String) dsConfigMap.getRdpExtraBean().get(ConfigKeys.RDP_EXTRA_OB_CLUSTER_NAME));
        return dsConfig;
    }
}

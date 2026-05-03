package com.clougence.clouddm.ds.oceanbase.dsconf.obforora;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class ObForOraConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new ObOraConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        ((ObOraConfig) dsConfig).setTenant((String) dsConfigMap.getRdpExtraBean().get(ConfigKeys.RDP_EXTRA_OB_TENANT));
        ((ObOraConfig) dsConfig).setCluster((String) dsConfigMap.getRdpExtraBean().get(ConfigKeys.RDP_EXTRA_OB_CLUSTER_NAME));
        return dsConfig;
    }
}

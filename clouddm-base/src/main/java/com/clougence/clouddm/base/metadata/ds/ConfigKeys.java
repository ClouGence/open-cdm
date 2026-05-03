package com.clougence.clouddm.base.metadata.ds;

/**
 * @author bucketli 2020/11/6 18:52
 */

public interface ConfigKeys {

    // map to com.clougence.rdp.dal.model.RdpDataSourceDO field name
    String RDP_DS_KEY_USERNAME       = "userName";
    String RDP_DS_KEY_PASSWORD       = "password";
    String RDP_DS_KEY_ACCESS_KEY     = "accessKey";
    String RDP_DS_KEY_SECRET_KEY     = "secretKey";
    String RDP_DS_KEY_DB_NAME        = "defaultDbName";
    String RDP_DS_KEY_CONNECT_TYPE   = "connectType";

    // map to com.clougence.clouddm.base.metadata.ds.DataSourceConfig field name
    String DM_DS_KEY_HOST            = "host";
    String DM_DS_KEY_DS_TYPE         = "dataSourceType";
    String DM_DS_KEY_SEC_TYPE        = "securityType";
    String DM_DS_KEY_USERNAME        = "userName";
    String DM_DS_KEY_PASSWORD        = "password";
    String DM_DS_KEY_STORE_PASSWORD  = "storePassword";
    String DM_DS_KEY_VERSION         = "version";
    String DM_DS_KEY_DRIVER_VERSION  = "driverVersion";
    String DM_DS_KEY_CONFIG_VERSION  = "configVersion";

    // map to com.clougence.rdp.component.dskvconfig.model.McExtraConfig field name
    String RDP_EXTRA_MC_SDK_ENDPOINT = "sdkEndpoint";
    String RDP_EXTRA_MC_SCHEMA_STYLE = "schemaStyle";

    // map to com.clougence.rdp.component.dskvconfig.model.ObForOracleExtraConf field name
    //     to com.clougence.rdp.component.dskvconfig.model.OceanBaseExtraConfig field name
    String RDP_EXTRA_OB_TENANT       = "tenant";
    String RDP_EXTRA_OB_CLUSTER_NAME = "clusterName";
}

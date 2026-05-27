/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.base.metadata.ds;

/**
 * @author bucketli 2020/11/6 18:52
 */

public interface ConfigKeys {

    // map to com.clougence.clouddm.platform.dal.model.RdpDataSourceDO field name
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

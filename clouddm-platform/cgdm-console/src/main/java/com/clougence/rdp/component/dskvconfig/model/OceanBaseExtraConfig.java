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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ObIncreMode;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author wanshao create time is 2022/11/29
 **/
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class OceanBaseExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "obIncreMode", defaultValue = "LogProxy", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_INCRE_MODE, readOnly = false, valueAdvance = "LogProxy / Binlog")
    private ObIncreMode obIncreMode;

    @DsConfigDef(name = "obLogProxyHost", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_LOGPROXY_HOST, readOnly = false)
    private String      obLogProxyHost;

    @DsConfigDef(name = "clusterUrl", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_CLUSTER_URL, readOnly = false)
    private String      clusterUrl;

    @DsConfigDef(name = "rpcPortList", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_RPC_PORT, readOnly = false)
    private String      rpcPortList;

    @DsConfigDef(name = "syncAccount", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_SYNC_ACCOUNT, readOnly = false)
    private String      syncAccount;

    @DsConfigDef(name = "syncPwd", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_SYNC_PWD, readOnly = false, isSecret = true)
    private String      syncPwd;

    @DsConfigDef(name = "tenant", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_SUB_TENANT, readOnly = false)
    private String      tenant;

    @DsConfigDef(name = "clusterName", descKey = I18nDsConfigMsgKeys.DS_CONFIG_OCEANBASE_CLUSTER, readOnly = false)
    private String      clusterName;
}

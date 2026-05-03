package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ObIncreMode;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
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

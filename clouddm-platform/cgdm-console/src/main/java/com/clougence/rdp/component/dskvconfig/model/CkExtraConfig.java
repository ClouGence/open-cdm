package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.clougence.rdp.constant.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2022/8/10 09:33:48
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class CkExtraConfig extends CommonDsExtraConfig {

    @DsConfigDef(name = "clusterName", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_CLUSTER_NAME, readOnly = false)
    private String  clusterName;

    @DsConfigDef(name = "multiReplica", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_IS_MULTI_REPLICA, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean multiReplica;

    @DsConfigDef(name = "shardName", defaultValue = "{shard}", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_SHARD_NAME, readOnly = false)
    private String  shardName;

    @DsConfigDef(name = "replicaName", defaultValue = "{replica}", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_REPLICA_NAME, readOnly = false)
    private String  replicaName;
}

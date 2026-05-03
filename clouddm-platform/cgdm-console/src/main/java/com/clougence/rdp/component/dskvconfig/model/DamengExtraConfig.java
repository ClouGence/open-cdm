package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.clougence.rdp.constant.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class DamengExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "isDscNode", defaultValue = "false", valueAdvance = "true/false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DAMENG_IS_DCS_NODE, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean isDscNode;

    @DsConfigDef(name = "dscHosts", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DAMENG_DSC_HOSTS, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String  dscHosts;

    @DsConfigDef(name = "dscSyncLsnTable", defaultValue = "SYS.CC_DSC_SYNC_TABLE", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DAMENG_SYNC_LSN_TABLE, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String  dscSyncLsnTable;
}

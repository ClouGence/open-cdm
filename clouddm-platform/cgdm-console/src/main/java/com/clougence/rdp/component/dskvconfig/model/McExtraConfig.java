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
public class McExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "sdkEndpoint", descKey = I18nDsConfigMsgKeys.DS_CONFIG_MC_SDK_ENDPOINT, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String  sdkEndpoint;

    @DsConfigDef(name = "schemaStyle", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_MC_SCHEMA_STYLE, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private Boolean schemaStyle;

}

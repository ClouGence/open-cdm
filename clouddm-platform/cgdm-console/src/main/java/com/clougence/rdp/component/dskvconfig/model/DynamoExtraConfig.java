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
public class DynamoExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "httpsEnabled", defaultValue = "true", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DYNAMODB_URI_HTTPS_ENABLED, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean httpsEnabled;

}

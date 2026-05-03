package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
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
public class EsExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "httpsEnabled", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ES_HTTPS_ENABLED, readOnly = false, valueAdvance = "true / false", kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean httpsEnabled;
}

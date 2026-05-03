package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author chunlin create time is 2024/11/18
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class PulsarExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "serviceUrl", descKey = I18nDsConfigMsgKeys.DS_CONFIG_PULSAR_SERVICE_URL, readOnly = false, valueAdvance = "e.g.,pulsar://192.168.0.101:6650,192.168.0.102:6650")
    private String serviceUrl;
}

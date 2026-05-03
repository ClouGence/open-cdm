package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author chunlin create time is 2024/12/26
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class GreptimeDBExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "greptimeDBGrpcAddr", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GREPTIMEDB_GRPC_ADDR, readOnly = false, valueAdvance = "e.g.,192.168.0.101:4001,192.168.0.102:4001")
    private String greptimeDBGrpcAddr;
}

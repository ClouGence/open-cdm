package com.clougence.rdp.component.dskvconfig.model;

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
public class TiDBExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "pdHost", valueAdvance = "pd_ip1:port,pd_ip2:port,... e.g.,192.168.0.10:2379,192.168.0.11:2379", descKey = I18nDsConfigMsgKeys.DS_CONFIG_TIDB_PD_HOST, readOnly = false)
    private String pdHost;
}

package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class StarRocksExtraConfig extends CommonDsExtraConfig {

    @DsConfigDef(name = "publicHttpHost", valueAdvance = " [starrocks_fe_host:8030] or [starrocks_be_host:8040]", descKey = I18nDsConfigMsgKeys.DS_CONFIG_STARROCKS_PUBLIC_HTTP_HOST, readOnly = false)
    private String publicHttpHost;

    @DsConfigDef(name = "privateHttpHost", valueAdvance = " [starrocks_fe_host:8030] or [starrocks_be_host:8040]", descKey = I18nDsConfigMsgKeys.DS_CONFIG_STARROCKS_PRIVATE_HTTP_HOST, readOnly = false)
    private String privateHttpHost;

    @DsConfigDef(name = "feHttpAddr", valueAdvance = "[starrocks_fe_host:8030]", descKey = I18nDsConfigMsgKeys.DS_CONFIG_STARROCKS_FE_HTTP_ADDR, readOnly = false)
    private String feHttpAddr;

    @DsConfigDef(name = "beThriftAddr", valueAdvance = "starrocks_be_host1:9060,starrocks_be_host2:9060", descKey = I18nDsConfigMsgKeys.DS_CONFIG_STARROCKS_BE_THRIFT_ADDR, readOnly = false)
    private String beThriftAddr;
}

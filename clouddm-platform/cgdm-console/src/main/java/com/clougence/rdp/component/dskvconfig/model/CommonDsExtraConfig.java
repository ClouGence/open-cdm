package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ProxyMode;
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
public class CommonDsExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "useSSL", defaultValue = "false", valueAdvance = "true/false", descKey = I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_CONNECT_USE_SSL, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean   useSSL;

    @DsConfigDef(name = "proxyMode", defaultValue = "NONE", valueAdvance = "NONE/SSH", descKey = I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_CONNECT_PROXY_MODE, readOnly = false)
    private ProxyMode proxyMode;

    @DsConfigDef(name = "remoteProxyIp", descKey = I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_CONNECT_PROXY_IP, readOnly = false)
    private String    remoteProxyIp;

    @DsConfigDef(name = "remoteProxyPort", defaultValue = "22", descKey = I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_CONNECT_PROXY_PORT, readOnly = false)
    private Integer   remoteProxyPort;

    @DsConfigDef(name = "remoteProxyAccount", descKey = I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_CONNECT_PROXY_ACCOUNT, readOnly = false)
    private String    remoteProxyAccount;

    @DsConfigDef(name = "remoteProxyPwd", descKey = I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_CONNECT_PROXY_PWD, isSecret = true, readOnly = false)
    private String    remoteProxyPwd;
}

package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ProxyMode;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.CommonDsExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.utils.StringUtils;

public abstract class CommonExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        DsExtraConfig config = newDsExtraConfig();
        if (!(config instanceof CommonDsExtraConfig)) {
            throw new IllegalArgumentException("newDsExtraConfig() return value not instanceof CommonDsExtraConfig");
        }

        CommonDsExtraConfig extraConfig = (CommonDsExtraConfig) config;
        for (InitDsKvBaseConfigFO configFO : fos) {
            if (configFO.getConfigName().equals(CommonDsExtraConfig.Fields.useSSL)) {
                extraConfig.setUseSSL(Boolean.TRUE.toString().equals(configFO.getConfigValue()));
            }

            if (configFO.getConfigName().equals(CommonDsExtraConfig.Fields.proxyMode)) {
                if (StringUtils.isNotBlank(configFO.getConfigValue())) {
                    ProxyMode pm = ProxyMode.valueOf(configFO.getConfigValue().trim());
                    extraConfig.setProxyMode(pm);
                }
            }

            if (configFO.getConfigName().equals(CommonDsExtraConfig.Fields.remoteProxyIp)) {
                extraConfig.setRemoteProxyIp(configFO.getConfigValue());
            }

            if (configFO.getConfigName().equals(CommonDsExtraConfig.Fields.remoteProxyPort)) {
                if (StringUtils.isNotBlank(configFO.getConfigValue())) {
                    extraConfig.setRemoteProxyPort(Integer.valueOf(configFO.getConfigValue().trim()));
                }
            }

            if (configFO.getConfigName().equals(CommonDsExtraConfig.Fields.remoteProxyAccount)) {
                extraConfig.setRemoteProxyAccount(configFO.getConfigValue());
            }

            if (configFO.getConfigName().equals(CommonDsExtraConfig.Fields.remoteProxyPwd)) {
                extraConfig.setRemoteProxyPwd(configFO.getConfigValue());
            }
        }
        return extraConfig;
    }

    protected void fillEntry(CommonDsExtraConfig config, String key, String val) {
        if (key.equals(CommonDsExtraConfig.Fields.useSSL) && StringUtils.isNotBlank(val)) {
            config.setUseSSL(Boolean.parseBoolean(val));
        }

        if (key.equals(CommonDsExtraConfig.Fields.proxyMode) && StringUtils.isNotBlank(val)) {
            ProxyMode pm = ProxyMode.valueOf(val.trim());
            config.setProxyMode(pm);
        }

        if (key.equals(CommonDsExtraConfig.Fields.remoteProxyIp) && StringUtils.isNotBlank(val)) {
            config.setRemoteProxyIp(val.trim());
        }

        if (key.equals(CommonDsExtraConfig.Fields.remoteProxyPort) && StringUtils.isNotBlank(val)) {
            config.setRemoteProxyPort(Integer.valueOf(val.trim()));
        }

        if (key.equals(CommonDsExtraConfig.Fields.remoteProxyAccount) && StringUtils.isNotBlank(val)) {
            config.setRemoteProxyAccount(val);
        }

        if (key.equals(CommonDsExtraConfig.Fields.remoteProxyPwd) && StringUtils.isNotBlank(val)) {
            config.setRemoteProxyPwd(val);
        }
    }
}

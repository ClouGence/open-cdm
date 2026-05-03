package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.StarRocksExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SrExtraConfGen extends CommonExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public StarRocksExtraConfig newDsExtraConfig() {
        return new StarRocksExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        StarRocksExtraConfig config = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(dsDO, config);
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        StarRocksExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(StarRocksExtraConfig config, String key, String val) {
        if (key.equals(StarRocksExtraConfig.Fields.publicHttpHost)) {
            config.setPublicHttpHost(val);
        } else if (key.equals(StarRocksExtraConfig.Fields.privateHttpHost)) {
            config.setPrivateHttpHost(val);
        } else if (key.equals(StarRocksExtraConfig.Fields.feHttpAddr)) {
            config.setFeHttpAddr(val);
        } else if (key.equals(StarRocksExtraConfig.Fields.beThriftAddr)) {
            config.setBeThriftAddr(val);
        } else {
            super.fillEntry(config, key, val);
        }
    }

    protected void validate(RdpDataSourceDO dsDO, StarRocksExtraConfig extraConfig) {
        if (StringUtils.isNotBlank(dsDO.getPublicHost())) {
            String publicHttpHost = extraConfig.getPublicHttpHost();
            if (StringUtils.isBlank(publicHttpHost)) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config publicHttpHost can not blank");
            }

            String[] host = publicHttpHost.split(":");
            if (host.length != 2) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config publicHttpHost must be in the ip:port format");
            }
        }

        if (StringUtils.isNotBlank(dsDO.getPrivateHost())) {
            String privateHttpHost = extraConfig.getPrivateHttpHost();
            if (StringUtils.isBlank(privateHttpHost)) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config privateHttpHost can not blank");
            }

            String[] host = privateHttpHost.split(":");
            if (host.length != 2) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config privateHttpHost must be in the ip:port format");
            }
        }
    }
}

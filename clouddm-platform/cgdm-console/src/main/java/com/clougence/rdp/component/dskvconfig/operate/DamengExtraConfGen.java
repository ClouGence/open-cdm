package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.DamengExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DamengExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public DamengExtraConfig newDsExtraConfig() {
        return new DamengExtraConfig();
    }

    @Override
    public DamengExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        DamengExtraConfig extraConfig = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(extraConfig);
        }

        return extraConfig;
    }

    @Override
    public DamengExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        DamengExtraConfig extraConfig = newDsExtraConfig();

        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        return extraConfig;
    }

    protected void fillEntry(DamengExtraConfig config, String key, String val) {
        if (key.equals(DamengExtraConfig.Fields.isDscNode)) {
            config.setIsDscNode(Boolean.parseBoolean(val));
        }

        if (key.equals(DamengExtraConfig.Fields.dscHosts)) {
            config.setDscHosts(val);
        }

        if (key.equals(DamengExtraConfig.Fields.dscSyncLsnTable)) {
            config.setDscSyncLsnTable(val);
        }
    }

    protected void validate(DamengExtraConfig extraConfig) {
        if (extraConfig.getIsDscNode() != null && extraConfig.getIsDscNode()) {
            if (StringUtils.isBlank(extraConfig.getDscHosts())) {
                throw new IllegalArgumentException("Dameng extra config dscHosts can not blank");
            }

            if (StringUtils.isBlank(extraConfig.getDscSyncLsnTable())) {
                throw new IllegalArgumentException("Dameng extra config dscSyncLsnTable can not blank");
            }
        }
    }
}

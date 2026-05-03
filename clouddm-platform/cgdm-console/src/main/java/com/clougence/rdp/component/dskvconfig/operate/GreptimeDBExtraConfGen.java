package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.GreptimeDBExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chunlin create time is 2024/12/26
 */
@Service
@Slf4j
public class GreptimeDBExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public GreptimeDBExtraConfig newDsExtraConfig() {
        return new GreptimeDBExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        GreptimeDBExtraConfig config = new GreptimeDBExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(config);
        }
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        GreptimeDBExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(GreptimeDBExtraConfig config, String key, String val) {
        if (key.equals(GreptimeDBExtraConfig.Fields.greptimeDBGrpcAddr)) {
            config.setGreptimeDBGrpcAddr(val);
        }
    }

    protected void validate(GreptimeDBExtraConfig config) {
        if (StringUtils.isBlank(config.getGreptimeDBGrpcAddr())) {
            throw new IllegalArgumentException("GreptimeDB gRPC address is empty.");
        }
    }
}

package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.ObForOracleExtraConf;
import com.clougence.rdp.component.dskvconfig.model.OceanBaseExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chunlin create time is 2024/8/28
 */
@Service
@Slf4j
public class ObForOracleExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public ObForOracleExtraConf newDsExtraConfig() {
        return new ObForOracleExtraConf();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        ObForOracleExtraConf config = new ObForOracleExtraConf();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        // necessary
        if (StringUtils.isBlank(config.getTenant())) {
            throw new IllegalArgumentException("The parameter tenant can not be blank.");
        }

        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(config);
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        ObForOracleExtraConf config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(ObForOracleExtraConf config, String key, String val) {
        if (key.equals(OceanBaseExtraConfig.Fields.obLogProxyHost)) {
            config.setObLogProxyHost(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.clusterUrl)) {
            config.setClusterUrl(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.rpcPortList)) {
            config.setRpcPortList(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.syncAccount)) {
            config.setSyncAccount(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.syncPwd)) {
            config.setSyncPwd(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.tenant)) {
            config.setTenant(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.clusterName)) {
            config.setClusterName(val);
        }
    }

    protected void validate(ObForOracleExtraConf config) {
        if (StringUtils.isBlank(config.getObLogProxyHost())) {
            throw new IllegalArgumentException("The parameter obLogProxyHost can not be blank.");
        }

        if (StringUtils.isBlank(config.getRpcPortList())) {
            throw new IllegalArgumentException("The parameter rpcPortList can not be blank.");
        }

    }
}

package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ObIncreMode;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.OceanBaseExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2022/11/29
 **/
@Service
@Slf4j
public class ObExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public OceanBaseExtraConfig newDsExtraConfig() {
        return new OceanBaseExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        OceanBaseExtraConfig config = new OceanBaseExtraConfig();
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
        OceanBaseExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(OceanBaseExtraConfig config, String key, String val) {
        if (key.equals(OceanBaseExtraConfig.Fields.obIncreMode) && StringUtils.isNotBlank(val)) {
            config.setObIncreMode(ObIncreMode.valueOf(val));
        } else if (key.equals(OceanBaseExtraConfig.Fields.obLogProxyHost)) {
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

    protected void validate(OceanBaseExtraConfig config) {
        if (config.getObIncreMode() != null && config.getObIncreMode() == ObIncreMode.LogProxy) {
            if (StringUtils.isBlank(config.getObLogProxyHost())) {
                throw new IllegalArgumentException("The parameter obLogProxyHost can not be blank.");
            }

            if (StringUtils.isBlank(config.getRpcPortList())) {
                throw new IllegalArgumentException("The parameter rpcPortList can not be blank.");
            }
        }
    }
}

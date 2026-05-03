package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.TiDBExtraConfig;
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
public class TiDbExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public TiDBExtraConfig newDsExtraConfig() {
        return new TiDBExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        TiDBExtraConfig config = new TiDBExtraConfig();
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
        TiDBExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(TiDBExtraConfig config, String key, String val) {
        if (key.equals(TiDBExtraConfig.Fields.pdHost)) {
            config.setPdHost(val);
        }
    }

    protected void validate(RdpDataSourceDO dsDo, TiDBExtraConfig extraConfig) {
        String pdHost = extraConfig.getPdHost();
        if (StringUtils.isBlank(pdHost)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " datasource extra config pdHost can not blank");
        }

        String[] hosts = StringUtils.split(pdHost, ",");
        for (String host : hosts) {
            if (StringUtils.split(host, ":").length != 2) {
                throw new IllegalArgumentException(dsDo.getDataSourceType() + " datasource extra config pdHost must be in the ip:port,ip:port,... format");
            }
        }
    }
}

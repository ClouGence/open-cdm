package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.DuckDbExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/5/29 16:54
 */
@Service
@Slf4j
public class DuckExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public DuckDbExtraConfig newDsExtraConfig() {
        return new DuckDbExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        DuckDbExtraConfig config = new DuckDbExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        DuckDbExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(DuckDbExtraConfig config, String key, String val) {
        if (key.equals(DuckDbExtraConfig.Fields.motherDuckToken) && StringUtils.isNotBlank(val)) {
            config.setMotherDuckToken(val);
        }
    }
}

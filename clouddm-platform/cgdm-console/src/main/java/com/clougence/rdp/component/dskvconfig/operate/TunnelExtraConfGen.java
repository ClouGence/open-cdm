package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.SchemaLessValueFormat;
import com.clougence.clouddm.base.metadata.rdp.enumeration.TunnelSrcType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.TunnelExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class TunnelExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public TunnelExtraConfig newDsExtraConfig() {
        return new TunnelExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        TunnelExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        TunnelExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(TunnelExtraConfig config, String key, String val) {
        if (key.equals(TunnelExtraConfig.Fields.dbsJson)) {
            config.setDbsJson(val);
        } else if (key.equals(TunnelExtraConfig.Fields.uriPrefix)) {
            config.setUriPrefix(val);
        } else if (key.equals(TunnelExtraConfig.Fields.tunnelSrcType)) {
            if (StringUtils.isNotBlank(val)) {
                config.setTunnelSrcType(TunnelSrcType.valueOf(val));
            }
        } else if (key.equals(TunnelExtraConfig.Fields.valueFormat)) {
            if (StringUtils.isNotBlank(val)) {
                config.setValueFormat(SchemaLessValueFormat.valueOf(val));
            }
        }
    }
}

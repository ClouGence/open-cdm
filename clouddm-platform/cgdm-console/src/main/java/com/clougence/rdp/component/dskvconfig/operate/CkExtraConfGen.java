package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.CkExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class CkExtraConfGen extends CommonExtraConfGen {

    @Override
    public CkExtraConfig newDsExtraConfig() {
        return new CkExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        CkExtraConfig config = new CkExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        CkExtraConfig config = new CkExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(CkExtraConfig config, String key, String val) {
        if (key.equals(CkExtraConfig.Fields.clusterName)) {
            config.setClusterName(val);
        } else if (key.equals(CkExtraConfig.Fields.multiReplica)) {
            if (StringUtils.isNotBlank(val)) {
                config.setMultiReplica(Boolean.parseBoolean(val));
            }
        } else if (key.equals(CkExtraConfig.Fields.shardName)) {
            config.setShardName(val);
        } else if (key.equals(CkExtraConfig.Fields.replicaName)) {
            config.setReplicaName(val);
        } else {
            super.fillEntry(config, key, val);
        }
    }
}

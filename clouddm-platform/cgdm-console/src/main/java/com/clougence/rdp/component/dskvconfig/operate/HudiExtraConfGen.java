package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.HudiFsType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.HudiExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class HudiExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public HudiExtraConfig newDsExtraConfig() {
        return new HudiExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        HudiExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        HudiExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(HudiExtraConfig config, String key, String val) {
        switch (key) {
            case HudiExtraConfig.Fields.defaultFS:
                config.setDefaultFS(val);
                break;
            case HudiExtraConfig.Fields.fsDir:
                config.setFsDir(val);
                break;
            case HudiExtraConfig.Fields.fsType: {
                if (StringUtils.isNotBlank(val)) {
                    config.setFsType(HudiFsType.valueOf(val));
                }
                break;
            }
            case HudiExtraConfig.Fields.hdfsPrincipal:
                config.setHdfsPrincipal(val);
                break;
            case HudiExtraConfig.Fields.hadoopUser:
                config.setHadoopUser(val);
                break;
            default:
                break;
        }
    }
}

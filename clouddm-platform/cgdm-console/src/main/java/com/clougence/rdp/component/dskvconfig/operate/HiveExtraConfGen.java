package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.HiveExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

@Service
public class HiveExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public HiveExtraConfig newDsExtraConfig() {
        return new HiveExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> fos) {
        HiveExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        HiveExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        validate(dsDO, config);
        return config;
    }

    protected void fillEntry(HiveExtraConfig config, String key, String val) {
        switch (key) {
            case HiveExtraConfig.Fields.defaultFS:
                config.setDefaultFS(val);
                break;
            case HiveExtraConfig.Fields.fsDir:
                config.setFsDir(val);
                break;
            case HiveExtraConfig.Fields.hdfsPrincipal:
                config.setHdfsPrincipal(val);
                break;
            case HiveExtraConfig.Fields.hadoopUser:
                config.setHadoopUser(val);
                break;
            default:
                break;
        }
    }

    protected void validate(RdpDataSourceDO dsDO, HiveExtraConfig extraConfig) {
        String defaultFS = extraConfig.getDefaultFS();
        if (StringUtils.isBlank(defaultFS)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config defaultFS can not blank");
        }

        String[] host = defaultFS.split(":");
        if (host.length != 2) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config defaultFS must be in the ip:port format");
        }

        if (StringUtils.isBlank(extraConfig.getHadoopUser())) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config fsDir can not blank");
        }

        if (StringUtils.isBlank(extraConfig.getFsDir())) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config hadoopUser can not blank");
        }

    }
}

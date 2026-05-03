package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.MySQLSslMode;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.MySQLExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

@Service
public class MySqlExtraConfGen extends CommonExtraConfGen {

    @Override
    public MySQLExtraConfig newDsExtraConfig() {
        return new MySQLExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        MySQLExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        MySQLExtraConfig config = new MySQLExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        return config;
    }

    protected void fillEntry(MySQLExtraConfig config, String key, String val) {
        if (key.equals(MySQLExtraConfig.Fields.sslMode) && StringUtils.isNotBlank(val)) {
            if (MySQLSslMode.nameOf(val) != null) {
                config.setSslMode(MySQLSslMode.valueOfCode(val).name());
            }
        } else {
            super.fillEntry(config, key, val);
        }
    }
}

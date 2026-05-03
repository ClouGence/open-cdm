package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.OracleExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class OraExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public OracleExtraConfig newDsExtraConfig() {
        return new OracleExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        OracleExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        OracleExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(OracleExtraConfig config, String key, String val) {
        if (key.equals(OracleExtraConfig.Fields.logminerUser)) {
            config.setLogminerUser(val);
        } else if (key.equals(OracleExtraConfig.Fields.logminerPasswd)) {
            config.setLogminerPasswd(val);
        } else if (key.equals(OracleExtraConfig.Fields.logminerConnectType) && StringUtils.isNotBlank(val)) {
            config.setLogminerConnectType(ConnectType.valueOf(val));
        } else if (key.equals(OracleExtraConfig.Fields.logminerSidOrService)) {
            config.setLogminerSidOrService(val);
            //        } else if (key.equals(OracleExtraConfig.Fields.oraLgwrFlushTable)) {
            //            config.setOraLgwrFlushTable(val);
            //        } else if (key.equals(OracleExtraConfig.Fields.racHosts)) {
            //            config.setRacHosts(val);
        } else if (key.equals(OracleExtraConfig.Fields.isDataGuard)) {
            config.setIsDataGuard(Boolean.parseBoolean(val));
        } else if (key.equals(OracleExtraConfig.Fields.dgDicLocation)) {
            config.setDgDicLocation(val);
        }
    }
}

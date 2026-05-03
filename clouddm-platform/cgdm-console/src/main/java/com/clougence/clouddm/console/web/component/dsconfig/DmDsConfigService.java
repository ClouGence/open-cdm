package com.clougence.clouddm.console.web.component.dsconfig;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;

/**
 * @author bucketli 2020/11/7 14:25
 */
public interface DmDsConfigService {

    Map<String, String> fetchSettingsMap(String ownerUid, List<String> names);

    List<RdpDsKvBaseConfigDO> fetchDsConfigDef(DataSourceType dsType);

    DataSourceConfig fetchDsConfigFromTemp(RdpDataSourceDO dsDO, Map<String, String> configMap, HostType hostType);

    DataSourceConfig fetchDsConfigFromRDP(long dsId, DataSourceType dsType, HostType hostType);

    DataSourceConfig fetchDsConfigFromDM(long dsId, DataSourceType dsType);

    String fetchDsConfig(long dsId, String configKey);

    void persistDsConfig(RdpDataSourceDO dsDO, HostType hostType, String version);

    void cleanDsConfig(long dsId);

    DsConfig dsConstantSettings(DataSourceType dsType);

    DsLevels parseLevels(List<String> levels);

    /** levels need is `/ssss/sss` path */
    DsLevels parseLevels(String levels);
}

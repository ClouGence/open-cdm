package com.clougence.rdp.component.dskvconfig;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.controller.model.vo.DsKvConfigVO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;

/**
 * @author bucketli 2020/11/7 14:25
 */
public interface RdpDsConfigService {

    List<DsKvConfigVO> getAllConfig(long dataSourceId);

    void persistDsConfig(RdpDataSourceDO dataSourceDO, List<InitDsKvBaseConfigFO> kvConfigs);

    void persistInnerDsConfig(RdpDataSourceDO dataSourceDO, List<RdpDsKvBaseConfigDO> kvConfigs);

    List<RdpDsKvBaseConfigDO> fetchDefaultConfig(long dataSourceId, DataSourceType dataSourceType);

    DsExtraConfig fetchDsExtraConfig(long dataSourceId, DataSourceType dataSourceType);

    RdpDsKvBaseConfigDO getSpecifiedConfig(long dataSourceId, String configName);

    void cleanDsConfig(long dataSourceId);

}

package com.clougence.clouddm.console.web.component.dsconfig;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.clouddm.console.web.model.fo.datasource.ConnectDsFO;
import com.clougence.clouddm.console.web.model.fo.datasource.EnableDsQueryFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsConfigFO;
import com.clougence.clouddm.console.web.model.vo.DsKvConfigVO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;

/**
 * @author bucketli 2020-01-13 18:08
 * @since 1.1.3
 */
public interface DmDsService {

    Map<DataSourceType, DsConfig> dsConstantSettings();

    List<DmDsConfigDO> fetchDsConfigByIds(String puid, List<Long> ids);

    List<DmDsConfigDO> fetchDsConfigByOwnerUid(String puid);

    DmDsConfigDO fetchDsConfigById(String puid, Long id);

    String testAndFetchDsVersion(String puid, EnableDsQueryFO fo);

    boolean testEnableDsQuery(String puid, long dsId);

    boolean testEnableDsDevOps(String puid, long dsId);

    ResWebData<Boolean> enableDsQuery(String puid, EnableDsQueryFO fo);

    ResWebData<Boolean> disableDsQuery(String puid, long dsId);

    ResWebData<Boolean> enableDsDevOps(String puid, long dsId);

    ResWebData<Boolean> disableDsDevOps(String puid, long dsId);

    ResWebData<Boolean> updateDsDesc(String puid, String uid, long dsId, String desc);

    void updateDsTag(long dsId, String uid, String remark);

    List<RdpDataSourceDO> listDsByClusterId(long clusterId);

    List<DsKvConfigVO> queryDsConfigIncludeNewEntries(Long dsId);

    void upsertDsConfigs(String puid, UpsertDsConfigFO fo);

    RdpDataSourceDO fetchById(Long dsId);

    RdpDataSourceDO queryDs(Long dsId);

    /**
     * Fill ds env info by dsenv id in dataSourceDO
     */
    void fillDsEnvInfo(List<RdpDataSourceDO> dataSourceDOList);

    String testConnect(String uid, ConnectDsFO fo);

    void testConnect(String puid, String uid, DsLevels dsLevels);
}

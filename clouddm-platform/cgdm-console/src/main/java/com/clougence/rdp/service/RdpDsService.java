package com.clougence.rdp.service;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.controller.model.fo.AddDsFO;
import com.clougence.rdp.controller.model.fo.UpdateSecurityInfoFO;
import com.clougence.rdp.controller.model.fo.UpsertDsKvConfigFO;
import com.clougence.rdp.controller.model.lo.UpdateDsConfigLO;
import com.clougence.rdp.controller.model.lo.UpdateDsDescLO;
import com.clougence.rdp.controller.model.lo.UpdatePriHostLO;
import com.clougence.rdp.controller.model.lo.UpdatePubHostLO;
import com.clougence.rdp.controller.model.vo.DefaultDsKvConfigVO;
import com.clougence.rdp.controller.model.vo.DsKvConfigVO;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.queryobj.DsQueryParam;

/**
 * @author bucketli 2023/11/24 10:24:16
 */
public interface RdpDsService {

    ResWebData<Long> addDataSource(String puid, String uid, AddDsFO addFO);

    ResWebData<Long> delDataSource(String puid, long dsId);

    List<DsKvConfigVO> queryDsConfigs(Long dataSourceId);

    DsKvConfigVO queryDsConfig(Long dataSourceId, String configName);

    RdpDataSourceDO queryById(Long dataSourceId);

    List<RdpDataSourceDO> listByIds(List<Long> ids);

    List<RdpDataSourceDO> fetchByCondition(DsQueryParam dsQueryParam);

    List<RdpDataSourceDO> fetchByCondition(String ownerUid, DsQueryParam dsQueryParam, boolean fillEnv);

    RdpDataSourceDO queryDsByIdWithoutPasswd(Long dataSourceId);

    RdpDataSourceDO fetchAndCheckById(Long dataSourceId);

    RdpDataSourceDO fetchByInstanceId(String instanceId);

    List<DefaultDsKvConfigVO> queryDsDefaultConfig(DataSourceType dsType, DeployEnvType envType);

    List<UpdateDsConfigLO> upsertDsConfigs(String puid, UpsertDsKvConfigFO fo);

    UpdateDsDescLO updateDataSourceDesc(String puid, Long dataSourceId, String instanceDesc);

    void updateAliyunRdsAkSk(String puid, Long dataSourceId, String accessKey, String secretKey);

    UpdatePubHostLO updateDataSourcePublicHost(String puid, Long dataSourceId, String publicHost);

    UpdatePriHostLO updateDataSourcePrivateHost(String puid, Long dataSourceId, String privateHost);

    void updateDataSourceAccount(String puid, UpdateSecurityInfoFO fo);

    void cleanDataSourceAccount(String puid, long dsId);

}

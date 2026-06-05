/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.rdp.service;

import java.util.List;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvType;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.AddDsFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsKvConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsConfigLO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsDescLO;
import com.clougence.clouddm.console.web.model.lo.UpdatePriHostLO;
import com.clougence.clouddm.console.web.model.lo.UpdatePubHostLO;
import com.clougence.clouddm.console.web.model.vo.DefaultDsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.RdpDsKvConfigVO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;

/**
 * @author bucketli 2023/11/24 10:24:16
 */
public interface RdpDsService {

    ResWebData<Long> addDataSource(String puid, String uid, AddDsFO addFO);

    ResWebData<Long> delDataSource(String puid, long dsId);

    List<RdpDsKvConfigVO> queryDsConfigs(Long dataSourceId);

    RdpDsKvConfigVO queryDsConfig(Long dataSourceId, String configName);

    DmDsDO queryById(Long dataSourceId);

    List<DmDsDO> listByIds(List<Long> ids);

    List<DmDsDO> fetchByCondition(ArgDsQueryParamObj dsQueryParam);

    List<DmDsDO> fetchByCondition(String ownerUid, ArgDsQueryParamObj dsQueryParam, boolean fillEnv);

    DmDsDO queryDsByIdWithoutPasswd(Long dataSourceId);

    DmDsDO fetchAndCheckById(Long dataSourceId);

    DmDsDO fetchByInstanceId(String instanceId);

    List<DefaultDsKvConfigVO> queryDsDefaultConfig(DataSourceType dsType, DeployEnvType envType);

    List<UpdateDsConfigLO> upsertDsConfigs(String puid, UpsertDsKvConfigFO fo);

    UpdateDsDescLO updateDataSourceDesc(String puid, Long dataSourceId, String instanceDesc);

    UpdatePubHostLO updateDataSourcePublicHost(String puid, Long dataSourceId, String publicHost);

    UpdatePriHostLO updateDataSourcePrivateHost(String puid, Long dataSourceId, String privateHost);

    void updateDataSourceAccount(String puid, UpdateSecurityInfoFO fo);

    void cleanDataSourceAccount(String puid, long dsId);

}

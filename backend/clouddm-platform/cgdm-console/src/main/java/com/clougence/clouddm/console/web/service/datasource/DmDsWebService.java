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
package com.clougence.clouddm.console.web.service.datasource;

import java.util.List;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.DsConfigSubmitFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsKvConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsConfigLO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsDescLO;
import com.clougence.clouddm.console.web.model.vo.RdpDsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.datasource.ConnectDsResultVO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;

/**
 * Web request oriented datasource service.
 */
public interface DmDsWebService {

    ResWebData<Long> addDs(String uid, DsConfigSubmitFO fo);

    ResWebData<Long> updateDs(String uid, DsConfigSubmitFO fo);

    ConnectDsResultVO testConnect(String uid, DsConfigSubmitFO fo);

    ResWebData<Long> delDataSource(String puid, long dsId);

    List<RdpDsKvConfigVO> queryDsConfigs(Long dataSourceId);

    RdpDsKvConfigVO queryDsConfig(Long dataSourceId, String configName);

    DmDsDO queryById(Long dataSourceId);

    List<DmDsDO> listByIds(List<Long> ids);

    List<DmDsDO> fetchByCondition(ArgDsQueryParamObj dsQueryParam);

    List<DmDsDO> fetchByCondition(String ownerUid, ArgDsQueryParamObj dsQueryParam, boolean fillEnv);

    DmDsDO queryDsByIdWithoutPasswd(Long dataSourceId);

    List<DmDsDO> fetchDsConfigByIds(String puid, List<Long> ids);

    List<DmDsDO> listDsByClusterId(long clusterId);

    List<UpdateDsConfigLO> upsertDsConfigs(String puid, UpsertDsKvConfigFO fo);

    UpdateDsDescLO updateDataSourceDesc(String puid, Long dataSourceId, String instanceDesc);

    void updateDataSourceAccount(String puid, UpdateSecurityInfoFO fo);
}

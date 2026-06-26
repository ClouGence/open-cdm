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
package com.clougence.rdp.service.openapi.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsKvConfigFO;
import com.clougence.clouddm.console.web.model.vo.RdpDsKvConfigVO;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.openapi.RdpDsOpenApiService;
import com.clougence.rdp.service.openapi.model.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpDsOpenApiServiceImpl implements RdpDsOpenApiService {

    @Resource
    private DmAuthServiceForBiz authServiceForBiz;
    @Resource
    private DmDsWebService      dsService;
    @Resource
    private ObjectCacheDao      cacheDao;

    @Override
    public List<ApiDataSourceVO> listDs(String requestId, String puid, ApiListDsFO fo) {
        ArgDsQueryParamObj dsQueryParam = ArgDsQueryParamObj.builder()
            .dataSourceType(fo.getType())
            .dataSourceDescLike(fo.getDataSourceDescLike())
            .dataSourceIds(Stream.of(fo.getDataSourceId()).filter(Objects::nonNull).collect(Collectors.toList()))
            .lifeCycleState(fo.getLifeCycleState())
            .dsHostLike(fo.getDsHostLike())
            .dataSourceType(fo.getType())
            .instanceIdLike(fo.getInstanceIdLike())
            .build();

        List<DmAuthResDO> authList = this.authServiceForBiz.listAuthByUser(puid, AuthKind.DataSource);
        if (authList == null || authList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> ids = authList.stream().map(DmAuthResDO::getResId).distinct().collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(dsQueryParam.getDataSourceIds())) {
            dsQueryParam.setDataSourceIds(new ArrayList<>(ids));
        } else {
            if (!ids.containsAll(dsQueryParam.getDataSourceIds())) {
                throw new IllegalArgumentException("Have no auth to query specified DataSource.");
            }
        }

        List<DmDsDO> result = dsService.fetchByCondition(dsQueryParam);
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<>();
        }

        return genFromDsVOs(result);
    }

    protected List<ApiDataSourceVO> genFromDsVOs(List<DmDsDO> dos) {
        List<ApiDataSourceVO> apiVos = new ArrayList<>();
        for (DmDsDO d : dos) {
            ApiDataSourceVO apiVo = new ApiDataSourceVO();
            apiVo.convertFromDsVO(d);
            apiVos.add(apiVo);
        }

        return apiVos;
    }

    @Override
    public ApiDataSourceVO queryDs(String puid, ApiQueryDsFO fo) {
        cacheDao.ownDataSource(puid, fo.getDataSourceId());
        DmDsDO result = dsService.queryDsByIdWithoutPasswd(fo.getDataSourceId());
        ApiDataSourceVO apiVo = new ApiDataSourceVO();
        apiVo.convertFromDsVO(result);

        return apiVo;
    }

    @Override
    public void deleteDs(String puid, ApiDeleteDsFO fo) {
        cacheDao.ownDataSource(puid, fo.getDataSourceId());
        dsService.delDataSource(puid, fo.getDataSourceId());
    }

    @Override
    public void updateDsDesc(String puid, ApiUpdateDsDescFO fo) {
        cacheDao.ownDataSource(puid, fo.getDataSourceId());
        dsService.updateDataSourceDesc(puid, fo.getDataSourceId(), fo.getInstanceDesc());
    }

    @Override
    public void updateAccountAndPasswd(String data, MultipartFile securityFile, MultipartFile secretFile, String puid) {
        if (StringUtils.isBlank(data)) {
            throw new IllegalArgumentException("data can not be empty.");
        }

        UpdateSecurityInfoFO updateFO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            updateFO = objectMapper.readValue(data, new TypeReference<>() {});
        } catch (Exception e) {
            String msg = "deserialize updateFO ds info error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        if (updateFO == null) {
            throw new IllegalArgumentException("datasource data is illegal.");
        }

        cacheDao.ownDataSource(puid, updateFO.getDataSourceId());

        updateFO.setSecurityFile(securityFile);
        updateFO.setSecretFile(secretFile);

        dsService.updateDataSourceAccount(puid, updateFO);
    }

    @Override
    public List<ApiDsKvConfigVo> listDsKvConfs(String puid, ApiListDsKvConfigsByDsIdFO fo) {
        cacheDao.ownDataSource(puid, fo.getDataSourceId());

        List<RdpDsKvConfigVO> confVos;
        if (StringUtils.isNotBlank(fo.getConfigName())) {
            RdpDsKvConfigVO vo = dsService.queryDsConfig(fo.getDataSourceId(), fo.getConfigName());

            if (vo == null) {
                return new ArrayList<>();
            }

            confVos = Collections.singletonList(vo);
        } else {
            confVos = dsService.queryDsConfigs(fo.getDataSourceId());
        }

        List<ApiDsKvConfigVo> apiConfVos = new ArrayList<>();
        for (RdpDsKvConfigVO v : confVos) {
            ApiDsKvConfigVo c = new ApiDsKvConfigVo();
            c.convertFromDsKvConfigVO(v);
            apiConfVos.add(c);
        }

        return apiConfVos;
    }

    @Override
    public void upsertDsKvConfs(String puid, ApiUpsertDsKvConfigFO fo) {
        if (fo.getUpdateConfigs() == null && fo.getNeedCreateConfigs() == null) {
            throw new IllegalArgumentException("update config map and need create config map are both empty.");
        }

        cacheDao.ownDataSource(puid, fo.getDataSourceId());

        UpsertDsKvConfigFO c = fo.genUpsertDsKvConfigFo();
        dsService.upsertDsConfigs(puid, c);
    }
}

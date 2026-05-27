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
package com.clougence.clouddm.console.web.component.auth.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.model.vo.RdpAuthObjectVO;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.model.analysis.resource.AuthBrowseObject;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/13 10:50
 */
@Slf4j
@Service
public class DmAuthServiceForManageImpl implements DmAuthServiceForManage {
    @Resource
    private DataSourceDal datasourceDal;
    @Resource
    private DmDsService   dmDsService;

    @Override
    public List<RdpAuthObjectVO> listElements(String puid, String envId, AuthKind authKind) {
        List<AuthBrowseObject> objs;
        if (authKind == AuthKind.DataSource) {
            objs = listDsEles(puid, envId);
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }

        return objs.stream().map(RdpConvertUtils::convertToRdpAuthObjectVO).collect(Collectors.toList());
    }

    private List<AuthBrowseObject> listDsEles(String puid, String envId) {
        List<DmDsDO> dsDOs = this.datasourceDal.dsMapper().listByDsEnvId(Long.parseLong(envId));
        if (dsDOs == null || dsDOs.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> dsIds = dsDOs.stream().map(DmDsDO::getId).collect(Collectors.toList());

        List<DmDsConfigDO> confList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        List<Long> enableQueryDsIds = confList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());

        List<AuthBrowseObject> objs = new ArrayList<>();
        for (DmDsDO dsDO : dsDOs) {
            boolean enable = enableQueryDsIds.contains(dsDO.getId());
            if (!enable) {
                continue;
            }

            AuthBrowseObject obj = new AuthBrowseObject();
            obj.setObjId(dsDO.getId());
            obj.setObjName(dsDO.getInstanceId());
            obj.setObjDesc(dsDO.getInstanceDesc());
            obj.setObjType(AuthElementType.Instance);
            obj.setObjAttr(new HashMap<>());
            obj.getObjAttr().put("dsDeployType", dsDO.getDeployType().name());
            obj.getObjAttr().put("dsType", dsDO.getDataSourceType().name());
            obj.getObjAttr().put("enableQuery", enable);
            obj.setLeaf(true);
            objs.add(obj);
        }

        return objs;
    }
}

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
package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.model.fo.env.UpdateDsEnvFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsEnvLO;
import com.clougence.rdp.constant.I18nRdpLabelKeys;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.RdpDataSourceMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpDsEnvMapper;
import com.clougence.clouddm.console.web.dal.model.RdpDataSourceDO;
import com.clougence.clouddm.console.web.dal.model.RdpDsEnvDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;

/**
 * @author wanshao create time is 2021/1/18
 **/
@Service
public class RdpDsEnvServiceImpl implements RdpDsEnvService {

    @Resource
    private RdpDsEnvMapper      dsEnvMapper;

    @Resource
    private RdpDataSourceMapper dsMapper;

    @Override
    public List<RdpDsEnvDO> listDsEnv(String puid, String uid, String match) {
        return this.dsEnvMapper.listByCondition(puid, match);
    }

    @Override
    public RdpDsEnvDO queryByUserAndId(String puid, String uid, long envID) {
        return this.dsEnvMapper.queryByEnvID(puid, envID);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int initPrimaryUserDefaultEnv(String puid, String uid) {
        RdpDsEnvDO dsEnvDO = new RdpDsEnvDO();
        dsEnvDO.setOwnerUid(puid);
        dsEnvDO.setEnvName(DmI18nUtils.getMessage(I18nRdpLabelKeys.DEFAULT_ENV.name()));
        dsEnvDO.setDescription(DmI18nUtils.getMessage(I18nRdpLabelKeys.DEFAULT_ENV_DESC.name()));

        return this.dsEnvMapper.insert(dsEnvDO);
    }

    @Override
    public void fillDsEnvInfo(List<RdpDataSourceDO> dss) {
        List<Long> dsEnvIds = dss.stream().filter(Objects::nonNull).map(RdpDataSourceDO::getDsEnvId).collect(Collectors.toCollection(ArrayList::new));
        if (dsEnvIds.isEmpty()) {
            return;
        }

        Map<Long, RdpDsEnvDO> dsEnvDOMap = new HashMap<>();
        List<RdpDsEnvDO> dsEnvDOs = this.dsEnvMapper.selectBatchIds(dsEnvIds);
        for (RdpDsEnvDO dsEnvDO : dsEnvDOs) {
            dsEnvDOMap.put(dsEnvDO.getId(), dsEnvDO);
        }

        for (RdpDataSourceDO ds : dss) {
            ds.setDsEnvDO(dsEnvDOMap.get(ds.getDsEnvId()));
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int addEnvDs(String puid, String uid, RdpDsEnvDO dsEnvDO) {
        if (this.dsEnvMapper.queryByEnvName(puid, dsEnvDO.getEnvName()) != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_NAME_IS_EXIST_ERROR.name(), dsEnvDO.getEnvName()));
        }
        dsEnvDO.setOwnerUid(puid);

        return this.dsEnvMapper.insert(dsEnvDO);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int deleteDsEnv(String puid, String uid, Long dsEnvId) {
        List<RdpDataSourceDO> bindDsList = dsMapper.listByDsEnvId(dsEnvId);
        if (CollectionUtils.isNotEmpty(bindDsList)) {
            List<String> instanceIdList = bindDsList.stream().map(RdpDataSourceDO::getInstanceId).collect(Collectors.toList());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_DELETE_HAVE_BIND_ERROR.name(), JsonUtils.toJson(instanceIdList)));
        }

        return this.dsEnvMapper.deleteDsEnv(dsEnvId, puid);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateDsEnvLO updateDsEnv(String puid, String uid, UpdateDsEnvFO envFO) {
        RdpDsEnvDO forOri = this.dsEnvMapper.queryByEnvID(puid, envFO.getDsEnvId());
        RdpDsEnvDO forName = this.dsEnvMapper.queryByEnvName(puid, envFO.getEnvName());

        if (forOri == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_NOT_EXIST_ERROR.name()));
        }

        if (forName != null) {
            if (!Objects.equals(forOri.getId(), forName.getId())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_NAME_IS_EXIST_ERROR.name(), envFO.getEnvName()));
            }
        }

        this.dsEnvMapper.updateDsEnv(envFO.getDsEnvId(), puid, envFO.getEnvName(), envFO.getDescription());

        UpdateDsEnvLO lo = new UpdateDsEnvLO();
        lo.setOldEnvName(forOri.getEnvName());
        lo.setOldDescription(forOri.getDescription());
        lo.setNewEnvName(envFO.getEnvName());
        lo.setNewDescription(envFO.getDescription());
        lo.setDsEnvId(envFO.getDsEnvId());
        return lo;
    }
}

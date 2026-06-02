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
package com.clougence.clouddm.console.web.component.config.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.env.UpdateDsEnvFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsEnvLO;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;

/**
 * @author wanshao create time is 2021/1/18
 **/
@Service
public class RdpDsEnvServiceImpl implements RdpDsEnvService {
    @Resource
    private SystemDal     systemDal;
    @Resource
    private DataSourceDal datasourceDal;

    @Override
    public List<DmSysEnvDO> listDsEnv(String puid, String uid, String match) {
        return this.systemDal.envMapper().listByCondition(puid, match);
    }

    @Override
    public DmSysEnvDO queryByUserAndId(String puid, String uid, long envID) {
        return this.systemDal.envMapper().queryByEnvID(puid, envID);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int initPrimaryUserDefaultEnv(String puid, String uid) {
        DmSysEnvDO dsEnvDO = new DmSysEnvDO();
        dsEnvDO.setOwnerUid(puid);
        dsEnvDO.setEnvName(DmI18nUtils.getMessage(I18nRdpLabelKeys.DEFAULT_ENV.name()));
        dsEnvDO.setDescription(DmI18nUtils.getMessage(I18nRdpLabelKeys.DEFAULT_ENV_DESC.name()));

        return this.systemDal.envMapper().insert(dsEnvDO);
    }

    @Override
    public void fillDsEnvInfo(List<DmDsDO> dss) {
        List<Long> dsEnvIds = dss.stream().filter(Objects::nonNull).map(DmDsDO::getDsEnvId).collect(Collectors.toCollection(ArrayList::new));
        if (dsEnvIds.isEmpty()) {
            return;
        }

        Map<Long, DmSysEnvDO> dsEnvDOMap = new HashMap<>();
        List<DmSysEnvDO> dsEnvDOs = this.systemDal.envMapper().selectBatchIds(dsEnvIds);
        for (DmSysEnvDO dsEnvDO : dsEnvDOs) {
            dsEnvDOMap.put(dsEnvDO.getId(), dsEnvDO);
        }

        for (DmDsDO ds : dss) {
            ds.setDsEnvDO(dsEnvDOMap.get(ds.getDsEnvId()));
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int addEnvDs(String puid, String uid, DmSysEnvDO dsEnvDO) {
        if (this.systemDal.envMapper().queryByEnvName(puid, dsEnvDO.getEnvName()) != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_NAME_IS_EXIST_ERROR.name(), dsEnvDO.getEnvName()));
        }
        dsEnvDO.setOwnerUid(puid);

        return this.systemDal.envMapper().insert(dsEnvDO);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int deleteDsEnv(String puid, String uid, Long dsEnvId) {
        List<DmDsDO> bindDsList = datasourceDal.dsMapper().listByDsEnvId(dsEnvId);
        if (CollectionUtils.isNotEmpty(bindDsList)) {
            List<String> instanceIdList = bindDsList.stream().map(DmDsDO::getInstanceId).collect(Collectors.toList());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_DELETE_HAVE_BIND_ERROR.name(), JsonUtils.toJson(instanceIdList)));
        }

        return this.systemDal.envMapper().deleteDsEnv(dsEnvId, puid);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateDsEnvLO updateDsEnv(String puid, String uid, UpdateDsEnvFO envFO) {
        DmSysEnvDO forOri = this.systemDal.envMapper().queryByEnvID(puid, envFO.getDsEnvId());
        DmSysEnvDO forName = this.systemDal.envMapper().queryByEnvName(puid, envFO.getEnvName());

        if (forOri == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_NOT_EXIST_ERROR.name()));
        }

        if (forName != null) {
            if (!Objects.equals(forOri.getId(), forName.getId())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.ENV_NAME_IS_EXIST_ERROR.name(), envFO.getEnvName()));
            }
        }

        this.systemDal.envMapper().updateDsEnv(envFO.getDsEnvId(), puid, envFO.getEnvName(), envFO.getDescription());

        UpdateDsEnvLO lo = new UpdateDsEnvLO();
        lo.setOldEnvName(forOri.getEnvName());
        lo.setOldDescription(forOri.getDescription());
        lo.setNewEnvName(envFO.getEnvName());
        lo.setNewDescription(envFO.getDescription());
        lo.setDsEnvId(envFO.getDsEnvId());
        return lo;
    }
}

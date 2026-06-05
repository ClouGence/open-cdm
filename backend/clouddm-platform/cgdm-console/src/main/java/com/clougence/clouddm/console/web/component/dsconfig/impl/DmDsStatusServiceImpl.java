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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.execute.MetaRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsStatusService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.ui.exception.ConnectionExceptionType;
import com.clougence.clouddm.sdk.ui.exception.DetermineExceptionSpi;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.schema.umi.struts.UmiTypes;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmDsStatusServiceImpl implements DmDsStatusService {
    @Resource
    private DataSourceDal                       dsDal;
    @Resource
    private List<RdpNotifyService>              notifyServices;
    @Resource
    private MetaRService                        dsMetaRService;

    private final Map<String, DataSourceStatus> map = new ConcurrentHashMap<>();

    public void handleException(String uid, DataSourceConfig dsConfig, Throwable e) {
        DetermineExceptionSpi spi = PluginManager.findDetermineExceptionSpi(dsConfig.getDataSourceType());
        if (spi != null) {
            ConnectionExceptionType errorType = spi.checkExceptionType(e);
            switch (errorType) {
                case ConnectionRefused: {
                    updateDsStatusIfNecessary(uid, dsConfig, DataSourceStatus.ConnectionFailed);
                    throw new ErrorMessageException(DmErrorCode.DS_DISCONNECT_ERROR.code(),
                        DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DISCONNECT_ERROR.name(), dsConfig.getInstanceId()));
                }
                case Authentication: {
                    updateDsStatusIfNecessary(uid, dsConfig, DataSourceStatus.NoAuthentication);
                    throw new ErrorMessageException(DmErrorCode.DS_DISCONNECT_ERROR.code(),
                        DmI18nUtils.getMessage(I18nDmMsgKeys.DS_AUTHENTICATION_ERROR.name(), dsConfig.getInstanceId()));
                }
            }
        }
    }

    private void updateDsStatusIfNecessary(String uid, DataSourceConfig dsConfig, DataSourceStatus newStatus) {
        DataSourceStatus dataSourceStatus = getDataSourceStatus(dsConfig.getInstanceId());
        // avoid frequently rewrite xml
        if (!dataSourceStatus.equals(newStatus)) {
            this.map.put(dsConfig.getInstanceId(), DataSourceStatus.ConnectionFailed);
            DmDsDO ds = dsDal.dsMapper().getByInstanceId(dsConfig.getInstanceId());
            dsDal.configMapper().updateStatusByDataSourceId(ds.getId(), newStatus);
            this.notifyServices.forEach(s -> s.onDsUpdate(ds.getId()));
        }
    }

    @Override
    public void changeStatusIfNecessary(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        String instanceId = dbConfig.getInstanceId();
        DataSourceStatus dataSourceStatus = getDataSourceStatus(instanceId);

        if (dataSourceStatus != DataSourceStatus.Normal) {
            testConnect(sendDTO, dbConfig, levelsParam);
        }
    }

    @Override
    public void resetStatus(String uid, DataSourceConfig dsConfig) {
        if (getDataSourceStatus(dsConfig.getInstanceId()) == DataSourceStatus.Normal) {
            return;
        }

        this.map.put(dsConfig.getInstanceId(), DataSourceStatus.Normal);
        DmDsDO ds = this.dsDal.dsMapper().getByInstanceId(dsConfig.getInstanceId());
        dsDal.configMapper().updateStatusByDataSourceId(ds.getId(), DataSourceStatus.Normal);
        this.notifyServices.forEach(s -> s.onDsUpdate(ds.getId()));
    }

    private void testConnect(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        try {
            this.dsMetaRService.getVersion(sendDTO, dbConfig, levelsParam);
            resetStatus(sendDTO.getUid(), dbConfig);
        } catch (Exception e) {
            handleException(sendDTO.getUid(), dbConfig, e);
            throw e;
        }
    }

    private DataSourceStatus getDataSourceStatus(String instanceId) {
        DataSourceStatus dataSourceStatus = this.map.get(instanceId);
        if (dataSourceStatus == null) {
            synchronized (this) {
                dataSourceStatus = this.map.get(instanceId);
                if (dataSourceStatus == null) {
                    DmDsDO rdpDataSourceDO = dsDal.dsMapper().getByInstanceId(instanceId);
                    DmDsConfigDO dmDsConfigDO = dsDal.configMapper().queryByDataSourceId(rdpDataSourceDO.getId());
                    if (dmDsConfigDO == null) {
                        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
                    }
                    map.put(instanceId, dmDsConfigDO.getStatus());
                    dataSourceStatus = this.map.get(instanceId);
                }
            }
        }
        return dataSourceStatus;
    }

}

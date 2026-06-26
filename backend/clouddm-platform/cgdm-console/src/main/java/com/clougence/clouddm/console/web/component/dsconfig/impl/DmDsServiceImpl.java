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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverRef;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
import com.clougence.clouddm.api.sidecar.session.execute.MetaRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.console.web.component.dsconfig.DmDriverService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.console.web.model.fo.datasource.ConnectDsFO;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsTagDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.ui.exception.ConnectionExceptionType;
import com.clougence.clouddm.sdk.ui.exception.DetermineExceptionSpi;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/11/24 10:24:56
 */
@Service
@Slf4j
public class DmDsServiceImpl implements DmDsService {

    @Resource
    private DataSourceDal                       dsDal;
    @Resource
    private DmDsConfigService                   configService;
    @Resource
    private DsSchemaService                     schemaService;
    @Resource
    private DmDriverService                     driverService;
    @Resource
    private MetaRService                        metaRService;
    @Resource
    private List<RdpNotifyService>              notifyServices;
    private final Map<String, DataSourceStatus> statusCache = new ConcurrentHashMap<>();

    @Override
    public DmDsDO fetchAndCheckById(Long dataSourceId) {
        if (dataSourceId == null || dataSourceId <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_ID_REQUIRED_ERROR.name()));
        }

        DmDsDO re = this.dsDal.dsMapper().selectById(dataSourceId);
        if (re == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_WITH_ID_ERROR.name(), dataSourceId));
        }

        fillExtraConfig(re, null);
        return re;
    }

    @Override
    public DmDsDO fetchByInstanceId(String instanceId) {
        if (StringUtils.isBlank(instanceId)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_INSTANCE_ID_REQUIRED_ERROR.name()));
        }

        DmDsDO re = this.dsDal.dsMapper().getByInstanceId(instanceId);
        if (re == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_WITH_ID_ERROR.name(), instanceId));
        }

        fillExtraConfig(re, null);
        return re;
    }

    private void fillExtraConfig(DmDsDO re, Map<Long, DmSysEnvDO> envMap) {
        if (envMap != null && envMap.containsKey(re.getDsEnvId())) {
            re.setDsEnvDO(envMap.get(re.getDsEnvId()));
        }
    }

    @Override
    public void updateDsTag(long dsId, String uid, String remark) {
        if (StringUtils.isBlank(remark)) {
            this.dsDal.tagMapper().deleteByDsAndUser(dsId, uid);
            return;
        }

        DmDsTagDO dsTagDO = this.dsDal.tagMapper().getByDsAndUser(dsId, uid);
        if (dsTagDO == null) {
            this.dsDal.tagMapper().insertByDsAndUser(dsId, uid, remark);
        } else {
            this.dsDal.tagMapper().updateByDsAndUser(dsId, uid, remark);
        }
    }

    @Override
    public String testConnect(long dsId) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        if (dsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }
        Long clusterId = dsDO.getBindClusterId();
        if (clusterId == null || clusterId <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_BIND_CLUSTER_ID_REQUIRED_ERROR.name()));
        }

        DataSourceConfig dsConfig = this.configService.fetchDsConfigFromExists(dsDO.getId());
        return getVersion(clusterId, dsConfig);
    }

    private String getVersion(long clusterId, DataSourceConfig dsConfig) {
        Map<UmiTypes, Object> levelsParam = new HashMap<>();
        try {
            SessionSpi spi = PluginManager.findSessionSpi(dsConfig.getDataSourceType());
            SessionContextDTO ctxDTO = spi.createSessionContext(dsConfig, Collections.emptyMap());
            levelsParam.put(UmiTypes.Catalog, ctxDTO.getRdbCatalog());
            levelsParam.put(UmiTypes.Schema, ctxDTO.getRdbSchema());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_UNSUPPORTED_ERROR.name(), dsConfig.getDataSourceType().name()));
        }

        try {
            return this.schemaService.realTimeFetchVersion(clusterId, dsConfig, levelsParam);
        } catch (ErrorMessageException e) {
            if (StringUtils.equals(e.getErrorCode(), DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code())) {
                throw e;
            }

            log.error(e.getMessage(), e);
            String msgStr = ExceptionUtils.getRootCauseMessage(e);
            throw new ErrorMessageException(msgStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String msgStr = ExceptionUtils.getRootCauseMessage(e);
            throw new ErrorMessageException(msgStr);
        }
    }

    @Override
    public String testConnect(ConnectDsFO fo) {
        if (fo.getBindClusterId() == null || fo.getBindClusterId() <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_BIND_CLUSTER_ID_REQUIRED_ERROR.name()));
        }
        if (fo.getDataSourceType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TYPE_REQUIRED_ERROR.name()));
        }
        if (fo.getSecurityType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_SECURITY_TYPE_REQUIRED_ERROR.name()));
        }
        if (StringUtils.isBlank(fo.getHost())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_HOST_REQUIRED_ERROR.name()));
        }

        validateDriverReadyBeforeTestConnect(fo.getBindClusterId(), fo.getDriver());
        Map<String, String> configMap;
        if (StringUtils.isBlank(fo.getDsPropsJson())) {
            configMap = new HashMap<>();
        } else {
            configMap = JsonUtils.toMap(fo.getDsPropsJson());
        }

        if (CollectionUtils.isNotEmpty(fo.getDsKvConfigs())) {
            for (InitDsKvBaseConfigFO config : fo.getDsKvConfigs()) {
                if (config == null || StringUtils.isBlank(config.getConfigName()) || config.getConfigValue() == null) {
                    continue;
                }
                configMap.put(config.getConfigName(), config.getConfigValue());
            }
        }

        DmDsDO tempDs = new DmDsDO();
        tempDs.setInstanceId(UUID.randomUUID().toString().replace("-", ""));
        tempDs.setInstanceDesc(StringUtils.isNotBlank(fo.getInstanceDesc()) ? fo.getInstanceDesc() : fo.getDefaultHost());
        tempDs.setDataSourceType(fo.getDataSourceType());
        tempDs.setHost(fo.getHost());
        tempDs.setSecurityType(fo.getSecurityType());
        tempDs.setDriver(fo.getDriver());
        tempDs.setDsEnvId(fo.getEnvId());
        tempDs.setAccessKey(configMap.get(DataSourceConfig.Fields.userName));
        tempDs.setSecretKey(configMap.get(DataSourceConfig.Fields.password));
        tempDs.setVersion(configMap.get(DataSourceConfig.Fields.version));

        DataSourceConfig dsConfig = this.configService.fetchDsConfigFromNotExist(tempDs, configMap);
        return this.testConnect(fo.getBindClusterId(), fo.getDriver(), dsConfig);
    }

    private String testConnect(long clusterId, String driver, DataSourceConfig dsConfig) {
        if (clusterId <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_BIND_CLUSTER_ID_REQUIRED_ERROR.name()));
        }
        if (dsConfig == null || dsConfig.getDataSourceType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TYPE_REQUIRED_ERROR.name()));
        }
        if (StringUtils.isBlank(dsConfig.getHost())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_HOST_REQUIRED_ERROR.name()));
        }

        validateDriverReadyBeforeTestConnect(clusterId, driver);
        return this.getVersion(clusterId, dsConfig);
    }

    private void validateDriverReadyBeforeTestConnect(Long clusterId, String driverSpec) {
        if (clusterId == null || clusterId <= 0 || StringUtils.isBlank(driverSpec)) {
            return;
        }

        DriverRef driverRef;
        try {
            driverRef = DriverUtils.parseDriverRef(driverSpec);
        } catch (IllegalArgumentException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DRIVER_SPEC_INVALID_ERROR.name(), e.getMessage()));
        }

        DriverVersionStatusVO statusVO = this.driverService.checkDriverStatus(clusterId, driverRef.getDriverFamily(), driverRef.getDriverVersion());
        if (statusVO != null && statusVO.isAvailable()) {
            return;
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DRIVER_NOT_READY_ERROR.name(), driverRef.getDriverFamily(), driverRef.getDriverVersion()));
    }

    @Override
    public void handleException(DataSourceConfig dsConfig, Throwable e) {
        DetermineExceptionSpi spi = PluginManager.findDetermineExceptionSpi(dsConfig.getDataSourceType());
        if (spi != null) {
            ConnectionExceptionType errorType = spi.checkExceptionType(e);
            switch (errorType) {
                case ConnectionRefused: {
                    updateDsStatusIfNecessary(dsConfig, DataSourceStatus.ConnectionFailed);
                    throw new ErrorMessageException(DmErrorCode.DS_DISCONNECT_ERROR.code(),
                        DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DISCONNECT_ERROR.name(), dsConfig.getInstanceId()));
                }
                case Authentication: {
                    updateDsStatusIfNecessary(dsConfig, DataSourceStatus.NoAuthentication);
                    throw new ErrorMessageException(DmErrorCode.DS_DISCONNECT_ERROR.code(),
                        DmI18nUtils.getMessage(I18nDmMsgKeys.DS_AUTHENTICATION_ERROR.name(), dsConfig.getInstanceId()));
                }
            }
        }
    }

    private void updateDsStatusIfNecessary(DataSourceConfig dsConfig, DataSourceStatus newStatus) {
        DataSourceStatus dataSourceStatus = fetchDsStatus(dsConfig.getInstanceId());
        if (!dataSourceStatus.equals(newStatus)) {
            this.statusCache.put(dsConfig.getInstanceId(), DataSourceStatus.ConnectionFailed);
            DmDsDO ds = dsDal.dsMapper().getByInstanceId(dsConfig.getInstanceId());
            dsDal.dsMapper().updateStatusByDataSourceId(ds.getId(), newStatus);
            this.notifyServices.forEach(s -> s.onDsUpdate(ds.getId()));
        }
    }

    @Override
    public void changeStatusIfNecessary(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        String instanceId = dbConfig.getInstanceId();
        DataSourceStatus dataSourceStatus = fetchDsStatus(instanceId);

        if (dataSourceStatus != DataSourceStatus.Normal) {
            try {
                this.metaRService.getVersion(sendDTO, dbConfig, levelsParam);
                resetStatus(dbConfig);
            } catch (Exception e) {
                handleException(dbConfig, e);
                throw e;
            }
        }
    }

    @Override
    public void resetStatus(DataSourceConfig dsConfig) {
        if (fetchDsStatus(dsConfig.getInstanceId()) == DataSourceStatus.Normal) {
            return;
        }

        this.statusCache.put(dsConfig.getInstanceId(), DataSourceStatus.Normal);
        DmDsDO ds = this.dsDal.dsMapper().getByInstanceId(dsConfig.getInstanceId());
        dsDal.dsMapper().updateStatusByDataSourceId(ds.getId(), DataSourceStatus.Normal);
        this.notifyServices.forEach(s -> s.onDsUpdate(ds.getId()));
    }

    private DataSourceStatus fetchDsStatus(String instanceId) {
        DataSourceStatus dataSourceStatus = this.statusCache.get(instanceId);
        if (dataSourceStatus == null) {
            synchronized (this) {
                dataSourceStatus = this.statusCache.get(instanceId);
                if (dataSourceStatus == null) {
                    DmDsDO rdpDataSourceDO = dsDal.dsMapper().getByInstanceId(instanceId);
                    if (rdpDataSourceDO == null || rdpDataSourceDO.getStatus() == null) {
                        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
                    }
                    statusCache.put(instanceId, rdpDataSourceDO.getStatus());
                    dataSourceStatus = this.statusCache.get(instanceId);
                }
            }
        }
        return dataSourceStatus;
    }
}

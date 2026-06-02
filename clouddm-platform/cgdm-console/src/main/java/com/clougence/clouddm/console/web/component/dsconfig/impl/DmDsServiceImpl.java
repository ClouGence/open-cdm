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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverRef;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsUsageEndpoint;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDriverService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsStatusService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.console.web.model.fo.datasource.ConnectDsFO;
import com.clougence.clouddm.console.web.model.fo.datasource.EnableDsQueryFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsConfigFO;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.console.web.model.vo.DsKvConfigVO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.datasource.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpDsUsageService;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2019-12-29 10:53
 * @since 1.1.3
 */
@Slf4j
@Service
public class DmDsServiceImpl implements DmDsService {
    @Resource
    private SystemDal              systemDal;
    @Resource
    private DataSourceDal          dsDal;
    @Resource
    private DmDsConfigService      dmDsConfigService;
    @Resource
    private ObjectCacheDao         dmOwnerCacheService;
    @Resource
    private RdpDsUsageService      rdpDsUsageService;
    @Resource
    private RdpDsService           rdpDsService;
    @Resource
    private DsSchemaService        dsSchemaService;
    @Resource
    private DmDriverService        dmDriverService;
    @Resource
    private DmDsStatusService      dmDsStatusService;
    @Resource
    private List<RdpNotifyService> notifyServices;

    @Override
    public Map<DataSourceType, DsConfig> dsConstantSettings() {
        Map<DataSourceType, DsConfig> data = new HashMap<>();
        for (DataSourceType dsType : DataSourceType.values()) {
            DsConfig dsConfig = this.dmDsConfigService.dsConstantSettings(dsType);
            if (dsConfig != null) {
                data.put(dsType, dsConfig.clone());
            }
        }
        return data;
    }

    @Override
    public List<DmDsConfigDO> fetchDsConfigByIds(String ownerUid, List<Long> ids) {
        return this.dsDal.configMapper().queryByIds(ownerUid, ids);
    }

    @Override
    public List<DmDsConfigDO> fetchDsConfigByOwnerUid(String ownerUid) {
        return this.dsDal.configMapper().queryByUid(ownerUid);
    }

    @Override
    public DmDsConfigDO fetchDsConfigById(String ownerUid, Long id) {
        return this.dsDal.configMapper().queryById(ownerUid, id);
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
    public ResWebData<Boolean> updateDsDesc(String puid, String uid, long dsId, String desc) {
        DmDsDO dsDO = this.rdpDsService.queryById(dsId);
        if (dsDO == null || StringUtils.isBlank(desc) || StringUtils.equals(dsDO.getInstanceDesc(), desc)) {
            return ResWebDataUtils.buildSuccess(true);
        }

        this.dsDal.dsMapper().updateDescByInstanceId(dsId, desc);
        return ResWebDataUtils.buildSuccess(true);
    }

    @Override
    public String testAndFetchDsVersion(String puid, EnableDsQueryFO fo) {
        DmDsDO dsDO = this.rdpDsService.queryById(fo.getDataSourceId());
        if (dsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromRDP(dsDO.getId(), dsDO.getDataSourceType(), fo.getHostType());
        return getVersion(puid, fo.getClusterId(), dsConfig);
    }

    private String getVersion(String puid, long clusterId, DataSourceConfig dsConfig) {
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
            return this.dsSchemaService.realTimeFetchVersion(puid, clusterId, dsConfig, levelsParam);
        } catch (ErrorMessageException e) {
            if (StringUtils.equals(e.getErrorCode(), DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code())) {
                throw e;
            }

            log.error(e.getMessage(), e);
            String instId = dsConfig.getInstanceId();
            String msgStr = ExceptionUtils.getRootCauseMessage(e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_CONNECT_ERROR.name(), dsConfig.getDataSourceType().name(), instId, msgStr));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String instId = dsConfig.getInstanceId();
            String msgStr = ExceptionUtils.getRootCauseMessage(e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_CONNECT_ERROR.name(), dsConfig.getDataSourceType().name(), instId, msgStr));
        }
    }

    @Override
    public boolean testEnableDsQuery(String puid, long dsId) {
        DmDsConfigDO configDO = this.dsDal.configMapper().queryById(puid, dsId);
        return configDO != null;
    }

    @Override
    public boolean testEnableDsDevOps(String puid, long dsId) {
        DmDsConfigDO configDO = this.dsDal.configMapper().queryById(puid, dsId);
        return configDO != null && configDO.isEnableDevops();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public ResWebData<Boolean> enableDsQuery(String puid, EnableDsQueryFO fo) {
        DmDsDO dsDO = this.rdpDsService.queryById(fo.getDataSourceId());
        if (dsDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }
        DmDsConfigDO configDO = this.dsDal.configMapper().queryById(puid, fo.getDataSourceId());
        if (configDO != null) {
            return ResWebDataUtils.buildSuccess(true);
        }

        // store kv config
        this.enableAndStore(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void enableAndStore(String puid, EnableDsQueryFO fo) {
        // this.dmAuthCodeCheckService.checkAuthDsQueryCount();

        DmDsDO dsDO = this.rdpDsService.queryById(fo.getDataSourceId());
        DmDsConfigDO configDO = this.insertNewDsConfig(dsDO, puid, fo);
        DmDsUsageDO usageDO = new DmDsUsageDO();
        usageDO.setDsId(dsDO.getId());
        usageDO.setResId(configDO.getId());
        usageDO.setResType(ResourceType.QUERY);
        usageDO.setResInstanceId(configDO.getConfigInstanceId());
        usageDO.setEndpoint(DsUsageEndpoint.NONE);
        this.rdpDsUsageService.addDsUsages(Collections.singletonList(usageDO));
        this.dmOwnerCacheService.removeDataSourceCache(configDO.getDataSourceId());
        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));

        if (StringUtils.isBlank(dsDO.getVersion())) {
            DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromRDP(dsDO.getId(), dsDO.getDataSourceType(), fo.getHostType());
            try {
                String version = this.getVersion(puid, fo.getClusterId(), dsConfig);
                this.dsDal.dsMapper().updateVersionByInstanceId(dsDO.getId(), version);
            } catch (Exception e) {
                this.dsDal.configMapper().updateStatusByDataSourceId(dsDO.getId(), DataSourceStatus.ConnectionFailed);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public DmDsConfigDO insertNewDsConfig(DmDsDO dsDO, String puid, EnableDsQueryFO fo) {
        this.dmDsConfigService.persistDsConfig(dsDO, fo.getHostType(), dsDO.getVersion());

        DmDsConfigDO configDO = new DmDsConfigDO();
        configDO.setConfigInstanceId(UUID.randomUUID().toString().replace("-", ""));
        configDO.setUid(puid);
        configDO.setDataSourceId(dsDO.getId());
        configDO.setDataSourceType(dsDO.getDataSourceType());
        configDO.setStatus(DataSourceStatus.Normal);
        configDO.setStatusMessage("");
        configDO.setBindClusterId(fo.getClusterId());
        configDO.setBindEnvId(dsDO.getDsEnvId());
        configDO.setHostType(fo.getHostType());
        this.dsDal.configMapper().insert(configDO);
        return configDO;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public ResWebData<Boolean> disableDsQuery(String puid, long dsId) {
        DmDsConfigDO dsConfig = this.dsDal.configMapper().queryById(puid, dsId);
        if (dsConfig == null) {
            return ResWebDataUtils.buildSuccess(true);
        }

        this.dmDsConfigService.cleanDsConfig(dsConfig.getDataSourceId());
        this.dsDal.configMapper().deleteByDisable(puid, dsId);

        DmDsUsageDO usageDO = new DmDsUsageDO();
        usageDO.setDsId(dsConfig.getDataSourceId());
        usageDO.setResId(dsConfig.getId());
        usageDO.setResType(ResourceType.QUERY);
        usageDO.setResInstanceId(dsConfig.getConfigInstanceId());
        usageDO.setEndpoint(DsUsageEndpoint.NONE);
        this.rdpDsUsageService.deleteDsUsage(Collections.singletonList(usageDO));
        this.dmOwnerCacheService.removeDataSourceCache(dsConfig.getDataSourceId());
        this.notifyServices.forEach(s -> s.onDsUpdate(dsId));
        return ResWebDataUtils.buildSuccess(true);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public ResWebData<Boolean> enableDsDevOps(String puid, long dsId) {
        DmDsConfigDO dsConfig = this.dsDal.configMapper().queryById(puid, dsId);
        if (dsConfig == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_REQUIRE_ENABLE_QUERY.name()));
        }

        this.dsDal.configMapper().updateDevOps(puid, dsConfig.getDataSourceId(), true);
        this.notifyServices.forEach(s -> s.onDsUpdate(dsId));
        return ResWebDataUtils.buildSuccess(true);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public ResWebData<Boolean> disableDsDevOps(String puid, long dsId) {
        DmDsConfigDO dsConfig = this.dsDal.configMapper().queryById(puid, dsId);
        if (dsConfig == null) {
            return ResWebDataUtils.buildSuccess(true);
        }

        this.dsDal.configMapper().updateDevOps(puid, dsConfig.getDataSourceId(), false);
        this.notifyServices.forEach(s -> s.onDsUpdate(dsId));
        return ResWebDataUtils.buildSuccess(true);
    }

    @Override
    public List<DmDsDO> listDsByClusterId(long clusterId) {
        List<DmDsConfigDO> configs = this.dsDal.configMapper().queryByClusterId(clusterId);
        List<Long> dsIds = configs.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        if (dsIds.isEmpty()) {
            return Collections.emptyList();
        } else {
            return dsDal.dsMapper().listByIds(dsIds);
        }
    }

    @Override
    public DmDsDO fetchById(Long dsId) {
        if (dsId == null || dsId <= 0) {
            throw new RuntimeException("data source id cannot be null.");
        }

        DmDsDO re = this.rdpDsService.queryById(dsId);
        if (re == null) {
            throw new IllegalArgumentException("datasource(" + dsId + ") not exist.");
        }

        return re;
    }

    @Override
    public DmDsDO queryDs(Long dsId) {
        if (dsId == null) {
            return null;
        }
        DmDsDO ds = this.rdpDsService.queryById(dsId);
        if (ds == null) {
            return null;
        }

        DmSysEnvDO dsEnvDO = this.systemDal.envMapper().selectById(ds.getDsEnvId());
        ds.setDsEnvDO(dsEnvDO);
        return ds;
    }

    @Override
    public List<DsKvConfigVO> queryDsConfigIncludeNewEntries(Long dsId) {
        if (dsId == null) {
            return new ArrayList<>();
        }

        DmDsDO ds = this.rdpDsService.queryById(dsId);
        if (ds == null) {
            return new ArrayList<>();
        }

        List<DmDsConfigKv4DmDO> configList = this.dsDal.configKv4DmMapper().listByDsId(ds.getId());
        Map<String, DmDsConfigKv4DmDO> configMap = new HashMap<>();
        for (DmDsConfigKv4DmDO configDO : configList) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DmDsConfigKv4RdpDO> defaultConfigs = this.dmDsConfigService.fetchDsConfigDef(ds.getDataSourceType());

        List<DsKvConfigVO> resultConfigs = new ArrayList<>();
        for (DmDsConfigKv4RdpDO configDO : defaultConfigs) {
            DmDsConfigKv4DmDO config = configMap.get(configDO.getConfigName());
            DsKvConfigVO v;
            if (config == null) {
                v = DmConvertUtils.convertToDsKvConfigVO(configDO);
                v.setNeedCreated(true);
                resultConfigs.add(v);
            } else {
                v = DmConvertUtils.convertToDsKvConfigVO(config);
                v.setConfValType(configDO.getConfValType());
                resultConfigs.add(v);
            }
        }

        return resultConfigs;
    }

    @Override
    public String testConnect(String uid, ConnectDsFO fo) {
        if (fo.getBindClusterId() == null || fo.getBindClusterId() <= 0) {
            throw new IllegalArgumentException("bind cluster id can not be empty.");
        }
        if (fo.getDataSourceType() == null) {
            throw new IllegalArgumentException("data source type can not be empty.");
        }
        if (fo.getDeployEnvType() == null) {
            throw new IllegalArgumentException("deploy env type can not be empty.");
        }
        if (fo.getSecurityType() == null) {
            throw new IllegalArgumentException("security type can not be empty.");
        }

        HostType hostType = resolveHostType(fo);
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
        tempDs.setDeployType(fo.getDeployEnvType());
        tempDs.setHostType(hostType);
        tempDs.setHost(hostType == HostType.PUBLIC ? fo.getPublicHost() : fo.getPrivateHost());
        tempDs.setPrivateHost(fo.getPrivateHost());
        tempDs.setPublicHost(fo.getPublicHost());
        tempDs.setSecurityType(fo.getSecurityType());
        tempDs.setConnectType(fo.getConnectType());
        tempDs.setDriver(fo.getDriver());
        tempDs.setDefaultDbName(configMap.get("database"));
        tempDs.setDsEnvId(fo.getEnvId());
        tempDs.setAccount(configMap.get("userName"));
        tempDs.setPassword(configMap.getOrDefault("password", ""));
        tempDs.setVersion(configMap.get("version"));

        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromTemp(tempDs, configMap, hostType);
        return this.getVersion(uid, fo.getBindClusterId(), dsConfig);
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

        DriverVersionStatusVO statusVO = this.dmDriverService.checkDriverStatus(clusterId, driverRef.getDriverFamily(), driverRef.getDriverVersion());
        if (statusVO != null && statusVO.isAvailable()) {
            return;
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DRIVER_NOT_READY_ERROR.name(), driverRef.getDriverFamily(), driverRef.getDriverVersion()));
    }

    @Override
    public void testConnect(String puid, String uid, DsLevels levels) {
        DmDsDO dsDO = levels.dsDO();
        DataSourceConfig dsConfig = dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
        try {
            this.dsSchemaService.realTimeFetchVersion(uid, dsDO, levels.levelsParam());
            this.dmDsStatusService.resetStatus(uid, dsConfig);
        } catch (Exception e) {
            this.dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    private HostType resolveHostType(ConnectDsFO fo) {
        if (StringUtils.isNotBlank(fo.getPublicHost())) {
            return HostType.PUBLIC;
        }
        if (StringUtils.isNotBlank(fo.getPrivateHost())) {
            return HostType.PRIVATE;
        }
        throw new IllegalArgumentException("private host and public host can not be both blank.");
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

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void upsertDsConfigs(String puid, UpsertDsConfigFO fo) {
        if (CollectionUtils.isEmpty(fo.getUpdateConfigMap()) && CollectionUtils.isEmpty(fo.getNeedCreateConfigMap())) {
            throw new IllegalArgumentException("update config map and need create config map are both empty.");
        }

        DmDsDO rdpDs = this.rdpDsService.queryById(fo.getDataSourceId());
        if (CollectionUtils.isNotEmpty(fo.getUpdateConfigMap())) {
            for (Map.Entry<String, String> config : fo.getUpdateConfigMap().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                if (configDO != null) {
                    String value = config.getValue();
                    if (value != null) {
                        value = value.trim();
                        if (configDO.isSecret()) {
                            value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
                        }
                    }

                    this.dsDal.configKv4DmMapper().updateDsConfig(fo.getDataSourceId(), config.getKey(), value);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(fo.getNeedCreateConfigMap())) {
            List<DmDsConfigKv4RdpDO> defaultConfigs = this.dmDsConfigService.fetchDsConfigDef(rdpDs.getDataSourceType());

            for (Map.Entry<String, String> config : fo.getNeedCreateConfigMap().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                if (configDO == null) {
                    DmDsConfigKv4RdpDO defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(config.getKey())).findFirst().orElse(null);
                    if (defaultConfig != null) {
                        String value = config.getValue();
                        if (value != null) {
                            value = value.trim();
                        }

                        if (defaultConfig.isSecret()) {
                            defaultConfig.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value));
                        } else {
                            defaultConfig.setConfigValue(value);
                        }

                        DmDsConfigKv4DmDO dmKvConf = DmConvertUtils.convertToDmDsKvBaseConfigDOForInsert(defaultConfig);
                        dmKvConf.setDataSourceId(rdpDs.getId());
                        this.dsDal.configKv4DmMapper().insert(dmKvConf);
                    }
                }
            }
        }

        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
    }
}

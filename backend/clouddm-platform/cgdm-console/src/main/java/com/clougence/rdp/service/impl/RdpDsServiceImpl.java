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

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.clouddm.api.common.exception.ConsoleRuntimeException;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.AddDsFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsKvConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsConfigLO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsDescLO;
import com.clougence.clouddm.console.web.model.lo.UpdatePriHostLO;
import com.clougence.clouddm.console.web.model.lo.UpdatePubHostLO;
import com.clougence.clouddm.console.web.model.vo.RdpDsKvConfigVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpDsUsageService;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/11/24 10:24:56
 */
@Service
@Slf4j
public class RdpDsServiceImpl implements RdpDsService, UnifiedPostConstruct {

    @Resource
    private SystemDal              systemDal;
    @Resource
    private DataSourceDal          datasourceDal;
    @Resource
    private AuthDal                authDal;
    @Resource
    private RdpUserService         rdpUserService;
    @Resource
    private DmAuthServiceForManage rdpAuthServiceForManager;
    @Resource
    private RdpDsUsageService      rdpDsUsageService;
    @Resource
    private DmDsConfigService      dmDsConfigService;
    @Resource
    private List<RdpNotifyService> notifyServices;

    @Override
    public void init() {
    }

    @Override
    public void stop() {

    }

    @Override
    public List<DmDsDO> fetchByCondition(ArgDsQueryParamObj dsQueryParam) {
        List<DmDsDO> dsList = this.datasourceDal.dsMapper().listByCondition(dsQueryParam);
        for (DmDsDO ds : dsList) {
            fillExtraConfig(ds, null);
        }
        return dsList;
    }

    @Override
    public List<DmDsDO> fetchByCondition(String ownerUid, ArgDsQueryParamObj dsQueryParam, boolean fillEnv) {
        List<DmDsDO> dsList = this.datasourceDal.dsMapper().listByCondition(dsQueryParam);
        if (CollectionUtils.isEmpty(dsList)) {
            return dsList;
        }
        Map<Long, DmSysEnvDO> envMap = new HashMap<>();
        if (fillEnv) {
            List<Long> envIds = dsList.stream().map(DmDsDO::getDsEnvId).distinct().collect(Collectors.toList());
            List<DmSysEnvDO> envList = this.systemDal.envMapper().queryListByUidAndId(ownerUid, envIds);
            envList.forEach(e -> envMap.put(e.getId(), e));
        }

        for (DmDsDO ds : dsList) {
            fillExtraConfig(ds, envMap);
        }

        return dsList;
    }

    @Override
    public DmDsDO queryDsByIdWithoutPasswd(Long dataSourceId) {
        DmDsDO dataSourceDO = fetchAndCheckById(dataSourceId);
        dataSourceDO.setPassword(null);
        return dataSourceDO;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public List<UpdateDsConfigLO> upsertDsConfigs(String puid, UpsertDsKvConfigFO fo) {
        List<UpdateDsConfigLO> result = new ArrayList<>();

        DmDsDO dataSourceDO = this.fetchAndCheckById(fo.getDataSourceId());
        List<DsConfigKvDef> defaultConfigs = this.dmDsConfigService.fetchDsConfigDef(dataSourceDO.getDataSourceType());

        if (fo.getUpdateConfigs() != null && !fo.getUpdateConfigs().isEmpty()) {
            for (Map.Entry<String, String> config : fo.getUpdateConfigs().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.datasourceDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                DsConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(config.getKey())).findFirst().orElse(null);
                if (configDO != null && defaultConfig != null) {
                    String value = config.getValue();
                    if (value != null) {
                        value = value.trim();
                    }

                    if (defaultConfig.isSecret() && StringUtils.isNotBlank(value)) {
                        value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
                    }

                    if (defaultConfig.isReadOnly()) {
                        continue;
                    }

                    UpdateDsConfigLO configLO = new UpdateDsConfigLO();
                    configLO.setConfigName(configDO.getConfigName());
                    configLO.setNeedCreate(false);
                    if (!defaultConfig.isSecret()) {
                        configLO.setOldConfigValue(configDO.getConfigValue());
                        configLO.setConfigValue(config.getValue());
                    }
                    this.datasourceDal.configKv4DmMapper().updateDsConfig(fo.getDataSourceId(), config.getKey(), value);
                    result.add(configLO);
                }
            }
        }

        if (fo.getNeedCreateConfigs() != null && !fo.getNeedCreateConfigs().isEmpty()) {
            for (Map.Entry<String, String> config : fo.getNeedCreateConfigs().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.datasourceDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                if (configDO == null) {
                    DsConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(config.getKey())).findFirst().orElse(null);
                    if (defaultConfig != null) {
                        String value = config.getValue();
                        if (value != null) {
                            value = value.trim();
                        }

                        UpdateDsConfigLO configLO = new UpdateDsConfigLO();
                        configLO.setConfigName(config.getKey());
                        configLO.setNeedCreate(true);

                        if (defaultConfig.isSecret()) {
                            value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
                        } else {
                            configLO.setConfigValue(config.getValue());
                        }

                        DmDsConfigKv4DmDO newConfig = new DmDsConfigKv4DmDO();
                        newConfig.setDataSourceId(dataSourceDO.getId());
                        newConfig.setConfigName(defaultConfig.getConfigName());
                        newConfig.setConfigValue(value);
                        this.datasourceDal.configKv4DmMapper().insert(newConfig);
                        result.add(configLO);
                    }
                }
            }
        }

        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
        return result;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public UpdateDsDescLO updateDataSourceDesc(String puid, Long dataSourceId, String instanceDesc) {
        DmDsDO dataSourceDO = this.fetchAndCheckById(dataSourceId);
        UpdateDsDescLO lo = new UpdateDsDescLO();
        lo.setDataSourceId(dataSourceId);
        lo.setOldInstanceDesc(dataSourceDO.getInstanceDesc());
        lo.setNewInstanceDesc(instanceDesc);
        this.datasourceDal.dsMapper().updateDescByInstanceId(dataSourceId, instanceDesc);
        this.notifyServices.forEach(s -> s.onDsUpdate(dataSourceId));
        return lo;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateAkSk(String puid, Long dataSourceId, String accessKey, String secretKey) {
        DmDsDO dataSourceDO = this.fetchAndCheckById(dataSourceId);

        String encSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(secretKey);
        this.datasourceDal.dsMapper().updateAkAndSk(dataSourceDO.getId(), accessKey, encSecretKey);
        this.notifyServices.forEach(s -> s.onDsUpdate(dataSourceId));
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public UpdatePubHostLO updateDataSourcePublicHost(String puid, Long dataSourceId, String publicHost) {
        this.fetchAndCheckById(dataSourceId);
        DmDsDO rdpDataSourceDO = this.datasourceDal.dsMapper().queryDsIdentityById(dataSourceId);
        if (rdpDataSourceDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_CHECK_NOT_EXIST_ERROR.name()));
        }
        this.datasourceDal.dsMapper().updatePublicHostByInstanceId(dataSourceId, publicHost);

        UpdatePubHostLO lo = new UpdatePubHostLO();
        lo.setDataSourceId(dataSourceId);
        lo.setOldPublicHost(rdpDataSourceDO.getPublicHost());
        lo.setNewPublicHost(publicHost);

        this.notifyServices.forEach(s -> s.onDsUpdate(dataSourceId));
        return lo;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public UpdatePriHostLO updateDataSourcePrivateHost(String puid, Long dataSourceId, String privateHost) {
        this.fetchAndCheckById(dataSourceId);
        DmDsDO rdpDataSourceDO = this.datasourceDal.dsMapper().queryDsIdentityById(dataSourceId);
        if (rdpDataSourceDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_CHECK_NOT_EXIST_ERROR.name()));
        }
        this.datasourceDal.dsMapper().updatePrivateHostByInstanceId(dataSourceId, privateHost);

        UpdatePriHostLO lo = new UpdatePriHostLO();
        lo.setDataSourceId(dataSourceId);
        lo.setOldPrivateHost(rdpDataSourceDO.getPrivateHost());
        lo.setNewPrivateHost(privateHost);
        this.notifyServices.forEach(s -> s.onDsUpdate(dataSourceId));
        return lo;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateDataSourceAccount(String puid, UpdateSecurityInfoFO fo) {
        DmDsDO dsDo = this.fetchAndCheckById(fo.getDataSourceId());

        String encPasswd = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(fo.getPassword());

        String securityFilePassword = null;
        String clientSecurityFilePassword = null;
        String secretFilePassword = null;
        if (StringUtils.isNotBlank(fo.getSecurityFilePassword())) {
            securityFilePassword = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(fo.getSecurityFilePassword());
        }
        if (StringUtils.isNotBlank(fo.getClientSecurityFilePassword())) {
            clientSecurityFilePassword = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(fo.getClientSecurityFilePassword());
        }
        if (StringUtils.isNotBlank(fo.getSecretFilePassword())) {
            secretFilePassword = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(fo.getSecretFilePassword());
        }
        this.datasourceDal.dsMapper()
            .updateSecurityAllInfo(dsDo.getId(), fo.getUserName(), encPasswd, fo.getSecurityType(), null, dsDo.getAccessKey(), dsDo
                .getSecretKey(), null, securityFilePassword, null, clientSecurityFilePassword, null, secretFilePassword);

        SecurityType securityType = fo.getSecurityType();
        if (securityType == null || securityType == SecurityType.AK_SK) {
            updateAkSk(puid, dsDo.getId(), fo.getAccessKey(), fo.getSecretKey());
        }

        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
    }

    @Override
    public List<RdpDsKvConfigVO> queryDsConfigs(Long dataSourceId) {
        if (dataSourceId == null) {
            return new ArrayList<>();
        }

        DmDsDO ds = this.datasourceDal.dsMapper().selectById(dataSourceId);
        if (ds == null) {
            return new ArrayList<>();
        }

        List<DmDsConfigKv4DmDO> configList = this.datasourceDal.configKv4DmMapper().listByDsId(dataSourceId);
        Map<String, DmDsConfigKv4DmDO> configMap = new HashMap<>();
        for (DmDsConfigKv4DmDO configDO : configList) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DsConfigKvDef> defaultConfigs = this.dmDsConfigService.fetchDsConfigDef(ds.getDataSourceType());

        List<RdpDsKvConfigVO> resultConfigs = new ArrayList<>();
        for (DsConfigKvDef configDO : defaultConfigs) {
            DmDsConfigKv4DmDO config = configMap.get(configDO.getConfigName());
            if (config == null) {
                RdpDsKvConfigVO v = RdpConvertUtils.convertToDsKvConfigVO(configDO);
                v.setNeedCreated(true);
                resultConfigs.add(v);
            } else {
                RdpDsKvConfigVO v = RdpConvertUtils.convertToDsKvConfigVO(configDO, config);
                resultConfigs.add(v);
            }
        }

        return resultConfigs;
    }

    @Override
    public RdpDsKvConfigVO queryDsConfig(Long dataSourceId, String configName) {
        if (dataSourceId == null) {
            return null;
        }

        DmDsDO ds = this.datasourceDal.dsMapper().selectById(dataSourceId);
        if (ds == null) {
            return null;
        }

        DmDsConfigKv4DmDO config = this.datasourceDal.configKv4DmMapper().queryByDsIdAndConfigName(dataSourceId, configName);
        if (config == null || StringUtils.isBlank(config.getConfigValue())) {
            return null;
        }

        DsConfigKvDef configDef = this.dmDsConfigService.fetchDsConfigDef(ds.getDataSourceType())//
            .stream()
            .filter(c -> c.getConfigName().equals(configName))
            .findFirst()
            .orElse(null);
        if (configDef == null) {
            return null;
        }
        return RdpConvertUtils.convertToDsKvConfigVO(configDef, config);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void cleanDataSourceAccount(String puid, long dsId) {
        this.datasourceDal.dsMapper().cleanDataSourceAccount(dsId);
        this.notifyServices.forEach(s -> s.onDsUpdate(dsId));
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Long> addDataSource(String puid, String uid, AddDsFO addFO) {
        DmAuthUserDO pUserDO = this.rdpUserService.getUserByUid(puid);
        long dsId;
        try {
            dsId = saveSelfMaintainDs(addFO, puid, pUserDO.getUsername());
            addCreatorAuth(uid, dsId);
        } catch (Exception e) {
            throw ExceptionUtils.toRuntime(ExceptionUtils.getRootCause(e));
        }

        this.notifyServices.forEach(s -> s.onDsAdd(uid, dsId));
        return ResWebDataUtils.buildSuccess(dsId);
    }

    protected void addCreatorAuth(String uid, Long dsId) {
        DmAuthUserDO opUserDO = this.rdpUserService.getUserByUid(uid);
        if (opUserDO.getAccountType() != AccountType.SUB_ACCOUNT) {
            return;
        }

        List<AuthInfo> dsManageAuths = this.rdpAuthServiceForManager.getCascadeAuthByLabel(SecDataAuthLabel.RDP_DAUTH_DS_MANAGER);
        //List<AuthInfo> createDatajobAuths = this.rdpAuthServiceForManager.getCascadeAuthByLabel(CcDataAuthLabel.CC_DAUTH_DS_DATA_WRITE);
        List<AuthInfo> dataOperateAuths = this.rdpAuthServiceForManager.getCascadeAuthByLabel(SecDataAuthLabel.DM_DAUTH_TICKET);

        Set<String> dsManageLabels = dsManageAuths.stream().map(AuthInfo::getKey).collect(Collectors.toSet());
        //Set<String> createDatajobLabels = createDatajobAuths.stream().map(AuthInfo::getKey).collect(Collectors.toSet());
        Set<String> dataOperateLabels = dataOperateAuths.stream().map(AuthInfo::getKey).collect(Collectors.toSet());

        //dsManageLabels.addAll(createDatajobLabels);
        dsManageLabels.addAll(dataOperateLabels);

        DmDsDO dataSourceDO = datasourceDal.dsMapper().queryDsIdentityById(dsId);
        DmAuthResDO selfAudit = new DmAuthResDO();
        selfAudit.setOwnerUid(uid);
        selfAudit.setKindType(AuthKind.DataSource);
        selfAudit.setResId(dsId);
        selfAudit.setResInstId(dataSourceDO.getInstanceId());
        selfAudit.setResDesc(dataSourceDO.getInstanceDesc());
        selfAudit.setResPath(RdpAuthUtils.genEmptyResPath().getResPath());
        selfAudit.setLevelOne(RdpAuthUtils.genEmptyResPath().getResPath());
        selfAudit.setAuthLabels(new ArrayList<>(dsManageLabels));
        this.authDal.resMapper().insert(selfAudit); // add DataSource auth time is forever
    }

    protected long saveSelfMaintainDs(AddDsFO addDsFO, String uid, String owner) {
        DmDsDO entity = new DmDsDO();
        entity.setDataSourceType(addDsFO.getType());
        entity.setHost(addDsFO.getHost());
        entity.setPrivateHost(addDsFO.getPrivateHost());
        entity.setPublicHost(addDsFO.getPublicHost());
        entity.setHostType(addDsFO.getHostType());
        entity.setUid(uid);
        entity.setOwner(owner);
        entity.setSecurityType(addDsFO.getSecurityType());
        entity.setLifeCycleState(LifeCycleState.CREATED);
        entity.setStatus(DataSourceStatus.Normal);
        entity.setStatusMessage("");
        entity.setBindClusterId(addDsFO.getBindClusterId());
        entity.setDriver(addDsFO.getDriver());
        entity.setDsEnvId(addDsFO.getEnvId());

        if (StringUtils.isNotBlank(addDsFO.getVersion())) {
            entity.setVersion(addDsFO.getVersion());
        }

        if (entity.getSecurityType() == null) {
            entity.setSecurityType(SecurityType.USER_PASSWD);
        }

        entity.setAccessKey(addDsFO.getAccessKey());

        if (StringUtils.isNotBlank(addDsFO.getSecretKey())) {
            entity.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getSecretKey()));
        }

        if (StringUtils.isNotBlank(addDsFO.getAccount())) {
            entity.setAccount(addDsFO.getAccount());
        }

        if (StringUtils.isNotBlank(addDsFO.getPassword())) {
            entity.setPassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getPassword()));
        } else {
            // for compatibility
            entity.setPassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(""));
        }

        fillInstanceIdAndDesc(addDsFO, entity);

        this.datasourceDal.dsMapper().insert(entity);
        this.dmDsConfigService.persistDsConfig(entity, entity.getHostType(), entity.getVersion());
        this.applyInitialDsConfigOverrides(entity.getId(), addDsFO.getDsKvConfigs());

        return entity.getId();
    }

    protected void fillInstanceIdAndDesc(AddDsFO addDsFO, DmDsDO entity) {
        if (addDsFO.getInstanceId() == null) {
            entity.setInstanceId(genInstanceId(addDsFO.getType()));
        } else {
            entity.setInstanceId(addDsFO.getInstanceId());
        }

        if (StringUtils.isNotBlank(addDsFO.getInstanceDesc())) {
            entity.setInstanceDesc(addDsFO.getInstanceDesc());
        } else {
            entity.setInstanceDesc(addDsFO.getInstanceId());
        }
    }

    private void applyInitialDsConfigOverrides(long dsId, List<InitDsKvBaseConfigFO> kvConfigs) {
        if (CollectionUtils.isEmpty(kvConfigs)) {
            return;
        }

        DmDsDO ds = this.datasourceDal.dsMapper().selectById(dsId);
        if (ds == null) {
            return;
        }
        List<DsConfigKvDef> defaultConfigs = this.dmDsConfigService.fetchDsConfigDef(ds.getDataSourceType());
        for (InitDsKvBaseConfigFO kvConfig : kvConfigs) {
            DmDsConfigKv4DmDO configDO = this.datasourceDal.configKv4DmMapper().queryByDsIdAndConfigName(dsId, kvConfig.getConfigName());
            DsConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(kvConfig.getConfigName())).findFirst().orElse(null);
            if (configDO == null || defaultConfig == null || defaultConfig.isReadOnly()) {
                continue;
            }

            String value = kvConfig.getConfigValue();
            if (defaultConfig.isSecret() && StringUtils.isNotBlank(value)) {
                value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
            }
            this.datasourceDal.configKv4DmMapper().updateDsConfig(dsId, kvConfig.getConfigName(), value);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Long> delDataSource(String puid, long dsId) {
        DmDsDO userDs = this.fetchAndCheckById(dsId);

        List<DmDsUsageDO> usageDOs = rdpDsUsageService.listDsUsage(dsId);
        if (usageDOs != null && usageDOs.size() > 0) {
            String resInsts = usageDOs.stream().map(DmDsUsageDO::getResInstanceId).collect(Collectors.joining(","));
            throw new ConsoleRuntimeException(ConsoleErrorCode.STILL_HAVE_BIZ_USE_IT_WHEN_DELETE_DATASOURCE, resInsts);
        }

        this.rdpAuthServiceForManager.clearAuthOfRes(dsId, AuthKind.DataSource);
        this.datasourceDal.dsMapper().updateLifeCycleStateById(dsId, LifeCycleState.DELETED);
        this.dmDsConfigService.cleanDsConfig(dsId);

        this.notifyServices.forEach(s -> s.onDsDelete(dsId));
        return ResWebDataUtils.buildSuccess();
    }

    @Override
    public DmDsDO queryById(Long dataSourceId) {
        return this.datasourceDal.dsMapper().selectById(dataSourceId);
    }

    @Override
    public List<DmDsDO> listByIds(List<Long> ids) {
        return this.datasourceDal.dsMapper().listByIds(ids);
    }

    @Override
    public DmDsDO fetchAndCheckById(Long dataSourceId) {
        if (dataSourceId == null || dataSourceId <= 0) {
            throw new RuntimeException("data source id cannot be null.");
        }

        DmDsDO re = this.datasourceDal.dsMapper().selectById(dataSourceId);
        if (re == null) {
            throw new IllegalArgumentException("datasource(" + dataSourceId + ") not exist.");
        }

        fillExtraConfig(re, null);
        return re;
    }

    @Override
    public DmDsDO fetchByInstanceId(String instanceId) {
        if (StringUtils.isBlank(instanceId)) {
            throw new RuntimeException("instance id cannot be empty.");
        }

        DmDsDO re = this.datasourceDal.dsMapper().getByInstanceId(instanceId);
        if (re == null) {
            throw new IllegalArgumentException("datasource(" + instanceId + ") not exist.");
        }

        fillExtraConfig(re, null);
        return re;
    }

    private void fillExtraConfig(DmDsDO re, Map<Long, DmSysEnvDO> envMap) {
        if (envMap != null && envMap.containsKey(re.getDsEnvId())) {
            re.setDsEnvDO(envMap.get(re.getDsEnvId()));
        }
    }

    protected String genInstanceId(DataSourceType dataSourceType) {
        return dataSourceType.getShortName() + "-" + RandomStrUtils.fixedLenRandomStr(15);
    }
}

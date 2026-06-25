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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.impl.DmDsConfigHelper;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.DsConfigSubmitFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsConfigFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsKvConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsConfigLO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsDescLO;
import com.clougence.clouddm.console.web.model.vo.DsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.RdpDsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.datasource.ConnectDsResultVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.upload.ConsoleUploadService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
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
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmDsWebServiceImpl implements DmDsWebService {

    @Resource
    private SystemDal              systemDal;
    @Resource
    private DataSourceDal          dsDal;
    @Resource
    private AuthDal                authDal;
    @Resource
    private RdpUserService         userService;
    @Resource
    private DmAuthServiceForManage authServiceForManage;
    @Resource
    private DmDsService            dmDsService;
    @Resource
    private DmDsConfigService      configService;
    @Resource
    private ConsoleUploadService   uploadService;
    @Resource
    private List<RdpNotifyService> notifyServices;

    @Override
    public List<DmDsDO> fetchByCondition(ArgDsQueryParamObj dsQueryParam) {
        List<DmDsDO> dsList = this.dsDal.dsMapper().listByCondition(dsQueryParam);
        for (DmDsDO ds : dsList) {
            fillExtraConfig(ds, null);
        }
        return dsList;
    }

    @Override
    public List<DmDsDO> fetchByCondition(String ownerUid, ArgDsQueryParamObj dsQueryParam, boolean fillEnv) {
        List<DmDsDO> dsList = this.dsDal.dsMapper().listByCondition(dsQueryParam);
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
        DmDsDO dataSourceDO = this.dmDsService.fetchAndCheckById(dataSourceId);
        dataSourceDO.setSecretKey(null);
        return dataSourceDO;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public List<UpdateDsConfigLO> upsertDsConfigs(String puid, UpsertDsKvConfigFO fo) {
        List<UpdateDsConfigLO> result = new ArrayList<>();

        DmDsDO dataSourceDO = this.dmDsService.fetchAndCheckById(fo.getDataSourceId());
        List<DsConfigKvDef> defaultConfigs = this.configService.fetchDsConfigDef(dataSourceDO.getDataSourceType());

        if (fo.getUpdateConfigs() != null && !fo.getUpdateConfigs().isEmpty()) {
            for (Map.Entry<String, String> config : fo.getUpdateConfigs().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
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
                    this.dsDal.configKv4DmMapper().updateDsConfig(fo.getDataSourceId(), config.getKey(), value);
                    result.add(configLO);
                }
            }
        }

        if (fo.getNeedCreateConfigs() != null && !fo.getNeedCreateConfigs().isEmpty()) {
            for (Map.Entry<String, String> config : fo.getNeedCreateConfigs().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
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
                        this.dsDal.configKv4DmMapper().insert(newConfig);
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
        DmDsDO dataSourceDO = this.dmDsService.fetchAndCheckById(dataSourceId);
        UpdateDsDescLO lo = new UpdateDsDescLO();
        lo.setDataSourceId(dataSourceId);
        lo.setOldInstanceDesc(dataSourceDO.getInstanceDesc());
        lo.setNewInstanceDesc(instanceDesc);
        this.dsDal.dsMapper().updateDescByInstanceId(dataSourceId, instanceDesc);
        this.notifyServices.forEach(s -> s.onDsUpdate(dataSourceId));
        return lo;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateDataSourceAccount(String puid, UpdateSecurityInfoFO fo) {
        DmDsDO dsDo = this.dmDsService.fetchAndCheckById(fo.getDataSourceId());

        SecurityType securityType = fo.getSecurityType();
        String accessKey = securityType == SecurityType.AK_SK ? fo.getAccessKey() : fo.getUserName();
        String secretKey = securityType == SecurityType.AK_SK ? fo.getSecretKey() : fo.getPassword();
        String encSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(secretKey);

        this.dsDal.dsMapper().updateSecurityInfo(dsDo.getId(), accessKey, encSecretKey, securityType);
        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
    }

    @Override
    public List<RdpDsKvConfigVO> queryDsConfigs(Long dataSourceId) {
        if (dataSourceId == null) {
            return new ArrayList<>();
        }

        DmDsDO ds = this.dsDal.dsMapper().selectById(dataSourceId);
        if (ds == null) {
            return new ArrayList<>();
        }

        List<DmDsConfigKv4DmDO> configList = this.dsDal.configKv4DmMapper().listByDsId(dataSourceId);
        Map<String, DmDsConfigKv4DmDO> configMap = new HashMap<>();
        for (DmDsConfigKv4DmDO configDO : configList) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DsConfigKvDef> defaultConfigs = this.configService.fetchDsConfigDef(ds.getDataSourceType());

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

        DmDsDO ds = this.dsDal.dsMapper().selectById(dataSourceId);
        if (ds == null) {
            return null;
        }

        DmDsConfigKv4DmDO config = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(dataSourceId, configName);
        if (config == null || StringUtils.isBlank(config.getConfigValue())) {
            return null;
        }

        DsConfigKvDef configDef = this.configService.fetchDsConfigDef(ds.getDataSourceType())//
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
        this.dsDal.dsMapper().cleanDataSourceAccount(dsId);
        this.notifyServices.forEach(s -> s.onDsUpdate(dsId));
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Long> addDataSource(String uid, DsConfigSubmitFO addFO) {
        Map<String, String> configMap = resolveConfigMap(addFO);
        DataSourceConfig dsConfig = resolveDsConfig(addFO, configMap);
        for (DsConfigKvDef configDef : DmDsConfigHelper.collectConfigs(dsConfig)) {
            if (StringUtils.isBlank(configDef.getConfigName())) {
                continue;
            }
            if (!configMap.containsKey(configDef.getConfigName()) || StringUtils.isBlank(configMap.get(configDef.getConfigName()))) {
                configMap.put(configDef.getConfigName(), configDef.getConfigValue());
            }
        }

        //
        DmDsDO entity = new DmDsDO();
        entity.setDataSourceType(dsConfig.getDataSourceType());
        entity.setHost(dsConfig.getHost());
        entity.setUid(uid);
        entity.setOwner(AuthDal.ROOT_USER_UID);
        entity.setSecurityType(dsConfig.getSecurityType() == null ? SecurityType.USER_PASSWD : dsConfig.getSecurityType());
        entity.setLifeCycleState(LifeCycleState.CREATED);
        entity.setStatus(DataSourceStatus.Normal);
        entity.setStatusMessage("");
        entity.setBindClusterId(addFO.getClusterId());
        entity.setDriver(addFO.getDriver());
        entity.setDsEnvId(addFO.getEnvId());
        entity.setVersion(dsConfig.getVersion());
        entity.setAccessKey(dsConfig.getUserName());
        entity.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(StringUtils.defaultString(dsConfig.getPassword())));

        if (StringUtils.isBlank(dsConfig.getInstanceId())) {
            dsConfig.setInstanceId(dsConfig.getDataSourceType().getShortName() + "-" + RandomStrUtils.fixedLenRandomStr(15));
        }
        entity.setInstanceId(dsConfig.getInstanceId());
        entity.setInstanceDesc(StringUtils.isNotBlank(addFO.getInstanceDesc()) ? addFO.getInstanceDesc() : dsConfig.getInstanceId());

        this.dsDal.dsMapper().insert(entity);
        this.configService.upsertDsConfigs(entity.getId(), configMap);

        long dsId = entity.getId();
        addCreatorAuth(uid, dsId);

        this.notifyServices.forEach(s -> s.onDsAdd(uid, dsId));
        return ResWebDataUtils.buildSuccess(dsId);
    }

    @Override
    public ConnectDsResultVO testConnect(String uid, DsConfigSubmitFO fo) {
        Map<String, String> configMap = resolveConfigMap(fo);
        DataSourceConfig dsConfig = resolveDsConfig(fo, configMap);
        ConnectDsResultVO result = new ConnectDsResultVO();
        try {
            if (fo.getClusterId() == null || fo.getClusterId() <= 0) {
                throw new IllegalArgumentException("bind cluster id can not be empty.");
            }
            String version = this.dmDsService.testConnect(uid, fo.getClusterId(), fo.getDriver(), dsConfig);
            result.setSuccess(true);
            result.setVersion(version);
        } catch (Exception e) {
            log.error("connectDs failed, uid={}, clusterId={}, dsType={}, {}", uid, fo.getClusterId(), fo.getDsType(), e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    protected void addCreatorAuth(String uid, Long dsId) {
        DmAuthUserDO opUserDO = this.userService.getUserByUid(uid);
        if (opUserDO.getAccountType() != AccountType.SUB_ACCOUNT) {
            return;
        }

        List<AuthInfo> dsManageAuths = this.authServiceForManage.getCascadeAuthByLabel(SecDataAuthLabel.RDP_DAUTH_DS_MANAGER);
        List<AuthInfo> dataOperateAuths = this.authServiceForManage.getCascadeAuthByLabel(SecDataAuthLabel.DM_DAUTH_TICKET);

        Set<String> dsManageLabels = dsManageAuths.stream().map(AuthInfo::getKey).collect(Collectors.toSet());
        Set<String> dataOperateLabels = dataOperateAuths.stream().map(AuthInfo::getKey).collect(Collectors.toSet());

        dsManageLabels.addAll(dataOperateLabels);

        DmDsDO dataSourceDO = dsDal.dsMapper().queryDsIdentityById(dsId);
        DmAuthResDO selfAudit = new DmAuthResDO();
        selfAudit.setOwnerUid(uid);
        selfAudit.setKindType(AuthKind.DataSource);
        selfAudit.setResId(dsId);
        selfAudit.setResInstId(dataSourceDO.getInstanceId());
        selfAudit.setResDesc(dataSourceDO.getInstanceDesc());
        selfAudit.setResPath(RdpAuthUtils.genEmptyResPath().getResPath());
        selfAudit.setLevelOne(RdpAuthUtils.genEmptyResPath().getResPath());
        selfAudit.setAuthLabels(new ArrayList<>(dsManageLabels));
        this.authDal.resMapper().insert(selfAudit);
    }

    private Map<String, String> resolveConfigMap(DsConfigSubmitFO fo) {
        if (fo == null || fo.getDsType() == null) {
            throw new IllegalArgumentException("data source type can not be empty.");
        }

        Map<String, String> configMap = new LinkedHashMap<>();
        if (fo.getConfigMap() != null) {
            configMap.putAll(fo.getConfigMap());
        }

        configMap.put(DataSourceConfig.Fields.dataSourceType, fo.getDsType().name());
        configMap.putIfAbsent(DataSourceConfig.Fields.configVersion, "1");
        if (StringUtils.isBlank(configMap.get(DataSourceConfig.Fields.instanceId))) {
            configMap.put(DataSourceConfig.Fields.instanceId, fo.getDsType().getShortName() + "-" + RandomStrUtils.fixedLenRandomStr(15));
        }

        configMap.replaceAll((key, value) -> this.uploadService.resolveCertificateData(value));
        return configMap;
    }

    private DataSourceConfig resolveDsConfig(DsConfigSubmitFO fo, Map<String, String> configMap) {
        DataSourceType dsType = fo.getDsType();
        DmDsDO tempDs = new DmDsDO();
        tempDs.setInstanceId(configMap.get(DataSourceConfig.Fields.instanceId));
        tempDs.setDataSourceType(dsType);
        tempDs.setHost(configMap.get(DataSourceConfig.Fields.host));
        tempDs.setDriver(fo.getDriver());
        tempDs.setVersion(configMap.get(DataSourceConfig.Fields.version));

        String securityType = configMap.get(DataSourceConfig.Fields.securityType);
        if (StringUtils.isNotBlank(securityType)) {
            tempDs.setSecurityType(SecurityType.valueOf(securityType));
        }
        tempDs.setAccessKey(configMap.get(DataSourceConfig.Fields.userName));
        tempDs.setSecretKey(configMap.get(DataSourceConfig.Fields.password));
        tempDs.setDsEnvId(fo.getEnvId());

        return this.configService.fetchDsConfigFromNotExist(tempDs, configMap);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Long> delDataSource(String puid, long dsId) {
        this.dmDsService.fetchAndCheckById(dsId);

        this.authServiceForManage.clearAuthOfRes(dsId, AuthKind.DataSource);
        this.dsDal.dsMapper().updateLifeCycleStateById(dsId, LifeCycleState.DELETED);
        this.configService.cleanDsConfig(dsId);

        this.notifyServices.forEach(s -> s.onDsDelete(dsId));
        return ResWebDataUtils.buildSuccess();
    }

    @Override
    public DmDsDO queryById(Long dataSourceId) {
        return this.dsDal.dsMapper().selectById(dataSourceId);
    }

    @Override
    public List<DmDsDO> listByIds(List<Long> ids) {
        return this.dsDal.dsMapper().listByIds(ids);
    }

    private void fillExtraConfig(DmDsDO re, Map<Long, DmSysEnvDO> envMap) {
        if (envMap != null && envMap.containsKey(re.getDsEnvId())) {
            re.setDsEnvDO(envMap.get(re.getDsEnvId()));
        }
    }

    @Override
    public List<DmDsDO> fetchDsConfigByIds(String ownerUid, List<Long> ids) {
        return this.dsDal.dsMapper().listByOwnerAndIds(ownerUid, ids);
    }

    @Override
    public ResWebData<Boolean> updateDsDesc(String puid, String uid, long dsId, String desc) {
        DmDsDO dsDO = this.queryById(dsId);
        if (dsDO == null || StringUtils.isBlank(desc) || StringUtils.equals(dsDO.getInstanceDesc(), desc)) {
            return ResWebDataUtils.buildSuccess(true);
        }

        this.dsDal.dsMapper().updateDescByInstanceId(dsId, desc);
        return ResWebDataUtils.buildSuccess(true);
    }

    @Override
    public List<DmDsDO> listDsByClusterId(long clusterId) {
        return this.dsDal.dsMapper().listByClusterId(clusterId);
    }

    @Override
    public List<DsKvConfigVO> queryDsConfigIncludeNewEntries(Long dsId) {
        if (dsId == null) {
            return new ArrayList<>();
        }

        DmDsDO ds = this.queryById(dsId);
        if (ds == null) {
            return new ArrayList<>();
        }

        List<DmDsConfigKv4DmDO> configList = this.dsDal.configKv4DmMapper().listByDsId(ds.getId());
        Map<String, DmDsConfigKv4DmDO> configMap = new HashMap<>();
        for (DmDsConfigKv4DmDO configDO : configList) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DsConfigKvDef> defaultConfigs = this.configService.fetchDsConfigDef(ds.getDataSourceType());

        List<DsKvConfigVO> resultConfigs = new ArrayList<>();
        for (DsConfigKvDef configDO : defaultConfigs) {
            DmDsConfigKv4DmDO config = configMap.get(configDO.getConfigName());
            DsKvConfigVO v;
            if (config == null) {
                v = DmConvertUtils.convertToDsKvConfigVO(configDO);
                v.setNeedCreated(true);
                resultConfigs.add(v);
            } else {
                v = DmConvertUtils.convertToDsKvConfigVO(configDO, config);
                resultConfigs.add(v);
            }
        }

        return resultConfigs;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void upsertConfigs(String puid, UpsertDsConfigFO fo) {
        if (CollectionUtils.isEmpty(fo.getUpdateConfigMap()) && CollectionUtils.isEmpty(fo.getNeedCreateConfigMap())) {
            throw new IllegalArgumentException("update config map and need create config map are both empty.");
        }

        DmDsDO rdpDs = this.queryById(fo.getDataSourceId());
        List<DsConfigKvDef> defaultConfigs = this.configService.fetchDsConfigDef(rdpDs.getDataSourceType());
        if (CollectionUtils.isNotEmpty(fo.getUpdateConfigMap())) {
            for (Map.Entry<String, String> config : fo.getUpdateConfigMap().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                DsConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(config.getKey())).findFirst().orElse(null);
                if (configDO != null && defaultConfig != null) {
                    String value = config.getValue();
                    if (value != null) {
                        value = value.trim();
                        if (defaultConfig.isSecret()) {
                            value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
                        }
                    }

                    this.dsDal.configKv4DmMapper().updateDsConfig(fo.getDataSourceId(), config.getKey(), value);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(fo.getNeedCreateConfigMap())) {
            for (Map.Entry<String, String> config : fo.getNeedCreateConfigMap().entrySet()) {
                DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                if (configDO == null) {
                    DsConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(config.getKey())).findFirst().orElse(null);
                    if (defaultConfig != null) {
                        String value = config.getValue();
                        if (value != null) {
                            value = value.trim();
                        }

                        if (defaultConfig.isSecret()) {
                            value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
                        }

                        DmDsConfigKv4DmDO dmKvConf = new DmDsConfigKv4DmDO();
                        dmKvConf.setDataSourceId(rdpDs.getId());
                        dmKvConf.setConfigName(defaultConfig.getConfigName());
                        dmKvConf.setConfigValue(value);
                        this.dsDal.configKv4DmMapper().insert(dmKvConf);
                    }
                }
            }
        }

        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
    }
}

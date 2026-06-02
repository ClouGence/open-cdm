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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.clouddm.api.common.exception.ConsoleRuntimeException;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.AddDsFO;
import com.clougence.clouddm.console.web.model.fo.datasource.UpsertDsKvConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsConfigLO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsDescLO;
import com.clougence.clouddm.console.web.model.lo.UpdatePriHostLO;
import com.clougence.clouddm.console.web.model.lo.UpdatePubHostLO;
import com.clougence.clouddm.console.web.model.vo.DefaultDsKvConfigVO;
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
import com.clougence.rdp.component.dskvconfig.RdpDsConfigService;
import com.clougence.rdp.component.dskvconfig.util.PropsCryptUtil;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpDsUsageService;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.RdpSecurityService;
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
    private RdpSecurityService     rdpSecurityService;
    @Resource
    private RdpDsConfigService     rdpDsConfigService;
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

    @Override
    public List<DefaultDsKvConfigVO> queryDsDefaultConfig(DataSourceType dataSourceType, DeployEnvType envType) {
        long dummyDsID = 1;
        List<DmDsConfigKv4RdpDO> configs = this.rdpDsConfigService.fetchDefaultConfig(dummyDsID, dataSourceType);
        List<DefaultDsKvConfigVO> cs = new ArrayList<>();
        for (DmDsConfigKv4RdpDO c : configs) {
            if (isDefaultValueTrue(c, dataSourceType, envType)) {
                c.setDefaultValue(Boolean.TRUE.toString());
            }

            DefaultDsKvConfigVO v = new DefaultDsKvConfigVO();
            v.convertFromDO(c);
            cs.add(v);
        }

        return cs;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public List<UpdateDsConfigLO> upsertDsConfigs(String puid, UpsertDsKvConfigFO fo) {
        List<UpdateDsConfigLO> result = new ArrayList<>();

        DmDsDO dataSourceDO = this.fetchAndCheckById(fo.getDataSourceId());

        if (fo.getUpdateConfigs() != null && !fo.getUpdateConfigs().isEmpty()) {
            for (Map.Entry<String, String> config : fo.getUpdateConfigs().entrySet()) {
                DmDsConfigKv4RdpDO configDO = this.datasourceDal.configKv4RdpMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                if (configDO != null) {
                    String value = config.getValue();
                    if (value != null) {
                        value = value.trim();
                    }

                    DataSourceType dsType = dataSourceDO.getDataSourceType();
                    if (PropsCryptUtil.CONFIG_SECRET_NAMES.containsKey(dsType)) {
                        Set<String> configs = PropsCryptUtil.CONFIG_SECRET_NAMES.get(dsType);
                        if (configs.contains(configDO.getConfigName())) {
                            String oldValue = configDO.getConfigValue();
                            String newProps = PropsCryptUtil.diffAndEncryptSecrets(dsType, oldValue, value);
                            if (StringUtils.isNotBlank(newProps)) {
                                value = newProps;
                            }
                        }
                    }

                    if (configDO.isSecret() && StringUtils.isNotBlank(value)) {
                        value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
                    }

                    if (configDO.isReadOnly()) {
                        continue;
                    }

                    UpdateDsConfigLO configLO = new UpdateDsConfigLO();
                    configLO.setConfigName(configDO.getConfigName());
                    configLO.setNeedCreate(false);
                    if (!configDO.isSecret()) {
                        configLO.setOldConfigValue(configDO.getConfigValue());
                        configLO.setConfigValue(config.getValue());
                    }
                    this.datasourceDal.configKv4RdpMapper().updateDsConfig(fo.getDataSourceId(), config.getKey(), value);
                    result.add(configLO);
                }
            }
        }

        if (fo.getNeedCreateConfigs() != null && !fo.getNeedCreateConfigs().isEmpty()) {
            List<DmDsConfigKv4RdpDO> defaultConfigs = this.rdpDsConfigService.fetchDefaultConfig(dataSourceDO.getId(), dataSourceDO.getDataSourceType());

            for (Map.Entry<String, String> config : fo.getNeedCreateConfigs().entrySet()) {
                DmDsConfigKv4RdpDO configDO = this.datasourceDal.configKv4RdpMapper().queryByDsIdAndConfigName(fo.getDataSourceId(), config.getKey());
                if (configDO == null) {
                    DmDsConfigKv4RdpDO defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(config.getKey())).findFirst().orElse(null);
                    if (defaultConfig != null) {
                        String value = config.getValue();
                        if (value != null) {
                            value = value.trim();
                        }

                        UpdateDsConfigLO configLO = new UpdateDsConfigLO();
                        configLO.setConfigName(config.getKey());
                        configLO.setNeedCreate(true);

                        if (defaultConfig.isSecret()) {
                            defaultConfig.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value));
                        } else {
                            defaultConfig.setConfigValue(value);
                            configLO.setConfigValue(config.getValue());
                        }

                        this.datasourceDal.configKv4RdpMapper().insert(defaultConfig);
                        result.add(configLO);
                    }
                }
            }
        }

        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
        return result;
    }

    private boolean isDefaultValueTrue(DmDsConfigKv4RdpDO c, DataSourceType dataSourceType, DeployEnvType envType) {
        if (StringUtils.equals(c.getConfigName(), "useSSL")) {
            return envType == DeployEnvType.MICROSOFT_AZURE_CLOUD_HOSTED && (dataSourceType == DataSourceType.MySQL || dataSourceType == DataSourceType.PostgreSQL
                                                                             || dataSourceType == DataSourceType.MariaDB || dataSourceType == DataSourceType.SQLServer);
        }

        return false;
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

        MultipartFile securityFile = fo.getSecurityFile();
        String securityFileName = securityFile == null ? null : UUID.randomUUID() + "-" + securityFile.getOriginalFilename();
        String securityFilePath = securityFile != null ? this.rdpSecurityService.genSecurityFileRelatePath(dsDo.getInstanceId(), securityFileName) : null;

        MultipartFile secretFile = fo.getSecretFile();
        String secretFileName = secretFile != null ? UUID.randomUUID() + "-" + secretFile.getOriginalFilename() : null;
        String secretFilePath = secretFile != null ? this.rdpSecurityService.genSecurityFileRelatePath(dsDo.getInstanceId(), secretFileName) : null;

        MultipartFile keyStoreFile = fo.getSecretFile();
        String keyStoreFileName = keyStoreFile != null ? UUID.randomUUID() + "-" + keyStoreFile.getOriginalFilename() : null;
        String keyStoreFilePath = keyStoreFile != null ? this.rdpSecurityService.genSecurityFileRelatePath(dsDo.getInstanceId(), keyStoreFileName) : null;

        MultipartFile clientSecurityFile = fo.getClientSecurityFile();
        String clientSecurityName = clientSecurityFile != null ? UUID.randomUUID() + "-" + clientSecurityFile.getOriginalFilename() : null;
        String clientSecurityFilePath = clientSecurityFile != null ? this.rdpSecurityService.genSecurityFileRelatePath(dsDo.getInstanceId(), clientSecurityName) : null;

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
            .updateSecurityAllInfo(dsDo.getId(), fo.getUserName(), encPasswd, fo.getSecurityType(), SecurityFileStoreType.META_DB, dsDo.getAccessKey(), dsDo
                .getSecretKey(), securityFilePath, securityFilePassword, clientSecurityFilePath, clientSecurityFilePassword, secretFilePath, secretFilePassword);

        SecurityType securityType = fo.getSecurityType();
        if (securityType == null || securityType == SecurityType.AK_SK) {
            updateAkSk(puid, dsDo.getId(), fo.getAccessKey(), fo.getSecretKey());
        } else if (securityFile != null && secretFile != null && securityType == SecurityType.KERBEROS) {
            updateDsSecurityFile(securityFile, dsDo, SecurityFileType.kerberos_keytab_file);
            updateDsSecurityFile(secretFile, dsDo, SecurityFileType.kerberos_conf_file);
        } else if (securityFile != null && securityType == SecurityType.USER_PASSWD_WITH_TLS) {
            updateDsSecurityFile(securityFile, dsDo, SecurityFileType.ssl_truststore_file);
            if (clientSecurityFile != null) {
                updateDsSecurityFile(clientSecurityFile, dsDo, SecurityFileType.ssl_keystore_file);
            }
        } else if (securityFile != null && securityType == SecurityType.CA_CERTIFICATE) {
            updateDsSecurityFile(securityFile, dsDo, SecurityFileType.ca_certificate_file);
            if (clientSecurityFile != null) {
                updateDsSecurityFile(clientSecurityFile, dsDo, SecurityFileType.client_certificate_file);
            }
            if (secretFile != null) {
                updateDsSecurityFile(secretFile, dsDo, SecurityFileType.secret_file);
            }
        } else if (keyStoreFilePath != null && securityType == SecurityType.USER_PASSWD_WITH_KEYSTORE) {
            updateDsSecurityFile(securityFile, dsDo, SecurityFileType.keystore_file);
        } else if (securityFile != null && (dsDo.getDataSourceType() == DataSourceType.Redis || dsDo.getDataSourceType() == DataSourceType.ElastiCache)) {
            updateDsSecurityFile(securityFile, dsDo, SecurityFileType.ca_certificate_file);
        }

        this.notifyServices.forEach(s -> s.onDsUpdate(fo.getDataSourceId()));
    }

    protected void updateDsSecurityFile(MultipartFile file, DmDsDO entity, SecurityFileType securityFileType) {
        if (file == null) {
            throw new IllegalArgumentException("datasource security file is null.security file type:" + securityFileType);
        }

        try {
            DmDsBlobResourceDO blobResourceDO = this.datasourceDal.blobResourceMapper().queryByIdentify(entity.getInstanceId(), ResourceType.DATASOURCE, securityFileType);
            if (blobResourceDO == null) {
                saveDsSecurityFile(file, entity, securityFileType);
            } else {
                this.datasourceDal.blobResourceMapper().updateByIdentify(file.getBytes(), entity.getInstanceId(), ResourceType.DATASOURCE, securityFileType);
            }
        } catch (IOException e) {
            String msg = "read security file from stream error.datasource instance id:" + entity.getInstanceId() + ".msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
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

        List<DmDsConfigKv4RdpDO> configList = this.datasourceDal.configKv4RdpMapper().listByDsId(dataSourceId);
        Map<String, DmDsConfigKv4RdpDO> configMap = new HashMap<>();
        for (DmDsConfigKv4RdpDO configDO : configList) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DmDsConfigKv4RdpDO> defaultConfigs = this.rdpDsConfigService.fetchDefaultConfig(ds.getId(), ds.getDataSourceType());

        List<RdpDsKvConfigVO> resultConfigs = new ArrayList<>();
        for (DmDsConfigKv4RdpDO configDO : defaultConfigs) {
            DmDsConfigKv4RdpDO config = configMap.get(configDO.getConfigName());
            if (config == null) {
                RdpDsKvConfigVO v = RdpConvertUtils.convertToDsKvConfigVO(configDO);
                v.setNeedCreated(true);
                resultConfigs.add(v);
            } else {
                RdpDsKvConfigVO v = RdpConvertUtils.convertToDsKvConfigVO(config);
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

        DmDsConfigKv4RdpDO c = this.datasourceDal.configKv4RdpMapper().queryByDsIdAndConfigName(dataSourceId, configName);
        if (c == null || StringUtils.isBlank(c.getConfigValue())) {
            return null;
        } else {
            return RdpConvertUtils.convertToDsKvConfigVO(c);
        }
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
        if (addFO.getDeployType() == DeployEnvType.SELF_MAINTENANCE || //
            addFO.getDeployType() == DeployEnvType.AWS_CLOUD_HOSTED || //
            addFO.getDeployType() == DeployEnvType.MICROSOFT_AZURE_CLOUD_HOSTED || //
            addFO.getDeployType() == DeployEnvType.HUAWEI_CLOUD_HOSTED || //
            addFO.getDeployType() == DeployEnvType.TENCENT_CLOUD_HOSTED || //
            addFO.getDeployType() == DeployEnvType.INDEPENDENT_CLOUD_PLATFORM) {
            try {
                dsId = saveSelfMaintainDs(addFO, puid, pUserDO.getUsername());
                addCreatorAuth(uid, dsId);
            } catch (Exception e) {
                throw ExceptionUtils.toRuntime(ExceptionUtils.getRootCause(e));
            }
        } else {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_ADD_UNSUPPORTED_ERROR.name(), addFO.getDeployType().name()));
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
        boolean isDb2 = DataSourceType.Db2 == addDsFO.getType();
        boolean hasDbName = StringUtils.isNotEmpty(addDsFO.getDbName());
        if (isDb2 && hasDbName) {
            addDsFO.setDbName(StringUtils.upperCase(addDsFO.getDbName()));
        }
        if (isDb2 && !hasDbName) {
            throw new IllegalArgumentException("DB2 datasource dbName can not be empty.");
        }

        DmDsDO entity = new DmDsDO();
        entity.setDataSourceType(addDsFO.getType());
        entity.setDeployType(addDsFO.getDeployType());
        entity.setInfoFetchType(addDsFO.getInfoFetchType());
        entity.setHost(addDsFO.getHost());
        entity.setPrivateHost(addDsFO.getPrivateHost());
        entity.setPublicHost(addDsFO.getPublicHost());
        entity.setHostType(addDsFO.getHostType());
        entity.setUid(uid);
        entity.setOwner(owner);
        entity.setSecurityType(addDsFO.getSecurityType());
        entity.setLifeCycleState(LifeCycleState.CREATED);
        entity.setConnectType(addDsFO.getConnectType());
        entity.setDriver(addDsFO.getDriver());
        entity.setDefaultDbName(addDsFO.getDbName());
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

        if (entity.getSecurityType() == SecurityType.KERBEROS) {
            entity.setSecurityFileStoreType(SecurityFileStoreType.META_DB);
            String keytabFileName = UUID.randomUUID() + "-" + addDsFO.getSecretFile().getOriginalFilename();
            String keytabFilePath = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), keytabFileName);
            entity.setSecretFileUrl(keytabFilePath);

            String kerberosConfFileName = UUID.randomUUID() + "-" + addDsFO.getSecurityFile().getOriginalFilename();
            String kerberosConfPath = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), kerberosConfFileName);
            entity.setSecurityFileUrl(kerberosConfPath);
        } else if (entity.getSecurityType() == SecurityType.USER_PASSWD_WITH_TLS) {
            if (addDsFO.getSecurityFile() == null) {
                throw new IllegalArgumentException("datasource security type is plain with ssl,but xxx.truststore.jks file is empty.");
            }

            entity.setSecurityFileStoreType(SecurityFileStoreType.META_DB);
            // trustStore file
            String sslTrustStoreFileName = UUID.randomUUID() + "-" + addDsFO.getSecurityFile().getOriginalFilename();
            String sslTrustStoreFilePath = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), sslTrustStoreFileName);
            entity.setSecurityFileUrl(sslTrustStoreFilePath);
            if (StringUtils.isNotBlank(addDsFO.getClientTrustStorePassword())) {
                // ClientTrustStorePassword is used in Kafka/AutoMQ/Tunnel, and now TLS authentication has switched to using (trustStore/keyStore - securityFilePassword/clientSecurityFilePassword)
                entity.setClientTrustStorePassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getClientTrustStorePassword()));
            }
            if (StringUtils.isNotBlank(addDsFO.getSecurityFilePassword())) {
                // trustStore file password
                entity.setSecurityFilePassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getSecurityFilePassword()));
            }

            if (addDsFO.getClientSecurityFile() != null) {
                // keystore file
                String keystoreFileName = UUID.randomUUID() + "-" + addDsFO.getClientSecurityFile().getOriginalFilename();
                String clientSecurityFileUrl = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), keystoreFileName);
                entity.setClientSecurityFileUrl(clientSecurityFileUrl);
            }
            if (StringUtils.isNotBlank(addDsFO.getClientSecurityFilePassword())) {
                // keystore file password
                entity.setClientSecurityFilePassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getClientSecurityFilePassword()));
            }
        } else if (entity.getSecurityType() == SecurityType.CA_CERTIFICATE) {
            if (addDsFO.getSecurityFile() == null) {
                throw new IllegalArgumentException("datasource security type is plain with ca certificate,but xxx.crt file is empty.");
            }

            entity.setSecurityFileStoreType(SecurityFileStoreType.META_DB);
            String caCertificateFileName = UUID.randomUUID() + "-" + addDsFO.getSecurityFile().getOriginalFilename();
            String caCertificateFilePath = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), caCertificateFileName);
            entity.setSecurityFileUrl(caCertificateFilePath);
            if (addDsFO.getClientSecurityFile() != null) {
                String filename = UUID.randomUUID() + "-" + addDsFO.getClientSecurityFile().getOriginalFilename();
                String clientSecurityFileUrl = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), filename);
                entity.setClientSecurityFileUrl(clientSecurityFileUrl);
            }
            if (addDsFO.getSecretFile() != null) {
                String filename = UUID.randomUUID() + "-" + addDsFO.getSecretFile().getOriginalFilename();
                String secretFileUrl = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), filename);
                entity.setSecretFileUrl(secretFileUrl);
            }
            if (StringUtils.isNotBlank(addDsFO.getSecretFilePassword())) {
                entity.setSecretFilePassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getSecretFilePassword()));
            }
        } else if (entity.getSecurityType() == SecurityType.USER_PASSWD_WITH_KEYSTORE) {
            if (addDsFO.getSecurityFile() == null) {
                throw new IllegalArgumentException("datasource security type is plain with ssl,but .keystore file is empty.");
            }

            entity.setSecurityFileStoreType(SecurityFileStoreType.META_DB);
            String keyStoreFileName = UUID.randomUUID() + "-" + addDsFO.getSecurityFile().getOriginalFilename();
            String keyStoreFilePath = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), keyStoreFileName);
            entity.setSecurityFileUrl(keyStoreFilePath);
            if (addDsFO.getClientTrustStorePassword() != null) {
                entity.setClientTrustStorePassword(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(addDsFO.getClientTrustStorePassword()));
            }
        }

        if (Arrays.asList(DataSourceType.Redis, DataSourceType.ElastiCache).contains(entity.getDataSourceType()) && addDsFO.getSecurityFile() != null) {
            // Redis datasource with ca certificate file
            entity.setSecurityFileStoreType(SecurityFileStoreType.META_DB);
            String caCertificateFilePath = this.rdpSecurityService.genSecurityFileRelatePath(entity.getInstanceId(), addDsFO.getSecurityFile().getOriginalFilename());
            entity.setSecurityFileUrl(caCertificateFilePath);
            // redis security type never be CA_CERTIFICATE, but all types of redis config can have ca_certificate_file
            saveDsSecurityFile(addDsFO.getSecurityFile(), entity, SecurityFileType.ca_certificate_file);
        }

        this.datasourceDal.dsMapper().insert(entity);

        if (addDsFO.getDsKvConfigs() != null && !addDsFO.getDsKvConfigs().isEmpty()) {
            this.rdpDsConfigService.persistDsConfig(entity, addDsFO.getDsKvConfigs());
        }

        if (entity.getSecurityType() == SecurityType.KERBEROS) {
            saveDsSecurityFile(addDsFO.getSecretFile(), entity, SecurityFileType.kerberos_keytab_file);
            saveDsSecurityFile(addDsFO.getSecurityFile(), entity, SecurityFileType.kerberos_conf_file);
        } else if (entity.getSecurityType() == SecurityType.USER_PASSWD_WITH_TLS) {
            // trustStore file
            saveDsSecurityFile(addDsFO.getSecurityFile(), entity, SecurityFileType.ssl_truststore_file);
            if (addDsFO.getClientSecurityFile() != null) {
                // keystore file
                saveDsSecurityFile(addDsFO.getClientSecurityFile(), entity, SecurityFileType.ssl_keystore_file);
            }
        } else if (entity.getSecurityType() == SecurityType.CA_CERTIFICATE) {
            saveDsSecurityFile(addDsFO.getSecurityFile(), entity, SecurityFileType.ca_certificate_file);
            if (addDsFO.getClientSecurityFile() != null) {
                saveDsSecurityFile(addDsFO.getClientSecurityFile(), entity, SecurityFileType.client_certificate_file);
            }
            if (addDsFO.getSecretFile() != null) {
                saveDsSecurityFile(addDsFO.getSecretFile(), entity, SecurityFileType.secret_file);
            }
        } else if (entity.getSecurityType() == SecurityType.USER_PASSWD_WITH_KEYSTORE) {
            saveDsSecurityFile(addDsFO.getSecurityFile(), entity, SecurityFileType.keystore_file);
        }

        return entity.getId();
    }

    protected void saveDsSecurityFile(MultipartFile file, DmDsDO entity, SecurityFileType securityFileType) {
        if (file == null) {
            throw new IllegalArgumentException("datasource security file is null.security file type:" + securityFileType);
        }

        try {
            DmDsBlobResourceDO resourceDO = new DmDsBlobResourceDO();
            resourceDO.setBlobType(securityFileType);
            resourceDO.setContent(file.getBytes());
            resourceDO.setInstanceId(entity.getInstanceId());
            resourceDO.setOwnerName(entity.getInstanceId());
            resourceDO.setOwnerType(ResourceType.DATASOURCE);
            this.datasourceDal.blobResourceMapper().insert(resourceDO);
        } catch (IOException e) {
            String msg = "read security file from stream error.datasource instance id:" + entity.getInstanceId() + ".msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
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
        this.rdpDsConfigService.cleanDsConfig(dsId);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.kerberos_conf_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.kerberos_keytab_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.ssl_truststore_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.ssl_keystore_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.jaas_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.keystore_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.ca_certificate_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.client_certificate_file);
        this.datasourceDal.blobResourceMapper().deleteByIdentify(userDs.getInstanceId(), ResourceType.DATASOURCE, SecurityFileType.secret_file);

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
        //        re.setExtraDO(dataSourceExtraMapper.queryByDataSourceId(re.getId()));
        re.setDsExtraConfig(this.rdpDsConfigService.fetchDsExtraConfig(re.getId(), re.getDataSourceType()));

        if (envMap != null && envMap.containsKey(re.getDsEnvId())) {
            re.setDsEnvDO(envMap.get(re.getDsEnvId()));
        }
    }

    protected String genInstanceId(DataSourceType dataSourceType) {
        return dataSourceType.getShortName() + "-" + RandomStrUtils.fixedLenRandomStr(15);
    }
}

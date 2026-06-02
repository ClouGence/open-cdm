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
package com.clougence.clouddm.console.web.component.config;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.UpsertUserConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpsertUserConfigLO;
import com.clougence.clouddm.console.web.model.vo.RdpUserConfigVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserConfigHelper;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.global.config.user.SubAccountConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/1/10 20:28:25
 */
@Service
@Slf4j
public class UserConfigServiceImpl implements UserConfigService {

    private static final int       DEFAULT_LANGUAGE_MAX_REQUESTS         = 8;
    private static final int       DEFAULT_LANGUAGE_MAX_REQUESTS_BY_USER = 4;

    @Resource
    private SystemDal              systemDal;
    @Resource
    private AuthDal                authDal;
    @Resource
    private RdpUserConfigHelper    rdpUserConfigHelper;
    @Resource
    private DmConsoleConfig        rdpConfig;
    @Resource
    private List<RdpNotifyService> notifyServices;

    @Override
    public List<RdpUserConfigVO> queryUserConfigVosWithNewEntries(String uid) {
        List<DmSysUserConfDO> configs = this.systemDal.userConfMapper().listByUid(uid);
        Map<String, DmSysUserConfDO> configMap = new HashMap<>();
        for (DmSysUserConfDO configDO : configs) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DmSysUserConfDO> defaultConfigs = fetchUserDefinedDefaultConfig(uid);

        Set<String> userConfigBlack;
        if (StringUtils.isNotBlank(this.rdpConfig.getUserConfigBlacklist())) {
            userConfigBlack = Arrays.stream(this.rdpConfig.getUserConfigBlacklist().split(",")).collect(Collectors.toSet());
        } else {
            userConfigBlack = new HashSet<>();
        }

        List<RdpUserConfigVO> resultConfigs = new ArrayList<>();
        for (DmSysUserConfDO configDO : defaultConfigs) {
            if (userConfigBlack.contains(configDO.getConfigName())) {
                continue;
            }
            DmSysUserConfDO config = configMap.get(configDO.getConfigName());
            RdpUserConfigVO v = new RdpUserConfigVO();
            if (config == null) {
                v.convertFromDO(configDO);
                v.setNeedCreated(true);
                resultConfigs.add(v);
            } else {
                configDO.setConfigValue(config.getConfigValue());
                v.convertFromDO(configDO);
                resultConfigs.add(v);
            }
        }

        return resultConfigs;
    }

    @Override
    public Map<String, RdpUserConfigVO> queryWithNewEntriesAndSpecifiedConfs(String uid, List<String> configNames) {
        List<DmSysUserConfDO> configs = this.systemDal.userConfMapper().listByUidAndConfigNames(uid, configNames);
        Map<String, DmSysUserConfDO> configMap = new HashMap<>();
        for (DmSysUserConfDO configDO : configs) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<DmSysUserConfDO> defaultConfigs = fetchUserDefinedDefaultConfig(uid);

        Set<String> userConfigBlack;
        if (StringUtils.isNotBlank(this.rdpConfig.getUserConfigBlacklist())) {
            userConfigBlack = Arrays.stream(this.rdpConfig.getUserConfigBlacklist().split(",")).collect(Collectors.toSet());
        } else {
            userConfigBlack = new HashSet<>();
        }

        Map<String, RdpUserConfigVO> resultConfigs = new HashMap<>();
        for (DmSysUserConfDO configDO : defaultConfigs) {
            if (userConfigBlack.contains(configDO.getConfigName())) {
                continue;
            }

            if (!configNames.contains(configDO.getConfigName())) {
                continue;
            }

            DmSysUserConfDO config = configMap.get(configDO.getConfigName());
            RdpUserConfigVO v = new RdpUserConfigVO();
            if (config == null) {
                v.convertFromDO(configDO);
                v.setNeedCreated(true);
                resultConfigs.put(configDO.getConfigName(), v);
            } else {
                configDO.setConfigValue(config.getConfigValue());
                v.convertFromDO(configDO);
                resultConfigs.put(configDO.getConfigName(), v);
            }
        }

        return resultConfigs;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public List<UpsertUserConfigLO> upsertConfigValue(String ownerUid, UpsertUserConfigFO config) {
        List<UserConfigMO> configList = new ArrayList<>();
        List<UpsertUserConfigLO> configLOs = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(config.getUpdateConfigs())) {
            for (Map.Entry<String, String> configEntry : config.getUpdateConfigs().entrySet()) {
                String configName = configEntry.getKey();
                DmSysUserConfDO oldConfig = systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, configName);
                String newValue = configEntry.getValue();
                if (newValue != null) {
                    newValue = newValue.trim();
                }

                if (oldConfig.isSecret() && StringUtils.isNotBlank(newValue)) {
                    newValue = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newValue);
                }

                UpsertUserConfigLO configLO = new UpsertUserConfigLO();
                configLO.setConfigName(configName);
                configLO.setNeedCreate(false);
                if (!oldConfig.isSecret()) {
                    configLO.setOldConfigValue(oldConfig.getConfigValue());
                    configLO.setConfigValue(newValue);
                }
                configLOs.add(configLO);

                UserConfigMO configMO = new UserConfigMO();
                configMO.setConfig(configName);
                configMO.setOldValue(oldConfig.getConfigValue());
                configMO.setNewValue(newValue);
                configMO.setDefaultValue(oldConfig.getDefaultValue());
                configMO.setTagType(oldConfig.getUserConfigTagType());
                configMO.setInsert(false);
                configMO.setUpdate(true);
                configMO.setDelete(false);
                configList.add(configMO);

                systemDal.userConfMapper().updateUserConfig(ownerUid, configName, newValue);
            }
        }

        if (CollectionUtils.isNotEmpty(config.getNeedCreateConfigs())) {
            List<DmSysUserConfDO> defaultConfigs = fetchUserDefinedDefaultConfig(ownerUid);
            for (Map.Entry<String, String> configEntry : config.getNeedCreateConfigs().entrySet()) {
                String configName = configEntry.getKey();
                DmSysUserConfDO configInDb = systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, configName);
                // if config already exists, skip
                if (configInDb != null) {
                    continue;
                }

                DmSysUserConfDO defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(configName)).findFirst().orElse(null);
                // if config not exists in default config, skip
                if (defaultConfig == null) {
                    continue;
                }

                String newValue = configEntry.getValue();
                if (newValue != null) {
                    newValue = newValue.trim();
                }

                UpsertUserConfigLO configLO = new UpsertUserConfigLO();
                configLO.setConfigName(configName);
                configLO.setNeedCreate(true);
                if (defaultConfig.isSecret()) {
                    defaultConfig.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newValue));
                } else {
                    defaultConfig.setConfigValue(newValue);
                    configLO.setConfigValue(newValue);
                }
                configLOs.add(configLO);

                UserConfigMO configMO = new UserConfigMO();
                configMO.setConfig(configName);
                configMO.setOldValue(null);
                configMO.setNewValue(newValue);
                configMO.setDefaultValue(defaultConfig.getDefaultValue());
                configMO.setTagType(defaultConfig.getUserConfigTagType());
                configMO.setInsert(true);
                configMO.setUpdate(false);
                configMO.setDelete(false);
                configList.add(configMO);

                systemDal.userConfMapper().insert(defaultConfig);
            }
        }

        this.notifyServices.forEach(s -> s.notifyUserConfig(ownerUid, configList));

        return configLOs;
    }

    public List<DmSysUserConfDO> fetchUserDefinedDefaultConfig(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        boolean isPrimary = userDO != null && (userDO.getParentId() == null || userDO.getParentId() <= 0);
        if (isPrimary) {
            return rdpUserConfigHelper.collectConfigs(new UserDefinedConfig(), uid);
        } else {
            return rdpUserConfigHelper.collectConfigs(new SubAccountConfig(), uid);
        }
    }

    @Override
    public List<DmSysUserConfDO> getSpecifiedConfigs(String uid, List<String> configNames) {
        List<DmSysUserConfDO> configs = systemDal.userConfMapper().listByUidAndConfigNames(uid, configNames);
        if (configs != null) {
            for (DmSysUserConfDO configDO : configs) {
                if (configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
                    String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
                    configDO.setConfigValue(val);
                }
            }
        }

        return configs;
    }

    @Override
    public DmSysUserConfDO getSpecifiedConfig(String uid, String configName) {
        DmSysUserConfDO configDO = systemDal.userConfMapper().queryByUidAndConfigName(uid, configName);
        if (configDO != null && configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
            String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
            configDO.setConfigValue(val);
        }

        return configDO;
    }

    @Override
    public int languageMaxRequests(String uid) {
        return intConfig(uid, UserDefinedConfig.Fields.languageMaxRequests, DEFAULT_LANGUAGE_MAX_REQUESTS);
    }

    @Override
    public int languageMaxRequestsByUser(String uid) {
        return intConfig(uid, UserDefinedConfig.Fields.languageMaxRequestsByUser, DEFAULT_LANGUAGE_MAX_REQUESTS_BY_USER);
    }

    private int intConfig(String uid, String configName, int defaultValue) {
        if (StringUtils.isBlank(uid)) {
            return defaultValue;
        }

        DmSysUserConfDO config = getSpecifiedConfig(uid, configName);
        String configValue = config == null ? null : config.getConfigValue();
        if (StringUtils.isBlank(configValue) && config != null) {
            configValue = config.getDefaultValue();
        }
        try {
            int value = Integer.parseInt(StringUtils.isBlank(configValue) ? String.valueOf(defaultValue) : configValue.trim());
            return Math.max(1, value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void initSubAccountConfigs(String uid) {
        SubAccountConfig config = new SubAccountConfig();
        List<DmSysUserConfDO> dos = rdpUserConfigHelper.collectConfigs(config, uid);
        insertConfigDOs(dos);
    }

    protected void insertConfigDOs(List<DmSysUserConfDO> dos) {
        for (DmSysUserConfDO obj : dos) {
            if (obj.isSecret() && StringUtils.isNotBlank(obj.getConfigValue())) {
                String val = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(obj.getConfigValue());
                obj.setConfigValue(val);
            }

            systemDal.userConfMapper().insert(obj);
        }
    }

    protected List<RdpUserConfigVO> convertToVO(List<DmSysUserConfDO> configs) {
        List<RdpUserConfigVO> userConfigs = new ArrayList<>();
        for (DmSysUserConfDO config : configs) {
            RdpUserConfigVO configVO = new RdpUserConfigVO();
            configVO.convertFromDO(config);
            userConfigs.add(configVO);
        }

        return userConfigs;
    }
}

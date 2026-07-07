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

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.console.web.model.fo.UpsertUserConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpsertUserConfigLO;
import com.clougence.clouddm.console.web.model.vo.RdpUserConfigVO;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
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
    private ConsoleConfig rdpConfig;
    @Resource
    private List<RdpNotifyService> notifyServices;

    @Override
    public List<RdpUserConfigVO> queryUserConfigVosWithNewEntries(String uid) {
        List<DmSysUserConfDO> configs = this.systemDal.userConfMapper().listByUid(uid);
        Map<String, DmSysUserConfDO> configMap = new HashMap<>();
        for (DmSysUserConfDO configDO : configs) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<UserConfigKvDef> defaultConfigs = fetchUserDefinedDefaultConfig(uid);

        Set<String> userConfigBlack;
        if (StringUtils.isNotBlank(this.rdpConfig.getUserConfigBlacklist())) {
            userConfigBlack = Arrays.stream(this.rdpConfig.getUserConfigBlacklist().split(",")).collect(Collectors.toSet());
        } else {
            userConfigBlack = new HashSet<>();
        }

        List<RdpUserConfigVO> resultConfigs = new ArrayList<>();
        for (UserConfigKvDef configDO : defaultConfigs) {
            if (userConfigBlack.contains(configDO.getConfigName())) {
                continue;
            }
            DmSysUserConfDO config = configMap.get(configDO.getConfigName());
            RdpUserConfigVO v;
            if (config == null) {
                v = RdpConvertUtils.convertToUserConfigVO(configDO);
                v.setNeedCreated(true);
                resultConfigs.add(v);
            } else {
                v = RdpConvertUtils.convertToUserConfigVO(configDO, config);
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

        List<UserConfigKvDef> defaultConfigs = fetchUserDefinedDefaultConfig(uid);

        Set<String> userConfigBlack;
        if (StringUtils.isNotBlank(this.rdpConfig.getUserConfigBlacklist())) {
            userConfigBlack = Arrays.stream(this.rdpConfig.getUserConfigBlacklist().split(",")).collect(Collectors.toSet());
        } else {
            userConfigBlack = new HashSet<>();
        }

        Map<String, RdpUserConfigVO> resultConfigs = new HashMap<>();
        for (UserConfigKvDef configDO : defaultConfigs) {
            if (userConfigBlack.contains(configDO.getConfigName())) {
                continue;
            }

            if (!configNames.contains(configDO.getConfigName())) {
                continue;
            }

            DmSysUserConfDO config = configMap.get(configDO.getConfigName());
            RdpUserConfigVO v;
            if (config == null) {
                v = RdpConvertUtils.convertToUserConfigVO(configDO);
                v.setNeedCreated(true);
                resultConfigs.put(configDO.getConfigName(), v);
            } else {
                v = RdpConvertUtils.convertToUserConfigVO(configDO, config);
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
        List<UserConfigKvDef> defaultConfigs = fetchUserDefinedDefaultConfig(ownerUid);

        if (CollectionUtils.isNotEmpty(config.getUpdateConfigs())) {
            for (Map.Entry<String, String> configEntry : config.getUpdateConfigs().entrySet()) {
                String configName = configEntry.getKey();
                DmSysUserConfDO oldConfig = systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, configName);
                UserConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> {
                    return c.getConfigName().equals(configName);
                }).findFirst().orElse(null);
                if (oldConfig == null || defaultConfig == null) {
                    continue;
                }
                String newValue = configEntry.getValue();
                if (newValue != null) {
                    newValue = newValue.trim();
                }

                if (defaultConfig.isSecret() && StringUtils.isNotBlank(newValue)) {
                    newValue = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newValue);
                }

                UpsertUserConfigLO configLO = new UpsertUserConfigLO();
                configLO.setConfigName(configName);
                configLO.setNeedCreate(false);
                if (!defaultConfig.isSecret()) {
                    configLO.setOldConfigValue(oldConfig.getConfigValue());
                    configLO.setConfigValue(newValue);
                }
                configLOs.add(configLO);

                UserConfigMO configMO = new UserConfigMO();
                configMO.setConfig(configName);
                configMO.setOldValue(oldConfig.getConfigValue());
                configMO.setNewValue(newValue);
                configMO.setDefaultValue(defaultConfig.getDefaultValue());
                configMO.setTagType(defaultConfig.getUserConfigTagType());
                configMO.setInsert(false);
                configMO.setUpdate(true);
                configMO.setDelete(false);
                configList.add(configMO);

                systemDal.userConfMapper().updateUserConfig(ownerUid, configName, newValue);
            }
        }

        if (CollectionUtils.isNotEmpty(config.getNeedCreateConfigs())) {
            for (Map.Entry<String, String> configEntry : config.getNeedCreateConfigs().entrySet()) {
                String configName = configEntry.getKey();
                DmSysUserConfDO configInDb = systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, configName);
                // if config already exists, skip
                if (configInDb != null) {
                    continue;
                }

                UserConfigKvDef defaultConfig = defaultConfigs.stream().filter(c -> {
                    return c.getConfigName().equals(configName);
                }).findFirst().orElse(null);
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
                    newValue = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newValue);
                } else {
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

                DmSysUserConfDO newConfig = new DmSysUserConfDO();
                newConfig.setUid(ownerUid);
                newConfig.setConfigName(defaultConfig.getConfigName());
                newConfig.setConfigValue(newValue);
                systemDal.userConfMapper().insert(newConfig);
            }
        }

        this.notifyServices.forEach(s -> s.notifyUserConfig(ownerUid, configList));

        return configLOs;
    }

    public List<UserConfigKvDef> fetchUserDefinedDefaultConfig(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        boolean isPrimary = userDO != null && (userDO.getParentId() == null || userDO.getParentId() <= 0);
        if (isPrimary) {
            return collectConfigs(new RootUserConfig(), uid);
        } else {
            return collectConfigs(new NormalUserConfig(), uid);
        }
    }

    @Override
    public List<DmSysUserConfDO> getSpecifiedConfigs(String uid, List<String> configNames) {
        List<DmSysUserConfDO> configs = systemDal.userConfMapper().listByUidAndConfigNames(uid, configNames);
        if (configs != null) {
            Map<String, DmSysUserConfDO> configMap = new HashMap<>();
            for (DmSysUserConfDO configDO : configs) {
                configMap.put(configDO.getConfigName(), configDO);
            }

            List<DmSysUserConfDO> normalizedConfigs = new ArrayList<>();
            for (String configName : configNames) {
                DmSysUserConfDO configDO = configMap.get(configName);
                if (configDO == null) {
                    continue;
                }
                configDO.setConfigName(configName);
                normalizedConfigs.add(configDO);
            }
            return normalizedConfigs;
        }

        return Collections.emptyList();
    }

    @Override
    public DmSysUserConfDO getSpecifiedConfig(String uid, String configName) {
        return systemDal.userConfMapper().queryByUidAndConfigName(uid, configName);
    }

    @Override
    public int languageMaxRequests() {
        return Math.max(1, this.systemDal.fetchSystemConf(RootUserConfig.Fields.languageMaxRequests, Integer.class, DEFAULT_LANGUAGE_MAX_REQUESTS));
    }

    @Override
    public int languageMaxRequests(String uid) {
        int systemLimit = languageMaxRequests();
        if (StringUtils.isBlank(uid)) {
            return systemLimit;
        }

        Integer value = this.systemDal.fetchUserConf(uid, RootUserConfig.Fields.languageMaxRequestsByUser, Integer.class, DEFAULT_LANGUAGE_MAX_REQUESTS_BY_USER);
        int userLimit = Math.max(1, value);
        return Math.min(systemLimit, userLimit);
    }

    @Override
    public void initSubAccountConfigs(String uid) {
        NormalUserConfig config = new NormalUserConfig();
        List<UserConfigKvDef> defs = collectConfigs(config, uid);
        insertConfigDefs(defs);
    }

    private List<UserConfigKvDef> collectConfigs(Object instance, String uid) {
        List<UserConfigKvDef> configs = new ArrayList<>();
        collectConfigs(instance, uid, instance.getClass(), configs);
        return configs;
    }

    private void collectConfigs(Object instance, String uid, Class<?> clazz, List<UserConfigKvDef> configs) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                UserConfigDef configDef = field.getAnnotation(UserConfigDef.class);
                if (configDef == null) {
                    continue;
                }

                String val = configDef.defaultValue();
                Object oriVal = field.get(instance);
                if (oriVal != null) {
                    val = String.valueOf(oriVal);
                }

                configs.add(genConfigDef(configDef, val, uid));
            }

            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                collectConfigs(instance, uid, clazz.getSuperclass(), configs);
            }
        } catch (Exception e) {
            String msg = "collect field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private UserConfigKvDef genConfigDef(UserConfigDef configDef, String val, String uid) {
        UserConfigKvDef config = new UserConfigKvDef();
        config.setConfigName(configDef.name());
        config.setConfigValue(val);
        config.setUid(uid);
        config.setValueRange(configDef.valueRange());
        config.setUserConfigTagType(configDef.configTagType());
        config.setConfBelong(configDef.confBelong());
        config.setConfValType(configDef.kvConfWebOp());

        config.setDefaultValue(configDef.defaultValue());
        config.setReadOnly(configDef.readOnly());
        config.setSecret(configDef.isSecret());
        config.setDescKey(configDef.descKey().name());

        return config;
    }

    protected void insertConfigDefs(List<UserConfigKvDef> defs) {
        for (UserConfigKvDef obj : defs) {
            DmSysUserConfDO configDO = new DmSysUserConfDO();
            configDO.setUid(obj.getUid());
            configDO.setConfigName(obj.getConfigName());
            configDO.setConfigValue(obj.getConfigValue());
            if (obj.isSecret() && StringUtils.isNotBlank(obj.getConfigValue())) {
                String val = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(obj.getConfigValue());
                configDO.setConfigValue(val);
            }

            systemDal.userConfMapper().insert(configDO);
        }
    }

    protected List<RdpUserConfigVO> convertToVO(List<DmSysUserConfDO> configs) {
        List<RdpUserConfigVO> userConfigs = new ArrayList<>();
        for (DmSysUserConfDO config : configs) {
            userConfigs.add(RdpConvertUtils.convertToUserConfigVO(config));
        }

        return userConfigs;
    }
}

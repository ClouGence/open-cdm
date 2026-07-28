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

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bucketli 2022/1/10 20:28:25
 */
@Service
@Slf4j
public class UserConfigServiceImpl implements UserConfigService {

    private static final int DEFAULT_LANGUAGE_MAX_REQUESTS = 50;
    private static final int DEFAULT_LANGUAGE_MAX_REQUESTS_BY_USER = 2;

    @Resource
    private SystemDal systemDal;
    @Resource
    private AuthDal authDal;
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
        Map<String, UserConfigKvDef> defaultConfigMap = defaultConfigs.stream().collect(Collectors.toMap(UserConfigKvDef::getConfigName, c -> c));

        validateRequestedConfigs(config, defaultConfigMap);

        if (CollectionUtils.isNotEmpty(config.getUpdateConfigs())) {
            for (Map.Entry<String, String> configEntry : config.getUpdateConfigs().entrySet()) {
                upsertOneConfig(ownerUid, configEntry.getKey(), configEntry.getValue(), defaultConfigMap, configList, configLOs);
            }
        }

        if (CollectionUtils.isNotEmpty(config.getNeedCreateConfigs())) {
            for (Map.Entry<String, String> configEntry : config.getNeedCreateConfigs().entrySet()) {
                upsertOneConfig(ownerUid, configEntry.getKey(), configEntry.getValue(), defaultConfigMap, configList, configLOs);
            }
        }

        this.notifyServices.forEach(s -> s.notifyUserConfig(ownerUid, configList));

        return configLOs;
    }

    private void validateRequestedConfigs(UpsertUserConfigFO config, Map<String, UserConfigKvDef> defaultConfigMap) {
        Map<String, String> updateConfigs = config.getUpdateConfigs();
        Map<String, String> createConfigs = config.getNeedCreateConfigs();

        if (CollectionUtils.isNotEmpty(updateConfigs) && CollectionUtils.isNotEmpty(createConfigs)) {
            Set<String> duplicateKeys = new HashSet<>(updateConfigs.keySet());
            duplicateKeys.retainAll(createConfigs.keySet());
            if (!duplicateKeys.isEmpty()) {
                String configName = duplicateKeys.iterator().next();
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_DUPLICATE_ERROR.name(), configName));
            }
        }

        if (CollectionUtils.isNotEmpty(updateConfigs)) {
            updateConfigs.forEach((configName, configValue) -> validateConfigValue(defaultConfigMap, configName, configValue));
        }
        if (CollectionUtils.isNotEmpty(createConfigs)) {
            createConfigs.forEach((configName, configValue) -> validateConfigValue(defaultConfigMap, configName, configValue));
        }
    }

    private void validateConfigValue(Map<String, UserConfigKvDef> defaultConfigMap, String configName, String configValue) {
        UserConfigKvDef configDef = defaultConfigMap.get(configName);
        if (configDef == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_UNKNOWN_ERROR.name(), configName));
        }
        if (configDef.isReadOnly()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_READ_ONLY_ERROR.name(), configName));
        }

        String value = configValue;
        if (value != null) {
            value = value.trim();
        }
        if (configDef.isRequired() && StringUtils.isBlank(value)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_REQUIRED_ERROR.name(), configName));
        }

        Class<?> valueType = configDef.getValueType();
        if (Number.class.isAssignableFrom(valueType)) {
            validateNumberConfig(configDef, value);
        } else if (valueType == Boolean.class && !"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_BOOLEAN_ERROR.name(), configName, value));
        }

        List<String> allowedValues = configDef.getAllowedValues();
        if (CollectionUtils.isNotEmpty(allowedValues) && !allowedValues.contains(value)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_ALLOWED_VALUES_ERROR.name(), configName, String.join(", ", allowedValues)));
        }
    }

    private void validateNumberConfig(UserConfigKvDef configDef, String value) {
        long number;
        try {
            number = Long.parseLong(value);
        } catch (Exception e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_NEED_NUMBER_ERROR.name(), configDef.getConfigName(), value));
        }

        long minValue = configDef.getMinValue();
        long maxValue = configDef.getMaxValue();
        if (configDef.getValueType() == Integer.class) {
            minValue = Math.max(minValue, Integer.MIN_VALUE);
            maxValue = Math.min(maxValue, Integer.MAX_VALUE);
        }
        if (number < minValue || number > maxValue) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_VERIFICATION_ERROR.name(), configDef.getConfigName()));
        }
    }

    private void upsertOneConfig(String ownerUid, String configName, String configValue, Map<String, UserConfigKvDef> defaultConfigMap, List<UserConfigMO> configList,
                                 List<UpsertUserConfigLO> configLOs) {
        UserConfigKvDef defaultConfig = defaultConfigMap.get(configName);

        DmSysUserConfDO oldConfig = systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, configName);
        String newValue = configValue;
        if (newValue != null) {
            newValue = newValue.trim();
        }

        if (oldConfig == null) {
            insertConfig(ownerUid, defaultConfig, newValue, configList, configLOs);
        } else {
            updateConfig(ownerUid, oldConfig, defaultConfig, newValue, configList, configLOs);
        }
    }

    private void updateConfig(String ownerUid, DmSysUserConfDO oldConfig, UserConfigKvDef defaultConfig, String newValue, List<UserConfigMO> configList,
                              List<UpsertUserConfigLO> configLOs) {
        String configName = defaultConfig.getConfigName();
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

    private void insertConfig(String ownerUid, UserConfigKvDef defaultConfig, String newValue, List<UserConfigMO> configList, List<UpsertUserConfigLO> configLOs) {
        String configName = defaultConfig.getConfigName();
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
        newConfig.setConfigName(configName);
        newConfig.setConfigValue(newValue);
        systemDal.userConfMapper().insert(newConfig);
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

                configs.add(genConfigDef(configDef, val, uid, field.getType()));
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

    private UserConfigKvDef genConfigDef(UserConfigDef configDef, String val, String uid, Class<?> valueType) {
        UserConfigKvDef config = new UserConfigKvDef();
        config.setConfigName(configDef.name());
        config.setConfigValue(val);
        config.setUid(uid);
        config.setValueRange(configDef.valueRange());
        config.setMinValue(configDef.minValue());
        config.setMaxValue(configDef.maxValue());
        config.setAllowedValues(Arrays.asList(configDef.allowedValues()));
        config.setRequired(configDef.required());
        config.setValueType(valueType);
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

package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.controller.model.fo.UpsertUserConfigFO;
import com.clougence.rdp.controller.model.lo.UpsertUserConfigLO;
import com.clougence.rdp.controller.model.vo.RdpUserConfigVO;
import com.clougence.rdp.dal.mapper.RdpUserKvBaseConfigMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.config.user.SubAccountConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.RdpUserConfigHelper;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/1/10 20:28:25
 */
@Service
@Slf4j
public class RdpUserConfigServiceImpl implements RdpUserConfigService {

    @Resource
    private RdpUserKvBaseConfigMapper rdpUserKvBaseConfigMapper;

    @Resource
    private RdpUserConfigHelper       rdpUserConfigHelper;

    @Resource
    private RdpUserMapper             rdpUserMapper;

    @Resource
    private RdpConsoleConfig          rdpConfig;

    @Resource
    private List<RdpNotifyService>    notifyServices;

    @Override
    public UserDefinedConfig fetchPriUserConfig(String uid) {
        List<RdpUserKvBaseConfigDO> configs = rdpUserKvBaseConfigMapper.listByUid(uid);

        Map<String, String> configMap = new HashMap<>();
        if (configs != null && !configs.isEmpty()) {
            configs.forEach(kvBaseConfigDO -> configMap.put(kvBaseConfigDO.getConfigName(), kvBaseConfigDO.getConfigValue()));
        }

        UserDefinedConfig config = new UserDefinedConfig();
        rdpUserConfigHelper.fillFieldValue(config, configMap);
        return config;
    }

    @Override
    public List<RdpUserConfigVO> getAllConfig(String uid) {
        List<RdpUserKvBaseConfigDO> configs = rdpUserKvBaseConfigMapper.listByUid(uid);
        for (RdpUserKvBaseConfigDO configDO : configs) {
            if (configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
                String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
                configDO.setConfigValue(val);
            }
        }

        return convertToVO(configs);
    }

    @Override
    public List<RdpUserConfigVO> queryUserConfigVosWithNewEntries(String uid) {
        List<RdpUserKvBaseConfigDO> configs = this.rdpUserKvBaseConfigMapper.listByUid(uid);
        Map<String, RdpUserKvBaseConfigDO> configMap = new HashMap<>();
        for (RdpUserKvBaseConfigDO configDO : configs) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<RdpUserKvBaseConfigDO> defaultConfigs = fetchUserDefinedDefaultConfig(uid);

        Set<String> userConfigBlack;
        if (StringUtils.isNotBlank(this.rdpConfig.getUserConfigBlacklist())) {
            userConfigBlack = Arrays.stream(this.rdpConfig.getUserConfigBlacklist().split(",")).collect(Collectors.toSet());
        } else {
            userConfigBlack = new HashSet<>();
        }

        List<RdpUserConfigVO> resultConfigs = new ArrayList<>();
        for (RdpUserKvBaseConfigDO configDO : defaultConfigs) {
            if (userConfigBlack.contains(configDO.getConfigName())) {
                continue;
            }
            RdpUserKvBaseConfigDO config = configMap.get(configDO.getConfigName());
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
        List<RdpUserKvBaseConfigDO> configs = this.rdpUserKvBaseConfigMapper.listByUidAndConfigNames(uid, configNames);
        Map<String, RdpUserKvBaseConfigDO> configMap = new HashMap<>();
        for (RdpUserKvBaseConfigDO configDO : configs) {
            configMap.put(configDO.getConfigName(), configDO);
        }

        List<RdpUserKvBaseConfigDO> defaultConfigs = fetchUserDefinedDefaultConfig(uid);

        Set<String> userConfigBlack;
        if (StringUtils.isNotBlank(this.rdpConfig.getUserConfigBlacklist())) {
            userConfigBlack = Arrays.stream(this.rdpConfig.getUserConfigBlacklist().split(",")).collect(Collectors.toSet());
        } else {
            userConfigBlack = new HashSet<>();
        }

        Map<String, RdpUserConfigVO> resultConfigs = new HashMap<>();
        for (RdpUserKvBaseConfigDO configDO : defaultConfigs) {
            if (userConfigBlack.contains(configDO.getConfigName())) {
                continue;
            }

            if (!configNames.contains(configDO.getConfigName())) {
                continue;
            }

            RdpUserKvBaseConfigDO config = configMap.get(configDO.getConfigName());
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
                RdpUserKvBaseConfigDO oldConfig = rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, configName);
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

                rdpUserKvBaseConfigMapper.updateUserConfig(ownerUid, configName, newValue);
            }
        }

        if (CollectionUtils.isNotEmpty(config.getNeedCreateConfigs())) {
            List<RdpUserKvBaseConfigDO> defaultConfigs = fetchUserDefinedDefaultConfig(ownerUid);
            for (Map.Entry<String, String> configEntry : config.getNeedCreateConfigs().entrySet()) {
                String configName = configEntry.getKey();
                RdpUserKvBaseConfigDO configInDb = rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, configName);
                // if config already exists, skip
                if (configInDb != null) {
                    continue;
                }

                RdpUserKvBaseConfigDO defaultConfig = defaultConfigs.stream().filter(c -> c.getConfigName().equals(configName)).findFirst().orElse(null);
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

                rdpUserKvBaseConfigMapper.insert(defaultConfig);
            }
        }

        this.notifyServices.forEach(s -> s.notifyUserConfig(ownerUid, configList));

        return configLOs;
    }

    public List<RdpUserKvBaseConfigDO> fetchUserDefinedDefaultConfig(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        boolean isPrimary = userDO != null && (userDO.getParentId() == null || userDO.getParentId() <= 0);
        if (isPrimary) {
            return rdpUserConfigHelper.collectConfigs(new UserDefinedConfig(), uid);
        } else {
            return rdpUserConfigHelper.collectConfigs(new SubAccountConfig(), uid);
        }
    }

    @Override
    public List<RdpUserKvBaseConfigDO> getSpecifiedConfigs(String uid, List<String> configNames) {
        List<RdpUserKvBaseConfigDO> configs = rdpUserKvBaseConfigMapper.listByUidAndConfigNames(uid, configNames);
        if (configs != null) {
            for (RdpUserKvBaseConfigDO configDO : configs) {
                if (configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
                    String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
                    configDO.setConfigValue(val);
                }
            }
        }

        return configs;
    }

    @Override
    public RdpUserKvBaseConfigDO getDefaultClusterName(String uid) {
        return rdpUserKvBaseConfigMapper.queryByUidAndConfigName(uid, "defaultClusterName");
    }

    @Override
    public RdpUserKvBaseConfigDO getSpecifiedConfig(String uid, String configName) {
        RdpUserKvBaseConfigDO configDO = rdpUserKvBaseConfigMapper.queryByUidAndConfigName(uid, configName);
        if (configDO != null && configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
            String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
            configDO.setConfigValue(val);
        }

        return configDO;
    }

    @Override
    public List<RdpUserConfigVO> queryOneConfigTypeByUid(String uid, UserConfigTagType type) {
        List<RdpUserKvBaseConfigDO> configs = rdpUserKvBaseConfigMapper.listOneConfigTypeByUid(uid, type);
        for (RdpUserKvBaseConfigDO configDO : configs) {
            if (configDO.isSecret() && com.clougence.utils.StringUtils.isNotBlank(configDO.getConfigValue())) {
                String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
                configDO.setConfigValue(val);
            }
        }

        return convertToVO(configs);
    }

    @Override
    public void initUserConfigs(String uid) {
        UserDefinedConfig config = new UserDefinedConfig();
        List<RdpUserKvBaseConfigDO> dos = rdpUserConfigHelper.collectConfigs(config, uid);
        insertConfigDOs(dos);
    }

    @Override
    public void initSubAccountConfigs(String uid) {
        SubAccountConfig config = new SubAccountConfig();
        List<RdpUserKvBaseConfigDO> dos = rdpUserConfigHelper.collectConfigs(config, uid);
        insertConfigDOs(dos);
    }

    protected void insertConfigDOs(List<RdpUserKvBaseConfigDO> dos) {
        for (RdpUserKvBaseConfigDO obj : dos) {
            if (obj.isSecret() && StringUtils.isNotBlank(obj.getConfigValue())) {
                String val = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(obj.getConfigValue());
                obj.setConfigValue(val);
            }

            rdpUserKvBaseConfigMapper.insert(obj);
        }
    }

    protected List<RdpUserConfigVO> convertToVO(List<RdpUserKvBaseConfigDO> configs) {
        List<RdpUserConfigVO> userConfigs = new ArrayList<>();
        for (RdpUserKvBaseConfigDO config : configs) {
            RdpUserConfigVO configVO = new RdpUserConfigVO();
            configVO.convertFromDO(config);
            userConfigs.add(configVO);
        }

        return userConfigs;
    }
}

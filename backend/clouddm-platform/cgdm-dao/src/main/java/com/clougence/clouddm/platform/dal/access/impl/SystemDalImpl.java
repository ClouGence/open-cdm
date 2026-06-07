package com.clougence.clouddm.platform.dal.access.impl;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.mapper.system.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemDalImpl implements SystemDal {

    @Resource
    private DmSysClusterMapper   clusterMapper;
    @Resource
    private DmSysConfMapper      confMapper;
    @Resource
    private DmSysEnvMapper       envMapper;
    @Resource
    private DmSysEnvParamMapper  envParamMapper;
    @Resource
    private DmSysMessengerMapper messengerMapper;
    @Resource
    private DmSysUserConfMapper  userConfMapper;
    @Resource
    private DmSysWorkerMapper    workerMapper;
    @Resource
    private AuthDal              authDal;

    @Override
    public DmSysClusterMapper clusterMapper() {
        return clusterMapper;
    }

    @Override
    public DmSysConfMapper confMapper() {
        return confMapper;
    }

    @Override
    public DmSysEnvMapper envMapper() {
        return envMapper;
    }

    @Override
    public DmSysEnvParamMapper envParamMapper() {
        return envParamMapper;
    }

    @Override
    public DmSysMessengerMapper messengerMapper() {
        return messengerMapper;
    }

    @Override
    public DmSysUserConfMapper userConfMapper() {
        return userConfMapper;
    }

    @Override
    public DmSysWorkerMapper workerMapper() {
        return workerMapper;
    }

    // ---------- dal service methods ----------

    private DmSysUserConfDO querySpecifiedConfig(String uid, String configName) {
        DmSysUserConfDO configDO = userConfMapper.queryByUidAndConfigName(uid, configName);
        if (configDO != null && configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
            String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
            configDO.setConfigValue(val);
        }
        return configDO;
    }

    @Override
    public String fetchSystemConf(String configName) {
        DmAuthUserDO rootUser = this.authDal.queryRootUser();
        String rootUid = rootUser == null ? AuthDal.ROOT_USER_UID : rootUser.getUid();
        DmSysUserConfDO config = querySpecifiedConfig(rootUid, configName);
        return config == null ? null : config.getConfigValue();
    }

    @Override
    public <T> T fetchSystemConf(String configName, Class<T> type) {
        String value = fetchSystemConf(configName);
        return convertConfigValue(configName, value, type);
    }

    @Override
    public <T> T fetchSystemConf(String configName, Class<T> type, T defaultValue) {
        T value = fetchSystemConf(configName, type);
        return value == null ? defaultValue : value;
    }

    @Override
    public String fetchUserConf(String uid, String configName) {
        if (StringUtils.isBlank(uid)) {
            return null;
        }
        DmSysUserConfDO config = querySpecifiedConfig(uid, configName);
        if (config == null) {
            return null;
        }
        return StringUtils.isBlank(config.getConfigValue()) ? config.getDefaultValue() : config.getConfigValue();
    }

    @Override
    public <T> T fetchUserConf(String uid, String configName, Class<T> type) {
        String value = fetchUserConf(uid, configName);
        return convertConfigValue(configName, value, type);
    }

    @Override
    public <T> T fetchUserConf(String uid, String configName, Class<T> type, T defaultValue) {
        T value = fetchUserConf(uid, configName, type);
        return value == null ? defaultValue : value;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T convertConfigValue(String configName, String value, Class<T> type) {
        if (type == null || type == String.class) {
            return (T) value;
        }
        if (StringUtils.isBlank(value)) {
            return null;
        }

        String trimmedValue = value.trim();
        try {
            if (type == Boolean.class || type == boolean.class) {
                return (T) ConvertUtils.toBoolean(trimmedValue.toLowerCase(Locale.ROOT), type == boolean.class);
            }
            if (type == Integer.class || type == int.class) {
                return (T) ConvertUtils.toInteger(trimmedValue, type == int.class);
            }
            if (type == Long.class || type == long.class) {
                return (T) ConvertUtils.toLong(trimmedValue, type == long.class);
            }
            if (type == Double.class || type == double.class) {
                return (T) ConvertUtils.toDouble(trimmedValue, type == double.class);
            }
            if (type == Float.class || type == float.class) {
                return (T) ConvertUtils.toFloat(trimmedValue, type == float.class);
            }
            if (type == Short.class || type == short.class) {
                return (T) ConvertUtils.toShort(trimmedValue, type == short.class);
            }
            if (type == Byte.class || type == byte.class) {
                return (T) ConvertUtils.toByte(trimmedValue, type == byte.class);
            }
            if (Enum.class.isAssignableFrom(type)) {
                return (T) Enum.valueOf((Class<? extends Enum>) type.asSubclass(Enum.class), trimmedValue);
            }
        } catch (RuntimeException e) {
            log.warn("failed to convert system config value, configName={}, type={}", configName, type.getName(), e);
            return null;
        }

        throw new IllegalArgumentException("Unsupported system config type: " + type.getName());
    }
}

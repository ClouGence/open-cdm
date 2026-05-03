package com.clougence.rdp.component.dskvconfig.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.component.dskvconfig.RdpDsKvConfigHelper;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.DsUseActualValueAsDefault;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/11/7 17:11
 */
@Service
@Slf4j
public class RdpDsKvConfigHelperImpl implements RdpDsKvConfigHelper {

    @Override
    public void fillFieldValue(Object instance, Map<String, String> configMap) {
        fillFieldValue(instance, instance.getClass(), configMap);
    }

    @Override
    public List<RdpDsKvBaseConfigDO> collectConfigs(Object instance, Long dataSourceId, DataSourceType dsType) {
        List<RdpDsKvBaseConfigDO> configs = new ArrayList<>();
        collectConfigs(instance, dataSourceId, instance.getClass(), configs);
        return configs;
    }

    protected void collectConfigs(Object instance, Long dataSourceId, Class clazz, List<RdpDsKvBaseConfigDO> configs) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                DsConfigDef configDef = field.getAnnotation(DsConfigDef.class);
                if (configDef == null) {
                    continue;
                }

                String val = configDef.defaultValue();
                Object oriVal = field.get(instance);
                if (oriVal != null) {
                    val = String.valueOf(oriVal);
                }

                RdpDsKvBaseConfigDO configDO = genConfigDo(configDef, val, dataSourceId);

                DsUseActualValueAsDefault defaultDef = field.getAnnotation(DsUseActualValueAsDefault.class);
                if (defaultDef != null) {
                    configDO.setDefaultValue(configDO.getConfigValue());
                }

                configs.add(configDO);
            }

            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                collectConfigs(instance, dataSourceId, clazz.getSuperclass(), configs);
            }
        } catch (Exception e) {
            String msg = "collect field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    protected RdpDsKvBaseConfigDO genConfigDo(DsConfigDef configDef, String val, Long dataSourceId) {
        RdpDsKvBaseConfigDO configDO = new RdpDsKvBaseConfigDO();
        configDO.setDataSourceId(dataSourceId);
        configDO.setConfigName(configDef.name());
        configDO.setConfigGroup(configDef.group());
        configDO.setDisplay(configDef.display());
        configDO.setDescKey(configDef.descKey().name());
        configDO.setValueRequire(configDef.valueRequire());
        configDO.setValueValidRegex(configDef.valueValidRegex());
        configDO.setConfigValue(val);
        configDO.setDefaultValue(configDef.defaultValue());
        configDO.setValueAdvance(configDef.valueAdvance());
        configDO.setConfValType(configDef.kvConfWebOp());
        configDO.setReadOnly(configDef.readOnly());
        configDO.setSecret(configDef.isSecret());
        return configDO;
    }

    protected void fillFieldValue(Object instance, Class clazz, Map<String, String> configMap) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                DsConfigDef configDef = field.getAnnotation(DsConfigDef.class);
                if (configDef == null) {
                    continue;
                }

                String configValue = configMap.get(configDef.name());
                if (StringUtils.isBlank(configValue)) {
                    configValue = configDef.defaultValue();
                    if (StringUtils.isBlank(configValue)) {
                        continue;
                    }
                }

                if (configDef.isSecret()) {
                    configValue = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configValue);
                }

                fillSimpleTypeValue(instance, field, configValue);
            }

            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                fillFieldValue(instance, clazz.getSuperclass(), configMap);
            }
        } catch (Exception e) {
            String msg = "fill field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * only handle simple type
     */
    protected void fillSimpleTypeValue(Object reflectObject, Field field, String configValue) {
        try {
            Class<?> type = field.getType();
            if (type.isEnum()) {
                if (StringUtils.isBlank(configValue)) {
                    field.set(reflectObject, null);
                } else {
                    fillValueOfValue(reflectObject, field, configValue);
                }
            } else if (type == Boolean.class || type == Integer.class || type == Long.class) {
                fillValueOfValue(reflectObject, field, configValue);
            } else if (type == String.class) {
                field.set(reflectObject, configValue);
            }
        } catch (Exception e) {
            String msg = "set simple type field with config value (" + configValue + ") failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    protected void fillValueOfValue(Object reflectObject, Field field, String configValue) throws Exception {
        Method valueOf = field.getType().getMethod("valueOf", String.class);
        Object value = valueOf.invoke(null, configValue);
        field.set(reflectObject, value);
    }
}

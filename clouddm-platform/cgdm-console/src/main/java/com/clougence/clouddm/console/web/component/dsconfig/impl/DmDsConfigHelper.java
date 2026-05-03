package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/11/7 17:11
 */
@Slf4j
public class DmDsConfigHelper {

    public static <T extends DataSourceConfig> T initFieldDefaultValue(T instance) {
        fillFieldValue(instance, instance.getClass(), Collections.emptyMap());
        return instance;
    }

    public static void fillFieldValue(Object instance, Map<String, String> configMap) {
        // fill
        if (instance instanceof DataSourceConfig) {
            fillFieldValue(instance, DataSourceConfig.class, configMap);
        }
        fillFieldValue(instance, instance.getClass(), configMap);
    }

    public static List<RdpDsKvBaseConfigDO> collectConfigs(Object instance) {
        List<RdpDsKvBaseConfigDO> configs = new ArrayList<>();
        collectConfigs(instance, instance.getClass(), configs);
        return configs;
    }

    protected static void collectConfigs(Object instance, Class<?> clazz, List<RdpDsKvBaseConfigDO> configs) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                ConfigDef configDef = field.getAnnotation(ConfigDef.class);
                if (configDef == null) {
                    continue;
                }

                String val = configDef.defaultValue();
                Object oriVal = field.get(instance);
                if (oriVal != null) {
                    val = String.valueOf(oriVal);
                }

                RdpDsKvBaseConfigDO configDO = genConfigDo(configDef, val);

                configs.add(configDO);
            }

            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                collectConfigs(instance, clazz.getSuperclass(), configs);
            }
        } catch (Exception e) {
            String msg = "collect field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    protected static RdpDsKvBaseConfigDO genConfigDo(ConfigDef configDef, String val) {
        RdpDsKvBaseConfigDO configDO = new RdpDsKvBaseConfigDO();
        //configDO.setDataSourceId(dataSourceId);
        configDO.setConfigName(configDef.name());
        if (configDef.group() == null) {
            configDO.setConfigGroup(null);
        } else {
            switch (configDef.group()) {
                case GENERAL:
                    configDO.setConfigGroup(DsConfigGroup.GENERAL);
                    break;
                case CLOUD:
                    configDO.setConfigGroup(DsConfigGroup.CLOUD);
                    break;
                case OPTIONS:
                    configDO.setConfigGroup(DsConfigGroup.OPTIONS);
                    break;
            }
        }

        configDO.setDisplay(configDef.display());
        configDO.setDescKey(configDef.descKey().name());
        configDO.setValueRequire(configDef.valueRequire());
        configDO.setValueValidRegex(configDef.valueValidRegex());
        configDO.setConfigValue(val);
        configDO.setDefaultValue(configDef.defaultValue());
        configDO.setValueAdvance(configDef.valueAdvance());
        configDO.setReadOnly(configDef.readOnly());
        configDO.setSecret(configDef.isSecret());
        return configDO;
    }

    protected static void fillFieldValue(Object instance, Class clazz, Map<String, String> configMap) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                ConfigDef configDef = field.getAnnotation(ConfigDef.class);
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

    /** only handle simple type */
    protected static void fillSimpleTypeValue(Object reflectObject, Field field, String configValue) {
        try {
            Class<?> type = field.getType();
            if (type.isEnum()) {
                if (StringUtils.isBlank(configValue)) {
                    field.set(reflectObject, null);
                } else {
                    fillEnumOfValue(reflectObject, (Class<Enum<?>>) type, field, configValue);
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

    protected static void fillValueOfValue(Object reflectObject, Field field, String configValue) throws Exception {
        Method valueOf = field.getType().getMethod("valueOf", String.class);
        Object value = valueOf.invoke(null, configValue);
        field.set(reflectObject, value);
    }

    protected static void fillEnumOfValue(Object reflectObject, Class<Enum<?>> type, Field field, String configValue) throws Exception {
        Enum<?>[] enums = type.getEnumConstants();
        for (Enum<?> e : enums) {
            if (e.name().equalsIgnoreCase(configValue)) {
                field.set(reflectObject, e);
                return;
            }
        }
    }
}

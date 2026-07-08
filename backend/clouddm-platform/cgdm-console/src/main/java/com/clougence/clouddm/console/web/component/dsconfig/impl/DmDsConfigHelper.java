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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.ConfigValType;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/11/7 17:11
 */
@Slf4j
public class DmDsConfigHelper {

    public static final String CERTIFICATE_CONFIGURED_VALUE = "configured://certificate";

    public static <T extends DataSourceConfig> T initBaseFieldDefaultValue(T instance) {
        fillFieldValue(instance, DataSourceConfig.class, Map.of());
        return instance;
    }

    public static void fillBaseFieldValue(DataSourceConfig instance, Map<String, String> configMap) {
        fillFieldValue(instance, DataSourceConfig.class, configMap);
    }

    public static List<DsConfigKvDef> collectConfigs(Object instance) {
        return collectConfigs(instance, false);
    }

    public static List<DsConfigKvDef> collectConfigs(Object instance, boolean includeLazyValue) {
        List<DsConfigKvDef> configs = new ArrayList<>();
        collectConfigs(instance, instance.getClass(), configs, includeLazyValue);
        return configs;
    }

    protected static void collectConfigs(Object instance, Class<?> clazz, List<DsConfigKvDef> configs, boolean includeLazyValue) {
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

                DsConfigKvDef configDO = genConfigDef(configDef, val, field.getType(), includeLazyValue);

                configs.add(configDO);
            }

            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                collectConfigs(instance, clazz.getSuperclass(), configs, includeLazyValue);
            }
        } catch (Exception e) {
            String msg = "collect field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    protected static DsConfigKvDef genConfigDef(ConfigDef configDef, String val, Class<?> fieldType) {
        return genConfigDef(configDef, val, fieldType, false);
    }

    protected static DsConfigKvDef genConfigDef(ConfigDef configDef, String val, Class<?> fieldType, boolean includeLazyValue) {
        DsConfigKvDef config = new DsConfigKvDef();
        config.setConfigName(configDef.name());
        config.setConfigGroup(configDef.group());

        config.setLabelKey(configDef.labelKey());
        config.setDescKey(configDef.descKey());
        config.setValueValidRegex(configDef.valueValidRegex());
        config.setValueRequire(StringUtils.isNotBlank(configDef.valueValidRegex()));
        config.setConfigValue(configValue(configDef, val, includeLazyValue));
        config.setDefaultValue(configDef.defaultValue());
        config.setConfValType(resolveValType(configDef, fieldType));
        config.setReadOnly(configDef.readOnly());
        config.setSecret(configDef.isSecret());
        config.setLazy(configDef.lazy());
        config.setActiveField(configDef.activeField());
        config.setActiveEquals(configDef.activeEquals());
        return config;
    }

    private static String configValue(ConfigDef configDef, String val, boolean includeLazyValue) {
        if (!configDef.lazy() || includeLazyValue) {
            return val;
        }
        return isCertificateField(configDef.name()) && StringUtils.isNotBlank(val) ? CERTIFICATE_CONFIGURED_VALUE : "";
    }

    private static boolean isCertificateField(String configName) {
        return StringUtils.equals(configName, DataSourceConfig.Fields.sslCaData) || StringUtils.equals(configName, DataSourceConfig.Fields.sslClientCertData)
               || StringUtils.equals(configName, DataSourceConfig.Fields.sslClientKeyData);
    }

    private static ConfigValType resolveValType(ConfigDef configDef, Class<?> fieldType) {
        if (configDef.valType() != ConfigValType.AUTO) {
            return configDef.valType();
        }
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            return ConfigValType.BOOLEAN;
        }
        return ConfigValType.TEXT;
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

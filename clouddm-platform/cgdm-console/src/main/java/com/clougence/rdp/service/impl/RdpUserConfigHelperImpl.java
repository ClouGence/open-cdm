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
package com.clougence.rdp.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.rdp.constant.UserConfigDef;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.service.RdpUserConfigHelper;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/11/7 17:11
 */
@Service
@Slf4j
public class RdpUserConfigHelperImpl implements RdpUserConfigHelper {

    @Override
    public void fillFieldValue(Object instance, Map<String, String> configMap) {
        fillFieldValue(instance, instance.getClass(), configMap);
    }

    @Override
    public List<DmSysUserConfDO> collectConfigs(Object instance, String uid) {
        List<DmSysUserConfDO> configs = new ArrayList<>();
        collectConfigs(instance, uid, instance.getClass(), configs);
        return configs;
    }

    protected void collectConfigs(Object instance, String uid, Class clazz, List<DmSysUserConfDO> configs) {
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

                DmSysUserConfDO configDO = genConfigDo(configDef, val, uid);

                configs.add(configDO);
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

    protected DmSysUserConfDO genConfigDo(UserConfigDef configDef, String val, String uid) {
        DmSysUserConfDO configDO = new DmSysUserConfDO();
        configDO.setConfigName(configDef.name());
        configDO.setConfigValue(val);
        configDO.setUid(uid);
        configDO.setValueRange(configDef.valueRange());
        configDO.setUserConfigTagType(configDef.configTagType());
        configDO.setConfBelong(configDef.confBelong());
        configDO.setConfValType(configDef.kvConfWebOp());

        configDO.setDefaultValue(configDef.defaultValue());
        configDO.setReadOnly(configDef.readOnly());
        configDO.setSecret(configDef.isSecret());
        configDO.setDescKey(configDef.descKey().name());

        return configDO;
    }

    protected void fillFieldValue(Object instance, Class clazz, Map<String, String> configMap) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            UserConfigDef configDef = field.getAnnotation(UserConfigDef.class);
            if (configDef == null) {
                continue;
            }

            String configValue = configMap.get(configDef.name());

            if (configValue == null) {
                continue;
            }

            Object convert = null;
            //            Object convert = ConverterUtils.convert(configValue, field.getType());
            //            if (convert == null && field.getType().isPrimitive()) {
            //                convert = BeanUtils.getDefaultValue(field.getType());
            //            }

            try {
                if (StringUtils.isBlank(configValue)) {
                    field.set(instance, null);
                } else {
                    field.set(instance, convert);
                }
            } catch (Exception e) {
                String msg = "fill field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }

        }

        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            fillFieldValue(instance, clazz.getSuperclass(), configMap);
        }
    }
}

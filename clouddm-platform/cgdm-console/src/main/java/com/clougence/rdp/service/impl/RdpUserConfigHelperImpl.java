package com.clougence.rdp.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.rdp.constant.UserConfigDef;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
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
    public List<RdpUserKvBaseConfigDO> collectConfigs(Object instance, String uid) {
        List<RdpUserKvBaseConfigDO> configs = new ArrayList<>();
        collectConfigs(instance, uid, instance.getClass(), configs);
        return configs;
    }

    protected void collectConfigs(Object instance, String uid, Class clazz, List<RdpUserKvBaseConfigDO> configs) {
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

                RdpUserKvBaseConfigDO configDO = genConfigDo(configDef, val, uid);

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

    protected RdpUserKvBaseConfigDO genConfigDo(UserConfigDef configDef, String val, String uid) {
        RdpUserKvBaseConfigDO configDO = new RdpUserKvBaseConfigDO();
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

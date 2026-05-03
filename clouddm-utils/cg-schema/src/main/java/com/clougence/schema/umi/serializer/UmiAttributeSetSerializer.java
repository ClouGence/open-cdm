package com.clougence.schema.umi.serializer;

import java.util.Map;
import java.util.function.BiConsumer;

import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.utils.StringUtils;

public class UmiAttributeSetSerializer<T extends AttributeUmiData> extends Serializer<T> {

    public void readData(Map<String, Object> jsonMap, T readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        Object attributes = jsonMap.get(KEY_ATTRIBUTES);
        if (attributes instanceof Map) {
            ((Map<?, ?>) attributes).forEach((BiConsumer<Object, Object>) (key, value) -> {
                String keyStr = StringUtils.toString(key);
                String valueStr = StringUtils.toString(value);
                readTo.setAttribute(keyStr, valueStr);
            });
        }
    }

    public void writeToMap(T umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr == null) {
            return;
        }

        Map<String, String> attrsMap = umiAttr.getAttributes();
        if (!(attrsMap == null || attrsMap.isEmpty())) {
            toMap.put(KEY_ATTRIBUTES, attrsMap);
        }
    }
}

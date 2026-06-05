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

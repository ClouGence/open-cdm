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
package com.clougence.schema.umi.serializer.mq;

import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.mq.MqValue;
import com.clougence.schema.umi.struts.UmiTypes;

public class MqValueSerializer extends UmiAttributeSetSerializer<MqValue> {
    @Override
    public void readData(Map<String, Object> jsonMap, MqValue value) {
        super.readData(jsonMap, value);
        if (jsonMap == null) {
            return;
        }
        value.setName((String) jsonMap.get(KEY_NAME));
        value.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
    }

    @Override
    public void writeToMap(MqValue value, Map<String, Object> jsonMap) {
        super.writeToMap(value, jsonMap);
        if (value == null) {
            return;
        }
        jsonMap.put(KEY_NAME, value.getName());
        jsonMap.put(KEY_UMI_TYPE, value.getUmiType().getTypeName());
    }
}

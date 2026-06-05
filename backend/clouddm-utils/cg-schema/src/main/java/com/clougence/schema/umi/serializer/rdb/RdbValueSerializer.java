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
package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.ConstraintUmiDataSerializer;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;

public class RdbValueSerializer extends ConstraintUmiDataSerializer<RdbValue> {

    public void readData(Map<String, Object> jsonMap, RdbValue readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setValue((String) jsonMap.get(KEY_VALUE));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
    }

    @Override
    public void writeToMap(RdbValue topic, Map<String, Object> toMap) {
        super.writeToMap(topic, toMap);
        if (topic == null) {
            return;
        }

        toMap.put(KEY_VALUE, topic.getValue());
        if (topic.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, topic.getUmiType().getTypeName());
        }
    }
}

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

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbSequence;

public class RdbSequenceSerializer extends UmiAttributeSetSerializer<RdbSequence> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbSequence readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setMaxValue((String) jsonMap.get(KEY_RDB_MAX_VALUE));
        readTo.setMinValue((String) jsonMap.get(KEY_RDB_MIN_VALUE));
        readTo.setIncrementBy((String) jsonMap.get(KEY_RDB_INCREMENT_BY));
    }

    @Override
    public void writeToMap(RdbSequence umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if (umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if (umiAttr.getMaxValue() != null) {
            toMap.put(KEY_RDB_MAX_VALUE, umiAttr.getMaxValue());
        }
        if (umiAttr.getMinValue() != null) {
            toMap.put(KEY_RDB_MIN_VALUE, umiAttr.getMinValue());
        }
        if (umiAttr.getIncrementBy() != null) {
            toMap.put(KEY_RDB_INCREMENT_BY, umiAttr.getIncrementBy());
        }

    }
}

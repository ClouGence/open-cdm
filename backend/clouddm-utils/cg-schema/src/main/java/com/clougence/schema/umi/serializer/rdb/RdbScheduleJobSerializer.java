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
import com.clougence.schema.umi.special.rdb.RdbScheduleJob;

public class RdbScheduleJobSerializer extends UmiAttributeSetSerializer<RdbScheduleJob> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbScheduleJob readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setExecSql((String) jsonMap.get(KEY_RDB_SQL));
        readTo.setCreator((String) jsonMap.get(KEY_RDB_JOB_CREATOR));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setEnabled((String) jsonMap.get(KEY_RDB_JOB_ENABLE));
        readTo.setComment((String) jsonMap.get(KEY_COMMENT));
        readTo.setStatus((String) jsonMap.get(KEY_RDB_STATUS));
    }

    @Override
    public void writeToMap(RdbScheduleJob umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if (umiAttr.getExecSql() != null) {
            toMap.put(KEY_RDB_SQL, umiAttr.getExecSql());
        }
        if (umiAttr.getCreator() != null) {
            toMap.put(KEY_RDB_JOB_CREATOR, umiAttr.getCreator());
        }
        if (umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if (umiAttr.getEnabled() != null) {
            toMap.put(KEY_RDB_JOB_ENABLE, umiAttr.getEnabled());
        }
        if (umiAttr.getComment() != null) {
            toMap.put(KEY_COMMENT, umiAttr.getComment());
        }
        if (umiAttr.getStatus() != null) {
            toMap.put(KEY_RDB_STATUS, umiAttr.getStatus());
        }
    }
}

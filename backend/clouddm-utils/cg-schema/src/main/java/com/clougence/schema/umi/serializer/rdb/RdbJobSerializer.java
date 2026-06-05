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

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbJob;

import java.util.Map;

public class RdbJobSerializer extends UmiAttributeSetSerializer<RdbJob> {
    @Override
    public void readData(Map<String, Object> jsonMap, RdbJob readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setExecSql((String) jsonMap.get(KEY_RDB_SQL));
        readTo.setRunning((Boolean) jsonMap.get(KEY_RDB_JOB_RUNNING));
        readTo.setCreator((String) jsonMap.get(KEY_RDB_JOB_CREATOR));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setInterval((String) jsonMap.get(KEY_RDB_JOB_INTERVAL));
    }

    @Override
    public void writeToMap(RdbJob umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if(umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if(umiAttr.getExecSql() != null) {
            toMap.put(KEY_RDB_SQL, umiAttr.getExecSql());
        }
        if(umiAttr.getRunning() != null) {
            toMap.put(KEY_RDB_JOB_RUNNING, umiAttr.getRunning());
        }
        if(umiAttr.getCreator() != null) {
            toMap.put(KEY_RDB_JOB_CREATOR, umiAttr.getCreator());
        }
        if(umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if(umiAttr.getInterval() != null) {
            toMap.put(KEY_RDB_JOB_INTERVAL, umiAttr.getInterval());
        }

    }
}

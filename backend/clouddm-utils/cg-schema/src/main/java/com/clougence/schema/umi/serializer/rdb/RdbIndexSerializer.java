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

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbIndexType;

public class RdbIndexSerializer extends UmiAttributeSetSerializer<RdbIndex> {

    public void readData(Map<String, Object> jsonMap, RdbIndex readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setTable((String) jsonMap.get(KEY_RDB_TABLE));
        readTo.setName((String) jsonMap.get(KEY_NAME));

        readTo.setType(RdbIndexType.valueOfCode((String) jsonMap.get(KEY_TYPE)));
        readTo.setColumnList((List<String>) jsonMap.get(KEY_RDB_COLUMN_LIST));
    }

    @Override
    public void writeToMap(RdbIndex rdbIndex, Map<String, Object> toMap) {
        super.writeToMap(rdbIndex, toMap);
        if (rdbIndex == null) {
            return;
        }

        if (rdbIndex.getCatalog() != null) {
            toMap.put(KEY_RDB_CATALOG, rdbIndex.getCatalog());
        }
        if (rdbIndex.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, rdbIndex.getSchema());
        }
        if (rdbIndex.getTable() != null) {
            toMap.put(KEY_RDB_TABLE, rdbIndex.getTable());
        }
        if (rdbIndex.getName() != null) {
            toMap.put(KEY_NAME, rdbIndex.getName());
        }
        if (rdbIndex.getType() != null) {
            toMap.put(KEY_TYPE, rdbIndex.getType().getTypeName());
        }
        if (rdbIndex.getColumnList() != null) {
            toMap.put(KEY_RDB_COLUMN_LIST, rdbIndex.getColumnList());
        }
    }
}

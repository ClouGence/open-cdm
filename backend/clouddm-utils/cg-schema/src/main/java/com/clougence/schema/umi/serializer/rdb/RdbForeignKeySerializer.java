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

import com.clougence.schema.umi.serializer.ConstraintObjectSerializer;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;

public class RdbForeignKeySerializer extends ConstraintObjectSerializer<RdbForeignKey> {

    public void readData(Map<String, Object> jsonMap, RdbForeignKey readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setReferenceCatalog((String) jsonMap.get(KEY_RDB_REF_CATALOG));
        readTo.setReferenceSchema((String) jsonMap.get(KEY_RDB_REF_SCHEMA));
        readTo.setReferenceTable((String) jsonMap.get(KEY_RDB_REF_TABLE));
        readTo.setReferenceMapping((Map<String, String>) jsonMap.get(KEY_RDB_REF_MAPPING));
        readTo.setUpdateRule(RdbForeignKeyRule.valueOfCode((String) jsonMap.get(KEY_RDB_REF_UPDATE_RULE)));
        readTo.setDeleteRule(RdbForeignKeyRule.valueOfCode((String) jsonMap.get(KEY_RDB_REF_DELETE_RULE)));

        readTo.setColumnList((List<String>) jsonMap.get(KEY_RDB_COLUMN_LIST));
    }

    @Override
    public void writeToMap(RdbForeignKey foreignKey, Map<String, Object> toMap) {
        super.writeToMap(foreignKey, toMap);
        if (foreignKey == null) {
            return;
        }

        if (foreignKey.getReferenceCatalog() != null) {
            toMap.put(KEY_RDB_REF_CATALOG, foreignKey.getReferenceCatalog());
        }
        if (foreignKey.getReferenceSchema() != null) {
            toMap.put(KEY_RDB_REF_SCHEMA, foreignKey.getReferenceSchema());
        }
        if (foreignKey.getReferenceTable() != null) {
            toMap.put(KEY_RDB_REF_TABLE, foreignKey.getReferenceTable());
        }
        if (foreignKey.getReferenceMapping() != null) {
            toMap.put(KEY_RDB_REF_MAPPING, foreignKey.getReferenceMapping());
        }
        if (foreignKey.getUpdateRule() != null) {
            toMap.put(KEY_RDB_REF_UPDATE_RULE, foreignKey.getUpdateRule().getTypeName());
        }
        if (foreignKey.getUpdateRule() != null) {
            toMap.put(KEY_RDB_REF_DELETE_RULE, foreignKey.getDeleteRule().getTypeName());
        }
        if (foreignKey.getColumnList() != null) {
            toMap.put(KEY_RDB_COLUMN_LIST, foreignKey.getColumnList());
        }
    }
}

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

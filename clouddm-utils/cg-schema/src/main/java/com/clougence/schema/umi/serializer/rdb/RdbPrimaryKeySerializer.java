package com.clougence.schema.umi.serializer.rdb;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.serializer.PrimarySerializer;
import com.clougence.schema.umi.special.rdb.RdbPrimaryKey;

public class RdbPrimaryKeySerializer extends PrimarySerializer<RdbPrimaryKey> {

    public void readData(Map<String, Object> jsonMap, RdbPrimaryKey readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setColumnList((List<String>) jsonMap.get(KEY_RDB_COLUMN_LIST));
    }

    @Override
    public void writeToMap(RdbPrimaryKey primaryKey, Map<String, Object> toMap) {
        super.writeToMap(primaryKey, toMap);
        if (primaryKey == null) {
            return;
        }

        if (primaryKey.getColumnList() != null) {
            toMap.put(KEY_RDB_COLUMN_LIST, primaryKey.getColumnList());
        }
    }
}

package com.clougence.schema.umi.serializer.rdb;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.serializer.UniqueSerializer;
import com.clougence.schema.umi.special.rdb.RdbUniqueKey;

public class RdbUniqueKeySerializer extends UniqueSerializer<RdbUniqueKey> {

    public void readData(Map<String, Object> jsonMap, RdbUniqueKey readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setColumnList((List<String>) jsonMap.get(KEY_RDB_COLUMN_LIST));
    }

    @Override
    public void writeToMap(RdbUniqueKey primaryKey, Map<String, Object> toMap) {
        super.writeToMap(primaryKey, toMap);
        if (primaryKey == null) {
            return;
        }

        if (primaryKey.getColumnList() != null) {
            toMap.put(KEY_RDB_COLUMN_LIST, primaryKey.getColumnList());
        }
    }
}

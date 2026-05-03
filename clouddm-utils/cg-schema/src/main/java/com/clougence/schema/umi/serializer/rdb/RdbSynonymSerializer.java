package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbSynonym;

public class RdbSynonymSerializer extends UmiAttributeSetSerializer<RdbSynonym> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbSynonym readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setTable((String) jsonMap.get(KEY_RDB_TABLE));
        readTo.setTableSchema((String) jsonMap.get(KEY_RDB_TABLE_SCHEMA));
    }

    @Override
    public void writeToMap(RdbSynonym umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if (umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if (umiAttr.getTable() != null) {
            toMap.put(KEY_RDB_TABLE, umiAttr.getTable());
        }
        if (umiAttr.getTableSchema() != null) {
            toMap.put(KEY_RDB_TABLE_SCHEMA, umiAttr.getTableSchema());
        }

    }
}

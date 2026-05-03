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

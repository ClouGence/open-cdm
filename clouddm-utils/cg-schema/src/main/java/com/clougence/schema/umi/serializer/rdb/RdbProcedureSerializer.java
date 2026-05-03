package com.clougence.schema.umi.serializer.rdb;

import com.clougence.schema.umi.special.rdb.RdbProcedure;
import com.clougence.schema.umi.struts.UmiTypes;

import java.util.Map;

public class RdbProcedureSerializer extends RdbRoutineUmiDataSerializer<RdbProcedure> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbProcedure readTo) {
        super.readData(jsonMap, readTo);
        readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
        readTo.setSql((String) jsonMap.get(KEY_RDB_SQL));
        readTo.setFeatures((Map<String, Object>) jsonMap.get(KEY_RDB_FEATURES));
    }

    @Override
    public void writeToMap(RdbProcedure umiData, Map<String, Object> toMap) {
        super.writeToMap(umiData, toMap);
        if (umiData.getCatalog() != null) {
            toMap.put(KEY_RDB_CATALOG, umiData.getCatalog());
        }
        if (umiData.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiData.getSchema());
        }
        if (umiData.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, umiData.getUmiType().getTypeName());
        }
        if (umiData.getSql() != null) {
            toMap.put(KEY_RDB_SQL, umiData.getSql());
        }
        if (umiData.getFeatures() != null) {
            toMap.put(KEY_RDB_FEATURES, umiData.getFeatures());
        }
    }
}

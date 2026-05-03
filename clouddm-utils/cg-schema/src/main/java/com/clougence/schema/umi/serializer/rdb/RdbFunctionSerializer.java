package com.clougence.schema.umi.serializer.rdb;

import java.util.HashMap;
import java.util.Map;

import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.special.rdb.RdbProcedure;
import com.clougence.schema.umi.struts.UmiTypes;

public class RdbFunctionSerializer extends RdbRoutineUmiDataSerializer<RdbFunction> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbFunction readTo) {
        super.readData(jsonMap, readTo);
        readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setReturns(readReturns((Map<String, Object>) jsonMap.get(KEY_RDB_FUNC_RETURN)));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
        readTo.setSql((String) jsonMap.get(KEY_RDB_SQL));
        readTo.setFeatures((Map<String, Object>) jsonMap.get(KEY_RDB_FEATURES));
    }

    private RdbParam readReturns(Map<String, Object> map) {
        RdbParam rdbParam = new RdbParam();
        rdbParamSerializer.readData(map, rdbParam);
        return rdbParam;
    }

    @Override
    public void writeToMap(RdbFunction umiData, Map<String, Object> toMap) {
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
        if (umiData.getReturns() != null) {
            toMap.put(KEY_RDB_FUNC_RETURN, writeReturns(umiData.getReturns()));
        }
        if (umiData.getSql() != null) {
            toMap.put(KEY_RDB_SQL, umiData.getSql());
        }
        if (umiData.getFeatures() != null) {
            toMap.put(KEY_RDB_FEATURES, umiData.getFeatures());
        }
    }

    private Map<String, Object> writeReturns(RdbParam returns) {
        Map<String, Object> toMap = new HashMap<>();
        rdbParamSerializer.writeToMap(returns, toMap);
        return toMap;
    }

}

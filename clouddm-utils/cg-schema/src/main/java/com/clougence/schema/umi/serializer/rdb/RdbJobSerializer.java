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

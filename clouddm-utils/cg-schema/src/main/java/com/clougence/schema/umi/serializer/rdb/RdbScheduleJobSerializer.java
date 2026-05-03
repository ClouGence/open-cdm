package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbScheduleJob;

public class RdbScheduleJobSerializer extends UmiAttributeSetSerializer<RdbScheduleJob> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbScheduleJob readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setExecSql((String) jsonMap.get(KEY_RDB_SQL));
        readTo.setCreator((String) jsonMap.get(KEY_RDB_JOB_CREATOR));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setEnabled((String) jsonMap.get(KEY_RDB_JOB_ENABLE));
        readTo.setComment((String) jsonMap.get(KEY_COMMENT));
        readTo.setStatus((String) jsonMap.get(KEY_RDB_STATUS));
    }

    @Override
    public void writeToMap(RdbScheduleJob umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if (umiAttr.getExecSql() != null) {
            toMap.put(KEY_RDB_SQL, umiAttr.getExecSql());
        }
        if (umiAttr.getCreator() != null) {
            toMap.put(KEY_RDB_JOB_CREATOR, umiAttr.getCreator());
        }
        if (umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if (umiAttr.getEnabled() != null) {
            toMap.put(KEY_RDB_JOB_ENABLE, umiAttr.getEnabled());
        }
        if (umiAttr.getComment() != null) {
            toMap.put(KEY_COMMENT, umiAttr.getComment());
        }
        if (umiAttr.getStatus() != null) {
            toMap.put(KEY_RDB_STATUS, umiAttr.getStatus());
        }
    }
}

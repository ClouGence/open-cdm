package com.clougence.schema.umi.serializer.rdb;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbDbLink;

import java.util.Map;

public class RdbDbLinkSerializer extends UmiAttributeSetSerializer<RdbDbLink> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbDbLink readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setUsername((String) jsonMap.get(KEY_RDB_DBLINK_USERNAME));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setHost((String) jsonMap.get(KEY_RDB_DBLINK_HOST));
    }

    @Override
    public void writeToMap(RdbDbLink umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if (umiAttr.getUsername() != null) {
            toMap.put(KEY_RDB_DBLINK_USERNAME, umiAttr.getUsername());
        }
        if (umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if (umiAttr.getHost() != null) {
            toMap.put(KEY_RDB_DBLINK_HOST, umiAttr.getHost());
        }
    }
}

package com.clougence.schema.umi.serializer.rdb;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbUser;

import java.util.Map;

public class RdbUserSerializer extends UmiAttributeSetSerializer<RdbUser> {
    @Override
    public void readData(Map<String, Object> jsonMap, RdbUser readTo) {
        super.readData(jsonMap, readTo);
        readTo.setUsername((String) jsonMap.get(KEY_RDB_USERNAME));
    }

    @Override
    public void writeToMap(RdbUser umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getUsername() != null) {
            toMap.put(KEY_RDB_USERNAME, umiAttr.getUsername());
        }
    }
}

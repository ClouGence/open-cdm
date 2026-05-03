package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbRole;

public class RdbRoleSerializer  extends UmiAttributeSetSerializer<RdbRole> {
    @Override
    public void readData(Map<String, Object> jsonMap, RdbRole readTo) {
        super.readData(jsonMap, readTo);
        readTo.setRoleName((String) jsonMap.get(KEY_RDB_ROLE_NAME));
    }

    @Override
    public void writeToMap(RdbRole umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getRoleName() != null) {
            toMap.put(KEY_RDB_ROLE_NAME, umiAttr.getRoleName());
        }
    }
}
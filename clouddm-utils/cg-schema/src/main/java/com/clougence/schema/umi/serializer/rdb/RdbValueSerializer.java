package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.ConstraintUmiDataSerializer;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;

public class RdbValueSerializer extends ConstraintUmiDataSerializer<RdbValue> {

    public void readData(Map<String, Object> jsonMap, RdbValue readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setValue((String) jsonMap.get(KEY_VALUE));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
    }

    @Override
    public void writeToMap(RdbValue topic, Map<String, Object> toMap) {
        super.writeToMap(topic, toMap);
        if (topic == null) {
            return;
        }

        toMap.put(KEY_VALUE, topic.getValue());
        if (topic.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, topic.getUmiType().getTypeName());
        }
    }
}

package com.clougence.schema.umi.serializer.rdb;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.struts.RoutineUmiData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RdbRoutineUmiDataSerializer<T extends RoutineUmiData> extends UmiAttributeSetSerializer<T> {

    protected static final RdbParamSerializer rdbParamSerializer = new RdbParamSerializer();

    @Override
    public void readData(Map<String, Object> jsonMap, T readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setComment((String) jsonMap.get(KEY_COMMENT));
        readTo.setRdbParams(readRdbParams((List<Map<String, Object>>)jsonMap.get(KEY_RDB_ROUTINE_PARAMS)));
    }

    private List<RdbParam> readRdbParams(List<Map<String, Object>> maps) {
        return maps.stream().map(map -> {
            RdbParam rdbParam = new RdbParam();
            rdbParamSerializer.readData(map, rdbParam);
            return rdbParam;
        }).collect(Collectors.toList());
    }


    @Override
    public void writeToMap(T umiData, Map<String, Object> toMap) {
        super.writeToMap(umiData, toMap);
        if (umiData == null) {
            return;
        }

        if (umiData.getName() != null) {
            toMap.put(KEY_NAME, umiData.getName());
        }
        if (umiData.getComment() != null) {
            toMap.put(KEY_COMMENT, umiData.getComment());
        }
        if (umiData.getRdbParams() != null) {
            toMap.put(KEY_RDB_ROUTINE_PARAMS, writeParams(umiData.getRdbParams()));
        }
    }

    private List<Map<String, Object>> writeParams(List<RdbParam> rdbParams) {
        return rdbParams.stream().map(rdbParam -> {
            Map<String, Object> toMap = new HashMap<>();
            rdbParamSerializer.writeToMap(rdbParam, toMap);
            return toMap;
        }).collect(Collectors.toList());
    }
}

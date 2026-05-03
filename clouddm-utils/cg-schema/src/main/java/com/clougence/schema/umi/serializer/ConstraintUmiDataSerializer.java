package com.clougence.schema.umi.serializer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.struts.ConstraintUmiData;
import com.clougence.schema.umi.struts.UmiConstraint;

public class ConstraintUmiDataSerializer<T extends ConstraintUmiData> extends UmiAttributeSetSerializer<T> {

    public void readData(Map<String, Object> jsonMap, T readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        List<Map<String, Object>> jsonMaps = (List<Map<String, Object>>) jsonMap.get(KEY_CONSTRAINTS);
        if (jsonMaps != null) {
            List<UmiConstraint> constraintList = new ArrayList<>();
            for (Map<String, Object> map : jsonMaps) {
                String aClassName = (String) map.get(KEY_CLASS);
                UmiConstraint constraint = (UmiConstraint) SerializerRegistry.createByType(aClassName);
                Serializer<UmiConstraint> constraintSerializer = SerializerRegistry.lookSerializer(aClassName);

                constraintSerializer.readData(map, constraint);
                constraintList.add(constraint);
            }

            readTo.setConstraints(constraintList);
        }
    }

    @Override
    public void writeToMap(T umiData, Map<String, Object> toMap) {
        super.writeToMap(umiData, toMap);
        if (umiData == null) {
            return;
        }

        if (umiData.getConstraints() != null) {
            List<Map<String, Object>> jsonMaps = new ArrayList<>();
            for (UmiConstraint constraint : umiData.getConstraints()) {
                Map<String, Object> constraintData = new LinkedHashMap<>();
                Serializer<UmiConstraint> constraintSerializer = SerializerRegistry.lookSerializer(constraint.getClass().getSimpleName());

                constraintSerializer.writeToMap(constraint, constraintData);
                jsonMaps.add(constraintData);
            }
            toMap.put(KEY_CONSTRAINTS, jsonMaps);
        }
    }

}

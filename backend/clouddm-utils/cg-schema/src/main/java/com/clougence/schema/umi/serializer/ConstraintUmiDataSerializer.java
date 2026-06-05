/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

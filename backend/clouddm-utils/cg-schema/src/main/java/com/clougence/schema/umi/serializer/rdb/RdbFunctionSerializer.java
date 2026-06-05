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

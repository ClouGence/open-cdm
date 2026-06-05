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

import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.special.rdb.RdbParamMode;
import com.clougence.schema.umi.struts.UmiTypes;

public class RdbParamSerializer extends UmiAttributeSetSerializer<RdbParam> {

    @Override
    public void writeToMap(RdbParam object, Map<String, Object> toMap) {
        super.writeToMap(object, toMap);
        if (object.getCatalog() != null) {
            toMap.put(KEY_RDB_CATALOG, object.getCatalog());
        }
        if (object.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, object.getSchema());
        }
        if (object.getName() != null) {
            toMap.put(KEY_NAME, object.getName());
        }
        if (object.getOrdinal() != null) {
            toMap.put(KEY_RDB_PARAM_ORDINAL, object.getOrdinal());
        }
        if (object.getReferenceObject() != null) {
            toMap.put(KEY_RDB_REFERENCE_OBJECT, object.getReferenceObject());
        }
        if (object.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, object.getUmiType().getTypeName());
        }
        if (object.getType() != null) {
            toMap.put(KEY_TYPE, object.getType());
        }
        if (object.getCharacterMaximumLength() != null) {
            toMap.put(KEY_RDB_PARAM_MAXIMUM_LENGTH, object.getCharacterMaximumLength());
        }
        if (object.getLength() != null) {
            toMap.put(KEY_RDB_PARAM_LENGTH, object.getLength());
        }
        if (object.getNumericPrecision() != null) {
            toMap.put(KEY_RDB_PARAM_NUMERIC_PRECISION, object.getNumericPrecision());
        }
        if (object.getNumericScale() != null) {
            toMap.put(KEY_RDB_PARAM_NUMERIC_SCALE, object.getNumericScale());
        }
        if (object.getMode() != null) {
            toMap.put(KEY_RDB_PARAM_MODE, object.getMode());
        }
        if (object.getDatetimePrecision() != null) {
            toMap.put(KEY_RDB_PARAM_DATETIME_PRECISION, object.getDatetimePrecision());
        }

    }

    @Override
    public void readData(Map<String, Object> jsonMap, RdbParam readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }
        if (jsonMap.containsKey(KEY_RDB_CATALOG)) {
            readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        }
        if (jsonMap.containsKey(KEY_RDB_SCHEMA)) {
            readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        }
        if (jsonMap.containsKey(KEY_NAME)) {
            readTo.setName((String) jsonMap.get(KEY_NAME));
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_ORDINAL)) {
            readTo.setOrdinal((Integer) jsonMap.get(KEY_RDB_PARAM_ORDINAL));
        }
        if (jsonMap.containsKey(KEY_RDB_REFERENCE_OBJECT)) {
            readTo.setReferenceObject((String) jsonMap.get(KEY_RDB_REFERENCE_OBJECT));
        }
        if (jsonMap.containsKey(KEY_UMI_TYPE)) {
            readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
        }
        if (jsonMap.containsKey(KEY_TYPE)) {
            readTo.setType((String) jsonMap.get(KEY_TYPE));
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_MAXIMUM_LENGTH)) {
            readTo.setCharacterMaximumLength((String) jsonMap.get(KEY_RDB_PARAM_MAXIMUM_LENGTH));
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_LENGTH)) {
            readTo.setLength(((Number) jsonMap.get(KEY_RDB_PARAM_LENGTH)).longValue());
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_NUMERIC_PRECISION)) {
            readTo.setNumericPrecision((Integer) jsonMap.get(KEY_RDB_PARAM_NUMERIC_PRECISION));
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_NUMERIC_SCALE)) {
            readTo.setNumericScale((Integer) jsonMap.get(KEY_RDB_PARAM_NUMERIC_SCALE));
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_DATETIME_PRECISION)) {
            readTo.setDatetimePrecision((Integer) jsonMap.get(KEY_RDB_PARAM_DATETIME_PRECISION));
        }
        if (jsonMap.containsKey(KEY_RDB_PARAM_MODE)) {
            readTo.setMode(RdbParamMode.valueOfCode((String) jsonMap.get(KEY_RDB_PARAM_MODE)));
        }
    }
}

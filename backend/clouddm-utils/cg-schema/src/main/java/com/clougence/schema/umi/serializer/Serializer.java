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

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public abstract class Serializer<T> {

    // general
    public static final String KEY_CLASS                        = "c";
    public static final String KEY_ATTRIBUTES                   = "a";
    public static final String KEY_CONSTRAINTS                  = "con";
    public static final String KEY_NAME                         = "n";
    public static final String KEY_ALIAS                        = "al";
    public static final String KEY_VALUE                        = "v";
    public static final String KEY_TYPE                         = "t";
    public static final String KEY_COMMENT                      = "com";
    public static final String KEY_INSTANCE                     = "i";
    public static final String KEY_UMI_TYPE                     = "ut";
    // mq
    public static final String KEY_MQ_ENDPOINT_LIST             = "eps";
    // rdb
    public static final String KEY_RDB_CATALOG                  = "cat";
    public static final String KEY_RDB_SCHEMA                   = "sch";
    public static final String KEY_RDB_TABLE                    = "tab";
    public static final String KEY_RDB_COLUMN_LIST              = "cos";
    public static final String KEY_RDB_ORDER                    = "num";
    public static final String KEY_RDB_CHAR_LENGTH              = "cl";
    public static final String KEY_RDB_BYTE_LENGTH              = "bl";
    public static final String KEY_RDB_NUMERIC_PRECISION        = "np";
    public static final String KEY_RDB_NUMERIC_SCALE            = "ns";
    public static final String KEY_RDB_NUMERIC_UNSIGNED         = "nu";
    public static final String KEY_RDB_DATETIME_PRECISION       = "dp";
    public static final String KEY_RDB_DEFAULT                  = "df";
    public static final String KEY_RDB_DEFAULT_T                = "dft";
    public static final String KEY_RDB_REF_CATALOG              = "rcat";
    public static final String KEY_RDB_REF_SCHEMA               = "rsch";
    public static final String KEY_RDB_REF_TABLE                = "rtab";
    public static final String KEY_RDB_REF_MAPPING              = "rmap";
    public static final String KEY_RDB_REF_UPDATE_RULE          = "rur";
    public static final String KEY_RDB_REF_DELETE_RULE          = "rdr";
    public static final String KEY_RDB_PRIMARY_KEY              = "pk";
    public static final String KEY_RDB_UNIQUE_KEY               = "uk";
    public static final String KEY_RDB_FOREIGN_KEY              = "fk";
    public static final String KEY_RDB_INDEX                    = "idx";
    public static final String KEY_RDB_SQL                      = "sql";
    public static final String KEY_RDB_TRIGGER_TABLE_CATALOG    = "ttabc";
    public static final String KEY_RDB_TRIGGER_TABLE_SCHEMA     = "ttabs";
    public static final String KEY_RDB_TRIGGER_TABLE_NAME       = "ttabt";
    public static final String KEY_RDB_TRIGGER_EVENT            = "te";
    public static final String KEY_RDB_TRIGGER_TIME             = "ttime";
    public static final String KEY_RDB_TRIGGER_COLUMN           = "tcolumn";
    public static final String KEY_RDB_ROUTINE_PARAMS           = "para";
    public static final String KEY_RDB_FEATURES                 = "feature";

    public static final String KEY_RDB_REFERENCE_OBJECT         = "refo";
    public static final String KEY_RDB_PARAM_ORDINAL            = "ord";
    public static final String KEY_RDB_PARAM_MAXIMUM_LENGTH     = "mlen";
    public static final String KEY_RDB_FUNC_RETURN              = "re";

    public static final String KEY_RDB_PARAM_LENGTH             = "pl";
    public static final String KEY_RDB_PARAM_NUMERIC_PRECISION  = "pnp";
    public static final String KEY_RDB_PARAM_NUMERIC_SCALE      = "pns";
    public static final String KEY_RDB_PARAM_DATETIME_PRECISION = "pdp";
    public static final String KEY_RDB_PARAM_MODE               = "pm";

    public static final String KEY_RDB_JOB_RUNNING              = "jr";
    public static final String KEY_RDB_JOB_CREATOR              = "jc";
    public static final String KEY_RDB_JOB_INTERVAL             = "ji";
    public static final String KEY_RDB_JOB_ENABLE               = "je";

    public static final String KEY_RDB_DBLINK_USERNAME          = "du";
    public static final String KEY_RDB_DBLINK_HOST              = "dh";
    public static final String KEY_RDB_STATUS                   = "status";

    public static final String KEY_RDB_MAX_VALUE                = "maxv";
    public static final String KEY_RDB_MIN_VALUE                = "minv";
    public static final String KEY_RDB_INCREMENT_BY             = "ib";

    public static final String KEY_RDB_USERNAME                 = "kru";
    public static final String KEY_RDB_ROLE_NAME                = "rn";

    public static final String KEY_RDB_TABLE_SCHEMA             = "ts";

    @SneakyThrows
    public String serialize(T object) {
        if (object == null) {
            return "null";
        }

        Map<String, Object> dataMap = new LinkedHashMap<>();
        Serializer<T> serializer = SerializerRegistry.lookSerializer(object.getClass().getSimpleName());

        serializer.writeToMap(object, dataMap);
        return new ObjectMapper().writer().writeValueAsString(dataMap);
    }

    public void writeToMap(T object, Map<String, Object> toMap) {
        if (object == null) {
            toMap.put(KEY_CLASS, null);
        } else {
            toMap.put(KEY_CLASS, object.getClass().getSimpleName());
        }
    }

    @SneakyThrows
    public T deserialize(String jsonData) {
        Map<String, Object> readValue = new ObjectMapper().readValue(jsonData, new TypeReference<Map<String, Object>>() {
        });

        String className = (String) readValue.get(KEY_CLASS);
        Serializer<T> serializer = SerializerRegistry.lookSerializer(className);
        T newObject = (T) SerializerRegistry.createByType(className);
        serializer.readData(readValue, newObject);
        return newObject;
    }

    public void readData(Map<String, Object> jsonMap, T object) {
    }
}

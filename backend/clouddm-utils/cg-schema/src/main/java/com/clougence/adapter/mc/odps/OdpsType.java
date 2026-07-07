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
package com.clougence.adapter.mc.odps;

import java.util.List;

/**
 * copy from odps driver com.aliyun.odps.OdpsType
 * Field type supported by the ODS table
 */
public enum OdpsType {

    /**
     * 8-byte symbol integer
     */
    BIGINT,

    /**
     * Double Float
     */
    DOUBLE,

    /**
     * Boolean
     */
    BOOLEAN,

    /**
     * Date Type
     */
    DATETIME,

    /**
     * String Type
     */
    STRING,

    /**
     * Accurate decimal type
     */
    DECIMAL,

    /**
     * MAP Type
     */
    MAP,

    /**
     * ARRAY type
     */
    ARRAY,

    /**
     * Empty
     */
    VOID,

    /**
     * 1 byte with symbol integer
     */
    TINYINT,

    /**
     * 2-byte symbol integer
     */
    SMALLINT,

    /**
     * 4-byte symbol integer
     */
    INT,

    /**
     * Single Float
     */
    FLOAT,

    /**
     * Fixed length string
     */
    CHAR,

    /**
     * Variable length string
     */
    VARCHAR,

    /**
     * Time type
     */
    DATE,

    /**
     * Timetamp
     */
    TIMESTAMP,

    /**
     * Bytes
     */
    BINARY,

    /**
     * Period
     */
    INTERVAL_DAY_TIME,

    /**
     * Year interval
     */
    INTERVAL_YEAR_MONTH,

    /**
     * Structure
     */
    STRUCT,

    /**
     * JSON Type
     */
    JSON,

    /**
     * Unsupported types from external systems
     */
    UNKNOWN;

    @Deprecated
    public static String getFullTypeString(OdpsType type, List<OdpsType> genericTypeList) {
        StringBuilder sb = new StringBuilder();
        sb.append(type.toString());
        if (genericTypeList != null && genericTypeList.size() != 0) {
            sb.append("<");
            for (OdpsType genericType : genericTypeList) {
                sb.append(genericType.toString()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(">");
        }
        return sb.toString();
    }
}

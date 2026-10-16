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
package com.clougence.clouddm.ds.kingbasees.execute.mysql;

import java.sql.JDBCType;
import java.util.Locale;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderUtils;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;

public class KingbaseESMySQLMetaProviderUtils extends MyMetaProviderUtils {

    @Override
    protected MySQLTypes safeToMySqlTypes(Object value) {
        MySQLTypes type = super.safeToMySqlTypes(value);
        if (type != null) {
            return type;
        }

        String typeName = StringUtils.trimToEmpty(StringUtils.toString(value)).toLowerCase(Locale.ROOT);
        int separator = typeName.lastIndexOf('.');
        if (separator >= 0) {
            typeName = typeName.substring(separator + 1);
        }
        return switch (typeName) {
            case "int2", "smallserial" -> MySQLTypes.SMALLINT;
            case "int4", "integer", "serial" -> MySQLTypes.INT;
            case "int8", "bigserial" -> MySQLTypes.BIGINT;
            case "numeric", "number" -> MySQLTypes.DECIMAL;
            case "float4", "real" -> MySQLTypes.FLOAT;
            case "float8", "double precision" -> MySQLTypes.DOUBLE;
            case "character varying" -> MySQLTypes.VARCHAR;
            case "bpchar", "character" -> MySQLTypes.CHAR;
            case "bool", "boolean" -> MySQLTypes.BIT;
            case "timestamp without time zone" -> MySQLTypes.DATETIME;
            case "timestamp with time zone" -> MySQLTypes.TIMESTAMP;
            case "oid", "uint4", "cardinal_number" -> MySQLTypes.BIGINT;
            case "uint8", "int16" -> MySQLTypes.DECIMAL;
            case "character_data", "sql_identifier", "yes_or_no", "name", "regproc", "regtype" -> MySQLTypes.VARCHAR;
            default -> throw new IllegalArgumentException("Unsupported KingbaseES MySQL physical type: " + typeName);
        };
    }

    @Override
    protected JDBCType columnTypeMappingToJdbcType(FieldType typeDef, String columnType) {
        JDBCType jdbcType = super.columnTypeMappingToJdbcType(typeDef, columnType);
        if (jdbcType != null) {
            return jdbcType;
        }
        return typeDef == null ? JDBCType.OTHER : typeDef.toJDBCType();
    }
}

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
package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.schema.editor.builder.mappings.MappingHandler;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:54
*/
public class SrTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        return buildColumnType(columnInfo, triggerContext, false);
    }

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext, boolean isPartitionCol) {
        StarRocksTypes sqlTypes = StarRocksTypes.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        String options = buildOptions(columnInfo);
        String columnNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()), isPartitionCol);

        // todo now not support defualt value for target starrocks

        return columnType + " " + columnNullable + " " + options;
    }

    public static String buildColumnTypeOnly(EColumn columnInfo, TriggerContext triggerContext) {
        StarRocksTypes sqlTypes = StarRocksTypes.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        return columnType.toUpperCase();
    }

    private static String buildOptions(EColumn columnInfo) {
        // no need to add extra options
        return "";
    }

    private static String buildNullable(boolean nullable, boolean isPartitionCol) {
        // StarRocks partition column can not be null.
        if (!nullable || isPartitionCol) {
            return "NOT NULL";
        } else {
            return "NULL";
        }
    }

    protected static String buildDefault(StarRocksTypes sqlTypes, EColumn columnInfo, MappingHandler mappingHandler, TriggerContext triggerContext) {
        StringBuilder sqlBuild = new StringBuilder("");
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (triggerContext.isSelf()) {
                return "DEFAULT " + defaultValue;
            }
            if (mappingHandler.hasFunctionMapping(defaultValue)) {
                String mappingTo = mappingHandler.functionMapping(defaultValue, columnInfo.getAttribute());
                if (StringUtils.isNotBlank(mappingTo)) {
                    sqlBuild.append("DEFAULT ").append(mappingTo);
                }
            }
            return sqlBuild.toString();
        }

        if (defaultValue.toLowerCase().contains("NOW()")) {
            // not support default now() value
            return "";
        }

        if (!"".equals(defaultValue)) {
            switch (sqlTypes) {
                case STRING:
                case VARCHAR:
                case CHAR: {
                    defaultValue = "'" + replaceSingleQuotes(defaultValue) + "'";
                    break;
                }
                default:
                    defaultValue = "'" + defaultValue + "'";
                    break;
            }

            sqlBuild.append("DEFAULT ").append(defaultValue);
        }

        return sqlBuild.toString();
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    private static String buildColumnType(StarRocksTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();

        switch (sqlTypes) {
            case TINYINT:
                return "tinyint";
            case SMALLINT:
                return "smallint";
            case INT:
                return "int";
            case BIGINT:
                return "bigint";
            case LARGEINT:
                return "largeint";
            case DECIMAL:
                return decimal(numericPrecision, numericScale, "decimal");
            case DECIMALV2:
                return decimal(numericPrecision, numericScale, "decimalv2");
            case DECIMAL32:
                return decimal(numericPrecision, numericScale, "decimal32");
            case DECIMAL64:
                return decimal(numericPrecision, numericScale, "decimal64");
            case DECIMAL128:
                return decimal(numericPrecision, numericScale, "decimal128");
            case DOUBLE:
                return "double";
            case FLOAT:
                return "float";
            case BOOLEAN:
                return "boolean";
            case CHAR:
                if (length != null && (length == 1 || length == 0)) {
                    return "char";
                } else {
                    // tips: starrocks char use bytes, need double the length
                    return "char" + ((length == null) ? "" : "(" + between(length, 1, 255) + ")");
                }
            case VARCHAR:
                if (length == null) {
                    return "string";
                } else if (length <= 0) {
                    return "varchar(1)";
                } else {
                    // tips: starrocks char use bytes, need double the length
                    return "varchar(" + between(length, 1, 65533) + ")";
                }
            case STRING:
                return "string";
            case DATE:
                return "date";
            case DATETIME:
                return "datetime";
            case JSON:
                return "json";
            case VARBINARY:
                if (length == null || length <= 0) {
                    return "varbinary";
                } else {
                    return "varbinary(" + between(length, 1, 1048576) + ")";
                }
            case HLL:
                return "hll";
            case BITMAP:
                return "bitmap";
            case ARRAY:
            default:
                throw new UnsupportedOperationException("Unsupported sql type " + sqlTypes);
        }
    }

    private static String decimal(Integer numericPrecision, Integer numericScale, String decimalType) {
        if (numericPrecision == null) {
            return decimalType;
        }
        if (numericPrecision > 38) {
            return "varchar(" + numericPrecision * 2 + ")";
        }
        // starRocks decimal type can not be primary key, if numericScale = 0 first convert largeInt
        if (numericScale == null || numericScale == 0) {
            return "largeint";
        }
        return decimalType + "(" + between(numericPrecision, 1, 38) + ", " + between(numericScale, 0, 38) + ")";
    }
}

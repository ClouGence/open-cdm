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
package com.clougence.clouddm.ds.ads.definition.ui.editor.table;

import static com.clougence.adapter.adbmysql.AdsMyUmiAttributeNames.COLUMN_TYPE;
import static com.clougence.adapter.adbmysql.AdsMyUmiAttributeNames.CURRENT_UPDATE_TYPE;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * https://help.aliyun.com/document_detail/123577.html
 */
class AdbMyTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        AdbMySQLTypes sqlTypes = AdbMySQLTypes.valueOfCode(columnInfo.getDbType());

        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        String options = buildOptions(columnInfo);
        String columnNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()));
        String defaultValue = buildDefault(sqlTypes, columnInfo, triggerContext);

        return columnType + " " + columnNullable + " " + defaultValue + " " + options;
    }

    private static String buildOptions(EColumn columnInfo) {
        AdbMyOnCurrentUpdateType currentUpdateType = AdbMyOnCurrentUpdateType.valueOfCode(CURRENT_UPDATE_TYPE.getValue(columnInfo.getAttribute()));

        StringBuilder optionBuilder = new StringBuilder("");

        if (columnInfo.isAutoGenerate()) {
            optionBuilder.append("auto_increment ");
        }
        if (currentUpdateType != null) {
            optionBuilder.append("on update ").append(currentUpdateType.getTypeName());
            if (columnInfo.getDatetimePrecision() != null && COLUMN_TYPE.getValue(columnInfo.getAttribute()).contains("(")) {
                optionBuilder.append("(").append(columnInfo.getDatetimePrecision()).append(")");
            }
        }

        return optionBuilder.toString();
    }

    private static String buildNullable(boolean nullable) {
        if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    private static String buildDefault(AdbMySQLTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        StringBuilder sqlBuild = new StringBuilder();
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        } else if ("".equals(defaultValue)) {
            switch (sqlTypes) {
                case TIME:
                case TIMESTAMP:
                case DATETIME:
                case DATE:
                    return "default null";
                default:
                    return "default ''";
            }
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                sqlBuild.append("default ").append(defaultValue);
            }
            return sqlBuild.toString();
        }

        switch (sqlTypes) {
            case VARCHAR:
            case JSON: {
                String replaced = replaceSingleQuotes(defaultValue);
                defaultValue = "'" + replaced + "'";
                break;
            }
            case TIME:
            case TIMESTAMP:
            case DATETIME:
            case DATE: {
                defaultValue = "'" + defaultValue + "'";
                break;
            }
            default:
                break;
        }

        sqlBuild.append("default ").append(defaultValue);
        return sqlBuild.toString();
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    private static String buildColumnType(AdbMySQLTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();

        switch (sqlTypes) {
            case BOOLEAN: {
                return "boolean";
            }
            case TINYINT: {
                return "tinyint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
            }
            case SMALLINT: {
                return "smallint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
            }
            case INT: {
                return "int" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
            }
            case BIGINT: {
                return "bigint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
            }
            case DECIMAL: {
                if (numericPrecision != null && numericScale != null) {
                    return "decimal(" + between(numericPrecision, 0, 1000) + ", " + between(numericScale, 0, 30) + ")";
                } else if (numericPrecision != null) {
                    return "decimal(" + between(numericPrecision, 0, 1000) + ")";
                } else {
                    return "decimal";
                }
            }
            case FLOAT: {
                return "float" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 0, 53) + ")");
            }
            case DOUBLE: {
                if (numericPrecision != null && numericScale != null) {
                    return "double(" + between(numericPrecision, 0, 255) + ", " + between(numericPrecision, 0, 30) + ")";
                } else if (numericPrecision != null) {
                    return "double(" + between(numericPrecision, 0, 255) + ")";
                } else {
                    return "double";
                }
            }
            case DATE: {
                return "date";
            }
            case DATETIME: {
                return "datetime" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
            }
            case TIMESTAMP: {
                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
            }
            case TIME: {
                return "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
            }
            case VARCHAR: {
                return "varchar";
            }
            case BINARY: {
                return "binary" + ((length == null) ? "" : "(" + between(length, 0, 255) + ")");
            }
            case JSON: {
                return "json";
            }
            case POINT: {
                return "point";
            }
            case ARRAY: {
                return "array"; // TODO more structs, like: array<int>
            }
            case MAP: {
                return "map"; // TODO more structs, like: map<string,int>
            }
            default: {
                return columnInfo.getDbType();
            }
        }
    }
}

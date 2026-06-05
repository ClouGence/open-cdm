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
package com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table;

import static com.clougence.adapter.sqlserver.SqlServerAttributeNames.COLLATION_NAME;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:58
*/
class MsSqlTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        SqlServerTypes sqlTypes = SqlServerTypes.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);

        String characterCollate = buildCharacterCollate(columnInfo, triggerContext);
        String options = buildOptions(columnInfo);
        String columnNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()), sqlTypes, mainVersion);
        String defaultValue = buildDefault(sqlTypes, mainVersion, columnInfo, triggerContext);
        return columnType + " " + characterCollate + " " + columnNullable + " " + defaultValue + " " + options;
    }

    protected static String buildCharacterCollate(EColumn columnInfo, TriggerContext triggerContext) {
        String value = COLLATION_NAME.getValue(columnInfo.getAttribute());
        return StringUtils.isNotBlank(value) ? "collate " + value : "";
    }

    private static String buildOptions(EColumn columnInfo) {
        if (columnInfo.isAutoGenerate()) {
            return "identity";
        }
        return "";
    }

    private static String buildNullable(boolean nullable, SqlServerTypes sqlTypes, MainVersion mainVersion) {
        if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    protected static String buildDefault(SqlServerTypes sqlTypes, MainVersion mainVersion, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                return "default " + defaultValue;
            }
            return "";
        }

        if (!"".equals(defaultValue)) {
            switch (sqlTypes) {
                case BIT: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        return "default " + defaultValue;
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "default 0x" + HexadecimalUtils.bit2hex(defaultValue);
                    } else {
                        return ""; //unknown
                    }
                }
                case DECIMAL:
                case NUMERIC:
                case SMALLINT:
                case TINYINT:
                case INT:
                case BIGINT:
                case SMALLMONEY:
                case MONEY:
                case FLOAT:
                case REAL:
                    //the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
                    return "default " + NumberUtils.trimZero(defaultValue);
                case DATE:
                case DATETIMEOFFSET:
                case DATETIME:
                case DATETIME2:
                case SMALLDATETIME:
                case TIME:
                    return "";//""default '" + replaceSingleQuotes(defaultValue) + "'";
                case CHAR:
                case VARCHAR:
                case TEXT:
                case NCHAR:
                case NVARCHAR:
                case NTEXT:
                case XML:
                    return "default '" + replaceSingleQuotes(defaultValue) + "'";
                case BINARY:
                case VARBINARY:
                case IMAGE: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        return "default " + defaultValue;
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "default 0x" + defaultValue.substring(2, defaultValue.length() - 1);
                    } else {
                        return ""; //unknown
                    }
                }
            }
        }
        return "";
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    protected static String buildColumnType(SqlServerTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();
        //
        switch (sqlTypes) {
            case BIT:
                return "bit";
            case DECIMAL:
            case NUMERIC: {
                if (numericPrecision != null && numericScale != null) {
                    return "decimal(" + between(numericPrecision, 0, 38) + ", " + between(numericScale, 0, 38) + ")";
                } else if (numericPrecision != null) {
                    return "decimal(" + between(numericPrecision, 0, 38) + ")";
                } else {
                    return "decimal";
                }
            }
            case SMALLINT:
                return "smallint";
            case TINYINT:
                return "tinyint";
            case INT:
                return "int";
            case BIGINT:
                return "bigint";
            case SMALLMONEY:
                return "smallmoney";
            case MONEY:
                return "money";
            case REAL:
                return "real";
            case FLOAT:
                return "float" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 53) + ")");
            case DATE:
                return "date";
            case DATETIMEOFFSET:
                return "datetimeoffset" + ((datetimePrecision == null || datetimePrecision == 7) ? "" : "(" + between(datetimePrecision, 0, 7) + ")");
            case DATETIME:
                return "datetime";
            case DATETIME2:
                return "datetime2" + ((datetimePrecision == null || datetimePrecision == 7) ? "" : "(" + between(datetimePrecision, 0, 7) + ")");
            case SMALLDATETIME:
                return "smalldatetime";
            case TIME:
                return "time" + ((datetimePrecision == null || datetimePrecision == 7) ? "" : "(" + between(datetimePrecision, 0, 7) + ")");
            case TIMESTAMP:
            case ROWVERSION:
                return "rowversion";
            case CHAR:
                return "char" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 8000) + ")");
            case VARCHAR: {
                if (length == null || length == 1) {
                    return "varchar";
                } else if (length == -1 || length > 8000) {
                    return "varchar(max)";
                } else {
                    return "varchar(" + between(length, 1, 8000) + ")";
                }
            }
            case TEXT:
                return "varchar(max)";
            case NCHAR:
                return "nchar" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 4000) + ")");
            case NVARCHAR: {
                if (length == null || length == 1) {
                    return "nvarchar";
                } else if (length == -1 || length > 4000) {
                    return "nvarchar(max)";
                } else {
                    return "nvarchar(" + between(length, 1, 4000) + ")";
                }
            }
            case NTEXT:
                return "nvarchar(max)";
            case BINARY:
                return "binary" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 8000) + ")");
            case VARBINARY: {
                if (length == null || length == 1) {
                    return "varbinary";
                } else if (length == -1 || length > 8000) {
                    return "varbinary(max)";
                } else {
                    return "varbinary(" + between(length, 1, 8000) + ")";
                }
            }
            case IMAGE:
                return "varbinary(max)";
            case HIERARCHYID:
                return "hierarchyid";
            case UNIQUEIDENTIFIER:
                return "uniqueidentifier";
            case SQL_VARIANT:
                return "sql_variant";
            case XML:
                return "xml";
            case GEOMETRY:
                return "geometry";
            case GEOGRAPHY:
                return "geography";
            default:
                return columnInfo.getDbType();
        }
    }
}

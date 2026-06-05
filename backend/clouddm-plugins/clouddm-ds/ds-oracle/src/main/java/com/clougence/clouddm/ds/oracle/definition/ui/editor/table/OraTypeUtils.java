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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.table;

import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:56
*/
class OraTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext, EPrimaryKey ePrimaryKey) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        OracleSqlTypes sqlTypes = OracleSqlTypes.valueOfCode(columnInfo.getDbType());

        String columnType = buildColumnType(sqlTypes, columnInfo, mainVersion);
        String buildNullable = buildNullable(columnInfo, ePrimaryKey, Boolean.TRUE.equals(columnInfo.getNullable()));
        String buildDefault = buildDefault(sqlTypes, columnInfo, triggerContext);

        return columnType + " " + buildDefault + " " + buildNullable;
    }

    private static String buildNullable(EColumn eColumn, EPrimaryKey ePrimaryKey, boolean nullable) {
        if (ePrimaryKey != null && ePrimaryKey.getColumnList().contains(eColumn.getName())) {
            return "NOT NULL";
        } else if (!nullable) {
            return "NOT NULL";
        } else {
            return "NULL";
        }
    }

    protected static String buildDefault(OracleSqlTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                return "default " + defaultValue;
            } else {
                return "";
            }
        }

        if (!"".equals(defaultValue)) {
            switch (sqlTypes) {
                case XMLTYPE:
                case CHAR:
                case NCHAR:
                case VARCHAR2:
                case NVARCHAR:
                case NVARCHAR2:
                case CLOB:
                case NCLOB:
                    return "default '" + replaceSingleQuotes(defaultValue) + "'";
                case DATE:
                case TIMESTAMP:
                case TIMESTAMP_WITH_TIME_ZONE:
                case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                    return "default '" + defaultValue + "'";
                case RAW:
                case LONG_RAW:
                case BLOB: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        return "default '" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "default '" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
                    } else {
                        return ""; // unknown
                    }
                }
                case NUMBER_BIGINT:
                case NUMBER_DECIMAL:
                    //the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
                    return "default " + NumberUtils.trimZero(defaultValue);
                default:
                    return "default " + defaultValue;
            }
        }
        return "";
    }

    protected static String buildColumnType(OracleSqlTypes sqlTypes, EColumn columnInfo, MainVersion mainVersion) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();

        switch (sqlTypes) {
            case CHAR: {
                return "char" + ((length == null) ? "" : "(" + between(length, 1, 2000) + ")");
            }
            case NCHAR: {
                return "nchar" + ((length == null) ? "" : "(" + between(length, 1, 2000) + ")");
            }
            case VARCHAR2: {
                return "varchar2" + ((length == null) ? "" : "(" + between(length, 1, 4000) + ")");
            }
            case NVARCHAR:
            case NVARCHAR2: {
                return "nvarchar2" + ((length == null) ? "" : "(" + between(length, 1, 2000) + ")");
            }
            case CLOB: {
                return "clob";
            }
            case NCLOB: {
                return "nclob";
            }
            // case NUMBER:
            case NUMBER_BIGINT:
            case NUMBER_DECIMAL: {
                if (numericPrecision != null && numericScale != null) {
                    return "number(" + between(numericPrecision, 1, 38) + ", " + between(numericScale, -84, 127) + ")";
                } else if (numericPrecision == null && numericScale != null) {
                    return "number(*, " + between(numericScale, -84, 127) + ")";
                } else if (numericPrecision != null) {
                    return "number(" + between(numericPrecision, 1, 38) + ")";
                } else {
                    return "number";
                }
            }
            case FLOAT: {
                return "float" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 126) + ")");
            }
            case BINARY_FLOAT: {
                return "binary_float";
            }
            case BINARY_DOUBLE: {
                return "binary_double";
            }
            case DATE: {
                return "date";
            }
            case TIMESTAMP: {
                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 9) + ")");
            }
            case TIMESTAMP_WITH_TIME_ZONE: {
                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 9) + ")") + " with time zone";
            }
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE: {
                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 9) + ")") + " with local time zone";
            }
            case INTERVAL_YEAR_TO_MONTH: {
                return "interval year to month";// interval year(9) to month
            }
            case INTERVAL_DAY_TO_SECOND: {
                return "interval day to second";// interval day(9) to second(9)
            }
            case RAW: {
                if (length == null) {
                    return "raw(2000)";
                } else {
                    return "raw(" + between(length, 1, 2000) + ")";
                }
            }
            case LONG_RAW: {
                return "long raw";
            }
            case LONG: {
                return "long";
            }
            case BLOB: {
                return "blob";
            }
            case BFILE: {
                return "bfile";
            }
            case ROWID: {
                return "rowid";
            }
            case UROWID: {
                return "urowid";// urowid(xx) not supported.
            }
            case XMLTYPE: {
                return "xmltype";
            }
            case OBJECT:
            case REF:
            case VARRAY:
            case NESTED_TABLE:
            case PLSQL_BOOLEAN:
            case ANYTYPE:
            case ANYDATA:
            case ANYDATASET:
            case HTTPURITYPE:
            case XDBURITYPE:
            case DBURITYPE:
            case SDO_GEOMETRY:
            case SDO_TOPO_GEOMETRY:
            case SDO_GEORASTER:
            case ORDAUDIO:
            case ORDDICOM:
            case ORDDOC:
            case ORDIMAGE:
            case ORDVIDEO:
            case SI_AVERAGE_COLOR:
            case SI_COLOR:
            case SI_COLOR_HISTOGRAM:
            case SI_FEATURE_LIST:
            case SI_POSITIONAL_COLOR:
            case SI_STILL_IMAGE:
            case SI_TEXTURE:
            default:
                return columnInfo.getDbType();
        }
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }
}

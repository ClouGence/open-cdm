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
package com.clougence.clouddm.ds.oceanbase.definition.ob4my.ui.editor.table;

import static com.clougence.adapter.ob.obformysql.ObForMySQLAttributeNames.*;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.ob.obformysql.ObForMySQLOnCurrentUpdateType;
import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:56
*/
public class ObTypeUtils {

    public static String buildColumnType(EColumn columnInfo, EPrimaryKey primaryKey, TriggerContext triggerContext) {
        ObForMySQLTypes sqlTypes = ObForMySQLTypes.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        String defaultValue = buildDefault(sqlTypes, mainVersion, columnInfo, triggerContext);
        String options = buildOptions(columnInfo, defaultValue);
        String columnNullable = buildNullable(columnInfo, primaryKey);
        return columnType + " " + columnNullable + " " + defaultValue + " " + options;
    }

    private static String buildOptions(EColumn columnInfo, String defaultValue) {
        ObForMySQLOnCurrentUpdateType currentUpdateType = ObForMySQLOnCurrentUpdateType.valueOfCode(CURRENT_UPDATE_TYPE.getValue(columnInfo.getAttribute()));
        if (StringUtils.startsWithIgnoreCase(defaultValue, "default ")) {
            defaultValue = defaultValue.substring("default ".length());
        }

        StringBuilder optionBuilder = new StringBuilder("");

        if (columnInfo.isAutoGenerate()) {
            optionBuilder.append("auto_increment ");
        }
        if (currentUpdateType != null) {
            if (currentUpdateType == ObForMySQLOnCurrentUpdateType.DefaultGenerated) {
                optionBuilder.append("on update ").append(defaultValue);
            } else if (StringUtils.startsWithIgnoreCase(defaultValue, "current_timestamp") && currentUpdateType == ObForMySQLOnCurrentUpdateType.CurrentTimestamp) {
                optionBuilder.append("on update ").append(defaultValue);
            } else {
                optionBuilder.append("on update ").append(currentUpdateType.getTypeName());
                if (columnInfo.getDatetimePrecision() != null && COLUMN_TYPE.getValue(columnInfo.getAttribute()).contains("(")) {
                    optionBuilder.append("(" + columnInfo.getDatetimePrecision() + ")");
                }
            }
        }

        return optionBuilder.toString();
    }

    private static String buildNullable(EColumn columnInfo, EPrimaryKey primaryKey) {
        boolean isPrimaryKey = primaryKey != null && primaryKey.getColumnList().contains(columnInfo.getName());
        if (!Boolean.TRUE.equals(columnInfo.getNullable()) || isPrimaryKey) {
            return "not null";
        } else {
            return "null";
        }
    }

    protected static String buildDefault(ObForMySQLTypes sqlTypes, MainVersion mainVersion, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                if (StringUtils.equalsIgnoreCase(defaultValue, "curdate()") || StringUtils.equalsIgnoreCase(defaultValue, "curtime()")) {
                    return "default (" + defaultValue + ")";
                } else if (defaultValue.toUpperCase().matches("CURRENT_TIMESTAMP(\\((\\d+)?\\))?")) {
                    Integer precision = columnInfo.getDatetimePrecision();
                    if (precision != null) {
                        return "default CURRENT_TIMESTAMP(" + NumberUtils.between(precision, 0, 6) + ")";
                    } else {
                        return "default CURRENT_TIMESTAMP";
                    }
                } else {
                    return "default " + defaultValue;
                }
            } else {
                return "";
            }
        }

        if ("".equals(defaultValue)) {
            switch (sqlTypes) {
                case TIMESTAMP:
                    return "default null";
                default:
                    return "default ''";
            }
        }

        switch (sqlTypes) {
            case BIT: {
                if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                    return "default b'" + HexadecimalUtils.hex2bit(defaultValue.substring(2)) + "'";
                } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                    return "default " + defaultValue;
                } else {
                    return ""; // unknown
                }
            }
            case BINARY:
            case VARBINARY: {
                if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                    return "default " + defaultValue;
                } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                    return "default " + defaultValue;
                } else {
                    return ""; // unknown
                }
            }
            // BLOB, TEXT, GEOMETRY or JSON column 'xxxxx' can't have a default value (5.6/5.7/8.0)
            case BLOB:
            case TINYBLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case TEXT:
            case TINYTEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
                return "";
            case VARCHAR:
            case CHAR:
            case SET:
            case ENUM:
                return "default '" + replaceSingleQuotes(defaultValue) + "'";
            case TINYINT:
            case SMALLINT:
            case MEDIUMINT:
            case INT:
            case BIGINT:
            case DECIMAL:
            case FLOAT:
            case DOUBLE:
                // the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
                return "default " + NumberUtils.trimZero(defaultValue);
            case TIME:
            case TIMESTAMP:
            case DATETIME:
            case DATE:
            case YEAR:
                return "default '" + defaultValue + "'";
            default:
                return "default " + defaultValue;
        }
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    private static String buildColumnType(ObForMySQLTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();
        // tips use source ds a
        String columnType = COLUMN_TYPE.getValue(columnInfo.getAttribute());
        String enumSetValue = DEFAULT_ENUMSET_COLLATION.getValue(columnInfo.getAttribute());
        String[] enumSetList = null;
        if (StringUtils.isNotBlank(enumSetValue)) {
            String content = enumSetValue.substring(1, enumSetValue.length() - 1);
            enumSetList = content.split(",");
        }
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();

        switch (sqlTypes) {
            case TINYINT: {
                if ("tinyint(4)".equalsIgnoreCase(columnType)) {
                    return "tinyint";
                } else if (StringUtils.startsWithIgnoreCase(columnType, "tinyint(3) ")) {
                    if (columnType.toLowerCase().contains("unsigned") && columnType.toLowerCase().contains("zerofill")) {
                        return "tinyint unsigned zerofill";
                    } else if (columnType.toLowerCase().contains("unsigned")) {
                        return "tinyint unsigned";
                    } else if (columnType.toLowerCase().contains("zerofill")) {
                        return "tinyint zerofill";
                    } else {
                        return columnType;
                    }
                } else {
                    return "tinyint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
                }
            }
            case SMALLINT: {
                if ("smallint(6)".equalsIgnoreCase(columnType)) {
                    return "smallint";
                } else if (StringUtils.startsWithIgnoreCase(columnType, "smallint(5) ")) {
                    if (columnType.toLowerCase().contains("unsigned") && columnType.toLowerCase().contains("zerofill")) {
                        return "smallint unsigned zerofill";
                    } else if (columnType.toLowerCase().contains("unsigned")) {
                        return "smallint unsigned";
                    } else if (columnType.toLowerCase().contains("zerofill")) {
                        return "smallint zerofill";
                    } else {
                        return columnType;
                    }
                } else {
                    return "smallint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
                }
            }
            case MEDIUMINT: {
                if ("mediumint(9)".equalsIgnoreCase(columnType)) {
                    return "mediumint";
                } else if (StringUtils.startsWithIgnoreCase(columnType, "mediumint(8) ")) {
                    if (columnType.toLowerCase().contains("unsigned") && columnType.toLowerCase().contains("zerofill")) {
                        return "mediumint unsigned zerofill";
                    } else if (columnType.toLowerCase().contains("unsigned")) {
                        return "mediumint unsigned";
                    } else if (columnType.toLowerCase().contains("zerofill")) {
                        return "mediumint zerofill";
                    } else {
                        return columnType;
                    }
                } else {
                    return "mediumint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
                }
            }
            case INT: {
                if ("int(11)".equalsIgnoreCase(columnType)) {
                    return "int";
                } else if (StringUtils.startsWithIgnoreCase(columnType, "int(10) ")) {
                    if (columnType.toLowerCase().contains("unsigned") && columnType.toLowerCase().contains("zerofill")) {
                        return "int unsigned zerofill";
                    } else if (columnType.toLowerCase().contains("unsigned")) {
                        return "int unsigned";
                    } else if (columnType.toLowerCase().contains("zerofill")) {
                        return "int zerofill";
                    } else {
                        return columnType;
                    }
                } else {
                    return "int" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
                }
            }
            case BIGINT: {
                if (StringUtils.startsWithIgnoreCase(columnType, "bigint(20)")) {
                    if ("bigint(20)".equalsIgnoreCase(columnType)) {
                        return "bigint";
                    } else if (columnType.toLowerCase().contains("unsigned") && columnType.toLowerCase().contains("zerofill")) {
                        return "bigint unsigned zerofill";
                    } else if (columnType.toLowerCase().contains("unsigned")) {
                        return "bigint unsigned";
                    } else if (columnType.toLowerCase().contains("zerofill")) {
                        return "bigint zerofill";
                    } else {
                        return columnType;
                    }
                } else {
                    return "bigint" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 255) + ")");
                }
            }
            case DECIMAL: {
                if (StringUtils.startsWithIgnoreCase(columnType, "decimal(10,0)")) {
                    if ("decimal(10,0)".equalsIgnoreCase(columnType)) {
                        return "decimal";
                    } else if (columnType.toLowerCase().contains("unsigned") && columnType.toLowerCase().contains("zerofill")) {
                        return "decimal unsigned zerofill";
                    } else if (columnType.toLowerCase().contains("unsigned")) {
                        return "decimal unsigned";
                    } else if (columnType.toLowerCase().contains("zerofill")) {
                        return "decimal zerofill";
                    } else {
                        return columnType;
                    }
                } else if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else if (numericPrecision != null && numericScale != null) {
                    return "decimal(" + between(numericPrecision, 0, 65) + ", " + between(numericScale, 0, 30) + ")";
                } else if (numericPrecision != null) {
                    return "decimal(" + between(numericPrecision, 0, 65) + ")";
                } else {
                    return "decimal";
                }
            }
            case FLOAT: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "float" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 0, 53) + ")");
                }
            }
            case DOUBLE: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "double";
                }
            }
            case DATE: {
                return "date";
            }
            case DATETIME: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "datetime" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                }
            }
            case TIMESTAMP: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                }
            }
            case TIME: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                }
            }
            case YEAR: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "year" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 4, 4) + ")");
                }
            }
            case CHAR: {
                if (length != null && length == 1) {
                    return "char";
                } else {
                    return "char" + ((length == null) ? "" : "(" + between(length, 0, 255) + ")");
                }
            }
            case VARCHAR: {
                if (length == null) {
                    return "text";
                } else {
                    return "varchar" + "(" + between(length, 0, 262144) + ")";
                }
            }
            case BINARY: {
                return "binary" + ((length == null) ? "" : "(" + between(length, 0, 255) + ")");
            }
            case VARBINARY: {
                return "varbinary" + ((length == null) ? "" : "(" + between(length, 0, 1024 * 1024 * 1024) + ")");
            }
            case TINYBLOB: {
                return "tinyblob";
            }
            case BLOB: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return "blob" + ((length == null) ? "" : "(" + between(length, 0, 4294967295L) + ")");
                }
            }
            case MEDIUMBLOB: {
                return "mediumblob";
            }
            case LONGBLOB: {
                return "longblob";
            }
            case TINYTEXT: {
                return "tinytext";
            }
            case TEXT: {
                return "text";
            }
            case MEDIUMTEXT: {
                return "mediumtext";
            }
            case LONGTEXT: {
                return "longtext";
            }
            case ENUM: {
                if (enumSetList != null) {
                    StringBuilder enumValue = new StringBuilder("enum(");
                    for (int i = 0; i < enumSetList.length; i++) {
                        if (i != enumSetList.length - 1) {
                            enumValue.append("'").append(enumSetList[i].trim()).append("',");
                        } else {
                            enumValue.append("'").append(enumSetList[i].trim()).append("')");
                            return enumValue.toString();
                        }
                    }
                }
                return "enum";
            }
            case SET: {
                if (enumSetList != null) {
                    StringBuilder enumValue = new StringBuilder("set(");
                    for (int i = 0; i < enumSetList.length; i++) {
                        if (i != enumSetList.length - 1) {
                            enumValue.append("'").append(enumSetList[i].trim()).append("',");
                        } else {
                            enumValue.append("'").append(enumSetList[i].trim()).append("')");
                            return enumValue.toString();
                        }
                    }
                }
                return "set";
            }
            default: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return columnInfo.getDbType();
                }
            }
        }
    }
}

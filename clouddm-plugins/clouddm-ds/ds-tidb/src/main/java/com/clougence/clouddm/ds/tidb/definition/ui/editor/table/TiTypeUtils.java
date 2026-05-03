package com.clougence.clouddm.ds.tidb.definition.ui.editor.table;

import static com.clougence.adapter.tidb.TiDBAttributeNames.*;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.tidb.TiDBOnCurrentUpdateType;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.schema.editor.builder.CharsetsAndCollations;
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
class TiTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        TiDBTypes sqlTypes = TiDBTypes.valueOfCode(columnInfo.getDbType());

        String characterCollate = buildCharacterCollate(columnInfo, triggerContext);
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        String options = buildOptions(columnInfo, sqlTypes);
        String columnNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()), sqlTypes, mainVersion);
        String defaultValue = buildDefault(sqlTypes, mainVersion, columnInfo, triggerContext);

        return columnType + " " + characterCollate + " " + columnNullable + " " + defaultValue + " " + options;
    }

    public static String buildColumnType(MainVersion mainVersion, EColumn columnInfo) {
        TiDBTypes sqlTypes = TiDBTypes.valueOfCode(columnInfo.getDbType());
        return buildColumnType(sqlTypes, columnInfo, null);
    }

    private static String buildCharacterCollate(EColumn columnInfo, TriggerContext triggerContext) {
        CharsetsAndCollations charsetAndCollationMap = triggerContext.getTarDsInfo().getCharsetAndCollation();
        StringBuilder optionsBuilder = new StringBuilder("");

        String characterSetName = DEFAULT_CHARACTER_SET_NAME.getValue(columnInfo.getAttribute());
        if (charsetAndCollationMap.containsCharset(characterSetName)) {
            optionsBuilder.append(" character set ").append(characterSetName);
        }

        String collationName = DEFAULT_COLLATION_NAME.getValue(columnInfo.getAttribute());
        if (charsetAndCollationMap.containsCollation(collationName)) {
            optionsBuilder.append(" collate ").append(collationName);
        }
        return optionsBuilder.toString();
    }

    private static String buildOptions(EColumn columnInfo, TiDBTypes sqlTypes) {
        TiDBOnCurrentUpdateType currentUpdateType = TiDBOnCurrentUpdateType.valueOfCode(CURRENT_UPDATE_TYPE.getValue(columnInfo.getAttribute()));

        StringBuilder optionBuilder = new StringBuilder("");

        if (columnInfo.isAutoGenerate()) {
            optionBuilder.append("auto_increment ");
        }
        if (currentUpdateType != null) {
            optionBuilder.append("on update ").append(currentUpdateType.getTypeName());
            if (columnInfo.getDatetimePrecision() != null && COLUMN_TYPE.getValue(columnInfo.getAttribute()).contains("(")) {
                optionBuilder.append("(" + columnInfo.getDatetimePrecision() + ")");
            }
        }

        return optionBuilder.toString();
    }

    private static String buildNullable(boolean nullable, TiDBTypes sqlTypes, MainVersion mainVersion) {
        if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    public static String buildDefault(TiDBTypes sqlTypes, MainVersion mainVersion, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        } else if ("".equals(defaultValue)) {
            switch (sqlTypes) {
                case TIMESTAMP:
                    return "default null";
                default:
                    return "default ''";
            }
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                if (sqlTypes.isNumber() && !NumberUtils.isNumber(defaultValue)) {
                    // tidb unsupport like this "f_number_expr decimal null default (123 + 321)"
                    return "";
                }

                if (StringUtils.equalsIgnoreCase(defaultValue, "curdate()") || StringUtils.equalsIgnoreCase(defaultValue, "curtime()")) {
                    return "default (" + defaultValue + ")";
                } else {
                    return "default " + defaultValue;
                }
            } else {
                return "";
            }
        }

        switch (sqlTypes) {
            case BIT: {
                if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                    return "default b'" + HexadecimalUtils.hex2bit(defaultValue.substring(2)) + "'";
                } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                    return "default " + defaultValue;
                } else {
                    return ""; //unknown
                }
            }
            case BINARY:
            case VARBINARY: {
                if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                    return "default " + defaultValue;
                } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                    return "default " + defaultValue;
                } else {
                    return ""; //unknown
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
            case JSON:
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

    private static String buildColumnType(TiDBTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();
        String columnType = COLUMN_TYPE.getValue(columnInfo.getAttribute());
        String enumSetValue = DEFAULT_ENUMSET_COLLATION.getValue(columnInfo.getAttribute());
        String[] enumSetList = null;
        if (StringUtils.isNotBlank(enumSetValue)) {
            String content = enumSetValue.substring(1, enumSetValue.length() - 1);
            enumSetList = content.split(",");
        }
        switch (sqlTypes) {
            case BIT: {
                if ("bit(1)".equalsIgnoreCase(columnType)) {
                    return "bit";
                }
                return "bit" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 1, 64) + ")");
            }
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
                } else if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
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
                } else if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
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
                } else if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
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
                } else if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
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
                } else if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
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
                // 1. TIMESTAMP
                // 2. TIMESTAMP(6)
                // 3. TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else if (columnInfo.getDefaultValue() == null) {
                    return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                } else {
                    return "timestamp";
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
                return "varchar" + ((length == null) ? "" : "(" + between(length, 0, 16383) + ")");
            }
            case BINARY: {
                return "binary" + ((length == null) ? "" : "(" + between(length, 0, 255) + ")");
            }
            case VARBINARY: {
                return "varbinary" + ((length == null) ? "" : "(" + between(length, 0, 65535) + ")");
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
            case JSON: {
                return "json";
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

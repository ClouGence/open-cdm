package com.clougence.clouddm.ds.polardb.definition.pormy.editor.table;

import static com.clougence.adapter.polar.pormy.PolarDBMyAttributeNames.*;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.polar.pormy.PolarDBMyOnCurrentUpdateType;
import com.clougence.adapter.polar.pormy.PolarDBMyTypes;
import com.clougence.schema.editor.builder.CharsetsAndCollations;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

class PorMyTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        PolarDBMyTypes sqlTypes = PolarDBMyTypes.valueOfCode(columnInfo.getDbType());

        String characterCollate = buildCharacterCollate(columnInfo, triggerContext);
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        String options = buildOptions(columnInfo);
        String columnNullable = buildNullable(columnInfo.getNullable(), sqlTypes, mainVersion);
        String defaultValue = buildDefault(sqlTypes, mainVersion, columnInfo, triggerContext);

        return columnType + " " + characterCollate + " " + columnNullable + " " + defaultValue + " " + options;
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

    private static String buildOptions(EColumn columnInfo) {
        PolarDBMyOnCurrentUpdateType currentUpdateType = PolarDBMyOnCurrentUpdateType.valueOfCode(CURRENT_UPDATE_TYPE.getValue(columnInfo.getAttribute()));

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

    private static String buildNullable(Boolean nullable, PolarDBMyTypes sqlTypes, MainVersion mainVersion) {
        if (nullable == null) {
            return "";
        }
        if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    private static boolean isDate(PolarDBMyTypes sqlTypes) {
        switch (sqlTypes) {
            case TIME:
            case TIMESTAMP:
            case DATETIME:
            case DATE:
                return true;
            default:
                return false;
        }
    }

    private static String buildDefault(PolarDBMyTypes sqlTypes, MainVersion mainVersion, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue) && isDate(sqlTypes)) {
                if (StringUtils.equalsIgnoreCase(defaultValue, "curdate()") || StringUtils.equalsIgnoreCase(defaultValue, "curtime()")) {
                    return "default (" + defaultValue + ")";
                } else {
                    return "default " + defaultValue;
                }
            } else {
                return "";
            }
        }

        if (!"".equals(defaultValue)) {
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
                case GEOMETRY:
                case POINT:
                case LINESTRING:
                case POLYGON:
                case MULTIPOINT:
                case MULTILINESTRING:
                case MULTIPOLYGON:
                case GEOMETRY_COLLECTION:
                case GEOM_COLLECTION:
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
                    //the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
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
        return "";
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    private static String buildColumnType(PolarDBMyTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();
        String columnType = COLUMN_TYPE.getValue(columnInfo.getAttribute());
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();

        // https://dev.mysql.com/doc/refman/5.7/en/fractional-seconds.html
        //      the default precision is 0. (This differs from the standard SQL default of 6, for compatibility with previous MySQL versions.)
        if (datetimePrecision != null && datetimePrecision == 0) {
            datetimePrecision = null; // if precision is 0 will be set no fsp param, use mysql default precision
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
                } else if (numericPrecision != null && numericScale != null) {
                    return "decimal(" + between(numericPrecision, 0, 65) + ", " + between(numericScale, 0, 30) + ")";
                } else if (numericPrecision != null) {
                    return "decimal(" + between(numericPrecision, 0, 65) + ")";
                } else {
                    return "decimal";
                }
            }
            case FLOAT: {
                return "float" + ((numericPrecision == null) ? "" : "(" + between(numericPrecision, 0, 53) + ")");
            }
            case DOUBLE: {
                return "double";
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
            case YEAR: {
                return "year" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 4, 4) + ")");
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
                    return "varchar" + "(" + between(length, 0, 16383) + ")";
                }
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
                return "blob" + ((length == null) ? "" : "(" + between(length, 0, 4294967295L) + ")");
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
            case GEOMETRY: {
                return "geometry";
            }
            case POINT: {
                return "point";
            }
            case LINESTRING: {
                return "linestring";
            }
            case POLYGON: {
                return "polygon";
            }
            case GEOMETRY_COLLECTION: {
                return "geometrycollection";
            }
            case GEOM_COLLECTION: {
                return "geomcollection";
            }
            case MULTIPOINT: {
                return "multipoint";
            }
            case MULTILINESTRING: {
                return "multilinestring";
            }
            case MULTIPOLYGON: {
                return "multipolygon";
            }
            case ENUM:
            case SET:
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

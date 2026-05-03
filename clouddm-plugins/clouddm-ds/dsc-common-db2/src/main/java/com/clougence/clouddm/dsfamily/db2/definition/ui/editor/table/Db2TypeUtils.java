package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table;

import static com.clougence.utils.NumberUtils.between;
import static com.clougence.utils.NumberUtils.trimZero;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:55
*/
class Db2TypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext, EPrimaryKey ePrimaryKey) {
        Db2Types sqlTypes = Db2Types.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);

        String options = buildOptions(columnInfo);
        String columnNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()), columnInfo, ePrimaryKey);
        String defaultValue = buildDefault(sqlTypes, columnInfo, triggerContext);
        return columnType + " " + columnNullable + " " + defaultValue + " " + options;
    }

    protected static String buildOptions(EColumn columnInfo) {
        if (columnInfo.isAutoGenerate()) {
            return "generated always as identity";
        }
        return "";
    }

    private static String buildNullable(boolean nullable, EColumn columnInfo, EPrimaryKey ePrimaryKey) {
        if (ePrimaryKey != null && ePrimaryKey.getColumnList().contains(columnInfo.getName())) {
            return "not null";
        } else if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    protected static String buildDefault(Db2Types sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        } else if ("".equals(defaultValue)) {
            switch (sqlTypes) {
                case TIMESTAMP:
                case DATE:
                case TIME:
                    return "default null";
                default:
                    return "default ''";
            }
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                return "default " + defaultValue;
            }
            return "";
        }

        if (!"".equals(defaultValue)) {
            switch (sqlTypes) {
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                case REAL:
                case DOUBLE:
                case DECIMAL:
                    // the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
                    return "default " + trimZero(defaultValue);
                case DATE:
                case TIME:
                case TIMESTAMP:
                case CHARACTER:
                case VARCHAR:
                case LONG_VARCHAR:
                case GRAPHIC:
                case LONG_VARGRAPHIC:
                case CLOB:
                case DBCLOB:
                case CHAR_FOR_BIT_DATA:
                case VARCHAR_FOR_BIT_DATA:
                case LONG_VARCHAR_FOR_BIT_DATA:
                    return "default '" + replaceSingleQuotes(defaultValue) + "'";
                default:
                    break;
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

    protected static String buildColumnType(Db2Types sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();
        //
        switch (sqlTypes) {
            case SMALLINT:
                return "smallint";
            case INTEGER:
                return "integer";
            case BIGINT:
                return "bigint";
            case REAL:
                return "real";
            case DOUBLE:
                return "double";
            case DECIMAL: {
                if (numericPrecision != null && numericScale != null) {
                    return "decimal(" + between(numericPrecision, 0, 31) + ", " + between(numericScale, 0, 31) + ")";
                } else if (numericPrecision != null) {
                    return "decimal(" + between(numericPrecision, 0, 31) + ")";
                } else {
                    return "decimal";
                }
            }
            case CHARACTER:
                return "character" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 255) + ")");
            case VARCHAR: {
                if (length == null || length == 1) {
                    return "varchar(32672)";
                } else if (length == -1 || length > 8000) {
                    return "varchar(32672)";
                } else {
                    return "varchar(" + between(length, 1, 8000) + ")";
                }
            }
            case LONG_VARCHAR: {
                return "long varchar";
            }
            case GRAPHIC:
                return "graphic" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 127) + ")");
            case VARGRAPHIC:
                if (length == null || length == 1) {
                    return "vargraphic";
                } else if (length == -1 || length > 8000) {
                    return "vargraphic(16336)";
                } else {
                    return "vargraphic(" + between(length, 1, 8000) + ")";
                }
            case LONG_VARGRAPHIC:
                return "long vargraphic";
            case CLOB: {
                if (length == null || length == 1) {
                    return "clob";
                } else if (length == -1 || length > 8000) {
                    return "clob(1048576)";
                } else {
                    return "clob(" + between(length, 1, 8000) + ")";
                }
            }
            case DBCLOB: {
                if (length == null || length == 1) {
                    return "dbclob";
                } else if (length == -1 || length > 8000) {
                    return "dbclob(1048576)";
                } else {
                    return "dbclob(" + between(length, 1, 8000) + ")";
                }
            }
            case XML:
                return "xml";
            case CHAR_FOR_BIT_DATA: {
                if (length == null || length == 1) {
                    return "character for bit data";
                } else if (length == -1 || length > 8000) {
                    return "character(255) for bit data";
                } else {
                    return "character(" + between(length, 1, 8000) + ") for bit data";
                }
            }
            case VARCHAR_FOR_BIT_DATA: {
                if (length == null || length == 1) {
                    return "varchar for bit data";
                } else if (length == -1 || length > 8000) {
                    return "varchar(32672) for bit data";
                } else {
                    return "varchar(" + between(length, 1, 8000) + ") for bit data";
                }
            }
            case LONG_VARCHAR_FOR_BIT_DATA: {
                return "long varchar for bit data";
            }
            case BLOB: {
                if (length == null || length == 1) {
                    return "blob";
                } else if (length == -1 || length > 8000) {
                    return "blob(1048576)";
                } else {
                    return "blob(" + between(length, 1, 8000) + ")";
                }
            }
            case TIME:
                return "time";
            case DATE:
                return "date";
            case TIMESTAMP:
                return "timestamp" + ((datetimePrecision == null || datetimePrecision == 1) ? "" : "(" + between(datetimePrecision, 1, 12) + ")");
            case BOOLEAN:
                return "boolean";
            default:
                return columnInfo.getDbType();
        }
    }
}

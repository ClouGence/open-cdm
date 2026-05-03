package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import static com.clougence.adapter.hana.HanaTypes.*;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author wanshao create time is 2021/12/3
 **/
public class HanaTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        HanaTypes sqlTypes = HanaTypes.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        String options = buildOptions(columnInfo);
        String colNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()));
        String colDefault = buildDefault(columnInfo);

        return columnType + options + colNullable + colDefault;
    }

    private static String buildDefault(EColumn columnInfo) {
        if (columnInfo.getDefaultValue() == null) {
            return StringUtils.EMPTY;
        }
        String value = StringUtils.trim(columnInfo.getDefaultValue());
        if (!columnInfo.isDefaultValueIsFunc() && !value.startsWith("'")) {
            value = "'" + value + "'";
        }
        return " DEFAULT " + value;
    }

    private static String buildOptions(EColumn eColumn) {
        StringBuilder sqlBuilder = new StringBuilder();
        String generationType = HanaAttributeNames.GENERATION_TYPE.getValue(eColumn.getAttribute());
        if (StringUtils.isNotBlank(generationType)) {
            sqlBuilder.append(" GENERATED ").append(generationType);
            String generationAlwaysAs = HanaAttributeNames.GENERATION_ALWAYS_AS.getValue(eColumn.getAttribute());
            if (StringUtils.isNotBlank(generationAlwaysAs)) {
                sqlBuilder.append(" ( ").append(generationAlwaysAs).append(" )");
            }
        }
        return sqlBuilder.toString();
    }

    private static String buildNullable(boolean nullable) {
        if (!nullable) {
            return " NOT NULL";
        } else {
            return " NULL";
        }
    }

    private static String buildColumnType(HanaTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();

        switch (sqlTypes) {
            case TINYINT:
                return TINYINT.getCodeKey();
            case SMALLINT:
                return SMALLINT.getCodeKey();
            case INTEGER:
                return INTEGER.getCodeKey();
            case BIGINT:
                return BIGINT.getCodeKey();
            case DECIMAL: {
                if (numericPrecision != null && numericScale != null) {
                    return DECIMAL.getCodeKey() + "(" + between(numericPrecision, 1, 38) + ", " + between(numericScale, 0, 38) + ")";
                } else if (numericPrecision != null) {
                    return DECIMAL.getCodeKey() + "(" + between(numericPrecision, 1, 38) + ")";
                } else {
                    return DECIMAL.getCodeKey();
                }
            }
            case SMALLDECIMAL:
                return SMALLDECIMAL.getCodeKey();
            case REAL:
                return REAL.getCodeKey();
            case FLOAT:
                return FLOAT.getCodeKey();
            case DOUBLE:
                return DOUBLE.getCodeKey();
            case BOOLEAN:
                return BOOLEAN.getCodeKey();
            case CHAR: {
                if (length == null || length <= 1) {
                    return CHAR.getCodeKey();
                } else {
                    return CHAR.getCodeKey() + "(" + between(length, 1, 2000) + ")";
                }
            }
            case VARCHAR: {
                if (length == null || length <= 1) {
                    return VARCHAR.getCodeKey();
                } else {
                    return VARCHAR.getCodeKey() + "(" + between(length, 1, 5000) + ")";
                }
            }
            case NCHAR: {
                if (length == null || length <= 1) {
                    return NCHAR.getCodeKey();
                } else {
                    return NCHAR.getCodeKey() + "(" + between(length, 1, 2000) + ")";
                }
            }
            case NVARCHAR: {
                if (length == null || length <= 1) {
                    return NVARCHAR.getCodeKey();
                } else {
                    return NVARCHAR.getCodeKey() + "(" + between(length, 1, 5000) + ")";
                }
            }
            case ALPHANUM: {
                if (length == null || length <= 1) {
                    return ALPHANUM.getCodeKey();
                } else {
                    return ALPHANUM.getCodeKey() + "(" + between(length, 1, 127) + ")";
                }
            }
            case SHORTTEXT: {
                if (length == null || length <= 1) {
                    return SHORTTEXT.getCodeKey();
                } else {
                    return SHORTTEXT.getCodeKey() + "(" + between(length, 1, 5000) + ")";
                }
            }
            case DATE:
                return DATE.getCodeKey();
            case TIME:
                return TIME.getCodeKey();
            case SECONDDATE:
                return SECONDDATE.getCodeKey();
            case TIMESTAMP:
                return TIMESTAMP.getCodeKey();
            case BLOB:
                return BLOB.getCodeKey();
            case BINARY:
                if (length == null || length <= 1) {
                    return BINARY.getCodeKey();
                } else {
                    return BINARY.getCodeKey() + "(" + between(length, 1, 2000) + ")";
                }
            case VARBINARY:
                if (length == null || length <= 1) {
                    return VARBINARY.getCodeKey();
                } else {
                    return VARBINARY.getCodeKey() + "(" + between(length, 1, 5000) + ")";
                }
            case CLOB:
                return CLOB.getCodeKey();
            case NCLOB:
                return NCLOB.getCodeKey();
            case TEXT:
                return TEXT.getCodeKey();
            case BINTEXT:
                return BINTEXT.getCodeKey();
            case ST_POINT:
                return ST_POINT.getCodeKey();
            case ST_GEOMETRY:
                return ST_GEOMETRY.getCodeKey();
            default: {
                throw new UnsupportedOperationException("Unsupported sql type " + sqlTypes);
            }
        }
    }
}

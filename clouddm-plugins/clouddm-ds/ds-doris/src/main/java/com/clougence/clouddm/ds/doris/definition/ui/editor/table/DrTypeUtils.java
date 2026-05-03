package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.doris.DorisMainVersion;
import com.clougence.adapter.doris.DorisTypes;
import com.clougence.schema.editor.builder.mappings.MappingHandler;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:54
*/
public class DrTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        DorisTypes sqlTypes = DorisTypes.valueOfCode(columnInfo.getDbType());
        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        String options = buildOptions(columnInfo);
        String columnNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()));

        // todo now not support defualt value for target doris

        return columnType + " " + columnNullable + " " + options;
    }

    public static String buildColumnTypeOnly(EColumn columnInfo, TriggerContext triggerContext) {
        DorisTypes sqlTypes = DorisTypes.valueOfCode(columnInfo.getDbType());

        return buildColumnType(sqlTypes, columnInfo, triggerContext);
    }

    private static String buildOptions(EColumn columnInfo) {
        // no need to add extra options
        return "";
    }

    private static String buildNullable(boolean nullable) {
        if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    private static String buildColumnType(DorisTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();

        switch (sqlTypes) {
            case BIGINT:
                return "bigint";
            case LARGEINT:
                return "largeint";
            case SMALLINT:
                return "smallint";
            case TINYINT:
                return "tinyint";
            case BOOLEAN:
                return "boolean";
            case DECIMALV3:
            case DECIMAL: {
                if (numericPrecision == null || numericScale == null) {
                    return "decimal";
                }
                if (numericPrecision > 38) {
                    return "varchar(" + numericPrecision * 2 + ")";
                }
                // target doris version less than 1.2.1
                if (DorisMainVersion.Doris_1_2_1.isGt(triggerContext.getTarDsInfo().getMainVersion())) {
                    // decimal integer range less than or equal to 18, first convert large int
                    if (numericScale == 0) {
                        return DorisTypes.LARGEINT.getCodeKey();
                    }
                    return "decimal(" + between(numericPrecision, 1, 38) + ", " + between(numericScale, 0, 38) + ")";
                }
                return "decimalv3(" + between(numericPrecision, 1, 38) + ", " + between(numericScale, 0, 38) + ")";
            }
            case FLOAT:
                return "float";
            case DOUBLE:
                return "double";
            case INT:
                return "int";
            case CHAR: {
                if (length != null && (length == 1 || length == 0)) {
                    return "char";
                } else {
                    // tips: Doris char use bytes, need double the length
                    return "char" + ((length == null) ? "" : "(" + between(length, 1, 255) + ")");
                }
            }
            case VARCHAR: {
                if (length == null) {
                    return "string";
                } else if (length <= 0) {
                    return "varchar(1)";
                } else {
                    // tips: starrocks char use bytes, need double the length
                    return "varchar(" + between(length, 1, 65533) + ")";
                }
            }
            case STRING:
                return "string";
            case DATE:
            case DATEV2:
                if (DorisMainVersion.Doris_1_2_0.isGt(triggerContext.getTarDsInfo().getMainVersion())) {
                    return "date";
                }
                return "datev2";
            case DATETIME:
            case DATETIMEV2:
                if (DorisMainVersion.Doris_1_2_0.isGt(triggerContext.getTarDsInfo().getMainVersion())) {
                    return "datetime";
                }
                return "datetimev2" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
            case JSONB:
                return "jsonb";
            case QUANTILE_STATE:
            case HLL:
            case BITMAP:
            case ARRAY:
            default: {
                throw new UnsupportedOperationException("Unsupported sql type " + sqlTypes);
            }
        }
    }

    protected static String buildDefault(DorisTypes sqlTypes, EColumn columnInfo, MappingHandler mappingHandler, TriggerContext triggerContext) {
        StringBuilder sqlBuild = new StringBuilder("");
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (triggerContext.isSelf()) {
                return "default " + defaultValue;
            }
            if (mappingHandler.hasFunctionMapping(defaultValue)) {
                String mappingTo = mappingHandler.functionMapping(defaultValue, columnInfo.getAttribute());
                if (StringUtils.isNotBlank(mappingTo)) {
                    sqlBuild.append("default ").append(mappingTo);
                }
            }
            return sqlBuild.toString();
        }

        if (defaultValue != null && defaultValue.toLowerCase().contains("now()")) {
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

            sqlBuild.append("default ").append(defaultValue);
        }

        return sqlBuild.toString();
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }
}

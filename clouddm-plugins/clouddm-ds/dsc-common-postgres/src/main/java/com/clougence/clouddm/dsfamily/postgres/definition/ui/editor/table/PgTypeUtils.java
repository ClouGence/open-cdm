package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table;

import static com.clougence.adapter.postgre.PostgreAttributeNames.*;
import static com.clougence.utils.NumberUtils.between;

import java.util.List;
import java.util.Map;

import com.clougence.adapter.postgre.PostgresMainVersion;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.TypeInfo;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

public class PgTypeUtils {

    public static String buildColumnMsg(EColumn columnInfo, TriggerContext triggerContext, String tableName, List<String> seqNames, EPrimaryKey primaryKey) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        PostgresTypes sqlTypes = PostgresTypes.valueOfCode(columnInfo.getDbType());
        return buildColumnType(sqlTypes, columnInfo, mainVersion, triggerContext) + " " //
               + buildCollate(columnInfo) + " " //
               + checkNull(columnInfo) + " " //
               + buildDefault2Create(sqlTypes, columnInfo, tableName, seqNames, triggerContext, primaryKey);
    }

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        PostgresTypes sqlTypes = PostgresTypes.valueOfCode(columnInfo.getDbType());
        return buildColumnType(sqlTypes, columnInfo, mainVersion, triggerContext) + " " //
               + buildCollate(columnInfo);
    }

    private static String checkNull(EColumn columnInfo) {
        Map<String, String> attribute = columnInfo.getAttribute();
        // ERROR:  conflicting NULL/NOT NULL declarations for column "column_name_5" of table "my_table"
        // LINE 7:   column_name_5  integer   null GENERATED ALWAYS AS IDENTITY...
        String virtual = attribute.get(VIRTUAL_TYPE.getCodeKey());
        if (StringUtils.isNotBlank(virtual) && !"STORED".equals(virtual)) {
            return "NOT NULL";
        } else if ("smallserial".equals(columnInfo.getDbType())) {
            return "NOT NULL";
        } else if ("serial".equals(columnInfo.getDbType())) {
            return "NOT NULL";
        } else if ("bigserial".equals(columnInfo.getDbType())) {
            return "NOT NULL";
        } else if (!Boolean.TRUE.equals(columnInfo.getNullable())) {
            return "NOT NULL";
        } else {
            return "NULL";
        }
    }

    public static String buildDefault2Create(PostgresTypes sqlTypes, EColumn columnInfo, String tableName, //
                                             List<String> seqNames, TriggerContext triggerContext, EPrimaryKey primaryKey) {
        //        Map<String, String> attribute = columnInfo.getAttribute();
        //      String defaultOption = attribute.get(PgTableEditorFields.FIELD_COLUMN_DEFAULT_OPTION);
        //        if (StringUtils.isNotBlank(defaultOption)) {
        // switch (defaultOption) {
        //     case "NULL": {
        //         if (columnInfo.getDefaultValue() == null) {
        //             return "";
        //         }
        //         switch (sqlTypes) {
        //             case SERIAL:
        //             case SMALLSERIAL:
        //             case BIGSERIAL:
        //             case PG_NODE_TREE_ARRAY:
        //                 return "";
        //         }
        //         if (primaryKey != null && primaryKey.getColumnList().contains(columnInfo.getName())) {
        //             return "";
        //         } else {
        //             return "DEFAULT null::" + columnInfo.getDbType();
        //         }
        //     }
        //     //DETAIL:  Array value must start with "{" or dimension information.
        //     case "EMPTY": {
        //         return "DEFAULT ''::" + columnInfo.getDbType();
        //     }
        //     case "CUSTOM": {
        //         if (sqlTypes.isString() || sqlTypes.isDataOrTime()) {
        //             return "DEFAULT '" + columnInfo.getDefaultValue().replace("'", "''") + "'";
        //         } else {
        //             return "DEFAULT " + columnInfo.getDefaultValue();
        //         }
        //     }
        //     case "Next Val": {
        //         switch (sqlTypes) {
        //             case SMALLINT:
        //             case INTEGER:
        //             case BIGINT:
        //                 return "DEFAULT " + buildIncrementName(tableName, columnInfo.getName(), seqNames);
        //             default:
        //                 return "";
        //         }
        //     }
        //     case "Virtual Type": {
        //         return buildVirtualType(columnInfo);
        //     }
        //     default:
        //         return "";
        // }
        //        } else {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        } else if (StringUtils.isBlank(defaultValue)) {
            return "DEFAULT ''::" + columnInfo.getDbType();
        } else {
            return buildDefault(sqlTypes, columnInfo, triggerContext);
        }
    }

    protected static String buildDefault(PostgresTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        String defaultValue = columnInfo.getDefaultValue();
        if (defaultValue == null) {
            return "";
        }

        if (columnInfo.isDefaultValueIsFunc()) {
            if (StringUtils.isNotBlank(defaultValue)) {
                return "DEFAULT " + defaultValue;
            }
            return "";
        }

        if (!"".equals(defaultValue)) {
            switch (sqlTypes) {
                case BIT: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        return "DEFAULT '" + HexadecimalUtils.hex2bit(defaultValue.substring(2)) + "'";
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "DEFAULT '" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
                    } else {
                        return ""; // unknown
                    }
                }
                case BOOLEAN: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        String v = defaultValue.substring(2);
                        if (v.endsWith("01")) {
                            return "DEFAULT 't'";
                        } else {
                            return "DEFAULT 'f'";
                        }
                    } else {
                        return "DEFAULT " + defaultValue; // unknown
                    }
                }
                case BYTEA: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        return "DEFAULT E'\\x" + defaultValue.substring(2) + "'";
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "DEFAULT E'\\x" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
                    }
                    return ""; // unknown
                }
                case BIT_VARYING: {
                    return "DEFAULT '" + defaultValue + "'::bit varying";
                }
                case CHARACTER:
                case BPCHAR:
                case CHARACTER_VARYING:
                case TEXT:
                case NAME:
                case TIMESTAMP_WITHOUT_TIME_ZONE:
                case TIMESTAMP_WITH_TIME_ZONE:
                case TIME_WITHOUT_TIME_ZONE:
                case TIME_WITH_TIME_ZONE:
                case DATE:
                    return "DEFAULT '" + defaultValue.replace("'", "''") + "'";
                case JSON:
                    return "DEFAULT '" + defaultValue.replace("'", "''") + "'::json";
                case JSONB:
                    return "DEFAULT '" + defaultValue.replace("'", "''") + "'::jsonb";
                case UUID:
                    return "DEFAULT '" + defaultValue.replace("'", "''") + "'::uuid";
                case SMALLSERIAL:
                case SERIAL:
                case BIGSERIAL:
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                case NUMERIC:
                case REAL:
                case DOUBLE_PRECISION:
                    //the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
                    return "DEFAULT " + NumberUtils.trimZero(defaultValue);
                case MONEY:
                case OID:
                default:
                    if (sqlTypes.isArray()) {
                        if (defaultValue.startsWith("[") && defaultValue.endsWith("]")) {
                            return "DEFAULT ARRAY " + defaultValue;
                        } else if (defaultValue.startsWith("{") && defaultValue.endsWith("}")) {
                            return "DEFAULT '" + defaultValue + "'";
                        }
                    }
                    return "DEFAULT " + defaultValue;
            }
        }
        return "";
    }

    private static String buildColumnType(PostgresTypes sqlTypes, EColumn columnInfo, MainVersion mainVersion, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();
        Map<String, String> attrMap = columnInfo.getAttribute();
        String targetTypes;
        switch (sqlTypes) {
            case SMALLSERIAL:
                targetTypes = "smallserial";
                break;
            case SERIAL:
                targetTypes = "serial";
                break;
            case BIGSERIAL:
                targetTypes = "bigserial";
                break;
            case SMALLINT:
                targetTypes = "smallint";
                break;
            case INTEGER:
                targetTypes = "integer";
                break;
            case BIGINT:
                targetTypes = "bigint";
                break;
            case OID:
                targetTypes = "oid";
                break;
            case NUMERIC: {
                if (numericPrecision != null && numericScale != null) {
                    targetTypes = "numeric(" + between(numericPrecision, 1, 1000) + ", " + between(numericScale, 0, 1000) + ")";
                    break;
                } else if (numericPrecision != null) {
                    targetTypes = "numeric(" + between(numericPrecision, 0, 1000) + ")";
                    break;
                } else {
                    targetTypes = "numeric";
                    break;
                }
            }
            case REAL:
                targetTypes = "real";
                break;
            case DOUBLE_PRECISION:
                targetTypes = "double precision";
                break;
            case MONEY:
                targetTypes = "money";
                break;
            case CHARACTER:
            case BPCHAR:
                targetTypes = "char" + ((length == null) ? "" : "(" + between(length, 1, 10485760) + ")");
                break;
            case CHARACTER_VARYING:
                targetTypes = "varchar" + ((length == null) ? "" : "(" + between(length, 1, 10485760) + ")");
                break;
            case TEXT:
                targetTypes = "text";
                break;
            case NAME:
                targetTypes = "name";
                break;
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                targetTypes = "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                break;
            case TIMESTAMP_WITH_TIME_ZONE: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                targetTypes = "timestamp" + timeLength + " with time zone";
                break;
            }
            case TIME_WITHOUT_TIME_ZONE: {
                targetTypes = "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                break;
            }
            case TIME_WITH_TIME_ZONE: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                targetTypes = "time" + timeLength + " with time zone";
                break;
            }
            case DATE:
                targetTypes = "date";
                break;
            case INTERVAL:
                targetTypes = "interval";
                break;
            case BIT:
                if (length != null) {
                    targetTypes = "bit(" + between(length, 0, 83886080) + ")";
                    break;
                } else {
                    targetTypes = "bit";
                    break;
                }
            case BIT_VARYING:
                if (length != null) {
                    targetTypes = "bit varying (" + between(length, 0, 83886080) + ")";
                    break;
                } else {
                    targetTypes = "bit varying ";
                    break;
                }
            case BOOLEAN:
                targetTypes = "boolean";
                break;
            case XML:
                targetTypes = "xml";
                break;
            case BYTEA:
                targetTypes = "bytea";
                break;
            case POINT:
                targetTypes = "point";
                break;
            case LINE:
                targetTypes = "line";
                break;
            case LSEG:
                targetTypes = "lseg";
                break;
            case BOX:
                targetTypes = "box";
                break;
            case PATH:
                targetTypes = "path";
                break;
            case POLYGON:
                targetTypes = "polygon";
                break;
            case CIRCLE:
                targetTypes = "circle";
                break;
            case GEOMETRY: {
                TypeInfo info = findGisType(triggerContext);
                String gisSRID = GIS_SRID.getValue(columnInfo.getAttribute());
                String gisTYPE = GIS_TYPE.getValue(columnInfo.getAttribute());
                String typeName = info != null ? info.toFullName() : null;

                boolean ispublic = StringUtils.isBlank(typeName) || StringUtils.equalsIgnoreCase(typeName, "public.geometry");
                boolean speSRID = !"0".equals(gisSRID) && !StringUtils.isBlank(gisSRID);

                if (ispublic) {
                    targetTypes = "geometry" + (speSRID ? ("(" + gisTYPE + "," + gisSRID + ")") : "");
                    break;
                } else {
                    targetTypes = typeName + (speSRID ? ("(" + gisTYPE + "," + gisSRID + ")") : "");
                    break;
                }
            }
            case CIDR:
                targetTypes = "cidr";
                break;
            case INET:
                targetTypes = "inet";
                break;
            case MACADDR:
                targetTypes = "macaddr";
                break;
            case MACADDR8:
                targetTypes = PostgresMainVersion.PostgreSQL_9_6.isGe(mainVersion) ? "text" : "macaddr8";
                break;
            case TSVECTOR:
                targetTypes = "tsvector";
                break;
            case TSQUERY:
                targetTypes = "tsquery";
                break;
            case UUID:
                targetTypes = "uuid";
                break;
            case JSON:
                targetTypes = "json";
                break;
            case JSONB:
                targetTypes = "jsonb";
                break;
            case INT4RANGE:
                targetTypes = "int4range";
                break;
            case INT8RANGE:
                targetTypes = "int8range";
                break;
            case NUMRANGE:
                targetTypes = "numrange";
                break;
            case TSRANGE:
                targetTypes = "tsrange";
                break;
            case TSTZRANGE:
                targetTypes = "tstzrange";
                break;
            case DATERANGE:
                targetTypes = "daterange";
                break;
            case PG_LSN:
                targetTypes = "pg_lsn";
                break;
            case TXID_SNAPSHOT:
                targetTypes = "txid_snapshot";
                break;
            //
            //
            case SMALLINT_ARRAY:
                targetTypes = "smallint[]";
                break;
            case INTEGER_ARRAY:
                targetTypes = "integer[]";
                break;
            case BIGINT_ARRAY:
                targetTypes = "bigint[]";
                break;
            case OID_ARRAY:
                targetTypes = "oid[]";
                break;
            case NUMERIC_ARRAY: {
                if (numericPrecision != null && numericScale != null) {
                    targetTypes = "numeric(" + between(numericPrecision, 1, 1000) + ", " + between(numericScale, 0, 1000) + ")[]";
                    break;
                } else if (numericPrecision != null) {
                    targetTypes = "numeric(" + between(numericPrecision, 0, 1000) + ")[]";
                    break;
                } else {
                    targetTypes = "numeric[]";
                    break;
                }
            }
            case REAL_ARRAY:
                targetTypes = "real[]";
                break;
            case DOUBLE_PRECISION_ARRAY:
                targetTypes = "double precision[]";
                break;
            case MONEY_ARRAY:
                targetTypes = "money[]";
                break;
            case CHARACTER_ARRAY:
                targetTypes = "char" + ((length == null) ? "" : "(" + between(length, 0, 10485760) + ")") + "[]";
                break;
            case BPCHAR_ARRAY:// TODO ？？
            case CHARACTER_VARYING_ARRAY:
                targetTypes = "varchar" + ((length == null) ? "" : "(" + between(length, 0, 10485760) + ")") + "[]";
                break;
            case TEXT_ARRAY:
                targetTypes = "text[]";
                break;
            case NAME_ARRAY:
                targetTypes = "name[]";
                break;
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
                targetTypes = "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")") + "[]";
                break;
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                targetTypes = "timestamp" + timeLength + " with time zone[]";
                break;
            }
            case TIME_WITHOUT_TIME_ZONE_ARRAY: {
                targetTypes = "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")") + "[]";
                break;
            }
            case TIME_WITH_TIME_ZONE_ARRAY: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                targetTypes = "time" + timeLength + " with time zone[]";
                break;
            }
            case DATE_ARRAY:
                targetTypes = "date[]";
                break;
            case INTERVAL_ARRAY:
                targetTypes = "interval[]";
                break;
            case BIT_ARRAY:
                targetTypes = "bit" + ((length == null) ? "" : "(" + between(length, 1, 83886080) + ")") + "[]";
                break;
            case BIT_VARYING_ARRAY:
                targetTypes = "bit varying" + ((length == null) ? "" : "(" + between(length, 1, 83886080) + ")") + "[]";
                break;
            case BOOLEAN_ARRAY:
                targetTypes = "boolean[]";
                break;
            case XML_ARRAY:
                targetTypes = "xml[]";
                break;
            case BYTEA_ARRAY:
                targetTypes = "bytea[]";
                break;
            case POINT_ARRAY:
                targetTypes = "point[]";
                break;
            case LINE_ARRAY:
                targetTypes = "line[]";
                break;
            case LSEG_ARRAY:
                targetTypes = "lseg[]";
                break;
            case BOX_ARRAY:
                targetTypes = "box[]";
                break;
            case PATH_ARRAY:
                targetTypes = "path[]";
                break;
            case POLYGON_ARRAY:
                targetTypes = "polygon[]";
                break;
            case CIRCLE_ARRAY:
                targetTypes = "circle[]";
                break;
            case GEOMETRY_ARRAY: {
                TypeInfo info = findGisType(triggerContext);
                String gisSRID = GIS_SRID.getValue(columnInfo.getAttribute());
                String gisTYPE = GIS_TYPE.getValue(columnInfo.getAttribute());
                String typeName = info != null ? info.toFullName() : null;

                boolean ispublic = StringUtils.isBlank(typeName) || StringUtils.equalsIgnoreCase(typeName, "public.geometry");
                boolean speSRID = !"0".equals(gisSRID) && !StringUtils.isBlank(gisSRID);
                String result;
                if (ispublic) {
                    result = "geometry" + (speSRID ? ("(" + gisTYPE + "," + gisSRID + ")") : "");
                } else {
                    result = typeName + (speSRID ? ("(" + gisTYPE + "," + gisSRID + ")") : "");
                }
                targetTypes = result + "[]";
                break;
            }
            case CIDR_ARRAY:
                targetTypes = "cidr[]";
                break;
            case INET_ARRAY:
                targetTypes = "inet[]";
                break;
            case MACADDR_ARRAY:
                targetTypes = "macaddr[]";
                break;
            case MACADDR8_ARRAY:
                targetTypes = PostgresMainVersion.PostgreSQL_9_6.isGe(mainVersion) ? "text[]" : "macaddr8[]";
                break;
            case TSVECTOR_ARRAY:
                targetTypes = "tsvector[]";
                break;
            case TSQUERY_ARRAY:
                targetTypes = "tsquery[]";
                break;
            case UUID_ARRAY:
                targetTypes = "uuid[]";
                break;
            case JSON_ARRAY:
                targetTypes = "json[]";
                break;
            case JSONB_ARRAY:
                targetTypes = "jsonb[]";
                break;
            case INT4RANGE_ARRAY:
                targetTypes = "int4range[]";
                break;
            case INT8RANGE_ARRAY:
                targetTypes = "int8range[]";
                break;
            case NUMRANGE_ARRAY:
                targetTypes = "numrange[]";
                break;
            case TSRANGE_ARRAY:
                targetTypes = "tsrange[]";
                break;
            case TSTZRANGE_ARRAY:
                targetTypes = "tstzrange[]";
                break;
            case DATERANGE_ARRAY:
                targetTypes = "daterange[]";
                break;
            case PG_LSN_ARRAY:
                targetTypes = "pg_lsn[]";
                break;
            case TXID_SNAPSHOT_ARRAY:
                targetTypes = "txid_snapshot[]";
                break;
            default:
                targetTypes = columnInfo.getDbType();
                break;
        }
        if (targetTypes.contains("[]")) {
            String arrayDimension = attrMap.get(ARRAY_DIMENSION.getCodeKey());
            if (StringUtils.isNotBlank(arrayDimension)) {
                targetTypes = targetTypes + arrayDimension(arrayDimension);
            }
        }
        return targetTypes;
    }

    private static TypeInfo findGisType(TriggerContext triggerContext) {
        List<TypeInfo> infoList = triggerContext.getTarDsInfo().getTypeInfoList();
        if (infoList != null) {
            for (TypeInfo info : infoList) {
                String simpleName = info.getTypeName();
                String codeKey = PostgresTypes.GEOMETRY.getCodeKey();
                if (StringUtils.equalsIgnoreCase(simpleName, codeKey)) {
                    return info;
                }
            }
        }
        return null;
    }

    //default length 1
    private static String arrayDimension(String dimension) {
        StringBuilder array = new StringBuilder();
        try {
            Integer value = Integer.valueOf(dimension);
            int actualDimension = value < 1 ? 1 : value;
            for (int i = 1; i < actualDimension; i++) {
                array.append("[]");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Conversion exception when determining array dimensions, name is '" + dimension + "'");
        }
        return array.toString();
    }

    public static String buildIncrementName(String tableName, String columnName, List<String> seqNames) {
        //nextval('my_table_id_seq'::regclass)
        String seqName = "'" + tableName + "_" + columnName + "_seq'";
        String name = tableName + "_" + columnName + "_seq";
        seqNames.add(name);
        return "nextval(" + seqName + "::regclass)";
    }

    public static String buildCollate(EColumn columnInfo) {
        String dbType = columnInfo.getDbType();
        PostgresTypes postgresTypes = PostgresTypes.valueOfCode(dbType);
        if (!canSort(postgresTypes)) {
            return "";
        }
        Map<String, String> attrMap = columnInfo.getAttribute();
        String sortRules = attrMap.get(COLUMN_SORT_RULES.getCodeKey());
        if (StringUtils.isBlank(sortRules)) {
            return "";
        } else {
            String[] split = sortRules.split("\\.");
            return "COLLATE \"" + split[0] + "\"." + "\"" + split[1] + "\"";
        }
    }

    public static String buildVirtualType(EColumn eColumn) {
        Map<String, String> attrMap = eColumn.getAttribute();
        String virtualOption = attrMap.get(VIRTUAL_TYPE.getCodeKey());
        if (StringUtils.isBlank(virtualOption)) {
            return "";
        }
        switch (virtualOption) {
            case "ALWAYS":
            case "BY DEFAULT": {
                return buildIdentityColumn(virtualOption, eColumn.getAttribute());
            }
            case "STORED": {
                String expression = attrMap.get(VIRTUAL_TYPE_EXPRESSION.getCodeKey());
                return "GENERATED ALWAYS AS ( (" + expression + ")) STORED";
            }
            default: {
                return "";
            }
        }
    }

    public static String buildIdentityColumn(String identityType, Map<String, String> attrMap) {
        String incremental = attrMap.get(VIRTUAL_TYPE_INCREMENTAL.getCodeKey());
        String min = attrMap.get(VIRTUAL_TYPE_MIN.getCodeKey());
        String max = attrMap.get(VIRTUAL_TYPE_MAX.getCodeKey());
        String start = attrMap.get(VIRTUAL_TYPE_START.getCodeKey());
        String cache = attrMap.get(VIRTUAL_TYPE_CACHE.getCodeKey());
        String loop = attrMap.get(VIRTUAL_TYPE_LOOP.getCodeKey());
        switch (identityType) {
            case "ALWAYS": {
                StringBuilder builder = new StringBuilder("GENERATED ALWAYS AS IDENTITY (");
                return buildIndetityCondition(incremental, min, max, start, cache, loop, builder);
            }
            case "BY DEFAULT": {
                StringBuilder builder = new StringBuilder("GENERATED BY DEFAULT AS IDENTITY (");
                return buildIndetityCondition(incremental, min, max, start, cache, loop, builder);
            }
            default:
                return "";
        }
    }

    private static String buildIndetityCondition(String incremental, String min, String max, String start, String cache, String loop, StringBuilder builder) {
        if (incremental != null && !"0".equals(incremental)) {
            builder.append(" INCREMENT ");
            builder.append(incremental);
            builder.append("\n");
        }
        if (min != null && !"0".equals(min)) {
            builder.append(" MINVALUE ");
            builder.append(min);
            builder.append("\n");
        }
        if (max != null && !"0".equals(max)) {
            builder.append(" MAXVALUE ");
            builder.append(max);
            builder.append("\n");
        }
        if (start != null && !"0".equals(start)) {
            builder.append(" START ");
            builder.append(start);
            builder.append("\n");
        }
        if (cache != null && !"0".equals(cache)) {
            builder.append(" CACHE ");
            builder.append(cache);
            builder.append("\n");
        }
        if ("true".equals(loop)) {
            builder.append(" CYCLE ");
            builder.append("\n");
        }
        builder.append(")");
        if (builder.toString().endsWith("()")) {
            return builder.toString().replace("()", "");
        }
        return builder.toString();
    }

    public static boolean canSort(PostgresTypes types) {
        //SELECT * FROM pg_type WHERE typcollation!=0
        switch (types) {
            case NAME:
            case TEXT:
            case PG_NODE_TREE:
            case BPCHAR:
            case CHARACTER_VARYING:
            case NAME_ARRAY:
            case TEXT_ARRAY:
            case BPCHAR_ARRAY:
            case CHARACTER_VARYING_ARRAY:
                return true;
            default:
                return false;
        }
    }

    public static boolean isLength(PostgresTypes types) {
        switch (types) {
            case CHARACTER:
            case BPCHAR:
            case CHARACTER_VARYING:
            case CHARACTER_ARRAY:
            case CHARACTER_VARYING_ARRAY:
            case BIT:
            case BIT_VARYING:
            case BIT_ARRAY:
            case BIT_VARYING_ARRAY:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDate(PostgresTypes types) {
        switch (types) {
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIME_WITH_TIME_ZONE:
            case TIME_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
            case TIME_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITH_TIME_ZONE_ARRAY:
                return true;
            default:
                return false;
        }
    }

    public static boolean isScale(PostgresTypes types) {
        switch (types) {
            case NUMERIC_ARRAY:
            case NUMERIC:
                return true;
            default:
                return false;
        }
    }
}

package com.clougence.clouddm.ds.polardb.definition.porpg.editor.table;

import static com.clougence.utils.NumberUtils.between;

import java.util.List;

import com.clougence.adapter.polar.porpg.PolarDBPgAttributeNames;
import com.clougence.adapter.polar.porpg.PolarDBPgTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.TypeInfo;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

class PorPgTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
        PolarDBPgTypes sqlTypes = PolarDBPgTypes.valueOfCode(columnInfo.getDbType());

        return buildColumnType(sqlTypes, columnInfo, mainVersion, triggerContext) + " " //
               + buildNullable(columnInfo.getNullable()) + " " //
               + buildDefault(sqlTypes, columnInfo, triggerContext);
    }

    private static String buildNullable(Boolean nullable) {
        if (nullable == null) {
            return "";
        }
        if (!nullable) {
            return "not null";
        } else {
            return "null";
        }
    }

    private static String buildDefault(PolarDBPgTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
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
                        return "default '" + HexadecimalUtils.hex2bit(defaultValue.substring(2)) + "'";
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "default '" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
                    } else {
                        return ""; //unknown
                    }
                }
                case BYTEA: {
                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                        return "default E'\\x" + defaultValue.substring(2) + "'";
                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                        return "default E'\\x" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
                    }
                    return ""; //unknown
                }
                case CHARACTER:
                case BPCHAR:
                case CHARACTER_VARYING:
                case TEXT:
                case NAME:
                case JSON:
                case TIMESTAMP_WITHOUT_TIME_ZONE:
                case TIMESTAMP_WITH_TIME_ZONE:
                case TIME_WITHOUT_TIME_ZONE:
                case TIME_WITH_TIME_ZONE:
                case DATE:
                    return "default '" + defaultValue.replace("'", "''") + "'";
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
                    return "default " + NumberUtils.trimZero(defaultValue);
                case MONEY:
                case OID:
                default:
                    return "default " + defaultValue;
            }
        }
        return "";
    }

    private static String buildColumnType(PolarDBPgTypes sqlTypes, EColumn columnInfo, MainVersion mainVersion, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        Integer datetimePrecision = columnInfo.getDatetimePrecision();

        boolean autoIncrement = "true".equalsIgnoreCase(PolarDBPgAttributeNames.AUTO_INCREMENT.getValue(columnInfo.getAttribute()));
        if (autoIncrement) {
            switch (sqlTypes) {
                case SMALLINT:
                    sqlTypes = PolarDBPgTypes.SMALLSERIAL;
                    break;
                case INTEGER:
                    sqlTypes = PolarDBPgTypes.SERIAL;
                    break;
                case BIGINT:
                    sqlTypes = PolarDBPgTypes.BIGSERIAL;
                    break;
                default:
                    break;
            }
        }

        switch (sqlTypes) {
            case SMALLSERIAL:
                return "smallserial";
            case SERIAL:
                return "serial";
            case BIGSERIAL:
                return "bigserial";
            case SMALLINT:
                return "smallint";
            case INTEGER:
                return "integer";
            case BIGINT:
                return "bigint";
            case OID:
                return "oid";
            case NUMERIC: {
                if (numericPrecision != null && numericScale != null) {
                    return "numeric(" + between(numericPrecision, 1, 1000) + ", " + between(numericScale, 0, 1000) + ")";
                } else if (numericPrecision != null) {
                    return "numeric(" + between(numericPrecision, 0, 1000) + ")";
                } else {
                    return "numeric";
                }
            }
            case REAL:
                return "real";
            case DOUBLE_PRECISION:
                return "double precision";
            case MONEY:
                return "money";
            case CHARACTER:
                return "char" + ((length == null) ? "" : "(" + between(length, 1, 10485760) + ")");
            case BPCHAR:
                return "bpchar" + ((length == null) ? "" : "(" + between(length, 1, 10485760) + ")");
            case CHARACTER_VARYING:
                return "varchar" + ((length == null) ? "" : "(" + between(length, 1, 10485760) + ")");
            case TEXT:
                return "text";
            case NAME:
                return "name";
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
            case TIMESTAMP_WITH_TIME_ZONE: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                return "timestamp" + timeLength + " with time zone";
            }
            case TIME_WITHOUT_TIME_ZONE: {
                return "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
            }
            case TIME_WITH_TIME_ZONE: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                return "time" + timeLength + " with time zone";
            }
            case DATE:
                return "date";
            case INTERVAL:
                return "interval";
            case BIT:
                if (numericPrecision != null && numericPrecision > 1) {
                    return "bit varying (" + between(numericPrecision, 0, 83886080) + ")";
                } else {
                    return "bit";
                }
            case BIT_VARYING:
                if (numericPrecision != null) {
                    return "bit varying (" + between(numericPrecision, 0, 83886080) + ")";
                } else {
                    return "bit varying ";
                }
            case BOOLEAN:
                return "boolean";
            case XML:
                return "xml";
            case BYTEA:
                return "bytea";
            case POINT:
                return "point";
            case LINE:
                return "line";
            case LSEG:
                return "lseg";
            case BOX:
                return "box";
            case PATH:
                return "path";
            case POLYGON:
                return "polygon";
            case CIRCLE:
                return "circle";
            case GEOMETRY: {
                List<TypeInfo> infoList = triggerContext.getTarDsInfo().getTypeInfoList();
                String fullTypeName = null;
                for (TypeInfo info : infoList) {
                    String simpleName = info.getTypeName();
                    String codeKey = PolarDBPgTypes.GEOMETRY.getCodeKey();
                    if (StringUtils.equalsIgnoreCase(simpleName, codeKey)) {
                        fullTypeName = info.toFullName();
                        break;
                    }
                }

                if (StringUtils.isBlank(fullTypeName) || StringUtils.equalsIgnoreCase(fullTypeName, "public.geometry")) {
                    return "geometry";
                } else {
                    return fullTypeName;
                }
            }
            case CIDR:
                return "cidr";
            case INET:
                return "inet";
            case MACADDR:
                return "macaddr";
            case MACADDR8:
                return "macaddr8";
            case TSVECTOR:
                return "tsvector";
            case TSQUERY:
                return "tsquery";
            case UUID:
                return "uuid";
            case JSON:
                return "json";
            case JSONB:
                return "jsonb";
            case INT4RANGE:
                return "int4range";
            case INT8RANGE:
                return "int8range";
            case NUMRANGE:
                return "numrange";
            case TSRANGE:
                return "tsrange";
            case TSTZRANGE:
                return "tstzrange";
            case DATERANGE:
                return "daterange";
            case PG_LSN:
                return "pg_lsn";
            case TXID_SNAPSHOT:
                return "txid_snapshot";
            //
            //
            case SMALLINT_ARRAY:
                return "smallint[]";
            case INTEGER_ARRAY:
                return "integer[]";
            case BIGINT_ARRAY:
                return "bigint[]";
            case OID_ARRAY:
                return "oid[]";
            case NUMERIC_ARRAY: {
                if (numericPrecision != null && numericScale != null) {
                    return "numeric(" + between(numericPrecision, 1, 1000) + ", " + between(numericScale, 0, 1000) + ")[]";
                } else if (numericPrecision != null) {
                    return "numeric(" + between(numericPrecision, 0, 1000) + ")[]";
                } else {
                    return "numeric[]";
                }
            }
            case REAL_ARRAY:
                return "real[]";
            case DOUBLE_PRECISION_ARRAY:
                return "double precision[]";
            case MONEY_ARRAY:
                return "money[]";
            case CHARACTER_ARRAY:
                return "varchar" + ((length == null) ? "" : "(" + between(length, 0, 10485760) + ")") + "[]";
            case BPCHAR_ARRAY:// TODO ？？
            case CHARACTER_VARYING_ARRAY:
                return "varchar" + ((length == null) ? "" : "(" + between(length, 0, 10485760) + ")") + "[]";
            case TEXT_ARRAY:
                return "text[]";
            case NAME_ARRAY:
                return "name[]";
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")") + "[]";
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                return "timestamp" + timeLength + " with time zone[]";
            }
            case TIME_WITHOUT_TIME_ZONE_ARRAY: {
                return "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")") + "[]";
            }
            case TIME_WITH_TIME_ZONE_ARRAY: {
                String timeLength = ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
                return "time" + timeLength + " with time zone[]";
            }
            case DATE_ARRAY:
                return "date[]";
            case INTERVAL_ARRAY:
                return "interval[]";
            case BIT_ARRAY:
                return "bit" + ((length == null) ? "" : "(" + between(length, 1, 83886080) + ")") + "[]";
            case BIT_VARYING_ARRAY:
                return "bit varying" + ((length == null) ? "" : "(" + between(length, 1, 83886080) + ")") + "[]";
            case BOOLEAN_ARRAY:
                return "boolean[]";
            case XML_ARRAY:
                return "xml[]";
            case BYTEA_ARRAY:
                return "bytea[]";
            case POINT_ARRAY:
                return "point[]";
            case LINE_ARRAY:
                return "line[]";
            case LSEG_ARRAY:
                return "lseg[]";
            case BOX_ARRAY:
                return "box[]";
            case PATH_ARRAY:
                return "path[]";
            case POLYGON_ARRAY:
                return "polygon[]";
            case CIRCLE_ARRAY:
                return "circle[]";
            case GEOMETRY_ARRAY: {
                List<TypeInfo> infoList = triggerContext.getTarDsInfo().getTypeInfoList();
                String fullTypeName = null;
                for (TypeInfo info : infoList) {
                    String simpleName = info.getTypeName();
                    String codeKey = PolarDBPgTypes.GEOMETRY.getCodeKey();
                    if (StringUtils.equalsIgnoreCase(simpleName, codeKey)) {
                        fullTypeName = info.toFullName();
                        break;
                    }
                }

                if (StringUtils.isBlank(fullTypeName)) {
                    return "geometry[]";
                } else {
                    return fullTypeName + "[]";
                }
            }
            case CIDR_ARRAY:
                return "cidr[]";
            case INET_ARRAY:
                return "inet[]";
            case MACADDR_ARRAY:
                return "macaddr[]";
            case MACADDR8_ARRAY:
                return "macaddr8[]";
            case TSVECTOR_ARRAY:
                return "tsvector[]";
            case TSQUERY_ARRAY:
                return "tsquery[]";
            case UUID_ARRAY:
                return "uuid[]";
            case JSON_ARRAY:
                return "json[]";
            case JSONB_ARRAY:
                return "jsonb[]";
            case INT4RANGE_ARRAY:
                return "int4range[]";
            case INT8RANGE_ARRAY:
                return "int8range[]";
            case NUMRANGE_ARRAY:
                return "numrange[]";
            case TSRANGE_ARRAY:
                return "tsrange[]";
            case TSTZRANGE_ARRAY:
                return "tstzrange[]";
            case DATERANGE_ARRAY:
                return "daterange[]";
            case PG_LSN_ARRAY:
                return "pg_lsn[]";
            case TXID_SNAPSHOT_ARRAY:
                return "txid_snapshot[]";
            default:
                return columnInfo.getDbType();
        }
    }
}

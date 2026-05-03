//package com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table;
//
//import static com.clougence.utils.NumberUtils.between;
//
//import java.util.List;
//import java.util.Map;
//
//import com.clougence.schema.editor.domain.EColumn;
//import com.clougence.schema.editor.triggers.TriggerContext;
//import com.clougence.schema.metadata.MainVersion;
//import com.clougence.utils.NumberUtils;
//import com.clougence.utils.StringUtils;
//
///**
// * @author Ekko
// * @date 2023/10/30 11:55
//*/
//class McTypeUtils {
//
//    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
//        MainVersion mainVersion = triggerContext.getTarDsInfo().getMainVersion();
//        DmSqlTypes sqlTypes = DmSqlTypes.valueOfCode(columnInfo.getDbType());
//        Map<String, String> attr = columnInfo.getAttribute();
//        String option = attr.get(DmAttributeNames.DEFAULT_OPTION.getCodeKey());
//        String columnType = buildColumnType(sqlTypes, columnInfo, mainVersion);
//        String identity = attr.get(DmAttributeNames.IDENTITY.getCodeKey());
//        String buildNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()), Boolean.parseBoolean(identity));
//        return columnType + " "
//               + (Boolean.parseBoolean(identity) ? buildIdentity(attr) : "virtual".equals(option) ? buildVirtual(attr) : buildDefault(sqlTypes, columnInfo, triggerContext)) + " "
//               + buildNullable;
//    }
//
//    private static String buildIdentity(Map<String, String> attr) {
//        String seed = attr.get(DmAttributeNames.IDENTITY_SEED.getCodeKey());
//        String increment = attr.get(DmAttributeNames.IDENTITY_INCREMENT.getCodeKey());
//        return String.format("identity(%s, %s)", StringUtils.isNotBlank(seed) ? seed : 1, StringUtils.isNotBlank(increment) ? increment : 1);
//    }
//
//    protected static void buildIdentityForAlter(Map<String, String> oldAttr, Map<String, String> newAttr, List<String> ddlScripts, String prefix, String columnName) {
//        boolean oldHas = Boolean.parseBoolean(oldAttr.get(DmAttributeNames.IDENTITY.getCodeKey()));
//        boolean newHas = Boolean.parseBoolean(newAttr.get(DmAttributeNames.IDENTITY.getCodeKey()));
//        String seed = newAttr.get(DmAttributeNames.IDENTITY_SEED.getCodeKey());
//        String increment = newAttr.get(DmAttributeNames.IDENTITY_INCREMENT.getCodeKey());
//        if (oldHas) {
//            if (!newHas) {
//                ddlScripts.add(prefix + " drop identity;");
//            } else if (StringUtils.isNotBlank(seed) || StringUtils.isNotBlank(increment)) {
//                ddlScripts.add(prefix + " drop identity;");
//                ddlScripts.add(prefix + " add column " + columnName + " " + buildIdentity(newAttr));
//            }
//        } else {
//            if (newHas) {
//                ddlScripts.add(prefix + " add column " + columnName + " " + buildIdentity(newAttr));
//            }
//        }
//    }
//
//    protected static void buildIdentityForAdd(Map<String, String> attr, List<String> ddlScripts, String prefix, String columnName) {
//        boolean has = Boolean.parseBoolean(attr.get(DmAttributeNames.IDENTITY.getCodeKey()));
//        if (has) {
//            ddlScripts.add(prefix + " add column " + columnName + " " + buildIdentity(attr));
//        }
//    }
//
//    protected static String buildVirtual(Map<String, String> attr) {
//        String expr = attr.get(DmAttributeNames.VIRTUAL_EXPR.getCodeKey());
//        return String.format(" as (%s)", expr);
//    }
//
//    private static String buildNullable(boolean nullable, boolean notNull) {
//        if (!nullable || notNull) {
//            return "not null";
//        } else {
//            return "null";
//        }
//    }
//
//    protected static String buildDefault(DmSqlTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
//        String defaultValue = columnInfo.getDefaultValue();
//        if (defaultValue == null) {
//            return "default null";
//        }
//
//        if (columnInfo.isDefaultValueIsFunc()) {
//            if (StringUtils.isNotBlank(defaultValue)) {
//                return "default " + defaultValue;
//            } else {
//                return "";
//            }
//        }
//
//        if (!"".equals(defaultValue)) {
//            switch (sqlTypes) {
//                case CHAR:
//                case NCHAR:
//                case VARCHAR:
//                case NVARCHAR2:
//                case TEXT:
//                case CLOB:
//                    return "default '" + replaceSingleQuotes(defaultValue) + "'";
//                case BIT:
//                case NUMERIC:
//                case TINYINT:
//                case SMALLINT:
//                case INT:
//                case BIGINT:
//                case FLOAT:
//                case REAL:
//                    //the extra 0, like `0.000 -> 0` or `000123.123000 to 123.123`
//                    return "default " + NumberUtils.trimZero(defaultValue);
//                case BINARY:
//                case VARBINARY:
//                case IMAGE: {
//                    if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
//                        return "default '" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
//                    } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
//                        return "default '" + defaultValue.substring(2, defaultValue.length() - 1) + "'";
//                    } else {
//                        return ""; // unknown
//                    }
//                }
//                case DATE:
//                case TIME:
//                case TIMESTAMP:
//                case TIMESTAMP_WITH_TIME_ZONE:
//                case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
//                    return "default '" + defaultValue + "'";
//                default:
//                    return "default " + defaultValue;
//            }
//        }
//        return "default ''";
//    }
//
//    protected static String buildColumnType(DmSqlTypes sqlTypes, EColumn columnInfo, MainVersion mainVersion) {
//        if (sqlTypes == null) {
//            return columnInfo.getDbType();
//        }
//        Long length = columnInfo.getLength();
//        Integer numericPrecision = columnInfo.getNumericPrecision();
//        Integer numericScale = columnInfo.getNumericScale();
//        Integer datetimePrecision = columnInfo.getDatetimePrecision();
//
//        switch (sqlTypes) {
//            case CHAR: {
//                return "char" + ((length == null) ? "" : "(" + between(length, 1, 32767) + ")");
//            }
//            case NCHAR: {
//                return "nchar" + ((length == null) ? "" : "(" + between(length, 1, 32767) + ")");
//            }
//            case VARCHAR:
//            case NVARCHAR2: {
//                return "varchar" + ((length == null) ? "" : "(" + between(length, 1, 32767) + ")");
//            }
//            case TEXT: {
//                return "text";
//            }
//            case CLOB: {
//                return "clob";
//            }
//            case BIT: {
//                return "bit";
//            }
//            case TINYINT: {
//                return "tinyint";
//            }
//            case SMALLINT: {
//                return "smallint";
//            }
//            case INT: {
//                return "int";
//            }
//            case BIGINT: {
//                return "bigint";
//            }
//            case FLOAT: {
//                return "float";
//            }
//            case REAL: {
//                return "real";
//            }
//            case NUMERIC: {
//                if (numericPrecision != null && numericScale != null) {
//                    return "number(" + between(numericPrecision, 1, 38) + ", " + numericScale + ")";
//                } else if (numericPrecision == null && numericScale != null) {
//                    return "number(*, " + numericScale + ")";
//                } else if (numericPrecision != null) {
//                    return "number(" + between(numericPrecision, 1, 38) + ")";
//                } else {
//                    return "number";
//                }
//            }
//            case BINARY: {
//                return "binary" + ((length == null) ? "" : "(" + between(length, 0, 32767) + ")");
//            }
//            case VARBINARY: {
//                return "varbinary" + ((length == null) ? "" : "(" + between(length, 0, 32767) + ")");
//            }
//            case IMAGE: {
//                return "image";
//            }
//            case BFILE: {
//                return "bfile";
//            }
//            case DATE: {
//                return "date";
//            }
//            case TIME: {
//                return "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")");
//            }
//            case TIMESTAMP: {
//                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 9) + ")");
//            }
//            case TIME_WITH_TIME_ZONE: {
//                return "time" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 6) + ")") + " with time zone";
//            }
//            case TIMESTAMP_WITH_TIME_ZONE: {
//                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 9) + ")") + " with time zone";
//            }
//            case TIMESTAMP_WITH_LOCAL_TIME_ZONE: {
//                return "timestamp" + ((datetimePrecision == null) ? "" : "(" + between(datetimePrecision, 0, 9) + ")") + " with local time zone";
//            }
//            case INTERVAL_YEAR: {
//                return "interval year";
//            }
//            case INTERVAL_YEAR_TO_MONTH: {
//                return "interval year to month";
//            }
//            case INTERVAL_MONTH: {
//                return "interval month";
//            }
//            case INTERVAL_DAY: {
//                return "interval day";
//            }
//            case INTERVAL_DAY_TO_HOUR: {
//                return "interval day to hour";
//            }
//            case INTERVAL_DAY_TO_MINUTE: {
//                return "interval day to minute";
//            }
//            case INTERVAL_DAY_TO_SECOND: {
//                return "interval day to second";
//            }
//            case INTERVAL_HOUR: {
//                return "interval hour";
//            }
//            case INTERVAL_HOUR_TO_MINUTE: {
//                return "interval hour to minute";
//            }
//            case INTERVAL_HOUR_TO_SECOND: {
//                return "interval hour to second";
//            }
//            case INTERVAL_MINUTE: {
//                return "interval minute";
//            }
//            case INTERVAL_MINUTE_TO_SECOND: {
//                return "interval minute to second";
//            }
//            case INTERVAL_SECOND: {
//                return "interval second";
//            }
//            case ROWID: {
//                return "rowid";
//            }
//            case INDTAB:
//            case CLASS:
//            default:
//                return columnInfo.getDbType();
//        }
//    }
//
//    private static String replaceSingleQuotes(String str) {
//        if (StringUtils.isBlank(str)) {
//            return str;
//        }
//
//        return str.replace("'", "''");
//    }
//}

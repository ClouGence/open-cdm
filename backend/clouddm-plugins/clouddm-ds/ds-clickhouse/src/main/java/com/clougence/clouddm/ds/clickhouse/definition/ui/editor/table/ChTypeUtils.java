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
package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table;

import static com.clougence.adapter.clickhouse.ClickHouseAttributeNames.COLUMN_TYPE;
import static com.clougence.adapter.clickhouse.ClickHouseAttributeNames.DATETIME_ZONE;
import static com.clougence.utils.NumberUtils.between;

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/10/30 11:54
*/
class ChTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        ClickHouseTypes sqlTypes = ClickHouseTypes.valueOfCode(columnInfo.getDbType());

        String columnType = buildColumnType(sqlTypes, columnInfo, triggerContext);
        columnType = buildNullable(sqlTypes, Boolean.TRUE.equals(columnInfo.getNullable()), columnType);

        String defaultValue = buildDefault(sqlTypes, columnInfo, triggerContext);

        return columnType + " " + defaultValue;
    }

    private static String buildNullable(ClickHouseTypes sqlTypes, boolean nullable, String columnType) {
        if (!nullable) {
            return columnType;
        } else {
            switch (sqlTypes) {
                case Point:
                case Polygon:
                case MultiPolygon:
                    return columnType; // ClickHouse exception, code: 43, Nested type Point cannot be inside Nullable type (version 21.8.2.1)
                default:
                    return "Nullable(" + columnType + ")";
            }
        }
    }

    protected static String buildDefault(ClickHouseTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        StringBuilder sqlBuild = new StringBuilder("");
        String defaultValue = columnInfo.getDefaultValue();

        if (defaultValue == null) {
            return "";
        }

        // if (defaultFunc != null && "true".equals(defaultFunc.toLowerCase())) {
        // if (StringUtils.isNotBlank(defaultValue)) {
        // sqlBuild.append("default ").append(defaultValue);
        // }
        // return sqlBuild.toString();
        // }
        //
        // if (!"".equals(defaultValue)) {
        // switch (sqlTypes) {
        // case VARCHAR:
        // case JSON: {
        // String replaced = replaceSingleQuotes(defaultValue);
        // defaultValue = "'" + replaced + "'";
        // break;
        // }
        // case TIME:
        // case TIMESTAMP:
        // case DATETIME:
        // case DATE: {
        // defaultValue = "'" + defaultValue + "'";
        // break;
        // }
        // default:
        // break;
        // }
        //
        // sqlBuild.append("default ").append(defaultValue);
        // }
        return sqlBuild.toString();
    }

    private static String replaceSingleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    protected static String buildColumnType(ClickHouseTypes sqlTypes, EColumn columnInfo, TriggerContext triggerContext) {
        if (sqlTypes == null) {
            return columnInfo.getDbType();
        }
        Long length = columnInfo.getLength();
        Integer numericPrecision = columnInfo.getNumericPrecision();
        Integer numericScale = columnInfo.getNumericScale();
        String columnType = COLUMN_TYPE.getValue(columnInfo.getAttribute());

        switch (sqlTypes) {
            case Bool:
            case Int8:
            case Int16:
            case Int32:
            case Int64:
            case Int128:
            case Int256:
            case UInt8:
            case UInt16:
            case UInt32:
            case UInt64:
            case UInt128:
            case UInt256:
            case Float32:
            case Float64:
            case String:
            case UUID:
            case IPv4:
            case IPv6:
            case Point:
            case Ring:
            case Polygon:
            case MultiPolygon: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }
                return sqlTypes.getCodeKey();
            }
            case Decimal32: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                if (zeroOrNull(numericScale)) {
                    numericScale = 4;
                }
                return "Decimal32(" + between(numericScale, 0, 9) + ")";
            }
            case Decimal64: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                if (zeroOrNull(numericScale)) {
                    numericScale = 9;
                }
                return "Decimal64(" + between(numericScale, 0, 18) + ")";
            }
            case Decimal128: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                if (zeroOrNull(numericScale)) {
                    numericScale = 16;
                }
                return "Decimal128(" + between(numericScale, 0, 38) + ")";
            }
            case Decimal256: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                if (zeroOrNull(numericScale)) {
                    numericScale = 38;
                }
                return "Decimal256(" + between(numericScale, 0, 76) + ")";
            }
            case Decimal: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }
                if (numericPrecision == null && numericScale == null) {
                    return "Float64";
                }

                if (numericPrecision != null && numericScale == null) {
                    return "Decimal(" + Math.max(4, numericPrecision) + ",4)";
                } else if (numericPrecision == null) {
                    if (numericScale > 0) {
                        return "Decimal64(" + numericScale + ")";
                    } else {
                        return "Decimal64(4)";
                    }
                } else if (numericPrecision > 0 && numericScale == 0) {
                    return "Int64";
                } else {
                    return "Decimal(" + numericPrecision + ", " + between(numericScale, 0, numericPrecision) + ")";
                }
            }
            case FixedString: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                return "FixedString(" + length + ")";
            }
            case Date: {
                return "Date";
            }
            case Date32: {
                return "Date32";
            }
            case DateTime:
            case DateTime32: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                String datetimeZone = DATETIME_ZONE.getValue(columnInfo.getAttribute());
                if (StringUtils.isBlank(datetimeZone)) {
                    return "DateTime";
                } else {
                    return "DateTime('" + datetimeZone + "')";
                }
            }
            case DateTime64: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                }

                String datetimeZone = DATETIME_ZONE.getValue(columnInfo.getAttribute());
                Integer datetimePrecision = columnInfo.getDatetimePrecision();

                if (StringUtils.isBlank(datetimeZone)) {
                    if (datetimePrecision == null || datetimePrecision == 0) {
                        return "DateTime64";
                    } else {
                        return "DateTime64(" + datetimePrecision + ")";
                    }
                } else {
                    if (datetimePrecision == null || datetimePrecision == 0) {
                        return "DateTime64('" + datetimeZone + "')";
                    } else {
                        return "DateTime64(" + datetimePrecision + ", " + datetimePrecision + ")";
                    }
                }
            }
            case Enum:
            case Enum8:
            case Enum16:
            case Nested:
            case Tuple:
            case Array:
            case Map:
            case LowCardinality:
            case AggregateFunction:
            case SimpleAggregateFunction:
                throw new UnsupportedOperationException("the current version does not support it");
            case Nothing:
            case IntervalSecond:
            case IntervalMinute:
            case IntervalHour:
            case IntervalDay:
            case IntervalWeek:
            case IntervalMonth:
            case IntervalQuarter:
            case IntervalYear:
                throw new UnsupportedOperationException(sqlTypes.getCodeKey() + " cannot be used in tables.");
            case Nullable:
                throw new UnsupportedOperationException("wrong usage.");
            default: {
                if (StringUtils.isNotBlank(columnType)) {
                    return columnType;
                } else {
                    return columnInfo.getDbType();
                }
            }
        }
    }

    private static boolean zeroOrNull(Integer value) {
        return value == null || value <= 0;
    }
}

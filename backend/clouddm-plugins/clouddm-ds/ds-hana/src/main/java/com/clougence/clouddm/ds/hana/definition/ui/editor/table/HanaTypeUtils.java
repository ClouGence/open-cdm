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
package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import static com.clougence.adapter.hana.HanaTypes.*;

/**
 * @author wanshao create time is 2021/12/3
 **/
public class HanaTypeUtils {

    public static String buildColumnType(EColumn columnInfo, TriggerContext triggerContext) {
        String columnType = buildDataType(columnInfo);
        String options = buildOptions(columnInfo);
        String colNullable = buildNullable(Boolean.TRUE.equals(columnInfo.getNullable()));
        String colDefault = buildDefault(columnInfo);

        return columnType + options + colNullable + colDefault;
    }

    static String buildDefault(EColumn columnInfo) {
        if (columnInfo.getDefaultValue() == null) {
            return StringUtils.EMPTY;
        }
        String value = columnInfo.getDefaultValue();
        if (!columnInfo.isDefaultValueIsFunc()) {
            value = "'" + value.replace("'", "''") + "'";
        }
        return " DEFAULT " + value;
    }

    private static String buildOptions(EColumn eColumn) {
        StringBuilder sqlBuilder = new StringBuilder();
        String generationType = HanaAttributeNames.GENERATION_TYPE.getValue(eColumn.getAttribute());
        if (StringUtils.isBlank(generationType) && eColumn.isAutoGenerate()) {
            generationType = "BY DEFAULT AS IDENTITY";
        }

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

    static String buildDataType(EColumn columnInfo) {
        HanaTypes sqlTypes = HanaTypes.valueOfCode(columnInfo.getDbType());
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
                if (numericScale != null && (numericPrecision == null || numericScale > numericPrecision)) {
                    throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_DECIMAL_SCALE_INVALID);
                }
                if (numericPrecision != null && numericScale != null) {
                    return DECIMAL.getCodeKey() + "(" + checkedRange(numericPrecision, 1, 38) + ", " + checkedRange(numericScale, 0, 38) + ")";
                } else if (numericPrecision != null) {
                    return DECIMAL.getCodeKey() + "(" + checkedRange(numericPrecision, 1, 38) + ")";
                } else {
                    return DECIMAL.getCodeKey();
                }
            }
            case SMALLDECIMAL:
                return SMALLDECIMAL.getCodeKey();
            case REAL:
                return REAL.getCodeKey();
            case FLOAT:
                if (numericPrecision != null) {
                    return "FLOAT(" + checkedRange(numericPrecision, 1, 53) + ")";
                }
                return FLOAT.getCodeKey();
            case DOUBLE:
                return DOUBLE.getCodeKey();
            case BOOLEAN:
                return BOOLEAN.getCodeKey();
            case CHAR: {
                if (length == null) {
                    return CHAR.getCodeKey();
                } else {
                    return CHAR.getCodeKey() + "(" + length + ")";
                }
            }
            case VARCHAR: {
                if (length == null) {
                    return VARCHAR.getCodeKey();
                } else {
                    return VARCHAR.getCodeKey() + "(" + checkedRange(length, 1, 5000) + ")";
                }
            }
            case NCHAR: {
                if (length == null) {
                    return NCHAR.getCodeKey();
                } else {
                    return NCHAR.getCodeKey() + "(" + length + ")";
                }
            }
            case NVARCHAR: {
                if (length == null) {
                    return NVARCHAR.getCodeKey();
                } else {
                    return NVARCHAR.getCodeKey() + "(" + checkedRange(length, 1, 5000) + ")";
                }
            }
            case ALPHANUM: {
                if (length == null) {
                    return ALPHANUM.getCodeKey();
                } else {
                    return ALPHANUM.getCodeKey() + "(" + checkedRange(length, 1, 127) + ")";
                }
            }
            case SHORTTEXT: {
                if (length == null) {
                    return SHORTTEXT.getCodeKey();
                } else {
                    return SHORTTEXT.getCodeKey() + "(" + checkedRange(length, 1, 5000) + ")";
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
                if (length == null) {
                    return BINARY.getCodeKey();
                } else {
                    return BINARY.getCodeKey() + "(" + length + ")";
                }
            case VARBINARY:
                if (length == null) {
                    return VARBINARY.getCodeKey();
                } else {
                    return VARBINARY.getCodeKey() + "(" + checkedRange(length, 1, 5000) + ")";
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
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_TYPE_UNSUPPORTED, sqlTypes);
            }
        }
    }

    private static long checkedRange(long value, long min, long max) {
        if (value < min || value > max) {
            throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_TYPE_PARAMETER_RANGE, value, min, max);
        }
        return value;
    }
}

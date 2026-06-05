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
package com.clougence.clouddm.ds.dameng.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;

/**
 * https://eco.dameng.com/document/dm/zh-cn/pm/dm8_sql-data-types-operators.html
 */
public class DmColReader extends AbstractColReader {

    @SneakyThrows
    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        switch (colType.toLowerCase()) {
            case "bit":
                return BOOLEAN_VALUE_FETCHER;
            case "char":
            case "nchar":
            case "varchar":
            case "nvarchar2":
                return STRING_VALUE_FETCHER;
            case " rowid":
                return STRING_VALUE_FETCHER;
            case "byte":
            case "tinyint":
                return BYTE_VALUE_FETCHER;
            case "smallint":
                return SHORT_VALUE_FETCHER;
            case "int":
            case "integer":
                return INTEGER_VALUE_FETCHER;
            case "bigint":
                return LONG_VALUE_FETCHER;
            case "numeric":
            case "number":
            case "decimal":
            case "dec":
                return STRING_VALUE_FETCHER;
            case "float":
            case "double":
            case "double precision":
                return DOUBLE_VALUE_FETCHER;
            case "real":
                return FLOAT_VALUE_FETCHER;
            case "data":
                return DATE_VALUE_FETCHER;
            case "time":
                return TIME_VALUE_FETCHER;
            case "timestamp":
                return DATETIME_VALUE_FETCHER;
            case "time with time zone":
                return TIMEZ_VALUE_FETCHER;
            case "timestamp with time zone":
            case "timestamp with local time zone":
                return DATETIMEZ_VALUE_FETCHER;
            case "interval year":
            case "interval year to month":
            case "interval month":
            case "interval day":
            case "interval day to hour":
            case "interval day to minute":
            case "interval day to second":
            case "interval hour":
            case "interval hour to minute":
            case "interval hour to second":
            case "interval minute":
            case "interval minute to second":
            case "interval second":
                return ISO8601_VALUE_FETCHER;
            case "text":
            case "clob":
            case "long":
            case "longvarchar":
                return STRING_AS_CLOB_FETCHER;
            case "binary":
            case "varbinary":
            case "raw":
                return BYTES_VALUE_FETCHER;
            case "image":
            case "blob":
            case "longvarbinary":
            case "bfile":
                return BYTES_AS_STREAM_FETCHER;
            default:
                return null;
        }
    }
}

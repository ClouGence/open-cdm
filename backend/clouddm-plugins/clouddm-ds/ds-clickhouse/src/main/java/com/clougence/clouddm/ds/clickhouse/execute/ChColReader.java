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
package com.clougence.clouddm.ds.clickhouse.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class ChColReader extends AbstractColReader {

    private static final ValueFetcher TUPLE_VALUE_FETCHER = new ChStructuredValueFetcher(true);
    private static final ValueFetcher ARRAY_VALUE_FETCHER = new ChStructuredValueFetcher(false);
    private static final ValueFetcher TIME_VALUE_FETCHER  = new ChTimeValueFetcher();

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "").toLowerCase();
        colType = unwrapColumnType(colType);
        if (colType.startsWith("fixedstring")) {
            colType = "fixedstring";
        } else if (colType.startsWith("decimal")) {
            colType = "decimal";
        } else if (colType.startsWith("datetime")) {
            colType = "datetime";
        } else if (colType.startsWith("time64")) {
            colType = "time64";
        } else if (colType.startsWith("enum")) {
            colType = "enum";
        } else if (colType.startsWith("json")) {
            colType = "json";
        } else if (colType.startsWith("object")) {
            colType = "object";
        } else if (colType.startsWith("interval")) {
            colType = "interval";
        } else if (colType.startsWith("map")) {
            colType = "map";
        } else if (colType.startsWith("array")) {
            colType = "array";
        } else if (colType.startsWith("tuple")) {
            colType = "tuple";
        } else if (colType.startsWith("dynamic")) {
            colType = "dynamic";
        } else if (colType.startsWith("variant")) {
            colType = "variant";
        } else if (colType.startsWith("nested")) {
            colType = "nested";
        }
        switch (colType) {
            case "bool":
                return BOOLEAN_VALUE_FETCHER;
            case "int8":
                return BYTE_VALUE_FETCHER;
            case "int16":
            case "uint8":
                return SHORT_VALUE_FETCHER;
            case "int32":
            case "uint16":
                return INTEGER_VALUE_FETCHER;
            case "int64":
            case "uint32":
                return LONG_VALUE_FETCHER;
            case "uint64":
            case "int128":
            case "uint128":
            case "int256":
            case "uint256":
                return BIGINTEGER_VALUE_FETCHER;
            case "float32":
            case "float64":
            case "bfloat16":
                return FLOAT_VALUE_FETCHER;
            case "decimal":
                return STRING_VALUE_FETCHER;
            case "string":
            case "fixedstring":
            case "ipv4":
            case "ipv6":
            case "uuid":
            case "json":
            case "object":
            case "map":
            case "array":
            case "dynamic":
            case "variant":
            case "enum":
            case "nothing":
            case "interval":
                return STRING_VALUE_FETCHER;
            case "tuple":
            case "point":
                return TUPLE_VALUE_FETCHER;
            case "nested":
            case "ring":
            case "polygon":
            case "multipolygon":
            case "linestring":
            case "multilinestring":
                return ARRAY_VALUE_FETCHER;
            case "date":
            case "date32":
                return DATE_VALUE_FETCHER;
            case "time":
            case "time64":
                return TIME_VALUE_FETCHER;
            case "datetime":
            case "datetime64":
                return DATETIME_VALUE_FETCHER;
            default:
                return null;
        }
    }

    private String unwrapColumnType(String colType) {
        while (true) {
            if (colType.startsWith("nullable(") || colType.startsWith("lowcardinality(")) {
                colType = colType.substring(colType.indexOf('(') + 1, colType.lastIndexOf(')')).trim();
            } else if (colType.startsWith("simpleaggregatefunction(")) {
                // Skip the function, including parameters such as groupUniqArrayArray(2).
                int depth = 0;
                int index = colType.indexOf('(') + 1;
                for (; index < colType.length(); index++) {
                    char ch = colType.charAt(index);
                    if (ch == '(') {
                        depth++;
                    } else if (ch == ')') {
                        depth--;
                    } else if (ch == ',' && depth == 0) {
                        break;
                    }
                }
                colType = colType.substring(index + 1, colType.lastIndexOf(')')).trim();
            } else {
                return colType;
            }
        }
    }
}

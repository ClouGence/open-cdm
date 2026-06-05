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
package com.clougence.clouddm.ds.starrocks.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * https://docs.starrocks.io/zh/docs/sql-reference/data-types/
 *
 * @author mode create time is 2021/1/12
 **/
public class SrColReader extends AbstractColReader {

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        switch (colType.toLowerCase()) {
            case "boolean":
                return BOOLEAN_VALUE_FETCHER;
            case "tinyint":
                return BYTE_VALUE_FETCHER;
            case "smallint":
                return SHORT_VALUE_FETCHER;
            case "int":
                return INTEGER_VALUE_FETCHER;
            case "bigint":
                return LONG_VALUE_FETCHER;
            case "largeint":
                return BIGINTEGER_VALUE_FETCHER;
            case "float":
                return FLOAT_VALUE_FETCHER;
            case "double":
                return DOUBLE_VALUE_FETCHER;
            case "decimal":
                return STRING_VALUE_FETCHER;
            case "char":
            case "string":
            case "varchar":
                return STRING_VALUE_FETCHER;
            case "binary":
            case "varbinary":
                return BYTES_VALUE_FETCHER;
            case "date":
                return DATE_VALUE_FETCHER;
            case "datetime":
                return DATETIME_VALUE_FETCHER;
            case "array":
            case "json":
            case "map":
            case "struct":
            case "bitmap":
            case "hll":
                return STRING_AS_CLOB_FETCHER;
            default:
                return null;
        }
    }
}

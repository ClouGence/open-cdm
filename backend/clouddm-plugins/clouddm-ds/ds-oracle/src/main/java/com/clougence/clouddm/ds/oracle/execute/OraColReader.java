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
package com.clougence.clouddm.ds.oracle.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * https://docs.oracle.com/en/database/oracle/oracle-database/21/sqlrf/Data-Types.html
 *
 * @author mode create time is 2021/1/12
 **/
public class OraColReader extends AbstractColReader {

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        switch (colType.toLowerCase()) {
            case "char":
            case "nchar":
            case "varchar2":
            case "nvarchar":
            case "nvarchar2":
            case "long":
                return STRING_VALUE_FETCHER;
            case "clob":
            case "nclob":
            case "json":
            case "sys.xmltype":
                return STRING_AS_CLOB_FETCHER;
            case "binary_float":
                return FLOAT_VALUE_FETCHER;
            case "binary_double":
                return DOUBLE_VALUE_FETCHER;
            case "number":
            case "float":
                return STRING_VALUE_FETCHER;
            case "raw":
                return BYTES_VALUE_FETCHER;
            case "bfile":
            case "blob":
            case "long raw":
                return BYTES_AS_STREAM_FETCHER;
            case "date":
                //                return DATE_VALUE_FETCHER;
            case "timestamp":
                return DATETIME_VALUE_FETCHER;
            case "timestamp with time zone":
            case "timestamp with local time zone":
                return DATETIMEZ_VALUE_FETCHER;
            case "intervalym":
            case "intervalds":
                return STRING_VALUE_FETCHER;
            case "rowid":
            case "urowid":
                return STRING_VALUE_FETCHER;
            case "plsql_boolean":
                return BOOLEAN_VALUE_FETCHER;
            default:
                return null;
        }
    }
}

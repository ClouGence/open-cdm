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
package com.clougence.clouddm.dsfamily.execute;

import java.sql.JDBCType;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

public class DefaultColReader extends AbstractColReader {

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        JDBCType jdbcType = colMetaData.getJdbcType();
        switch (jdbcType) {
            case BLOB:
                return BYTES_AS_BLOB_FETCHER;
            case BINARY:
            case VARBINARY:
            case LONGVARBINARY:
                return BYTES_AS_STREAM_FETCHER;
            case BOOLEAN:
                return BOOLEAN_VALUE_FETCHER;
            case TINYINT:
                return BYTE_VALUE_FETCHER;
            case SMALLINT:
                return SHORT_VALUE_FETCHER;
            case INTEGER:
                return INTEGER_VALUE_FETCHER;
            case BIGINT:
                return LONG_VALUE_FETCHER;
            case REAL:
                return FLOAT_VALUE_FETCHER;
            case FLOAT:
            case DOUBLE:
                return DOUBLE_VALUE_FETCHER;
            case NUMERIC:
            case DECIMAL:
                return STRING_VALUE_FETCHER;
            case CHAR:
            case NCHAR:
            case VARCHAR:
            case NVARCHAR:
            case DATALINK:
                return STRING_VALUE_FETCHER;
            case CLOB:
            case NCLOB:
                return STRING_AS_CLOB_FETCHER;
            case LONGVARCHAR:
            case LONGNVARCHAR:
                return STRING_AS_READER_FETCHER;
            case DATE:
                return DATE_VALUE_FETCHER;
            case TIME:
                return TIME_VALUE_FETCHER;
            case TIME_WITH_TIMEZONE:
                return TIMEZ_VALUE_FETCHER;
            case TIMESTAMP:
                return DATETIME_VALUE_FETCHER;
            case TIMESTAMP_WITH_TIMEZONE:
                return DATETIMEZ_VALUE_FETCHER;
            default:
                return null;
        }
    }
}

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

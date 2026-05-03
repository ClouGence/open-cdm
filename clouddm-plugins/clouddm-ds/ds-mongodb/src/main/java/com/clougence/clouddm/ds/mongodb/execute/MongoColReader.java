package com.clougence.clouddm.ds.mongodb.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.drivers.adapter.AdapterType;
import com.clougence.utils.StringUtils;

/**
 * @author mode create time is 2021/1/12
 **/
public class MongoColReader extends AbstractColReader {

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        switch (colType) {
            case AdapterType.Boolean:
                return BOOLEAN_VALUE_FETCHER;
            case AdapterType.Byte:
                return BYTE_VALUE_FETCHER;
            case AdapterType.Short:
                return SHORT_VALUE_FETCHER;
            case AdapterType.Int:
                return INTEGER_VALUE_FETCHER;
            case AdapterType.Long:
                return LONG_VALUE_FETCHER;
            case AdapterType.Float:
                return FLOAT_VALUE_FETCHER;
            case AdapterType.Double:
                return DOUBLE_VALUE_FETCHER;
            case AdapterType.BigDecimal:
                return BIGDECIMAL_VALUE_FETCHER;
            case AdapterType.BigInteger:
                return BIGINTEGER_VALUE_FETCHER;
            case AdapterType.String:
                return STRING_AS_READER_FETCHER;
            case AdapterType.Bytes:
                return BYTES_VALUE_FETCHER;
            case AdapterType.SqlDate:
                return DATE_VALUE_FETCHER;
            case AdapterType.SqlTime:
                return TIME_VALUE_FETCHER;
            case AdapterType.SqlTimestamp:
                return DATETIME_VALUE_FETCHER;
            case AdapterType.OffsetTime:
                return TIMEZ_VALUE_FETCHER;
            case AdapterType.OffsetDateTime:
                return DATETIMEZ_VALUE_FETCHER;
            default:
                return STRING_AS_READER_FETCHER;
        }
    }
}

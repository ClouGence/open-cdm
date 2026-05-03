package com.clougence.clouddm.dsfamily.execute;

import com.clougence.clouddm.dsfamily.execute.fetcher.*;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

public abstract class AbstractColReader implements ColReader {

    public static final ValueFetcher BOOLEAN_VALUE_FETCHER    = new BooleanValueFetcher();
    public static final ValueFetcher BYTE_VALUE_FETCHER       = new ByteValueFetcher();
    public static final ValueFetcher SHORT_VALUE_FETCHER      = new ShortValueFetcher();
    public static final ValueFetcher INTEGER_VALUE_FETCHER    = new IntegerValueFetcher();
    public static final ValueFetcher LONG_VALUE_FETCHER       = new LongValueFetcher();
    public static final ValueFetcher BIGINTEGER_VALUE_FETCHER = new BigIntegerValueFetcher();
    public static final ValueFetcher BIGDECIMAL_VALUE_FETCHER = new BigDecimalValueFetcher();
    public static final ValueFetcher FLOAT_VALUE_FETCHER      = new FloatValueFetcher();
    public static final ValueFetcher DOUBLE_VALUE_FETCHER     = new DoubleValueFetcher();
    public static final ValueFetcher STRING_VALUE_FETCHER     = new StringValueFetcher();
    public static final ValueFetcher STRING_AS_CLOB_FETCHER   = new StringAsClobFetcher();
    public static final ValueFetcher STRING_AS_READER_FETCHER = new StringAsReaderFetcher();
    public static final ValueFetcher BYTES_VALUE_FETCHER      = new BytesValueFetcher();
    public static final ValueFetcher BYTES_AS_BLOB_FETCHER    = new BytesAsBlobFetcher();
    public static final ValueFetcher BYTES_AS_STREAM_FETCHER  = new BytesAsStreamFetcher();
    public static final ValueFetcher DATE_VALUE_FETCHER       = new DateValueFetcher();
    public static final ValueFetcher TIME_VALUE_FETCHER       = new TimeValueFetcher();
    public static final ValueFetcher TIMEZ_VALUE_FETCHER      = new TimeWithZoneValueFetcher();
    public static final ValueFetcher DATETIME_VALUE_FETCHER   = new DateTimeValueFetcher();
    public static final ValueFetcher DATETIMEZ_VALUE_FETCHER  = new DateTimeWithZoneValueFetcher();
    public static final ValueFetcher ISO8601_VALUE_FETCHER    = new ISO8601ValueFetcher();
    public static final ValueFetcher GEOMETRY_VALUE_FETCHER   = new GeometryValueFetcher();
}

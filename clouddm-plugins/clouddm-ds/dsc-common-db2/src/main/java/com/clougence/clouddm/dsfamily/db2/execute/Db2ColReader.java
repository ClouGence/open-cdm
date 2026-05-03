package com.clougence.clouddm.dsfamily.db2.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class Db2ColReader extends AbstractColReader {

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "").toLowerCase();
        switch (colType) {
            case "boolean":
                return BOOLEAN_VALUE_FETCHER;
            case "smallint":
                return SHORT_VALUE_FETCHER;
            case "integer":
                return INTEGER_VALUE_FETCHER;
            case "bigint":
                return LONG_VALUE_FETCHER;
            case "real":
                return FLOAT_VALUE_FETCHER;
            case "double":
                return DOUBLE_VALUE_FETCHER;
            case "decfloat":
            case "decimal":
                return STRING_VALUE_FETCHER;
            case "char":
            case "nchar":
            case "varchar":
            case "nvarchar":
                return STRING_VALUE_FETCHER;
            case "datalink":
            case "graphic":
            case "clob":
            case "nclob":
            case "dbclob":
            case "xml":
            case "long varchar":
            case "vargraphic":
            case "long vargraphic":
                return STRING_AS_CLOB_FETCHER;
            case "binary":
            case "varbinary":
            case "blob":
            case "char for bit data":
            case "varchar for bit data":
            case "long varchar for bit data":
                return BYTES_AS_STREAM_FETCHER;
            case "date":
                return DATE_VALUE_FETCHER;
            case "time":
                return TIME_VALUE_FETCHER;
            case "timestamp":
                return DATETIME_VALUE_FETCHER;
            default:
                return null;
        }
    }
}

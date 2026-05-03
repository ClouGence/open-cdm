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

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "").toLowerCase();
        if (colType.startsWith("nullable(")) {
            colType = colType.substring(9, colType.lastIndexOf(")"));
        }
        if (colType.startsWith("fixedstring")) {
            colType = "fixedstring";
        } else if (colType.startsWith("decimal")) {
            colType = "decimal";
        } else if (colType.startsWith("datetime64")) {
            colType = "datetime64";
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
                return STRING_VALUE_FETCHER;
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
}

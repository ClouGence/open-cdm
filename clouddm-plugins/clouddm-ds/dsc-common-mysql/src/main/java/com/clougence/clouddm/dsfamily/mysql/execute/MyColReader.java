package com.clougence.clouddm.dsfamily.mysql.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.dsfamily.mysql.execute.fetcher.BitValueFetcher;
import com.clougence.clouddm.dsfamily.mysql.execute.fetcher.MyGeometryValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * <li>https://dev.mysql.com/doc/refman/5.7/en/data-types.html</li>
 * <li>https://dev.mysql.com/doc/refman/8.0/en/data-types.html</li>
 *
 * @author mode create time is 2021/1/12
 **/
public class MyColReader extends AbstractColReader {

    public static final ValueFetcher BIT_VALUE_FETCHER   = new BitValueFetcher();
    public static final ValueFetcher MY_GEOMETRY_FETCHER = new MyGeometryValueFetcher();

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        switch (colType.toLowerCase()) {
            case "bit":
                return BIT_VALUE_FETCHER;
            case "tinyint":
                return BYTE_VALUE_FETCHER;
            case "tinyint unsigned":
                return SHORT_VALUE_FETCHER;
            case "smallint":
                return SHORT_VALUE_FETCHER;
            case "smallint unsigned":
                return INTEGER_VALUE_FETCHER;
            case "mediumint":
            case "mediumint unsigned":
            case "int":
                return INTEGER_VALUE_FETCHER;
            case "int unsigned":
            case "bigint":
                return LONG_VALUE_FETCHER;
            case "bigint unsigned":
                return BIGINTEGER_VALUE_FETCHER;
            case "dec":
            case "decimal":
            case "decimal unsigned":
                return STRING_VALUE_FETCHER;
            case "float":
                return FLOAT_VALUE_FETCHER;
            case "float unsigned":
            case "double":
                return DOUBLE_VALUE_FETCHER;
            case "double unsigned":
            case "double precision":
                return STRING_VALUE_FETCHER;
            case "char":
            case "varchar":
            case "enum":
            case "set":
                return STRING_VALUE_FETCHER;
            case "binary":
            case "varbinary":
                return BYTES_VALUE_FETCHER;
            case "tinytext":
            case "text":
                return STRING_VALUE_FETCHER;
            case "tinyblob":
            case "blob":
                return BYTES_VALUE_FETCHER;
            case "mediumtext":
            case "longtext":
            case "json":
                return STRING_AS_CLOB_FETCHER;
            case "mediumblob":
            case "longblob":
                return BYTES_AS_STREAM_FETCHER;
            case "date":     // '0000-00-00'
            case "time":     // '-838:59:59'/'838:59:59'
            case "datetime": // '0000-00-00 00:00:00'
            case "timestamp":// '0000-00-00 00:00:00'
                return STRING_VALUE_FETCHER;
            case "year":
                return SHORT_VALUE_FETCHER;
            case "geometry":
            case "geomcollection":
                return MY_GEOMETRY_FETCHER;
            case "point":
            case "linestring":
            case "polygon":
            case "multipoint":
            case "multilinestring":
            case "multipolygon":
                return STRING_VALUE_FETCHER;
            default:
                return null;
        }
    }
}

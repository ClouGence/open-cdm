package com.clougence.clouddm.ds.sqlserver.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.sqlserver.execute.fetcher.MsSqlGeographyValueFetcher;
import com.clougence.clouddm.ds.sqlserver.execute.fetcher.MsSqlGeometryValueFetcher;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * https://docs.microsoft.com/zh-cn/sql/t-sql/data-types/data-types-transact-sql?view=sql-server-ver16
 * https://learn.microsoft.com/en-us/sql/t-sql/data-types/ntext-text-and-image-transact-sql?view=sql-server-ver16
 *
 * @author mode create time is 2021/1/12
 **/
public class MsSqlColReader extends AbstractColReader {

    public static final ValueFetcher MS_GEOMETRY_FETCHER  = new MsSqlGeometryValueFetcher();
    public static final ValueFetcher MS_GEOGRAPHY_FETCHER = new MsSqlGeographyValueFetcher();

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        switch (colType.toLowerCase()) {
            case "bit":
                return BOOLEAN_VALUE_FETCHER;
            case "tinyint":
                return BYTE_VALUE_FETCHER;
            case "smallint":
                return SHORT_VALUE_FETCHER;
            case "int":
                return INTEGER_VALUE_FETCHER;
            case "bigint":
                return LONG_VALUE_FETCHER;
            case "decimal":
            case "numeric":
            case "smallmoney":
            case "money":
                return STRING_VALUE_FETCHER;
            case "float":
            case "real":
                return DOUBLE_VALUE_FETCHER;
            case "binary":
            case "timestamp":
            case "rowversion":
                return BYTES_VALUE_FETCHER;
            case "varbinary":
            case "image":
                return BYTES_AS_STREAM_FETCHER;
            case "char":
            case "nchar":
            case "sysname":
            case "uniqueidentifier":
            case "hierarchyid":
                return STRING_VALUE_FETCHER;
            case "varchar":
            case "nvarchar":
            case "text":
            case "ntext":
            case "xml":
                return STRING_AS_READER_FETCHER;
            case "date":
                return DATE_VALUE_FETCHER;
            case "time":
                return TIME_VALUE_FETCHER;
            case "datetime":
            case "datetime2":
            case "smalldatetime":
                return DATETIME_VALUE_FETCHER;
            case "datetimeoffset":
                return DATETIMEZ_VALUE_FETCHER;
            case "geometry":
                return MS_GEOMETRY_FETCHER;
            case "geography":
                return MS_GEOGRAPHY_FETCHER;
            default:
                return null;
        }
    }
}

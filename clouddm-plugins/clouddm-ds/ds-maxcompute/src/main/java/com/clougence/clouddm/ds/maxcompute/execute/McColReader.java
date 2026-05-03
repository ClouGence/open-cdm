package com.clougence.clouddm.ds.maxcompute.execute;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;

/**
 * https://help.aliyun.com/zh/maxcompute/user-guide/data-types/
 */
public class McColReader extends AbstractColReader {

    @SneakyThrows
    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        String type = colType.toLowerCase();
        if ("boolean".equals(type)) {
            return BOOLEAN_VALUE_FETCHER;
        } else if ("float".equals(type)) {
            return FLOAT_VALUE_FETCHER;
        } else if ("double".equals(type)) {
            return DOUBLE_VALUE_FETCHER;
        } else if (type.startsWith("decimal") || type.startsWith("varchar") || type.startsWith("char") || type.startsWith("string")) {
            return STRING_VALUE_FETCHER;
        } else if ("tinyint".equals(type)) {
            return BYTE_VALUE_FETCHER;
        } else if ("smallint".equals(type)) {
            return SHORT_VALUE_FETCHER;
        } else if ("int".equals(type)) {
            return INTEGER_VALUE_FETCHER;
        } else if ("bigint".equals(type)) {
            return BIGINTEGER_VALUE_FETCHER;
        } else if ("date".equals(type)) {
            return DATE_VALUE_FETCHER;
        } else if ("datetime".equals(type)) {
            return DATETIME_VALUE_FETCHER;
        } else if ("binary".equals(type)) {
            return BYTES_VALUE_FETCHER;
        } else if ("timestamp".equals(type) || "timestamp_ntz".equals(type)) {
            return DATETIME_VALUE_FETCHER;
        } else {
            return null;
        }
    }
}

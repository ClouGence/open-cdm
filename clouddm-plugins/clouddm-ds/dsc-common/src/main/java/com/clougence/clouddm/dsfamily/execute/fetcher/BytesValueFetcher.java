package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.ArrayUtils;

public class BytesValueFetcher extends BytesAsBlobFetcher {

    protected BytesValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BytesValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof BytesValueFCD)) {
            byte[] bytes = rs.getBytes(columnName);
            if (bytes == null) {
                fcd = BytesValueFCD.ofInMemory(true, 0, 0, null);
            } else if (bytes.length == 0) {
                fcd = BytesValueFCD.ofInMemory(true, 0, 0, ArrayUtils.EMPTY_BYTE_ARRAY);
            } else {
                fcd = BytesValueFCD.ofInMemory(false, bytes.length, bytes.length, bytes);
            }
            ctx.setContext(fcd);
        } else {
            fcd = (BytesValueFCD) ctx.getContext();
        }
        return fcd;
    }
}

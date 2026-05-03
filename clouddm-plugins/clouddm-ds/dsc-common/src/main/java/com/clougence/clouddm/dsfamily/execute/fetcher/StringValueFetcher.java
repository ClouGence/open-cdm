package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.ArrayUtils;

public class StringValueFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            String str = rs.getString(columnName);
            if (str == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else if (str.isEmpty()) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, "", ArrayUtils.EMPTY_BYTE_ARRAY);
            } else {
                byte[] dataBytes = str.getBytes();
                fcd = StringValueFCD.ofInMemory(true, str.length(), str.length(), str, dataBytes);
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }
}

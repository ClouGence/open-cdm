package com.clougence.clouddm.dsfamily.mysql.execute.fetcher;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

public class BitValueFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            String str = rs.getString(columnName);
            if (str == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                str = new BigInteger(str, 10).toString(2);
                byte[] b = str.getBytes();
                fcd = StringValueFCD.ofInMemory(true, str.length(), str.length(), str, b);
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }
}

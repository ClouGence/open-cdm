package com.clougence.clouddm.dsfamily.postgres.execute.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.StringUtils;

public class MoneyValueFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            String str = rs.getString(columnName);
            if (str == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                char[] chars = str.toCharArray();
                int index = -1;
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    if (Character.isDigit(c)) {
                        index = i;
                        break;
                    }
                }
                String data;
                if (index == -1) {
                    data = StringUtils.replace(str, ",", "");
                } else {
                    data = StringUtils.replace(str.substring(index), ",", "");
                }
                byte[] b = data.getBytes();
                fcd = StringValueFCD.ofInMemory(true, data.length(), data.length(), data, b);
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }
}

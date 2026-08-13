/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

/**
 * ClickHouse Tuple columns are read by the driver as Object[], whose toString() is
 * not readable, so format the elements explicitly.
 */
public class ChTupleValueFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            Object value = rs.getObject(columnName);
            if (value == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                String str = formatTuple(value);
                byte[] dataBytes = str.getBytes();
                fcd = StringValueFCD.ofInMemory(true, str.length(), str.length(), str, dataBytes);
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private static String formatTuple(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(formatTuple(array[i]));
            }
            return sb.append(")").toString();
        }
        return value.toString();
    }
}

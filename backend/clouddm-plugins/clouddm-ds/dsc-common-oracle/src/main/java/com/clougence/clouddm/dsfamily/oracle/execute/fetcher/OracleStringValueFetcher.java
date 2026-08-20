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
package com.clougence.clouddm.dsfamily.oracle.execute.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.StringUtils;

import oracle.jdbc.OracleResultSet;
import oracle.sql.CHAR;
import oracle.sql.CharacterSet;

public class OracleStringValueFetcher extends StringValueFetcher {

    private final String clientCharset;

    public OracleStringValueFetcher(String clientCharset){
        this.clientCharset = clientCharset;
    }

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        if (StringUtils.isBlank(this.clientCharset)) {
            return super.fetchState(columnName, rs, ctx);
        }
        if (ctx.getContext() instanceof StringValueFCD) {
            return (StringValueFCD) ctx.getContext();
        }

        OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
        CHAR rawValue = oracleResultSet.getCHAR(columnName);
        StringValueFCD fcd;
        if (rawValue == null) {
            fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
        } else {
            byte[] rawBytes = rawValue.getBytes();
            if (rawBytes.length == 0) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, "", ArrayUtils.EMPTY_BYTE_ARRAY);
            } else {
                CharacterSet characterSet = OracleClientCharsetRegistry.resolve(rs.getStatement().getConnection(), this.clientCharset);
                String value = characterSet.toString(rawBytes, 0, rawBytes.length);
                fcd = StringValueFCD.ofInMemory(true, value.length(), value.length(), value, value.getBytes());
            }
        }
        ctx.setContext(fcd);
        return fcd;
    }
}

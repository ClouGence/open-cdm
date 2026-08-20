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
package com.clougence.clouddm.dsfamily.mysql.execute.fetcher;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

public class VectorValueFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            byte[] vectorBytes = rs.getBytes(columnName);
            if (vectorBytes == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else if (vectorBytes.length % Float.BYTES != 0) {
                throw new SQLException("Invalid VECTOR byte length: " + vectorBytes.length);
            } else {
                ByteBuffer vectorBuffer = ByteBuffer.wrap(vectorBytes).order(ByteOrder.LITTLE_ENDIAN);
                StringBuilder displayValue = new StringBuilder(vectorBytes.length + 2);
                displayValue.append('[');
                while (vectorBuffer.hasRemaining()) {
                    if (displayValue.length() > 1) {
                        displayValue.append(',');
                    }
                    displayValue.append(Float.toString(vectorBuffer.getFloat()));
                }
                displayValue.append(']');

                String str = displayValue.toString();
                byte[] displayBytes = str.getBytes(StandardCharsets.UTF_8);
                fcd = StringValueFCD.ofInMemory(true, str.length(), str.length(), str, displayBytes);
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }
}

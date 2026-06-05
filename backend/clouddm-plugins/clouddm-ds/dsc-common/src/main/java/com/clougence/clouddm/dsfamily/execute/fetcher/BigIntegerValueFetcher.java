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
package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextBytesStreamInfo;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

public class BigIntegerValueFetcher extends AbstractValueFetcher {

    public BigIntegerValueFetcher(){
        super(ValueType.BigInteger);
    }

    private static final class BigIntValueFCD implements ValueFetcherContextData, ValueFetcherContextBytesStreamInfo {

        private final BigInteger data;
        private final byte[]     dataBytes;
        private final long       size;

        private BigIntValueFCD(BigInteger data){
            this.data = data;
            if (data != null) {
                this.dataBytes = data.toByteArray();
                this.size = this.dataBytes.length;
            } else {
                this.dataBytes = null;
                this.size = 0L;
            }
        }

        @Override
        public boolean isComplete() { return true; }

        @Override
        public long fullSize() {
            return this.size;
        }

        @Override
        public long readSize() {
            return this.size;
        }

        @Override
        public byte[] sampleData(int displayBytes) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void free() {
        }
    }

    private static BigIntValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigIntValueFCD context;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof BigIntValueFCD)) {
            String str = rs.getString(columnName);
            if (str == null) {
                context = new BigIntValueFCD(null);
            } else {
                context = new BigIntValueFCD(new BigInteger(str));
            }

            ctx.setContext(context);
            return context;
        } else {
            return (BigIntValueFCD) ctx.getContext();
        }
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return fetchState(columnName, rs, ctx).readSize();
    }

    @Override
    public BigInteger getBigInteger(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return fetchState(columnName, rs, ctx).data;
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigInteger bigInteger = getBigInteger(columnName, rs, ctx);
        return bigInteger == null ? null : new BigDecimal(bigInteger);
    }

    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigInteger v = fetchState(columnName, rs, ctx).data;
        return v == null ? null : v.toString(10);
    }

    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigInteger v = fetchState(columnName, rs, ctx).data;
        return v == null ? null : new StringReader(v.toString(10));
    }

    @Override
    public byte[] getBytes(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return fetchState(columnName, rs, ctx).dataBytes;
    }

    @Override
    public InputStream getInputStream(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        byte[] v = fetchState(columnName, rs, ctx).dataBytes;
        return v == null ? null : new ByteArrayInputStream(v);
    }
}

package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextBytesStreamInfo;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

public class BigDecimalValueFetcher extends AbstractValueFetcher {

    public BigDecimalValueFetcher(){
        super(ValueType.BigDecimal);
    }

    private static final class BigDecimalValueFCD implements ValueFetcherContextData, ValueFetcherContextBytesStreamInfo {

        private final BigDecimal data;
        private final long       size;

        private BigDecimalValueFCD(BigDecimal data){
            this.data = data;
            if (data != null) {
                this.size = data.unscaledValue().toByteArray().length + 8; // size is stored as scale + unscaledValue
            } else {
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

    private static BigDecimalValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigDecimalValueFCD context;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof BigDecimalValueFCD)) {
            BigDecimal val = rs.getBigDecimal(columnName);
            if (val == null) {
                context = new BigDecimalValueFCD(null);
            } else {
                context = new BigDecimalValueFCD(val);
            }

            ctx.setContext(context);
            return context;
        } else {
            return (BigDecimalValueFCD) ctx.getContext();
        }
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return fetchState(columnName, rs, ctx).readSize();
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return fetchState(columnName, rs, ctx).data;
    }

    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigDecimal v = fetchState(columnName, rs, ctx).data;
        return v == null ? null : v.toPlainString();
    }

    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BigDecimal v = fetchState(columnName, rs, ctx).data;
        return v == null ? null : new StringReader(v.toPlainString());
    }
}

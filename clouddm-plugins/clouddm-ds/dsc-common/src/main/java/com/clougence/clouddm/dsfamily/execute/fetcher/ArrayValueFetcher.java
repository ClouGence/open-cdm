package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.output.DeferredFileOutputStream;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * m (mark)     : T = Type, V = dataValue
 * v (value)    : dataValue
 * t (truncated): F = false, T = true
 */
@Slf4j
public class ArrayValueFetcher extends StringAsClobFetcher {

    private final ValueFetcher valueFetcher;
    private final JsonFactory  jsonFactory = new JsonFactory();

    public ArrayValueFetcher(ValueFetcher valueFetcher){
        this.valueFetcher = valueFetcher;
    }

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            Array array = rs.getArray(columnName);
            if (array == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                try {
                    fcd = this.fetchStringData(array, ctx);
                } catch (Exception e) {
                    String dataString = "ReadException: " + e.getMessage();
                    byte[] dataBytes = dataString.getBytes();
                    fcd = StringValueFCD.ofInMemory(false, 0, 0, dataString, dataBytes);
                } finally {
                    array.free();
                }
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private static class ArrayElementValue {

        private final long    readSize;
        private final boolean truncated;

        public ArrayElementValue(long readSize, boolean truncated){
            this.readSize = readSize;
            this.truncated = truncated;
        }
    }

    private StringValueFCD fetchStringData(Array array, ValueFetcherContext ctx) throws SQLException, IOException {
        DeferredFileOutputStream dfout = new DeferredFileOutputStream(1048576, tmpFile(ctx));

        try (OutputStreamWriter out = new OutputStreamWriter(dfout)) {
            long dataReadSize = 0;
            boolean complete = true;

            JsonGenerator jsonGenerator = jsonFactory.createGenerator(out);
            jsonGenerator.writeStartArray();
            ValueFetcherContext subCtx = new ValueFetcherContext(ctx.getVfcId() + "_sub", ctx.getMeta(), ctx.getOptions());
            try (ResultSet ars = array.getResultSet()) {
                while (ars.next()) {
                    ArrayElementValue ele = readArrayToOutput(ars, dataReadSize, jsonGenerator, subCtx);

                    dataReadSize += ele.readSize;
                    complete = (complete && !ele.truncated);

                    if (dataReadSize >= ctx.getOptions().getColumnBytesLimit()) {
                        complete = false; // Stop reading if we reach the limit

                        // m (mark)     : T = Type, V = dataValue
                        // v (value)    : dataValue
                        // t (truncated): F = false, T = true
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeStringField("m", "T");
                        jsonGenerator.writeStringField("v", "...");
                        jsonGenerator.writeStringField("t", "T");
                        jsonGenerator.writeEndObject();
                        break;
                    }

                    subCtx.free();
                }
            } finally {
                subCtx.free();
                array.free();
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.flush();
            IOUtils.closeQuietly(dfout);

            if (dfout.isInMemory()) {
                byte[] bytes = dfout.getData();
                return StringValueFCD.ofInMemory(complete, -1, bytes.length, new String(bytes), bytes);
            } else {
                File dfoutFile = dfout.getFile();
                return StringValueFCD.ofInFile(complete, -1, dfoutFile.length(), dfoutFile);
            }
        }
    }

    private ArrayElementValue readArrayToOutput(ResultSet ars, long readSize, JsonGenerator out, ValueFetcherContext ctx) throws SQLException, IOException {
        long colBytesLimit = ctx.getOptions().getColumnBytesLimit();
        long eleBytesLimit = Math.min(Integer.MAX_VALUE, ctx.getOptions().getElementBytesLimit());
        long dataSize = this.valueFetcher.getSize("VALUE", ars, ctx);
        long expectedSize = readSize + dataSize;

        boolean truncated = expectedSize > eleBytesLimit || expectedSize > colBytesLimit;
        Reader reader = this.valueFetcher.getReader("VALUE", ars, ctx);
        // m (mark)     : T = Type, V = dataValue
        // v (value)    : dataValue
        // t (truncated): F = false, T = true
        out.writeStartObject();
        out.writeStringField("m", "V");
        out.writeObjectFieldStart("v");
        if (reader == null) {
            out.writeNull();
        } else {
            out.writeString(reader, (int) eleBytesLimit);
        }
        out.writeStringField("t", truncated ? "T" : "F");
        out.writeEndObject();
        return new ArrayElementValue(dataSize, truncated);
    }
}

package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.output.DeferredFileOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BytesAsStreamFetcher extends BytesAsBlobFetcher {

    @Override
    protected BytesValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BytesValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof BytesValueFCD)) {
            try (InputStream in = rs.getBinaryStream(columnName)) {
                if (in == null) {
                    fcd = BytesValueFCD.ofInMemory(true, 0, 0, null);
                } else {
                    fcd = fetchBytesData(in, ctx);
                }
                ctx.setContext(fcd);
            } catch (IOException e) {
                StringWriter sw = new StringWriter();
                sw.write("ReadException: " + e.getMessage() + "\n");
                e.printStackTrace(new PrintWriter(sw));
                String dataString = sw.toString();
                byte[] dataBytes = dataString.getBytes();
                fcd = BytesValueFCD.ofInMemory(false, -1, 0, dataBytes);
            }
        } else {
            fcd = (BytesValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private BytesValueFCD fetchBytesData(InputStream in, ValueFetcherContext ctx) throws IOException {
        long bytesLimit = ctx.getOptions().getColumnBytesLimit();

        // use DeferredFileOutputStream read, cache max 1M in memory
        DeferredFileOutputStream dfout = new DeferredFileOutputStream(1048576, tmpFile(ctx));
        long dataReadSize = 0;
        long charReadSize = 0;
        byte[] buffer = new byte[8192]; // 8K buffer size
        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            dfout.write(buffer, 0, bytesRead);
            dfout.flush();
            charReadSize += bytesRead;

            dataReadSize = dfout.isInMemory() ? dfout.getByteCount() : dfout.getFile().length();
            if (dataReadSize >= bytesLimit) {
                break; // Stop reading if we reach the limit
            }
        }

        IOUtils.closeQuietly(dfout);

        if (dfout.isInMemory()) {
            boolean complete = bytesRead == -1;
            byte[] bytes = dfout.getData();
            return BytesValueFCD.ofInMemory(complete, complete ? charReadSize : -1, charReadSize, bytes);
        } else {
            boolean complete = bytesRead == -1;
            File dfoutFile = dfout.getFile();
            return BytesValueFCD.ofInFile(complete, complete ? charReadSize : -1, charReadSize, dfoutFile);
        }
    }
}

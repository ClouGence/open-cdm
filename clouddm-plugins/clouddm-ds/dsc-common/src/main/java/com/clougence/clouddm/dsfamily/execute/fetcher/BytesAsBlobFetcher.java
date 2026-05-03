package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.*;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextBytesStreamInfo;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.output.DeferredFileOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BytesAsBlobFetcher extends AbstractValueFetcher {

    public BytesAsBlobFetcher(){
        super(ValueType.Bytes);
    }

    protected static final class BytesValueFCD implements ValueFetcherContextData, ValueFetcherContextBytesStreamInfo {

        private boolean complete;
        private long    bytesFullSize;
        private long    bytesReadSize;

        // for in-memory data
        private byte[]  tmpData;
        // for file data
        private File    tmpFile;

        @Override
        public void free() {
            this.complete = false;
            this.bytesFullSize = 0;
            this.bytesReadSize = 0;
            this.tmpData = null;
            if (this.tmpFile != null && this.tmpFile.exists()) {
                this.tmpFile.delete();
                this.tmpFile = null; // No file for in-memory data
            } else {
                this.tmpFile = null;
            }
        }

        public static BytesValueFCD ofInMemory(boolean complete, long bytesFullSize, long bytesReadSize, byte[] bytes) {
            BytesValueFCD sd = new BytesValueFCD();
            sd.complete = complete;
            sd.bytesFullSize = bytesFullSize;
            sd.bytesReadSize = bytesReadSize;
            sd.tmpData = bytes;
            sd.tmpFile = null; // No file for in-memory data
            return sd;
        }

        public static BytesValueFCD ofInFile(boolean complete, long bytesFullSize, long bytesReadSize, File tmpFile) {
            BytesValueFCD sd = new BytesValueFCD();
            sd.complete = complete;
            sd.bytesFullSize = bytesFullSize;
            sd.bytesReadSize = bytesReadSize;
            sd.tmpData = null; // No data for file data
            sd.tmpFile = tmpFile;
            return sd;
        }

        @Override
        public byte[] sampleData(int displayBytes) throws IOException {
            if (this.tmpData != null) {
                if (this.tmpData.length > displayBytes) {
                    byte[] sample = new byte[displayBytes];
                    System.arraycopy(this.tmpData, 0, sample, 0, displayBytes);
                    return sample;
                } else {
                    return this.tmpData;
                }
            } else if (this.tmpFile != null && this.tmpFile.exists()) {
                try (InputStream in = Files.newInputStream(this.tmpFile.toPath())) {
                    byte[] bytes = new byte[displayBytes];
                    int read = in.read(bytes);

                    byte[] sample = new byte[read];
                    System.arraycopy(bytes, 0, sample, 0, read);
                    return sample;
                }
            } else {
                return null;
            }
        }

        @Override
        public boolean isComplete() { return this.complete; }

        @Override
        public long fullSize() {
            return this.bytesFullSize;
        }

        @Override
        public long readSize() {
            return this.bytesReadSize;
        }

    }

    protected BytesValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BytesValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof BytesValueFCD)) {
            Blob blob = rs.getBlob(columnName);
            if (blob == null) {
                fcd = BytesValueFCD.ofInMemory(true, 0, 0, null);
            } else if (blob.length() == 0) {
                blob.free();
                fcd = BytesValueFCD.ofInMemory(true, 0, 0, ArrayUtils.EMPTY_BYTE_ARRAY);
            } else {
                try {
                    fcd = fetchBytesData(blob, ctx);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    sw.write("ReadException: " + e.getMessage() + "\n");
                    e.printStackTrace(new PrintWriter(sw));
                    String dataString = sw.toString();
                    byte[] dataBytes = dataString.getBytes();
                    fcd = BytesValueFCD.ofInMemory(false, blob.length(), 0, dataBytes);
                } finally {
                    blob.free();
                }
            }
            ctx.setContext(fcd);
        } else {
            fcd = (BytesValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private BytesValueFCD fetchBytesData(Blob blob, ValueFetcherContext ctx) throws SQLException, IOException {
        long bytesLimit = ctx.getOptions().getColumnBytesLimit();
        long bytesFullSize = blob.length();

        // use DeferredFileOutputStream read, cache max 1M in memory
        DeferredFileOutputStream dfout = new DeferredFileOutputStream(1048576, tmpFile(ctx));
        try (InputStream in = blob.getBinaryStream()) {
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
                boolean complete = charReadSize == bytesFullSize;
                byte[] bytes = dfout.getData();
                return BytesValueFCD.ofInMemory(complete, bytesFullSize, charReadSize, bytes);
            } else {
                boolean complete = charReadSize == bytesFullSize;
                File dfoutFile = dfout.getFile();
                return BytesValueFCD.ofInFile(complete, bytesFullSize, charReadSize, dfoutFile);
            }
        }
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BytesValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.tmpData != null) {
            return sd.tmpData.length;
        } else if (sd.tmpFile != null && sd.tmpFile.exists()) {
            return sd.tmpFile.length();
        } else {
            return 0;
        }
    }

    @Override
    public byte[] getBytes(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BytesValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.tmpData != null) {
            return sd.tmpData;
        } else if (sd.tmpFile != null && sd.tmpFile.exists()) {
            try (InputStream in = Files.newInputStream(sd.tmpFile.toPath())) {
                return IOUtils.toByteArray(in);
            } catch (IOException e) {
                throw new SQLException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }

    @Override
    public InputStream getInputStream(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        BytesValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.tmpData != null) {
            return new ByteArrayInputStream(sd.tmpData);
        } else if (sd.tmpFile != null && sd.tmpFile.exists()) {
            try {
                return Files.newInputStream(sd.tmpFile.toPath());
            } catch (IOException e) {
                throw new SQLException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
}

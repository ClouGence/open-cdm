package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.*;
import java.nio.file.Files;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextCharStreamInfo;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContextData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.output.DeferredFileOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringAsClobFetcher extends AbstractValueFetcher {

    public StringAsClobFetcher(){
        super(ValueType.String);
    }

    protected static final class StringValueFCD implements ValueFetcherContextData, ValueFetcherContextCharStreamInfo {

        private boolean complete;
        private long    charFullSize;
        private long    charReadSize;

        // for in-memory data
        private String  data;
        private byte[]  dataBytes;
        // for file data
        private File    tmpFile;

        @Override
        public void free() {
            this.complete = false;
            this.charFullSize = 0;
            this.charReadSize = 0;
            this.data = null;
            this.dataBytes = null;
            if (this.tmpFile != null && this.tmpFile.exists()) {
                this.tmpFile.delete();
                this.tmpFile = null; // No file for in-memory data
            } else {
                this.tmpFile = null;
            }
        }

        public static StringValueFCD ofInMemory(boolean complete, long charFullSize, long charReadSize, String data, byte[] dataBytes) {
            StringValueFCD sd = new StringValueFCD();
            sd.complete = complete;
            sd.charFullSize = charFullSize;
            sd.charReadSize = charReadSize;
            sd.data = data;
            sd.dataBytes = dataBytes;
            sd.tmpFile = null; // No file for in-memory data
            return sd;
        }

        public static StringValueFCD ofInFile(boolean complete, long charFullSize, long charReadSize, File tmpFile) {
            StringValueFCD sd = new StringValueFCD();
            sd.complete = complete;
            sd.charFullSize = charFullSize;
            sd.charReadSize = charReadSize;
            sd.data = null; // No data for file data
            sd.dataBytes = null; // No bytes for file data
            sd.tmpFile = tmpFile;
            return sd;
        }

        @Override
        public String sampleData(int maxChars) throws IOException {
            if (this.data != null) {
                if (this.data.length() > maxChars) {
                    return this.data.substring(0, maxChars);
                } else {
                    return this.data;
                }
            } else if (this.tmpFile != null && this.tmpFile.exists()) {
                try (Reader in = new FileReader(this.tmpFile)) {
                    char[] chars = new char[maxChars];
                    int read = in.read(chars);
                    return new String(chars, 0, read);
                }
            } else {
                return null;
            }
        }

        @Override
        public boolean isComplete() { return this.complete; }

        @Override
        public long fullSize() {
            return this.charFullSize;
        }

        @Override
        public long readSize() {
            return this.charReadSize;
        }

    }

    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            Clob clob = rs.getClob(columnName);
            if (clob == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else if (clob.length() == 0) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, "", ArrayUtils.EMPTY_BYTE_ARRAY);
                clob.free();
            } else {
                try {
                    fcd = fetchStringData(clob, ctx);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    sw.write("ReadException: " + e.getMessage() + "\n");
                    e.printStackTrace(new PrintWriter(sw));
                    String dataString = sw.toString();
                    byte[] dataBytes = dataString.getBytes();
                    fcd = StringValueFCD.ofInMemory(false, clob.length(), 0, dataString, dataBytes);
                } finally {
                    clob.free();
                }
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private StringValueFCD fetchStringData(Clob clob, ValueFetcherContext ctx) throws SQLException, IOException {
        long bytesLimit = ctx.getOptions().getColumnBytesLimit();
        long charFullSize = clob.length();

        if (charFullSize < (256 * 1024)) {
            // read full to memory (bad case 256K * 4 Bytes/char = 1MB)
            StringWriter sw = new StringWriter();
            try (Reader reader = clob.getCharacterStream()) {
                IOUtils.copyLarge(reader, sw);

                String dataString = sw.toString();
                byte[] dataBytes = dataString.getBytes();
                return StringValueFCD.ofInMemory(true, dataString.length(), dataString.length(), dataString, dataBytes);
            }
        } else {
            // use DeferredFileOutputStream read, cache max 1M in memory
            DeferredFileOutputStream dfout = new DeferredFileOutputStream(1048576, tmpFile(ctx));
            try (Reader in = clob.getCharacterStream(); OutputStreamWriter out = new OutputStreamWriter(dfout)) {
                long dataReadSize = 0;
                long charReadSize = 0;
                char[] buffer = new char[8192]; // 8K buffer size
                int charRead;

                while ((charRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, charRead);
                    out.flush();
                    charReadSize += charRead;

                    dataReadSize = dfout.isInMemory() ? dfout.getByteCount() : dfout.getFile().length();
                    if (dataReadSize >= bytesLimit) {
                        break; // Stop reading if we reach the limit
                    }
                }

                IOUtils.closeQuietly(dfout);

                if (dfout.isInMemory()) {
                    boolean complete = charReadSize == charFullSize;
                    byte[] bytes = dfout.getData();
                    return StringValueFCD.ofInMemory(complete, charFullSize, charReadSize, new String(bytes), bytes);
                } else {
                    boolean complete = charReadSize == charFullSize;
                    File dfoutFile = dfout.getFile();
                    return StringValueFCD.ofInFile(complete, charFullSize, charReadSize, dfoutFile);
                }
            }
        }
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.data != null) {
            return sd.dataBytes.length;
        } else if (sd.tmpFile != null) {
            return sd.tmpFile.length();
        } else {
            return 0;
        }
    }

    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.data != null) {
            return sd.data;
        } else if (sd.tmpFile != null) {
            try {
                return IOUtils.readToString(new FileReader(sd.tmpFile));
            } catch (IOException e) {
                throw new SQLException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }

    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.data != null) {
            return new StringReader(sd.data);
        } else if (sd.tmpFile != null) {
            try {
                return new FileReader(sd.tmpFile);
            } catch (IOException e) {
                throw new SQLException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }

    @Override
    public byte[] getBytes(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.dataBytes != null) {
            return sd.dataBytes;
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
        StringValueFCD sd = fetchState(columnName, rs, ctx);
        if (sd.dataBytes != null) {
            return new ByteArrayInputStream(sd.dataBytes);
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

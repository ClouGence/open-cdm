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

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.output.DeferredFileOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringAsReaderFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            try (Reader clob = rs.getCharacterStream(columnName)) {
                if (clob == null) {
                    fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
                } else {
                    fcd = fetchStringData(clob, ctx);
                }
                ctx.setContext(fcd);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                sw.write("ReadException: " + e.getMessage() + "\n");
                e.printStackTrace(new PrintWriter(sw));
                String dataString = sw.toString();
                byte[] dataBytes = dataString.getBytes();
                fcd = StringValueFCD.ofInMemory(false, -1, 0, dataString, dataBytes);
            }
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private StringValueFCD fetchStringData(Reader in, ValueFetcherContext ctx) throws IOException {
        long bytesLimit = ctx.getOptions().getColumnBytesLimit();

        // use DeferredFileOutputStream read, cache max 1M in memory
        DeferredFileOutputStream dfout = new DeferredFileOutputStream(1048576, tmpFile(ctx));
        try (OutputStreamWriter out = new OutputStreamWriter(dfout)) {
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
                boolean complete = charRead == -1;
                byte[] bytes = dfout.getData();
                return StringValueFCD.ofInMemory(complete, complete ? charReadSize : -1, charReadSize, new String(bytes), bytes);
            } else {
                boolean complete = charRead == -1;
                File dfoutFile = dfout.getFile();
                return StringValueFCD.ofInFile(complete, complete ? charReadSize : -1, charReadSize, dfoutFile);
            }
        }
    }
}

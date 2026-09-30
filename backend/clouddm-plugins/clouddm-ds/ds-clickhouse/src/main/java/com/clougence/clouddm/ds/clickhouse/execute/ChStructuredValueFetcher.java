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

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.output.DeferredFileOutputStream;

/**
 * ClickHouse tuples, nested rows and geometry values are materialized by JDBC as arrays.
 * Format them as bounded text without exposing Java array identities.
 * <p>
 * Follows {@code ArrayValueFetcher}: output goes through a DeferredFileOutputStream (spills to
 * file above 1MB), every element is bounded by {@code elementBytesLimit} and the whole cell by
 * {@code columnBytesLimit} from {@link ValueFetcherContext} options; when a limit is hit the
 * value is truncated and the complete flag is cleared.
 */
public class ChStructuredValueFetcher extends StringAsClobFetcher {

    // m (mark): T = Type, V = dataValue / t (truncated): F = false, T = true
    private static final String STOP_MARKER = ", {\"m\":\"T\",\"v\":\"...\",\"t\":\"T\"}";

    private final boolean       tuple;

    public ChStructuredValueFetcher(boolean tuple){
        this.tuple = tuple;
    }

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            Object value = rs.getObject(columnName);
            if (value == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                java.sql.Array array = null;
                try {
                    if (value instanceof java.sql.Array) {
                        array = (java.sql.Array) value;
                        value = array.getArray();
                    }
                    fcd = fetchStructuredData(value, ctx);
                } catch (IOException e) {
                    throw new SQLException("Failed to read ClickHouse structured value", e);
                } finally {
                    if (array != null) {
                        array.free();
                    }
                }
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private StringValueFCD fetchStructuredData(Object value, ValueFetcherContext ctx) throws IOException {
        long columnBytesLimit = ctx.getOptions().getColumnBytesLimit();
        long elementBytesLimit = ctx.getOptions().getElementBytesLimit();

        DeferredFileOutputStream dfout = new DeferredFileOutputStream(1048576, tmpFile(ctx));
        try (OutputStreamWriter out = new OutputStreamWriter(dfout)) {
            long dataReadSize = 0;
            boolean complete = true;

            String opening = "[";
            String closing = "]";
            if (this.tuple) {
                opening = "(";
                closing = ")";
            }
            out.write(opening);
            boolean[] truncatedFlag = { false };
            if (value.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(value); i++) {
                    String eleText = boundedText(Array.get(value, i), elementBytesLimit, truncatedFlag);
                    boolean eleTruncated = truncatedFlag[0];
                    truncatedFlag[0] = false;

                    // check before writing so the cell never exceeds the limit; the marker
                    // signals that the current and following elements were skipped
                    if (i > 0 && dataReadSize + eleText.length() >= columnBytesLimit) {
                        complete = false;
                        out.write(STOP_MARKER);
                        break;
                    }
                    if (i > 0) {
                        out.write(", ");
                    }
                    out.write(eleText);
                    dataReadSize += eleText.length();
                    complete = complete && !eleTruncated;
                }
            } else {
                String eleText = boundedText(value, elementBytesLimit, truncatedFlag);
                out.write(eleText);
                dataReadSize += eleText.length();
                complete = !truncatedFlag[0];
            }
            out.write(closing);
            out.flush();
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

    private static String boundedText(Object value, long budget, boolean[] truncated) {
        if (budget <= 0) {
            truncated[0] = true;
            return "";
        }
        if (value == null) {
            return "null";
        }
        if (value.getClass().isArray()) {
            return boundedNestedArray(value, budget, truncated);
        }
        if (value instanceof List) {
            return boundedNestedArray((List<?>) value, budget, truncated);
        }
        String text = value.toString();
        if (text.length() > budget) {
            truncated[0] = true;
            return text.substring(0, (int) budget);
        }
        return text;
    }

    private static String boundedNestedArray(Object elements, long budget, boolean[] truncated) {
        String opening = "(";
        String closing = ")";
        if (elements.getClass().getComponentType().isArray()) {
            opening = "[";
            closing = "]";
        }
        StringBuilder sb = new StringBuilder(opening);
        long used = 1;
        for (int i = 0; i < Array.getLength(elements); i++) {
            if (i > 0) {
                sb.append(", ");
                used += 2;
            }
            sb.append(boundedText(Array.get(elements, i), budget - used, truncated));
            used = sb.length();
            if (truncated[0] || used >= budget) {
                truncated[0] = true;
                break;
            }
        }
        return sb.append(closing).toString();
    }

    private static String boundedNestedArray(List<?> elements, long budget, boolean[] truncated) {
        StringBuilder sb = new StringBuilder("[");
        long used = 1;
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) {
                sb.append(", ");
                used += 2;
            }
            sb.append(boundedText(elements.get(i), budget - used, truncated));
            used = sb.length();
            if (truncated[0] || used >= budget) {
                truncated[0] = true;
                break;
            }
        }
        return sb.append("]").toString();
    }
}

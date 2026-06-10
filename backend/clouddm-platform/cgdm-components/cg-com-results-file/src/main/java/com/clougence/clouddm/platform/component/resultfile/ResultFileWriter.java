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
package com.clougence.clouddm.platform.component.resultfile;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.utils.JsonUtils;

public class ResultFileWriter implements Closeable {

    private final ResultSetOutputStream output;
    private final int                   columnCount;

    public static ResultFileWriter open(File file, QueryRequest query, ColMetaData[] columns) throws IOException {
        return open(file, query, columns, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    public static ResultFileWriter open(File file, QueryRequest query, ColMetaData[] columns, long fileLimit, long columnLimit) throws IOException {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        if (query == null) {
            throw new NullPointerException("query is null");
        }
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("columns must not be empty.");
        }

        ResultSetOutputStream output = new FileResultSetOutputStream(file.getAbsoluteFile(), false, ByteOrder.BIG_ENDIAN, fileLimit, columnLimit);
        return new ResultFileWriter(output, query, columns);
    }

    public ResultFileWriter(ResultSetOutputStream output, QueryRequest query, ColMetaData[] columns) throws IOException{
        this.output = output;
        this.columnCount = columns.length;
        this.writeMetadata(query, columns);
    }

    private void writeMetadata(QueryRequest query, ColMetaData[] columns) throws IOException {
        this.output.newRow();
        this.output.writeString((byte) 0, JsonUtils.toJson(query), StandardCharsets.UTF_8);
        this.output.closeRow();

        this.output.newRow();
        for (ColMetaData column : columns) {
            this.output.writeString((byte) 0, JsonUtils.toJson(column), StandardCharsets.UTF_8);
        }
        this.output.closeRow();
    }

    public void writeRow(List<String> values) throws IOException {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        if (values.size() != this.columnCount) {
            throw new IllegalArgumentException("values size " + values.size() + " does not match column count " + this.columnCount + ".");
        }

        this.output.newRow();
        for (String value : values) {
            this.output.writeString((byte) 0, value, StandardCharsets.UTF_8);
        }
        this.output.closeRow();
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
}

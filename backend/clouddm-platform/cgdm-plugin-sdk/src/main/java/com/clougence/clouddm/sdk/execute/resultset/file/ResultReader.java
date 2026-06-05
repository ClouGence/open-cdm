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
package com.clougence.clouddm.sdk.execute.resultset.file;

import java.io.Closeable;
import java.io.IOException;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;

public interface ResultReader extends Closeable {

    long getFileSize();

    long getRowCount();

    boolean isFileEof();

    //

    QueryRequest getQueryInfo();

    ColMetaData[] getMetadataInfo();

    //

    byte getRowTag();

    long getRowSize();

    long getDataCount();

    boolean nextRow() throws IOException;

    long nextRow(long skipNumber) throws IOException;

    boolean hasNextRow();

    boolean isLastRow();

    //

    byte nextDataTag() throws IOException;

    byte nextDataType() throws IOException;

    long nextDataSize() throws IOException;

    boolean nextData() throws IOException;

    long nextData(long skipData) throws IOException;

    boolean hasNextData() throws IOException;

    boolean isLastData() throws IOException;

    // for read objects

    ResultSetValue readAsString(ColMetaData dataMeta, QueryResultConf resultConf) throws IOException;

    ResultSetValue readAsString(ColMetaData meta, QueryResultConf conf, long offset, long length) throws IOException;
}

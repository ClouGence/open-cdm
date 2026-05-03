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

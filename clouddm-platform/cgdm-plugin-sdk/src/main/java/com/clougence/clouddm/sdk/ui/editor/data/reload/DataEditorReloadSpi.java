package com.clougence.clouddm.sdk.ui.editor.data.reload;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.schema.umi.special.rdb.RdbTable;

public interface DataEditorReloadSpi extends Spi {

    // before do dml [IUD]
    void beforeExecute(RdbTable tableMeta, DataEditorSqlType dmlType, QueryRequest request);

    // after do dml [IUD]
    Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData);
}

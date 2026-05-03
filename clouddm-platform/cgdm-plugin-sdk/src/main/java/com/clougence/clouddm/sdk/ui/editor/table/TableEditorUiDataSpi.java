package com.clougence.clouddm.sdk.ui.editor.table;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.schema.editor.domain.ETable;

public interface TableEditorUiDataSpi extends Spi {

    void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion);

    void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion);
}

package com.clougence.clouddm.console.web.model.fo.editor.table;

import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public enum EditorViewModeEnum {

    CREATE(EditorViewMode.Create),
    ALTER(EditorViewMode.Alter);

    private final EditorViewMode viewMode;

    EditorViewModeEnum(EditorViewMode viewMode){
        this.viewMode = viewMode;
    }

    public EditorViewMode getViewMode() { return this.viewMode; }
}

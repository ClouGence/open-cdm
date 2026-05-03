package com.clougence.clouddm.sdk.ui.editor.table;

public enum TableEditorVarKeys {

    CURRENT_DB_NAME,
    CURRENT_SCHEMA_NAME,
    I18N_LOCAL_DEFAULT,
    I18N_LOCAL_USER;

    public String codeKey() {
        return this.name();
    }
}

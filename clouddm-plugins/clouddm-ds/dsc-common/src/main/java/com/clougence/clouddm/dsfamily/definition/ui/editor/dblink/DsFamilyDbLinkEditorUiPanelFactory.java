package com.clougence.clouddm.dsfamily.definition.ui.editor.dblink;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.dblink.DbLinkEditorFields;

import java.sql.Connection;

public abstract class DsFamilyDbLinkEditorUiPanelFactory implements DbLinkEditorFields {

    public UiPanel create(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanel uiPanel = new UiPanel();

        fetchBaseInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    protected void fetchBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

}

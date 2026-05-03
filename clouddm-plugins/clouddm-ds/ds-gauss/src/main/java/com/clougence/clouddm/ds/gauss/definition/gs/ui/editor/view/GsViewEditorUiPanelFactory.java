package com.clougence.clouddm.ds.gauss.definition.gs.ui.editor.view;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.view.PgViewEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class GsViewEditorUiPanelFactory extends PgViewEditorUiPanelFactory {

    @Override
    protected void fillOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

}

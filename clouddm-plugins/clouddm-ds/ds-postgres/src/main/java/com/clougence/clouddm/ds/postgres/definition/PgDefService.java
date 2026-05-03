package com.clougence.clouddm.ds.postgres.definition;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.function.PgFunctionEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.procedure.PgProcedureOptionUiPanelFactory;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.trigger.PgTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.view.PgViewEditorUiPanelFactory;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

public class PgDefService extends AbstractDefService implements Spi, TableEditorUiDefService, FunctionUiDefService, ProcedureUiDefService, ViewUiDefService, TriggerUiDefService {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PgTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PgViewEditorUiPanelFactory().createViewUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PgProcedureOptionUiPanelFactory().createProcedureUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PgFunctionEditorUiPanelFactory().createFunctionUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PgTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, con);
    }
}

package com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.table.ObTableEditorUiPanelFactory;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.view.ObViewUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function.MyFunctionEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.procedure.MyProcedureUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger.MyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * @author Ekko
 * @date 2023/9/14 15:57
 */
public class ObDefService extends AbstractDefService implements TableEditorUiDefService, ViewUiDefService, TriggerUiDefService, FunctionUiDefService, ProcedureUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new ObTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new ObViewUiPanelFactory().createViewUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyProcedureUiPanelFactory().createProcedureUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyFunctionEditorUiPanelFactory().createFunctionUiPanel(dsConfig, viewMode, dsConnection);
    }
}

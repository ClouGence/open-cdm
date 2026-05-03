package com.clougence.clouddm.ds.polardb.definition.pormy;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.polardb.definition.pormy.editor.table.PorMyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function.MyFunctionEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function.MyFunctionPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.procedure.MyProcedurePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.procedure.MyProcedureUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTablePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger.MyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger.MyTriggerPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view.MyViewPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view.MyViewUiPanelFactory;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class PorMyDefService extends AbstractDefService implements Spi, TableEditorUiDefService, FunctionUiDefService, ProcedureUiDefService, ViewUiDefService, TriggerUiDefService {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PorMyTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyTablePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyViewPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyFunctionPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyTriggerPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchProcedurePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyProcedurePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyFunctionEditorUiPanelFactory().createFunctionUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyProcedureUiPanelFactory().createProcedureUiPanel(dsConfig, viewMode, con);
    }

    @Override
    public UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyViewUiPanelFactory().createViewUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, dsConnection);
    }
}

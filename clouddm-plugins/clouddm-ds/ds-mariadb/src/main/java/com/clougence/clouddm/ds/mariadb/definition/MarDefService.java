package com.clougence.clouddm.ds.mariadb.definition;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.mariadb.definition.ui.editor.table.MarTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTablePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger.MyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view.MyViewUiPanelFactory;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
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
public class MarDefService extends AbstractDefService implements TableEditorUiDefService, ViewUiDefService, TriggerUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MarTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    public UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyViewUiPanelFactory().createViewUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyTablePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }
}

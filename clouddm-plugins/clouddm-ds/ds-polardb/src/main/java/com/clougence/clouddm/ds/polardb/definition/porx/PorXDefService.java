package com.clougence.clouddm.ds.polardb.definition.porx;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.polardb.definition.porx.editor.table.PorXTableEditorUiPanelFactory;
import com.clougence.clouddm.ds.polardb.definition.porx.editor.view.PorXViewUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTablePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view.MyViewPropertyUiPanelFactory;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class PorXDefService extends AbstractDefService implements TableEditorUiDefService, ViewUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PorXTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new PorXViewUiPanelFactory().createViewUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyTablePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new MyViewPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }
}

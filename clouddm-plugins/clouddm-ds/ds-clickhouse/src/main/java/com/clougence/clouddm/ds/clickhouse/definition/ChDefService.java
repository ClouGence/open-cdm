package com.clougence.clouddm.ds.clickhouse.definition;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table.ChTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class ChDefService extends AbstractDefService implements TableEditorUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new ChTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }
}

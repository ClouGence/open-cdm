package com.clougence.clouddm.ds.starrocks.definition;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.starrocks.definition.ui.editor.table.SrTableEditorUiPanelFactory;
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
public class SrDefService extends AbstractDefService implements TableEditorUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        TableEditorUiPanel uiPanel = new SrTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
        return uiPanel;
    }
}

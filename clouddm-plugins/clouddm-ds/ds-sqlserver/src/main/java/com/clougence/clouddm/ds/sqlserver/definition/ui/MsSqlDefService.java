package com.clougence.clouddm.ds.sqlserver.definition.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table.MsSqlTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * @author Ekko
 * @date 2023/9/7 15:51
 */
public class MsSqlDefService extends AbstractDefService implements TableEditorUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MsSqlTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }
}

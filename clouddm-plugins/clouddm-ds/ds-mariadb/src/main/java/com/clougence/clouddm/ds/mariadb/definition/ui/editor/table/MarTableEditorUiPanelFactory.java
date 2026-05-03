package com.clougence.clouddm.ds.mariadb.definition.ui.editor.table;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * create table
 * https://docs.pingcap.com/zh/tidb/stable/sql-statement-create-table
 * */
public class MarTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        uiPanel.getTableInfo().removeField(FIELD_TABLE_COMPRESSION);
    }
}

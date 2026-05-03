package com.clougence.clouddm.ds.polardb.definition.porx.editor.table;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * create table
 * https://help.aliyun.com/document_detail/264119.html?spm=a2c4g.313269.0.0.214c227cMvhGyh
 * */
public class PorXTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory implements PorXTableEditorFields {

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        uiPanel.getKeys().removeField(FIELD_PRIMARY_INDEX_TYPE);
    }
}

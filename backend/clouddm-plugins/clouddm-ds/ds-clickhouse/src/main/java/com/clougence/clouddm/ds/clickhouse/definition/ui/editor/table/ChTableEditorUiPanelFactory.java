/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.boolValueDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;

import com.clougence.adapter.clickhouse.ClickHouseMainVersion;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * create table
 * https://clickhouse.com/docs/en/sql-reference/statements/create/table
 * */
public class ChTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements ChTableEditorFields {

    @Override
    public TableEditorUiPanel createTableEditorUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        TableEditorUiPanel uiPanel = new TableEditorUiPanel();
        uiPanel.setViewMode(viewMode);

        fillTableInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTableColumnsUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTableKeysUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    @Override
    protected void fillTableInfoUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_CATALOG)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLECATALOG_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLECATALOG_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_SCHEMA)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLESCHEMA_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLESCHEMA_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_NAME)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("table_name"))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLENAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLENAME_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_COMMENT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_COMMENT_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .hide(ClickHouseMainVersion.CK_21_1.isLt(ClickHouseMainVersion.parserVersion(dsConfig.getVersion())))
                .build());
    }

    @Override
    protected void fillTableColumnsUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_SOURCE)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_SOURCE_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_SOURCE_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_NAME)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("column_name"))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COLUMNNAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COLUMNNAME_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COMMENT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COMMENT_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_DATATYPE)
                .type(UiPanelFieldType.Options)
                .require(true)
                .defaultValue(strValueDef("Int8"))
                .options(fetchTypes(uiPanel, dsConfig, con))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DATATYPE_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DATATYPE_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_NOTNULL)
                .type(UiPanelFieldType.Check)
                .defaultValue(boolValueDef(false))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_NOTNULL_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_NOTNULL_DESC)
                .build());
    }

    @Override
    protected void fillTableKeysUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getKeys()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("Primary"))
                .readOnly(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_KEYS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_KEYS_NAME_DESC)
                .build());

        UiPanelField primaryColumns = UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .readOnly(true)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_KEYS_COLUMNS_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_KEYS_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
        this.fillTableKeysColumnsUiPanel(primaryColumns, dsConfig, viewMode, con);
        uiPanel.getKeys().addField(primaryColumns);
    }
}

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
package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.doris.i18n.DrDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * @Author mode
 * @Date 2023-09-06 11:48
 */
public class DrTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements DrTableEditorFields {

    public TableEditorUiPanel createTableEditorUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        TableEditorUiPanel uiPanel = new TableEditorUiPanel();
        uiPanel.setViewMode(viewMode);

        this.fillTableInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        this.fillTableColumnsUiPanel(uiPanel, dsConfig, viewMode, con);
        this.fillTableKeysUiPanel(uiPanel, dsConfig, viewMode, con);
        this.fillTableIndexesUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTED_BY)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef("RANDOM"))
                .options(fetchDistributedBy(dsConfig, uiPanel))
                .titleI18N(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_TITLE)
                .descI18N(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_BUCKET_NUMBER)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("16"))
                .titleI18N(DrDsI18nKeys.EDITOR_TABLEINFO_BUCKET_NUMBER_TITLE)
                .descI18N(DrDsI18nKeys.EDITOR_TABLEINFO_BUCKET_NUMBER_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COLUMN_KEY_TYPE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef("UNIQUE KEY"))
                .options(fetchColumnKeyType())
                .titleI18N(DrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEY_TYPE_TITLE)
                .descI18N(DrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEY_TYPE_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
    }

    protected List<ValueDef> fetchColumnKeyType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_UNIQUE_LABEL, "UNIQUE KEY"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_AGGREGATE_LABEL, "AGGREGATE KEY"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_DUPLICATE_LABEL, "DUPLICATE KEY"));
        return result;
    }

    protected List<ValueDef> fetchDistributedBy(DataSourceConfig dsConfig, TableEditorUiPanel uiPanel) {
        UiPanelField primaryColumns = UiPanelField.builder()
            .field(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_TITLE)
            .descI18N(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_DESC)
            .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_NAME_TITLE)
                .descI18N(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_NAME_TITLE)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_RANDOM, "RANDOM"));
        result.add(fieldOptionDef(DrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_HASH, "HASH").addField(primaryColumns));
        return result;
    }

    // tableEditor Columns panel
    @Override
    protected void fillTableColumnsUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_AGG_TYPE)
                .type(UiPanelFieldType.Options)
                .options(fetchColumnAggType())
                .titleI18N(DrDsI18nKeys.EDITOR_COLUMNS_AGG_TYPE_TITLE)
                .descI18N(DrDsI18nKeys.EDITOR_COLUMNS_AGG_TYPE_DESC)
                .hide(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
    }

    protected List<ValueDef> fetchColumnAggType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_SUM_LABEL, "SUM"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_MAX_LABEL, "MAX"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_MIN_LABEL, "MIN"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_REPLACE_LABEL, "REPLACE"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_HLL_UNION_LABEL, "HLL_UNION"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_BITMAP_UNION_LABEL, "BITMAP_UNION"));
        result.add(optionDef(DrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_REPLACE_IF_NOT_NULL_LABEL, "REPLACE_IF_NOT_NULL"));
        return result;
    }

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getKeys()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("Columns"))
                .readOnly(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_KEYS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_KEYS_NAME_DESC)
                .build());

        UiPanelField primaryColumns = UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_KEYS_COLUMNS_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_KEYS_COLUMNS_DESC)
            .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .build());
        this.fillTableKeysColumnsUiPanel(primaryColumns, dsConfig, viewMode, con);
        uiPanel.getKeys().addField(primaryColumns);
    }

    // tableEditor Indexes panel
    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes().removeField(FIELD_INDEXES_TYPE);
    }
}

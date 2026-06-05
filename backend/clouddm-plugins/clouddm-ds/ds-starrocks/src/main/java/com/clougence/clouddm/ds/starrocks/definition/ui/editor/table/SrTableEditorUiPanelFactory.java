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
package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.starrocks.StarRocksMainVersion;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.starrocks.i18n.SrDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;

/**
 * @Author mode
 * @Date 2023-09-06 11:48
 */
public class SrTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements SrTableEditorFields {

    @Override
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
    protected void fillTableInfoUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_CATALOG)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_TABLECATALOG_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_TABLECATALOG_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_SCHEMA)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_TABLESCHEMA_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_TABLESCHEMA_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_NAME)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("table_name"))
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_TABLENAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_TABLENAME_DESC)
                .build());

        MainVersion mainVersion = SchemaFramework.getMainVersion(DsType.StarRocks, dsConfig.getVersion());
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_COMMENT_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_COMMENT_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter && StarRocksMainVersion.StarRocks_3_0.isLt(mainVersion))
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_ENGINE)
                .type(UiPanelFieldType.Options)
                .defaultValue(strValueDef("OLAP"))
                .options(fetchTableEngine(dsConfig))
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTED_BY)
                .type(UiPanelFieldType.Options)
                .defaultValue(strValueDef("HASH"))
                .options(fetchDistributedBy(dsConfig, uiPanel))
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_BUCKET_NUMBER)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("16"))
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_BUCKET_NUMBER_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_BUCKET_NUMBER_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
    }

    // tableEditor Keys panel
    protected void fillTableKeysUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTableKeysUiPanelForBasic(uiPanel, dsConfig, viewMode, con);
        this.fillTableKeysUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTableKeysUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getKeys()
            .addField(UiPanelField.builder()
                .field(FIELD_MODEL_KEY_NAME)
                .type(UiPanelFieldType.Options)
                .defaultValue(strValueDef("PRIMARY KEY"))
                .options(fetchTableKeyType(uiPanel, dsConfig))
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .titleI18N(SrDsI18nKeys.EDITOR_KEYS_NAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_KEYS_NAME_DESC)
                .build());

        UiPanelField primaryColumns = UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEY_TYPE_TITLE)
            .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEY_TYPE_DESC)
            .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEY_TYPE_TITLE_COLUMN)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEY_TYPE_DESC)
                .build());
        this.fillTableKeysColumnsUiPanel(primaryColumns, dsConfig, viewMode, con);
        uiPanel.getKeys().addField(primaryColumns);
    }

    @Override
    protected void fillTableColumnsUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_AGG_TYPE)
                .type(UiPanelFieldType.Options)
                .options(fetchColumnAggType())
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_AGG_TYPE_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_AGG_TYPE_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
    }

    protected void fillTableColumnsUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_SOURCE)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_SOURCE_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_SOURCE_DESC)
                .build());

        StarRocksMainVersion starRocksMainVersion = StarRocksMainVersion.parserVersion(dsConfig.getVersion());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_NAME)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("column_name"))
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_COLUMNNAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_COLUMNNAME_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter && StarRocksMainVersion.StarRocks_3_3.isGt(starRocksMainVersion))
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_COMMENT_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_COMMENT_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_DATATYPE)
                .type(UiPanelFieldType.Options)
                .require(true)
                .defaultValue(strValueDef("INT"))
                .options(fetchTypes(uiPanel, dsConfig, con))
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_DATATYPE_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_DATATYPE_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_NOTNULL)
                .type(UiPanelFieldType.Check)
                .defaultValue(boolValueDef(false))
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_NOTNULL_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_NOTNULL_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_AUTOINCREMENT)
                .type(UiPanelFieldType.Check)
                .defaultValue(boolValueDef(false))
                .titleI18N(SrDsI18nKeys.EDITOR_COLUMNS_AUTOINCREMENT_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COLUMNS_AUTOINCREMENT_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());

        List<ValueDef> defaultOptions = fetchDefault();
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_DEFAULT_OPTION)
                .type(UiPanelFieldType.Radios)
                .defaultValue(CollectionUtils.isEmpty(defaultOptions) ? null : strValueDef((String) defaultOptions.get(0).asValue()))
                .options(defaultOptions)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());
    }

    // tableEditor Indexes panel
    protected void fillTableIndexesUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);
        this.fillTableIndexesUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("index_name"))
                .require(true)
                .titleI18N(SrDsI18nKeys.EDITOR_INDEXES_NAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_INDEXES_NAME_DESC)
                .build());

        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(SrDsI18nKeys.EDITOR_INDEXES_COMMENT_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_INDEXES_COMMENT_DESC)
                .build());

        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEX_TYPE)
                .type(UiPanelFieldType.Options)
                .options(fetchIndexIndexTypes(uiPanel.getViewMode()))
                .defaultValue(strValueDef("BitMap"))
                .titleI18N(SrDsI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_INDEXES_TYPE_DESC)
                .readOnly(true)
                .build());

        UiPanelField indexColumns = UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(SrDsI18nKeys.EDITOR_INDEXES_COLUMNS_TITLE)
            .descI18N(SrDsI18nKeys.EDITOR_INDEXES_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(SrDsI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .build());
        this.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);
        uiPanel.getIndexes().addField(indexColumns);
    }

    protected List<ValueDef> fetchDefault() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_NULL_LABEL, "NULL"));
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_EMPTY_LABEL, ""));
        result.add(fieldOptionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_LABEL, FIELD_COLUMN_DEFAULT_CUSTOM).addField(UiPanelField.builder()
            .field(FIELD_COLUMN_DEFAULT_CUSTOM)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_DESC)
            .readOnly(true)
            .build()));

        return result;
    }

    protected List<ValueDef> fetchTableEngine(DataSourceConfig dsConfig) {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_OLAP_LABEL, "OLAP"));
        //        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_MYSQL_LABEL, "mysql"));
        //        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_ELASTICSEARCH_LABEL, "elasticsearch"));
        //        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_HIVE_LABEL, "hive"));
        //        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_HUDI_LABEL, "hudi"));
        //        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_JDBC_LABEL, "jdbc"));
        //        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_ENGINE_ICEBERG_LABEL, "iceberg"));
        return result;
    }

    protected List<ValueDef> fetchColumnAggType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_SUM_LABEL, "SUM"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_MAX_LABEL, "MAX"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_MIN_LABEL, "MIN"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_REPLACE_LABEL, "REPLACE"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_HLL_UNION_LABEL, "HLL_UNION"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_BITMAP_UNION_LABEL, "BITMAP_UNION"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_COLUMN_AGGTYPE_REPLACE_IF_NOT_NULL_LABEL, "REPLACE_IF_NOT_NULL"));
        return result;
    }

    protected List<ValueDef> fetchTableKeyType(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig) {
        MainVersion mainVersion = SchemaFramework.getMainVersion(DsType.StarRocks, dsConfig.getVersion());
        List<ValueDef> result = new ArrayList<>();

        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_DUPLICATE_LABEL, "DUPLICATE KEY"));
        result.add(fieldOptionDef(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_PRIMARY_LABEL, "PRIMARY KEY").addField(UiPanelField.builder()
            .field(FIELD_TABLE_ORDER_BY)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_ORDERBY_TITLE)
            .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_ORDERBY_DESC)
            .defaultValue(UiUtils.ListValueDef(new ArrayList<>()))
            .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter && StarRocksMainVersion.StarRocks_3_0.isLt(mainVersion))
            .hide(!StarRocksMainVersion.StarRocks_3_0.isLt(mainVersion))
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_ORDER_BY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_ORDERBY_NAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_ORDERBY_NAME_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter && StarRocksMainVersion.StarRocks_3_0.isLt(mainVersion))
                .build())

        ));

        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_UNIQUE_LABEL, "UNIQUE KEY"));
        result.add(optionDef(SrDsI18nKeys.EDITOR_TABLEINFO_COLUMN_KEYTYPE_AGGREGATE_LABEL, "AGGREGATE KEY"));
        return result;
    }

    protected List<ValueDef> fetchDistributedBy(DataSourceConfig dsConfig, TableEditorUiPanel uiPanel) {
        MainVersion mainVersion = SchemaFramework.getMainVersion(DsType.StarRocks, dsConfig.getVersion());
        UiPanelField primaryColumns = UiPanelField.builder()
            .field(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_TITLE)
            .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_DESC)
            .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_NAME_TITLE)
                .descI18N(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY_COLUMN_NAME_DESC)
                .readOnly(uiPanel.getViewMode() == EditorViewMode.Alter)
                .build());

        List<ValueDef> result = new ArrayList<>();
        if (StarRocksMainVersion.StarRocks_3_1.isLt(mainVersion)) {
            result.add(fieldOptionDef(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_RANDOM, "RANDOM"));
        }
        result.add(fieldOptionDef(SrDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_HASH, "HASH").addField(primaryColumns));
        return result;
    }

    protected List<ValueDef> fetchIndexIndexTypes(EditorViewMode mode) {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(SrDsI18nKeys.EDITOR_INDEXES_TYPE_BITMAP_LABEL, "BITMAP"));
        if (mode == EditorViewMode.Alter) {
            result.add(optionDef("Ngrambf", "NGRAMBF"));
        }
        return result;
    }
}

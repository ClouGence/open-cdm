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
package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;
import static com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * @author chunlin
 * @date 2024/4/2
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20d58a5f75191014b2fe92141b7df228.html?version=2.0.03&locale=en-US
 */
public class HanaTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements HanaTableEditorFields {

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
                .defaultValue(strValueDef("INTEGER"))
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

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_DEFAULT)
                .type(UiPanelFieldType.Options)
                .options(fetchDefault())
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_DESC)
                .build());
    }

    @Override
    protected List<ValueDef> fetchDefault() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(HanaDsI18nKeys.EDITOR_COLUMN_DEFAULT_NULL_LABEL, "NULL"));
        result.add(optionDef(HanaDsI18nKeys.EDITOR_COLUMN_DEFAULT_EMPTY_LABEL, ""));
        result.add(fieldOptionDef(HanaDsI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_LABEL, HanaTableEditorFields.FIELD_COLUMN_DEFAULT_CUSTOM).addField(UiPanelField.builder()
            .field(HanaTableEditorFields.FIELD_COLUMN_DEFAULT_CUSTOM)
            .type(UiPanelFieldType.Input)
            .titleI18N(HanaDsI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_TITLE)
            .descI18N(HanaDsI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_DESC)
            .build()));

        return result;
    }

    @Override
    protected void fillTableColumnsUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_AUTOINCREMENT)
                .type(UiPanelFieldType.Check)
                .defaultValue(boolValueDef(false))
                .readOnly(true)
                .titleI18N(EDITOR_COLUMNS_AUTOINCREMENT_TITLE)
                .descI18N(EDITOR_COLUMNS_AUTOINCREMENT_DESC)
                .build());
    }

    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        /*
            data types that can change length:
                DECIMAL, FLOAT(data_type, cs_data_type, essence is REAL),
                10 ,REAL ,FLOAT
                VARBINARY, VARCHAR, NVARCHAR, ALPHANUM, SHORTTEXT, CHAR, BINARY
         */
        List<ValueDef> result = new ArrayList<>();
        for (HanaTypes type : HanaTypes.values()) {
            switch (type) {
                case DECIMAL:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                case VARBINARY:
                case VARCHAR:
                case NVARCHAR:
                case ALPHANUM:
                case SHORTTEXT:
                case CHAR:
                case BINARY:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length));
                    break;
                case TINYINT:
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                case DOUBLE:
                case TIMESTAMP:
                case TIME:
                case DATE:
                case SECONDDATE:
                case BLOB:
                case TEXT:
                default:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
                    break;
            }
        }
        return result;
    }

    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("index_name"))
                .require(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_NAME_DESC)
                .build());

        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_TYPE)
                .type(UiPanelFieldType.Options)
                .options(fetchIndexTypes())
                .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_DESC)
                .build());

        UiPanelField indexColumns = UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_COLUMNS_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .build());
        this.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);
        uiPanel.getIndexes().addField(indexColumns);
    }

    @Override
    protected void fillTableKeysColumnsUiPanel(UiPanelField primaryColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysColumnsUiPanel(primaryColumns, dsConfig, viewMode, con);

        primaryColumns.addField(UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS_ORDER)
            .type(UiPanelFieldType.Options)
            .options(fetchIndexColumnOrder())
            .titleI18N(EDITOR_COMM_COLUMNS_ORDER_TITLE)
            .descI18N(EDITOR_COMM_COLUMNS_ORDER_DESC)
            .build());
    }

    @Override
    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_INDEXES_INDEX_TYPE)
                .type(UiPanelFieldType.Options)
                .options(fetchIndexTypes())
                .defaultValue(strValueDef("Normal"))
                .titleI18N(EDITOR_INDEXES_INDEX_TYPE_TITLE)
                .descI18N(EDITOR_INDEXES_INDEX_TYPE_DESC)
                .build(), FIELD_INDEXES_COLUMNS);

    }

    @Override
    protected void fillTableIndexesColumnsUiPanel(UiPanelField indexColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);

        super.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);

        indexColumns.addField(UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS_ORDER)
            .type(UiPanelFieldType.Options)
            .options(fetchIndexColumnOrder())
            .titleI18N(EDITOR_COMM_COLUMNS_ORDER_TITLE)
            .descI18N(EDITOR_COMM_COLUMNS_ORDER_DESC)
            .build());
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(EDITOR_INDEXES_TYPE_INVERTED_VALUE_LABEL, "INVERTED VALUE"));
        result.add(optionDef(EDITOR_INDEXES_TYPE_INVERTED_HASH_LABEL, "INVERTED HASH"));
        result.add(optionDef(EDITOR_INDEXES_TYPE_INVERTED_INDIVIDUAL_LABEL, "INVERTED INDIVIDUAL"));
        result.add(optionDef(EDITOR_INDEXES_TYPE_FULLTEXT_LABEL, "FULLTEXT"));
        return result;
    }

    protected List<ValueDef> fetchIndexColumnOrder() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(HanaDsI18nKeys.EDITOR_COMM_COLUMNS_ORDER_ASC_LABEL, "ASC"));
        result.add(optionDef(HanaDsI18nKeys.EDITOR_COMM_COLUMNS_ORDER_DESC_LABEL, "DESC"));
        return result;
    }
}

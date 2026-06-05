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
package com.clougence.clouddm.ds.ads.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.ads.i18n.AdsMyDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * create table
 * https://help.aliyun.com/document_detail/123333.html?spm=a2c4g.123333.0.0.3bf05f26FtH1c4
 * */
public class AdsMyTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory implements AdsMyTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getTableInfo().removeField(FIELD_TABLE_TEMPORARY);
    }

    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_STORAGEPOLICY)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef("MIXED"))
                .options(fetchStoragePolicy())
                .titleI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_TITLE)
                .descI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_RTENGINE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(""))
                .options(fetchRtEngine())
                .titleI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_RTENGINE_TITLE)
                .descI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_RTENGINE_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_PROPERTIES)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("{\"format\":\"columnstore\"}"))
                .titleI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_PROPERTIES_TITLE)
                .descI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_PROPERTIES_DESC)
                .build());
    }

    protected List<ValueDef> fetchStoragePolicy() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_MIXED_LABEL, "MIXED")//
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_STORAGEPOLICY_PT_CNT)
                .type(UiPanelFieldType.Input)
                .require(true)
                .titleI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_MIXED_PT_CNT_TITLE)
                .descI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_MIXED_PT_CNT_DESC)
                .build()));
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_HOT_LABEL, "HOT"));
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_COLD_LABEL, "COLD"));
        return result;
    }

    protected List<ValueDef> fetchRtEngine() {
        UiPanelField blockSize = UiPanelField.builder()
            .field(FIELD_TABLE_BLOCKSIZE)
            .type(UiPanelFieldType.Input)
            .titleI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_BLOCKSIZE_TITLE)
            .descI18N(AdsMyDsI18nKeys.EDITOR_TABLEINFO_BLOCKSIZE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_TABLEINFO_STORAGEPOLICY_EMPTY_LABEL, ""));
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_TABLEINFO_RTENGINE_ROWSTORE_LABEL, "ROWSTORE"));
        result.add(fieldOptionDef(AdsMyDsI18nKeys.EDITOR_TABLEINFO_RTENGINE_COLUMNSTORE_LABEL, "COLUMNSTORE").addField(blockSize));
        return result;
    }

    // tableEditor Columns panel
    @Override
    protected void fillTableColumnsUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        // keep empty
    }

    @Override
    protected ValueDef fillTableColumnsUiPanelForPreferenceColumnType(List<ValueDef> fetchTypes) {
        return strValueDef(AdbMySQLTypes.INT.getCodeKey());
    }

    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        UiPanelField columnStructure = UiPanelField.builder()
            .field(FIELD_COLUMN_STRUCTURE)
            .type(UiPanelFieldType.Input)
            .titleI18N(AdsMyDsI18nKeys.EDITOR_COLUMN_LENGTH_TITLE)
            .descI18N(AdsMyDsI18nKeys.EDITOR_COLUMN_LENGTH_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        for (AdbMySQLTypes type : AdbMySQLTypes.values()) {
            switch (type) {
                case TINYINT:
                case SMALLINT:
                case INT:
                case BIGINT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision));
                    break;
                case DECIMAL:
                case FLOAT:
                case DOUBLE:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                case DATETIME:
                case TIMESTAMP:
                case TIME:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision));
                    break;
                case VARCHAR:
                case BINARY:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
                    break;
                case ARRAY:
                case MAP:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(columnStructure));
                    break;
                default:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
                    break;
            }
        }
        return result;
    }

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        // keep empty
    }

    // tableEditor Indexes panel
    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes().removeField(FIELD_INDEXES_COMMENT);
    }

    @Override
    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_INDEXES_INDEX_WAY)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef("FullText"))
                .options(fetchIndexTypes())
                .titleI18N(MyDsI18nKeys.EDITOR_INDEXES_INDEX_TYPE_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_INDEXES_INDEX_TYPE_DESC)
                .build(), FIELD_INDEXES_COLUMNS);
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_INDEXES_TYPE_FULLTEXT_LABEL, "FullText"));
        result.add(fieldOptionDef(AdsMyDsI18nKeys.EDITOR_INDEXES_TYPE_ANN_LABEL, "ANN")//
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_ANNDISFUNCTION)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(""))
                .options(fetchIndexAnnDisFunction())
                .titleI18N(AdsMyDsI18nKeys.EDITOR_INDEXES_ANNDISFUNCTION_TITLE)
                .descI18N(AdsMyDsI18nKeys.EDITOR_INDEXES_ANNDISFUNCTION_DESC)
                .build())

            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_ANNALGORITHM)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(""))
                .options(fetchIndexAnnAlgorithm())
                .titleI18N(AdsMyDsI18nKeys.EDITOR_INDEXES_ANNALGORITHM_TITLE)
                .descI18N(AdsMyDsI18nKeys.EDITOR_INDEXES_ANNALGORITHM_DESC)
                .build()));
        return result;
    }

    protected List<ValueDef> fetchIndexAnnAlgorithm() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_INDEXES_TYPE_ANNALGORITHM_EMPTY_LABEL, ""));
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_INDEXES_TYPE_ANNALGORITHM_HNSWPQ_LABEL, "HNSW_PQ"));
        return result;
    }

    protected List<ValueDef> fetchIndexAnnDisFunction() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_INDEXES_TYPE_ANNDISFUNCTION_EMPTY_LABEL, ""));
        result.add(optionDef(AdsMyDsI18nKeys.EDITOR_INDEXES_TYPE_ANNDISFUNCTION_SQUAREDL2_LABEL, "SquaredL2"));
        return result;
    }
}

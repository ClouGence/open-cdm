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
package com.clougence.clouddm.ds.gauss.definition.gs.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.gauss.i18n.gs.GsDsI18nKeys;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: mode
 * @Date: 2023-09-22 16:54
 */
@Slf4j
public class GsTableEditorUiPanelFactory extends PgTableEditorUiPanelFactory implements GsTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        boolean readOnly = viewMode == EditorViewMode.Alter;

        // uiPanel.getTableInfo()
        //     .addField(UiPanelField.builder()
        //         .field(FIELD_TABLE_ORIENTATION)
        //         .type(UiPanelFieldType.Radios)
        //         .defaultValue(strValueDef(""))
        //         .readOnly(readOnly)
        //         .options(fetchTableOrientationTypes(uiPanel.getViewMode()))
        //         .titleI18N(GsDsI18nKeys.EDITOR_TABLEINFO_TABLE_ORIENTATION_TITLE)
        //         .descI18N(GsDsI18nKeys.EDITOR_TABLEINFO_TABLE_ORIENTATION_DESC)
        //         .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_STORAGE_TYPE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(""))
                .readOnly(readOnly)
                .options(fetchTableStorageType(uiPanel.getViewMode()))
                .titleI18N(GsDsI18nKeys.EDITOR_TABLEINFO_TABLE_STORAGE_TYPE_TITLE)
                .descI18N(GsDsI18nKeys.EDITOR_TABLEINFO_TABLE_STORAGE_TYPE_DESC)
                .build());
    }

    private List<ValueDef> fetchTableOrientationTypes(EditorViewMode viewMode) {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_DEFAULT, ""));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_ROW, "row"));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_COLUMN, "column"));
        return result;
    }

    private List<ValueDef> fetchTableStorageType(EditorViewMode viewMode) {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_TABLEINFO_STORAGE_DEFAULT, ""));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_TABLEINFO_STORAGE_ASTORE, "astore"));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_TABLEINFO_STORAGE_USTORE, "ustore"));
        return result;
    }

    // tableEditor Indexes panel
    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> idxTypeOptions = fetchIndexIndexTypes();
        UiPanelField forALL = UiPanelField.builder()
            .field(FIELD_INDEXES_INDEX_TYPE)
            .type(UiPanelFieldType.Radios)
            .defaultValue(strValueDef(""))
            .options(idxTypeOptions)
            .titleI18N(GsDsI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
            .descI18N(GsDsI18nKeys.EDITOR_INDEXES_TYPE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "Normal").addField(forALL));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "Unique"));
        return result;
    }

    @Override
    protected List<ValueDef> fetchIndexIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_INDEXES_TYPE_EMPTY_LABEL, ""));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_INDEXES_TYPE_UBTREE_LABEL, "ubtree"));
        result.add(fieldOptionDef(GsDsI18nKeys.EDITOR_INDEXES_TYPE_BTREE_LABEL, "btree"));
        return result;
    }
}

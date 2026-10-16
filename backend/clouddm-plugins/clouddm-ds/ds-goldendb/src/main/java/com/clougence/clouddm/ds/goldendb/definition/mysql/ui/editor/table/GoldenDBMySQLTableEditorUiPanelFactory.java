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
package com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.goldendb.i18n.GoldenDBDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

public class GoldenDBMySQLTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory implements GoldenDBMySQLTableEditorFields {

    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection connection) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, connection);
        boolean readOnly = viewMode == EditorViewMode.Alter;

        UiPanelField columns = UiPanelField.builder()
            .field(FIELD_TABLE_DISTRIBUTION_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .readOnly(readOnly)
            .titleI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_COLUMNS_TITLE)
            .descI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTION_COLUMN)
                .type(UiPanelFieldType.Columns)
                .readOnly(readOnly)
                .titleI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_COLUMNS_TITLE)
                .descI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_COLUMNS_DESC)
                .build());
        UiPanelField expression = UiPanelField.builder()
            .field(FIELD_TABLE_DISTRIBUTION_EXPRESSION)
            .type(UiPanelFieldType.Input)
            .require(true)
            .readOnly(readOnly)
            .titleI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_EXPRESSION_TITLE)
            .descI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_EXPRESSION_DESC)
            .build();
        UiPanelField groups = UiPanelField.builder()
            .field(FIELD_TABLE_DISTRIBUTION_GROUPS)
            .type(UiPanelFieldType.TextArea)
            .require(true)
            .readOnly(readOnly)
            .titleI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_GROUPS_TITLE)
            .descI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_GROUPS_DESC)
            .build();
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTION_TYPE)
                .type(UiPanelFieldType.Radios)
                .readOnly(readOnly)
                .options(distributionOptions(columns, expression, groups))
                .titleI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_TYPE_TITLE)
                .descI18N(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_TYPE_DESC)
                .build());
    }

    private List<ValueDef> distributionOptions(UiPanelField columns, UiPanelField expression, UiPanelField groups) {
        List<ValueDef> options = new ArrayList<>();
        options.add(optionDef(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_EMPTY, null));
        options.add(fieldOptionDef(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_HASH, "HASH").addField(columns).addField(groups));
        options.add(fieldOptionDef(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_RANGE, "RANGE").addField(expression).addField(groups));
        options.add(fieldOptionDef(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_LIST, "LIST").addField(expression).addField(groups));
        options.add(fieldOptionDef(GoldenDBDsI18nKeys.EDITOR_DISTRIBUTION_DUPLICATE, "DUPLICATE").addField(groups));
        return options;
    }
}

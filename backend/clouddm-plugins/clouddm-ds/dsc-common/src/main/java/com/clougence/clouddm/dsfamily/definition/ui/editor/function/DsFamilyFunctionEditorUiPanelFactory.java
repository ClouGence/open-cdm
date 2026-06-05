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
package com.clougence.clouddm.dsfamily.definition.ui.editor.function;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ObjectValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.i18n.DsFunctionEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;

public abstract class DsFamilyFunctionEditorUiPanelFactory implements FunctionEditorFields {

    public UiPanel createFunctionUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanel uiPanel = new UiPanel();

        fetchBaseInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchReturnTypeUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchParamTypeUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchSqlUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchOptionUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    protected void fetchSqlUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .type(UiPanelFieldType.TextArea)
            .field(SQL)
            .require(true)
            .defaultValue(ObjectValueDef.builder().value("begin\n \nend").build())
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_BODY_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_BODY_DESC)
            .build());
    }

    protected void fetchOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    protected void fetchParamTypeUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField params = UiPanelField.builder()
            .field(FUNCTION_PARAMS)
            .type(UiPanelFieldType.SelectColumns)
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_PARAM_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_PARAM_DESC)
            .build();
        params.addField(UiPanelField.builder()
            .field(PARAM_NAME)
            .type(UiPanelFieldType.Input)
            .require(true)
            .defaultValue(strValueDef("param_name"))
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_PARAM_PARAMNAME_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_PARAM_PARAMNAME_DESC)
            .build());

        params.addField(UiPanelField.builder()
            .field(PARAM_DATATYPE)
            .type(UiPanelFieldType.Options)
            .require(true)
            .defaultValue(strValueDef("INT"))
            .options(fetchTypes())
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_PARAM_PARAMTYPE_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_PARAM_PARAMTYPE_DESC)
            .build());

        uiPanel.addField(params);
    }

    protected void fetchReturnTypeUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .field(RETURN_TYPE)
            .type(UiPanelFieldType.Options)
            .require(true)
            .defaultValue(strValueDef("INT"))
            .options(fetchTypes())
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_RETURN_TYPE_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_RETURN_TYPE_DESC)
            .build());
    }

    protected void fetchBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(FUNCTION_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_NAME_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_NAME_DESC)
            .build());
    }

    protected abstract List<ValueDef> fetchTypes();
}

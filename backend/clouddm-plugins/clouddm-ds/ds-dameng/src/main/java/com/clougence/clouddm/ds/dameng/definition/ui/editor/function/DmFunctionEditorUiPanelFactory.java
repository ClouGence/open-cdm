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
package com.clougence.clouddm.ds.dameng.definition.ui.editor.function;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.dameng.DmParamModeTypes;
import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.dameng.i18n.DmDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.function.DsFamilyFunctionEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsFunctionEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class DmFunctionEditorUiPanelFactory extends DsFamilyFunctionEditorUiPanelFactory {

    @Override
    protected void fetchParamTypeUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField params = UiPanelField.builder()
            .field(FUNCTION_PARAMS)
            .type(UiPanelFieldType.SelectColumns)
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_NAME_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_NAME_DESC)
            .build();

        params.addField(UiPanelField.builder()
            .field(PARAM_NAME)
            .type(UiPanelFieldType.Input)
            .require(true)
            .defaultValue(strValueDef("param_name"))
            .titleI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_PARAMNAME_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_PARAMNAME_DESC)
            .build());

        params.addField(UiPanelField.builder()
            .field(PARAM_DATATYPE)
            .type(UiPanelFieldType.Options)
            .require(true)
            .defaultValue(strValueDef("INT"))
            .options(fetchTypes())
            .titleI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_PARAMTYPE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_PARAMTYPE_DESC)
            .build());

        params.addField(UiPanelField.builder()
            .field(PARAM_MODE)
            .type(UiPanelFieldType.Options)
            .require(true)
            .defaultValue(strValueDef(""))
            .options(fetchModeTypes())
            .titleI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_MODE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_MODE_DESC)
            .build());

        uiPanel.addField(params);
    }

    private List<ValueDef> fetchModeTypes() {

        UiPanelField defaultValue = UiPanelField.builder()
            .field(PARAM_DEFAULT_VALUE)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_DEFAULT_VALUE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_DEFAULT_VALUE_DESC)
            .build();

        List<ValueDef> valueDefs = new ArrayList<>();
        for (DmParamModeTypes value : DmParamModeTypes.values()) {
            if (DmParamModeTypes.IN == value) {
                valueDefs.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(defaultValue));
            } else {
                valueDefs.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()));
            }
        }
        return valueDefs;
    }

    @Override
    protected List<ValueDef> fetchTypes() {
        List<ValueDef> result = new ArrayList<>();
        for (DmSqlTypes value : DmSqlTypes.values()) {
            result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()));
        }
        return result;
    }

}

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
package com.clougence.clouddm.ds.dameng.definition.ui.editor.tablespace;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.dameng.i18n.DmDsI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsFunctionEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.tablespace.TablespaceEditorFields;

import java.sql.Connection;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

public class DmTablespaceUiPanelFactory implements TablespaceEditorFields {

    // todo
    public UiPanel createTablespaceUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanel uiPanel = new UiPanel();
        uiPanel.addField(UiPanelField.builder()
                .field(TABLESPACE_NAME)
                .type(UiPanelFieldType.Input)
                .titleI18N("name")
                .descI18N("name").build());

        UiPanelField params = UiPanelField.builder()
                .field(FILE_PATH)
                .type(UiPanelFieldType.SelectColumns)
                .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_NAME_TITLE)
                .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_NAME_DESC)
                .build();

        params.addField(UiPanelField.builder()
                .field(FILE_CONFIG)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("param_name"))
                .titleI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_PARAMNAME_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_PARAMNAME_DESC)
                .build());


        uiPanel.addField(params);
        return uiPanel;
    }
}

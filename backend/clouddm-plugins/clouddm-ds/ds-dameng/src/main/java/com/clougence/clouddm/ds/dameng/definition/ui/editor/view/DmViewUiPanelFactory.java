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
package com.clougence.clouddm.ds.dameng.definition.ui.editor.view;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsViewEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

import java.sql.Connection;

public class DmViewUiPanelFactory extends DsFamilyViewEditorUiPanelFactory {

    protected void fillBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .readOnly(viewMode == EditorViewMode.Alter)
            .field(VIEW_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsViewEditorI18nKeys.EDITOR_VIEW_NAME_TITLE)
            .descI18N(DsViewEditorI18nKeys.EDITOR_VIEW_NAME_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(COMMENT)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsViewEditorI18nKeys.EDITOR_VIEW_COMMENT_TITLE)
            .descI18N(DsViewEditorI18nKeys.EDITOR_VIEW_COMMENT_DESC)
            .build());
    }

}

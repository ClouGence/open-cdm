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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.view;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class OraViewEditorUiPanelFactory extends DsFamilyViewEditorUiPanelFactory implements OraViewEditorFields {

    @Override
    protected void fillOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField features = UiPanelField.builder()
            .field("features")
            .type(UiPanelFieldType.Fold)
            .titleI18N(Ora18nKeys.EDITOR_FUNCTION_FEATURES_TITLE)
            .descI18N(Ora18nKeys.EDITOR_FUNCTION_FEATURES_DESC)
            .build();

        features.addField(UiPanelField.builder()
            .field(FORCE_CREATE_VIEW)
            .type(UiPanelFieldType.Check)
            .titleI18N(Ora18nKeys.EDITOR_FORCE_CREATE_VIEW_TITLE)
            .descI18N(Ora18nKeys.EDITOR_FORCE_CREATE_VIEW_DESC)
            .build());

        uiPanel.addField(features);
    }
}

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
package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.view;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class Db2ViewEditorUiPanelFactory extends DsFamilyViewEditorUiPanelFactory implements Db2ViewEditorFields {

    @Override
    protected void fillOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField features = UiPanelField.builder()
            .field(FEATURES)
            .type(UiPanelFieldType.Fold)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_FEATURES_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_FEATURES_DESC)
            .build();

        features.addField(UiPanelField.builder()
            .field(VIEW_CHECK_OPTION)
            .type(UiPanelFieldType.Options)
            .options(fetchCheckOptions())
            .titleI18N(Db2DsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_DESC)
            .build());

        uiPanel.addField(features);
    }

    private List<ValueDef> fetchCheckOptions() {
        List<ValueDef> result = new ArrayList<>();
        result.add(UiUtils.fieldOptionDef(Db2DsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_CASCADED_LABEL, "cascaded"));
        result.add(UiUtils.fieldOptionDef(Db2DsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_LOCAL_LABEL, "local"));
        return result;
    }
}

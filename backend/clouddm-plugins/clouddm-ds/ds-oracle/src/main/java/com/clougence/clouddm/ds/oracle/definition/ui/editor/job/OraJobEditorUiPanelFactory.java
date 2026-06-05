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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.job;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.dsfamily.definition.ui.editor.job.DsFamilyJobEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsJobEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class OraJobEditorUiPanelFactory extends DsFamilyJobEditorUiPanelFactory implements OraJobFields {

    protected void fetchBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(EXEC_SQL)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsJobEditorI18nKeys.EDITOR_JOB_WHAT_TITLE)
            .descI18N(DsJobEditorI18nKeys.EDITOR_JOB_WHAT_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(JOB_NEXT_EXEC)
            .defaultValue(UiUtils.strValueDef("sysdate"))
            .type(UiPanelFieldType.Input)
            .titleI18N(DsJobEditorI18nKeys.EDITOR_JOB_NEXT_DATE_TITLE)
            .descI18N(DsJobEditorI18nKeys.EDITOR_JOB_NEXT_DATE_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(INTERVAL)
            .defaultValue(UiUtils.strValueDef("null"))
            .type(UiPanelFieldType.Input)
            .titleI18N(DsJobEditorI18nKeys.EDITOR_JOB_INTERVAL_TITLE)
            .descI18N(DsJobEditorI18nKeys.EDITOR_JOB_INTERVAL_DESC)
            .build());
    }
}

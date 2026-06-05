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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.schedule;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.schedule.ScheduleJobFields;

public class OraScheduleJobEditorUiPanelFactory implements OraScheduleJobFields, ScheduleJobFields {

    public UiPanel create(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanel uiPanel = new UiPanel();

        fetchBaseInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    protected void fetchBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_NAME_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(JOB_ACTION)
            .defaultValue(UiUtils.strValueDef("begin\n\nend;"))
            .type(UiPanelFieldType.TextArea)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ACTION_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ACTION_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .field(START_DATE)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_START_DATE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_START_DATE_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .field(REPEAT_INTERVAL)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_REPEAT_INTERVAL_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_REPEAT_INTERVAL_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .field(END_DATE)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_END_DATE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_END_DATE_DESC)
            .build());
        //        uiPanel.addField(UiPanelField.builder()
        //            .field(JOB_CLASS)
        //            .type(UiPanelFieldType.Input)
        //            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_JOB_CLASS_TITLE)
        //            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_JOB_CLASS_DESC)
        //            .build());
        uiPanel.addField(UiPanelField.builder()
            .field(ENABLED)
            .type(UiPanelFieldType.Check)
            .defaultValue(UiUtils.boolValueDef(true))
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ENABLE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ENABLE_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .field(AUTO_DROP)
            .type(UiPanelFieldType.Check)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_AUTO_DROP_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_AUTO_DROP_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .field(COMMENTS)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_COMMENTS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_COMMENTS_DESC)
            .build());
    }
}

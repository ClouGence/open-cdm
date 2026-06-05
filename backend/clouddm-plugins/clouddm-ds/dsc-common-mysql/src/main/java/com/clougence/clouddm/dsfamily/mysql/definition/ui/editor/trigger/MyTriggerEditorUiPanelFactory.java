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
package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTriggerEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public class MyTriggerEditorUiPanelFactory extends DsFamilyTriggerEditorUiPanelFactory implements TriggerEditorFields {

    @Override
    protected void fillBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_NAME_TITLE)
            .descI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_NAME_DESC)
            .build());

        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_TIME)
            .type(UiPanelFieldType.Radios)
            .options(fetchTriggerTimes())
            .defaultValue(strValueDef("before"))
            .titleI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_TIME_TITLE)
            .descI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_TIME_DESC)
            .build());

        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_EVENT)
            .type(UiPanelFieldType.Radios)
            .options(fetchTriggerEvents(dsConfig, con))
            .defaultValue(strValueDef("delete"))
            .titleI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_TITLE)
            .descI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_DESC)
            .build());
    }

    protected List<ValueDef> fetchTriggerEvents(DataSourceConfig dsConfig, Connection con) {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_DELETE_LABEL, "delete"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_INSERT_LABEL, "insert"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_UPDATE_LABEL, "update"));
        return result;
    }

    protected List<ValueDef> fetchTriggerTimes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_TIME_BEFORE_LABEL, "before"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_TIME_AFTER_LABEL, "after"));
        return result;
    }
}

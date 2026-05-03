package com.clougence.clouddm.ds.hana.definition.ui.editor.trigger;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTriggerEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class HanaTriggerEditorUiPanelFactory extends DsFamilyTriggerEditorUiPanelFactory implements HanaTriggerEditorFields {

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
            .type(UiPanelFieldType.CheckBox)
            .options(fetchTriggerEvents())
            .titleI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_TITLE)
            .descI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_DESC)
            .build());
    }

    protected List<ValueDef> fetchTriggerEvents() {
        List<ValueDef> result = new ArrayList<>();
        UiPanelField columnList = UiPanelField.builder()
            .field(TRIGGER_COLUMNS)
            .type(UiPanelFieldType.TriggerColumns)
            .titleI18N(HanaDsI18nKeys.EDITOR_TRIGGER_UPDATE_COLUMNS_TITLE)
            .descI18N(HanaDsI18nKeys.EDITOR_TRIGGER_UPDATE_COLUMNS_DESC)
            .build();
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_DELETE_LABEL, "delete"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_INSERT_LABEL, "insert"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_UPDATE_LABEL, "update").addField(columnList));

        return result;
    }

    protected List<ValueDef> fetchTriggerTimes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(HanaDsI18nKeys.EDITOR_TRIGGER_TIME_BEFORE_LABEL, "before"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_TIME_AFTER_LABEL, "after"));
        return result;
    }

}

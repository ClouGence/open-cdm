package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.trigger;

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
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTriggerEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class Db2TriggerEditorUiPanelFactory extends DsFamilyTriggerEditorUiPanelFactory implements Db2TriggerEditorFields {

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
            .defaultValue(strValueDef("delete"))
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
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_UPDATE_COLUMNS_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_UPDATE_COLUMNS_DESC)
            .build();
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_DELETE_LABEL, "delete"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_INSERT_LABEL, "insert"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_UPDATE_LABEL, "update").addField(columnList));

        return result;
    }

    protected List<ValueDef> fetchTriggerTimes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(Db2DsI18nKeys.EDITOR_TRIGGER_TIME_BEFORE_LABEL, "before"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_TIME_AFTER_LABEL, "after"));
        return result;
    }

    @Override
    protected void fillFeaturesUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

        uiPanel.addField(UiPanelField.builder()
            .field(TRIGGER_GRANULARITY)
            .type(UiPanelFieldType.Radios)
            .options(fetchGranularity())
            .require(true)
            .defaultValue(strValueDef("statement"))
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_DESC)
            .build());
    }

    private List<ValueDef> fetchGranularity() {
        ArrayList<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(Db2DsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_ROW_LABEL, "row").addFields(Arrays.asList(UiPanelField.builder()
            .field(NEW_ALIAS)
            .type(UiPanelFieldType.Input)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_NEW_ALIAS_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_NEW_ALIAS_DESC)
            .build(), UiPanelField.builder()
                .field(OLD_ALIAS)
                .type(UiPanelFieldType.Input)
                .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_OLD_ALIAS_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_OLD_ALIAS_DESC)
                .build(), UiPanelField.builder()
                    .field(CONDITION)
                    .type(UiPanelFieldType.Input)
                    .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_CONDITION_TITLE)
                    .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_CONDITION_DESC)
                    .build())));
//        result.add(fieldOptionDef(Db2DsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_STATEMENT_LABEL, "statement"));
        return result;
    }
}

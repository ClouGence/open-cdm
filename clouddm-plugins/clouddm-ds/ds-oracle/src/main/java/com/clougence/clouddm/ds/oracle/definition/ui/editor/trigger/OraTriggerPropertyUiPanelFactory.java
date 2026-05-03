package com.clougence.clouddm.ds.oracle.definition.ui.editor.trigger;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerPropertyUiPanelFactory;

public class OraTriggerPropertyUiPanelFactory extends DsFamilyTriggerPropertyUiPanelFactory implements OraTriggerEditorFields {

    @Override
    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_DDL_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(OBJ_STATUS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_STATUS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_STATUS_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(TRIGGER_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TRIGGER_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TRIGGER_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TRIGGER_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TRIGGER_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TRIGGER_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TRIGGER_EVENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TRIGGER_EVENT_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TRIGGER_EVENT_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TRIGGER_TABLE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TRIGGER_TABLE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TRIGGER_TABLE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TRIGGER_GRANULARITY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(CONDITION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TRIGGER_CONDITION_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TRIGGER_CONDITION_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(OBJ_ENABLED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ENABLE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ENABLE_DESC)
            .readOnly(true)
            .build());
        return fields;
    }
}

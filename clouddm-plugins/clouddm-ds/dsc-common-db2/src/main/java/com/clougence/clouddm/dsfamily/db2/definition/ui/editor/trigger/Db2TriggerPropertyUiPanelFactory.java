package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.trigger;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerPropertyUiPanelFactory;

public class Db2TriggerPropertyUiPanelFactory extends DsFamilyTriggerPropertyUiPanelFactory implements Db2TriggerEditorFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_NAME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(UPDATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_UPDATE_TIME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_UPDATE_TIME_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_TIME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_GRANULARITY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(DELETE_EVENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_DELETE_EVENT_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_DELETE_EVENT_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(INSERT_EVENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_INSERT_EVENT_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_INSERT_EVENT_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(UPDATE_EVENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_UPDATE_EVENT_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_UPDATE_EVENT_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_SECURE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TRIGGER_SECURE_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TRIGGER_SECURE_DESC)
            .build());

        return fields;
    }
}

package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function.MyFunctionEditorFields;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;

public class MyTriggerPropertyUiPanelFactory extends DsFamilyTriggerPropertyUiPanelFactory implements MyFunctionEditorFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TRIGGER_NAME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TRIGGER_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(DEFINER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TRIGGER_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TRIGGER_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_EVENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TRIGGER_EVENT_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TRIGGER_EVENT_DESC)
            .build());

        return fields;
    }
}

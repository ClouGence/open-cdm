package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;

import com.clougence.clouddm.dsfamily.definition.ui.editor.function.DsFamilyFunctionPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;

public class MyFunctionPropertyUiPanelFactory extends DsFamilyFunctionPropertyUiPanelFactory implements MyFunctionEditorFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(UPDATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_UPDATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_UPDATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(DEFINER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(SECURITY_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_SECURITY_TYPE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_SECURITY_TYPE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(SQL_DATA_ACCESS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_SQL_DATA_ACCESS_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_SQL_DATA_ACCESS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(DETERMINISTIC)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_OPTION_DETERMINISTIC_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_OPTION_DETERMINISTIC_DESC)
            .build());

        return fields;
    }
}

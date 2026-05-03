package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;

import java.util.ArrayList;
import java.util.List;

public class MyViewPropertyUiPanelFactory extends DsFamilyViewPropertyUiPanelFactory implements MyViewEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(VIEW_DEFINER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(VIEW_CHECK_OPTION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_CHECK_OPTIONS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(VIEW_UPDATABLE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_UPDATABLE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_UPDATABLE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(VIEW_SECURITY_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_SECURITY_TYPE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_SECURITY_TYPE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(VIEW_COLLATION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_COLLATION_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_COLLATION_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(VIEW_CHARACTER_SET)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(SQL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_BODY_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_BODY_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

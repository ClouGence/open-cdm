package com.clougence.clouddm.ds.oracle.definition.ui.editor.view;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewPropertyUiPanelFactory;

public class OraViewPropertyUiPanelFactory extends DsFamilyViewPropertyUiPanelFactory implements OraViewEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(DDL_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(STATUS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_STATUS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_STATUS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(VIEW_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_VIEW_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_VIEW_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TEXT_LENGTH)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_VIEW_TEXT_LENGTH_TITLE)
            .descI18N(Ora18nKeys.EDITOR_VIEW_TEXT_LENGTH_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(SQL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_VIEW_BODY_TITLE)
            .descI18N(Ora18nKeys.EDITOR_VIEW_BODY_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(COMMENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_VIEW_COMMENT_TITLE)
            .descI18N(Ora18nKeys.EDITOR_VIEW_COMMENT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(READ_ONLY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_VIEW_READONLY_TITLE)
            .descI18N(Ora18nKeys.EDITOR_VIEW_READONLY_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

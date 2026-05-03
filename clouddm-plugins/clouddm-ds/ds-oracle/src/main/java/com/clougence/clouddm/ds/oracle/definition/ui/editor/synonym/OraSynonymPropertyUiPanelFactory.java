package com.clougence.clouddm.ds.oracle.definition.ui.editor.synonym;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.synonym.DsFamilySynonymPropertyUiPanelFactory;

public class OraSynonymPropertyUiPanelFactory extends DsFamilySynonymPropertyUiPanelFactory implements OraSynonymFields {

    @Override
    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_DESC)
            .build());
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
            .field(NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SYNONYM_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SYNONYM_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TABLE_SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SYNONYM_TABLE_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SYNONYM_TABLE_SCHEMA_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TABLE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SYNONYM_TABLE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SYNONYM_TABLE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(DBLINK)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SYNONYM_TABLE_DBLINK_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SYNONYM_TABLE_DBLINK_DESC)
            .build());
        return fields;

    }
}

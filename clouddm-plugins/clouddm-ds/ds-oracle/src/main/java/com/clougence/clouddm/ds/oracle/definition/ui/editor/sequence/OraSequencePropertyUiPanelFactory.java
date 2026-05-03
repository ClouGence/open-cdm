package com.clougence.clouddm.ds.oracle.definition.ui.editor.sequence;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.sequence.DsFamilySequencePropertiesUiPanelFactory;

public class OraSequencePropertyUiPanelFactory extends DsFamilySequencePropertiesUiPanelFactory implements OraSequenceFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_SCHEMA_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(DDL_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(STATUS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_STATUS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_STATUS_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(SEQUENCE_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(MIN_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_MIN_VALUE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_MIN_VALUE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(MAX_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_MAX_VALUE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_MAX_VALUE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(INCREMENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_INCREMENT_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_INCREMENT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CYCLE_FLAG)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_CYCLE_FLAG_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_CYCLE_FLAG_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CACHE_SIZE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_CACHE_SIZE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_CACHE_SIZE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_NUMBER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_LAST_NUMBER_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_LAST_NUMBER_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(SESSION_FLAG)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_SESSION_FLAG_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_SESSION_FLAG_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(KEEP_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_KEEP_VALUE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_KEEP_VALUE_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

package com.clougence.clouddm.ds.oceanbase.definition.obforora.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraTableEditorFields;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;

public class ObForOraTablePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements OraTableEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
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
            .field(FIELD_TABLE_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TEMPORARY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_TEMPORARY_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_TEMPORARY_DESC)
            .readOnly(true)
            .build());
        return fields;
    }
}

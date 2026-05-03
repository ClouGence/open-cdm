package com.clougence.clouddm.ds.oracle.definition.ui.editor.procedure;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedurePropertyUiPanelFactory;

public class OraProcedurePropertyUiPanelFactory extends DsFamilyProcedurePropertyUiPanelFactory implements OraProcedureFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_SCHEMA_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_DDL_TIME)
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
            .field(PROCEDURE_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_PROCEDURE_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_PROCEDURE_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(PIPELINED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_PROCEDURE_PIPELINED_TITLE)
            .descI18N(Ora18nKeys.EDITOR_PROCEDURE_PIPELINED_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(PARALLEL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_PROCEDURE_PARALLEL_TITLE)
            .descI18N(Ora18nKeys.EDITOR_PROCEDURE_PARALLEL_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

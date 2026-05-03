package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.procedure;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.db2.definition.ui.editor.trigger.Db2TriggerEditorFields;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedurePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerPropertyUiPanelFactory;

public class Db2ProcedurePropertyUiPanelFactory extends DsFamilyProcedurePropertyUiPanelFactory implements Db2ProcedureFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .require(true)
            .field(PROCEDURE_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_PROCEDURE_NAME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_PROCEDURE_NAME_DESC)
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
            .field(DEBUG_MODE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_DEBUG_MODE_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_DEBUG_MODE_DESC)
            .build());

        return fields;
    }
}

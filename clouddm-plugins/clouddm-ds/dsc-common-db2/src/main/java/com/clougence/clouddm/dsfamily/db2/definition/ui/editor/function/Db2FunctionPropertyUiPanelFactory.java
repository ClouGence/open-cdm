package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.function;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedurePropertyUiPanelFactory;

public class Db2FunctionPropertyUiPanelFactory extends DsFamilyProcedurePropertyUiPanelFactory implements Db2FunctionFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .require(true)
            .field(FUNCTION_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_FUNCTION_NAME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_FUNCTION_NAME_DESC)
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
        fields.add(UiPanelField.builder()
            .require(true)
            .field(DETERMINISTIC)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_DETERMINISTIC_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_DETERMINISTIC_DESC)
            .build());

        return fields;
    }
}

package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.procedure;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.postgre.PostgresParamModeTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedureEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;

public class PgProcedureOptionUiPanelFactory extends DsFamilyProcedureEditorUiPanelFactory {

    protected List<ValueDef> fetchModeTypes() {
        UiPanelField defaultValue = UiPanelField.builder()
            .field(PARAM_DEFAULT_VALUE)
            .type(UiPanelFieldType.Input)
            .require(true)
            .titleI18N(PgDsI18nKeys.EDITOR_PROCEDURE_PARAM_DEFAULT_VALUE_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_PROCEDURE_PARAM_DEFAULT_VALUE_DESC)
            .build();
        List<ValueDef> valueDefs = new ArrayList<>();
        valueDefs.add(fieldOptionDef(PostgresParamModeTypes.IN.getCodeKey(), PostgresParamModeTypes.IN.getCodeKey()).addField(defaultValue));
        valueDefs.add(fieldOptionDef(PostgresParamModeTypes.INOUT.getCodeKey(), PostgresParamModeTypes.INOUT.getCodeKey()));
        valueDefs.add(fieldOptionDef(PostgresParamModeTypes.VARIADIC.getCodeKey(), PostgresParamModeTypes.VARIADIC.getCodeKey()));
        return valueDefs;
    }

    protected List<ValueDef> fetchTypes() {
        List<ValueDef> result = new ArrayList<>();
        for (PostgresTypes type : PostgresTypes.values()) {

            result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));

        }
        return result;
    }
}

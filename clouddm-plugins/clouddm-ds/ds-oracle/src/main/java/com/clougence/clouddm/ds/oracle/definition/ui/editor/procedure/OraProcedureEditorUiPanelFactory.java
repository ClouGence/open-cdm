package com.clougence.clouddm.ds.oracle.definition.ui.editor.procedure;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.oracle.OracleParamModeTypes;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedureEditorUiPanelFactory;

public class OraProcedureEditorUiPanelFactory extends DsFamilyProcedureEditorUiPanelFactory {

    protected List<ValueDef> fetchModeTypes() {
        UiPanelField defaultValue = UiPanelField.builder()
            .field(PARAM_DEFAULT_VALUE)
            .type(UiPanelFieldType.Input)
            .require(true)
            .titleI18N(Ora18nKeys.EDITOR_FUNCTION_PARAM_DEFAULT_VALUE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_FUNCTION_PARAM_DEFAULT_VALUE_DESC)
            .build();
        List<ValueDef> valueDefs = new ArrayList<>();
        for (OracleParamModeTypes value : OracleParamModeTypes.values()) {
            if (OracleParamModeTypes.IN == value) {
                valueDefs.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(defaultValue));
            } else {
                valueDefs.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()));
            }
        }
        return valueDefs;
    }

    @Override
    protected List<ValueDef> fetchTypes() {
        List<ValueDef> result = new ArrayList<>();
        for (OracleSqlTypes type : OracleSqlTypes.values()) {
            result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
        }
        return result;
    }
}

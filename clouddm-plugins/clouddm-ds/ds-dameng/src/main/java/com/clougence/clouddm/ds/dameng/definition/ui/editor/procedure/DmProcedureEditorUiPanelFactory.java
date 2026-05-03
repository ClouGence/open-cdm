package com.clougence.clouddm.ds.dameng.definition.ui.editor.procedure;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.dameng.DmParamModeTypes;
import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.dameng.i18n.DmDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedureEditorUiPanelFactory;

public class DmProcedureEditorUiPanelFactory extends DsFamilyProcedureEditorUiPanelFactory {

    protected List<ValueDef> fetchModeTypes() {
        UiPanelField defaultValue = UiPanelField.builder()
            .field(PARAM_DEFAULT_VALUE)
            .type(UiPanelFieldType.Input)
            .require(true)
            .titleI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_DEFAULT_VALUE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_FUNCTION_PARAM_DEFAULT_VALUE_DESC)
            .build();
        List<ValueDef> valueDefs = new ArrayList<>();
        for (DmParamModeTypes value : DmParamModeTypes.values()) {
            if (DmParamModeTypes.IN == value) {
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
        for (DmSqlTypes type : DmSqlTypes.values()) {
            result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
        }
        return result;
    }
}

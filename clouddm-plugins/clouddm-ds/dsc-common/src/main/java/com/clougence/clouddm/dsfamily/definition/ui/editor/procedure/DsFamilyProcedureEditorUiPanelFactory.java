package com.clougence.clouddm.dsfamily.definition.ui.editor.procedure;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ObjectValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.i18n.DsProcedureEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;

public abstract class DsFamilyProcedureEditorUiPanelFactory implements ProcedureEditorFields {

    public UiPanel createProcedureUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanel uiPanel = new UiPanel();
        fetchBaseInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchParamTypeUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchSqlUiPanel(uiPanel, dsConfig, viewMode, con);
        fetchOptionUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    protected void fetchSqlUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .type(UiPanelFieldType.TextArea)
            .field(SQL)
            .titleI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_BODY_TITLE)
            .descI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_BODY_DESC)
            .defaultValue(ObjectValueDef.builder().value("begin\n \nend").build())
            .build());
    }

    protected void fetchOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    protected void fetchParamTypeUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField params = UiPanelField.builder()
            .field(PROCEDURE_PARAMS)
            .type(UiPanelFieldType.SelectColumns)
            .titleI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_NAME_TITLE)
            .descI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_NAME_DESC)
            .build();

        params.addField(UiPanelField.builder()
            .field(PARAM_NAME)
            .type(UiPanelFieldType.Input)
            .require(true)
            .defaultValue(strValueDef("param_name"))
            .titleI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_PARAMNAME_TITLE)
            .descI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_PARAMNAME_DESC)
            .build());

        params.addField(UiPanelField.builder()
            .field("mode")
            .require(false)
            .type(UiPanelFieldType.Options)
            .options(fetchModeTypes())
            .titleI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_MODE_TITLE)
            .descI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_MODE_DESC)
            .build());

        params.addField(UiPanelField.builder()
            .field(PARAM_DATATYPE)
            .type(UiPanelFieldType.Options)
            .require(true)
            .defaultValue(strValueDef("INT"))
            .options(fetchTypes())
            .titleI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_PARAMTYPE_TITLE)
            .descI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_PARAM_PARAMTYPE_DESC)
            .build());

        uiPanel.addField(params);

    }

    protected void fetchBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(PROCEDURE_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_NAME_TITLE)
            .descI18N(DsProcedureEditorI18nKeys.EDITOR_PROCEDURE_NAME_DESC)
            .build());
    }

    protected abstract List<ValueDef> fetchModeTypes();

    protected abstract List<ValueDef> fetchTypes();

}

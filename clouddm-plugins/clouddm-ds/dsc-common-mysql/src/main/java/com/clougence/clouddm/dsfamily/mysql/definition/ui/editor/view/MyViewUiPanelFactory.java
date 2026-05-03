package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class MyViewUiPanelFactory extends DsFamilyViewEditorUiPanelFactory implements MyViewEditorFields {

    @Override
    protected void fillOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField features = UiPanelField.builder()
            .field("features")
            .type(UiPanelFieldType.Fold)
            .titleI18N(MyDsI18nKeys.EDITOR_PROCEDURE_FEATURES_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_PROCEDURE_FEATURES_DESC)
            .build();

        features.addField(UiPanelField.builder()
            .field(VIEW_CHECK_OPTION)
            .type(UiPanelFieldType.Options)
            .options(fetchCheckOptions())
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_DESC)
            .build());

        uiPanel.addField(features);
    }

    private List<ValueDef> fetchCheckOptions() {
        List<ValueDef> result = new ArrayList<>();
        result.add(UiUtils.fieldOptionDef(MyDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_CASCADED_LABEL, "cascaded"));
        result.add(UiUtils.fieldOptionDef(MyDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_LOCAL_LABEL, "local"));
        return result;
    }
}

package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.view;

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
import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class PgViewEditorUiPanelFactory extends DsFamilyViewEditorUiPanelFactory implements PgViewEditorFields {

    @Override
    protected void fillOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField features = UiPanelField.builder()
            .field(FEATURES)
            .type(UiPanelFieldType.Fold)
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_FEATURES_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_FEATURES_TITLE)
            .build();
        //        uiPanel.getFeatures()
        //                .addField(UiPanelField.builder()
        //                        .field(VIEW_TEMP)
        //                        .type(UiPanelFieldType.Check)
        //                        .titleI18N(PgDsI18nKeys.EDITOR_VIEW_TEMP_CREATE_TITLE)
        //                        .descI18N(PgDsI18nKeys.EDITOR_VIEW_TEMP_CREATE_DESC)
        //                        .build());
        features.addField(UiPanelField.builder()
            .field(VIEW_SECURITY_BARRIER)
            .type(UiPanelFieldType.Check)
            .titleI18N(PgDsI18nKeys.EDITOR_VIEW_SECURITY_BARRIER_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_VIEW_SECURITY_BARRIER_DESC)
            .build());
        features.addField(UiPanelField.builder()
            .field(VIEW_CHECK_OPTION)
            .type(UiPanelFieldType.Options)
            .options(fetchCheckOptions())
            .titleI18N(PgDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_DESC)
            .build());

        uiPanel.addField(features);
    }

    private List<ValueDef> fetchCheckOptions() {
        List<ValueDef> result = new ArrayList<>();
        result.add(UiUtils.fieldOptionDef(PgDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_CASCADED_LABEL, "cascaded"));
        result.add(UiUtils.fieldOptionDef(PgDsI18nKeys.EDITOR_VIEW_CHECK_OPTIONS_LOCAL_LABEL, "local"));
        return result;
    }
}

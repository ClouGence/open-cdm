package com.clougence.clouddm.ds.oracle.definition.ui.editor.view;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class OraViewEditorUiPanelFactory extends DsFamilyViewEditorUiPanelFactory implements OraViewEditorFields {

    @Override
    protected void fillOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField features = UiPanelField.builder()
            .field("features")
            .type(UiPanelFieldType.Fold)
            .titleI18N(Ora18nKeys.EDITOR_FUNCTION_FEATURES_TITLE)
            .descI18N(Ora18nKeys.EDITOR_FUNCTION_FEATURES_DESC)
            .build();

        features.addField(UiPanelField.builder()
            .field(FORCE_CREATE_VIEW)
            .type(UiPanelFieldType.Check)
            .titleI18N(Ora18nKeys.EDITOR_FORCE_CREATE_VIEW_TITLE)
            .descI18N(Ora18nKeys.EDITOR_FORCE_CREATE_VIEW_DESC)
            .build());

        uiPanel.addField(features);
    }
}

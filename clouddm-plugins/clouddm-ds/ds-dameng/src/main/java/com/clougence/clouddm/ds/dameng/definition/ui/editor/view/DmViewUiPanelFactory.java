package com.clougence.clouddm.ds.dameng.definition.ui.editor.view;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsViewEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

import java.sql.Connection;

public class DmViewUiPanelFactory extends DsFamilyViewEditorUiPanelFactory {

    protected void fillBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .readOnly(viewMode == EditorViewMode.Alter)
            .field(VIEW_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsViewEditorI18nKeys.EDITOR_VIEW_NAME_TITLE)
            .descI18N(DsViewEditorI18nKeys.EDITOR_VIEW_NAME_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(COMMENT)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsViewEditorI18nKeys.EDITOR_VIEW_COMMENT_TITLE)
            .descI18N(DsViewEditorI18nKeys.EDITOR_VIEW_COMMENT_DESC)
            .build());
    }

}

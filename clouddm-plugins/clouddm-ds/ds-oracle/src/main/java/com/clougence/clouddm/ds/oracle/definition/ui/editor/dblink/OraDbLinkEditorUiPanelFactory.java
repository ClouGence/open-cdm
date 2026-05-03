package com.clougence.clouddm.ds.oracle.definition.ui.editor.dblink;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.dblink.DsFamilyDbLinkEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsDbLinkEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class OraDbLinkEditorUiPanelFactory extends DsFamilyDbLinkEditorUiPanelFactory implements OraDbLinkFields {

    protected void fetchBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(DBLINK_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_NAME_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_NAME_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(LINK_USERNAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_USERNAME_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_USERNAME_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(LINK_PASSWORD)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_PASSWORD_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_PASSWORD_DESC)
            .build());
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(LINK_URL)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_URL_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_URL_DESC)
            .build());
    }
}

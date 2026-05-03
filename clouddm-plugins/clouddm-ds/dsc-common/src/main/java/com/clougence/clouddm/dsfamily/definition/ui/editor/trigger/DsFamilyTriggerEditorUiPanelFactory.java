package com.clougence.clouddm.dsfamily.definition.ui.editor.trigger;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.i18n.DsTriggerEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public abstract class DsFamilyTriggerEditorUiPanelFactory implements TriggerEditorFields {

    public UiPanel createTriggerEditorUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanel uiPanel = new UiPanel();
        fillBaseInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        fillSqlInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        fillFeaturesUiPanel(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    protected void fillFeaturesUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    protected void fillSqlInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .type(UiPanelFieldType.TextArea)
            .field(TRIGGER_BODY)
            .defaultValue(strValueDef("begin\n\nend"))
            .titleI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_BODY_TITLE)
            .descI18N(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_BODY_DESC)
            .build());

    }

    protected abstract void fillBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con);

}

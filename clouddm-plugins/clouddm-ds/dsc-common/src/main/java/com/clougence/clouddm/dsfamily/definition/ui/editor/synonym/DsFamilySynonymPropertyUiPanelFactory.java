package com.clougence.clouddm.dsfamily.definition.ui.editor.synonym;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.i18n.DsJobEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.synonym.SynonymFields;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public abstract class DsFamilySynonymPropertyUiPanelFactory implements SynonymFields {

    public PropertyUiPanel create(DataSourceConfig dsConfig, Connection con) {
        PropertyUiPanel uiPanel = new PropertyUiPanel();

        fillBaseInfoUiPanel(uiPanel, con);

        return uiPanel;
    }

    private void fillBaseInfoUiPanel(PropertyUiPanel uiPanel, Connection con) {
        UiPanelField map = UiPanelField.builder()
            .field(BASE_INFO)
            .type(UiPanelFieldType.Map)
            .titleI18N(DsJobEditorI18nKeys.EDITOR_JOB_BASE_INFO_TITLE)
            .descI18N(DsJobEditorI18nKeys.EDITOR_JOB_BASE_INFO_DESC)
            .build();
        List<UiPanelField> fields = fillBaseInfoItem();
        for (UiPanelField field : fields) {
            map.addField(field);
        }
        uiPanel.getBaseInfo().addField(map);
    }

    protected List<UiPanelField> fillBaseInfoItem() {
        return Collections.emptyList();
    }
}

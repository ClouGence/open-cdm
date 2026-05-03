package com.clougence.clouddm.dsfamily.definition.ui.editor.view;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.i18n.DsJobEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class DsFamilyViewPropertyUiPanelFactory implements ViewEditorFields {

    public PropertyUiPanel create(DataSourceConfig dsConfig, Connection con) {
        PropertyUiPanel uiPanel = new PropertyUiPanel();

        fillBaseInfoUiPanel(uiPanel, con, dsConfig);

        return uiPanel;
    }

    private void fillBaseInfoUiPanel(PropertyUiPanel uiPanel, Connection con, DataSourceConfig dsConfig) {
        UiPanelField map = UiPanelField.builder().field(BASE_INFO).type(UiPanelFieldType.Map).build();
        List<UiPanelField> fields = fillBaseInfoItem(dsConfig);
        for (UiPanelField field : fields) {
            map.addField(field);
        }
        uiPanel.getBaseInfo().addField(map);
    }

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        return Collections.emptyList();
    }
}

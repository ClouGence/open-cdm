package com.clougence.clouddm.dsfamily.definition.ui.editor.procedure;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.i18n.DsJobEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public abstract class DsFamilyProcedurePropertyUiPanelFactory implements ProcedureEditorFields {

    public PropertyUiPanel create(DataSourceConfig dsConfig, Connection con) {
        PropertyUiPanel uiPanel = new PropertyUiPanel();
        fillBaseInfoUiPanel(uiPanel, con);
        return uiPanel;
    }

    private void fillBaseInfoUiPanel(PropertyUiPanel uiPanel, Connection con) {
        UiPanelField map = UiPanelField.builder().field(BASE_INFO).type(UiPanelFieldType.Map).build();
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

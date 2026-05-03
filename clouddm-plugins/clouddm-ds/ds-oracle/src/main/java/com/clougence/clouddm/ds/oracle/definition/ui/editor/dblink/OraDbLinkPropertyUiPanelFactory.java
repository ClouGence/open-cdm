package com.clougence.clouddm.ds.oracle.definition.ui.editor.dblink;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsDbLinkEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsJobEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;

public class OraDbLinkPropertyUiPanelFactory implements OraDbLinkFields {

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
        List<UiPanelField> fields = fillJobBaseInfoItem();
        for (UiPanelField field : fields) {
            map.addField(field);
        }
        uiPanel.getBaseInfo().addField(map);
    }

    protected List<UiPanelField> fillJobBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .require(true)
            .field(SCHEMA)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_SEQUENCE_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SEQUENCE_SCHEMA_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(DBLINK_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_NAME_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(CREATE_TIME)
            .type(UiPanelFieldType.Input)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(LINK_USERNAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_USERNAME_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_USERNAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .require(true)
            .field(LINK_URL)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_URL_TITLE)
            .descI18N(DsDbLinkEditorI18nKeys.EDITOR_DBLINK_URL_DESC)
            .build());

        return fields;

    }
}

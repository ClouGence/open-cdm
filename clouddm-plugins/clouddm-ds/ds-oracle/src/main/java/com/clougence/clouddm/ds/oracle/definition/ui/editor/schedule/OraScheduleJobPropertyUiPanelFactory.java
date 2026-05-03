package com.clougence.clouddm.ds.oracle.definition.ui.editor.schedule;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsJobEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;

public class OraScheduleJobPropertyUiPanelFactory implements OraScheduleJobFields {

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
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(STATUS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_STATUS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_STATUS_TITLE)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATOR)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_CREATOR_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_CREATOR_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(JOB_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_TYPE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_TYPE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(JOB_ACTION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ACTION_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ACTION_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(START_DATE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_START_DATE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_START_DATE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(END_DATE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_END_DATE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_END_DATE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(REPEAT_INTERVAL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_REPEAT_INTERVAL_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_REPEAT_INTERVAL_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(ENABLED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ENABLE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_JOB_ENABLE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(AUTO_DROP)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_AUTO_DROP_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_AUTO_DROP_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(COMMENTS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_SCHEDULE_COMMENTS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_SCHEDULE_COMMENTS_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

package com.clougence.clouddm.ds.oracle.definition.ui.editor.job;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.job.DsFamilyJobPropertiesUiPanelFactory;

public class OraJobPropertyUiPanelFactory extends DsFamilyJobPropertiesUiPanelFactory implements OraJobFields {

    protected List<UiPanelField> fillJobBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder().field(NAME).type(UiPanelFieldType.MapItem).titleI18N(Ora18nKeys.EDITOR_JOB_NAME_TITLE).descI18N(Ora18nKeys.EDITOR_JOB_NAME_DESC).build());
        fields.add(UiPanelField.builder()
            .field(EXEC_SQL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_WHAT_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_WHAT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(RUNNING)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_BROKEN_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_BROKEN_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATOR)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_CREATOR_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_CREATOR_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(INTERVAL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_INTERVAL_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_INTERVAL_DESC)
            .readOnly(true)
            .build());
        //        fields.add(UiPanelField.builder()
        //            .field(JOB_WHAT)
        //            .type(UiPanelFieldType.MapItem)
        //            .titleI18N(Ora18nKeys.EDITOR_JOB_WHAT_TITLE)
        //            .descI18N(Ora18nKeys.EDITOR_JOB_WHAT_DESC)
        //            .readOnly(true)
        //            .build());
        fields.add(UiPanelField.builder()
            .field(JOB_LAST_EXEC)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_LAST_DATA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_LAST_DATA_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(JOB_NEXT_EXEC)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_JOB_NEXT_DATE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_JOB_NEXT_DATE_DESC)
            .readOnly(true)
            .build());

        return fields;

    }

}

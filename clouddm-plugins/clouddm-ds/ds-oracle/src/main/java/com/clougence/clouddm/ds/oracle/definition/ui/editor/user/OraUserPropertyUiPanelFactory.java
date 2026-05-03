package com.clougence.clouddm.ds.oracle.definition.ui.editor.user;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.user.DsFamilyUserPropertyUiPanelFactory;

public class OraUserPropertyUiPanelFactory extends DsFamilyUserPropertyUiPanelFactory implements OraUserFields {

    @Override
    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(USER_ID)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_USER_ID_TITLE)
            .descI18N(Ora18nKeys.EDITOR_EDITOR_USER_ID_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(USER_COMMON)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_USER_COMMON_TITLE)
            .descI18N(Ora18nKeys.EDITOR_USER_COMMON_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(USER_ORACLE_MAINTAINED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_USER_ORACLE_MAINTAINED_TITLE)
            .descI18N(Ora18nKeys.EDITOR_USER_ORACLE_MAINTAINED_DESC)
            .build());
        return fields;
    }
}

package com.clougence.clouddm.ds.oracle.definition.ui.editor.role;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.role.DsFamilyRolePropertyUiPanelFactory;

public class OraRolePropertyUiPanelFactory extends DsFamilyRolePropertyUiPanelFactory implements OraRoleFields {

    @Override
    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(ROLE_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_ROLE_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_ROLE_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(ROLE_AUTHENTICATION_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_ROLE_AUTHENTICATION_TYPE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_ROLE_AUTHENTICATION_TYPE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(USER_COMMON)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_ROLE_COMMON_TITLE)
            .descI18N(Ora18nKeys.EDITOR_ROLE_COMMON_DESC)
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

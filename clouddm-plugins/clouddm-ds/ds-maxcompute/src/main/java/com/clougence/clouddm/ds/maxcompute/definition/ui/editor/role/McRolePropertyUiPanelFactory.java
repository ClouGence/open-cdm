package com.clougence.clouddm.ds.maxcompute.definition.ui.editor.role;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table.McTableEditorFields;
import com.clougence.clouddm.ds.maxcompute.i18n.McI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;

public class McRolePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements McTableEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(ROLE_POLICY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_ROLE_POLICY_TITLE)
            .descI18N(McI18nKeys.EDITOR_ROLE_POLICY_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(ROLE_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_ROLE_TYPE_TITLE)
            .descI18N(McI18nKeys.EDITOR_ROLE_TYPE_DESC)
            .readOnly(true)
            .build());
        return fields;
    }
}

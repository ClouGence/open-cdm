package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.view;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.view.DsFamilyViewPropertyUiPanelFactory;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2ViewPropertyUiPanelFactory extends DsFamilyViewPropertyUiPanelFactory implements Db2ViewEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        boolean isIBM = dsConfig.getDataSourceType() == DataSourceType.Db2Fori;

        fields.add(UiPanelField.builder()
            .field(VIEW_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TABLEINFO_TABLENAME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TABLEINFO_TABLENAME_DESC)
            .readOnly(true)
            .build());
        if (!isIBM) {
            fields.add(UiPanelField.builder()
                .field(CREATE_TIME)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_CREATE_TIME_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_CREATE_TIME_DESC)
                .readOnly(true)
                .build());
        }
        fields.add(UiPanelField.builder()
            .field(UPDATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_UPDATE_TIME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_UPDATE_TIME_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

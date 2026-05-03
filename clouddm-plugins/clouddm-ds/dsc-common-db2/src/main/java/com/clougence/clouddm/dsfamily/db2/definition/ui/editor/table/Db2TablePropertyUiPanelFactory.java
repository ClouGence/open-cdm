package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2TablePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements Db2TableEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        boolean isIBM = dsConfig.getDataSourceType() == DataSourceType.Db2Fori;

        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_NAME)
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
        if (!isIBM) {
            fields.add(UiPanelField.builder()
                .field(INVALIDATE_TIME)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_INVALIDATE_TIME_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_INVALIDATE_TIME_DESC)
                .readOnly(true)
                .build());
        }
        if (!isIBM) {
            fields.add(UiPanelField.builder()
                .field(STATS_TIME)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_STATS_TIME_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_STATS_TIME_DESC)
                .readOnly(true)
                .build());
        }
        fields.add(UiPanelField.builder()
            .field(COLUMN_COUNT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_COLUMN_COUNT_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TABLE_COLUMN_COUNT_DESC)
            .build());
        if (!isIBM) {
            fields.add(UiPanelField.builder()
                .field(ROW_COUNT)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_ROW_COUNT_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_ROW_COUNT_DESC)
                .build());
            fields.add(UiPanelField.builder()
                .field(NPAGES)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_NPAGES_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_NPAGES_DESC)
                .readOnly(true)
                .build());
            fields.add(UiPanelField.builder()
                .field(MPAGES)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_MPAGES_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_MPAGES_DESC)
                .readOnly(true)
                .build());
            fields.add(UiPanelField.builder()
                .field(FPAGES)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_FPAGES_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_FPAGES_DESC)
                .readOnly(true)
                .build());
        }

        if (isIBM) {
            fields.add(UiPanelField.builder()
                .field(SYSTEM_TABLE)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_SYSTEM_TABLE_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_SYSTEM_TABLE_DESC)
                .readOnly(true)
                .build());
            fields.add(UiPanelField.builder()
                .field(INSERTABLE)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_INSERTABLE_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_INSERTABLE_DESC)
                .readOnly(true)
                .build());
            fields.add(UiPanelField.builder()
                .field(DEFINER)
                .type(UiPanelFieldType.MapItem)
                .titleI18N(Db2DsI18nKeys.EDITOR_TABLE_DEFINER_TITLE)
                .descI18N(Db2DsI18nKeys.EDITOR_TABLE_DEFINER_DESC)
                .readOnly(true)
                .build());
        }
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_COMMENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_TABLEINFO_COMMENT_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_TABLEINFO_COMMENT_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

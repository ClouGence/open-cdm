package com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.maxcompute.i18n.McI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;

public class McTablePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements McTableEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(TABLE_ID)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_ID_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_ID_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(OWNER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_OWNER_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_OWNER_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATED_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_CREATE_TIME_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_CREATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(TABLE_MAX_LABEL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_MAX_LABEL_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_MAX_LABEL_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_META_MODIFIED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_LAST_META_MODIFIED_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_LAST_META_MODIFIED_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_DATA_MODIFIED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_LAST_DATA_MODIFIED_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_LAST_DATA_MODIFIED_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_ACCESS_MODIFIED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_LAST_ACCESS_MODIFIED_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_LAST_ACCESS_MODIFIED_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(DATA_BYTES)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_DATA_BYTES_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_DATA_BYTES_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(PHYSICAL_BYTES)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_PHYSICAL_BYTES_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_PHYSICAL_BYTES_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FILE_NUMBER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_FILE_NUMBER_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_FILE_NUMBER_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(RECORD_NUM)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_RECORD_NUM_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_RECORD_NUM_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(LIFE_DAY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_LIFE_DAY_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_LIFE_DAY_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(ARCHIVED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_ARCHIVED_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_ARCHIVED_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(TABLE_LABEL)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_LABEL_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_TABLE_LABEL_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(TABLE_CRYPTO_ALGO_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_TABLEINFO_CRYPTO_ALGO_NAME_TITLE)
            .descI18N(McI18nKeys.EDITOR_TABLEINFO_CRYPTO_ALGO_NAME_DESC)
            .readOnly(true)
            .build());
        return fields;
    }
}

package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;

public class MyTablePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements MyTableEditorFields {

    @Override
    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_ENGINE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_AUTOINCREMENT_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_ROWFORMAT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_UPDATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_UPDATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_UPDATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_CHECK_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_CHECK_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_CHECK_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_INDEX_LENGTH)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_INDEX_LENGTH_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_INDEX_LENGTH_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_DATA_LENGTH)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_DATA_LENGTH_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_DATA_LENGTH_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_DATA_FREE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_DATA_FREE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_DATA_FREE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_DATA_ROW)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_DATA_ROW_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_DATA_ROW_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_COLLATION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_CREATE_OPTION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_OPTION_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_OPTION_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_COMMENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_COMMENT_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_COMMENT_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}

package com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.oceanbase.i18n.ObDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * @Author: Ekko
 * @Date: 2023-09-14 15:53
 */
public class ObTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory implements ObTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        boolean readOnly = viewMode == EditorViewMode.Alter;

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_CHARACTER_SET)
                .type(UiPanelFieldType.Options)
                .readOnly(readOnly)
                .options(fetchCharacterSet(FIELD_TABLE_COLLATION, readOnly, con))
                .titleI18N(ObDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
                .descI18N(ObDsI18nKeys.EDITOR_COMM_CHARACTER_SET_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_AUTOINCREMENT_VALUE)
                .type(UiPanelFieldType.Input)
                .titleI18N(ObDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_TITLE)
                .descI18N(ObDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_ROWFORMAT)
                .type(UiPanelFieldType.Radios)
                .options(fetchRowFormat())
                .defaultValue(strValueDef(""))
                .hide(viewMode == EditorViewMode.Alter)
                .titleI18N(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_TITLE)
                .descI18N(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COMPRESSION)
                .type(UiPanelFieldType.Radios)
                .options(fetchCompression())
                .hide(viewMode == EditorViewMode.Alter)
                .titleI18N(ObDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_TITLE)
                .descI18N(ObDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_DESC)
                .build());
    }

    @Override
    protected List<ValueDef> fetchRowFormat() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_EMPTY_LABEL, ""));
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_DYNAMIC_LABEL, "DYNAMIC"));
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_COMPRESSED_LABEL, "COMPRESSED"));
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_CONDENSED_LABEL, "CONDENSED"));
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_CSV_REDUNDANT_LABEL, "REDUNDANT"));
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_COMPACT_LABEL, "COMPACT"));
        //result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_FIXED_LABEL, "Fixed")); // more db not support.
        return result;
    }

    @Override
    protected List<ValueDef> fetchCompression() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_EMPTY_LABEL, null));
        result.add(optionDef(ObDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_NONE_LABEL, "NONE"));
        result.add(optionDef("LZ4 v1.0", "lz4_1.0"));
        result.add(optionDef("LZ4 v1.9.1", "lz4_1.9.1"));
        result.add(optionDef("ZLIB v1.0", "zlib_1.0"));
        result.add(optionDef("ZSTD v1.0", "zstd_1.0"));
        result.add(optionDef("ZSTD v1.3.8", "zstd_1.3.8"));
        result.add(optionDef("Snappy v1.0", "snappy_1.0"));
        return result;
    }

    // tableEditor Columns panel
    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        UiPanelField numAutoincrement = UiPanelField.builder()
            .field(FIELD_COLUMN_AUTOINCREMENT)
            .type(UiPanelFieldType.Check)
            .defaultValue(boolValueDef(false))
            .titleI18N(ObDsI18nKeys.EDITOR_COLUMNS_AUTOINCREMENT_TITLE)
            .descI18N(ObDsI18nKeys.EDITOR_COLUMNS_AUTOINCREMENT_DESC)
            .build();

        UiPanelField characterSet = UiPanelField.builder()
            .field(FIELD_COLUMN_CHARACTER_SET)
            .type(UiPanelFieldType.Options)
            .options(fetchCharacterSet(FIELD_COLUMN_COLLATION, false, con))
            .titleI18N(ObDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
            .descI18N(ObDsI18nKeys.EDITOR_COMM_CHARACTER_SET_DESC)
            .build();

        UiPanelField enumSet = UiPanelField.builder()
            .field(FIELD_COLUMN_ENUMSET)
            .type(UiPanelFieldType.Tags)
            .titleI18N(ObDsI18nKeys.EDITOR_COLUMN_ENUMSET_TITLE)
            .descI18N(ObDsI18nKeys.EDITOR_COLUMN_ENUMSET_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        for (ObForMySQLTypes type : ObForMySQLTypes.values()) {
            switch (type) {
                case BIT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision));
                    break;
                case TINYINT:
                case SMALLINT:
                case MEDIUMINT:
                case INT:
                case BIGINT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numAutoincrement).addField(numPrecision));
                    break;
                case DECIMAL:
                case FLOAT:
                case DOUBLE:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                case DATETIME:
                case TIMESTAMP:
                case TIME:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision));
                    break;
                case CHAR:
                case VARCHAR:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length).addField(characterSet));
                    break;
                case TEXT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(characterSet));
                    break;
                case BINARY:
                case VARBINARY:
                case BLOB:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length));
                    break;
                case ENUM:
                case SET:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(enumSet).addField(characterSet));
                    break;
                default:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
                    break;
            }
        }
        return result;
    }

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    @Override
    protected void fillTableKeysColumnsUiPanel(UiPanelField primaryColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        primaryColumns.addField(UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS_PART)
            .type(UiPanelFieldType.Input)
            .titleI18N(ObDsI18nKeys.EDITOR_COMM_COLUMNS_PART_TITLE)
            .descI18N(ObDsI18nKeys.EDITOR_COMM_COLUMNS_PART_DESC)
            .build());
    }

    // tableEditor Indexes panel
    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes().removeField(FIELD_INDEXES_TYPE);
    }

    @Override
    protected void fillTableIndexesColumnsUiPanel(UiPanelField indexColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        indexColumns.addField(UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS_PART)
            .type(UiPanelFieldType.Input)
            .titleI18N(ObDsI18nKeys.EDITOR_COMM_COLUMNS_PART_TITLE)
            .descI18N(ObDsI18nKeys.EDITOR_COMM_COLUMNS_PART_DESC)
            .build());
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(ObDsI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "Normal"));
        result.add(optionDef(ObDsI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "Unique"));
        return result;
    }
}

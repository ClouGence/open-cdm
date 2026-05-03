package com.clougence.clouddm.ds.tidb.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.tidb.i18n.TiDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * create table
 * https://docs.pingcap.com/zh/tidb/stable/sql-statement-create-table
 * */
public class TiTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory implements TiTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_AUTOINCREMENT_VALUE)
                .type(UiPanelFieldType.Input)
                .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_DESC)
                .build());
    }

    // tableEditor Columns panel
    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        UiPanelField numUnsigned = UiPanelField.builder()
            .field(FIELD_COLUMN_UNSIGNED)
            .type(UiPanelFieldType.Check)
            .defaultValue(boolValueDef(false))
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMNS_UNSIGNED_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMNS_UNSIGNED_DESC)
            .build();

        UiPanelField numAutoincrement = UiPanelField.builder()
            .field(FIELD_COLUMN_AUTOINCREMENT)
            .type(UiPanelFieldType.Check)
            .defaultValue(boolValueDef(false))
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMNS_AUTOINCREMENT_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMNS_AUTOINCREMENT_DESC)
            .build();

        UiPanelField characterSet = UiPanelField.builder()
            .field(FIELD_COLUMN_CHARACTER_SET)
            .type(UiPanelFieldType.Options)
            .options(fetchCharacterSet(FIELD_COLUMN_COLLATION, false, con))
            .defaultValue(strValueDef("utf8mb4"))
            .titleI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_DESC)
            .build();

        UiPanelField enumSet = UiPanelField.builder()
            .field(FIELD_COLUMN_ENUMSET)
            .type(UiPanelFieldType.Tags)
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMN_ENUMSET_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMN_ENUMSET_DESC)
            .build();

        UiPanelField timeOnUpdate = UiPanelField.builder()
            .field(FIELD_COLUMN_ONUPDATE)
            .type(UiPanelFieldType.Radios)
            .defaultValue(strValueDef(""))
            .options(fetchOnUpdate())
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMNS_ONUPDATE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMNS_ONUPDATE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        for (MySQLTypes type : MySQLTypes.values()) {
            switch (type) {
                case BIT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision));
                    break;
                case TINYINT:
                case SMALLINT:
                case MEDIUMINT:
                case INT:
                case BIGINT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numAutoincrement).addField(numPrecision).addField(numUnsigned));
                    break;
                case DECIMAL:
                case FLOAT:
                case DOUBLE:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale).addField(numUnsigned));
                    break;
                case DATETIME:
                case TIMESTAMP:
                case TIME:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision).addField(timeOnUpdate));
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
        super.fillTableKeysUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        uiPanel.getKeys().removeField(FIELD_PRIMARY_INDEX_TYPE);
    }

    // tableEditor Indexes panel
    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("index_name"))
                .require(true)
                .titleI18N(TiDsI18nKeys.EDITOR_INDEXES_NAME_TITLE)
                .descI18N(TiDsI18nKeys.EDITOR_INDEXES_NAME_DESC)
                .build());

        UiPanelField indexColumns = UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(TiDsI18nKeys.EDITOR_INDEXES_COLUMNS_TITLE)
            .descI18N(TiDsI18nKeys.EDITOR_INDEXES_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(TiDsI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(TiDsI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .build());
        this.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);
        uiPanel.getIndexes().addField(indexColumns);
    }

    // tableEditor Indexes panel
    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(TiDsI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "Normal"));
        result.add(optionDef(TiDsI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "Unique"));
        return result;
    }
}

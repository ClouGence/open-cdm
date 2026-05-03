package com.clougence.clouddm.dsfamily.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public abstract class DsFamilyTableEditorUiPanelFactory implements DsFamilyTableEditorFields {

    public TableEditorUiPanel createTableEditorUiPanel(DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        TableEditorUiPanel uiPanel = new TableEditorUiPanel();
        uiPanel.setViewMode(viewMode);

        fillTableInfoUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTableColumnsUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTableKeysUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTableIndexesUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTablePartitionUiPanel(uiPanel, dsConfig, viewMode, con);
        fillTableConstraints(uiPanel, dsConfig, viewMode, con);
        fillTableForeignKeys(uiPanel, dsConfig, viewMode, con);
        return uiPanel;
    }

    // tableEditor TableInfo panel
    protected void fillTableInfoUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTableInfoUiPanelForBasic(uiPanel, dsConfig, viewMode, con);
        this.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTableInfoUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_CATALOG)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLECATALOG_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLECATALOG_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_SCHEMA)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLESCHEMA_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLESCHEMA_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_NAME)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("table_name"))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLENAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_TABLENAME_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_COMMENT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_TABLEINFO_COMMENT_DESC)
                .build());
    }

    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    // tableEditor Columns panel
    protected void fillTableColumnsUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTableColumnsUiPanelForBasic(uiPanel, dsConfig, viewMode, con);
        this.fillTableColumnsUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTableColumnsUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_SOURCE)
                .type(UiPanelFieldType.Input)
                .require(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_SOURCE_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_SOURCE_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_NAME)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("column_name"))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COLUMNNAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COLUMNNAME_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COMMENT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_COMMENT_DESC)
                .build());

        List<ValueDef> fetchTypes = fetchTypes(uiPanel, dsConfig, con);
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_DATATYPE)
                .type(UiPanelFieldType.Options)
                .require(true)
                .defaultValue(fillTableColumnsUiPanelForPreferenceColumnType(fetchTypes))
                .options(fetchTypes)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DATATYPE_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DATATYPE_DESC)
                .build());

        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_NOTNULL)
                .type(UiPanelFieldType.Check)
                .defaultValue(boolValueDef(false))
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_NOTNULL_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_NOTNULL_DESC)
                .build());

        List<ValueDef> defaultOptions = fetchDefault();
        uiPanel.getColumns()
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_DEFAULT_OPTION)
                .type(UiPanelFieldType.Radios)
                .defaultValue(CollectionUtils.isEmpty(defaultOptions) ? null : strValueDef((String) defaultOptions.get(0).asValue()))
                .options(defaultOptions)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_DESC)
                .build());
    }

    protected void fillTableColumnsUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    protected final List<ValueDef> fetchTypes(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con) {
        UiPanelField numPrecision = UiPanelField.builder()
            .field(FIELD_COLUMN_NUM_PRECISION)
            .type(UiPanelFieldType.Input)
            .defaultValue(strValueDef("10"))
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_NUM_PRECISION_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_NUM_PRECISION_DESC)
            .build();

        UiPanelField numScale = UiPanelField.builder()
            .field(FIELD_COLUMN_NUM_SCALE)
            .type(UiPanelFieldType.Input)
            .defaultValue(strValueDef("4"))
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_NUM_SCALE_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_NUM_SCALE_DESC)
            .build();

        UiPanelField datePrecision = UiPanelField.builder()
            .field(FIELD_COLUMN_DATE_PRECISION)
            .type(UiPanelFieldType.Input)
            .defaultValue(strValueDef("3"))
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DATE_PRECISION_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DATE_PRECISION_DESC)
            .build();

        UiPanelField length = UiPanelField.builder()
            .field(FIELD_COLUMN_LENGTH)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_LENGTH_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_LENGTH_DESC)
            .build();

        return fetchTypesUseBasic(uiPanel, dsConfig, con, numPrecision, numScale, datePrecision, length);
    }

    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        FieldType[] fieldTypes = TypeMapUtils.findColumnTypes(dsConfig.getDataSourceType());
        List<ValueDef> result = new ArrayList<>();
        for (FieldType type : fieldTypes) {
            if (type.isNumber() && !type.isAccurateDecimal()) {
                result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision));
            } else if (type.isDataOrTime()) {
                result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision));
            } else if (type.isString() || type.isBinary()) {
                result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length));
            } else if (type.hasApproximate() || type.isAccurateDecimal()) {
                result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale));
            } else {
                result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
            }
        }
        return result;
    }

    protected ValueDef fillTableColumnsUiPanelForPreferenceColumnType(List<ValueDef> fetchTypes) {
        ValueDef useDefault = CollectionUtils.isEmpty(fetchTypes) ? null : fetchTypes.get(0);
        for (ValueDef item : fetchTypes) {
            String typeStr = item.asValue().toString();
            if (StringUtils.containsIgnoreCase(typeStr, "int") || StringUtils.containsIgnoreCase(typeStr, "varchar")) {
                useDefault = item;
                break;
            }
        }
        return useDefault == null ? null : strValueDef((String) useDefault.asValue());
    }

    protected List<ValueDef> fetchDefault() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_NULL_LABEL, "NULL"));
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_EMPTY_LABEL, "EMPTY"));
        result.add(fieldOptionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_LABEL, "CUSTOM").addField(UiPanelField.builder()
            .field(FIELD_COLUMN_DEFAULT)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_DESC)
            .build()));

        return result;
    }

    // tableEditor Keys panel
    protected void fillTableKeysUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTableKeysUiPanelForBasic(uiPanel, dsConfig, viewMode, con);
        this.fillTableKeysUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTableKeysUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getKeys()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("Primary"))
                .readOnly(true)
                .hide(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_KEYS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_KEYS_NAME_DESC)
                .build());

        UiPanelField primaryColumns = UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_KEYS_COLUMNS_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_KEYS_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .build());
        this.fillTableKeysColumnsUiPanel(primaryColumns, dsConfig, viewMode, con);
        uiPanel.getKeys().addField(primaryColumns);
    }

    protected void fillTableKeysUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    protected void fillTableKeysColumnsUiPanel(UiPanelField primaryColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    // tableEditor Indexes panel
    protected void fillTableIndexesUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);
        this.fillTableIndexesUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("index_name"))
                .require(true)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_NAME_DESC)
                .build());

        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_COMMENT)
                .type(UiPanelFieldType.Input)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_COMMENT_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_COMMENT_DESC)
                .build());

        List<ValueDef> idxTypeOptions = fetchIndexTypes();
        uiPanel.getIndexes()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_TYPE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(CollectionUtils.isEmpty(idxTypeOptions) ? null : strValueDef((String) idxTypeOptions.get(0).asValue()))
                .options(idxTypeOptions)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_DESC)
                .build());

        UiPanelField indexColumns = UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .require(true)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_COLUMNS_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_INDEXES_COLUMNS_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_TITLE)
                .descI18N(DsTableEditorI18nKeys.EDITOR_COMM_COLUMNS_NAME_DESC)
                .build());
        this.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);
        uiPanel.getIndexes().addField(indexColumns);
    }

    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    protected void fillTableIndexesColumnsUiPanel(UiPanelField indexColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {

    }

    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "Normal"));
        result.add(optionDef(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "Unique"));
        return result;
    }

    // tableEditor partitions panel
    protected void fillTablePartitionUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        this.fillTablePartitionForOption(uiPanel, dsConfig, viewMode, con);
        this.fillTablePartitionForDefinition(uiPanel, dsConfig, viewMode, con);
        this.fillTablePartitionForContent(uiPanel, dsConfig, viewMode, con);
    }

    protected void fillTablePartitionForOption(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    protected void fillTablePartitionForDefinition(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    protected void fillTablePartitionForContent(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    // tableEditor constraints panel
    protected void fillTableConstraints(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }

    // tableEditor foreignKeys panel
    protected void fillTableForeignKeys(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
    }
}

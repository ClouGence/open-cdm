package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.clougence.adapter.mysql.MySQLOnCurrentUpdateType;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;

/** https://dev.mysql.com/doc/refman/8.0/en/create-table.html */
public class MyTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements MyTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_TEMPORARY)
                .type(UiPanelFieldType.Check)
                .readOnly(EditorViewMode.Alter == uiPanel.getViewMode())
                .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_TEMPORARY_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_TEMPORARY_DESC)
                .build());
    }

    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        boolean readOnly = viewMode == EditorViewMode.Alter;

        List<ValueDef> engineOptions = fetchTableEngine(dsConfig);
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_ENGINE)
                .type(UiPanelFieldType.Options)
                .readOnly(readOnly)
                .defaultValue(strValueDef("InnoDB"))
                .options(engineOptions)
                .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_AUTOINCREMENT_VALUE)
                .type(UiPanelFieldType.Input)
                .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_AUTOINCREMENT_VALUE_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_CHARACTER_SET)
                .type(UiPanelFieldType.Options)
                .readOnly(readOnly)
                .options(fetchCharacterSet(FIELD_TABLE_COLLATION, readOnly, con))
                .titleI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_COMM_CHARACTER_SET_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_ROWFORMAT)
                .type(UiPanelFieldType.Radios)
                .readOnly(readOnly)
                .defaultValue(strValueDef(""))
                .options(fetchRowFormat())
                .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COMPRESSION)
                .type(UiPanelFieldType.Radios)
                .readOnly(readOnly)
                .options(fetchCompression())
                .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_DESC)
                .build());
    }

    protected List<ValueDef> fetchTableEngine(DataSourceConfig dsConfig) {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_INNODB_LABEL, "InnoDB"));
        result.add(fieldOptionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_MYISAM_LABEL, "MyISAM").addField(UiPanelField.builder()
            .field(FIELD_TABLE_KEY_BLOCK_SIZE)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLEINFO_KEY_BLOCK_SIZE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLEINFO_KEY_BLOCK_SIZE_DESC)
            .build()));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_MEMORY_LABEL, "Memory"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_CSV_LABEL, "CSV"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_ARCHIVE_LABEL, "Archive"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_BLACKHOLE_LABEL, "Blackhole"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_NDB_LABEL, "NDB"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_MERGE_LABEL, "Merge"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_FEDERATED_LABEL, "Federated"));
        return result;
    }

    protected List<ValueDef> fetchRowFormat() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_EMPTY_LABEL, ""));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_DYNAMIC_LABEL, "Dynamic"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_COMPRESSED_LABEL, "Compressed"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_CSV_REDUNDANT_LABEL, "Redundant"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_COMPACT_LABEL, "Compact"));
        //result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ROWFORMAT_FIXED_LABEL, "Fixed")); // more db not support.
        return result;
    }

    protected List<ValueDef> fetchCompression() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_EMPTY_LABEL, null));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_NONE_LABEL, "NONE"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_ZLIB_LABEL, "ZLIB"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_COMPRESSION_LZ4_LABEL, "LZ4"));
        return result;
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

        UiPanelField numZerofill = UiPanelField.builder()
            .field(FIELD_COLUMN_ZEROFILL)
            .type(UiPanelFieldType.Check)
            .defaultValue(boolValueDef(false))
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMNS_ZEROFILL_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMNS_ZEROFILL_DESC)
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
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numAutoincrement).addField(numUnsigned).addField(numZerofill));
                    break;
                case DECIMAL:
                case FLOAT:
                case DOUBLE:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale).addField(numUnsigned).addField(numZerofill));
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
                case BINARY:
                case VARBINARY:
                case BLOB:
                case TEXT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length));
                    break;
                case ENUM:
                case SET:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(enumSet));
                    break;
                default:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
                    break;
            }
        }
        return result;
    }

    protected List<ValueDef> fetchOnUpdate() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(MyDsI18nKeys.EDITOR_COLUMNS_ONUPDATE_EMPTY_LABEL, ""));
        for (MySQLOnCurrentUpdateType updateType : MySQLOnCurrentUpdateType.values()) {
            result.add(optionDef(updateType.getTypeName().toUpperCase(), updateType.getTypeName()));
        }
        return result;
    }

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        List<ValueDef> idxTypeOptions = fetchIndexIndexTypes();
        uiPanel.getKeys()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_PRIMARY_INDEX_TYPE)
                .type(UiPanelFieldType.Radios)
                .options(idxTypeOptions)
                .titleI18N(MyDsI18nKeys.EDITOR_KEYS_INDEX_TYPE_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_KEYS_INDEX_TYPE_DESC)
                .build(), FIELD_PRIMARY_COLUMNS);
    }

    @Override
    protected void fillTableKeysColumnsUiPanel(UiPanelField primaryColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysColumnsUiPanel(primaryColumns, dsConfig, viewMode, con);

        primaryColumns.addField(UiPanelField.builder()
            .field(FIELD_PRIMARY_COLUMNS_PART)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_COMM_COLUMNS_PART_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COMM_COLUMNS_PART_DESC)
            .build());
    }

    // tableEditor Indexes panel
    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes().removeField(FIELD_INDEXES_TYPE);
    }

    @Override
    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_INDEXES_INDEX_WAY)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef("Normal"))
                .options(fetchIndexTypes())
                .titleI18N(MyDsI18nKeys.EDITOR_INDEXES_INDEX_TYPE_TITLE)
                .descI18N(MyDsI18nKeys.EDITOR_INDEXES_INDEX_TYPE_DESC)
                .build(), FIELD_INDEXES_COLUMNS);
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> idxTypeOptions = fetchIndexIndexTypes();
        UiPanelField indexType = UiPanelField.builder()
            .field(FIELD_INDEXES_INDEX_TYPE)
            .type(UiPanelFieldType.Radios)
            .options(idxTypeOptions)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(MyDsI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "Normal").addField(indexType));
        result.add(fieldOptionDef(MyDsI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "Unique").addField(indexType));
        result.add(optionDef(MyDsI18nKeys.EDITOR_INDEXES_TYPE_FULLTEXT_LABEL, "FullText"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_INDEXES_TYPE_SPATIAL_LABEL, "SPATIAL"));
        return result;
    }

    protected List<ValueDef> fetchIndexIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(MyDsI18nKeys.EDITOR_COMM_INDEX_TYPE_EMPTY_LABEL, null));
        result.add(optionDef(MyDsI18nKeys.EDITOR_COMM_INDEX_TYPE_BTREE_LABEL, "BTREE"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_COMM_INDEX_TYPE_HASH_LABEL, "HASH"));
        return result;
    }

    @Override
    protected void fillTableIndexesColumnsUiPanel(UiPanelField indexColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesColumnsUiPanel(indexColumns, dsConfig, viewMode, con);

        indexColumns.addField(UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS_PART)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_COMM_COLUMNS_PART_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COMM_COLUMNS_PART_DESC)
            .build());

        // List<ValueDef> idxColOrderOptions = fetchIndexColumnOrder();
        // indexColumns.addField(UiPanelField.builder()
        //     .field(FIELD_INDEXES_COLUMNS_ORDER)
        //     .type(UiPanelFieldType.Options)
        //     .defaultValue(CollectionUtils.isEmpty(idxColOrderOptions) ? null : strValueDef((String) idxColOrderOptions.get(0).asValue()))
        //     .options(idxColOrderOptions)
        //     .titleI18N(MyDsI18nKeys.EDITOR_COMM_COLUMNS_ORDER_TITLE)
        //     .descI18N(MyDsI18nKeys.EDITOR_COMM_COLUMNS_ORDER_DESC)
        //     .build());
    }

    // tableEditor other
    protected List<ValueDef> fetchIndexColumnOrder() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(MyDsI18nKeys.EDITOR_COMM_COLUMNS_ORDER_ASC_LABEL, "ASC"));
        result.add(optionDef(MyDsI18nKeys.EDITOR_COMM_COLUMNS_ORDER_DESC_LABEL, "DESC"));
        return result;
    }

    protected List<ValueDef> fetchCharacterSet(String collation, boolean readOnly, Connection con) {
        List<ValueDef> result = new ArrayList<>();
        Map<String, List<String>> characterSetMap = new LinkedHashMap<>();
        String sqlQuery = "select CHARACTER_SET_NAME,COLLATION_NAME from information_schema.COLLATIONS order by CHARACTER_SET_NAME";
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                String charSetName = rs.getString(1);
                String collationName = rs.getString(2);
                characterSetMap.computeIfAbsent(charSetName, key -> new ArrayList<>()).add(collationName);
                return null;
            }).extractData(resultSet);

            for (String characterSet : characterSetMap.keySet()) {
                result.add(defCollation(collation, readOnly, characterSet, characterSetMap.get(characterSet)));
            }
        } catch (SQLException e) {
            result.add(defCollation(collation, readOnly, "latin1", Arrays.asList("latin1_bin", "latin1_general_ci", "latin1_general_cs")));
            result.add(defCollation(collation, readOnly, "utf8", Arrays.asList("utf8_bin", "utf8_general_ci", "utf8_unicode_ci")));
            result.add(defCollation(collation, readOnly, "utf8mb4", Arrays.asList("utf8mb4_bin", "utf8mb4_general_ci", "utf8mb4_unicode_ci")));
        }
        return result;
    }

    protected ValueDef defCollation(String collationField, boolean readOnly, String characterSet, List<String> collations) {
        List<ValueDef> result = new ArrayList<>();
        for (String collation : collations) {
            result.add(optionDef(collation, collation));
        }

        return fieldOptionDef(characterSet, characterSet).addField(UiPanelField.builder()
            .field(collationField)
            .type(UiPanelFieldType.Options)
            .options(result)
            .readOnly(readOnly)
            .titleI18N(MyDsI18nKeys.EDITOR_COMM_COLLATION_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COMM_COLLATION_DESC)
            .build());
    }

    //    @Override
    //    protected void fillTablePartitionForOption(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con) {
    //        uiPanel.getPartitions()
    //            .addField(UiPanelField.builder()
    //                .field(MODE_PARTITION_TYPE)
    //                .type(UiPanelFieldType.Options)
    //                .options(fetchPartitionTypes())
    //                .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_TYPE_TITLE)
    //                .descI18N(MyDsI18nKeys.EDITOR_PARTITION_TYPE_DESC)
    //                .build());
    //
    //        uiPanel.getPartitions()
    //            .addField(UiPanelField.builder()
    //                .field(MODE_PARTITION_EXPRESSION)
    //                .type(UiPanelFieldType.Input)
    //                .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_EXPRESSION_TITLE)
    //                .descI18N(MyDsI18nKeys.EDITOR_PARTITION_EXPRESSION_DESC)
    //                .build());
    //
    //        uiPanel.getPartitions()
    //            .addField(UiPanelField.builder()
    //                .field(MODE_SUB_PARTITION_TYPE)
    //                .type(UiPanelFieldType.Options)
    //                .options(fetchSubPartitionTypes())
    //                .titleI18N(MyDsI18nKeys.EDITOR_SUB_PARTITION_TYPE_TITLE)
    //                .descI18N(MyDsI18nKeys.EDITOR_SUB_PARTITION_TYPE_DESC)
    //                .build());
    //        uiPanel.getPartitions()
    //            .addField(UiPanelField.builder()
    //                .field(MODE_SUB_PARTITION_EXPRESSION)
    //                .type(UiPanelFieldType.Input)
    //                .titleI18N(MyDsI18nKeys.EDITOR_SUB_PARTITION_EXPRESSION_TITLE)
    //                .descI18N(MyDsI18nKeys.EDITOR_SUB_PARTITION_EXPRESSION_DESC)
    //                .build());
    //    }
    //
    //    @Override
    //    protected void fillTablePartitionForContent(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con) {
    //        UiPanelField content = UiPanelField.builder()
    //            .field(MODE_PARTITION_ITEMS)
    //            .type(UiPanelFieldType.Tree)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_DEFINITION_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_DEFINITION_DESC)
    //            .build();
    //
    //        content.addField(UiPanelField.builder()
    //            .field(MODE_PARTITION_ITEM_NAME)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_NAME_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_NAME_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        content.addField(UiPanelField.builder()
    //            .field(MODE_PARTITION_ITEM_DESCRIPTION)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_DESCRIPTION_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_DESCRIPTION_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        content.addField(UiPanelField.builder()
    //            .field(FIELD_PARTITION_DATA_DIRECTORY)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_DATA_DIRECTORY_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_DATA_DIRECTORY_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        content.addField(UiPanelField.builder()
    //            .field(FIELD_PARTITION_INDEX_DIRECTORY)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_INDEX_DIRECTORY_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_INDEX_DIRECTORY_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        content.addField(UiPanelField.builder()
    //            .field(FIELD_PARTITION_MAX_ROWS)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_MAX_ROWS_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_MAX_ROWS_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        content.addField(UiPanelField.builder()
    //            .field(FIELD_PARTITION_MIN_ROWS)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_MIN_ROWS_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_MIN_ROWS_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        content.addField(UiPanelField.builder()
    //            .field(MODE_PARTITION_ITEM_COMMENT)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_COMMENT_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_ITEM_COMMENT_DESC)
    //            .options(fetchPartitionTypes())
    //            .build());
    //
    //        uiPanel.getPartitions().addField(content);
    //    }
    //
    //    private List<ValueDef> fetchPartitionTypes() {
    //        List<ValueDef> result = new ArrayList<>();
    //        UiPanelField columnList = UiPanelField.builder()
    //            .field(MODE_PARTITION_COUNT)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_COUNT_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_COUNT_DESC)
    //            .build();
    //        result.add(fieldOptionDef(MySQLPartitionType.HASH.getTypeName(), MySQLPartitionType.HASH.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.LINEAR_HASH.getTypeName(), MySQLPartitionType.LINEAR_HASH.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.KEY.getTypeName(), MySQLPartitionType.KEY.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.LINEAR_KEY.getTypeName(), MySQLPartitionType.LINEAR_KEY.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.RANGE.getTypeName(), MySQLPartitionType.RANGE.getTypeName()));
    //        result.add(fieldOptionDef(MySQLPartitionType.LIST.getTypeName(), MySQLPartitionType.LIST.getTypeName()));
    //
    //        return result;
    //    }
    //
    //    private List<ValueDef> fetchSubPartitionTypes() {
    //        List<ValueDef> result = new ArrayList<>();
    //        UiPanelField columnList = UiPanelField.builder()
    //            .field(MODE_PARTITION_COUNT)
    //            .type(UiPanelFieldType.Input)
    //            .titleI18N(MyDsI18nKeys.EDITOR_PARTITION_COUNT_TITLE)
    //            .descI18N(MyDsI18nKeys.EDITOR_PARTITION_COUNT_DESC)
    //            .build();
    //        result.add(fieldOptionDef(MySQLPartitionType.HASH.getTypeName(), MySQLPartitionType.HASH.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.LINEAR_HASH.getTypeName(), MySQLPartitionType.LINEAR_HASH.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.KEY.getTypeName(), MySQLPartitionType.KEY.getTypeName()).addField(columnList));
    //        result.add(fieldOptionDef(MySQLPartitionType.LINEAR_KEY.getTypeName(), MySQLPartitionType.LINEAR_KEY.getTypeName()).addField(columnList));
    //
    //        return result;
    //    }

}

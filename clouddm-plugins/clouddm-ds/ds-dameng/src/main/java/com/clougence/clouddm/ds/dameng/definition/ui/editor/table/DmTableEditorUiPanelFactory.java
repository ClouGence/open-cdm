package com.clougence.clouddm.ds.dameng.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.dameng.DmPartitionType;
import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.config.PartitionDefineTypeConfig;
import com.clougence.clouddm.base.metadata.ui.form.config.ReferencedColumnTypeConfig;
import com.clougence.clouddm.base.metadata.ui.form.value.FieldOptionValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.dameng.i18n.DmDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public class DmTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements DmTableEditorFields {

    @Override
    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        uiPanel.getIndexes()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_INDEXES_INDEX_WAY)
                .type(UiPanelFieldType.Options)
                .options(fetchIndexTypes())
                .defaultValue(strValueDef("NORMAL"))
                .titleI18N(DmDsI18nKeys.EDITOR_INDEXES_INDEX_WAY_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_INDEXES_INDEX_WAY_DESC)
                .build(), FIELD_INDEXES_COLUMNS);

    }

    @Override
    protected void fillTableForeignKeys(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getForeignKeys()
            .addField(UiPanelField.builder()
                .field(FIELD_FOREIGN_KEY_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("name"))
                .descI18N(DmDsI18nKeys.EDITOR_FOREIGN_KEY_NAME_TITLE)
                .titleI18N(DmDsI18nKeys.EDITOR_FOREIGN_KEY_NAME_DESC)
                .build());
        for (UiPanelField uiPanelField : fillTableConstarintsForForeign()) {
            uiPanel.getForeignKeys().addField(uiPanelField);
        }
    }

    @Override
    protected void fillTablePartitionForOption(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        // for delete or create partition
        uiPanel.getPartitions().addField(UiPanelField.builder().field("name").hide(true).type(UiPanelFieldType.Input).defaultValue(strValueDef("Partition")).build());
    }

    @Override
    protected void fillTablePartitionUiPanel(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        if (uiPanel.getViewMode() == EditorViewMode.Alter) {
            return;
        }
        super.fillTablePartitionUiPanel(uiPanel, dsConfig, viewMode, con);

    }

    @Override
    protected List<ValueDef> fetchDefault() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_NULL_LABEL, "NULL"));
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_EMPTY_LABEL, "EMPTY"));
        result.add(fieldOptionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_LABEL, FIELD_COLUMN_DEFAULT_CUSTOM).addField(UiPanelField.builder()
            .field(FIELD_COLUMN_DEFAULT_CUSTOM)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_DESC)
            .build()));

        return result;
    }

    @Override
    protected void fillTablePartitionForDefinition(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField definition = UiPanelField.builder()
            .field(MODE_PARTITION_DEFINITION)
            .type(UiPanelFieldType.PartitionDefineList)
            .typeConfig(new PartitionDefineTypeConfig(8))
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_DEFINITION_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_DEFINITION_DESC)
            .build();

        definition.addField(UiPanelField.builder()
            .field(MODE_DEFINITION_PARTITION_TYPE)
            .type(UiPanelFieldType.Options)
            .require(true)
            .defaultValue(strValueDef("RANGE"))
            .options(fetchPartitionDefinitionType())
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_TYPE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_TYPE_DESC)
            .build());

        definition.addField(UiPanelField.builder()
            .field(MODE_DEFINITION_PARTITION_EXPRESSION)
            .type(UiPanelFieldType.Input)
            .require(true)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_EXPRESSION_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_EXPRESSION_DESC)
            .build());

        UiPanelField template = UiPanelField.builder()
            .field(MODE_PARTITION_TEMPLATE)
            .type(UiPanelFieldType.PartitionTemplateList)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_TEMPLATE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_TEMPLATE_DESC)
            .build();
        for (UiPanelField uiPanelField : fillPartitionItem()) {
            template.addField(uiPanelField);
        }

        definition.addField(template);

        uiPanel.getPartitions().addField(definition);
    }

    private List<UiPanelField> fillPartitionItem() {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(MODE_PARTITION_ITEM_NAME)
            .type(UiPanelFieldType.Input)
            .defaultValue(strValueDef("part"))
            .require(true)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_NAME_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_PARTITION_ITEM_BOUNDARY_TYPE)
            .type(UiPanelFieldType.Options)
            .options(fetchBoundaryTypes())
            .defaultValue(strValueDef("LESS"))
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_BOUNDARY_TYPE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_BOUNDARY_TYPE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(MODE_PARTITION_ITEM_DESCRIPTION)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_BOUNDARY_VALUE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_BOUNDARY_VALUE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(MODE_PARTITION_ITEM_TABLESPACE)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_TABLE_SPACE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_TABLE_SPACE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_PARTITION_ITEM_FILL_RATIO)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_FILL_RATIO_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_ITEM_FILL_RATIO_DESC)
            .build());

        return fields;
    }

    private List<ValueDef> fetchBoundaryTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef("LESS", "LESS"));
        result.add(optionDef("EQU OR LESS", "EQU OR LESS"));
        return result;
    }

    @Override
    protected void fillTablePartitionForContent(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField content = UiPanelField.builder()
            .field(MODE_PARTITION_ITEMS)
            .type(UiPanelFieldType.Tree)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_CONTENT_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_CONTENT_DESC)
            .build();

        List<UiPanelField> uiPanelFields = fillPartitionItem();
        for (UiPanelField uiPanelField : uiPanelFields) {
            content.addField(uiPanelField);
        }
        uiPanel.getPartitions().addField(content);
    }

    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_TABLE_TYPE)
                .type(UiPanelFieldType.Options)
                .defaultValue(strValueDef("NORMAL"))
                .options(fetchTableType())
                .titleI18N(DmDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_DESC)
                .readOnly(uiPanel.getViewMode().equals(EditorViewMode.Alter))
                .build());
    }

    @Override
    protected void fillTableColumnsUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableColumnsUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        uiPanel.getColumns().removeField(FIELD_COLUMN_DEFAULT);

    }

    @Override
    protected void fillTableConstraints(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getConstraints()
            .addField(UiPanelField.builder()
                .field(FIELD_CONSTRAINT_NAME)
                .type(UiPanelFieldType.Input)
                .defaultValue(strValueDef("constraint_name"))
                .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_NAME_TITLE)
                .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_NAME_DESC)
                .build());
        uiPanel.getConstraints()
            .addField(UiPanelField.builder()
                .field(FIELD_CONSTRAINT_TYPE)
                .type(UiPanelFieldType.Options)
                .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_TYPE_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_TYPE_DESC)
                .options(fetchConstraintTypes(uiPanel.getViewMode()))
                .build());
    }

    private List<ValueDef> fetchConstraintTypes(EditorViewMode viewMode) {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_CONSTRAINTS_TYPE_CHECK_LABEL, "Check").addFields(fillTableConstraintsForCheck(viewMode)));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_CONSTRAINTS_TYPE_UNIQUE_LABEL, "Unique").addFields(fillTableConstraintsForUnique(viewMode)));
        return result;

    }

    private List<UiPanelField> fillTableConstarintsForForeign() {
        List<UiPanelField> result = new ArrayList<>();
        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_REFERENCED_SCHEMA)
            .type(UiPanelFieldType.ReferenceSchema)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_SCHEMA_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_SCHEMA_DESC)
            .build());
        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_REFERENCED_TABLE)
            .type(UiPanelFieldType.ReferenceTable)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_TABLE_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_TABLE_DESC)
            .build());

        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_REFERENCED_RELATION)
            .type(UiPanelFieldType.ReferenceRelation)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_RELATION_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_RELATION_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_CONSTRAINT_COLUMN_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_COLUMN_NAME_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_COLUMN_NAME_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(FIELD_CONSTRAINT_REFERENCED_COLUMN_NAME)
                .type(UiPanelFieldType.ReferenceColumn)
                .typeConfig(new ReferencedColumnTypeConfig(GeneralConstraintType.Primary.name()))
                .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_COLUMN_NAME_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_REFERENCED_COLUMN_NAME_DESC)
                .build()));

        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_UPDATE_RULE)
            .type(UiPanelFieldType.Options)
            .options(fetchUpdateRules())
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_UPDATE_RULE_DESC)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_UPDATE_RULE_TITLE)
            .build());

        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_DELETE_RULE)
            .type(UiPanelFieldType.Options)
            .options(fetchDeleteRules())
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_DELETE_RULE_DESC)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_DELETE_RULE_TITLE)
            .build());
        return result;
    }

    private List<ValueDef> fetchUpdateRules() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_UPDATE_NO_ACTION_RULE_LABEL, RdbForeignKeyRule.NoAction.getTypeName()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_UPDATE_CASCADE_RULE_LABEL, RdbForeignKeyRule.Cascade.getTypeName()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_UPDATE_SET_NULL_RULE_LABEL, RdbForeignKeyRule.SetNull.getTypeName()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_UPDATE_SET_DEFAULT_RULE_LABEL, RdbForeignKeyRule.SetDefault.getTypeName()));
        return result;
    }

    private List<ValueDef> fetchDeleteRules() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_DELETE_NO_ACTION_RULE_LABEL, RdbForeignKeyRule.NoAction.getTypeName()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_DELETE_CASCADE_RULE_LABEL, RdbForeignKeyRule.Cascade.getTypeName()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_DELETE_SET_NULL_RULE_LABEL, RdbForeignKeyRule.SetNull.getTypeName()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_FOREIGN_DELETE_SET_DEFAULT_RULE_LABEL, RdbForeignKeyRule.SetDefault.getTypeName()));
        return result;
    }

    private List<UiPanelField> fillTableConstraintsForUnique(EditorViewMode viewMode) {
        List<UiPanelField> result = new ArrayList<>();
        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_COLUMNS)
            .type(UiPanelFieldType.SelectColumns)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_COLUMNS_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_COLUMNS_DESC)
            .build()
            .addField(UiPanelField.builder()
                .field(FIELD_CONSTRAINT_COLUMN_NAME)
                .type(UiPanelFieldType.Columns)
                .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_COLUMN_NAME_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_COLUMN_NAME_DESC)
                .build()));

        //        if (viewMode == EditorViewMode.Alter) {
        //            result.add(UiPanelField.builder()
        //                .field(FIELD_CONSTRAINT_ENABLE)
        //                .type(UiPanelFieldType.Check)
        //                .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_ENABLED_TITLE)
        //                .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_ENABLED_DESC)
        //                .build());
        //        }
        return result;
    }

    private List<UiPanelField> fillTableConstraintsForCheck(EditorViewMode viewMode) {
        List<UiPanelField> result = new ArrayList<>();
        result.add(UiPanelField.builder()
            .field(FIELD_CONSTRAINT_EXPRESSION)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_EXPRESSION_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_CONSTRAINTS_EXPRESSION_DESC)
            .build());

        return result;
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "NORMAL"));
        result.add(optionDef(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "UNIQUE"));
        result.add(optionDef(DmDsI18nKeys.EDITOR_INDEXES_TYPE_BITMAP_LABEL, "BITMAP"));
        return result;
    }

    protected List<ValueDef> fetchPartitionDefinitionType() {
        List<ValueDef> result = new ArrayList<>();

        UiPanelField interval = UiPanelField.builder()
            .field(FIELD_PARTITION_ITEM_RANGE_INTERVAL)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_PARTITION_INTERVAL_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_PARTITION_INTERVAL_DESC)
            .build();

        result.add(optionDef(DmPartitionType.HASH.getTypeName(), DmPartitionType.HASH.getTypeName()));
        result.add(optionDef(DmPartitionType.LIST.getTypeName(), DmPartitionType.LIST.getTypeName()));
        result.add(fieldOptionDef(DmPartitionType.RANGE.getTypeName(), DmPartitionType.RANGE.getTypeName()).addField(interval));
        return result;
    }

    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        UiPanelField identity = UiPanelField.builder()
            .field(FIELD_COLUMN_IDENTITY)
            .type(UiPanelFieldType.Check)
            .titleI18N(DmDsI18nKeys.EDITOR_COLUMN_IDENTITY_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_COLUMN_IDENTITY_DESC)
            .build();
        UiPanelField seed = UiPanelField.builder()
            .field(FIELD_COLUMN_IDENTITY_SEED)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_COLUMN_IDENTITY_SEED_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_COLUMN_IDENTITY_SEED_DESC)
            .build();
        UiPanelField increment = UiPanelField.builder()
            .field(FIELD_COLUMN_IDENTITY_INCREMENT)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_COLUMN_IDENTITY_INCREMENT_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_COLUMN_IDENTITY_INCREMENT_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        for (DmSqlTypes type : DmSqlTypes.values()) {
            FieldOptionValueDef fieldOptionDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey());
            switch (type) {
                case TINYINT:
                case SMALLINT:
                case INT:
                case BIGINT:
                    result.add(fieldOptionDef.addField(numPrecision).addField(identity).addField(seed).addField(increment));
                    break;
                case CLASS:
                case INDTAB:
                    break;
                default:
                    if (type.isNumber() && !type.isAccurateDecimal()) {
                        result.add(fieldOptionDef.addField(numPrecision));
                    } else if (type.isDataOrTime()) {
                        result.add(fieldOptionDef.addField(datePrecision));
                    } else if (type.isString() || type.isBinary()) {
                        result.add(fieldOptionDef.addField(length));
                    } else if (type.hasApproximate() || type.isAccurateDecimal()) {
                        result.add(fieldOptionDef.addField(numPrecision).addField(numScale));
                    } else {
                        result.add(fieldOptionDef);
                    }
                    break;
            }

            UiPanelField defOption = UiPanelField.builder()
                .field(FIELD_COLUMN_DEFAULT_OPTION)
                .type(UiPanelFieldType.Options)
                .titleI18N(DmDsI18nKeys.EDITOR_COLUMN_DEFAULT_OPTION_TITLE)
                .descI18N(DmDsI18nKeys.EDITOR_COLUMN_DEFAULT_OPTION_DESC)
                .options(fetchDefOption())
                .defaultValue(strValueDef("default"))
                .readOnly(EditorViewMode.Alter.equals(uiPanel.getViewMode()))
                .build();
            fieldOptionDef.addField(defOption);
        }
        return result;
    }

    private List<ValueDef> fetchDefOption() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_COLUMN_DEFAULT, "default").addField(UiPanelField.builder()
            .field(FIELD_COLUMN_DEFAULT)
            .type(UiPanelFieldType.Options)
            .options(fetchDefault())
            .titleI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_COLUMNS_DEFAULT_DESC)
            .build()));
        result.add(fieldOptionDef(DmDsI18nKeys.EDITOR_COLUMN_VIRTUAL, "virtual").addField(UiPanelField.builder()
            .field(FIELD_COLUMN_VIRTUAL_EXPR)
            .type(UiPanelFieldType.Input)
            .titleI18N(DmDsI18nKeys.EDITOR_COLUMN_VIRTUAL_EXPR_TITLE)
            .descI18N(DmDsI18nKeys.EDITOR_COLUMN_VIRTUAL_EXPR_DESC)
            .build()));
        return result;
    }

    private List<ValueDef> fetchTableType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DmDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_NORMAL, "NORMAL"));
        result.add(optionDef(DmDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_HUGE, "HUGE"));
        result.add(optionDef(DmDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_TEMPORARY, "TEMPORARY"));
        return result;
    }

}

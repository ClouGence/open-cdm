package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.clougence.adapter.postgre.PostgresMainVersion;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.FieldOptionValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HashUtils;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;

import lombok.extern.slf4j.Slf4j;

/**
 * http://postgres.cn/docs/12/sql-createtable.html
 */
@Slf4j
public class PgTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements PgTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        boolean readOnly = viewMode == EditorViewMode.Alter;

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_TABLESPACE)
                .type(UiPanelFieldType.Options)
                .defaultValue(strValueDef("pg_default"))
                .readOnly(readOnly)
                .options(fetchTablespace(con))
                .titleI18N(PgDsI18nKeys.EDITOR_TABLEINFO_TABLESPACE_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TABLEINFO_TABLESPACE_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_TABLE_TYPE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef("p"))
                .readOnly(readOnly)
                .options(fetchTableType(uiPanel.getViewMode()))
                .titleI18N(PgDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_FILL_FACTOR)
                .type(UiPanelFieldType.Input)
                .readOnly(readOnly)
                .titleI18N(PgDsI18nKeys.EDITOR_TABLEINFO_FILL_FACTOR_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TABLEINFO_FILL_FACTOR_DESC)
                .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_INHERITED_FROM)
                .type(UiPanelFieldType.Tags)
                .readOnly(readOnly)
                .titleI18N(PgDsI18nKeys.EDITOR_TABLEINFO_INHERITED_FROM_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TABLEINFO_INHERITED_FROM_DESC)
                .build());
    }

    private List<ValueDef> fetchTableType(EditorViewMode viewMode) {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(PgDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_CONVENTION, "p"));
        result.add(optionDef(PgDsI18nKeys.EDITOR_TABLEINFO_TABLE_TYPE_UNLOGGED, "u"));
        return result;
    }

    private List<ValueDef> fetchTablespace(Connection con) {
        List<ValueDef> result = new ArrayList<>();
        String sqlQuery = "SELECT spcname FROM pg_tablespace;";
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                String spcname = rs.getString(1);
                result.add(optionDef(spcname, spcname));
                return null;
            }).extractData(resultSet);
        } catch (SQLException e) {
            String msg = "Init tablespace error" + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }

    // tableEditor Columns panel
    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        UiPanelField sortRules = UiPanelField.builder()
            .field(FIELD_COLUMN_SORT_RULES)
            .type(UiPanelFieldType.Options)
            .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_SORT_RULES_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_COLUMN_SORT_RULES_DESC)
            .options(fetchSortRules(con))
            .build();

        UiPanelField arrayDimension = UiPanelField.builder()
            .field(FIELD_COLUMN_ARRAY_DIMENSION)
            .type(UiPanelFieldType.Input)
            .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_ARRAY_DIMENSION_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_COLUMN_ARRAY_DIMENSION_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        PostgresTypes[] postgresTypes = PostgresTypes.values();
        Arrays.sort(postgresTypes);
        for (PostgresTypes type : postgresTypes) {
            FieldOptionValueDef valueDef = null;
            switch (type) {
                case BIT:
                case BIT_VARYING:
                case BIT_ARRAY:
                case BIT_VARYING_ARRAY: {
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length);
                    break;
                }
                case NUMERIC_ARRAY:
                case NUMERIC: {
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale);
                    break;
                }
                case DATE:
                case TIMESTAMP_WITHOUT_TIME_ZONE:
                case TIMESTAMP_WITH_TIME_ZONE:
                case TIME_WITH_TIME_ZONE:
                case TIME_WITHOUT_TIME_ZONE:
                case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
                case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
                case TIME_WITHOUT_TIME_ZONE_ARRAY:
                case TIME_WITH_TIME_ZONE_ARRAY: {
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision);
                    break;
                }
                case CHARACTER:
                case BPCHAR:
                case CHARACTER_VARYING:
                case CHARACTER_ARRAY:
                case CHARACTER_VARYING_ARRAY: {
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length).addField(sortRules);
                    break;
                }
                case NAME:
                case TEXT:
                case PG_NODE_TREE:
                case TEXT_ARRAY:
                case NAME_ARRAY:
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(sortRules);
                    break;
                case BIGINT:
                case SMALLINT:
                case INTEGER: {
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey());
                    break;
                }
                case INT2VECTOR_ARRAY:
                case OIDVECTOR_ARRAY:
                case TXID:
                case TXID_ARRAY:
                case ABSTIME:
                case ABSTIME_ARRAY:
                case PG_NODE_TREE_ARRAY:
                case ANYARRAY:
                case ANYARRAY_ARRAY:
                    break;
                default:
                    valueDef = fieldOptionDef(type.getCodeKey(), type.getCodeKey());
                    break;
            }
            if (valueDef != null) {
                result.add(valueDef);
                if (type.isArray()) {
                    valueDef.addField(arrayDimension);
                }
            }

            // if (!types.isArray()) {
            //     //https://www.postgresql.org/docs/10/sql-createtable.html
            //     PostgresMainVersion version = PostgresMainVersion.parserVersion(fmtVersion(dsConfig));
            //     if (!PostgresMainVersion.PostgreSQL_10.isGt(version.getVersion())) {
            //         result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE, "Virtual Type").addField(UiPanelField.builder()
            //             .field(FIELD_COLUMN_VIRTUAL_TYPE)
            //             .type(UiPanelFieldType.Options)
            //             .options(fetchVirtualType(viewMode, isNextval, dsConfig))
            //             .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_TITLE)
            //             .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_DESC)
            //             .build()));
            //     }
            // }
        }
        return result;
    }

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        UiPanelField nameField = uiPanel.getKeys().findField(FIELD_PRIMARY_NAME);
        nameField.setHide(false);
        nameField.setReadOnly(false);
        if (viewMode == EditorViewMode.Create) {
            String pkName = ("pk_" + Long.toString(HashUtils.fnvHash(UUID.randomUUID().toString()), 16)).toUpperCase();
            nameField.setDefaultValue(strValueDef(pkName));
        }
    }

    @Override
    protected void fillTableKeysUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        uiPanel.getKeys()
            .addField(UiPanelField.builder()
                .field(FIELD_PRIMARY_CONSTRAINT_DELAY)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(""))
                .options(fetchConstraintDelay())
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_CONSTRAINT_DELAY_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_CONSTRAINT_DELAY_DESC)
                .build());
    }

    protected List<ValueDef> fetchConstraintDelay() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(PgDsI18nKeys.EDITOR_COLUMN_CONSTRAINT_DELAY_DEFAULT, ""));
        result.add(optionDef(PgDsI18nKeys.EDITOR_COLUMN_CONSTRAINT_DELAY_NOTDEFERRABLE, "NOT DEFERRABLE"));
        result.add(optionDef(PgDsI18nKeys.EDITOR_COLUMN_CONSTRAINT_DELAY_DEFERRABLE, "DEFERRABLE"));
        return result;
    }

    private List<ValueDef> fetchVirtualType(EditorViewMode viewMode, Boolean nextval, DataSourceConfig dsConfig) {
        UiPanelField panelField = UiPanelField.builder()
            .field(FIELD_COLUMN_VIRTUAL_TYPE_EXPRESSION)
            .type(UiPanelFieldType.Input)
            .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_EXPRESSION_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_EXPRESSION_DESC)
            .build();
        if (EditorViewMode.Alter.equals(viewMode)) {
            panelField.setReadOnly(true);
        }
        List<ValueDef> result = new ArrayList<>();
        //ERROR:  identity column type must be smallint, integer, or bigint
        if (nextval) {
            result.add(buildVirtualTypeOptionsPanel(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_GAAI, "ALWAYS"));
            result.add(buildVirtualTypeOptionsPanel(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_GBDAI, "BY DEFAULT"));
        }
        PostgresMainVersion version = PostgresMainVersion.parserVersion(fmtVersion(dsConfig));
        if (!PostgresMainVersion.PostgreSQL_12.isGt(version.getVersion())) {
            result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_GAAS, "STORED").addField(panelField));
        }
        return result;
    }

    public ValueDef buildVirtualTypeOptionsPanel(String optionName, String value) {
        return fieldOptionDef(optionName, value)
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_VIRTUAL_TYPE_INCREMENTAL)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_INCREMENTAL_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_INCREMENTAL_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_VIRTUAL_TYPE_MAX)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_MAX_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_MAX_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_VIRTUAL_TYPE_MIN)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_MIN_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_MIN_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_VIRTUAL_TYPE_START)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_START_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_START_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_VIRTUAL_TYPE_CACHE)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_CACHE_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_CACHE_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(FIELD_COLUMN_VIRTUAL_TYPE_LOOP)
                .type(UiPanelFieldType.Check)
                .defaultValue(boolValueDef(false))
                .titleI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_LOOP_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_COLUMN_VIRTUAL_TYPE_LOOP_DESC)
                .build());
    }

    // tableEditor Indexes panel
    protected List<ValueDef> fetchBuffering() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_EMPTY, ""));
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_AUTO, "auto"));
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_OFF, "off"));
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_ON, "on"));
        return result;
    }

    @Override
    protected void fillTableIndexesUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableIndexesUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        uiPanel.getIndexes().removeField(FIELD_INDEXES_TYPE);
    }

    @Override
    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_INDEXES_INDEX_WAY)
                .type(UiPanelFieldType.Radios)
                .options(fetchIndexTypes())
                .defaultValue(strValueDef("Normal"))
                .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_INDEX_WAY_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_INDEXES_INDEX_WAY_DESC)
                .build(), FIELD_INDEXES_COLUMNS);
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        List<ValueDef> idxTypeOptions = fetchIndexIndexTypes();
        UiPanelField forALL = UiPanelField.builder()
            .field(FIELD_INDEXES_INDEX_TYPE)
            .type(UiPanelFieldType.Radios)
            .defaultValue(strValueDef(""))
            .options(idxTypeOptions)
            .titleI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
            .descI18N(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_NORMAL_LABEL, "Normal").addField(forALL));
        result.add(fieldOptionDef(DsTableEditorI18nKeys.EDITOR_INDEXES_TYPE_UNIQUE_LABEL, "Unique"));
        return result;
    }

    protected List<ValueDef> fetchIndexIndexTypes() {
        UiPanelField fillPanel = UiPanelField.builder()
            .field(FIELD_INDEXES_WITH_FILLFACTOR)
            .type(UiPanelFieldType.Input)
            .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_WITH_FILLFACTOR_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_INDEXES_WITH_FILLFACTOR_DESC)
            .build();
        UiPanelField bufferPanel = UiPanelField.builder()
            .field(FIELD_INDEXES_WITH_BUFFERING)
            .type(UiPanelFieldType.Radios)
            .defaultValue(strValueDef(""))
            .options(fetchBuffering())
            .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_DESC)
            .build();
        UiPanelField fastPanel = UiPanelField.builder()
            .field(FIELD_INDEXES_WITH_FASTUPDATE)
            .type(UiPanelFieldType.Check)
            .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_WITH_FASTUPDATE_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_INDEXES_WITH_FASTUPDATE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_EMPTY_LABEL, ""));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_BTREE_LABEL, "btree").addField(fillPanel));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_HASH_LABEL, "hash").addField(fillPanel));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_GIST_LABEL, "gist").addField(fillPanel).addField(bufferPanel));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_SPGIST_LABEL, "spgist").addField(fillPanel));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_GIN_LABEL, "gin").addField(fastPanel));
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_TYPE_BRIN_LABEL, "brin"));
        return result;
    }

    @Override
    protected void fillTableIndexesColumnsUiPanel(UiPanelField indexColumns, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        indexColumns.addField(UiPanelField.builder()
            .field(FIELD_INDEXES_COLUMNS_LENGTH)
            .type(UiPanelFieldType.Input)
            .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_PREFIX_LENGTH_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_INDEXES_PREFIX_LENGTH_DESC)
            .build());

        // indexColumns.addField(UiPanelField.builder()
        //     .field(FIELD_INDEXES_SORT_RULES)
        //     .type(UiPanelFieldType.Options)
        //     .options(fetchSortRules(con))
        //     .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_SORT_RULES_TITLE)
        //     .descI18N(PgDsI18nKeys.EDITOR_INDEXES_SORT_RULES_DESC)
        //     .build());
        //
        // indexColumns.addField(UiPanelField.builder()
        //     .field(FIELD_INDEXES_OPERATOR_TYPE)
        //     .type(UiPanelFieldType.Options)
        //     .options(fetchOperatorType(con))
        //     .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_OPERATOR_TYPE_TITLE)
        //     .descI18N(PgDsI18nKeys.EDITOR_INDEXES_OPERATOR_TYPE_DESC)
        //     .build());

        // if (!versionThan12(dsConfig)) {
        //     indexColumns.addField(UiPanelField.builder()
        //         .field(FIELD_INDEXES_COLUMNS_ORDER)
        //         .type(UiPanelFieldType.Options)
        //         .options(fetchSortOrder())
        //         .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_SORT_ORDER_TITLE)
        //         .descI18N(PgDsI18nKeys.EDITOR_INDEXES_SORT_ORDER_DESC)
        //         .build());
        //
        //     indexColumns.addField(UiPanelField.builder()
        //         .field(FIELD_INDEXES_NULLS_SORT)
        //         .type(UiPanelFieldType.Options)
        //         .options(fetchNullsSort())
        //         .titleI18N(PgDsI18nKeys.EDITOR_INDEXES_NULLS_SORT_TITLE)
        //         .descI18N(PgDsI18nKeys.EDITOR_INDEXES_NULLS_SORT_DESC)
        //         .build());
        // }
    }

    private List<ValueDef> fetchSortRules(Connection con) {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(PgDsI18nKeys.EDITOR_COLUMN_SORT_RULES_DEFAULT, ""));

        String sqlQuery = "SELECT concat ( n.nspname,'.', C.collname )  FROM pg_collation C JOIN pg_namespace n ON C.collnamespace = n.oid;";
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                String collname = rs.getString(1);
                result.add(optionDef(collname, collname));
                return null;
            }).extractData(resultSet);
        } catch (SQLException e) {
            String msg = "Init sort rules error" + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }

    protected List<ValueDef> fetchOperatorType(Connection con) {
        List<ValueDef> result = new ArrayList<>();
        List<String> nspnameList = new ArrayList<>();
        String sqlQuery = "SELECT concat(pn.nspname , '.', po.opcname) as OPERATOR FROM pg_namespace pn left join pg_opclass po on po.opcnamespace = pn.oid ORDER BY opcname;";
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                nspnameList.add(rs.getString(1));
                return null;
            }).extractData(resultSet);
            //Operator categories are actually divided according to index categories, but there are some duplicates
            List<String> collect = nspnameList.stream().distinct().collect(Collectors.toList());
            collect.forEach(nspname -> result.add(optionDef(nspname, nspname)));
        } catch (SQLException e) {
            String msg = "Init opclass type error" + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }

    private List<ValueDef> fetchSortOrder() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_SORT_ORDER_ASC_LABEL, "A"));
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_SORT_ORDER_DESC_LABEL, "D"));
        return result;
    }

    private List<ValueDef> fetchNullsSort() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_NULLS_SORT_NULLS_FIRST_LABEL, "NULLS FIRST"));
        result.add(optionDef(PgDsI18nKeys.EDITOR_INDEXES_NULLS_SORT_NULLS_LAST_LABEL, "NULLS LAST"));
        return result;
    }

    // other

    private List<ValueDef> fetchInherits(Connection con) {
        List<ValueDef> result = new ArrayList<>();
        List<String> tableList = new ArrayList<>();
        String sqlQuery = "SELECT concat ( table_schema, '.', TABLE_NAME ) FROM information_schema.tables WHERE table_type = 'BASE TABLE' ORDER BY table_schema,TABLE_NAME";
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                String charSetName = rs.getString(1);
                tableList.add(charSetName);
                return null;
            }).extractData(resultSet);
            for (String table : tableList) {
                result.add(optionDef(table, table));
            }
        } catch (SQLException e) {
            String msg = "Init inherits error" + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }

    private List<ValueDef> fetchPartitionType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TABLEINFO_PARTITION_TYPE_RANGE, "range"));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TABLEINFO_PARTITION_TYPE_LIST, "list"));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TABLEINFO_PARTITION_TYPE_HASH, "hash"));
        return result;
    }

    private String fmtVersion(DataSourceConfig dsConfig) {
        String[] vers = dsConfig.getVersion().trim().split(" ");
        if (vers.length > 1) {
            if ("PostgreSQL".equals(vers[0])) {
                return vers[1];
            } else {
                return vers[0];
            }
        } else {
            return dsConfig.getVersion();
        }
    }

    private boolean versionThan12(DataSourceConfig dsConfig) {
        PostgresMainVersion version = PostgresMainVersion.parserVersion(fmtVersion(dsConfig));
        return PostgresMainVersion.PostgreSQL_12.isGt(version);
    }

    private boolean useOid(DataSourceConfig dsConfig) {
        PostgresMainVersion version = PostgresMainVersion.parserVersion(fmtVersion(dsConfig));
        return !PostgresMainVersion.PostgreSQL_12.isGt(version);
    }
}

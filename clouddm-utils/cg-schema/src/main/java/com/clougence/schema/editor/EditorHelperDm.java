package com.clougence.schema.editor;

import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.AUTO_INCREMENT;

import java.sql.SQLException;
import java.util.*;

import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.ForeignKeyEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.TableEditor.PrimaryEditor;
import com.clougence.schema.editor.builder.TableEditorImpl;
import com.clougence.schema.editor.domain.EConstraintType;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EditorHelperDm {

    private final RdbUmiServiceDm dmUmiService;
    private final SqlBuilder      builderProvider;

    public EditorHelperDm(RdbUmiServiceDm dmUmiService, SqlBuilder builderProvider){
        this.dmUmiService = Objects.requireNonNull(dmUmiService, "dmUmiService must not null.");
        this.builderProvider = Objects.requireNonNull(builderProvider, "builderProvider must not null.");
    }

    private EditorContext createEditorContext(EditorOptions options) throws SQLException {
        EditorContext editorContext = new EditorContext(this.builderProvider, this.dmUmiService.getVersion());
        editorContext.merge(options);
        editorContext.setUseDelimited(options.isUseDelimited());
        editorContext.setSkipHandlers(options.isSkipHandlers());
        return editorContext;
    }

    public ETable loadTable(String catalog, String schema, String table) throws SQLException {
        Map<UmiTypes, Object> levelsParam = new HashMap<>();
        levelsParam.put(UmiTypes.Catalog, catalog);
        levelsParam.put(UmiTypes.Schema, schema);
        RdbTable rdbTable = (RdbTable) this.dmUmiService.detailLeaf(levelsParam, UmiTypes.Table, table);
        if (rdbTable == null) {
            return null;
        }
        EditorContext editorContext = this.createEditorContext(new EditorOptions());
        return buildETable(catalog, schema, rdbTable, editorContext);
    }

    private ETable buildETable(String catalog, String schema, RdbTable rdbTable, EditorContext editorContext) {
        TableEditor tableEditor = new TableEditorImpl(catalog, schema, rdbTable.getName(), editorContext);

        // table
        tableEditor.setComment(rdbTable.getComment());

        // columns
        if (rdbTable.getColumns() != null) {
            rdbTable.getColumns().forEach((columnName, rdbColumn) -> {
                String dbType = rdbColumn.getSqlType().getCodeKey();
                boolean nullable = !rdbColumn.hasConstraint(GeneralConstraintType.NonNull);

                Long charLength = rdbColumn.getCharLength();
                Long byteLength = rdbColumn.getByteLength();
                Long length = (charLength != null && charLength > 0) ? charLength : byteLength;

                Integer numericPrecision = rdbColumn.getNumericPrecision();
                Integer numericScale = rdbColumn.getNumericScale();
                boolean numericUnsigned = Boolean.TRUE.equals(rdbColumn.getNumericUnsigned());
                Integer datetimePrecision = rdbColumn.getDatetimePrecision();
                String defaultValue = rdbColumn.getDefaultValue();
                boolean defaultValueIsFunc = Boolean.TRUE.equals(rdbColumn.getDefaultValueIsFunc());
                String comment = rdbColumn.getComment();
                boolean autoGenerate = "true".equalsIgnoreCase(AUTO_INCREMENT.getValue(rdbColumn.getAttributes()));

                ColumnEditor columnEditor = tableEditor.addColumn(columnName, dbType, nullable, length, //
                        numericPrecision, numericScale, numericUnsigned, datetimePrecision, //
                        defaultValue, defaultValueIsFunc, //
                        autoGenerate, comment);

                columnEditor.fillAttr(rdbColumn);
            });
        }

        // pk
        if (rdbTable.getPrimaryKey() != null) {
            RdbPrimaryKey primaryKey = rdbTable.getPrimaryKey();
            String primaryName = primaryKey.getName();
            List<String> columns = primaryKey.getColumnList();
            PrimaryEditor pkEditor = tableEditor.createPrimaryEditor(primaryName, columns);
            pkEditor.fillAttr(primaryKey);
        }

        // uk
        List<RdbUniqueKey> uniqueKeys = rdbTable.getUniqueKeys();
        if (uniqueKeys != null && !uniqueKeys.isEmpty()) {
            for (RdbUniqueKey uniqueKey : uniqueKeys) {
                String uniqueKeyName = uniqueKey.getName();
                List<String> columns = uniqueKey.getColumnList();
                IndexEditor indexEditor = tableEditor.addIndexEditor(uniqueKeyName, EIndexType.Unique, columns);
                indexEditor.setComment(uniqueKey.getComment());
                indexEditor.fillAttr(uniqueKey);
            }
        }

        // fk
        List<RdbForeignKey> foreignKeys = rdbTable.getForeignKeys();
        if (foreignKeys != null && !foreignKeys.isEmpty()) {
            rdbTable.getForeignKeys().forEach(fk -> {
                RdbForeignKeyRule updateRole = fk.getUpdateRule();
                RdbForeignKeyRule deleteRole = fk.getDeleteRule();
                Map<String, String> refMapping = new LinkedHashMap<>();
                for (String column : fk.getColumnList()) {
                    String refToColumn = fk.getReferenceMapping().get(column);
                    refMapping.put(column, refToColumn);
                }
                ForeignKeyEditor fkEditor = tableEditor.addForeignKeyEditor(fk.getName(), fk.getReferenceSchema(), fk.getReferenceTable(), updateRole, deleteRole, refMapping);
                fkEditor.fillAttr(fk);
            });
        }

        // index
        List<RdbIndex> indices = rdbTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            rdbTable.getIndices().stream().filter(idx -> {
                return idx.getType() == RdbIndexType.Normal || idx.getType() == RdbIndexType.Unique;
            }).forEach(idx -> {
                boolean isUnique = idx.getType() == RdbIndexType.Unique;
                IndexEditor idxEditor = null;
                if (isUnique) {
                    idxEditor = tableEditor.addIndexEditor(idx.getName(), EIndexType.Unique, idx.getColumnList());
                } else {
                    idxEditor = tableEditor.addIndexEditor(idx.getName(), EIndexType.Normal, idx.getColumnList());
                }
                idxEditor.setComment(idx.getComment());
                idxEditor.fillAttr(idx);
            });
        }

        // partition
        RdbPartition partition = rdbTable.getPartition();
        if (partition != null) {
            String partitionMethod = partition.getPartitionMethod();
            String partitionExpression = partition.getPartitionExpression();
            String subPartitionMethod = partition.getSubPartitionMethod();
            String subPartitionExpression = partition.getSubPartitionExpression();

            TableEditor.PartitionEditor partitionEditor = tableEditor.addPartition(partitionMethod, partitionExpression, subPartitionMethod, subPartitionExpression);
            if (CollectionUtils.isNotEmpty(partition.getPtDefs())) {
                for (RdbPartitionDef definition : partition.getPtDefs()) {
                    TableEditor.PartitionDefinitionEditor definitionEditor = partitionEditor.addDefinition(definition.getType(), definition.getExpression());
                    if (CollectionUtils.isNotEmpty(definition.getDefItems())) {
                        for (RdbPartitionDef item : definition.getDefItems()) {
                            definitionEditor.addTemplate(item.getName(), item.getDescription());
                            definitionEditor.fillAttr(item);
                        }
                    }
                    definitionEditor.fillAttr(definition);
                }
            }

            if (CollectionUtils.isNotEmpty(partition.getPtItems())) {
                for (RdbPartitionItem item : partition.getPtItems()) {
                    TableEditor.PartitionItemEditor itemEditor = partitionEditor.addItem(item.getName(), item.getDescription(), item.getPartitionMethod());
                    if (CollectionUtils.isNotEmpty(item.getSubItems())) {
                        handleSubItems(itemEditor, item.getSubItems());
                    }
                    itemEditor.fillAttr(item);
                }
            }
            partitionEditor.fillAttr(partition);

        }
        // constraints
        List<RdbUniqueKey> uniqueConstraints = rdbTable.getUniqueConstraints();
        if (CollectionUtils.isNotEmpty(uniqueConstraints)) {
            for (RdbUniqueKey uniqueKey : uniqueConstraints) {
                tableEditor.addConstraint(uniqueKey.getName(), EConstraintType.Unique, null, uniqueKey.getColumnList());
            }
        }
        List<RdbCheckConstraint> checkConstraints = rdbTable.getCheckConstraints();
        if (CollectionUtils.isNotEmpty(checkConstraints)) {
            for (RdbCheckConstraint checkConstraint : checkConstraints) {
                tableEditor.addConstraint(checkConstraint.getName(), EConstraintType.Check, checkConstraint.getExpression(), null);
            }
        }

        // attr
        tableEditor.fillAttr(rdbTable);
        return tableEditor.getSource();
    }

    private void handleSubItems(TableEditor.PartitionItemEditor itemEditor, List<RdbPartitionItem> subPartitionItems) {
        for (RdbPartitionItem item : subPartitionItems) {
            TableEditor.PartitionItemEditor editor = itemEditor.addSubItem(item.getName(), item.getDescription());
            if (CollectionUtils.isNotEmpty(item.getSubItems())) {
                handleSubItems(editor, item.getSubItems());
            }
            editor.fillAttr(item.getAttributes());
        }
    }

    public TableEditor createTableEditor(String catalog, String schema, String table, EditorOptions options) throws SQLException {
        Map<UmiTypes, Object> levelsParam = new HashMap<>();
        levelsParam.put(UmiTypes.Catalog, catalog);
        levelsParam.put(UmiTypes.Schema, schema);
        RdbTable rdbTable = (RdbTable) this.dmUmiService.detailLeaf(levelsParam, UmiTypes.Table, table);
        if (rdbTable != null) {
            throw new ConflictException("table '" + schema + "." + table + " already exists.");
        }
        //
        EditorContext editorContext = this.createEditorContext(options);
        return new TableEditorImpl(catalog, schema, table, editorContext);
    }

    public TableEditor editTableEditor(String catalog, String schema, String table, EditorOptions options) throws SQLException {
        ETable eTable = loadTable(catalog, schema, table);
        if (eTable == null) {
            throw new ConflictException("table '" + schema + "." + table + " is not exists.");
        }
        //
        EditorContext editorContext = this.createEditorContext(options);
        eTable.initDomain();
        return new TableEditorImpl(eTable, editorContext);
    }

    public TableEditor buildTableEditor(String catalog, String schema, RdbTable rdbTable) throws SQLException {
        EditorContext editorContext = this.createEditorContext(new EditorOptions());
        ETable eTable = buildETable(catalog, schema, rdbTable, editorContext);
        return new TableEditorImpl(eTable, editorContext);
    }

    /* ----------------------------------------------------------------------------------------------------------------------- */

    public static TableEditor restoreTableEditor(String editorData, EditorContext editorContext) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ETable eTable = objectMapper.readValue(editorData, new TypeReference<ETable>() {
            });

            if (eTable == null) {
                throw new NullPointerException("ETable data deserialization failed. data is null.");
            }
            return new TableEditorImpl(eTable, editorContext);
        } catch (Exception e) {
            String msg = "editorData to TableEditor error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            throw ExceptionUtils.toRuntime(e, ee -> new RuntimeException(msg, ee));
        }
    }

    public static TableEditor restoreTableEditor(ETable eTable, EditorContext editorContext) {
        return new TableEditorImpl(eTable, editorContext);
    }

    public static TableEditor restoreTableEditor(ETable eTable, TriggerContext triggerContext) {
        return new TableEditorImpl(eTable, triggerContext.toEditorContext());
    }

    public ETable loadTableByParser(String catalog, String schema, String table, RdbTable rdbTable) throws SQLException {
        if (rdbTable == null) {
            return null;
        }
        EditorContext editorContext = this.createEditorContext(new EditorOptions());
        return buildETable(catalog, schema, rdbTable, editorContext);
    }

}

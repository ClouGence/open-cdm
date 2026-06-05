/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.schema.editor.builder;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.schema.editor.ConflictException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.NoColumnException;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.builder.actions.ExecuteGenerateSql;
import com.clougence.schema.editor.builder.actions.TriggerCreate;
import com.clougence.schema.editor.builder.utils.ConstraintChangeUtil;
import com.clougence.schema.editor.builder.utils.ForeignKeyChangeUtil;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/5/21 19:56
 */
public class TableEditorImpl extends AbstractEditor implements TableEditor {

    private ETable eTable;

    public TableEditorImpl(String catalog, String schema, String name, EditorContext context){
        super(context);
        this.eTable = new ETable();
        this.eTable.setCatalog(catalog);
        this.eTable.setSchema(schema);
        this.eTable.setName(name);
    }

    public TableEditorImpl(ETable eTable, EditorContext context){
        super(context);
        this.eTable = eTable;
    }

    TableEditorImpl(EditorContext context, ETable eTable){
        super(context);
        this.eTable = eTable;
    }

    @Override
    public ETable getSource() { return this.eTable; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }

    @Override
    public List<Action> diffActions(ETable sourceETable, boolean create) {
        String sourceCatalog = sourceETable.getCatalog();
        String sourceSchema = sourceETable.getSchema();
        String sourceName = sourceETable.getName();

        List<Action> actions = new ArrayList<>();
        TriggerContext triggerContext = super.buildContext(super.context.getSrcDsInfo(), super.context.getTarDsInfo());
        triggerContext.setUseDelimited(true);
        SqlBuilder provider = triggerContext.getTarDsInfo().getProvider();
        if (create) {
            // if not exist, create
            List<String> tableCreateSql = provider.tableCreate(triggerContext, sourceCatalog, sourceSchema, sourceName, sourceETable);
            actions.add(new Action(tableCreateSql, sourceCatalog, sourceSchema, sourceName));
        } else if (sourceETable.testChanged(this.eTable)) {
            String sourceComment = sourceETable.getComment();
            Map<String, String> sourceAttr = sourceETable.getAttribute();

            List<EColumn> sourceColumnList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(sourceETable.getColumnList())) {
                sourceColumnList = sourceETable.getColumnList();
            }

            EPrimaryKey sourcePrimaryKey = new EPrimaryKey();
            if (sourceETable.getPrimaryKey() != null) {
                sourcePrimaryKey = sourceETable.getPrimaryKey();
            }

            List<EForeignKey> sourceForeignKeys = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(sourceETable.getForeignKeys())) {
                sourceForeignKeys = sourceETable.getForeignKeys();
            }

            List<EConstraint> sourceConstraints = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(sourceETable.getConstraints())) {
                sourceConstraints = sourceETable.getConstraints();
            }

            List<EIndex> sourceIndex = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(sourceETable.getIndices())) {
                sourceIndex = sourceETable.getIndices();
            }

            String targetCatalog = this.eTable.getCatalog();
            String targetSchema = this.eTable.getSchema();
            String targetName = this.eTable.getName();
            String targetComment = this.eTable.getComment();
            Map<String, String> targetAttr = this.eTable.getAttribute();

            List<EColumn> targetColumnList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.eTable.getColumnList())) {
                targetColumnList = this.eTable.getColumnList();
            }

            EPrimaryKey targetPrimaryKey = new EPrimaryKey();
            if (this.eTable.getPrimaryKey() != null) {
                targetPrimaryKey = this.eTable.getPrimaryKey();
            }

            List<EForeignKey> targetForeignKeys = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.eTable.getForeignKeys())) {
                targetForeignKeys = this.eTable.getForeignKeys();
            }

            List<EConstraint> targetConstraints = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.eTable.getConstraints())) {
                targetConstraints = this.eTable.getConstraints();
            }

            List<EIndex> targetIndex = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.eTable.getIndices())) {
                targetIndex = this.eTable.getIndices();
            }

            // table rename
            if (!sourceName.equals(targetName)) {
                List<String> sqlString = provider.tableRename(triggerContext, sourceCatalog, sourceSchema, sourceName, targetName);
                actions.add(new Action(sqlString, sourceCatalog, sourceSchema, sourceName));
                sourceName = targetName;
            }

            ETable eTableForAlter = new ETable();
            // table reset comment
            if (sourceComment == null) {
                sourceComment = "";
            }
            if (targetComment == null) {
                targetComment = "";
            }
            if (!sourceComment.equals(targetComment)) {
                eTableForAlter.setComment(targetComment);
            }

            // table reset attr
            if (EditorUtils.testAttribute(sourceAttr, targetAttr)) {
                Map<String, Map<String, String>> diffMap = this.diffAttribute(sourceAttr, targetAttr);
                eTableForAlter.setAttribute(diffMap.get("change"));
            }

            // gen table ddl
            List<String> tableAlterBeFore = provider.tableAlterBeFore(triggerContext, sourceCatalog, sourceSchema, sourceName, eTableForAlter);
            List<String> tableAlter = provider.tableAlter(triggerContext, sourceCatalog, sourceSchema, sourceName, eTableForAlter, sourceAttr);
            if (CollectionUtils.isNotEmpty(tableAlterBeFore)) {
                tableAlterBeFore.addAll(tableAlter);
                tableAlter = tableAlterBeFore;
            }
            if (CollectionUtils.isNotEmpty(tableAlter)) {
                actions.add(new Action(tableAlter, sourceCatalog, sourceSchema, sourceName));
            }

            // column change
            if (EditorUtils.testList(sourceColumnList, targetColumnList)) {
                Map<String, EColumn> sourceMap = sourceColumnList.stream().collect(Collectors.toMap(EColumn::getName, EColumn -> EColumn));
                Map<String, EColumn> targetMap = targetColumnList.stream()
                    .collect(Collectors.toMap(column -> column.getSource() != null ? column.getSource() : UUID.randomUUID().toString(), column -> column));
                Map<String, Integer> renameLocationMap = new HashMap<>();
                for (EColumn eColumn : sourceColumnList) {
                    EColumn targetEColumn = targetMap.get(eColumn.getName());
                    if (!targetMap.containsKey(eColumn.getName())) {
                        // drop source
                        List<String> sqlDropStrings = provider.dropColumn(triggerContext, sourceCatalog, sourceSchema, sourceName, eColumn);
                        if (renameLocationMap.containsKey(eColumn.getName())) {
                            // drop must precede modify and rename
                            actions.add(renameLocationMap.get(eColumn.getName()), new Action(sqlDropStrings, sourceCatalog, sourceSchema, sourceName));
                        } else {
                            actions.add(new Action(sqlDropStrings, sourceCatalog, sourceSchema, sourceName));
                        }

                    } else if (eColumn.testChanged(targetEColumn)) {
                        // modify source to target
                        String targetEColumnName = targetEColumn.getName();
                        if (!eColumn.getName().equals(targetEColumnName)) {
                            // rename
                            List<String> sqlColumnRename = provider.columnRename(triggerContext, sourceCatalog, sourceSchema, sourceName, eColumn, targetEColumnName);
                            actions.add(new Action(sqlColumnRename, sourceCatalog, sourceSchema, sourceName));

                            // name change, need renew info
                            eColumn.setName(targetEColumnName);

                            // record rename action location
                            renameLocationMap.put(targetEColumnName, actions.size() - 1);
                        }

                        if (eColumn.testChangedExcept(targetEColumn)) { // To prevent the front-end from passing values to the eColumn.source
                            List<String> sqlChangeStrings = provider
                                .columnChange(triggerContext, sourceCatalog, sourceSchema, sourceName, eColumn, targetEColumn, null, sourceETable);
                            actions.add(new Action(sqlChangeStrings, sourceCatalog, sourceSchema, sourceName));
                        }
                    }
                }

                for (EColumn eColumn : targetColumnList) {
                    if (!sourceMap.containsKey(eColumn.getSource())) {
                        // add target
                        List<String> sqlAddStrings = provider.addColumn(triggerContext, sourceCatalog, sourceSchema, sourceName, eColumn, sourceETable);
                        actions.add(new Action(sqlAddStrings, sourceCatalog, sourceSchema, sourceName));
                    }
                }
            }

            // pk change
            if (sourcePrimaryKey.testChanged(targetPrimaryKey)) {
                if (CollectionUtils.isNotEmpty(sourcePrimaryKey.getColumnList())) {
                    List<String> dropSqlString = provider.dropPrimaryKey(triggerContext, sourceCatalog, sourceSchema, sourceName, sourcePrimaryKey);
                    actions.add(new Action(dropSqlString, sourceCatalog, sourceSchema, sourceName));
                }

                if (CollectionUtils.isNotEmpty(targetPrimaryKey.getColumnList())) {
                    List<String> addSqlString = provider.createPrimaryKey(triggerContext, sourceCatalog, sourceSchema, sourceName, targetPrimaryKey);
                    actions.add(new Action(addSqlString, sourceCatalog, sourceSchema, sourceName));
                }
            }

            // foreign change
            if (EditorUtils.testList(sourceForeignKeys, targetForeignKeys)) {
                ForeignKeyChangeUtil.genChangeSql(sourceForeignKeys, targetForeignKeys, provider, triggerContext, sourceCatalog, sourceSchema, sourceName, actions);
            }

            // constraint change
            if (EditorUtils.testList(sourceConstraints, targetConstraints)) {
                ConstraintChangeUtil.genChangeSql(sourceConstraints, targetConstraints, provider, triggerContext, sourceCatalog, sourceSchema, sourceName, actions);
            }

            // index change
            if (EditorUtils.testList(sourceIndex, targetIndex)) {
                Map<String, EIndex> sourceMap = sourceIndex.stream().collect(Collectors.toMap(EIndex::getName, EIndex -> EIndex));
                Map<String, EIndex> targetMap = targetIndex.stream()
                    .collect(Collectors.toMap(index -> index.getSource() != null ? index.getSource() : UUID.randomUUID().toString(), index -> index));
                for (EIndex eIndex : sourceIndex) {
                    EIndex targetEIndex = targetMap.get(eIndex.getName());
                    if (!targetMap.containsKey(eIndex.getName())) {
                        // drop source
                        List<String> sqlDropStrings = provider.indexDropColumn(triggerContext, sourceCatalog, sourceSchema, sourceName, eIndex, new ArrayList<>());
                        actions.add(new Action(sqlDropStrings, sourceCatalog, sourceSchema, sourceName));
                    } else if (eIndex.testChanged(targetEIndex) && eIndex.testChangedExcept(targetEIndex)) {
                        // modify source to target, index not have modify, so rebuild
                        List<String> dropSqlString = provider.indexDropColumn(triggerContext, sourceCatalog, sourceSchema, sourceName, eIndex, new ArrayList<>());
                        actions.add(new Action(dropSqlString, sourceCatalog, sourceSchema, sourceName));

                        if (CollectionUtils.isNotEmpty(targetEIndex.getColumnList())) {
                            List<String> addSqlString = provider.indexAddColumn(triggerContext, sourceCatalog, sourceSchema, sourceName, targetEIndex, new ArrayList<>());
                            actions.add(new Action(addSqlString, sourceCatalog, sourceSchema, sourceName));
                        }
                    }
                }

                for (EIndex eIndex : targetIndex) {
                    if (!sourceMap.containsKey(eIndex.getSource())) {
                        // add target
                        List<String> sqlAddStrings = provider.indexAddColumn(triggerContext, sourceCatalog, sourceSchema, sourceName, eIndex, new ArrayList<>());
                        actions.add(new Action(sqlAddStrings, sourceCatalog, sourceSchema, sourceName));
                    }
                }
            }
        }
        super.actions.clear();
        super.actions = actions;
        return actions;
    }

    @Override
    public String catalog() {
        return this.eTable.getCatalog();
    }

    @Override
    public void changeCatalog(String newCatalog) {
        if (StringUtils.equals(this.eTable.getCatalog(), newCatalog)) {
            return;
        }
        this.eTable.setCatalog(newCatalog);
    }

    @Override
    public String schema() {
        return this.eTable.getSchema();
    }

    @Override
    public void changeSchema(String newSchema) {
        if (StringUtils.equals(this.eTable.getSchema(), newSchema)) {
            return;
        }
        this.eTable.setSchema(newSchema);

    }

    @Override
    public void rename(String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new NullPointerException("new name is null.");
        }

        SqlBuilder provider = this.context.getTarDsInfo().getProvider();
        if (!provider.supportTableRename()) {
            throw new UnsupportedOperationException("provider " + provider.getClass().getName() + " table rename Unsupported.");
        }

        if (Objects.equals(this.eTable.getName(), newName)) {
            return;
        }

        this.eTable.setName(newName);

    }

    @Override
    public void setComment(String comment) {
        if (Objects.equals(eTable.getComment(), comment)) {
            return;
        }

        this.eTable.setComment(comment);
    }

    @Override
    public PrimaryEditor getPrimaryEditor() {
        if (this.eTable.getPrimaryKey() == null) {
            return null;
        }

        return new PrimaryEditorImpl(this.context, this.eTable, this.eTable.getPrimaryKey());
    }

    @Override
    public void buildDrop() {
        super.triggerDropTable(this.context.getSrcDsInfo(), this.context.getTarDsInfo(), this.eTable.getCatalog(), this.eTable.getSchema(), this.eTable.getName(), this.eTable);
    }

    @Override
    public PrimaryEditor createPrimaryEditor(String primaryName, List<String> columns) {
        if (columns.isEmpty()) {
            throw new ConflictException("primaryKey must be at least one column.");
        }

        EPrimaryKey primaryKey = this.eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            throw new ConflictException("primaryKey already exists.");
        }

        List<String> tableAllColumns = this.eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList());
        for (String column : columns) {
            if (!tableAllColumns.contains(column)) {
                throw new NoColumnException("column '" + column + "' not found.");
            }
        }

        primaryKey = new EPrimaryKey();
        primaryKey.setPrimaryKeyName(primaryName);
        primaryKey.setColumnList(columns);
        this.eTable.setPrimaryKey(primaryKey);

        return new PrimaryEditorImpl(this.context, this.eTable, primaryKey);
    }

    @Override
    public ColumnEditor addColumn(String columnName, String dbType, Boolean nullable, Long length, //
                                  Integer numericPrecision, Integer numericScale, boolean unsigned, Integer datetimePrecision, //
                                  String defaultValue, boolean defaultIsFunc, //
                                  boolean autoGenerate, String comment) {
        List<String> columnNames = this.eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList());
        if (columnNames.contains(columnName)) {
            throw new ConflictException("column '" + columnName + " already exists.");
        }

        EColumn eColumn = new EColumn(columnName);
        eColumn.setName(columnName);
        eColumn.setDbType(dbType);
        eColumn.setNullable(nullable);
        eColumn.setLength(length);
        eColumn.setNumericPrecision(numericPrecision);
        eColumn.setNumericScale(numericScale);
        eColumn.setNumericUnsigned(unsigned);
        eColumn.setDatetimePrecision(datetimePrecision);
        eColumn.setDefaultValue(defaultValue);
        eColumn.setDefaultValueIsFunc(defaultIsFunc);
        eColumn.setAutoGenerate(autoGenerate);
        eColumn.setComment(comment);
        this.eTable.getColumnList().add(eColumn);

        return new ColumnEditorImpl(this.context, this.eTable, eColumn);
    }

    @Override
    public ConstraintEditor addConstraint(String constraintName, EConstraintType type, String expression, List<String> columns) {
        List<String> constraintNames = this.eTable.getConstraints().stream().map(EConstraint::getName).collect(Collectors.toList());
        if (constraintNames.contains(constraintName)) {
            throw new ConflictException("constraint '" + constraintName + " already exists.");
        }

        EConstraint eConstraint = new EConstraint();
        eConstraint.setName(constraintName);
        eConstraint.setExpression(expression);
        eConstraint.setType(type);
        if (columns != null) {
            eConstraint.setColumnList(columns);
        }
        this.eTable.getConstraints().add(eConstraint);
        return new ConstraintEditorImpl(this.context, this.eTable, eConstraint);
    }

    @Override
    public PartitionEditor addPartition(String type, String expression, String subType, String subExpression) {

        EPartition partition = new EPartition();
        partition.setPartitionMethod(type);
        partition.setPartitionExpression(expression);
        partition.setSubPartitionMethod(subType);
        partition.setSubPartitionExpression(subExpression);
        this.eTable.setPartition(partition);

        return new PartitionEditorImpl(this.context, this.eTable, partition);

    }

    @Override
    public ColumnEditor getColumn(String columnName) {
        EColumn columnInfo = this.eTable.getColumnList().stream().filter(eColumn -> eColumn.getName().equals(columnName)).findFirst().orElse(null);
        if (columnInfo == null) {
            return null;
        }

        return new ColumnEditorImpl(this.context, this.eTable, columnInfo);
    }

    @Override
    public IndexEditor getIndexEditor(String indexName) {
        EIndex eIndex = this.eTable.getIndices().stream().filter(index -> index.getName().equals(indexName)).findFirst().orElse(null);

        if (eIndex == null) {
            return null;
        }

        return new IndexEditorImpl(this.context, this.eTable, eIndex);
    }

    @Override
    public IndexEditor addIndexEditor(String indexName, EIndexType indexType, List<String> columnName) {
        List<String> indexNames = this.eTable.getIndices().stream().map(EIndex::getName).collect(Collectors.toList());
        if (indexNames.contains(indexName)) {
            String name = "'" + this.eTable.getName() + "'";
            if (StringUtils.isNotBlank(this.eTable.getSchema())) {
                name = "'" + this.eTable.getSchema() + "'." + name;
            }

            throw new ConflictException("index '" + indexName + "' of " + name + " already exists.");
        }

        EIndex eIndex = new EIndex(indexName);
        eIndex.setName(indexName);
        eIndex.setType(indexType);
        this.eTable.getIndices().add(eIndex);

        IndexEditorImpl editorBuilder = new IndexEditorImpl(this.context, this.eTable, eIndex);
        editorBuilder.addColumn(columnName.toArray(new String[0]));
        return editorBuilder;
    }

    @Override
    public ForeignKeyEditor getForeignKeyEditor(String fkName) {
        EForeignKey eForeignKey = this.eTable.getForeignKeys().stream().filter(foreignKey -> foreignKey.getName().equals(fkName)).findFirst().orElse(null);

        if (eForeignKey == null) {
            return null;
        }

        return new ForeignKeyEditorImpl(this.context, this.eTable, eForeignKey);
    }

    @Override
    public ForeignKeyEditor addForeignKeyEditor(String fkName, String referenceSchema, String referenceTable, RdbForeignKeyRule updateRule, RdbForeignKeyRule deleteRule,
                                                Map<String, String> referenceMapping) {
        List<String> indexNames = this.eTable.getForeignKeys().stream().map(EForeignKey::getName).collect(Collectors.toList());
        if (indexNames.contains(fkName)) {
            throw new ConflictException("foreignKey '" + fkName + " already exists.");
        }

        EForeignKey fk = new EForeignKey();
        fk.setName(fkName);
        fk.setReferenceSchema(referenceSchema);
        fk.setReferenceTable(referenceTable);
        fk.setUpdateRule(updateRule == null ? RdbForeignKeyRule.NoAction : updateRule);
        fk.setDeleteRule(deleteRule == null ? RdbForeignKeyRule.NoAction : deleteRule);

        if (referenceMapping != null) {
            for (String columnName : referenceMapping.keySet()) {
                String refCol = referenceMapping.get(columnName);
                fk.getColumnList().add(columnName);
                fk.getReferenceMapping().put(columnName, refCol);
            }
        }

        this.eTable.getForeignKeys().add(fk);
        return new ForeignKeyEditorImpl(this.context, this.eTable, fk);
    }

    @Override
    public List<Action> getActions() { return this.actions; }

    @Override
    public TableBuilder createBuilder() {
        return new TableBuilderImpl(this);
    }

    @Override
    public List<Action> buildCreate(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo dsInfo = buildDsInfo(targetBuilder, mainVersion);
        TriggerCreate triggerCreate = (srcInfo, tarInfo, table) -> {
            triggerTableCreate(srcInfo, tarInfo, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eTable);
        };
        return doBuildCreate(dsInfo, false, triggerCreate);
    }

    private DsInfo buildDsInfo(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo dsInfo = new DsInfo(targetBuilder, targetBuilder.getDataSourceType(), mainVersion);
        dsInfo.setTypeInfoList(this.context.getTarDsInfo().getTypeInfoList());
        dsInfo.setCharsetAndCollation(this.context.getTarDsInfo().getCharsetAndCollation());
        return dsInfo;
    }

    @Override
    public List<Action> buildAddColumn(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            ExecuteGenerateSql sql = (dsInfo, context, eTable, eColumn) -> dsInfo.getProvider()
                .addColumn(context, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eColumn, eTable);
            processAndTriggerColumnBuild(context.getSrcDsInfo(), tarInfo1, eTable, sql);
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);
        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildModifyColumn(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            ExecuteGenerateSql sql = (dsInfo, context, eTable, eColumn) -> dsInfo.getProvider()
                .columnChange(context, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eColumn, eColumn, new LinkedList<>(), eTable);
            processAndTriggerColumnBuild(srcInfo, tarInfo1, eTable, sql);
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);

        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildChangeColumn(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            for (EColumn eColumn : table.getColumnList()) {
                if (eColumn.getInitData() != null && !eColumn.getInitData().getName().equals(eColumn.getName())) {
                    triggerColumnRename(srcInfo, tarInfo1, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eColumn.getInitData(), eColumn.getName());
                }
            }

            ExecuteGenerateSql sql = (dsInfo, context, eTable, eColumn) -> dsInfo.getProvider()
                .columnChange(context, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eColumn, eColumn, new LinkedList<>(), eTable);
            processAndTriggerColumnBuild(srcInfo, tarInfo1, eTable, sql);
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);

        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildDropColumn(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            ExecuteGenerateSql sql = (dsInfo, context, eTable, eColumn) -> dsInfo.getProvider()
                .dropColumn(context, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eColumn);
            processAndTriggerColumnBuild(srcInfo, tarInfo1, eTable, sql);
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);

        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildRenameColumn(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        this.doBuildCreate(tarInfo, false, (srcInfo, tarInfo1, table) -> {
            for (EColumn eColumn : table.getColumnList()) {
                if (eColumn.getInitData() != null) {
                    triggerColumnRename(srcInfo, tarInfo1, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eColumn.getInitData(), eColumn.getName());
                }
            }

        });
        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildRenameTableName(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            triggerTableRename(srcInfo, tarInfo1, eTable.getCatalog(), eTable.getSchema(), eTable.getInitData().getName(), eTable.getName());
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);
        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildTruncateTable(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            triggerTruncateTable(srcInfo, tarInfo1, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eTable);
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);
        return new ArrayList<>(this.context.getActions());
    }

    @Override
    public List<Action> buildDropTable(SqlBuilder targetBuilder, MainVersion mainVersion) {
        DsInfo tarInfo = buildDsInfo(targetBuilder, mainVersion);
        validBuilder(tarInfo.getProvider());
        TriggerCreate triggerCreate = (srcInfo, tarInfo1, table) -> {
            triggerDropTable(srcInfo, tarInfo1, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), eTable);
        };
        this.doBuildCreate(tarInfo, false, triggerCreate);
        return new ArrayList<>(this.context.getActions());
    }

    private List<Action> doBuildCreate(DsInfo tarInfo, boolean keepActions, TriggerCreate triggerCreate) {
        validBuilder(tarInfo.getProvider());
        DsInfo srcDsInfo = this.context.getSrcDsInfo();

        List<Action> cacheActions = new ArrayList<>(this.context.getActions());
        try {
            if (!keepActions) {
                this.context.getActions().clear();
            }

            triggerCreate.triggerCreate(srcDsInfo, tarInfo, this.eTable);

            return new ArrayList<>(this.context.getActions());
        } finally {
            if (keepActions) {
                this.context.getActions().clear();
                this.context.getActions().addAll(cacheActions);
            }
        }
    }

    private void validBuilder(SqlBuilder targetBuilder) {
        if (StringUtils.isBlank(this.eTable.getName())) {
            throw new NullPointerException("table name is null.");
        }

        //for schema less obj,e.g.,mq topic
        //        if (this.eTable.getColumnList().isEmpty()) {
        //            throw new NullPointerException("table '" + this.eTable.getName() + "' contains at least one column.");
        //        }

        if (targetBuilder == null) {
            throw new NullPointerException("targetBuilder is not ready.");
        }
    }

    private Map<String, Map<String, String>> diffAttribute(Map<String, String> source, Map<String, String> target) {
        Map<String, Map<String, String>> diffMap = new HashMap<>();
        diffMap.put("change", new HashMap<>());
        diffMap.put("add", new HashMap<>());
        diffMap.put("drop", new HashMap<>());

        for (String sourceKey : source.keySet()) {
            if (target.containsKey(sourceKey)) {
                String targetValue = target.get(sourceKey);
                if (!Objects.equals(source.get(sourceKey), targetValue)) {
                    diffMap.get("change").put(sourceKey, targetValue);
                }
            } else {
                diffMap.get("drop").put(sourceKey, source.get(sourceKey));
            }
        }

        for (String targetKey : target.keySet()) {
            if (!source.containsKey(targetKey)) {
                diffMap.get("add").put(targetKey, target.get(targetKey));
            }
        }

        return diffMap;
    }

    @Override
    public List<String> getColumnNames() { return this.eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList()); }

    @Override
    public List<String> getForeignKeys() { return this.eTable.getForeignKeys().stream().map(EForeignKey::getName).collect(Collectors.toList()); }

    @Override
    public List<String> getIndexes() { return this.eTable.getIndices().stream().map(EIndex::getName).collect(Collectors.toList()); }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.analysis.behavior;

import java.util.*;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.analysis.reference.DmResourceRegistry;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class DmBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    DmBehaviorParserVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        DmStatementBehaviorVisitor visitor = new DmStatementBehaviorVisitor(levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class DmStatementBehaviorVisitor extends DmSqlParserBaseVisitor<Void> {
    private static final Set<String>         DATE_PART_FUNCTIONS     = Set.of("BIGDATEDIFF", "DATEADD", "DATEDIFF", "DATEPART", "DATE_PART", "TIMESTAMPADD", "TIMESTAMPDIFF");
    private static final Set<String>         JSON_OBJECT_TYPES       = Set.of("JDOM_T", "JSON_ARRAY_T", "JSON_ELEMENT_T", "JSON_OBJECT_T", "JSON_SCALAR_T");
    private static final Set<String>         BUILT_IN_DATA_TYPES     = Set
        .of("BFILE", "BIGINT", "BINARY", "BIT", "BLOB", "BOOL", "BOOLEAN", "BYTE", "CHAR", "CHARACTER", "CLOB", "DATE", "DATETIME", "DEC", "DECIMAL", "DOUBLE", "FLOAT", "IMAGE", "INT", "INTEGER", "LONG", "LONGVARBINARY", "LONGVARCHAR", "NCHAR", "NUMBER", "NUMERIC", "NVARCHAR", "NVARCHAR2", "PLBOOL", "PLS_INTEGER", "RAW", "REAL", "ROWID", "SMALLINT", "TEXT", "TIME", "TIMESTAMP", "TINYINT", "VARBINARY", "VARCHAR", "VARCHAR2", "XMLTYPE");

    private final Map<UmiTypes, Object>      levels;
    private final RdbBehaviorObjectFactory   objects;
    private final StatementBehavior          behavior                = new StatementBehavior();
    private final List<String>               schemaScopes            = new ArrayList<>();
    private final Deque<Set<String>>         blockLocals             = new ArrayDeque<>();
    private final Deque<Map<String, String>> blockLocalTypes         = new ArrayDeque<>();
    private final Deque<NameParts>           packageScopes           = new ArrayDeque<>();
    private final Deque<Set<String>>         packageFunctionMembers  = new ArrayDeque<>();
    private final Deque<Set<String>>         packageProcedureMembers = new ArrayDeque<>();
    private final Deque<NameParts>           javaParentTypes         = new ArrayDeque<>();
    private int                              javaClassDepth;

    DmStatementBehaviorVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.levels = levels;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitSelectStatement(DmSqlParser.SelectStatementContext ctx) {
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.SELECT);
        }
        Set<String> localFunctions = withFunctionNames(ctx);
        if (!localFunctions.isEmpty()) {
            Set<String> localNames = new HashSet<>();
            if (!blockLocals.isEmpty()) {
                localNames.addAll(blockLocals.peek());
            }
            localNames.addAll(localFunctions);
            blockLocals.push(localNames);
        }
        try {
            addFunctionCalls(ctx);
            for (BehaviorObject sequence : sequenceSources(ctx)) {
                add(SplitQueryType.SELECT, BehaviorAction.READ, sequence);
            }
            for (BehaviorObject source : tableSources(ctx)) {
                add(SplitQueryType.SELECT, BehaviorAction.READ, source);
            }
            for (BehaviorObject source : lockSources(ctx)) {
                add(SplitQueryType.SELECT, BehaviorAction.LOCK, source);
            }
            addTypePredicateReads(ctx);
            for (DmSqlParser.TablePrimaryContext table : descendants(ctx, DmSqlParser.TablePrimaryContext.class)) {
                if (table.qualifiedName() == null) {
                    continue;
                }
                NameParts tableName = NameParts.from(table.qualifiedName());
                if (table.tableIndexClause() != null) {
                    String indexName = NameParts.clean(table.tableIndexClause().identifier().getText());
                    NameParts qualifiedIndex = new NameParts(tableName.catalog(), tableName.schema(), indexName);
                    add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Index, table.tableIndexClause().identifier(), qualifiedIndex));
                }
                for (DmSqlParser.PartitionExtensionClauseContext extension : table.partitionExtensionClause()) {
                    if (extension.identifier() == null) {
                        continue;
                    }
                    List<String> names = new ArrayList<>();
                    String catalog = tableName.catalog();
                    if (catalog == null && levels != null && levels.get(UmiTypes.Catalog) != null) {
                        catalog = levels.get(UmiTypes.Catalog).toString();
                    }
                    String schema = tableName.schema();
                    if (schema == null && levels != null && levels.get(UmiTypes.Schema) != null) {
                        schema = levels.get(UmiTypes.Schema).toString();
                    }
                    if (catalog != null) {
                        names.add(catalog);
                    }
                    if (schema != null) {
                        names.add(schema);
                    }
                    names.add(tableName.name());
                    names.add(NameParts.clean(extension.identifier().getText()));
                    add(SplitQueryType.SELECT, BehaviorAction.READ, objects.object(TargetType.Partition, extension.identifier(), names));
                }
            }
            return null;
        } finally {
            if (!localFunctions.isEmpty()) {
                blockLocals.pop();
            }
        }
    }

    private Set<String> withFunctionNames(ParseTree ctx) {
        Set<String> names = new HashSet<>();
        for (DmSqlParser.WithFunctionDefinitionContext function : descendants(ctx, DmSqlParser.WithFunctionDefinitionContext.class)) {
            NameParts name = NameParts.from(function.qualifiedName());
            if (name.name() != null) {
                List<String> parts = new ArrayList<>();
                if (name.catalog() != null) {
                    parts.add(name.catalog());
                }
                if (name.schema() != null) {
                    parts.add(name.schema());
                }
                parts.add(name.name());
                names.add(String.join(".", parts).toLowerCase(Locale.ROOT));
            }
        }
        return names;
    }

    private Set<String> pushWithFunctionNames(ParseTree tree) {
        Set<String> localFunctions = withFunctionNames(tree);
        if (localFunctions.isEmpty()) {
            return localFunctions;
        }
        Set<String> localNames = new HashSet<>();
        if (!blockLocals.isEmpty()) {
            localNames.addAll(blockLocals.peek());
        }
        localNames.addAll(localFunctions);
        blockLocals.push(localNames);
        return localFunctions;
    }

    private void popWithFunctionNames(Set<String> localFunctions) {
        if (!localFunctions.isEmpty()) {
            blockLocals.pop();
        }
    }

    @Override
    public Void visitInsertStatement(DmSqlParser.InsertStatementContext ctx) {
        Set<String> localFunctions = pushWithFunctionNames(ctx);
        try {
            List<BehaviorObject> sources = sequenceSources(ctx);
            addTableSources(sources, ctx);
            for (DmSqlParser.InsertTableSourceContext source : descendants(ctx, DmSqlParser.InsertTableSourceContext.class)) {
                addObject(sources, object(TargetType.Table, source.qualifiedName(), schemaScoped(NameParts.from(source.qualifiedName()))));
            }
            if (ctx.singleInsertStatement() != null) {
                addInsertTarget(ctx.singleInsertStatement().insertTarget(), sources);
                addErrorLoggingTarget(SplitQueryType.INSERT, ctx.singleInsertStatement().dmlErrorLoggingClause(), sources);
            }
            for (DmSqlParser.MultiInsertIntoContext into : descendants(ctx, DmSqlParser.MultiInsertIntoContext.class)) {
                addInsertTarget(into.insertTarget(), sources);
            }
            addFunctionCalls(ctx);
            return null;
        } finally {
            popWithFunctionNames(localFunctions);
        }
    }

    @Override
    public Void visitUpdateStatement(DmSqlParser.UpdateStatementContext ctx) {
        Set<String> localFunctions = pushWithFunctionNames(ctx);
        try {
            List<BehaviorObject> sources = sequenceSources(ctx);
            Set<String> ctes = cteNames(ctx);
            addTableSources(sources, ctx.withClause(), ctes);
            addTableSources(sources, ctx.fromClause(), ctes);
            addTableSources(sources, ctx.assignmentList(), ctes);
            addTableSources(sources, ctx.whereClause(), ctes);
            List<DmSqlParser.TableSourceContext> updateTargets = ctx.updateTargetList().tableSource();
            for (int index = 1; index < updateTargets.size(); index++) {
                addTableSources(sources, updateTargets.get(index), ctes);
            }
            DmSqlParser.TablePrimaryContext target = updateTargets.get(0).tablePrimary();
            if (target.qualifiedName() != null) {
                for (BehaviorObject object : tableAccessObjects(target)) {
                    add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object, sources);
                }
            } else {
                for (DmSqlParser.TablePrimaryContext table : descendants(target, DmSqlParser.TablePrimaryContext.class)) {
                    if (table.qualifiedName() != null) {
                        for (BehaviorObject object : tableAccessObjects(table)) {
                            add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object, sources);
                        }
                    }
                }
            }
            addErrorLoggingTarget(SplitQueryType.UPDATE, ctx.dmlErrorLoggingClause(), sources);
            addFunctionCalls(ctx);
            return null;
        } finally {
            popWithFunctionNames(localFunctions);
        }
    }

    @Override
    public Void visitDeleteStatement(DmSqlParser.DeleteStatementContext ctx) {
        Set<String> localFunctions = pushWithFunctionNames(ctx);
        try {
            List<BehaviorObject> sources = tableSources(ctx.deleteMultiTableClause());
            Set<String> ctes = cteNames(ctx);
            addTableSources(sources, ctx.withClause(), ctes);
            addTableSources(sources, ctx.whereClause(), ctes);
            DmSqlParser.TablePrimaryContext target = ctx.deleteTarget().tablePrimary();
            if (target.qualifiedName() != null) {
                for (BehaviorObject object : tableAccessObjects(target)) {
                    add(SplitQueryType.DELETE, BehaviorAction.DELETE, object, sources);
                }
            } else {
                for (DmSqlParser.QualifiedNameContext name : deleteTargetNames(ctx)) {
                    add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, name, schemaScoped(NameParts.from(name))), sources);
                }
            }
            addErrorLoggingTarget(SplitQueryType.DELETE, ctx.dmlErrorLoggingClause(), sources);
            addFunctionCalls(ctx);
            return null;
        } finally {
            popWithFunctionNames(localFunctions);
        }
    }

    @Override
    public Void visitMergeStatement(DmSqlParser.MergeStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx);
        if (ctx.mergeIntoTarget().qualifiedName() != null) {
            DmSqlParser.QualifiedNameContext name = ctx.mergeIntoTarget().qualifiedName();
            if (ctx.mergeIntoTarget().partitionExtensionClause().isEmpty()) {
                add(SplitQueryType.MERGE, BehaviorAction.MERGE, object(TargetType.Table, name, schemaScoped(NameParts.from(name))), sources);
            } else {
                for (DmSqlParser.PartitionExtensionClauseContext partition : ctx.mergeIntoTarget().partitionExtensionClause()) {
                    add(SplitQueryType.MERGE, BehaviorAction.MERGE, partitionObject(name, partition), sources);
                }
            }
        } else {
            for (BehaviorObject target : tableSources(ctx.mergeIntoTarget().selectStatement())) {
                add(SplitQueryType.MERGE, BehaviorAction.MERGE, target, sources);
            }
        }
        addErrorLoggingTarget(SplitQueryType.MERGE, ctx.dmlErrorLoggingClause(), sources);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitFlashbackStatement(DmSqlParser.FlashbackStatementContext ctx) {
        for (DmSqlParser.QualifiedNameContext name : ctx.qualifiedName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RESTORE, object(TargetType.Table, name, NameParts.from(name)));
        }
        return null;
    }

    @Override
    public Void visitRefreshMaterializedViewStatement(DmSqlParser.RefreshMaterializedViewStatementContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.Materialized, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitAdminStatement(DmSqlParser.AdminStatementContext ctx) {
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.ADMIN);
        }
        if (ctx.CHECKPOINT() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.CHECKPOINT, objects.instanceObject(TargetType.Instance, ctx.CHECKPOINT().getSymbol()));
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.DataWatcherAdminProcedureContext dataWatcher = ctx.dataWatcherAdminProcedure();
        if (dataWatcher != null) {
            List<BehaviorObject> targets = new ArrayList<>();
            if (dataWatcher.SP_SET_OGUID() != null) {
                addObject(targets, objects.instanceObject(TargetType.ConfigKey, dataWatcher, "OGUID"));
            } else {
                Token instance = null;
                if (dataWatcher.SP_CLEAR_ARCH_SEND_INFO() != null && ctx.routineArgumentList() != null) {
                    DmSqlParser.RoutineArgumentContext argument = ctx.routineArgumentList().routineArgument().get(0);
                    if (argument.expression() != null) {
                        instance = stringArgument(List.of(argument.expression()), 0);
                    }
                }
                if (instance == null) {
                    addObject(targets, objects.instanceObject(TargetType.Replication, dataWatcher));
                } else {
                    addObject(targets, objects.instanceObject(TargetType.Replication, instance, stringValue(instance)));
                }
            }
            String procedureName = NameParts.clean(dataWatcher.getText());
            add(SplitQueryType.ADMIN, BehaviorAction.CALL, object(TargetType.Procedure, dataWatcher, new NameParts(null, null, procedureName)), targets);
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.AlterDatabaseActionContext alterDatabase = ctx.alterDatabaseAction();
        if (alterDatabase != null && alterDatabase.LOGFILE() != null) {
            if (alterDatabase.RESIZE() != null) {
                add(SplitQueryType.ADMIN, BehaviorAction.ALTER, fileObject(alterDatabase.backupFilePath(0).getStart()));
            } else if (alterDatabase.ADD() != null) {
                for (DmSqlParser.AlterDatabaseFileItemContext item : alterDatabase.alterDatabaseFileItem()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.CREATE, fileObject(item.backupFilePath().getStart()));
                }
            } else if (alterDatabase.DROP() != null) {
                add(SplitQueryType.ADMIN, BehaviorAction.DROP, fileObject(alterDatabase.backupFilePath(0).getStart()));
            } else if (alterDatabase.RENAME() != null) {
                List<DmSqlParser.BackupFilePathContext> sources = new ArrayList<>();
                List<DmSqlParser.BackupFilePathContext> targets = new ArrayList<>();
                int toIndex = alterDatabase.TO().getSymbol().getTokenIndex();
                for (DmSqlParser.BackupFilePathContext path : alterDatabase.backupFilePath()) {
                    if (path.getStart().getTokenIndex() < toIndex) {
                        sources.add(path);
                    } else {
                        targets.add(path);
                    }
                }
                for (int i = 0; i < sources.size(); i++) {
                    add(SplitQueryType.ADMIN, BehaviorAction.RENAME, fileObject(sources.get(i).getStart()), List.of(fileObject(targets.get(i).getStart())));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        if (alterDatabase != null && alterDatabase.ADD() == null && alterDatabase.MODIFY() == null && alterDatabase.DELETE() == null) {
            add(SplitQueryType.ADMIN, BehaviorAction.UNSAFE, objects.instanceObject(TargetType.Instance, ctx.DATABASE().getSymbol()));
            addFunctionCalls(ctx);
            return null;
        }
        if (alterDatabase != null && alterDatabase.ARCHIVELOG() != null && alterDatabase.backupFilePath(0).getStart().getType() == DmSqlParser.STRING) {
            Token configuration = alterDatabase.backupFilePath(0).getStart();
            Token type = archiveConfigurationValue(configuration, "TYPE");
            Token destination = archiveConfigurationValue(configuration, "DEST");
            if (destination != null) {
                BehaviorObject archive;
                if (type != null && type.getText().equalsIgnoreCase("LOCAL")) {
                    archive = fileObject(destination);
                } else {
                    archive = objects.instanceObject(TargetType.Replication, destination, destination.getText());
                }
                List<BehaviorObject> targets = new ArrayList<>();
                Token timer = archiveConfigurationValue(configuration, "ARCH_TIMER_NAME");
                if (timer != null) {
                    addObject(targets, objects.instanceObject(TargetType.ConfigKey, timer, timer.getText()));
                }
                Token incoming = archiveConfigurationValue(configuration, "INCOMING_PATH");
                if (incoming != null) {
                    addObject(targets, fileObject(incoming));
                }
                add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, archive, targets);
            }
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.BackupStatementTailContext backup = ctx.backupStatementTail();
        if (ctx.BACKUP() != null && backup != null && backup.DATABASE() != null) {
            BehaviorObject database = objects.instanceObject(TargetType.Instance, backup.DATABASE().getSymbol());
            List<BehaviorObject> targets = new ArrayList<>();
            addObject(targets, database);
            if (backup.backupFilePath() != null && backup.backupFilePath().getStart().getType() == DmSqlParser.STRING) {
                addObject(targets, fileObject(backup.backupFilePath().getStart()));
            }
            for (DmSqlParser.BackupAdminOptionContext option : backup.backupAdminOption()) {
                if (option.WITH() != null && option.BACKUPDIR() != null) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        addObject(targets, fileObject(path.getStart()));
                    }
                } else if (option.BASE() != null && option.BACKUPSET() != null && !option.backupFilePath().isEmpty()) {
                    addObject(targets, fileObject(option.backupFilePath(0).getStart()));
                }
            }
            for (DmSqlParser.BackupAdminOptionContext option : backup.backupAdminOption()) {
                if ((option.BACKUPSET() != null && option.BASE() == null || option.FORMAT() != null) && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), targets);
                } else if (option.TRACE() != null && option.FILE() != null && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), List.of(database));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.BACKUP() != null && backup != null && backup.TABLESPACE() != null && backup.qualifiedName() != null) {
            DmSqlParser.QualifiedNameContext name = backup.qualifiedName();
            BehaviorObject tablespace = objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText()));
            List<BehaviorObject> targets = new ArrayList<>();
            addObject(targets, tablespace);
            for (DmSqlParser.BackupAdminOptionContext option : backup.backupAdminOption()) {
                if (option.WITH() != null && option.BACKUPDIR() != null) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        addObject(targets, fileObject(path.getStart()));
                    }
                } else if (option.BASE() != null && option.BACKUPSET() != null && !option.backupFilePath().isEmpty()) {
                    addObject(targets, fileObject(option.backupFilePath(0).getStart()));
                }
            }
            for (DmSqlParser.BackupAdminOptionContext option : backup.backupAdminOption()) {
                if ((option.BACKUPSET() != null && option.BASE() == null || option.FORMAT() != null) && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), targets);
                } else if (option.TRACE() != null && option.FILE() != null && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), List.of(tablespace));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.BACKUP() != null && backup != null && backup.backupArchiveLogTail() != null) {
            DmSqlParser.BackupArchiveLogTailContext archive = backup.backupArchiveLogTail();
            BehaviorObject archiveLog = objects.instanceObject(TargetType.Log, archive.archiveLogKeyword());
            List<BehaviorObject> targets = new ArrayList<>();
            addObject(targets, archiveLog);
            if (archive.backupFilePath() != null && archive.backupFilePath().getStart().getType() == DmSqlParser.STRING) {
                addObject(targets, fileObject(archive.backupFilePath().getStart()));
            }
            for (DmSqlParser.BackupAdminOptionContext option : archive.backupAdminOption()) {
                if (option.WITH() != null && option.BACKUPDIR() != null) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        addObject(targets, fileObject(path.getStart()));
                    }
                } else if (option.BASE() != null && option.BACKUPSET() != null && !option.backupFilePath().isEmpty()) {
                    addObject(targets, fileObject(option.backupFilePath(0).getStart()));
                }
            }
            for (DmSqlParser.BackupAdminOptionContext option : archive.backupAdminOption()) {
                if ((option.BACKUPSET() != null && option.BASE() == null || option.FORMAT() != null) && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), targets);
                } else if (option.TRACE() != null && option.FILE() != null && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), List.of(archiveLog));
                }
            }
            if (archive.DELETE() != null && archive.INPUT() != null) {
                add(SplitQueryType.ADMIN, BehaviorAction.DELETE, archiveLog);
            }
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.BACKUP() != null && backup != null && backup.TABLE() != null && backup.qualifiedName() != null) {
            BehaviorObject table = object(TargetType.Table, backup.qualifiedName(), NameParts.from(backup.qualifiedName()));
            for (DmSqlParser.BackupAdminOptionContext option : backup.backupAdminOption()) {
                if ((option.BACKUPSET() != null || option.FORMAT() != null) && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), List.of(table));
                } else if (option.TRACE() != null && option.FILE() != null && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath(0).getStart()), List.of(table));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.ShowBackupsetTailContext show = ctx.showBackupsetTail();
        if (ctx.SHOW() != null && show != null) {
            List<BehaviorObject> inputs = new ArrayList<>();
            if (show.backupFilePath() != null) {
                addObject(inputs, fileObject(show.backupFilePath().getStart()));
            }
            if (show.showDatabaseBackupDirectoryClause() != null) {
                for (DmSqlParser.BackupFilePathContext path : show.showDatabaseBackupDirectoryClause().backupFilePath()) {
                    addObject(inputs, fileObject(path.getStart()));
                }
            }
            if (show.showBackupsetOutputClause() != null) {
                add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(show.showBackupsetOutputClause().backupFilePath().getStart()), inputs);
            } else {
                for (BehaviorObject input : inputs) {
                    add(SplitQueryType.ADMIN, BehaviorAction.READ, input);
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.RemoveStatementTailContext remove = ctx.removeStatementTail();
        if (ctx.REMOVE() != null && remove != null && remove.backupFilePath() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.DELETE, fileObject(remove.backupFilePath().getStart()));
            for (DmSqlParser.RemoveBackupsetOptionContext option : remove.removeBackupsetOption()) {
                if (option.DATABASE() != null && option.backupFilePath() != null) {
                    add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(option.backupFilePath().getStart()));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.REMOVE() != null && remove != null && remove.BACKUPSETS() != null) {
            for (DmSqlParser.RemoveBackupsetsOptionContext option : remove.removeBackupsetsOption()) {
                if (option.WITH() != null && option.BACKUPDIR() != null) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        add(SplitQueryType.ADMIN, BehaviorAction.DELETE, fileObject(path.getStart()));
                    }
                } else if (option.DATABASE() != null && !option.backupFilePath().isEmpty()) {
                    add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(option.backupFilePath(0).getStart()));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.DumpStatementTailContext dump = ctx.dumpStatementTail();
        if (ctx.DUMP() != null && dump != null && dump.backupFilePath().size() >= 2) {
            List<DmSqlParser.BackupFilePathContext> paths = dump.backupFilePath();
            List<BehaviorObject> sources = new ArrayList<>();
            addObject(sources, fileObject(paths.get(0).getStart()));
            if (dump.DATABASE() != null) {
                addObject(sources, fileObject(paths.get(paths.size() - 2).getStart()));
            }
            add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(paths.get(paths.size() - 1).getStart()), sources);
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.LoadBackupsetsTailContext load = ctx.loadBackupsetsTail();
        if (ctx.LOAD() != null && load != null && !load.backupFilePath().isEmpty()) {
            List<DmSqlParser.BackupFilePathContext> paths = load.backupFilePath();
            BehaviorObject destination = fileObject(paths.get(paths.size() - 1).getStart());
            List<BehaviorObject> sources = new ArrayList<>();
            if (load.WITH() != null) {
                int sourceStart = load.PARMS() == null ? 0 : 1;
                for (int index = sourceStart; index < paths.size() - 1; index++) {
                    addObject(sources, fileObject(paths.get(index).getStart()));
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, destination, sources);
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.CheckStatementTailContext check = ctx.checkStatementTail();
        if (ctx.CHECK() != null && check != null && check.backupFilePath() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(check.backupFilePath().getStart()));
            for (DmSqlParser.CheckBackupsetOptionContext option : check.checkBackupsetOption()) {
                if (option.backupFilePath() != null) {
                    add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(option.backupFilePath().getStart()));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.RepairStatementTailContext repair = ctx.repairStatementTail();
        if (ctx.REPAIR() != null && repair != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.REPAIR, objects.instanceObject(TargetType.Log, repair.archiveLogKeyword()));
            add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(repair.backupFilePath().getStart()));
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.MergeDatabaseTailContext merge = ctx.mergeDatabaseTail();
        if (merge != null) {
            List<DmSqlParser.BackupFilePathContext> paths = merge.backupFilePath();
            BehaviorObject databaseFile = fileObject(paths.get(0).getStart());
            List<BehaviorObject> targets = new ArrayList<>();
            addObject(targets, fileObject(paths.get(1).getStart()));
            for (DmSqlParser.MergeDatabaseOptionContext option : merge.mergeDatabaseOption()) {
                if (option.BACKUPDIR() == null) {
                    continue;
                }
                for (DmSqlParser.BackupFilePathContext path : descendants(option, DmSqlParser.BackupFilePathContext.class)) {
                    addObject(targets, fileObject(path.getStart()));
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, databaseFile, targets);
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.ConfigureStatementTailContext configure = ctx.configureStatementTail();
        if (ctx.CONFIGURE() != null && configure != null && configure.CLEAR() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, configure.CLEAR().getSymbol()));
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.CONFIGURE() != null && configure != null && configure.configureDefaultClause() != null) {
            DmSqlParser.ConfigureDefaultClauseContext clause = configure.configureDefaultClause();
            Token keyStart;
            Token keyStop;
            String keyName;
            if (clause.DEVICE() != null) {
                keyStart = clause.DEVICE().getSymbol();
                keyStop = clause.TYPE() == null ? keyStart : clause.TYPE().getSymbol();
                keyName = "DEVICE_TYPE";
            } else if (clause.TRACE() != null) {
                keyStart = clause.TRACE().getSymbol();
                keyStop = keyStart;
                keyName = "TRACE";
            } else if (clause.BACKUPDIR() != null) {
                keyStart = clause.BACKUPDIR().getSymbol();
                keyStop = keyStart;
                keyName = "BACKUPDIR";
            } else if (clause.ARCHIVEDIR() != null) {
                keyStart = clause.ARCHIVEDIR().getSymbol();
                keyStop = keyStart;
                keyName = "ARCHIVEDIR";
            } else {
                keyStart = clause.OPEN().getSymbol();
                keyStop = clause.FILES().getSymbol();
                keyName = "OPEN_FILES";
            }
            List<BehaviorObject> targets = new ArrayList<>();
            if (clause.getStop().getType() != DmSqlParser.CLEAR && (clause.TRACE() != null || clause.BACKUPDIR() != null || clause.ARCHIVEDIR() != null)) {
                for (DmSqlParser.BackupFilePathContext path : descendants(clause, DmSqlParser.BackupFilePathContext.class)) {
                    addObject(targets, fileObject(path.getStart()));
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, keyStart, keyStop, keyName), targets);
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.RecoverStatementTailContext recover = ctx.recoverStatementTail();
        if (ctx.RECOVER() != null && recover != null && recover.DATABASE() != null && recover.backupFilePath() != null && recover.UPDATE() != null && recover.DB_MAGIC() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.RECOVER, fileObject(recover.backupFilePath().getStart()));
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.RECOVER() != null && recover != null && recover.DATABASE() != null && recover.backupFilePath() != null && recover.restoreFromClause() != null
            && recover.restoreFromClause().backupFilePath() != null) {
            BehaviorObject databaseFile = fileObject(recover.backupFilePath().getStart());
            BehaviorObject backupSet = fileObject(recover.restoreFromClause().backupFilePath().getStart());
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, databaseFile, List.of(backupSet));
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.RECOVER() != null && recover != null && recover.DATABASE() != null && recover.backupFilePath() != null && recover.TABLESPACE() != null
            && recover.qualifiedName() != null) {
            List<BehaviorObject> sources = new ArrayList<>();
            for (DmSqlParser.RecoverTablespaceOptionContext option : recover.recoverTablespaceOption()) {
                if (option.WITH() != null && option.ARCHIVEDIR() != null) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        addObject(sources, fileObject(path.getStart()));
                    }
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, objects
                .instanceObject(TargetType.Tablespace, recover.qualifiedName(), NameParts.clean(recover.qualifiedName().getText())), sources);
            add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(recover.backupFilePath().getStart()));
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.RECOVER() != null && recover != null && recover.DATABASE() != null && recover.backupFilePath() != null && recover.TABLESPACE() == null) {
            List<BehaviorObject> sources = new ArrayList<>();
            if (recover.recoverArchiveClause() != null) {
                for (DmSqlParser.BackupFilePathContext path : recover.recoverArchiveClause().backupFilePath()) {
                    addObject(sources, fileObject(path.getStart()));
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, fileObject(recover.backupFilePath().getStart()), sources);
            addFunctionCalls(ctx);
            return null;
        }
        DmSqlParser.RestoreStatementTailContext restore = ctx.restoreStatementTail();
        if (ctx.RESTORE() != null && restore != null && restore.DATABASE() != null && restore.backupFilePath() != null && restore.restoreTablespaceTail() != null) {
            DmSqlParser.RestoreTablespaceTailContext tail = restore.restoreTablespaceTail();
            BehaviorObject tablespace = objects.instanceObject(TargetType.Tablespace, tail.qualifiedName(), NameParts.clean(tail.qualifiedName().getText()));
            List<BehaviorObject> sources = new ArrayList<>();
            if (tail.restoreFromClause().backupFilePath() != null) {
                addObject(sources, fileObject(tail.restoreFromClause().backupFilePath().getStart()));
            }
            for (DmSqlParser.RestoreOptionContext option : tail.restoreOption()) {
                if ((option.WITH() != null && option.BACKUPDIR() != null) || (option.MAPPED() != null && option.FILE() != null)) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        addObject(sources, fileObject(path.getStart()));
                    }
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, tablespace, sources);
            if (tail.restoreDatafileClause() != null) {
                for (DmSqlParser.RestoreDatafileItemContext datafile : tail.restoreDatafileClause().restoreDatafileItem()) {
                    Token start = datafile.getStart();
                    if (start.getType() == DmSqlParser.STRING && start.getTokenIndex() == datafile.getStop().getTokenIndex()) {
                        add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, fileObject(start), sources);
                    }
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.READ, fileObject(restore.backupFilePath().getStart()));
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.RESTORE() != null && restore != null && restore.DATABASE() != null && restore.restoreDatabaseTarget() != null && restore.restoreFromClause() != null) {
            BehaviorObject destination = fileObject(restore.restoreDatabaseTarget().backupFilePath().getStart());
            List<BehaviorObject> sources = new ArrayList<>();
            if (restore.restoreFromClause().backupFilePath() != null) {
                addObject(sources, fileObject(restore.restoreFromClause().backupFilePath().getStart()));
            }
            for (DmSqlParser.RestoreOptionContext option : restore.restoreOption()) {
                if ((option.WITH() != null && option.BACKUPDIR() != null) || (option.MAPPED() != null && option.FILE() != null)) {
                    for (DmSqlParser.BackupFilePathContext path : option.backupFilePath()) {
                        addObject(sources, fileObject(path.getStart()));
                    }
                }
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, destination, sources);
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.RESTORE() != null && restore != null && restore.TABLE() != null && restore.restoreFromClause() != null && restore.restoreFromClause().backupFilePath() != null) {
            BehaviorObject table;
            if (restore.qualifiedName() == null) {
                table = objects.instanceObject(TargetType.Table, restore.TABLE().getSymbol());
            } else {
                table = object(TargetType.Table, restore.qualifiedName(), NameParts.from(restore.qualifiedName()));
            }
            BehaviorObject backupSet = fileObject(restore.restoreFromClause().backupFilePath().getStart());
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, table, List.of(backupSet));
            for (DmSqlParser.RestoreTableOptionContext option : restore.restoreTableOption()) {
                if (option.TRACE() != null && option.FILE() != null && option.backupFilePath() != null) {
                    add(SplitQueryType.ADMIN, BehaviorAction.EXPORT, fileObject(option.backupFilePath().getStart()), List.of(table));
                }
            }
            addFunctionCalls(ctx);
            return null;
        }
        if (ctx.RESTORE() != null && restore != null && restore.archiveLogKeyword() != null && restore.restoreArchiveTail() != null) {
            DmSqlParser.RestoreArchiveTailContext tail = restore.restoreArchiveTail();
            List<BehaviorObject> sources = new ArrayList<>();
            if (tail.restoreFromClause().backupFilePath() != null) {
                addObject(sources, fileObject(tail.restoreFromClause().backupFilePath().getStart()));
            }
            add(SplitQueryType.ADMIN, BehaviorAction.IMPORT, fileObject(tail.backupFilePath().getStart()), sources);
            addFunctionCalls(ctx);
            return null;
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitStatStatement(DmSqlParser.StatStatementContext ctx) {
        DmSqlParser.StatTargetContext target = ctx.statTarget();
        DmSqlParser.QualifiedNameContext name = target.qualifiedName();
        TargetType type = target.INDEX() == null ? TargetType.Table : TargetType.Index;
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, object(type, name, NameParts.from(name)));
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitStatProcedureStatement(DmSqlParser.StatProcedureStatementContext ctx) {
        String procedureName = ctx.statProcedureName().getText();
        List<DmSqlParser.ExpressionContext> arguments = ctx.expressionList() == null ? List.of() : ctx.expressionList().expression();
        List<BehaviorObject> targets = new ArrayList<>();
        if (isTableStatProcedure(procedureName)) {
            addStatProcedureTarget(targets, arguments, TargetType.Table);
        } else if (isIndexStatProcedure(procedureName)) {
            addStatProcedureTarget(targets, arguments, TargetType.Index);
        } else {
            addObject(targets, objects.instanceObject(TargetType.Instance, ctx.statProcedureName()));
        }
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.CALL, object(TargetType.Procedure, ctx.statProcedureName(), new NameParts(null, null, procedureName)), targets);
        addFunctionCalls(ctx);
        return null;
    }

    private boolean isTableStatProcedure(String procedure) {
        return "SP_TAB_INDEX_STAT_INIT".equalsIgnoreCase(procedure) || "SP_COL_STAT_INIT".equalsIgnoreCase(procedure) || "SP_TAB_COL_STAT_INIT".equalsIgnoreCase(procedure)
               || "SP_STAT_ON_TABLE_COLS".equalsIgnoreCase(procedure) || "SP_TAB_STAT_INIT".equalsIgnoreCase(procedure) || "SP_COL_STAT_DEINIT".equalsIgnoreCase(procedure)
               || "SP_TAB_COL_STAT_DEINIT".equalsIgnoreCase(procedure) || "SP_TAB_STAT_DEINIT".equalsIgnoreCase(procedure) || "SP_TAB_MSTAT_DEINIT".equalsIgnoreCase(procedure);
    }

    private boolean isIndexStatProcedure(String procedure) {
        return "SP_INDEX_STAT_INIT".equalsIgnoreCase(procedure) || "SP_INDEX_STAT_DEINIT".equalsIgnoreCase(procedure);
    }

    private void addStatProcedureTarget(List<BehaviorObject> targets, List<DmSqlParser.ExpressionContext> arguments, TargetType type) {
        Token schema = stringArgument(arguments, 0);
        Token name = stringArgument(arguments, 1);
        String schemaName = stringValue(schema);
        String objectName = stringValue(name);
        if (schemaName == null || objectName == null) {
            return;
        }
        addObject(targets, objects.object(type, name, List.of(schemaName, objectName)));
    }

    @Override
    public Void visitTableCreate(DmSqlParser.TableCreateContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        ParseTree createBody = ctx.tableCreateBody();
        if (createBody == null) {
            createBody = ctx.hugeTableCreateBody();
        }
        List<DmSqlParser.SelectStatementContext> selectStatements = descendants(createBody, DmSqlParser.SelectStatementContext.class);
        if (!selectStatements.isEmpty()) {
            addTableSources(sources, selectStatements.get(0));
        }
        if (ctx.likeSourceTable != null) {
            addObject(sources, object(TargetType.Table, ctx.likeSourceTable, NameParts.from(ctx.likeSourceTable)));
        }
        for (DmSqlParser.ColumnDefinitionContext column : descendants(createBody, DmSqlParser.ColumnDefinitionContext.class)) {
            addUserDefinedDataType(sources, column.dataType());
        }
        for (DmSqlParser.HugeColumnDefinitionContext column : descendants(createBody, DmSqlParser.HugeColumnDefinitionContext.class)) {
            addUserDefinedDataType(sources, column.dataType());
        }
        addColumnEncryptionTargets(sources, createBody);
        for (DmSqlParser.ReferenceConstraintContext reference : descendants(ctx, DmSqlParser.ReferenceConstraintContext.class)) {
            DmSqlParser.QualifiedNameContext referencedTable = reference.qualifiedName();
            addObject(sources, object(TargetType.Table, referencedTable, schemaScoped(NameParts.from(referencedTable))));
        }
        for (DmSqlParser.TablespaceClauseContext tablespace : descendants(ctx, DmSqlParser.TablespaceClauseContext.class)) {
            DmSqlParser.IdentifierContext name = tablespace.identifier();
            addObject(sources, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
        }
        for (DmSqlParser.StorageClauseContext storage : descendants(ctx, DmSqlParser.StorageClauseContext.class)) {
            if (storage.identifier() != null) {
                DmSqlParser.IdentifierContext name = storage.identifier();
                addObject(sources, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
            }
            for (DmSqlParser.StorageItemContext item : storage.storageItem()) {
                if (item.ON() == null) {
                    continue;
                }
                for (DmSqlParser.QualifiedNameContext name : item.qualifiedName()) {
                    addObject(sources, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
                }
            }
        }
        for (DmSqlParser.HugeTableStorageItemContext storage : descendants(ctx, DmSqlParser.HugeTableStorageItemContext.class)) {
            if (storage.ON() == null) {
                continue;
            }
            DmSqlParser.IdentifierContext name = storage.identifier();
            addObject(sources, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
        }
        for (DmSqlParser.PartitionGroupTableClauseContext group : descendants(ctx, DmSqlParser.PartitionGroupTableClauseContext.class)) {
            DmSqlParser.QualifiedNameContext groupName = group.qualifiedName();
            addObject(sources, object(TargetType.ResourceGroup, groupName, schemaScoped(NameParts.from(groupName))));
        }
        for (DmSqlParser.ExternalTableDirectoryOptionContext directory : descendants(ctx, DmSqlParser.ExternalTableDirectoryOptionContext.class)) {
            DmSqlParser.IdentifierContext name = directory.identifier();
            String directoryName = NameParts.clean(name.getText());
            addObject(sources, object(TargetType.ConfigKey, name, schemaScoped(new NameParts(null, null, directoryName))));

            Token location = directory.STRING().getSymbol();
            String locationName = stringValue(location);
            if (locationName != null) {
                addObject(sources, objects.instanceObject(TargetType.File, location, directoryName + "/" + locationName));
            }
            for (DmSqlParser.ExternalTableParmContext parameter : descendants(ctx, DmSqlParser.ExternalTableParmContext.class)) {
                boolean fileOption = parameter.LOG() != null;
                if (parameter.identifier() != null && "BADFILE".equalsIgnoreCase(NameParts.clean(parameter.identifier().getText()))) {
                    fileOption = true;
                }
                if (!fileOption || parameter.STRING() == null) {
                    continue;
                }
                Token file = parameter.STRING().getSymbol();
                String fileName = stringValue(file);
                if (fileName != null) {
                    addObject(sources, objects.instanceObject(TargetType.File, file, directoryName + "/" + fileName));
                }
            }
        }
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.targetTable, schemaScoped(NameParts.from(ctx.targetTable))), sources);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitViewCreate(DmSqlParser.ViewCreateContext ctx) {
        TargetType type = ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized;
        boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
        List<BehaviorObject> targets = tableSources(ctx);
        for (DmSqlParser.TablePrimaryContext table : descendants(ctx.selectStatement(), DmSqlParser.TablePrimaryContext.class)) {
            if (table.qualifiedName() == null || table.tableIndexClause() == null) {
                continue;
            }
            NameParts tableName = NameParts.from(table.qualifiedName());
            DmSqlParser.IdentifierContext index = table.tableIndexClause().identifier();
            NameParts indexName = new NameParts(tableName.catalog(), tableName.schema(), NameParts.clean(index.getText()));
            addObject(targets, object(TargetType.Index, index, indexName));
        }
        for (DmSqlParser.TablespaceClauseContext tablespace : descendants(ctx, DmSqlParser.TablespaceClauseContext.class)) {
            DmSqlParser.IdentifierContext name = tablespace.identifier();
            addObject(targets, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
        }
        add(SplitQueryType.CREATE_VIEW, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, object(type, ctx
            .qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), targets);
        for (DmSqlParser.MaterializedViewPrebuiltClauseContext prebuilt : descendants(ctx, DmSqlParser.MaterializedViewPrebuiltClauseContext.class)) {
            if (prebuilt.prebuiltTable != null) {
                add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, prebuilt.prebuiltTable, NameParts.from(prebuilt.prebuiltTable)));
            }
        }
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitMaterializedViewLogCreate(DmSqlParser.MaterializedViewLogCreateContext ctx) {
        NameParts name = schemaScoped(NameParts.from(ctx.qualifiedName()));
        List<BehaviorObject> targets = new ArrayList<>();
        addObject(targets, object(TargetType.Table, ctx.qualifiedName(), name));
        for (DmSqlParser.TablespaceClauseContext tablespace : descendants(ctx, DmSqlParser.TablespaceClauseContext.class)) {
            DmSqlParser.IdentifierContext tablespaceName = tablespace.identifier();
            addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespaceName, NameParts.clean(tablespaceName.getText())));
        }
        add(SplitQueryType.CREATE_LOG, BehaviorAction.CREATE, object(TargetType.Log, ctx.qualifiedName(), name), targets);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitIndexCreate(DmSqlParser.IndexCreateContext ctx) {
        List<DmSqlParser.QualifiedNameContext> names = ctx.qualifiedName();
        if (!names.isEmpty()) {
            List<BehaviorObject> targets = new ArrayList<>();
            if (names.size() > 1) {
                addObject(targets, object(TargetType.Table, names.get(1), schemaScoped(NameParts.from(names.get(1)))));
            }
            if (ctx.bitmapJoinClause() != null) {
                for (BehaviorObject source : tableSources(ctx.bitmapJoinClause())) {
                    addObject(targets, source);
                }
            }
            for (DmSqlParser.TablespaceClauseContext tablespace : descendants(ctx, DmSqlParser.TablespaceClauseContext.class)) {
                DmSqlParser.IdentifierContext name = tablespace.identifier();
                addObject(targets, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
            }
            for (DmSqlParser.StorageClauseContext storage : descendants(ctx, DmSqlParser.StorageClauseContext.class)) {
                if (storage.identifier() != null) {
                    DmSqlParser.IdentifierContext name = storage.identifier();
                    addObject(targets, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
                }
                for (DmSqlParser.StorageItemContext item : storage.storageItem()) {
                    if (item.ON() == null) {
                        continue;
                    }
                    for (DmSqlParser.QualifiedNameContext name : item.qualifiedName()) {
                        addObject(targets, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())));
                    }
                }
            }
            boolean replace = ctx.OR() != null || ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
            BehaviorAction action = replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE;
            add(SplitQueryType.ADD_INDEX, action, object(TargetType.Index, names.get(0), schemaScoped(NameParts.from(names.get(0)))), targets);
        }
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitObjectCreate(DmSqlParser.ObjectCreateContext ctx) {
        if (ctx.DOMAIN() != null) {
            DmSqlParser.QualifiedNameContext name = ctx.qualifiedName();
            add(SplitQueryType.CREATE_TYPE, BehaviorAction.CREATE, object(TargetType.Type, name, schemaScoped(NameParts.from(name))));
            addFunctionCalls(ctx.domainCreateTail());
            return null;
        }
        if (ctx.PROFILE() != null) {
            DmSqlParser.IdentifierContext name = ctx.identifier();
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.Profile, name, NameParts.clean(name.getText())));
            return null;
        }
        if (ctx.partitionGroupCreate() != null) {
            DmSqlParser.PartitionGroupCreateContext group = ctx.partitionGroupCreate();
            List<BehaviorObject> targets = new ArrayList<>();
            for (DmSqlParser.StorageItemContext item : descendants(group, DmSqlParser.StorageItemContext.class)) {
                for (DmSqlParser.QualifiedNameContext tablespace : item.qualifiedName()) {
                    addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, NameParts.clean(tablespace.getText())));
                }
                if (item.qualifiedName().isEmpty() && item.identifier() != null) {
                    DmSqlParser.IdentifierContext tablespace = item.identifier();
                    addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, NameParts.clean(tablespace.getText())));
                }
            }
            for (DmSqlParser.StoreInClauseContext storeIn : descendants(group, DmSqlParser.StoreInClauseContext.class)) {
                for (DmSqlParser.QualifiedNameContext tablespace : storeIn.qualifiedName()) {
                    addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, NameParts.clean(tablespace.getText())));
                }
            }
            DmSqlParser.QualifiedNameContext name = group.qualifiedName();
            add(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE, object(TargetType.ResourceGroup, name, schemaScoped(NameParts.from(name))), targets);
            addFunctionCalls(group);
            behavior.setStatementType(SplitQueryType.CREATE_RESOURCE_GROUP);
            return null;
        }
        if (ctx.TABLESPACE() == null) {
            return visitChildren(ctx);
        }
        List<BehaviorObject> targets = new ArrayList<>();
        for (DmSqlParser.TablespaceFilePathContext path : descendants(ctx.tablespaceCreateTail(), DmSqlParser.TablespaceFilePathContext.class)) {
            addObject(targets, fileObject(path.getStart()));
        }
        for (DmSqlParser.StorageItemContext item : descendants(ctx.tablespaceCreateTail(), DmSqlParser.StorageItemContext.class)) {
            if (item.ON() == null) {
                continue;
            }
            for (DmSqlParser.QualifiedNameContext storage : item.qualifiedName()) {
                addObject(targets, objects.instanceObject(TargetType.Replication, storage, NameParts.clean(storage.getText())));
            }
        }
        DmSqlParser.QualifiedNameContext name = ctx.qualifiedName();
        add(SplitQueryType.CREATE_TABLESPACE, BehaviorAction.CREATE, objects.instanceObject(TargetType.Tablespace, name, NameParts.clean(name.getText())), targets);
        return null;
    }

    @Override
    public Void visitSchemaCreate(DmSqlParser.SchemaCreateContext ctx) {
        NameParts parsed = ctx.schemaName == null ? null : NameParts.from(ctx.schemaName);
        String schema = parsed == null ? schemaAuthorizationOwner(ctx) : parsed.name();
        ParserRuleContext schemaContext = ctx.schemaName;
        DmSqlParser.IdentifierContext owner = null;
        if (ctx.schemaAuthorizationOnly() != null) {
            owner = ctx.schemaAuthorizationOnly().schemaOwner;
        } else if (ctx.schemaAuthorizationClause() != null) {
            owner = ctx.schemaAuthorizationClause().schemaOwner;
        }
        if (schemaContext == null) {
            schemaContext = owner;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        if (owner != null) {
            addObject(targets, objects.instanceObject(TargetType.User, owner, NameParts.clean(owner.getText())));
        }
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, schemaContext, new NameParts(parsed == null ? null : parsed.catalog(),
            null,
            schema)), targets);
        if (schema == null) {
            return visitChildren(ctx);
        }
        schemaScopes.add(schema);
        try {
            return visitChildren(ctx);
        } finally {
            schemaScopes.remove(schemaScopes.size() - 1);
        }
    }

    @Override
    public Void visitSequenceCreate(DmSqlParser.SequenceCreateContext ctx) {
        add(SplitQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))));
        return null;
    }

    @Override
    public Void visitUserCreate(DmSqlParser.UserCreateContext ctx) {
        String user = NameParts.clean(ctx.identifier().getText());
        List<BehaviorObject> targets = new ArrayList<>();
        addUserPropertyTargets(targets, ctx.userPropertyClause());
        add(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, objects.instanceObject(TargetType.User, ctx.identifier(), user), targets);
        return null;
    }

    private void addUserPropertyTargets(List<BehaviorObject> targets, List<DmSqlParser.UserPropertyClauseContext> properties) {
        for (DmSqlParser.UserPropertyClauseContext property : properties) {
            if (property.DEFAULT() != null && property.TABLESPACE() != null && property.GROUP() == null && property.identifier() != null) {
                DmSqlParser.IdentifierContext tablespace = property.identifier();
                addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, NameParts.clean(tablespace.getText())));
            }
            if (property.PROFILE() != null && property.DROP() == null && property.identifier() != null) {
                DmSqlParser.IdentifierContext profile = property.identifier();
                addObject(targets, objects.instanceObject(TargetType.Profile, profile, NameParts.clean(profile.getText())));
            }
            for (DmSqlParser.QuotaClauseContext quota : descendants(property, DmSqlParser.QuotaClauseContext.class)) {
                if (quota.identifier() == null) {
                    continue;
                }
                DmSqlParser.IdentifierContext tablespace = quota.identifier();
                addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, NameParts.clean(tablespace.getText())));
            }
        }
    }

    @Override
    public Void visitRoleCreate(DmSqlParser.RoleCreateContext ctx) {
        String role = NameParts.clean(ctx.identifier().getText());
        add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, objects.instanceObject(TargetType.Role, ctx.identifier(), role));
        return null;
    }

    @Override
    public Void visitProcedureCreate(DmSqlParser.ProcedureCreateContext ctx) {
        boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
        add(SplitQueryType.CREATE_PROG_OBJ, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, object(TargetType.Procedure, ctx
            .qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))));
        blockLocals.push(blockLocalNames(ctx));
        try {
            addNestedStatements(ctx);
            addFunctionCalls(ctx);
            return null;
        } finally {
            blockLocals.pop();
        }
    }

    @Override
    public Void visitFunctionCreate(DmSqlParser.FunctionCreateContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.aggregateFunctionCreateTail() != null) {
            DmSqlParser.QualifiedNameContext implementation = ctx.aggregateFunctionCreateTail().qualifiedName();
            addObject(targets, object(TargetType.Function, implementation, NameParts.from(implementation)));
        } else if (ctx.externalFunctionCreateTail() != null) {
            DmSqlParser.ExternalFunctionBodyContext body = ctx.externalFunctionCreateTail().externalFunctionBody();
            if (body.EXTERNAL() != null) {
                Token file = body.STRING(0).getSymbol();
                addObject(targets, fileObject(file));
            } else if (body.qualifiedName() != null) {
                addObject(targets, object(TargetType.Library, body.qualifiedName(), NameParts.from(body.qualifiedName())));
            }
        }
        boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
        add(SplitQueryType.CREATE_PROG_OBJ, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, object(TargetType.Function, ctx
            .qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), targets);
        blockLocals.push(blockLocalNames(ctx));
        try {
            addNestedStatements(ctx);
            addFunctionCalls(ctx);
            return null;
        } finally {
            blockLocals.pop();
        }
    }

    @Override
    public Void visitOperatorCreate(DmSqlParser.OperatorCreateContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Operator, ctx
            .operatorQualifiedName(), schemaScoped(operatorName(ctx.operatorQualifiedName()))), List
                .of(object(TargetType.Function, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName())))));
        return null;
    }

    @Override
    public Void visitReplaceableObjectCreate(DmSqlParser.ReplaceableObjectCreateContext ctx) {
        if (ctx.LINK() != null) {
            DmSqlParser.QualifiedNameContext link = ctx.qualifiedName();
            BehaviorObject subject;
            NameParts linkName = NameParts.from(link);
            if (ctx.PUBLIC() != null && linkName.schema() == null) {
                subject = objects.instanceObject(TargetType.Link, link, NameParts.clean(link.getText()));
            } else {
                subject = object(TargetType.Link, link, schemaScoped(linkName));
            }
            boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
            add(SplitQueryType.SYSTEM_SETTING_WRITE, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, subject);
            return null;
        }
        if (ctx.CONTEXT() != null) {
            DmSqlParser.IdentifierContext context = ctx.identifier();
            DmSqlParser.QualifiedNameContext provider = ctx.qualifiedName();
            boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
            add(SplitQueryType.SYSTEM_SETTING_WRITE, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, objects.instanceObject(TargetType.Context, context, NameParts
                .clean(context.getText())), List.of(object(TargetType.Package, provider, schemaScoped(NameParts.from(provider)))));
            return null;
        }
        if (ctx.DIRECTORY() != null) {
            DmSqlParser.IdentifierContext directory = ctx.identifier();
            NameParts name = new NameParts(null, null, NameParts.clean(directory.getText()));
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, object(TargetType.ConfigKey, directory, name), List.of(fileObject(ctx.STRING().getSymbol())));
            return null;
        }
        if (ctx.LIBRARY() != null) {
            Token file = ctx.STRING().getSymbol();
            add(SplitQueryType.CREATE_LIBRARY, createAction(ctx), object(TargetType.Library, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), List
                .of(fileObject(file)));
            return null;
        }
        if (ctx.PACKAGE() != null) {
            boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
            NameParts packageName = schemaScoped(NameParts.from(ctx.qualifiedName()));
            add(SplitQueryType.CREATE_PROG_OBJ, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, object(TargetType.Package, ctx.qualifiedName(), packageName));
            packageScopes.push(packageName);
            packageFunctionMembers.push(packageFunctionMembers(ctx));
            packageProcedureMembers.push(packageProcedureMembers(ctx));
            try {
                addNestedStatements(ctx);
                addFunctionCalls(ctx);
                return null;
            } finally {
                packageProcedureMembers.pop();
                packageFunctionMembers.pop();
                packageScopes.pop();
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitTypeCreate(DmSqlParser.TypeCreateContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        NameParts createdType = schemaScoped(NameParts.from(ctx.qualifiedName()));
        for (DmSqlParser.DataTypeContext dataType : descendants(ctx.typeCreateTail(), DmSqlParser.DataTypeContext.class)) {
            if (dataType.qualifiedName() != null) {
                NameParts referencedType = NameParts.from(dataType.qualifiedName());
                boolean unqualifiedSelf = referencedType.catalog() == null && referencedType.schema() == null && createdType.name().equals(referencedType.name());
                if (unqualifiedSelf || createdType.equals(schemaScoped(referencedType))) {
                    continue;
                }
            }
            addUserDefinedDataType(targets, dataType);
        }
        DmSqlParser.ObjectTypeDefinitionContext definition = ctx.typeCreateTail().typeDefinition().objectTypeDefinition();
        if (definition != null && definition.UNDER() != null) {
            targets.add(object(TargetType.Type, definition.qualifiedName(), schemaScoped(NameParts.from(definition.qualifiedName()))));
        }
        add(SplitQueryType.CREATE_TYPE, createAction(ctx), object(TargetType.Type, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), targets);
        addNestedStatements(ctx);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitTypeBodyCreate(DmSqlParser.TypeBodyCreateContext ctx) {
        add(SplitQueryType.CREATE_TYPE, createAction(ctx), object(TargetType.Type, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))));
        addNestedStatements(ctx);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitClassCreate(DmSqlParser.ClassCreateContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        DmSqlParser.ClassCreateTailContext tail = ctx.classCreateTail();
        if (tail.UNDER() != null) {
            DmSqlParser.QualifiedNameContext parent = first(tail.qualifiedName());
            targets.add(object(TargetType.Type, parent, schemaScoped(NameParts.from(parent))));
        }
        add(SplitQueryType.CREATE_TYPE, createAction(ctx), object(TargetType.Type, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), targets);
        addNestedStatements(ctx);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitClassBodyCreate(DmSqlParser.ClassBodyCreateContext ctx) {
        add(SplitQueryType.CREATE_TYPE, createAction(ctx), object(TargetType.Type, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))));
        addNestedStatements(ctx);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitJavaClassCreate(DmSqlParser.JavaClassCreateContext ctx) {
        NameParts name = new NameParts(null, null, NameParts.clean(ctx.identifier().getText()));
        List<BehaviorObject> targets = new ArrayList<>();
        DmSqlParser.JavaClassExtendsClauseContext extendsClause = ctx.javaClassExtendsClause();
        NameParts parentType = null;
        if (extendsClause != null) {
            parentType = schemaScoped(NameParts.from(extendsClause.qualifiedName()));
            targets.add(object(TargetType.Type, extendsClause.qualifiedName(), parentType));
        }
        add(SplitQueryType.CREATE_TYPE, createAction(ctx), object(TargetType.Type, ctx.identifier(), schemaScoped(name)), targets);
        javaClassDepth++;
        if (parentType != null) {
            javaParentTypes.push(parentType);
        }
        try {
            addNestedStatements(ctx);
            addFunctionCalls(ctx);
            return null;
        } finally {
            if (parentType != null) {
                javaParentTypes.pop();
            }
            javaClassDepth--;
        }
    }

    @Override
    public Void visitTriggerCreate(DmSqlParser.TriggerCreateContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        DmSqlParser.TriggerCreateTailContext triggerTail = ctx.triggerCreateTail();
        if (triggerTail.tableTriggerCreateTail() != null) {
            DmSqlParser.TableTriggerCreateTailContext tail = triggerTail.tableTriggerCreateTail();
            DmSqlParser.QualifiedNameContext table = first(tail.qualifiedName());
            TargetType targetType = TargetType.Table;
            if (tail.tableTriggerTiming().INSTEAD() != null) {
                targetType = TargetType.View;
            }
            addObject(targets, object(targetType, table, schemaScoped(NameParts.from(table))));
            DmSqlParser.TriggerOrderClauseContext order = tail.triggerOrderClause();
            if (order != null) {
                for (DmSqlParser.QualifiedNameContext dependency : order.qualifiedName()) {
                    addObject(targets, object(TargetType.Trigger, dependency, schemaScoped(NameParts.from(dependency))));
                }
            }
        } else if (triggerTail.eventTriggerCreateTail() != null) {
            DmSqlParser.EventTriggerTargetContext target = triggerTail.eventTriggerCreateTail().eventTriggerTarget();
            if (target.DATABASE() != null) {
                addObject(targets, objects.object(TargetType.Catalog, target.DATABASE().getSymbol(), List.of(levels.get(UmiTypes.Catalog).toString())));
            } else {
                String schema = target.identifier() == null ? levels.get(UmiTypes.Schema).toString() : NameParts.clean(target.identifier().getText());
                ParserRuleContext token = target.identifier() == null ? target : target.identifier();
                addObject(targets, objects.object(TargetType.Schema, token, List.of(schema)));
            }
        } else {
            DmSqlParser.TimerTriggerCreateTailContext tail = triggerTail.timerTriggerCreateTail();
            addObject(targets, objects.object(TargetType.Catalog, tail.DATABASE().getSymbol(), List.of(levels.get(UmiTypes.Catalog).toString())));
        }
        add(SplitQueryType.CREATE_TRIGGER, createAction(ctx), object(TargetType.Trigger, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), targets);
        blockLocals.push(blockLocalNames(ctx));
        try {
            addNestedStatements(ctx);
            addFunctionCalls(ctx);
            return null;
        } finally {
            blockLocals.pop();
        }
    }

    @Override
    public Void visitSynonymCreate(DmSqlParser.SynonymCreateContext ctx) {
        DmSqlParser.QualifiedNameContext synonym = ctx.synonymName;
        NameParts synonymName = NameParts.from(synonym);
        BehaviorObject subject = ctx.PUBLIC() == null ? object(TargetType.Synonym, synonym, schemaScoped(synonymName)) : objects
            .instanceObject(TargetType.Synonym, synonym, synonymName.name());
        DmSqlParser.QualifiedNameContext target = ctx.synonymTarget;
        List<BehaviorObject> targets = new ArrayList<>();
        addObject(targets, object(TargetType.SchemaObject, target.dottedName(), schemaScoped(NameParts.from(target))));
        if (target.linkName() != null) {
            List<String> linkParts = new ArrayList<>();
            linkParts.add(NameParts.clean(target.linkName().identifier().getText()));
            if (target.linkName().dottedNamePart() != null) {
                linkParts.add(NameParts.clean(target.linkName().dottedNamePart().getText()));
            }
            addObject(targets, object(TargetType.Link, target.linkName(), schemaScoped(NameParts.fromParts(linkParts))));
        }
        boolean replace = ctx.getParent() instanceof DmSqlParser.CreateReplaceTargetContext;
        add(SplitQueryType.CREATE_SYNONYM, replace ? BehaviorAction.REPLACE : BehaviorAction.CREATE, subject, targets);
        return null;
    }

    @Override
    public Void visitAlterTarget(DmSqlParser.AlterTargetContext ctx) {
        DmSqlParser.QualifiedNameContext qualified = first(ctx.qualifiedName());
        NameParts name = qualified == null ? null : schemaScoped(NameParts.from(qualified));
        if (ctx.TABLE() != null) {
            DmSqlParser.AlterTableActionContext action = ctx.alterTableAction();
            if (addConstraintBehavior(action, qualified, name)) {
                addFunctionCalls(ctx);
                return null;
            }
            if (action.RENAME() != null && action.PARTITION() == null && action.SUBPARTITION() == null) {
                if (action.CONSTRAINT() != null) {
                    List<DmSqlParser.IdentifierContext> identifiers = action.identifier();
                    BehaviorObject source = constraintObject(identifiers.get(0));
                    BehaviorObject target = constraintObject(identifiers.get(1));
                    add(SplitQueryType.RENAME_CONSTRAINT, BehaviorAction.RENAME, source, List.of(target));
                } else if (action.COLUMN() != null || action.ALTER() != null) {
                    add(SplitQueryType.RENAME_COLUMN, BehaviorAction.ALTER, object(TargetType.Table, qualified, name));
                } else {
                    DmSqlParser.QualifiedNameContext target = action.qualifiedName();
                    add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, object(TargetType.Table, qualified, name), List
                        .of(object(TargetType.Table, target, schemaScoped(NameParts.from(target)))));
                }
                addFunctionCalls(ctx);
                return null;
            }
            if (action.RENAME() != null && (action.PARTITION() != null || action.SUBPARTITION() != null)) {
                List<DmSqlParser.IdentifierContext> partitions = action.identifier();
                add(SplitQueryType.ALTER_TABLE, BehaviorAction.RENAME, partitionObject(name, partitions.get(0)), List.of(partitionObject(name, partitions.get(1))));
                addFunctionCalls(ctx);
                return null;
            }
            if (action.MOVE() != null) {
                List<BehaviorObject> targets = new ArrayList<>();
                if (action.PARTITION() != null || action.SUBPARTITION() != null) {
                    addPartitionTarget(targets, name, action.identifier(0));
                }
                DmSqlParser.IdentifierContext tablespace = null;
                if (action.tablespaceClause() != null) {
                    tablespace = action.tablespaceClause().identifier();
                } else if (action.TABLESPACE() != null) {
                    tablespace = action.identifier(0);
                }
                if (tablespace != null) {
                    addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, NameParts.clean(tablespace.getText())));
                }
                add(SplitQueryType.ALTER_TABLE, BehaviorAction.MOVE, object(TargetType.Table, qualified, name), targets);
                addFunctionCalls(ctx);
                return null;
            }
            SplitQueryType statementType = alterTableType(action);
            List<BehaviorObject> targets = new ArrayList<>();
            addColumnEncryptionTargets(targets, action);
            for (BehaviorObject sequence : sequenceSources(action)) {
                addObject(targets, sequence);
            }
            if (action.DEFAULT() != null && action.DIRECTORY() != null && !action.identifier().isEmpty()) {
                DmSqlParser.IdentifierContext directory = action.identifier(0);
                addObject(targets, object(TargetType.ConfigKey, directory, schemaScoped(new NameParts(null, null, NameParts.clean(directory.getText())))));
            }
            if (action.LOCATION() != null && action.STRING() != null) {
                addObject(targets, fileObject(action.STRING().getSymbol()));
            }
            if (action.MODIFY() != null && action.PATH() != null && action.expression() != null) {
                Token file = stringArgument(List.of(action.expression()), 0);
                if (file != null) {
                    addObject(targets, fileObject(file));
                }
            }
            if (action.partitionAddAction() != null) {
                addPartitionTarget(targets, name, action.partitionAddAction().identifier());
            }
            if (action.partitionDropAction() != null && action.partitionDropAction().partitionDropSelector() != null) {
                DmSqlParser.PartitionDropSelectorContext selector = action.partitionDropAction().partitionDropSelector();
                if (selector.identifier() != null) {
                    addPartitionTarget(targets, name, selector.identifier());
                } else {
                    addObject(targets, objects.object(TargetType.Partition, action.partitionDropAction(), partitionPath(name)));
                }
            }
            if (action.EXCHANGE() != null) {
                addPartitionTarget(targets, name, action.identifier(0));
                DmSqlParser.QualifiedNameContext exchangeTable = action.qualifiedName();
                addObject(targets, object(TargetType.Table, exchangeTable, schemaScoped(NameParts.from(exchangeTable))));
            }
            if (action.partitionModifyAction() != null) {
                DmSqlParser.PartitionModifyActionContext modify = action.partitionModifyAction();
                if (modify.partitionSelector().identifier() != null) {
                    addPartitionTarget(targets, name, modify.partitionSelector().identifier());
                }
                if (modify.identifier() != null) {
                    addPartitionTarget(targets, name, modify.identifier());
                }
            }
            if (action.SPLIT() != null || action.MERGE() != null || (action.RENAME() != null && action.PARTITION() != null)) {
                for (DmSqlParser.IdentifierContext partition : action.identifier()) {
                    addPartitionTarget(targets, name, partition);
                }
            }
            for (DmSqlParser.SplitPartitionItemContext item : descendants(action, DmSqlParser.SplitPartitionItemContext.class)) {
                addPartitionTarget(targets, name, item.identifier());
            }
            for (DmSqlParser.AlterPartitionTruncateTargetContext truncate : descendants(ctx, DmSqlParser.AlterPartitionTruncateTargetContext.class)) {
                if (truncate.partitionTruncateTarget() == null) {
                    continue;
                }
                DmSqlParser.IdentifierContext partition = truncate.partitionTruncateTarget().identifier();
                addPartitionTarget(targets, name, partition);
            }
            add(statementType, BehaviorAction.ALTER, object(TargetType.Table, qualified, name), targets);
            addFunctionCalls(ctx);
        } else if (ctx.contextTableName != null) {
            add(SplitQueryType.ALTER_INDEX, BehaviorAction.ALTER, object(TargetType.Index, ctx.contextIndexName, schemaScoped(NameParts.from(ctx.contextIndexName))), List
                .of(object(TargetType.Table, ctx.contextTableName, NameParts.from(ctx.contextTableName))));
        } else if (ctx.INDEX() != null) {
            DmSqlParser.AlterIndexActionContext action = ctx.alterIndexAction();
            if (action != null && action.RENAME() != null) {
                DmSqlParser.QualifiedNameContext target = action.qualifiedName();
                add(SplitQueryType.RENAME_INDEX, BehaviorAction.RENAME, object(TargetType.Index, qualified, name), List
                    .of(object(TargetType.Index, target, schemaScoped(NameParts.from(target)))));
            } else {
                add(SplitQueryType.ALTER_INDEX, BehaviorAction.ALTER, object(TargetType.Index, qualified, name));
            }
        } else if (ctx.VIEW() != null) {
            add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized, qualified, name));
            addFunctionCalls(ctx);
        } else if (ctx.SEQUENCE() != null) {
            DmSqlParser.AlterSequenceActionContext action = ctx.alterSequenceAction();
            if (action.RENAME() != null) {
                DmSqlParser.IdentifierContext target = action.identifier();
                NameParts targetName = new NameParts(name.catalog(), name.schema(), NameParts.clean(target.getText()));
                add(SplitQueryType.RENAME_SEQUENCE, BehaviorAction.RENAME, object(TargetType.Sequence, qualified, name), List.of(object(TargetType.Sequence, target, targetName)));
            } else {
                add(SplitQueryType.ALTER_SEQUENCE, BehaviorAction.ALTER, object(TargetType.Sequence, qualified, name));
            }
        } else if (ctx.USER() != null) {
            DmSqlParser.IdentifierContext user = ctx.identifier();
            List<BehaviorObject> targets = new ArrayList<>();
            DmSqlParser.AlterUserActionContext action = ctx.alterUserAction();
            addUserPropertyTargets(targets, descendants(action, DmSqlParser.UserPropertyClauseContext.class));
            for (DmSqlParser.AlterUserClauseContext clause : descendants(action, DmSqlParser.AlterUserClauseContext.class)) {
                if (clause.SCHEMA() == null || clause.identifier() == null) {
                    continue;
                }
                DmSqlParser.IdentifierContext schema = clause.identifier();
                addObject(targets, objects.object(TargetType.Schema, schema, List.of(NameParts.clean(schema.getText()))));
            }
            BehaviorAction behaviorAction = BehaviorAction.ALTER;
            if (action.CONNECT() != null && action.identifier() != null) {
                behaviorAction = action.GRANT() == null ? BehaviorAction.REVOKE : BehaviorAction.GRANT;
                DmSqlParser.IdentifierContext proxy = action.identifier();
                addObject(targets, objects.instanceObject(TargetType.User, proxy, NameParts.clean(proxy.getText())));
            }
            add(SplitQueryType.ALTER_USER, behaviorAction, objects.instanceObject(TargetType.User, user, NameParts.clean(user.getText())), targets);
        } else if (ctx.PROCEDURE() != null || ctx.FUNCTION() != null) {
            add(SplitQueryType.ALTER_PROG_OBJ, BehaviorAction.ALTER, object(ctx.PROCEDURE() != null ? TargetType.Procedure : TargetType.Function, qualified, name));
        } else if (ctx.TRIGGER() != null) {
            add(SplitQueryType.ALTER_TRIGGER, BehaviorAction.ALTER, object(TargetType.Trigger, qualified, name));
        } else if (ctx.PACKAGE() != null) {
            add(SplitQueryType.ADMIN_PROG_OBJ, BehaviorAction.ALTER, object(TargetType.Package, qualified, name));
        } else if (ctx.TABLESPACE() != null) {
            DmSqlParser.TablespaceAlterActionContext action = ctx.tablespaceAlterAction();
            List<DmSqlParser.TablespaceFilePathContext> files = descendants(action, DmSqlParser.TablespaceFilePathContext.class);
            if (action.RENAME() != null && action.DATAFILE() == null) {
                DmSqlParser.QualifiedNameContext target = action.qualifiedName();
                add(SplitQueryType.RENAME_TABLESPACE, BehaviorAction.RENAME, objects.instanceObject(TargetType.Tablespace, qualified, NameParts.clean(qualified.getText())), List
                    .of(objects.instanceObject(TargetType.Tablespace, target, NameParts.clean(target.getText()))));
            } else if (action.RENAME() != null) {
                int toIndex = action.TO().getSymbol().getTokenIndex();
                List<DmSqlParser.TablespaceFilePathContext> sources = files.stream().filter(file -> file.getStart().getTokenIndex() < toIndex).toList();
                List<DmSqlParser.TablespaceFilePathContext> targets = files.stream().filter(file -> file.getStart().getTokenIndex() > toIndex).toList();
                for (int i = 0; i < sources.size(); i++) {
                    add(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.RENAME, fileObject(sources.get(i).getStart()), List.of(fileObject(targets.get(i).getStart())));
                }
            } else if (action.ADD() != null && !files.isEmpty()) {
                for (DmSqlParser.TablespaceFilePathContext file : files) {
                    add(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.CREATE, fileObject(file.getStart()));
                }
            } else if (action.DROP() != null && !files.isEmpty()) {
                add(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.DROP, fileObject(files.get(0).getStart()));
            } else if (!files.isEmpty()) {
                List<BehaviorObject> targets = new ArrayList<>();
                if (action.ON() != null && action.identifier() != null) {
                    addObject(targets, objects.instanceObject(TargetType.Replication, action.identifier(), NameParts.clean(action.identifier().getText())));
                }
                for (DmSqlParser.TablespaceFilePathContext file : files) {
                    add(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.ALTER, fileObject(file.getStart()), targets);
                }
            } else {
                add(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.ALTER, objects.instanceObject(TargetType.Tablespace, qualified, NameParts.clean(qualified.getText())));
            }
        } else if (ctx.PROFILE() != null) {
            ParserRuleContext profile = ctx.profileName().identifier();
            if (profile == null) {
                profile = ctx.profileName();
            }
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, objects.instanceObject(TargetType.Profile, profile, NameParts.clean(profile.getText())));
        } else if (ctx.TYPE() != null || ctx.CLASS() != null) {
            add(SplitQueryType.ADMIN_TYPE, BehaviorAction.ALTER, object(TargetType.Type, qualified, name));
        }
        return null;
    }

    private boolean addConstraintBehavior(DmSqlParser.AlterTableActionContext action, DmSqlParser.QualifiedNameContext tableContext, NameParts tableName) {
        if (action.CONSTRAINT() == null && action.PRIMARY() == null && action.tableConstraint() == null) {
            return false;
        }
        if (action.ADD() != null && action.tableConstraint() != null) {
            DmSqlParser.TableConstraintContext constraint = action.tableConstraint();
            DmSqlParser.IdentifierContext identifier = constraint.identifier();
            BehaviorObject subject;
            if (identifier == null) {
                subject = objects.unnamedObject(TargetType.Constraint, constraint, UmiTypes.Schema);
            } else {
                subject = constraintObject(identifier);
            }
            List<BehaviorObject> targets = new ArrayList<>();
            addObject(targets, object(TargetType.Table, tableContext, tableName));
            for (DmSqlParser.ReferenceConstraintContext reference : descendants(constraint, DmSqlParser.ReferenceConstraintContext.class)) {
                DmSqlParser.QualifiedNameContext referencedTable = reference.qualifiedName();
                addObject(targets, object(TargetType.Table, referencedTable, schemaScoped(NameParts.from(referencedTable))));
            }
            add(SplitQueryType.ADD_CONSTRAINT, BehaviorAction.CREATE, subject, targets);
            return true;
        }
        if (action.RENAME() != null) {
            return false;
        }
        SplitQueryType type = SplitQueryType.ALTER_CONSTRAINT;
        BehaviorAction behaviorAction = BehaviorAction.ALTER;
        BehaviorObject subject;
        if (!action.DROP().isEmpty()) {
            type = SplitQueryType.DROP_CONSTRAINT;
            behaviorAction = BehaviorAction.DROP;
        }
        if (action.CONSTRAINT() != null) {
            subject = constraintObject(action.identifier(0));
        } else {
            CommonToken primaryKey = new CommonToken(action.PRIMARY().getSymbol());
            primaryKey.setText("PRIMARY KEY");
            subject = objects.unnamedObject(TargetType.Constraint, primaryKey, UmiTypes.Schema);
        }
        add(type, behaviorAction, subject);
        return true;
    }

    private BehaviorObject constraintObject(DmSqlParser.IdentifierContext identifier) {
        String name = NameParts.clean(identifier.getText());
        return object(TargetType.Constraint, identifier, schemaScoped(new NameParts(null, null, name)));
    }

    private SplitQueryType alterTableType(DmSqlParser.AlterTableActionContext action) {
        String actionText = action.getText().toUpperCase(Locale.ROOT);
        if (actionText.startsWith("RENAMECOLUMN")) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (action.LOGIC() != null || actionText.startsWith("ADDLOGICLOG") || actionText.startsWith("DROPLOGICLOG")) {
            return SplitQueryType.ALTER_TABLE;
        }
        if (action.ADD() != null && (action.IDENTITY() != null || action.AUTO_INCREMENT() != null)) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (action.ADD() != null && (action.columnDefinition() != null || action.tableElementList() != null)) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (action.MODIFY() != null && (action.modifyColumnDefinitionList() != null || action.modifyColumnDefinition() != null)) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (action.ALTER() != null) {
            if (actionText.contains("RENAMETO")) {
                return SplitQueryType.RENAME_COLUMN;
            }
            return SplitQueryType.ALTER_COLUMN;
        }
        if (!action.DROP().isEmpty()) {
            if (action.dropColumnTarget() != null) {
                return SplitQueryType.DROP_COLUMN;
            }
            if (action.IDENTITY() != null || action.AUTO_INCREMENT() != null) {
                return SplitQueryType.ALTER_COLUMN;
            }
        }
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public Void visitDropTarget(DmSqlParser.DropTargetContext ctx) {
        DmSqlParser.QualifiedNameContext qualified = first(ctx.qualifiedName());
        NameParts name = qualified == null ? null : NameParts.from(qualified);
        if (ctx.TABLE() != null) {
            add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, qualified, name));
        } else if (ctx.MATERIALIZED() != null && ctx.LOG() != null) {
            add(SplitQueryType.DROP_LOG, BehaviorAction.DROP, object(TargetType.Log, qualified, name));
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, qualified, name));
        } else if (ctx.VIEW() != null) {
            add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized, qualified, name));
        } else if (ctx.contextTableName != null) {
            add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ctx.contextIndexName, schemaScoped(NameParts.from(ctx.contextIndexName))), List
                .of(object(TargetType.Table, ctx.contextTableName, NameParts.from(ctx.contextTableName))));
        } else if (ctx.INDEX() != null) {
            add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, qualified, name));
        } else if (ctx.SCHEMA() != null) {
            add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, qualified, name));
        } else if (ctx.SEQUENCE() != null) {
            add(SplitQueryType.DROP_SEQUENCE, BehaviorAction.DROP, object(TargetType.Sequence, qualified, name));
        } else if (ctx.USER() != null) {
            String user = NameParts.clean(ctx.identifier().getText());
            add(SplitQueryType.DROP_USER, BehaviorAction.DROP, objects.instanceObject(TargetType.User, ctx.identifier(), user));
        } else if (ctx.ROLE() != null) {
            String role = NameParts.clean(ctx.identifier().getText());
            add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, objects.instanceObject(TargetType.Role, ctx.identifier(), role));
        } else if (ctx.PROCEDURE() != null || ctx.FUNCTION() != null) {
            add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(ctx.PROCEDURE() != null ? TargetType.Procedure : TargetType.Function, qualified, name));
        } else if (ctx.TRIGGER() != null) {
            add(SplitQueryType.DROP_TRIGGER, BehaviorAction.DROP, object(TargetType.Trigger, qualified, name));
        } else if (ctx.SYNONYM() != null) {
            BehaviorObject synonym = ctx.PUBLIC() == null ? object(TargetType.Synonym, qualified, name) : objects
                .instanceObject(TargetType.Synonym, qualified, NameParts.from(qualified).name());
            add(SplitQueryType.DROP_SYNONYM, BehaviorAction.DROP, synonym);
        } else if (ctx.OPERATOR() != null) {
            add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Operator, ctx
                .operatorQualifiedName(), schemaScoped(operatorName(ctx.operatorQualifiedName()))));
        } else if (ctx.PACKAGE() != null) {
            add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Package, qualified, name));
        } else if (ctx.TABLESPACE() != null) {
            add(SplitQueryType.DROP_TABLESPACE, BehaviorAction.DROP, objects.instanceObject(TargetType.Tablespace, qualified, NameParts.clean(qualified.getText())));
        } else if (ctx.LIBRARY() != null) {
            add(SplitQueryType.DROP_LIBRARY, BehaviorAction.DROP, object(TargetType.Library, qualified, name));
        } else if (ctx.DOMAIN() != null || ctx.TYPE() != null || ctx.CLASS() != null) {
            add(SplitQueryType.DROP_TYPE, BehaviorAction.DROP, object(TargetType.Type, qualified, name));
        } else if (ctx.LINK() != null) {
            BehaviorObject subject;
            if (ctx.PUBLIC() != null && name.schema() == null) {
                subject = objects.instanceObject(TargetType.Link, qualified, name.name());
            } else {
                subject = object(TargetType.Link, qualified, name);
            }
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, subject);
        } else if (ctx.DIRECTORY() != null) {
            NameParts config = new NameParts(null, null, NameParts.clean(qualified.getText()));
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, object(TargetType.ConfigKey, qualified, config));
        } else if (ctx.CONTEXT() != null || ctx.PROFILE() != null) {
            if (ctx.PROFILE() != null) {
                DmSqlParser.IdentifierContext profile = ctx.identifier();
                add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.Profile, profile, NameParts.clean(profile.getText())));
            } else {
                add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.Context, qualified, name.name()));
            }
        } else if (ctx.PARTITION() != null && ctx.GROUP() != null) {
            add(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP, object(TargetType.ResourceGroup, qualified, schemaScoped(name)));
        }
        return null;
    }

    @Override
    public Void visitTruncateStatement(DmSqlParser.TruncateStatementContext ctx) {
        NameParts name = NameParts.from(ctx.qualifiedName());
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.truncatePartitionClause() != null) {
            addPartitionTarget(targets, name, ctx.truncatePartitionClause().partitionTruncateTarget().identifier());
        }
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName(), name), targets);
        return null;
    }

    @Override
    public Void visitCommentStatement(DmSqlParser.CommentStatementContext ctx) {
        DmSqlParser.CommentTargetContext target = ctx.commentTarget();
        if (target.VIEW() != null) {
            add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, target.qualifiedName(), schemaScoped(NameParts.from(target.qualifiedName()))));
        } else if (target.TABLE() != null) {
            add(SplitQueryType.COMMENT_TABLE, BehaviorAction.ALTER, object(TargetType.Table, target.qualifiedName(), schemaScoped(NameParts.from(target.qualifiedName()))));
        } else {
            NameParts column = NameParts.from(target.qualifiedName());
            String table = column.schema();
            List<String> names = new ArrayList<>();
            if (column.catalog() != null) {
                names.add(column.catalog());
            } else if (!schemaScopes.isEmpty()) {
                names.add(schemaScopes.get(schemaScopes.size() - 1));
            }
            names.add(table);
            DmSqlParser.DottedNameContext dotted = target.qualifiedName().dottedName();
            Token tableStart = dotted.identifier().getStart();
            List<DmSqlParser.DottedNamePartContext> parts = dotted.dottedNamePart();
            Token tableStop = dotted.identifier().getStop();
            if (parts.size() > 1) {
                tableStop = parts.get(parts.size() - 2).getStop();
            }
            add(SplitQueryType.COMMENT_COLUMN, BehaviorAction.ALTER, objects.object(TargetType.Table, tableStart, tableStop, names));
        }
        return null;
    }

    @Override
    public Void visitGrantStatement(DmSqlParser.GrantStatementContext ctx) {
        if (ctx.grantPrivilegeStatement() != null) {
            DmSqlParser.GrantPrivilegeStatementContext grant = ctx.grantPrivilegeStatement();
            if (grant.privilegeObjectClause() != null) {
                DmSqlParser.PrivilegeObjectContext privilegeObject = grant.privilegeObjectClause().privilegeObject();
                boolean missingDirectory = privilegeObject.privilegeObjectType() == null && "DIRECTORY".equalsIgnoreCase(privilegeObject.qualifiedName().getText()) && grant
                    .privilegeList()
                    .privilegeItem()
                    .stream()
                    .allMatch(privilege -> privilege.privilegeAction() != null && (privilege.privilegeAction().READ() != null || privilege.privilegeAction().WRITE() != null));
                if (missingDirectory) {
                    // DIRECTORY is the object type here; a missing name must
                    // not become a fabricated object named DIRECTORY. Retain
                    // the known resource kind at its nearest instance scope.
                    add(SplitQueryType.GRANT, BehaviorAction.GRANT, objects
                        .instanceObject(TargetType.ConfigKey, privilegeObject.qualifiedName()), granteeTargets(grant.granteeList()));
                    return null;
                }
                addPrivilege(SplitQueryType.GRANT, BehaviorAction.GRANT, grant.privilegeObjectClause().privilegeObject(), grant.granteeList());
            } else if (isRoleList(grant.privilegeList())) {
                addRolePrivileges(SplitQueryType.GRANT, BehaviorAction.GRANT, grant.privilegeList(), grant.granteeList());
            } else {
                addDatabasePrivileges(SplitQueryType.GRANT, BehaviorAction.GRANT, grant.privilegeList(), grant.granteeList());
            }
        } else if (ctx.grantRoleStatement() != null) {
            List<DmSqlParser.GranteeListContext> grantees = ctx.grantRoleStatement().granteeList();
            addRolePrivileges(SplitQueryType.GRANT, BehaviorAction.GRANT, grantees.get(0), grantees.get(1));
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(DmSqlParser.RevokeStatementContext ctx) {
        if (ctx.revokePrivilegeStatement() != null) {
            DmSqlParser.RevokePrivilegeStatementContext revoke = ctx.revokePrivilegeStatement();
            if (revoke.privilegeObjectClause() != null) {
                addPrivilege(SplitQueryType.REVOKE, BehaviorAction.REVOKE, revoke.privilegeObjectClause().privilegeObject(), revoke.granteeList());
            } else if (isRoleList(revoke.privilegeList())) {
                addRolePrivileges(SplitQueryType.REVOKE, BehaviorAction.REVOKE, revoke.privilegeList(), revoke.granteeList());
            } else {
                addDatabasePrivileges(SplitQueryType.REVOKE, BehaviorAction.REVOKE, revoke.privilegeList(), revoke.granteeList());
            }
        } else if (ctx.revokeRoleStatement() != null) {
            List<DmSqlParser.GranteeListContext> grantees = ctx.revokeRoleStatement().granteeList();
            addRolePrivileges(SplitQueryType.REVOKE, BehaviorAction.REVOKE, grantees.get(0), grantees.get(1));
        }
        return null;
    }

    private boolean isRoleList(DmSqlParser.PrivilegeListContext privileges) {
        return !privileges.privilegeItem().isEmpty() && privileges.privilegeItem().stream().allMatch(privilege -> privilege.identifier() != null);
    }

    private void addRolePrivileges(SplitQueryType type, BehaviorAction action, DmSqlParser.PrivilegeListContext roles, DmSqlParser.GranteeListContext recipients) {
        List<BehaviorObject> targets = granteeTargets(recipients);
        for (DmSqlParser.PrivilegeItemContext role : roles.privilegeItem()) {
            Token token = role.identifier().getStart();
            add(type, action, objects.instanceObject(TargetType.Role, token, NameParts.clean(token.getText())), targets);
        }
    }

    private void addRolePrivileges(SplitQueryType type, BehaviorAction action, DmSqlParser.GranteeListContext roles, DmSqlParser.GranteeListContext recipients) {
        List<BehaviorObject> targets = granteeTargets(recipients);
        for (DmSqlParser.GranteeContext role : roles.grantee()) {
            Token token = role.getStart();
            add(type, action, objects.instanceObject(TargetType.Role, token, NameParts.clean(token.getText())), targets);
        }
    }

    private List<BehaviorObject> granteeTargets(DmSqlParser.GranteeListContext recipients) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (DmSqlParser.GranteeContext recipient : recipients.grantee()) {
            Token token = recipient.getStart();
            addObject(targets, objects.instanceObject(TargetType.UserOrRole, token, NameParts.clean(token.getText())));
        }
        return targets;
    }

    private void addDatabasePrivileges(SplitQueryType type, BehaviorAction action, DmSqlParser.PrivilegeListContext privileges, DmSqlParser.GranteeListContext grantees) {
        List<BehaviorObject> targets = granteeTargets(grantees);
        for (DmSqlParser.PrivilegeItemContext privilege : privileges.privilegeItem()) {
            DmSqlParser.PrivilegeObjectTypeContext objectType = privilege.privilegeObjectType();
            if (objectType == null) {
                continue;
            }
            if (objectType.DATABASE() != null) {
                add(type, action, objects.instanceObject(TargetType.Instance, objectType.DATABASE().getSymbol()), targets);
            } else {
                add(type, action, objects.instanceObject(privilegeTarget(objectType), objectType), targets);
            }
        }
    }

    @Override
    public Void visitAuditAdminStatement(DmSqlParser.AuditAdminStatementContext ctx) {
        DmSqlParser.AuditAdminProcedureContext procedure = ctx.auditAdminProcedure();
        String procedureName = NameParts.clean(procedure.getText());
        NameParts name = new NameParts(null, null, procedureName);
        List<DmSqlParser.ExpressionContext> arguments = ctx.expressionList() == null ? List.of() : ctx.expressionList().expression();
        List<BehaviorObject> targets = new ArrayList<>();
        if (procedureName.equalsIgnoreCase("sp_audit_stmt") || procedureName.equalsIgnoreCase("sp_noaudit_stmt")) {
            addAuditUserTarget(targets, arguments, 1);
        } else if (procedureName.equalsIgnoreCase("sp_audit_object") || procedureName.equalsIgnoreCase("sp_noaudit_object")) {
            addAuditUserTarget(targets, arguments, 1);
            addAuditObjectTargets(targets, arguments);
        } else if (procedureName.equalsIgnoreCase("sp_audit_sqlseq_start") || procedureName.equalsIgnoreCase("sp_audit_sqlseq_add")
                   || procedureName.equalsIgnoreCase("sp_audit_sqlseq_end") || procedureName.equalsIgnoreCase("sp_audit_sqlseq_del")) {
            addAuditPolicyTarget(targets, arguments);
        } else if (procedureName.equalsIgnoreCase("sp_create_audit_rule")) {
            addAuditPolicyTarget(targets, arguments);
            addAuditUserTarget(targets, arguments, 2);
        } else if (procedureName.equalsIgnoreCase("sp_drop_audit_rule")) {
            addAuditPolicyTarget(targets, arguments);
        } else if (procedureName.equalsIgnoreCase("sp_set_enable_audit")) {
            addObject(targets, objects.instanceObject(TargetType.ConfigKey, procedure, "ENABLE_AUDIT"));
        } else if (procedureName.equalsIgnoreCase("sp_switch_audit_file") || procedureName.equalsIgnoreCase("sp_audit_set_enc")
                   || procedureName.equalsIgnoreCase("sp_drop_audit_file")) {
            addObject(targets, objects.instanceObject(TargetType.File, procedure));
        }
        add(SplitQueryType.ADMIN, BehaviorAction.CALL, object(TargetType.Procedure, procedure, name), targets);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitSecurityAdminStatement(DmSqlParser.SecurityAdminStatementContext ctx) {
        DmSqlParser.SecurityAdminProcedureContext procedure = ctx.securityAdminProcedure();
        String procedureName = NameParts.clean(procedure.getText());
        NameParts name = new NameParts(null, null, procedureName);
        List<DmSqlParser.ExpressionContext> arguments = ctx.expressionList() == null ? List.of() : ctx.expressionList().expression();
        List<BehaviorObject> targets = new ArrayList<>();
        SplitQueryType type = SplitQueryType.ADMIN;
        if (procedureName.equalsIgnoreCase("sp_set_role")) {
            Token role = stringArgument(arguments, 0);
            String roleName = stringValue(role);
            if (roleName != null) {
                addObject(targets, objects.instanceObject(TargetType.Role, role, roleName));
            }
            type = SplitQueryType.CALL_PROG_OBJ;
        }
        add(type, BehaviorAction.CALL, object(TargetType.Procedure, procedure, name), targets);
        addFunctionCalls(ctx);
        return null;
    }

    private void addAuditPolicyTarget(List<BehaviorObject> targets, List<DmSqlParser.ExpressionContext> arguments) {
        Token token = stringArgument(arguments, 0);
        String name = stringValue(token);
        if (name != null && !name.equalsIgnoreCase("NULL")) {
            addObject(targets, objects.instanceObject(TargetType.Policy, token, name));
        }
    }

    private void addAuditUserTarget(List<BehaviorObject> targets, List<DmSqlParser.ExpressionContext> arguments, int index) {
        Token token = stringArgument(arguments, index);
        String user = stringValue(token);
        if (user != null && !user.equalsIgnoreCase("NULL")) {
            addObject(targets, objects.instanceObject(TargetType.User, token, user));
        }
    }

    private void addAuditObjectTargets(List<BehaviorObject> targets, List<DmSqlParser.ExpressionContext> arguments) {
        Token schemaToken = stringArgument(arguments, 2);
        Token objectToken = stringArgument(arguments, 3);
        String schema = stringValue(schemaToken);
        String objectName = stringValue(objectToken);
        if (schema == null || schema.equalsIgnoreCase("NULL") || objectName == null || objectName.equalsIgnoreCase("NULL")) {
            return;
        }
        TargetType objectType = auditObjectType(arguments);
        addObject(targets, objects.object(objectType, objectToken, List.of(schema, objectName)));
    }

    private TargetType auditObjectType(List<DmSqlParser.ExpressionContext> arguments) {
        String type = stringValue(stringArgument(arguments, 0));
        if (type == null) {
            return TargetType.SchemaObject;
        }
        if (type.equalsIgnoreCase("INSERT") || type.equalsIgnoreCase("UPDATE") || type.equalsIgnoreCase("DELETE") || type.equalsIgnoreCase("SELECT")
            || type.equalsIgnoreCase("LOCK TABLE") || type.equalsIgnoreCase("BACKUP TABLE") || type.equalsIgnoreCase("RESTORE TABLE")) {
            return TargetType.Table;
        }
        if (type.equalsIgnoreCase("EXECUTE TRIGGER")) {
            return TargetType.Trigger;
        }
        if (type.equalsIgnoreCase("EXECUTE")) {
            return TargetType.ProgramObject;
        }
        return TargetType.SchemaObject;
    }

    private Token stringArgument(List<DmSqlParser.ExpressionContext> arguments, int index) {
        if (index >= arguments.size()) {
            return null;
        }
        DmSqlParser.ExpressionContext argument = arguments.get(index);
        Token token = argument.getStart();
        if (token != argument.getStop() || token.getType() != DmSqlParser.STRING) {
            return null;
        }
        return token;
    }

    private String stringValue(Token token) {
        if (token == null) {
            return null;
        }
        String text = token.getText();
        if (text == null || text.length() < 2 || text.charAt(0) != '\'' || text.charAt(text.length() - 1) != '\'') {
            return null;
        }
        return text.substring(1, text.length() - 1).replace("''", "'");
    }

    private Token archiveConfigurationValue(Token configuration, String key) {
        String text = configuration.getText();
        int start = text.startsWith("'") ? 1 : 0;
        int limit = text.endsWith("'") ? text.length() - 1 : text.length();
        while (start < limit) {
            int end = text.indexOf(',', start);
            if (end < 0 || end > limit) {
                end = limit;
            }
            int equals = text.indexOf('=', start);
            if (equals >= start && equals < end && text.substring(start, equals).trim().equalsIgnoreCase(key)) {
                int valueStart = equals + 1;
                while (valueStart < end && Character.isWhitespace(text.charAt(valueStart))) {
                    valueStart++;
                }
                int valueEnd = end;
                while (valueEnd > valueStart && Character.isWhitespace(text.charAt(valueEnd - 1))) {
                    valueEnd--;
                }
                CommonToken value = new CommonToken(configuration);
                value.setText(text.substring(valueStart, valueEnd));
                value.setCharPositionInLine(configuration.getCharPositionInLine() + valueStart);
                value.setStartIndex(configuration.getStartIndex() + valueStart);
                value.setStopIndex(configuration.getStartIndex() + valueEnd - 1);
                return value;
            }
            start = end + 1;
        }
        return null;
    }

    @Override
    public Void visitCallStatement(DmSqlParser.CallStatementContext ctx) {
        NameParts name = NameParts.from(ctx.qualifiedName());
        if (isJavaConstructorDelegate(name)) {
            addFunctionCalls(ctx);
            return null;
        }
        if (isPackageProcedureMember(name)) {
            name = packageMemberName(name.name());
        }
        List<BehaviorObject> targets = new ArrayList<>();
        String procedure = name.name().toUpperCase(Locale.ROOT);
        addSystemPackageTargets(targets, name, procedure, ctx.qualifiedName(), ctx.routineArgumentList());
        addStandaloneProcedureTargets(targets, procedure, ctx.routineArgumentList(), ctx.qualifiedName());
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, routineObject(TargetType.Procedure, ctx.qualifiedName(), name), targets);
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitProcedureCallStatement(DmSqlParser.ProcedureCallStatementContext ctx) {
        ParserRuleContext context = ctx.qualifiedName() == null ? ctx.bareRoutineName() : ctx.qualifiedName();
        NameParts name = ctx.qualifiedName() == null ? NameParts.from(ctx.bareRoutineName()) : NameParts.from(ctx.qualifiedName());
        if (isJavaConstructorDelegate(name)) {
            addFunctionCalls(ctx);
            return null;
        }
        boolean packageMember = isPackageProcedureMember(name);
        if (packageMember) {
            name = packageMemberName(name.name());
        }
        String localType = packageMember ? null : localMethodType(name);
        if (localType != null) {
            name = new NameParts(null, null, name.name());
            context = finalNamePart(ctx.qualifiedName());
        } else if (isBlockLocal(name)) {
            addFunctionCalls(ctx);
            return null;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        String procedure = name.name().toUpperCase(Locale.ROOT);
        if ("DBMS_SCHEDULER".equalsIgnoreCase(name.schema())) {
            addSchedulerTargets(targets, procedure, ctx.routineArgumentList());
        }
        addSystemPackageTargets(targets, name, procedure, context, ctx.routineArgumentList());
        addStandaloneProcedureTargets(targets, procedure, ctx.routineArgumentList(), context);
        BehaviorObject subject = localType == null ? routineObject(TargetType.Procedure, context, name) : localMethodObject(TargetType.Procedure, context, localType, name.name());
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, subject, targets);
        addFunctionCalls(ctx);
        return null;
    }

    private void addStandaloneProcedureTargets(List<BehaviorObject> targets, String procedure, DmSqlParser.RoutineArgumentListContext arguments, ParserRuleContext context) {
        switch (procedure) {
            case "SP_CREATE_SYSTEM_PACKAGES" -> addStringRoutineTarget(targets, arguments, 1, TargetType.Package);
            case "SP_CREATE_JOB", "SP_JOB_CONFIG_START", "SP_DROP_JOB" -> addStringRoutineTarget(targets, arguments, 0, TargetType.Job);
            case "SP_CREATE_DPC_BP_GROUP", "SP_DROP_DPC_BP_GROUP", "SP_CREATE_DPC_SP_GROUP", "SP_DROP_DPC_SP_GROUP", "SP_TS_GROUP_CREATE", "SP_TS_GROUP_DROP",
                    "SP_GET_ALL_TS_BY_TSGROUP" ->
                addStringRoutineTarget(targets, arguments, 0, TargetType.ResourceGroup);
            case "SP_CREATE_DPC_RAFT" -> addStringRoutineTarget(targets, arguments, 1, TargetType.Replication);
            case "SP_DROP_DPC_BP_RAFT", "SP_DROP_DPC_RAFT" -> addStringRoutineTarget(targets, arguments, 0, TargetType.Replication);
            case "SP_CREATE_DPC_INSTANCE" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.Replication);
                addStringRoutineTarget(targets, arguments, 1, TargetType.Instance);
            }
            case "SP_DROP_DPC_INSTANCE", "SP_MODIFY_DPC_INSTANCE" -> addStringRoutineTarget(targets, arguments, 0, TargetType.Instance);
            case "SP_BP_GROUP_ADD_RAFT", "SP_BP_GROUP_DEL_RAFT", "SP_SP_GROUP_ADD_RAFT", "SP_SP_GROUP_DEL_RAFT" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.ResourceGroup);
                addStringRoutineTarget(targets, arguments, 1, TargetType.Replication);
            }
            case "SP_SET_DPC_NET_CONF", "SP_SET_DBG_SHOW" -> addStringRoutineTarget(targets, arguments, 0, TargetType.ConfigKey);
            case "SP_DISABLE_DPC_RAFT", "SP_ENABLE_DPC_RAFT", "SP_ADD_RAFT_LEARNER", "SP_DELETE_RAFT_LEARNER" ->
                addStringRoutineTarget(targets, arguments, 0, TargetType.Replication);
            case "SP_TS_GROUP_ADD_TS", "SP_TS_GROUP_REMOVE_TS" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.ResourceGroup);
                addStringRoutineTarget(targets, arguments, 1, TargetType.Tablespace);
            }
            case "SP_RENAME_DPC_INSTANCE" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.Instance);
                addStringRoutineTarget(targets, arguments, 1, TargetType.Instance);
            }
            case "SP_ALTER_DPC_INSTANCE", "SP_SET_DPC_INST_AUX" -> addStringRoutineTarget(targets, arguments, 0, TargetType.Instance);
            case "SP_DPC_MOVE_TS_OFFLINE" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.Tablespace);
                addStringRoutineTarget(targets, arguments, 1, TargetType.Replication);
            }
            case "SP_TABLESPACE_PREPARE_RECOVER", "SP_TABLESPACE_RECOVER" -> addStringRoutineTarget(targets, arguments, 0, TargetType.Tablespace);
            case "SP_FILE_SYS_CHECK" -> addObject(targets, objects.instanceObject(TargetType.Instance, context));
            case "SP_ALTER_RAFT_NODE", "SP_ADD_RAFT_NODE", "SP_DELETE_RAFT_NODE" -> addDelimitedStringRoutineTargets(targets, arguments, 0, TargetType.Replication);
            case "SP_REPLACE_RAFT_NODE" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.Replication);
                addStringRoutineTarget(targets, arguments, 1, TargetType.Replication);
            }
            case "SP_DPC_DUMP_INST" -> {
                Token file = stringRoutineArgument(arguments, 0);
                if (file != null) {
                    addObject(targets, fileObject(file));
                }
            }
            case "SP_SET_SP_UPGRADE", "SP_RESET_SP_UPGRADE", "SP_RAFT_RESUME_THREAD" -> addStringRoutineTarget(targets, arguments, 0, TargetType.Instance);
            case "SP_DPC_REBANLANCE_SESSION" -> addObject(targets, objects.instanceObject(TargetType.ConfigKey, context));
            case "SP_RAFT_SUSPEND_THREAD", "SP_RAFT_SWITCHOVER" -> addObject(targets, objects.instanceObject(TargetType.Replication, context));
            case "SP_TS_DROP_INVALID" -> addObject(targets, objects.instanceObject(TargetType.Tablespace, context));
            case "SP_SET_RAFT_ASYNC_INTERVAL", "SP_GET_RAFT_ASYNC_INTERVAL" -> {
                addStringRoutineTarget(targets, arguments, 0, TargetType.Instance);
                addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "RAFT_ASYNC_INTERVAL"));
            }
            case "SP_SET_SESSION_MPP_SELECT_LOCAL" -> addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "MPP_SELECT_LOCAL"));
            case "SP_SET_SESSION_LOCAL_TYPE" -> addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "LOCAL_TYPE"));
            case "SP_GET_EP_COUNT", "SP_TABLEDEF" -> {
                Token schema = stringRoutineArgument(arguments, 0);
                Token table = stringRoutineArgument(arguments, 1);
                if (schema != null && table != null) {
                    addObject(targets, objects.object(TargetType.Table, table, List.of(stringValue(schema), stringValue(table))));
                }
            }
            case "SP_CLEAR_TAB_ROWCNT_CACHE" -> addObject(targets, objects.instanceObject(TargetType.Environment, context));
            default -> {
                // A procedure without documented resource arguments still
                // remains a Procedure/CALL relation.
            }
        }
    }

    private void addSystemPackageTargets(List<BehaviorObject> targets, NameParts name, String procedure, ParserRuleContext context,
                                         DmSqlParser.RoutineArgumentListContext arguments) {
        if ("DBMS_ALERT".equalsIgnoreCase(name.schema())) {
            switch (procedure) {
                case "REGISTER", "REMOVE", "SIGNAL", "WAITONE" -> addNamedStringInstanceTarget(targets, arguments, 0, "NAME", TargetType.Event);
                case "REMOVEALL", "WAITANY" -> addObject(targets, objects.instanceObject(TargetType.Event, context));
                default -> {
                    // SET_DEFAULTS only accepts a compatibility scalar.
                }
            }
            return;
        }
        if ("DBMS_JOB".equalsIgnoreCase(name.schema())) {
            if ("JOB_CONFIG_COMMIT".equals(procedure)) {
                addNamedStringInstanceTarget(targets, arguments, 0, "JOBNAME", TargetType.Job);
            } else {
                addJobTarget(targets, arguments, context);
            }
            return;
        }
        if ("DBMS_MVIEW".equalsIgnoreCase(name.schema()) && "REFRESH".equals(procedure)) {
            addDelimitedNamedRoutineTargets(targets, arguments, 0, "LST", TargetType.Materialized);
            return;
        }
        if ("SYS".equalsIgnoreCase(name.schema()) && ("AWR_REPORT_HTML".equals(procedure) || "AWR_REPORT_TEXT".equals(procedure))) {
            addAwrReportTarget(targets, arguments);
            return;
        }
        if ("DBMS_AQADM".equalsIgnoreCase(name.schema())) {
            addAqadmTargets(targets, procedure, arguments);
            return;
        }
        if ("DBMS_AQ".equalsIgnoreCase(name.schema())) {
            addAqTargets(targets, procedure, arguments);
            return;
        }
        if ("DBMS_RLS".equalsIgnoreCase(name.schema())) {
            addRlsTargets(targets, procedure, context, arguments);
            return;
        }
        if ("DBMS_STATS".equalsIgnoreCase(name.schema())) {
            addStatsTargets(targets, procedure, context, arguments);
            return;
        }
        if ("DBMS_SESSION".equalsIgnoreCase(name.schema())) {
            addSessionTargets(targets, procedure, context, arguments);
            return;
        }
        if ("DBMS_METADATA".equalsIgnoreCase(name.schema()) && "SET_TRANSFORM_PARAM".equals(procedure)) {
            addNamedStringInstanceTarget(targets, arguments, 1, "NAME", TargetType.ConfigKey);
            return;
        }
        if ("DBMS_FLASHBACK".equalsIgnoreCase(name.schema())) {
            addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "FLASHBACK"));
            return;
        }
        if ("DBMS_REDEFINITION".equalsIgnoreCase(name.schema())) {
            addRedefinitionTargets(targets, procedure, arguments);
            return;
        }
        if ("DBMS_PIPE".equalsIgnoreCase(name.schema()) && "PURGE".equals(procedure)) {
            addNamedStringInstanceTarget(targets, arguments, 0, "PIPENAME", TargetType.Pipe);
            return;
        }
        if ("UTL_FILE".equalsIgnoreCase(name.schema())) {
            addUtlFileTargets(targets, procedure, context, arguments);
            return;
        }
        if ("UTL_HTTP".equalsIgnoreCase(name.schema())) {
            addHttpTargets(targets, procedure, context, arguments);
            return;
        }
        if ("DBMS_APPLICATION_INFO".equalsIgnoreCase(name.schema())) {
            switch (procedure) {
                case "SET_CLIENT_INFO", "READ_CLIENT_INFO" -> addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "CLIENT_INFO"));
                case "SET_ACTION" -> addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "ACTION"));
                case "SET_MODULE", "READ_MODULE" -> {
                    addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "MODULE"));
                    addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "ACTION"));
                }
                default -> {
                    // The package has no other public DM8 routines.
                }
            }
            return;
        }
        if ("DBMS_AUDIT".equalsIgnoreCase(name.schema())) {
            if ("ADD_AUDFILE".equals(procedure) || "REMOVE_AUDFILE".equals(procedure)) {
                Token file = stringRoutineArgument(arguments, 0);
                if (file != null) {
                    addObject(targets, fileObject(file));
                }
            } else if ("START_AUD".equals(procedure) || "END_AUD".equals(procedure)) {
                addObject(targets, objects.instanceObject(TargetType.Environment, context));
            }
            return;
        }
        if ("DBMS_LOGMNR".equalsIgnoreCase(name.schema())) {
            if ("ADD_LOGFILE".equals(procedure) || "REMOVE_LOGFILE".equals(procedure)) {
                DmSqlParser.ExpressionContext fileArgument = routineArgumentExpression(arguments, 0, "LOGFILENAME");
                Token file = fileArgument == null ? null : stringArgument(List.of(fileArgument), 0);
                if (file != null) {
                    addObject(targets, fileObject(file));
                } else if (fileArgument != null) {
                    addObject(targets, objects.unnamedObject(TargetType.File, fileArgument, UmiTypes.Instance));
                }
            } else if ("START_LOGMNR".equals(procedure) || "END_LOGMNR".equals(procedure)) {
                addObject(targets, objects.instanceObject(TargetType.Environment, context));
            }
            return;
        }
        if ("DBMS_ERRLOG".equalsIgnoreCase(name.schema()) && "CREATE_ERROR_LOG".equals(procedure)) {
            addErrlogTargets(targets, arguments);
            return;
        }
        if ("DBMS_XMLDOM".equalsIgnoreCase(name.schema()) && "WRITETOFILE".equals(procedure)) {
            Token file = stringRoutineArgument(arguments, 1);
            if (file != null) {
                addObject(targets, fileObject(file));
            }
            return;
        }
        if ("DBMS_XMLPARSER".equalsIgnoreCase(name.schema())) {
            Token file = switch (procedure) {
                case "PARSE", "PARSEDTD" -> stringRoutineArgument(arguments, 1, "URL");
                case "SETBASEDIR" -> stringRoutineArgument(arguments, 1, "DIR");
                default -> null;
            };
            if (file != null) {
                addObject(targets, fileObject(file));
            }
            return;
        }
        if ("DBMS_SYSTEM".equalsIgnoreCase(name.schema()) && "KSDWRT".equals(procedure)) {
            addObject(targets, objects.instanceObject(TargetType.File, context));
        }
    }

    private void addSessionTargets(List<BehaviorObject> targets, String procedure, ParserRuleContext context, DmSqlParser.RoutineArgumentListContext arguments) {
        switch (procedure) {
            case "SET_IDENTIFIER" -> addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, "CLIENT_IDENTIFIER"));
            case "SET_CONTEXT", "CLEAR_CONTEXT", "CLEAR_ALL_CONTEXT" -> addSessionArgumentTarget(targets, arguments, 0, "NAMESPACE", TargetType.ConfigKey);
            case "LIST_CONTEXT", "RESET_PACKAGE" -> addObject(targets, objects.instanceObject(TargetType.Environment, context));
            case "CLOSE_DATABASE_LINK" -> addSessionArgumentTarget(targets, arguments, 0, "DBLINK", TargetType.Link);
            default -> {
                // DBMS_SESSION functions have scalar results and no resource target.
            }
        }
    }

    private void addUtlFileTargets(List<BehaviorObject> targets, String procedure, ParserRuleContext context, DmSqlParser.RoutineArgumentListContext arguments) {
        switch (procedure) {
            case "FREMOVE", "FGETATTR" -> addUtlFileTarget(targets, arguments, 0, "LOCATION", 1, "FILENAME");
            case "FRENAME", "FCOPY" -> {
                addUtlFileTarget(targets, arguments, 0, "SRC_LOCATION", 1, "SRC_FILENAME");
                addUtlFileTarget(targets, arguments, 2, "DEST_LOCATION", 3, "DEST_FILENAME");
            }
            case "FCLOSE_ALL" -> addObject(targets, objects.unnamedObject(TargetType.File, context, UmiTypes.Instance));
            default -> {
                // FILE_TYPE handles and scalar arguments are not named resources.
            }
        }
    }

    private void addAwrReportTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments) {
        Token directory = rlsStringRoutineArgument(arguments, 2, "DEST_DIR");
        Token filename = rlsStringRoutineArgument(arguments, 3, "DEST_FILE");
        String directoryValue = stringValue(directory);
        String filenameValue = stringValue(filename);
        if (directoryValue == null || filenameValue == null) {
            return;
        }
        directoryValue = directoryValue.replaceFirst("^/+", "");
        addObject(targets, objects.instanceObject(TargetType.File, filename, directoryValue + "/" + filenameValue));
    }

    private void addUtlFileTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int locationIndex, String locationName, int filenameIndex,
                                  String filenameName) {
        Token location = rlsStringRoutineArgument(arguments, locationIndex, locationName);
        Token filename = rlsStringRoutineArgument(arguments, filenameIndex, filenameName);
        String locationValue = stringValue(location);
        String filenameValue = stringValue(filename);
        if (locationValue == null || filenameValue == null || "NULL".equalsIgnoreCase(locationValue) || "NULL".equalsIgnoreCase(filenameValue)) {
            return;
        }
        addObject(targets, objects.instanceObject(TargetType.File, filename, locationValue + "/" + filenameValue));
    }

    private void addSessionArgumentTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName, TargetType type) {
        DmSqlParser.ExpressionContext expression = rlsRoutineArgumentExpression(arguments, index, parameterName);
        if (expression == null || "NULL".equalsIgnoreCase(expression.getText())) {
            return;
        }
        Token token = stringArgument(List.of(expression), 0);
        if (token == null) {
            addObject(targets, objects.instanceObject(type, expression));
        } else {
            addObject(targets, objects.instanceObject(type, token, stringValue(token)));
        }
    }

    private void addStatsTargets(List<BehaviorObject> targets, String procedure, ParserRuleContext context, DmSqlParser.RoutineArgumentListContext arguments) {
        switch (procedure) {
            case "COLUMN_STATS_SHOW", "CONV_DATA", "DELETE_COLUMN_STATS", "DELETE_TABLE_STATS", "GATHER_TABLE_STATS", "GET_COLUMN_STATS", "LOCK_TABLE_STATS", "SET_COLUMN_STATS",
                    "SET_TABLE_PREFS", "SET_TABLE_STATS", "TABLE_STATS_SHOW", "UNLOCK_TABLE_STATS" ->
                addStatsTableTarget(targets, arguments, statsPartitionIndex(procedure));
            case "DELETE_INDEX_STATS", "GATHER_INDEX_STATS", "INDEX_STATS_SHOW" -> addStatsIndexTarget(targets, arguments);
            case "DELETE_SCHEMA_STATS", "GATHER_SCHEMA_STATS" -> addStatsSchemaTarget(targets, arguments, 0, "OWNNAME");
            case "CREATE_STAT_TABLE", "DROP_STAT_TABLE" -> addStatsTableTarget(targets, arguments, 0, "STATOWN", 1, "STATTAB", -1, null);
            case "EXPORT_DATABASE_STATS", "IMPORT_DATABASE_STATS" -> {
                addObject(targets, objects.instanceObject(TargetType.Instance, context));
                addStatsTableTarget(targets, arguments, 2, "STATOWN", 0, "STATTAB", -1, null);
            }
            case "FLUSH_DATABASE_MONITORING_INFO", "UPDATE_ALL_STATS" -> addObject(targets, objects.instanceObject(TargetType.Instance, context));
            case "COPY_TABLE_STATS" -> {
                addStatsPartitionTarget(targets, arguments, 0, "OWNNAME", 1, "TABNAME", 2, "SRCPARTNAME");
                addStatsPartitionTarget(targets, arguments, 0, "OWNNAME", 1, "TABNAME", 3, "DSTPARTNAME");
            }
            case "LOCK_PARTITION_STATS", "UNLOCK_PARTITION_STATS" -> addStatsPartitionTarget(targets, arguments, 0, "OWNNAME", 1, "TABNAME", 2, "PARTNAME");
            case "EXPORT_SCHEMA_STATS", "IMPORT_SCHEMA_STATS" -> {
                addStatsSchemaTarget(targets, arguments, 0, "OWNNAME");
                addStatsTableTarget(targets, arguments, 3, "STATOWN", 1, "STATTAB", -1, null);
            }
            case "EXPORT_TABLE_STATS", "IMPORT_TABLE_STATS", "IMPORT_TABLE_STATS_CUSTOM" -> {
                addStatsTableTarget(targets, arguments, 2);
                addStatsTableTarget(targets, arguments, 6, "STATOWN", 3, "STATTAB", -1, null);
            }
            default -> {
                // The remaining DBMS_STATS routines have scalar or record-only arguments.
            }
        }
        if ("SET_TABLE_PREFS".equals(procedure)) {
            addStatsInstanceTarget(targets, arguments, 2, "PPNAME", TargetType.ConfigKey);
        }
    }

    private int statsPartitionIndex(String procedure) {
        return switch (procedure) {
            case "DELETE_COLUMN_STATS", "GET_COLUMN_STATS", "SET_COLUMN_STATS" -> 3;
            case "DELETE_TABLE_STATS", "GATHER_TABLE_STATS", "SET_TABLE_STATS" -> 2;
            default -> -1;
        };
    }

    private void addStatsTableTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int partitionIndex) {
        addStatsTableTarget(targets, arguments, 0, "OWNNAME", 1, "TABNAME", partitionIndex, partitionIndex < 0 ? null : "PARTNAME");
    }

    private void addStatsTableTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int schemaIndex, String schemaName, int tableIndex,
                                     String tableName, int partitionIndex, String partitionName) {
        if (partitionIndex >= 0) {
            Token partition = rlsStringRoutineArgument(arguments, partitionIndex, partitionName);
            if (partition != null) {
                addStatsPartitionTarget(targets, arguments, schemaIndex, schemaName, tableIndex, tableName, partitionIndex, partitionName);
                return;
            }
        }
        Token table = rlsStringRoutineArgument(arguments, tableIndex, tableName);
        Token schema = rlsStringRoutineArgument(arguments, schemaIndex, schemaName);
        addStatsSchemaObjectTarget(targets, TargetType.Table, table, schema);
    }

    private void addStatsIndexTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments) {
        Token index = rlsStringRoutineArgument(arguments, 1, "INDNAME");
        if (index == null) {
            index = rlsStringRoutineArgument(arguments, 1, "INDEXNAME");
        }
        Token schema = rlsStringRoutineArgument(arguments, 0, "OWNNAME");
        addStatsSchemaObjectTarget(targets, TargetType.Index, index, schema);
    }

    private void addStatsSchemaTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName) {
        Token schema = rlsStringRoutineArgument(arguments, index, parameterName);
        String value = stringValue(schema);
        if (value != null && !"NULL".equalsIgnoreCase(value)) {
            addObject(targets, objects.object(TargetType.Schema, schema, List.of(value)));
        }
    }

    private void addStatsPartitionTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int schemaIndex, String schemaName, int tableIndex,
                                         String tableName, int partitionIndex, String partitionName) {
        Token table = rlsStringRoutineArgument(arguments, tableIndex, tableName);
        Token partition = rlsStringRoutineArgument(arguments, partitionIndex, partitionName);
        String tableValue = stringValue(table);
        String partitionValue = stringValue(partition);
        if (tableValue == null || partitionValue == null || "NULL".equalsIgnoreCase(tableValue) || "NULL".equalsIgnoreCase(partitionValue)) {
            return;
        }
        Token schema = rlsStringRoutineArgument(arguments, schemaIndex, schemaName);
        String schemaValue = stringValue(schema);
        if (schemaValue == null || "NULL".equalsIgnoreCase(schemaValue)) {
            Object defaultSchema = levels == null ? null : levels.get(UmiTypes.Schema);
            if (defaultSchema != null) {
                schemaValue = defaultSchema.toString();
            }
        }
        List<String> names = new ArrayList<>();
        Object catalog = levels == null ? null : levels.get(UmiTypes.Catalog);
        if (catalog != null) {
            names.add(catalog.toString());
        }
        if (schemaValue != null) {
            names.add(schemaValue);
        }
        names.add(tableValue);
        names.add(partitionValue);
        addObject(targets, objects.object(TargetType.Partition, partition, names));
    }

    private void addStatsSchemaObjectTarget(List<BehaviorObject> targets, TargetType type, Token object, Token schema) {
        String objectValue = stringValue(object);
        if (objectValue == null || "NULL".equalsIgnoreCase(objectValue)) {
            return;
        }
        String schemaValue = stringValue(schema);
        List<String> names = new ArrayList<>();
        if (schemaValue != null && !"NULL".equalsIgnoreCase(schemaValue)) {
            names.add(schemaValue);
        }
        names.add(objectValue);
        addObject(targets, objects.object(type, object, names));
    }

    private void addStatsInstanceTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName, TargetType type) {
        Token token = rlsStringRoutineArgument(arguments, index, parameterName);
        String value = stringValue(token);
        if (value != null && !"NULL".equalsIgnoreCase(value)) {
            addObject(targets, objects.instanceObject(type, token, value));
        }
    }

    private void addAqadmTargets(List<BehaviorObject> targets, String procedure, DmSqlParser.RoutineArgumentListContext arguments) {
        boolean queueMethod = switch (procedure) {
            case "ADD_SUBSCRIBER", "ALTER_QUEUE", "ALTER_SUBSCRIBER", "CREATE_QUEUE", "DROP_QUEUE", "REMOVE_SUBSCRIBER", "START_QUEUE", "STOP_QUEUE" -> true;
            default -> false;
        };
        boolean queueTableMethod = switch (procedure) {
            case "ALTER_QUEUE_TABLE", "CREATE_QUEUE_TABLE", "DROP_QUEUE_TABLE", "PURGE_QUEUE_TABLE" -> true;
            default -> false;
        };

        Token queue = null;
        List<String> queueNames = List.of();
        if (queueMethod) {
            queue = rlsStringRoutineArgument(arguments, 0, "QUEUE_NAME");
            queueNames = addAqadmNamedTarget(targets, TargetType.Queue, queue);
        }
        if (queueTableMethod || "CREATE_QUEUE".equals(procedure)) {
            int tableIndex = "CREATE_QUEUE".equals(procedure) ? 1 : 0;
            Token queueTable = rlsStringRoutineArgument(arguments, tableIndex, "QUEUE_TABLE");
            addAqadmNamedTarget(targets, TargetType.Table, queueTable);
        }

        if (!"ADD_SUBSCRIBER".equals(procedure) && !"ALTER_SUBSCRIBER".equals(procedure) && !"REMOVE_SUBSCRIBER".equals(procedure)) {
            return;
        }
        DmSqlParser.ExpressionContext subscriberExpression = rlsRoutineArgumentExpression(arguments, 1, "SUBSCRIBER");
        if (subscriberExpression == null) {
            return;
        }
        for (DmSqlParser.FunctionCallContext function : descendants(subscriberExpression, DmSqlParser.FunctionCallContext.class)) {
            NameParts functionName = function.functionName().qualifiedName() == null ? null : NameParts.from(function.functionName().qualifiedName());
            if (functionName == null || !"SYS".equalsIgnoreCase(functionName.schema()) || !"AQ$_AGENT".equalsIgnoreCase(functionName.name())
                || function.functionArguments() == null) {
                continue;
            }
            Token subscriber = stringFunctionArgument(function.functionArguments(), 0, "NAME");
            String subscriberValue = stringValue(subscriber);
            if (subscriberValue == null || "NULL".equalsIgnoreCase(subscriberValue)) {
                return;
            }
            List<String> names = aqadmCompleteNames(queueNames);
            names.add(subscriberValue);
            addObject(targets, objects.object(TargetType.QueueSubscriber, subscriber, names));
            return;
        }
    }

    private void addAqTargets(List<BehaviorObject> targets, String procedure, DmSqlParser.RoutineArgumentListContext arguments) {
        if ("ENQUEUE".equals(procedure) || "DEQUEUE".equals(procedure)) {
            Token queue = rlsStringRoutineArgument(arguments, 0, "QUEUE_NAME");
            addAqadmNamedTarget(targets, TargetType.Queue, queue);
            return;
        }
        if (!"REGISTER".equals(procedure) && !"UNREGISTER".equals(procedure)) {
            return;
        }
        DmSqlParser.ExpressionContext registrationList = rlsRoutineArgumentExpression(arguments, 0, "REG_LIST");
        if (registrationList == null) {
            return;
        }
        for (DmSqlParser.FunctionCallContext function : descendants(registrationList, DmSqlParser.FunctionCallContext.class)) {
            NameParts functionName = function.functionName().qualifiedName() == null ? null : NameParts.from(function.functionName().qualifiedName());
            if (functionName == null || !"SYS".equalsIgnoreCase(functionName.schema()) || !"AQ$_REG_INFO".equalsIgnoreCase(functionName.name())
                || function.functionArguments() == null) {
                continue;
            }
            Token registration = stringFunctionArgument(function.functionArguments(), 0, "NAME");
            String value = stringValue(registration);
            if (value == null || "NULL".equalsIgnoreCase(value)) {
                return;
            }
            List<String> queueAndSubscriber = splitUnquoted(value, ':');
            List<String> queueNames = splitUnquoted(queueAndSubscriber.get(0), '.').stream().map(NameParts::clean).filter(part -> !part.isEmpty()).toList();
            List<String> names = aqadmCompleteNames(queueNames);
            if (queueAndSubscriber.size() > 1) {
                String subscriber = NameParts.clean(queueAndSubscriber.get(1));
                if (!subscriber.isEmpty()) {
                    names.add(subscriber);
                }
            }
            if (!names.isEmpty()) {
                addObject(targets, objects.object(TargetType.QueueSubscriber, registration, names));
            }
            return;
        }
    }

    private List<String> addAqadmNamedTarget(List<BehaviorObject> targets, TargetType type, Token token) {
        String value = stringValue(token);
        if (value == null || "NULL".equalsIgnoreCase(value)) {
            return List.of();
        }
        List<String> names = splitUnquoted(value, '.').stream().map(NameParts::clean).filter(part -> !part.isEmpty()).toList();
        if (!names.isEmpty()) {
            addObject(targets, objects.object(type, token, names));
        }
        return names;
    }

    private List<String> aqadmCompleteNames(List<String> names) {
        List<String> result = new ArrayList<>();
        if (names.size() == 1) {
            Object catalog = levels == null ? null : levels.get(UmiTypes.Catalog);
            Object schema = levels == null ? null : levels.get(UmiTypes.Schema);
            if (catalog != null) {
                result.add(catalog.toString());
            }
            if (schema != null) {
                result.add(schema.toString());
            }
        } else if (names.size() == 2) {
            Object catalog = levels == null ? null : levels.get(UmiTypes.Catalog);
            if (catalog != null) {
                result.add(catalog.toString());
            }
        }
        result.addAll(names);
        return result;
    }

    private void addRlsTargets(List<BehaviorObject> targets, String procedure, ParserRuleContext context, DmSqlParser.RoutineArgumentListContext arguments) {
        switch (procedure) {
            case "ADD_POLICY" -> {
                addRlsTableTarget(targets, arguments);
                addRlsSchemaObjectTarget(targets, arguments, 0, "OBJECT_SCHEMA", 2, "POLICY_NAME", TargetType.RowAccessPolicy);
                addRlsSchemaObjectTarget(targets, arguments, 3, "FUNCTION_SCHEMA", 4, "POLICY_FUNCTION", TargetType.Function);
            }
            case "ADD_GROUPED_POLICY" -> {
                addRlsTableTarget(targets, arguments);
                addRlsInstanceTarget(targets, arguments, 2, "POLICY_GROUP", TargetType.Policy);
                addRlsSchemaObjectTarget(targets, arguments, 0, "OBJECT_SCHEMA", 3, "POLICY_NAME", TargetType.RowAccessPolicy);
                addRlsSchemaObjectTarget(targets, arguments, 4, "FUNCTION_SCHEMA", 5, "POLICY_FUNCTION", TargetType.Function);
            }
            case "DROP_POLICY", "REFRESH_POLICY", "ENABLE_POLICY" -> {
                addRlsTableTarget(targets, arguments);
                addRlsSchemaObjectTarget(targets, arguments, 0, "OBJECT_SCHEMA", 2, "POLICY_NAME", TargetType.RowAccessPolicy);
            }
            case "DROP_GROUPED_POLICY", "REFRESH_GROUPED_POLICY", "ENABLE_GROUPED_POLICY", "DISABLE_GROUPED_POLICY" -> {
                addRlsTableTarget(targets, arguments);
                String groupParameter = "DROP_GROUPED_POLICY".equals(procedure) ? "POLICY_GROUP" : "GROUP_NAME";
                Token group = rlsStringRoutineArgument(arguments, 2, groupParameter);
                if (group == null && "DROP_GROUPED_POLICY".equals(procedure)) {
                    addObject(targets, objects.instanceObject(TargetType.Policy, context, "SYS_DEFAULT"));
                } else if (group != null) {
                    addObject(targets, objects.instanceObject(TargetType.Policy, group, stringValue(group)));
                }
                addRlsSchemaObjectTarget(targets, arguments, 0, "OBJECT_SCHEMA", 3, "POLICY_NAME", TargetType.RowAccessPolicy);
            }
            case "CREATE_POLICY_GROUP", "DELETE_POLICY_GROUP" -> {
                addRlsTableTarget(targets, arguments);
                addRlsInstanceTarget(targets, arguments, 2, "POLICY_GROUP", TargetType.Policy);
            }
            case "ADD_POLICY_CONTEXT", "DROP_POLICY_CONTEXT" -> {
                addRlsTableTarget(targets, arguments);
                addRlsInstanceTarget(targets, arguments, 2, "NAMESPACE", TargetType.ConfigKey);
            }
            case "ADD_MASK" -> {
                addRlsTableTarget(targets, arguments);
                DmSqlParser.ExpressionContext packageName = rlsRoutineArgumentExpression(arguments, 4, "PACKAGE_NAME");
                if (packageName == null || "NULL".equalsIgnoreCase(packageName.getText())) {
                    addRlsSchemaObjectTarget(targets, arguments, 3, "FUNCTION_SCHEMA", 5, "FUNCTION_NAME", TargetType.Function);
                }
            }
            case "DROP_MASK" -> addRlsTableTarget(targets, arguments);
            default -> {
                // DBMS_RLS scalar-only or future procedures retain the package call relation.
            }
        }
    }

    private void addRlsTableTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments) {
        addRlsSchemaObjectTarget(targets, arguments, 0, "OBJECT_SCHEMA", 1, "OBJECT_NAME", TargetType.Table);
    }

    private void addRlsSchemaObjectTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int schemaIndex, String schemaName, int objectIndex,
                                          String objectName, TargetType type) {
        Token object = rlsStringRoutineArgument(arguments, objectIndex, objectName);
        String objectValue = stringValue(object);
        if (objectValue == null || objectValue.equalsIgnoreCase("NULL")) {
            return;
        }
        Token schema = rlsStringRoutineArgument(arguments, schemaIndex, schemaName);
        String schemaValue = stringValue(schema);
        if (schemaValue == null || schemaValue.equalsIgnoreCase("NULL")) {
            addObject(targets, objects.object(type, object, List.of(objectValue)));
            return;
        }
        addObject(targets, objects.object(type, object, List.of(schemaValue, objectValue)));
    }

    private void addRlsInstanceTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName, TargetType type) {
        Token token = rlsStringRoutineArgument(arguments, index, parameterName);
        String value = stringValue(token);
        if (value != null && !value.equalsIgnoreCase("NULL")) {
            addObject(targets, objects.instanceObject(type, token, value));
        }
    }

    private void addHttpTargets(List<BehaviorObject> targets, String procedure, ParserRuleContext context, DmSqlParser.RoutineArgumentListContext arguments) {
        String configKey = switch (procedure) {
            case "GET_PROXY", "SET_PROXY" -> "PROXY";
            case "GET_PERSISTENT_CONN_SUPPORT" -> "PERSISTENT_CONN_SUPPORT";
            case "GET_DETAILED_EXCP_SUPPORT", "SET_DETAILED_EXCP_SUPPORT" -> "DETAILED_EXCP_SUPPORT";
            case "SET_RESPONSE_ERROR_CHECK" -> "RESPONSE_ERROR_CHECK";
            case "SET_WALLET" -> "WALLET";
            default -> null;
        };
        if ("SET_BODY_CHARSET".equals(procedure) && isHttpSessionOverload(arguments, "CHARSET")) {
            configKey = "BODY_CHARSET";
        } else if ("SET_TRANSFER_TIMEOUT".equals(procedure) && isHttpSessionOverload(arguments, "TIMEOUT")) {
            configKey = "TRANSFER_TIMEOUT";
        } else if ("SET_PERSISTENT_CONN_SUPPORT".equals(procedure) && isHttpSessionOverload(arguments, "ENABLE_FLAG")) {
            configKey = "PERSISTENT_CONN_SUPPORT";
        }
        if (configKey != null) {
            addObject(targets, objects.instanceObject(TargetType.ConfigKey, context, configKey));
        }
        if ("SET_WALLET".equals(procedure)) {
            Token file = stringRoutineArgument(arguments, 0, "PATH");
            if (file != null) {
                addObject(targets, fileObject(file));
            }
        }
    }

    private boolean isHttpSessionOverload(DmSqlParser.RoutineArgumentListContext arguments, String sessionParameter) {
        if (arguments == null || arguments.routineArgument().isEmpty()) {
            return true;
        }
        boolean hasSessionParameter = false;
        for (DmSqlParser.RoutineArgumentContext argument : arguments.routineArgument()) {
            if (argument.namedArgument() == null) {
                continue;
            }
            String name = argument.namedArgument().dottedNamePart().getText();
            if ("R".equalsIgnoreCase(name)) {
                return false;
            }
            if (sessionParameter.equalsIgnoreCase(name)) {
                hasSessionParameter = true;
            }
        }
        if (hasSessionParameter) {
            return true;
        }
        String first = arguments.routineArgument(0).getText();
        if ("CHARSET".equals(sessionParameter)) {
            return first.startsWith("'");
        }
        if ("TIMEOUT".equals(sessionParameter)) {
            return first.matches("[+-]?\\d+(?:\\.\\d+)?");
        }
        return "TRUE".equalsIgnoreCase(first) || "FALSE".equalsIgnoreCase(first) || "NULL".equalsIgnoreCase(first);
    }

    private void addRedefinitionTargets(List<BehaviorObject> targets, String procedure, DmSqlParser.RoutineArgumentListContext arguments) {
        switch (procedure) {
            case "ABORT_REDEF_TABLE", "COPY_TABLE_DEPENDENTS", "FINISH_REDEF_TABLE", "START_REDEF_TABLE", "SYNC_INTERIM_TABLE" -> {
                addRedefinitionTableTarget(targets, arguments, 1, "ORIG_TABLE");
                addRedefinitionTableTarget(targets, arguments, 2, "INT_TABLE");
            }
            case "CAN_REDEF_TABLE" -> addRedefinitionTableTarget(targets, arguments, 1, "TNAME");
            case "DELETE_REDUNDANT_RECORD" -> {
                DmSqlParser.ExpressionContext objectId = routineArgumentExpression(arguments, 0, "OBJ_ID");
                if (objectId != null) {
                    addObject(targets, objects.unnamedObject(TargetType.Table, objectId, UmiTypes.Catalog));
                }
            }
            case "SET_PARAM" -> {
                DmSqlParser.ExpressionContext redefinitionId = routineArgumentExpression(arguments, 0, "REDEFINITION_ID");
                if (redefinitionId != null) {
                    addObject(targets, objects.unnamedObject(TargetType.Table, redefinitionId, UmiTypes.Catalog));
                }
                addNamedStringInstanceTarget(targets, arguments, 1, "PARAM_NAME", TargetType.ConfigKey);
            }
            default -> {
                // The package has no other public DM8 routines.
            }
        }
    }

    private void addRedefinitionTableTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int tableIndex, String tableParameter) {
        Token schema = stringRoutineArgument(arguments, 0, "UNAME");
        Token table = stringRoutineArgument(arguments, tableIndex, tableParameter);
        if (table == null) {
            return;
        }
        List<String> names = new ArrayList<>();
        if (schema != null) {
            names.add(stringValue(schema));
        }
        names.add(stringValue(table));
        addObject(targets, objects.object(TargetType.Table, table, names));
    }

    private void addJobTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, ParserRuleContext context) {
        DmSqlParser.ExpressionContext job = routineArgumentExpression(arguments, 0, "JOB");
        if (job != null && job.getText().matches("\\d+")) {
            addObject(targets, objects.instanceObject(TargetType.Job, job, job.getText()));
            return;
        }
        addObject(targets, objects.instanceObject(TargetType.Job, context));
    }

    private void addSchedulerTargets(List<BehaviorObject> targets, String procedure, DmSqlParser.RoutineArgumentListContext arguments) {
        switch (procedure) {
            case "CREATE_SCHEDULE", "DROP_SCHEDULE" -> addNamedStringRoutineTarget(targets, arguments, 0, "SCHEDULE_NAME", TargetType.Event);
            case "CREATE_PROGRAM" -> {
                addNamedStringRoutineTarget(targets, arguments, 0, "PROGRAM_NAME", TargetType.ProgramObject);
                String programType = stringValue(stringRoutineArgument(arguments, 1, "PROGRAM_TYPE"));
                if ("STORED_PROCEDURE".equalsIgnoreCase(programType)) {
                    addNamedStringRoutineTarget(targets, arguments, 2, "PROGRAM_ACTION", TargetType.Procedure);
                }
            }
            case "DEFINE_PROGRAM_ARGUMENT", "DROP_PROGRAM" -> addNamedStringRoutineTarget(targets, arguments, 0, "PROGRAM_NAME", TargetType.ProgramObject);
            case "CREATE_JOB" -> {
                addNamedStringRoutineTarget(targets, arguments, 0, "JOB_NAME", TargetType.Job);
                Token program = namedStringRoutineArgument(arguments, "PROGRAM_NAME");
                Token schedule = namedStringRoutineArgument(arguments, "SCHEDULE_NAME");
                Token jobType = namedStringRoutineArgument(arguments, "JOB_TYPE");
                Token jobAction = namedStringRoutineArgument(arguments, "JOB_ACTION");
                if (program == null && jobType == null) {
                    Token second = stringRoutineArgument(arguments, 1);
                    String value = stringValue(second);
                    if ("PLSQL_BLOCK".equalsIgnoreCase(value) || "STORED_PROCEDURE".equalsIgnoreCase(value)) {
                        jobType = second;
                        jobAction = stringRoutineArgument(arguments, 2);
                    } else {
                        program = second;
                        schedule = stringRoutineArgument(arguments, 2);
                    }
                }
                if (program != null) {
                    addNamedObject(targets, TargetType.ProgramObject, program);
                }
                if (schedule != null) {
                    addNamedObject(targets, TargetType.Event, schedule);
                }
                if ("STORED_PROCEDURE".equalsIgnoreCase(stringValue(jobType)) && jobAction != null) {
                    addNamedObject(targets, TargetType.Procedure, jobAction);
                }
            }
            case "SET_JOB_ARGUMENT_VALUE", "DROP_JOB", "RUN_JOB", "STOP_JOB", "ADD_JOB_EMAIL_NOTIFICATION", "REMOVE_JOB_EMAIL_NOTIFICATION" ->
                addNamedStringRoutineTarget(targets, arguments, 0, "JOB_NAME", TargetType.Job);
            case "GET_ATTRIBUTE", "SET_ATTRIBUTE", "ENABLE", "DISABLE" -> addNamedStringRoutineTarget(targets, arguments, 0, "NAME", TargetType.SchedulerObject);
            case "GET_SCHEDULER_ATTRIBUTE", "SET_SCHEDULER_ATTRIBUTE" -> addNamedStringRoutineTarget(targets, arguments, 0, "ATTRIBUTE", TargetType.ConfigKey);
            case "PURGE_LOG" -> addNamedStringRoutineTarget(targets, arguments, 2, "JOB_NAME", TargetType.Job);
            default -> {
                // The remaining scheduler routines have scalar-only arguments.
            }
        }
    }

    private Token stringRoutineArgument(DmSqlParser.RoutineArgumentListContext arguments, int index) {
        return stringRoutineArgument(arguments, index, null);
    }

    private Token stringRoutineArgument(DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName) {
        DmSqlParser.ExpressionContext expression = routineArgumentExpression(arguments, index, parameterName);
        if (expression == null) {
            return null;
        }
        return stringArgument(List.of(expression), 0);
    }

    private Token rlsStringRoutineArgument(DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName) {
        DmSqlParser.ExpressionContext expression = rlsRoutineArgumentExpression(arguments, index, parameterName);
        if (expression == null) {
            return null;
        }
        return stringArgument(List.of(expression), 0);
    }

    private DmSqlParser.ExpressionContext rlsRoutineArgumentExpression(DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName) {
        if (arguments == null) {
            return null;
        }
        for (DmSqlParser.RoutineArgumentContext argument : arguments.routineArgument()) {
            if (argument.namedArgument() != null && parameterName.equalsIgnoreCase(argument.namedArgument().dottedNamePart().getText())) {
                return argument.namedArgument().expression();
            }
        }
        if (index >= arguments.routineArgument().size()) {
            return null;
        }
        DmSqlParser.RoutineArgumentContext argument = arguments.routineArgument(index);
        if (argument.namedArgument() != null) {
            return null;
        }
        return argument.expression();
    }

    private DmSqlParser.ExpressionContext routineArgumentExpression(DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName) {
        if (arguments == null) {
            return null;
        }
        if (parameterName != null) {
            for (DmSqlParser.RoutineArgumentContext argument : arguments.routineArgument()) {
                if (argument.namedArgument() != null && parameterName.equalsIgnoreCase(argument.namedArgument().dottedNamePart().getText())) {
                    return argument.namedArgument().expression();
                }
            }
        }
        if (index >= arguments.routineArgument().size()) {
            return null;
        }
        DmSqlParser.RoutineArgumentContext argument = arguments.routineArgument(index);
        return argument.expression();
    }

    private Token namedStringRoutineArgument(DmSqlParser.RoutineArgumentListContext arguments, String parameterName) {
        if (arguments == null) {
            return null;
        }
        for (DmSqlParser.RoutineArgumentContext argument : arguments.routineArgument()) {
            if (argument.namedArgument() != null && parameterName.equalsIgnoreCase(argument.namedArgument().dottedNamePart().getText())) {
                return stringArgument(List.of(argument.namedArgument().expression()), 0);
            }
        }
        return null;
    }

    private void addStringRoutineTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, TargetType type) {
        Token token = stringRoutineArgument(arguments, index);
        if (token != null) {
            addObject(targets, objects.instanceObject(type, token, stringValue(token)));
        }
    }

    private void addNamedStringInstanceTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName, TargetType type) {
        Token token = stringRoutineArgument(arguments, index, parameterName);
        if (token != null) {
            addObject(targets, objects.instanceObject(type, token, stringValue(token)));
        }
    }

    private void addNamedStringRoutineTarget(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName, TargetType type) {
        Token token = stringRoutineArgument(arguments, index, parameterName);
        if (token != null) {
            addNamedObject(targets, type, token);
        }
    }

    private void addDelimitedNamedRoutineTargets(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, String parameterName, TargetType type) {
        Token token = stringRoutineArgument(arguments, index, parameterName);
        String value = stringValue(token);
        if (value == null || value.equalsIgnoreCase("NULL")) {
            return;
        }
        for (String member : splitUnquoted(value, ',')) {
            List<String> names = splitUnquoted(member.trim(), '.').stream().map(NameParts::clean).toList();
            if (!names.isEmpty() && names.stream().noneMatch(String::isEmpty)) {
                addObject(targets, objects.object(type, token, names));
            }
        }
    }

    private List<String> splitUnquoted(String value, char separator) {
        List<String> parts = new ArrayList<>();
        boolean quoted = false;
        int start = 0;
        for (int index = 0; index < value.length(); index++) {
            if (value.charAt(index) == '"') {
                if (quoted && index + 1 < value.length() && value.charAt(index + 1) == '"') {
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (!quoted && value.charAt(index) == separator) {
                parts.add(value.substring(start, index));
                start = index + 1;
            }
        }
        parts.add(value.substring(start));
        return parts;
    }

    private void addNamedObject(List<BehaviorObject> targets, TargetType type, Token token) {
        String value = stringValue(token);
        if (value == null || value.equalsIgnoreCase("NULL")) {
            return;
        }
        if (type == TargetType.ConfigKey) {
            addObject(targets, objects.instanceObject(type, token, value));
            return;
        }
        List<String> names = Arrays.stream(value.split("\\.")).map(NameParts::clean).toList();
        addObject(targets, objects.object(type, token, names));
    }

    private void addErrlogTargets(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments) {
        Token source = stringRoutineArgument(arguments, 0, "DML_TABLE_NAME");
        String sourceValue = stringValue(source);
        if (sourceValue == null || sourceValue.equalsIgnoreCase("NULL")) {
            return;
        }
        List<String> sourceNames = splitUnquoted(sourceValue, '.').stream().map(NameParts::clean).filter(part -> !part.isEmpty()).toList();
        if (sourceNames.isEmpty()) {
            return;
        }
        addObject(targets, objects.object(TargetType.Table, source, sourceNames));

        Token errorTable = stringRoutineArgument(arguments, 1, "ERR_LOG_TABLE_NAME");
        String errorTableName = stringValue(errorTable);
        if (errorTableName == null || errorTableName.equalsIgnoreCase("NULL")) {
            errorTable = source;
            errorTableName = "ERR$_" + sourceNames.get(sourceNames.size() - 1);
        }
        Token owner = stringRoutineArgument(arguments, 2, "ERR_LOG_TABLE_OWNER");
        String ownerName = stringValue(owner);
        List<String> errorNames = new ArrayList<>();
        if (ownerName != null && !ownerName.equalsIgnoreCase("NULL")) {
            errorNames.add(NameParts.clean(ownerName));
        }
        errorNames.add(NameParts.clean(errorTableName));
        addObject(targets, objects.object(TargetType.Table, errorTable, errorNames));

        Token tablespace = stringRoutineArgument(arguments, 3, "ERR_LOG_TABLE_SPACE");
        String tablespaceName = stringValue(tablespace);
        if (tablespaceName != null && !tablespaceName.equalsIgnoreCase("NULL")) {
            addObject(targets, objects.instanceObject(TargetType.Tablespace, tablespace, tablespaceName));
        }
    }

    private void addDelimitedStringRoutineTargets(List<BehaviorObject> targets, DmSqlParser.RoutineArgumentListContext arguments, int index, TargetType type) {
        Token token = stringRoutineArgument(arguments, index);
        if (token == null) {
            return;
        }
        String text = token.getText();
        int start = text.startsWith("'") ? 1 : 0;
        int limit = text.endsWith("'") ? text.length() - 1 : text.length();
        while (start < limit) {
            int separator = text.indexOf('/', start);
            int end = separator < 0 || separator >= limit ? limit : separator;
            if (end > start) {
                CommonToken member = new CommonToken(token);
                member.setText(text.substring(start, end));
                member.setCharPositionInLine(token.getCharPositionInLine() + start);
                member.setStartIndex(token.getStartIndex() + start);
                member.setStopIndex(token.getStartIndex() + end - 1);
                addObject(targets, objects.instanceObject(type, member, member.getText()));
            }
            start = end + 1;
        }
    }

    @Override
    public Void visitTransactionStatement(DmSqlParser.TransactionStatementContext ctx) {
        behavior.setStatementType(SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitExplainStatement(DmSqlParser.ExplainStatementContext ctx) {
        for (BehaviorObject table : tableSources(ctx)) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, table);
        }
        addFunctionCalls(ctx);
        return null;
    }

    @Override
    public Void visitLockTableStatement(DmSqlParser.LockTableStatementContext ctx) {
        add(SplitQueryType.SESSION_LOCK, BehaviorAction.LOCK, object(TargetType.Table, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitSetSchemaStatement(DmSqlParser.SetSchemaStatementContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitSetTimeZoneStatement(DmSqlParser.SetTimeZoneStatementContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects
            .instanceObject(TargetType.ConfigKey, ctx.TIME().getSymbol(), ctx.ZONE().getSymbol(), "TIME_ZONE"));
        return null;
    }

    @Override
    public Void visitAlterSessionParallelDmlStatement(DmSqlParser.AlterSessionParallelDmlStatementContext ctx) {
        Token feature = ctx.DML() == null ? null : ctx.DML().getSymbol();
        if (feature == null && ctx.DDL() != null) {
            feature = ctx.DDL().getSymbol();
        }
        if (feature == null) {
            feature = ctx.QUERY().getSymbol();
        }
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects
            .instanceObject(TargetType.ConfigKey, ctx.PARALLEL().getSymbol(), feature, "PARALLEL_" + feature.getText().toUpperCase(Locale.ROOT)));
        return null;
    }

    @Override
    public Void visitSetIdentityInsertStatement(DmSqlParser.SetIdentityInsertStatementContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, object(TargetType.Table, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitExecuteImmediateStatement(DmSqlParser.ExecuteImmediateStatementContext ctx) {
        add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, objects.instanceObject(TargetType.PrepareStatement, ctx.getStart()));
        return null;
    }

    @Override
    public Void visitConfigWriteStatement(DmSqlParser.ConfigWriteStatementContext ctx) {
        DmSqlParser.ConfigKeyContext key;
        SplitQueryType type;
        if (ctx.configAssignment() != null) {
            key = ctx.configAssignment().configKey();
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        } else if (ctx.sessionConfigAssignment() != null) {
            key = ctx.sessionConfigAssignment().configKey();
            type = SplitQueryType.SESSION_SETTING_WRITE;
        } else {
            addConfigWriteProcedure(ctx);
            return null;
        }
        String keyName = NameParts.clean(key.getText());
        if (key.STRING() != null) {
            keyName = stringValue(key.STRING().getSymbol());
        }
        add(type, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, key, keyName));
        addFunctionCalls(ctx);
        return null;
    }

    private void addConfigWriteProcedure(DmSqlParser.ConfigWriteStatementContext ctx) {
        DmSqlParser.ConfigWriteProcedureContext procedure = ctx.configWriteProcedure();
        String procedureName = NameParts.clean(procedure.getText());
        List<DmSqlParser.ExpressionContext> arguments = ctx.expressionList() == null ? List.of() : ctx.expressionList().expression();
        List<BehaviorObject> targets = new ArrayList<>();
        SplitQueryType type = SplitQueryType.SYSTEM_SETTING_WRITE;
        int keyIndex = 1;
        if (procedureName.equalsIgnoreCase("sf_set_session_para_value") || procedureName.equalsIgnoreCase("sp_reset_session_para_value")) {
            type = SplitQueryType.SESSION_SETTING_WRITE;
            keyIndex = 0;
        } else if (procedureName.equalsIgnoreCase("sp_set_param_in_session")) {
            type = SplitQueryType.SESSION_SETTING_WRITE;
            keyIndex = 2;
        } else if (procedureName.equalsIgnoreCase("sf_set_system_para_value")) {
            keyIndex = 0;
        } else if (procedureName.equalsIgnoreCase("sp_set_session_readonly")) {
            type = SplitQueryType.SESSION_SETTING_WRITE;
            addObject(targets, objects.instanceObject(TargetType.ConfigKey, procedure, "SESSION_READONLY"));
            keyIndex = -1;
        }
        if (keyIndex >= 0) {
            Token key = stringArgument(arguments, keyIndex);
            if (key != null) {
                addObject(targets, objects.instanceObject(TargetType.ConfigKey, key, stringValue(key)));
            }
        }
        add(type, BehaviorAction.CALL, object(TargetType.Procedure, procedure, new NameParts(null, null, procedureName)), targets);
        addFunctionCalls(ctx);
        for (BehaviorObject source : tableSources(ctx)) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, source);
        }
    }

    @Override
    public Void visitSqlBlockStatement(DmSqlParser.SqlBlockStatementContext ctx) {
        behavior.setStatementType(SplitQueryType.BLOCK);
        blockLocals.push(blockLocalNames(ctx));
        blockLocalTypes.push(blockLocalTypes(ctx));
        try {
            for (DmSqlParser.PackageVariableDeclarationContext declaration : descendants(ctx, DmSqlParser.PackageVariableDeclarationContext.class)) {
                DmSqlParser.DeclarationDataTypeContext declarationType = declaration.declarationDataType();
                if (declarationType == null || declarationType.dataType() == null || !isUserDefinedDataType(declarationType.dataType())) {
                    continue;
                }
                DmSqlParser.QualifiedNameContext type = declarationType.dataType().qualifiedName();
                add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Type, type, schemaScoped(NameParts.from(type))));
            }
            addNestedStatements(ctx);
            addFunctionCalls(ctx);
            return null;
        } finally {
            blockLocalTypes.pop();
            blockLocals.pop();
        }
    }

    @Override
    public Void visitCStyleBlockStatement(DmSqlParser.CStyleBlockStatementContext ctx) {
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.BLOCK);
        }
        addNestedStatements(ctx);
        addFunctionCalls(ctx);
        return null;
    }

    private Set<String> packageFunctionMembers(ParseTree tree) {
        Set<String> names = new HashSet<>();
        for (DmSqlParser.PackageFunctionDeclarationContext declaration : descendants(tree, DmSqlParser.PackageFunctionDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageFunctionImplementationContext implementation : descendants(tree, DmSqlParser.PackageFunctionImplementationContext.class)) {
            names.add(NameParts.clean(implementation.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        return names;
    }

    private Set<String> packageProcedureMembers(ParseTree tree) {
        Set<String> names = new HashSet<>();
        for (DmSqlParser.PackageProcedureDeclarationContext declaration : descendants(tree, DmSqlParser.PackageProcedureDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageProcedureImplementationContext implementation : descendants(tree, DmSqlParser.PackageProcedureImplementationContext.class)) {
            names.add(NameParts.clean(implementation.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        return names;
    }

    private boolean isPackageFunctionMember(NameParts name) {
        return name.catalog() == null && name.schema() == null && !packageFunctionMembers.isEmpty() && packageFunctionMembers.peek().contains(name.name().toLowerCase(Locale.ROOT));
    }

    private boolean isPackageProcedureMember(NameParts name) {
        return name.catalog() == null && name.schema() == null && !packageProcedureMembers.isEmpty()
               && packageProcedureMembers.peek().contains(name.name().toLowerCase(Locale.ROOT));
    }

    private NameParts packageMemberName(String member) {
        NameParts owner = packageScopes.peek();
        if (owner.schema() == null) {
            return new NameParts(null, owner.name(), member);
        }
        return new NameParts(owner.schema(), owner.name(), member);
    }

    private boolean isJavaConstructorDelegate(NameParts name) {
        if (javaClassDepth == 0 || name.catalog() != null || name.schema() != null) {
            return false;
        }
        return "THIS".equalsIgnoreCase(name.name()) || "SUPER".equalsIgnoreCase(name.name());
    }

    private NameParts superMethodName(DmSqlParser.PostfixOperatorContext postfix, String member) {
        if (javaParentTypes.isEmpty() || !(postfix.getParent() instanceof DmSqlParser.PostfixExpressionContext expression) || expression.primaryExpression().functionCall() == null
            || !"SUPER".equalsIgnoreCase(expression.primaryExpression().functionCall().functionName().getText())) {
            return null;
        }
        NameParts parent = javaParentTypes.peek();
        if (parent.schema() == null) {
            return new NameParts(null, parent.name(), member);
        }
        return new NameParts(parent.schema(), parent.name(), member);
    }

    private Set<String> blockLocalNames(ParserRuleContext ctx) {
        Set<String> names = new HashSet<>();
        for (DmSqlParser.BlockDeclarationContext declaration : descendants(ctx, DmSqlParser.BlockDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.getStart().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageVariableDeclarationContext declaration : descendants(ctx, DmSqlParser.PackageVariableDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.BlockTypeDeclarationContext declaration : descendants(ctx, DmSqlParser.BlockTypeDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageCursorDeclarationContext declaration : descendants(ctx, DmSqlParser.PackageCursorDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageProcedureDeclarationContext declaration : descendants(ctx, DmSqlParser.PackageProcedureDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageFunctionDeclarationContext declaration : descendants(ctx, DmSqlParser.PackageFunctionDeclarationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageProcedureImplementationContext declaration : descendants(ctx, DmSqlParser.PackageProcedureImplementationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        for (DmSqlParser.PackageFunctionImplementationContext declaration : descendants(ctx, DmSqlParser.PackageFunctionImplementationContext.class)) {
            names.add(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT));
        }
        String source = ctx.getStart().getInputStream().getText(Interval.of(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex()));
        java.util.regex.Matcher begin = java.util.regex.Pattern.compile("(?i)\\bBEGIN\\b").matcher(source);
        if (begin.find()) {
            java.util.regex.Matcher declaration = java.util.regex.Pattern.compile("(?i)(?:\\bDECLARE\\b|;)\\s*([A-Z_][A-Z0-9_$#]*)\\s+")
                .matcher(source.substring(0, begin.start()));
            while (declaration.find()) {
                names.add(declaration.group(1).toLowerCase(Locale.ROOT));
            }
        }
        return names;
    }

    private Map<String, String> blockLocalTypes(DmSqlParser.SqlBlockStatementContext ctx) {
        Map<String, String> types = new HashMap<>();
        for (DmSqlParser.PackageVariableDeclarationContext declaration : descendants(ctx, DmSqlParser.PackageVariableDeclarationContext.class)) {
            DmSqlParser.DeclarationDataTypeContext declarationType = declaration.declarationDataType();
            if (declarationType == null || declarationType.dataType() == null || declarationType.dataType().qualifiedName() == null) {
                continue;
            }
            NameParts type = NameParts.from(declarationType.dataType().qualifiedName());
            if (type.name() != null) {
                List<String> parts = new ArrayList<>();
                if (type.catalog() != null) {
                    parts.add(type.catalog());
                }
                if (type.schema() != null) {
                    parts.add(type.schema());
                }
                parts.add(type.name());
                types.put(NameParts.clean(declaration.identifier().getText()).toLowerCase(Locale.ROOT), String.join(".", parts));
            }
        }
        return types;
    }

    private String localMethodType(NameParts name) {
        if (name == null || name.catalog() != null || name.schema() == null || blockLocalTypes.isEmpty()) {
            return null;
        }
        return blockLocalTypes.peek().get(name.schema().toLowerCase(Locale.ROOT));
    }

    private BehaviorObject localMethodObject(TargetType targetType, ParserRuleContext context, String localType, String method) {
        List<String> names = Arrays.stream(localType.split("\\.")).map(NameParts::clean).filter(part -> !part.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        if (names.size() < 3 && levels != null && levels.get(UmiTypes.Catalog) != null) {
            names.add(0, levels.get(UmiTypes.Catalog).toString());
        }
        names.add(method);
        return objects.object(targetType, context, names);
    }

    private String localReceiverType(Token method) {
        if (blockLocalTypes.isEmpty() || method.getStartIndex() <= 0) {
            return null;
        }
        String prefix = method.getInputStream().getText(Interval.of(0, method.getStartIndex() - 1));
        int index = prefix.length() - 1;
        while (index >= 0 && Character.isWhitespace(prefix.charAt(index))) {
            index--;
        }
        if (index < 0 || prefix.charAt(index) != '.') {
            return null;
        }
        index--;
        while (index >= 0 && Character.isWhitespace(prefix.charAt(index))) {
            index--;
        }
        int end = index + 1;
        while (index >= 0) {
            char value = prefix.charAt(index);
            if (!Character.isLetterOrDigit(value) && value != '_' && value != '$' && value != '#') {
                break;
            }
            index--;
        }
        if (end <= index + 1) {
            return null;
        }
        String receiver = prefix.substring(index + 1, end).toLowerCase(Locale.ROOT);
        return blockLocalTypes.peek().get(receiver);
    }

    private boolean isBlockLocal(NameParts name) {
        if (blockLocals.isEmpty() || name == null) {
            return false;
        }
        if (isExactBlockLocal(name)) {
            return true;
        }
        String root = name.catalog();
        if (root == null) {
            root = name.schema();
        }
        if (root == null) {
            root = name.name();
        }
        return root != null && blockLocals.peek().contains(root.toLowerCase(Locale.ROOT));
    }

    private boolean isExactBlockLocal(NameParts name) {
        if (blockLocals.isEmpty() || name == null) {
            return false;
        }
        List<String> parts = new ArrayList<>();
        if (name.catalog() != null) {
            parts.add(name.catalog());
        }
        if (name.schema() != null) {
            parts.add(name.schema());
        }
        if (name.name() != null) {
            parts.add(name.name());
        }
        String qualified = String.join(".", parts).toLowerCase(Locale.ROOT);
        return blockLocals.peek().contains(qualified);
    }

    private void addNestedStatements(ParseTree tree) {
        List<ParserRuleContext> statements = new ArrayList<>();
        statements.addAll(descendants(tree, DmSqlParser.SelectStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.InsertStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.UpdateStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.DeleteStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.MergeStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.CallStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.ProcedureCallStatementContext.class));
        statements.addAll(descendants(tree, DmSqlParser.ExecuteImmediateStatementContext.class));
        statements.sort(Comparator.comparingInt(statement -> statement.getStart().getStartIndex()));

        for (ParserRuleContext statement : statements) {
            if (statement instanceof DmSqlParser.SelectStatementContext select) {
                visitSelectStatement(select);
            } else if (statement instanceof DmSqlParser.InsertStatementContext insert) {
                visitInsertStatement(insert);
            } else if (statement instanceof DmSqlParser.UpdateStatementContext update) {
                visitUpdateStatement(update);
            } else if (statement instanceof DmSqlParser.DeleteStatementContext delete) {
                visitDeleteStatement(delete);
            } else if (statement instanceof DmSqlParser.MergeStatementContext merge) {
                visitMergeStatement(merge);
            } else if (statement instanceof DmSqlParser.CallStatementContext call) {
                visitCallStatement(call);
            } else if (statement instanceof DmSqlParser.ProcedureCallStatementContext procedureCall) {
                visitProcedureCallStatement(procedureCall);
            } else if (statement instanceof DmSqlParser.ExecuteImmediateStatementContext executeImmediate) {
                visitExecuteImmediateStatement(executeImmediate);
            }
        }
    }

    private void addInsertTarget(DmSqlParser.InsertTargetContext ctx, List<BehaviorObject> sources) {
        if (ctx == null) {
            return;
        }
        if (ctx.qualifiedName() != null) {
            if (ctx.partitionExtensionClause().isEmpty()) {
                add(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), sources);
            } else {
                for (DmSqlParser.PartitionExtensionClauseContext partition : ctx.partitionExtensionClause()) {
                    add(SplitQueryType.INSERT, BehaviorAction.INSERT, partitionObject(ctx.qualifiedName(), partition), sources);
                }
            }
        } else {
            for (BehaviorObject target : tableSources(ctx.selectStatement())) {
                add(SplitQueryType.INSERT, BehaviorAction.INSERT, target, sources);
            }
        }
    }

    private void addErrorLoggingTarget(SplitQueryType type, DmSqlParser.DmlErrorLoggingClauseContext ctx, List<BehaviorObject> sources) {
        if (ctx == null || ctx.qualifiedName() == null) {
            return;
        }
        add(type, BehaviorAction.INSERT, object(TargetType.Table, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), sources);
    }

    private void addPrivilege(SplitQueryType type, BehaviorAction action, DmSqlParser.PrivilegeObjectContext ctx, DmSqlParser.GranteeListContext grantees) {
        if (ctx == null) {
            return;
        }
        TargetType target = TargetType.SchemaObject;
        if (ctx.SCHEMA() != null) {
            target = TargetType.Schema;
        } else if (ctx.privilegeObjectType() != null) {
            target = privilegeTarget(ctx.privilegeObjectType());
        }
        NameParts name = ctx.SCHEMA() != null ? new NameParts(null, null, NameParts.clean(ctx.identifier().getText())) : schemaScoped(NameParts.from(ctx.qualifiedName()));
        add(type, action, object(target, ctx.qualifiedName(), name), granteeTargets(grantees));
    }

    private TargetType privilegeTarget(DmSqlParser.PrivilegeObjectTypeContext ctx) {
        if (ctx.SCHEMA() != null)
            return TargetType.Schema;
        if (ctx.TABLE() != null)
            return TargetType.Table;
        if (ctx.VIEW() != null)
            return ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized;
        if (ctx.INDEX() != null)
            return TargetType.Index;
        if (ctx.SEQUENCE() != null)
            return TargetType.Sequence;
        if (ctx.PROCEDURE() != null)
            return TargetType.Procedure;
        if (ctx.FUNCTION() != null)
            return TargetType.Function;
        if (ctx.PACKAGE() != null)
            return TargetType.Package;
        if (ctx.TRIGGER() != null)
            return TargetType.Trigger;
        if (ctx.SYNONYM() != null)
            return TargetType.Synonym;
        if (ctx.LINK() != null)
            return TargetType.Link;
        if (ctx.ROLE() != null)
            return TargetType.Role;
        if (ctx.USER() != null)
            return TargetType.User;
        if (ctx.CLASS() != null || ctx.TYPE() != null || ctx.DOMAIN() != null)
            return TargetType.Type;
        if (ctx.DIRECTORY() != null)
            return TargetType.ConfigKey;
        if (ctx.CONTEXT() != null)
            return TargetType.Context;
        if (ctx.PROFILE() != null)
            return TargetType.Profile;
        if (ctx.TABLESPACE() != null)
            return TargetType.Tablespace;
        return TargetType.SchemaObject;
    }

    private void addUserDefinedDataType(List<BehaviorObject> targets, DmSqlParser.DataTypeContext dataType) {
        if (!isUserDefinedDataType(dataType)) {
            return;
        }
        DmSqlParser.QualifiedNameContext type = dataType.qualifiedName();
        addObject(targets, object(TargetType.Type, type, schemaScoped(NameParts.from(type))));
    }

    private void addColumnEncryptionTargets(List<BehaviorObject> targets, ParseTree tree) {
        for (DmSqlParser.ColumnEncryptAlgorithmClauseContext algorithm : descendants(tree, DmSqlParser.ColumnEncryptAlgorithmClauseContext.class)) {
            DmSqlParser.IdentifierContext name = algorithm.identifier();
            addObject(targets, objects.instanceObject(TargetType.ConfigKey, name, NameParts.clean(name.getText())));
        }
        for (DmSqlParser.ColumnEncryptPlainKeyContext key : descendants(tree, DmSqlParser.ColumnEncryptPlainKeyContext.class)) {
            addObject(targets, objects.instanceObject(TargetType.Key, key, NameParts.clean(key.getText())));
        }
    }

    private boolean isUserDefinedDataType(DmSqlParser.DataTypeContext dataType) {
        if (dataType == null || dataType.qualifiedName() == null) {
            return false;
        }
        DmSqlParser.QualifiedNameContext type = dataType.qualifiedName();
        NameParts name = NameParts.from(type);
        if (isBlockLocalType(dataType, name)) {
            return false;
        }
        if (name.catalog() != null || name.schema() != null || type.getText().startsWith("\"")) {
            return true;
        }
        return !BUILT_IN_DATA_TYPES.contains(name.name().toUpperCase(Locale.ROOT));
    }

    private boolean isBlockLocalType(ParseTree tree, NameParts name) {
        if (name.catalog() != null || name.schema() != null) {
            return false;
        }
        String typeName = name.name().toLowerCase(Locale.ROOT);
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (parent instanceof DmSqlParser.SqlBlockStatementContext block) {
                for (DmSqlParser.BlockDeclarationContext declaration : block.blockDeclaration()) {
                    DmSqlParser.IdentifierContext identifier = null;
                    if (declaration.blockTypeDeclaration() != null) {
                        identifier = declaration.blockTypeDeclaration().identifier();
                    } else if (declaration.packageSubtypeDeclaration() != null) {
                        identifier = declaration.packageSubtypeDeclaration().identifier();
                    }
                    if (identifier != null && NameParts.clean(identifier.getText()).equalsIgnoreCase(typeName)) {
                        return true;
                    }
                }
            }
            parent = parent.getParent();
        }
        return false;
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private List<BehaviorObject> lockSources(DmSqlParser.SelectStatementContext statement) {
        List<BehaviorObject> allSources = tableSources(statement);
        List<BehaviorObject> result = new ArrayList<>();
        for (DmSqlParser.ForUpdateClauseContext clause : descendants(statement, DmSqlParser.ForUpdateClauseContext.class)) {
            if (clause.UPDATE() == null) {
                continue;
            }
            if (clause.forUpdateColumnList() == null || allSources.size() == 1) {
                for (BehaviorObject source : allSources) {
                    addObject(result, source);
                }
                continue;
            }
            Set<String> qualifiers = new HashSet<>();
            for (DmSqlParser.QualifiedNameContext column : clause.forUpdateColumnList().qualifiedName()) {
                if (!column.dottedName().dottedNamePart().isEmpty()) {
                    qualifiers.add(NameParts.clean(column.dottedName().identifier().getText()).toLowerCase(Locale.ROOT));
                }
            }
            if (qualifiers.isEmpty()) {
                for (BehaviorObject source : allSources) {
                    addObject(result, source);
                }
                continue;
            }
            Set<String> ctes = cteNames(statement);
            for (DmSqlParser.TablePrimaryContext table : descendants(statement, DmSqlParser.TablePrimaryContext.class)) {
                if (table.qualifiedName() == null) {
                    continue;
                }
                NameParts tableName = NameParts.from(table.qualifiedName());
                if (isCte(tableName, ctes)) {
                    continue;
                }
                String reference = tableName.name();
                if (table.tableAlias() != null) {
                    reference = NameParts.clean(table.tableAlias().aliasIdentifier().getText());
                }
                if (!qualifiers.contains(reference.toLowerCase(Locale.ROOT))) {
                    continue;
                }
                for (BehaviorObject source : tableAccessObjects(table)) {
                    addObject(result, source);
                }
            }
        }
        return result;
    }

    private List<BehaviorObject> sequenceSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (DmSqlParser.PrimaryExpressionContext primary : descendants(tree, DmSqlParser.PrimaryExpressionContext.class)) {
            DmSqlParser.QualifiedNameContext qualified = primary.qualifiedName();
            if (qualified == null) {
                continue;
            }
            List<DmSqlParser.DottedNamePartContext> dotted = qualified.dottedName().dottedNamePart();
            if (dotted.isEmpty()) {
                continue;
            }
            String operation = NameParts.clean(dotted.get(dotted.size() - 1).getText());
            if (!"NEXTVAL".equalsIgnoreCase(operation) && !"CURRVAL".equalsIgnoreCase(operation)) {
                continue;
            }
            List<String> names = new ArrayList<>();
            names.add(NameParts.clean(qualified.dottedName().identifier().getText()));
            for (int index = 0; index < dotted.size() - 1; index++) {
                names.add(NameParts.clean(dotted.get(index).getText()));
            }
            Token stop = dotted.size() == 1 ? qualified.dottedName().identifier().getStop() : dotted.get(dotted.size() - 2).getStop();
            addObject(result, objects.object(TargetType.Sequence, qualified.getStart(), stop, names));
        }
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        if (tree == null) {
            return;
        }
        addTableSources(result, tree, cteNames(tree));
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree, Set<String> ctes) {
        if (tree == null) {
            return;
        }
        for (DmSqlParser.TablePrimaryContext table : descendants(tree, DmSqlParser.TablePrimaryContext.class)) {
            if (table.qualifiedName() != null) {
                NameParts name = NameParts.from(table.qualifiedName());
                if (!isCte(name, ctes)) {
                    if (!table.partitionExtensionClause().isEmpty()) {
                        for (DmSqlParser.PartitionExtensionClauseContext partition : table.partitionExtensionClause()) {
                            addObject(result, partitionObject(table.qualifiedName(), partition));
                        }
                    } else {
                        TargetType type = TargetType.Table;
                        boolean systemNamespace = name.schema() == null || "SYS".equalsIgnoreCase(name.schema());
                        if (systemNamespace && (name.name().toUpperCase(Locale.ROOT).startsWith("V$") || DmResourceRegistry.instance().isSystemView(name.name()))) {
                            type = TargetType.View;
                        }
                        addObject(result, object(type, table.qualifiedName(), schemaScoped(name)));
                    }
                }
            }
        }
    }

    private void addFunctionCalls(ParseTree tree) {
        if (tree == null) {
            return;
        }
        if (tree instanceof DmSqlParser.FunctionCallContext function) {
            DmSqlParser.FunctionNameContext context = function.functionName();
            ParserRuleContext nameContext = context;
            NameParts name;
            if (context.qualifiedName() != null) {
                name = NameParts.from(context.qualifiedName());
            } else {
                name = new NameParts(null, null, NameParts.clean(context.getText()));
            }
            if ("ROW".equalsIgnoreCase(context.getText())) {
                for (int index = 0; index < tree.getChildCount(); index++) {
                    addFunctionCalls(tree.getChild(index));
                }
                return;
            }
            if (isJavaConstructorDelegate(name)) {
                for (int index = 0; index < tree.getChildCount(); index++) {
                    addFunctionCalls(tree.getChild(index));
                }
                return;
            }
            boolean packageMember = isPackageFunctionMember(name);
            if (packageMember) {
                name = packageMemberName(name.name());
            }
            String localType = packageMember ? null : localMethodType(name);
            if (localType != null) {
                name = new NameParts(null, null, name.name());
                nameContext = finalNamePart(context.qualifiedName());
            } else if (isBlockLocal(name)) {
                if (!isExactBlockLocal(name) && (name.catalog() != null || name.schema() != null)) {
                    name = new NameParts(null, null, name.name());
                    nameContext = finalNamePart(context);
                } else {
                    for (int index = 0; index < tree.getChildCount(); index++) {
                        addFunctionCalls(tree.getChild(index));
                    }
                    return;
                }
            } else if ((name.catalog() != null || name.schema() != null) && isTableAlias(function, name.catalog() == null ? name.schema() : name.catalog())) {
                name = new NameParts(null, null, name.name());
                nameContext = finalNamePart(context);
            }
            BehaviorAction action = BehaviorAction.CALL;
            if (name.catalog() == null && name.schema() == null) {
                action = DmResourceRegistry.instance().functionBehavior(name.name());
            } else if (name.catalog() == null) {
                action = DmResourceRegistry.instance().functionBehavior(name.schema(), name.name());
            }
            SplitQueryType type = DmResourceRegistry.instance().functionType(name.name()).orElse(null);
            if (type == null) {
                type = switch (action) {
                    case CALL -> SplitQueryType.CALL_PROG_OBJ;
                    case READ -> SplitQueryType.SELECT;
                    case LOCK -> SplitQueryType.QUERY_LOCK;
                    case CONFIGURE -> SplitQueryType.SYSTEM_SETTING_WRITE;
                    case CREATE, ALTER, CHECKPOINT, RESET, START, STOP, SWITCH, UNSAFE -> SplitQueryType.ADMIN;
                    default -> throw new IllegalStateException("unsupported functional function action " + action);
                };
            }
            List<BehaviorObject> targets = new ArrayList<>();
            OptionalInt configArgument = DmResourceRegistry.instance().functionConfigArgument(name.name());
            if (configArgument.isPresent() && function.functionArguments() != null && configArgument.getAsInt() < function.functionArguments().functionArgument().size()) {
                DmSqlParser.FunctionArgumentContext argument = function.functionArguments().functionArgument(configArgument.getAsInt());
                if (argument.expression() != null) {
                    Token key = stringArgument(List.of(argument.expression()), 0);
                    if (key != null) {
                        addObject(targets, objects.instanceObject(TargetType.ConfigKey, key, stringValue(key)));
                    }
                }
            }
            addSystemFunctionTargets(targets, name, function);
            BehaviorObject subject = localType == null ? routineObject(TargetType.Function, nameContext, name) : localMethodObject(TargetType.Function, nameContext, localType, name
                .name());
            add(type, action, subject, targets);
        } else if (tree instanceof DmSqlParser.SpecialFunctionCallContext special) {
            Token name = special.getStart();
            NameParts local = new NameParts(null, null, NameParts.clean(name.getText()));
            boolean typeConversion = special.CAST() != null || special.TREAT() != null;
            if (special.TREAT() != null && isUserDefinedDataType(special.dataType())) {
                DmSqlParser.QualifiedNameContext type = special.dataType().qualifiedName();
                add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Type, type, schemaScoped(NameParts.from(type))));
            }
            if (!typeConversion && !isBlockLocal(local)) {
                add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, objects.object(TargetType.Function, name, List.of(local.name())));
            }
        } else if (tree instanceof DmSqlParser.JsonTableExpressionContext || tree instanceof DmSqlParser.JsonCollectionTableExpressionContext
                   || tree instanceof DmSqlParser.XmlTableExpressionContext || tree instanceof DmSqlParser.XmlAttributesFunctionContext
                   || tree instanceof DmSqlParser.ContainsPredicateContext) {
            Token name = ((ParserRuleContext) tree).getStart();
            add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, objects.object(TargetType.Function, name, List.of(NameParts.clean(name.getText()))));
        } else if (tree instanceof DmSqlParser.NewArrayExpressionContext allocation && isUserDefinedDataType(allocation.dataType())) {
            DmSqlParser.QualifiedNameContext constructor = allocation.dataType().qualifiedName();
            NameParts constructorName = NameParts.from(constructor);
            if (isBlockLocal(constructorName)) {
                return;
            }
            if (allocation.arrayType == null) {
                add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Function, constructor, schemaScoped(constructorName)));
            } else {
                add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Type, constructor, schemaScoped(constructorName)));
            }
        } else if (tree instanceof DmSqlParser.OperatorFunctionClauseContext clause) {
            DmSqlParser.OperatorQualifiedNameContext operator = clause.operatorQualifiedName();
            add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Operator, operator, schemaScoped(operatorName(operator))));
        } else if (tree instanceof DmSqlParser.UserDefinedOperatorContext operator && operator.symbolicOperatorName() != null) {
            DmSqlParser.SymbolicOperatorNameContext symbol = operator.symbolicOperatorName();
            NameParts name = new NameParts(null, null, NameParts.clean(symbol.getText()));
            add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Operator, symbol, schemaScoped(name)));
        } else if (tree instanceof DmSqlParser.ComparisonOperatorContext operator && operator.JSON_CONTAINS() != null) {
            addBuiltInOperator(operator.JSON_CONTAINS().getSymbol());
        } else if (tree instanceof DmSqlParser.AdditiveContext additive) {
            for (org.antlr.v4.runtime.tree.TerminalNode minus : additive.MINUS()) {
                if (isJsonBinaryOperator(additive, minus.getSymbol())) {
                    addBuiltInOperator(minus.getSymbol());
                }
            }
        } else if (tree instanceof DmSqlParser.PostfixOperatorContext postfix && (postfix.JSON_ARROW() != null || postfix.JSON_TEXT_ARROW() != null)) {
            Token operator = postfix.JSON_ARROW() == null ? postfix.JSON_TEXT_ARROW().getSymbol() : postfix.JSON_ARROW().getSymbol();
            addBuiltInOperator(operator);
        } else if (tree instanceof DmSqlParser.PostfixOperatorContext postfix && postfix.methodName != null && postfix.LPAREN() != null) {
            DmSqlParser.MethodIdentifierContext method = postfix.methodName;
            NameParts methodName = new NameParts(null, null, NameParts.clean(method.getText()));
            String localType = localReceiverType(method.getStart());
            NameParts superMethod = superMethodName(postfix, methodName.name());
            BehaviorObject subject;
            if (superMethod != null) {
                subject = object(TargetType.Function, method, superMethod);
            } else if (localType != null) {
                subject = localMethodObject(TargetType.Function, method, localType, methodName.name());
            } else {
                subject = object(TargetType.Function, method, schemaScoped(methodName));
            }
            add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, subject);
        } else if (tree instanceof DmSqlParser.PrimaryExpressionContext primary && primary.qualifiedName() != null) {
            NameParts name = NameParts.from(primary.qualifiedName());
            if (!isDatePartArgument(primary.qualifiedName()) && !isBlockLocal(name) && isNoParenthesesSystemFunction(name)) {
                add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Function, primary.qualifiedName(), name));
            }
        }
        for (int index = 0; index < tree.getChildCount(); index++) {
            addFunctionCalls(tree.getChild(index));
        }
    }

    private void addBuiltInOperator(Token token) {
        String name = NameParts.clean(token.getText());
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, objects.object(TargetType.Operator, token, List.of(name)));
    }

    private boolean isJsonBinaryOperator(DmSqlParser.AdditiveContext expression, Token operator) {
        int end = operator.getStartIndex() - 1;
        if (end < expression.getStart().getStartIndex()) {
            return false;
        }
        String left = operator.getInputStream().getText(Interval.of(expression.getStart().getStartIndex(), end));
        return left.toUpperCase(Locale.ROOT).contains("::JSONB");
    }

    private void addTypePredicateReads(ParseTree tree) {
        for (DmSqlParser.IsOfTypePredicateClauseContext predicate : descendants(tree, DmSqlParser.IsOfTypePredicateClauseContext.class)) {
            for (DmSqlParser.DataTypeContext dataType : predicate.dataType()) {
                if (!isUserDefinedDataType(dataType)) {
                    continue;
                }
                DmSqlParser.QualifiedNameContext type = dataType.qualifiedName();
                add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Type, type, schemaScoped(NameParts.from(type))));
            }
        }
    }

    private ParserRuleContext finalNamePart(DmSqlParser.FunctionNameContext context) {
        return finalNamePart(context.qualifiedName());
    }

    private ParserRuleContext finalNamePart(DmSqlParser.QualifiedNameContext qualified) {
        List<DmSqlParser.DottedNamePartContext> parts = qualified.dottedName().dottedNamePart();
        if (parts.isEmpty()) {
            return qualified.dottedName().identifier();
        }
        return parts.get(parts.size() - 1);
    }

    private boolean isTableAlias(ParseTree tree, String candidate) {
        ParseTree statement = tree;
        while (statement != null && !(statement instanceof DmSqlParser.SelectStatementContext)) {
            statement = statement.getParent();
        }
        if (statement == null) {
            return false;
        }
        for (DmSqlParser.TablePrimaryContext table : descendants(statement, DmSqlParser.TablePrimaryContext.class)) {
            if (table.tableAlias() == null) {
                continue;
            }
            String alias = NameParts.clean(table.tableAlias().aliasIdentifier().getText());
            if (alias.equalsIgnoreCase(candidate)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDatePartArgument(DmSqlParser.QualifiedNameContext context) {
        ParseTree parent = context.getParent();
        while (parent != null && !(parent instanceof DmSqlParser.FunctionCallContext)) {
            parent = parent.getParent();
        }
        if (!(parent instanceof DmSqlParser.FunctionCallContext function) || function.functionArguments() == null || function.functionArguments().functionArgument().isEmpty()) {
            return false;
        }
        DmSqlParser.FunctionArgumentContext firstArgument = function.functionArguments().functionArgument(0);
        return firstArgument.getText().equals(context.getText()) && DATE_PART_FUNCTIONS.contains(function.functionName().getText().toUpperCase(Locale.ROOT));
    }

    private void addSystemFunctionTargets(List<BehaviorObject> targets, NameParts name, DmSqlParser.FunctionCallContext function) {
        if (name.schema() == null && "SF_GET_SESSION_MPP_SELECT_LOCAL".equalsIgnoreCase(name.name())) {
            addObject(targets, objects.instanceObject(TargetType.ConfigKey, function, "MPP_SELECT_LOCAL"));
            return;
        }
        if (function.functionArguments() == null) {
            return;
        }
        DmSqlParser.FunctionArgumentsContext arguments = function.functionArguments();
        if (name.schema() == null && "SF_CHECK_SYSTEM_PACKAGE".equalsIgnoreCase(name.name())) {
            Token packageName = stringFunctionArgument(arguments, 0, null);
            String packageValue = stringValue(packageName);
            if (packageValue != null && !packageValue.isBlank()) {
                addObject(targets, objects.instanceObject(TargetType.Package, packageName, packageValue));
            }
            return;
        }
        if (name.schema() == null && "SF_MPP_INST_ADD".equalsIgnoreCase(name.name())) {
            Token instance = stringFunctionArgument(arguments, 1, null);
            String instanceValue = stringValue(instance);
            if (instanceValue != null && !instanceValue.isBlank()) {
                addObject(targets, objects.instanceObject(TargetType.Instance, instance, instanceValue));
            }
            return;
        }
        if (name.schema() == null && "SF_MPP_INST_REMOVE".equalsIgnoreCase(name.name())) {
            Token instance = stringFunctionArgument(arguments, 0, null);
            String instanceValue = stringValue(instance);
            if (instanceValue != null && !instanceValue.isBlank()) {
                addObject(targets, objects.instanceObject(TargetType.Instance, instance, instanceValue));
            }
            return;
        }
        if (name.schema() == null) {
            TargetType targetType = switch (name.name().toUpperCase(Locale.ROOT)) {
                case "PARTGROUPDEF" -> TargetType.ResourceGroup;
                case "TABLEDEF", "TABLE_USED_PAGES" -> TargetType.Table;
                default -> null;
            };
            if (targetType != null) {
                Token schema = stringFunctionArgument(arguments, 0, null);
                Token object = stringFunctionArgument(arguments, 1, null);
                if (schema != null && object != null) {
                    addObject(targets, objects.object(targetType, object, List.of(stringValue(schema), stringValue(object))));
                }
                return;
            }
        }
        if (name.schema() == null && "SYS_CONTEXT".equalsIgnoreCase(name.name())) {
            Token namespace = stringFunctionArgument(arguments, 0, null);
            Token key = stringFunctionArgument(arguments, 1, null);
            String namespaceValue = stringValue(namespace);
            String keyValue = stringValue(key);
            if (namespaceValue != null && keyValue != null && !"NULL".equalsIgnoreCase(namespaceValue) && !"NULL".equalsIgnoreCase(keyValue)) {
                addObject(targets, objects.instanceObject(TargetType.ConfigKey, key, namespaceValue + "/" + keyValue));
            }
            return;
        }
        if ("UTL_FILE".equalsIgnoreCase(name.schema()) && ("FOPEN".equalsIgnoreCase(name.name()) || "FOPEN_NCHAR".equalsIgnoreCase(name.name()))) {
            Token location = stringFunctionArgument(arguments, 0, "LOCATION");
            Token filename = stringFunctionArgument(arguments, 1, "FILENAME");
            String locationValue = stringValue(location);
            String filenameValue = stringValue(filename);
            if (locationValue != null && filenameValue != null && !"NULL".equalsIgnoreCase(locationValue) && !"NULL".equalsIgnoreCase(filenameValue)) {
                addObject(targets, objects.instanceObject(TargetType.File, filename, locationValue + "/" + filenameValue));
            }
            return;
        }
        if ("DBMS_PIPE".equalsIgnoreCase(name.schema())) {
            switch (name.name().toUpperCase(Locale.ROOT)) {
                case "CREATE_PIPE", "RECEIVE_MESSAGE", "REMOVE_PIPE", "SEND_MESSAGE" -> {
                    Token pipe = stringFunctionArgument(arguments, 0, "PIPENAME");
                    if (pipe != null) {
                        addObject(targets, objects.instanceObject(TargetType.Pipe, pipe, stringValue(pipe)));
                    }
                }
                default -> {
                    // The remaining DBMS_PIPE functions have no named resource argument.
                }
            }
            return;
        }
        if ("DBMS_XMLPARSER".equalsIgnoreCase(name.schema()) && "PARSE".equalsIgnoreCase(name.name())) {
            Token file = stringFunctionArgument(arguments, 0, "URL");
            if (file != null) {
                addObject(targets, fileObject(file));
            }
            return;
        }
        if ("DBMS_STATS".equalsIgnoreCase(name.schema()) && "GET_PREFS".equalsIgnoreCase(name.name())) {
            Token table = stringFunctionArgument(arguments, 2, "TABNAME");
            Token schema = stringFunctionArgument(arguments, 1, "OWNNAME");
            addStatsSchemaObjectTarget(targets, TargetType.Table, table, schema);
            Token preference = stringFunctionArgument(arguments, 0, "PPNAME");
            String preferenceValue = stringValue(preference);
            if (preferenceValue != null && !"NULL".equalsIgnoreCase(preferenceValue)) {
                addObject(targets, objects.instanceObject(TargetType.ConfigKey, preference, preferenceValue));
            }
            return;
        }
        if (name.schema() == null && "OBJECT_ID".equalsIgnoreCase(name.name())) {
            addObjectIdTarget(targets, arguments);
            return;
        }
        if (!"DBMS_METADATA".equalsIgnoreCase(name.schema())) {
            return;
        }
        if ("GET_DDL".equalsIgnoreCase(name.name())) {
            Token type = stringFunctionArgument(arguments, 0, "OBJECT_TYPE");
            Token object = stringFunctionArgument(arguments, 1, "NAME");
            Token schema = stringFunctionArgument(arguments, 2, "SCHNAME");
            addMetadataObjectTarget(targets, type, object, schema, false);
        } else if ("GET_DEPENDENT_DDL".equalsIgnoreCase(name.name())) {
            Token type = stringFunctionArgument(arguments, 0, "OBJECT_TYPE");
            Token object = stringFunctionArgument(arguments, 1, "BASE_OBJECT_NAME");
            Token schema = stringFunctionArgument(arguments, 2, "BASE_OBJECT_SCHEMA");
            addMetadataObjectTarget(targets, type, object, schema, true);
        } else if ("GET_GRANTED_DDL".equalsIgnoreCase(name.name())) {
            Token grantee = stringFunctionArgument(arguments, 1, "GRANTEE");
            if (grantee != null) {
                addObject(targets, objects.instanceObject(TargetType.UserOrRole, grantee, stringValue(grantee)));
            }
        }
    }

    private void addObjectIdTarget(List<BehaviorObject> targets, DmSqlParser.FunctionArgumentsContext arguments) {
        Token object = stringFunctionArgument(arguments, 0, null);
        String objectValue = stringValue(object);
        if (objectValue == null || "NULL".equalsIgnoreCase(objectValue)) {
            return;
        }
        Token type = stringFunctionArgument(arguments, 1, null);
        String typeValue = stringValue(type);
        boolean explicitType = arguments.functionArgument().size() > 1;
        TargetType targetType = switch (typeValue == null ? explicitType ? "UNKNOWN" : "TABLE" : typeValue.toUpperCase(Locale.ROOT)) {
            case "VIEW" -> TargetType.View;
            case "C", "UQ", "PK" -> TargetType.Constraint;
            case "PACKAGE", "PKG" -> TargetType.Package;
            case "TYPE", "CLASS", "JCLASS" -> TargetType.Type;
            case "UNKNOWN" -> TargetType.SchemaObject;
            case "INDEX" -> null;
            default -> TargetType.Table;
        };
        if (targetType == null) {
            return;
        }
        List<String> names = Arrays.stream(objectValue.split("\\.")).filter(part -> !part.isBlank()).toList();
        addObject(targets, objects.object(targetType, object, names));
    }

    private Token stringFunctionArgument(DmSqlParser.FunctionArgumentsContext arguments, int index, String parameterName) {
        for (DmSqlParser.FunctionArgumentContext argument : arguments.functionArgument()) {
            if (argument.namedArgument() != null && parameterName != null && parameterName.equalsIgnoreCase(argument.namedArgument().dottedNamePart().getText())) {
                return stringArgument(List.of(argument.namedArgument().expression()), 0);
            }
        }
        if (index >= arguments.functionArgument().size()) {
            return null;
        }
        DmSqlParser.FunctionArgumentContext argument = arguments.functionArgument(index);
        if (argument.namedArgument() != null || argument.expression() == null) {
            return null;
        }
        return stringArgument(List.of(argument.expression()), 0);
    }

    private void addMetadataObjectTarget(List<BehaviorObject> targets, Token typeToken, Token objectToken, Token schemaToken, boolean dependentObject) {
        if (objectToken == null) {
            return;
        }
        String typeName = stringValue(typeToken);
        TargetType type = metadataTargetType(typeName);
        if (dependentObject && "INDEX".equalsIgnoreCase(typeName)) {
            type = TargetType.Table;
        }
        if (type == TargetType.Tablespace) {
            addObject(targets, objects.instanceObject(TargetType.Tablespace, objectToken, stringValue(objectToken)));
            return;
        }
        List<String> names = new ArrayList<>();
        if (schemaToken != null) {
            names.add(stringValue(schemaToken));
        }
        names.add(stringValue(objectToken));
        addObject(targets, objects.object(type, objectToken, names));
    }

    private TargetType metadataTargetType(String type) {
        if (type == null) {
            return TargetType.SchemaObject;
        }
        return switch (type.toUpperCase(Locale.ROOT)) {
            case "TABLE" -> TargetType.Table;
            case "VIEW" -> TargetType.View;
            case "MATERIALIZED_VIEW" -> TargetType.Materialized;
            case "INDEX" -> TargetType.Index;
            case "SEQUENCE" -> TargetType.Sequence;
            case "SYNONYM" -> TargetType.Synonym;
            case "PROCEDURE" -> TargetType.Procedure;
            case "FUNCTION" -> TargetType.Function;
            case "PACKAGE" -> TargetType.Package;
            case "TRIGGER" -> TargetType.Trigger;
            case "TABLESPACE" -> TargetType.Tablespace;
            default -> TargetType.SchemaObject;
        };
    }

    private boolean isNoParenthesesSystemFunction(NameParts name) {
        if (name == null) {
            return false;
        }
        if (name.catalog() == null && name.schema() == null) {
            return "USER".equalsIgnoreCase(name.name()) || DmResourceRegistry.instance().isNoParenthesesFunction(name.name());
        }
        return name.catalog() == null && name.schema() != null && DmResourceRegistry.instance().isNoParenthesesFunction(name.schema(), name.name());
    }

    private Set<String> cteNames(ParseTree tree) {
        Set<String> names = new HashSet<>();
        for (DmSqlParser.CteDefinitionContext cte : descendants(tree, DmSqlParser.CteDefinitionContext.class)) {
            names.add(NameParts.clean(cte.identifier().getText()).toLowerCase());
        }
        return names;
    }

    private boolean isCte(NameParts name, Set<String> ctes) {
        return name != null && name.catalog() == null && name.schema() == null && name.name() != null && ctes.contains(name.name().toLowerCase());
    }

    private List<DmSqlParser.QualifiedNameContext> deleteTargetNames(DmSqlParser.DeleteStatementContext ctx) {
        List<DmSqlParser.QualifiedNameContext> result = new ArrayList<>();
        DmSqlParser.TablePrimaryContext target = ctx.deleteTarget().tablePrimary();
        if (target.qualifiedName() != null) {
            result.add(target.qualifiedName());
        } else {
            for (DmSqlParser.TablePrimaryContext table : descendants(target, DmSqlParser.TablePrimaryContext.class)) {
                if (table.qualifiedName() != null) {
                    result.add(table.qualifiedName());
                }
            }
        }
        return result;
    }

    private String schemaAuthorizationOwner(DmSqlParser.SchemaCreateContext ctx) {
        if (ctx.schemaAuthorizationOnly() != null && ctx.schemaAuthorizationOnly().schemaOwner != null) {
            return NameParts.clean(ctx.schemaAuthorizationOnly().schemaOwner.getText());
        }
        if (ctx.schemaAuthorizationClause() != null && ctx.schemaAuthorizationClause().schemaOwner != null) {
            return NameParts.clean(ctx.schemaAuthorizationClause().schemaOwner.getText());
        }
        return null;
    }

    private NameParts schemaScoped(NameParts name) {
        if (name == null || name.schema() != null || schemaScopes.isEmpty()) {
            return name;
        }
        return new NameParts(name.catalog(), schemaScopes.get(schemaScopes.size() - 1), name.name());
    }

    private NameParts operatorName(DmSqlParser.OperatorQualifiedNameContext context) {
        String text = context.getText();
        int separator = text.lastIndexOf('.');
        if (separator < 0) {
            return new NameParts(null, null, NameParts.clean(text));
        }
        return new NameParts(null, NameParts.clean(text.substring(0, separator)), NameParts.clean(text.substring(separator + 1)));
    }

    private BehaviorObject fileObject(Token token) {
        String path = token.getText();
        if (path.length() >= 2 && path.startsWith("'") && path.endsWith("'")) {
            path = path.substring(1, path.length() - 1).replace("''", "'");
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.isEmpty()) {
            return objects.instanceObject(TargetType.File, token);
        }
        return objects.instanceObject(TargetType.File, token, path);
    }

    private DmSqlParser.QualifiedNameContext first(List<DmSqlParser.QualifiedNameContext> values) {
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context, NameParts name) {
        if (context == null || name == null || name.name() == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (name.catalog() != null)
            names.add(name.catalog());
        if (name.schema() != null)
            names.add(name.schema());
        names.add(name.name());
        return objects.object(type, context, names);
    }

    private BehaviorObject routineObject(TargetType type, ParserRuleContext context, NameParts name) {
        if (context == null || name == null || name.name() == null || name.catalog() == null || !context.getText().contains("@")) {
            return object(type, context, name);
        }
        List<String> names = new ArrayList<>();
        if (levels != null && levels.get(UmiTypes.Catalog) != null) {
            names.add(levels.get(UmiTypes.Catalog).toString());
        }
        names.add(name.catalog());
        names.add(name.schema());
        names.add(name.name());
        return objects.object(type, context, names);
    }

    private List<BehaviorObject> tableAccessObjects(DmSqlParser.TablePrimaryContext table) {
        if (table.partitionExtensionClause().isEmpty()) {
            return List.of(object(TargetType.Table, table.qualifiedName(), schemaScoped(NameParts.from(table.qualifiedName()))));
        }
        List<BehaviorObject> result = new ArrayList<>();
        for (DmSqlParser.PartitionExtensionClauseContext partition : table.partitionExtensionClause()) {
            addObject(result, partitionObject(table.qualifiedName(), partition));
        }
        return result;
    }

    private BehaviorObject partitionObject(DmSqlParser.QualifiedNameContext table, DmSqlParser.PartitionExtensionClauseContext partition) {
        NameParts tableName = schemaScoped(NameParts.from(table));
        List<String> names = partitionPath(tableName);
        ParserRuleContext context = partition;
        if (partition.identifier() != null) {
            names.add(NameParts.clean(partition.identifier().getText()));
            context = partition.identifier();
        }
        return objects.object(TargetType.Partition, context, names);
    }

    private void addPartitionTarget(List<BehaviorObject> targets, NameParts table, DmSqlParser.IdentifierContext partition) {
        if (table == null || table.name() == null || partition == null) {
            return;
        }
        addObject(targets, partitionObject(table, partition));
    }

    private BehaviorObject partitionObject(NameParts table, DmSqlParser.IdentifierContext partition) {
        List<String> names = partitionPath(table);
        names.add(NameParts.clean(partition.getText()));
        return objects.object(TargetType.Partition, partition, names);
    }

    private List<String> partitionPath(NameParts table) {
        List<String> names = new ArrayList<>();
        if (table.catalog() != null) {
            names.add(table.catalog());
        } else if (levels != null && levels.get(UmiTypes.Catalog) != null) {
            names.add(levels.get(UmiTypes.Catalog).toString());
        }
        if (table.schema() != null) {
            names.add(table.schema());
        } else if (levels != null && levels.get(UmiTypes.Schema) != null) {
            names.add(levels.get(UmiTypes.Schema).toString());
        }
        names.add(table.name());
        return names;
    }

    private BehaviorAction createAction(ParserRuleContext context) {
        ParseTree parent = context.getParent();
        while (parent != null && !(parent instanceof DmSqlParser.CreateStatementContext)) {
            if (parent instanceof DmSqlParser.CreateReplaceTargetContext) {
                return BehaviorAction.REPLACE;
            }
            parent = parent.getParent();
        }
        return BehaviorAction.CREATE;
    }

    private void add(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        add(type, action, subject, List.of());
    }

    private void add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        for (BehaviorObject target : targets) {
            addObject(relation.getTarget(), target);
        }
        boolean duplicate = behavior.getRelations().stream().anyMatch(existing -> {
            return existing.getAction() == relation.getAction() && sameObject(existing.getSubject(), relation.getSubject())
                   && sameTargets(existing.getTarget(), relation.getTarget());
        });
        if (duplicate) {
            return;
        }
        behavior.getRelations().add(relation);
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || behavior.getStatementType() == SplitQueryType.SELECT && type == SplitQueryType.SESSION_VARIABLE_RW) {
            behavior.setStatementType(type);
        }
    }

    private boolean sameTargets(List<BehaviorObject> left, List<BehaviorObject> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int index = 0; index < left.size(); index++) {
            if (!sameObject(left.get(index), right.get(index))) {
                return false;
            }
        }
        return true;
    }

    private boolean sameObject(BehaviorObject left, BehaviorObject right) {
        return left.getObjectType() == right.getObjectType() && Objects.equals(left.getObjectPath(), right.getObjectPath()) && left.getStartLine() == right.getStartLine()
               && left.getStartColumn() == right.getStartColumn() && left.getEndLine() == right.getEndLine() && left.getEndColumn() == right.getEndColumn();
    }

    private void addObject(List<BehaviorObject> values, BehaviorObject value) {
        if (value != null && values.stream().noneMatch(existing -> sameObject(existing, value))) {
            values.add(value);
        }
    }

    private <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        collectDescendants(tree, type, result);
        return result;
    }

    private <T extends ParserRuleContext> void collectDescendants(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDescendants(tree.getChild(i), type, result);
        }
    }

    private record NameParts(String catalog, String schema, String name) {
        private static NameParts from(DmSqlParser.QualifiedNameContext ctx) {
            if (ctx == null)
                return new NameParts(null, null, null);
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.dottedName().identifier().getText()));
            for (DmSqlParser.DottedNamePartContext part : ctx.dottedName().dottedNamePart()) {
                parts.add(clean(part.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts from(DmSqlParser.BareRoutineNameContext ctx) {
            if (ctx == null)
                return new NameParts(null, null, null);
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.regularIdentifier().getText()));
            for (DmSqlParser.DottedNamePartContext part : ctx.dottedNamePart()) {
                parts.add(clean(part.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts fromParts(List<String> parts) {
            int size = parts.size();
            return size == 0 ? new NameParts(null, null, null) : new NameParts(size > 2 ? parts.get(size - 3) : null, size > 1 ? parts.get(size - 2) : null, parts.get(size - 1));
        }

        private static String clean(String text) {
            if (text == null || text.length() < 2)
                return text;
            if (text.startsWith("\"") && text.endsWith("\"")) {
                return text.substring(1, text.length() - 1).replace("\"\"", "\"");
            }
            if (text.startsWith("[") && text.endsWith("]")) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        }
    }
}

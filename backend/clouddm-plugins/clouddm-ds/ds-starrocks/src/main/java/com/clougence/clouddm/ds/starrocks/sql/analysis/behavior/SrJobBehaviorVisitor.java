/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.ds.starrocks.sql.analysis.behavior;

import static com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

/** Job definitions, executable bodies, data transfer and persistent backups. */
class SrJobBehaviorVisitor extends SrStatementBehaviorVisitor {
    protected final Map<UmiTypes, Object> levels;
    protected final int baseLine;
    protected final int baseColumn;

    SrJobBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        super(parser, levels, baseLine, baseColumn);
        this.levels = new HashMap<>();
        if (levels != null) {
            this.levels.putAll(levels);
        }
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    private String databasePath(ParserRuleContext database, Token anchor) {
        if (database != null) {
            return object(TargetType.Schema, database).getObjectPath();
        }
        return objects.unnamedObject(TargetType.Schema, anchor, UmiTypes.Schema).getObjectPath();
    }

    private BehaviorObject atPath(TargetType type, ParserRuleContext anchor, String path) {
        BehaviorObject result = objects.instanceObject(type, anchor);
        result.setObjectPath(path);
        return result;
    }

    private BehaviorObject atPath(TargetType type, Token anchor, String path) {
        BehaviorObject result = objects.instanceObject(type, anchor);
        result.setObjectPath(path);
        return result;
    }

    private BehaviorObject databaseJob(ParserRuleContext db, IdentifierContext id, Token keyword, String kind) {
        String path = databasePath(db, keyword) + kind + "/";
        if (id == null) {
            return atPath(TargetType.Job, keyword, path);
        }
        BehaviorObject result = objects.instanceObject(TargetType.Job, id);
        if (db != null && db.getStart().getTokenIndex() < id.getStart().getTokenIndex()) {
            result = objects.instanceObject(TargetType.Job, db.getStart(), id.getStop(), kind);
        }
        result.setObjectPath(path + name(id) + "/");
        return result;
    }

    protected List<BehaviorObject> partitions(BehaviorObject table, PartitionNamesContext clause) {
        if (clause == null || clause.identifierOrString().isEmpty()) {
            return List.of(table);
        }
        List<BehaviorObject> result = new ArrayList<>();
        for (IdentifierOrStringContext partition : clause.identifierOrString()) {
            result.add(atPath(TargetType.Partition, partition, table.getObjectPath() + name(partition) + "/"));
        }
        return result;
    }

    private StringContext propertyValue(PropertyListContext properties, String key) {
        if (properties != null) {
            for (PropertyContext property : properties.property()) {
                if (key.equalsIgnoreCase(name(property.key))) {
                    return property.value;
                }
            }
        }
        return null;
    }

    private BehaviorObject inputFile(StringContext value) {
        String path = name(value);
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return objects.instanceObject(TargetType.File, value, path);
    }

    protected SrJobBehaviorVisitor scopedVisitor(ParserRuleContext database) {
        Map<UmiTypes, Object> context = new HashMap<>(levels);
        if (database != null) {
            context.put(UmiTypes.Schema, name(database));
        }
        return new SrJobBehaviorVisitor(parser, context, baseLine, baseColumn);
    }

    private void appendBody(ParseTree body, ParserRuleContext database) {
        SrJobBehaviorVisitor visitor = scopedVisitor(database);
        visitor.visit(body);
        behavior().getRelations().addAll(visitor.behavior().getRelations());
    }

    @Override
    public Void visitCreateAnalyzeStatement(CreateAnalyzeStatementContext ctx) {
        Token anchor = ctx.getStart();
        List<BehaviorObject> targets;
        if (ctx.histogramStatement() != null) {
            HistogramStatementContext histogram = ctx.histogramStatement();
            anchor = histogram.ANALYZE().getSymbol();
            targets = List.of(object(TargetType.Table, histogram.tableName().qualifiedName()));
        } else {
            anchor = ctx.ANALYZE().getSymbol();
            if (ctx.db != null) {
                targets = List.of(object(TargetType.Schema, ctx.db));
            } else if (!ctx.qualifiedName().isEmpty()) {
                targets = List.of(object(TargetType.Table, ctx.qualifiedName(0)));
            } else {
                targets = List.of(objects.unnamedObject(TargetType.Table, ctx.ALL().getSymbol(), UmiTypes.Catalog));
            }
        }
        add(SplitQueryType.CREATE_JOB, BehaviorAction.CREATE, objects.instanceObject(TargetType.Job, anchor, "analyze_job"), targets);
        return null;
    }

    @Override
    public Void visitDropAnalyzeJobStatement(DropAnalyzeJobStatementContext ctx) {
        Token anchor = ctx.ANALYZE().getSymbol();
        String path = "analyze_job";
        if (ctx.INTEGER_VALUE() != null) {
            anchor = ctx.INTEGER_VALUE().getSymbol();
            path += "/" + anchor.getText();
        }
        add(SplitQueryType.DROP_JOB, BehaviorAction.DROP, objects.instanceObject(TargetType.Job, anchor, path));
        return null;
    }

    @Override
    public Void visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        if (ctx.STATUS() != null) {
            return super.visitShowAnalyzeStatement(ctx);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.ANALYZE().getSymbol(), "analyze_job"));
        return null;
    }

    @Override
    public Void visitSubmitTaskStatement(SubmitTaskStatementContext ctx) {
        QualifiedNameContext taskName = ctx.qualifiedName();
        BehaviorObject task = objects.instanceObject(TargetType.Job, ctx.TASK().getSymbol(), "task");
        IdentifierContext database = null;
        if (taskName != null) {
            List<IdentifierContext> names = taskName.identifier();
            task = objects.instanceObject(TargetType.Job, taskName, "task/" + name(names.get(names.size() - 1)));
            if (names.size() > 1) {
                database = names.get(names.size() - 2);
            }
        }
        add(SplitQueryType.CREATE_JOB, BehaviorAction.CREATE, task);
        addHints(ctx.SUBMIT().getSymbol(), ctx.TASK().getSymbol(), task.getObjectPath() + "session.", SplitQueryType.CREATE_JOB);
        for (TaskClauseContext clause : ctx.taskClause()) {
            if (clause.properties() == null) {
                continue;
            }
            for (PropertyContext property : clause.properties().property()) {
                String key = name(property.key).toLowerCase(Locale.ROOT);
                if (key.startsWith("session.")) {
                    add(SplitQueryType.CREATE_JOB, BehaviorAction.CONFIGURE, atPath(TargetType.ConfigKey, property.key, task.getObjectPath() + key + "/"));
                }
            }
        }
        ParseTree body = ctx.createTableAsSelectStatement();
        if (body == null) {
            body = ctx.insertStatement();
        }
        if (body == null) {
            body = ctx.dataCacheSelectStatement();
        }
        appendBody(body, database);
        return null;
    }

    @Override
    public Void visitDropTaskStatement(DropTaskStatementContext ctx) {
        List<IdentifierContext> names = ctx.qualifiedName().identifier();
        add(SplitQueryType.DROP_JOB, BehaviorAction.DROP,
            objects.instanceObject(TargetType.Job, ctx.qualifiedName(), "task/" + name(names.get(names.size() - 1))));
        return null;
    }

    // Query effects are analyzed independently, then reads become data dependencies.
    private List<BehaviorRelation> queryEffects(ParseTree query) {
        if (query == null) {
            return List.of();
        }
        SrJobBehaviorVisitor visitor = scopedVisitor(null);
        visitor.visit(query);
        return visitor.behavior().getRelations();
    }

    private List<BehaviorObject> querySources(List<BehaviorRelation> effects) {
        List<BehaviorObject> sources = new ArrayList<>();
        for (BehaviorRelation effect : effects) {
            if (effect.getAction() == BehaviorAction.READ || effect.getAction() == BehaviorAction.CALL) {
                sources.add(effect.getSubject());
                sources.addAll(effect.getTarget());
            }
        }
        return sources;
    }

    private void appendQueryEffects(List<BehaviorRelation> effects) {
        for (BehaviorRelation effect : effects) {
            if (effect.getAction() != BehaviorAction.READ || effect.getSubject().getObjectType() == TargetType.ConfigKey) {
                behavior().getRelations().add(effect);
            }
        }
    }

    @Override
    public Void visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        List<BehaviorRelation> effects = queryEffects(ctx.queryStatement());
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()), querySources(effects));
        appendQueryEffects(effects);
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        List<BehaviorRelation> effects = new ArrayList<>(queryEffects(ctx.queryStatement()));
        for (ExpressionsWithDefaultContext row : ctx.expressionsWithDefault()) {
            effects.addAll(queryEffects(row));
        }
        SplitQueryType type = SplitQueryType.INSERT;
        BehaviorAction action = BehaviorAction.INSERT;
        if (ctx.OVERWRITE() != null) {
            type = SplitQueryType.MERGE;
            action = BehaviorAction.MERGE;
        }
        List<BehaviorObject> destinations = List.of();
        if (ctx.qualifiedName() != null) {
            destinations = partitions(object(TargetType.Table, ctx.qualifiedName()), ctx.partitionNames());
        }
        for (BehaviorObject target : destinations) {
            BehaviorRelation relation = add(type, action, target, querySources(effects));
            if (!ctx.expressionsWithDefault().isEmpty()) {
                relation.setInsertRows((long) ctx.expressionsWithDefault().size());
            }
        }
        appendQueryEffects(effects);
        return null;
    }

    @Override
    public Void visitSimpleFunctionCall(SimpleFunctionCallContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, ctx.qualifiedName()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitFileTableFunction(FileTableFunctionContext ctx) {
        List<BehaviorObject> files = new ArrayList<>();
        StringContext path = propertyValue(ctx.propertyList(), "path");
        if (path != null) {
            files.add(inputFile(path));
        }
        add(SplitQueryType.SELECT, BehaviorAction.CALL,
            objects.object(TargetType.Function, ctx.FILES().getSymbol(), List.of(ctx.FILES().getText())), files);
        return null;
    }

    @Override
    public Void visitCreateRoutineLoadStatement(CreateRoutineLoadStatementContext ctx) {
        BehaviorObject job = databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load");
        add(SplitQueryType.CREATE_JOB, BehaviorAction.CREATE, job);
        SrJobBehaviorVisitor scope = scopedVisitor(ctx.db);
        BehaviorObject table = scope.object(TargetType.Table, ctx.table);
        String kind = name(ctx.source).toLowerCase(Locale.ROOT);
        List<BehaviorObject> inputs = new ArrayList<>();
        if (ctx.dataSourceProperties() != null) {
            PropertyListContext properties = ctx.dataSourceProperties().propertyList();
            StringContext topic = propertyValue(properties, kind + "_topic");
            String endpointKey = "kafka_broker_list";
            if ("pulsar".equals(kind)) {
                endpointKey = "pulsar_service_url";
            }
            StringContext endpoint = propertyValue(properties, endpointKey);
            if (topic != null && endpoint != null) {
                inputs.add(objects.instanceObject(TargetType.Queue, topic, kind + "/" + name(endpoint) + "/" + name(topic)));
            }
        }
        PartitionNamesContext partitionClause = null;
        for (LoadPropertiesContext property : ctx.loadProperties()) {
            if (property.partitionNames() != null) {
                partitionClause = property.partitionNames();
            }
        }
        for (BehaviorObject target : partitions(table, partitionClause)) {
            add(SplitQueryType.CREATE_JOB, BehaviorAction.IMPORT, target, inputs);
        }
        for (LoadPropertiesContext property : ctx.loadProperties()) {
            appendQueryEffects(scope.queryEffects(property));
        }
        return null;
    }

    @Override
    public Void visitAlterRoutineLoadStatement(AlterRoutineLoadStatementContext ctx) {
        add(SplitQueryType.ALTER_JOB, BehaviorAction.ALTER, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load"));
        for (LoadPropertiesContext property : ctx.loadProperties()) {
            appendQueryEffects(queryEffects(property));
        }
        return null;
    }

    @Override
    public Void visitPauseRoutineLoadStatement(PauseRoutineLoadStatementContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load"));
        return null;
    }

    @Override
    public Void visitResumeRoutineLoadStatement(ResumeRoutineLoadStatementContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.START, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load"));
        return null;
    }

    @Override
    public Void visitStopRoutineLoadStatement(StopRoutineLoadStatementContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.TERMINATE, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load"));
        return null;
    }

    @Override
    public Void visitShowRoutineLoadStatement(ShowRoutineLoadStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load"));
        return null;
    }

    @Override
    public Void visitShowStreamLoadStatement(ShowStreamLoadStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "stream_load"));
        return null;
    }

    @Override
    public Void visitShowCreateRoutineLoadStatement(ShowCreateRoutineLoadStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "routine_load"));
        return null;
    }

    // Only a literal equality on a single column can identify one metadata object.
    private ParserRuleContext equalityValue(ExpressionContext expression, String key) {
        if (expression == null) {
            return null;
        }
        for (int i = expression.getStart().getTokenIndex(); i <= expression.getStop().getTokenIndex(); i++) {
            Token token = parser.getTokenStream().get(i);
            if (token.getType() == OR || token.getType() == LOGICAL_OR || token.getType() == NOT) {
                return null;
            }
        }
        for (ComparisonContext comparison : descendants(expression, ComparisonContext.class)) {
            if (!"=".equals(comparison.comparisonOperator().getText())) {
                continue;
            }
            List<ColumnRefContext> columns = descendants(comparison.left, ColumnRefContext.class);
            if (columns.size() != 1 || !key.equalsIgnoreCase(name(columns.get(0))) ||
                comparison.left.getStart() != columns.get(0).getStart() || comparison.left.getStop() != columns.get(0).getStop()) {
                continue;
            }
            for (StringContext value : descendants(comparison.right, StringContext.class)) {
                if (value.getStart() == comparison.right.getStart() && value.getStop() == comparison.right.getStop()) {
                    return value;
                }
            }
            for (NumericLiteralContext value : descendants(comparison.right, NumericLiteralContext.class)) {
                if (value.getStart() == comparison.right.getStart() && value.getStop() == comparison.right.getStop()) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public Void visitShowRoutineLoadTaskStatement(ShowRoutineLoadTaskStatementContext ctx) {
        ParserRuleContext jobName = equalityValue(ctx.expression(), "JobName");
        String path = databasePath(ctx.db, ctx.LOAD().getSymbol()) + "routine_load/";
        BehaviorObject job = atPath(TargetType.Job, ctx.TASK().getSymbol(), path);
        if (jobName != null) {
            job = atPath(TargetType.Job, jobName, path + name(jobName) + "/task/");
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, job);
        return null;
    }

    @Override
    public Void visitLoadStatement(LoadStatementContext ctx) {
        SrJobBehaviorVisitor scope = scopedVisitor(ctx.label.db);
        if (ctx.data != null) {
            for (DataDescContext descriptor : ctx.data.dataDesc()) {
                BehaviorObject table = scope.object(TargetType.Table, descriptor.dstTableName);
                List<BehaviorObject> inputs = new ArrayList<>();
                if (descriptor.srcFiles != null) {
                    for (StringContext file : descriptor.srcFiles.string()) {
                        inputs.add(inputFile(file));
                    }
                } else if (descriptor.srcTableName != null) {
                    inputs.add(scope.object(TargetType.Table, descriptor.srcTableName));
                }
                for (BehaviorObject target : partitions(table, descriptor.partitions)) {
                    add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, target, inputs);
                }
                appendQueryEffects(scope.queryEffects(descriptor.colMappingList));
                appendQueryEffects(scope.queryEffects(descriptor.where));
            }
        }
        if (ctx.broker != null && ctx.broker.name != null) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.READ, objects.instanceObject(TargetType.Broker, ctx.broker.name, "broker/" + name(ctx.broker.name)));
        }
        if (ctx.resource != null) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.READ, objects.instanceObject(TargetType.Resource, ctx.resource.name, name(ctx.resource.name)));
        }
        return null;
    }

    @Override
    public Void visitAlterLoadStatement(AlterLoadStatementContext ctx) {
        add(SplitQueryType.ALTER_JOB, BehaviorAction.ALTER, databaseJob(ctx.db, ctx.name, ctx.LOAD().getSymbol(), "load/label"));
        return null;
    }

    private BehaviorObject loadJob(Token anchor, ParserRuleContext database, ExpressionContext filter, boolean all) {
        String path = databasePath(database, anchor) + "load/";
        if (all) {
            path = objects.instanceObject(TargetType.Job, anchor, "load").getObjectPath();
        }
        ParserRuleContext value = equalityValue(filter, "label");
        if (value != null) {
            return atPath(TargetType.Job, value, path + "label/" + name(value) + "/");
        }
        value = equalityValue(filter, "load_job_id");
        if (value != null) {
            return atPath(TargetType.Job, value, path + "id/" + name(value) + "/");
        }
        return atPath(TargetType.Job, anchor, path);
    }

    @Override
    public Void visitCancelLoadStatement(CancelLoadStatementContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.TERMINATE, loadJob(ctx.LOAD().getSymbol(), ctx.identifier(), ctx.expression(), false));
        return null;
    }

    @Override
    public Void visitShowLoadStatement(ShowLoadStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, loadJob(ctx.LOAD().getSymbol(), ctx.identifier(), ctx.expression(), ctx.ALL() != null));
        return null;
    }

    @Override
    public Void visitShowLoadWarningsStatement(ShowLoadWarningsStatementContext ctx) {
        if (ctx.string() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, inputFile(ctx.string()));
        } else {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, loadJob(ctx.LOAD().getSymbol(), ctx.identifier(), ctx.expression(), false));
        }
        return null;
    }

    @Override
    public Void visitCreatePipeStatement(CreatePipeStatementContext ctx) {
        QualifiedNameContext tableName = ctx.insertStatement().qualifiedName();
        IdentifierContext database = null;
        if (tableName.identifier().size() > 1) {
            database = tableName.identifier(tableName.identifier().size() - 2);
        }
        SrJobBehaviorVisitor scope = scopedVisitor(database);
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.orReplace().REPLACE() != null) {
            action = BehaviorAction.REPLACE;
        }
        add(SplitQueryType.CREATE_JOB, action, scope.object(TargetType.Pipe, ctx.qualifiedName()));
        appendBody(ctx.insertStatement(), database);
        return null;
    }

    @Override
    public Void visitAlterPipeStatement(AlterPipeStatementContext ctx) {
        AlterPipeClauseContext clause = ctx.alterPipeClause();
        BehaviorAction action = BehaviorAction.START;
        SplitQueryType type = SplitQueryType.ADMIN_JOB;
        if (clause.SET() != null) {
            action = BehaviorAction.ALTER;
            type = SplitQueryType.ALTER_JOB;
        } else if (clause.SUSPEND() != null) {
            action = BehaviorAction.STOP;
        }
        List<BehaviorObject> inputs = new ArrayList<>();
        if (clause.fileName != null) {
            inputs.add(inputFile(clause.fileName));
        }
        add(type, action, object(TargetType.Pipe, ctx.qualifiedName()), inputs);
        return null;
    }

    @Override
    public Void visitDropPipeStatement(DropPipeStatementContext ctx) {
        add(SplitQueryType.DROP_JOB, BehaviorAction.DROP, object(TargetType.Pipe, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitDescPipeStatement(DescPipeStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Pipe, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitShowPipeStatement(ShowPipeStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, atPath(TargetType.Pipe, ctx.PIPES().getSymbol(), databasePath(ctx.qualifiedName(), ctx.PIPES().getSymbol())));
        return null;
    }

    @Override
    public Void visitExportStatement(ExportStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.tableDesc().qualifiedName());
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, inputFile(ctx.string()), partitions(table, ctx.tableDesc().partitionNames()));
        if (ctx.brokerDesc() != null && ctx.brokerDesc().name != null) {
            IdentifierOrStringContext broker = ctx.brokerDesc().name;
            add(SplitQueryType.DATA_EXPORT, BehaviorAction.READ, objects.instanceObject(TargetType.Broker, broker, "broker/" + name(broker)));
        }
        return null;
    }

    @Override
    public Void visitShowExportStatement(ShowExportStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, atPath(TargetType.Job, ctx.EXPORT().getSymbol(), databasePath(ctx.catalog, ctx.EXPORT().getSymbol()) + "export/"));
        return null;
    }

    @Override
    public Void visitCancelExportStatement(CancelExportStatementContext ctx) {
        String path = databasePath(ctx.catalog, ctx.EXPORT().getSymbol()) + "export/";
        ParserRuleContext query = equalityValue(ctx.expression(), "queryid");
        BehaviorObject job = atPath(TargetType.Job, ctx.EXPORT().getSymbol(), path);
        if (query != null) {
            job = atPath(TargetType.Job, query, path + "query/" + name(query) + "/");
        }
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.TERMINATE, job);
        return null;
    }

    private BehaviorObject repository(IdentifierContext name) {
        return objects.instanceObject(TargetType.Repository, name, "repository/" + name(name));
    }

    @Override
    public Void visitCreateRepositoryStatement(CreateRepositoryStatementContext ctx) {
        List<BehaviorObject> inputs = new ArrayList<>();
        inputs.add(inputFile(ctx.location));
        if (ctx.brokerName != null) {
            inputs.add(objects.instanceObject(TargetType.Broker, ctx.brokerName, "broker/" + name(ctx.brokerName)));
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, repository(ctx.repoName), inputs);
        return null;
    }

    @Override
    public Void visitDropRepositoryStatement(DropRepositoryStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, repository(ctx.identifier()));
        return null;
    }

    @Override
    public Void visitShowRepositoriesStatement(ShowRepositoriesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Repository, ctx.REPOSITORIES().getSymbol(), "repository"));
        return null;
    }

    private BehaviorObject snapshot(QualifiedNameContext label, BehaviorObject repository, PropertyListContext properties) {
        List<IdentifierContext> names = label.identifier();
        String path = repository.getObjectPath() + "snapshot/" + name(names.get(names.size() - 1)) + "/";
        StringContext timestamp = propertyValue(properties, "backup_timestamp");
        if (timestamp != null) {
            path += "timestamp/" + name(timestamp) + "/";
        }
        return atPath(TargetType.Snapshot, label, path);
    }

    private ParserRuleContext backupDatabase(QualifiedNameContext label, IdentifierContext database, IdentifierContext alias) {
        if (alias != null) {
            return alias;
        }
        if (database != null) {
            return database;
        }
        if (label.identifier().size() > 1) {
            return label.identifier(0);
        }
        return null;
    }

    private List<BehaviorObject> backupObjects(List<BackupRestoreObjectDescContext> selections, ParserRuleContext database, boolean restore) {
        List<BehaviorObject> result = new ArrayList<>();
        SrJobBehaviorVisitor scope = scopedVisitor(database);
        for (BackupRestoreObjectDescContext selection : selections) {
            TargetType type = TargetType.Table;
            if (selection.MATERIALIZED() != null) {
                type = TargetType.Materialized;
            } else if (selection.VIEW() != null || selection.VIEWS() != null) {
                type = TargetType.View;
            } else if (selection.FUNCTION() != null || selection.FUNCTIONS() != null) {
                type = TargetType.Function;
            }
            if (selection.ALL() != null) {
                Token start = parser.getTokenStream().get(selection.ALL().getSymbol().getTokenIndex() + 1);
                // Skip hidden whitespace/comments without expanding the semantic anchor.
                int index = start.getTokenIndex();
                while (start.getChannel() != Token.DEFAULT_CHANNEL) {
                    start = parser.getTokenStream().get(++index);
                }
                BehaviorObject set = objects.instanceObject(type, start, selection.getStop(), "");
                set.setObjectPath(databasePath(database, start));
                result.add(set);
                continue;
            }
            BackupRestoreTableDescContext table = selection.backupRestoreTableDesc();
            QualifiedNameContext qualified = selection.qualifiedName();
            IdentifierContext alias = selection.identifier();
            if (table != null) {
                qualified = table.qualifiedName();
                alias = table.identifier();
            }
            ParserRuleContext targetName = qualified;
            if (restore && alias != null) {
                targetName = alias;
            }
            BehaviorObject target = scope.object(type, targetName);
            if (table != null) {
                result.addAll(partitions(target, table.partitionNames()));
            } else {
                result.add(target);
            }
        }
        return result;
    }

    @Override
    public Void visitBackupStatement(BackupStatementContext ctx) {
        BehaviorObject repo = repository(ctx.repoName);
        BehaviorObject snapshot = snapshot(ctx.qualifiedName(), repo, null);
        ParserRuleContext database = backupDatabase(ctx.qualifiedName(), ctx.dbName, null);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.EXTERNAL() != null) {
            if (ctx.identifierList() != null) {
                for (IdentifierContext catalog : ctx.identifierList().identifier()) {
                    targets.add(object(TargetType.Catalog, catalog));
                }
            } else {
                targets.add(objects.instanceObject(TargetType.Catalog, ctx.ALL().getSymbol(), ctx.CATALOGS().getSymbol(), ""));
                targets.get(0).setObjectPath(objects.instanceObject(TargetType.Catalog, ctx.ALL().getSymbol()).getObjectPath());
            }
        } else if (ctx.backupRestoreObjectDesc().isEmpty()) {
            targets.add(atPath(TargetType.Schema, ctx.qualifiedName(), databasePath(database, ctx.getStart())));
            if (database != null) {
                targets.set(0, object(TargetType.Schema, database));
            }
        } else {
            targets.addAll(backupObjects(ctx.backupRestoreObjectDesc(), database, false));
        }
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, snapshot, targets);
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.READ, repo);
        return null;
    }

    @Override
    public Void visitRestoreStatement(RestoreStatementContext ctx) {
        BehaviorObject repo = repository(ctx.repoName);
        BehaviorObject snapshot = snapshot(ctx.qualifiedName(), repo, ctx.propertyList());
        ParserRuleContext database = backupDatabase(ctx.qualifiedName(), ctx.dbName, ctx.dbAlias);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.EXTERNAL() != null) {
            if (ctx.identifierWithAliasList() != null) {
                for (IdentifierWithAliasContext catalog : ctx.identifierWithAliasList().identifierWithAlias()) {
                    IdentifierContext target = catalog.originalName;
                    if (catalog.alias != null) {
                        target = catalog.alias;
                    }
                    targets.add(object(TargetType.Catalog, target));
                }
            } else {
                BehaviorObject all = objects.instanceObject(TargetType.Catalog, ctx.ALL().getSymbol(), ctx.CATALOGS().getSymbol(), "");
                all.setObjectPath(objects.instanceObject(TargetType.Catalog, ctx.ALL().getSymbol()).getObjectPath());
                targets.add(all);
            }
        } else if (!ctx.backupRestoreObjectDesc().isEmpty()) {
            targets.addAll(backupObjects(ctx.backupRestoreObjectDesc(), database, true));
            if (database == null) {
                // Restore obtains the database from the snapshot manifest, never the current session.
                String unknownDatabase = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Catalog).getObjectPath();
                for (BehaviorObject target : targets) {
                    target.setObjectPath(unknownDatabase);
                }
            }
        } else if (database == null) {
            targets.add(objects.instanceObject(TargetType.Object, ctx.qualifiedName()));
        } else {
            targets.add(object(TargetType.Schema, database));
        }
        for (BehaviorObject target : targets) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.RESTORE, target, List.of(snapshot));
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.READ, repo);
        return null;
    }

    private Void backupJob(Token keyword, IdentifierContext database, boolean external, boolean cancel, String kind) {
        SplitQueryType type = SplitQueryType.PERFORMANCE;
        BehaviorAction action = BehaviorAction.READ;
        String instancePath = objects.instanceObject(TargetType.Job, keyword, kind).getObjectPath();
        if (cancel) {
            type = SplitQueryType.ADMIN_JOB;
            action = BehaviorAction.TERMINATE;
        }
        String path = instancePath;
        if (external) {
            path += "external_catalog/";
        } else if (database != null || cancel) {
            path = databasePath(database, keyword) + kind + "/";
        }
        add(type, action, atPath(TargetType.Job, keyword, path));
        // Native SHOW also includes the external-catalog job even with FROM <db>.
        if (!cancel && database != null) {
            add(type, action, atPath(TargetType.Job, keyword, instancePath + "external_catalog/"));
        }
        return null;
    }

    @Override
    public Void visitCancelBackupStatement(CancelBackupStatementContext ctx) {
        return backupJob(ctx.BACKUP().getSymbol(), ctx.identifier(), ctx.EXTERNAL() != null, true, "backup");
    }

    @Override
    public Void visitShowBackupStatement(ShowBackupStatementContext ctx) {
        return backupJob(ctx.BACKUP().getSymbol(), ctx.identifier(), false, false, "backup");
    }

    @Override
    public Void visitCancelRestoreStatement(CancelRestoreStatementContext ctx) {
        return backupJob(ctx.RESTORE().getSymbol(), ctx.identifier(), ctx.EXTERNAL() != null, true, "restore");
    }

    @Override
    public Void visitShowRestoreStatement(ShowRestoreStatementContext ctx) {
        return backupJob(ctx.RESTORE().getSymbol(), ctx.identifier(), false, false, "restore");
    }

    @Override
    public Void visitShowSnapshotStatement(ShowSnapshotStatementContext ctx) {
        BehaviorObject repo = repository(ctx.identifier());
        String path = repo.getObjectPath() + "snapshot/";
        ParserRuleContext label = equalityValue(ctx.expression(), "snapshot");
        BehaviorObject snapshot = atPath(TargetType.Snapshot, ctx.SNAPSHOT().getSymbol(), path);
        if (label != null) {
            path += name(label) + "/";
            ParserRuleContext timestamp = equalityValue(ctx.expression(), "timestamp");
            if (timestamp != null) {
                path += "timestamp/" + name(timestamp) + "/";
            }
            snapshot = atPath(TargetType.Snapshot, label, path);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, snapshot, List.of(repo));
        return null;
    }

    @Override
    public Void visitRecoverDbStmt(RecoverDbStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.RECOVER, object(TargetType.Schema, ctx.identifier()));
        return null;
    }

    @Override
    public Void visitRecoverTableStatement(RecoverTableStatementContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RECOVER, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitRecoverPartitionStatement(RecoverPartitionStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.table);
        add(SplitQueryType.ADMIN_PARTITION, BehaviorAction.RECOVER, atPath(TargetType.Partition, ctx.identifier(), table.getObjectPath() + name(ctx.identifier()) + "/"));
        return null;
    }

    @Override
    public Void visitCreateImageClause(CreateImageClauseContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.CHECKPOINT, objects.instanceObject(TargetType.Instance, ctx.IMAGE().getSymbol()));
        return null;
    }

    @Override
    public Void visitAdminSetAutomatedSnapshotOnStatement(AdminSetAutomatedSnapshotOnStatementContext ctx) {
        BehaviorObject config = objects.instanceObject(TargetType.ConfigKey, ctx.AUTOMATED().getSymbol(), ctx.SNAPSHOT().getSymbol(), "automated_cluster_snapshot");
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.svName != null) {
            targets.add(objects.instanceObject(TargetType.StorageVolume, ctx.svName, name(ctx.svName)));
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, config, targets);
        return null;
    }

    @Override
    public Void visitAdminSetAutomatedSnapshotOffStatement(AdminSetAutomatedSnapshotOffStatementContext ctx) {
        BehaviorObject config = objects.instanceObject(TargetType.ConfigKey, ctx.AUTOMATED().getSymbol(), ctx.SNAPSHOT().getSymbol(), "automated_cluster_snapshot");
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, config);
        BehaviorObject snapshots = objects.instanceObject(TargetType.Snapshot, ctx.SNAPSHOT().getSymbol(), "automated_cluster_snapshot");
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.PURGE, snapshots);
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.UNSAFE, snapshots);
        return null;
    }
}

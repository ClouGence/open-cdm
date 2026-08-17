/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.doris.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.doris.parser.DrSplitVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParser.*;

final class DrBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    DrBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        DrStatementBehaviorVisitor visitor = new DrStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        SplitQueryType statementType = tree.accept(DrSplitVisitor.INSTANCE);
        visitor.behavior().setStatementType(statementType == null ? SplitQueryType.UNKNOWN : statementType);
        visitor.visit(tree);
        visitor.complete(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class DrStatementBehaviorVisitor extends DorisParserBaseVisitor<Void> {
    private static final java.util.Set<String> EXTERNAL_TABLE_FUNCTIONS = java.util.Set.of("azure", "file", "gcs", "hdfs", "http_stream", "jdbc", "local", "s3");
    private final Parser                       parser;
    private final RdbBehaviorObjectFactory     objects;
    private final StatementBehavior            behavior                 = new StatementBehavior();
    private int                                fallbackStartLine        = 1;
    private int                                fallbackStartColumn;

    DrStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    void complete(ParseTree tree) {
        if (!behavior.getRelations().isEmpty() || !(tree instanceof ParserRuleContext context)) {
            return;
        }
        SplitQueryType type = behavior.getStatementType();
        String sql = parser.getTokenStream().getText(context.getStart(), context.getStop());
        String statementSql = stripLeadingComments(sql);
        int statementOffset = sql.indexOf(statementSql);
        String normalized = statementSql.stripLeading().toUpperCase(Locale.ROOT);
        fallbackStartLine = context.getStart().getLine();
        fallbackStartColumn = context.getStart().getCharPositionInLine();
        if (addTextFallback(sql, statementOffset, normalized, context, type)) {
            return;
        }
        if (isUnsafe(normalized)) {
            add(type, BehaviorAction.UNSAFE, objects.unnamedObject(TargetType.Unknown, context, UmiTypes.Instance));
        } else if (type == SplitQueryType.SELECT) {
            add(type, BehaviorAction.READ, objects.unnamedObject(TargetType.Query, context, UmiTypes.Instance));
        } else if (type == SplitQueryType.TRANSACTION) {
            add(type, transactionAction(normalized), objects.unnamedObject(TargetType.Transaction, context, UmiTypes.Instance));
        } else {
            add(type, BehaviorAction.UNKNOWN, objects.unnamedObject(TargetType.Unknown, context, UmiTypes.Instance));
        }
    }

    private boolean addTextFallback(String sql, int statementOffset, String normalized, ParserRuleContext context, SplitQueryType type) {
        if (normalized.startsWith("MERGE INTO")) {
            return addFirstNamed(sql, statementOffset, "MERGE INTO", TargetType.Table, BehaviorAction.MERGE, type, false);
        }
        if (normalized.startsWith("ALTER TABLE")) {
            BehaviorAction action = normalized.contains("EXECUTE EXPIRE_SNAPSHOTS") ? BehaviorAction.PURGE : BehaviorAction.ALTER;
            TargetType target = normalized.contains(" SET STATS") ? TargetType.Statistics : TargetType.Table;
            return addFirstNamed(sql, statementOffset, "ALTER TABLE", target, action, type, false);
        }
        if (normalized.startsWith("ALTER MATERIALIZED VIEW")) {
            return addFirstNamed(sql, statementOffset, "ALTER MATERIALIZED VIEW", TargetType.Materialized, BehaviorAction.ALTER, type, false);
        }
        if (normalized.startsWith("ALTER ROLE")) {
            return addFirstNamed(sql, statementOffset, "ALTER ROLE", TargetType.Role, BehaviorAction.ALTER, type, true);
        }
        if (normalized.startsWith("ALTER USER")) {
            return addFirstNamed(sql, statementOffset, "ALTER USER", TargetType.User, BehaviorAction.ALTER, type, true);
        }
        if (normalized.startsWith("SET PROPERTY FOR")) {
            return addFirstNamed(sql, statementOffset, "SET PROPERTY FOR", TargetType.User, BehaviorAction.CONFIGURE, type, true);
        }
        if (normalized.startsWith("SET PROPERTY ")) {
            add(type, BehaviorAction.CONFIGURE, objects.unnamedObject(TargetType.User, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("USE ") && normalized.contains("@")) {
            int at = sql.indexOf('@', statementOffset);
            return at >= 0 && addIdentifierAt(sql, at + 1, TargetType.ComputeGroup, BehaviorAction.SWITCH, type, true);
        }
        if (normalized.startsWith("CREATE INVERTED INDEX ")) {
            TargetType target = normalized
                .startsWith("CREATE INVERTED INDEX ANALYZER") ? TargetType.Analyzer : normalized.startsWith("CREATE INVERTED INDEX TOKENIZER") ? TargetType.Tokenizer : normalized
                    .startsWith("CREATE INVERTED INDEX TOKEN_FILTER") ? TargetType.TokenFilter : normalized
                        .startsWith("CREATE INVERTED INDEX CHAR_FILTER") ? TargetType.CharFilter : TargetType.Normalizer;
            return addFirstNamed(sql, statementOffset, invertedIndexComponent(target), "IF NOT EXISTS", target, BehaviorAction.CREATE, type, true);
        }
        if (normalized.startsWith("CREATE DICTIONARY")) {
            return addFirstNamed(sql, statementOffset, "CREATE DICTIONARY", TargetType.Dictionary, BehaviorAction.CREATE, type, false);
        }
        if (normalized.startsWith("DROP INVERTED INDEX ")) {
            TargetType target = normalized
                .startsWith("DROP INVERTED INDEX ANALYZER") ? TargetType.Analyzer : normalized.startsWith("DROP INVERTED INDEX TOKENIZER") ? TargetType.Tokenizer : normalized
                    .startsWith("DROP INVERTED INDEX TOKEN_FILTER") ? TargetType.TokenFilter : normalized
                        .startsWith("DROP INVERTED INDEX CHAR_FILTER") ? TargetType.CharFilter : TargetType.Normalizer;
            return addFirstNamed(sql, statementOffset, invertedIndexComponent(target), "IF EXISTS", target, BehaviorAction.DROP, type, true);
        }
        if (normalized.startsWith("CREATE SQL_BLOCK_RULE") || normalized.startsWith("ALTER SQL_BLOCK_RULE") || normalized.startsWith("DROP SQL_BLOCK_RULE")) {
            BehaviorAction action = normalized.startsWith("CREATE") ? BehaviorAction.CREATE : normalized.startsWith("ALTER") ? BehaviorAction.ALTER : BehaviorAction.DROP;
            boolean added = addFirstNamed(sql, statementOffset, "SQL_BLOCK_RULE", TargetType.SqlBlockRule, action, type, true);
            if (added && action != BehaviorAction.DROP) {
                // A block rule persists SQL text or a regular expression that will be
                // applied to later statements. Treat that second-order execution surface
                // independently from the rule's lifecycle operation.
                addFirstNamed(sql, statementOffset, "SQL_BLOCK_RULE", TargetType.SqlBlockRule, BehaviorAction.UNSAFE, type, true);
            }
            return added;
        }
        if (normalized.startsWith("CREATE ENCRYPTKEY") || normalized.startsWith("DROP ENCRYPTKEY")) {
            BehaviorAction action = normalized.startsWith("CREATE") ? BehaviorAction.CREATE : BehaviorAction.DROP;
            return addFirstNamed(sql, statementOffset, "ENCRYPTKEY", "IF EXISTS", TargetType.EncryptionKey, action, type, true);
        }
        if (normalized.startsWith("SET LDAP_ADMIN_PASSWORD")) {
            add(type, BehaviorAction.CONFIGURE, objects.unnamedObject(TargetType.AuthenticationIntegration, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("SET ") && normalized.contains(" AS DEFAULT STORAGE VAULT")) {
            return addFirstNamed(sql, statementOffset, "SET", TargetType.StorageVault, BehaviorAction.SWITCH, type, true);
        }
        if (normalized.startsWith("UNSET DEFAULT STORAGE VAULT")) {
            add(type, BehaviorAction.RESET, objects.unnamedObject(TargetType.StorageVault, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("REFRESH LDAP")) {
            add(type, BehaviorAction.REFRESH, objects.unnamedObject(TargetType.AuthenticationIntegration, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("REFRESH CATALOG")) {
            return addFirstNamed(sql, statementOffset, "REFRESH CATALOG", TargetType.Catalog, BehaviorAction.REFRESH, type, false);
        }
        if (normalized.startsWith("REFRESH DATABASE")) {
            return addFirstNamed(sql, statementOffset, "REFRESH DATABASE", TargetType.Schema, BehaviorAction.REFRESH, type, false);
        }
        if (normalized.startsWith("REFRESH MATERIALIZED VIEW")) {
            return addFirstNamed(sql, statementOffset, "REFRESH MATERIALIZED VIEW", TargetType.Materialized, BehaviorAction.REFRESH, type, false);
        }
        if (normalized.startsWith("BUILD INDEX")) {
            boolean named = !startsWithKeyword(normalized, "BUILD INDEX ON");
            if (named && addFirstNamed(sql, statementOffset, "BUILD INDEX", TargetType.Index, BehaviorAction.LOAD, type, false))
                return true;
            add(type, BehaviorAction.LOAD, objects.unnamedObject(TargetType.Index, context, UmiTypes.Schema));
            return true;
        }
        if (normalized.startsWith("SHOW BACKUP") || normalized.startsWith("SHOW RESTORE") || normalized.startsWith("SHOW BRIEF RESTORE")
            || normalized.startsWith("SHOW SNAPSHOT")) {
            add(type, BehaviorAction.READ, objects.unnamedObject(TargetType.Backup, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("SHOW ")) {
            TargetType target = normalized
                .contains("INDEX") ? TargetType.Index : normalized.contains("STATS") || normalized.contains("ANALYZE") ? TargetType.Statistics : TargetType.Query;
            add(type, BehaviorAction.READ, objects.unnamedObject(target, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("DESC FUNCTION")) {
            return addFirstNamed(sql, statementOffset, "DESC FUNCTION", TargetType.Function, BehaviorAction.READ, type, false);
        }
        if (normalized.startsWith("DESC ") || normalized.startsWith("DESCRIBE ")) {
            String phrase = normalized.startsWith("DESC ") ? "DESC" : "DESCRIBE";
            return addFirstNamed(sql, statementOffset, phrase, TargetType.Table, BehaviorAction.READ, type, false);
        }
        if (startsWithKeyword(normalized, "EXPLAIN")) {
            add(type, BehaviorAction.ANALYZE, objects.unnamedObject(TargetType.Query, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("HELP ")) {
            add(type, BehaviorAction.READ, objects.unnamedObject(TargetType.Query, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("PLAN REPLAYER PLAY")) {
            if (!addFirstLiteral(sql, statementOffset, "PLAY", TargetType.File, BehaviorAction.UNSAFE, type)) {
                add(type, BehaviorAction.UNSAFE, objects.unnamedObject(TargetType.File, context, UmiTypes.Instance));
            }
            return true;
        }
        if (normalized.startsWith("WARM UP COMPUTE GROUP")) {
            return addFirstNamed(sql, statementOffset, "WARM UP COMPUTE GROUP", TargetType.ComputeGroup, BehaviorAction.LOAD, type, true);
        }
        if (normalized.startsWith("WARM UP SELECT")) {
            add(type, BehaviorAction.LOAD, objects.unnamedObject(TargetType.Query, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("CLEAN ALL PROFILE") || normalized.startsWith("CLEAN ALL QUERY STATS")) {
            add(type, BehaviorAction.PURGE, objects.unnamedObject(TargetType.Query, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("DROP ANALYZE JOB") || normalized.startsWith("KILL ANALYZE") || normalized.startsWith("CANCEL WARM UP JOB")) {
            BehaviorAction action = normalized.startsWith("DROP") ? BehaviorAction.DROP : BehaviorAction.TERMINATE;
            add(type, action, objects.unnamedObject(TargetType.Job, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("CANCEL LOAD") || normalized.startsWith("CANCEL EXPORT") || normalized.startsWith("CANCEL MATERIALIZED VIEW TASK")) {
            add(type, BehaviorAction.TERMINATE, objects.unnamedObject(TargetType.Job, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("PAUSE MATERIALIZED VIEW JOB") || normalized.startsWith("RESUME MATERIALIZED VIEW JOB") || normalized.startsWith("PAUSE ALL ROUTINE LOAD")
            || normalized.startsWith("RESUME ALL ROUTINE LOAD")) {
            add(type, normalized.startsWith("PAUSE") ? BehaviorAction.STOP : BehaviorAction.START, objects.unnamedObject(TargetType.Job, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("RECOVER PARTITION")) {
            return addFirstNamed(sql, statementOffset, "RECOVER PARTITION", TargetType.Partition, BehaviorAction.RECOVER, type, false);
        }
        if (normalized.startsWith("RECOVER TABLE")) {
            return addFirstNamed(sql, statementOffset, "RECOVER TABLE", TargetType.Table, BehaviorAction.RECOVER, type, false);
        }
        if (normalized.startsWith("REFRESH TABLE")) {
            return addFirstNamed(sql, statementOffset, "REFRESH TABLE", TargetType.Table, BehaviorAction.REFRESH, type, false);
        }
        if (normalized.startsWith("CANCEL ALTER TABLE")) {
            return addFirstNamed(sql, statementOffset, "FROM", TargetType.Table, BehaviorAction.STOP, type, false);
        }
        if (normalized.startsWith("LOAD DATA")) {
            boolean added = addFirstNamed(sql, statementOffset, "INTO TABLE", TargetType.Table, BehaviorAction.IMPORT, type, false);
            addFirstLiteral(sql, statementOffset, "INFILE", TargetType.File, BehaviorAction.UNSAFE, type);
            return added;
        }
        if (normalized.startsWith("RECOVER DATABASE")) {
            return addFirstNamed(sql, statementOffset, "RECOVER DATABASE", TargetType.Schema, BehaviorAction.RECOVER, type, false);
        }
        if (normalized.startsWith("CANCEL BACKUP")) {
            add(type, BehaviorAction.TERMINATE, objects.unnamedObject(TargetType.Backup, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("KILL ")) {
            add(type, BehaviorAction.TERMINATE, objects.unnamedObject(TargetType.Session, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ALTER SYSTEM")) {
            BehaviorAction action = normalized
                .contains(" ADD ") ? BehaviorAction.CREATE : normalized.contains(" DROP ")
                                                             || normalized.contains(" DECOMMISSION ") ? BehaviorAction.DROP : BehaviorAction.CONFIGURE;
            add(type, action, objects.unnamedObject(TargetType.Machine, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN SET TABLE")) {
            return addFirstNamed(sql, statementOffset, "ADMIN SET TABLE", TargetType.Table, BehaviorAction.CONFIGURE, type, false);
        }
        if (normalized.startsWith("ADMIN COMPACT TABLE")) {
            return addFirstNamed(sql, statementOffset, "ADMIN COMPACT TABLE", TargetType.Table, BehaviorAction.OPTIMIZE, type, false);
        }
        if (normalized.startsWith("ADMIN REPAIR TABLE") || normalized.startsWith("ADMIN CANCEL REPAIR TABLE")) {
            BehaviorAction action = normalized.startsWith("ADMIN CANCEL") ? BehaviorAction.STOP : BehaviorAction.REPAIR;
            String phrase = normalized.startsWith("ADMIN CANCEL") ? "CANCEL REPAIR TABLE" : "REPAIR TABLE";
            return addFirstNamed(sql, statementOffset, phrase, TargetType.Table, action, type, false);
        }
        if (normalized.startsWith("ADMIN SHOW REPLICA")) {
            return addFirstNamed(sql, statementOffset, "FROM", TargetType.Table, BehaviorAction.READ, type, false);
        }
        if (normalized.startsWith("ADMIN SET REPLICA")) {
            add(type, BehaviorAction.CONFIGURE, objects.unnamedObject(TargetType.Replication, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN SHOW FRONTEND CONFIG")) {
            add(type, BehaviorAction.READ, objects.unnamedObject(TargetType.ConfigKey, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN CLEAN TRASH")) {
            add(type, BehaviorAction.PURGE, objects.unnamedObject(TargetType.Machine, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN COPY TABLET")) {
            add(type, BehaviorAction.COPY, objects.unnamedObject(TargetType.Partition, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN REBALANCE DISK") || normalized.startsWith("ADMIN CANCEL REBALANCE DISK")) {
            add(type, normalized.startsWith("ADMIN CANCEL") ? BehaviorAction.STOP : BehaviorAction.MOVE, objects.unnamedObject(TargetType.Machine, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN SET ALL FRONTENDS CONFIG")) {
            add(type, BehaviorAction.CONFIGURE, objects.unnamedObject(TargetType.ConfigKey, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ALTER COLOCATE GROUP")) {
            return addFirstNamed(sql, statementOffset, "ALTER COLOCATE GROUP", TargetType.ResourceGroup, BehaviorAction.ALTER, type, true);
        }
        if (normalized.startsWith("CANCEL BUILD INDEX")) {
            add(type, BehaviorAction.STOP, objects.unnamedObject(TargetType.Index, context, UmiTypes.Schema));
            return true;
        }
        if (normalized.startsWith("CANCEL DECOMMISSION BACKEND")) {
            add(type, BehaviorAction.STOP, objects.unnamedObject(TargetType.Machine, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("CANCEL RESTORE")) {
            add(type, BehaviorAction.TERMINATE, objects.unnamedObject(TargetType.Backup, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("CLEAN LABEL")) {
            add(type, BehaviorAction.PURGE, objects.unnamedObject(TargetType.Job, context, UmiTypes.Instance));
            return true;
        }
        if (normalized.startsWith("ADMIN CHECK TABLET")) {
            add(type, BehaviorAction.VALIDATE, objects.unnamedObject(TargetType.Partition, context, UmiTypes.Instance));
            return true;
        }
        if (type == SplitQueryType.ADMIN_PERFORMANCE && normalized.startsWith("ALTER") && normalized.contains(" SET STATS")) {
            return addFirstNamed(sql, statementOffset, "ALTER TABLE", TargetType.Statistics, BehaviorAction.ALTER, type, false);
        }
        if (normalized.equals("SYNC") || normalized.equals("SYNC;")) {
            add(type, BehaviorAction.FLUSH, objects.unnamedObject(TargetType.Instance, context, UmiTypes.Instance));
            return true;
        }
        return false;
    }

    private boolean isUnsafe(String sql) {
        return sql.startsWith("SHUTDOWN") || sql.startsWith("ADMIN SET FRONTEND CONFIG") || sql.startsWith("INSTALL PLUGIN") || sql.startsWith("UNINSTALL PLUGIN")
               || sql.startsWith("CREATE FUNCTION") || sql.startsWith("CREATE ALIAS FUNCTION") || sql.startsWith("CREATE JOB") || sql.startsWith("ALTER JOB");
    }

    private BehaviorAction transactionAction(String sql) {
        if (sql.startsWith("BEGIN") || sql.startsWith("START TRANSACTION")) {
            return BehaviorAction.START;
        }
        if (sql.startsWith("COMMIT")) {
            return BehaviorAction.STOP;
        }
        return BehaviorAction.RESET;
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.multipartIdentifier()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitFunctionCallExpression(FunctionCallExpressionContext ctx) {
        String name = functionName(ctx.functionIdentifier());
        add(SplitQueryType.SELECT, BehaviorAction.CALL, function(ctx.functionIdentifier()));
        if (EXTERNAL_TABLE_FUNCTIONS.contains(name)) {
            BehaviorObject source = externalFunctionSource(ctx);
            add(SplitQueryType.SELECT, BehaviorAction.READ, source);
            add(SplitQueryType.SELECT, BehaviorAction.UNSAFE, source);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitTableValuedFunction(TableValuedFunctionContext ctx) {
        String name = unquote(text(ctx.tvfName)).toLowerCase(Locale.ROOT);
        add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, ctx.tvfName));
        if (EXTERNAL_TABLE_FUNCTIONS.contains(name)) {
            BehaviorObject source = externalTableFunctionSource(ctx);
            add(SplitQueryType.SELECT, BehaviorAction.READ, source);
            add(SplitQueryType.SELECT, BehaviorAction.UNSAFE, source);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCallProcedure(CallProcedureContext ctx) {
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.name));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitUserVariable(UserVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, variableName(ctx.identifierOrText())));
        return null;
    }

    @Override
    public Void visitSystemVariable(SystemVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, unquote(text(ctx.identifier()))));
        return null;
    }

    @Override
    public Void visitInsertTable(InsertTableContext ctx) {
        if (ctx.explain() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableName), tableSources(ctx.query()));
            return null;
        }
        SplitQueryType type = ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        BehaviorRelation relation = add(type, type == SplitQueryType.INSERT ? BehaviorAction.INSERT : BehaviorAction.MERGE, object(TargetType.Table, ctx.tableName), tableSources(ctx.query()));
        InlineTableContext values = first(ctx.query(), InlineTableContext.class);
        if (relation != null && values != null) {
            relation.setInsertRows((long) values.rowConstructor().size());
        }
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitUpdate(UpdateContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.whereClause());
        if (ctx.explain() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableName), sources);
            return null;
        }
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.tableName), sources);
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitDelete(DeleteContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.relations());
        addTableSources(sources, ctx.whereClause());
        if (ctx.explain() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableName), sources);
            return null;
        }
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.tableName), sources);
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitCreateTable(CreateTableContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.name), tableSources(ctx.query()));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitCreateTableLike(CreateTableLikeContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.name), List.of(object(TargetType.Table, ctx.existedTable)));
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.name), tableSources(ctx.query()));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitCreateMTMV(CreateMTMVContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.mvName), tableSources(ctx.query()));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        add(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateCatalog(CreateCatalogContext ctx) {
        add(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitCreateResource(CreateResourceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, instanceObject(TargetType.Resource, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateWorkloadGroup(CreateWorkloadGroupContext ctx) {
        add(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE, instanceObject(TargetType.ResourceGroup, ctx.name), optionalTarget(TargetType.ComputeGroup, ctx.computeGroup));
        return null;
    }

    @Override
    public Void visitCreateWorkloadPolicy(CreateWorkloadPolicyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, instanceObject(TargetType.WorkloadPolicy, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateStoragePolicy(CreateStoragePolicyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, instanceObject(TargetType.StoragePolicy, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateStorageVault(CreateStorageVaultContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, instanceObject(TargetType.StorageVault, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateStage(CreateStageContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, instanceObject(TargetType.Stage, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateRepository(CreateRepositoryContext ctx) {
        BehaviorObject repository = instanceObject(TargetType.Repository, ctx.name);
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, repository, List.of(repositoryLocation(ctx.storageBackend())));
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.UNSAFE, repository);
        return null;
    }

    @Override
    public Void visitCreateFile(CreateFileContext ctx) {
        BehaviorObject file = literalObject(TargetType.File, ctx.name);
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, file, optionalTarget(TargetType.Schema, ctx.database));
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.UNSAFE, file);
        return null;
    }

    @Override
    public Void visitCreateRowPolicy(CreateRowPolicyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(object(TargetType.Table, ctx.table));
        if (ctx.user != null) {
            targets.add(user(ctx.user));
        } else if (ctx.roleName != null) {
            targets.add(instanceObject(TargetType.Role, ctx.roleName));
        }
        add(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, instanceObject(TargetType.RowAccessPolicy, ctx.name), targets);
        return null;
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, instanceObject(TargetType.Role, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        add(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, user(ctx.grantUserIdentify()));
        return null;
    }

    @Override
    public Void visitCreateUserDefineFunction(CreateUserDefineFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, function(ctx.functionIdentifier()));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitCreateAliasFunction(CreateAliasFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, function(ctx.functionIdentifier()));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitDropCatalog(DropCatalogContext ctx) {
        add(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitDropResource(DropResourceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instanceObject(TargetType.Resource, ctx.name));
        return null;
    }

    @Override
    public Void visitDropWorkloadGroup(DropWorkloadGroupContext ctx) {
        add(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP, instanceObject(TargetType.ResourceGroup, ctx.name), optionalTarget(TargetType.ComputeGroup, ctx.computeGroup));
        return null;
    }

    @Override
    public Void visitDropWorkloadPolicy(DropWorkloadPolicyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instanceObject(TargetType.WorkloadPolicy, ctx.name));
        return null;
    }

    @Override
    public Void visitDropStoragePolicy(DropStoragePolicyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instanceObject(TargetType.StoragePolicy, ctx.name));
        return null;
    }

    @Override
    public Void visitDropStage(DropStageContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instanceObject(TargetType.Stage, ctx.name));
        return null;
    }

    @Override
    public Void visitDropRepository(DropRepositoryContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instanceObject(TargetType.Repository, ctx.name));
        return null;
    }

    @Override
    public Void visitDropFile(DropFileContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.DROP, literalObject(TargetType.File, ctx.name), optionalTarget(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitDropRowPolicy(DropRowPolicyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(object(TargetType.Table, ctx.tableName));
        if (ctx.userIdentify() != null) {
            targets.add(user(ctx.userIdentify()));
        } else if (ctx.roleName != null) {
            targets.add(instanceObject(TargetType.Role, ctx.roleName));
        }
        add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, instanceObject(TargetType.RowAccessPolicy, ctx.policyName), targets);
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, instanceObject(TargetType.Role, ctx.name));
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        add(SplitQueryType.DROP_USER, BehaviorAction.DROP, user(ctx.userIdentify()));
        return null;
    }

    @Override
    public Void visitDropFunction(DropFunctionContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, function(ctx.functionIdentifier()));
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.name));
        return null;
    }

    @Override
    public Void visitDropMV(DropMVContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableName));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitAlterView(AlterViewContext ctx) {
        add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, ctx.name), tableSources(ctx.query()));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitAlterResource(AlterResourceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, instanceObject(TargetType.Resource, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterWorkloadGroup(AlterWorkloadGroupContext ctx) {
        add(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER, instanceObject(TargetType.ResourceGroup, ctx.name), optionalTarget(TargetType.ComputeGroup, ctx.computeGroup));
        return null;
    }

    @Override
    public Void visitAlterComputeGroup(AlterComputeGroupContext ctx) {
        add(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER, instanceObject(TargetType.ComputeGroup, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterWorkloadPolicy(AlterWorkloadPolicyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, instanceObject(TargetType.WorkloadPolicy, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterStoragePolicy(AlterStoragePolicyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, instanceObject(TargetType.StoragePolicy, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterStorageVault(AlterStorageVaultContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, object(TargetType.StorageVault, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterRepository(AlterRepositoryContext ctx) {
        BehaviorObject repository = instanceObject(TargetType.Repository, ctx.name);
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, repository);
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.UNSAFE, repository);
        return null;
    }

    @Override
    public Void visitAlterCatalogRename(AlterCatalogRenameContext ctx) {
        add(SplitQueryType.RENAME_CATALOG, BehaviorAction.RENAME, instanceObject(TargetType.Catalog, ctx.name), optionalTarget(TargetType.Catalog, ctx.newName));
        return null;
    }

    @Override
    public Void visitAlterCatalogProperties(AlterCatalogPropertiesContext ctx) {
        add(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, instanceObject(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterCatalogComment(AlterCatalogCommentContext ctx) {
        add(SplitQueryType.COMMENT_CATALOG, BehaviorAction.ALTER, instanceObject(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterDatabaseRename(AlterDatabaseRenameContext ctx) {
        add(SplitQueryType.RENAME_SCHEMA, BehaviorAction.RENAME, instanceObject(TargetType.Schema, ctx.name), optionalTarget(TargetType.Schema, ctx.newName));
        return null;
    }

    @Override
    public Void visitAlterDatabaseSetQuota(AlterDatabaseSetQuotaContext ctx) {
        add(SplitQueryType.ALTER_SCHEMA, BehaviorAction.CONFIGURE, instanceObject(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterDatabaseProperties(AlterDatabasePropertiesContext ctx) {
        add(SplitQueryType.ALTER_SCHEMA, BehaviorAction.ALTER, instanceObject(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitSwitchCatalog(SwitchCatalogContext ctx) {
        add(SplitQueryType.SWITCH_CATALOG, BehaviorAction.SWITCH, object(TargetType.Catalog, ctx.catalog));
        return null;
    }

    @Override
    public Void visitUseDatabase(UseDatabaseContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitSetVariableWithType(SetVariableWithTypeContext ctx) {
        add(behavior.getStatementType(), BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitSetSystemVariable(SetSystemVariableContext ctx) {
        add(behavior.getStatementType(), BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitSetUserVariable(SetUserVariableContext ctx) {
        add(behavior.getStatementType(), BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitCreateScheduledJob(CreateScheduledJobContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.label);
        List<BehaviorObject> dependencies = jobDependencies(ctx);
        add(SplitQueryType.CREATE_JOB, BehaviorAction.CREATE, job, dependencies);
        // A stored future statement is a second-order execution surface.
        add(SplitQueryType.CREATE_JOB, BehaviorAction.UNSAFE, job);
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitAlterJob(AlterJobContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.jobName);
        add(SplitQueryType.ALTER_JOB, BehaviorAction.ALTER, job, jobDependencies(ctx));
        add(SplitQueryType.ALTER_JOB, BehaviorAction.UNSAFE, job);
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitPauseJob(PauseJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, literalObject(TargetType.Job, ctx.jobNameValue));
        return null;
    }

    @Override
    public Void visitResumeJob(ResumeJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.START, literalObject(TargetType.Job, ctx.jobNameValue));
        return null;
    }

    @Override
    public Void visitDropJob(DropJobContext ctx) {
        add(SplitQueryType.DROP_JOB, BehaviorAction.DROP, literalObject(TargetType.Job, ctx.jobNameValue));
        return null;
    }

    @Override
    public Void visitCancelJobTask(CancelJobTaskContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.TERMINATE, literalObject(TargetType.Job, ctx.jobNameValue));
        return null;
    }

    @Override
    public Void visitCreateRoutineLoad(CreateRoutineLoadContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.label);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.table != null) {
            targets.add(instanceObject(TargetType.Table, ctx.table));
        }
        BehaviorObject source = instanceObject(TargetType.Resource, ctx.type);
        targets.add(source);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.CREATE, job, targets);
        if (ctx.table != null) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, instanceObject(TargetType.Table, ctx.table), List.of(source));
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, job);
        return null;
    }

    @Override
    public Void visitAlterRoutineLoad(AlterRoutineLoadContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.name);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.ALTER, job);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, job);
        return null;
    }

    @Override
    public Void visitPauseRoutineLoad(PauseRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.STOP, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitResumeRoutineLoad(ResumeRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.START, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitStopRoutineLoad(StopRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.TERMINATE, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitLoad(LoadContext ctx) {
        for (DataDescContext data : ctx.dataDescs) {
            BehaviorObject target = instanceObject(TargetType.Table, data.targetTableName);
            List<BehaviorObject> sources = new ArrayList<>();
            if (data.sourceTableName != null) {
                sources.add(instanceObject(TargetType.Table, data.sourceTableName));
            }
            for (org.antlr.v4.runtime.Token path : data.filePaths) {
                sources.add(literalObject(TargetType.File, path));
            }
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, target, sources);
            for (BehaviorObject source : sources) {
                if (source.getObjectType() == TargetType.File) {
                    add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, source);
                }
            }
        }
        return null;
    }

    @Override
    public Void visitCopyInto(CopyIntoContext ctx) {
        BehaviorObject stage = ctx.stageAndPattern() == null ? null : stage(ctx.stageAndPattern());
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, object(TargetType.Table, ctx.multipartIdentifier()), stage == null ? List.of() : List.of(stage));
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitGrantTablePrivilege(GrantTablePrivilegeContext ctx) {
        add(SplitQueryType.GRANT, BehaviorAction.GRANT, namedObject(TargetType.Table, ctx.multipartIdentifierOrAsterisk()), List
            .of(grantee(ctx.userIdentify(), ctx.identifierOrText())));
        return null;
    }

    @Override
    public Void visitRevokeTablePrivilege(RevokeTablePrivilegeContext ctx) {
        add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, namedObject(TargetType.Table, ctx.multipartIdentifierOrAsterisk()), List
            .of(grantee(ctx.userIdentify(), ctx.identifierOrText())));
        return null;
    }

    @Override
    public Void visitGrantResourcePrivilege(GrantResourcePrivilegeContext ctx) {
        add(SplitQueryType.GRANT, BehaviorAction.GRANT, instanceObject(TargetType.Object, ctx.identifierOrTextOrAsterisk()), List
            .of(grantee(ctx.userIdentify(), ctx.identifierOrText())));
        return null;
    }

    @Override
    public Void visitRevokeResourcePrivilege(RevokeResourcePrivilegeContext ctx) {
        add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, instanceObject(TargetType.Object, ctx.identifierOrTextOrAsterisk()), List
            .of(grantee(ctx.userIdentify(), ctx.identifierOrText())));
        return null;
    }

    @Override
    public Void visitGrantRole(GrantRoleContext ctx) {
        BehaviorObject target = user(ctx.userIdentify());
        for (IdentifierOrTextContext role : ctx.roles) {
            add(SplitQueryType.GRANT, BehaviorAction.GRANT, instanceObject(TargetType.Role, role), List.of(target));
        }
        return null;
    }

    @Override
    public Void visitRevokeRole(RevokeRoleContext ctx) {
        BehaviorObject target = user(ctx.userIdentify());
        for (IdentifierOrTextContext role : ctx.roles) {
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, instanceObject(TargetType.Role, role), List.of(target));
        }
        return null;
    }

    @Override
    public Void visitExport(ExportContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.tableName);
        BehaviorObject file = literalObject(TargetType.File, ctx.filePath);
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, table, List.of(file));
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, file);
        addNestedReads(ctx);
        return null;
    }

    @Override
    public Void visitBackup(BackupContext ctx) {
        BehaviorObject backup = object(TargetType.Backup, ctx.label);
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(instanceObject(TargetType.Repository, ctx.repo));
        for (BaseTableRefContext table : ctx.baseTableRef()) {
            targets.add(object(TargetType.Table, table.multipartIdentifier()));
        }
        add(SplitQueryType.BACKUP, BehaviorAction.EXPORT, backup, targets);
        return null;
    }

    @Override
    public Void visitRestore(RestoreContext ctx) {
        BehaviorObject backup = object(TargetType.Backup, ctx.label);
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(instanceObject(TargetType.Repository, ctx.repo));
        for (BaseTableRefContext table : ctx.baseTableRef()) {
            targets.add(object(TargetType.Table, table.multipartIdentifier()));
        }
        add(SplitQueryType.RESTORE, BehaviorAction.RESTORE, backup, targets);
        return null;
    }

    @Override
    public Void visitInstallPlugin(InstallPluginContext ctx) {
        BehaviorObject plugin = instanceObject(TargetType.Plugin, ctx.source);
        add(SplitQueryType.CREATE_LIBRARY, BehaviorAction.CREATE, plugin, List.of(instanceObject(TargetType.File, ctx.source)));
        add(SplitQueryType.CREATE_LIBRARY, BehaviorAction.UNSAFE, plugin);
        return null;
    }

    @Override
    public Void visitUninstallPlugin(UninstallPluginContext ctx) {
        BehaviorObject plugin = instanceObject(TargetType.Plugin, ctx.name);
        add(SplitQueryType.DROP_LIBRARY, BehaviorAction.DROP, plugin);
        add(SplitQueryType.DROP_LIBRARY, BehaviorAction.UNSAFE, plugin);
        return null;
    }

    @Override
    public Void visitLockTables(LockTablesContext ctx) {
        for (LockTableContext table : ctx.lockTable()) {
            add(SplitQueryType.SESSION_LOCK, BehaviorAction.LOCK, object(TargetType.Table, table.name));
        }
        if (ctx.lockTable().isEmpty()) {
            add(SplitQueryType.SESSION_LOCK, BehaviorAction.LOCK, objects.unnamedObject(TargetType.Session, ctx, UmiTypes.Instance));
        }
        return null;
    }

    @Override
    public Void visitUnlockTables(UnlockTablesContext ctx) {
        add(SplitQueryType.SESSION_LOCK, BehaviorAction.UNLOCK, objects.unnamedObject(TargetType.Session, ctx, UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitAnalyzeDatabase(AnalyzeDatabaseContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ANALYZE, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterTableStats(AlterTableStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ALTER, object(TargetType.Statistics, ctx.name));
        return null;
    }

    @Override
    public Void visitDropStats(DropStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, object(TargetType.Statistics, ctx.tableName));
        return null;
    }

    @Override
    public Void visitDropCachedStats(DropCachedStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, object(TargetType.Statistics, ctx.tableName));
        return null;
    }

    @Override
    public Void visitRenameClause(RenameClauseContext ctx) {
        BehaviorObject source = renameSource(ctx);
        if (source != null) {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, List.of(object(TargetType.Table, ctx.newName)));
        } else {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, object(TargetType.Table, ctx.newName));
        }
        return null;
    }

    private BehaviorObject renameSource(RenameClauseContext ctx) {
        ParseTree parent = ctx.getParent();
        while (parent instanceof ParserRuleContext context) {
            if (context instanceof AlterTableContext alter) {
                return object(TargetType.Table, alter.tableName);
            }
            parent = context.getParent();
        }
        return null;
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addNestedReads(ParseTree tree) {
        for (FunctionCallExpressionContext function : descendants(tree, FunctionCallExpressionContext.class)) {
            add(behavior.getStatementType(), BehaviorAction.CALL, function(function.functionIdentifier()));
        }
        for (SystemVariableContext variable : descendants(tree, SystemVariableContext.class)) {
            add(behavior.getStatementType(), BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, variable, unquote(text(variable.identifier()))));
        }
        for (UserVariableContext variable : descendants(tree, UserVariableContext.class)) {
            add(behavior.getStatementType(), BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, variable, variableName(variable.identifierOrText())));
        }
    }

    private boolean addFirstNamed(String sql, int statementOffset, String phrase, TargetType targetType, BehaviorAction action, SplitQueryType statementType, boolean instance) {
        return addFirstNamed(sql, statementOffset, phrase, null, targetType, action, statementType, instance);
    }

    private boolean addFirstNamed(String sql, int statementOffset, String phrase, String optionalPhrase, TargetType targetType, BehaviorAction action, SplitQueryType statementType,
                                  boolean instance) {
        int offset = findPhraseEnd(sql, statementOffset, phrase);
        if (offset < 0) {
            return false;
        }
        if (optionalPhrase != null) {
            int optionalEnd = matchPhraseAt(sql, offset, optionalPhrase);
            if (optionalEnd >= 0 && optionalEnd < sql.length() && Character.isWhitespace(sql.charAt(optionalEnd))) {
                offset = skipWhitespace(sql, optionalEnd);
            }
        }
        int end = qualifiedIdentifierEnd(sql, offset);
        if (end < 0) {
            return false;
        }
        return addNamed(sql, offset, sql.substring(offset, end), targetType, action, statementType, instance);
    }

    private boolean addFirstLiteral(String sql, int statementOffset, String phrase, TargetType targetType, BehaviorAction action, SplitQueryType statementType) {
        int offset = findPhraseEnd(sql, statementOffset, phrase);
        if (offset < 0) {
            return false;
        }
        int end = quotedEnd(sql, offset, true);
        if (end < 0) {
            return false;
        }
        return addNamed(sql, offset, sql.substring(offset, end), targetType, action, statementType, true);
    }

    private boolean addIdentifierAt(String sql, int start, TargetType targetType, BehaviorAction action, SplitQueryType statementType, boolean instance) {
        int offset = skipWhitespace(sql, start);
        int end = identifierEnd(sql, offset);
        if (end < 0) {
            return false;
        }
        return addNamed(sql, offset, sql.substring(offset, end), targetType, action, statementType, instance);
    }

    private int findPhraseEnd(String sql, int start, String phrase) {
        for (int offset = Math.max(0, start); offset < sql.length(); offset++) {
            if (offset > 0 && isIdentifierPart(sql.charAt(offset - 1))) {
                continue;
            }
            int end = matchPhraseAt(sql, offset, phrase);
            if (end >= 0 && end < sql.length() && Character.isWhitespace(sql.charAt(end))) {
                return skipWhitespace(sql, end);
            }
        }
        return -1;
    }

    private int matchPhraseAt(String sql, int offset, String phrase) {
        int sqlIndex = offset;
        int phraseIndex = 0;
        while (phraseIndex < phrase.length()) {
            char expected = phrase.charAt(phraseIndex);
            if (Character.isWhitespace(expected)) {
                if (sqlIndex >= sql.length() || !Character.isWhitespace(sql.charAt(sqlIndex))) {
                    return -1;
                }
                while (phraseIndex < phrase.length() && Character.isWhitespace(phrase.charAt(phraseIndex))) {
                    phraseIndex++;
                }
                sqlIndex = skipWhitespace(sql, sqlIndex);
                continue;
            }
            if (sqlIndex >= sql.length() || Character.toUpperCase(sql.charAt(sqlIndex)) != Character.toUpperCase(expected)) {
                return -1;
            }
            phraseIndex++;
            sqlIndex++;
        }
        if (sqlIndex < sql.length() && isIdentifierPart(sql.charAt(sqlIndex))) {
            return -1;
        }
        return sqlIndex;
    }

    private int qualifiedIdentifierEnd(String sql, int offset) {
        int firstEnd = identifierEnd(sql, offset);
        if (firstEnd < 0) {
            return -1;
        }
        int dot = skipWhitespace(sql, firstEnd);
        if (dot >= sql.length() || sql.charAt(dot) != '.') {
            return firstEnd;
        }
        int secondStart = skipWhitespace(sql, dot + 1);
        int secondEnd = identifierEnd(sql, secondStart);
        return secondEnd < 0 ? firstEnd : secondEnd;
    }

    private int identifierEnd(String sql, int offset) {
        if (offset >= sql.length()) {
            return -1;
        }
        char first = sql.charAt(offset);
        if (first == '`' || first == '"' || first == '\'') {
            return quotedEnd(sql, offset, false);
        }
        if (!isIdentifierStart(first)) {
            return -1;
        }
        int end = offset + 1;
        while (end < sql.length() && isIdentifierPart(sql.charAt(end))) {
            end++;
        }
        return end;
    }

    private int quotedEnd(String sql, int offset, boolean backslashEscape) {
        if (offset >= sql.length()) {
            return -1;
        }
        char quote = sql.charAt(offset);
        if (quote != '`' && quote != '"' && quote != '\'') {
            return -1;
        }
        boolean hasContent = false;
        for (int index = offset + 1; index < sql.length(); index++) {
            char current = sql.charAt(index);
            if (backslashEscape && current == '\\' && index + 1 < sql.length()) {
                hasContent = true;
                index++;
                continue;
            }
            if (current != quote) {
                hasContent = true;
                continue;
            }
            if (index + 1 < sql.length() && sql.charAt(index + 1) == quote) {
                hasContent = true;
                index++;
                continue;
            }
            return backslashEscape || hasContent ? index + 1 : -1;
        }
        return -1;
    }

    private int skipWhitespace(String sql, int offset) {
        int result = Math.max(0, offset);
        while (result < sql.length() && Character.isWhitespace(sql.charAt(result))) {
            result++;
        }
        return result;
    }

    private boolean isIdentifierStart(char value) {
        return value >= 'A' && value <= 'Z' || value >= 'a' && value <= 'z' || value == '_' || value == '$';
    }

    private boolean isIdentifierPart(char value) {
        return isIdentifierStart(value) || value >= '0' && value <= '9';
    }

    private boolean startsWithKeyword(String value, String keyword) {
        if (!value.startsWith(keyword)) {
            return false;
        }
        return value.length() == keyword.length() || !isIdentifierPart(value.charAt(keyword.length()));
    }

    private String invertedIndexComponent(TargetType target) {
        if (target == TargetType.Analyzer) {
            return "ANALYZER";
        }
        if (target == TargetType.Tokenizer) {
            return "TOKENIZER";
        }
        if (target == TargetType.TokenFilter) {
            return "TOKEN_FILTER";
        }
        if (target == TargetType.CharFilter) {
            return "CHAR_FILTER";
        }
        return "NORMALIZER";
    }

    private boolean addNamed(String sql, int offset, String raw, TargetType targetType, BehaviorAction action, SplitQueryType statementType, boolean instance) {
        String value = unquoteLiteral(normalizeQualifiedName(raw));
        CommonToken token = positionedToken(sql, offset, raw);
        BehaviorObject subject = instance ? objects.instanceObject(targetType, token, value) : objects.object(targetType, token, token, qualifiedNameParts(value, false));
        add(statementType, action, subject);
        return subject != null;
    }

    private String normalizeQualifiedName(String value) {
        StringBuilder result = new StringBuilder(value.length());
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (!Character.isWhitespace(current)) {
                if (current == '.') {
                    while (!result.isEmpty() && Character.isWhitespace(result.charAt(result.length() - 1))) {
                        result.setLength(result.length() - 1);
                    }
                    result.append(current);
                    while (index + 1 < value.length() && Character.isWhitespace(value.charAt(index + 1))) {
                        index++;
                    }
                } else {
                    result.append(current);
                }
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private List<String> qualifiedNameParts(String value, boolean stripAndUnquote) {
        List<String> result = new ArrayList<>();
        int start = 0;
        for (int index = 0; index <= value.length(); index++) {
            if (index < value.length() && value.charAt(index) != '.') {
                continue;
            }
            String part = value.substring(start, index);
            if (!part.isBlank()) {
                result.add(stripAndUnquote ? unquote(part.strip()) : part);
            }
            start = index + 1;
        }
        return result;
    }

    private CommonToken positionedToken(String sql, int offset, String raw) {
        int line = fallbackStartLine;
        int column = fallbackStartColumn;
        for (int index = 0; index < offset; index++) {
            if (sql.charAt(index) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        CommonToken token = new CommonToken(0, raw);
        token.setLine(line);
        token.setCharPositionInLine(column);
        return token;
    }

    private String stripLeadingComments(String sql) {
        int offset = 0;
        while (offset < sql.length()) {
            while (offset < sql.length() && Character.isWhitespace(sql.charAt(offset)))
                offset++;
            if (sql.startsWith("--", offset)) {
                int newline = sql.indexOf('\n', offset + 2);
                offset = newline < 0 ? sql.length() : newline + 1;
                continue;
            }
            if (sql.startsWith("/*", offset)) {
                int end = sql.indexOf("*/", offset + 2);
                offset = end < 0 ? sql.length() : end + 2;
                continue;
            }
            break;
        }
        return sql.substring(offset);
    }

    private BehaviorObject function(FunctionIdentifierContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (context.dbName != null) {
            names.add(unquote(text(context.dbName)));
        }
        names.add(unquote(text(context.functionNameIdentifier())));
        return objects.object(TargetType.Function, context, names);
    }

    private String functionName(FunctionIdentifierContext context) {
        return context == null ? "" : unquote(text(context.functionNameIdentifier())).toLowerCase(Locale.ROOT);
    }

    private List<BehaviorObject> jobDependencies(ParseTree tree) {
        List<BehaviorObject> dependencies = tableSources(tree);
        for (InsertTableContext insert : descendants(tree, InsertTableContext.class)) {
            BehaviorObject target = object(TargetType.Table, insert.tableName);
            if (target != null) {
                dependencies.add(target);
            }
        }
        for (TableValuedFunctionContext function : descendants(tree, TableValuedFunctionContext.class)) {
            String name = unquote(text(function.tvfName)).toLowerCase(Locale.ROOT);
            if (EXTERNAL_TABLE_FUNCTIONS.contains(name)) {
                dependencies.add(externalTableFunctionSource(function));
            }
        }
        for (FunctionCallExpressionContext function : descendants(tree, FunctionCallExpressionContext.class)) {
            if (EXTERNAL_TABLE_FUNCTIONS.contains(functionName(function.functionIdentifier()))) {
                dependencies.add(externalFunctionSource(function));
            }
        }
        return dependencies;
    }

    private BehaviorObject externalTableFunctionSource(TableValuedFunctionContext context) {
        if (context.properties != null) {
            for (PropertyItemContext property : context.properties.propertyItem()) {
                String key = unquote(text(property.key)).toLowerCase(Locale.ROOT);
                if (key.equals("uri") || key.equals("path") || key.equals("file") || key.equals("filename")) {
                    return objects.instanceObject(TargetType.File, property.value, unquoteLiteral(text(property.value)));
                }
            }
        }
        return objects.unnamedObject(TargetType.File, context, UmiTypes.Instance);
    }

    private BehaviorObject externalFunctionSource(FunctionCallExpressionContext context) {
        int start = context.getStart().getTokenIndex();
        int stop = context.getStop().getTokenIndex();
        for (int index = start; index <= stop; index++) {
            org.antlr.v4.runtime.Token token = parser.getTokenStream().get(index);
            String key = unquoteLiteral(token.getText()).toLowerCase(Locale.ROOT);
            if (!(key.equals("uri") || key.equals("path") || key.equals("file") || key.equals("filename"))) {
                continue;
            }
            for (int valueIndex = index + 1; valueIndex <= stop; valueIndex++) {
                org.antlr.v4.runtime.Token candidate = parser.getTokenStream().get(valueIndex);
                String value = candidate.getText();
                if (value.length() >= 2 && (value.charAt(0) == '\'' || value.charAt(0) == '"')) {
                    return literalObject(TargetType.File, candidate);
                }
            }
        }
        return objects.unnamedObject(TargetType.File, context, UmiTypes.Instance);
    }

    private List<BehaviorObject> optionalTarget(TargetType type, ParserRuleContext context) {
        BehaviorObject target = context == null ? null : instanceObject(type, context);
        return target == null ? List.of() : List.of(target);
    }

    private BehaviorObject literalObject(TargetType type, org.antlr.v4.runtime.tree.TerminalNode node) {
        if (node == null) {
            return null;
        }
        return objects.instanceObject(type, node.getSymbol(), unquoteLiteral(node.getText()));
    }

    private BehaviorObject literalObject(TargetType type, org.antlr.v4.runtime.Token token) {
        if (token == null) {
            return null;
        }
        return objects.instanceObject(type, token, unquoteLiteral(token.getText()));
    }

    private BehaviorObject repositoryLocation(StorageBackendContext context) {
        if (context.STRING_LITERAL() != null) {
            return literalObject(TargetType.File, context.STRING_LITERAL());
        }
        return objects.unnamedObject(TargetType.File, context, UmiTypes.Instance);
    }

    private BehaviorObject stage(StageAndPatternContext context) {
        if (context.stage != null) {
            return instanceObject(TargetType.Stage, context.stage);
        }
        return objects.unnamedObject(TargetType.Stage, context, UmiTypes.Instance);
    }

    private BehaviorObject instanceObject(TargetType type, ParserRuleContext context) {
        return objects.instanceObject(type, context, unquote(text(context)));
    }

    private BehaviorObject namedObject(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        return objects.object(type, context, qualifiedNameParts(text(context), true));
    }

    private BehaviorObject user(ParserRuleContext context) {
        return context == null ? null : objects.instanceObject(TargetType.User, context, unquote(text(context)));
    }

    private BehaviorObject grantee(ParserRuleContext user, ParserRuleContext role) {
        return user != null ? this.user(user) : instanceObject(TargetType.Role, role);
    }

    private String variableName(IdentifierOrTextContext context) {
        return context == null ? "" : unquote(text(context));
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        for (TableNameContext table : descendants(tree, TableNameContext.class)) {
            BehaviorObject object = object(TargetType.Table, table.multipartIdentifier());
            if (object != null) {
                result.add(object);
            }
        }
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (context instanceof MultipartIdentifierContext multipart) {
            for (ErrorCapturingIdentifierContext identifier : multipart.errorCapturingIdentifier()) {
                names.add(unquote(text(identifier)));
            }
        } else {
            names.add(unquote(text(context)));
        }
        return objects.object(type, context, names);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String unquote(String value) {
        if (value.length() >= 2 && ((value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`') || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"'))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private String unquoteLiteral(String value) {
        if (value.length() >= 2 && ((value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'') || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"'))) {
            return value.substring(1, value.length() - 1);
        }
        return unquote(value);
    }

    private BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        return add(type, action, subject, List.of());
    }

    private BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return null;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        for (BehaviorObject target : targets) {
            if (target != null) {
                relation.getTarget().add(target);
            }
        }
        behavior.getRelations().add(relation);
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || type != SplitQueryType.SELECT) {
            behavior.setStatementType(type);
        }
        return relation;
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        List<T> values = descendants(tree, type);
        return values.isEmpty() ? null : values.get(0);
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
}

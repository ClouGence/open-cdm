/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

/** EXPLAIN separates syntax display, dependency analysis and actual query execution. */
final class ChExplainBehaviorVisitor extends ChAccessBehaviorVisitor {
    private final Deque<Set<String>> commonTables = new ArrayDeque<>();
    private final String instanceScope;
    private boolean planning;
    private boolean diagnostic;

    ChExplainBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        super(parser, levels, baseLine, baseColumn);
        instanceScope = objects.instanceObject(TargetType.Instance, parser.getTokenStream().get(0)).getObjectPath();
    }

    @Override
    public Void visitExplainStmt(ExplainStmtContext ctx) {
        diagnostic = true;
        if (ctx.CURRENT() != null) {
            BehaviorObject transaction = objects.instanceObject(TargetType.Transaction,
                ctx.CURRENT().getSymbol(), ctx.TRANSACTION().getSymbol(), "");
            transaction.setObjectPath(instanceScope);
            add(SplitQueryType.TRANSACTION, BehaviorAction.READ, transaction);
            return null;
        }
        setType(SplitQueryType.PERFORMANCE);
        boolean previous = planning;
        planning = ctx.ANALYZE() == null;
        if (!planning && behavior().getStatementType() == SplitQueryType.PERFORMANCE) {
            behavior().setStatementType(SplitQueryType.SELECT);
        }
        try {
            if (ctx.AST() != null) {
                ParseTree query = ctx.explainAstQuery();
                if (option(ctx, "optimize", 0) != 0) {
                    analyzeSelects(query);
                } else {
                    visitBindings(query);
                }
            } else if (ctx.SYNTAX() != null) {
                ExplainSyntaxQueryContext query = ctx.explainSyntaxQuery();
                if (query.selectUnionStmt() == null || !analyzerEnabled(ctx) || (option(ctx, "run_query_tree_passes", 0) != 0
                        && option(ctx, "query_tree_passes", -1) != 0)) {
                    // Non-SELECT SYNTAX uses the legacy SELECT analyzer, never a DDL/DML executor.
                    analyzeSelects(query);
                } else {
                    visitBindings(query);
                }
            } else if (ctx.TREE() != null && (option(ctx, "run_passes", 1) == 0 || option(ctx, "passes", -1) == 0)) {
                visitBindings(ctx.selectUnionStmt());
            } else if (ctx.OVERRIDE() != null) {
                // Only the source table function executes; override expressions are inspected as ASTs.
                visit(ctx.tableFunctionExpr());
                visitBindings(ctx.explainTableOverride());
            } else if (ctx.insertStmt() != null) {
                InsertStmtContext insert = ctx.insertStmt();
                if (insert.tableIdentifier() != null) {
                    add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, insert.tableIdentifier()));
                }
                analyzeSelects(insert);
            } else {
                if (ctx.WHATIF() != null) {
                    // The estimator reads the candidate store for each physical source, not all indexes.
                    int first = behavior().getRelations().size();
                    visit(ctx.selectUnionStmt());
                    List<BehaviorRelation> sources = new ArrayList<>(behavior().getRelations().subList(first, behavior().getRelations().size()));
                    for (BehaviorRelation source : sources) {
                        if (source.getSubject().getObjectType() == TargetType.Table && source.getAction() == BehaviorAction.READ) {
                            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, hypotheticalIndex(
                                ctx.WHATIF().getSymbol(), ctx.WHATIF().getSymbol(), source.getSubject(), null));
                        }
                    }
                } else {
                    visit(ctx.selectUnionStmt());
                }
            }
        } finally {
            planning = previous;
        }
        return null;
    }

    private boolean analyzerEnabled(ExplainStmtContext ctx) {
        List<SettingsClauseContext> clauses = new ArrayList<>();
        if (ctx.getParent().getParent() instanceof QueryStmtQueryContext wrapper && wrapper.settingsClause() != null) {
            clauses.add(wrapper.settingsClause());
        }
        SelectUnionStmtContext query = ctx.explainSyntaxQuery().selectUnionStmt();
        List<SelectStmtWithParensContext> selects = query.selectStmtWithParens();
        SelectStmtContext last = selects.get(selects.size() - 1).selectStmt();
        if (last != null && last.settingsClause() != null) {
            clauses.add(last.settingsClause());
        }
        boolean enabled = true;
        for (SettingsClauseContext clause : clauses) {
            for (SettingExprContext setting : clause.settingExprList().settingExpr()) {
                String key = name(setting.identifier());
                if ((key.equals("enable_analyzer") || key.equals("allow_experimental_analyzer")) && setting.literal() != null) {
                    String value = setting.literal().getText();
                    enabled = !value.equals("0") && !value.equalsIgnoreCase("false");
                }
            }
        }
        return enabled;
    }

    private long option(ExplainStmtContext ctx, String option, long fallback) {
        if (ctx.explainSettings() != null) {
            for (ExplainSettingContext setting : ctx.explainSettings().explainSetting()) {
                if (name(setting.identifier()).equals(option)) {
                    try {
                        return Long.parseLong(setting.literal().getText());
                    } catch (NumberFormatException ignored) {
                        // Invalid option values are the server's responsibility, not executed expressions.
                        return fallback;
                    }
                }
            }
        }
        return fallback;
    }

    private void analyzeSelects(ParseTree tree) {
        if (tree instanceof SelectUnionStmtContext query) {
            visit(query);
            return;
        }
        if (tree instanceof QueryParameterContext parameter) {
            visit(parameter);
            return;
        }
        if (tree instanceof SettingsClauseContext settings) {
            // Settings on a query wrapper apply; stored CREATE engine settings do not.
            if (settings.getParent() instanceof QueryStmtQueryContext) {
                visit(settings);
            }
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            analyzeSelects(tree.getChild(i));
        }
    }

    private void visitBindings(ParseTree tree) {
        // Parameter replacement precedes interpretation, including syntax-only EXPLAIN.
        for (QueryParameterContext parameter : descendants(tree, QueryParameterContext.class)) {
            visit(parameter);
        }
        applyQuerySettings(tree);
    }

    private void applyQuerySettings(ParseTree tree) {
        if (tree instanceof SelectUnionStmtContext query) {
            List<SelectStmtWithParensContext> selects = query.selectStmtWithParens();
            SelectStmtContext last = selects.get(selects.size() - 1).selectStmt();
            // Native applySettingsFromSelectWithUnion uses only the last direct SELECT.
            if (last != null && last.settingsClause() != null) {
                visit(last.settingsClause());
            }
            return;
        }
        if (tree instanceof InsertStmtContext || tree instanceof EngineClauseContext) {
            return;
        }
        if (tree instanceof SettingsClauseContext settings) {
            if (settings.getParent() instanceof QueryStmtQueryContext) {
                visit(settings);
            }
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            applyQuerySettings(tree.getChild(i));
        }
    }

    @Override
    public Void visitSelectUnionStmt(SelectUnionStmtContext ctx) {
        Set<String> scope = new HashSet<>();
        if (!commonTables.isEmpty()) {
            scope.addAll(commonTables.peek());
        }
        commonTables.push(scope);
        try {
            return super.visitSelectUnionStmt(ctx);
        } finally {
            commonTables.pop();
        }
    }

    @Override
    public Void visitWithExprSubquery(WithExprSubqueryContext ctx) {
        String alias = name(ctx.identifier());
        if (ctx.RECURSIVE() != null) {
            commonTables.peek().add(alias);
        }
        visit(ctx.selectUnionStmt());
        commonTables.peek().add(alias);
        return null;
    }

    @Override
    public Void visitTableExprIdentifier(TableExprIdentifierContext ctx) {
        TableIdentifierContext table = ctx.tableIdentifier();
        if (table.databaseIdentifier() == null && !commonTables.isEmpty() && commonTables.peek().contains(name(table.identifier()))) {
            return null;
        }
        return super.visitTableExprIdentifier(ctx);
    }

    @Override
    public Void visitTableFunctionExpr(TableFunctionExprContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.CALL,
            objects.object(TargetType.Function, ctx.identifier(), List.of(name(ctx.identifier()))));
        return visitChildren(ctx);
    }

    @Override
    protected BehaviorAction functionAction(ParserRuleContext ctx) {
        if (!planning) {
            return BehaviorAction.CALL;
        }
        if (ctx instanceof ColumnExprFunctionContext function && ChPlanningFunctions.readsSetting(name(function.identifier()))) {
            return BehaviorAction.CALL;
        }
        return BehaviorAction.READ;
    }

    @Override
    public Void visitColumnExprFunction(ColumnExprFunctionContext ctx) {
        boolean previous = planning;
        // A folded higher-order call also executes its lambda's nested function calls.
        if (planning && ChPlanningFunctions.foldsLambda(name(ctx.identifier())) && constantArguments(ctx, Set.of())) {
            planning = false;
        }
        try {
            return super.visitColumnExprFunction(ctx);
        } finally {
            planning = previous;
        }
    }

    private boolean constantArguments(ParseTree tree, Set<String> bound) {
        if (tree instanceof ColumnExprIdentifierContext column) {
            return bound.contains(name(column.columnIdentifier()));
        }
        if (tree instanceof ColumnExprSubqueryContext || tree instanceof QueryParameterContext) {
            return false;
        }
        if (tree instanceof ColumnLambdaExprContext lambda) {
            Set<String> parameters = new HashSet<>(bound);
            lambda.identifier().forEach(id -> parameters.add(name(id)));
            return constantArguments(lambda.columnExpr(), parameters);
        }
        if (tree instanceof ColumnArgExprContext arg && arg.columnExpr() instanceof ColumnExprFunctionContext) {
            // No metadata lookup is available for arbitrary nested functions or UDFs.
            return false;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (!constantArguments(tree.getChild(i), bound)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Void visitHypotheticalIndexStmt(HypotheticalIndexStmtContext ctx) {
        diagnostic = true;
        setType(SplitQueryType.ADMIN_PERFORMANCE);
        if (ctx.ALL() != null) {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE,
                hypotheticalIndex(ctx.ALL().getSymbol(), ctx.INDEXES().getSymbol(), null, null));
            return null;
        }
        BehaviorObject table = object(TargetType.Table, ctx.tableIdentifier());
        BehaviorObject index = hypotheticalIndex(ctx.identifier().getStart(), ctx.identifier().getStop(), table, name(ctx.identifier()));
        if (ctx.DROP() != null) {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, index);
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.READ, table);
            return null;
        }
        boolean previous = planning;
        planning = true;
        int first = behavior().getRelations().size();
        try {
            for (ColumnExprContext expression : ctx.hypotheticalIndexDeclaration().columnExpr()) {
                visit(expression);
            }
        } finally {
            planning = previous;
        }
        List<BehaviorObject> dependencies = new ArrayList<>();
        dependencies.add(table);
        for (BehaviorRelation relation : behavior().getRelations().subList(first, behavior().getRelations().size())) {
            BehaviorObject source = relation.getSubject();
            if (dependencies.stream().noneMatch(existing -> existing.getObjectType() == source.getObjectType()
                    && existing.getObjectPath().equals(source.getObjectPath()))) {
                dependencies.add(source);
            }
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CREATE, index, dependencies);
        return null;
    }

    private BehaviorObject hypotheticalIndex(Token start, Token stop, BehaviorObject table, String name) {
        String relative = "hypothetical_index";
        if (table != null) {
            relative += "/" + instanceRelativeName(table);
        }
        if (name != null) {
            relative += "/" + name;
        }
        BehaviorObject index = objects.instanceObject(TargetType.Index, start, stop, relative);
        index.setObjectName(new ObjectName(null, null, name));
        return index;
    }

    void finish() {
        if (!diagnostic) {
            return;
        }
        List<BehaviorRelation> relations = behavior().getRelations();
        relations.sort(Comparator.comparingInt((BehaviorRelation r) -> r.getSubject().getStartLine())
            .thenComparingInt(r -> r.getSubject().getStartColumn()));
        Set<List<Object>> seen = new HashSet<>();
        relations.removeIf(relation -> {
            List<Object> key = new ArrayList<>();
            key.add(relation.getAction());
            key.add(relation.getSubject().getObjectType());
            key.add(relation.getSubject().getObjectPath());
            for (BehaviorObject target : relation.getTarget()) {
                key.add(target.getObjectType());
                key.add(target.getObjectPath());
            }
            return !seen.add(key);
        });
    }
}

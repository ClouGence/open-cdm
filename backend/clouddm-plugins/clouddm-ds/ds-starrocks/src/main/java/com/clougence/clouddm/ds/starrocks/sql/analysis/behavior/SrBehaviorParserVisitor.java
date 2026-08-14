/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.starrocks.sql.analysis.behavior;

import static com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class SrBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final SplitQueryType          statementType;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    SrBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn, SplitQueryType statementType){
        this.parser = parser;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
        this.statementType = statementType;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        SrStatementBehaviorVisitor visitor = new SrStatementBehaviorVisitor(parser, levels, baseLine, baseColumn, statementType);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class SrStatementBehaviorVisitor extends StarRocksBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();
    private final SplitQueryType           statementType;

    SrStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn, SplitQueryType statementType){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.statementType = statementType;
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitQueryStatement(QueryStatementContext ctx) {
        if (statementType == SplitQueryType.UNSAFE || isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitTableAtom(TableAtomContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateDbStatement(CreateDbStatementContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitDropDbStatement(DropDbStatementContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        add(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        add(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        add(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitCreateTableStatement(CreateTableStatementContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        if (!ctx.qualifiedName().isEmpty()) {
            List<BehaviorObject> sources = ctx.qualifiedName().size() > 1 ? List.of(object(TargetType.Table, ctx.qualifiedName(1))) : List.of();
            add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName(0)), sources);
        }
        return null;
    }

    @Override
    public Void visitDropTableStatement(DropTableStatementContext ctx) {
        add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitAlterTableStatement(AlterTableStatementContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateViewStatement(CreateViewStatementContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitAlterViewStatement(AlterViewStatementContext ctx) {
        add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitDropViewStatement(DropViewStatementContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        if (statementType == SplitQueryType.UNSAFE || isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        if (statementType == SplitQueryType.SELECT || ctx.explainDesc() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
            return null;
        }
        SplitQueryType type = ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        BehaviorRelation relation = add(type, type == SplitQueryType.INSERT ? BehaviorAction.INSERT : BehaviorAction.MERGE, object(TargetType.Table, ctx
            .qualifiedName()), tableSources(ctx.queryStatement()));
        if (relation != null && !ctx.expressionsWithDefault().isEmpty()) {
            relation.setInsertRows((long) ctx.expressionsWithDefault().size());
        }
        return null;
    }

    @Override
    public Void visitUpdateStatement(UpdateStatementContext ctx) {
        if (statementType == SplitQueryType.UNSAFE || isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.expression());
        if (statementType == SplitQueryType.SELECT || ctx.explainDesc() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()), sources);
            return null;
        }
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.qualifiedName()), sources);
        return null;
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext ctx) {
        if (statementType == SplitQueryType.UNSAFE || isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        List<BehaviorObject> sources = tableSources(ctx.relations());
        addTableSources(sources, ctx.expression());
        if (statementType == SplitQueryType.SELECT || ctx.explainDesc() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()), sources);
            return null;
        }
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.qualifiedName()), sources);
        return null;
    }

    private boolean isExplainAnalyze(ExplainDescContext ctx) {
        return ctx != null && ctx.ANALYZE() != null;
    }

    private void addUnsafe(ParserRuleContext ctx) {
        add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, objects.instanceObject(TargetType.Instance, ctx.getStart()));
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        for (TableAtomContext source : descendants(tree, TableAtomContext.class)) {
            BehaviorObject object = object(TargetType.Table, source.qualifiedName());
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
        if (context instanceof QualifiedNameContext qualified) {
            for (IdentifierContext identifier : qualified.identifier()) {
                names.add(name(identifier));
            }
        } else {
            names.add(name(context));
        }
        return objects.object(type, context, names);
    }

    private String name(ParserRuleContext context) {
        String value = parser.getTokenStream().getText(context.getStart(), context.getStop()).trim();
        if (value.length() >= 2 && ((value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`') || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"')
                                    || (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\''))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
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

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
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class SrBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    SrBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        SrStatementBehaviorVisitor visitor = new SrStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class SrStatementBehaviorVisitor extends StarRocksBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    SrStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SecQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitTableAtom(TableAtomContext ctx) {
        add(SecQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateDbStatement(CreateDbStatementContext ctx) {
        add(SecQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitDropDbStatement(DropDbStatementContext ctx) {
        add(SecQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        add(SecQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        add(SecQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        add(SecQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        add(SecQueryType.ALTER_CATALOG, BehaviorAction.ALTER, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitCreateTableStatement(CreateTableStatementContext ctx) {
        add(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        add(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        if (!ctx.qualifiedName().isEmpty()) {
            List<BehaviorObject> sources = ctx.qualifiedName().size() > 1 ? List.of(object(TargetType.Table, ctx.qualifiedName(1))) : List.of();
            add(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName(0)), sources);
        }
        return null;
    }

    @Override
    public Void visitDropTableStatement(DropTableStatementContext ctx) {
        add(SecQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitAlterTableStatement(AlterTableStatementContext ctx) {
        add(SecQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        add(SecQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateViewStatement(CreateViewStatementContext ctx) {
        add(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitAlterViewStatement(AlterViewStatementContext ctx) {
        add(SecQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitDropViewStatement(DropViewStatementContext ctx) {
        add(SecQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        add(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        add(SecQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        SecQueryType type = ctx.OVERWRITE() == null ? SecQueryType.INSERT : SecQueryType.MERGE;
        add(type, type == SecQueryType.INSERT ? BehaviorAction.INSERT : BehaviorAction.MERGE, object(TargetType.Table, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitUpdateStatement(UpdateStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.expression());
        add(SecQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.qualifiedName()), sources);
        return null;
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.relations());
        addTableSources(sources, ctx.expression());
        add(SecQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.qualifiedName()), sources);
        return null;
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

    private void add(SecQueryType type, BehaviorAction action, BehaviorObject subject) {
        add(type, action, subject, List.of());
    }

    private void add(SecQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
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
        if (behavior.getStatementType() == SecQueryType.UNKNOWN || type != SecQueryType.SELECT) {
            behavior.setStatementType(type);
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
}

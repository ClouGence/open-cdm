/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.iso.sql92.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92ParserBaseVisitor;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser.*;

final class Sql92BehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    Sql92BehaviorParserVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        Sql92StatementBehaviorVisitor visitor = new Sql92StatementBehaviorVisitor(levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class Sql92StatementBehaviorVisitor extends Sql92ParserBaseVisitor<Void> {

    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    Sql92StatementBehaviorVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitCrossJoin(CrossJoinContext ctx) {
        addUnary(SplitQueryType.SELECT, BehaviorAction.READ, table(ctx.tableName()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitExplicitTable(ExplicitTableContext ctx) {
        addUnary(SplitQueryType.SELECT, BehaviorAction.READ, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitSchemaDefinition(SchemaDefinitionContext ctx) {
        addUnary(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.schemaName()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropSchemaStatement(DropSchemaStatementContext ctx) {
        addUnary(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.schemaName()));
        return null;
    }

    @Override
    public Void visitTableDefinition(TableDefinitionContext ctx) {
        addUnary(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitAlterTableStatement(AlterTableStatementContext ctx) {
        addUnary(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitDropTableStatement(DropTableStatementContext ctx) {
        addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitViewDefinition(ViewDefinitionContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableName()), sources(ctx.non_joinQueryExpression()));
        return null;
    }

    @Override
    public Void visitDropViewStatement(DropViewStatementContext ctx) {
        addUnary(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.tableName()));
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        addRelation(SplitQueryType.INSERT, BehaviorAction.INSERT, table(ctx.tableName()), sources(ctx.insertColumnsAndSource()));
        return null;
    }

    @Override
    public Void visitUpdateStatement_Positioned(UpdateStatement_PositionedContext ctx) {
        addUnary(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitUpdateStatement_Searched(UpdateStatement_SearchedContext ctx) {
        addRelation(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(ctx.tableName()), sources(ctx.searchCondition()));
        return null;
    }

    @Override
    public Void visitDeleteStatement_Positioned(DeleteStatement_PositionedContext ctx) {
        addUnary(SplitQueryType.DELETE, BehaviorAction.DELETE, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitDeleteStatement_Searched(DeleteStatement_SearchedContext ctx) {
        addRelation(SplitQueryType.DELETE, BehaviorAction.DELETE, table(ctx.tableName()), sources(ctx.searchCondition()));
        return null;
    }

    private BehaviorObject table(ParserRuleContext context) {
        return object(TargetType.Table, context);
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        return context == null ? null : objects.object(type, context, Sql92Parser.IDENTIFIER);
    }

    private List<BehaviorObject> sources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (CrossJoinContext source : descendants(tree, CrossJoinContext.class)) {
            add(result, table(source.tableName()));
        }
        for (ExplicitTableContext source : descendants(tree, ExplicitTableContext.class)) {
            add(result, table(source.tableName()));
        }
        return result;
    }

    private void add(List<BehaviorObject> target, BehaviorObject object) {
        if (object != null) {
            target.add(object);
        }
    }

    private void addUnary(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        addRelation(type, action, subject, List.of());
    }

    private void addRelation(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
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

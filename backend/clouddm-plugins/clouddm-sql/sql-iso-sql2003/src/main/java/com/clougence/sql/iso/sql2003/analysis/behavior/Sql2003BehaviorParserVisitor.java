/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.iso.sql2003.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003ParserBaseVisitor;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser.*;

final class Sql2003BehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    Sql2003BehaviorParserVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        Sql2003StatementBehaviorVisitor visitor = new Sql2003StatementBehaviorVisitor(levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class Sql2003StatementBehaviorVisitor extends Sql2003ParserBaseVisitor<Void> {

    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    Sql2003StatementBehaviorVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SecQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitCrossJoin(CrossJoinContext ctx) {
        if (ctx.tableOrQueryName() != null) {
            addUnary(SecQueryType.SELECT, BehaviorAction.READ, table(ctx.tableOrQueryName().tableName()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitExplicitTable(ExplicitTableContext ctx) {
        if (ctx.tableOrQueryName() != null) {
            addUnary(SecQueryType.SELECT, BehaviorAction.READ, table(ctx.tableOrQueryName().tableName()));
        }
        return null;
    }

    @Override
    public Void visitSchemaDefinition(SchemaDefinitionContext ctx) {
        addUnary(SecQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.schemaName()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropSchemaStatement(DropSchemaStatementContext ctx) {
        addUnary(SecQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.schemaName()));
        return null;
    }

    @Override
    public Void visitTableDefinition(TableDefinitionContext ctx) {
        ParseTree source = ctx.tableContentsSource() == null ? null : ctx.tableContentsSource().asSubqueryClause();
        addRelation(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(ctx.tableName()), sources(source));
        return null;
    }

    @Override
    public Void visitAlterTableStatement(AlterTableStatementContext ctx) {
        addUnary(SecQueryType.ALTER_TABLE, BehaviorAction.ALTER, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitDropTableStatement(DropTableStatementContext ctx) {
        addUnary(SecQueryType.DROP_TABLE, BehaviorAction.DROP, table(ctx.tableName()));
        return null;
    }

    @Override
    public Void visitViewDefinition(ViewDefinitionContext ctx) {
        addRelation(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableName()), sources(ctx.queryExpression()));
        return null;
    }

    @Override
    public Void visitDropViewStatement(DropViewStatementContext ctx) {
        addUnary(SecQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.tableName()));
        return null;
    }

    @Override
    public Void visitDropRoutineStatement(DropRoutineStatementContext ctx) {
        addUnary(SecQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Procedure, ctx.specificRoutineDesignator()));
        return null;
    }

    @Override
    public Void visitDropTriggerStatement(DropTriggerStatementContext ctx) {
        addUnary(SecQueryType.DROP_TRIGGER, BehaviorAction.DROP, object(TargetType.Trigger, ctx.triggerName()));
        return null;
    }

    @Override
    public Void visitAlterSequenceGeneratorStatement(AlterSequenceGeneratorStatementContext ctx) {
        addUnary(SecQueryType.ALTER_SEQUENCE, BehaviorAction.ALTER, object(TargetType.Sequence, ctx.sequenceGeneratorName()));
        return null;
    }

    @Override
    public Void visitDropSequenceGeneratorStatement(DropSequenceGeneratorStatementContext ctx) {
        addUnary(SecQueryType.DROP_SEQUENCE, BehaviorAction.DROP, object(TargetType.Sequence, ctx.sequenceGeneratorName()));
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        BehaviorObject subject = ctx.insertionTarget() == null ? null : table(ctx.insertionTarget().tableName());
        addRelation(SecQueryType.INSERT, BehaviorAction.INSERT, subject, sources(ctx.insertColumnsAndSource()));
        return null;
    }

    @Override
    public Void visitUpdateStatement_Positioned(UpdateStatement_PositionedContext ctx) {
        addUnary(SecQueryType.UPDATE, BehaviorAction.UPDATE, table(ctx.targetTable().tableName()));
        return null;
    }

    @Override
    public Void visitUpdateStatement_Searched(UpdateStatement_SearchedContext ctx) {
        addRelation(SecQueryType.UPDATE, BehaviorAction.UPDATE, table(ctx.targetTable().tableName()), sources(ctx.searchCondition()));
        return null;
    }

    @Override
    public Void visitDeleteStatement_Positioned(DeleteStatement_PositionedContext ctx) {
        addUnary(SecQueryType.DELETE, BehaviorAction.DELETE, table(ctx.targetTable().tableName()));
        return null;
    }

    @Override
    public Void visitDeleteStatement_Searched(DeleteStatement_SearchedContext ctx) {
        addRelation(SecQueryType.DELETE, BehaviorAction.DELETE, table(ctx.targetTable().tableName()), sources(ctx.searchCondition()));
        return null;
    }

    @Override
    public Void visitMergeStatement(MergeStatementContext ctx) {
        addRelation(SecQueryType.MERGE, BehaviorAction.MERGE, table(ctx.targetTable().tableName()), sources(ctx));
        return null;
    }

    @Override
    public Void visitCallStatement(CallStatementContext ctx) {
        if (ctx.routineInvocation() != null && ctx.routineInvocation().IDENTIFIER() != null) {
            addUnary(SecQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, objects
                .object(TargetType.Procedure, ctx.routineInvocation(), List.of(unquote(ctx.routineInvocation().IDENTIFIER().getText()))));
        }
        return null;
    }

    private BehaviorObject table(ParserRuleContext context) {
        return object(TargetType.Table, context);
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        return context == null ? null : objects.object(type, context, Sql2003Parser.IDENTIFIER);
    }

    private List<BehaviorObject> sources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (CrossJoinContext source : descendants(tree, CrossJoinContext.class)) {
            if (source.tableOrQueryName() != null) {
                add(result, table(source.tableOrQueryName().tableName()));
            }
        }
        for (ExplicitTableContext source : descendants(tree, ExplicitTableContext.class)) {
            if (source.tableOrQueryName() != null) {
                add(result, table(source.tableOrQueryName().tableName()));
            }
        }
        return result;
    }

    private void add(List<BehaviorObject> target, BehaviorObject object) {
        if (object != null) {
            target.add(object);
        }
    }

    private void addUnary(SecQueryType type, BehaviorAction action, BehaviorObject subject) {
        addRelation(type, action, subject, List.of());
    }

    private void addRelation(SecQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        if (behavior.getStatementType() == SecQueryType.UNKNOWN) {
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

    private String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}

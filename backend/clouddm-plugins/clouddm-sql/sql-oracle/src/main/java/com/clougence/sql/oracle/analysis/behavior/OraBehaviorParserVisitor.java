/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.oracle.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.Parser;
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
import com.clougence.sql.oracle.parser.antlr.PlSqlParserBaseVisitor;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser.*;

final class OraBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    OraBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        OraStatementBehaviorVisitor visitor = new OraStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class OraStatementBehaviorVisitor extends PlSqlParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    OraStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SecQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitDml_table_expression_clause(Dml_table_expression_clauseContext ctx) {
        if (ctx.tableview_name() != null) {
            addUnary(SecQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableview_name()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreate_table(Create_tableContext ctx) {
        if (!ctx.tableview_name().isEmpty()) {
            addRelation(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.tableview_name(0)), sourceTables(ctx, Set.of()));
        }
        return null;
    }

    @Override
    public Void visitCreate_view(Create_viewContext ctx) {
        addRelation(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableview_name()), sourceTables(ctx.select_only_statement(), Set.of()));
        return null;
    }

    @Override
    public Void visitCreate_materialized_view(Create_materialized_viewContext ctx) {
        addRelation(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.tableview_name()), sourceTables(ctx, Set.of()));
        return null;
    }

    @Override
    public Void visitCreate_index(Create_indexContext ctx) {
        addRelation(SecQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.index_name()), objects(object(TargetType.Table, ctx.tableview_name())));
        return null;
    }

    @Override
    public Void visitCreate_procedure_body(Create_procedure_bodyContext ctx) {
        addUnary(SecQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx.procedure_name()));
        return null;
    }

    @Override
    public Void visitCreate_function_body(Create_function_bodyContext ctx) {
        addUnary(SecQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.function_name()));
        return null;
    }

    @Override
    public Void visitCreate_trigger(Create_triggerContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Tableview_nameContext table : descendants(ctx, Tableview_nameContext.class)) {
            addObject(targets, object(TargetType.Table, table));
        }
        addRelation(SecQueryType.CREATE_TRIGGER, BehaviorAction.CREATE, object(TargetType.Trigger, ctx.trigger_name()), targets);
        return null;
    }

    @Override
    public Void visitCreate_sequence(Create_sequenceContext ctx) {
        addUnary(SecQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.sequence_name()));
        return null;
    }

    @Override
    public Void visitCreate_synonym(Create_synonymContext ctx) {
        addUnary(SecQueryType.CREATE_SYNONYM, BehaviorAction.CREATE, object(TargetType.Synonym, ctx.synonym_name()));
        return null;
    }

    @Override
    public Void visitDrop_table(Drop_tableContext ctx) {
        addUnary(SecQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.tableview_name()));
        return null;
    }

    @Override
    public Void visitDrop_function(Drop_functionContext ctx) {
        addUnary(SecQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Function, ctx.function_name()));
        return null;
    }

    @Override
    public Void visitDrop_procedure(Drop_procedureContext ctx) {
        addUnary(SecQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Procedure, ctx.procedure_name()));
        return null;
    }

    @Override
    public Void visitDrop_sequence(Drop_sequenceContext ctx) {
        addUnary(SecQueryType.DROP_SEQUENCE, BehaviorAction.DROP, object(TargetType.Sequence, ctx.sequence_name()));
        return null;
    }

    @Override
    public Void visitDrop_trigger(Drop_triggerContext ctx) {
        addUnary(SecQueryType.DROP_TRIGGER, BehaviorAction.DROP, object(TargetType.Trigger, ctx.trigger_name()));
        return null;
    }

    @Override
    public Void visitAlter_table(Alter_tableContext ctx) {
        addUnary(SecQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableview_name()));
        return null;
    }

    @Override
    public Void visitRename_object(Rename_objectContext ctx) {
        List<Object_nameContext> names = ctx.object_name();
        if (names.size() >= 2) {
            BehaviorObject source = object(TargetType.Table, names.get(0));
            BehaviorObject target = object(TargetType.Table, names.get(1));
            moveToSameContainer(source, target);
            addRelation(SecQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, objects(target));
        }
        return null;
    }

    @Override
    public Void visitTruncate_table(Truncate_tableContext ctx) {
        addUnary(SecQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableview_name()));
        return null;
    }

    @Override
    public Void visitCall_statement(Call_statementContext ctx) {
        if (!ctx.routine_name().isEmpty()) {
            addUnary(SecQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.routine_name(0)));
        }
        return null;
    }

    @Override
    public Void visitInsert_statement(Insert_statementContext ctx) {
        List<Insert_into_clauseContext> inserts = descendants(ctx, Insert_into_clauseContext.class);
        Set<Dml_table_expression_clauseContext> insertTargets = new LinkedHashSet<>();
        for (Insert_into_clauseContext insert : inserts) {
            Dml_table_expression_clauseContext target = first(insert.general_table_ref(), Dml_table_expression_clauseContext.class);
            if (target != null) {
                insertTargets.add(target);
            }
        }
        List<BehaviorObject> sources = sourceTables(ctx, insertTargets);
        for (Dml_table_expression_clauseContext target : insertTargets) {
            addRelation(SecQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, target.tableview_name()), sources);
        }
        return null;
    }

    @Override
    public Void visitUpdate_statement(Update_statementContext ctx) {
        Dml_table_expression_clauseContext target = first(ctx.general_table_ref(), Dml_table_expression_clauseContext.class);
        addRelation(SecQueryType.UPDATE, BehaviorAction.UPDATE, target == null ? null : object(TargetType.Table, target.tableview_name()), sourceTables(ctx, target == null ? Set
            .of() : Set.of(target)));
        return null;
    }

    @Override
    public Void visitDelete_statement(Delete_statementContext ctx) {
        Dml_table_expression_clauseContext target = first(ctx.general_table_ref(), Dml_table_expression_clauseContext.class);
        addRelation(SecQueryType.DELETE, BehaviorAction.DELETE, target == null ? null : object(TargetType.Table, target.tableview_name()), sourceTables(ctx, target == null ? Set
            .of() : Set.of(target)));
        return null;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        collectNames(context, names);
        return objects.object(type, context, names);
    }

    private void collectNames(ParseTree tree, List<String> names) {
        if (tree instanceof IdentifierContext || tree instanceof Id_expressionContext) {
            ParserRuleContext context = (ParserRuleContext) tree;
            names.add(unquote(parser.getTokenStream().getText(context.getStart(), context.getStop())));
            return;
        }
        if (tree instanceof Link_nameContext) {
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), names);
        }
    }

    private void addUnary(SecQueryType type, BehaviorAction action, BehaviorObject subject) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
    }

    private void addRelation(SecQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
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
        behavior.setStatementType(type);
    }

    private List<BehaviorObject> sourceTables(ParseTree tree, Set<Dml_table_expression_clauseContext> excluded) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Dml_table_expression_clauseContext source : descendants(tree, Dml_table_expression_clauseContext.class)) {
            if (!excluded.contains(source) && source.tableview_name() != null) {
                addObject(result, object(TargetType.Table, source.tableview_name()));
            }
        }
        return result;
    }

    private List<BehaviorObject> objects(BehaviorObject... values) {
        List<BehaviorObject> result = new ArrayList<>();
        for (BehaviorObject value : values) {
            addObject(result, value);
        }
        return result;
    }

    private void addObject(List<BehaviorObject> target, BehaviorObject value) {
        if (value != null) {
            target.add(value);
        }
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        List<T> result = descendants(tree, type);
        return result.isEmpty() ? null : result.get(0);
    }

    private void moveToSameContainer(BehaviorObject source, BehaviorObject target) {
        if (source == null || target == null) {
            return;
        }
        String sourcePath = source.getResourcePath();
        String targetPath = target.getResourcePath();
        int sourceNameStart = sourcePath.lastIndexOf('/', sourcePath.length() - 2);
        int targetNameStart = targetPath.lastIndexOf('/', targetPath.length() - 2);
        if (sourceNameStart >= 0 && targetNameStart >= 0) {
            target.setResourcePath(sourcePath.substring(0, sourceNameStart + 1) + targetPath.substring(targetNameStart + 1));
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

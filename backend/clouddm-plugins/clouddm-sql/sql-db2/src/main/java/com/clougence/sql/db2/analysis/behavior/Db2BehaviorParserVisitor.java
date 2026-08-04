/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.db2.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;
import com.clougence.sql.db2.parser.antlr.Db2SqlParserBaseVisitor;

final class Db2BehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    Db2BehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        Db2StatementBehaviorVisitor visitor = new Db2StatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class Db2StatementBehaviorVisitor extends Db2SqlParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    Db2StatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitCreate_schema_statement(Db2SqlParser.Create_schema_statementContext ctx) {
        ParserRuleContext schema = ctx.schema_name() == null ? ctx.authorization_name() : ctx.schema_name();
        addUnary(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, schema));
        return null;
    }

    @Override
    public Void visitDrop_statement(Db2SqlParser.Drop_statementContext ctx) {
        if (ctx.schema_name() != null) {
            addUnary(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.schema_name()));
        } else if (ctx.table_name() != null) {
            addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, table(ctx.table_name()));
        } else if (ctx.index_name() != null) {
            addUnary(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ctx.index_name()));
        } else if (ctx.view_name() != null) {
            addUnary(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.view_name()));
        }
        return null;
    }

    @Override
    public Void visitCreate_table_statement(Db2SqlParser.Create_table_statementContext ctx) {
        List<BehaviorObject> targets = sources(ctx.as_result_table());
        if (ctx.table_or_view_name() != null) {
            add(targets, table(ctx.table_or_view_name()));
        }
        addRelation(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(ctx.table_name()), targets);
        return null;
    }

    @Override
    public Void visitAlter_table_statement(Db2SqlParser.Alter_table_statementContext ctx) {
        if (!ctx.table_name().isEmpty()) {
            addUnary(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, table(ctx.table_name(0)));
        }
        return null;
    }

    @Override
    public Void visitRename_statement(Db2SqlParser.Rename_statementContext ctx) {
        addRelation(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, table(ctx.source_table_name()), targets(table(ctx.target_identifier())));
        return null;
    }

    @Override
    public Void visitTruncate_statement(Db2SqlParser.Truncate_statementContext ctx) {
        addUnary(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, table(ctx.table_name()));
        return null;
    }

    @Override
    public Void visitCreate_index_statement(Db2SqlParser.Create_index_statementContext ctx) {
        ParserRuleContext table = ctx.table_name() == null ? ctx.nick_name() : ctx.table_name();
        addRelation(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.index_name()), targets(object(TargetType.Table, table)));
        return null;
    }

    @Override
    public Void visitCreate_view_statement(Db2SqlParser.Create_view_statementContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.view_name()), sources(ctx.fullselect()));
        return null;
    }

    @Override
    public Void visitAlter_view_statement(Db2SqlParser.Alter_view_statementContext ctx) {
        addUnary(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, ctx.view_name()));
        return null;
    }

    @Override
    public Void visitCall_statement(Db2SqlParser.Call_statementContext ctx) {
        addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.procedure_name()));
        return null;
    }

    @Override
    public Void visitSelect_statement(Db2SqlParser.Select_statementContext ctx) {
        addReads(ctx);
        return null;
    }

    @Override
    public Void visitSelect_into_statement(Db2SqlParser.Select_into_statementContext ctx) {
        addReads(ctx);
        return null;
    }

    @Override
    public Void visitInsert_statement(Db2SqlParser.Insert_statementContext ctx) {
        ParserRuleContext target = ctx.table_or_view_name() == null ? ctx.nick_name() : ctx.table_or_view_name();
        addRelation(SplitQueryType.INSERT, BehaviorAction.INSERT, table(target), sources(ctx));
        return null;
    }

    @Override
    public Void visitUpdate_statement(Db2SqlParser.Update_statementContext ctx) {
        Db2SqlParser.Update_statement_searched_updateContext searched = ctx.update_statement_searched_update();
        if (searched != null) {
            ParserRuleContext target = searched.table_or_view_name() == null ? searched.nick_name() : searched.table_or_view_name();
            addRelation(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(target), sources(searched.where_clause()));
        }
        return null;
    }

    @Override
    public Void visitDelete_statement(Db2SqlParser.Delete_statementContext ctx) {
        Db2SqlParser.Delete_statement_searched_deleteContext searched = ctx.delete_statement_searched_delete();
        if (searched != null) {
            ParserRuleContext target = searched.table_or_view_name() == null ? searched.nick_name() : searched.table_or_view_name();
            addRelation(SplitQueryType.DELETE, BehaviorAction.DELETE, table(target), sources(searched.where_clause()));
        }
        return null;
    }

    private void addReads(ParseTree tree) {
        for (BehaviorObject source : sources(tree)) {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, source);
        }
    }

    private List<BehaviorObject> sources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Db2SqlParser.Singles_table_referenceContext source : descendants(tree, Db2SqlParser.Singles_table_referenceContext.class)) {
            add(result, table(source.table_name()));
        }
        return result;
    }

    private BehaviorObject table(ParserRuleContext context) {
        return object(TargetType.Table, context);
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (Db2SqlParser.Id_Context id : descendants(context, Db2SqlParser.Id_Context.class)) {
            names.add(unquote(parser.getTokenStream().getText(id.getStart(), id.getStop())));
        }
        return objects.object(type, context, names);
    }

    private void add(List<BehaviorObject> target, BehaviorObject object) {
        if (object != null) {
            target.add(object);
        }
    }

    private List<BehaviorObject> targets(BehaviorObject object) {
        List<BehaviorObject> targets = new ArrayList<>();
        add(targets, object);
        return targets;
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
        for (BehaviorObject target : targets) {
            if (target != null) {
                relation.getTarget().add(target);
            }
        }
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

    private String unquote(String value) {
        String result = value;
        while (result.length() >= 2
               && (result.charAt(0) == '"' && result.charAt(result.length() - 1) == '"' || result.charAt(0) == '[' && result.charAt(result.length() - 1) == ']')) {
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }
}

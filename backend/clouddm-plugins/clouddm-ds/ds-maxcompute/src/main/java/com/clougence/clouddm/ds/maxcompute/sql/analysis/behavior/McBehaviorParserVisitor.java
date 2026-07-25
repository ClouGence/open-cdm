/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.maxcompute.sql.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserBaseVisitor;
import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class McBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    McBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        McStatementBehaviorVisitor visitor = new McStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class McStatementBehaviorVisitor extends McParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    McStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitFrom_table_name_clause(From_table_name_clauseContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitCreate_table_stmt(Create_table_stmtContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        if (ctx.create_table_definition() instanceof CreateTableSelectContext select) {
            addTableSources(sources, select.select_stmt());
        } else if (ctx.create_table_definition() instanceof CreateTableLikeContext like) {
            addObject(sources, object(TargetType.Table, like.table_name()));
        }
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.table_name()), sources);
        return null;
    }

    @Override
    public Void visitCreate_database_stmt(Create_database_stmtContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.qident()));
        return null;
    }

    @Override
    public Void visitDropSchema(DropSchemaContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.qident()));
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitDropMView(DropMViewContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.ident()));
        return null;
    }

    @Override
    public Void visitCreate_view_stmt(Create_view_stmtContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.table_name()), tableSources(ctx.select_stmt()));
        return null;
    }

    @Override
    public Void visitCreate_materialized_view_stmt(Create_materialized_view_stmtContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.name), tableSources(ctx.select_stmt()));
        return null;
    }

    @Override
    public Void visitAlter_materialized_view_stmt(Alter_materialized_view_stmtContext ctx) {
        add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.Materialized, ctx.name));
        return null;
    }

    @Override
    public Void visitAlter_table_stmt(Alter_table_stmtContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitTruncate_stmt(Truncate_stmtContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitInsert_stmt(Insert_stmtContext ctx) {
        add(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, ctx.table_name()), tableSources(ctx.select_stmt()));
        return null;
    }

    @Override
    public Void visitUpdate_stmt(Update_stmtContext ctx) {
        if (ctx.update_table() != null) {
            List<BehaviorObject> sources = tableSources(ctx.update_table().from_clause());
            addTableSources(sources, ctx.where_clause());
            add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.update_table().table_name()), sources);
        }
        return null;
    }

    @Override
    public Void visitDelete_stmt(Delete_stmtContext ctx) {
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.table_name()), tableSources(ctx.where_clause()));
        return null;
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        for (From_table_name_clauseContext source : descendants(tree, From_table_name_clauseContext.class)) {
            addObject(result, object(TargetType.Table, source.table_name()));
        }
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (context instanceof Table_nameContext table) {
            addNames(names, table.qident());
        } else if (context instanceof QidentContext qident) {
            addNames(names, qident);
        } else if (context instanceof IdentContext ident) {
            names.add(name(ident));
        }
        return objects.object(type, context, names);
    }

    private void addNames(List<String> names, QidentContext context) {
        for (IdentContext identifier : context.ident()) {
            names.add(name(identifier));
        }
    }

    private String name(ParserRuleContext context) {
        String value = parser.getTokenStream().getText(context.getStart(), context.getStop()).trim();
        if (value.length() >= 2 && ((value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`') || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"'))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
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
        behavior.getRelations().add(relation);
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || type != SplitQueryType.SELECT) {
            behavior.setStatementType(type);
        }
    }

    private void addObject(List<BehaviorObject> result, BehaviorObject object) {
        if (object != null) {
            result.add(object);
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

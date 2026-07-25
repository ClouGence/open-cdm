/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.analysis.behavior;

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
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

final class MsBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    MsBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        MsStatementBehaviorVisitor visitor = new MsStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class MsStatementBehaviorVisitor extends SqlServerParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    MsStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitSelect_statement_standalone(SqlServerParser.Select_statement_standaloneContext ctx) {
        for (BehaviorObject source : sourceTables(ctx)) {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, source);
        }
        return null;
    }

    @Override
    public Void visitCreate_table(SqlServerParser.Create_tableContext ctx) {
        addUnary(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitCreate_security_policy(SqlServerParser.Create_security_policyContext ctx) {
        addUnary(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, object(TargetType.RowAccessPolicy, ctx.security_policy_name));
        return null;
    }

    @Override
    public Void visitDrop_security_policy(SqlServerParser.Drop_security_policyContext ctx) {
        addUnary(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, object(TargetType.RowAccessPolicy, ctx.security_policy_name));
        return null;
    }

    @Override
    public Void visitDrop_table(SqlServerParser.Drop_tableContext ctx) {
        for (SqlServerParser.Table_nameContext tableName : ctx.table_name()) {
            addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, tableName));
        }
        return null;
    }

    @Override
    public Void visitCreate_view(SqlServerParser.Create_viewContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.simple_name()), sourceTables(ctx));
        return null;
    }

    @Override
    public Void visitDrop_view(SqlServerParser.Drop_viewContext ctx) {
        for (SqlServerParser.Simple_nameContext viewName : ctx.simple_name()) {
            addUnary(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, viewName));
        }
        return null;
    }

    @Override
    public Void visitCreate_index(SqlServerParser.Create_indexContext ctx) {
        if (!ctx.id_().isEmpty()) {
            addRelation(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.id_(0)), objects(object(TargetType.Table, ctx.table_name())));
        }
        return null;
    }

    @Override
    public Void visitDrop_index(SqlServerParser.Drop_indexContext ctx) {
        for (SqlServerParser.Drop_relational_or_xml_or_spatial_indexContext item : ctx.drop_relational_or_xml_or_spatial_index()) {
            List<SqlServerParser.Id_Context> ids = descendants(item, SqlServerParser.Id_Context.class);
            SqlServerParser.Full_table_nameContext tableName = first(item, SqlServerParser.Full_table_nameContext.class);
            if (!ids.isEmpty()) {
                addRelation(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ids.get(0)), objects(object(TargetType.Table, tableName)));
            }
        }
        return null;
    }

    @Override
    public Void visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.func_proc_name_schema()));
        return null;
    }

    @Override
    public Void visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx.func_proc_name_schema()));
        return null;
    }

    @Override
    public Void visitInsert_statement(SqlServerParser.Insert_statementContext ctx) {
        SqlServerParser.Full_table_nameContext tableName = first(ctx.ddl_object(), SqlServerParser.Full_table_nameContext.class);
        addRelation(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, tableName), sourceTables(ctx.insert_statement_value()));
        return null;
    }

    @Override
    public Void visitUpdate_statement(SqlServerParser.Update_statementContext ctx) {
        SqlServerParser.Full_table_nameContext tableName = first(ctx.ddl_object(), SqlServerParser.Full_table_nameContext.class);
        addRelation(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, tableName), sourceTables(ctx));
        return null;
    }

    @Override
    public Void visitDelete_statement(SqlServerParser.Delete_statementContext ctx) {
        SqlServerParser.Full_table_nameContext tableName = first(ctx.delete_statement_from(), SqlServerParser.Full_table_nameContext.class);
        if (tableName == null) {
            tableName = first(ctx.table_sources(), SqlServerParser.Full_table_nameContext.class);
        }
        addRelation(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, tableName), sourceTables(ctx));
        return null;
    }

    @Override
    public Void visitExecute_statement(SqlServerParser.Execute_statementContext ctx) {
        SqlServerParser.Func_proc_name_server_database_schemaContext procName = first(ctx.execute_body(), SqlServerParser.Func_proc_name_server_database_schemaContext.class);
        addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, procName));
        return null;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (SqlServerParser.Id_Context id : descendants(context, SqlServerParser.Id_Context.class)) {
            names.add(unquote(parser.getTokenStream().getText(id.getStart(), id.getStop())));
        }
        return objects.object(type, context, names);
    }

    private void addUnary(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
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
        behavior.setStatementType(type);
    }

    private List<BehaviorObject> sourceTables(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (SqlServerParser.Table_source_itemContext source : descendants(tree, SqlServerParser.Table_source_itemContext.class)) {
            if (source.full_table_name() != null) {
                addObject(result, object(TargetType.Table, source.full_table_name()));
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
        if (value.length() < 2) {
            return value;
        }
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        if (first == '[' && last == ']' || first == '"' && last == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4my.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForMySqlParserBaseVisitor;
import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForMySqlParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class ObMyStatementBehaviorVisitor extends ObForMySqlParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    ObMyStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof SelectStatementContext || node instanceof WithSelectStatementContext) {
            setType(SplitQueryType.SELECT);
        }
        return super.visitChildren(node);
    }

    @Override
    public Void visitUseStatement(UseStatementContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH,
            objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid())))));
        return null;
    }

    @Override
    public Void visitVariableClause(VariableClauseContext ctx) {
        Token start = ctx.getStart();
        Token stop = ctx.getStop();
        SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
        if (ctx.LOCAL_ID() != null) {
            type = SplitQueryType.SESSION_VARIABLE_RW;
        } else if (ctx.GLOBAL() != null || ctx.PERSIST() != null
            || ctx.GLOBAL_ID() != null && ctx.GLOBAL_ID().getText().toUpperCase(Locale.ROOT).startsWith("@@GLOBAL.")) {
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.uid() != null) {
            start = ctx.uid().getStart();
            stop = ctx.uid().getStop();
        }
        add(type, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, start, stop, variableName(start.getText())));
        return null;
    }

    @Override
    public Void visitMysqlVariable(MysqlVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ,
            objects.instanceObject(TargetType.ConfigKey, ctx, variableName(ctx.getText())));
        return null;
    }

    @Override
    public Void visitSetNames(SetNamesContext ctx) {
        configureCharset(ctx.NAMES().getSymbol(), ctx.NAMES().getSymbol(), ctx.COLLATE());
        return null;
    }

    @Override
    public Void visitSetCharset(SetCharsetContext ctx) {
        if (ctx.CHARSET() != null) {
            configureCharset(ctx.CHARSET().getSymbol(), ctx.CHARSET().getSymbol(), null);
        } else {
            configureCharset(ctx.CHARACTER().getSymbol(), ctx.SET(1).getSymbol(), null);
        }
        return null;
    }

    @Override
    public Void visitSessionSetItem(SessionSetItemContext ctx) {
        if (ctx.variableClause() != null) {
            return visitChildren(ctx);
        }
        Token start = ctx.getStart();
        Token stop = start;
        if (ctx.CHARACTER() != null) {
            stop = ctx.SET().getSymbol();
        }
        configureCharset(start, stop, ctx.COLLATE());
        return null;
    }

    private void configureCharset(Token start, Token stop, TerminalNode collate) {
        // BP7 writes all four keys for both NAMES and CHARSET; only their values differ.
        for (String key : List.of("character_set_client", "character_set_results", "character_set_connection")) {
            add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.ConfigKey, start, stop, key));
        }
        if (collate != null) {
            start = collate.getSymbol();
            stop = start;
        }
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE,
            objects.instanceObject(TargetType.ConfigKey, start, stop, "collation_connection"));
    }

    @Override
    public Void visitSetRole(SetRoleContext ctx) {
        Token selector = null;
        if (ctx.ALL() != null) {
            selector = ctx.ALL().getSymbol();
        } else if (ctx.NONE() != null) {
            selector = ctx.NONE().getSymbol();
        } else if (ctx.DEFAULT() != null) {
            selector = ctx.DEFAULT().getSymbol();
        }
        if (selector != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, selector));
        }
        BehaviorAction action = BehaviorAction.SWITCH;
        if (ctx.EXCEPT() != null) {
            // The resolver looks up excluded identities without activating them.
            action = BehaviorAction.READ;
        }
        for (UserNameContext role : ctx.userName()) {
            String host = "%";
            if (role.host != null) {
                host = unquote(role.host.getText().substring(1));
            }
            String identity = unquote(role.user.getText()) + "@" + host;
            add(SplitQueryType.SWITCH_ROLE, action, objects.instanceObject(TargetType.Role, role, identity));
        }
        return null;
    }

    @Override
    public Void visitShowObjectFilter(ShowObjectFilterContext ctx) {
        ShowCommonEntityContext entity = ctx.showCommonEntity();
        if (entity.VARIABLES() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, entity.VARIABLES().getSymbol()));
        } else if (entity.STATUS() != null && entity.FUNCTION() == null && entity.PROCEDURE() == null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, entity.STATUS().getSymbol()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitShowProcessList(ShowProcessListContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Session, ctx.PROCESSLIST().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowErrors(ShowErrorsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.errorFormat));
        return null;
    }

    @Override
    public Void visitShowCountErrors(ShowCountErrorsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.errorFormat));
        return null;
    }

    @Override
    public Void visitKillStatement(KillStatementContext ctx) {
        ExpressionContext id = ctx.expression();
        BehaviorObject subject;
        if (ctx.QUERY() != null) {
            // KILL QUERY takes a session ID, not the identity of its current query.
            subject = objects.instanceObject(TargetType.Query, id);
        } else if (id.getStart() == id.getStop() && unquote(id.getText()).matches("[0-9]+")) {
            subject = objects.instanceObject(TargetType.Session, id, unquote(id.getText()));
        } else {
            subject = objects.instanceObject(TargetType.Session, id);
        }
        add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, subject);
        return visit(id);
    }

    @Override
    public Void visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.CALL,
            objects.object(TargetType.Function, ctx.scalarFunctionName(), List.of(unquote(text(ctx.scalarFunctionName())))));
        return visitChildren(ctx);
    }

    @Override
    public Void visitAggregateFunctionCall(AggregateFunctionCallContext ctx) {
        Token name = ctx.aggregateFunction().getStart();
        add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, name, List.of(name.getText())));
        return visitChildren(ctx);
    }

    @Override
    public Void visitUdfFunctionCall(UdfFunctionCallContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, ctx.customFunctionName().fullId()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitNonAggregateFunctionCall(NonAggregateFunctionCallContext ctx) {
        Token name = ctx.nonAggregateFunction().getStart();
        add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, name, List.of(name.getText())));
        return visitChildren(ctx);
    }

    @Override
    public Void visitSpecificFunctionCall(SpecificFunctionCallContext ctx) {
        SpecificFunctionContext function = ctx.specificFunction();
        if (!(function instanceof CaseFunctionCallContext) && !(function instanceof SpecialTimeCallContext)) {
            Token name = function.getStart();
            add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, name, List.of(name.getText())));
        }
        return visitChildren(ctx);
    }

    private String variableName(String value) {
        if (value.startsWith("@@")) {
            value = value.substring(2).replaceFirst("(?i)^(SESSION|LOCAL|GLOBAL)\\.", "");
        } else if (value.startsWith("@")) {
            return unquote(value.substring(1));
        }
        return unquote(value).toLowerCase(Locale.ROOT);
    }

    private void setType(SplitQueryType type) {
        // The first outer action owns the statement type; nested expressions add relations only.
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(type);
        }
    }

    private void add(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        add(type, action, subject, List.of());
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, table(ctx), List.of());
        return null;
    }

    @Override
    public Void visitQueryCreateTable(QueryCreateTableContext ctx) {
        create(ctx.tableName(), descendants(ctx.selectStatement(), TableNameContext.class));
        return null;
    }

    @Override
    public Void visitCopyCreateTable(CopyCreateTableContext ctx) {
        List<TableNameContext> tables = ctx.tableName();
        create(tables.get(0), tables.subList(1, tables.size()));
        return null;
    }

    @Override
    public Void visitColumnCreateTable(ColumnCreateTableContext ctx) {
        create(ctx.tableName(), List.of());
        return null;
    }

    @Override
    public Void visitCallStatement(CallStatementContext ctx) {
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.procName().fullId()), List.of());
        return visitChildren(ctx);
    }

    private void create(TableNameContext subject, List<TableNameContext> sources) {
        List<BehaviorObject> targets = sources.stream().map(this::table).filter(Objects::nonNull).toList();
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(subject), targets);
    }

    private BehaviorObject table(TableNameContext context) {
        return context == null ? null : object(TargetType.Table, context.fullId());
    }

    private BehaviorObject object(TargetType type, FullIdContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = context.uid().stream().map(this::text).map(this::unquote).toList();
        return objects.object(type, context, names);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == value.charAt(value.length() - 1)
            && "`\"'".indexOf(value.charAt(0)) >= 0) {
            String quote = value.substring(0, 1);
            return value.substring(1, value.length() - 1).replace(quote + quote, quote);
        }
        return value;
    }

    private void add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        setType(type);
    }

    private <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        collect(tree, type, result);
        return result;
    }

    private <T extends ParserRuleContext> void collect(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collect(tree.getChild(i), type, result);
        }
    }
}

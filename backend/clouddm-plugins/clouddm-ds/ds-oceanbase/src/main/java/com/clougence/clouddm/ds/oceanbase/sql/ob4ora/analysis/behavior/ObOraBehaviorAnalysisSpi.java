/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.parser.ObOraDslProvider;
import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForOracleParser.*;
import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForOracleParserBaseVisitor;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.utils.StringUtils;

public class ObOraBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }
        ObOraBehaviorParserVisitor[] holder = new ObOraBehaviorParserVisitor[1];
        DslHelper.doVisitor(ObOraDslProvider.INSTANCE, query, (lexer, parser) -> {
            holder[0] = new ObOraBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}

final class ObOraBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser parser;
    private final Map<UmiTypes, Object> levels;
    private final int baseLine;
    private final int baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    ObOraBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        ObOraStatementBehaviorVisitor visitor = new ObOraStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class ObOraStatementBehaviorVisitor extends ObForOracleParserBaseVisitor<Void> {
    private final Parser parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior behavior = new StatementBehavior();

    ObOraStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SecQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitDml_table_expression_clause(Dml_table_expression_clauseContext ctx) {
        if (ctx.tableview_name() != null) {
            add(SecQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableview_name()), List.of());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreate_view(Create_viewContext ctx) {
        List<BehaviorObject> targets = descendants(ctx.select_only_statement(), Dml_table_expression_clauseContext.class).stream()
                .filter(source -> source.tableview_name() != null)
                .map(source -> object(TargetType.Table, source.tableview_name()))
                .filter(Objects::nonNull)
                .toList();
        add(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableview_name()), targets);
        return null;
    }

    @Override
    public Void visitCall_statement(Call_statementContext ctx) {
        if (!ctx.routine_name().isEmpty()) {
            add(SecQueryType.CALL_PROG_OBJ, BehaviorAction.CALL,
                    object(TargetType.Procedure, ctx.routine_name(0)), List.of());
        }
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

    private String unquote(String value) {
        return value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"'
                ? value.substring(1, value.length() - 1) : value;
    }

    private void add(SecQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
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

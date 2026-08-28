/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.TiDBDslProvider;
import com.clougence.clouddm.ds.tidb.sql.parser.TiSplitAnalysisSpi;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParserBaseVisitor;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

public class TiBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = new TiSplitAnalysisSpi().splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return analyzeStatement(reader, levels, codeLine, codeColumn).stream();
        }).onClose(scripts::close);
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        TiBehaviorParserVisitor[] holder = new TiBehaviorParserVisitor[1];
        DslHelper.doVisitor(TiDBDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new TiBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}

final class TiBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    TiBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        TiStatementBehaviorVisitor visitor = new TiStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class TiStatementBehaviorVisitor extends TiDBParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    TiStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        if (ctx.analyze != null) {
            add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, objects.instanceObject(TargetType.Instance, ctx.getStart()), List.of());
            return null;
        }
        return visitChildren(ctx);
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
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        BehaviorRelation relation = add(SplitQueryType.INSERT, BehaviorAction.INSERT, table(ctx.tableName()), tables(ctx.insertStatementValue()));
        setInsertRows(relation, ctx.insertStatementValue());
        return null;
    }

    @Override
    public Void visitReplaceStatement(ReplaceStatementContext ctx) {
        BehaviorRelation relation = add(SplitQueryType.MERGE, BehaviorAction.MERGE, table(ctx.tableName()), tables(ctx.insertStatementValue()));
        setInsertRows(relation, ctx.insertStatementValue());
        return null;
    }

    @Override
    public Void visitSingleUpdateStatement(SingleUpdateStatementContext ctx) {
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(ctx.tableName()), tables(ctx.whereClause()));
        return null;
    }

    @Override
    public Void visitSingleDeleteStatement(SingleDeleteStatementContext ctx) {
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, table(ctx.tableName()), tables(ctx.whereClause()));
        return null;
    }

    @Override
    public Void visitMultipleDeleteStatement(MultipleDeleteStatementContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>(tables(ctx.tableSources()));
        sources.addAll(tables(ctx.expression()));
        for (TableNameContext target : ctx.tableName()) {
            add(SplitQueryType.DELETE, BehaviorAction.DELETE, table(resolveTarget(target, ctx.tableSources())), sources);
        }
        return null;
    }

    @Override
    public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
        List<TableNameContext> sourceTables = descendants(ctx.tableSources(), TableNameContext.class);
        if (!sourceTables.isEmpty()) {
            List<BehaviorObject> sources = new ArrayList<>(tables(ctx.tableSources()));
            sources.addAll(tables(ctx.whereClause()));
            add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(sourceTables.get(0)), sources);
        }
        return null;
    }

    private void create(TableNameContext subject, List<TableNameContext> sources) {
        List<BehaviorObject> targets = sources.stream().map(this::table).filter(Objects::nonNull).toList();
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(subject), targets);
    }

    private List<BehaviorObject> tables(ParseTree tree) {
        return descendants(tree, TableNameContext.class).stream().map(this::table).filter(Objects::nonNull).toList();
    }

    private void setInsertRows(BehaviorRelation relation, InsertStatementValueContext value) {
        if (relation == null || value == null) {
            return;
        }
        List<CommentInsertValueContext> values = descendants(value, CommentInsertValueContext.class);
        if (!values.isEmpty()) {
            relation.setInsertRows((long) values.get(0).expressionsWithDefaults().size());
        }
    }

    private BehaviorObject table(TableNameContext context) {
        return context == null ? null : object(TargetType.Table, context.fullId());
    }

    private TableNameContext resolveTarget(TableNameContext target, TableSourcesContext sources) {
        String targetName = text(target.fullId());
        for (AtomTableItemContext source : descendants(sources, AtomTableItemContext.class)) {
            if (source.aliasName() != null && targetName.equalsIgnoreCase(text(source.aliasName()))) {
                return source.tableName();
            }
        }
        return target;
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
        return value.length() >= 2 && value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`' ? value.substring(1, value.length() - 1) : value;
    }

    private BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return null;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
        return relation;
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

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.doris.analysis.behavior;

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
import com.clougence.sql.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParser.*;

final class DrBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    DrBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        DrStatementBehaviorVisitor visitor = new DrStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class DrStatementBehaviorVisitor extends DorisParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    DrStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.multipartIdentifier()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertTable(InsertTableContext ctx) {
        if (ctx.explain() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableName), tableSources(ctx.query()));
            return null;
        }
        SplitQueryType type = ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        add(type, type == SplitQueryType.INSERT ? BehaviorAction.INSERT : BehaviorAction.MERGE, object(TargetType.Table, ctx.tableName), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitUpdate(UpdateContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.whereClause());
        if (ctx.explain() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableName), sources);
            return null;
        }
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.tableName), sources);
        return null;
    }

    @Override
    public Void visitDelete(DeleteContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.relations());
        addTableSources(sources, ctx.whereClause());
        if (ctx.explain() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableName), sources);
            return null;
        }
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.tableName), sources);
        return null;
    }

    @Override
    public Void visitCreateTable(CreateTableContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.name), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitCreateTableLike(CreateTableLikeContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.name), List.of(object(TargetType.Table, ctx.existedTable)));
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.name), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitCreateMTMV(CreateMTMVContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.mvName), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        add(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.name));
        return null;
    }

    @Override
    public Void visitDropMV(DropMVContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitRenameClause(RenameClauseContext ctx) {
        BehaviorObject source = renameSource(ctx);
        if (source != null) {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, List.of(object(TargetType.Table, ctx.newName)));
        } else {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, object(TargetType.Table, ctx.newName));
        }
        return null;
    }

    private BehaviorObject renameSource(RenameClauseContext ctx) {
        ParseTree parent = ctx.getParent();
        while (parent instanceof ParserRuleContext context) {
            if (context instanceof AlterTableContext alter) {
                return object(TargetType.Table, alter.tableName);
            }
            parent = context.getParent();
        }
        return null;
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        for (TableNameContext table : descendants(tree, TableNameContext.class)) {
            BehaviorObject object = object(TargetType.Table, table.multipartIdentifier());
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
        if (context instanceof MultipartIdentifierContext multipart) {
            for (ErrorCapturingIdentifierContext identifier : multipart.errorCapturingIdentifier()) {
                names.add(unquote(text(identifier)));
            }
        } else {
            names.add(unquote(text(context)));
        }
        return objects.object(type, context, names);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String unquote(String value) {
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
            if (target != null) {
                relation.getTarget().add(target);
            }
        }
        behavior.getRelations().add(relation);
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || type != SplitQueryType.SELECT) {
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

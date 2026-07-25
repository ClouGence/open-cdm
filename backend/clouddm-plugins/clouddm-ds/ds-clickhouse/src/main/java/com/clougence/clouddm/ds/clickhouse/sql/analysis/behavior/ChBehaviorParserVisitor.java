/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParserBaseVisitor;
import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class ChBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    ChBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        ChStatementBehaviorVisitor visitor = new ChStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class ChStatementBehaviorVisitor extends ClickHouseParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    ChStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitTableExprIdentifier(TableExprIdentifierContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableIdentifier()));
        return null;
    }

    @Override
    public Void visitCreateTableStmt(CreateTableStmtContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        if (ctx.tableSchemaClause() != null) {
            for (SchemaAsTableClauseContext source : descendants(ctx.tableSchemaClause(), SchemaAsTableClauseContext.class)) {
                addObject(sources, object(TargetType.Table, source.tableIdentifier()));
            }
        }
        addTableSources(sources, ctx.subqueryClause());
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.tableIdentifier()), sources);
        return null;
    }

    @Override
    public Void visitCreateViewStmt(CreateViewStmtContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        addTableSources(sources, ctx.subqueryClause());
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableIdentifier()), sources);
        return null;
    }

    @Override
    public Void visitCreateMaterializedViewStmt(CreateMaterializedViewStmtContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        addTableSources(sources, ctx.subqueryClause());
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.tableIdentifier()), sources);
        if (ctx.destinationClause() != null) {
            add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.destinationClause().tableIdentifier()));
        }
        return null;
    }

    @Override
    public Void visitCreateDatabaseStmt(CreateDatabaseStmtContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.databaseIdentifier()));
        return null;
    }

    @Override
    public Void visitDropDatabaseStmt(DropDatabaseStmtContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.databaseIdentifier()));
        return null;
    }

    @Override
    public Void visitDropTableStmt(DropTableStmtContext ctx) {
        if (ctx.TABLE() != null) {
            add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.tableIdentifier()));
        } else if (ctx.VIEW() != null) {
            add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.tableIdentifier()));
        }
        return null;
    }

    @Override
    public Void visitAlterTableStmt(AlterTableStmtContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableIdentifier()), tableSources(ctx));
        return null;
    }

    @Override
    public Void visitInsertStmt(InsertStmtContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        addTableSources(sources, ctx.dataClause());
        add(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, ctx.tableIdentifier()), sources);
        return null;
    }

    @Override
    public Void visitDeleteStmt(DeleteStmtContext ctx) {
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.nestedIdentifier()), tableSources(ctx.whereClause()));
        return null;
    }

    @Override
    public Void visitUpdateStmt(UpdateStmtContext ctx) {
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.nestedIdentifier()), tableSources(ctx.whereClause()));
        return null;
    }

    @Override
    public Void visitRenameEntityClause(RenameEntityClauseContext ctx) {
        pairRenames(SplitQueryType.RENAME_TABLE, TargetType.Table, ctx.tableIdentifier());
        pairRenames(SplitQueryType.RENAME_SCHEMA, TargetType.Schema, ctx.databaseIdentifier());
        return null;
    }

    @Override
    public Void visitTruncateStmt(TruncateStmtContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableIdentifier()));
        return null;
    }

    @Override
    public Void visitUseStmt(UseStmtContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, ctx.databaseIdentifier()));
        return null;
    }

    private void addTableSources(List<BehaviorObject> sources, ParseTree tree) {
        for (TableExprIdentifierContext source : descendants(tree, TableExprIdentifierContext.class)) {
            addObject(sources, object(TargetType.Table, source.tableIdentifier()));
        }
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> sources = new ArrayList<>();
        addTableSources(sources, tree);
        return sources;
    }

    private <T extends ParserRuleContext> void pairRenames(SplitQueryType type, TargetType targetType, List<T> names) {
        for (int i = 0; i + 1 < names.size(); i += 2) {
            add(type, BehaviorAction.RENAME, object(targetType, names.get(i)), List.of(object(targetType, names.get(i + 1))));
        }
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (context instanceof TableIdentifierContext table) {
            if (table.databaseIdentifier() != null) {
                names.add(name(table.databaseIdentifier().identifier()));
            }
            names.add(name(table.identifier()));
        } else if (context instanceof DatabaseIdentifierContext database) {
            names.add(name(database.identifier()));
        } else if (context instanceof NestedIdentifierContext nested) {
            for (IdentifierContext identifier : nested.identifier()) {
                names.add(name(identifier));
            }
        }
        return objects.object(type, context, names);
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

    private void addObject(List<BehaviorObject> objects, BehaviorObject object) {
        if (object != null) {
            objects.add(object);
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

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class DmBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    DmBehaviorParserVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        DmStatementBehaviorVisitor visitor = new DmStatementBehaviorVisitor(levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class DmStatementBehaviorVisitor extends DmSqlParserBaseVisitor<Void> {
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior     = new StatementBehavior();
    private final List<String>             schemaScopes = new ArrayList<>();

    DmStatementBehaviorVisitor(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitSelectStatement(DmSqlParser.SelectStatementContext ctx) {
        for (BehaviorObject source : tableSources(ctx)) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, source);
        }
        return null;
    }

    @Override
    public Void visitInsertStatement(DmSqlParser.InsertStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx);
        if (ctx.singleInsertStatement() != null) {
            addInsertTarget(ctx.singleInsertStatement().insertTarget(), sources);
        }
        for (DmSqlParser.MultiInsertIntoContext into : descendants(ctx, DmSqlParser.MultiInsertIntoContext.class)) {
            addInsertTarget(into.insertTarget(), sources);
        }
        return null;
    }

    @Override
    public Void visitUpdateStatement(DmSqlParser.UpdateStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.whereClause());
        for (DmSqlParser.TableSourceContext table : ctx.updateTargetList().tableSource()) {
            for (NameParts name : directTableNames(table)) {
                add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, table, name), sources);
            }
        }
        return null;
    }

    @Override
    public Void visitDeleteStatement(DmSqlParser.DeleteStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.deleteMultiTableClause());
        addTableSources(sources, ctx.whereClause());
        for (NameParts name : deleteTargetNames(ctx)) {
            add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.deleteTarget(), name), sources);
        }
        return null;
    }

    @Override
    public Void visitMergeStatement(DmSqlParser.MergeStatementContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx);
        if (ctx.mergeIntoTarget().qualifiedName() != null) {
            add(SplitQueryType.MERGE, BehaviorAction.MERGE, object(TargetType.Table, ctx.mergeIntoTarget(), NameParts.from(ctx.mergeIntoTarget().qualifiedName())), sources);
        } else {
            for (BehaviorObject target : tableSources(ctx.mergeIntoTarget().selectStatement())) {
                add(SplitQueryType.MERGE, BehaviorAction.MERGE, target, sources);
            }
        }
        return null;
    }

    @Override
    public Void visitFlashbackStatement(DmSqlParser.FlashbackStatementContext ctx) {
        for (DmSqlParser.QualifiedNameContext name : ctx.qualifiedName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ADMIN, object(TargetType.Table, name, NameParts.from(name)));
        }
        return null;
    }

    @Override
    public Void visitRefreshMaterializedViewStatement(DmSqlParser.RefreshMaterializedViewStatementContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.ADMIN, object(TargetType.Materialized, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitTableCreate(DmSqlParser.TableCreateContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.tableCreateBody());
        if (ctx.likeSourceTable != null) {
            addObject(sources, object(TargetType.Table, ctx.likeSourceTable, NameParts.from(ctx.likeSourceTable)));
        }
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.targetTable, schemaScoped(NameParts.from(ctx.targetTable))), sources);
        return null;
    }

    @Override
    public Void visitViewCreate(DmSqlParser.ViewCreateContext ctx) {
        TargetType type = ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized;
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(type, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), tableSources(ctx));
        for (DmSqlParser.MaterializedViewPrebuiltClauseContext prebuilt : descendants(ctx, DmSqlParser.MaterializedViewPrebuiltClauseContext.class)) {
            if (prebuilt.prebuiltTable != null) {
                add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, prebuilt.prebuiltTable, NameParts.from(prebuilt.prebuiltTable)));
            }
        }
        return null;
    }

    @Override
    public Void visitMaterializedViewLogCreate(DmSqlParser.MaterializedViewLogCreateContext ctx) {
        NameParts name = NameParts.from(ctx.qualifiedName());
        add(SplitQueryType.CREATE_LOG, BehaviorAction.CREATE, object(TargetType.Log, ctx.qualifiedName(), name), List.of(object(TargetType.Table, ctx.qualifiedName(), name)));
        return null;
    }

    @Override
    public Void visitIndexCreate(DmSqlParser.IndexCreateContext ctx) {
        List<DmSqlParser.QualifiedNameContext> names = ctx.qualifiedName();
        if (!names.isEmpty()) {
            List<BehaviorObject> targets = names.size() > 1 ? List.of(object(TargetType.Table, names.get(1), schemaScoped(NameParts.from(names.get(1))))) : List.of();
            add(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, names.get(0), schemaScoped(NameParts.from(names.get(0)))), targets);
        }
        return null;
    }

    @Override
    public Void visitSchemaCreate(DmSqlParser.SchemaCreateContext ctx) {
        NameParts parsed = ctx.schemaName == null ? null : NameParts.from(ctx.schemaName);
        String schema = parsed == null ? schemaAuthorizationOwner(ctx) : parsed.name();
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx, new NameParts(parsed == null ? null : parsed.catalog(), null, schema)));
        if (schema == null) {
            return visitChildren(ctx);
        }
        schemaScopes.add(schema);
        try {
            return visitChildren(ctx);
        } finally {
            schemaScopes.remove(schemaScopes.size() - 1);
        }
    }

    @Override
    public Void visitSequenceCreate(DmSqlParser.SequenceCreateContext ctx) {
        add(SplitQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))));
        return null;
    }

    @Override
    public Void visitProcedureCreate(DmSqlParser.ProcedureCreateContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx
            .qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), tableSources(ctx));
        addNestedStatements(ctx);
        return null;
    }

    @Override
    public Void visitFunctionCreate(DmSqlParser.FunctionCreateContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx
            .qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), tableSources(ctx));
        addNestedStatements(ctx);
        return null;
    }

    @Override
    public Void visitTriggerCreate(DmSqlParser.TriggerCreateContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.triggerCreateTail().tableTriggerCreateTail() != null) {
            DmSqlParser.QualifiedNameContext table = first(ctx.triggerCreateTail().tableTriggerCreateTail().qualifiedName());
            addObject(targets, object(TargetType.Table, table, NameParts.from(table)));
        }
        addTableSources(targets, ctx);
        add(SplitQueryType.CREATE_TRIGGER, BehaviorAction.CREATE, object(TargetType.Trigger, ctx.qualifiedName(), schemaScoped(NameParts.from(ctx.qualifiedName()))), targets);
        addNestedStatements(ctx);
        return null;
    }

    @Override
    public Void visitSynonymCreate(DmSqlParser.SynonymCreateContext ctx) {
        add(SplitQueryType.CREATE_SYNONYM, BehaviorAction.CREATE, object(TargetType.Synonym, ctx.qualifiedName(0), schemaScoped(NameParts.from(ctx.qualifiedName(0)))));
        return null;
    }

    @Override
    public Void visitAlterTarget(DmSqlParser.AlterTargetContext ctx) {
        DmSqlParser.QualifiedNameContext qualified = first(ctx.qualifiedName());
        NameParts name = qualified == null ? null : schemaScoped(NameParts.from(qualified));
        if (ctx.TABLE() != null) {
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, qualified, name));
        } else if (ctx.INDEX() != null) {
            add(SplitQueryType.ALTER_INDEX, BehaviorAction.ALTER, object(TargetType.Index, qualified, name));
        } else if (ctx.VIEW() != null) {
            add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized, qualified, name));
        } else if (ctx.SEQUENCE() != null) {
            add(SplitQueryType.ALTER_SEQUENCE, BehaviorAction.ALTER, object(TargetType.Sequence, qualified, name));
        } else if (ctx.PROCEDURE() != null || ctx.FUNCTION() != null) {
            add(SplitQueryType.ALTER_PROG_OBJ, BehaviorAction.ALTER, object(ctx.PROCEDURE() != null ? TargetType.Procedure : TargetType.Function, qualified, name));
        } else if (ctx.TRIGGER() != null) {
            add(SplitQueryType.ALTER_TRIGGER, BehaviorAction.ALTER, object(TargetType.Trigger, qualified, name));
        } else if (ctx.contextTableName != null) {
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.contextTableName, NameParts.from(ctx.contextTableName)));
        } else if (ctx.PACKAGE() != null) {
            add(SplitQueryType.ADMIN_PROG_OBJ, BehaviorAction.ADMIN, object(TargetType.Package, qualified, name));
        } else if (ctx.TABLESPACE() != null) {
            add(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.ALTER, object(TargetType.Tablespace, qualified, name));
        } else if (ctx.PROFILE() != null) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, object(TargetType.ConfigKey, ctx
                .identifier(), new NameParts(null, null, NameParts.clean(ctx.identifier().getText()))));
        } else if (ctx.TYPE() != null || ctx.CLASS() != null) {
            add(SplitQueryType.ADMIN_TYPE, BehaviorAction.ADMIN, object(TargetType.Type, qualified, name));
        }
        return null;
    }

    @Override
    public Void visitDropTarget(DmSqlParser.DropTargetContext ctx) {
        DmSqlParser.QualifiedNameContext qualified = first(ctx.qualifiedName());
        NameParts name = qualified == null ? null : NameParts.from(qualified);
        if (ctx.TABLE() != null) {
            add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, qualified, name));
        } else if (ctx.MATERIALIZED() != null && ctx.LOG() != null) {
            add(SplitQueryType.DROP_LOG, BehaviorAction.DROP, object(TargetType.Log, qualified, name));
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, qualified, name));
        } else if (ctx.VIEW() != null) {
            add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized, qualified, name));
        } else if (ctx.INDEX() != null) {
            add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, qualified, name));
        } else if (ctx.SCHEMA() != null || ctx.DATABASE() != null) {
            add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, qualified, name));
        } else if (ctx.SEQUENCE() != null) {
            add(SplitQueryType.DROP_SEQUENCE, BehaviorAction.DROP, object(TargetType.Sequence, qualified, name));
        } else if (ctx.PROCEDURE() != null || ctx.FUNCTION() != null) {
            add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(ctx.PROCEDURE() != null ? TargetType.Procedure : TargetType.Function, qualified, name));
        } else if (ctx.TRIGGER() != null) {
            add(SplitQueryType.DROP_TRIGGER, BehaviorAction.DROP, object(TargetType.Trigger, qualified, name));
        } else if (ctx.SYNONYM() != null) {
            add(SplitQueryType.DROP_SYNONYM, BehaviorAction.DROP, object(TargetType.Synonym, qualified, name));
        } else if (ctx.contextTableName != null) {
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.contextTableName, NameParts.from(ctx.contextTableName)));
        } else if (ctx.PACKAGE() != null) {
            add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Package, qualified, name));
        } else if (ctx.TABLESPACE() != null) {
            add(SplitQueryType.DROP_TABLESPACE, BehaviorAction.DROP, object(TargetType.Tablespace, qualified, name));
        } else if (ctx.LIBRARY() != null) {
            add(SplitQueryType.DROP_LIBRARY, BehaviorAction.DROP, object(TargetType.Library, qualified, name));
        } else if (ctx.DOMAIN() != null || ctx.TYPE() != null || ctx.CLASS() != null) {
            add(SplitQueryType.DROP_TYPE, BehaviorAction.DROP, object(TargetType.Type, qualified, name));
        } else if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null || ctx.PROFILE() != null) {
            NameParts config = qualified == null ? new NameParts(null, null, NameParts.clean(ctx.identifier().getText())) : name;
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, object(TargetType.ConfigKey, ctx, config));
        }
        return null;
    }

    @Override
    public Void visitTruncateStatement(DmSqlParser.TruncateStatementContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitCommentStatement(DmSqlParser.CommentStatementContext ctx) {
        SplitQueryType type = ctx.commentTarget()
            .VIEW() != null ? SplitQueryType.ALTER_VIEW : ctx.commentTarget().TABLE() != null ? SplitQueryType.COMMENT_TABLE : SplitQueryType.COMMENT_COLUMN;
        TargetType target = ctx.commentTarget().VIEW() != null ? TargetType.View : TargetType.Table;
        add(type, BehaviorAction.ALTER, object(target, ctx.commentTarget(), NameParts.from(ctx.commentTarget().qualifiedName())));
        return null;
    }

    @Override
    public Void visitGrantStatement(DmSqlParser.GrantStatementContext ctx) {
        if (ctx.grantPrivilegeStatement() != null && ctx.grantPrivilegeStatement().privilegeObjectClause() != null) {
            addPrivilege(SplitQueryType.GRANT, BehaviorAction.GRANT, ctx.grantPrivilegeStatement().privilegeObjectClause().privilegeObject());
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(DmSqlParser.RevokeStatementContext ctx) {
        if (ctx.revokePrivilegeStatement() != null && ctx.revokePrivilegeStatement().privilegeObjectClause() != null) {
            addPrivilege(SplitQueryType.REVOKE, BehaviorAction.REVOKE, ctx.revokePrivilegeStatement().privilegeObjectClause().privilegeObject());
        }
        return null;
    }

    @Override
    public Void visitCallStatement(DmSqlParser.CallStatementContext ctx) {
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitProcedureCallStatement(DmSqlParser.ProcedureCallStatementContext ctx) {
        ParserRuleContext context = ctx.qualifiedName() == null ? ctx.bareRoutineName() : ctx.qualifiedName();
        NameParts name = ctx.qualifiedName() == null ? NameParts.from(ctx.bareRoutineName()) : NameParts.from(ctx.qualifiedName());
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, context, name));
        return null;
    }

    @Override
    public Void visitLockTableStatement(DmSqlParser.LockTableStatementContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.OTHER, object(TargetType.Table, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitSetSchemaStatement(DmSqlParser.SetSchemaStatementContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    @Override
    public Void visitSetIdentityInsertStatement(DmSqlParser.SetIdentityInsertStatementContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.OTHER, object(TargetType.Table, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())));
        return null;
    }

    private void addNestedStatements(ParseTree tree) {
        for (DmSqlParser.InsertStatementContext statement : descendants(tree, DmSqlParser.InsertStatementContext.class)) {
            visitInsertStatement(statement);
        }
        for (DmSqlParser.UpdateStatementContext statement : descendants(tree, DmSqlParser.UpdateStatementContext.class)) {
            visitUpdateStatement(statement);
        }
        for (DmSqlParser.DeleteStatementContext statement : descendants(tree, DmSqlParser.DeleteStatementContext.class)) {
            visitDeleteStatement(statement);
        }
        for (DmSqlParser.MergeStatementContext statement : descendants(tree, DmSqlParser.MergeStatementContext.class)) {
            visitMergeStatement(statement);
        }
        for (DmSqlParser.CallStatementContext statement : descendants(tree, DmSqlParser.CallStatementContext.class)) {
            visitCallStatement(statement);
        }
        for (DmSqlParser.ProcedureCallStatementContext statement : descendants(tree, DmSqlParser.ProcedureCallStatementContext.class)) {
            visitProcedureCallStatement(statement);
        }
    }

    private void addInsertTarget(DmSqlParser.InsertTargetContext ctx, List<BehaviorObject> sources) {
        if (ctx == null) {
            return;
        }
        if (ctx.qualifiedName() != null) {
            add(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, ctx.qualifiedName(), NameParts.from(ctx.qualifiedName())), sources);
        } else {
            for (BehaviorObject target : tableSources(ctx.selectStatement())) {
                add(SplitQueryType.INSERT, BehaviorAction.INSERT, target, sources);
            }
        }
    }

    private void addPrivilege(SplitQueryType type, BehaviorAction action, DmSqlParser.PrivilegeObjectContext ctx) {
        if (ctx == null) {
            return;
        }
        TargetType target = TargetType.Object;
        if (ctx.SCHEMA() != null) {
            target = TargetType.Schema;
        } else if (ctx.privilegeObjectType() != null) {
            target = privilegeTarget(ctx.privilegeObjectType());
        }
        NameParts name = ctx.SCHEMA() != null ? new NameParts(null, null, NameParts.clean(ctx.identifier().getText())) : NameParts.from(ctx.qualifiedName());
        add(type, action, object(target, ctx, name));
    }

    private TargetType privilegeTarget(DmSqlParser.PrivilegeObjectTypeContext ctx) {
        if (ctx.TABLE() != null)
            return TargetType.Table;
        if (ctx.VIEW() != null)
            return ctx.MATERIALIZED() == null ? TargetType.View : TargetType.Materialized;
        if (ctx.INDEX() != null)
            return TargetType.Index;
        if (ctx.SEQUENCE() != null)
            return TargetType.Sequence;
        if (ctx.PROCEDURE() != null)
            return TargetType.Procedure;
        if (ctx.FUNCTION() != null)
            return TargetType.Function;
        if (ctx.TRIGGER() != null)
            return TargetType.Trigger;
        if (ctx.SYNONYM() != null)
            return TargetType.Synonym;
        return TargetType.Object;
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        if (tree == null) {
            return;
        }
        Set<String> ctes = cteNames(tree);
        for (DmSqlParser.TablePrimaryContext table : descendants(tree, DmSqlParser.TablePrimaryContext.class)) {
            if (table.qualifiedName() != null) {
                NameParts name = NameParts.from(table.qualifiedName());
                if (!isCte(name, ctes)) {
                    addObject(result, object(TargetType.Table, table.qualifiedName(), name));
                }
            }
        }
    }

    private Set<String> cteNames(ParseTree tree) {
        Set<String> names = new HashSet<>();
        for (DmSqlParser.CteDefinitionContext cte : descendants(tree, DmSqlParser.CteDefinitionContext.class)) {
            names.add(NameParts.clean(cte.identifier().getText()).toLowerCase());
        }
        return names;
    }

    private boolean isCte(NameParts name, Set<String> ctes) {
        return name != null && name.catalog() == null && name.schema() == null && name.name() != null && ctes.contains(name.name().toLowerCase());
    }

    private List<NameParts> directTableNames(DmSqlParser.TableSourceContext source) {
        List<NameParts> result = new ArrayList<>();
        if (source != null && source.tablePrimary() != null && source.tablePrimary().qualifiedName() != null) {
            result.add(NameParts.from(source.tablePrimary().qualifiedName()));
        }
        return result;
    }

    private List<NameParts> deleteTargetNames(DmSqlParser.DeleteStatementContext ctx) {
        List<NameParts> result = new ArrayList<>();
        DmSqlParser.TablePrimaryContext target = ctx.deleteTarget().tablePrimary();
        if (target.qualifiedName() != null) {
            result.add(NameParts.from(target.qualifiedName()));
        } else {
            for (DmSqlParser.TablePrimaryContext table : descendants(target, DmSqlParser.TablePrimaryContext.class)) {
                if (table.qualifiedName() != null) {
                    result.add(NameParts.from(table.qualifiedName()));
                }
            }
        }
        return result;
    }

    private String schemaAuthorizationOwner(DmSqlParser.SchemaCreateContext ctx) {
        if (ctx.schemaAuthorizationOnly() != null && ctx.schemaAuthorizationOnly().schemaOwner != null) {
            return NameParts.clean(ctx.schemaAuthorizationOnly().schemaOwner.getText());
        }
        if (ctx.schemaAuthorizationClause() != null && ctx.schemaAuthorizationClause().schemaOwner != null) {
            return NameParts.clean(ctx.schemaAuthorizationClause().schemaOwner.getText());
        }
        return null;
    }

    private NameParts schemaScoped(NameParts name) {
        if (name == null || name.schema() != null || schemaScopes.isEmpty()) {
            return name;
        }
        return new NameParts(name.catalog(), schemaScopes.get(schemaScopes.size() - 1), name.name());
    }

    private DmSqlParser.QualifiedNameContext first(List<DmSqlParser.QualifiedNameContext> values) {
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context, NameParts name) {
        if (context == null || name == null || name.name() == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (name.catalog() != null)
            names.add(name.catalog());
        if (name.schema() != null)
            names.add(name.schema());
        names.add(name.name());
        return objects.object(type, context, names);
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
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(type);
        }
    }

    private void addObject(List<BehaviorObject> values, BehaviorObject value) {
        if (value != null && values.stream().noneMatch(existing -> existing.getResourcePath().equals(value.getResourcePath()))) {
            values.add(value);
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

    private record NameParts(String catalog, String schema, String name) {
        private static NameParts from(DmSqlParser.QualifiedNameContext ctx) {
            if (ctx == null)
                return new NameParts(null, null, null);
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.dottedName().identifier().getText()));
            for (DmSqlParser.DottedNamePartContext part : ctx.dottedName().dottedNamePart()) {
                parts.add(clean(part.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts from(DmSqlParser.BareRoutineNameContext ctx) {
            if (ctx == null)
                return new NameParts(null, null, null);
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.regularIdentifier().getText()));
            for (DmSqlParser.DottedNamePartContext part : ctx.dottedNamePart()) {
                parts.add(clean(part.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts fromParts(List<String> parts) {
            int size = parts.size();
            return size == 0 ? new NameParts(null, null, null) : new NameParts(size > 2 ? parts.get(size - 3) : null, size > 1 ? parts.get(size - 2) : null, parts.get(size - 1));
        }

        private static String clean(String text) {
            if (text == null || text.length() < 2)
                return text;
            if (text.startsWith("\"") && text.endsWith("\"")) {
                return text.substring(1, text.length() - 1).replace("\"\"", "\"");
            }
            if (text.startsWith("[") && text.endsWith("]")) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        }
    }
}

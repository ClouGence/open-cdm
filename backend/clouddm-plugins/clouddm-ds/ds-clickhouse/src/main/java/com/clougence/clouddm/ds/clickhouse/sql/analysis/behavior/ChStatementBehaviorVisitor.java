/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParserBaseVisitor;
import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

class ChStatementBehaviorVisitor extends ClickHouseParserBaseVisitor<Void> {
    private final Parser                     parser;
    protected final RdbBehaviorObjectFactory objects;
    private final String                     instancePath;
    private final StatementBehavior          behavior = new StatementBehavior();

    ChStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.instancePath = objects.instanceObject(TargetType.Instance, parser.getTokenStream().get(0)).getObjectPath();
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitSelectUnionStmt(SelectUnionStmtContext ctx) {
        setType(SplitQueryType.SELECT);
        return visitChildren(ctx);
    }

    @Override
    public Void visitSetStmt(SetStmtContext ctx) {
        String firstName = name(ctx.settingExprList().settingExpr(0).identifier());
        if (firstName.startsWith("param_")) {
            setType(SplitQueryType.SESSION_VARIABLE_RW);
        } else {
            setType(SplitQueryType.SESSION_SETTING_WRITE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitSettingExpr(SettingExprContext ctx) {
        String key = name(ctx.identifier());
        if (key.equals("profile")) {
            // Applying an existing profile does not alter its definition or store a "profile" setting.
            if (ctx.literal() != null && ctx.literal().stringLiteral() != null) {
                StringLiteralContext profile = ctx.literal().stringLiteral();
                add(SplitQueryType.SELECT, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Profile, profile, name(profile)));
            } else if (ctx.queryParameter() != null) {
                add(SplitQueryType.SELECT, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Profile, ctx.queryParameter()));
            }
        } else {
            if (key.startsWith("param_")) {
                key = key.substring("param_".length());
            }
            add(SplitQueryType.SELECT, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), key));
        }
        // Identifier/collection parameter values are stored data, not executed expressions.
        if (ctx.queryParameter() != null) {
            visit(ctx.queryParameter());
        }
        return null;
    }

    @Override
    public Void visitSetTimeZoneStmt(SetTimeZoneStmtContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects
            .instanceObject(TargetType.ConfigKey, ctx.TIME().getSymbol(), ctx.ZONE().getSymbol(), "session_timezone"));
        return null;
    }

    @Override
    public Void visitSetRoleStmt(SetRoleStmtContext ctx) {
        setType(SplitQueryType.SWITCH_ROLE);
        if (ctx.ALL() != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.ALL().getSymbol()));
        } else if (ctx.NONE() != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.NONE().getSymbol()));
        } else if (ctx.DEFAULT() != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.DEFAULT().getSymbol()));
        }
        if (ctx.roleNameList() != null) {
            BehaviorAction action = BehaviorAction.SWITCH;
            if (ctx.EXCEPT() != null) {
                // Excluded roles are resolved by name, but are not activated.
                action = BehaviorAction.READ;
            }
            for (RoleNameContext role : ctx.roleNameList().roleName()) {
                add(SplitQueryType.SWITCH_ROLE, action, objects.instanceObject(TargetType.Role, role, name(role)));
            }
        }
        return null;
    }

    @Override
    public Void visitExecuteAsStmt(ExecuteAsStmtContext ctx) {
        ParserRuleContext user = ctx.identifier();
        if (user == null) {
            user = ctx.stringLiteral();
        }
        add(SplitQueryType.SWITCH_USER, BehaviorAction.SWITCH, objects.instanceObject(TargetType.User, user, name(user)));
        if (ctx.executeAsBody() != null) {
            visit(ctx.executeAsBody());
        }
        return null;
    }

    @Override
    public Void visitShowSettingsStmt(ShowSettingsStmtContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.SETTINGS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowSettingStmt(ShowSettingStmtContext ctx) {
        ParserRuleContext key = ctx.identifier();
        if (key == null) {
            key = ctx.stringLiteral();
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, key, name(key)));
        return null;
    }

    @Override
    public Void visitShowRolesStmt(ShowRolesStmtContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Role, ctx.ROLES().getSymbol()));
        return null;
    }

    @Override
    public Void visitQueryParameter(QueryParameterContext ctx) {
        // The type subtree describes the bound value; Array/Tuple/Nullable are not calls.
        add(SplitQueryType.SESSION_VARIABLE_RW, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, ctx.getChild(1).getText()));
        return null;
    }

    protected BehaviorAction functionAction(ParserRuleContext ctx) {
        return BehaviorAction.CALL;
    }

    @Override
    public Void visitColumnExprFunction(ColumnExprFunctionContext ctx) {
        add(SplitQueryType.SELECT, functionAction(ctx), objects.object(TargetType.Function, ctx.identifier(), List.of(name(ctx.identifier()))));
        String function = name(ctx.identifier());
        if (ChPlanningFunctions.readsSetting(function) && ctx.columnArgList() != null && !ctx.columnArgList().columnArgExpr().isEmpty()) {
            ColumnExprContext argument = ctx.columnArgList().columnArgExpr(0).columnExpr();
            if (argument instanceof ColumnExprLiteralContext literal && literal.literal().stringLiteral() != null) {
                StringLiteralContext key = literal.literal().stringLiteral();
                add(SplitQueryType.SESSION_VARIABLE_RW, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, key, name(key)));
            } else if (argument != null) {
                add(SplitQueryType.SESSION_VARIABLE_RW, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, argument));
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitColumnExprWinFunction(ColumnExprWinFunctionContext ctx) {
        add(SplitQueryType.SELECT, functionAction(ctx), objects.object(TargetType.Function, ctx.identifier(), List.of(name(ctx.identifier()))));
        return visitChildren(ctx);
    }

    @Override
    public Void visitColumnExprWinFunctionTarget(ColumnExprWinFunctionTargetContext ctx) {
        IdentifierContext function = ctx.identifier(0);
        add(SplitQueryType.SELECT, functionAction(ctx), objects.object(TargetType.Function, function, List.of(name(function))));
        return visitChildren(ctx);
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
        BehaviorObject table = object(TargetType.Table, ctx.tableIdentifier());
        boolean hasAlter = false;
        // Mixed mutation/maintenance clauses retain the owning ALTER classification.
        if (ctx.alterTableClause().stream().anyMatch(clause ->
                !(clause instanceof AlterTableClauseUpdateContext) && !(clause instanceof AlterTableClauseDeleteContext))) {
            setType(SplitQueryType.ALTER_TABLE);
        }
        for (AlterTableClauseContext clause : ctx.alterTableClause()) {
            if (clause instanceof AlterTableClauseUpdateContext) {
                add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table, tableSources(clause));
            } else if (clause instanceof AlterTableClauseDeleteContext) {
                add(SplitQueryType.DELETE, BehaviorAction.DELETE, table, tableSources(clause));
            } else if (!maintenanceClause(clause, table)) {
                hasAlter = true;
            }
        }
        if (hasAlter) {
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, table, tableSources(ctx));
        }
        return null;
    }

    protected boolean maintenanceClause(AlterTableClauseContext clause, BehaviorObject table) {
        return false;
    }

    @Override
    public Void visitInsertStmt(InsertStmtContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        addTableSources(sources, ctx.dataClause());
        int relationCount = behavior.getRelations().size();
        add(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, ctx.tableIdentifier()), sources);
        if (ctx.dataClause() instanceof DataClauseValuesContext values && behavior.getRelations().size() > relationCount) {
            BehaviorRelation relation = behavior.getRelations().get(behavior.getRelations().size() - 1);
            relation.setInsertRows((long) values.assignmentValues().size());
        }
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
        DatabaseIdentifierContext database = ctx.databaseIdentifier();
        QueryParameterContext parameter = database.identifier().queryParameter();
        if (parameter == null) {
            add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, object(TargetType.Schema, database));
        } else {
            add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, objects.unnamedObject(TargetType.Schema, parameter, UmiTypes.Catalog));
            visit(parameter);
        }
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

    protected final BehaviorObject object(TargetType type, ParserRuleContext context) {
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

    protected final String name(ParserRuleContext context) {
        String value = parser.getTokenStream().getText(context.getStart(), context.getStop()).trim();
        char quote = value.charAt(0);
        if (value.length() < 2 || (quote != '`' && quote != '"' && quote != '\'') || value.charAt(value.length() - 1) != quote) {
            return value;
        }
        ByteArrayOutputStream decoded = new ByteArrayOutputStream();
        for (int i = 1; i < value.length() - 1; i++) {
            char ch = value.charAt(i);
            if (ch == quote && i + 1 < value.length() - 1 && value.charAt(i + 1) == quote) {
                i++;
            } else if (ch == '\\' && i + 1 < value.length() - 1) {
                char escaped = value.charAt(++i);
                if (escaped == 'N') {
                    continue;
                }
                if (escaped == 'x' && i + 2 < value.length() - 1 && Character.digit(value.charAt(i + 1), 16) >= 0 && Character.digit(value.charAt(i + 2), 16) >= 0) {
                    decoded.write(Character.digit(value.charAt(i + 1), 16) * 16 + Character.digit(value.charAt(i + 2), 16));
                    i += 2;
                    continue;
                }
                ch = switch (escaped) {
                    case '0' -> '\0';
                    case 'a' -> '\7';
                    case 'b' -> '\b';
                    case 'e' -> '\33';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 'v' -> '\13';
                    default -> {
                        if (escaped > 31 && "\\'\"`/=".indexOf(escaped) < 0) {
                            decoded.write('\\');
                        }
                        yield escaped;
                    }
                };
            }
            if (Character.isHighSurrogate(ch) && i + 1 < value.length() - 1 && Character.isLowSurrogate(value.charAt(i + 1))) {
                decoded.writeBytes(value.substring(i, i + 2).getBytes(StandardCharsets.UTF_8));
                i++;
            } else {
                decoded.writeBytes(String.valueOf(ch).getBytes(StandardCharsets.UTF_8));
            }
        }
        return decoded.toString(StandardCharsets.UTF_8);
    }

    protected final void add(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        add(type, action, subject, List.of());
    }

    protected final void add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        declareInstanceScope(subject);
        relation.setSubject(subject);
        relation.setAction(action);
        for (BehaviorObject target : targets) {
            declareInstanceScope(target);
            addObject(relation.getTarget(), target);
        }
        behavior.getRelations().add(relation);
        setType(type);
    }

    protected final String instanceRelativeName(BehaviorObject object) {
        String path = object.getObjectPath();
        if (path.equals(instancePath)) {
            return null;
        }
        return path.substring(instancePath.length(), path.length() - 1);
    }

    private void declareInstanceScope(BehaviorObject object) {
        if (object == null || object.getObjectName() != null) {
            return;
        }
        switch (object.getObjectType()) {
            case User, Role, UserOrRole, ConfigKey, File -> object.setObjectName(new ObjectName(null, null, instanceRelativeName(object)));
            default -> {
            }
        }
    }

    protected final void setType(SplitQueryType type) {
        // Resource actions in an executed body must not replace its owning statement's kind.
        // Only a SELECT's explicit setting/parameter access refines the query category.
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || (behavior.getStatementType() == SplitQueryType.SELECT && type == SplitQueryType.SESSION_VARIABLE_RW)) {
            behavior.setStatementType(type);
        }
    }

    private void addObject(List<BehaviorObject> objects, BehaviorObject object) {
        if (object != null) {
            objects.add(object);
        }
    }

    protected final <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
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

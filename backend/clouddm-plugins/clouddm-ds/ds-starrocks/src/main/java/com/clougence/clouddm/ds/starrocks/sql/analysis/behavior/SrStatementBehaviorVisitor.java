/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.starrocks.sql.analysis.behavior;

import static com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksBaseVisitor;
import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser;
import com.clougence.clouddm.ds.starrocks.sql.parser.SrDslProvider;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

class SrStatementBehaviorVisitor extends StarRocksBaseVisitor<Void> {
    private final String                   currentSchema;
    protected final Parser                   parser;
    protected final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    SrStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.currentSchema = java.util.Objects.toString(levels == null ? null : levels.get(UmiTypes.Schema), "");
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitQueryStatement(QueryStatementContext ctx) {
        if (isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.SELECT);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitTableAtom(TableAtomContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateDbStatement(CreateDbStatementContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitDropDbStatement(DropDbStatementContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.database));
        return null;
    }

    @Override
    public Void visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        QualifiedNameContext database = ctx.qualifiedName();
        BehaviorObject schema = object(TargetType.Schema, database);
        if (database.identifier().size() == 2) {
            BehaviorObject catalog = object(TargetType.Catalog, database.identifier(0));
            schema.setObjectPath(catalog.getObjectPath() + name(database.identifier(1)) + "/");
            add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, catalog);
        }
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, schema);
        return null;
    }

    @Override
    public Void visitSetCatalogStatement(SetCatalogStatementContext ctx) {
        add(SplitQueryType.SWITCH_CATALOG, BehaviorAction.SWITCH, object(TargetType.Catalog, ctx.identifierOrString()));
        return null;
    }

    @Override
    public Void visitSetSystemVar(SetSystemVarContext ctx) {
        IdentifierContext key = ctx.identifier();
        VarTypeContext scope = ctx.varType();
        if (ctx.systemVariable() != null) {
            key = ctx.systemVariable().identifier();
            scope = ctx.systemVariable().varType();
        }
        SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
        if (scope != null && scope.GLOBAL() != null) {
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        add(type, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, key, name(key).toLowerCase(Locale.ROOT)));
        // Only the value is an expression; visiting the assignment target would invent a read.
        return visit(ctx.setExprOrDefault());
    }

    @Override
    public Void visitSetUserVar(SetUserVarContext ctx) {
        IdentifierOrStringContext key = ctx.userVariable().identifierOrString();
        add(SplitQueryType.SESSION_VARIABLE_RW, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, key, name(key)));
        return visit(ctx.expression());
    }

    @Override
    public Void visitSetNames(SetNamesContext ctx) {
        // StarRocks 3.5.21 accepts SetNamesVar but SetExecutor performs no configuration writes.
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.SESSION_SETTING_WRITE);
        }
        return null;
    }

    @Override
    public Void visitUserVariable(UserVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, name(ctx.identifierOrString())));
        return null;
    }

    @Override
    public Void visitSystemVariable(SystemVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, name(ctx.identifier()).toLowerCase(Locale.ROOT)));
        return null;
    }

    @Override
    public Void visitAggregationFunction(AggregationFunctionContext ctx) {
        Token function = ctx.getStart();
        add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, function, List.of(function.getText())));
        return visitChildren(ctx);
    }

    @Override
    public Void visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.VARIABLES().getSymbol()));
        return null;
    }

    @Override
    public Void visitSetRoleStatement(SetRoleStatementContext ctx) {
        if (ctx.ALL() != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.ALL().getSymbol()));
        } else if (ctx.NONE() != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.NONE().getSymbol()));
        } else if (ctx.DEFAULT() != null) {
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.DEFAULT().getSymbol()));
        }
        if (ctx.roleList() != null) {
            BehaviorAction action = BehaviorAction.SWITCH;
            if (ctx.EXCEPT() != null) {
                // Excluded roles are looked up and validated, but are not activated.
                action = BehaviorAction.READ;
            }
            for (IdentifierOrStringContext role : ctx.roleList().identifierOrString()) {
                add(SplitQueryType.SWITCH_ROLE, action, objects.instanceObject(TargetType.Role, role, name(role)));
            }
        }
        return null;
    }

    @Override
    public Void visitExecuteAsStatement(ExecuteAsStatementContext ctx) {
        add(SplitQueryType.SWITCH_USER, BehaviorAction.SWITCH, userIdentity(ctx.user()));
        return null;
    }

    @Override
    public Void visitShowProcesslistStatement(ShowProcesslistStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.PROCESSLIST().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowRunningQueriesStatement(ShowRunningQueriesStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.QUERIES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowProcStatement(ShowProcStatementContext ctx) {
        String path = name(ctx.path);
        if ("/current_queries".equals(path) || "/global_current_queries".equals(path)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.PROC().getSymbol()));
        } else if ("/compactions".equals(path)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.PROC().getSymbol(), "compaction"));
        } else {
            String kind = switch (path) {
                case "/frontends" -> "frontend";
                case "/backends" -> "backend";
                case "/compute_nodes" -> "compute_node";
                case "/brokers" -> "broker";
                default -> null;
            };
            if (kind != null) {
                TargetType type = TargetType.ClusterNode;
                if ("broker".equals(kind)) {
                    type = TargetType.Broker;
                }
                add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(type, ctx.PROC().getSymbol(), kind));
            }
        }
        return null;
    }

    @Override
    public Void visitShowStatusStatement(ShowStatusStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.STATUS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowWarningStatement(ShowWarningStatementContext ctx) {
        Token keyword;
        if (ctx.WARNINGS() != null) {
            keyword = ctx.WARNINGS().getSymbol();
        } else {
            keyword = ctx.ERRORS().getSymbol();
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, keyword));
        return null;
    }

    @Override
    public Void visitKillStatement(KillStatementContext ctx) {
        BehaviorObject target;
        if (ctx.QUERY() == null) {
            if (ctx.connId == null) {
                // Native AstBuilder rejects string IDs for connection termination.
                return null;
            }
            target = objects.instanceObject(TargetType.Instance, ctx.connId);
        } else if (ctx.queryId != null) {
            target = objects.instanceObject(TargetType.Query, ctx.queryId, name(ctx.queryId));
        } else {
            // A numeric ID identifies a connection; the query UUID is unknown.
            target = objects.instanceObject(TargetType.Query, ctx.connId);
        }
        add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, target);
        return null;
    }

    @Override
    public Void visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        add(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        add(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        add(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitCreateTableStatement(CreateTableStatementContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        if (!ctx.qualifiedName().isEmpty()) {
            List<BehaviorObject> sources = ctx.qualifiedName().size() > 1 ? List.of(object(TargetType.Table, ctx.qualifiedName(1))) : List.of();
            add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.qualifiedName(0)), sources);
        }
        return null;
    }

    @Override
    public Void visitDropTableStatement(DropTableStatementContext ctx) {
        add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitAlterTableStatement(AlterTableStatementContext ctx) {
        boolean compaction = false;
        for (AlterClauseContext clause : ctx.alterClause()) {
            if (clause.compactionClause() != null) {
                compaction = true;
                CompactionClauseContext compact = clause.compactionClause();
                List<IdentifierContext> partitions = new ArrayList<>();
                if (compact.identifier() != null) {
                    partitions.add(compact.identifier());
                } else if (compact.identifierList() != null) {
                    partitions.addAll(compact.identifierList().identifier());
                }
                BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
                if (partitions.isEmpty()) {
                    add(SplitQueryType.ALTER_TABLE, BehaviorAction.OPTIMIZE, table);
                }
                for (IdentifierContext partition : partitions) {
                    BehaviorObject subject = objects.instanceObject(TargetType.Partition, partition);
                    subject.setObjectPath(table.getObjectPath() + name(partition) + "/");
                    add(SplitQueryType.ALTER_TABLE, BehaviorAction.OPTIMIZE, subject);
                }
            }
        }
        if (!compaction || ctx.alterClause().stream().anyMatch(clause -> clause.compactionClause() == null)) {
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName()));
        }
        return null;
    }

    @Override
    public Void visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateViewStatement(CreateViewStatementContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitAlterViewStatement(AlterViewStatementContext ctx) {
        add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitDropViewStatement(DropViewStatementContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
        return null;
    }

    @Override
    public Void visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        if (isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        if (ctx.explainDesc() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()), tableSources(ctx.queryStatement()));
            return null;
        }
        SplitQueryType type = ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        BehaviorRelation relation = add(type, type == SplitQueryType.INSERT ? BehaviorAction.INSERT : BehaviorAction.MERGE, object(TargetType.Table, ctx
            .qualifiedName()), tableSources(ctx.queryStatement()));
        if (relation != null && !ctx.expressionsWithDefault().isEmpty()) {
            relation.setInsertRows((long) ctx.expressionsWithDefault().size());
        }
        return null;
    }

    @Override
    public Void visitUpdateStatement(UpdateStatementContext ctx) {
        if (isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.expression());
        if (ctx.explainDesc() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()), sources);
            return null;
        }
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.qualifiedName()), sources);
        return null;
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext ctx) {
        if (isExplainAnalyze(ctx.explainDesc())) {
            addUnsafe(ctx);
            return null;
        }
        List<BehaviorObject> sources = tableSources(ctx.relations());
        addTableSources(sources, ctx.expression());
        if (ctx.explainDesc() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()), sources);
            return null;
        }
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.qualifiedName()), sources);
        return null;
    }

    @Override
    public Void visitCreateUserStatement(CreateUserStatementContext ctx) {
        BehaviorObject user = userIdentity(ctx.user());
        List<BehaviorObject> roles = roleObjects(ctx.roleList());
        add(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, user, roles);
        // CREATE USER DEFAULT ROLE grants membership as well as configuring defaults.
        for (BehaviorObject role : roles) {
            add(SplitQueryType.CREATE_USER, BehaviorAction.GRANT, role, List.of(user));
        }
        if (ctx.properties() != null) {
            addUserProperties(ctx.properties().property(), name(userName(ctx.user())), SplitQueryType.CREATE_USER);
        }
        return null;
    }

    @Override
    public Void visitAlterUserStatement(AlterUserStatementContext ctx) {
        if (ctx.properties() != null) {
            IdentifierOrStringContext user = userName(ctx.user());
            // UserProperty is shared by username, independently of authentication host.
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, objects.instanceObject(TargetType.User, user, name(user)));
            addUserProperties(ctx.properties().property(), name(user), SplitQueryType.ALTER_USER);
        } else {
            List<BehaviorObject> roles = roleObjects(ctx.roleList());
            if (ctx.ALL() != null) {
                roles.add(objects.instanceObject(TargetType.Role, ctx.ALL().getSymbol()));
            }
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, userIdentity(ctx.user()), roles);
        }
        return null;
    }

    @Override
    public Void visitSetDefaultRoleStatement(SetDefaultRoleStatementContext ctx) {
        List<BehaviorObject> roles = roleObjects(ctx.roleList());
        if (ctx.ALL() != null) {
            roles.add(objects.instanceObject(TargetType.Role, ctx.ALL().getSymbol()));
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, userIdentity(ctx.user()), roles);
        return null;
    }

    @Override
    public Void visitSetPassword(SetPasswordContext ctx) {
        BehaviorObject user = objects.instanceObject(TargetType.User, ctx.getStart());
        if (ctx.user() != null) {
            user = userIdentity(ctx.user());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user);
        return null;
    }

    @Override
    public Void visitSetUserPropertyStatement(SetUserPropertyStatementContext ctx) {
        String username = null;
        BehaviorObject user = objects.instanceObject(TargetType.User, ctx.PROPERTY().getSymbol());
        if (ctx.FOR() != null) {
            username = name(ctx.string());
            user = objects.instanceObject(TargetType.User, ctx.string(), username);
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user);
        addUserProperties(ctx.userPropertyList().property(), username, SplitQueryType.ALTER_USER);
        return null;
    }

    @Override
    public Void visitDropUserStatement(DropUserStatementContext ctx) {
        add(SplitQueryType.DROP_USER, BehaviorAction.DROP, userIdentity(ctx.user()));
        return null;
    }

    private List<BehaviorObject> roleObjects(RoleListContext ctx) {
        List<BehaviorObject> roles = new ArrayList<>();
        if (ctx != null) {
            for (IdentifierOrStringContext role : ctx.identifierOrString()) {
                roles.add(objects.instanceObject(TargetType.Role, role, name(role)));
            }
        }
        return roles;
    }

    private IdentifierOrStringContext userName(UserContext ctx) {
        return ctx.getRuleContext(IdentifierOrStringContext.class, 0);
    }

    private BehaviorObject userIdentity(UserContext user) {
        String identity;
        if (user instanceof UserWithHostAndBlanketContext domain) {
            identity = "'" + name(domain.identifierOrString(0)) + "'@['" + name(domain.identifierOrString(1)) + "']";
        } else if (user instanceof UserWithHostContext host) {
            identity = "'" + name(host.identifierOrString(0)) + "'@'" + name(host.identifierOrString(1)) + "'";
        } else {
            identity = "'" + name(userName(user)) + "'@'%'";
        }
        return objects.instanceObject(TargetType.User, user, identity);
    }

    private void addUserProperties(List<PropertyContext> properties, String username, SplitQueryType type) {
        String catalog = null;
        for (PropertyContext property : properties) {
            String key = name(property.key);
            if ("catalog".equalsIgnoreCase(key) || "session.catalog".equalsIgnoreCase(key)) {
                catalog = name(property.value);
            }
        }
        for (PropertyContext property : properties) {
            String key = name(property.key).toLowerCase(Locale.ROOT);
            String value = name(property.value);
            BehaviorObject config = objects.instanceObject(TargetType.ConfigKey, property.key);
            if (username != null) {
                config = objects.instanceObject(TargetType.ConfigKey, property.key, username + "/" + key);
            }
            List<BehaviorObject> targets = new ArrayList<>();
            if (!value.isEmpty()) {
                if ("catalog".equals(key) || "session.catalog".equals(key)) {
                    targets.add(objects.instanceObject(TargetType.Catalog, property.value, value));
                } else if ("database".equals(key)) {
                    // Without an explicit catalog, the user's stored catalog is unknown.
                    BehaviorObject schema = objects.instanceObject(TargetType.Schema, property.value);
                    if (catalog != null && !catalog.isEmpty()) {
                        schema.setObjectPath(schema.getObjectPath() + catalog + "/" + value + "/");
                    }
                    targets.add(schema);
                }
            }
            add(type, BehaviorAction.CONFIGURE, config, targets);
        }
    }

    @Override
    public Void visitCreateRoleStatement(CreateRoleStatementContext ctx) {
        for (BehaviorObject role : roleObjects(ctx.roleList())) {
            add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, role);
        }
        return null;
    }

    @Override
    public Void visitAlterRoleStatement(AlterRoleStatementContext ctx) {
        for (BehaviorObject role : roleObjects(ctx.roleList())) {
            add(SplitQueryType.COMMENT_ROLE, BehaviorAction.ALTER, role);
        }
        return null;
    }

    @Override
    public Void visitDropRoleStatement(DropRoleStatementContext ctx) {
        for (BehaviorObject role : roleObjects(ctx.roleList())) {
            add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, role);
        }
        return null;
    }

    private void addRoleMembership(IdentifierOrStringListContext roles, BehaviorObject recipient, BehaviorAction action) {
        SplitQueryType type = SplitQueryType.GRANT;
        if (action == BehaviorAction.REVOKE) {
            type = SplitQueryType.REVOKE;
        }
        for (IdentifierOrStringContext role : roles.identifierOrString()) {
            add(type, action, objects.instanceObject(TargetType.Role, role, name(role)), List.of(recipient));
        }
    }

    private Void addPrivileges(ParserRuleContext ctx, BehaviorAction action) {
        SplitQueryType type = SplitQueryType.GRANT;
        if (action == BehaviorAction.REVOKE) {
            type = SplitQueryType.REVOKE;
        }
        GrantRevokeClauseContext clause = ctx.getRuleContext(GrantRevokeClauseContext.class, 0);
        BehaviorObject recipient;
        if (clause.user() != null) {
            recipient = userIdentity(clause.user());
        } else {
            recipient = objects.instanceObject(TargetType.Role, clause.identifierOrString(), name(clause.identifierOrString()));
        }
        List<BehaviorObject> subjects = new ArrayList<>();
        PrivObjectNameListContext names = ctx.getRuleContext(PrivObjectNameListContext.class, 0);
        PrivFunctionObjectNameListContext functions = ctx.getRuleContext(PrivFunctionObjectNameListContext.class, 0);
        PrivObjectTypePluralContext allType = ctx.getRuleContext(PrivObjectTypePluralContext.class, 0);
        if (ctx.getToken(SYSTEM, 0) != null) {
            subjects.add(objects.instanceObject(TargetType.Instance, ctx.getToken(SYSTEM, 0).getSymbol()));
        } else if (!ctx.getRuleContexts(UserContext.class).isEmpty()) {
            for (UserContext user : ctx.getRuleContexts(UserContext.class)) {
                subjects.add(userIdentity(user));
            }
        } else if (functions != null) {
            subjects.addAll(functionPrivileges(functions, ctx.getToken(GLOBAL, 0) != null));
        } else if (allType != null) {
            BehaviorObject subject = privilegeSet(ctx, allType);
            if (subject != null) {
                subjects.add(subject);
            }
        } else if (names != null) {
            PrivObjectTypeContext objectType = ctx.getRuleContext(PrivObjectTypeContext.class, 0);
            TargetType targetType = TargetType.Table;
            if (objectType != null) {
                targetType = privilegeObjectType(objectType.getText());
            }
            if (targetType == null) {
                return null;
            }
            for (PrivObjectNameContext objectName : names.privObjectName()) {
                subjects.add(privilegeObject(targetType, objectName));
            }
        }
        for (BehaviorObject subject : subjects) {
            add(type, action, subject, List.of(recipient));
        }
        return null;
    }

    private List<BehaviorObject> functionPrivileges(PrivFunctionObjectNameListContext functions, boolean global) {
        List<BehaviorObject> subjects = new ArrayList<>();
        for (QualifiedNameContext function : functions.qualifiedName()) {
            String functionName = name(function.identifier(function.identifier().size() - 1));
            if (global) {
                subjects.add(objects.instanceObject(TargetType.Function, function, functionName));
                continue;
            }
            // Database UDF privileges are resolved by the native local metastore.
            String database = currentSchema;
            if (function.identifier().size() > 1) {
                database = name(function.identifier(0));
            }
            BehaviorObject subject = objects.instanceObject(TargetType.Function, function);
            String path = subject.getObjectPath() + "default_catalog/";
            if (!database.isEmpty()) {
                path += database + "/" + functionName + "/";
            }
            subject.setObjectPath(path);
            subjects.add(subject);
        }
        return subjects;
    }

    private BehaviorObject privilegeSet(ParserRuleContext ctx, PrivObjectTypePluralContext allType) {
        TargetType type = privilegeObjectType(allType.getText());
        if (type == null) {
            return null;
        }
        Token start = ctx.getToken(ALL, 0).getSymbol();
        Token stop = allType.getStop();
        IdentifierOrStringContext database = ctx.getRuleContext(IdentifierOrStringContext.class, 0);
        BehaviorObject subject = objects.instanceObject(type, start);
        if (type == TargetType.Schema || type == TargetType.Table || type == TargetType.View
            || type == TargetType.Materialized || (type == TargetType.Function && allType.GLOBAL() == null)) {
            subject = objects.unnamedObject(type, start, UmiTypes.Catalog);
            if (type == TargetType.Function) {
                subject.setObjectPath(objects.instanceObject(type, start).getObjectPath() + "default_catalog/");
            }
            if (database != null) {
                subject.setObjectPath(subject.getObjectPath() + name(database) + "/");
                stop = database.getStop();
            } else if (ctx.getToken(DATABASES, 0) != null) {
                stop = ctx.getToken(DATABASES, 0).getSymbol();
            }
        }
        BehaviorObject range = objects.instanceObject(type, start, stop, "");
        subject.setEndLine(range.getEndLine());
        subject.setEndColumn(range.getEndColumn());
        return subject;
    }

    private TargetType privilegeObjectType(String value) {
        return switch (value.toUpperCase(Locale.ROOT)) {
            case "CATALOG", "CATALOGS" -> TargetType.Catalog;
            case "DATABASE", "DATABASES" -> TargetType.Schema;
            case "TABLE", "TABLES" -> TargetType.Table;
            case "VIEW", "VIEWS" -> TargetType.View;
            case "MATERIALIZEDVIEW", "MATERIALIZEDVIEWS" -> TargetType.Materialized;
            case "FUNCTIONS", "GLOBALFUNCTIONS" -> TargetType.Function;
            case "RESOURCE", "RESOURCES" -> TargetType.Resource;
            case "RESOURCEGROUP", "RESOURCEGROUPS" -> TargetType.ResourceGroup;
            case "STORAGEVOLUME", "STORAGEVOLUMES" -> TargetType.StorageVolume;
            case "USERS" -> TargetType.User;
            default -> null;
        };
    }

    private BehaviorObject privilegeObject(TargetType type, PrivObjectNameContext ctx) {
        List<String> names = new ArrayList<>();
        for (IdentifierOrStringOrStarContext identifier : ctx.identifierOrStringOrStar()) {
            names.add(name(identifier));
        }
        boolean wildcard = names.contains("*");
        if (type == TargetType.Catalog || type == TargetType.Resource || type == TargetType.ResourceGroup || type == TargetType.StorageVolume) {
            if (wildcard) {
                return objects.instanceObject(type, ctx);
            }
            return objects.instanceObject(type, ctx, names.get(0));
        }
        if (wildcard) {
            BehaviorObject subject = objects.unnamedObject(type, ctx, UmiTypes.Catalog);
            if (type != TargetType.Schema && names.size() == 2 && !"*".equals(names.get(0))) {
                subject.setObjectPath(subject.getObjectPath() + names.get(0) + "/");
            }
            return subject;
        }
        BehaviorObject subject = objects.object(type, ctx, names);
        if (type == TargetType.Schema && names.size() == 2) {
            subject.setObjectPath(objects.instanceObject(type, ctx).getObjectPath() + String.join("/", names) + "/");
        }
        return subject;
    }

    @Override
    public Void visitShowUserStatement(ShowUserStatementContext ctx) {
        Token keyword = ctx.getStop();
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.User, keyword));
        return null;
    }

    @Override
    public Void visitShowAllAuthentication(ShowAllAuthenticationContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.User, ctx.AUTHENTICATION().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowAuthenticationForUser(ShowAuthenticationForUserContext ctx) {
        BehaviorObject user = objects.instanceObject(TargetType.User, ctx.AUTHENTICATION().getSymbol());
        if (ctx.user() != null) {
            user = userIdentity(ctx.user());
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, user);
        return null;
    }

    @Override
    public Void visitShowRolesStatement(ShowRolesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Role, ctx.ROLES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        BehaviorObject subject = objects.instanceObject(TargetType.User, ctx.GRANTS().getSymbol());
        if (ctx.user() != null) {
            subject = userIdentity(ctx.user());
        } else if (ctx.identifierOrString() != null) {
            subject = objects.instanceObject(TargetType.Role, ctx.identifierOrString(), name(ctx.identifierOrString()));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowUserPropertyStatement(ShowUserPropertyStatementContext ctx) {
        Token keyword;
        if (ctx.PROPERTY() != null) {
            keyword = ctx.PROPERTY().getSymbol();
        } else {
            keyword = ctx.PROPERTIES().getSymbol();
        }
        BehaviorObject properties = objects.instanceObject(TargetType.ConfigKey, keyword);
        if (ctx.FOR() != null) {
            properties = objects.instanceObject(TargetType.ConfigKey, keyword, name(ctx.string(0)));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, properties);
        return null;
    }

    @Override
    public Void visitCreateSecurityIntegrationStatement(CreateSecurityIntegrationStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE,
            objects.instanceObject(TargetType.SecurityIntegration, ctx.identifier(), name(ctx.identifier())), integrationProviders(ctx.properties().property()));
        return null;
    }

    @Override
    public Void visitAlterSecurityIntegrationStatement(AlterSecurityIntegrationStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER,
            objects.instanceObject(TargetType.SecurityIntegration, ctx.identifier(), name(ctx.identifier())), integrationProviders(ctx.propertyList().property()));
        return null;
    }

    private List<BehaviorObject> integrationProviders(List<PropertyContext> properties) {
        List<BehaviorObject> providers = new ArrayList<>();
        for (PropertyContext property : properties) {
            if ("group_provider".equals(name(property.key))) {
                for (String provider : name(property.value).split(",\\s*")) {
                    if (!provider.isBlank()) {
                        providers.add(objects.instanceObject(TargetType.GroupProvider, property.value, provider));
                    }
                }
            }
        }
        return providers;
    }

    @Override
    public Void visitCreateGroupProviderStatement(CreateGroupProviderStatementContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        boolean fileProvider = ctx.properties().property().stream()
            .anyMatch(property -> "type".equals(name(property.key)) && "file".equals(name(property.value)));
        if (fileProvider) {
            for (PropertyContext property : ctx.properties().property()) {
                if ("group_file_url".equals(name(property.key))) {
                    String path = name(property.value);
                    BehaviorObject file = objects.instanceObject(TargetType.File, property.value);
                    // A relative filename depends on STARROCKS_HOME; do not invent an absolute server path.
                    if (path.startsWith("http://") || path.startsWith("https://")) {
                        file = objects.instanceObject(TargetType.File, property.value, path);
                    }
                    targets.add(file);
                }
            }
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE,
            objects.instanceObject(TargetType.GroupProvider, ctx.identifier(), name(ctx.identifier())), targets);
        return null;
    }

    @Override
    public Void visitGrantRoleToUser(GrantRoleToUserContext ctx) {
        addRoleMembership(ctx.identifierOrStringList(), userIdentity(ctx.user()), BehaviorAction.GRANT);
        return null;
    }

    @Override
    public Void visitGrantRoleToRole(GrantRoleToRoleContext ctx) {
        addRoleMembership(ctx.identifierOrStringList(), objects.instanceObject(TargetType.Role, ctx.identifierOrString(), name(ctx.identifierOrString())), BehaviorAction.GRANT);
        return null;
    }

    @Override
    public Void visitRevokeRoleFromUser(RevokeRoleFromUserContext ctx) {
        addRoleMembership(ctx.identifierOrStringList(), userIdentity(ctx.user()), BehaviorAction.REVOKE);
        return null;
    }

    @Override
    public Void visitRevokeRoleFromRole(RevokeRoleFromRoleContext ctx) {
        addRoleMembership(ctx.identifierOrStringList(), objects.instanceObject(TargetType.Role, ctx.identifierOrString(), name(ctx.identifierOrString())), BehaviorAction.REVOKE);
        return null;
    }

    @Override
    public Void visitGrantOnUser(GrantOnUserContext ctx) {
        return addPrivileges(ctx, BehaviorAction.GRANT);
    }

    @Override
    public Void visitGrantOnSystem(GrantOnSystemContext ctx) {
        return addPrivileges(ctx, BehaviorAction.GRANT);
    }

    @Override
    public Void visitGrantOnTableBrief(GrantOnTableBriefContext ctx) {
        return addPrivileges(ctx, BehaviorAction.GRANT);
    }

    @Override
    public Void visitGrantOnFunc(GrantOnFuncContext ctx) {
        return addPrivileges(ctx, BehaviorAction.GRANT);
    }

    @Override
    public Void visitGrantOnPrimaryObj(GrantOnPrimaryObjContext ctx) {
        return addPrivileges(ctx, BehaviorAction.GRANT);
    }

    @Override
    public Void visitGrantOnAll(GrantOnAllContext ctx) {
        return addPrivileges(ctx, BehaviorAction.GRANT);
    }

    @Override
    public Void visitRevokeOnUser(RevokeOnUserContext ctx) {
        return addPrivileges(ctx, BehaviorAction.REVOKE);
    }

    @Override
    public Void visitRevokeOnSystem(RevokeOnSystemContext ctx) {
        return addPrivileges(ctx, BehaviorAction.REVOKE);
    }

    @Override
    public Void visitRevokeOnTableBrief(RevokeOnTableBriefContext ctx) {
        return addPrivileges(ctx, BehaviorAction.REVOKE);
    }

    @Override
    public Void visitRevokeOnFunc(RevokeOnFuncContext ctx) {
        return addPrivileges(ctx, BehaviorAction.REVOKE);
    }

    @Override
    public Void visitRevokeOnPrimaryObj(RevokeOnPrimaryObjContext ctx) {
        return addPrivileges(ctx, BehaviorAction.REVOKE);
    }

    @Override
    public Void visitRevokeOnAll(RevokeOnAllContext ctx) {
        return addPrivileges(ctx, BehaviorAction.REVOKE);
    }

    @Override
    public Void visitDropSecurityIntegrationStatement(DropSecurityIntegrationStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.SecurityIntegration, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    @Override
    public Void visitShowSecurityIntegrationStatement(ShowSecurityIntegrationStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.SecurityIntegration, ctx.INTEGRATIONS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowCreateSecurityIntegrationStatement(ShowCreateSecurityIntegrationStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.SecurityIntegration, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    @Override
    public Void visitDropGroupProviderStatement(DropGroupProviderStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.GroupProvider, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    @Override
    public Void visitShowGroupProvidersStatement(ShowGroupProvidersStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.GroupProvider, ctx.PROVIDERS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowCreateGroupProviderStatement(ShowCreateGroupProviderStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.GroupProvider, ctx.identifier(), name(ctx.identifier())));
        return null;
    }


    @Override
    public Void visitAdminSetConfigStatement(AdminSetConfigStatementContext ctx) {
        PropertyContext property = ctx.property();
        String key = name(property.key).toLowerCase(Locale.ROOT);
        List<BehaviorObject> integrations = new ArrayList<>();
        if ("authentication_chain".equals(key)) {
            for (String integration : name(property.value).split(",")) {
                integration = integration.trim();
                if (!integration.isEmpty() && !"native".equals(integration)) {
                    integrations.add(objects.instanceObject(TargetType.SecurityIntegration, property.value, integration));
                }
            }
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE,
            objects.instanceObject(TargetType.ConfigKey, property.key, key), integrations);
        return null;
    }

    @Override
    public Void visitAdminShowConfigStatement(AdminShowConfigStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.CONFIG().getSymbol()));
        return null;
    }

    private BehaviorObject clusterNode(StringContext address, String kind) {
        String endpoint = name(address);
        int separator = endpoint.lastIndexOf(':');
        if (separator >= 0) {
            endpoint = endpoint.substring(0, separator) + "/" + endpoint.substring(separator + 1);
        }
        return objects.instanceObject(TargetType.ClusterNode, address, kind + "/" + endpoint);
    }

    private Void clusterMembers(List<StringContext> addresses, String kind, BehaviorAction action) {
        for (StringContext address : addresses) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, action, clusterNode(address, kind));
        }
        return null;
    }

    @Override
    public Void visitModifyFrontendHostClause(ModifyFrontendHostClauseContext ctx) {
        // HOST omits the port: retain the known address range instead of inventing an endpoint.
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.RENAME,
            objects.instanceObject(TargetType.ClusterNode, ctx.string(0), "frontend/" + name(ctx.string(0))),
            List.of(objects.instanceObject(TargetType.ClusterNode, ctx.string(1), "frontend/" + name(ctx.string(1)))));
        return null;
    }

    @Override
    public Void visitModifyBackendClause(ModifyBackendClauseContext ctx) {
        if (ctx.HOST() != null) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.RENAME,
                objects.instanceObject(TargetType.ClusterNode, ctx.string(0), "backend/" + name(ctx.string(0))),
                List.of(objects.instanceObject(TargetType.ClusterNode, ctx.string(1), "backend/" + name(ctx.string(1)))));
        } else {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, clusterNode(ctx.string(0), "backend"));
        }
        return null;
    }

    @Override
    public Void visitModifyBrokerClause(ModifyBrokerClauseContext ctx) {
        String brokerName = "broker/" + name(ctx.identifierOrString());
        BehaviorObject broker = objects.instanceObject(TargetType.Broker, ctx.identifierOrString(), brokerName);
        if (ctx.ALL() != null) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, broker);
            return null;
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, broker);
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.DROP() != null) {
            action = BehaviorAction.DROP;
        }
        for (StringContext address : ctx.string()) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, action, clusterNode(address, brokerName), List.of(broker));
        }
        return null;
    }

    @Override
    public Void visitCreateResourceGroupStatement(CreateResourceGroupStatementContext ctx) {
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.REPLACE() != null) {
            action = BehaviorAction.REPLACE;
        }
        add(SplitQueryType.CREATE_RESOURCE_GROUP, action,
            objects.instanceObject(TargetType.ResourceGroup, ctx.identifier(), name(ctx.identifier())), classifierTargets(ctx.classifier()));
        return null;
    }

    @Override
    public Void visitAlterResourceGroupStatement(AlterResourceGroupStatementContext ctx) {
        add(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER,
            objects.instanceObject(TargetType.ResourceGroup, ctx.identifier(), name(ctx.identifier())), classifierTargets(ctx.classifier()));
        return null;
    }

    private List<BehaviorObject> classifierTargets(List<ClassifierContext> classifiers) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (ClassifierContext classifier : classifiers) {
            for (ComparisonContext comparison : descendants(classifier, ComparisonContext.class)) {
                if (!"=".equals(comparison.comparisonOperator().getText())) {
                    continue;
                }
                List<ColumnRefContext> keys = descendants(comparison.left, ColumnRefContext.class);
                List<StringContext> values = descendants(comparison.right, StringContext.class);
                if (keys.size() != 1 || values.size() != 1) {
                    continue;
                }
                String key = name(keys.get(0)).toLowerCase(Locale.ROOT);
                StringContext value = values.get(0);
                switch (key) {
                    case "user" -> targets.add(objects.instanceObject(TargetType.User, value, name(value)));
                    case "role" -> targets.add(objects.instanceObject(TargetType.Role, value, name(value)));
                    case "db" -> {
                        for (String database : name(value).split(",")) {
                            targets.add(objects.instanceObject(TargetType.Schema, value, "default_catalog/" + database.trim()));
                        }
                    }
                    default -> {
                        // Query types, IP ranges and cost limits are classifier values, not resources.
                    }
                }
            }
        }
        return targets;
    }

    @Override
    public Void visitShowResourceGroupStatement(ShowResourceGroupStatementContext ctx) {
        BehaviorObject group = objects.instanceObject(TargetType.ResourceGroup, ctx.getStop());
        if (ctx.identifier() != null) {
            group = objects.instanceObject(TargetType.ResourceGroup, ctx.identifier(), name(ctx.identifier()));
        } else {
            group = objects.instanceObject(TargetType.ResourceGroup, ctx.GROUPS().getSymbol());
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, group);
        return null;
    }

    @Override
    public Void visitShowResourceGroupUsageStatement(ShowResourceGroupUsageStatementContext ctx) {
        BehaviorObject group;
        if (ctx.identifier() != null) {
            group = objects.instanceObject(TargetType.ResourceGroup, ctx.identifier(), name(ctx.identifier()));
        } else {
            group = objects.instanceObject(TargetType.ResourceGroup, ctx.GROUPS().getSymbol());
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, group);
        return null;
    }

    private List<BehaviorObject> resourceTargets(PropertiesContext properties) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (properties == null) {
            return targets;
        }
        for (PropertyContext property : properties.property()) {
            switch (name(property.key)) {
                case "broker" -> targets.add(objects.instanceObject(TargetType.Broker, property.value, "broker/" + name(property.value)));
                case "working_dir", "driver_url" -> targets.add(fileLocation(property.value));
                default -> {
                    // Connection settings and credentials do not declare local database objects.
                }
            }
        }
        return targets;
    }

    private BehaviorObject fileLocation(ParserRuleContext location) {
        String path = name(location);
        // Local absolute paths are appended below the instance; preserve URL scheme separators.
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.isEmpty()) {
            return objects.instanceObject(TargetType.File, location);
        }
        return objects.instanceObject(TargetType.File, location, path);
    }

    @Override
    public Void visitCreateResourceStatement(CreateResourceStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE,
            objects.instanceObject(TargetType.Resource, ctx.resourceName, name(ctx.resourceName)), resourceTargets(ctx.properties()));
        return null;
    }

    @Override
    public Void visitAlterResourceStatement(AlterResourceStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER,
            objects.instanceObject(TargetType.Resource, ctx.resourceName, name(ctx.resourceName)), resourceTargets(ctx.properties()));
        return null;
    }

    @Override
    public Void visitCreateStorageVolumeStatement(CreateStorageVolumeStatementContext ctx) {
        List<BehaviorObject> locations = new ArrayList<>();
        for (StringContext location : ctx.locationsDesc().stringList().string()) {
            locations.add(fileLocation(location));
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE,
            objects.instanceObject(TargetType.StorageVolume, ctx.storageVolumeName, name(ctx.storageVolumeName)), locations);
        return null;
    }

    @Override
    public Void visitSetDefaultStorageVolumeStatement(SetDefaultStorageVolumeStatementContext ctx) {
        BehaviorObject instance = objects.instanceObject(TargetType.Instance, ctx.DEFAULT().getSymbol());
        BehaviorObject end = objects.instanceObject(TargetType.Instance, ctx.VOLUME().getSymbol());
        instance.setEndLine(end.getEndLine());
        instance.setEndColumn(end.getEndColumn());
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, instance,
            List.of(objects.instanceObject(TargetType.StorageVolume, ctx.identifierOrString(), name(ctx.identifierOrString()))));
        return null;
    }

    @Override
    public Void visitInstallPluginStatement(InstallPluginStatementContext ctx) {
        // The installed name comes from the manifest, not the archive's filename.
        add(SplitQueryType.CREATE_LIBRARY, BehaviorAction.CREATE,
            objects.instanceObject(TargetType.Library, ctx.PLUGIN().getSymbol()), List.of(fileLocation(ctx.identifierOrString())));
        return null;
    }

    private BehaviorObject smallFile(Token anchor, QualifiedNameContext database, PropertiesContext properties, String filename) {
        String db = currentSchema;
        if (database != null) {
            db = name(database);
        }
        BehaviorObject file = objects.instanceObject(TargetType.File, anchor);
        String path = file.getObjectPath() + "default_catalog/";
        if (!db.isEmpty()) {
            path += db + "/";
            if (properties != null) {
                for (PropertyContext property : properties.property()) {
                    if ("catalog".equals(name(property.key))) {
                        path += name(property.value) + "/" + filename + "/";
                        break;
                    }
                }
            }
        }
        file.setObjectPath(path);
        return file;
    }

    @Override
    public Void visitCreateFileStatement(CreateFileStatementContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        for (PropertyContext property : ctx.properties().property()) {
            if ("url".equals(name(property.key))) {
                sources.add(fileLocation(property.value));
            }
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.IMPORT,
            smallFile(ctx.string().getStart(), ctx.catalog, ctx.properties(), name(ctx.string())), sources);
        return null;
    }

    @Override
    public Void visitDropFileStatement(DropFileStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP,
            smallFile(ctx.string().getStart(), ctx.catalog, ctx.properties(), name(ctx.string())));
        return null;
    }

    @Override
    public Void visitShowSmallFilesStatement(ShowSmallFilesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, smallFile(ctx.FILE().getSymbol(), ctx.catalog, null, null));
        return null;
    }

    @Override
    public Void visitAddSqlBlackListStatement(AddSqlBlackListStatementContext ctx) {
        // Native SQL blacklist IDs are generated at execution time.
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE,
            objects.instanceObject(TargetType.Policy, ctx.string(), "sql_blacklist"));
        return null;
    }

    private Void blacklistEntries(ParserRuleContext ctx, boolean backend, BehaviorAction action) {
        String namespace = "sql_blacklist/";
        if (backend) {
            namespace = "backend_blacklist/";
        }
        for (org.antlr.v4.runtime.tree.TerminalNode id : ctx.getTokens(INTEGER_VALUE)) {
            List<BehaviorObject> targets = List.of();
            if (backend) {
                targets = List.of(objects.instanceObject(TargetType.ClusterNode, id.getSymbol(), "backend/id/" + id.getText()));
            }
            add(SplitQueryType.SYSTEM_SETTING_WRITE, action,
                objects.instanceObject(TargetType.Policy, id.getSymbol(), namespace + id.getText()), targets);
        }
        return null;
    }

    @Override
    public Void visitAddFrontendClause(AddFrontendClauseContext ctx) {
        return clusterMembers(List.of(ctx.string()), "frontend", BehaviorAction.CREATE);
    }

    @Override
    public Void visitDropFrontendClause(DropFrontendClauseContext ctx) {
        return clusterMembers(List.of(ctx.string()), "frontend", BehaviorAction.DROP);
    }

    @Override
    public Void visitAddBackendClause(AddBackendClauseContext ctx) {
        return clusterMembers(ctx.string(), "backend", BehaviorAction.CREATE);
    }

    @Override
    public Void visitDropBackendClause(DropBackendClauseContext ctx) {
        return clusterMembers(ctx.string(), "backend", BehaviorAction.DROP);
    }

    @Override
    public Void visitDecommissionBackendClause(DecommissionBackendClauseContext ctx) {
        return clusterMembers(ctx.string(), "backend", BehaviorAction.CONFIGURE);
    }

    @Override
    public Void visitCancelAlterSystemStatement(CancelAlterSystemStatementContext ctx) {
        return clusterMembers(ctx.string(), "backend", BehaviorAction.CONFIGURE);
    }

    @Override
    public Void visitAddComputeNodeClause(AddComputeNodeClauseContext ctx) {
        return clusterMembers(ctx.string(), "compute_node", BehaviorAction.CREATE);
    }

    @Override
    public Void visitDropComputeNodeClause(DropComputeNodeClauseContext ctx) {
        return clusterMembers(ctx.string(), "compute_node", BehaviorAction.DROP);
    }

    @Override
    public Void visitDropResourceGroupStatement(DropResourceGroupStatementContext ctx) {
        add(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP, objects.instanceObject(TargetType.ResourceGroup, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    @Override
    public Void visitDropResourceStatement(DropResourceStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.Resource, ctx.resourceName, name(ctx.resourceName)));
        return null;
    }

    @Override
    public Void visitAlterStorageVolumeStatement(AlterStorageVolumeStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, objects.instanceObject(TargetType.StorageVolume, ctx.identifierOrString(), name(ctx.identifierOrString())));
        return null;
    }

    @Override
    public Void visitDropStorageVolumeStatement(DropStorageVolumeStatementContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.StorageVolume, ctx.storageVolumeName, name(ctx.storageVolumeName)));
        return null;
    }

    @Override
    public Void visitDescStorageVolumeStatement(DescStorageVolumeStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.StorageVolume, ctx.identifierOrString(), name(ctx.identifierOrString())));
        return null;
    }

    @Override
    public Void visitUninstallPluginStatement(UninstallPluginStatementContext ctx) {
        add(SplitQueryType.DROP_LIBRARY, BehaviorAction.DROP, objects.instanceObject(TargetType.Library, ctx.identifierOrString(), name(ctx.identifierOrString())));
        return null;
    }

    @Override
    public Void visitShowFrontendsStatement(ShowFrontendsStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ClusterNode, ctx.FRONTENDS().getSymbol(), "frontend"));
        return null;
    }

    @Override
    public Void visitShowBackendsStatement(ShowBackendsStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ClusterNode, ctx.BACKENDS().getSymbol(), "backend"));
        return null;
    }

    @Override
    public Void visitShowComputeNodesStatement(ShowComputeNodesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ClusterNode, ctx.COMPUTE().getSymbol(), "compute_node"));
        return null;
    }

    @Override
    public Void visitShowBrokerStatement(ShowBrokerStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Broker, ctx.BROKER().getSymbol(), "broker"));
        return null;
    }

    @Override
    public Void visitShowResourceStatement(ShowResourceStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Resource, ctx.RESOURCES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowStorageVolumesStatement(ShowStorageVolumesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.StorageVolume, ctx.VOLUMES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowPluginsStatement(ShowPluginsStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Library, ctx.PLUGINS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowSqlBlackListStatement(ShowSqlBlackListStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.SQLBLACKLIST().getSymbol(), "sql_blacklist"));
        return null;
    }

    @Override
    public Void visitShowBackendBlackListStatement(ShowBackendBlackListStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.BLACKLIST().getSymbol(), "backend_blacklist"));
        return null;
    }

    @Override
    public Void visitDelSqlBlackListStatement(DelSqlBlackListStatementContext ctx) {
        return blacklistEntries(ctx, false, BehaviorAction.DROP);
    }

    @Override
    public Void visitAddBackendBlackListStatement(AddBackendBlackListStatementContext ctx) {
        return blacklistEntries(ctx, true, BehaviorAction.CREATE);
    }

    @Override
    public Void visitDelBackendBlackListStatement(DelBackendBlackListStatementContext ctx) {
        return blacklistEntries(ctx, true, BehaviorAction.DROP);
    }


    private List<BehaviorObject> maintenanceTargets(QualifiedNameContext tableName, PartitionNamesContext partitions, TargetType type) {
        BehaviorObject table = object(type, tableName);
        if (partitions == null) {
            return List.of(table);
        }
        TargetType partitionType = type;
        if (type == TargetType.Table) {
            partitionType = TargetType.Partition;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        for (IdentifierOrStringContext name : partitions.identifierOrString()) {
            BehaviorObject partition = objects.instanceObject(partitionType, name);
            partition.setObjectPath(table.getObjectPath() + name(name) + "/");
            targets.add(partition);
        }
        if (targets.isEmpty()) {
            // Key/value partition selectors do not provide a partition name.
            BehaviorObject range = objects.instanceObject(partitionType, partitions);
            range.setObjectPath(table.getObjectPath());
            targets.add(range);
        }
        return targets;
    }

    @Override
    public Void visitAnalyzeStatement(AnalyzeStatementContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN_TABLE;
        if (ctx.partitionNames() != null) {
            type = SplitQueryType.ADMIN_PARTITION;
        } else if (ctx.analyzeColumnClause() instanceof MultiColumnSetContext) {
            type = SplitQueryType.ADMIN_PERFORMANCE;
        }
        for (BehaviorObject target : maintenanceTargets(ctx.tableName().qualifiedName(), ctx.partitionNames(), TargetType.Table)) {
            add(type, BehaviorAction.ANALYZE, target);
        }
        return null;
    }

    @Override
    public Void visitAnalyzeHistogramStatement(AnalyzeHistogramStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ANALYZE, object(TargetType.Table, ctx.histogramStatement().tableName().qualifiedName()));
        return null;
    }

    @Override
    public Void visitDropStatsStatement(DropStatsStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        String internalCatalog = objects.instanceObject(TargetType.Instance, ctx.getStart()).getObjectPath() + "default_catalog/";
        if (table.getObjectPath().startsWith(internalCatalog)) {
            BehaviorObject stats = object(TargetType.Statistics, ctx.qualifiedName());
            stats.setObjectPath(table.getObjectPath() + "statistics/multiple_columns/");
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, stats);
        }
        if (ctx.MULTIPLE() == null) {
            BehaviorObject stats = object(TargetType.Statistics, ctx.qualifiedName());
            stats.setObjectPath(table.getObjectPath() + "statistics/basic/");
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, stats);
        }
        return null;
    }

    @Override
    public Void visitDropHistogramStatement(DropHistogramStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName(0));
        String internalCatalog = objects.instanceObject(TargetType.Instance, ctx.getStart()).getObjectPath() + "default_catalog/";
        if (table.getObjectPath().startsWith(internalCatalog)) {
            // 3.5.21 removes all histograms for an internal table, irrespective of its column list.
            BehaviorObject stats = object(TargetType.Statistics, ctx.qualifiedName(0));
            stats.setObjectPath(table.getObjectPath() + "statistics/histogram/");
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, stats);
        } else {
            for (QualifiedNameContext column : ctx.qualifiedName().subList(1, ctx.qualifiedName().size())) {
                BehaviorObject stats = objects.instanceObject(TargetType.Statistics, column);
                String columnName = String.join(".", column.identifier().stream().map(this::name).toList());
                stats.setObjectPath(table.getObjectPath() + "statistics/histogram/" + columnName + "/");
                add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, stats);
            }
        }
        return null;
    }

    @Override
    public Void visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        if (ctx.STATUS() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.ANALYZE().getSymbol(), "analyze"));
        }
        return null;
    }

    @Override
    public Void visitKillAnalyzeStatement(KillAnalyzeStatementContext ctx) {
        BehaviorObject job;
        if (ctx.INTEGER_VALUE() != null) {
            job = objects.instanceObject(TargetType.Job, ctx.INTEGER_VALUE().getSymbol(), "analyze/" + ctx.INTEGER_VALUE().getText());
        } else {
            job = objects.instanceObject(TargetType.Job, ctx.PENDING().getSymbol(), "analyze");
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.TERMINATE, job);
        return null;
    }

    @Override
    public Void visitAdminRepairTableStatement(AdminRepairTableStatementContext ctx) {
        boolean dryRun = false;
        boolean lakeRecovery = false;
        if (ctx.properties() != null) {
            for (PropertyContext property : ctx.properties().property()) {
                String key = name(property.key);
                if ("dry_run".equals(key)) {
                    dryRun = Boolean.parseBoolean(name(property.value));
                }
                if (List.of("dry_run", "enforce_consistent_version", "allow_empty_tablet_recovery").contains(key)) {
                    lakeRecovery = true;
                }
            }
        }
        SplitQueryType type = SplitQueryType.ADMIN_TABLE;
        BehaviorAction action = BehaviorAction.REPAIR;
        if (ctx.partitionNames() != null) {
            type = SplitQueryType.ADMIN_PARTITION;
        }
        if (dryRun) {
            type = SplitQueryType.PERFORMANCE;
            action = BehaviorAction.READ;
        }
        for (BehaviorObject target : maintenanceTargets(ctx.qualifiedName(), ctx.partitionNames(), TargetType.Table)) {
            add(type, action, target);
            if (lakeRecovery && !dryRun) {
                add(type, BehaviorAction.UNSAFE, target);
            }
        }
        return null;
    }

    @Override
    public Void visitAdminCancelRepairTableStatement(AdminCancelRepairTableStatementContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN_TABLE;
        if (ctx.partitionNames() != null) {
            type = SplitQueryType.ADMIN_PARTITION;
        }
        // This removes urgent priority; it does not stop automatic replica repair.
        for (BehaviorObject target : maintenanceTargets(ctx.qualifiedName(), ctx.partitionNames(), TargetType.Table)) {
            add(type, BehaviorAction.CONFIGURE, target);
        }
        return null;
    }

    @Override
    public Void visitAdminCheckTabletsStatement(AdminCheckTabletsStatementContext ctx) {
        for (org.antlr.v4.runtime.tree.TerminalNode id : ctx.tabletList().INTEGER_VALUE()) {
            add(SplitQueryType.ADMIN, BehaviorAction.CHECKSUM, objects.instanceObject(TargetType.Tablet, id.getSymbol(), "tablet/" + id.getText()));
        }
        return null;
    }

    @Override
    public Void visitAdminSetReplicaStatusStatement(AdminSetReplicaStatusStatementContext ctx) {
        StringContext tablet = null;
        StringContext backend = null;
        for (PropertyContext property : ctx.properties().property()) {
            if ("tablet_id".equals(name(property.key))) {
                tablet = property.value;
            } else if ("backend_id".equals(name(property.key))) {
                backend = property.value;
            }
        }
        if (tablet != null && backend != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.Replica, tablet, "tablet/" + name(tablet) + "/backend/" + name(backend)),
                List.of(objects.instanceObject(TargetType.ClusterNode, backend, "backend/id/" + name(backend))));
        }
        return null;
    }

    @Override
    public Void visitAdminSetPartitionVersion(AdminSetPartitionVersionContext ctx) {
        BehaviorObject partition;
        String suffix;
        if (ctx.partitionName != null) {
            partition = objects.instanceObject(TargetType.Partition, ctx.partitionName);
            suffix = name(ctx.partitionName);
        } else {
            partition = objects.instanceObject(TargetType.Partition, ctx.partitionId);
            suffix = "id/" + ctx.partitionId.getText();
        }
        partition.setObjectPath(object(TargetType.Table, ctx.qualifiedName()).getObjectPath() + suffix + "/");
        add(SplitQueryType.ADMIN_PARTITION, BehaviorAction.CONFIGURE, partition);
        add(SplitQueryType.ADMIN_PARTITION, BehaviorAction.UNSAFE, partition);
        return null;
    }

    @Override
    public Void visitShowTabletStatement(ShowTabletStatementContext ctx) {
        if (ctx.INTEGER_VALUE() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
                objects.instanceObject(TargetType.Tablet, ctx.INTEGER_VALUE().getSymbol(), "tablet/" + ctx.INTEGER_VALUE().getText()));
        } else {
            for (BehaviorObject target : maintenanceTargets(ctx.qualifiedName(), ctx.partitionNames(), TargetType.Tablet)) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, target);
            }
        }
        return null;
    }

    @Override
    public Void visitCancelCompactionStatement(CancelCompactionStatementContext ctx) {
        for (ComparisonContext comparison : descendants(ctx.expression(), ComparisonContext.class)) {
            if ("TXN_ID".equalsIgnoreCase(comparison.left.getText()) && "=".equals(comparison.comparisonOperator().getText())) {
                List<NumericLiteralContext> ids = descendants(comparison.right, NumericLiteralContext.class);
                if (ids.size() == 1) {
                    NumericLiteralContext id = ids.get(0);
                    add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Job, id, "compaction/txn/" + id.getText()));
                }
            }
        }
        return null;
    }

    @Override
    public Void visitRefreshTableStatement(RefreshTableStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        if (ctx.string().isEmpty()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.REFRESH, table);
        }
        for (StringContext name : ctx.string()) {
            BehaviorObject partition = objects.instanceObject(TargetType.Partition, name);
            partition.setObjectPath(table.getObjectPath() + name(name) + "/");
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.REFRESH, partition);
        }
        return null;
    }

    @Override
    public Void visitRefreshMaterializedViewStatement(RefreshMaterializedViewStatementContext ctx) {
        BehaviorObject mv = object(TargetType.Materialized, ctx.mvName);
        if (ctx.PARTITION() == null) {
            add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, mv);
        } else {
            ParserRuleContext selector = ctx.partitionRangeDesc();
            if (selector == null) {
                selector = ctx.listPartitionValues();
            }
            BehaviorObject partitions = objects.instanceObject(TargetType.Partition, ctx.PARTITION().getSymbol(), selector.getStop(), "");
            partitions.setObjectPath(mv.getObjectPath());
            add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, partitions);
        }
        return null;
    }

    @Override
    public Void visitCancelRefreshMaterializedViewStatement(CancelRefreshMaterializedViewStatementContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.mvName);
        job.setObjectPath(job.getObjectPath() + "refresh/");
        add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, job);
        return null;
    }

    @Override
    public Void visitCreateDataCacheRuleStatement(CreateDataCacheRuleStatementContext ctx) {
        BehaviorObject policy = objects.instanceObject(TargetType.Policy, ctx.DATACACHE().getSymbol(), ctx.RULE().getSymbol(), "datacache");
        BehaviorObject target = objects.instanceObject(TargetType.Table, ctx.dataCacheTarget());
        String path = target.getObjectPath();
        for (IdentifierOrStringOrStarContext part : ctx.dataCacheTarget().identifierOrStringOrStar()) {
            if (part.ASTERISK_SYMBOL() != null) {
                break;
            }
            path += name(part) + "/";
        }
        target.setObjectPath(path);
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CREATE, policy, List.of(target));
        return null;
    }

    @Override
    public Void visitDataCacheSelectStatement(DataCacheSelectStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.LOAD, table);
        addCacheHints(ctx);
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.READ, table);
        // Only executable select items and predicates, not properties or hint text.
        for (SelectItemContext item : ctx.selectItem()) {
            visit(item);
        }
        if (ctx.where != null) {
            visit(ctx.where);
        }
        return null;
    }

    private void addCacheHints(DataCacheSelectStatementContext ctx) {
        addHints(ctx.SELECT().getSymbol(), ctx.selectItem(0).getStart(), null, SplitQueryType.ADMIN_PERFORMANCE);
    }

    protected void addHints(Token start, Token stop, String configPrefix, SplitQueryType type) {
        // Only optimizer tokens between the designated keyword and its executable body.
        for (int i = start.getTokenIndex() + 1; i < stop.getTokenIndex(); i++) {
            Token hint = parser.getTokenStream().get(i);
            if (hint.getType() != OPTIMIZER_HINT) {
                continue;
            }
            String body = hint.getText().substring(3, hint.getText().length() - 2);
            Lexer lexer = SrDslProvider.INSTANCE.createLexer(CharStreams.fromString(body));
            StarRocksParser hintParser = (StarRocksParser) SrDslProvider.INSTANCE.createParser(lexer);
            hintParser.removeErrorListeners();
            hintParser.setErrorHandler(new BailErrorStrategy());
            TokenStream tokens = hintParser.getTokenStream();
            if (!"SET_VAR".equalsIgnoreCase(tokens.LT(1).getText())) {
                continue;
            }
            tokens.consume();
            if (!"(".equals(tokens.LT(1).getText())) {
                continue;
            }
            tokens.consume();
            List<HintMapContext> settings = new ArrayList<>();
            try {
                settings.add(hintParser.hintMap());
                while (",".equals(tokens.LT(1).getText())) {
                    tokens.consume();
                    settings.add(hintParser.hintMap());
                }
            } catch (org.antlr.v4.runtime.misc.ParseCancellationException ignored) {
                // Malformed hints do not become partial configuration writes.
                continue;
            }
            if (!")".equals(tokens.LT(1).getText())) {
                continue;
            }
            for (HintMapContext setting : settings) {
                Token key = setting.k.getStart();
                CommonToken anchor = new CommonToken(key);
                anchor.setLine(hint.getLine() + key.getLine() - 1);
                if (key.getLine() == 1) {
                    anchor.setCharPositionInLine(hint.getCharPositionInLine() + 3 + key.getCharPositionInLine());
                }
                String name = setting.k.getText();
                if (name.length() >= 2 && (name.startsWith("`") || name.startsWith("'") || name.startsWith("\""))) {
                    String quote = name.substring(0, 1);
                    name = name.substring(1, name.length() - 1).replace(quote + quote, quote);
                }
                BehaviorObject config = objects.instanceObject(TargetType.ConfigKey, anchor, name.toLowerCase(Locale.ROOT));
                if (configPrefix != null) {
                    config.setObjectPath(configPrefix + name.toLowerCase(Locale.ROOT) + "/");
                }
                add(type, BehaviorAction.CONFIGURE, config);
            }
        }
    }

    @Override
    public Void visitShowStatsMetaStatement(ShowStatsMetaStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Statistics, ctx.STATS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowHistogramMetaStatement(ShowHistogramMetaStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Statistics, ctx.HISTOGRAM().getSymbol()));
        return null;
    }

    @Override
    public Void visitCleanTabletSchedQClause(CleanTabletSchedQClauseContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, objects.instanceObject(TargetType.Queue, ctx.QUEUE().getSymbol(), "tablet_scheduler"));
        return null;
    }

    @Override
    public Void visitDropDataCacheRuleStatement(DropDataCacheRuleStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.INTEGER_VALUE().getSymbol(), "datacache/" + ctx.INTEGER_VALUE().getText()));
        return null;
    }

    @Override
    public Void visitClearDataCacheRulesStatement(ClearDataCacheRulesStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.RULES().getSymbol(), "datacache"));
        return null;
    }

    @Override
    public Void visitShowDataCacheRulesStatement(ShowDataCacheRulesStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.RULES().getSymbol(), "datacache"));
        return null;
    }

    @Override
    public Void visitSyncStatement(SyncStatementContext ctx) {
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.READ, objects.instanceObject(TargetType.Replication, ctx.SYNC().getSymbol()));
        return null;
    }

    @Override
    public Void visitAdminShowReplicaDistributionStatement(AdminShowReplicaDistributionStatementContext ctx) {
        for (BehaviorObject target : maintenanceTargets(ctx.qualifiedName(), ctx.partitionNames(), TargetType.Replica)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, target);
        }
        return null;
    }

    @Override
    public Void visitAdminShowReplicaStatusStatement(AdminShowReplicaStatusStatementContext ctx) {
        for (BehaviorObject target : maintenanceTargets(ctx.qualifiedName(), ctx.partitionNames(), TargetType.Replica)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, target);
        }
        return null;
    }

    @Override
    public Void visitAdminShowTabletStatusStatement(AdminShowTabletStatusStatementContext ctx) {
        for (BehaviorObject target : maintenanceTargets(ctx.qualifiedName(), ctx.partitionNames(), TargetType.Tablet)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, target);
        }
        return null;
    }

    private boolean isExplainAnalyze(ExplainDescContext ctx) {
        return ctx != null && ctx.ANALYZE() != null;
    }

    private void addUnsafe(ParserRuleContext ctx) {
        add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, objects.instanceObject(TargetType.Instance, ctx.getStart()));
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        for (TableAtomContext source : descendants(tree, TableAtomContext.class)) {
            BehaviorObject object = object(TargetType.Table, source.qualifiedName());
            if (object != null) {
                result.add(object);
            }
        }
    }

    protected BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (context instanceof QualifiedNameContext qualified) {
            for (IdentifierContext identifier : qualified.identifier()) {
                names.add(name(identifier));
            }
        } else {
            names.add(name(context));
        }
        return objects.object(type, context, names);
    }

    protected String name(ParserRuleContext context) {
        String value = parser.getTokenStream().getText(context.getStart(), context.getStop()).trim();
        if (value.length() >= 2 && ((value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`') || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"')
                                    || (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\''))) {
            String quote = value.substring(0, 1);
            return value.substring(1, value.length() - 1).replace(quote + quote, quote);
        }
        return value;
    }

    protected BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        return add(type, action, subject, List.of());
    }

    protected BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return null;
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
        return relation;
    }

    protected <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
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

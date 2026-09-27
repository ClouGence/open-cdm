/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4my.analysis.behavior;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForMySqlParser;
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
            add(SplitQueryType.SWITCH_ROLE, action, identity(TargetType.Role, role));
        }
        return null;
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        for (UserAuthOptionContext option : ctx.userAuthOption()) {
            add(SplitQueryType.CREATE_USER, BehaviorAction.CREATE,
                identity(TargetType.User, option.getRuleContext(UserNameContext.class, 0)));
        }
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        for (UserSpecificationContext user : ctx.userSpecification()) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, identity(TargetType.User, user.userName()));
        }
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        if (ctx.CURRENT_USER() != null) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, currentUser(ctx.CURRENT_USER(), ctx.RR_BRACKET()));
            return null;
        }
        for (UserAuthOptionContext option : ctx.userAuthOption()) {
            BehaviorObject user = identity(TargetType.User, option.getRuleContext(UserNameContext.class, 0));
            if (ctx.userLockOption().isEmpty() || !(option instanceof SimpleAuthOptionContext)
                || ctx.REQUIRE() != null || !ctx.userResourceOption().isEmpty() || !ctx.userPasswordOption().isEmpty()) {
                add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user);
            }
            for (UserLockOptionContext lock : ctx.userLockOption()) {
                BehaviorAction action = BehaviorAction.LOCK;
                if (lock.UNLOCK() != null) {
                    action = BehaviorAction.UNLOCK;
                }
                add(SplitQueryType.ALTER_USER, action, user);
            }
        }
        return null;
    }

    @Override
    public Void visitSetPasswordStatement(SetPasswordStatementContext ctx) {
        BehaviorObject user = objects.instanceObject(TargetType.User, ctx.PASSWORD().getSymbol());
        if (ctx.userName() != null) {
            user = identity(TargetType.User, ctx.userName());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user);
        // PASSWORD(string) is authentication syntax, not an expression function call.
        return null;
    }

    @Override
    public Void visitRenameUser(RenameUserContext ctx) {
        for (RenameUserClauseContext rename : ctx.renameUserClause()) {
            add(SplitQueryType.RENAME_USER, BehaviorAction.RENAME, identity(TargetType.User, rename.fromFirst),
                List.of(identity(TargetType.User, rename.toFirst)));
        }
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        for (UserNameContext user : ctx.userName()) {
            add(SplitQueryType.DROP_USER, BehaviorAction.DROP, identity(TargetType.User, user));
        }
        return null;
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, identity(TargetType.Role, role));
        }
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, identity(TargetType.Role, role));
        }
        return null;
    }

    @Override
    public Void visitSetDefaultRole(SetDefaultRoleContext ctx) {
        List<BehaviorObject> roles = defaultRoles(ctx.defaultRoleClause());
        for (UserNameContext user : ctx.userName()) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, identity(TargetType.User, user), roles);
        }
        return null;
    }

    @Override
    public Void visitAlterUserDefaultRole(AlterUserDefaultRoleContext ctx) {
        BehaviorObject user;
        if (ctx.userName() != null) {
            user = identity(TargetType.User, ctx.userName());
        } else {
            user = currentUser(ctx.CURRENT_USER(), ctx.RR_BRACKET());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user, defaultRoles(ctx.defaultRoleClause()));
        return null;
    }

    private List<BehaviorObject> defaultRoles(DefaultRoleClauseContext ctx) {
        if (ctx.ALL() != null) {
            return List.of(objects.instanceObject(TargetType.Role, ctx.ALL().getSymbol()));
        }
        return ctx.roleName().stream().map(role -> identity(TargetType.Role, role)).toList();
    }

    @Override
    public Void visitGrantStatement(GrantStatementContext ctx) {
        if (ctx.privilegeLevel() == null) {
            List<BehaviorObject> recipients = roleRecipients(ctx);
            for (RoleNameContext role : ctx.roleName()) {
                add(SplitQueryType.GRANT, BehaviorAction.GRANT, identity(TargetType.Role, role), recipients);
            }
        } else {
            List<BehaviorObject> recipients = ctx.userAuthOption().stream()
                .map(option -> identity(TargetType.UserOrRole, option.getRuleContext(UserNameContext.class, 0))).toList();
            add(SplitQueryType.GRANT, BehaviorAction.GRANT, privilegeScope(ctx.privilegeLevel(), ctx.privilegeObject), recipients);
            // BP7 also changes an existing account's password when credentials are supplied.
            for (UserAuthOptionContext option : ctx.userAuthOption()) {
                if (changesPassword(option)) {
                    add(SplitQueryType.GRANT, BehaviorAction.ALTER,
                        identity(TargetType.UserOrRole, option.getRuleContext(UserNameContext.class, 0)));
                }
            }
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(RevokeStatementContext ctx) {
        List<BehaviorObject> recipients = roleRecipients(ctx);
        if (ctx.privilegeLevel() != null) {
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, privilegeScope(ctx.privilegeLevel(), ctx.privilegeObject), recipients);
        } else if (ctx.ALL() != null) {
            // No ON clause: remove privileges across the tenant, not role memberships.
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE,
                unnamedRange(TargetType.Instance, ctx.ALL().getSymbol(), ctx.OPTION().getSymbol()), recipients);
        } else {
            for (RoleNameContext role : ctx.roleName()) {
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, identity(TargetType.Role, role), recipients);
            }
        }
        return null;
    }

    private List<BehaviorObject> roleRecipients(ParserRuleContext ctx) {
        List<BehaviorObject> recipients = new ArrayList<>();
        // Both grammar alternatives can occur in one list; preserve their source order.
        for (ParseTree child : ctx.children) {
            if (child instanceof UserNameContext user) {
                recipients.add(identity(TargetType.UserOrRole, user));
            } else if (child instanceof UidContext uid) {
                recipients.add(identity(TargetType.UserOrRole, uid));
            }
        }
        return recipients;
    }

    private boolean changesPassword(UserAuthOptionContext ctx) {
        if (ctx instanceof StringAuthOptionContext) {
            return true;
        }
        if (ctx instanceof HashAuthOptionContext hash) {
            return !unquote(hash.hashed.getText()).isEmpty();
        }
        if (ctx instanceof ModuleAuthOptionContext module) {
            for (AuthenticationRuleContext rule : module.authenticationRule()) {
                if (rule instanceof ModuleContext auth && auth.BY() != null && auth.STRING_LITERAL() != null) {
                    if (auth.PASSWORD() == null || !unquote(auth.STRING_LITERAL().getText()).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private BehaviorObject privilegeScope(PrivilegeLevelContext ctx, Token kind) {
        if (ctx instanceof GlobalPrivLevelContext) {
            return objects.instanceObject(TargetType.Instance, ctx);
        }
        if (ctx instanceof CurrentSchemaPriviLevelContext) {
            return objects.unnamedObject(TargetType.Schema, ctx, UmiTypes.Schema);
        }
        if (ctx instanceof DefiniteSchemaPrivLevelContext schema) {
            return objects.object(TargetType.Schema, ctx, List.of(unquote(text(schema.uid()))));
        }
        TargetType type = TargetType.Table;
        if (kind != null && kind.getType() == ObForMySqlParser.FUNCTION) {
            type = TargetType.Function;
        } else if (kind != null && kind.getType() == ObForMySqlParser.PROCEDURE) {
            type = TargetType.Procedure;
        }
        List<String> names = descendants(ctx, UidContext.class).stream().map(this::text).map(this::unquote).toList();
        return objects.object(type, ctx, names);
    }

    @Override
    public Void visitShowGrants(ShowGrantsContext ctx) {
        BehaviorObject user;
        if (ctx.userName() != null) {
            user = identity(TargetType.UserOrRole, ctx.userName());
        } else if (ctx.CURRENT_USER() != null) {
            user = currentUser(ctx.CURRENT_USER(), ctx.RR_BRACKET());
        } else {
            user = objects.instanceObject(TargetType.User, ctx.GRANTS().getSymbol());
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, user);
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, identity(TargetType.Role, role));
        }
        return null;
    }

    @Override
    public Void visitShowCreateUser(ShowCreateUserContext ctx) {
        BehaviorObject user;
        if (ctx.userName() != null) {
            user = identity(TargetType.User, ctx.userName());
        } else {
            user = currentUser(ctx.CURRENT_USER(), ctx.RR_BRACKET());
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, user);
        return null;
    }

    @Override
    public Void visitShowPrivileges(ShowPrivilegesContext ctx) {
        setType(SplitQueryType.METADATA);
        return null;
    }

    private BehaviorObject identity(TargetType type, ParserRuleContext ctx) {
        String host = "%";
        TerminalNode hostToken = ctx.getToken(ObForMySqlParser.LOCAL_ID, 0);
        if (hostToken != null) {
            host = unquote(hostToken.getText().substring(1));
        }
        String name = unquote(ctx.getStart().getText());
        return objects.instanceObject(type, ctx, name + "@" + host);
    }

    private BehaviorObject currentUser(TerminalNode current, TerminalNode close) {
        Token stop = current.getSymbol();
        if (close != null) {
            stop = close.getSymbol();
        }
        return unnamedRange(TargetType.User, current.getSymbol(), stop);
    }

    private BehaviorObject unnamedRange(TargetType type, Token start, Token stop) {
        BehaviorObject object = objects.instanceObject(type, start);
        BehaviorObject end = objects.instanceObject(type, stop);
        object.setEndLine(end.getEndLine());
        object.setEndColumn(end.getEndColumn());
        return object;
    }

    @Override
    public Void visitAlterSystemParameters(AlterSystemParametersContext ctx) {
        for (SystemParameterAssignmentContext assignment : ctx.systemParameterAssignment()) {
            UidContext key = assignment.uid();
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.ConfigKey, key, unquote(text(key)).toLowerCase(Locale.ROOT)));
        }
        return null;
    }

    @Override
    public Void visitEnableSqlThrottle(EnableSqlThrottleContext ctx) {
        configureSqlThrottle(ctx.SQL().getSymbol(), ctx.THROTTLE().getSymbol(), ctx.PRIORITY(), ctx.sqlThrottleMetric());
        return null;
    }

    @Override
    public Void visitDisableSqlThrottle(DisableSqlThrottleContext ctx) {
        configureSqlThrottle(ctx.SQL().getSymbol(), ctx.THROTTLE().getSymbol(), null, List.of());
        return null;
    }

    private void configureSqlThrottle(Token start, Token stop, TerminalNode priority, List<SqlThrottleMetricContext> metrics) {
        Map<String, Token> explicitKeys = new HashMap<>();
        if (priority != null) {
            explicitKeys.put("priority", priority.getSymbol());
        }
        for (SqlThrottleMetricContext metric : metrics) {
            if (metric.RT() != null) {
                explicitKeys.put("rt", metric.RT().getSymbol());
            } else if (metric.QUEUE_TIME() != null) {
                // BP7's executor assigns get_queue_time() to sql_throttle_network.
                explicitKeys.put("network", metric.QUEUE_TIME().getSymbol());
            }
        }
        // Enable and disable both write all six globals, including omitted metrics.
        for (String key : List.of("priority", "rt", "cpu", "io", "network", "logical_reads")) {
            Token keyStart = start;
            Token keyStop = stop;
            if (explicitKeys.containsKey(key)) {
                keyStart = explicitKeys.get(key);
                keyStop = keyStart;
            }
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.ConfigKey, keyStart, keyStop, "sql_throttle_" + key));
        }
    }

    @Override
    public Void visitShowTenant(ShowTenantContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.TENANT().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowCreateTenant(ShowCreateTenantContext ctx) {
        // In a normal MySQL tenant, the server only permits displaying that tenant.
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.uid()));
        return null;
    }

    @Override
    public Void visitShowEngines(ShowEnginesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.TableEngine, ctx.ENGINES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowObjectFilter(ShowObjectFilterContext ctx) {
        ShowCommonEntityContext entity = ctx.showCommonEntity();
        if (entity.VARIABLES() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, entity.VARIABLES().getSymbol()));
        } else if (entity.PARAMETERS() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, entity.PARAMETERS().getSymbol()));
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

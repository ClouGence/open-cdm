/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.polardbx.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.sql.polardbx.parser.PolarXDslProvider;
import com.clougence.sql.polardbx.parser.PorXSplitAnalysisSpi;
import com.clougence.sql.polardbx.parser.PorXSplitVisitor;
import com.clougence.sql.polardbx.parser.antlr.PolardbXParserBaseVisitor;
import com.clougence.sql.polardbx.parser.antlr.PolardbXParser;
import com.clougence.sql.polardbx.parser.antlr.PolardbXParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

public class PorXBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = new PorXSplitAnalysisSpi().splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return analyzeStatement(reader, levels, codeLine, codeColumn).stream();
        }).onClose(scripts::close);
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        PorXBehaviorParserVisitor[] holder = new PorXBehaviorParserVisitor[1];
        DslHelper.doVisitor(PolarXDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new PorXBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}

final class PorXBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    PorXBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        PorXStatementBehaviorVisitor visitor = new PorXStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class PorXStatementBehaviorVisitor extends PolardbXParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final String                   currentSchema;
    private final StatementBehavior        behavior = new StatementBehavior();

    PorXStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.currentSchema = Objects.toString(levels.get(UmiTypes.Schema), "");
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
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
    public Void visitCreateUser(CreateUserContext ctx) {
        users(ctx, SplitQueryType.CREATE_USER, BehaviorAction.CREATE);
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        users(ctx, SplitQueryType.DROP_USER, BehaviorAction.DROP);
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        users(ctx, SplitQueryType.ALTER_USER, BehaviorAction.ALTER);
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        users(ctx, SplitQueryType.ALTER_USER, BehaviorAction.ALTER);
        return null;
    }

    @Override
    public Void visitAlterUserReadStrategy(AlterUserReadStrategyContext ctx) {
        users(ctx, SplitQueryType.ALTER_USER, BehaviorAction.ALTER);
        return null;
    }

    @Override
    public Void visitSetPasswordStatement(SetPasswordStatementContext ctx) {
        BehaviorObject subject = objects.instanceObject(TargetType.User, ctx);
        if (ctx.userName() != null) {
            subject = account(TargetType.User, ctx.userName());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, subject, List.of());
        return null;
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, account(TargetType.Role, role), List.of());
        }
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, account(TargetType.Role, role), List.of());
        }
        return null;
    }

    @Override
    public Void visitGrantStatement(GrantStatementContext ctx) {
        if (ctx.privilegeLevel() != null) {
            List<BehaviorObject> targets = ctx.userAuthOption().stream()
                .flatMap(auth -> descendants(auth, UserNameContext.class).stream())
                .map(user -> account(TargetType.UserOrRole, user)).toList();
            add(SplitQueryType.GRANT, BehaviorAction.GRANT, privilege(ctx.privilegeLevel(), ctx.privilegeObject), targets);
        } else {
            List<BehaviorObject> targets = recipients(ctx);
            for (RoleNameContext role : ctx.roleName()) {
                add(SplitQueryType.GRANT, BehaviorAction.GRANT, account(TargetType.Role, role), targets);
            }
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(RevokeStatementContext ctx) {
        List<BehaviorObject> targets = recipients(ctx);
        if (ctx.privilegeLevel() != null) {
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, privilege(ctx.privilegeLevel(), ctx.privilegeObject), targets);
        } else if (!ctx.roleName().isEmpty()) {
            for (RoleNameContext role : ctx.roleName()) {
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, account(TargetType.Role, role), targets);
            }
        } else {
            for (BehaviorObject target : targets) {
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, target, List.of());
            }
        }
        return null;
    }

    @Override
    public Void visitSetDefaultRole(SetDefaultRoleContext ctx) {
        List<BehaviorObject> roles = ctx.roleName().stream().map(role -> account(TargetType.Role, role)).toList();
        for (UserNameContext user : ctx.userName()) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, account(TargetType.User, user), roles);
        }
        return null;
    }

    @Override
    public Void visitShowGrants(ShowGrantsContext ctx) {
        BehaviorObject subject;
        if (ctx.userName() != null) {
            subject = account(TargetType.UserOrRole, ctx.userName());
        } else {
            // Anchor the unnamed login identity without guessing it from the current schema.
            ParserRuleContext anchor = new ParserRuleContext();
            anchor.start = ctx.SHOW().getSymbol();
            anchor.stop = ctx.GRANTS().getSymbol();
            if (ctx.CURRENT_USER() != null) {
                anchor.start = ctx.CURRENT_USER().getSymbol();
                anchor.stop = anchor.start;
                if (ctx.RR_BRACKET() != null) {
                    anchor.stop = ctx.RR_BRACKET().getSymbol();
                }
            }
            subject = objects.instanceObject(TargetType.User, anchor);
        }
        List<BehaviorObject> roles = ctx.roleName().stream().map(role -> account(TargetType.Role, role)).toList();
        add(SplitQueryType.METADATA, BehaviorAction.READ, subject, roles);
        return null;
    }

    @Override
    public Void visitShowRoutingRules(ShowRoutingRulesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.ID().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitUseStatement(UseStatementContext ctx) {
        BehaviorObject schema = objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid()))));
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, schema, List.of());
        return null;
    }

    @Override
    public Void visitSetRole(SetRoleContext ctx) {
        List<BehaviorObject> roles = ctx.roleName().stream().map(role -> account(TargetType.Role, role)).toList();
        if (ctx.ALL() != null || ctx.NONE() != null || ctx.DEFAULT() != null) {
            // EXCEPT roles filter the set; they are not independently activated roles.
            add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx.ROLE().getSymbol()), roles);
        } else {
            for (BehaviorObject role : roles) {
                add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, role, List.of());
            }
        }
        return null;
    }

    @Override
    public Void visitSetVariable(SetVariableContext ctx) {
        SplitQueryType type = ctx.accept(PorXSplitVisitor.INSTANCE);
        for (int index = 0; index < ctx.variableClause().size(); index++) {
            VariableClauseContext variable = ctx.variableClause(index);
            String name = variable.getText();
            if (variable.uid() != null) {
                name = text(variable.uid());
            }
            add(type, BehaviorAction.CONFIGURE, variable(variable, name), List.of());
            visit(ctx.expression(index));
        }
        // Nested reads must not replace the first assignment's primary classification.
        behavior.setStatementType(type);
        return null;
    }

    @Override
    public Void visitSetAutocommitStatement(SetAutocommitStatementContext ctx) {
        Token key = ctx.AUTOCOMMIT().getSymbol();
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, key, key.getText()), List.of());
        return null;
    }

    @Override
    public Void visitSetNames(SetNamesContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSetCharset(SetCharsetContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSimpleSelect(SimpleSelectContext ctx) {
        return select(ctx);
    }

    @Override
    public Void visitParenthesisSelect(ParenthesisSelectContext ctx) {
        return select(ctx);
    }

    @Override
    public Void visitUnionSelect(UnionSelectContext ctx) {
        return select(ctx);
    }

    @Override
    public Void visitUnionParenthesisSelect(UnionParenthesisSelectContext ctx) {
        return select(ctx);
    }

    private Void select(SelectStatementContext ctx) {
        if (!isExecuted(ctx)) {
            return null;
        }
        behavior.setStatementType(SplitQueryType.SELECT);
        visitChildren(ctx);
        behavior.setStatementType(SplitQueryType.SELECT);
        return null;
    }

    @Override
    public Void visitMysqlVariable(MysqlVariableContext ctx) {
        if (!isExecuted(ctx)) {
            return null;
        }
        for (ParseTree parent = ctx.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SelectStatementContext || parent instanceof SetVariableContext) {
                add(behavior.getStatementType(), BehaviorAction.READ, variable(ctx, ctx.getText()), List.of());
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitUdfFunctionCall(UdfFunctionCallContext ctx) {
        if (!isExecuted(ctx)) {
            return null;
        }
        for (ParseTree parent = ctx.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SelectStatementContext || parent instanceof SetVariableContext) {
                add(behavior.getStatementType(), BehaviorAction.CALL, object(TargetType.Function, ctx.customFunctionName().fullId()), List.of());
                return visitChildren(ctx);
            }
        }
        return null;
    }

    private boolean isExecuted(ParseTree tree) {
        for (ParseTree parent = tree.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof FullDescribeStatementContext || parent instanceof PolardbxExplainStatementContext
                || parent instanceof BaselineStatementContext || parent instanceof RoutineBodyContext
                || parent instanceof CreateFunctionContext || parent instanceof CreateViewContext || parent instanceof AlterViewContext) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Void visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        // Planning inspects referenced tables, but does not execute variable expressions.
        for (TableNameContext table : descendants(ctx.describeObjectClause(), TableNameContext.class)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, table(table), List.of());
        }
        behavior.setStatementType(SplitQueryType.PERFORMANCE);
        return null;
    }

    @Override
    public Void visitStartTransaction(StartTransactionContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.START, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitBeginWork(BeginWorkContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.START, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitCommitWork(CommitWorkContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.STOP, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitRollbackWork(RollbackWorkContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.RESET, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSavepointStatement(SavepointStatementContext ctx) {
        BehaviorObject savepoint = objects.instanceObject(TargetType.Object, ctx.uid(), unquote(text(ctx.uid())));
        add(SplitQueryType.TRANSACTION, BehaviorAction.CREATE, savepoint, List.of());
        return null;
    }

    @Override
    public Void visitReleaseStatement(ReleaseStatementContext ctx) {
        BehaviorObject savepoint = objects.instanceObject(TargetType.Object, ctx.uid(), unquote(text(ctx.uid())));
        add(SplitQueryType.TRANSACTION, BehaviorAction.DROP, savepoint, List.of());
        return null;
    }

    @Override
    public Void visitRollbackStatement(RollbackStatementContext ctx) {
        BehaviorObject savepoint = objects.instanceObject(TargetType.Object, ctx.uid(), unquote(text(ctx.uid())));
        add(SplitQueryType.TRANSACTION, BehaviorAction.RESET, objects.instanceObject(TargetType.Transaction, ctx.ROLLBACK().getSymbol()), List.of(savepoint));
        return null;
    }

    @Override
    public Void visitSetTransactionStatement(SetTransactionStatementContext ctx) {
        TargetType target = TargetType.Transaction;
        if (ctx.transactionContext != null) {
            target = TargetType.ConfigKey;
        }
        add(SplitQueryType.TRANSACTION, BehaviorAction.CONFIGURE, objects.instanceObject(target, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowObjectFilter(ShowObjectFilterContext ctx) {
        ShowCommonEntityContext entity = ctx.showCommonEntity();
        if (entity.VARIABLES() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, entity), List.of());
        } else if (entity.STATUS() != null && entity.FUNCTION() == null && entity.PROCEDURE() == null) {
            TargetType target = TargetType.Session;
            if (entity.GLOBAL() != null) {
                target = TargetType.Instance;
            }
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(target, entity), List.of());
        } else if (entity.DATABASES() != null || entity.SCHEMAS() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.unnamedObject(TargetType.Schema, entity, UmiTypes.Catalog), List.of());
        } else if (entity.CHARACTER() != null || entity.COLLATION() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, entity), List.of());
        }
        return null;
    }

    @Override
    public Void visitShowErrors(ShowErrorsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Session, ctx.errorFormat), List.of());
        return null;
    }

    @Override
    public Void visitShowProcessList(ShowProcessListContext ctx) {
        TargetType target = TargetType.Session;
        Token start;
        Token stop;
        if (ctx.PHYSICAL_PROCESSLIST() != null) {
            target = TargetType.Query;
            start = ctx.PHYSICAL_PROCESSLIST().getSymbol();
            stop = start;
        } else {
            start = ctx.PROCESSLIST().getSymbol();
            stop = start;
            if (ctx.PHYSICAL() != null) {
                target = TargetType.Query;
                start = ctx.PHYSICAL().getSymbol();
            }
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, instanceRange(target, start, stop), List.of());
        return null;
    }

    @Override
    public Void visitKillStatement(KillStatementContext ctx) {
        TargetType target = TargetType.Session;
        if (ctx.STRING_LITERAL() != null || ctx.QUERY() != null) {
            target = TargetType.Query;
        }
        add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, objects.instanceObject(target, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowCharset(ShowCharsetContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.CHARSET().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowEngines(ShowEnginesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.ENGINES().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowPlugins(ShowPluginsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.PLUGINS().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowPrivileges(ShowPrivilegesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.PRIVILEGES().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowHelp(ShowHelpContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.HELP().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitHelpStatement(HelpStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.HELP().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowSlow(ShowSlowContext ctx) {
        Token anchor;
        if (ctx.SLOW() != null) {
            anchor = ctx.SLOW().getSymbol();
        } else {
            anchor = ctx.PHYSICAL_SLOW().getSymbol();
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Log, anchor), List.of());
        return null;
    }

    @Override
    public Void visitClearSlow(ClearSlowContext ctx) {
        add(SplitQueryType.MAINTAIN_LOG, BehaviorAction.RESET, objects.instanceObject(TargetType.Log, ctx.SLOW().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowMasterLogs(ShowMasterLogsContext ctx) {
        logRead(instanceRange(TargetType.Log, ctx.logFormat, ctx.LOGS().getSymbol()), ctx.streamName);
        return null;
    }

    @Override
    public Void visitShowStatus(ShowStatusContext ctx) {
        logRead(instanceRange(TargetType.Log, ctx.MASTER().getSymbol(), ctx.STATUS().getSymbol()), ctx.streamName);
        return null;
    }

    @Override
    public Void visitShowLogEvents(ShowLogEventsContext ctx) {
        BehaviorObject subject;
        if (ctx.filename != null) {
            subject = objects.instanceObject(TargetType.Log, ctx.filename, unquote(ctx.filename.getText()));
        } else {
            subject = instanceRange(TargetType.Log, ctx.logFormat, ctx.EVENTS().getSymbol());
        }
        logRead(subject, ctx.streamName);
        return null;
    }

    @Override
    public Void visitShowProcedureCache(ShowProcedureCacheContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            instanceRange(TargetType.Cache, ctx.PROCEDURE().getSymbol(), ctx.CACHE().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitResizeProcedureCache(ResizeProcedureCacheContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CONFIGURE,
            instanceRange(TargetType.Cache, ctx.PROCEDURE().getSymbol(), ctx.CACHE().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitClearProcedureCache(ClearProcedureCacheContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET,
            instanceRange(TargetType.Cache, ctx.PROCEDURE().getSymbol(), ctx.CACHE().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitReloadProcedures(ReloadProceduresContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.LOAD, objects.instanceObject(TargetType.Cache, ctx.PROCEDURES().getSymbol()), List.of());
        return null;
    }

    private void logRead(BehaviorObject subject, Token stream) {
        List<BehaviorObject> targets = List.of();
        if (stream != null) {
            targets = List.of(objects.instanceObject(TargetType.Object, stream, unquote(stream.getText())));
        }
        add(SplitQueryType.LOG_READ, BehaviorAction.READ, subject, targets);
    }

    private BehaviorObject instanceRange(TargetType type, Token start, Token stop) {
        ParserRuleContext anchor = new ParserRuleContext();
        anchor.start = start;
        anchor.stop = stop;
        return objects.instanceObject(type, anchor);
    }

    private BehaviorObject variable(ParserRuleContext ctx, String name) {
        if (name.startsWith("@@")) {
            name = name.substring(2);
            int dot = name.indexOf('.');
            if (dot >= 0) {
                String scope = name.substring(0, dot);
                if (scope.equalsIgnoreCase("SESSION") || scope.equalsIgnoreCase("GLOBAL") || scope.equalsIgnoreCase("LOCAL")) {
                    name = name.substring(dot + 1);
                }
            }
        } else if (name.startsWith("@")) {
            name = name.substring(1);
        }
        return objects.instanceObject(TargetType.ConfigKey, ctx, unquote(name));
    }

    private void users(ParserRuleContext ctx, SplitQueryType type, BehaviorAction action) {
        for (UserNameContext user : descendants(ctx, UserNameContext.class)) {
            add(type, action, account(TargetType.User, user), List.of());
        }
    }

    private BehaviorObject account(TargetType type, ParserRuleContext ctx) {
        String name = unquote(ctx.getStart().getText());
        Token host = null;
        if (ctx instanceof UserNameContext user) {
            host = user.host;
        } else if (ctx instanceof RoleNameContext role) {
            host = role.host;
        }
        if (host != null) {
            name += "@" + unquote(host.getText().substring(1));
        }
        return objects.instanceObject(type, ctx, name);
    }

    private List<BehaviorObject> recipients(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        // Only direct children are recipients; descendants also contain source role names.
        for (int index = 0; index < ctx.getChildCount(); index++) {
            ParseTree child = ctx.getChild(index);
            if (child instanceof UserNameContext || child instanceof UidContext) {
                targets.add(account(TargetType.UserOrRole, (ParserRuleContext) child));
            }
        }
        return targets;
    }

    private BehaviorObject privilege(PrivilegeLevelContext ctx, Token explicitType) {
        if (ctx instanceof GlobalPrivLevelContext) {
            return objects.instanceObject(TargetType.Instance, ctx);
        }
        if (ctx instanceof CurrentSchemaPriviLevelContext) {
            return objects.object(TargetType.Schema, ctx, List.of(currentSchema));
        }
        if (ctx instanceof DefiniteSchemaPrivLevelContext schema) {
            return objects.object(TargetType.Schema, schema.uid(), List.of(unquote(text(schema.uid()))));
        }
        TargetType type = TargetType.Table;
        if (explicitType != null && explicitType.getType() == PolardbXParser.FUNCTION) {
            type = TargetType.Function;
        } else if (explicitType != null && explicitType.getType() == PolardbXParser.PROCEDURE) {
            type = TargetType.Procedure;
        }
        List<String> names = descendants(ctx, UidContext.class).stream().map(this::text).map(this::unquote).toList();
        return objects.object(type, ctx, names);
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
        if (value.length() < 2) {
            return value;
        }
        char quote = value.charAt(0);
        if ((quote == '`' || quote == '\'' || quote == '"') && value.charAt(value.length() - 1) == quote) {
            String marker = String.valueOf(quote);
            return value.substring(1, value.length() - 1).replace(marker + marker, marker);
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

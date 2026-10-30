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
import org.antlr.v4.runtime.tree.TerminalNode;

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
        // INSERT/REPLACE sources are attached to the destination write relation.
        for (ParseTree parent = ctx.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof InsertStatementContext || parent instanceof ReplaceStatementContext) {
                return null;
            }
        }
        add(SplitQueryType.SELECT, BehaviorAction.READ, table(ctx), List.of());
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        SplitQueryType type = SplitQueryType.INSERT;
        BehaviorAction action = BehaviorAction.INSERT;
        if (ctx.duplicatedFirst != null) {
            type = SplitQueryType.MERGE;
            action = BehaviorAction.MERGE;
        }
        return write(ctx, ctx.tableName(), type, action, true);
    }

    @Override
    public Void visitReplaceStatement(ReplaceStatementContext ctx) {
        return write(ctx, ctx.tableName(), SplitQueryType.MERGE, BehaviorAction.MERGE, true);
    }

    @Override
    public Void visitSingleUpdateStatement(SingleUpdateStatementContext ctx) {
        return write(ctx, ctx.tableName(), SplitQueryType.UPDATE, BehaviorAction.UPDATE, false);
    }

    @Override
    public Void visitSingleDeleteStatement(SingleDeleteStatementContext ctx) {
        return write(ctx, ctx.tableName(), SplitQueryType.DELETE, BehaviorAction.DELETE, false);
    }

    private Void write(ParserRuleContext ctx, TableNameContext destination, SplitQueryType type, BehaviorAction action, boolean attachSources) {
        if (!isExecuted(ctx)) {
            return null;
        }
        List<BehaviorObject> sources = List.of();
        if (attachSources) {
            sources = descendants(ctx, TableNameContext.class).stream().filter(table -> table != destination).map(this::table).toList();
        }
        add(type, action, table(destination), sources);
        // Evaluate expressions and subqueries without reclassifying the destination as a read.
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child != destination) {
                visit(child);
            }
        }
        behavior.setStatementType(type);
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        List<BehaviorObject> sources = descendants(ctx.selectStatement(), TableNameContext.class).stream().map(this::table).toList();
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.fullId()), sources);
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
            if (parent instanceof SelectStatementContext || parent instanceof SetVariableContext
                || parent instanceof InsertStatementContext || parent instanceof ReplaceStatementContext
                || parent instanceof SingleUpdateStatementContext || parent instanceof SingleDeleteStatementContext) {
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
            if (parent instanceof SelectStatementContext || parent instanceof SetVariableContext
                || parent instanceof InsertStatementContext || parent instanceof ReplaceStatementContext
                || parent instanceof SingleUpdateStatementContext || parent instanceof SingleDeleteStatementContext) {
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
        return explain(ctx.describeObjectClause());
    }

    @Override
    public Void visitPolardbxExplainStatement(PolardbxExplainStatementContext ctx) {
        return explain(ctx.describeObjectClause());
    }

    private Void explain(DescribeObjectClauseContext ctx) {
        // Planning inspects table metadata without executing the SQL body.
        for (TableNameContext table : descendants(ctx, TableNameContext.class)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, table(table), List.of());
        }
        behavior.setStatementType(SplitQueryType.PERFORMANCE);
        return null;
    }

    @Override
    public Void visitLockClause(LockClauseContext ctx) {
        if (!isExecuted(ctx)) {
            return null;
        }
        ParseTree query = ctx.getParent();
        for (TableNameContext table : descendants(query, TableNameContext.class)) {
            ParseTree owner = table.getParent();
            while (owner != query && !(owner instanceof SelectStatementContext)) {
                owner = owner.getParent();
            }
            // A subquery has its own lock clause; an outer lock must not leak into it.
            if (owner == query) {
                add(behavior.getStatementType(), BehaviorAction.LOCK, table(table), List.of());
            }
        }
        return null;
    }

    @Override
    public Void visitShowTrace(ShowTraceContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Session, ctx.TRACE().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitClearPlanCache(ClearPlanCacheContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET,
            objects.unnamedObject(TargetType.Cache, ctx.PLANCACHE().getSymbol(), UmiTypes.Schema), List.of());
        return null;
    }

    @Override
    public Void visitBaselineStatement(BaselineStatementContext ctx) {
        Token command = ctx.BASELINE().getSymbol();
        if (ctx.HELP() != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, command), List.of());
            return null;
        }
        if (ctx.selectStatement() != null) {
            List<BehaviorObject> sources = descendants(ctx.selectStatement(), TableNameContext.class).stream().map(this::table).toList();
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Object, command), sources);
            return null;
        }
        List<SignedBaselineIdContext> ids = ctx.signedBaselineId();
        if (ctx.DELETE_PLAN() != null) {
            BehaviorObject baseline = objects.instanceObject(TargetType.Object, ids.get(0), ids.get(0).getText());
            for (SignedBaselineIdContext id : ids.subList(1, ids.size())) {
                BehaviorObject plan = objects.childObject(TargetType.Object, id, baseline, id.getText());
                add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, plan, List.of(baseline));
            }
            return null;
        }
        SplitQueryType type = SplitQueryType.ADMIN_PERFORMANCE;
        BehaviorAction action;
        if (ctx.LIST() != null) {
            type = SplitQueryType.PERFORMANCE;
            action = BehaviorAction.READ;
        } else if (ctx.LOAD() != null) {
            action = BehaviorAction.LOAD;
        } else if (ctx.PERSIST() != null) {
            action = BehaviorAction.FLUSH;
        } else if (ctx.CLEAR() != null) {
            action = BehaviorAction.RESET;
        } else {
            action = BehaviorAction.DROP;
        }
        // Some maintenance commands span schemas; IDs are instance-level symbolic selectors.
        if (ids.isEmpty()) {
            add(type, action, objects.instanceObject(TargetType.Object, command), List.of());
        } else {
            for (SignedBaselineIdContext id : ids) {
                add(type, action, objects.instanceObject(TargetType.Object, id, id.getText()), List.of());
            }
        }
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

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        maintainTables(ctx.tables(), BehaviorAction.ANALYZE);
        return null;
    }

    @Override
    public Void visitCheckTable(CheckTableContext ctx) {
        maintainTables(ctx.tables(), BehaviorAction.VALIDATE);
        return null;
    }

    @Override
    public Void visitChecksumTable(ChecksumTableContext ctx) {
        maintainTables(ctx.tables(), BehaviorAction.CHECKSUM);
        return null;
    }

    @Override
    public Void visitOptimizeTable(OptimizeTableContext ctx) {
        maintainTables(ctx.tables(), BehaviorAction.OPTIMIZE);
        return null;
    }

    @Override
    public Void visitCheckGlobalIndex(CheckGlobalIndexContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN;
        BehaviorAction action = BehaviorAction.VALIDATE;
        if (ctx.SHOW() != null) {
            type = SplitQueryType.PERFORMANCE;
            action = BehaviorAction.READ;
        } else if (ctx.CORRECTION_BASED_ON_PRIMARY() != null) {
            action = BehaviorAction.REPAIR;
        }
        String schema = currentSchema;
        List<BehaviorObject> targets = List.of();
        if (ctx.tableName() != null) {
            targets = List.of(table(ctx.tableName()));
            List<UidContext> names = ctx.tableName().fullId().uid();
            if (names.size() == 2) {
                schema = unquote(text(names.get(0)));
            }
        }
        BehaviorObject index = objects.object(TargetType.Index, ctx.uid(), List.of(schema, unquote(text(ctx.uid()))));
        add(type, action, index, targets);
        return null;
    }

    @Override
    public Void visitInspectIndex(InspectIndexContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName() != null) {
            subject = table(ctx.tableName());
        } else {
            subject = objects.unnamedObject(TargetType.Index, ctx.INDEX().getSymbol(), UmiTypes.Schema);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject, List.of());
        return null;
    }

    @Override
    public Void visitShowDdl(ShowDdlContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            instanceRange(TargetType.Job, ctx.DDL().getSymbol(), ctx.getStop()), List.of());
        return null;
    }

    @Override
    public Void visitDdlJobControl(DdlJobControlContext ctx) {
        jobControl(ctx.getStart(), ctx.unsignedInteger());
        return null;
    }

    @Override
    public Void visitRebalanceJobControl(RebalanceJobControlContext ctx) {
        jobControl(ctx.getStart(), ctx.unsignedInteger());
        return null;
    }

    @Override
    public Void visitShowRule(ShowRuleContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName() != null) {
            subject = table(ctx.tableName());
        } else {
            subject = objects.unnamedObject(TargetType.Table, ctx.RULE().getSymbol(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, subject, List.of());
        return null;
    }

    @Override
    public Void visitShowTopology(ShowTopologyContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitShowPartitions(ShowPartitionsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitShowBroadcasts(ShowBroadcastsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ,
            objects.unnamedObject(TargetType.Table, ctx.BROADCASTS().getSymbol(), UmiTypes.Schema), List.of());
        return null;
    }

    @Override
    public Void visitShowTablegroup(ShowTablegroupContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Object, ctx.TABLEGROUP().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowDistributedStatus(ShowDistributedStatusContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.getStop()), List.of());
        return null;
    }

    @Override
    public Void visitCreateCclRule(CreateCclRuleContext ctx) {
        UidContext rule = ctx.uid(0);
        BehaviorObject policy = objects.instanceObject(TargetType.Policy, rule, unquote(text(rule)));
        List<BehaviorObject> targets = List.of(cclScope(ctx), account(TargetType.User, ctx.userName()));
        add(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, policy, targets);
        return null;
    }

    @Override
    public Void visitDropCclRule(DropCclRuleContext ctx) {
        for (UidContext rule : ctx.uid()) {
            add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, rule, unquote(text(rule))), List.of());
        }
        return null;
    }

    @Override
    public Void visitClearCclRules(ClearCclRulesContext ctx) {
        add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.CCL_RULES().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowCclRules(ShowCclRulesContext ctx) {
        if (ctx.CCL_RULES() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.CCL_RULES().getSymbol()), List.of());
        } else {
            for (UidContext rule : ctx.uid()) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, rule, unquote(text(rule))), List.of());
            }
        }
        return null;
    }

    @Override
    public Void visitSlowSqlCcl(SlowSqlCclContext ctx) {
        Token anchor = ctx.SLOW_SQL_CCL().getSymbol();
        if (ctx.SHOW() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, anchor), List.of());
            return null;
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, anchor), List.of());
        if (ctx.GO() != null) {
            // GO can immediately terminate excess queries, in addition to configuring throttling.
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Query, ctx.GO().getSymbol()), List.of());
        } else {
            // BACK removes the generated rules, not every explicitly named CCL policy.
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.BACK().getSymbol()), List.of());
        }
        return null;
    }

    private void maintainTables(TablesContext ctx, BehaviorAction action) {
        for (TableNameContext table : ctx.tableName()) {
            add(SplitQueryType.ADMIN_TABLE, action, table(table), List.of());
        }
    }

    private void jobControl(Token command, UnsignedIntegerContext id) {
        BehaviorAction action = switch (command.getType()) {
            case PolardbXParser.PAUSE -> BehaviorAction.STOP;
            case PolardbXParser.CONTINUE -> BehaviorAction.START;
            case PolardbXParser.CANCEL -> BehaviorAction.TERMINATE;
            default -> BehaviorAction.RESET; // The remaining grammar alternative is ROLLBACK DDL.
        };
        add(SplitQueryType.ADMIN_JOB, action, objects.instanceObject(TargetType.Job, id, text(id)), List.of());
    }

    private BehaviorObject cclScope(CreateCclRuleContext ctx) {
        // The scope consists of two uid-or-wildcard children between ON and TO.
        ParseTree first = ctx.getChild(ctx.children.indexOf(ctx.ON()) + 1);
        ParseTree last = ctx.getChild(ctx.children.indexOf(ctx.TO()) - 1);
        ParserRuleContext scope = new ParserRuleContext();
        if (first instanceof ParserRuleContext name) {
            scope.start = name.getStart();
        } else {
            scope.start = ((TerminalNode) first).getSymbol();
        }
        if (last instanceof ParserRuleContext name) {
            scope.stop = name.getStop();
        } else {
            scope.stop = ((TerminalNode) last).getSymbol();
        }
        if (first instanceof TerminalNode && last instanceof TerminalNode) {
            return objects.instanceObject(TargetType.Instance, scope);
        }
        if (last instanceof TerminalNode) {
            return objects.object(TargetType.Schema, scope, List.of(unquote(first.getText())));
        }
        return objects.object(TargetType.Table, scope, List.of(unquote(first.getText()), unquote(last.getText())));
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

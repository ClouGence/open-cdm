/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParserBaseVisitor;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class TiStatementBehaviorVisitor extends TiDBParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();
    private ParseTree                      root;

    TiStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        List<BehaviorRelation> reads = new ArrayList<>();
        if (behavior.getStatementType() == SplitQueryType.SELECT || behavior.getStatementType() == SplitQueryType.PERFORMANCE) {
            reads.addAll(behavior.getRelations());
            behavior.getRelations().clear();
        }
        addFunctions(root);
        for (DefaultValueContext value : descendants(root, DefaultValueContext.class)) {
            if (value.fullId() != null) {
                add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Sequence, value.fullId()), List.of());
            }
        }
        behavior.getRelations().addAll(reads);
        return behavior;
    }

    @Override
    public Void visit(ParseTree tree) {
        if (root == null) {
            root = tree;
        }
        return super.visit(tree);
    }

    private void addFunctions(ParseTree tree) {
        for (ParserRuleContext context : descendants(tree, ParserRuleContext.class)) {
            if (context instanceof CurrentTimestampContext timestamp) {
                Token token = timestamp.getStart();
                add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, token, List.of(unquote(token.getText()))), List.of());
                continue;
            }
            if (!(context instanceof FunctionCallContext function)) {
                continue;
            }
            if (function instanceof UdfFunctionCallContext udf) {
                add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, udf.customFunctionName().fullId()), List.of());
            } else {
                if (function instanceof SpecificFunctionCallContext specific
                    && (specific.specificFunction() instanceof CaseFunctionCallContext || specific.specificFunction() instanceof SpecialTimeCallContext)) {
                    continue;
                }
                var token = function.getStart();
                add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, token, List.of(unquote(token.getText()))), List.of());
            }
        }
    }

    @Override
    public Void visitChildren(org.antlr.v4.runtime.tree.RuleNode ctx) {
        if (ctx instanceof TransactionStatementContext) {
            behavior.setStatementType(SplitQueryType.TRANSACTION);
        }
        if (ctx instanceof SelectStatementContext && behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.SELECT);
        }
        if (ctx instanceof SelectStatementContext select) {
            List<SelectIntoTextFileContext> textFiles = descendants(select, SelectIntoTextFileContext.class);
            List<SelectIntoDumpFileContext> dumpFiles = descendants(select, SelectIntoDumpFileContext.class);
            if (!textFiles.isEmpty() || !dumpFiles.isEmpty()) {
                Token filename;
                if (!textFiles.isEmpty()) {
                    filename = textFiles.get(0).filename;
                } else {
                    filename = dumpFiles.get(0).STRING_LITERAL().getSymbol();
                }
                BehaviorObject file = file(filename);
                for (BehaviorObject table : tables(select)) {
                    add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, table, List.of(file));
                }
                add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, file, List.of());
                return null;
            }
        }
        return super.visitChildren(ctx);
    }

    @Override
    public Void visitLoadDataStatement(LoadDataStatementContext ctx) {
        BehaviorObject file = file(ctx.filename);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, table(ctx.tableName()), List.of(file));
        for (UpdatedElementContext assignment : ctx.updatedElement()) {
            if (assignment.expression() != null) {
                visit(assignment.expression());
            }
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, file, List.of());
        return null;
    }

    @Override
    public Void visitLoadXmlStatement(LoadXmlStatementContext ctx) {
        BehaviorObject file = file(ctx.filename);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, table(ctx.tableName()), List.of(file));
        for (UpdatedElementContext assignment : ctx.updatedElement()) {
            if (assignment.expression() != null) {
                visit(assignment.expression());
            }
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, file, List.of());
        return null;
    }

    private BehaviorObject file(Token token) {
        String name = unquote(token.getText()).replaceFirst("^/+", "");
        if (name.isEmpty()) {
            return objects.instanceObject(TargetType.File, token);
        }
        return objects.instanceObject(TargetType.File, token, name);
    }

    @Override
    public Void visitCreateBinding(CreateBindingContext ctx) {
        binding(ctx, BehaviorAction.CREATE);
        return null;
    }

    @Override
    public Void visitCreatePlacementPolicy(CreatePlacementPolicyContext ctx) {
        SplitQueryType type = SplitQueryType.CREATE_POLICY;
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.REPLACE() != null) {
            type = SplitQueryType.ALTER_POLICY;
            action = BehaviorAction.REPLACE;
        }
        add(type, action, objects.instanceObject(TargetType.Policy, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitAlterPlacementPolicy(AlterPlacementPolicyContext ctx) {
        add(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, objects.instanceObject(TargetType.Policy, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitDropPlacementPolicy(DropPlacementPolicyContext ctx) {
        add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitShowCreatePlacementPolicy(ShowCreatePlacementPolicyContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitDropBinding(DropBindingContext ctx) {
        binding(ctx, BehaviorAction.DROP);
        return null;
    }

    @Override
    public Void visitSetBinding(SetBindingContext ctx) {
        binding(ctx, BehaviorAction.ALTER);
        return null;
    }

    private void binding(ParserRuleContext ctx, BehaviorAction action) {
        add(SplitQueryType.ADMIN_PERFORMANCE, action, objects.unnamedObject(TargetType.Policy, ctx, UmiTypes.Schema), List.of());
        // A binding describes a plan; its DML is not executed here.
        for (BehaviorObject table : tables(ctx)) {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.READ, table, List.of());
        }
    }

    @Override
    public Void visitShowBindings(ShowBindingsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.unnamedObject(TargetType.Policy, ctx, UmiTypes.Schema), List.of());
        return null;
    }

    @Override
    public Void visitShowBindingCache(ShowBindingCacheContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        BehaviorObject owner = table(ctx.tableName());
        boolean altersTable = ctx.alterSpecification().isEmpty();
        for (AlterSpecificationContext clause : ctx.alterSpecification()) {
            if (clause instanceof AlterByRenameContext rename) {
                add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, owner, List.of(table(rename.tableName())));
                continue;
            }
            ParserRuleContext name = null;
            TargetType targetType = TargetType.Index;
            BehaviorAction action = BehaviorAction.CREATE;
            if (clause instanceof AlterByAddIndexContext index) {
                name = index.indexName();
            } else if (clause instanceof AlterByAddUniqueKeyContext index) {
                name = index.indexName();
            } else if (clause instanceof AlterByAddSpecialIndexContext index) {
                name = index.indexName();
            } else if (clause instanceof AlterByDropIndexContext index) {
                name = index.indexName();
                action = BehaviorAction.DROP;
            } else if (clause instanceof AlterByAddPrimaryKeyContext key) {
                name = key.name;
                targetType = TargetType.Constraint;
            } else if (clause instanceof AlterByAddForeignKeyContext key) {
                name = key.name;
                targetType = TargetType.Constraint;
            } else if (clause instanceof AlterByAddCheckTableConstraintContext key) {
                name = key.name;
                targetType = TargetType.Constraint;
            } else if (clause instanceof AlterByDropConstraintCheckContext key) {
                name = key.uid();
                targetType = TargetType.Constraint;
                action = BehaviorAction.DROP;
            } else if (clause instanceof AlterByDropPrimaryKeyContext) {
                targetType = TargetType.Constraint;
                action = BehaviorAction.DROP;
            } else if (clause instanceof AlterByDropForeignKeyContext key) {
                name = key.uid();
                targetType = TargetType.Constraint;
                action = BehaviorAction.DROP;
            } else {
                altersTable = true;
                continue;
            }
            BehaviorObject subject;
            if (name == null) {
                subject = objects.unnamedObject(targetType, clause, UmiTypes.Schema);
            } else {
                subject = objects.object(targetType, name, List.of(unquote(text(name))));
            }
            List<BehaviorObject> targets = new ArrayList<>();
            targets.add(owner);
            targets.addAll(tables(clause));
            add(SplitQueryType.ALTER_TABLE, action, subject, targets);
        }
        if (altersTable || ctx.partitionDefinitions() != null || ctx.REMOVE() != null) {
            add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, owner, policies(ctx));
        }
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, objects.object(TargetType.Schema, ctx.databaseName(), List.of(unquote(text(ctx.databaseName())))), policies(ctx));
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, objects.object(TargetType.Schema, ctx.databaseName(), List.of(unquote(text(ctx.databaseName())))), List.of());
        return null;
    }

    @Override
    public Void visitUseStatement(UseStatementContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid())))), List.of());
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        for (FullIdContext name : ctx.fullId()) {
            add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, name), List.of());
        }
        return null;
    }

    @Override
    public Void visitRenameTable(RenameTableContext ctx) {
        for (RenameTableClauseContext clause : ctx.renameTableClause()) {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, table(clause.tableName(0)), List.of(table(clause.tableName(1))));
        }
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitSetVariable(SetVariableContext ctx) {
        for (VariableClauseContext variable : ctx.variableClause()) {
            String name = text(variable);
            SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
            if (name.startsWith("@") && !name.startsWith("@@")) {
                type = SplitQueryType.SESSION_VARIABLE_RW;
            } else if (variable.GLOBAL() != null || name.regionMatches(true, 0, "@@GLOBAL.", 0, 9) || variable.PERSIST() != null) {
                type = SplitQueryType.SYSTEM_SETTING_WRITE;
            }
            add(type, BehaviorAction.CONFIGURE, variable(variable), List.of());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitMysqlVariable(MysqlVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, variable(ctx), List.of());
        return null;
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        for (UserNameContext user : descendants(ctx, UserNameContext.class)) {
            add(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, user(TargetType.User, user), List.of());
        }
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        for (UserNameContext user : descendants(ctx, UserNameContext.class)) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user(TargetType.User, user), List.of());
        }
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        for (UserNameContext user : descendants(ctx, UserNameContext.class)) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user(TargetType.User, user), List.of());
        }
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        for (UserNameContext user : ctx.userName()) {
            add(SplitQueryType.DROP_USER, BehaviorAction.DROP, user(TargetType.User, user), List.of());
        }
        return null;
    }

    private BehaviorObject user(TargetType type, UserNameContext ctx) {
        String name = unquote(ctx.user.getText());
        if (ctx.host != null) {
            name += "@" + unquote(ctx.host.getText().substring(1));
        }
        return objects.instanceObject(type, ctx, name);
    }

    @Override
    public Void visitGrantStatement(GrantStatementContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.privilegeLevel() != null) {
            for (UserAuthOptionContext option : ctx.userAuthOption()) {
                for (UserNameContext account : descendants(option, UserNameContext.class)) {
                    targets.add(user(TargetType.UserOrRole, account));
                }
            }
            TargetType type = privilegeType(ctx.privilegeObject);
            add(SplitQueryType.GRANT, BehaviorAction.GRANT, privilege(type, ctx.privilegeLevel()), targets);
        } else {
            for (UserNameContext account : ctx.userName()) {
                targets.add(user(TargetType.UserOrRole, account));
            }
            for (UidContext account : ctx.uid()) {
                targets.add(objects.instanceObject(TargetType.UserOrRole, account, unquote(text(account))));
            }
            for (RoleNameContext role : ctx.roleName()) {
                add(SplitQueryType.GRANT, BehaviorAction.GRANT, objects.instanceObject(TargetType.Role, role, unquote(text(role))), targets);
            }
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(RevokeStatementContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (UserNameContext account : ctx.userName()) {
            targets.add(user(TargetType.UserOrRole, account));
        }
        for (UidContext account : ctx.uid()) {
            targets.add(objects.instanceObject(TargetType.UserOrRole, account, unquote(text(account))));
        }
        if (ctx.privilegeLevel() != null) {
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, privilege(privilegeType(ctx.privilegeObject), ctx.privilegeLevel()), targets);
        } else if (!ctx.roleName().isEmpty()) {
            for (RoleNameContext role : ctx.roleName()) {
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, objects.instanceObject(TargetType.Role, role, unquote(text(role))), targets);
            }
        } else {
            for (BehaviorObject target : targets) {
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, target, List.of());
            }
        }
        return null;
    }

    private TargetType privilegeType(Token token) {
        if (token != null) {
            if (token.getText().equalsIgnoreCase("FUNCTION")) {
                return TargetType.Function;
            }
            if (token.getText().equalsIgnoreCase("PROCEDURE")) {
                return TargetType.Procedure;
            }
        }
        return TargetType.Table;
    }

    private BehaviorObject privilege(TargetType type, PrivilegeLevelContext ctx) {
        if (ctx instanceof GlobalPrivLevelContext) {
            return objects.instanceObject(TargetType.Instance, ctx);
        }
        if (ctx instanceof CurrentSchemaPriviLevelContext) {
            return objects.unnamedObject(TargetType.Schema, ctx, UmiTypes.Schema);
        }
        if (ctx instanceof DefiniteSchemaPrivLevelContext schema) {
            return objects.object(TargetType.Schema, schema.uid(), List.of(unquote(text(schema.uid()))));
        }
        List<String> names = new ArrayList<>();
        for (UidContext name : descendants(ctx, UidContext.class)) {
            names.add(unquote(text(name)));
        }
        return objects.object(type, ctx, names);
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, objects.instanceObject(TargetType.Role, ctx.roleName(), unquote(text(ctx.roleName()))), List.of());
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, objects.instanceObject(TargetType.Role, role, unquote(text(role))), List.of());
        }
        return null;
    }

    @Override
    public Void visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        BehaviorObject schema;
        if (ctx.databaseName() == null) {
            schema = objects.unnamedObject(TargetType.Schema, ctx, UmiTypes.Schema);
        } else {
            schema = objects.object(TargetType.Schema, ctx.databaseName(), List.of(unquote(text(ctx.databaseName()))));
        }
        add(SplitQueryType.ALTER_SCHEMA, BehaviorAction.ALTER, schema, policies(ctx));
        return null;
    }

    private BehaviorObject variable(ParserRuleContext ctx) {
        String name = text(ctx).replaceFirst("(?i)^(?:@@)?(?:GLOBAL|SESSION|LOCAL|PERSIST)(?:\\s*\\.\\s*|\\s+)", "");
        name = name.replaceFirst("^@{1,2}", "");
        return objects.instanceObject(TargetType.ConfigKey, ctx, unquote(name));
    }

    @Override
    public Void visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        if (ctx.analyze != null) {
            return visitChildren(ctx);
        }
        behavior.setStatementType(SplitQueryType.PERFORMANCE);
        for (BehaviorObject table : tables(ctx)) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, table, List.of());
        }
        return null;
    }

    @Override
    public Void visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitShowCreateDb(ShowCreateDbContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid())))), List.of());
        return null;
    }

    @Override
    public Void visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        TargetType type = switch (ctx.namedEntity.getText().toUpperCase(java.util.Locale.ROOT)) {
            case "EVENT" -> TargetType.Event;
            case "FUNCTION" -> TargetType.Function;
            case "PROCEDURE" -> TargetType.Procedure;
            case "TRIGGER" -> TargetType.Trigger;
            case "VIEW" -> TargetType.View;
            default -> TargetType.Table;
        };
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(type, ctx.fullId()), List.of());
        return null;
    }

    @Override
    public Void visitShowCreateUser(ShowCreateUserContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, user(TargetType.User, ctx.userName()), List.of());
        return null;
    }

    @Override
    public Void visitShowGrants(ShowGrantsContext ctx) {
        BehaviorObject target;
        if (ctx.userName() == null) {
            target = objects.instanceObject(TargetType.UserOrRole, ctx);
        } else {
            target = user(TargetType.UserOrRole, ctx.userName());
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, target, List.of());
        return null;
    }

    @Override
    public Void visitShowColumns(ShowColumnsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, metadataTable(ctx.tableName(), ctx.uid()), List.of());
        return null;
    }

    @Override
    public Void visitShowIndexes(ShowIndexesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, metadataTable(ctx.tableName(), ctx.uid()), List.of());
        return null;
    }

    private BehaviorObject metadataTable(TableNameContext table, UidContext schema) {
        if (schema == null) {
            return table(table);
        }
        List<UidContext> names = table.fullId().uid();
        return objects.object(TargetType.Table, table, List.of(unquote(text(schema)), unquote(text(names.get(names.size() - 1)))));
    }

    @Override
    public Void visitShowTables(ShowTablesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.object(TargetType.Schema, ctx.uid(0), List.of(unquote(text(ctx.uid(0))))), List.of());
        return null;
    }

    @Override
    public Void visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        BehaviorObject schema;
        if (ctx.uid() == null) {
            schema = objects.unnamedObject(TargetType.Schema, ctx, UmiTypes.Schema);
        } else {
            schema = objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid()))));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema, List.of());
        return null;
    }

    @Override
    public Void visitShowObjectFilter(ShowObjectFilterContext ctx) {
        ShowCommonEntityContext entity = ctx.showCommonEntity();
        TargetType type = TargetType.ConfigKey;
        UmiTypes ancestor = UmiTypes.Instance;
        SplitQueryType statementType = SplitQueryType.METADATA;
        if (entity.DATABASES() != null || entity.SCHEMAS() != null) {
            type = TargetType.Schema;
            ancestor = UmiTypes.Catalog;
        } else if (entity.FUNCTION() != null) {
            type = TargetType.Function;
            ancestor = UmiTypes.Schema;
        } else if (entity.PROCEDURE() != null) {
            type = TargetType.Procedure;
            ancestor = UmiTypes.Schema;
        } else if (entity.STATUS() != null) {
            type = TargetType.Instance;
            statementType = SplitQueryType.PERFORMANCE;
        }
        add(statementType, BehaviorAction.READ, objects.unnamedObject(type, entity, ancestor), List.of());
        return null;
    }

    @Override
    public Void visitShowProcessList(ShowProcessListContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        SplitQueryType type = SplitQueryType.CREATE_VIEW;
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.REPLACE() != null) {
            type = SplitQueryType.ALTER_VIEW;
            action = BehaviorAction.REPLACE;
        }
        add(type, action, object(TargetType.View, ctx.fullId()), tables(ctx.selectStatement()));
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        add(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, objects.object(TargetType.Index, ctx.indexName(), List.of(unquote(text(ctx.indexName())))), List
            .of(table(ctx.tableName())));
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, objects.object(TargetType.Index, ctx.indexName(), List.of(unquote(text(ctx.indexName())))), List
            .of(table(ctx.tableName())));
        return null;
    }

    @Override
    public Void visitCreateSequence(CreateSequenceContext ctx) {
        add(SplitQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.sequence_name().fullId()), List.of());
        return null;
    }

    @Override
    public Void visitDropSequence(DropSequenceContext ctx) {
        for (Sequence_nameContext name : ctx.sequence_name()) {
            add(SplitQueryType.DROP_SEQUENCE, BehaviorAction.DROP, object(TargetType.Sequence, name.fullId()), List.of());
        }
        return null;
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
        for (ParserRuleContext definition : descendants(ctx.createDefinitions(), ParserRuleContext.class)) {
            ParserRuleContext name = null;
            TargetType targetType = TargetType.Constraint;
            if (definition instanceof PrimaryKeyTableConstraintContext key) {
                name = key.name;
            } else if (definition instanceof UniqueKeyTableConstraintContext key) {
                name = key.name;
                if (name == null) {
                    name = key.index;
                }
            } else if (definition instanceof ForeignKeyTableConstraintContext key) {
                name = key.name;
            } else if (definition instanceof CheckTableConstraintContext check) {
                name = check.name;
            } else if (definition instanceof CheckColumnConstraintContext check) {
                name = check.name;
            } else if (definition instanceof SimpleIndexDeclarationContext index) {
                name = index.uid();
                targetType = TargetType.Index;
            } else if (definition instanceof SpecialIndexDeclarationContext index) {
                name = index.uid();
                targetType = TargetType.Index;
            } else if (!(definition instanceof PrimaryKeyColumnConstraintContext) && !(definition instanceof UniqueKeyColumnConstraintContext)
                       && !(definition instanceof CheckColumnConstraintContext) && !(definition instanceof ReferenceColumnConstraintContext)) {
                continue;
            }
            BehaviorObject subject;
            if (name == null) {
                subject = objects.unnamedObject(targetType, definition, UmiTypes.Schema);
            } else {
                subject = objects.object(targetType, name, List.of(unquote(text(name))));
            }
            List<BehaviorObject> targets = new ArrayList<>();
            targets.add(table(ctx.tableName()));
            targets.addAll(tables(definition));
            add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, subject, targets);
        }
        return null;
    }

    @Override
    public Void visitCallStatement(CallStatementContext ctx) {
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.procName().fullId()), List.of());
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        SplitQueryType type = SplitQueryType.INSERT;
        BehaviorAction action = BehaviorAction.INSERT;
        if (ctx.DUPLICATE() != null) {
            type = SplitQueryType.MERGE;
            action = BehaviorAction.MERGE;
        }
        BehaviorRelation relation = add(type, action, table(ctx.tableName()), tables(ctx.insertStatementValue()));
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
        List<BehaviorObject> targets = new ArrayList<>(sources.stream().map(this::table).filter(Objects::nonNull).toList());
        targets.addAll(policies(subject.getParent()));
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(subject), targets);
    }

    private List<BehaviorObject> policies(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (PlacementPolicyReferenceContext reference : descendants(tree, PlacementPolicyReferenceContext.class)) {
            if (reference.uid() != null) {
                result.add(objects.instanceObject(TargetType.Policy, reference.uid(), unquote(text(reference.uid()))));
            } else if (reference.STRING_LITERAL() != null) {
                Token token = reference.STRING_LITERAL().getSymbol();
                String name = unquote(token.getText());
                if (!name.equalsIgnoreCase("DEFAULT")) {
                    result.add(objects.instanceObject(TargetType.Policy, token, name));
                }
            }
        }
        return result;
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
        if (context == null || isCte(context)) {
            return null;
        }
        return object(TargetType.Table, context.fullId());
    }

    private boolean isCte(TableNameContext table) {
        if (table.fullId().uid().size() != 1) {
            return false;
        }
        String name = unquote(text(table.fullId()));
        java.util.Set<ParseTree> ancestors = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            ancestors.add(parent);
        }
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof WithSelectStatementContext with) {
                for (WithSelectExprContext cte : with.withClause().withSelectExpr()) {
                    boolean current = ancestors.contains(cte);
                    if (current && with.withClause().RECURSIVE() == null) {
                        break;
                    }
                    if (name.equalsIgnoreCase(unquote(text(cte.uid())))) {
                        return true;
                    }
                    if (current) {
                        break;
                    }
                }
            }
        }
        return false;
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
        if (value.length() >= 2) {
            char quote = value.charAt(0);
            if ((quote == '`' || quote == '\'' || quote == '"') && value.charAt(value.length() - 1) == quote) {
                return value.substring(1, value.length() - 1).replace("" + quote + quote, "" + quote);
            }
        }
        return value;
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
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || type != SplitQueryType.SELECT) {
            behavior.setStatementType(type);
        }
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

    @Override
    public Void visitCreateResourceGroup(CreateResourceGroupContext ctx) {
        add(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE, objects
            .instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))), List.of());
        return null;
    }

    @Override
    public Void visitAlterResourceGroup(AlterResourceGroupContext ctx) {
        add(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER, objects
            .instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))), List.of());
        return null;
    }

    @Override
    public Void visitDropResourceGroup(DropResourceGroupContext ctx) {
        add(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP, objects
            .instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))), List.of());
        return null;
    }

    @Override
    public Void visitShowCreateResourceGroup(ShowCreateResourceGroupContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))), List
            .of());
        return null;
    }

    @Override
    public Void visitSetResourceGroup(SetResourceGroupContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects
            .instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))), List.of());
        return null;
    }

    @Override
    public Void visitImportIntoStatement(ImportIntoStatementContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.filename != null) {
            targets.add(file(ctx.filename));
        } else {
            if (ctx.selectStatement() != null) {
                targets.addAll(tables(ctx.selectStatement()));
            }
            if (ctx.withSelectStatement() != null) {
                targets.addAll(tables(ctx.withSelectStatement()));
            }
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, table(ctx.tableName()), targets);
        for (UpdatedElementContext assignment : ctx.updatedElement()) {
            if (assignment.expression() != null) {
                visit(assignment.expression());
            }
        }
        if (ctx.filename != null) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, file(ctx.filename), List.of());
        }
        return null;
    }

    @Override
    public Void visitSplitRegionStatement(SplitRegionStatementContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ALTER, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitCreateProcedure(CreateProcedureContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx.fullId()), tables(ctx.routineBody()));
        return null;
    }

    @Override
    public Void visitCreateFunction(CreateFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.fullId()), tables(ctx.routineBody()));
        return null;
    }

    @Override
    public Void visitDropProcedure(DropProcedureContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Procedure, ctx.fullId()), List.of());
        return null;
    }

    @Override
    public Void visitDropFunction(DropFunctionContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Function, ctx.fullId()), List.of());
        return null;
    }

    @Override
    public Void visitAlterProcedure(AlterProcedureContext ctx) {
        add(SplitQueryType.ALTER_PROG_OBJ, BehaviorAction.ALTER, object(TargetType.Procedure, ctx.fullId()), List.of());
        return null;
    }

    @Override
    public Void visitAlterFunction(AlterFunctionContext ctx) {
        add(SplitQueryType.ALTER_PROG_OBJ, BehaviorAction.ALTER, object(TargetType.Function, ctx.fullId()), List.of());
        return null;
    }

    @Override
    public Void visitBatchStatement(BatchStatementContext ctx) {
        if (ctx.DRY() != null) {
            behavior.setStatementType(SplitQueryType.PERFORMANCE);
            for (BehaviorObject table : tables(ctx)) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, table, List.of());
            }
            return null;
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCalibrateResource(CalibrateResourceContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ANALYZE, objects.instanceObject(TargetType.ResourceGroup, ctx), List.of());
        return null;
    }

    @Override
    public Void visitAddQueryWatch(AddQueryWatchContext ctx) {
        List<BehaviorObject> groups = new ArrayList<>();
        for (ResourceGroupNameContext group : descendants(ctx, ResourceGroupNameContext.class)) {
            groups.add(objects.instanceObject(TargetType.ResourceGroup, group, unquote(text(group))));
        }
        add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.CREATE, objects.instanceObject(TargetType.Policy, ctx), groups);
        return null;
    }

    @Override
    public Void visitRemoveQueryWatch(RemoveQueryWatchContext ctx) {
        List<BehaviorObject> groups = new ArrayList<>();
        if (ctx.resourceGroupName() != null) {
            groups.add(objects.instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))));
        }
        add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx), groups);
        return null;
    }

    @Override
    public Void visitAlterSequence(AlterSequenceContext ctx) {
        add(SplitQueryType.ALTER_SEQUENCE, BehaviorAction.ALTER, object(TargetType.Sequence, ctx.sequence_name().fullId()), List.of());
        return null;
    }
}

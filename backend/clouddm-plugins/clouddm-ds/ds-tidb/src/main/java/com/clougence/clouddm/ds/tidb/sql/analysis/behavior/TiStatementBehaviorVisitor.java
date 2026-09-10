/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.util.*;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiFunctionRegistry;
import com.clougence.clouddm.ds.tidb.sql.parser.TiQueryAnalysis;
import com.clougence.clouddm.ds.tidb.sql.parser.TiRoutineAnalysis;
import com.clougence.clouddm.ds.tidb.sql.parser.TiSplitVisitor;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
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
        if (behavior.getStatementType() == SplitQueryType.SELECT && TiQueryAnalysis.isMetadataOnly(root)) {
            behavior.setStatementType(SplitQueryType.METADATA);
        }
        deduplicateRelations();
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
                add(SplitQueryType.SELECT, TiFunctionRegistry.behavior(token.getText(), false), objects.object(TargetType.Function, token, List.of(unquote(token.getText()))), List
                    .of());
                continue;
            }
            if (!(context instanceof FunctionCallContext function)) {
                continue;
            }
            int tokenType = function.getStart().getType();
            if (tokenType == TiDBParser.ROW || tokenType == TiDBParser.VALUES) {
                continue;
            }
            if (function instanceof UdfFunctionCallContext udf) {
                var name = udf.customFunctionName().fullId();
                String functionName = text(name.uid(name.uid().size() - 1));
                add(SplitQueryType.SELECT, TiFunctionRegistry.behavior(functionName, name.uid().size() > 1), object(TargetType.Function, name), List.of());
            } else {
                if (function instanceof SpecificFunctionCallContext specific
                    && (specific.specificFunction() instanceof CaseFunctionCallContext || specific.specificFunction() instanceof SpecialTimeCallContext)) {
                    continue;
                }
                var token = function.getStart();
                add(SplitQueryType.SELECT, TiFunctionRegistry.behavior(token.getText(), false), objects.object(TargetType.Function, token, List.of(unquote(token.getText()))), List
                    .of());
            }
            String sequenceFunction = function.getStart().getText().toUpperCase(Locale.ROOT);
            if (Set.of("NEXTVAL", "LASTVAL", "SETVAL").contains(sequenceFunction)) {
                List<FunctionArgContext> arguments = descendants(function, FunctionArgContext.class);
                if (!arguments.isEmpty() && arguments.get(0).fullColumnName() != null) {
                    FullColumnNameContext sequence = arguments.get(0).fullColumnName();
                    List<String> names = descendants(sequence, UidContext.class).stream().map(this::text).map(this::unquote).toList();
                    BehaviorAction action = BehaviorAction.READ;
                    if ("SETVAL".equals(sequenceFunction)) {
                        action = BehaviorAction.UPDATE;
                    }
                    add(SplitQueryType.SELECT, action, objects.object(TargetType.Sequence, sequence, names), List.of());
                }
            }
        }
    }

    @Override
    public Void visitChildren(RuleNode ctx) {
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
                add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, file, tables(select));
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
        for (AssignmentFieldContext field : ctx.assignmentField()) {
            visit(field);
        }
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
        for (AssignmentFieldContext field : ctx.assignmentField()) {
            visit(field);
        }
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
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        BehaviorObject owner = table(ctx.tableName());
        int relationStart = behavior.getRelations().size();
        boolean altersTable = ctx.alterSpecification().isEmpty();
        for (AlterSpecificationContext clause : ctx.alterSpecification()) {
            if (clause instanceof AlterByAddColumnContext || clause instanceof AlterByAddColumnsContext) {
                addTableConstraints(clause, ctx.tableName(), SplitQueryType.ALTER_TABLE);
                altersTable = true;
                continue;
            }
            if (clause instanceof AlterByExchangePartitionContext exchange) {
                add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, table(exchange.tableName()), List.of());
                altersTable = true;
                continue;
            }
            if (clause instanceof AlterByRenameIndexContext rename) {
                List<String> names = new ArrayList<>(ctx.tableName().fullId().uid().stream().map(this::text).map(this::unquote).toList());
                names.set(names.size() - 1, unquote(text(rename.uid(0))));
                var subject = objects.object(TargetType.Index, rename.uid(0), names);
                names.set(names.size() - 1, unquote(text(rename.uid(1))));
                var destination = objects.object(TargetType.Index, rename.uid(1), names);
                add(SplitQueryType.ALTER_TABLE, BehaviorAction.RENAME, subject, List.of(destination, owner));
                continue;
            }
            if (clause instanceof AlterByCompactContext) {
                add(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, owner, List.of());
                continue;
            }
            if (clause instanceof AlterByRenameContext rename) {
                add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, owner, List.of(table(rename.tableName())));
                continue;
            }
            ParserRuleContext name = null;
            TargetType targetType = TargetType.Index;
            BehaviorAction action = BehaviorAction.CREATE;
            if (clause instanceof AlterByAddStatisticsContext stats) {
                name = stats.uid();
                targetType = TargetType.Statistics;
            } else if (clause instanceof AlterByDropStatisticsContext stats) {
                name = stats.uid();
                targetType = TargetType.Statistics;
                action = BehaviorAction.DROP;
            } else if (clause instanceof AlterByAddIndexContext index) {
                name = index.indexName();
            } else if (clause instanceof AlterByAddUniqueKeyContext index) {
                name = index.indexName();
                targetType = TargetType.Constraint;
            } else if (clause instanceof AlterByAddSpecialIndexContext index) {
                name = index.indexName();
            } else if (clause instanceof AlterByAlterIndexVisibilityContext index) {
                name = index.uid();
                action = BehaviorAction.ALTER;
            } else if (clause instanceof AlterConstraintEnforcementContext constraint) {
                name = constraint.uid();
                targetType = TargetType.Constraint;
                action = BehaviorAction.ALTER;
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
                String ownerPath = owner.getObjectPath();
                subject.setObjectPath(ownerPath.substring(0, ownerPath.lastIndexOf('/', ownerPath.length() - 2) + 1));
            } else {
                List<String> names = new ArrayList<>(ctx.tableName().fullId().uid().stream().map(this::text).map(this::unquote).toList());
                names.set(names.size() - 1, unquote(text(name)));
                subject = objects.object(targetType, name, names);
            }
            List<BehaviorObject> targets = new ArrayList<>();
            targets.add(owner);
            targets.addAll(tables(clause));
            add(SplitQueryType.ALTER_TABLE, action, subject, targets);
        }
        if (altersTable || ctx.partitionDefinitions() != null || ctx.REMOVE() != null) {
            BehaviorRelation relation = add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, owner, policies(ctx));
            behavior.getRelations().remove(relation);
            behavior.getRelations().add(relationStart, relation);
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
            if (TiRoutineAnalysis.isRoutineVariable(variable)) {
                continue;
            }
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
                add(SplitQueryType.GRANT, BehaviorAction.GRANT, role(role), targets);
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
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, role(role), targets);
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
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, role(role), List.of());
        }
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        for (RoleNameContext role : ctx.roleName()) {
            add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, role(role), List.of());
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
        if (ctx.describeObjectClause() instanceof DescribeDigestContext digest) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.ANALYZE, objects
                .instanceObject(TargetType.Query, digest.STRING_LITERAL().getSymbol(), unquote(digest.STRING_LITERAL().getText())), List.of());
            return null;
        }
        if (ctx.analyze != null) {
            return visitChildren(ctx);
        }
        behavior.setStatementType(SplitQueryType.PERFORMANCE);
        if (ctx.describeObjectClause() instanceof DescribeConnectionContext connection) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, connection.decimalLiteral(), text(connection.decimalLiteral())), List
                .of());
        }
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
        TargetType type = switch (ctx.namedEntity.getText().toUpperCase(Locale.ROOT)) {
            case "EVENT" -> TargetType.Event;
            case "FUNCTION" -> TargetType.Function;
            case "PROCEDURE" -> TargetType.Procedure;
            case "TRIGGER" -> TargetType.Trigger;
            case "VIEW" -> TargetType.View;
            case "SEQUENCE" -> TargetType.Sequence;
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
        if (ctx.CURRENT_USER() != null) {
            target = objects.instanceObject(TargetType.UserOrRole, ctx.CURRENT_USER().getSymbol());
        } else if (ctx.account == null) {
            target = objects.instanceObject(TargetType.UserOrRole, ctx);
        } else {
            target = user(TargetType.UserOrRole, ctx.account);
        }
        List<BehaviorObject> roles = new ArrayList<>();
        if (ctx.grantRoleList() != null) {
            for (UserNameContext role : ctx.grantRoleList().userName()) {
                roles.add(user(TargetType.Role, role));
            }
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, target, roles);
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
    public Void visitFlashbackTable(FlashbackTableContext ctx) {
        for (TableNameContext source : ctx.tableName()) {
            List<BehaviorObject> targets = new ArrayList<>();
            if (ctx.newName != null) {
                List<String> names = new ArrayList<>(source.fullId().uid().stream().map(this::text).map(this::unquote).toList());
                names.set(names.size() - 1, unquote(text(ctx.newName)));
                targets.add(objects.object(TargetType.Table, ctx.newName, names));
            }
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RESTORE, table(source), targets);
        }
        return null;
    }

    @Override
    public Void visitFlashbackDatabase(FlashbackDatabaseContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.newName != null) {
            targets.add(objects.object(TargetType.Schema, ctx.newName, List.of(unquote(text(ctx.newName)))));
        }
        add(SplitQueryType.ADMIN, BehaviorAction.RESTORE, objects.object(TargetType.Schema, ctx.name, List.of(unquote(text(ctx.name)))), targets);
        return null;
    }

    @Override
    public Void visitFlashbackCluster(FlashbackClusterContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.RESTORE, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitIndexAdviseStatement(IndexAdviseStatementContext ctx) {
        BehaviorObject source = file(ctx.filename);
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ANALYZE, objects.unnamedObject(TargetType.Query, ctx, UmiTypes.Schema), List.of(source));
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    @Override
    public Void visitPlanReplayerDump(PlanReplayerDumpContext ctx) {
        BehaviorObject query = objects.unnamedObject(TargetType.Query, ctx, UmiTypes.Schema);
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ANALYZE, query, List.of());
        if (ctx.bindableStatement() != null) {
            if (ctx.ANALYZE() != null) {
                visit(ctx.bindableStatement());
            } else {
                for (BehaviorObject table : tables(ctx.bindableStatement())) {
                    add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.READ, table, List.of());
                }
            }
        } else if (ctx.filename != null) {
            BehaviorObject source = file(ctx.filename);
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.READ, source, List.of());
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.UNSAFE, source, List.of());
        } else if (!ctx.STRING_LITERAL().isEmpty()) {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.UNSAFE, query, List.of());
        }
        behavior.setStatementType(SplitQueryType.ADMIN_PERFORMANCE);
        return null;
    }

    @Override
    public Void visitPlanReplayerLoad(PlanReplayerLoadContext ctx) {
        BehaviorObject source = file(ctx.filename);
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESTORE, objects.instanceObject(TargetType.Instance, ctx), List.of(source));
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    @Override
    public Void visitPlanReplayerCapture(PlanReplayerCaptureContext ctx) {
        BehaviorAction action = BehaviorAction.START;
        if (ctx.REMOVE() != null) {
            action = BehaviorAction.STOP;
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, action, objects.instanceObject(TargetType.Query, ctx.sqlDigest, unquote(ctx.sqlDigest.getText())), List
            .of(objects.instanceObject(TargetType.Query, ctx.planDigest, unquote(ctx.planDigest.getText()))));
        return null;
    }

    @Override
    public Void visitAdminShowDdl(AdminShowDdlContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.DDL().getSymbol()), List.of());
        if (ctx.whereClause() != null) {
            visit(ctx.whereClause());
        }
        return null;
    }

    @Override
    public Void visitAdminShowNextRowId(AdminShowNextRowIdContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitAdminShowSlow(AdminShowSlowContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx.SLOW().getSymbol()), List.of());
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
        addTableConstraints(ctx.createDefinitions(), ctx.tableName(), SplitQueryType.CREATE_TABLE);
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
        addTableConstraints(ctx.createDefinitions(), ctx.tableName(), SplitQueryType.CREATE_TABLE);
        return null;
    }

    private void addTableConstraints(ParseTree definitions, TableNameContext owner, SplitQueryType type) {
        for (ParserRuleContext definition : descendants(definitions, ParserRuleContext.class)) {
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
                String ownerPath = table(owner).getObjectPath();
                subject.setObjectPath(ownerPath.substring(0, ownerPath.lastIndexOf('/', ownerPath.length() - 2) + 1));
            } else {
                List<String> names = new ArrayList<>(owner.fullId().uid().stream().map(this::text).map(this::unquote).toList());
                names.set(names.size() - 1, unquote(text(name)));
                subject = objects.object(targetType, name, names);
            }
            List<BehaviorObject> targets = new ArrayList<>();
            targets.add(table(owner));
            targets.addAll(tables(definition));
            add(type, BehaviorAction.CREATE, subject, targets);
        }
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
        List<BehaviorObject> sources = new ArrayList<>(tables(ctx.whereClause()));
        sources.addAll(tables(ctx.withClause()));
        for (UpdatedElementContext assignment : ctx.updatedElement()) {
            sources.addAll(tables(assignment.expression()));
        }
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(ctx.tableName()), sources);
        return null;
    }

    @Override
    public Void visitSingleDeleteStatement(SingleDeleteStatementContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>(tables(ctx.withClause()));
        sources.addAll(tables(ctx.whereClause()));
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, table(ctx.tableName()), sources);
        return null;
    }

    @Override
    public Void visitMultipleDeleteStatement(MultipleDeleteStatementContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>(tables(ctx.withClause()));
        sources.addAll(tables(ctx.tableSources()));
        sources.addAll(tables(ctx.expression()));
        for (TableNameContext target : ctx.tableName()) {
            add(SplitQueryType.DELETE, BehaviorAction.DELETE, table(resolveTarget(target, ctx.tableSources())), sources);
        }
        return null;
    }

    @Override
    public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
        List<AtomTableItemContext> inputs = descendants(ctx.tableSources(), AtomTableItemContext.class);
        inputs.removeIf(input -> {
            for (ParseTree scope = input.getParent(); scope != ctx.tableSources(); scope = scope.getParent()) {
                if (scope instanceof SelectStatementContext) {
                    return true;
                }
            }
            return false;
        });
        Set<TableNameContext> modified = new LinkedHashSet<>();
        List<BehaviorObject> sources = new ArrayList<>(tables(ctx.withClause()));
        if (tables(ctx.tableSources()).size() > 1) {
            sources.addAll(tables(ctx.tableSources()));
        }
        sources.addAll(tables(ctx.whereClause()));
        for (UpdatedElementContext assignment : ctx.updatedElement()) {
            sources.addAll(tables(assignment.expression()));
            List<UidContext> parts = descendants(assignment.fullColumnName(), UidContext.class);
            if (parts.size() == 1) {
                for (AtomTableItemContext input : inputs) {
                    modified.add(input.tableName());
                }
                continue;
            }
            String qualifier = parts.subList(0, parts.size() - 1).stream().map(this::text).map(this::unquote).collect(Collectors.joining("."));
            for (AtomTableItemContext input : inputs) {
                List<UidContext> tableParts = input.tableName().fullId().uid();
                String name = tableParts.stream().map(this::text).map(this::unquote).collect(Collectors.joining("."));
                if (qualifier.equalsIgnoreCase(name) || qualifier.equalsIgnoreCase(unquote(text(tableParts.get(tableParts.size() - 1))))
                    || input.aliasName() != null && qualifier.equalsIgnoreCase(unquote(text(input.aliasName())))) {
                    modified.add(input.tableName());
                }
            }
        }
        for (TableNameContext target : modified) {
            add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, table(target), sources);
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
        if (context == null || TiQueryAnalysis.isCte(context)) {
            return null;
        }
        if (context.fullId().getStart().getText().equals("*")) {
            return objects.object(TargetType.Table, context.fullId(), List.of("*", unquote(text(context.fullId().uid(0)))));
        }
        return object(TargetType.Table, context.fullId());
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
        add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.SWITCH, objects
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
        if (ctx.filename == null) {
            add(SplitQueryType.INSERT, BehaviorAction.INSERT, table(ctx.tableName()), targets);
        } else {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, table(ctx.tableName()), targets);
        }
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
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx.fullId()), List.of());
        visit(ctx.routineBody());
        behavior.setStatementType(SplitQueryType.CREATE_PROG_OBJ);
        return null;
    }

    @Override
    public Void visitCreateFunction(CreateFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.fullId()), List.of());
        visit(ctx.routineBody());
        behavior.setStatementType(SplitQueryType.CREATE_PROG_OBJ);
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
        for (QueryWatchOptionContext option : ctx.queryWatchOption()) {
            if (option.LOCAL_ID() != null) {
                Token token = option.LOCAL_ID().getSymbol();
                add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, token, unquote(token.getText().substring(1))), List
                    .of());
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitRemoveQueryWatch(RemoveQueryWatchContext ctx) {
        List<BehaviorObject> groups = new ArrayList<>();
        if (ctx.resourceGroupName() != null) {
            groups.add(objects.instanceObject(TargetType.ResourceGroup, ctx.resourceGroupName(), unquote(text(ctx.resourceGroupName()))));
        }
        add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx), groups);
        if (ctx.LOCAL_ID() != null) {
            Token token = ctx.LOCAL_ID().getSymbol();
            add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, token, unquote(token.getText().substring(1))), List.of());
        }
        return null;
    }

    @Override
    public Void visitAlterSequence(AlterSequenceContext ctx) {
        add(SplitQueryType.ALTER_SEQUENCE, BehaviorAction.ALTER, object(TargetType.Sequence, ctx.sequence_name().fullId()), List.of());
        return null;
    }

    @Override
    public Void visitAdminRepairStatement(AdminRepairStatementContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.REPAIR, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitShowPlacement(ShowPlacementContext ctx) {
        BehaviorObject subject = objects.instanceObject(TargetType.Policy, ctx.PLACEMENT().getSymbol());
        if (ctx.tableName() != null) {
            subject = table(ctx.tableName());
        } else if (ctx.DATABASE() != null || ctx.SCHEMA() != null) {
            subject = objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid()))));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, subject, List.of());
        return null;
    }

    @Override
    public Void visitShowTableRegions(ShowTableRegionsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitSetRole(SetRoleContext ctx) {
        List<BehaviorObject> roles = new ArrayList<>();
        for (UserNameContext role : ctx.roles) {
            roles.add(user(TargetType.Role, role));
        }
        add(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, objects.instanceObject(TargetType.Session, ctx.ROLE().getSymbol()), roles);
        return null;
    }

    @Override
    public Void visitSetDefaultRole(SetDefaultRoleContext ctx) {
        List<BehaviorObject> roles = new ArrayList<>();
        for (UserNameContext role : ctx.roles) {
            roles.add(user(TargetType.Role, role));
        }
        for (UserNameContext account : ctx.users) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.CONFIGURE, user(TargetType.User, account), roles);
        }
        return null;
    }

    @Override
    public Void visitTraceStatement(TraceStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.ANALYZE, objects.unnamedObject(TargetType.Query, ctx, UmiTypes.Schema), List.of());
        if (ctx.PLAN() != null) {
            for (TableNameContext source : descendants(ctx.bindableStatement(), TableNameContext.class)) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, table(source), List.of());
            }
        } else {
            visitChildren(ctx);
        }
        behavior.setStatementType(SplitQueryType.PERFORMANCE);
        return null;
    }

    @Override
    public Void visitBackupData(BackupDataContext ctx) {
        BehaviorObject destination = file(ctx.filename);
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, destination, backupObjects(ctx.brieTables()));
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, destination, List.of());
        return null;
    }

    @Override
    public Void visitRestoreData(RestoreDataContext ctx) {
        BehaviorObject source = file(ctx.filename);
        List<BehaviorObject> destinations;
        if (ctx.brieTables() == null) {
            destinations = List.of(objects.instanceObject(TargetType.Instance, ctx.POINT().getSymbol()));
        } else {
            destinations = backupObjects(ctx.brieTables());
        }
        for (BehaviorObject destination : destinations) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.RESTORE, destination, List.of(source));
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    private List<BehaviorObject> backupObjects(BrieTablesContext ctx) {
        List<BehaviorObject> result = new ArrayList<>();
        if (ctx.tables() != null) {
            for (TableNameContext table : ctx.tables().tableName()) {
                result.add(table(table));
            }
        } else if (ctx.uidList() != null) {
            for (UidContext schema : ctx.uidList().uid()) {
                result.add(objects.object(TargetType.Schema, schema, List.of(unquote(text(schema)))));
            }
        } else {
            result.add(objects.instanceObject(TargetType.Instance, ctx.getStop()));
        }
        return result;
    }

    @Override
    public Void visitShowBackupJobs(ShowBackupJobsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.getStart()), List.of());
        return null;
    }

    @Override
    public Void visitShowBackupJob(ShowBackupJobContext ctx) {
        TargetType type = TargetType.Job;
        if (ctx.QUERY() != null) {
            type = TargetType.Query;
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(type, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
        return null;
    }

    @Override
    public Void visitCancelBackupJob(CancelBackupJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
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
        add(SplitQueryType.TRANSACTION, BehaviorAction.RESTORE, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSetTransaction(SetTransactionContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitLockTables(LockTablesContext ctx) {
        for (LockTableElementContext lock : ctx.lockTableElement()) {
            add(SplitQueryType.SESSION_LOCK, BehaviorAction.LOCK, table(lock.tableName()), List.of());
        }
        return null;
    }

    @Override
    public Void visitUnlockTables(UnlockTablesContext ctx) {
        add(SplitQueryType.SESSION_LOCK, BehaviorAction.UNLOCK, objects.instanceObject(TargetType.Session, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSetNames(SetNamesContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.NAMES().getSymbol(), ctx.getStop(), "NAMES"), List
            .of());
        return null;
    }

    @Override
    public Void visitSetCharset(SetCharsetContext ctx) {
        Token name;
        if (ctx.CHARSET() != null) {
            name = ctx.CHARSET().getSymbol();
        } else if (ctx.CHAR() != null) {
            name = ctx.CHAR().getSymbol();
        } else {
            name = ctx.CHARACTER().getSymbol();
        }
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, name, ctx.getStop(), "CHARACTER SET"), List.of());
        return null;
    }

    @Override
    public Void visitSetPassword(SetPasswordContext ctx) {
        var setting = ctx.setPasswordStatement();
        BehaviorObject subject = objects.instanceObject(TargetType.User, setting.PASSWORD().getSymbol());
        if (setting.userName() != null) {
            subject = user(TargetType.User, setting.userName());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.CONFIGURE, subject, List.of());
        return null;
    }

    @Override
    public Void visitRenameUser(RenameUserContext ctx) {
        for (RenameUserClauseContext rename : ctx.renameUserClause()) {
            add(SplitQueryType.RENAME_USER, BehaviorAction.RENAME, user(TargetType.User, rename.fromFirst), List.of(user(TargetType.User, rename.toFirst)));
        }
        return null;
    }

    @Override
    public Void visitShowErrors(ShowErrorsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowCountErrors(ShowCountErrorsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowCharset(ShowCharsetContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowPlugins(ShowPluginsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Library, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowPrivileges(ShowPrivilegesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowEngines(ShowEnginesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowStatus(ShowStatusContext ctx) {
        add(SplitQueryType.LOG_READ, BehaviorAction.READ, objects.instanceObject(TargetType.Log, ctx), List.of());
        return null;
    }

    @Override
    public Void visitKillStatement(KillStatementContext ctx) {
        TargetType target = TargetType.Session;
        if (ctx.connectionFormat != null && ctx.connectionFormat.getType() == TiDBParser.QUERY) {
            target = TargetType.Query;
        }
        if (ctx.CONNECTION_ID() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, objects.instanceObject(target, ctx), List.of());
            add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, ctx.CONNECTION_ID().getSymbol(), List.of(ctx.CONNECTION_ID().getText())), List
                .of());
        }
        for (DecimalLiteralContext id : ctx.decimalLiteral()) {
            add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, objects.instanceObject(target, id, text(id)), List.of());
        }
        return null;
    }

    @Override
    public Void visitPrepareStatement(PrepareStatementContext ctx) {
        var subject = objects.instanceObject(TargetType.PrepareStatement, ctx.uid(), unquote(text(ctx.uid())));
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.variable != null) {
            targets.add(objects.instanceObject(TargetType.ConfigKey, ctx.variable, unquote(ctx.variable.getText().substring(1))));
        }
        add(SplitQueryType.UNSAFE, BehaviorAction.CREATE, subject, targets);
        add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, subject, List.of());
        return null;
    }

    @Override
    public Void visitExecuteStatement(ExecuteStatementContext ctx) {
        var subject = objects.instanceObject(TargetType.PrepareStatement, ctx.uid(), unquote(text(ctx.uid())));
        add(SplitQueryType.UNSAFE, BehaviorAction.CALL, subject, List.of());
        add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, subject, List.of());
        if (ctx.userVariables() != null) {
            for (var variable : ctx.userVariables().LOCAL_ID()) {
                Token token = variable.getSymbol();
                add(SplitQueryType.UNSAFE, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, token, unquote(token.getText().substring(1))), List.of());
            }
        }
        return null;
    }

    @Override
    public Void visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        add(SplitQueryType.UNSAFE, BehaviorAction.DROP, objects.instanceObject(TargetType.PrepareStatement, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitSetConfig(SetConfigContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.STRING_LITERAL() != null) {
            var host = ctx.STRING_LITERAL().getSymbol();
            targets.add(objects.instanceObject(TargetType.Machine, host, unquote(host.getText())));
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, objects
            .instanceObject(TargetType.ConfigKey, ctx.configItemName(), unquote(text(ctx.configItemName()))), targets);
        return visitChildren(ctx);
    }

    @Override
    public Void visitShowConfig(ShowConfigContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.CONFIG().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowStatistics(ShowStatisticsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Statistics, ctx), List.of());
        return null;
    }

    @Override
    public Void visitCreateStatistics(CreateStatisticsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CREATE, objects.object(TargetType.Statistics, ctx.uid(), List.of(unquote(text(ctx.uid())))), List
            .of(table(ctx.tableName())));
        return null;
    }

    @Override
    public Void visitDropStatistics(DropStatisticsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, objects.object(TargetType.Statistics, ctx.uid(), List.of(unquote(text(ctx.uid())))), List.of());
        return null;
    }

    @Override
    public Void visitDropStats(DropStatsContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, objects
                .object(TargetType.Statistics, table, table.fullId().uid().stream().map(this::text).map(this::unquote).toList()), List.of(table(table)));
        }
        return null;
    }

    @Override
    public Void visitLoadStats(LoadStatsContext ctx) {
        var source = file(ctx.STRING_LITERAL().getSymbol());
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.LOAD, objects.instanceObject(TargetType.Statistics, ctx), List.of(source));
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    @Override
    public Void visitRecoverTable(RecoverTableContext ctx) {
        BehaviorObject subject = objects.unnamedObject(TargetType.Table, ctx.TABLE().getSymbol(), UmiTypes.Schema);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.tableName() != null) {
            subject = table(ctx.tableName());
        }
        if (ctx.decimalLiteral() != null) {
            targets.add(objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())));
        }
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RECOVER, subject, targets);
        return null;
    }

    @Override
    public Void visitAdminCheckTable(AdminCheckTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            BehaviorAction action = BehaviorAction.VALIDATE;
            if (ctx.CHECKSUM() != null) {
                action = BehaviorAction.CHECKSUM;
            }
            add(SplitQueryType.ADMIN_TABLE, action, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitAdminReload(AdminReloadContext ctx) {
        TargetType target = TargetType.Policy;
        if (ctx.STATS_EXTENDED() != null || ctx.STATISTICS() != null) {
            target = TargetType.Statistics;
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.REFRESH, objects.instanceObject(target, ctx.getStop()), List.of());
        return null;
    }

    @Override
    public Void visitFlushStatement(FlushStatementContext ctx) {
        for (FlushOptionContext option : ctx.flushOption()) {
            if (option instanceof TableFlushOptionContext tableFlush) {
                for (TableNameContext table : tableFlush.tables().tableName()) {
                    if (tableFlush.flushTableOption() != null && tableFlush.flushTableOption().EXPORT() != null) {
                        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, table(table), List.of());
                        add(SplitQueryType.DATA_EXPORT, BehaviorAction.LOCK, table(table), List.of());
                        continue;
                    }
                    add(SplitQueryType.ADMIN_TABLE, BehaviorAction.FLUSH, table(table), List.of());
                    if (tableFlush.flushTableOption() != null && tableFlush.flushTableOption().LOCK() != null) {
                        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.LOCK, table(table), List.of());
                    }
                }
            } else if (option instanceof SimpleFlushOptionContext simple && (simple.TABLES() != null || simple.TABLE() != null)) {
                var subject = objects.unnamedObject(TargetType.Table, simple.getStart(), UmiTypes.Schema);
                add(SplitQueryType.ADMIN_TABLE, BehaviorAction.FLUSH, subject, List.of());
                if (simple.LOCK() != null) {
                    add(SplitQueryType.ADMIN_TABLE, BehaviorAction.LOCK, subject, List.of());
                }
            } else {
                TargetType target = TargetType.ConfigKey;
                SplitQueryType type = SplitQueryType.SYSTEM_SETTING_WRITE;
                if (option instanceof ChannelFlushOptionContext || option instanceof SimpleFlushOptionContext simple && simple.LOGS() != null) {
                    target = TargetType.Log;
                    type = SplitQueryType.MAINTAIN_LOG;
                } else if (option instanceof SimpleFlushOptionContext simple
                           && (simple.STATUS() != null || simple.CLIENT_ERRORS_SUMMARY() != null || simple.HOSTS() != null || simple.OPTIMIZER_COSTS() != null
                               || simple.QUERY() != null || simple.USER_RESOURCES() != null)) {
                    if (simple.STATUS() != null || simple.CLIENT_ERRORS_SUMMARY() != null) {
                        target = TargetType.Statistics;
                    }
                    type = SplitQueryType.ADMIN_PERFORMANCE;
                }
                add(type, BehaviorAction.FLUSH, objects.instanceObject(target, option), List.of());
            }
        }
        return null;
    }

    @Override
    public Void visitGrantProxy(GrantProxyContext ctx) {
        for (UserNameContext target : ctx.userName()) {
            if (target != ctx.fromFirst) {
                add(SplitQueryType.GRANT, BehaviorAction.GRANT, user(TargetType.User, ctx.fromFirst), List.of(user(TargetType.User, target)));
            }
        }
        return null;
    }

    @Override
    public Void visitRevokeProxy(RevokeProxyContext ctx) {
        for (UserNameContext target : ctx.userName()) {
            if (target != ctx.onUser) {
                add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, user(TargetType.User, ctx.onUser), List.of(user(TargetType.User, target)));
            }
        }
        return null;
    }

    private BehaviorObject role(RoleNameContext ctx) {
        String name = unquote(ctx.getChild(0).getText());
        if (ctx.LOCAL_ID() != null) {
            name += "@" + unquote(ctx.LOCAL_ID().getText().substring(1));
        }
        return objects.instanceObject(TargetType.Role, ctx, name);
    }

    @Override
    public Void visitSequenceValueExpressionAtom(SequenceValueExpressionAtomContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Sequence, ctx.fullId()), List.of());
        return null;
    }

    @Override
    public Void visitAdminCleanupIndex(AdminCleanupIndexContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.PURGE, objects.object(TargetType.Index, ctx.uid(), List.of(unquote(text(ctx.uid())))), List.of(table(ctx.tableName())));
        return null;
    }

    @Override
    public Void visitAdminFlushPlanCache(AdminFlushPlanCacheContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.FLUSH, objects.instanceObject(TargetType.ConfigKey, ctx.PLAN_CACHE().getSymbol(), ctx.PLAN_CACHE().getText()), List
            .of());
        return null;
    }

    @Override
    public Void visitShowImportJobs(ShowImportJobsContext ctx) {
        BehaviorObject job = objects.instanceObject(TargetType.Job, ctx);
        if (ctx.decimalLiteral() != null) {
            job = objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral()));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, job, List.of());
        return null;
    }

    @Override
    public Void visitCancelImportJob(CancelImportJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
        return null;
    }

    @Override
    public Void visitAlterDatabaseReplica(AlterDatabaseReplicaContext ctx) {
        add(SplitQueryType.ALTER_SCHEMA, BehaviorAction.CONFIGURE, objects.object(TargetType.Schema, ctx.databaseName(), List.of(unquote(text(ctx.databaseName())))), List.of());
        return null;
    }

    @Override
    public Void visitRecommendIndex(RecommendIndexContext ctx) {
        BehaviorAction action = BehaviorAction.ANALYZE;
        if (ctx.APPLY() != null) {
            action = BehaviorAction.APPLY;
        } else if (ctx.SET() != null || ctx.IGNORE() != null) {
            action = BehaviorAction.CONFIGURE;
        } else if (ctx.SHOW() != null) {
            action = BehaviorAction.READ;
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, action, objects.instanceObject(TargetType.Query, ctx), List.of());
        return null;
    }

    private void deduplicateRelations() {
        Map<List<Object>, BehaviorRelation> relations = new LinkedHashMap<>();
        for (BehaviorRelation relation : behavior.getRelations()) {
            Map<List<Object>, BehaviorObject> targets = new LinkedHashMap<>();
            for (BehaviorObject target : relation.getTarget()) {
                targets.putIfAbsent(List.of(target.getObjectType(), target.getObjectPath()), target);
            }
            relation.getTarget().clear();
            relation.getTarget().addAll(targets.values());
            List<Object> identity = new ArrayList<>();
            identity.add(relation.getAction());
            identity.add(relation.getSubject().getObjectType());
            identity.add(relation.getSubject().getObjectPath());
            identity.add(relation.getInsertRows());
            identity.add(targets.keySet());
            relations.putIfAbsent(identity, relation);
        }
        behavior.getRelations().clear();
        behavior.getRelations().addAll(relations.values());
    }

    @Override
    public Void visitCreateLegacyImport(CreateLegacyImportContext ctx) {
        var source = file(ctx.STRING_LITERAL().getSymbol());
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.CREATE, objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid()))), List.of(source));
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    @Override
    public Void visitStopLegacyImport(StopLegacyImportContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitResumeLegacyImport(ResumeLegacyImportContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.START, objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitDropLegacyImport(DropLegacyImportContext ctx) {
        add(SplitQueryType.DROP_JOB, BehaviorAction.DROP, objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitShowCreateLegacyImport(ShowCreateLegacyImportContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid()))), List.of());
        return null;
    }

    @Override
    public Void visitAdminCleanupTableLock(AdminCleanupTableLockContext ctx) {
        for (TableNameContext name : ctx.tables().tableName()) {
            add(SplitQueryType.SESSION_LOCK, BehaviorAction.UNLOCK, table(name), List.of());
        }
        return null;
    }

    @Override
    public Void visitAdminCheckIndex(AdminCheckIndexContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.VALIDATE, objects.object(TargetType.Index, ctx.uid(), List.of(unquote(text(ctx.uid())))), List.of(table(ctx.tableName())));
        return null;
    }

    @Override
    public Void visitAdminEvolveBindings(AdminEvolveBindingsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.REFRESH, objects.instanceObject(TargetType.Policy, ctx.BINDINGS().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitLockStatistics(LockStatisticsContext ctx) {
        BehaviorAction action = BehaviorAction.LOCK;
        if (ctx.UNLOCK() != null) {
            action = BehaviorAction.UNLOCK;
        }
        for (TableNameContext name : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_PERFORMANCE, action, objects
                .object(TargetType.Statistics, name, name.fullId().uid().stream().map(this::text).map(this::unquote).toList()), List.of(table(name)));
        }
        return null;
    }

    @Override
    public Void visitFlushStatsDelta(FlushStatsDeltaContext ctx) {
        for (StatsObjectContext source : ctx.statsObject()) {
            BehaviorObject target = statisticsTarget(source);
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.FLUSH, objects.instanceObject(TargetType.Statistics, ctx.STATS_DELTA().getSymbol()), List.of(target));
        }
        return null;
    }

    @Override
    public Void visitAlterRange(AlterRangeContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.uid(), unquote(text(ctx.uid()))), policies(ctx));
        return null;
    }

    @Override
    public Void visitShowNextRowId(ShowNextRowIdContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitAlterInstance(AlterInstanceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShutdownStatement(ShutdownStatementContext ctx) {
        add(SplitQueryType.UNSAFE, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            var token = ctx.LOCAL_ID().getSymbol();
            add(SplitQueryType.SELECT, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, token, unquote(token.getText().substring(1))), List.of());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitDoStatement(DoStatementContext ctx) {
        behavior.setStatementType(SplitQueryType.BLOCK);
        return visitChildren(ctx);
    }

    @Override
    public Void visitHelpStatement(HelpStatementContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowProfile(ShowProfileContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowBuiltins(ShowBuiltinsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Function, ctx.BUILTINS().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitAdminManageBindings(AdminManageBindingsContext ctx) {
        BehaviorAction action = BehaviorAction.FLUSH;
        if (ctx.CAPTURE() != null) {
            action = BehaviorAction.START;
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, action, objects.instanceObject(TargetType.Policy, ctx.BINDINGS().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitAdminManageDdlJobs(AdminManageDdlJobsContext ctx) {
        BehaviorAction action = BehaviorAction.STOP;
        if (ctx.RESUME() != null) {
            action = BehaviorAction.START;
        }
        for (DecimalLiteralContext id : ctx.decimalLiteral()) {
            add(SplitQueryType.ADMIN_JOB, action, objects.instanceObject(TargetType.Job, id, text(id)), List.of());
        }
        return null;
    }

    @Override
    public Void visitAdminRecoverIndex(AdminRecoverIndexContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RECOVER, objects.object(TargetType.Index, ctx.uid(), List.of(unquote(text(ctx.uid())))), List.of(table(ctx.tableName())));
        return null;
    }

    @Override
    public Void visitAdminPlugins(AdminPluginsContext ctx) {
        BehaviorAction action = BehaviorAction.CONFIGURE;
        for (UidContext name : ctx.uidList().uid()) {
            add(SplitQueryType.ALTER_LIBRARY, action, objects.instanceObject(TargetType.Library, name, unquote(text(name))), List.of());
        }
        return null;
    }

    @Override
    public Void visitFlushPlugins(FlushPluginsContext ctx) {
        for (UidContext name : ctx.uidList().uid()) {
            add(SplitQueryType.ALTER_LIBRARY, BehaviorAction.FLUSH, objects.instanceObject(TargetType.Library, name, unquote(text(name))), List.of());
        }
        return null;
    }

    @Override
    public Void visitRestartInstance(RestartInstanceContext ctx) {
        add(SplitQueryType.UNSAFE, BehaviorAction.STOP, objects.instanceObject(TargetType.Instance, ctx), List.of());
        add(SplitQueryType.UNSAFE, BehaviorAction.START, objects.instanceObject(TargetType.Instance, ctx), List.of());
        return null;
    }

    @Override
    public Void visitMixedSetItem(MixedSetItemContext ctx) {
        VariableClauseContext variable = ctx.variableClause();
        if (variable != null) {
            if (TiRoutineAnalysis.isRoutineVariable(variable)) {
                return visitChildren(ctx);
            }
            SplitQueryType type = new TiSplitVisitor().visitMixedSetItem(ctx);
            add(type, BehaviorAction.CONFIGURE, variable(variable), List.of());
        } else {
            String name = "CHARACTER SET";
            if (ctx.NAMES() != null) {
                name = "NAMES";
            }
            add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx, name), List.of());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitSetMixed(SetMixedContext ctx) {
        visitChildren(ctx);
        behavior.setStatementType(new TiSplitVisitor().visitMixedSetItem(ctx.mixedSetItem(0)));
        return null;
    }

    @Override
    public Void visitAlterCurrentUser(AlterCurrentUserContext ctx) {
        add(SplitQueryType.ALTER_USER, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.User, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowCreateCurrentUser(ShowCreateCurrentUserContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.User, ctx), List.of());
        return null;
    }

    @Override
    public Void visitAdminResetTelemetry(AdminResetTelemetryContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.RESET, objects.instanceObject(TargetType.ConfigKey, ctx.TELEMETRY_ID().getSymbol(), "TELEMETRY_ID"), List.of());
        return null;
    }

    @Override
    public Void visitBinlogStatement(BinlogStatementContext ctx) {
        var subject = objects.instanceObject(TargetType.Replication, ctx);
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.APPLY, subject, List.of());
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.UNSAFE, subject, List.of());
        return null;
    }

    @Override
    public Void visitAlterLegacyImport(AlterLegacyImportContext ctx) {
        var job = objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid())));
        add(SplitQueryType.ALTER_JOB, BehaviorAction.ALTER, job, List.of());
        if (ctx.TRUNCATE() != null) {
            add(SplitQueryType.ALTER_JOB, BehaviorAction.PURGE, job, tables(ctx.tables()));
        }
        return null;
    }

    @Override
    public Void visitShowLegacyImport(ShowLegacyImportContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.uid(), unquote(text(ctx.uid()))), tables(ctx.tables()));
        return null;
    }

    @Override
    public Void visitShowLegacyImports(ShowLegacyImportsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.IMPORTS().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitPurgeLegacyImport(PurgeLegacyImportContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.PURGE, objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
        return null;
    }

    @Override
    public Void visitShowTelemetry(ShowTelemetryContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Statistics, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowBinlogNodes(ShowBinlogNodesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Replication, ctx), List.of());
        return null;
    }

    @Override
    public Void visitChangeBinlogNode(ChangeBinlogNodeContext ctx) {
        Token node = ctx.STRING_LITERAL(1).getSymbol();
        add(SplitQueryType.ALTER_REPLICATION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Replication, node, unquote(node.getText())), List.of());
        return null;
    }

    @Override
    public Void visitAssignmentField(AssignmentFieldContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            Token token = ctx.LOCAL_ID().getSymbol();
            add(SplitQueryType.SELECT, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, token, unquote(token.getText().substring(1))), List.of());
        }
        return null;
    }

    @Override
    public Void visitAdminAlterDdlJob(AdminAlterDdlJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
        return null;
    }

    @Override
    public Void visitCaptureTraffic(CaptureTrafficContext ctx) {
        var destination = file(ctx.STRING_LITERAL().getSymbol());
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, destination, List.of(objects.instanceObject(TargetType.Instance, ctx.TRAFFIC().getSymbol())));
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, destination, List.of());
        return null;
    }

    @Override
    public Void visitReplayTraffic(ReplayTrafficContext ctx) {
        var source = file(ctx.STRING_LITERAL().getSymbol());
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(source);
        for (TrafficReplayOptionContext option : ctx.trafficReplayOption()) {
            if (option.USER() != null) {
                Token user = option.STRING_LITERAL().getSymbol();
                targets.add(objects.instanceObject(TargetType.User, user, unquote(user.getText())));
            }
        }
        add(SplitQueryType.UNSAFE, BehaviorAction.CALL, objects.instanceObject(TargetType.Query, ctx), targets);
        add(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    @Override
    public Void visitShowTrafficJobs(ShowTrafficJobsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx), List.of());
        return null;
    }

    @Override
    public Void visitCancelTrafficJobs(CancelTrafficJobsContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx), List.of());
        return null;
    }

    @Override
    public Void visitSetAutocommit(SetAutocommitContext ctx) {
        Token name = ctx.setAutocommitStatement().AUTOCOMMIT().getSymbol();
        add(SplitQueryType.TRANSACTION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, name, name.getText()), List.of());
        return null;
    }

    @Override
    public Void visitOptimizeTable(OptimizeTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitCheckTable(CheckTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.VALIDATE, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitChecksumTable(ChecksumTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.CHECKSUM, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitRepairTable(RepairTableContext ctx) {
        for (TableNameContext table : ctx.tables().tableName()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.REPAIR, table(table), List.of());
        }
        return null;
    }

    @Override
    public Void visitShowOpenTables(ShowOpenTablesContext ctx) {
        BehaviorObject subject = objects.unnamedObject(TargetType.Table, ctx, UmiTypes.Schema);
        if (ctx.uid() != null) {
            subject = objects.object(TargetType.Schema, ctx.uid(), List.of(unquote(text(ctx.uid()))));
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject, List.of());
        return null;
    }

    @Override
    public Void visitSetSessionStates(SetSessionStatesContext ctx) {
        add(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Session, ctx.SESSION_STATES().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowSessionStates(ShowSessionStatesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Session, ctx.SESSION_STATES().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitManageLoadJob(ManageLoadJobContext ctx) {
        BehaviorAction action = BehaviorAction.STOP;
        if (ctx.DROP() != null) {
            action = BehaviorAction.DROP;
        } else if (ctx.RESUME() != null) {
            action = BehaviorAction.START;
        }
        add(SplitQueryType.ADMIN_JOB, action, objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
        return null;
    }

    @Override
    public Void visitDistributeTable(DistributeTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ALTER, table(ctx.tableName()), List.of());
        return null;
    }

    @Override
    public Void visitShowDistributionJobs(ShowDistributionJobsContext ctx) {
        BehaviorObject job = objects.instanceObject(TargetType.Job, ctx);
        if (ctx.decimalLiteral() != null) {
            job = objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral()));
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, job, List.of());
        return null;
    }

    @Override
    public Void visitCancelDistributionJob(CancelDistributionJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx.decimalLiteral(), text(ctx.decimalLiteral())), List.of());
        return null;
    }

    @Override
    public Void visitShowAffinity(ShowAffinityContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.AFFINITY().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitSetBdrRole(SetBdrRoleContext ctx) {
        add(SplitQueryType.ALTER_REPLICATION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Replication, ctx.BDR().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitUnsetBdrRole(UnsetBdrRoleContext ctx) {
        add(SplitQueryType.ALTER_REPLICATION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Replication, ctx.BDR().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowBdrRole(ShowBdrRoleContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Replication, ctx.BDR().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitShowPlan(ShowPlanContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.ANALYZE, objects
            .instanceObject(TargetType.Query, ctx.STRING_LITERAL().getSymbol(), unquote(ctx.STRING_LITERAL().getText())), List.of());
        return null;
    }

    @Override
    public Void visitCreateWorkloadSnapshot(CreateWorkloadSnapshotContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CREATE, objects.instanceObject(TargetType.Statistics, ctx.WORKLOAD().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitRefreshStats(RefreshStatsContext ctx) {
        for (StatsObjectContext source : ctx.statsObject()) {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.REFRESH, objects.instanceObject(TargetType.Statistics, ctx.STATS().getSymbol()), List
                .of(statisticsTarget(source)));
        }
        return null;
    }

    @Override
    public Void visitBackupLogs(BackupLogsContext ctx) {
        BehaviorObject destination = file(ctx.filename);
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, destination, List.of(objects.instanceObject(TargetType.Log, ctx.LOGS().getSymbol())));
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, destination, List.of());
        return null;
    }

    @Override
    public Void visitManageBackupLogs(ManageBackupLogsContext ctx) {
        BehaviorAction action = BehaviorAction.STOP;
        if (ctx.RESUME() != null) {
            action = BehaviorAction.START;
        }
        add(SplitQueryType.ADMIN_JOB, action, objects.instanceObject(TargetType.Job, ctx.LOGS().getSymbol()), List.of());
        return null;
    }

    @Override
    public Void visitPurgeBackupLogs(PurgeBackupLogsContext ctx) {
        BehaviorObject source = file(ctx.filename);
        add(SplitQueryType.MAINTAIN_LOG, BehaviorAction.PURGE, objects.instanceObject(TargetType.Log, ctx.LOGS().getSymbol()), List.of(source));
        add(SplitQueryType.MAINTAIN_LOG, BehaviorAction.UNSAFE, source, List.of());
        return null;
    }

    @Override
    public Void visitShowBackupLogs(ShowBackupLogsContext ctx) {
        if (ctx.filename != null) {
            add(SplitQueryType.LOG_READ, BehaviorAction.READ, objects.instanceObject(TargetType.Log, ctx.LOGS().getSymbol()), List.of(file(ctx.filename)));
            add(SplitQueryType.LOG_READ, BehaviorAction.UNSAFE, file(ctx.filename), List.of());
        } else {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.LOGS().getSymbol()), List.of());
        }
        return null;
    }

    @Override
    public Void visitShowTableDistributions(ShowTableDistributionsContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, table(ctx.tableName()), List.of());
        return null;
    }

    private BehaviorObject statisticsTarget(StatsObjectContext source) {
        if (source.tableName() != null) {
            return table(source.tableName());
        }
        if (source.uid() != null) {
            return objects.object(TargetType.Schema, source.uid(), List.of(unquote(text(source.uid()))));
        }
        return objects.instanceObject(TargetType.Instance, source);
    }

    @Override
    public Void visitSavepointStatement(SavepointStatementContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitReleaseStatement(ReleaseStatementContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitRollbackStatement(RollbackStatementContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.RESTORE, objects.instanceObject(TargetType.Transaction, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowProfiles(ShowProfilesContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Query, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Replication, ctx), List.of());
        return null;
    }

    @Override
    public Void visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Replication, ctx), List.of());
        return null;
    }
}

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
package com.clougence.sql.hana.analysis;

import java.util.*;
import java.util.function.BiFunction;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.execute.MetaIndexedObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.hana.i18n.HanaSqlI18nKeys;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import com.clougence.sql.hana.parser.antlr.HanaParserBaseVisitor;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

final class HanaBehaviorParserVisitor extends HanaParserBaseVisitor<Void> {
    private static final Comparator<BehaviorObject>                            SECURITY_SOURCE_ORDER = Comparator.comparingInt(BehaviorObject::getStartLine)
        .thenComparingInt(BehaviorObject::getStartColumn)
        .thenComparingInt(BehaviorObject::getEndLine)
        .thenComparingInt(BehaviorObject::getEndColumn);

    private final HanaAnalysisResult                                           result                = new HanaAnalysisResult();
    private final SplitScript                                                  script;
    private final Map<UmiTypes, Object>                                        levels;
    private final RdbBehaviorObjectFactory                                     objects;
    private final Deque<Map<String, List<BehaviorObject>>>                     cteScopes             = new ArrayDeque<>();
    private final Deque<Map<String, List<BehaviorObject>>>                     variables             = new ArrayDeque<>();
    private final BiFunction<Map<UmiTypes, Object>, String, MetaIndexedObject> indexedObjectResolver;
    private boolean                                                            explaining;

    HanaBehaviorParserVisitor(SplitScript script, Map<UmiTypes, Object> levels, BiFunction<Map<UmiTypes, Object>, String, MetaIndexedObject> indexedObjectResolver){
        this.indexedObjectResolver = indexedObjectResolver;
        this.script = script;
        this.levels = new HashMap<>(levels);
        this.objects = new RdbBehaviorObjectFactory(this.levels, script.getBodyStartCodeLine(), script.getBodyStartCodeColumn());
        result.getBehavior().setStatementType(script.getType().stream().findFirst().orElse(SplitQueryType.UNKNOWN));
    }

    HanaAnalysisResult result() {
        if (result.getDomains().isEmpty()) {
            addDomain(new RdbResourceDomain(), RuleQueryType.BLOCK);
        }
        if (result.getBehavior().getRelations().isEmpty()) {
            // A parsed block containing only local computation has no persistent resource effects.
            BehaviorObject session = new BehaviorObject();
            session.setObjectType(TargetType.Session);
            session.setObjectPath("/");
            addRelation(BehaviorAction.SWITCH, session);
        }
        return result;
    }

    @Override
    public Void visitQuery(HanaParser.QueryContext ctx) {
        Map<String, List<BehaviorObject>> scope = new LinkedHashMap<>();
        cteScopes.push(scope);
        if (ctx.withClause() != null) {
            for (var cte : ctx.withClause().cte()) {
                String name = name(cte.identifier());
                if (scope.containsKey(name)) {
                    throw unsupported(cte, "Duplicate CTE name");
                }
                int start = result.getBehavior().getRelations().size();
                visit(cte.query());
                scope.put(name, readObjectsSince(start));
            }
        }
        for (var term : ctx.queryTerm()) {
            visit(term);
        }
        if (ctx.orderBy() != null) {
            visit(ctx.orderBy());
        }
        cteScopes.pop();
        return null;
    }

    @Override
    public Void visitSelectQuery(HanaParser.SelectQueryContext ctx) {
        int start = result.getBehavior().getRelations().size();
        visitChildren(ctx);
        List<BehaviorObject> tables = readObjectsSince(start);
        HanaSelectDomain domain = new HanaSelectDomain(tables.stream().map(this::resource).toList());
        domain.setMode(RdbQueryMode.NORMAL);
        domain.setHasWith(cteScopes.stream().anyMatch(scope -> !scope.isEmpty()));
        domain.setHasUnion(ancestor(ctx, HanaParser.QueryContext.class).setOperator().size() > 0);
        domain.setEmptyFrom(ctx.fromClause() == null);
        domain.setHasSelectAll(ctx.selectItem().stream().anyMatch(item -> item.STAR() != null));
        domain.setHasAs(ctx.selectItem().stream().anyMatch(item -> item.identifier() != null));
        domain.setSelectInSelect(ctx.selectItem().stream().anyMatch(item -> contains(item, HanaParser.QueryContext.class)));
        domain.setFuncInSelect(ctx.selectItem().stream().anyMatch(item -> contains(item, HanaParser.FunctionCallContext.class)));
        domain.setSelectInFrom(contains(ctx.fromClause(), HanaParser.QueryContext.class));
        domain.setHasSubQuery(contains(ctx, HanaParser.QueryContext.class));
        if (ctx.fromClause() != null) {
            List<RdbJoinType> joins = new ArrayList<>();
            if (ctx.fromClause().tableSource().size() > 1)
                joins.add(RdbJoinType.CROSS_JOIN);
            for (var source : ctx.fromClause().tableSource()) {
                for (var join : source.joinClause()) {
                    RdbJoinType type = RdbJoinType.INNER_JOIN;
                    if (join.LEFT() != null)
                        type = RdbJoinType.LEFT_JOIN;
                    else if (join.RIGHT() != null)
                        type = RdbJoinType.RIGHT_JOIN;
                    else if (join.FULL() != null)
                        type = RdbJoinType.OTHER_JOIN;
                    else if (join.CROSS() != null)
                        type = RdbJoinType.CROSS_JOIN;
                    joins.add(type);
                }
            }
            domain.setJoinTypes(joins);
        }
        if (tables.size() == 1)
            configureName(domain, tables.get(0));
        domain.setExprInSelect(ctx.selectItem()
            .stream()
            .anyMatch(item -> descendants(item, HanaParser.AdditiveExpressionContext.class).stream().anyMatch(e -> e.multiplicativeExpression().size() > 1)
                              || descendants(item, HanaParser.MultiplicativeExpressionContext.class).stream().anyMatch(e -> e.unaryExpression().size() > 1)));
        domain.setSelectColumns(ctx.selectItem().stream().flatMap(item -> columnNamesIn(item).stream()).distinct().toList());
        domain.setSelectFunc(ctx.selectItem()
            .stream()
            .flatMap(item -> descendants(item, HanaParser.FunctionCallContext.class).stream())
            .map(f -> f.qualifiedName().getText())
            .toList());
        domain.setSelectVariables(ctx.selectItem().stream().flatMap(item -> descendants(item, HanaParser.VariableContext.class).stream()).map(ParseTree::getText).toList());
        domain.setSelectValue(ctx.selectItem().stream().flatMap(item -> descendants(item, HanaParser.LiteralContext.class).stream()).map(ParseTree::getText).toList());
        configureWhere(domain, ctx.whereClause());
        configureQuery(domain, ctx);
        addDomain(domain, RuleQueryType.SELECT);
        if (tables.isEmpty()) {
            addRelation(BehaviorAction.READ, objects.unnamedObject(TargetType.Query, ctx, UmiTypes.Schema));
        }
        return null;
    }

    @Override
    public Void visitTablePrimary(HanaParser.TablePrimaryContext ctx) {
        if (ctx.COLON() != null) {
            requireVariable(ctx.identifier());
            String variable = name(ctx.identifier());
            for (var scope : variables) {
                if (scope.containsKey(variable)) {
                    scope.get(variable).forEach(object -> addRelation(BehaviorAction.READ, object));
                    break;
                }
            }
            return null;
        }
        if (ctx.qualifiedName() != null) {
            var name = ctx.qualifiedName();
            if (name.identifier().size() == 1) {
                String table = name(name.identifier(0));
                for (var scope : cteScopes) {
                    if (scope.containsKey(table)) {
                        scope.get(table).forEach(object -> addRelation(BehaviorAction.READ, object));
                        return null;
                    }
                }
            }
            addRelation(BehaviorAction.READ, object(TargetType.Table, name));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertStatement(HanaParser.InsertStatementContext ctx) {
        BehaviorAction action = BehaviorAction.INSERT;
        RuleQueryType type = RuleQueryType.INSERT;
        if (ctx.INSERT() == null) {
            action = BehaviorAction.REPLACE;
            type = RuleQueryType.INSERT;
        }
        BehaviorObject target = object(TargetType.Table, ctx.qualifiedName());
        BehaviorRelation relation = addRelation(action, target);
        if (ctx.valuesClause() != null) {
            relation.setInsertRows((long) ctx.valuesClause().valueRow().size());
        }
        HanaInsertDomain domain = new HanaInsertDomain();
        configureName(domain, target);
        domain.setColumns(columns(ctx.columnNames()));
        domain.setHasSpecifyColumn(ctx.columnNames() != null);
        domain.setConflict(RdbInsertConflictStrategy.NONE);
        if (ctx.INSERT() == null)
            domain.setConflict(RdbInsertConflictStrategy.UPDATE);
        configureQuery(domain, ctx);
        domain.setOnlyValues(ctx.valuesClause() != null);
        domain.setMultipleValues(ctx.valuesClause() != null && ctx.valuesClause().valueRow().size() > 1);
        domain.setFromSelect(ctx.query() != null);
        domain.setHasSubQuery(contains(ctx, HanaParser.QueryContext.class));
        domain.setHasNullValue(descendants(ctx, HanaParser.LiteralContext.class).stream().anyMatch(l -> l.NULL() != null));
        addDomain(domain, type);
        if (ctx.INSERT() == null) {
            HanaUpdateDomain update = new HanaUpdateDomain();
            configureName(update, target);
            update.setSetColumns(columns(ctx.columnNames()));
            addDomain(update, RuleQueryType.MERGE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitUpdateStatement(HanaParser.UpdateStatementContext ctx) {
        var targetName = ctx.qualifiedName();
        if (!ctx.fromClause().isEmpty() && targetName.identifier().size() == 1 && ctx.alias() == null) {
            List<HanaParser.QualifiedNameContext> matches = new ArrayList<>();
            for (var from : ctx.fromClause()) {
                // Only aliases in this FROM scope can name the update target, never subquery aliases.
                for (var source : from.tableSource()) {
                    List<HanaParser.TablePrimaryContext> tables = new ArrayList<>();
                    tables.add(source.tablePrimary());
                    source.joinClause().forEach(join -> tables.add(join.tablePrimary()));
                    for (var table : tables) {
                        if (table.alias() != null && name(table.alias().identifier()).equals(lastName(targetName))) {
                            if (table.qualifiedName() == null) {
                                throw unsupported(table, "Update target is a derived table");
                            }
                            matches.add(table.qualifiedName());
                        }
                    }
                }
            }
            if (matches.size() > 1)
                throw unsupported(ctx, "Ambiguous update alias");
            if (!matches.isEmpty())
                targetName = matches.get(0);
        }
        BehaviorObject target = object(TargetType.Table, targetName);
        addRelation(BehaviorAction.UPDATE, target);
        HanaUpdateDomain domain = new HanaUpdateDomain();
        configureName(domain, target);
        domain.setHasLimit(ctx.TOP() != null);
        domain.setSetColumns(ctx.assignment().stream().map(a -> lastName(a.qualifiedName())).toList());
        domain.setSelectInSet(ctx.assignment().stream().anyMatch(a -> contains(a, HanaParser.QueryContext.class)));
        configureWhere(domain, ctx.whereClause());
        configureQuery(domain, ctx);
        addDomain(domain, RuleQueryType.UPDATE);
        return visitChildren(ctx);
    }

    @Override
    public Void visitDeleteStatement(HanaParser.DeleteStatementContext ctx) {
        BehaviorObject target = object(TargetType.Table, ctx.qualifiedName());
        addRelation(BehaviorAction.DELETE, target);
        HanaDeleteDomain domain = new HanaDeleteDomain();
        configureName(domain, target);
        domain.setHasLimit(ctx.TOP() != null);
        configureWhere(domain, ctx.whereClause());
        configureQuery(domain, ctx);
        addDomain(domain, RuleQueryType.DELETE);
        return visitChildren(ctx);
    }

    @Override
    public Void visitMergeStatement(HanaParser.MergeStatementContext ctx) {
        BehaviorObject target = object(TargetType.Table, ctx.qualifiedName());
        addRelation(BehaviorAction.MERGE, target);
        HanaUpdateDomain merge = new HanaUpdateDomain();
        configureName(merge, target);
        merge.setHasWhere(true);
        merge.setWhereColumns(columnNamesIn(ctx.expression()));
        addDomain(merge, RuleQueryType.MERGE);
        // Audit each possible mutation with the corresponding existing rule model.
        for (var branch : ctx.mergeAction()) {
            if (branch.UPDATE() != null) {
                HanaUpdateDomain domain = new HanaUpdateDomain();
                configureName(domain, target);
                domain.setSetColumns(branch.assignment().stream().map(a -> lastName(a.qualifiedName())).toList());
                domain.setSelectInSet(branch.assignment().stream().anyMatch(a -> contains(a, HanaParser.QueryContext.class)));
                domain.setHasWhere(true);
                domain.setWhereColumns(columnNamesIn(ctx.expression()));
                addDomain(domain, RuleQueryType.UPDATE);
            } else if (branch.DELETE() != null) {
                HanaDeleteDomain domain = new HanaDeleteDomain();
                configureName(domain, target);
                domain.setHasWhere(true);
                domain.setWhereColumns(columnNamesIn(ctx.expression()));
                addDomain(domain, RuleQueryType.DELETE);
            } else {
                HanaInsertDomain domain = new HanaInsertDomain();
                configureName(domain, target);
                domain.setColumns(columns(branch.columnNames()));
                domain.setHasSpecifyColumn(branch.columnNames() != null);
                domain.setFromSelect(true);
                addDomain(domain, RuleQueryType.INSERT);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCallStatement(HanaParser.CallStatementContext ctx) {
        call(ctx.qualifiedName(), ctx.expression(), false);
        return visitChildren(ctx);
    }

    @Override
    public Void visitFunctionCall(HanaParser.FunctionCallContext ctx) {
        var function = ctx.qualifiedName();
        if (!HanaSqlFunctions.isBuiltIn(function)) {
            call(function, ctx.expression(), true);
        }
        return visitChildren(ctx);
    }

    private void call(HanaParser.QualifiedNameContext name, List<HanaParser.ExpressionContext> args, boolean function) {
        TargetType targetType = TargetType.Procedure;
        if (function) {
            targetType = TargetType.Function;
        }
        BehaviorObject target = object(targetType, name);
        addRelation(BehaviorAction.CALL, target);
        RdbCallDomain domain = new RdbCallDomain();
        configureName(domain, target);
        domain.setFunc(function);
        domain.setArgs(args.stream().map(ParseTree::getText).toList());
        domain.setEmptyArg(args.isEmpty());
        addDomain(domain, RuleQueryType.CALL);
    }

    @Override
    public Void visitPrimary(HanaParser.PrimaryContext ctx) {
        var qualified = ctx.qualifiedName();
        if (qualified != null && qualified.identifier().size() > 1) {
            var tail = qualified.identifier(qualified.identifier().size() - 1);
            if (tail.QUOTED_IDENTIFIER() == null && Set.of("NEXTVAL", "CURRVAL").contains(name(tail))) {
                List<String> parts = names(qualified);
                BehaviorObject sequence = object(TargetType.Sequence, qualified, parts.subList(0, parts.size() - 1));
                BehaviorAction action = BehaviorAction.READ;
                if ("NEXTVAL".equals(name(tail))) {
                    action = BehaviorAction.UPDATE;
                }
                addRelation(action, sequence);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitVariable(HanaParser.VariableContext ctx) {
        if (ctx.COLON().size() == 1) {
            requireVariable(ctx.identifier(0));
        }
        return null;
    }

    @Override
    public Void visitBlock(HanaParser.BlockContext ctx) {
        variables.push(new LinkedHashMap<>());
        visitChildren(ctx);
        variables.pop();
        return null;
    }

    @Override
    public Void visitAnonymousBlock(HanaParser.AnonymousBlockContext ctx) {
        variables.push(new LinkedHashMap<>());
        for (var parameter : ctx.parameter()) {
            variables.peek().put(name(parameter.identifier()), List.of());
            visit(parameter);
        }
        visit(ctx.block());
        variables.pop();
        return null;
    }

    @Override
    public Void visitDeclaration(HanaParser.DeclarationContext ctx) {
        if (ctx.identifier() != null) {
            variables.peek().put(name(ctx.identifier()), List.of());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitVariableAssignment(HanaParser.VariableAssignmentContext ctx) {
        if (ctx.query() == null) {
            requireVariable(ctx.identifier());
            return visitChildren(ctx);
        }
        int start = result.getBehavior().getRelations().size();
        visit(ctx.query());
        String name = name(ctx.identifier());
        var scope = variables.stream().filter(candidate -> candidate.containsKey(name)).findFirst().orElse(variables.peek());
        // Keep all possible sources across conditional branches and loop iterations.
        List<BehaviorObject> sources = new ArrayList<>(scope.getOrDefault(name, List.of()));
        sources.addAll(readObjectsSince(start));
        scope.put(name, sources);
        return null;
    }

    @Override
    public Void visitForStatement(HanaParser.ForStatementContext ctx) {
        variables.push(new LinkedHashMap<>(Map.of(name(ctx.identifier()), List.of())));
        visitChildren(ctx);
        variables.pop();
        return null;
    }

    @Override
    public Void visitSessionStatement(HanaParser.SessionStatementContext ctx) {
        if (ctx.SCHEMA() != null) {
            // Resource authorization uses the editor context; a SQL-side switch must not desynchronize it.
            if (!Objects.equals(name(ctx.identifier()), levels.get(UmiTypes.Schema))) {
                throw unsupported(ctx, "Change the schema through the editor context before executing SQL");
            }
            addRelation(BehaviorAction.SWITCH, object(TargetType.Schema, ctx.identifier(), List.of(name(ctx.identifier()))));
            addDomain(new RdbResourceDomain(), RuleQueryType.SWITCH_SCHEMA);
        } else if (ctx.TRANSACTION() != null) {
            addRelation(BehaviorAction.SWITCH, objects.unnamedObject(TargetType.Transaction, ctx, UmiTypes.Catalog));
            addDomain(new RdbResourceDomain(), RuleQueryType.TRANSACTION);
        } else {
            Token keyToken = ctx.STRING(0).getSymbol();
            String key = stringValue(keyToken);
            BehaviorAction action = BehaviorAction.CONFIGURE;
            if (ctx.UNSET() != null) {
                action = BehaviorAction.RESET;
            }
            addRelation(action, catalogObject(TargetType.ConfigKey, ctx, keyToken, key));
            RuleQueryType type = RuleQueryType.SESSION_VARIABLE_RW;
            if (Set.of("DATE_FORMAT", "TIME_FORMAT", "SECONDDATE_FORMAT", "TIMESTAMP_FORMAT").contains(key)) {
                type = RuleQueryType.SESSION_SETTING_WRITE;
            }
            addDomain(new RdbResourceDomain(), type);
        }
        return null;
    }

    @Override
    public Void visitConnectStatement(HanaParser.ConnectStatementContext ctx) {
        BehaviorObject user;
        if (ctx.userName != null) {
            user = catalogObject(TargetType.User, ctx, ctx.userName.getStart(), name(ctx.userName));
        } else {
            // Authentication payloads do not statically identify a database user or provider.
            user = catalogObject(TargetType.User, ctx, ctx.CONNECT().getSymbol(), null);
        }
        addRelation(BehaviorAction.SWITCH, user);
        addDomain(new RdbResourceDomain(), RuleQueryType.SWITCH_USER);
        return null;
    }

    @Override
    public Void visitHistorySessionStatement(HanaParser.HistorySessionStatementContext ctx) {
        addRelation(BehaviorAction.CONFIGURE, objects.unnamedObject(TargetType.Session, ctx.historySessionTarget(), UmiTypes.Catalog));
        addDomain(new RdbResourceDomain(), RuleQueryType.SESSION_SETTING_WRITE);
        return null;
    }

    @Override
    public Void visitSystemSessionStatement(HanaParser.SystemSessionStatementContext ctx) {
        if (ctx.sessionId != null) {
            BehaviorObject session = catalogObject(TargetType.Session, ctx, ctx.sessionId, ctx.sessionId.getText());
            String keyName = stringValue(ctx.key);
            BehaviorObject key = catalogObject(TargetType.ConfigKey, ctx, ctx.key, keyName);
            key.setObjectPath(session.getObjectPath() + keyName + "/");
            BehaviorAction action = BehaviorAction.CONFIGURE;
            if (ctx.UNSET() != null) {
                action = BehaviorAction.RESET;
            }
            addRelation(action, session).setTarget(List.of(key));
        } else {
            BehaviorObject session = catalogObject(TargetType.Session, ctx, ctx.connectionId, stringValue(ctx.connectionId));
            if (ctx.CANCEL() != null) {
                // A connection ID locates the query's owning session, not the query's own identity.
                BehaviorObject query = catalogObject(TargetType.Query, ctx, ctx.connectionId, null);
                query.setObjectPath(session.getObjectPath());
                addRelation(BehaviorAction.TERMINATE, query).setTarget(List.of(session));
            } else {
                addRelation(BehaviorAction.TERMINATE, session);
            }
        }
        addDomain(new RdbResourceDomain(), RuleQueryType.ADMIN);
        return null;
    }

    private BehaviorObject catalogObject(TargetType type, ParserRuleContext context, Token token, String name) {
        BehaviorObject object = objects.unnamedObject(type, token, UmiTypes.Catalog);
        if (name != null) {
            if (name.contains("/")) {
                throw unsupported(context, "Object name contains a resource path separator");
            }
            object.setObjectPath(object.getObjectPath() + name + "/");
            object.setObjectName(new ObjectName(Objects.toString(levels.get(UmiTypes.Catalog), null), null, name));
        }
        return object;
    }

    private String stringValue(Token token) {
        String text = token.getText();
        return text.substring(text.indexOf('\'') + 1, text.length() - 1).replace("''", "'");
    }

    @Override
    public Void visitSecurityStatement(HanaParser.SecurityStatementContext ctx) {
        visitChildren(ctx);
        RuleQueryType type = RuleQueryType.CONFIG_WRITE;
        ParserRuleContext statement = (ParserRuleContext) ctx.getChild(0);
        String operation = statement.getStart().getText().toUpperCase(Locale.ROOT);
        if (ctx.securityUser() != null) {
            var grant = ctx.securityUser().securitySpecialGrant();
            if (grant != null) {
                type = RuleQueryType.valueOf(grant.getStart().getText().toUpperCase(Locale.ROOT));
            } else {
                type = RuleQueryType.valueOf(operation + "_USER");
            }
        } else if (ctx.securityRole() != null) {
            type = RuleQueryType.valueOf(operation + "_ROLE");
        } else if (ctx.securityGrant() != null) {
            type = RuleQueryType.valueOf(operation);
        } else if (ctx.securityPolicy() != null || ctx.securityAudit() != null) {
            type = RuleQueryType.valueOf(operation + "_POLICY");
        } else if (ctx.securityAuditLog() != null) {
            type = RuleQueryType.ALTER_LOG;
            if (ctx.securityAuditLog().CLEAR() != null) {
                type = RuleQueryType.MAINTAIN_LOG;
            }
        } else if (ctx.securityValidate() != null || ctx.securityEncryption() != null && ctx.securityEncryption().VALIDATE() != null) {
            type = RuleQueryType.ADMIN;
        }
        addDomain(new RdbResourceDomain(), type);
        return null;
    }

    @Override
    public Void visitSecurityUser(HanaParser.SecurityUserContext ctx) {
        BehaviorObject user = securityLocal(TargetType.User, ctx.userName);
        var special = ctx.securitySpecialGrant();
        if (special != null) {
            BehaviorObject scope;
            if (special.PUBLIC() != null) {
                scope = catalogObject(TargetType.Role, special, special.PUBLIC().getSymbol(), "PUBLIC");
            } else {
                // HANA creates the personal schema with the user's name.
                scope = object(TargetType.Schema, ctx.userName, List.of(name(ctx.userName)));
            }
            addRelation(securityAction(special), scope).setTarget(List.of(user));
            return null;
        }
        var attempts = descendants(ctx, HanaParser.SecurityConnectAttemptsContext.class);
        if (!attempts.isEmpty()) {
            for (var attempt : attempts) {
                addRelation(BehaviorAction.RESET, securityChild(TargetType.ConfigKey, attempt.securityAttemptKey(), "CONNECT ATTEMPTS", user));
            }
            return null;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        for (var provider : descendants(ctx, HanaParser.SecurityIdentityProviderContext.class)) {
            if (provider.providerName != null) {
                targets.add(securityLocal(TargetType.SecurityIntegration, provider.providerName));
            } else if (provider.securityLdapIdentity() != null) {
                targets.add(objects.unnamedObject(TargetType.SecurityIntegration, provider.securityLdapIdentity(), UmiTypes.Catalog));
            }
        }
        for (var group : descendants(ctx, HanaParser.SecurityUsergroupReferenceContext.class)) {
            targets.add(securityLocal(TargetType.Object, group.identifier()));
        }
        for (var remote : descendants(ctx, HanaParser.SecurityRemoteIdentityContext.class)) {
            BehaviorObject target = securityLocal(TargetType.User, remote.userName);
            BehaviorObject database = objects.unnamedObject(TargetType.Catalog, remote.databaseName, UmiTypes.Instance);
            String catalog = name(remote.databaseName);
            requireSecurityPathPart(remote, catalog);
            target.setObjectPath(database.getObjectPath() + catalog + "/" + name(remote.userName) + "/");
            target.setObjectName(new ObjectName(catalog, null, name(remote.userName)));
            targets.add(target);
        }
        addRelation(securityAction(ctx), user).setTarget(securityTargets(targets));
        int settingStart = result.getBehavior().getRelations().size();
        for (var parameter : descendants(ctx, HanaParser.SecurityUserParameterContext.class)) {
            var key = parameter.securityParameterName();
            addRelation(BehaviorAction.CONFIGURE, securityChild(TargetType.ConfigKey, key, securityWords(key), user));
        }
        for (var clear : descendants(ctx, HanaParser.SecurityClearParametersContext.class)) {
            for (var key : clear.securityParameterName()) {
                addRelation(BehaviorAction.RESET, securityChild(TargetType.ConfigKey, key, securityWords(key), user));
            }
            if (clear.securityAllParameters() != null) {
                BehaviorObject key = objects.unnamedObject(TargetType.ConfigKey, clear.securityAllParameters(), UmiTypes.Catalog);
                key.setObjectPath(user.getObjectPath());
                addRelation(BehaviorAction.RESET, key);
            }
        }
        result.getBehavior()
            .getRelations()
            .subList(settingStart, result.getBehavior().getRelations().size())
            .sort(Comparator.comparing(BehaviorRelation::getSubject, SECURITY_SOURCE_ORDER));
        return null;
    }

    @Override
    public Void visitSecurityUsergroup(HanaParser.SecurityUsergroupContext ctx) {
        BehaviorObject group = securityLocal(TargetType.Object, ctx.groupName);
        List<BehaviorObject> targets = new ArrayList<>();
        for (var option : ctx.securityGroupOption()) {
            for (var restriction : descendants(option, HanaParser.SecurityRestrictionDefinitionContext.class)) {
                targets.add(securityChild(TargetType.Policy, restriction.identifier(), name(restriction.identifier()), group));
            }
            if (option.securityRestrictionNames() != null) {
                for (var name : option.securityRestrictionNames().identifier()) {
                    targets.add(securityChild(TargetType.Policy, name, name(name), group));
                }
            } else if (option.ALL() != null) {
                BehaviorObject all = objects.unnamedObject(TargetType.Policy, option.ALL().getSymbol(), UmiTypes.Catalog);
                all.setObjectPath(group.getObjectPath());
                targets.add(all);
            }
        }
        addRelation(securityAction(ctx), group).setTarget(securityTargets(targets));
        int settingStart = result.getBehavior().getRelations().size();
        for (var parameter : descendants(ctx, HanaParser.SecurityGroupParameterContext.class)) {
            BehaviorObject key = catalogObject(TargetType.ConfigKey, parameter, parameter.key, stringValue(parameter.key));
            key.setObjectPath(group.getObjectPath() + stringValue(parameter.key) + "/");
            addRelation(BehaviorAction.CONFIGURE, key);
        }
        for (var clear : descendants(ctx, HanaParser.SecurityGroupClearParametersContext.class)) {
            for (var token : clear.STRING()) {
                BehaviorObject key = catalogObject(TargetType.ConfigKey, clear, token.getSymbol(), stringValue(token.getSymbol()));
                key.setObjectPath(group.getObjectPath() + stringValue(token.getSymbol()) + "/");
                addRelation(BehaviorAction.RESET, key);
            }
        }
        for (var set : descendants(ctx, HanaParser.SecurityParameterSetContext.class)) {
            var token = set.STRING().getSymbol();
            BehaviorObject key = catalogObject(TargetType.ConfigKey, set, token, stringValue(token));
            key.setObjectPath(group.getObjectPath() + stringValue(token) + "/");
            addRelation(BehaviorAction.CONFIGURE, key);
        }
        result.getBehavior()
            .getRelations()
            .subList(settingStart, result.getBehavior().getRelations().size())
            .sort(Comparator.comparing(BehaviorRelation::getSubject, SECURITY_SOURCE_ORDER));
        return null;
    }

    @Override
    public Void visitSecurityRole(HanaParser.SecurityRoleContext ctx) {
        addRelation(securityAction(ctx), securityQualified(TargetType.Role, ctx.roleName, false));
        return null;
    }

    @Override
    public Void visitSecurityGrant(HanaParser.SecurityGrantContext ctx) {
        TargetType recipientType = TargetType.UserOrRole;
        if (ctx.recipient.identifier().size() == 2) {
            recipientType = TargetType.Role;
        }
        BehaviorObject recipient = securityQualified(recipientType, ctx.recipient, false);
        List<BehaviorObject> subjects = new ArrayList<>();
        var scope = ctx.securityGrantScope();
        if (!scope.securitySystemPrivilege().isEmpty()) {
            subjects.add(objects.unnamedObject(TargetType.Catalog, ctx.getStart(), UmiTypes.Catalog));
        } else if (scope.policyName != null) {
            subjects.add(securityQualified(TargetType.Policy, scope.policyName, true));
        } else if (scope.securityGrantObject() != null) {
            var object = scope.securityGrantObject();
            if (object.schemaName != null) {
                subjects.add(object(TargetType.Schema, object.schemaName, List.of(name(object.schemaName))));
            } else if (object.groupName != null) {
                subjects.add(securityLocal(TargetType.Object, object.groupName));
            } else if (object.sourceName != null) {
                subjects.add(securityLocal(TargetType.Link, object.sourceName));
            } else if (object.keyName != null) {
                subjects.add(securityQualified(TargetType.ColumnEncryptionKey, object.keyName, true));
            } else {
                subjects.add(securityQualified(TargetType.SchemaObject, object.objectName, true));
            }
        } else {
            for (var role : scope.roles) {
                subjects.add(securityQualified(TargetType.Role, role, false));
            }
        }
        for (var subject : subjects) {
            addRelation(securityAction(ctx), subject).setTarget(List.of(recipient));
        }
        return null;
    }

    @Override
    public Void visitSecurityPolicy(HanaParser.SecurityPolicyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (var view : ctx.views) {
            targets.add(securityQualified(TargetType.View, view, true));
        }
        if (ctx.procedureName != null) {
            targets.add(securityQualified(TargetType.Procedure, ctx.procedureName, true));
        }
        if (ctx.expression() != null) {
            // Analyze predicate dependencies in isolation: their actions are not executed by CREATE POLICY.
            HanaBehaviorParserVisitor dependencies = new HanaBehaviorParserVisitor(script, levels, indexedObjectResolver);
            dependencies.visit(ctx.expression());
            for (var dependency : dependencies.result.getBehavior().getRelations()) {
                targets.add(dependency.getSubject());
                if (dependency.getTarget() != null) {
                    targets.addAll(dependency.getTarget());
                }
            }
        }
        // Defining a policy does not execute its predicate or condition provider.
        addRelation(securityAction(ctx), securityQualified(TargetType.Policy, ctx.policyName, true)).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSecurityCredential(HanaParser.SecurityCredentialContext ctx) {
        var identity = ctx.securityCredentialIdentity();
        BehaviorObject credential = objects.unnamedObject(TargetType.Credential, identity, UmiTypes.Catalog);
        String path = credential.getObjectPath();
        List<BehaviorObject> targets = new ArrayList<>();
        if (identity.userName != null) {
            BehaviorObject user = securityLocal(TargetType.User, identity.userName);
            path = user.getObjectPath();
            targets.add(user);
        }
        for (var token : List.of(identity.component, identity.purpose, identity.mechanism)) {
            String part = stringValue(token);
            requireSecurityPathPart(identity, part);
            path += part + "/";
        }
        credential.setObjectPath(path);
        addRelation(securityAction(ctx), credential).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSecurityProvider(HanaParser.SecurityProviderContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (var group : descendants(ctx, HanaParser.SecurityUsergroupReferenceContext.class)) {
            targets.add(securityLocal(TargetType.Object, group.identifier()));
        }
        addRelation(securityAction(ctx), securityLocal(TargetType.SecurityIntegration, ctx.providerName)).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSecurityValidate(HanaParser.SecurityValidateContext ctx) {
        BehaviorObject subject;
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.providerName != null) {
            subject = securityLocal(TargetType.SecurityIntegration, ctx.providerName);
            if (ctx.userName != null) {
                targets.add(securityChild(TargetType.User, ctx.userName, name(ctx.userName), subject));
            }
        } else if (ctx.userName != null) {
            subject = securityLocal(TargetType.User, ctx.userName);
        } else {
            subject = objects.unnamedObject(TargetType.User, ctx.USER(0).getSymbol(), UmiTypes.Catalog);
        }
        addRelation(BehaviorAction.VALIDATE, subject).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSecurityAudit(HanaParser.SecurityAuditContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        var actions = ctx.securityAuditActions();
        if (actions != null) {
            if (actions.securityAuditUsers() != null) {
                for (var user : actions.securityAuditUsers().identifier()) {
                    targets.add(securityLocal(TargetType.User, user));
                }
            }
            for (var object : actions.objects) {
                TargetType type = TargetType.SchemaObject;
                if (actions.securityAuditAction().stream().anyMatch(action -> action.EXECUTE() != null)) {
                    type = TargetType.Procedure;
                }
                targets.add(securityQualified(type, object, true));
            }
            if (actions.schemaName != null) {
                targets.add(object(TargetType.Schema, actions.schemaName, List.of(name(actions.schemaName))));
            }
        }
        if (ctx.databaseName != null) {
            targets.add(object(TargetType.Catalog, ctx.databaseName, List.of(name(ctx.databaseName))));
        }
        addRelation(securityAction(ctx), securityLocal(TargetType.Policy, ctx.policyName)).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSecurityAuditLog(HanaParser.SecurityAuditLogContext ctx) {
        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.CLEAR() != null) {
            action = BehaviorAction.PURGE;
        }
        BehaviorRelation relation = addRelation(action, objects.unnamedObject(TargetType.Log, ctx.securityAuditLogName(), UmiTypes.Catalog));
        if (ctx.policyName != null) {
            relation.setTarget(List.of(securityLocal(TargetType.Policy, ctx.policyName)));
        }
        return null;
    }

    @Override
    public Void visitSecurityCertificate(HanaParser.SecurityCertificateContext ctx) {
        BehaviorObject certificate;
        if (ctx.certificateName != null) {
            certificate = securityLocal(TargetType.Certificate, ctx.certificateName);
        } else {
            certificate = securityCertificateId(ctx.securityCertificateId());
        }
        addRelation(securityAction(ctx), certificate);
        return null;
    }

    @Override
    public Void visitSecurityPublicKey(HanaParser.SecurityPublicKeyContext ctx) {
        BehaviorObject key;
        if (ctx.keyName != null) {
            key = securityLocal(TargetType.AsymmetricKey, ctx.keyName);
        } else {
            key = objects.unnamedObject(TargetType.AsymmetricKey, ctx.securityPublicKeyType(), UmiTypes.Catalog);
        }
        addRelation(securityAction(ctx), key);
        return null;
    }

    @Override
    public Void visitSecurityPse(HanaParser.SecurityPseContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        var option = ctx.securityPseOption();
        if (option != null) {
            for (var certificate : option.securityCertificateId()) {
                targets.add(securityCertificateId(certificate));
            }
            for (var key : option.INTEGER()) {
                targets.add(catalogObject(TargetType.AsymmetricKey, option, key.getSymbol(), key.getText()));
            }
        }
        for (var assignment : descendants(ctx, HanaParser.SecurityPseTargetsContext.class)) {
            TargetType type = TargetType.SecurityIntegration;
            if (assignment.REMOTE() != null) {
                type = TargetType.Link;
            }
            // SNI HOST values are matching strings, not topology object identities.
            for (var name : assignment.identifier()) {
                targets.add(securityLocal(type, name));
            }
        }
        BehaviorAction action;
        if (ctx.SET() != null) {
            action = BehaviorAction.CONFIGURE;
        } else if (ctx.UNSET() != null) {
            action = BehaviorAction.RESET;
        } else {
            action = securityAction(ctx);
        }
        addRelation(action, securityLocal(TargetType.Object, ctx.pseName)).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSecurityClientKey(HanaParser.SecurityClientKeyContext ctx) {
        BehaviorObject key;
        if (ctx.pairName != null) {
            key = securityLocal(TargetType.AsymmetricKey, ctx.pairName);
        } else {
            key = securityQualified(TargetType.ColumnEncryptionKey, ctx.columnName, true);
        }
        BehaviorRelation relation = addRelation(securityAction(ctx), key);
        if (ctx.securityKeyEncryption() != null) {
            relation.setTarget(List.of(securityLocal(TargetType.AsymmetricKey, ctx.securityKeyEncryption().identifier())));
        }
        return null;
    }

    @Override
    public Void visitSecurityEncryption(HanaParser.SecurityEncryptionContext ctx) {
        if (ctx.securityEncryptionPassword() != null) {
            var key = ctx.securityEncryptionPassword();
            BehaviorAction action = BehaviorAction.CONFIGURE;
            if (ctx.VALIDATE() != null) {
                action = BehaviorAction.VALIDATE;
            }
            addRelation(action, securityNamed(TargetType.ConfigKey, key, List.of(securityWords(key)), false));
        } else if (ctx.securityEncryptionControl() != null) {
            var key = ctx.securityEncryptionControl();
            addRelation(BehaviorAction.CONFIGURE, securityNamed(TargetType.ConfigKey, key, List.of(securityWords(key)), false));
        } else if (ctx.securityCurrentKey() != null) {
            addRelation(BehaviorAction.APPLY, objects.unnamedObject(TargetType.SymmetricKey, ctx.securityCurrentKey(), UmiTypes.Catalog));
        } else {
            ParserRuleContext change = ctx.securityApplicationKey();
            if (change == null) {
                change = ctx.securityRootEncryption();
            }
            if (change != null && (change.getStart().getType() == HanaParser.CREATE || change.getStart().getType() == HanaParser.ACTIVATE)) {
                var roots = descendants(change, HanaParser.SecurityRootKeyContext.class);
                BehaviorObject key;
                if (!roots.isEmpty()) {
                    key = objects.unnamedObject(TargetType.SymmetricKey, roots.get(0), UmiTypes.Catalog);
                } else {
                    key = objects.unnamedObject(TargetType.SymmetricKey, ((HanaParser.SecurityApplicationKeyContext) change).KEY().getSymbol(), UmiTypes.Catalog);
                }
                BehaviorAction action = BehaviorAction.CREATE;
                if (change.getStart().getType() == HanaParser.ACTIVATE) {
                    action = BehaviorAction.SWITCH;
                }
                addRelation(action, key);
            } else {
                Token start = null;
                if (ctx.BACKUP() != null) {
                    start = ctx.BACKUP().getSymbol();
                }
                if (start == null && ctx.LOG() != null) {
                    start = ctx.LOG().getSymbol();
                }
                if (start == null) {
                    start = ctx.PERSISTENCE().getSymbol();
                }
                String name = start.getText().toUpperCase(Locale.ROOT) + " ENCRYPTION";
                BehaviorObject key = catalogObject(TargetType.ConfigKey, ctx, start, name);
                BehaviorObject end = objects.unnamedObject(TargetType.ConfigKey, ctx.ENCRYPTION().getSymbol(), UmiTypes.Catalog);
                key.setEndLine(end.getEndLine());
                key.setEndColumn(end.getEndColumn());
                addRelation(BehaviorAction.CONFIGURE, key);
            }
        }
        return null;
    }

    @Override
    public Void visitSecurityKeyConfiguration(HanaParser.SecurityKeyConfigurationContext ctx) {
        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.ADD() != null) {
            action = BehaviorAction.CREATE;
        } else if (ctx.ACTIVATE() != null) {
            action = BehaviorAction.SWITCH;
        } else if (ctx.DROP() != null) {
            action = BehaviorAction.DROP;
        }
        addRelation(action, securityLocal(TargetType.ConfigKey, ctx.configName));
        return null;
    }

    @Override
    public Void visitSecurityClientPki(HanaParser.SecurityClientPkiContext ctx) {
        BehaviorObject certificate;
        if (ctx.securityRootCa() != null) {
            certificate = objects.unnamedObject(TargetType.Certificate, ctx.securityRootCa(), UmiTypes.Catalog);
        } else {
            certificate = objects.unnamedObject(TargetType.Certificate, ctx.CERTIFICATES().getSymbol(), UmiTypes.Catalog);
        }
        BehaviorAction action = BehaviorAction.REPLACE;
        if (ctx.DROP() != null) {
            action = BehaviorAction.DROP;
        }
        addRelation(action, certificate);
        return null;
    }

    private static List<BehaviorObject> securityTargets(List<BehaviorObject> targets) {
        targets.sort(SECURITY_SOURCE_ORDER);
        return targets;
    }

    private BehaviorObject securityLocal(TargetType type, HanaParser.IdentifierContext identifier) {
        return securityNamed(type, identifier, List.of(name(identifier)), false);
    }

    private BehaviorObject securityQualified(TargetType type, HanaParser.QualifiedNameContext context, boolean schemaScoped) {
        return securityNamed(type, context, names(context), schemaScoped);
    }

    private BehaviorObject securityNamed(TargetType type, ParserRuleContext context, List<String> parts, boolean schemaScoped) {
        if (parts.size() > 2) {
            throw unsupported(context, "Security object names cannot include a database qualifier");
        }
        for (String part : parts) {
            requireSecurityPathPart(context, part);
        }
        BehaviorObject object = objects.unnamedObject(type, context, UmiTypes.Catalog);
        String schema = null;
        if (parts.size() == 2) {
            schema = parts.get(0);
        } else if (schemaScoped) {
            schema = Objects.toString(levels.get(UmiTypes.Schema), null);
        }
        String path = object.getObjectPath();
        if (schema != null) {
            path += schema + "/";
        }
        String objectName = parts.get(parts.size() - 1);
        object.setObjectPath(path + objectName + "/");
        object.setObjectName(new ObjectName(Objects.toString(levels.get(UmiTypes.Catalog), null), schema, objectName));
        return object;
    }

    private BehaviorObject securityChild(TargetType type, ParserRuleContext context, String name, BehaviorObject owner) {
        BehaviorObject object = securityNamed(type, context, List.of(name), false);
        object.setObjectPath(owner.getObjectPath() + name + "/");
        return object;
    }

    private BehaviorObject securityCertificateId(HanaParser.SecurityCertificateIdContext ctx) {
        if (ctx.identifier() != null) {
            return securityLocal(TargetType.Certificate, ctx.identifier());
        }
        return catalogObject(TargetType.Certificate, ctx, ctx.INTEGER().getSymbol(), ctx.INTEGER().getText());
    }

    private void requireSecurityPathPart(ParserRuleContext context, String name) {
        if (name.contains("/")) {
            throw unsupported(context, "Object name contains a resource path separator");
        }
    }

    private static BehaviorAction securityAction(ParserRuleContext context) {
        return BehaviorAction.valueOf(context.getStart().getText().toUpperCase(Locale.ROOT));
    }

    private static String securityWords(ParserRuleContext context) {
        return String.join(" ", descendants(context, TerminalNode.class).stream().map(ParseTree::getText).toList()).toUpperCase(Locale.ROOT);
    }

    @Override
    public Void visitSystemManagement(HanaParser.SystemManagementContext ctx) {
        visitChildren(ctx);
        RuleQueryType type = RuleQueryType.CONFIG_WRITE;
        if (ctx.workloadClass() != null) {
            type = RuleQueryType.valueOf(ctx.workloadClass().getStart().getText().toUpperCase(Locale.ROOT) + "_RESOURCE_GROUP");
        } else if (ctx.workloadMapping() != null) {
            type = RuleQueryType.valueOf(ctx.workloadMapping().getStart().getText().toUpperCase(Locale.ROOT) + "_POLICY");
        } else if (ctx.systemReplication() != null) {
            var replication = ctx.systemReplication();
            type = RuleQueryType.ALTER_REPLICATION;
            if (replication.REGISTER() != null) {
                type = RuleQueryType.CREATE_REPLICATION;
            } else if (replication.UNREGISTER() != null) {
                type = RuleQueryType.DROP_REPLICATION;
            }
        } else if (ctx.systemIniHistory() != null || ctx.systemStopService() != null || ctx.systemReclaim() != null) {
            type = RuleQueryType.ADMIN;
        }
        addDomain(new RdbResourceDomain(), type);
        return null;
    }

    @Override
    public Void visitSystemIni(HanaParser.SystemIniContext ctx) {
        String layer = stringValue(ctx.layer);
        if (!Set.of("SYSTEM", "HOST", "DATABASE").contains(layer) || (layer.equals("SYSTEM") != (ctx.layerName == null))) {
            throw unsupported(ctx, "Invalid configuration layer or layer name");
        }
        BehaviorObject owner = objects.unnamedObject(TargetType.Catalog, ctx, UmiTypes.Catalog);
        List<BehaviorObject> targets = new ArrayList<>();
        String filename = stringValue(ctx.fileName);
        requireSecurityPathPart(ctx, filename);
        String prefix;
        if (layer.equals("DATABASE")) {
            String database = stringValue(ctx.layerName);
            requireSecurityPathPart(ctx, database);
            BehaviorObject tenant = objects.instanceObject(TargetType.Catalog, ctx.layerName, database);
            tenant.setObjectName(new ObjectName(database, null, null));
            targets.add(tenant);
            owner = tenant;
        } else if (layer.equals("HOST")) {
            String host = stringValue(ctx.layerName);
            requireSecurityPathPart(ctx, host);
            targets.add(objects.instanceObject(TargetType.Machine, ctx.layerName, host));
        }
        prefix = owner.getObjectPath() + filename + "/" + layer + "/";
        if (layer.equals("HOST")) {
            prefix += stringValue(ctx.layerName) + "/";
        }
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (ctx.UNSET() != null) {
            action = BehaviorAction.RESET;
        }
        for (var entry : descendants(ctx, HanaParser.SystemIniKeyContext.class)) {
            String section = stringValue(entry.section);
            String keyName = stringValue(entry.key);
            requireSecurityPathPart(entry, section);
            BehaviorObject key = catalogObject(TargetType.ConfigKey, entry, entry.key, keyName);
            BehaviorObject range = managementRange(TargetType.ConfigKey, entry.section, entry.key);
            key.setStartLine(range.getStartLine());
            key.setStartColumn(range.getStartColumn());
            key.setObjectPath(prefix + section + "/" + keyName + "/");
            if (layer.equals("DATABASE")) {
                key.setObjectName(new ObjectName(stringValue(ctx.layerName), null, keyName));
            }
            addRelation(action, key).setTarget(targets);
        }
        return null;
    }

    @Override
    public Void visitSystemIniHistory(HanaParser.SystemIniHistoryContext ctx) {
        addRelation(BehaviorAction.PURGE, managementRange(TargetType.Log, ctx.INIFILE().getSymbol(), ctx.HISTORY().getSymbol()));
        return null;
    }

    @Override
    public Void visitSystemReconfigure(HanaParser.SystemReconfigureContext ctx) {
        String host = stringValue(ctx.host);
        String service = stringValue(ctx.serviceName);
        String name = null;
        if (!host.isEmpty()) {
            name = host + ":" + ctx.port.getText();
        } else if (!service.isEmpty()) {
            name = service;
        }
        BehaviorObject resource = catalogObject(TargetType.Resource, ctx, ctx.serviceName, name);
        BehaviorObject range = managementRange(TargetType.Resource, ctx.serviceName, ctx.port);
        resource.setEndLine(range.getEndLine());
        resource.setEndColumn(range.getEndColumn());
        // Empty endpoint selectors address a service-type collection, or all services.
        resource.setObjectName(null);
        addRelation(BehaviorAction.CONFIGURE, resource);
        return null;
    }

    @Override
    public Void visitSystemStopService(HanaParser.SystemStopServiceContext ctx) {
        Token start = ctx.address;
        Token stop = ctx.address;
        String address;
        if (ctx.address != null) {
            address = stringValue(ctx.address);
        } else {
            start = ctx.host;
            stop = ctx.port;
            address = stringValue(ctx.host) + ":" + ctx.port.getText();
        }
        BehaviorObject service = catalogObject(TargetType.Resource, ctx, start, address);
        BehaviorObject range = managementRange(TargetType.Resource, start, stop);
        service.setEndLine(range.getEndLine());
        service.setEndColumn(range.getEndColumn());
        // A runtime endpoint is a resource path, not a schema-qualified SQL object name.
        service.setObjectName(null);
        addRelation(BehaviorAction.STOP, service);
        addRelation(BehaviorAction.UNSAFE, service);
        return null;
    }

    @Override
    public Void visitSystemLicense(HanaParser.SystemLicenseContext ctx) {
        BehaviorObject license = catalogObject(TargetType.ConfigKey, ctx, ctx.LICENSE().getSymbol(), "LICENSE");
        if (ctx.UNSET() != null) {
            addRelation(BehaviorAction.RESET, license);
            addRelation(BehaviorAction.UNSAFE, license);
        } else {
            addRelation(BehaviorAction.CONFIGURE, license);
        }
        return null;
    }

    @Override
    public Void visitWorkloadClass(HanaParser.WorkloadClassContext ctx) {
        BehaviorObject resource;
        if (ctx.ALL() != null) {
            resource = objects.unnamedObject(TargetType.ResourceGroup, ctx.ALL().getSymbol(), UmiTypes.Catalog);
        } else {
            resource = securityLocal(TargetType.ResourceGroup, ctx.className);
        }
        BehaviorRelation relation = addRelation(securityAction(ctx), resource);
        if (ctx.workloadParent() != null && ctx.workloadParent().identifier() != null) {
            relation.setTarget(List.of(securityLocal(TargetType.ResourceGroup, ctx.workloadParent().identifier())));
        }
        return null;
    }

    @Override
    public Void visitWorkloadMapping(HanaParser.WorkloadMappingContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.className != null) {
            targets.add(securityLocal(TargetType.ResourceGroup, ctx.className));
        }
        List<HanaParser.WorkloadPropertyContext> properties = descendants(ctx, HanaParser.WorkloadPropertyContext.class);
        Token schema = null;
        for (var property : properties) {
            if (stringValue(property.key).equals("SCHEMA NAME")) {
                schema = property.value;
            }
        }
        for (var property : properties) {
            String key = stringValue(property.key);
            String value = stringValue(property.value);
            switch (key) {
                case "USER NAME":
                    targets.add(catalogObject(TargetType.User, property, property.value, value));
                    break;
                case "USERGROUP NAME":
                    targets.add(catalogObject(TargetType.Object, property, property.value, value));
                    break;
                case "SCHEMA NAME":
                    BehaviorObject schemaObject = catalogObject(TargetType.Schema, property, property.value, value);
                    schemaObject.setObjectName(new ObjectName(Objects.toString(levels.get(UmiTypes.Catalog), null), value, null));
                    targets.add(schemaObject);
                    break;
                case "OBJECT NAME":
                    if (schema == null) {
                        throw unsupported(ctx, "Workload object filter requires a schema filter");
                    }
                    String schemaName = stringValue(schema);
                    requireSecurityPathPart(ctx, schemaName);
                    BehaviorObject program = catalogObject(TargetType.ProgramObject, property, property.value, value);
                    BehaviorObject owner = objects.unnamedObject(TargetType.Schema, schema, UmiTypes.Catalog);
                    program.setObjectPath(owner.getObjectPath() + schemaName + "/" + value + "/");
                    program.setObjectName(new ObjectName(Objects.toString(levels.get(UmiTypes.Catalog), null), schemaName, value));
                    targets.add(program);
                    break;
                default:
                    break;
            }
        }
        addRelation(securityAction(ctx), securityLocal(TargetType.Policy, ctx.mappingName)).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSystemPlacement(HanaParser.SystemPlacementContext ctx) {
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (ctx.UNSET() != null) {
            action = BehaviorAction.RESET;
        }
        if (ctx.locationName != null) {
            addRelation(action, securityLocal(TargetType.Resource, ctx.locationName));
            return null;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        Token schema = null;
        for (var selector : ctx.placementSelector()) {
            if (selector.key.getType() == HanaParser.SCHEMA_NAME) {
                schema = selector.value;
            }
        }
        for (var selector : ctx.placementSelector()) {
            if (selector.key.getType() == HanaParser.SCHEMA_NAME) {
                String schemaName = stringValue(selector.value);
                BehaviorObject schemaObject = catalogObject(TargetType.Schema, selector, selector.value, schemaName);
                schemaObject.setObjectName(new ObjectName(Objects.toString(levels.get(UmiTypes.Catalog), null), schemaName, null));
                targets.add(schemaObject);
            } else if (selector.key.getType() == HanaParser.TABLE_NAME) {
                BehaviorObject table = objects.unnamedObject(TargetType.Table, selector.value, UmiTypes.Catalog);
                // A schema-less selector matches across schemas, including tables not yet created.
                if (schema != null) {
                    String schemaName = stringValue(schema);
                    String tableName = stringValue(selector.value);
                    requireSecurityPathPart(ctx, schemaName);
                    requireSecurityPathPart(ctx, tableName);
                    table.setObjectPath(table.getObjectPath() + schemaName + "/" + tableName + "/");
                    table.setObjectName(new ObjectName(Objects.toString(levels.get(UmiTypes.Catalog), null), schemaName, tableName));
                }
                targets.add(table);
            }
        }
        for (var property : ctx.placementProperty()) {
            if (property.placementKey().LOCATION() != null && property.STRING() != null) {
                String location = stringValue(property.STRING().getSymbol());
                String keyword = location.toUpperCase(Locale.ROOT);
                if (!Set.of("MASTER", "SLAVE", "ALL", "DEFAULT", "COORDINATOR").contains(keyword) && !keyword.startsWith("WORKER#")) {
                    targets.add(catalogObject(TargetType.Resource, property, property.STRING().getSymbol(), location));
                }
            }
        }
        addRelation(action, managementRange(TargetType.Policy, ctx.TABLE().getSymbol(), ctx.PLACEMENT().getSymbol())).setTarget(securityTargets(targets));
        return null;
    }

    @Override
    public Void visitSystemVolume(HanaParser.SystemVolumeContext ctx) {
        BehaviorObject owner = objects.unnamedObject(TargetType.StorageVolume, ctx.DATAVOLUME().getSymbol(), UmiTypes.Catalog);
        List<BehaviorObject> targets = new ArrayList<>();
        BehaviorObject site = null;
        if (ctx.siteName != null || ctx.siteId != null) {
            Token token = ctx.siteName;
            String siteName;
            if (token != null) {
                siteName = stringValue(token);
            } else {
                token = ctx.siteId;
                siteName = token.getText();
            }
            requireSecurityPathPart(ctx, siteName);
            site = objects.instanceObject(TargetType.Replication, token, siteName);
            owner = site;
        }
        if (ctx.path != null) {
            String path = stringValue(ctx.path);
            BehaviorObject file = objects.unnamedObject(TargetType.File, ctx.path, UmiTypes.Catalog);
            file.setObjectPath(owner.getObjectPath() + path.replaceFirst("^/+", "") + "/");
            file.setObjectName(new ObjectName(null, null, path));
            targets.add(file);
        }
        if (site != null) {
            targets.add(site);
        }
        BehaviorObject partition;
        BehaviorAction action;
        if (ctx.ADD() != null) {
            partition = objects.unnamedObject(TargetType.Partition, ctx.PARTITION().getSymbol(), UmiTypes.Catalog);
            partition.setObjectPath(owner.getObjectPath());
            action = BehaviorAction.CREATE;
        } else {
            partition = catalogObject(TargetType.Partition, ctx, ctx.partitionId, ctx.partitionId.getText());
            partition.setObjectPath(owner.getObjectPath() + ctx.partitionId.getText() + "/");
            partition.setObjectName(new ObjectName(null, null, ctx.partitionId.getText()));
            action = BehaviorAction.DROP;
        }
        addRelation(action, partition).setTarget(targets);
        return null;
    }

    @Override
    public Void visitSystemReclaim(HanaParser.SystemReclaimContext ctx) {
        BehaviorObject subject;
        BehaviorAction action = BehaviorAction.OPTIMIZE;
        if (ctx.DATAVOLUME() != null) {
            subject = objects.unnamedObject(TargetType.StorageVolume, ctx.DATAVOLUME().getSymbol(), UmiTypes.Catalog);
        } else if (ctx.FLEXIBLE() != null) {
            subject = managementRange(TargetType.Table, ctx.FLEXIBLE().getSymbol(), ctx.TABLES().getSymbol());
        } else if (ctx.LOB() != null) {
            Token start = ctx.LOB().getSymbol();
            if (ctx.ROW() != null) {
                start = ctx.ROW().getSymbol();
            } else if (ctx.COLUMN() != null) {
                start = ctx.COLUMN().getSymbol();
            }
            subject = managementRange(TargetType.File, start, ctx.SPACE().getSymbol());
            action = BehaviorAction.PURGE;
        } else if (ctx.VERSION() != null) {
            subject = managementRange(TargetType.Resource, ctx.VERSION().getSymbol(), ctx.SPACE().getSymbol());
            action = BehaviorAction.PURGE;
        } else {
            Token start = ctx.DATA().getSymbol();
            if (ctx.ROW() != null) {
                start = ctx.ROW().getSymbol();
            }
            subject = managementRange(TargetType.Resource, start, ctx.SPACE().getSymbol());
        }
        BehaviorRelation relation = addRelation(action, subject);
        if (ctx.address != null) {
            relation.setTarget(List.of(catalogObject(TargetType.Resource, ctx, ctx.address, stringValue(ctx.address))));
        }
        return null;
    }

    @Override
    public Void visitSystemReplication(HanaParser.SystemReplicationContext ctx) {
        BehaviorObject replication;
        if (ctx.siteName != null) {
            String name = stringValue(ctx.siteName);
            requireSecurityPathPart(ctx, name);
            replication = objects.instanceObject(TargetType.Replication, ctx.siteName, name);
        } else {
            replication = managementRange(TargetType.Replication, ctx.SYSTEM(1).getSymbol(), ctx.REPLICATION(0).getSymbol());
            replication.setObjectPath(objects.instanceObject(TargetType.Instance, ctx).getObjectPath());
        }
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (ctx.REGISTER() != null) {
            action = BehaviorAction.CREATE;
        } else if (ctx.UNREGISTER() != null) {
            action = BehaviorAction.DROP;
        }
        BehaviorRelation relation = addRelation(action, replication);
        if (ctx.host != null) {
            String host = stringValue(ctx.host);
            requireSecurityPathPart(ctx, host);
            relation.setTarget(List.of(objects.instanceObject(TargetType.Machine, ctx.host, host)));
        }
        if (ctx.DISABLE() != null || ctx.UNREGISTER() != null) {
            addRelation(BehaviorAction.UNSAFE, replication);
        }
        return null;
    }

    private BehaviorObject managementRange(TargetType type, Token start, Token stop) {
        BehaviorObject object = objects.unnamedObject(type, start, UmiTypes.Catalog);
        BehaviorObject end = objects.unnamedObject(type, stop, UmiTypes.Catalog);
        object.setEndLine(end.getEndLine());
        object.setEndColumn(end.getEndColumn());
        return object;
    }

    @Override
    public Void visitDatabaseManagement(HanaParser.DatabaseManagementContext ctx) {
        visitChildren(ctx);
        for (SplitQueryType splitType : script.getType()) {
            RuleQueryType type;
            if (splitType == SplitQueryType.SYSTEM_SETTING_WRITE) {
                type = RuleQueryType.CONFIG_WRITE;
            } else {
                type = RuleQueryType.valueOf(splitType.name());
            }
            addDomain(new RdbResourceDomain(), type);
        }
        return null;
    }

    private BehaviorObject databaseOwner(HanaParser.BackupTenantContext tenant, Token anchor) {
        if (tenant != null) {
            return object(TargetType.Catalog, tenant.identifier(), List.of(name(tenant.identifier())));
        }
        return objects.unnamedObject(TargetType.Catalog, anchor, UmiTypes.Catalog);
    }

    private BehaviorObject databaseResource(TargetType type, Token start, Token stop, BehaviorObject owner, String name) {
        BehaviorObject value = managementRange(type, start, stop);
        value.setObjectPath(owner.getObjectPath());
        if (name != null) {
            String pathName = name.replaceFirst("^/+", "");
            value.setObjectPath(owner.getObjectPath() + pathName + "/");
            value.setObjectName(new ObjectName(null, null, name));
        }
        return value;
    }

    private BehaviorObject databaseResource(TargetType type, Token token, BehaviorObject owner, String name) {
        return databaseResource(type, token, token, owner, name);
    }

    @Override
    public Void visitTenantDatabase(HanaParser.TenantDatabaseContext ctx) {
        BehaviorObject tenant = object(TargetType.Catalog, ctx.tenant, List.of(name(ctx.tenant)));
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.sourceTenant != null) {
            targets.add(object(TargetType.Catalog, ctx.sourceTenant, List.of(name(ctx.sourceTenant))));
        }
        for (var location : descendants(ctx, HanaParser.DatabaseLocationContext.class)) {
            Token token = location.STRING().getSymbol();
            String address = stringValue(token);
            TargetType type = TargetType.Machine;
            if (address.contains(":")) {
                type = TargetType.Resource;
            }
            targets.add(databaseResource(type, token, tenant, address));
        }
        if (ctx.getStart().getType() == HanaParser.RENAME) {
            addRelation(BehaviorAction.RENAME, tenant).setTarget(List.of(object(TargetType.Catalog, ctx.newTenant, List.of(name(ctx.newTenant)))));
        } else if (ctx.AS() != null) {
            addRelation(BehaviorAction.CREATE, tenant).setTarget(targets);
            BehaviorObject replication = databaseResource(TargetType.Replication, ctx.AS().getSymbol(), ctx.OF().getSymbol(), tenant, null);
            addRelation(BehaviorAction.CREATE, replication).setTarget(targets);
        } else if (ctx.FINALIZE() != null) {
            BehaviorObject replication = databaseResource(TargetType.Replication, ctx.FINALIZE().getSymbol(), ctx.REPLICA().getSymbol(), tenant, null);
            addRelation(BehaviorAction.DROP, replication);
            if (ctx.SOURCE() != null) {
                BehaviorObject source = managementRange(TargetType.Catalog, ctx.SOURCE().getSymbol(), ctx.getStop());
                source.setObjectPath(objects.instanceObject(TargetType.Instance, ctx).getObjectPath());
                addRelation(BehaviorAction.DROP, source);
            }
            addRelation(BehaviorAction.UNSAFE, replication);
        } else if (ctx.CANCEL() != null) {
            addRelation(BehaviorAction.RESET, databaseResource(TargetType.Replication, ctx.CANCEL().getSymbol(), ctx.REPLICA().getSymbol(), tenant, null));
        } else if (ctx.FALLBACK() != null) {
            BehaviorAction action = BehaviorAction.CREATE;
            if (!ctx.DROP().isEmpty()) {
                action = BehaviorAction.DROP;
            }
            addRelation(action, databaseResource(TargetType.Snapshot, ctx.FALLBACK().getSymbol(), ctx.SNAPSHOT().getSymbol(), tenant, null)).setTarget(List.of(tenant));
        } else {
            BehaviorAction action = securityAction(ctx);
            if (ctx.getStart().getType() == HanaParser.ALTER && ctx.TO() != null) {
                Token token = ctx.STRING(ctx.STRING().size() - 1).getSymbol();
                targets.add(databaseResource(TargetType.Resource, token, tenant, stringValue(token)));
            }
            addRelation(action, tenant).setTarget(targets);
            if (ctx.BACKUPS() != null) {
                BehaviorObject backup = databaseResource(TargetType.Backup, ctx.BACKUPS().getSymbol(), tenant, null);
                addRelation(BehaviorAction.PURGE, backup);
                addRelation(BehaviorAction.UNSAFE, backup);
            }
        }
        return null;
    }

    @Override
    public Void visitDatabaseRuntime(HanaParser.DatabaseRuntimeContext ctx) {
        BehaviorObject tenant = object(TargetType.Catalog, ctx.tenant, List.of(name(ctx.tenant)));
        if (ctx.FALLBACK() != null) {
            BehaviorObject snapshot = databaseResource(TargetType.Snapshot, ctx.FALLBACK().getSymbol(), ctx.SNAPSHOT().getSymbol(), tenant, null);
            addRelation(BehaviorAction.RESTORE, tenant).setTarget(List.of(snapshot));
        }
        BehaviorAction action = BehaviorAction.START;
        if (ctx.STOP() != null) {
            action = BehaviorAction.STOP;
        }
        addRelation(action, tenant);
        if (ctx.STOP() != null || ctx.FALLBACK() != null) {
            addRelation(BehaviorAction.UNSAFE, tenant);
        }
        return null;
    }

    @Override
    public Void visitDatabaseMaintenance(HanaParser.DatabaseMaintenanceContext ctx) {
        if (ctx.SAVEPOINT() != null) {
            addRelation(BehaviorAction.CHECKPOINT, objects.unnamedObject(TargetType.Catalog, ctx.SAVEPOINT().getSymbol(), UmiTypes.Catalog));
        } else if (ctx.LOGGING() != null) {
            BehaviorObject log = objects.unnamedObject(TargetType.Log, ctx.LOGGING().getSymbol(), UmiTypes.Catalog);
            addRelation(BehaviorAction.CONFIGURE, log);
            if (ctx.OFF() != null) {
                addRelation(BehaviorAction.UNSAFE, log);
            }
        } else {
            addRelation(BehaviorAction.PURGE, objects.unnamedObject(TargetType.Log, ctx.LOG().getSymbol(), UmiTypes.Catalog));
        }
        return null;
    }

    @Override
    public Void visitTableReplicas(HanaParser.TableReplicasContext ctx) {
        addRelation(BehaviorAction.CONFIGURE, managementRange(TargetType.Replication, ctx.ALL().getSymbol(), ctx.REPLICAS().getSymbol()));
        return null;
    }

    private BehaviorObject backupDescriptor(HanaParser.BackupDestinationContext ctx, BehaviorObject owner) {
        return databaseResource(TargetType.Backup, ctx.prefix, owner, stringValue(ctx.prefix));
    }

    private List<BehaviorObject> backupDirectory(HanaParser.BackupDestinationContext ctx, BehaviorObject owner) {
        List<BehaviorObject> references = new ArrayList<>();
        if (ctx.directory != null) {
            references.add(databaseResource(TargetType.File, ctx.directory, owner, stringValue(ctx.directory)));
        }
        return references;
    }

    private List<BehaviorObject> recoveryReferences(ParserRuleContext ctx, BehaviorObject owner) {
        List<BehaviorObject> references = new ArrayList<>();
        for (var reference : descendants(ctx, HanaParser.RecoveryReferenceContext.class)) {
            if (reference.source != null) {
                references.add(databaseResource(TargetType.Resource, reference.source, owner, stringValue(reference.source)));
            } else if (!reference.paths.isEmpty()) {
                for (Token path : reference.paths) {
                    references.add(databaseResource(TargetType.File, path, owner, stringValue(path)));
                }
            } else if (reference.backupId != null) {
                references.add(databaseResource(TargetType.Backup, reference.backupId, owner, reference.backupId.getText()));
            } else if (reference.SNAPSHOT() != null) {
                references.add(databaseResource(TargetType.Snapshot, reference.SNAPSHOT().getSymbol(), owner, null));
            } else if (reference.backupDestination() != null) {
                references.addAll(backupDirectory(reference.backupDestination(), owner));
                references.add(backupDescriptor(reference.backupDestination(), owner));
            }
        }
        return references;
    }

    @Override
    public Void visitBackupData(HanaParser.BackupDataContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.DATA().getSymbol());
        List<BehaviorObject> references = new ArrayList<>(List.of(owner));
        references.addAll(backupDirectory(ctx.backupDestination(), owner));
        addRelation(BehaviorAction.EXPORT, backupDescriptor(ctx.backupDestination(), owner)).setTarget(references);
        return null;
    }

    @Override
    public Void visitBackupCancel(HanaParser.BackupCancelContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.BACKUP().getSymbol());
        addRelation(BehaviorAction.TERMINATE, databaseResource(TargetType.Backup, ctx.backupId, owner, ctx.backupId.getText()));
        return null;
    }

    @Override
    public Void visitBackupCheck(HanaParser.BackupCheckContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.BACKUP().getSymbol());
        if (ctx.backupId != null) {
            addRelation(BehaviorAction.VALIDATE, databaseResource(TargetType.Backup, ctx.backupId, owner, ctx.backupId.getText())).setTarget(recoveryReferences(ctx, owner));
        } else {
            BehaviorRelation relation = addRelation(BehaviorAction.VALIDATE, databaseResource(TargetType.File, ctx.path, owner, stringValue(ctx.path)));
            if (ctx.backupTenant() != null) {
                relation.setTarget(List.of(owner));
            }
        }
        return null;
    }

    @Override
    public Void visitBackupCatalog(HanaParser.BackupCatalogContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.BACKUP().getSymbol());
        BehaviorObject backup = databaseResource(TargetType.Backup, ctx.backupId, owner, ctx.backupId.getText());
        if (ctx.DELETE() == null) {
            BehaviorAction action = BehaviorAction.CONFIGURE;
            if (ctx.UNSET() != null) {
                action = BehaviorAction.RESET;
            }
            addRelation(action, backup);
            return null;
        }
        BehaviorObject catalog = databaseResource(TargetType.Log, ctx.BACKUP().getSymbol(), ctx.CATALOG().getSymbol(), owner, null);
        addRelation(BehaviorAction.PURGE, catalog).setTarget(List.of(backup));
        if (ctx.WITH() != null || ctx.COMPLETE() != null) {
            if (ctx.ALL() != null) {
                BehaviorObject affected = databaseResource(TargetType.Backup, ctx.ALL().getSymbol(), ctx.BEFORE().getSymbol(), owner, null);
                addRelation(BehaviorAction.PURGE, affected).setTarget(List.of(backup));
            } else {
                addRelation(BehaviorAction.PURGE, backup);
            }
        }
        addRelation(BehaviorAction.UNSAFE, catalog);
        return null;
    }

    @Override
    public Void visitBackupSnapshot(HanaParser.BackupSnapshotContext ctx) {
        BehaviorObject snapshot;
        BehaviorAction action = BehaviorAction.DROP;
        if (ctx.backupId != null) {
            snapshot = objects.instanceObject(TargetType.Snapshot, ctx.backupId, ctx.backupId.getText());
            action = BehaviorAction.ALTER;
        } else {
            snapshot = objects.instanceObject(TargetType.Snapshot, ctx.SNAPSHOT().getSymbol());
            if (ctx.CREATE() != null) {
                action = BehaviorAction.CREATE;
            }
        }
        addRelation(action, snapshot);
        return null;
    }

    @Override
    public Void visitBackupList(HanaParser.BackupListContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.DATA().getSymbol());
        addRelation(BehaviorAction.READ, databaseResource(TargetType.Backup, ctx.DATA().getSymbol(), owner, null)).setTarget(recoveryReferences(ctx, owner));
        return null;
    }

    @Override
    public Void visitRecoverDatabase(HanaParser.RecoverDatabaseContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.getStart());
        if (ctx.backupTenant() == null) {
            owner = databaseOwner(null, ctx.DATA().getSymbol());
        }
        addRelation(BehaviorAction.RECOVER, owner).setTarget(recoveryReferences(ctx, owner));
        Token clearLog = null;
        Token license = null;
        Token configuration = null;
        if (ctx.CLEAR() != null) {
            clearLog = ctx.LOG().getSymbol();
        }
        for (var option : ctx.recoveryOption()) {
            if (option.CLEAR() != null) {
                clearLog = option.LOG().getSymbol();
            } else if (option.SET() != null) {
                license = option.LICENSE().getSymbol();
            } else if (option.INCLUDE() != null) {
                configuration = option.CONFIGURATION().getSymbol();
            }
        }
        if (clearLog != null) {
            addRelation(BehaviorAction.RESET, databaseResource(TargetType.Log, clearLog, owner, null));
        }
        if (license != null) {
            addRelation(BehaviorAction.CONFIGURE, databaseResource(TargetType.ConfigKey, license, owner, "LICENSE"));
        }
        if (configuration != null) {
            addRelation(BehaviorAction.RESTORE, databaseResource(TargetType.ConfigKey, configuration, owner, "CONFIGURATION"));
        }
        addRelation(BehaviorAction.UNSAFE, owner);
        return null;
    }

    @Override
    public Void visitBackupKeys(HanaParser.BackupKeysContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.getStart());
        BehaviorObject keys = databaseResource(TargetType.SymmetricKey, ctx.rootKeys().getStart(), ctx.rootKeys().getStop(), owner, null);
        List<BehaviorObject> references = new ArrayList<>(List.of(keys));
        references.addAll(backupDirectory(ctx.backupDestination(), owner));
        addRelation(BehaviorAction.EXPORT, backupDescriptor(ctx.backupDestination(), owner)).setTarget(references);
        return null;
    }

    @Override
    public Void visitRecoverKeys(HanaParser.RecoverKeysContext ctx) {
        BehaviorObject owner = databaseOwner(ctx.backupTenant(), ctx.getStart());
        BehaviorObject keys = databaseResource(TargetType.SymmetricKey, ctx.rootKeys().getStart(), ctx.rootKeys().getStop(), owner, null);
        List<BehaviorObject> references = backupDirectory(ctx.backupDestination(), owner);
        references.add(backupDescriptor(ctx.backupDestination(), owner));
        addRelation(BehaviorAction.RESTORE, keys).setTarget(references);
        if (ctx.SETTINGS() != null) {
            BehaviorObject settings = databaseResource(TargetType.ConfigKey, ctx.SETTINGS().getSymbol(), owner, null);
            addRelation(BehaviorAction.RESTORE, settings);
            addRelation(BehaviorAction.UNSAFE, settings);
        }
        return null;
    }

    @Override
    public Void visitTableLoad(HanaParser.TableLoadContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        List<BehaviorObject> columns = new ArrayList<>();
        if (ctx.columnNames() != null) {
            for (var column : ctx.columnNames().identifier()) {
                requireSecurityPathPart(column, name(column));
                columns.add(databaseResource(TargetType.Column, column.getStart(), table, name(column)));
            }
        }
        addRelation(BehaviorAction.LOAD, table).setTarget(columns);
        return null;
    }

    @Override
    public Void visitTableUnload(HanaParser.TableUnloadContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        if (ctx.unloadPartition().isEmpty()) {
            // UNLOAD releases residency; DELETE PERSISTENT MEMORY does not delete table rows.
            addRelation(BehaviorAction.RESET, table);
        } else {
            for (var partition : ctx.unloadPartition()) {
                Token id = partition.INTEGER().getSymbol();
                addRelation(BehaviorAction.RESET, databaseResource(TargetType.Partition, id, table, id.getText())).setTarget(List.of(table));
            }
        }
        return null;
    }

    @Override
    public Void visitDeltaMerge(HanaParser.DeltaMergeContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        if (ctx.partitionId == null) {
            addRelation(BehaviorAction.OPTIMIZE, table);
        } else {
            addRelation(BehaviorAction.OPTIMIZE, databaseResource(TargetType.Partition, ctx.partitionId, table, ctx.partitionId.getText())).setTarget(List.of(table));
        }
        return null;
    }

    private List<BehaviorObject> transferObjects(HanaParser.TransferObjectsContext ctx) {
        if (ctx.ALL() != null) {
            return List.of(objects.unnamedObject(TargetType.Catalog, ctx.ALL().getSymbol(), UmiTypes.Catalog));
        }
        List<BehaviorObject> result = new ArrayList<>();
        for (var item : ctx.qualifiedName()) {
            List<String> parts = names(item);
            if (parts.size() == 2 && parts.get(1).equals("*")) {
                result.add(object(TargetType.Schema, item, List.of(parts.get(0))));
            } else {
                result.add(object(TargetType.SchemaObject, item));
            }
        }
        return result;
    }

    private List<BehaviorObject> transferReferences(ParserRuleContext ctx, BehaviorObject owner) {
        List<BehaviorObject> references = new ArrayList<>();
        for (var credential : descendants(ctx, HanaParser.TransferCredentialContext.class)) {
            Token token = credential.STRING().getSymbol();
            requireSecurityPathPart(credential, stringValue(token));
            references.add(databaseResource(TargetType.Credential, token, owner, stringValue(token)));
        }
        for (var location : descendants(ctx, HanaParser.DatabaseLocationContext.class)) {
            Token token = location.STRING().getSymbol();
            references.add(databaseResource(TargetType.Resource, token, owner, stringValue(token)));
        }
        references.sort(SECURITY_SOURCE_ORDER);
        return references;
    }

    @Override
    public Void visitCatalogExport(HanaParser.CatalogExportContext ctx) {
        BehaviorObject owner = databaseOwner(null, ctx.getStart());
        BehaviorObject file = databaseResource(TargetType.File, ctx.path, owner, stringValue(ctx.path));
        List<BehaviorObject> references = new ArrayList<>(transferObjects(ctx.transferObjects()));
        references.addAll(transferReferences(ctx, owner));
        addRelation(BehaviorAction.EXPORT, file).setTarget(references);
        if (ctx.whereClause() != null) {
            visit(ctx.whereClause());
        }
        return null;
    }

    @Override
    public Void visitCatalogImport(HanaParser.CatalogImportContext ctx) {
        BehaviorObject owner = databaseOwner(null, ctx.getStart());
        List<BehaviorObject> references = new ArrayList<>(List.of(databaseResource(TargetType.File, ctx.path, owner, stringValue(ctx.path))));
        references.addAll(transferReferences(ctx, owner));
        for (var destination : transferObjects(ctx.transferObjects())) {
            addRelation(BehaviorAction.IMPORT, destination).setTarget(references);
        }
        return null;
    }

    @Override
    public Void visitExportRows(HanaParser.ExportRowsContext ctx) {
        BehaviorObject owner = databaseOwner(null, ctx.getStart());
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        List<BehaviorObject> references = new ArrayList<>(List.of(table));
        references.addAll(transferReferences(ctx, owner));
        addRelation(BehaviorAction.EXPORT, databaseResource(TargetType.File, ctx.path, owner, stringValue(ctx.path))).setTarget(references);
        return null;
    }

    @Override
    public Void visitImportRows(HanaParser.ImportRowsContext ctx) {
        BehaviorObject owner = databaseOwner(null, ctx.getStart());
        BehaviorObject table = objects.unnamedObject(TargetType.Table, ctx.FROM().getSymbol(), UmiTypes.Catalog);
        if (ctx.qualifiedName() != null) {
            table = object(TargetType.Table, ctx.qualifiedName());
        }
        BehaviorObject file = databaseResource(TargetType.File, ctx.path, owner, stringValue(ctx.path));
        List<BehaviorObject> references = new ArrayList<>(List.of(file));
        references.addAll(transferReferences(ctx, owner));
        addRelation(BehaviorAction.IMPORT, table).setTarget(references);
        for (var option : ctx.importRowsOption()) {
            if (option.columnNames() != null) {
                for (var column : option.columnNames().identifier()) {
                    requireSecurityPathPart(column, name(column));
                    addRelation(BehaviorAction.IMPORT, databaseResource(TargetType.Column, column.getStart(), table, name(column))).setTarget(List.of(file));
                }
            }
            if (option.errorPath != null) {
                addRelation(BehaviorAction.EXPORT, databaseResource(TargetType.File, option.errorPath, owner, stringValue(option.errorPath))).setTarget(List.of(table));
            }
        }
        return null;
    }

    @Override
    public Void visitImportScan(HanaParser.ImportScanContext ctx) {
        BehaviorObject owner = databaseOwner(null, ctx.getStart());
        addRelation(BehaviorAction.READ, databaseResource(TargetType.File, ctx.path, owner, stringValue(ctx.path))).setTarget(transferReferences(ctx, owner));
        return null;
    }

    @Override
    public Void visitTransactionStatement(HanaParser.TransactionStatementContext ctx) {
        addRelation(BehaviorAction.SWITCH, objects.unnamedObject(TargetType.Transaction, ctx, UmiTypes.Catalog));
        addDomain(new RdbResourceDomain(), RuleQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitExplainStatement(HanaParser.ExplainStatementContext ctx) {
        explaining = true;
        visitChildren(ctx);
        explaining = false;
        return null;
    }

    @Override
    public Void visitCreateSchema(HanaParser.CreateSchemaContext ctx) {
        String schema = name(ctx.identifier());
        if (schema.contains("/"))
            throw unsupported(ctx, "Schema contains a resource path separator");
        BehaviorObject target = object(TargetType.Schema, ctx, List.of(schema));
        addRelation(BehaviorAction.CREATE, target);
        addDomain(HanaRuleDomains.objectDomain(TargetType.Schema, resource(target)), RuleQueryType.CREATE_SCHEMA);
        return null;
    }

    @Override
    public Void visitCreateTable(HanaParser.CreateTableContext ctx) {
        BehaviorObject target = object(TargetType.Table, ctx.qualifiedName());
        addRelation(BehaviorAction.CREATE, target);
        RdbTableDomain domain = (RdbTableDomain) HanaRuleDomains.objectDomain(TargetType.Table, resource(target));
        List<HanaParser.ColumnDefinitionContext> columns = descendants(ctx, HanaParser.ColumnDefinitionContext.class);
        domain.setColumns(columns.stream().map(c -> name(c.identifier())).toList());
        domain.setHasPrimary(descendants(ctx, HanaParser.ColumnOptionContext.class).stream().anyMatch(c -> c.PRIMARY() != null)
                             || descendants(ctx, HanaParser.TableConstraintContext.class).stream().anyMatch(c -> c.PRIMARY() != null));
        domain.setHasUnique(descendants(ctx, HanaParser.ColumnOptionContext.class).stream().anyMatch(c -> c.UNIQUE() != null)
                            || descendants(ctx, HanaParser.TableConstraintContext.class).stream().anyMatch(c -> c.UNIQUE() != null));
        domain.setHasForeignKey(descendants(ctx, HanaParser.ColumnOptionContext.class).stream().anyMatch(c -> c.REFERENCES() != null)
                                || descendants(ctx, HanaParser.TableConstraintContext.class).stream().anyMatch(c -> c.FOREIGN() != null));
        if (ctx.STRING() != null) {
            domain.setComment(ctx.STRING().getText());
        }
        RuleQueryType type = RuleQueryType.CREATE_TABLE;
        if (ctx.query() != null) {
            type = RuleQueryType.CREATE_TABLE_SELECT;
        }
        addDomain(domain, type);
        addColumns(target, columns, descendants(ctx, HanaParser.TableConstraintContext.class), RuleQueryType.CREATE_TABLE_ADD_COLUMN, RuleQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        addConstraints(target, descendants(ctx, HanaParser.TableConstraintContext.class), RuleQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateView(HanaParser.CreateViewContext ctx) {
        definition(BehaviorAction.CREATE, TargetType.View, ctx.qualifiedName(), RuleQueryType.CREATE_VIEW);
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateIndex(HanaParser.CreateIndexContext ctx) {
        BehaviorObject index = object(TargetType.Index, ctx.qualifiedName(0));
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName(1));
        // Index ownership is the table; require DDL on that table, not a guessed schema/index path.
        addRelation(BehaviorAction.ALTER, table);
        RdbIndexDomain domain = new RdbIndexDomain();
        var names = resource(index);
        var tableNames = resource(table);
        domain.setCatalog(names.get(TargetType.Catalog));
        domain.setSchema(names.get(TargetType.Schema));
        domain.setName(names.get(TargetType.Index));
        domain.setTableCatalog(tableNames.get(TargetType.Catalog));
        domain.setTableSchema(tableNames.get(TargetType.Schema));
        domain.setTableName(tableNames.get(TargetType.Table));
        domain.setColumns(columnNamesIn(ctx));
        domain.setType("INDEX");
        if (ctx.UNIQUE() != null) {
            domain.setType("UNIQUE");
        }
        addDomain(domain, RuleQueryType.ADD_INDEX);
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateSequence(HanaParser.CreateSequenceContext ctx) {
        definition(BehaviorAction.CREATE, TargetType.Sequence, ctx.qualifiedName(), RuleQueryType.CREATE_SEQUENCE);
        return null;
    }

    @Override
    public Void visitCreateSynonym(HanaParser.CreateSynonymContext ctx) {
        definition(BehaviorAction.CREATE, TargetType.Synonym, ctx.qualifiedName(0), RuleQueryType.CREATE_SYNONYM);
        addRelation(BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName(1)));
        return null;
    }

    @Override
    public Void visitAlterTable(HanaParser.AlterTableContext ctx) {
        BehaviorObject table = definition(BehaviorAction.ALTER, TargetType.Table, ctx.qualifiedName(), RuleQueryType.ALTER_TABLE);
        RuleQueryType type = RuleQueryType.ALTER_TABLE_ADD_COLUMN;
        if (ctx.alterAction().ALTER() != null) {
            type = RuleQueryType.ALTER_TABLE_ALTER_COLUMN;
        }
        addColumns(table, descendants(ctx.alterAction(), HanaParser.ColumnDefinitionContext.class), descendants(ctx
            .alterAction(), HanaParser.TableConstraintContext.class), type, RuleQueryType.ALTER_TABLE_ADD_CONSTRAINT);
        addConstraints(table, descendants(ctx.alterAction(), HanaParser.TableConstraintContext.class), RuleQueryType.ALTER_TABLE_ADD_CONSTRAINT);

        for (var column : ctx.alterAction().alterColumnDefinition()) {
            RdbColumnDomain domain = columnDomain(table, name(column.identifier()));
            if (column.dataType() != null) {
                domain.setTypeName(name(column.dataType().identifier()));
                domain.setTypeDesc(column.dataType().getText());
            }
            for (var option : column.columnOption()) {
                if (option.DEFAULT() != null && option.expression() != null)
                    domain.setDefaultValue(option.expression().getText());
                if (option.NULL() != null)
                    domain.setNullable(option.NOT() == null);
            }
            addDomain(domain, RuleQueryType.ALTER_TABLE_ALTER_COLUMN);
        }

        if (ctx.alterAction().DROP() != null && ctx.alterAction().PRIMARY() != null) {
            HanaConstraintDomain domain = constraintDomain(table);
            domain.setType(SqlConstraintType.Primary);
            addDomain(domain, RuleQueryType.ALTER_TABLE_DROP_CONSTRAINT);
        } else if (ctx.alterAction().DROP() != null && ctx.alterAction().CONSTRAINT() != null) {
            HanaConstraintDomain domain = constraintDomain(table);
            domain.setName(name(ctx.alterAction().identifier(0)));
            addDomain(domain, RuleQueryType.ALTER_TABLE_DROP_CONSTRAINT);
        } else if (ctx.alterAction().DROP() != null) {
            for (var column : ctx.alterAction().identifier()) {
                RdbColumnDomain domain = columnDomain(table, name(column));
                addDomain(domain, RuleQueryType.ALTER_TABLE_DROP_COLUMN);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropStatement(HanaParser.DropStatementContext ctx) {
        String kind = ctx.objectType().getText().toUpperCase(Locale.ROOT);
        TargetType target = objectType(kind);
        if (target == TargetType.Index) {
            indexChange(ctx.qualifiedName(), null);
            return null;
        }
        definition(BehaviorAction.DROP, target, ctx.qualifiedName(), RuleQueryType.valueOf("DROP_" + kind));
        return null;
    }

    private void indexChange(HanaParser.QualifiedNameContext name, String newName) {
        BehaviorObject index = object(TargetType.Index, name);
        var indexResource = resource(index);
        if (indexedObjectResolver == null) {
            throw unsupported(name, "Index DDL requires live indexed-object metadata");
        }
        Map<UmiTypes, Object> indexLevels = new HashMap<>(levels);
        indexLevels.put(UmiTypes.Schema, indexResource.get(TargetType.Schema));
        MetaIndexedObject indexedObject = indexedObjectResolver.apply(indexLevels, indexResource.get(TargetType.Index));
        if (indexedObject == null || indexedObject.getName() == null || indexedObject.getName().isBlank()) {
            throw unsupported(name, "Indexed object was not found");
        }

        if (indexedObject.getType() != UmiTypes.Table) {
            throw unsupported(name, "Unsupported HANA indexed object type: " + indexedObject.getType());
        }

        if (!Objects.equals(indexedObject.getCatalog(), indexResource.get(TargetType.Catalog))) {
            throw unsupported(name, "Cross-database indexed objects are not supported");
        }
        BehaviorObject table = object(TargetType.Table, name, List.of(indexedObject.getSchema(), indexedObject.getName()));
        addRelation(BehaviorAction.ALTER, table);
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.setCatalog(indexResource.get(TargetType.Catalog));
        domain.setSchema(indexResource.get(TargetType.Schema));
        domain.setName(indexResource.get(TargetType.Index));
        domain.setTableCatalog(indexedObject.getCatalog());
        domain.setTableSchema(indexedObject.getSchema());
        domain.setTableName(indexedObject.getName());
        domain.setNewName(newName);
        RuleQueryType type = RuleQueryType.DROP_INDEX;
        if (newName != null) {
            type = RuleQueryType.RENAME_INDEX;
        }
        addDomain(domain, type);
    }

    @Override
    public Void visitRenameStatement(HanaParser.RenameStatementContext ctx) {
        if (ctx.INDEX() != null) {
            if (ctx.qualifiedName(1).identifier().size() != 1) {
                throw unsupported(ctx, "RENAME INDEX requires an unqualified new name");
            }
            indexChange(ctx.qualifiedName(0), name(ctx.qualifiedName(1).identifier(0)));
            return null;
        }
        if (ctx.COLUMN() != null) {
            List<String> parts = names(ctx.qualifiedName(0));
            if (parts.size() < 2 || parts.size() > 3 || ctx.qualifiedName(1).identifier().size() != 1) {
                throw unsupported(ctx, "RENAME COLUMN requires a table/column and an unqualified new name");
            }
            BehaviorObject table = object(TargetType.Table, ctx.qualifiedName(0), parts.subList(0, parts.size() - 1));
            addRelation(BehaviorAction.ALTER, table);
            RdbColumnDomain domain = columnDomain(table, parts.get(parts.size() - 1));
            domain.setNewName(name(ctx.qualifiedName(1).identifier(0)));
            addDomain(domain, RuleQueryType.ALTER_TABLE_RENAME_COLUMN);
            return null;
        }
        BehaviorObject from = object(TargetType.Table, ctx.qualifiedName(0));
        BehaviorObject to = object(TargetType.Table, ctx.qualifiedName(1));
        addRelation(BehaviorAction.RENAME, from).getTarget().add(to);
        RdbTableDomain domain = (RdbTableDomain) HanaRuleDomains.objectDomain(TargetType.Table, resource(from));
        domain.setNewName(to.getObjectName().getObjectName());
        addDomain(domain, RuleQueryType.RENAME_TABLE);
        return null;
    }

    @Override
    public Void visitTruncateStatement(HanaParser.TruncateStatementContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.qualifiedName());
        if (ctx.maintenancePartitions() == null) {
            addRelation(BehaviorAction.DELETE, table);
        } else {
            for (var partition : ctx.maintenancePartitions().INTEGER()) {
                Token id = partition.getSymbol();
                addRelation(BehaviorAction.DELETE, databaseResource(TargetType.Partition, id, table, id.getText())).setTarget(List.of(table));
            }
        }
        HanaDeleteDomain domain = new HanaDeleteDomain();
        configureName(domain, table);
        addDomain(domain, RuleQueryType.TRUNCATE);
        return null;
    }

    @Override
    public Void visitCommentStatement(HanaParser.CommentStatementContext ctx) {
        if (ctx.COLUMN() != null) {
            List<String> names = names(ctx.qualifiedName());
            if (names.size() < 2 || names.size() > 3) {
                throw unsupported(ctx, "COMMENT COLUMN requires a table and column");
            }
            BehaviorObject table = object(TargetType.Table, ctx.qualifiedName(), names.subList(0, names.size() - 1));
            addRelation(BehaviorAction.ALTER, table);
            RdbColumnDomain domain = new RdbColumnDomain();
            var resource = resource(table);
            domain.setCatalog(resource.get(TargetType.Catalog));
            domain.setSchema(resource.get(TargetType.Schema));
            domain.setTable(resource.get(TargetType.Table));
            domain.setColumn(names.get(names.size() - 1));
            if (ctx.STRING() != null) {
                domain.setComment(ctx.STRING().getText());
            }
            addDomain(domain, RuleQueryType.COMMENT_COLUMN);
        } else {
            TargetType target = TargetType.Table;
            RuleQueryType type = RuleQueryType.COMMENT_TABLE;
            if (ctx.VIEW() != null) {
                target = TargetType.View;
                type = RuleQueryType.COMMENT_VIEW;
            }
            definition(BehaviorAction.ALTER, target, ctx.qualifiedName(), type);
        }
        return null;
    }

    @Override
    public Void visitCreateRoutine(HanaParser.CreateRoutineContext ctx) {
        TargetType target = TargetType.Procedure;
        RuleQueryType type = RuleQueryType.CREATE_PROCEDURE;
        if (ctx.FUNCTION() != null) {
            target = TargetType.Function;
            type = RuleQueryType.CREATE_FUNCTION;
        }
        definition(BehaviorAction.CREATE, target, ctx.qualifiedName(), type);
        Object previousSchema = levels.get(UmiTypes.Schema);
        for (var option : ctx.routineOption()) {
            if (option.SCHEMA() != null) {
                levels.put(UmiTypes.Schema, name(option.identifier()));
            }
        }
        variables.push(new LinkedHashMap<>());
        for (var parameter : ctx.parameter()) {
            variables.peek().put(name(parameter.identifier()), List.of());
            visit(parameter);
        }
        if (ctx.identifier() != null) {
            variables.peek().put(name(ctx.identifier()), List.of());
        }
        visit(ctx.block());
        variables.pop();
        levels.put(UmiTypes.Schema, previousSchema);
        return null;
    }

    @Override
    public Void visitCreateTrigger(HanaParser.CreateTriggerContext ctx) {
        definition(BehaviorAction.CREATE, TargetType.Trigger, ctx.qualifiedName(0), RuleQueryType.CREATE_TRIGGER);
        addRelation(BehaviorAction.ALTER, object(TargetType.Table, ctx.qualifiedName(1)));
        variables.push(new LinkedHashMap<>());
        ctx.identifier().forEach(identifier -> variables.peek().put(name(identifier), List.of()));
        visit(ctx.block());
        variables.pop();
        return null;
    }

    @Override
    public Void visitColumnOption(HanaParser.ColumnOptionContext ctx) {
        if (ctx.REFERENCES() != null) {
            addRelation(BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitTableConstraint(HanaParser.TableConstraintContext ctx) {
        if (ctx.REFERENCES() != null) {
            addRelation(BehaviorAction.READ, object(TargetType.Table, ctx.qualifiedName()));
        }
        return visitChildren(ctx);
    }

    private void addConstraints(BehaviorObject table, List<HanaParser.TableConstraintContext> constraints, RuleQueryType type) {
        for (var constraint : constraints) {
            HanaConstraintDomain domain = constraintDomain(table);
            if (constraint.identifier() != null)
                domain.setName(name(constraint.identifier()));
            if (constraint.constraintColumns() != null) {
                domain.setColumns(constraint.constraintColumns().identifier().stream().map(HanaBehaviorParserVisitor::name).toList());
            } else if (!constraint.columnNames().isEmpty()) {
                domain.setColumns(columns(constraint.columnNames(0)));
            }
            SqlConstraintType kind = SqlConstraintType.Check;
            if (constraint.PRIMARY() != null)
                kind = SqlConstraintType.Primary;
            else if (constraint.UNIQUE() != null)
                kind = SqlConstraintType.Unique;
            else if (constraint.FOREIGN() != null)
                kind = SqlConstraintType.ForeignKey;
            domain.setType(kind);
            addDomain(domain, type);
        }
    }

    private HanaConstraintDomain constraintDomain(BehaviorObject table) {
        var names = resource(table);
        HanaConstraintDomain domain = new HanaConstraintDomain();
        domain.setCatalog(names.get(TargetType.Catalog));
        domain.setSchema(names.get(TargetType.Schema));
        domain.setTableCatalog(names.get(TargetType.Catalog));
        domain.setTableSchema(names.get(TargetType.Schema));
        domain.setTableName(names.get(TargetType.Table));
        return domain;
    }

    private RdbColumnDomain columnDomain(BehaviorObject table, String column) {
        var resource = resource(table);
        RdbColumnDomain domain = new RdbColumnDomain();
        domain.setCatalog(resource.get(TargetType.Catalog));
        domain.setSchema(resource.get(TargetType.Schema));
        domain.setTable(resource.get(TargetType.Table));
        domain.setColumn(column);
        return domain;
    }

    private void addColumns(BehaviorObject table, List<HanaParser.ColumnDefinitionContext> columns, List<HanaParser.TableConstraintContext> constraints, RuleQueryType type,
                            RuleQueryType constraintType) {
        for (var column : columns) {
            RdbColumnDomain domain = columnDomain(table, name(column.identifier()));
            domain.setTypeName(name(column.dataType().identifier()));
            domain.setTypeDesc(column.dataType().getText());
            domain.setNullable(true);
            if (!column.dataType().INTEGER().isEmpty()) {
                domain.setLength(column.dataType().INTEGER(0).getText());
            }
            for (var option : column.columnOption()) {
                if (option.NOT() != null || option.PRIMARY() != null)
                    domain.setNullable(false);
                if (option.PRIMARY() != null)
                    domain.setPrimary(true);
                if (option.UNIQUE() != null)
                    domain.setUnique(true);
                if (option.REFERENCES() != null)
                    domain.setForeign(true);
                if (option.PRIMARY() != null || option.UNIQUE() != null || option.REFERENCES() != null) {
                    HanaConstraintDomain constraint = constraintDomain(table);
                    constraint.setColumns(List.of(domain.getColumn()));
                    SqlConstraintType kind = SqlConstraintType.ForeignKey;
                    if (option.PRIMARY() != null)
                        kind = SqlConstraintType.Primary;
                    else if (option.UNIQUE() != null)
                        kind = SqlConstraintType.Unique;
                    constraint.setType(kind);
                    addDomain(constraint, constraintType);
                }
                if (option.COMMENT() != null)
                    domain.setComment(option.STRING().getText());
                if (option.DEFAULT() != null && option.expression() != null)
                    domain.setDefaultValue(option.expression().getText());
            }
            for (var constraint : constraints) {
                List<String> constraintColumns = List.of();
                if (constraint.constraintColumns() != null) {
                    constraintColumns = constraint.constraintColumns().identifier().stream().map(HanaBehaviorParserVisitor::name).toList();
                } else if (!constraint.columnNames().isEmpty()) {
                    constraintColumns = columns(constraint.columnNames(0));
                }
                if (constraintColumns.contains(domain.getColumn())) {
                    if (constraint.PRIMARY() != null) {
                        domain.setPrimary(true);
                        domain.setNullable(false);
                    }
                    if (constraint.FOREIGN() != null)
                        domain.setForeign(true);
                    if (constraint.UNIQUE() != null && constraintColumns.size() == 1)
                        domain.setUnique(true);
                }
            }
            addDomain(domain, type);
        }
    }

    private BehaviorObject definition(BehaviorAction action, TargetType target, HanaParser.QualifiedNameContext name, RuleQueryType type) {
        BehaviorObject object = object(target, name);
        addRelation(action, object);
        addDomain(HanaRuleDomains.objectDomain(target, resource(object)), type);
        return object;
    }

    private static TargetType objectType(String keyword) {
        return switch (keyword) {
            case "SCHEMA" -> TargetType.Schema;
            case "TABLE" -> TargetType.Table;
            case "VIEW" -> TargetType.View;
            case "INDEX" -> TargetType.Index;
            case "SEQUENCE" -> TargetType.Sequence;
            case "SYNONYM" -> TargetType.Synonym;
            case "PROCEDURE" -> TargetType.Procedure;
            case "FUNCTION" -> TargetType.Function;
            case "TRIGGER" -> TargetType.Trigger;
            default -> throw new IllegalArgumentException(keyword);
        };
    }

    private void requireVariable(HanaParser.IdentifierContext identifier) {
        String name = name(identifier);
        if (variables.stream().noneMatch(scope -> scope.containsKey(name))) {
            throw unsupported(identifier, "Unresolved SQLScript variable " + name);
        }
    }

    private List<BehaviorObject> readObjectsSince(int start) {
        Map<String, BehaviorObject> objects = new LinkedHashMap<>();
        for (var relation : result.getBehavior().getRelations().subList(start, result.getBehavior().getRelations().size())) {
            if (relation.getAction() == BehaviorAction.READ && relation.getSubject().getObjectType() == TargetType.Table) {
                objects.put(relation.getSubject().getObjectPath(), relation.getSubject());
            }
        }
        return new ArrayList<>(objects.values());
    }

    private BehaviorObject object(TargetType type, HanaParser.QualifiedNameContext context) {
        return object(type, context, names(context));
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context, List<String> names) {
        if (names.size() > 2) {
            throw unsupported(context, "Cross-database object names require remote-source authorization and are not supported");
        }
        // Slash-delimited platform resource paths cannot represent these names without ambiguity.
        if (names.stream().anyMatch(part -> part.contains("/"))) {
            throw unsupported(context, "Object name contains a resource path separator");
        }
        return objects.object(type, context, names);
    }

    private BehaviorRelation addRelation(BehaviorAction action, BehaviorObject object) {
        BehaviorRelation relation = new BehaviorRelation();
        if (explaining && action != BehaviorAction.CALL) {
            action = BehaviorAction.READ;
        }
        relation.setAction(action);
        relation.setSubject(object);
        result.getBehavior().getRelations().add(relation);
        return relation;
    }

    private void addDomain(RuleDomain domain, RuleQueryType type) {
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        domain.setOptions(Map.of());
        domain.setSplitScript(script);
        result.getDomains().add(domain);
    }

    private void configureName(RdbConfigNames domain, BehaviorObject object) {
        var resource = resource(object);
        domain.configName(resource.get(TargetType.Catalog), resource.get(TargetType.Schema), object.getObjectName().getObjectName());
    }

    private Map<TargetType, String> resource(BehaviorObject object) {
        ObjectName name = object.getObjectName();
        Map<TargetType, String> resource = new EnumMap<>(TargetType.class);
        resource.put(TargetType.Catalog, Objects.toString(levels.get(UmiTypes.Catalog), null));
        resource.put(TargetType.Schema, Objects.toString(levels.get(UmiTypes.Schema), null));
        if (name.getCatalog() != null) {
            resource.put(TargetType.Catalog, name.getCatalog());
        }
        if (name.getSchema() != null) {
            resource.put(TargetType.Schema, name.getSchema());
        }
        if (object.getObjectType() != TargetType.Schema) {
            resource.put(object.getObjectType(), name.getObjectName());
        }
        return resource;
    }

    private void configureQuery(RdbQueryDomain domain, ParseTree context) {
        var queries = descendants(context, HanaParser.QueryContext.class);
        domain.setHasWith(domain.isHasWith() || queries.stream().anyMatch(query -> query.withClause() != null));
        boolean union = queries.stream().anyMatch(query -> !query.setOperator().isEmpty());
        if (domain instanceof RdbWhereDomain where) {
            where.setHasUnion(where.isHasUnion() || union);
        } else if (domain instanceof HanaInsertDomain insert) {
            insert.setHasUnion(union);
        }
    }

    private void configureWhere(RdbWhereDomain domain, HanaParser.WhereClauseContext where) {
        domain.setHasWhere(where != null);
        domain.setWhereColumns(columnNamesIn(where));
        domain.setSelectInWhere(contains(where, HanaParser.QueryContext.class));
    }

    private static List<String> columns(HanaParser.ColumnNamesContext context) {
        if (context == null) {
            return List.of();
        }
        return context.identifier().stream().map(HanaBehaviorParserVisitor::name).toList();
    }

    private static List<String> columnNamesIn(ParseTree tree) {
        return descendants(tree, HanaParser.PrimaryContext.class).stream().filter(p -> p.qualifiedName() != null).map(p -> lastName(p.qualifiedName())).distinct().toList();
    }

    private static List<String> names(HanaParser.QualifiedNameContext context) {
        return context.identifier().stream().map(HanaBehaviorParserVisitor::name).toList();
    }

    private static String lastName(HanaParser.QualifiedNameContext context) {
        return name(context.identifier(context.identifier().size() - 1));
    }

    private static String name(HanaParser.IdentifierContext context) {
        String text = context.getText();
        if (context.QUOTED_IDENTIFIER() != null) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toUpperCase(Locale.ROOT);
    }

    private ThirdPartyApiException unsupported(ParserRuleContext context, String reason) {
        return ThirdPartyApiException.as()
            .with(HanaSqlI18nKeys.HANA_SQL_ANALYSIS_UNSUPPORTED, script.getBodyStartCodeLine() + context.getStart().getLine() - 1, context.getStart()
                .getCharPositionInLine() + (context.getStart().getLine() == 1 ? script.getBodyStartCodeColumn() : 0), reason);
    }

    private static <T extends ParseTree> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        if (tree == null) {
            return result;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            result.addAll(descendants(tree.getChild(i), type));
        }
        return result;
    }

    private static boolean contains(ParseTree tree, Class<? extends ParseTree> type) {
        return !descendants(tree, type).isEmpty();
    }

    private static <T extends ParseTree> T ancestor(ParseTree tree, Class<T> type) {
        for (ParseTree parent = tree.getParent(); parent != null; parent = parent.getParent()) {
            if (type.isInstance(parent)) {
                return type.cast(parent);
            }
        }
        return null;
    }
}

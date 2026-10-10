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
import org.antlr.v4.runtime.tree.ParseTree;

final class HanaBehaviorParserVisitor extends HanaParserBaseVisitor<Void> {
    private final HanaAnalysisResult                                           result    = new HanaAnalysisResult();
    private final SplitScript                                                  script;
    private final Map<UmiTypes, Object>                                        levels;
    private final RdbBehaviorObjectFactory                                     objects;
    private final Deque<Map<String, List<BehaviorObject>>>                     cteScopes = new ArrayDeque<>();
    private final Deque<Map<String, List<BehaviorObject>>>                     variables = new ArrayDeque<>();
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
            addRelation(BehaviorAction.CONFIGURE, objects.unnamedObject(TargetType.Session, ctx, UmiTypes.Catalog));
            addDomain(new RdbResourceDomain(), RuleQueryType.SESSION_SETTING_WRITE);
        }
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
        addRelation(BehaviorAction.DELETE, table);
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

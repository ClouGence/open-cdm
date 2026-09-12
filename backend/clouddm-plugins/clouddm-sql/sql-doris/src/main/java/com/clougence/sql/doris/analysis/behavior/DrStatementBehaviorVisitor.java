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
package com.clougence.sql.doris.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParser.*;

final class DrStatementBehaviorVisitor extends DorisParserBaseVisitor<Void> {
    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();
    private ParseTree                      root;

    DrStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        List<BehaviorRelation> reads = new ArrayList<>();
        if (behavior.getStatementType() == SplitQueryType.SELECT) {
            reads.addAll(behavior.getRelations());
            behavior.getRelations().clear();
        } else {
            for (BehaviorRelation relation : behavior.getRelations()) {
                if (relation.getAction() == BehaviorAction.READ) {
                    reads.add(relation);
                }
            }
            behavior.getRelations().removeAll(reads);
        }
        for (ParserRuleContext ctx : descendants(root, ParserRuleContext.class)) {
            if (ctx instanceof FunctionCallExpressionContext function) {
                add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, function.functionIdentifier()));
            } else if (ctx instanceof InsertIntoTVFContext function) {
                add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, function.tvfName));
            } else if (ctx instanceof TableValuedFunctionContext function) {
                add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, function.tvfName));
            } else if (ctx instanceof LateralViewContext lateral) {
                add(SplitQueryType.SELECT, BehaviorAction.CALL, object(TargetType.Function, lateral.functionName));
            } else if (ctx instanceof UnnestFunctionContext function) {
                var token = function.UNNEST().getSymbol();
                add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, token, List.of(token.getText())));
            } else if (ctx instanceof SubstringContext || ctx instanceof PositionContext || ctx instanceof CastContext || ctx instanceof CharFunctionContext
                       || ctx instanceof ConvertCharSetContext || ctx instanceof ConvertTypeContext || ctx instanceof GroupConcatContext || ctx instanceof TrimContext
                       || ctx instanceof ExtractContext || ctx instanceof CurrentDateContext || ctx instanceof CurrentTimeContext || ctx instanceof CurrentTimestampContext
                       || ctx instanceof LocalTimeContext || ctx instanceof LocalTimestampContext || ctx instanceof CurrentUserContext || ctx instanceof SessionUserContext) {
                var token = ctx.getStart();
                add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, token, List.of(token.getText())));
            }
        }
        for (TableValuedFunctionContext function : descendants(root, TableValuedFunctionContext.class)) {
            String name = unquote(text(function.tvfName));
            if (!Set.of("s3", "hdfs", "local", "http", "azure", "gcs").contains(name.toLowerCase(Locale.ROOT))) {
                continue;
            }
            for (PropertyItemContext property : descendants(function.properties, PropertyItemContext.class)) {
                String key = unquote(text(property.key));
                if (key.equalsIgnoreCase("uri") || key.equalsIgnoreCase("file_path")) {
                    BehaviorObject source = file(property.value.getStart());
                    add(SplitQueryType.SELECT, BehaviorAction.READ, source);
                    add(SplitQueryType.SELECT, BehaviorAction.UNSAFE, source);
                }
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

    @Override
    public Void visitStatementDefault(StatementDefaultContext ctx) {
        if (ctx.explain() != null) {
            behavior.setStatementType(SplitQueryType.PERFORMANCE);
            return visit(ctx.query());
        }
        if (ctx.outFileClause() != null) {
            BehaviorObject file = file(ctx.outFileClause().filePath.getStart());
            add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, file, tableSources(ctx.query()));
            add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, file);
            return null;
        }
        behavior.setStatementType(SplitQueryType.SELECT);
        return visitChildren(ctx);
    }

    @Override
    public Void visitAlterTableExecute(AlterTableExecuteContext ctx) {
        BehaviorAction action = switch (unquote(text(ctx.actionName)).toLowerCase(Locale.ROOT)) {
            case "expire_snapshots" -> BehaviorAction.PURGE;
            case "rewrite_data_files", "rewrite_manifests" -> BehaviorAction.OPTIMIZE;
            case "cherrypick_snapshot", "fast_forward", "publish_changes", "rollback_to_snapshot", "rollback_to_timestamp", "set_current_snapshot" -> BehaviorAction.ALTER;
            default -> BehaviorAction.UNKNOWN;
        };
        add(SplitQueryType.ADMIN_TABLE, action, object(TargetType.Table, ctx.tableName));
        return visitChildren(ctx);
    }

    @Override
    public Void visitSwitchCatalog(SwitchCatalogContext ctx) {
        add(SplitQueryType.SWITCH_CATALOG, BehaviorAction.SWITCH, object(TargetType.Catalog, ctx.catalog));
        return null;
    }

    @Override
    public Void visitUseDatabase(UseDatabaseContext ctx) {
        List<String> names = new ArrayList<>();
        if (ctx.catalog != null) {
            names.add(unquote(text(ctx.catalog)));
        }
        names.add(unquote(text(ctx.database)));
        Token start = ctx.database.getStart();
        if (ctx.catalog != null) {
            start = ctx.catalog.getStart();
        }
        add(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, objects.object(TargetType.Schema, start, ctx.database.getStop(), names));
        return null;
    }

    @Override
    public Void visitSetVariableWithType(SetVariableWithTypeContext ctx) {
        SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
        if (ctx.statementScope().GLOBAL() != null) {
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        add(type, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        return visitChildren(ctx);
    }

    @Override
    public Void visitSetSystemVariable(SetSystemVariableContext ctx) {
        SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
        if (ctx.statementScope() != null && ctx.statementScope().GLOBAL() != null) {
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        add(type, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        return visitChildren(ctx);
    }

    @Override
    public Void visitSetUserVariable(SetUserVariableContext ctx) {
        add(SplitQueryType.SESSION_VARIABLE_RW, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        return visitChildren(ctx);
    }

    @Override
    public Void visitSystemVariable(SystemVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, unquote(text(ctx.identifier()))));
        return null;
    }

    @Override
    public Void visitUserVariable(UserVariableContext ctx) {
        add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx, unquote(text(ctx.identifierOrText()))));
        return null;
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        if (!isCte(ctx)) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.multipartIdentifier()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitPredicate(PredicateContext ctx) {
        if (ctx.analyzer != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.analyzer, unquote(text(ctx.analyzer))));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateDictionary(CreateDictionaryContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, object(TargetType.SchemaObject, ctx.name), List.of(object(TargetType.Table, ctx.source)));
        return null;
    }

    @Override
    public Void visitCreateFile(CreateFileContext ctx) {
        List<String> names = new ArrayList<>();
        if (ctx.database != null) {
            names.add(unquote(text(ctx.database)));
        }
        names.add(unquote(ctx.name.getText()));
        List<BehaviorObject> sources = new ArrayList<>();
        for (PropertyItemContext property : descendants(ctx.properties, PropertyItemContext.class)) {
            if (unquote(text(property.key)).equalsIgnoreCase("url")) {
                sources.add(file(property.value.getStart()));
            }
        }
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.object(TargetType.File, ctx.name, names), sources);
        for (BehaviorObject source : sources) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.UNSAFE, source);
        }
        return null;
    }

    @Override
    public Void visitDropFile(DropFileContext ctx) {
        List<String> names = new ArrayList<>();
        if (ctx.database != null) {
            names.add(unquote(text(ctx.database)));
        }
        names.add(unquote(ctx.name.getText()));
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.object(TargetType.File, ctx.name, names));
        return null;
    }

    @Override
    public Void visitShowLoadWarings(ShowLoadWaringsContext ctx) {
        if (ctx.url != null) {
            BehaviorObject source = file(ctx.url);
            add(SplitQueryType.METADATA, BehaviorAction.READ, source);
            add(SplitQueryType.METADATA, BehaviorAction.UNSAFE, source);
        } else {
            BehaviorObject schema;
            if (ctx.database != null) {
                schema = object(TargetType.Schema, ctx.database);
            } else {
                schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
            }
            add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        }
        return null;
    }

    @Override
    public Void visitDropDictionary(DropDictionaryContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, object(TargetType.SchemaObject, ctx.name));
        return null;
    }

    @Override
    public Void visitRefreshDictionary(RefreshDictionaryContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.SchemaObject, ctx.name));
        return null;
    }

    @Override
    public Void visitDescribeDictionary(DescribeDictionaryContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.SchemaObject, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitShowDictionaries(ShowDictionariesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.unnamedObject(TargetType.SchemaObject, ctx.DICTIONARIES().getSymbol(), UmiTypes.Schema));
        return null;
    }

    @Override
    public Void visitInsertTable(InsertTableContext ctx) {
        if (ctx.explain() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
            for (BehaviorObject source : tableSources(ctx.query())) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, source);
            }
            return null;
        }
        SplitQueryType type = ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        BehaviorRelation relation = add(type, type == SplitQueryType.INSERT ? BehaviorAction.INSERT : BehaviorAction.MERGE, object(TargetType.Table, ctx.tableName), tableSources(ctx
            .query()));
        InlineTableContext values = first(ctx.query(), InlineTableContext.class);
        if (relation != null && values != null) {
            relation.setInsertRows((long) values.rowConstructor().size());
        }
        return null;
    }

    @Override
    public Void visitUpdate(UpdateContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.fromClause());
        addTableSources(sources, ctx.whereClause());
        if (ctx.explain() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
            for (BehaviorObject source : sources) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, source);
            }
            return null;
        }
        add(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.tableName), sources);
        return null;
    }

    @Override
    public Void visitDelete(DeleteContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.relations());
        addTableSources(sources, ctx.whereClause());
        if (ctx.explain() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
            for (BehaviorObject source : sources) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, source);
            }
            return null;
        }
        add(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.tableName), sources);
        return null;
    }

    @Override
    public Void visitCreateTable(CreateTableContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.name);
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table, tableSources(ctx.query()));
        for (IndexDefContext index : descendants(ctx.indexDefs(), IndexDefContext.class)) {
            List<String> names = new ArrayList<>();
            for (ErrorCapturingIdentifierContext part : ctx.name.errorCapturingIdentifier()) {
                names.add(unquote(text(part)));
            }
            names.set(names.size() - 1, unquote(text(index.indexName)));
            add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, objects.object(TargetType.Index, index.indexName, names), List.of(table));
        }
        return null;
    }

    @Override
    public Void visitCreateTableLike(CreateTableLikeContext ctx) {
        add(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.name), List.of(object(TargetType.Table, ctx.existedTable)));
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        if (ctx.REPLACE() != null) {
            add(SplitQueryType.ALTER_VIEW, BehaviorAction.REPLACE, object(TargetType.View, ctx.name), tableSources(ctx.query()));
            return null;
        }
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.name), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitAlterView(AlterViewContext ctx) {
        add(SplitQueryType.ALTER_VIEW, BehaviorAction.ALTER, object(TargetType.View, ctx.name), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitAlterCatalogRename(AlterCatalogRenameContext ctx) {
        add(SplitQueryType.RENAME_CATALOG, BehaviorAction.RENAME, object(TargetType.Catalog, ctx.name), List.of(object(TargetType.Catalog, ctx.newName)));
        return null;
    }

    @Override
    public Void visitCallProcedure(CallProcedureContext ctx) {
        add(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.name));
        return null;
    }

    @Override
    public Void visitDropProcedure(DropProcedureContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Procedure, ctx.name));
        return null;
    }

    @Override
    public Void visitShowCreateProcedure(ShowCreateProcedureContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Procedure, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterDatabaseRename(AlterDatabaseRenameContext ctx) {
        add(SplitQueryType.RENAME_SCHEMA, BehaviorAction.RENAME, object(TargetType.Schema, ctx.name), List.of(object(TargetType.Schema, ctx.newName)));
        return null;
    }

    @Override
    public Void visitDropSqlBlockRule(DropSqlBlockRuleContext ctx) {
        for (ErrorCapturingIdentifierContext name : ctx.identifierSeq().errorCapturingIdentifier()) {
            add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, name, unquote(text(name))));
        }
        return null;
    }

    @Override
    public Void visitCreateMTMV(CreateMTMVContext ctx) {
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.mvName), tableSources(ctx.query()));
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        add(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitCreateCatalog(CreateCatalogContext ctx) {
        add(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.catalogName));
        return null;
    }

    @Override
    public Void visitDropCatalog(DropCatalogContext ctx) {
        add(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        if (!descendants(ctx, RenameClauseContext.class).isEmpty()) {
            return visitChildren(ctx);
        }
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitAlterTableProperties(AlterTablePropertiesContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitDescribeTable(DescribeTableContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitCreateUserDefineFunction(CreateUserDefineFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.functionIdentifier()));
        return null;
    }

    @Override
    public Void visitCreateAliasFunction(CreateAliasFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.functionIdentifier()));
        return null;
    }

    @Override
    public Void visitDropFunction(DropFunctionContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Function, ctx.functionIdentifier()));
        return null;
    }

    @Override
    public Void visitAdminSetTableStatus(AdminSetTableStatusContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitAdminSetFrontendConfig(AdminSetFrontendConfigContext ctx) {
        if (ctx.propertyItemList() == null) {
            add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.CONFIG().getSymbol()));
        } else {
            for (PropertyItemContext property : ctx.propertyItemList().propertyItem()) {
                add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, property.key, unquote(text(property.key))));
            }
        }
        return null;
    }

    @Override
    public Void visitShowCreateTable(ShowCreateTableContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitShowCreateView(ShowCreateViewContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.View, ctx.name));
        return null;
    }

    @Override
    public Void visitShowCreateCatalog(ShowCreateCatalogContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitShowCatalog(ShowCatalogContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitShowCreateDatabase(ShowCreateDatabaseContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitShowGrants(ShowGrantsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.UserOrRole, ctx));
        return null;
    }

    @Override
    public Void visitShowGrantsForUser(ShowGrantsForUserContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, user(ctx.userIdentify()));
        return null;
    }

    @Override
    public Void visitShowCreateUser(ShowCreateUserContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, user(ctx.userIdentify()));
        return null;
    }

    @Override
    public Void visitShowRoles(ShowRolesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Role, ctx.ROLES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowSqlBlockRule(ShowSqlBlockRuleContext ctx) {
        BehaviorObject target;
        if (ctx.ruleName == null) {
            target = objects.instanceObject(TargetType.Policy, ctx.SQL_BLOCK_RULE().getSymbol());
        } else {
            target = objects.instanceObject(TargetType.Policy, ctx.ruleName, unquote(text(ctx.ruleName)));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, target);
        return null;
    }

    @Override
    public Void visitShowCreateRepository(ShowCreateRepositoryContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        return null;
    }

    @Override
    public Void visitShowTables(ShowTablesContext ctx) {
        BehaviorObject schema;
        if (ctx.database == null) {
            schema = objects.unnamedObject(TargetType.Schema, ctx.TABLES().getSymbol(), UmiTypes.Schema);
        } else {
            schema = object(TargetType.Schema, ctx.database);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowVariables(ShowVariablesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.VARIABLES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowColumns(ShowColumnsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitShowPartitions(ShowPartitionsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitShowIndex(ShowIndexContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitShowProc(ShowProcContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.PROC().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowTransaction(ShowTransactionContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.TRANSACTION().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowProcessList(ShowProcessListContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.PROCESSLIST().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowCreateFunction(ShowCreateFunctionContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Function, ctx.functionIdentifier()));
        return null;
    }

    @Override
    public Void visitAdminSetPartitionVersion(AdminSetPartitionVersionContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitExport(ExportContext ctx) {
        BehaviorObject file = file(ctx.filePath);
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, file, List.of(object(TargetType.Table, ctx.tableName)));
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, file);
        return null;
    }

    @Override
    public Void visitMysqlLoad(MysqlLoadContext ctx) {
        MysqlDataDescContext data = ctx.mysqlDataDesc();
        BehaviorObject source = file(data.filePath);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, object(TargetType.Table, data.tableName), List.of(source));
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, source);
        return null;
    }

    @Override
    public Void visitLoad(LoadContext ctx) {
        for (DataDescContext data : ctx.dataDesc()) {
            List<BehaviorObject> sources = new ArrayList<>();
            List<Token> paths = new ArrayList<>(data.filePaths);
            paths.addAll(data.filePath);
            for (Token path : paths) {
                sources.add(file(path));
            }
            if (data.sourceTableName != null) {
                sources.add(object(TargetType.Table, data.sourceTableName));
            }
            List<String> target = new ArrayList<>();
            List<ErrorCapturingIdentifierContext> label = ctx.lableName.errorCapturingIdentifier();
            for (int i = 0; i < label.size() - 1; i++) {
                target.add(unquote(text(label.get(i))));
            }
            target.add(unquote(text(data.targetTableName)));
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, objects.object(TargetType.Table, data.targetTableName, target), sources);
            for (BehaviorObject source : sources) {
                if (source.getObjectType() == TargetType.File) {
                    add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, source);
                }
            }
        }
        return null;
    }

    private BehaviorObject file(Token token) {
        String path = unquote(token.getText()).replaceFirst("^/+", "");
        if (path.isEmpty()) {
            return objects.instanceObject(TargetType.File, token);
        }
        return objects.instanceObject(TargetType.File, token, path);
    }

    @Override
    public Void visitPauseRoutineLoad(PauseRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.STOP, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitResumeRoutineLoad(ResumeRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.START, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitStopRoutineLoad(StopRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.TERMINATE, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitPauseAllRoutineLoad(PauseAllRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx));
        return null;
    }

    @Override
    public Void visitResumeAllRoutineLoad(ResumeAllRoutineLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.START, objects.instanceObject(TargetType.Job, ctx));
        return null;
    }

    @Override
    public Void visitCreateRoutineLoad(CreateRoutineLoadContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.label);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.table != null) {
            List<String> names = new ArrayList<>();
            List<ErrorCapturingIdentifierContext> label = ctx.label.errorCapturingIdentifier();
            for (int i = 0; i < label.size() - 1; i++) {
                names.add(unquote(text(label.get(i))));
            }
            names.add(unquote(text(ctx.table)));
            targets.add(objects.object(TargetType.Table, ctx.table, names));
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.CREATE, job, targets);
        for (BehaviorObject table : targets) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, table);
        }
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, job);
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        add(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, ctx.name));
        return null;
    }

    @Override
    public Void visitDropMV(DropMVContext ctx) {
        add(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        add(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitRenameClause(RenameClauseContext ctx) {
        BehaviorObject source = renameSource(ctx);
        if (source != null) {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, List.of(object(TargetType.Table, ctx.newName)));
        } else {
            add(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, object(TargetType.Table, ctx.newName));
        }
        return null;
    }

    private BehaviorObject renameSource(RenameClauseContext ctx) {
        ParseTree parent = ctx.getParent();
        while (parent instanceof ParserRuleContext context) {
            if (context instanceof AlterTableContext alter) {
                return object(TargetType.Table, alter.tableName);
            }
            parent = context.getParent();
        }
        return null;
    }

    private List<BehaviorObject> tableSources(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        addTableSources(result, tree);
        return result;
    }

    private void addTableSources(List<BehaviorObject> result, ParseTree tree) {
        for (TableNameContext table : descendants(tree, TableNameContext.class)) {
            if (isCte(table)) {
                continue;
            }
            BehaviorObject object = object(TargetType.Table, table.multipartIdentifier());
            if (object != null) {
                result.add(object);
            }
        }
        for (TableValuedFunctionContext function : descendants(tree, TableValuedFunctionContext.class)) {
            result.add(object(TargetType.Function, function.tvfName));
            String name = unquote(text(function.tvfName)).toLowerCase(Locale.ROOT);
            if (!Set.of("s3", "hdfs", "local", "http", "azure", "gcs").contains(name)) {
                continue;
            }
            for (PropertyItemContext property : descendants(function.properties, PropertyItemContext.class)) {
                String key = unquote(text(property.key));
                if (key.equalsIgnoreCase("uri") || key.equalsIgnoreCase("file_path")) {
                    result.add(file(property.value.getStart()));
                }
            }
        }
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        add(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, user(ctx.grantUserIdentify().userIdentify()));
        return null;
    }

    @Override
    public Void visitSync(SyncContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.FLUSH, objects.instanceObject(TargetType.Instance, ctx));
        return null;
    }

    @Override
    public Void visitCreateScheduledJob(CreateScheduledJobContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.label);
        List<BehaviorObject> dependencies = new ArrayList<>(tableSources(ctx.supportedDmlStatement()));
        for (InsertTableContext insert : descendants(ctx.supportedDmlStatement(), InsertTableContext.class)) {
            dependencies.add(object(TargetType.Table, insert.tableName));
        }
        for (UpdateContext update : descendants(ctx.supportedDmlStatement(), UpdateContext.class)) {
            dependencies.add(object(TargetType.Table, update.tableName));
        }
        for (DeleteContext delete : descendants(ctx.supportedDmlStatement(), DeleteContext.class)) {
            dependencies.add(object(TargetType.Table, delete.tableName));
        }
        if (ctx.jobFromToClause() != null) {
            dependencies.add(object(TargetType.Schema, ctx.jobFromToClause().targetDb));
        }
        add(SplitQueryType.CREATE_JOB, BehaviorAction.CREATE, job, dependencies);
        if (ctx.supportedDmlStatement() != null) {
            visit(ctx.supportedDmlStatement());
        }
        add(SplitQueryType.CREATE_JOB, BehaviorAction.UNSAFE, job);
        return null;
    }

    @Override
    public Void visitDropJob(DropJobContext ctx) {
        add(SplitQueryType.DROP_JOB, BehaviorAction.DROP, objects.object(TargetType.Job, ctx.jobNameValue, List.of(unquote(ctx.jobNameValue.getText()))));
        return null;
    }

    @Override
    public Void visitPauseJob(PauseJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.object(TargetType.Job, ctx.jobNameValue, List.of(unquote(ctx.jobNameValue.getText()))));
        return null;
    }

    @Override
    public Void visitResumeJob(ResumeJobContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.START, objects.object(TargetType.Job, ctx.jobNameValue, List.of(unquote(ctx.jobNameValue.getText()))));
        return null;
    }

    @Override
    public Void visitDescribeTableValuedFunction(DescribeTableValuedFunctionContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Function, ctx.tvfName));
        return null;
    }

    @Override
    public Void visitAlterSystem(AlterSystemContext ctx) {
        AlterSystemClauseContext clause = ctx.alterSystemClause();
        List<Token> hosts;
        BehaviorAction action;
        if (clause instanceof AddBackendClauseContext backend) {
            hosts = backend.hostPorts;
            action = BehaviorAction.CREATE;
        } else if (clause instanceof DropBackendClauseContext backend) {
            hosts = backend.hostPorts;
            action = BehaviorAction.DROP;
        } else if (clause instanceof DecommissionBackendClauseContext backend) {
            hosts = backend.hostPorts;
            action = BehaviorAction.STOP;
        } else if (clause instanceof ModifyBackendClauseContext backend) {
            hosts = backend.hostPorts;
            action = BehaviorAction.CONFIGURE;
        } else if (clause instanceof AddObserverClauseContext observer) {
            hosts = List.of(observer.hostPort);
            action = BehaviorAction.CREATE;
        } else if (clause instanceof DropObserverClauseContext observer) {
            hosts = List.of(observer.hostPort);
            action = BehaviorAction.DROP;
        } else if (clause instanceof AddFollowerClauseContext follower) {
            hosts = List.of(follower.hostPort);
            action = BehaviorAction.CREATE;
        } else if (clause instanceof DropFollowerClauseContext follower) {
            hosts = List.of(follower.hostPort);
            action = BehaviorAction.DROP;
        } else if (clause instanceof ModifyFrontendOrBackendHostNameClauseContext host) {
            hosts = List.of(host.hostPort);
            action = BehaviorAction.ALTER;
        } else {
            return visitChildren(ctx);
        }
        for (Token host : hosts) {
            add(SplitQueryType.ADMIN, action, objects.instanceObject(TargetType.Machine, host, unquote(host.getText())));
        }
        return null;
    }

    @Override
    public Void visitCancelDecommisionBackend(CancelDecommisionBackendContext ctx) {
        for (Token host : ctx.hostPorts) {
            add(SplitQueryType.ADMIN, BehaviorAction.START, objects.instanceObject(TargetType.Machine, host, unquote(host.getText())));
        }
        return null;
    }

    @Override
    public Void visitSetPassword(SetPasswordContext ctx) {
        BehaviorObject target;
        if (ctx.userIdentify() == null) {
            target = objects.instanceObject(TargetType.User, ctx.PASSWORD(0).getSymbol());
        } else {
            target = user(ctx.userIdentify());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, target);
        return null;
    }

    @Override
    public Void visitSetLdapAdminPassword(SetLdapAdminPasswordContext ctx) {
        var token = ctx.LDAP_ADMIN_PASSWORD().getSymbol();
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, token, token.getText()));
        return null;
    }

    @Override
    public Void visitCreateRepository(CreateRepositoryContext ctx) {
        BehaviorObject repository = objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name)));
        BehaviorObject location = file(ctx.storageBackend().STRING_LITERAL().getSymbol());
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, repository, List.of(location));
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.UNSAFE, repository);
        return null;
    }

    @Override
    public Void visitAlterRepository(AlterRepositoryContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropRepository(DropRepositoryContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAddConstraint(AddConstraintContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(object(TargetType.Table, ctx.table));
        if (ctx.constraint().referenceTable != null) {
            targets.add(object(TargetType.Table, ctx.constraint().referenceTable));
        }
        add(SplitQueryType.ADD_CONSTRAINT, BehaviorAction.CREATE, object(TargetType.Constraint, ctx.constraintName), targets);
        return null;
    }

    @Override
    public Void visitDropConstraint(DropConstraintContext ctx) {
        add(SplitQueryType.DROP_CONSTRAINT, BehaviorAction.DROP, object(TargetType.Constraint, ctx.constraintName), List.of(object(TargetType.Table, ctx.table)));
        return null;
    }

    @Override
    public Void visitShowConstraint(ShowConstraintContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.table));
        return null;
    }

    @Override
    public Void visitTransactionBegin(TransactionBeginContext ctx) {
        behavior.setStatementType(SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitTranscationCommit(TranscationCommitContext ctx) {
        behavior.setStatementType(SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitTransactionRollback(TransactionRollbackContext ctx) {
        behavior.setStatementType(SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitAlterUser(AlterUserContext ctx) {
        add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user(ctx.grantUserIdentify().userIdentify()));
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        add(SplitQueryType.DROP_USER, BehaviorAction.DROP, user(ctx.userIdentify()));
        return null;
    }

    private BehaviorObject user(UserIdentifyContext ctx) {
        String name = unquote(text(ctx.user));
        if (ctx.host != null) {
            name += "@" + unquote(text(ctx.host));
        }
        return objects.instanceObject(TargetType.User, ctx, name);
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        add(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, objects.instanceObject(TargetType.Role, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        add(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, objects.instanceObject(TargetType.Role, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitGrantRole(GrantRoleContext ctx) {
        for (IdentifierOrTextContext role : ctx.roles) {
            add(SplitQueryType.GRANT, BehaviorAction.GRANT, objects.instanceObject(TargetType.Role, role, unquote(text(role))), List.of(user(ctx.userIdentify())));
        }
        return null;
    }

    @Override
    public Void visitRevokeRole(RevokeRoleContext ctx) {
        for (IdentifierOrTextContext role : ctx.roles) {
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, objects.instanceObject(TargetType.Role, role, unquote(text(role))), List.of(user(ctx.userIdentify())));
        }
        return null;
    }

    @Override
    public Void visitGrantTablePrivilege(GrantTablePrivilegeContext ctx) {
        BehaviorObject target;
        if (ctx.userIdentify() != null) {
            target = user(ctx.userIdentify());
        } else {
            target = objects.instanceObject(TargetType.Role, ctx.identifierOrText(), unquote(text(ctx.identifierOrText())));
        }
        add(SplitQueryType.GRANT, BehaviorAction.GRANT, object(TargetType.Table, ctx.multipartIdentifierOrAsterisk()), List.of(target));
        return null;
    }

    @Override
    public Void visitRevokeTablePrivilege(RevokeTablePrivilegeContext ctx) {
        BehaviorObject target;
        if (ctx.userIdentify() != null) {
            target = user(ctx.userIdentify());
        } else {
            target = objects.instanceObject(TargetType.Role, ctx.identifierOrText(), unquote(text(ctx.identifierOrText())));
        }
        add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, object(TargetType.Table, ctx.multipartIdentifierOrAsterisk()), List.of(target));
        return null;
    }

    @Override
    public Void visitCreateResource(CreateResourceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterResource(AlterResourceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropResource(DropResourceContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateStoragePolicy(CreateStoragePolicyContext ctx) {
        add(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterStoragePolicy(AlterStoragePolicyContext ctx) {
        add(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropStoragePolicy(DropStoragePolicyContext ctx) {
        add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateWorkloadGroup(CreateWorkloadGroupContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.computeGroup != null) {
            targets.add(objects.instanceObject(TargetType.ResourceGroup, ctx.computeGroup, unquote(text(ctx.computeGroup))));
        }
        add(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE, objects.instanceObject(TargetType.ResourceGroup, ctx.name, unquote(text(ctx.name))), targets);
        return null;
    }

    @Override
    public Void visitAlterWorkloadGroup(AlterWorkloadGroupContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.computeGroup != null) {
            targets.add(objects.instanceObject(TargetType.ResourceGroup, ctx.computeGroup, unquote(text(ctx.computeGroup))));
        }
        add(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER, objects.instanceObject(TargetType.ResourceGroup, ctx.name, unquote(text(ctx.name))), targets);
        return null;
    }

    @Override
    public Void visitDropWorkloadGroup(DropWorkloadGroupContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.computeGroup != null) {
            targets.add(objects.instanceObject(TargetType.ResourceGroup, ctx.computeGroup, unquote(text(ctx.computeGroup))));
        }
        add(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP, objects.instanceObject(TargetType.ResourceGroup, ctx.name, unquote(text(ctx.name))), targets);
        return null;
    }

    @Override
    public Void visitCreateSqlBlockRule(CreateSqlBlockRuleContext ctx) {
        add(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterSqlBlockRule(AlterSqlBlockRuleContext ctx) {
        add(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateWorkloadPolicy(CreateWorkloadPolicyContext ctx) {
        add(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterWorkloadPolicy(AlterWorkloadPolicyContext ctx) {
        add(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropWorkloadPolicy(DropWorkloadPolicyContext ctx) {
        add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.Policy, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterCatalogProperties(AlterCatalogPropertiesContext ctx) {
        add(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterCatalogComment(AlterCatalogCommentContext ctx) {
        add(SplitQueryType.COMMENT_CATALOG, BehaviorAction.ALTER, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterDatabaseProperties(AlterDatabasePropertiesContext ctx) {
        add(SplitQueryType.ALTER_SCHEMA, BehaviorAction.ALTER, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterDatabaseSetQuota(AlterDatabaseSetQuotaContext ctx) {
        add(SplitQueryType.ALTER_SCHEMA, BehaviorAction.CONFIGURE, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterRole(AlterRoleContext ctx) {
        add(SplitQueryType.ALTER_ROLE, BehaviorAction.ALTER, objects.instanceObject(TargetType.Role, ctx.role, unquote(text(ctx.role))));
        return null;
    }

    @Override
    public Void visitCreateStorageVault(CreateStorageVaultContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterStorageVault(AlterStorageVaultContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateIndexAnalyzer(CreateIndexAnalyzerContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateIndexTokenizer(CreateIndexTokenizerContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateIndexTokenFilter(CreateIndexTokenFilterContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropIndexAnalyzer(DropIndexAnalyzerContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropIndexTokenizer(DropIndexTokenizerContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropIndexTokenFilter(DropIndexTokenFilterContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateStage(CreateStageContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropStage(DropStageContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        if (context instanceof MultipartIdentifierContext multipart) {
            for (ErrorCapturingIdentifierContext identifier : multipart.errorCapturingIdentifier()) {
                names.add(unquote(text(identifier)));
            }
        } else if (context instanceof MultipartIdentifierOrAsteriskContext multipart) {
            for (IdentifierOrAsteriskContext identifier : multipart.parts) {
                names.add(unquote(text(identifier)));
            }
        } else if (context instanceof FunctionIdentifierContext function) {
            if (function.dbName != null) {
                names.add(unquote(text(function.dbName)));
            }
            names.add(unquote(text(function.functionNameIdentifier())));
        } else {
            names.add(unquote(text(context)));
        }
        return objects.object(type, context, names);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private boolean isCte(TableNameContext table) {
        if (table.multipartIdentifier().errorCapturingIdentifier().size() != 1) {
            return false;
        }
        String name = unquote(text(table.multipartIdentifier()));
        Set<ParseTree> ancestors = Collections.newSetFromMap(new IdentityHashMap<>());
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            ancestors.add(parent);
        }
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            CteContext cte = null;
            if (parent instanceof QueryContext query) {
                cte = query.cte();
            } else if (parent instanceof MergeIntoContext merge) {
                cte = merge.cte();
            } else if (parent instanceof InsertTableContext insert) {
                cte = insert.cte();
            } else if (parent instanceof UpdateContext update) {
                cte = update.cte();
            } else if (parent instanceof DeleteContext delete) {
                cte = delete.cte();
            }
            if (cte != null) {
                for (AliasQueryContext alias : cte.aliasQuery()) {
                    if (ancestors.contains(alias) && cte.RECURSIVE() == null) {
                        break;
                    }
                    if (name.equalsIgnoreCase(unquote(text(alias.identifier())))) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    private BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        return add(type, action, subject, List.of());
    }

    private BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
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
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN || type != SplitQueryType.SELECT) {
            behavior.setStatementType(type);
        }
        return relation;
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        List<T> values = descendants(tree, type);
        return values.isEmpty() ? null : values.get(0);
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

    @Override
    public Void visitCreateIndexCharFilter(CreateIndexCharFilterContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropIndexCharFilter(DropIndexCharFilterContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitCreateIndexNormalizer(CreateIndexNormalizerContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitDropIndexNormalizer(DropIndexNormalizerContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, objects.instanceObject(TargetType.ConfigKey, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAlterComputeGroup(AlterComputeGroupContext ctx) {
        add(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER, objects.instanceObject(TargetType.ResourceGroup, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitSetUserProperties(SetUserPropertiesContext ctx) {
        BehaviorObject subject;
        if (ctx.user != null) {
            subject = objects.instanceObject(TargetType.User, ctx.user, unquote(text(ctx.user)));
        } else {
            subject = objects.instanceObject(TargetType.User, ctx.PROPERTY().getSymbol());
        }
        add(SplitQueryType.ALTER_USER, BehaviorAction.CONFIGURE, subject);
        return null;
    }

    @Override
    public Void visitGrantResourcePrivilege(GrantResourcePrivilegeContext ctx) {
        TargetType type = TargetType.ConfigKey;
        if (ctx.GROUP() != null || ctx.CLUSTER() != null) {
            type = TargetType.ResourceGroup;
        }
        BehaviorObject target;
        if (ctx.userIdentify() != null) {
            target = user(ctx.userIdentify());
        } else {
            target = objects.instanceObject(TargetType.Role, ctx.identifierOrText(), unquote(text(ctx.identifierOrText())));
        }
        add(SplitQueryType.GRANT, BehaviorAction.GRANT, objects.instanceObject(type, ctx.identifierOrTextOrAsterisk(), unquote(text(ctx.identifierOrTextOrAsterisk()))), List
            .of(target));
        return null;
    }

    @Override
    public Void visitRevokeResourcePrivilege(RevokeResourcePrivilegeContext ctx) {
        TargetType type = TargetType.ConfigKey;
        if (ctx.GROUP() != null || ctx.CLUSTER() != null) {
            type = TargetType.ResourceGroup;
        }
        BehaviorObject target;
        if (ctx.userIdentify() != null) {
            target = user(ctx.userIdentify());
        } else {
            target = objects.instanceObject(TargetType.Role, ctx.identifierOrText(), unquote(text(ctx.identifierOrText())));
        }
        add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, objects.instanceObject(type, ctx.identifierOrTextOrAsterisk(), unquote(text(ctx.identifierOrTextOrAsterisk()))), List
            .of(target));
        return null;
    }

    @Override
    public Void visitCreateRowPolicy(CreateRowPolicyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(object(TargetType.Table, ctx.table));
        if (ctx.user != null) {
            targets.add(user(ctx.user));
        } else {
            targets.add(objects.instanceObject(TargetType.Role, ctx.roleName, unquote(text(ctx.roleName))));
        }
        add(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, objects.instanceObject(TargetType.RowAccessPolicy, ctx.name, unquote(text(ctx.name))), targets);
        return null;
    }

    @Override
    public Void visitDropRowPolicy(DropRowPolicyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(object(TargetType.Table, ctx.tableName));
        if (ctx.userIdentify() != null) {
            targets.add(user(ctx.userIdentify()));
        } else if (ctx.roleName != null) {
            targets.add(objects.instanceObject(TargetType.Role, ctx.roleName, unquote(text(ctx.roleName))));
        }
        add(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, objects.instanceObject(TargetType.RowAccessPolicy, ctx.policyName, unquote(text(ctx.policyName))), targets);
        return null;
    }

    @Override
    public Void visitAlterMTMV(AlterMTMVContext ctx) {
        BehaviorAction action = BehaviorAction.ALTER;
        List<BehaviorObject> targets = List.of();
        if (ctx.newName != null) {
            action = BehaviorAction.RENAME;
            if (ctx.REPLACE() != null) {
                action = BehaviorAction.REPLACE;
            }
            List<String> names = new ArrayList<>();
            for (ErrorCapturingIdentifierContext name : ctx.mvName.errorCapturingIdentifier()) {
                names.add(unquote(text(name)));
            }
            names.set(names.size() - 1, unquote(text(ctx.newName)));
            targets = List.of(objects.object(TargetType.Materialized, ctx.newName, names));
        }
        add(SplitQueryType.ALTER_VIEW, action, object(TargetType.Materialized, ctx.mvName), targets);
        return null;
    }

    @Override
    public Void visitRefreshMTMV(RefreshMTMVContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitRefreshCatalog(RefreshCatalogContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.Catalog, ctx.name));
        return null;
    }

    @Override
    public Void visitRefreshDatabase(RefreshDatabaseContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitRefreshTable(RefreshTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.REFRESH, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitRecoverDatabase(RecoverDatabaseContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.alias != null) {
            targets.add(object(TargetType.Schema, ctx.alias));
        }
        add(SplitQueryType.ADMIN, BehaviorAction.RECOVER, object(TargetType.Schema, ctx.name), targets);
        return null;
    }

    @Override
    public Void visitRecoverTable(RecoverTableContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.alias != null) {
            List<String> names = new ArrayList<>();
            for (ErrorCapturingIdentifierContext name : ctx.name.errorCapturingIdentifier()) {
                names.add(unquote(text(name)));
            }
            names.set(names.size() - 1, unquote(text(ctx.alias)));
            targets.add(objects.object(TargetType.Table, ctx.alias, names));
        }
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RECOVER, object(TargetType.Table, ctx.name), targets);
        return null;
    }

    @Override
    public Void visitRecoverPartition(RecoverPartitionContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        targets.add(object(TargetType.Table, ctx.tableName));
        if (ctx.alias != null) {
            targets.add(object(TargetType.Partition, ctx.alias));
        }
        add(SplitQueryType.ADMIN_PARTITION, BehaviorAction.RECOVER, object(TargetType.Partition, ctx.name), targets);
        return null;
    }

    @Override
    public Void visitAnalyzeDatabase(AnalyzeDatabaseContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ANALYZE, object(TargetType.Schema, ctx.name));
        return null;
    }

    @Override
    public Void visitDropStats(DropStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitDropCachedStats(DropCachedStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitAlterTableStats(AlterTableStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CONFIGURE, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitAlterColumnStats(AlterColumnStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CONFIGURE, object(TargetType.Table, ctx.name));
        return null;
    }

    @Override
    public Void visitShowConfig(ShowConfigContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.CONFIG().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowDatabases(ShowDatabasesContext ctx) {
        BehaviorObject catalog;
        if (ctx.catalog != null) {
            catalog = object(TargetType.Catalog, ctx.catalog);
        } else {
            catalog = objects.unnamedObject(TargetType.Catalog, ctx.getStart(), UmiTypes.Catalog);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, catalog);
        return null;
    }

    @Override
    public Void visitAdminShowReplicaStatus(AdminShowReplicaStatusContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitAdminShowReplicaDistribution(AdminShowReplicaDistributionContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitPauseMTMV(PauseMTMVContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitResumeMTMV(ResumeMTMVContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.START, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitShowCreateMTMV(ShowCreateMTMVContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Materialized, ctx.mvName));
        return null;
    }

    @Override
    public Void visitShowCreateMaterializedView(ShowCreateMaterializedViewContext ctx) {
        List<String> names = new ArrayList<>();
        for (ErrorCapturingIdentifierContext name : ctx.tableName.errorCapturingIdentifier()) {
            names.add(unquote(text(name)));
        }
        names.set(names.size() - 1, unquote(text(ctx.mvName)));
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.object(TargetType.Materialized, ctx.mvName, names), List.of(object(TargetType.Table, ctx.tableName)));
        return null;
    }

    @Override
    public Void visitShowTabletsFromTable(ShowTabletsFromTableContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitCancelAlterTable(CancelAlterTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.STOP, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitShowReplicaDistribution(ShowReplicaDistributionContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitShowDataSkew(ShowDataSkewContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitShowAlterTable(ShowAlterTableContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowLoad(ShowLoadContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowBuildIndex(ShowBuildIndexContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowExport(ShowExportContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowBackends(ShowBackendsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Machine, ctx.BACKENDS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowFrontends(ShowFrontendsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Machine, ctx.FRONTENDS().getSymbol()));
        return null;
    }

    @Override
    public Void visitWarmUpCluster(WarmUpClusterContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        if (ctx.source != null) {
            sources.add(objects.instanceObject(TargetType.ResourceGroup, ctx.source, unquote(text(ctx.source))));
        }
        for (WarmUpItemContext item : ctx.warmUpItem()) {
            sources.add(object(TargetType.Table, item.tableName));
            if (item.partitionName != null) {
                sources.add(object(TargetType.Partition, item.partitionName));
            }
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.LOAD, objects.instanceObject(TargetType.ResourceGroup, ctx.destination, unquote(text(ctx.destination))), sources);
        return null;
    }

    @Override
    public Void visitLockTables(LockTablesContext ctx) {
        behavior.setStatementType(SplitQueryType.SESSION_LOCK);
        for (LockTableContext table : ctx.lockTable()) {
            add(SplitQueryType.SESSION_LOCK, BehaviorAction.LOCK, object(TargetType.Table, table.name));
        }
        return null;
    }

    @Override
    public Void visitUnlockTables(UnlockTablesContext ctx) {
        add(SplitQueryType.SESSION_LOCK, BehaviorAction.UNLOCK, objects.instanceObject(TargetType.Instance, ctx));
        return null;
    }

    @Override
    public Void visitShowWarmUpJob(ShowWarmUpJobContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.JOB().getSymbol()));
        return null;
    }

    @Override
    public Void visitWarmUpSelect(WarmUpSelectContext ctx) {
        if (ctx.explain() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, ctx.warmUpSingleTableRef().multipartIdentifier()));
        } else {
            add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.LOAD, object(TargetType.Table, ctx.warmUpSingleTableRef().multipartIdentifier()));
        }
        visit(ctx.namedExpressionSeq());
        if (ctx.whereClause() != null) {
            visit(ctx.whereClause());
        }
        return null;
    }

    @Override
    public Void visitShowTableStatus(ShowTableStatusContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowViews(ShowViewsContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowOpenTables(ShowOpenTablesContext ctx) {
        BehaviorObject schema;
        if (ctx.database != null) {
            schema = object(TargetType.Schema, ctx.database);
        } else {
            schema = objects.unnamedObject(TargetType.Schema, ctx.getStart(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, schema);
        return null;
    }

    @Override
    public Void visitShowQueryStats(ShowQueryStatsContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName != null) {
            subject = object(TargetType.Table, ctx.tableName);
        } else if (ctx.database != null) {
            subject = object(TargetType.Schema, ctx.database);
        } else {
            subject = objects.instanceObject(TargetType.Instance, ctx.STATS().getSymbol());
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitCancelLoad(CancelLoadContext ctx) {
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Job, ctx));
        return null;
    }

    @Override
    public Void visitCancelExport(CancelExportContext ctx) {
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Job, ctx));
        return null;
    }

    @Override
    public Void visitAdminSetReplicaStatus(AdminSetReplicaStatusContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Replication, ctx));
        return null;
    }

    @Override
    public Void visitAdminSetReplicaVersion(AdminSetReplicaVersionContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.Replication, ctx));
        return null;
    }

    @Override
    public Void visitAlterRoutineLoad(AlterRoutineLoadContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.name);
        add(SplitQueryType.DATA_IMPORT, BehaviorAction.ALTER, job);
        if (ctx.type != null) {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.UNSAFE, job);
        }
        return null;
    }

    @Override
    public Void visitShowSnapshot(ShowSnapshotContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.repo, unquote(text(ctx.repo))));
        return null;
    }

    @Override
    public Void visitAlterJob(AlterJobContext ctx) {
        BehaviorObject job = object(TargetType.Job, ctx.name);
        List<BehaviorObject> targets = new ArrayList<>();
        for (InsertTableContext insert : descendants(ctx.supportedDmlStatement(), InsertTableContext.class)) {
            targets.add(object(TargetType.Table, insert.tableName));
        }
        for (UpdateContext update : descendants(ctx.supportedDmlStatement(), UpdateContext.class)) {
            targets.add(object(TargetType.Table, update.tableName));
        }
        for (DeleteContext delete : descendants(ctx.supportedDmlStatement(), DeleteContext.class)) {
            targets.add(object(TargetType.Table, delete.tableName));
        }
        targets.addAll(tableSources(ctx.supportedDmlStatement()));
        if (ctx.jobFromToClause() != null) {
            targets.add(object(TargetType.Schema, ctx.jobFromToClause().targetDb));
        }
        add(SplitQueryType.ALTER_JOB, BehaviorAction.ALTER, job, targets);
        if (ctx.supportedDmlStatement() != null) {
            visit(ctx.supportedDmlStatement());
        }
        if (ctx.supportedDmlStatement() != null || ctx.jobFromToClause() != null) {
            add(SplitQueryType.ALTER_JOB, BehaviorAction.UNSAFE, job);
        }
        return null;
    }

    @Override
    public Void visitAlterTableAddRollup(AlterTableAddRollupContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitAlterTableDropRollup(AlterTableDropRollupContext ctx) {
        add(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitBuildIndex(BuildIndexContext ctx) {
        if (ctx.name == null) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.LOAD, object(TargetType.Table, ctx.tableName));
        } else {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.LOAD, object(TargetType.Index, ctx.name), List.of(object(TargetType.Table, ctx.tableName)));
        }
        return null;
    }

    @Override
    public Void visitInstallPlugin(InstallPluginContext ctx) {
        BehaviorObject library = objects.instanceObject(TargetType.Library, ctx.source);
        add(SplitQueryType.CREATE_LIBRARY, BehaviorAction.CREATE, library, List.of(file(ctx.source.getStart())));
        add(SplitQueryType.CREATE_LIBRARY, BehaviorAction.UNSAFE, library);
        return null;
    }

    @Override
    public Void visitUninstallPlugin(UninstallPluginContext ctx) {
        add(SplitQueryType.DROP_LIBRARY, BehaviorAction.DROP, objects.instanceObject(TargetType.Library, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitAdminCopyTablet(AdminCopyTabletContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.COPY, objects.instanceObject(TargetType.Partition, ctx));
        return null;
    }

    @Override
    public Void visitAdminCleanTrash(AdminCleanTrashContext ctx) {
        if (ctx.backends.isEmpty()) {
            add(SplitQueryType.ADMIN, BehaviorAction.PURGE, objects.instanceObject(TargetType.Machine, ctx));
        }
        for (Token backend : ctx.backends) {
            add(SplitQueryType.ADMIN, BehaviorAction.PURGE, objects.instanceObject(TargetType.Machine, backend, unquote(backend.getText())));
        }
        return null;
    }

    @Override
    public Void visitShowData(ShowDataContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName != null) {
            subject = object(TargetType.Table, ctx.tableName);
        } else {
            subject = objects.instanceObject(TargetType.Instance, ctx);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowColumnHistogramStats(ShowColumnHistogramStatsContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName != null) {
            subject = object(TargetType.Table, ctx.tableName);
        } else {
            subject = objects.instanceObject(TargetType.Instance, ctx);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowColumnStats(ShowColumnStatsContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName != null) {
            subject = object(TargetType.Table, ctx.tableName);
        } else {
            subject = objects.instanceObject(TargetType.Instance, ctx);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowIndexStats(ShowIndexStatsContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName != null) {
            subject = object(TargetType.Table, ctx.tableName);
        } else {
            subject = objects.instanceObject(TargetType.Instance, ctx);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowTableStats(ShowTableStatsContext ctx) {
        BehaviorObject subject;
        if (ctx.tableName != null) {
            subject = object(TargetType.Table, ctx.tableName);
        } else {
            subject = objects.instanceObject(TargetType.Instance, ctx);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitAdminCancelRepairTable(AdminCancelRepairTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.STOP, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitAdminRepairTable(AdminRepairTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.REPAIR, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitAdminCompactTable(AdminCompactTableContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, object(TargetType.Table, ctx.baseTableRef().multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitShowStorageVault(ShowStorageVaultContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.STORAGE().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowRepositories(ShowRepositoriesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.REPOSITORIES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowResources(ShowResourcesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.RESOURCES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowStages(ShowStagesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.STAGES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowWorkloadGroups(ShowWorkloadGroupsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ResourceGroup, ctx.GROUPS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowCatalogs(ShowCatalogsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Catalog, ctx.CATALOGS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowPlugins(ShowPluginsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Library, ctx.PLUGINS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowIndexAnalyzer(ShowIndexAnalyzerContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.ANALYZER().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowIndexTokenizer(ShowIndexTokenizerContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.TOKENIZER().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowIndexTokenFilter(ShowIndexTokenFilterContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.TOKEN_FILTER().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowIndexCharFilter(ShowIndexCharFilterContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.CHAR_FILTER().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowIndexNormalizer(ShowIndexNormalizerContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.NORMALIZER().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowCreateRoutineLoad(ShowCreateRoutineLoadContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitShowCreateLoad(ShowCreateLoadContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Job, ctx.label));
        return null;
    }

    @Override
    public Void visitShowUserProperties(ShowUserPropertiesContext ctx) {
        BehaviorObject user;
        if (ctx.user != null) {
            user = objects.instanceObject(TargetType.User, ctx.user, unquote(text(ctx.user)));
        } else {
            user = objects.instanceObject(TargetType.User, ctx.PROPERTY().getSymbol());
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, user);
        return null;
    }

    @Override
    public Void visitShowRoutineLoad(ShowRoutineLoadContext ctx) {
        BehaviorObject job;
        if (ctx.label != null) {
            job = object(TargetType.Job, ctx.label);
        } else {
            job = objects.unnamedObject(TargetType.Job, ctx.LOAD().getSymbol(), UmiTypes.Schema);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, job);
        return null;
    }

    @Override
    public Void visitKillConnection(KillConnectionContext ctx) {
        Token id = ctx.INTEGER_VALUE().getSymbol();
        add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Session, id, id.getText()));
        return null;
    }

    @Override
    public Void visitKillQuery(KillQueryContext ctx) {
        Token id = ctx.getStop();
        add(SplitQueryType.ADMIN, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Query, id, unquote(id.getText())));
        return null;
    }

    @Override
    public Void visitBackup(BackupContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        if (ctx.ON() != null) {
            for (BaseTableRefContext table : ctx.baseTableRef()) {
                BehaviorObject source = snapshotTable(ctx.label, table.multipartIdentifier());
                sources.add(source);
                for (IdentifierContext partition : descendants(table.specifiedPartition(), IdentifierContext.class)) {
                    sources.add(objects.childObject(TargetType.Partition, partition, source, unquote(text(partition))));
                }
            }
        } else {
            sources.add(snapshotSchema(ctx.label));
        }
        sources.add(objects.instanceObject(TargetType.ConfigKey, ctx.repo, unquote(text(ctx.repo))));
        add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, object(TargetType.Backup, ctx.label), sources);
        return null;
    }

    @Override
    public Void visitRestore(RestoreContext ctx) {
        List<BehaviorObject> sources = List.of(object(TargetType.Backup, ctx.label), objects.instanceObject(TargetType.ConfigKey, ctx.repo, unquote(text(ctx.repo))));
        if (ctx.ON() != null) {
            for (BaseTableRefContext table : ctx.baseTableRef()) {
                BehaviorObject target = snapshotTable(ctx.label, table.multipartIdentifier());
                if (table.tableAlias() != null && table.tableAlias().strictIdentifier() != null) {
                    String name = unquote(text(table.tableAlias().strictIdentifier()));
                    List<String> names = new ArrayList<>();
                    for (ErrorCapturingIdentifierContext part : table.multipartIdentifier().errorCapturingIdentifier()) {
                        names.add(unquote(text(part)));
                    }
                    names.set(names.size() - 1, name);
                    if (names.size() == 1 && ctx.label.errorCapturingIdentifier().size() > 1) {
                        names.add(0, unquote(text(ctx.label.errorCapturingIdentifier(0))));
                    }
                    target = objects.object(TargetType.Table, table.tableAlias().strictIdentifier(), names);
                }
                add(SplitQueryType.DATA_IMPORT, BehaviorAction.RESTORE, target, sources);
                for (IdentifierContext partition : descendants(table.specifiedPartition(), IdentifierContext.class)) {
                    add(SplitQueryType.DATA_IMPORT, BehaviorAction.RESTORE, objects.childObject(TargetType.Partition, partition, target, unquote(text(partition))), sources);
                }
            }
        } else {
            add(SplitQueryType.DATA_IMPORT, BehaviorAction.RESTORE, snapshotSchema(ctx.label), sources);
        }
        return null;
    }

    private BehaviorObject snapshotSchema(MultipartIdentifierContext label) {
        if (label.errorCapturingIdentifier().size() > 1) {
            return object(TargetType.Schema, label.errorCapturingIdentifier(0));
        }
        return objects.unnamedObject(TargetType.Schema, label, UmiTypes.Schema);
    }

    private BehaviorObject snapshotTable(MultipartIdentifierContext label, MultipartIdentifierContext table) {
        List<String> names = new ArrayList<>();
        for (ErrorCapturingIdentifierContext part : table.errorCapturingIdentifier()) {
            names.add(unquote(text(part)));
        }
        if (names.size() == 1 && label.errorCapturingIdentifier().size() > 1) {
            names.add(0, unquote(text(label.errorCapturingIdentifier(0))));
        }
        return objects.object(TargetType.Table, table, names);
    }

    @Override
    public Void visitCancelBackup(CancelBackupContext ctx) {
        BehaviorObject scope = objects.unnamedObject(TargetType.Backup, ctx.getStart(), UmiTypes.Schema);
        if (ctx.database != null) {
            scope = object(TargetType.Schema, ctx.database);
            scope.setObjectType(TargetType.Backup);
        }
        add(SplitQueryType.ADMIN, BehaviorAction.STOP, scope);
        return null;
    }

    @Override
    public Void visitCancelRestore(CancelRestoreContext ctx) {
        BehaviorObject scope = objects.unnamedObject(TargetType.Backup, ctx.getStart(), UmiTypes.Schema);
        if (ctx.database != null) {
            scope = object(TargetType.Schema, ctx.database);
            scope.setObjectType(TargetType.Backup);
        }
        add(SplitQueryType.ADMIN, BehaviorAction.STOP, scope);
        return null;
    }

    @Override
    public Void visitRefreshLdap(RefreshLdapContext ctx) {
        BehaviorObject config = objects.instanceObject(TargetType.ConfigKey, ctx.LDAP().getSymbol(), "ldap");
        List<BehaviorObject> users = new ArrayList<>();
        if (ctx.user != null) {
            users.add(objects.instanceObject(TargetType.User, ctx.user, unquote(text(ctx.user))));
        }
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, config, users);
        return null;
    }

    @Override
    public Void visitCreateEncryptkey(CreateEncryptkeyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, object(TargetType.ConfigKey, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitDropEncryptkey(DropEncryptkeyContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, object(TargetType.ConfigKey, ctx.name));
        return null;
    }

    @Override
    public Void visitShowTabletId(ShowTabletIdContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Tablet, ctx.tabletId, ctx.tabletId.getText()));
        return null;
    }

    @Override
    public Void visitShowTabletsBelong(ShowTabletsBelongContext ctx) {
        for (Token id : ctx.tabletIds) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Tablet, id, id.getText()));
        }
        return null;
    }

    @Override
    public Void visitShowTabletStorageFormat(ShowTabletStorageFormatContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Tablet, ctx.TABLET().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowPrivileges(ShowPrivilegesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.PRIVILEGES().getSymbol()));
        return null;
    }

    @Override
    public Void visitCleanAllProfile(CleanAllProfileContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, objects.instanceObject(TargetType.Profile, ctx.PROFILE().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowAnalyze(ShowAnalyzeContext ctx) {
        BehaviorObject subject = objects.instanceObject(TargetType.Job, ctx.ANALYZE().getSymbol());
        if (ctx.jobId != null) {
            subject = objects.instanceObject(TargetType.Job, ctx.jobId, ctx.jobId.getText());
        }
        if (ctx.tableName != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject, List.of(object(TargetType.Table, ctx.tableName)));
        } else {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, subject);
        }
        return null;
    }

    @Override
    public Void visitDropAllBrokerClause(DropAllBrokerClauseContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.DROP, objects.instanceObject(TargetType.Machine, ctx.name, unquote(text(ctx.name))));
        return null;
    }

    @Override
    public Void visitMergeInto(MergeIntoContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.srcRelation);
        addTableSources(sources, ctx.cte());
        addTableSources(sources, ctx.expression());
        if (ctx.explain() != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, object(TargetType.Table, ctx.targetTable));
            for (BehaviorObject source : sources) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, source);
            }
        } else {
            add(SplitQueryType.MERGE, BehaviorAction.MERGE, object(TargetType.Table, ctx.targetTable), sources);
        }
        return null;
    }

    @Override
    public Void visitInsertIntoTVF(InsertIntoTVFContext ctx) {
        List<BehaviorObject> sources = tableSources(ctx.query());
        for (PropertyItemContext property : ctx.tvfProperties.propertyItem()) {
            String key = unquote(text(property.key));
            if (key.equalsIgnoreCase("uri") || key.equalsIgnoreCase("file_path")) {
                BehaviorObject destination = file(property.value.getStart());
                if (ctx.explain() != null) {
                    add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, destination);
                } else {
                    add(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, destination, sources);
                    add(SplitQueryType.DATA_EXPORT, BehaviorAction.UNSAFE, destination);
                }
            }
        }
        return null;
    }

    @Override
    public Void visitShowCharset(ShowCharsetContext ctx) {
        Token token = ctx.getStart();
        if (ctx.CHARSET() != null) {
            token = ctx.CHARSET().getSymbol();
        } else {
            token = ctx.CHAR().getSymbol();
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, token));
        return null;
    }

    @Override
    public Void visitShowCollation(ShowCollationContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.COLLATION().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowAllProperties(ShowAllPropertiesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.PROPERTIES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowBroker(ShowBrokerContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Machine, ctx.BROKER().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowLastInsert(ShowLastInsertContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Insert, ctx.INSERT().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowGlobalFunctions(ShowGlobalFunctionsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Function, ctx.FUNCTIONS().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowDataTypes(ShowDataTypesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Type, ctx.TYPES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowClusters(ShowClustersContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ResourceGroup, ctx.getStart()));
        return null;
    }

    @Override
    public Void visitShowPythonVersions(ShowPythonVersionsContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Library, ctx.PYTHON().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowPythonPackages(ShowPythonPackagesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Library, ctx.PYTHON().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowStorageEngines(ShowStorageEnginesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx.ENGINES().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowBackup(ShowBackupContext ctx) {
        return readSchemaCollection(TargetType.Backup, ctx.BACKUP().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowRestore(ShowRestoreContext ctx) {
        return readSchemaCollection(TargetType.Backup, ctx.RESTORE().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowDelete(ShowDeleteContext ctx) {
        return readSchemaCollection(TargetType.Table, ctx.DELETE().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowDynamicPartition(ShowDynamicPartitionContext ctx) {
        return readSchemaCollection(TargetType.Table, ctx.TABLES().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowEncryptKeys(ShowEncryptKeysContext ctx) {
        return readSchemaCollection(TargetType.ConfigKey, ctx.ENCRYPTKEYS().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowSmallFiles(ShowSmallFilesContext ctx) {
        return readSchemaCollection(TargetType.File, ctx.FILE().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowConvertLsc(ShowConvertLscContext ctx) {
        return readSchemaCollection(TargetType.Table, ctx.CONVERT_LSC().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowTypeCast(ShowTypeCastContext ctx) {
        return readSchemaCollection(TargetType.Type, ctx.TYPECAST().getSymbol(), ctx.database);
    }

    private Void readSchemaCollection(TargetType type, Token token, ParserRuleContext database) {
        BehaviorObject subject = objects.unnamedObject(type, token, UmiTypes.Schema);
        if (database != null) {
            subject = object(TargetType.Schema, database);
            subject.setObjectType(type);
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowDiagnoseTablet(ShowDiagnoseTabletContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Tablet, ctx.tabletId, ctx.tabletId.getText()));
        return null;
    }

    @Override
    public Void visitDropAnalyzeJob(DropAnalyzeJobContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, objects.instanceObject(TargetType.Job, ctx.INTEGER_VALUE().getSymbol(), ctx.INTEGER_VALUE().getText()));
        return null;
    }

    @Override
    public Void visitKillAnalyzeJob(KillAnalyzeJobContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.TERMINATE, objects.instanceObject(TargetType.Job, ctx.jobId, ctx.jobId.getText()));
        return null;
    }

    @Override
    public Void visitAddBrokerClause(AddBrokerClauseContext ctx) {
        for (Token host : ctx.hostPorts) {
            add(SplitQueryType.ADMIN, BehaviorAction.CREATE, objects.instanceObject(TargetType.Machine, host, unquote(host.getText())));
        }
        return null;
    }

    @Override
    public Void visitDropBrokerClause(DropBrokerClauseContext ctx) {
        for (Token host : ctx.hostPorts) {
            add(SplitQueryType.ADMIN, BehaviorAction.DROP, objects.instanceObject(TargetType.Machine, host, unquote(host.getText())));
        }
        return null;
    }

    @Override
    public Void visitDescribeTableAll(DescribeTableAllContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.multipartIdentifier()));
        return null;
    }

    @Override
    public Void visitShowRoutineLoadTask(ShowRoutineLoadTaskContext ctx) {
        if (ctx.label != null) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Job, ctx.label));
            return null;
        }
        return readSchemaCollection(TargetType.Job, ctx.TASK().getSymbol(), ctx.database);
    }

    @Override
    public Void visitShowQueuedAnalyzeJobs(ShowQueuedAnalyzeJobsContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.tableName != null) {
            targets.add(object(TargetType.Table, ctx.tableName));
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, objects.instanceObject(TargetType.Job, ctx.JOBS().getSymbol()), targets);
        return null;
    }

    @Override
    public Void visitShowQueryProfile(ShowQueryProfileContext ctx) {
        BehaviorObject profile = objects.instanceObject(TargetType.Profile, ctx.PROFILE().getSymbol());
        if (ctx.queryIdPath != null) {
            profile = objects.instanceObject(TargetType.Profile, ctx.queryIdPath, unquote(ctx.queryIdPath.getText()));
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, profile);
        return null;
    }

    @Override
    public Void visitCleanLabel(CleanLabelContext ctx) {
        BehaviorObject label = object(TargetType.Schema, ctx.database);
        label.setObjectType(TargetType.Job);
        if (ctx.label != null) {
            label = objects.object(TargetType.Job, ctx.label, List.of(unquote(text(ctx.database)), unquote(text(ctx.label))));
        }
        add(SplitQueryType.ADMIN, BehaviorAction.PURGE, label);
        return null;
    }

    @Override
    public Void visitCancelWarmUpJob(CancelWarmUpJobContext ctx) {
        BehaviorObject job = objects.instanceObject(TargetType.Job, ctx.JOB().getSymbol());
        for (ComparisonContext comparison : descendants(ctx.wildWhere(), ComparisonContext.class)) {
            if (text(comparison.left).equalsIgnoreCase("id") && text(comparison.comparisonOperator()).equals("=")) {
                job = objects.instanceObject(TargetType.Job, comparison.right, unquote(text(comparison.right)));
                break;
            }
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.STOP, job);
        return null;
    }

    @Override
    public Void visitSetDefaultStorageVault(SetDefaultStorageVaultContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier()))));
        return null;
    }

    @Override
    public Void visitShowFunctions(ShowFunctionsContext ctx) {
        return readSchemaCollection(TargetType.Function, ctx.FUNCTIONS().getSymbol(), ctx.database);
    }

    @Override
    public Void visitHelp(HelpContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Instance, ctx.HELP().getSymbol()));
        return null;
    }

    @Override
    public Void visitCleanQueryStats(CleanQueryStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, objects.instanceObject(TargetType.Query, ctx.QUERY().getSymbol()));
        return null;
    }

    @Override
    public Void visitCleanAllQueryStats(CleanAllQueryStatsContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, objects.instanceObject(TargetType.Query, ctx.QUERY().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowRowPolicy(ShowRowPolicyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.userIdentify() != null) {
            targets.add(user(ctx.userIdentify()));
        } else if (ctx.role != null) {
            targets.add(objects.instanceObject(TargetType.Role, ctx.role, unquote(text(ctx.role))));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Policy, ctx.POLICY().getSymbol()), targets);
        return null;
    }

    @Override
    public Void visitShowStoragePolicy(ShowStoragePolicyContext ctx) {
        BehaviorObject policy = objects.instanceObject(TargetType.ConfigKey, ctx.POLICY().getSymbol());
        if (ctx.policy != null) {
            policy = objects.instanceObject(TargetType.ConfigKey, ctx.policy, unquote(text(ctx.policy)));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, policy);
        return null;
    }

    @Override
    public Void visitAdminCheckTablets(AdminCheckTabletsContext ctx) {
        for (Token tablet : ctx.tabletList().tabletIdList) {
            add(SplitQueryType.ADMIN, BehaviorAction.VALIDATE, objects.instanceObject(TargetType.Tablet, tablet, tablet.getText()));
        }
        return null;
    }

    @Override
    public Void visitAdminRebalanceDisk(AdminRebalanceDiskContext ctx) {
        if (ctx.backends.isEmpty()) {
            add(SplitQueryType.ADMIN, BehaviorAction.OPTIMIZE, objects.instanceObject(TargetType.Machine, ctx.DISK().getSymbol()));
        }
        for (Token backend : ctx.backends) {
            add(SplitQueryType.ADMIN, BehaviorAction.OPTIMIZE, objects.instanceObject(TargetType.Machine, backend, unquote(backend.getText())));
        }
        return null;
    }

    @Override
    public Void visitAdminCancelRebalanceDisk(AdminCancelRebalanceDiskContext ctx) {
        if (ctx.backends.isEmpty()) {
            add(SplitQueryType.ADMIN, BehaviorAction.STOP, objects.instanceObject(TargetType.Machine, ctx.DISK().getSymbol()));
        }
        for (Token backend : ctx.backends) {
            add(SplitQueryType.ADMIN, BehaviorAction.STOP, objects.instanceObject(TargetType.Machine, backend, unquote(backend.getText())));
        }
        return null;
    }

    @Override
    public Void visitShowCatalogRecycleBin(ShowCatalogRecycleBinContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Catalog, ctx.CATALOG().getSymbol()));
        return null;
    }

    @Override
    public Void visitShowTrash(ShowTrashContext ctx) {
        BehaviorObject subject = objects.instanceObject(TargetType.Machine, ctx.TRASH().getSymbol());
        if (ctx.backend != null) {
            subject = objects.instanceObject(TargetType.Machine, ctx.backend, unquote(ctx.backend.getText()));
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitShowView(ShowViewContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, object(TargetType.Table, ctx.tableName));
        return null;
    }

    @Override
    public Void visitUseCloudCluster(UseCloudClusterContext ctx) {
        if (ctx.database != null) {
            List<String> names = new ArrayList<>();
            if (ctx.catalog != null) {
                names.add(unquote(text(ctx.catalog)));
            }
            names.add(unquote(text(ctx.database)));
            add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.SWITCH, objects.object(TargetType.Schema, ctx.database, names));
        }
        add(SplitQueryType.ADMIN_RESOURCE_GROUP, BehaviorAction.SWITCH, objects.instanceObject(TargetType.ResourceGroup, ctx.cluster, unquote(text(ctx.cluster))));
        return null;
    }

    @Override
    public Void visitShowTableId(ShowTableIdContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Table, ctx.tableId, ctx.tableId.getText()));
        return null;
    }

    @Override
    public Void visitShowDatabaseId(ShowDatabaseIdContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Schema, ctx.databaseId, ctx.databaseId.getText()));
        return null;
    }

    @Override
    public Void visitShowPartitionId(ShowPartitionIdContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, objects.instanceObject(TargetType.Partition, ctx.partitionId, ctx.partitionId.getText()));
        return null;
    }

    @Override
    public Void visitCancelBuildIndex(CancelBuildIndexContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.tableName);
        if (ctx.jobIds.isEmpty()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.STOP, table);
        }
        for (Token jobId : ctx.jobIds) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, jobId, jobId.getText()), List.of(table));
        }
        return null;
    }

    @Override
    public Void visitCancelJobTask(CancelJobTaskContext ctx) {
        BehaviorObject job = objects.object(TargetType.Job, ctx.jobNameValue, List.of(unquote(ctx.jobNameValue.getText())));
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx.taskIdValue, ctx.taskIdValue.getText()), List.of(job));
        return null;
    }

    @Override
    public Void visitCancelMTMVTask(CancelMTMVTaskContext ctx) {
        add(SplitQueryType.ADMIN_JOB, BehaviorAction.STOP, objects.instanceObject(TargetType.Job, ctx.taskId, ctx.taskId.getText()), List
            .of(object(TargetType.Materialized, ctx.mvName)));
        return null;
    }

    @Override
    public Void visitAlterColocateGroup(AlterColocateGroupContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE, object(TargetType.ResourceGroup, ctx.name));
        return null;
    }

    @Override
    public Void visitSupportedUnsetStatement(SupportedUnsetStatementContext ctx) {
        SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
        if (ctx.DEFAULT() != null || ctx.statementScope() != null && ctx.statementScope().GLOBAL() != null) {
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        BehaviorObject key;
        if (ctx.identifier() != null) {
            key = objects.instanceObject(TargetType.ConfigKey, ctx.identifier(), unquote(text(ctx.identifier())));
        } else {
            key = objects.instanceObject(TargetType.ConfigKey, ctx.getStop());
        }
        add(type, BehaviorAction.RESET, key);
        return null;
    }

    @Override
    public Void visitReplayCommand(ReplayCommandContext ctx) {
        ReplayTypeContext replay = ctx.replayType();
        List<BehaviorObject> targets = tableSources(replay.query());
        BehaviorObject source = null;
        if (replay.filePath != null) {
            source = file(replay.filePath);
            targets.add(source);
        }
        add(SplitQueryType.PERFORMANCE, BehaviorAction.ANALYZE, objects.unnamedObject(TargetType.Query, ctx, UmiTypes.Schema), targets);
        if (source != null) {
            add(SplitQueryType.PERFORMANCE, BehaviorAction.UNSAFE, source);
        }
        return null;
    }
}

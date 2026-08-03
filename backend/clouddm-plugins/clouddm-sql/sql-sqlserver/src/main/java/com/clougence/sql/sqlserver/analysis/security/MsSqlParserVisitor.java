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
package com.clougence.sql.sqlserver.analysis.security;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.sql.sqlserver.analysis.security.builder.MsBuildFactory;
import com.clougence.sql.sqlserver.analysis.security.domain.*;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

public class MsSqlParserVisitor extends SqlServerParserBaseVisitor<Void> {

    private final MsBuildFactory builder;
    private final Parser         parser;

    public MsSqlParserVisitor(MsBuildFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    @Override
    public Void visitSql_clauses(SqlServerParser.Sql_clausesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDdl_clause(SqlServerParser.Ddl_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDml_clause(SqlServerParser.Dml_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCfl_statement(SqlServerParser.Cfl_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAnother_statement(SqlServerParser.Another_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBatch_level_statement(SqlServerParser.Batch_level_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_database(SqlServerParser.Create_databaseContext ctx) {
        RdbCatalogDomain domain = builder.newCatalogDomain(RuleQueryType.CREATE_CATALOG);
        domain.setCatalog(clean(ctx.database));
        add(domain);
        return null;
    }

    @Override
    public Void visitDrop_database(SqlServerParser.Drop_databaseContext ctx) {
        for (SqlServerParser.Id_Context id : ctx.id_()) {
            RdbCatalogDomain domain = builder.newCatalogDomain(RuleQueryType.DROP_CATALOG);
            domain.setCatalog(clean(id));
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitCreate_schema(SqlServerParser.Create_schemaContext ctx) {
        List<String> names = names(ctx);
        if (!names.isEmpty()) {
            MsSchemaDomain domain = builder.newSchemaDomain(RuleQueryType.CREATE_SCHEMA);
            domain.setSchema(names.get(0));
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitDrop_schema(SqlServerParser.Drop_schemaContext ctx) {
        List<String> names = names(ctx);
        if (!names.isEmpty()) {
            MsSchemaDomain domain = builder.newSchemaDomain(RuleQueryType.DROP_SCHEMA);
            domain.setSchema(names.get(names.size() - 1));
            domain.setIfExists(ctx.IF() != null);
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitCreate_table(SqlServerParser.Create_tableContext ctx) {
        MsTableDomain domain = tableDomain(ctx.table_name(), RuleQueryType.CREATE_TABLE, SecQueryKind.CREATE);
        domain.setColumns(new ArrayList<>());
        Map<String, MsColumnDomain> columns = new LinkedHashMap<>();
        List<RuleDomain> children = tableElementDomains(ctx
            .column_def_table_constraints(), domain, columns, RuleQueryType.CREATE_TABLE_ADD_COLUMN, RuleQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        add(domain);
        children.forEach(this::add);
        return null;
    }

    @Override
    public Void visitAlter_table(SqlServerParser.Alter_tableContext ctx) {
        MsTableDomain domain = tableDomain(ctx.table_name(0), RuleQueryType.ALTER_TABLE, SecQueryKind.ALTER);
        domain.setColumns(new ArrayList<>());
        Map<String, MsColumnDomain> columns = new LinkedHashMap<>();
        List<RuleDomain> children = new ArrayList<>();
        if (ctx.ADD() != null && ctx.column_def_table_constraints() != null) {
            children
                .addAll(tableElementDomains(ctx.column_def_table_constraints(), domain, columns, RuleQueryType.ALTER_TABLE_ADD_COLUMN, RuleQueryType.ALTER_TABLE_ADD_CONSTRAINT));
        } else if (ctx.ALTER().size() > 1 && ctx.column_definition() != null) {
            MsColumnDomain column = columnDomain(ctx.column_definition(), domain, RuleQueryType.ALTER_TABLE_ALTER_COLUMN, SecQueryKind.ALTER);
            children.add(column);
            domain.getColumns().add(column.getColumn());
        } else if (ctx.DROP() != null && ctx.COLUMN() != null) {
            for (SqlServerParser.Id_Context id : ctx.id_()) {
                MsColumnDomain column = simpleColumnDomain(domain, clean(id), RuleQueryType.ALTER_TABLE_DROP_COLUMN, SecQueryKind.DROP);
                children.add(column);
                domain.getColumns().remove(column.getColumn());
            }
        } else if (ctx.DROP() != null && ctx.CONSTRAINT() != null && ctx.constraint != null) {
            RdbConstraintDomain constraint = constraintDomain(domain, RuleQueryType.ALTER_TABLE_DROP_CONSTRAINT, SqlConstraintType.ByName, clean(ctx.constraint), Collections
                .emptyList());
            constraint.setAuditKind(SecQueryKind.DROP);
            children.add(constraint);
        } else if (ctx.ADD() != null && ctx.CONSTRAINT() != null) {
            children.add(alterTableConstraintDomain(ctx, domain));
        }
        add(domain);
        children.forEach(this::add);
        return null;
    }

    @Override
    public Void visitDrop_table(SqlServerParser.Drop_tableContext ctx) {
        for (SqlServerParser.Table_nameContext tableName : ctx.table_name()) {
            MsTableDomain domain = tableDomain(tableName, RuleQueryType.DROP_TABLE, SecQueryKind.DROP);
            domain.setIfExists(ctx.IF() != null);
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitCreate_view(SqlServerParser.Create_viewContext ctx) {
        NameParts name = parts(ctx.simple_name());
        boolean alter = startsWithAlter(ctx);
        RdbViewDomain domain = builder.newViewDomain(alter ? RuleQueryType.ALTER_VIEW : RuleQueryType.CREATE_VIEW);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setView(name.name);
        add(domain);
        return null;
    }

    @Override
    public Void visitDrop_view(SqlServerParser.Drop_viewContext ctx) {
        for (SqlServerParser.Simple_nameContext simpleName : ctx.simple_name()) {
            NameParts name = parts(simpleName);
            RdbViewDomain domain = builder.newViewDomain(RuleQueryType.DROP_VIEW);
            domain.setCatalog(name.catalog);
            domain.setSchema(name.schema);
            domain.setView(name.name);
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitCreate_index(SqlServerParser.Create_indexContext ctx) {
        NameParts table = parts(ctx.table_name());
        RdbIndexDomain domain = builder.newIndexDomain(RuleQueryType.ADD_INDEX);
        domain.setName(clean(ctx.id_(0)));
        domain.setTableCatalog(table.catalog);
        domain.setTableSchema(table.schema);
        domain.setTableName(table.name);
        domain.setColumns(names(ctx.column_name_list_with_order()));
        add(domain);
        return null;
    }

    @Override
    public Void visitDrop_index(SqlServerParser.Drop_indexContext ctx) {
        for (SqlServerParser.Drop_relational_or_xml_or_spatial_indexContext item : ctx.drop_relational_or_xml_or_spatial_index()) {
            List<String> itemNames = names(item);
            RdbIndexDomain domain = builder.newIndexDomain(RuleQueryType.DROP_INDEX);
            if (!itemNames.isEmpty()) {
                domain.setName(itemNames.get(0));
            }
            SqlServerParser.Full_table_nameContext tableName = first(item, SqlServerParser.Full_table_nameContext.class);
            if (tableName != null) {
                NameParts table = parts(tableName);
                domain.setTableCatalog(table.catalog);
                domain.setTableSchema(table.schema);
                domain.setTableName(table.name);
            }
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitSelect_statement_standalone(SqlServerParser.Select_statement_standaloneContext ctx) {
        if (parent(ctx, SqlServerParser.Create_viewContext.class) != null || parent(ctx, SqlServerParser.Create_or_alter_dml_triggerContext.class) != null) {
            return null;
        }
        MsSelectDomain domain = builder.newSelectDomain();
        String selectText = getText(ctx).toLowerCase();
        domain.setHasLimit(selectText.contains("top") || selectText.contains(" offset "));
        domain.setHasUnion(selectText.contains(" union "));
        domain.setSelectInSelect(descendants(ctx, SqlServerParser.SubqueryContext.class).stream()
            .anyMatch(sub -> parent(sub, SqlServerParser.Select_list_elemContext.class) != null));
        domain.setSelectInFrom(descendants(ctx, SqlServerParser.Derived_tableContext.class).stream().anyMatch(d -> first(d, SqlServerParser.SubqueryContext.class) != null));
        domain.setFuncInSelect(!descendants(ctx, SqlServerParser.Function_callContext.class).isEmpty());
        if (ctx.with_expression() != null) {
            domain.setHasWith(true);
        }

        for (SqlServerParser.Select_list_elemContext selectItem : descendants(ctx, SqlServerParser.Select_list_elemContext.class)) {
            String text = getText(selectItem).trim();
            if (text.isEmpty()) {
                continue;
            }
            if (descendants(selectItem, SqlServerParser.As_column_aliasContext.class).stream().anyMatch(alias -> alias.AS() != null)) {
                domain.setHasAs(true);
            }
            QueryItem item = builder.newQueryItem();
            if (selectItem.asterisk() != null) {
                item.setSelectAll(true);
                item.setColumn("*");
                domain.setHasSelectAll(true);
            } else {
                item.setColumn(text);
                domain.addSelect(text, RdbQuerySelectType.Column);
            }
            domain.getColumns().add(item);
        }
        for (SqlServerParser.Table_source_itemContext source : descendants(ctx, SqlServerParser.Table_source_itemContext.class)) {
            SqlServerParser.Full_table_nameContext tableName = source.full_table_name();
            if (tableName == null) {
                continue;
            }
            domain.setEmptyFrom(false);
            MsTableDomain tableDomain = tableDomain(tableName, RuleQueryType.SELECT, SecQueryKind.QUERY);
            if (domain.getTable() == null) {
                domain.setCatalog(tableDomain.getCatalog());
                domain.setSchema(tableDomain.getSchema());
                domain.setTable(tableDomain.getTable());
            }
            domain.addChild(tableDomain);
        }
        for (SqlServerParser.Join_partContext join : descendants(ctx, SqlServerParser.Join_partContext.class)) {
            addJoinType(domain, join);
        }
        for (SqlServerParser.Query_specificationContext query : descendants(ctx, SqlServerParser.Query_specificationContext.class)) {
            if (query.where != null && parent(query, SqlServerParser.SubqueryContext.class) == null) {
                markWhere(domain, query.where);
            }
        }
        add(domain);
        return null;
    }

    @Override
    public Void visitInsert_statement(SqlServerParser.Insert_statementContext ctx) {
        SqlServerParser.Full_table_nameContext fullTableName = first(ctx.ddl_object(), SqlServerParser.Full_table_nameContext.class);
        NameParts table = parts(fullTableName);
        MsInsertDomain domain = builder.newInsertDomain();
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setColumns(names(ctx.insert_column_name_list()));
        domain.setOnlyValues(domain.getColumns().isEmpty());
        domain.setFromSelect(first(ctx.insert_statement_value(), SqlServerParser.Select_statementContext.class) != null);
        add(domain);
        return null;
    }

    @Override
    public Void visitUpdate_statement(SqlServerParser.Update_statementContext ctx) {
        SqlServerParser.Full_table_nameContext fullTableName = first(ctx.ddl_object(), SqlServerParser.Full_table_nameContext.class);
        NameParts table = parts(fullTableName);
        MsUpdateDomain domain = builder.newUpdateDomain();
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setSetColumns(new ArrayList<>());
        domain.setWhereColumns(new ArrayList<>());
        for (SqlServerParser.Update_elemContext updateElem : ctx.update_elem()) {
            List<String> elemNames = names(updateElem);
            if (!elemNames.isEmpty()) {
                domain.getSetColumns().add(elemNames.get(0));
            }
            if (first(updateElem, SqlServerParser.SubqueryContext.class) != null) {
                domain.setSelectInSet(true);
            }
        }
        markWhere(domain, ctx.search_condition());
        add(domain);
        return null;
    }

    @Override
    public Void visitDelete_statement(SqlServerParser.Delete_statementContext ctx) {
        SqlServerParser.Full_table_nameContext fullTableName = first(ctx.delete_statement_from(), SqlServerParser.Full_table_nameContext.class);
        if (fullTableName == null) {
            fullTableName = first(ctx.table_sources(), SqlServerParser.Full_table_nameContext.class);
        }
        NameParts table = parts(fullTableName);
        MsDeleteDomain domain = builder.newDeleteDomain();
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setWhereColumns(new ArrayList<>());
        markWhere(domain, ctx.search_condition());
        add(domain);
        return null;
    }

    @Override
    public Void visitExecute_statement(SqlServerParser.Execute_statementContext ctx) {
        if (tryAddRenameDomain(ctx)) {
            return null;
        }
        RdbCallDomain domain = builder.newCallDomain();
        SqlServerParser.Func_proc_name_server_database_schemaContext procName = first(ctx.execute_body(), SqlServerParser.Func_proc_name_server_database_schemaContext.class);
        List<String> names = names(procName);
        if (!names.isEmpty()) {
            domain.setCallName(names.get(names.size() - 1));
            if (names.size() > 1) {
                domain.setSchema(names.get(names.size() - 2));
            }
            if (names.size() > 2) {
                domain.setCatalog(names.get(names.size() - 3));
            }
        }
        add(domain);
        return null;
    }

    @Override
    public Void visitUse_statement(SqlServerParser.Use_statementContext ctx) {
        RdbCatalogDomain domain = builder.newCatalogDomain(RuleQueryType.SWITCH_CATALOG);
        domain.setCatalog(clean(ctx.database));
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_sequence(SqlServerParser.Create_sequenceContext ctx) {
        NameParts name = parts(ctx);
        RdbSequenceDomain domain = builder.newSequenceDomain(RuleQueryType.CREATE_SEQUENCE);
        domain.setSchema(name.schema);
        domain.setName(name.name);
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_synonym(SqlServerParser.Create_synonymContext ctx) {
        RdbResourceDomain domain = builder.newResourceDomain(RuleQueryType.CREATE_SYNONYM, SecQueryKind.CREATE);
        domain.setSchema(clean(ctx.schema_name_1));
        domain.setName(clean(ctx.synonym_name));
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_login_sql_server(SqlServerParser.Create_login_sql_serverContext ctx) {
        RdbUserDomain domain = builder.newUserDomain(RuleQueryType.CREATE_USER);
        domain.setUser(clean(ctx.login_name));
        if (ctx.password != null) {
            domain.setPassword(stripQuote(ctx.password.getText()));
        } else if (ctx.password_hash != null) {
            domain.setPassword(ctx.password_hash.getText());
        }
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        NameParts name = parts(ctx.func_proc_name_schema());
        boolean alter = startsWithAlter(ctx);
        RdbFunctionDomain domain = builder.newFunctionDomain(alter ? RuleQueryType.ALTER_PROG_OBJ : RuleQueryType.CREATE_PROG_OBJ);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setName(name.name);
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        NameParts name = parts(ctx.func_proc_name_schema());
        boolean alter = startsWithAlter(ctx);
        RdbProcedureDomain domain = builder.newProcedureDomain(alter ? RuleQueryType.ALTER_PROG_OBJ : RuleQueryType.CREATE_PROG_OBJ);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setName(name.name);
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_or_alter_trigger(SqlServerParser.Create_or_alter_triggerContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_or_alter_dml_trigger(SqlServerParser.Create_or_alter_dml_triggerContext ctx) {
        NameParts trigger = parts(ctx.simple_name());
        NameParts table = parts(ctx.table_name());
        boolean alter = startsWithAlter(ctx);
        RdbTriggerDomain domain = builder.newTriggerDomain(alter ? RuleQueryType.ALTER_TRIGGER : RuleQueryType.CREATE_TRIGGER);
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setName(trigger.name);
        add(domain);
        return null;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof ParserRuleContext) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText((ParserRuleContext) node));
        }
        throw new UnsupportedOperationException("unsupported SQL: " + node.getText());
    }

    private void dmVisitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            c.accept(this);
        }
    }

    private void add(RuleDomain domain) {
        if (domain.getAuditKind() == null) {
            domain.setAuditKind(domain.getSqlType() == null ? SecQueryKind.OTHER : domain.getSqlType().getAuditKind());
        }
        builder.addDomain(domain);
    }

    private MsTableDomain tableDomain(ParserRuleContext ctx, RuleQueryType type, SecQueryKind kind) {
        NameParts name = parts(ctx);
        MsTableDomain domain = builder.newTableDomain(type, kind);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        return domain;
    }

    private List<RuleDomain> tableElementDomains(SqlServerParser.Column_def_table_constraintsContext ctx, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns,
                                                 RuleQueryType columnType, RuleQueryType constraintType) {
        if (ctx == null) {
            return Collections.emptyList();
        }
        List<RuleDomain> result = new ArrayList<>();
        for (SqlServerParser.Column_def_table_constraintContext item : ctx.column_def_table_constraint()) {
            if (item.column_definition() != null) {
                MsColumnDomain column = columnDomain(item.column_definition(), tableDomain, columnType, columnType.getAuditKind());
                result.add(column);
                tableDomain.getColumnDomains().add(column);
                tableDomain.getColumns().add(column.getColumn());
                columns.put(column.getColumn(), column);
                if (column.isPrimary()) {
                    tableDomain.setHasPrimary(true);
                }
                if (column.isUnique()) {
                    tableDomain.setHasUnique(true);
                }
                if (column.isForeign()) {
                    tableDomain.setHasForeignKey(true);
                }
            } else if (item.table_constraint() != null) {
                RdbConstraintDomain constraint = tableConstraintDomain(item.table_constraint(), tableDomain, constraintType);
                result.add(constraint);
                tableDomain.getConstraintDomains().add(constraint);
                markTableConstraint(tableDomain, columns, constraint);
            }
        }
        return result;
    }

    private MsColumnDomain columnDomain(SqlServerParser.Column_definitionContext ctx, MsTableDomain tableDomain, RuleQueryType type, SecQueryKind kind) {
        MsColumnDomain domain = simpleColumnDomain(tableDomain, clean(ctx.id_()), type, kind);
        if (ctx.data_type() != null) {
            domain.setTypeDesc(getText(ctx.data_type()));
            domain.setTypeName(dataTypeName(ctx.data_type()));
            domain.setLength(dataTypeLength(ctx.data_type()));
        }
        domain.setNullable(true);
        for (SqlServerParser.Column_definition_elementContext element : ctx.column_definition_element()) {
            if (element.constant_expr != null) {
                domain.setDefaultValue(stripQuote(getText(element.constant_expr)));
            }
            SqlServerParser.Column_constraintContext constraint = element.column_constraint();
            if (constraint == null) {
                continue;
            }
            if (constraint.PRIMARY() != null) {
                domain.setPrimary(true);
            }
            if (constraint.UNIQUE() != null) {
                domain.setUnique(true);
            }
            if (constraint.FOREIGN() != null || constraint.foreign_key_options() != null) {
                domain.setForeign(true);
            }
            if (constraint.null_notnull() != null && constraint.null_notnull().NOT() != null) {
                domain.setNullable(false);
            }
        }
        return domain;
    }

    private MsColumnDomain simpleColumnDomain(MsTableDomain tableDomain, String column, RuleQueryType type, SecQueryKind kind) {
        MsColumnDomain domain = builder.newColumnDomain(type, kind);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setTable(tableDomain.getTable());
        domain.setColumn(column);
        return domain;
    }

    private RdbConstraintDomain tableConstraintDomain(SqlServerParser.Table_constraintContext ctx, MsTableDomain tableDomain, RuleQueryType type) {
        SqlConstraintType constraintType = SqlConstraintType.Check;
        List<String> columns = Collections.emptyList();
        if (ctx.PRIMARY() != null) {
            constraintType = SqlConstraintType.Primary;
            columns = names(ctx.column_name_list_with_order());
        } else if (ctx.UNIQUE() != null) {
            constraintType = SqlConstraintType.Unique;
            columns = names(ctx.column_name_list_with_order());
        } else if (ctx.FOREIGN() != null) {
            constraintType = SqlConstraintType.ForeignKey;
            columns = names(ctx.fk != null ? ctx.fk : ctx.column_name_list());
        } else if (ctx.DEFAULT() != null) {
            constraintType = SqlConstraintType.Default;
            columns = ctx.column != null ? Collections.singletonList(clean(ctx.column)) : Collections.emptyList();
        }
        return constraintDomain(tableDomain, type, constraintType, clean(ctx.constraint), columns);
    }

    private RdbConstraintDomain alterTableConstraintDomain(SqlServerParser.Alter_tableContext ctx, MsTableDomain tableDomain) {
        SqlConstraintType type = ctx.FOREIGN() != null ? SqlConstraintType.ForeignKey : SqlConstraintType.Check;
        List<String> columns = ctx.fk != null ? names(ctx.fk) : Collections.emptyList();
        RdbConstraintDomain domain = constraintDomain(tableDomain, RuleQueryType.ALTER_TABLE_ADD_CONSTRAINT, type, clean(ctx.constraint), columns);
        markTableConstraint(tableDomain, Collections.emptyMap(), domain);
        return domain;
    }

    private RdbConstraintDomain constraintDomain(MsTableDomain tableDomain, RuleQueryType queryType, SqlConstraintType constraintType, String name, List<String> columns) {
        RdbConstraintDomain domain = builder.newConstraintDomain(queryType);
        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(name);
        domain.setType(constraintType);
        domain.setColumns(new ArrayList<>(columns));
        return domain;
    }

    private void markTableConstraint(MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, RdbConstraintDomain constraint) {
        if (constraint.getType() == SqlConstraintType.Primary) {
            tableDomain.setHasPrimary(true);
            constraint.getColumns().forEach(column -> {
                if (columns.containsKey(column)) {
                    columns.get(column).setPrimary(true);
                }
            });
        } else if (constraint.getType() == SqlConstraintType.Unique) {
            tableDomain.setHasUnique(true);
            constraint.getColumns().forEach(column -> {
                if (columns.containsKey(column)) {
                    columns.get(column).setUnique(true);
                }
            });
        } else if (constraint.getType() == SqlConstraintType.ForeignKey) {
            tableDomain.setHasForeignKey(true);
            constraint.getColumns().forEach(column -> {
                if (columns.containsKey(column)) {
                    columns.get(column).setForeign(true);
                }
            });
        }
    }

    private String dataTypeName(SqlServerParser.Data_typeContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx.scaled != null) {
            return stripQuote(ctx.scaled.getText());
        }
        if (ctx.ext_type != null) {
            return clean(ctx.ext_type);
        }
        if (ctx.unscaled_type != null) {
            return clean(ctx.unscaled_type);
        }
        if (ctx.DOUBLE() != null) {
            return "DOUBLE";
        }
        return ctx.getChildCount() == 0 ? null : stripQuote(ctx.getChild(0).getText());
    }

    private String dataTypeLength(SqlServerParser.Data_typeContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx.MAX() != null) {
            return "MAX";
        }
        if (ctx.scale != null) {
            return ctx.scale.getText();
        }
        if (!ctx.DECIMAL().isEmpty()) {
            return ctx.DECIMAL(0).getText();
        }
        return null;
    }

    private void markWhere(RdbWhereDomain domain, SqlServerParser.Search_conditionContext ctx) {
        if (ctx == null) {
            return;
        }
        if (!descendants(ctx, SqlServerParser.SubqueryContext.class).isEmpty()) {
            domain.setSelectInWhere(true);
        }
        if (isTautology(ctx)) {
            return;
        }
        domain.setHasWhere(true);
        for (SqlServerParser.Full_column_nameContext column : descendants(ctx, SqlServerParser.Full_column_nameContext.class)) {
            String columnName = column.column_name != null ? clean(column.column_name) : lastName(column);
            domain.addWhereColumn(columnName);
        }
    }

    private boolean isTautology(SqlServerParser.Search_conditionContext ctx) {
        if (ctx == null || !ctx.NOT().isEmpty() || ctx.AND() != null || ctx.OR() != null) {
            return false;
        }
        if (ctx.predicate() != null) {
            SqlServerParser.PredicateContext predicate = ctx.predicate();
            return predicate.comparison_operator() != null && predicate.comparison_operator().getStart().getType() == SqlServerParser.EQUAL && predicate.expression().size() == 2
                   && sameAst(predicate.expression(0), predicate.expression(1));
        }
        return ctx.search_condition().size() == 1 && isTautology(ctx.search_condition(0));
    }

    private boolean sameAst(ParseTree left, ParseTree right) {
        if (left instanceof TerminalNode leftTerminal && right instanceof TerminalNode rightTerminal) {
            return leftTerminal.getSymbol().getType() == rightTerminal.getSymbol().getType() && leftTerminal.getText().equalsIgnoreCase(rightTerminal.getText());
        }
        if (left == null || right == null || left.getClass() != right.getClass() || left.getChildCount() != right.getChildCount()) {
            return false;
        }
        for (int i = 0; i < left.getChildCount(); i++) {
            if (!sameAst(left.getChild(i), right.getChild(i))) {
                return false;
            }
        }
        return true;
    }

    private void addJoinType(MsSelectDomain domain, SqlServerParser.Join_partContext join) {
        RdbJoinType type;
        if (join.cross_join() != null || join.apply_() != null && join.apply_().CROSS() != null) {
            type = RdbJoinType.CROSS_JOIN;
        } else if (join.join_on() != null && join.join_on().LEFT() != null) {
            type = RdbJoinType.LEFT_JOIN;
        } else if (join.join_on() != null && join.join_on().RIGHT() != null) {
            type = RdbJoinType.RIGHT_JOIN;
        } else if (join.join_on() != null) {
            type = RdbJoinType.INNER_JOIN;
        } else {
            type = RdbJoinType.OTHER_JOIN;
        }
        if (!domain.getJoinTypes().contains(type)) {
            domain.getJoinTypes().add(type);
        }
        domain.setJoinType(type);
    }

    private boolean tryAddRenameDomain(SqlServerParser.Execute_statementContext ctx) {
        SqlServerParser.Execute_bodyContext body = ctx.execute_body();
        if (body == null || body.func_proc_name_server_database_schema() == null) {
            return false;
        }
        List<String> procNames = names(body.func_proc_name_server_database_schema());
        if (procNames.isEmpty() || !"sp_rename".equalsIgnoreCase(procNames.get(procNames.size() - 1))) {
            return false;
        }
        List<String> args = executeArgs(body);
        if (args.size() < 2) {
            return false;
        }
        String target = args.size() > 2 ? args.get(2).toUpperCase(Locale.ROOT) : "";
        if (target.contains("DATABASE")) {
            MsCatalogDomain domain = builder.newCatalogDomain(RuleQueryType.RENAME_CATALOG);
            domain.setCatalog(lastIdentifier(args.get(0)));
            domain.setNewName(lastIdentifier(args.get(1)));
            add(domain);
            return true;
        }
        if (target.contains("COLUMN")) {
            MsColumnDomain domain = builder.newColumnDomain(RuleQueryType.RENAME_COLUMN, SecQueryKind.ALTER);
            domain.setColumn(lastIdentifier(args.get(0)));
            domain.setNewName(lastIdentifier(args.get(1)));
            add(domain);
            return true;
        }
        if (target.contains("OBJECT")) {
            MsTableDomain domain = builder.newTableDomain(RuleQueryType.RENAME_TABLE, SecQueryKind.ALTER);
            domain.setTable(lastIdentifier(args.get(0)));
            domain.setNewName(lastIdentifier(args.get(1)));
            add(domain);
            return true;
        }
        return false;
    }

    private List<String> executeArgs(SqlServerParser.Execute_bodyContext body) {
        List<String> args = new ArrayList<>();
        for (SqlServerParser.Execute_parameterContext argument : descendants(body.execute_statement_arg(), SqlServerParser.Execute_parameterContext.class)) {
            String cleanArg = stripQuote(argument.getText());
            if (!cleanArg.isEmpty()) {
                args.add(cleanArg);
            }
        }
        return args;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private boolean startsWithAlter(ParserRuleContext ctx) {
        return ctx.getChildCount() > 0 && "alter".equalsIgnoreCase(ctx.getChild(0).getText());
    }

    private String clean(SqlServerParser.Id_Context ctx) {
        return ctx == null ? null : stripQuote(getText(ctx));
    }

    private String stripQuote(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        if ((trimmed.startsWith("[") && trimmed.endsWith("]")) || (trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private NameParts parts(ParserRuleContext ctx) {
        List<String> names = names(ctx);
        if (names.isEmpty()) {
            return new NameParts(null, null, null);
        }
        if (names.size() == 1) {
            return new NameParts(null, null, names.get(0));
        }
        if (names.size() == 2) {
            return new NameParts(null, names.get(0), names.get(1));
        }
        return new NameParts(names.get(names.size() - 3), names.get(names.size() - 2), names.get(names.size() - 1));
    }

    private List<String> names(ParserRuleContext ctx) {
        if (ctx == null) {
            return Collections.emptyList();
        }
        List<String> names = new ArrayList<>();
        collectIds(ctx, names);
        return names;
    }

    private void collectIds(ParseTree tree, List<String> names) {
        if (tree instanceof SqlServerParser.Id_Context id) {
            names.add(stripQuote(getText(id)));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectIds(tree.getChild(i), names);
        }
    }

    private String lastName(ParserRuleContext ctx) {
        List<String> names = names(ctx);
        return names.isEmpty() ? null : names.get(names.size() - 1);
    }

    private String lastIdentifier(String text) {
        if (text == null) {
            return null;
        }
        int dot = text.lastIndexOf('.');
        return stripQuote(dot < 0 ? text : text.substring(dot + 1));
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        if (tree == null) {
            return null;
        }
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T found = first(tree.getChild(i), type);
            if (found != null) {
                return found;
            }
        }
        return null;
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

    private <T extends ParserRuleContext> T parent(ParserRuleContext ctx, Class<T> type) {
        ParserRuleContext parent = ctx.getParent();
        while (parent != null) {
            if (type.isInstance(parent)) {
                return type.cast(parent);
            }
            parent = parent.getParent();
        }
        return null;
    }

    private record NameParts(String catalog, String schema, String name) {
    }
}

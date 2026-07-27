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
package com.clougence.sql.db2.analysis.security;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.db2.analysis.security.builder.Db2BuildFactory;
import com.clougence.sql.db2.analysis.security.domain.*;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;
import com.clougence.sql.db2.parser.antlr.Db2SqlParserBaseVisitor;

public class Db2ParserVisitor extends Db2SqlParserBaseVisitor<Void> {

    private final Db2BuildFactory builder;
    private final Parser          parser;

    public Db2ParserVisitor(Db2BuildFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    @Override
    public Void visitCreate_schema_statement(Db2SqlParser.Create_schema_statementContext ctx) {
        Db2SchemaDomain domain = builder.newSchemaDomain(SplitQueryType.CREATE_SCHEMA);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setSchema(clean(ctx.schema_name() == null ? text(ctx.authorization_name()) : text(ctx.schema_name())));
        add(domain);
        return null;
    }

    @Override
    public Void visitDrop_statement(Db2SqlParser.Drop_statementContext ctx) {
        if (ctx.schema_name() != null) {
            Db2SchemaDomain domain = builder.newSchemaDomain(SplitQueryType.DROP_SCHEMA);
            domain.setAuditKind(SecQueryKind.DROP);
            domain.setIfExists(ctx.IF() != null);
            domain.setSchema(clean(text(ctx.schema_name())));
            add(domain);
        } else if (ctx.table_name() != null) {
            Db2TableDomain domain = tableDomain(ctx, SplitQueryType.DROP_TABLE, SecQueryKind.DROP, text(ctx.table_name()));
            domain.setIfExists(ctx.IF() != null);
            add(domain);
        } else if (ctx.index_name() != null) {
            RdbIndexDomain domain = builder.newIndexDomain(SplitQueryType.DROP_INDEX);
            domain.setName(lastName(text(ctx.index_name())));
            add(domain);
        } else if (ctx.view_name() != null) {
            add(viewDomain(SplitQueryType.DROP_VIEW, SecQueryKind.DROP, text(ctx.view_name())));
        }
        return null;
    }

    @Override
    public Void visitCreate_table_statement(Db2SqlParser.Create_table_statementContext ctx) {
        Db2TableDomain table = tableDomain(ctx, SplitQueryType.CREATE_TABLE, SecQueryKind.CREATE, text(ctx.table_name()));
        table.setColumns(new ArrayList<>());
        Map<String, Db2ColumnDomain> columns = new LinkedHashMap<>();
        List<RuleDomain> children = new ArrayList<>();
        if (ctx.element_list() != null) {
            for (Db2SqlParser.Element_list_itemContext item : ctx.element_list().element_list_item()) {
                collectCreateTableItem(table, columns, children, item);
            }
        }
        add(table);
        children.forEach(this::add);
        if (ctx.as_result_table() != null) {
            add(selectDomain(ctx.as_result_table(), RdbQueryMode.CREATE));
        }
        return null;
    }

    @Override
    public Void visitAlter_table_statement(Db2SqlParser.Alter_table_statementContext ctx) {
        Db2TableDomain table = tableDomain(ctx, SplitQueryType.ALTER_TABLE, SecQueryKind.ALTER, text(ctx.table_name(0)));
        table.setColumns(new ArrayList<>());
        Map<String, Db2ColumnDomain> columns = new LinkedHashMap<>();
        List<RuleDomain> children = new ArrayList<>();
        for (Db2SqlParser.Alter_table_optsContext opt : ctx.alter_table_opts()) {
            collectAlterTableOpt(table, columns, children, opt);
        }
        add(table);
        children.forEach(this::add);
        return null;
    }

    @Override
    public Void visitRename_statement(Db2SqlParser.Rename_statementContext ctx) {
        if (ctx.source_table_name() != null) {
            Db2TableDomain domain = tableDomain(ctx, SplitQueryType.RENAME_TABLE, SecQueryKind.ALTER, text(ctx.source_table_name()));
            domain.setNewName(lastName(text(ctx.target_identifier())));
            add(domain);
        }
        return null;
    }

    @Override
    public Void visitTruncate_statement(Db2SqlParser.Truncate_statementContext ctx) {
        add(tableDomain(ctx, SplitQueryType.TRUNCATE_TABLE, SecQueryKind.DML, text(ctx.table_name())));
        return null;
    }

    @Override
    public Void visitComment_statement(Db2SqlParser.Comment_statementContext ctx) {
        if (ctx.comment_objects() != null) {
            if (ctx.comment_objects().TABLE() != null) {
                Db2TableDomain domain = tableDomain(ctx, SplitQueryType.COMMENT_TABLE, SecQueryKind.ALTER, text(ctx.comment_objects().table_or_view_name()));
                domain.setComment(commentText(ctx.string_constant()));
                add(domain);
            } else if (ctx.comment_objects().COLUMN() != null) {
                commentColumn(ctx);
            }
        } else {
            commentColumns(ctx);
        }
        return null;
    }

    @Override
    public Void visitCreate_index_statement(Db2SqlParser.Create_index_statementContext ctx) {
        NameParts table = parts(ctx.table_name() == null ? text(ctx.nick_name()) : text(ctx.table_name()));
        RdbIndexDomain domain = builder.newIndexDomain(SplitQueryType.ADD_INDEX);
        domain.setName(lastName(text(ctx.index_name())));
        domain.setTableCatalog(table.catalog);
        domain.setTableSchema(table.schema);
        domain.setTableName(table.name);
        domain.setColumns(names(ctx.index_col_opts()));
        add(domain);
        return null;
    }

    @Override
    public Void visitCreate_view_statement(Db2SqlParser.Create_view_statementContext ctx) {
        add(viewDomain(SplitQueryType.CREATE_VIEW, SecQueryKind.CREATE, text(ctx.view_name())));
        if (ctx.fullselect() != null) {
            add(selectDomain(ctx.fullselect(), RdbQueryMode.NORMAL));
        }
        return null;
    }

    @Override
    public Void visitAlter_view_statement(Db2SqlParser.Alter_view_statementContext ctx) {
        add(viewDomain(SplitQueryType.ALTER_VIEW, SecQueryKind.ALTER, text(ctx.view_name())));
        return null;
    }

    @Override
    public Void visitCall_statement(Db2SqlParser.Call_statementContext ctx) {
        RdbCallDomain domain = builder.newCallDomain();
        NameParts name = parts(text(ctx.procedure_name()));
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setCallName(name.name);
        domain.setArgs(new ArrayList<>());
        if (ctx.arg_list_paren() == null) {
            domain.setEmptyArg(true);
        } else {
            List<Db2SqlParser.ArgumentContext> arguments = descendants(ctx.arg_list_paren(), Db2SqlParser.ArgumentContext.class);
            domain.setEmptyArg(arguments.isEmpty());
            for (Db2SqlParser.ArgumentContext argument : arguments) {
                domain.addArgStr(text(argument));
            }
        }
        add(domain);
        return null;
    }

    @Override
    public Void visitSelect_statement(Db2SqlParser.Select_statementContext ctx) {
        add(selectDomain(ctx, RdbQueryMode.NORMAL));
        return null;
    }

    @Override
    public Void visitSelect_into_statement(Db2SqlParser.Select_into_statementContext ctx) {
        add(selectDomain(ctx, RdbQueryMode.NORMAL));
        return null;
    }

    @Override
    public Void visitInsert_statement(Db2SqlParser.Insert_statementContext ctx) {
        String rawName = ctx.table_or_view_name() == null ? text(ctx.nick_name()) : text(ctx.table_or_view_name());
        NameParts table = parts(rawName);
        Db2InsertDomain domain = builder.newInsertDomain();
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setColumns(insertColumns(ctx));
        domain.setOnlyValues(domain.getColumns().isEmpty());
        domain.setFromSelect(!ctx.fullselect().isEmpty());
        domain.setHasSubQuery(!ctx.fullselect().isEmpty());
        add(domain);
        for (Db2SqlParser.FullselectContext fullselect : ctx.fullselect()) {
            add(selectDomain(fullselect, RdbQueryMode.SUB_SELECT));
        }
        return null;
    }

    @Override
    public Void visitUpdate_statement(Db2SqlParser.Update_statementContext ctx) {
        Db2SqlParser.Update_statement_searched_updateContext searched = ctx.update_statement_searched_update();
        if (searched == null) {
            return null;
        }
        String rawName = searched.table_or_view_name() == null ? text(searched.nick_name()) : text(searched.table_or_view_name());
        NameParts table = parts(rawName);
        Db2UpdateDomain domain = builder.newUpdateDomain();
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setSetColumns(assignmentColumns(searched.assignment_clause()));
        domain.setWhereColumns(new ArrayList<>());
        markWhere(domain, searched.where_clause());
        add(domain);
        return null;
    }

    @Override
    public Void visitDelete_statement(Db2SqlParser.Delete_statementContext ctx) {
        Db2SqlParser.Delete_statement_searched_deleteContext searched = ctx.delete_statement_searched_delete();
        if (searched == null) {
            return null;
        }
        String rawName = searched.table_or_view_name() == null ? text(searched.nick_name()) : text(searched.table_or_view_name());
        NameParts table = parts(rawName);
        Db2DeleteDomain domain = builder.newDeleteDomain();
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setWhereColumns(new ArrayList<>());
        markWhere(domain, searched.where_clause());
        add(domain);
        return null;
    }

    private void collectCreateTableItem(Db2TableDomain table, Map<String, Db2ColumnDomain> columns, List<RuleDomain> children, Db2SqlParser.Element_list_itemContext item) {
        if (item.column_definition() != null) {
            Db2ColumnDomain column = columnDomain(table, item.column_definition(), SplitQueryType.CREATE_TABLE, SecQueryKind.CREATE);
            children.add(column);
            table.getColumns().add(column.getColumn());
            columns.put(column.getColumn(), column);
            markColumnConstraint(table, column);
        } else if (item.index_name() != null) {
            RdbIndexDomain index = indexDomain(table, text(item.index_name()), item.index_col_opts(), SplitQueryType.CREATE_TABLE);
            children.add(index);
            table.setHasIndex(true);
        } else {
            RdbConstraintDomain constraint = constraintDomain(table, item, SplitQueryType.CREATE_TABLE);
            if (constraint != null) {
                children.add(constraint);
                markTableConstraint(table, columns, constraint);
            }
        }
    }

    private void collectAlterTableOpt(Db2TableDomain table, Map<String, Db2ColumnDomain> columns, List<RuleDomain> children, Db2SqlParser.Alter_table_optsContext opt) {
        if (opt.column_definition() != null) {
            Db2ColumnDomain column = columnDomain(table, opt.column_definition(), SplitQueryType.ADD_COLUMN, SecQueryKind.ALTER);
            children.add(column);
            table.getColumns().add(column.getColumn());
            columns.put(column.getColumn(), column);
            markColumnConstraint(table, column);
        } else if (opt.unique_constraint() != null || opt.referential_constraint() != null || opt.check_constraint() != null) {
            RdbConstraintDomain constraint = constraintDomain(table, opt, SplitQueryType.ADD_CONSTRAINT);
            if (constraint != null) {
                children.add(constraint);
                markTableConstraint(table, columns, constraint);
            }
        } else if (opt.index_name() != null) {
            RdbIndexDomain index = indexDomain(table, text(opt.index_name()), opt.index_col_opts(), SplitQueryType.ADD_INDEX);
            children.add(index);
            table.setHasIndex(true);
        } else if (opt.s != null && opt.t != null) {
            Db2ColumnDomain column = simpleColumnDomain(table, text(opt.s), SplitQueryType.RENAME_COLUMN, SecQueryKind.ALTER);
            column.setNewName(clean(text(opt.t)));
            children.add(column);
        } else if (opt.target_identifier() != null) {
            table.addSqlType(SplitQueryType.RENAME_TABLE);
            table.setNewName(lastName(text(opt.target_identifier())));
        } else if (opt.column_name().size() == 1 && opt.DROP().size() > 0) {
            children.add(simpleColumnDomain(table, text(opt.column_name(0)), SplitQueryType.DROP_COLUMN, SecQueryKind.ALTER));
        }
    }

    private void commentColumn(Db2SqlParser.Comment_statementContext ctx) {
        NameParts table = parts(text(ctx.comment_objects().table_or_view_name()));
        Db2ColumnDomain domain = builder.newColumnDomain(SplitQueryType.COMMENT_COLUMN, SecQueryKind.ALTER);
        domain.setCatalog(table.catalog);
        domain.setSchema(table.schema);
        domain.setTable(table.name);
        domain.setColumn(clean(text(ctx.comment_objects().column_name())));
        domain.setComment(commentText(ctx.string_constant()));
        add(domain);
    }

    private void commentColumns(Db2SqlParser.Comment_statementContext ctx) {
        NameParts table = parts(text(ctx.table_or_view_name()));
        for (Db2SqlParser.Column_commentContext columnComment : ctx.column_comment()) {
            Db2ColumnDomain domain = builder.newColumnDomain(SplitQueryType.COMMENT_COLUMN, SecQueryKind.ALTER);
            domain.setCatalog(table.catalog);
            domain.setSchema(table.schema);
            domain.setTable(table.name);
            domain.setColumn(clean(text(columnComment.column_name())));
            domain.setComment(commentText(columnComment.string_constant()));
            add(domain);
        }
    }

    private Db2SelectDomain selectDomain(ParserRuleContext ctx, RdbQueryMode mode) {
        Db2SelectDomain domain = builder.newSelectDomain(mode);
        domain.setHasWith(hasDescendant(ctx, Db2SqlParser.Common_table_expression_listContext.class));
        for (Db2SqlParser.FullselectContext fullselect : descendants(ctx, Db2SqlParser.FullselectContext.class)) {
            if (!fullselect.UNION().isEmpty()) {
                domain.setHasUnion(true);
            }
            if (fullselect.fetch_clause() != null) {
                domain.setHasLimit(true);
            }
        }
        for (Db2SqlParser.Select_clause_itemContext item : descendants(ctx, Db2SqlParser.Select_clause_itemContext.class)) {
            collectSelectItem(domain, item);
        }
        for (Db2SqlParser.Singles_table_referenceContext tableReference : descendants(ctx, Db2SqlParser.Singles_table_referenceContext.class)) {
            collectTable(domain, tableReference);
        }
        for (Db2SqlParser.Where_clauseContext where : descendants(ctx, Db2SqlParser.Where_clauseContext.class)) {
            markWhere(domain, where);
        }
        return domain;
    }

    private void collectSelectItem(Db2SelectDomain domain, Db2SqlParser.Select_clause_itemContext item) {
        QueryItem queryItem = builder.newQueryItem();
        queryItem.setColumn(text(item));
        if (item.STAR() != null) {
            queryItem.setSelectAll(true);
            domain.setHasSelectAll(true);
        } else if (item.expression() != null) {
            if (hasFunctionLikeExpression(item.expression())) {
                domain.setFuncInSelect(true);
                domain.addSelect(text(item.expression()), RdbQuerySelectType.Function);
            } else if (item.column_name() != null) {
                domain.addSelect(lastName(text(item.column_name())), RdbQuerySelectType.Column);
            }
        } else if (item.exposed_name() != null) {
            queryItem.setSelectAll(true);
            domain.setHasSelectAll(true);
        }
        if (item.AS() != null) {
            domain.setHasAs(true);
        }
        domain.getColumns().add(queryItem);
    }

    private void collectTable(Db2SelectDomain domain, Db2SqlParser.Singles_table_referenceContext tableReference) {
        Db2TableDomain table = tableDomain(tableReference, SplitQueryType.SELECT, SecQueryKind.QUERY, text(tableReference.table_name()));
        if (tableReference.correlation_clause() != null) {
            table.setAlias(clean(text(tableReference.correlation_clause().correlation_name())));
        }
        domain.addChild(table);
        domain.setEmptyFrom(false);
        if (domain.getTable() == null) {
            domain.setCatalog(table.getCatalog());
            domain.setSchema(table.getSchema());
            domain.setTable(table.getTable());
        }
        for (Db2SqlParser.Join_clauseContext join : tableReference.join_clause()) {
            addJoinType(domain, join);
        }
    }

    private Db2ColumnDomain columnDomain(Db2TableDomain table, Db2SqlParser.Column_definitionContext definition, SplitQueryType type, SecQueryKind kind) {
        Db2ColumnDomain domain = simpleColumnDomain(table, text(definition.column_name()), type, kind);
        if (definition.data_type() != null) {
            domain.setTypeDesc(text(definition.data_type()));
            domain.setTypeName(clean(firstTerminalText(definition.data_type())));
        }
        for (Db2SqlParser.Column_options_itemContext option : descendants(definition, Db2SqlParser.Column_options_itemContext.class)) {
            collectColumnOption(domain, option);
        }
        return domain;
    }

    private Db2ColumnDomain columnDomain(Db2TableDomain table, String definition, SplitQueryType type, SecQueryKind kind) {
        Db2ColumnDomain domain = simpleColumnDomain(table, firstIdentifier(definition), type, kind);
        domain.setTypeDesc(definition);
        domain.setTypeName(firstIdentifier(definition));
        return domain;
    }

    private void collectColumnOption(Db2ColumnDomain domain, Db2SqlParser.Column_options_itemContext option) {
        if (option.NOT() != null && option.NULL_() != null) {
            domain.setNullable(false);
        }
        if (option.PRIMARY() != null) {
            domain.setPrimary(true);
        }
        if (option.UNIQUE() != null) {
            domain.setUnique(true);
        }
        if (option.references_clause() != null) {
            domain.setForeign(true);
        }
        if (option.default_clause() != null) {
            domain.setDefaultValue(commentText(option.default_clause().default_values()));
        }
    }

    private Db2ColumnDomain simpleColumnDomain(Db2TableDomain table, String column, SplitQueryType type, SecQueryKind kind) {
        Db2ColumnDomain domain = builder.newColumnDomain(type, kind);
        domain.setCatalog(table.getCatalog());
        domain.setSchema(table.getSchema());
        domain.setTable(table.getTable());
        domain.setColumn(clean(column));
        return domain;
    }

    private RdbConstraintDomain constraintDomain(Db2TableDomain table, Db2SqlParser.Element_list_itemContext item, SplitQueryType type) {
        if (item.unique_constraint() != null) {
            return uniqueConstraintDomain(table, item.unique_constraint(), type);
        }
        if (item.referential_constraint() != null) {
            return referentialConstraintDomain(table, item.referential_constraint(), type);
        }
        if (item.check_constraint() != null) {
            return checkConstraintDomain(table, item.check_constraint(), type);
        }
        return null;
    }

    private RdbConstraintDomain constraintDomain(Db2TableDomain table, Db2SqlParser.Alter_table_optsContext opt, SplitQueryType type) {
        if (opt.unique_constraint() != null) {
            return uniqueConstraintDomain(table, opt.unique_constraint(), type);
        }
        if (opt.referential_constraint() != null) {
            return referentialConstraintDomain(table, opt.referential_constraint(), type);
        }
        if (opt.check_constraint() != null) {
            return checkConstraintDomain(table, opt.check_constraint(), type);
        }
        return null;
    }

    private RdbConstraintDomain constraintDomain(Db2TableDomain table, String text, SplitQueryType type) {
        RdbConstraintDomain domain = baseConstraint(table, type);
        domain.setColumns(new ArrayList<>());
        return domain;
    }

    private RdbConstraintDomain uniqueConstraintDomain(Db2TableDomain table, Db2SqlParser.Unique_constraintContext constraint, SplitQueryType type) {
        RdbConstraintDomain domain = baseConstraint(table, type);
        domain.setType(constraint.PRIMARY() == null ? SqlConstraintType.Unique : SqlConstraintType.Primary);
        if (constraint.constraint_name() != null) {
            domain.setName(clean(text(constraint.constraint_name())));
        }
        domain.setColumns(names(constraint.column_name_list()));
        return domain;
    }

    private RdbConstraintDomain referentialConstraintDomain(Db2TableDomain table, Db2SqlParser.Referential_constraintContext constraint, SplitQueryType type) {
        RdbConstraintDomain domain = baseConstraint(table, type);
        domain.setType(SqlConstraintType.ForeignKey);
        if (constraint.constraint_name() != null) {
            domain.setName(clean(text(constraint.constraint_name())));
        }
        domain.setColumns(names(constraint.column_name_list()));
        return domain;
    }

    private RdbConstraintDomain checkConstraintDomain(Db2TableDomain table, Db2SqlParser.Check_constraintContext constraint, SplitQueryType type) {
        RdbConstraintDomain domain = baseConstraint(table, type);
        domain.setType(SqlConstraintType.Check);
        if (constraint.constraint_name() != null) {
            domain.setName(clean(text(constraint.constraint_name())));
        }
        domain.setColumns(new ArrayList<>());
        return domain;
    }

    private RdbConstraintDomain baseConstraint(Db2TableDomain table, SplitQueryType type) {
        RdbConstraintDomain domain = builder.newConstraintDomain(type);
        domain.setTableCatalog(table.getCatalog());
        domain.setTableSchema(table.getSchema());
        domain.setTableName(table.getTable());
        domain.setCatalog(table.getCatalog());
        domain.setSchema(table.getSchema());
        return domain;
    }

    private RdbIndexDomain indexDomain(Db2TableDomain table, String name, Db2SqlParser.Index_col_optsContext columns, SplitQueryType type) {
        RdbIndexDomain domain = builder.newIndexDomain(type);
        domain.setTableCatalog(table.getCatalog());
        domain.setTableSchema(table.getSchema());
        domain.setTableName(table.getTable());
        domain.setName(lastName(name));
        domain.setColumns(names(columns));
        return domain;
    }

    private RdbIndexDomain indexDomain(Db2TableDomain table, String text, SplitQueryType type) {
        RdbIndexDomain domain = builder.newIndexDomain(type);
        domain.setTableCatalog(table.getCatalog());
        domain.setTableSchema(table.getSchema());
        domain.setTableName(table.getTable());
        domain.setName(firstIdentifier(text));
        domain.setColumns(new ArrayList<>());
        return domain;
    }

    private Db2TableDomain tableDomain(ParserRuleContext ctx, SplitQueryType type, SecQueryKind kind, String rawName) {
        NameParts name = parts(rawName);
        Db2TableDomain domain = builder.newTableDomain(type, kind);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        return domain;
    }

    private RdbViewDomain viewDomain(SplitQueryType type, SecQueryKind kind, String rawName) {
        NameParts name = parts(rawName);
        RdbViewDomain domain = builder.newViewDomain(type);
        domain.setAuditKind(kind);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setView(name.name);
        return domain;
    }

    private void markColumnConstraint(Db2TableDomain table, Db2ColumnDomain column) {
        if (column.isPrimary()) {
            table.setHasPrimary(true);
        }
        if (column.isUnique()) {
            table.setHasUnique(true);
        }
        if (column.isForeign()) {
            table.setHasForeignKey(true);
        }
    }

    private void markTableConstraint(Db2TableDomain table, Map<String, Db2ColumnDomain> columns, RdbConstraintDomain constraint) {
        if (constraint.getType() == SqlConstraintType.Primary) {
            table.setHasPrimary(true);
            constraint.getColumns().forEach(column -> markColumn(columns, column, Db2ColumnDomain::setPrimary));
        } else if (constraint.getType() == SqlConstraintType.Unique) {
            table.setHasUnique(true);
            constraint.getColumns().forEach(column -> markColumn(columns, column, Db2ColumnDomain::setUnique));
        } else if (constraint.getType() == SqlConstraintType.ForeignKey) {
            table.setHasForeignKey(true);
            constraint.getColumns().forEach(column -> markColumn(columns, column, Db2ColumnDomain::setForeign));
        }
    }

    private void markColumn(Map<String, Db2ColumnDomain> columns, String column, ColumnFlag flag) {
        Db2ColumnDomain domain = columns.get(column);
        if (domain != null) {
            flag.set(domain, true);
        }
    }

    private void markWhere(RdbWhereDomain domain, Db2SqlParser.Where_clauseContext where) {
        if (where == null) {
            return;
        }
        domain.setHasWhere(true);
        domain.setSelectInWhere(hasDescendant(where, Db2SqlParser.Select_statementContext.class) || hasDescendant(where, Db2SqlParser.FullselectContext.class));
        for (Db2SqlParser.Column_nameContext column : descendants(where, Db2SqlParser.Column_nameContext.class)) {
            domain.addWhereColumn(lastName(text(column)));
        }
    }

    private void addJoinType(Db2SelectDomain domain, Db2SqlParser.Join_clauseContext join) {
        RdbJoinType type = RdbJoinType.INNER_JOIN;
        if (join.CROSS() != null) {
            type = RdbJoinType.CROSS_JOIN;
        } else if (hasTerminal(join, Db2SqlParser.LEFT)) {
            type = RdbJoinType.LEFT_JOIN;
        } else if (hasTerminal(join, Db2SqlParser.RIGHT)) {
            type = RdbJoinType.RIGHT_JOIN;
        }
        if (!hasJoinType(domain, type)) {
            domain.getJoinTypes().add(type);
        }
        domain.setJoinType(type);
    }

    private boolean hasJoinType(Db2SelectDomain domain, RdbJoinType type) {
        for (RdbJoinType joinType : domain.getJoinTypes()) {
            if (joinType == type) {
                return true;
            }
        }
        return false;
    }

    private List<String> insertColumns(Db2SqlParser.Insert_statementContext ctx) {
        if (ctx.column_name_list_paren() != null) {
            return names(ctx.column_name_list_paren().column_name_list());
        }
        if (ctx.column_name_list() != null) {
            return names(ctx.column_name_list());
        }
        return new ArrayList<>();
    }

    private List<String> assignmentColumns(Db2SqlParser.Assignment_clauseContext ctx) {
        List<String> result = new ArrayList<>();
        if (ctx == null) {
            return result;
        }
        for (Db2SqlParser.Assignment_itemContext item : ctx.assignment_item()) {
            if (item.column_name() != null) {
                result.add(lastName(text(item.column_name())));
            } else if (item.column_name_list_paren() != null) {
                result.addAll(names(item.column_name_list_paren().column_name_list()));
            }
        }
        return result;
    }

    private List<String> names(Db2SqlParser.Column_name_listContext ctx) {
        List<String> result = new ArrayList<>();
        if (ctx == null) {
            return result;
        }
        for (Db2SqlParser.Column_nameContext column : ctx.column_name()) {
            result.add(lastName(text(column)));
        }
        return result;
    }

    private List<String> names(Db2SqlParser.Index_col_optsContext ctx) {
        List<String> result = new ArrayList<>();
        if (ctx == null) {
            return result;
        }
        for (Db2SqlParser.Index_col_opts_itemContext item : ctx.index_col_opts_item()) {
            if (item.column_name() != null) {
                result.add(lastName(text(item.column_name())));
            } else if (item.key_expression() != null) {
                result.add(text(item.key_expression()));
            }
        }
        return result;
    }

    private boolean hasFunctionLikeExpression(ParserRuleContext ctx) {
        return hasTerminal(ctx, Db2SqlParser.LEFT_RND_BKT);
    }

    private boolean hasTerminal(ParseTree tree, int tokenType) {
        Queue<ParseTree> queue = new ArrayDeque<>();
        queue.add(tree);
        while (!queue.isEmpty()) {
            ParseTree current = queue.remove();
            if (current instanceof TerminalNode terminal && terminal.getSymbol().getType() == tokenType) {
                return true;
            }
            for (int i = 0; i < current.getChildCount(); i++) {
                queue.add(current.getChild(i));
            }
        }
        return false;
    }

    private <T extends ParserRuleContext> boolean hasDescendant(ParserRuleContext ctx, Class<T> type) {
        return !descendants(ctx, type).isEmpty();
    }

    private <T extends ParserRuleContext> List<T> descendants(ParserRuleContext ctx, Class<T> type) {
        List<T> result = new ArrayList<>();
        Queue<ParseTree> queue = new ArrayDeque<>();
        queue.add(ctx);
        while (!queue.isEmpty()) {
            ParseTree current = queue.remove();
            if (type.isInstance(current)) {
                result.add(type.cast(current));
            }
            for (int i = 0; i < current.getChildCount(); i++) {
                queue.add(current.getChild(i));
            }
        }
        return result;
    }

    private String firstIdentifier(String value) {
        if (value == null) {
            return null;
        }
        int start = 0;
        while (start < value.length() && Character.isWhitespace(value.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < value.length()) {
            char ch = value.charAt(end);
            if (Character.isWhitespace(ch) || ch == ',' || ch == '(' || ch == ')') {
                break;
            }
            end++;
        }
        return clean(value.substring(start, end));
    }

    private String firstTerminalText(ParserRuleContext ctx) {
        Queue<ParseTree> queue = new ArrayDeque<>();
        queue.add(ctx);
        while (!queue.isEmpty()) {
            ParseTree current = queue.remove();
            if (current instanceof TerminalNode terminal) {
                return terminal.getText().toLowerCase(Locale.ROOT);
            }
            for (int i = 0; i < current.getChildCount(); i++) {
                queue.add(current.getChild(i));
            }
        }
        return null;
    }

    private String commentText(Db2SqlParser.String_constantContext ctx) {
        return ctx == null ? null : unquote(text(ctx));
    }

    private String commentText(ParserRuleContext ctx) {
        return ctx == null ? null : unquote(text(ctx));
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }
        String result = value.trim();
        while (result.length() >= 2 && isWrapped(result)) {
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }

    private boolean isWrapped(String value) {
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        return first == '"' && last == '"' || first == '[' && last == ']';
    }

    private String unquote(String value) {
        String result = clean(value);
        if (result != null && result.length() >= 2 && result.charAt(0) == '\'' && result.charAt(result.length() - 1) == '\'') {
            return result.substring(1, result.length() - 1);
        }
        return result;
    }

    private NameParts parts(String rawName) {
        List<String> names = new ArrayList<>();
        if (rawName != null) {
            int start = 0;
            for (int i = 0; i <= rawName.length(); i++) {
                if (i == rawName.length() || rawName.charAt(i) == '.') {
                    String cleaned = clean(rawName.substring(start, i));
                    if (cleaned != null && !cleaned.isBlank()) {
                        names.add(cleaned);
                    }
                    start = i + 1;
                }
            }
        }
        if (names.size() >= 3) {
            return new NameParts(names.get(names.size() - 3), names.get(names.size() - 2), null, names.get(names.size() - 1));
        }
        if (names.size() == 2) {
            return new NameParts(null, names.get(0), null, names.get(1));
        }
        if (names.size() == 1) {
            return new NameParts(null, null, null, names.get(0));
        }
        return new NameParts(null, null, null, null);
    }

    private String lastName(String rawName) {
        return parts(rawName).name;
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private void add(RuleDomain domain) {
        if (domain.getAuditKind() == null) {
            domain.setAuditKind(domain.resolveAuditKind());
        }
        builder.addDomain(domain);
    }

    private interface ColumnFlag {
        void set(Db2ColumnDomain domain, boolean value);
    }

    private record NameParts(String catalog, String schema, String table, String name) {
    }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql92.analysis.security;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.sql.common.antlr.AntlrAstUtils;
import com.clougence.sql.iso.sql92.analysis.security.builder.Sql92DomainCollector;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92ParserBaseVisitor;

public class Sql92SqlParserVisitor extends Sql92ParserBaseVisitor<Void> {

    private final Sql92DomainCollector collector;

    public Sql92SqlParserVisitor(Sql92DomainCollector collector){
        this.collector = collector;
    }

    @Override
    public Void visitSchemaDefinition(Sql92Parser.SchemaDefinitionContext ctx) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        domain.setSchema(clean(ctx.schemaName()));
        add(domain, SecQueryType.CREATE_SCHEMA);
        return null;
    }

    @Override
    public Void visitDropSchemaStatement(Sql92Parser.DropSchemaStatementContext ctx) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        domain.setSchema(clean(ctx.schemaName()));
        add(domain, SecQueryType.DROP_SCHEMA);
        return null;
    }

    @Override
    public Void visitTableDefinition(Sql92Parser.TableDefinitionContext ctx) {
        RdbTableDomain domain = tableDomain(ctx.tableName());
        add(domain, SecQueryType.CREATE_TABLE);
        return null;
    }

    @Override
    public Void visitAlterTableStatement(Sql92Parser.AlterTableStatementContext ctx) {
        RdbTableDomain domain = tableDomain(ctx.tableName());
        add(domain, SecQueryType.ALTER_TABLE);
        return null;
    }

    @Override
    public Void visitDropTableStatement(Sql92Parser.DropTableStatementContext ctx) {
        RdbTableDomain domain = tableDomain(ctx.tableName());
        add(domain, SecQueryType.DROP_TABLE);
        return null;
    }

    @Override
    public Void visitViewDefinition(Sql92Parser.ViewDefinitionContext ctx) {
        RdbViewDomain domain = viewDomain(ctx.tableName());
        add(domain, SecQueryType.CREATE_VIEW);
        return null;
    }

    @Override
    public Void visitDropViewStatement(Sql92Parser.DropViewStatementContext ctx) {
        RdbViewDomain domain = viewDomain(ctx.tableName());
        add(domain, SecQueryType.DROP_VIEW);
        return null;
    }

    @Override
    public Void visitDirectSelectStatement_MultipleRows(Sql92Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectStatement_SingleRow(Sql92Parser.SelectStatement_SingleRowContext ctx) {
        add(selectDomain(ctx.selectList(), ctx.tableExpression()), SecQueryType.SELECT);
        return null;
    }

    @Override
    public Void visitQuerySpecification(Sql92Parser.QuerySpecificationContext ctx) {
        add(selectDomain(ctx.selectList(), ctx.tableExpression()), SecQueryType.SELECT);
        return null;
    }

    @Override
    public Void visitInsertStatement(Sql92Parser.InsertStatementContext ctx) {
        RdbInsertDomain domain = new RdbInsertDomain();
        NameParts name = nameParts(ctx.tableName());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SecQueryType.INSERT);
        return null;
    }

    @Override
    public Void visitUpdateStatement_Searched(Sql92Parser.UpdateStatement_SearchedContext ctx) {
        RdbUpdateDomain domain = new RdbUpdateDomain();
        NameParts name = nameParts(ctx.tableName());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SecQueryType.UPDATE);
        return null;
    }

    @Override
    public Void visitUpdateStatement_Positioned(Sql92Parser.UpdateStatement_PositionedContext ctx) {
        RdbUpdateDomain domain = new RdbUpdateDomain();
        NameParts name = nameParts(ctx.tableName());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SecQueryType.UPDATE);
        return null;
    }

    @Override
    public Void visitDeleteStatement_Searched(Sql92Parser.DeleteStatement_SearchedContext ctx) {
        RdbDeleteDomain domain = new RdbDeleteDomain();
        NameParts name = nameParts(ctx.tableName());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SecQueryType.DELETE);
        return null;
    }

    @Override
    public Void visitDeleteStatement_Positioned(Sql92Parser.DeleteStatement_PositionedContext ctx) {
        RdbDeleteDomain domain = new RdbDeleteDomain();
        NameParts name = nameParts(ctx.tableName());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SecQueryType.DELETE);
        return null;
    }

    @Override
    public Void visitGrantStatement(Sql92Parser.GrantStatementContext ctx) {
        add(new RdbGrantDomain(), SecQueryType.GRANT);
        return null;
    }

    @Override
    public Void visitRevokeStatement(Sql92Parser.RevokeStatementContext ctx) {
        add(new RdbRevokeDomain(), SecQueryType.REVOKE);
        return null;
    }

    @Override
    public Void visitCommitStatement(Sql92Parser.CommitStatementContext ctx) {
        add(new RdbResourceDomain(), SecQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitRollbackStatement(Sql92Parser.RollbackStatementContext ctx) {
        add(new RdbResourceDomain(), SecQueryType.TRANSACTION);
        return null;
    }

    protected RdbSelectDomain selectDomain(Sql92Parser.SelectListContext selectList, Sql92Parser.TableExpressionContext tableExpression) {
        RdbSelectDomain domain = new RdbSelectDomain();
        domain.setMode(RdbQueryMode.NORMAL);
        domain.setEmptyFrom(tableExpression.fromClause() == null);
        for (RdbTableDomain tableDomain : tableDomains(tableExpression.fromClause())) {
            domain.addChild(tableDomain);
        }
        domain.getColumns().addAll(queryItems(selectList));
        return domain;
    }

    protected List<RdbTableDomain> tableDomains(Sql92Parser.FromClauseContext ctx) {
        List<RdbTableDomain> domains = new ArrayList<>();
        if (ctx == null) {
            return domains;
        }
        for (Sql92Parser.CrossJoinContext crossJoin : ctx.crossJoin()) {
            collectTables(crossJoin, domains);
        }
        return domains;
    }

    protected List<QueryItem> queryItems(Sql92Parser.SelectListContext ctx) {
        List<QueryItem> items = new ArrayList<>();
        if (ctx.selectSublist().isEmpty()) {
            QueryItem item = new QueryItem();
            item.setSelectAll(true);
            items.add(item);
            return items;
        }
        for (Sql92Parser.SelectSublistContext selectSublist : ctx.selectSublist()) {
            QueryItem item = new QueryItem();
            if (selectSublist.qualifier() != null) {
                item.setSelectAll(true);
                item.setTable(identifier(selectSublist.qualifier()));
            } else {
                fillDerivedColumn(item, selectSublist.derivedColumn());
            }
            items.add(item);
        }
        return items;
    }

    protected List<RdbTableDomain> resourceTableDomains(Sql92Parser.QuerySpecificationContext ctx) {
        List<RdbTableDomain> domains = tableDomains(ctx.tableExpression().fromClause());
        for (RdbTableDomain domain : domains) {
            domain.setSqlType(SecQueryType.SELECT);
            domain.setAuditKind(SecQueryType.SELECT.getAuditKind());
        }
        return domains;
    }

    private void collectTables(Sql92Parser.CrossJoinContext ctx, List<RdbTableDomain> domains) {
        if (ctx.tableName() != null) {
            RdbTableDomain domain = tableDomain(ctx.tableName());
            if (ctx.correlationSpecification() != null) {
                domain.setAlias(identifier(ctx.correlationSpecification().correlationName()));
            }
            domains.add(domain);
        }
        for (Sql92Parser.CrossJoinContext child : ctx.crossJoin()) {
            collectTables(child, domains);
        }
    }

    private void fillDerivedColumn(QueryItem item, Sql92Parser.DerivedColumnContext ctx) {
        if (ctx.asClause() != null) {
            item.setItemAlias(identifier(ctx.asClause().columnName()));
        }
        List<Sql92Parser.ColumnReferenceContext> columns = AntlrAstUtils.descendants(ctx.valueExpression(), Sql92Parser.ColumnReferenceContext.class);
        for (Sql92Parser.ColumnReferenceContext column : columns) {
            RdbColumnDomain columnDomain = new RdbColumnDomain();
            NameParts name = columnNameParts(column);
            columnDomain.setSchema(name.schema);
            columnDomain.setTable(name.table);
            columnDomain.setColumn(name.name);
            item.addDomain(columnDomain);
        }
        if (columns.isEmpty() && fillSingleIdentifierColumn(item, ctx.valueExpression())) {
        } else if (columns.size() == 1) {
            NameParts name = columnNameParts(columns.get(0));
            item.setTable(name.table);
            item.setColumn(name.name);
            if (item.getItemAlias() == null) {
                item.setItemAlias(name.name);
            }
        } else if (item.getItemAlias() == null) {
            item.setColumn(ctx.valueExpression().getText());
        }
    }

    private boolean fillSingleIdentifierColumn(QueryItem item, ParserRuleContext ctx) {
        List<String> identifiers = AntlrAstUtils.identifiers(ctx, Sql92Parser.IDENTIFIER);
        if (identifiers.size() != 1) {
            return false;
        }
        String column = identifiers.get(0);
        RdbColumnDomain columnDomain = new RdbColumnDomain();
        columnDomain.setColumn(column);
        item.addDomain(columnDomain);
        item.setColumn(column);
        if (item.getItemAlias() == null) {
            item.setItemAlias(column);
        }
        return true;
    }

    private RdbTableDomain tableDomain(ParserRuleContext ctx) {
        RdbTableDomain domain = new RdbTableDomain();
        NameParts name = nameParts(ctx);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        return domain;
    }

    private RdbViewDomain viewDomain(ParserRuleContext ctx) {
        RdbViewDomain domain = new RdbViewDomain();
        NameParts name = nameParts(ctx);
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setView(name.name);
        return domain;
    }

    private void add(RuleDomain domain, SecQueryType type) {
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        collector.add(domain);
    }

    private static NameParts nameParts(ParserRuleContext ctx) {
        List<String> parts = AntlrAstUtils.identifiers(ctx, Sql92Parser.IDENTIFIER);
        if (parts.size() >= 3) {
            return new NameParts(parts.get(parts.size() - 3), parts.get(parts.size() - 2), parts.get(parts.size() - 1));
        } else if (parts.size() == 2) {
            return new NameParts(null, parts.get(0), parts.get(1));
        } else if (parts.size() == 1) {
            return new NameParts(null, null, parts.get(0));
        } else {
            return new NameParts(null, null, ctx.getText());
        }
    }

    private static String clean(ParserRuleContext ctx) {
        return identifier(ctx);
    }

    private static NameParts columnNameParts(Sql92Parser.ColumnReferenceContext ctx) {
        NameParts column = nameParts(ctx.columnName());
        if (ctx.qualifier() == null) {
            return column;
        }
        NameParts qualifier = nameParts(ctx.qualifier());
        return new NameParts(qualifier.catalog, qualifier.schema, qualifier.name, column.name);
    }

    private static String identifier(ParserRuleContext ctx) {
        List<String> ids = AntlrAstUtils.identifiers(ctx, Sql92Parser.IDENTIFIER);
        if (ids.isEmpty()) {
            return ctx.getText();
        }
        return ids.get(ids.size() - 1);
    }

    private record NameParts(String catalog, String schema, String table, String name) {

        private NameParts(String catalog, String schema, String name){
            this(catalog, schema, null, name);
        }
    }
}

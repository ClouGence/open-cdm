/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003.analysis.security;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.antlr.AntlrAstUtils;
import com.clougence.sql.iso.sql2003.analysis.security.builder.Sql2003DomainCollector;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003ParserBaseVisitor;

public class Sql2003SqlParserVisitor extends Sql2003ParserBaseVisitor<Void> {

    private final Sql2003DomainCollector collector;

    public Sql2003SqlParserVisitor(Sql2003DomainCollector collector){
        this.collector = collector;
    }

    @Override
    public Void visitSchemaDefinition(Sql2003Parser.SchemaDefinitionContext ctx) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        domain.setSchema(clean(ctx.schemaName()));
        add(domain, SplitQueryType.CREATE_SCHEMA);
        return null;
    }

    @Override
    public Void visitDropSchemaStatement(Sql2003Parser.DropSchemaStatementContext ctx) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        domain.setSchema(clean(ctx.schemaName()));
        add(domain, SplitQueryType.DROP_SCHEMA);
        return null;
    }

    @Override
    public Void visitTableDefinition(Sql2003Parser.TableDefinitionContext ctx) {
        RdbTableDomain domain = tableDomain(ctx.tableName());
        add(domain, SplitQueryType.CREATE_TABLE);
        return null;
    }

    @Override
    public Void visitAlterTableStatement(Sql2003Parser.AlterTableStatementContext ctx) {
        RdbTableDomain domain = tableDomain(ctx.tableName());
        add(domain, SplitQueryType.ALTER_TABLE);
        return null;
    }

    @Override
    public Void visitDropTableStatement(Sql2003Parser.DropTableStatementContext ctx) {
        RdbTableDomain domain = tableDomain(ctx.tableName());
        add(domain, SplitQueryType.DROP_TABLE);
        return null;
    }

    @Override
    public Void visitViewDefinition(Sql2003Parser.ViewDefinitionContext ctx) {
        RdbViewDomain domain = viewDomain(ctx.tableName());
        add(domain, SplitQueryType.CREATE_VIEW);
        return null;
    }

    @Override
    public Void visitDropViewStatement(Sql2003Parser.DropViewStatementContext ctx) {
        RdbViewDomain domain = viewDomain(ctx.tableName());
        add(domain, SplitQueryType.DROP_VIEW);
        return null;
    }

    @Override
    public Void visitDirectSelectStatement_MultipleRows(Sql2003Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectStatement_SingleRow(Sql2003Parser.SelectStatement_SingleRowContext ctx) {
        add(selectDomain(ctx.selectList(), ctx.tableExpression()), SplitQueryType.SELECT);
        return null;
    }

    @Override
    public Void visitQuerySpecification(Sql2003Parser.QuerySpecificationContext ctx) {
        add(selectDomain(ctx.selectList(), ctx.tableExpression()), SplitQueryType.SELECT);
        return null;
    }

    @Override
    public Void visitInsertStatement(Sql2003Parser.InsertStatementContext ctx) {
        RdbInsertDomain domain = new RdbInsertDomain();
        NameParts name = nameParts(ctx.insertionTarget());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SplitQueryType.INSERT);
        return null;
    }

    @Override
    public Void visitUpdateStatement_Searched(Sql2003Parser.UpdateStatement_SearchedContext ctx) {
        RdbUpdateDomain domain = new RdbUpdateDomain();
        NameParts name = nameParts(ctx.targetTable());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SplitQueryType.UPDATE);
        return null;
    }

    @Override
    public Void visitUpdateStatement_Positioned(Sql2003Parser.UpdateStatement_PositionedContext ctx) {
        RdbUpdateDomain domain = new RdbUpdateDomain();
        NameParts name = nameParts(ctx.targetTable());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SplitQueryType.UPDATE);
        return null;
    }

    @Override
    public Void visitDeleteStatement_Searched(Sql2003Parser.DeleteStatement_SearchedContext ctx) {
        RdbDeleteDomain domain = new RdbDeleteDomain();
        NameParts name = nameParts(ctx.targetTable());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SplitQueryType.DELETE);
        return null;
    }

    @Override
    public Void visitDeleteStatement_Positioned(Sql2003Parser.DeleteStatement_PositionedContext ctx) {
        RdbDeleteDomain domain = new RdbDeleteDomain();
        NameParts name = nameParts(ctx.targetTable());
        domain.setCatalog(name.catalog);
        domain.setSchema(name.schema);
        domain.setTable(name.name);
        add(domain, SplitQueryType.DELETE);
        return null;
    }

    @Override
    public Void visitGrantStatement(Sql2003Parser.GrantStatementContext ctx) {
        add(new RdbGrantDomain(), SplitQueryType.GRANT);
        return null;
    }

    @Override
    public Void visitRevokeStatement(Sql2003Parser.RevokeStatementContext ctx) {
        add(new RdbRevokeDomain(), SplitQueryType.REVOKE);
        return null;
    }

    @Override
    public Void visitCommitStatement(Sql2003Parser.CommitStatementContext ctx) {
        add(new RdbResourceDomain(), SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitRollbackStatement(Sql2003Parser.RollbackStatementContext ctx) {
        add(new RdbResourceDomain(), SplitQueryType.TRANSACTION);
        return null;
    }

    protected RdbSelectDomain selectDomain(Sql2003Parser.SelectListContext selectList, Sql2003Parser.TableExpressionContext tableExpression) {
        RdbSelectDomain domain = new RdbSelectDomain();
        domain.setMode(RdbQueryMode.NORMAL);
        domain.setEmptyFrom(tableExpression.fromClause() == null);
        for (RdbTableDomain tableDomain : tableDomains(tableExpression.fromClause())) {
            domain.addChild(tableDomain);
        }
        domain.getColumns().addAll(queryItems(selectList));
        return domain;
    }

    protected List<RdbTableDomain> tableDomains(Sql2003Parser.FromClauseContext ctx) {
        List<RdbTableDomain> domains = new ArrayList<>();
        if (ctx == null || ctx.tableReferenceList() == null) {
            return domains;
        }
        for (Sql2003Parser.CrossJoinContext crossJoin : ctx.tableReferenceList().crossJoin()) {
            collectTables(crossJoin, domains);
        }
        return domains;
    }

    protected List<QueryItem> queryItems(Sql2003Parser.SelectListContext ctx) {
        List<QueryItem> items = new ArrayList<>();
        if (ctx.selectSublist().isEmpty()) {
            QueryItem item = new QueryItem();
            item.setSelectAll(true);
            items.add(item);
            return items;
        }
        for (Sql2003Parser.SelectSublistContext selectSublist : ctx.selectSublist()) {
            QueryItem item = new QueryItem();
            if (selectSublist.qualifiedAsterisk() != null) {
                item.setSelectAll(true);
                item.setTable(identifier(selectSublist.qualifiedAsterisk()));
            } else {
                fillDerivedColumn(item, selectSublist.derivedColumn());
            }
            items.add(item);
        }
        return items;
    }

    protected List<RdbTableDomain> resourceTableDomains(Sql2003Parser.QuerySpecificationContext ctx) {
        List<RdbTableDomain> domains = tableDomains(ctx.tableExpression().fromClause());
        for (RdbTableDomain domain : domains) {
            domain.addSqlType(SplitQueryType.SELECT);
            domain.setAuditKind(SplitQueryType.SELECT.getAuditKind());
        }
        return domains;
    }

    private void collectTables(Sql2003Parser.CrossJoinContext ctx, List<RdbTableDomain> domains) {
        if (ctx.tableOrQueryName() != null) {
            RdbTableDomain domain = tableDomain(ctx.tableOrQueryName());
            if (ctx.correlationName() != null) {
                domain.setAlias(identifier(ctx.correlationName()));
            }
            domains.add(domain);
        }
    }

    private void fillDerivedColumn(QueryItem item, Sql2003Parser.DerivedColumnContext ctx) {
        if (ctx.asClause() != null) {
            item.setItemAlias(identifier(ctx.asClause().columnName()));
        }
        List<Sql2003Parser.ColumnReferenceContext> columns = AntlrAstUtils.descendants(ctx, Sql2003Parser.ColumnReferenceContext.class);
        for (Sql2003Parser.ColumnReferenceContext column : columns) {
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
            item.setColumn(ctx.getChild(0).getText());
        }
    }

    private boolean fillSingleIdentifierColumn(QueryItem item, ParserRuleContext ctx) {
        List<String> identifiers = AntlrAstUtils.identifiers(ctx, Sql2003Parser.IDENTIFIER);
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

    private void add(RuleDomain domain, SplitQueryType type) {
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        collector.add(domain);
    }

    private static NameParts nameParts(ParserRuleContext ctx) {
        List<String> parts = AntlrAstUtils.identifiers(ctx, Sql2003Parser.IDENTIFIER);
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

    private static NameParts columnNameParts(Sql2003Parser.ColumnReferenceContext ctx) {
        List<String> parts = AntlrAstUtils.identifiers(ctx, Sql2003Parser.IDENTIFIER);
        if (parts.size() >= 3) {
            return new NameParts(null, parts.get(parts.size() - 3), parts.get(parts.size() - 2), parts.get(parts.size() - 1));
        } else if (parts.size() == 2) {
            return new NameParts(null, null, parts.get(0), parts.get(1));
        } else if (parts.size() == 1) {
            return new NameParts(null, null, null, parts.get(0));
        } else {
            return new NameParts(null, null, null, ctx.getText());
        }
    }

    private static String identifier(ParserRuleContext ctx) {
        List<String> ids = AntlrAstUtils.identifiers(ctx, Sql2003Parser.IDENTIFIER);
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

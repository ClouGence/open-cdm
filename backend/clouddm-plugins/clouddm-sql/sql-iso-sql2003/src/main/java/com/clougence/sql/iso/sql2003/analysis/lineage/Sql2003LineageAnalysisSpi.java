/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003.analysis.lineage;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;
import com.clougence.sql.iso.sql2003.analysis.security.Sql2003SqlParserVisitor;
import com.clougence.sql.iso.sql2003.analysis.security.builder.Sql2003DomainCollector;
import com.clougence.sql.iso.sql2003.parser.Sql2003DslProvider;
import com.clougence.sql.iso.sql2003.parser.Sql2003SplitAnalysisSpi;

public class Sql2003LineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    public Sql2003LineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return Sql2003DslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(Sql2003DomainCollector collector, Parser parser) {
        return new Sql2003SqlParserVisitor(collector);
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public List<LineageColumn> analyze(String sql, LineageContext lineageContext) {
        try (var scripts = new Sql2003SplitAnalysisSpi().splitScriptStream(new StringReader(sql), List.of(), 1, 0)) {
            var iterator = scripts.iterator();
            if (!iterator.hasNext()) {
                return List.of();
            }
            iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalArgumentException("Lineage analysis supports at most one SQL statement");
            }
        }

        return analyzeStatement(new StringReader(sql), lineageContext);
    }

    private List<LineageColumn> analyzeStatement(Reader sql, LineageContext lineageContext) {
        Sql2003DomainCollector collector = new Sql2003DomainCollector();
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> parserVisitor(collector, parser));

        return toResultColumns(analyzeColumns(lineageContext.getUserUID(), lineageContext.getDsId(), lineageContext.getLevelsParam(), collector.build()));
    }
}

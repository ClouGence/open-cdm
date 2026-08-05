/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003.analysis.lineage;

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

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
    public Stream<LineageColumn> analyzeStream(Reader sql, LineageContext lineageContext) {
        return analyzeMaterialized(sql, lineageContext).stream();
    }

    private List<LineageColumn> analyzeMaterialized(Reader sql, LineageContext lineageContext) {
        Sql2003DomainCollector collector = new Sql2003DomainCollector();
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> parserVisitor(collector, parser));
        return toResultColumns(analyzeColumns(lineageContext.getUserUID(), lineageContext.getDsId(), lineageContext.getLevelsParam(), collector.build()));
    }
}

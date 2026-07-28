/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql92.analysis.lineage;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.ColumnLineage;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;
import com.clougence.sql.iso.sql92.analysis.security.Sql92SqlParserVisitor;
import com.clougence.sql.iso.sql92.analysis.security.builder.Sql92DomainCollector;
import com.clougence.sql.iso.sql92.parser.Sql92DslProvider;

public class Sql92LineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    public Sql92LineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return Sql92DslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(Sql92DomainCollector collector, Parser parser) {
        return new Sql92SqlParserVisitor(collector);
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public List<ColumnLineage> analyze(String sql, LineageContext lineageContext) {
        Sql92DomainCollector collector = new Sql92DomainCollector();
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> parserVisitor(collector, parser));
        return toResultColumns(analyzeColumns(lineageContext.getUserUID(), lineageContext.getDsId(), lineageContext.getLevelsParam(), collector.build()));
    }
}

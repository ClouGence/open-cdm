/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql99.analysis.column;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.analysis.column.AbstractSelectColumnAnalysisSpi;
import com.clougence.sql.iso.sql99.analysis.security.Sql99SqlParserVisitor;
import com.clougence.sql.iso.sql99.analysis.security.builder.Sql99DomainCollector;
import com.clougence.sql.iso.sql99.parser.Sql99DslProvider;

public class Sql99SelectColumnAnalysisSpi extends AbstractSelectColumnAnalysisSpi {

    public Sql99SelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return Sql99DslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(Sql99DomainCollector collector, Parser parser) {
        return new Sql99SqlParserVisitor(collector);
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        Sql99DomainCollector collector = new Sql99DomainCollector();
        DslHelper.doVisitor(dslProvider(), script, (lexer, parser) -> parserVisitor(collector, parser));
        return analyzeRealColumn(contextInfo.getCuid(), contextInfo.getDsId(), contextInfo.getLevelsParam(), collector.build());
    }
}

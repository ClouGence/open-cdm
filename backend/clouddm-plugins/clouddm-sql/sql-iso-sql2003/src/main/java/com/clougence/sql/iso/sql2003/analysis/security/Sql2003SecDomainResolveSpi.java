/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003.analysis.security;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.iso.sql2003.analysis.security.builder.Sql2003DomainCollector;
import com.clougence.sql.iso.sql2003.parser.Sql2003DslProvider;
import com.clougence.sql.iso.sql2003.parser.Sql2003SplitAnalysisSpi;

public class Sql2003SecDomainResolveSpi implements SecDomainResolveSpi {

    public Sql2003SecDomainResolveSpi(MetaService metaService){
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader queryReader, int baseLine, int baseColumn, ContextInfo ctxInfo) {
        var scripts = new Sql2003SplitAnalysisSpi().splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return resolveStatement(dsType, reader, codeLine, codeColumn, ctxInfo).stream();
        }).onClose(scripts::close);
    }

    private List<RuleDomain> resolveStatement(DataSourceType dsType, Reader queryReader, int baseLine, int baseColumn, ContextInfo ctxInfo) {
        List<RuleDomain> domainList = new ArrayList<>();
        List<AstSplitScript> scripts = DslHelper.splitDsl(Sql2003DslProvider.INSTANCE, queryReader);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            Sql2003DomainCollector collector = new Sql2003DomainCollector();
            new Sql2003SqlParserVisitor(collector).visit(s.getAstTree());
            for (RuleDomain domain : collector.build()) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }
        return domainList;
    }
}

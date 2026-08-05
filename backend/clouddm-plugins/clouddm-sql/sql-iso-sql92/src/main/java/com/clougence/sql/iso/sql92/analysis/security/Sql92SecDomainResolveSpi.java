/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql92.analysis.security;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.iso.sql92.analysis.security.builder.Sql92DomainCollector;
import com.clougence.sql.iso.sql92.parser.Sql92DslProvider;

public class Sql92SecDomainResolveSpi implements SecDomainResolveSpi {

    public Sql92SecDomainResolveSpi(MetaService metaService){
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        return resolveDomainMaterialized(dsType, queryReader, codeInfo, ctxInfo).stream();
    }

    private List<RuleDomain> resolveDomainMaterialized(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        List<RuleDomain> domainList = new ArrayList<>();
        List<AstSplitScript> scripts = DslHelper.splitDsl(Sql92DslProvider.INSTANCE, queryReader);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            Sql92DomainCollector collector = new Sql92DomainCollector();
            new Sql92SqlParserVisitor(collector).visit(s.getAstTree());
            for (RuleDomain domain : collector.build()) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }
        return domainList;
    }
}

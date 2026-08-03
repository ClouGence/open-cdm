/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql99.analysis.security;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.iso.sql99.analysis.security.builder.Sql99DomainCollector;
import com.clougence.sql.iso.sql99.parser.Sql99DslProvider;

public class Sql99SecDomainResolveSpi implements SecDomainResolveSpi {

    public Sql99SecDomainResolveSpi(MetaService metaService){
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        List<RuleDomain> domainList = new ArrayList<>();
        List<AstSplitScript> scripts = DslHelper.splitDsl(Sql99DslProvider.INSTANCE, queryReader);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            Sql99DomainCollector collector = new Sql99DomainCollector();
            new Sql99SqlParserVisitor(collector).visit(s.getAstTree());
            for (RuleDomain domain : collector.build()) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }
        return domainList;
    }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis.security;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.sql.hana.analysis.HanaSqlAnalyzer;
import com.clougence.sql.hana.parser.HanaSplitAnalysisSpi;

import java.io.Reader;
import java.util.stream.Stream;

public class HanaSecDomainResolveSpi implements SecDomainResolveSpi {
    private final MetaService metaService;

    public HanaSecDomainResolveSpi(){
        this(null);
    }

    public HanaSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader reader, int baseLine, int baseColumn, ContextInfo context) {
        if (metaService == null) {
            return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn)
                .flatMap(script -> HanaSqlAnalyzer.analyze(script, context.getLevelsParam()).getDomains().stream())
                .peek(domain -> domain.setDsType(dsType));
        }

        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn)
            .flatMap(script -> HanaSqlAnalyzer
                .analyze(script, context.getLevelsParam(), (levels, index) -> metaService.fetchIndexedObject(context.getCuid(), context.getDsId(), levels, index))
                .getDomains()
                .stream())
            .peek(domain -> domain.setDsType(dsType));
    }
}

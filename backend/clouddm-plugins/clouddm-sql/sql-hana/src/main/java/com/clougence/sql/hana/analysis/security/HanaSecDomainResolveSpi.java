/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis.security;

import java.io.Reader;
import java.util.stream.Stream;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.sql.hana.analysis.HanaSqlAnalyzer;
import com.clougence.sql.hana.parser.HanaSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;

public class HanaSecDomainResolveSpi implements SecDomainResolveSpi {
    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader reader, int baseLine, int baseColumn, ContextInfo context) {
        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn)
            .flatMap(script -> HanaSqlAnalyzer.analyze(script, context.getLevelsParam()).getDomains().stream())
            .peek(domain -> domain.setDsType(dsType));
    }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql92;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.iso.sql92.analysis.behavior.Sql92BehaviorAnalysisSpi;
import com.clougence.sql.iso.sql92.analysis.lineage.Sql92LineageAnalysisSpi;
import com.clougence.sql.iso.sql92.analysis.security.Sql92SecDomainResolveSpi;
import com.clougence.sql.iso.sql92.parser.Sql92DslProvider;
import com.clougence.sql.iso.sql92.parser.Sql92SplitAnalysisSpi;

public class Sql92SqlEngineSpi implements SqlEngineSpi {
    public static final String        NAME = "ISO-SQL-92";

    private final SplitAnalysisSpi    splitAnalysisSpi;
    private final SecDomainResolveSpi secDomainResolveSpi;
    private final BehaviorAnalysisSpi behaviorAnalysisSpi;
    private final LineageAnalysisSpi  lineageAnalysisSpi;
    private final RewriteSpi          rewriteSpi;

    public Sql92SqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new Sql92SplitAnalysisSpi();
        this.secDomainResolveSpi = new Sql92SecDomainResolveSpi(metaService);
        this.behaviorAnalysisSpi = new Sql92BehaviorAnalysisSpi();
        this.lineageAnalysisSpi = new Sql92LineageAnalysisSpi(metaService);
        this.rewriteSpi = null;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return Sql92DslProvider.INSTANCE;
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        return splitAnalysisSpi;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return secDomainResolveSpi;
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        return behaviorAnalysisSpi;
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        return lineageAnalysisSpi;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return rewriteSpi;
    }
}

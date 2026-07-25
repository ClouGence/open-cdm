/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql99;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.iso.sql99.analysis.behavior.Sql99BehaviorAnalysisSpi;
import com.clougence.sql.iso.sql99.analysis.column.Sql99SelectColumnAnalysisSpi;
import com.clougence.sql.iso.sql99.analysis.security.Sql99SecDomainResolveSpi;
import com.clougence.sql.iso.sql99.parser.Sql99DslProvider;
import com.clougence.sql.iso.sql99.parser.Sql99SplitAnalysisSpi;

public class Sql99SqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "ISO-SQL-99";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final BehaviorAnalysisSpi     behaviorAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    public Sql99SqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new Sql99SplitAnalysisSpi();
        this.secDomainResolveSpi = new Sql99SecDomainResolveSpi(metaService);
        this.behaviorAnalysisSpi = new Sql99BehaviorAnalysisSpi();
        this.selectColumnAnalysisSpi = new Sql99SelectColumnAnalysisSpi(metaService);
        this.rewriteSpi = null;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return Sql99DslProvider.INSTANCE;
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
    public SelectColumnAnalysisSpi selectColumnAnalysisSpi(SqlParserParameters parameters) {
        return selectColumnAnalysisSpi;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return rewriteSpi;
    }
}

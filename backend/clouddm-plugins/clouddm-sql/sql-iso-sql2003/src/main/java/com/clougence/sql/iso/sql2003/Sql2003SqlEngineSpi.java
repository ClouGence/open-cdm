/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.iso.sql2003.analysis.behavior.Sql2003BehaviorAnalysisSpi;
import com.clougence.sql.iso.sql2003.analysis.security.Sql2003SecDomainResolveSpi;
import com.clougence.sql.iso.sql2003.parser.Sql2003DslProvider;
import com.clougence.sql.iso.sql2003.parser.Sql2003SplitAnalysisSpi;

public class Sql2003SqlEngineSpi implements SqlEngineSpi {
    public static final String        NAME = "ISO-SQL-2003";

    private final SplitAnalysisSpi    splitAnalysisSpi;
    private final SecDomainResolveSpi secDomainResolveSpi;
    private final BehaviorAnalysisSpi behaviorAnalysisSpi;
    private final LineageAnalysisSpi  lineageAnalysisSpi;
    private final RewriteSpi          rewriteSpi;

    public Sql2003SqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new Sql2003SplitAnalysisSpi();
        this.secDomainResolveSpi = new Sql2003SecDomainResolveSpi(metaService);
        this.behaviorAnalysisSpi = new Sql2003BehaviorAnalysisSpi();
        this.lineageAnalysisSpi = LineageAnalysisSpi.EMPTY;
        this.rewriteSpi = null;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return Sql2003DslProvider.INSTANCE;
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

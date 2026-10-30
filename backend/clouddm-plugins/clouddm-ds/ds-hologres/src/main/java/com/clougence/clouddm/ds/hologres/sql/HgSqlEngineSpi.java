/* Copyright 2026 杭州开云集致科技有限公司. Licensed under the Apache License, Version 2.0. */
package com.clougence.clouddm.ds.hologres.sql;

import com.clougence.clouddm.ds.hologres.sql.analysis.HgBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.hologres.sql.analysis.HgSecDomainResolveSpi;
import com.clougence.clouddm.ds.hologres.sql.parser.HgSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.sql.postgres.PgSqlEngineSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class HgSqlEngineSpi extends PgSqlEngineSpi {
    public static final String NAME = "Hologres SQL";
    private final MetaService  metaService;

    public HgSqlEngineSpi(MetaService metaService){
        super(metaService);
        this.metaService = metaService;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected PostgresVersion resolveVersion(SqlParserParameters parameters) {
        // Hologres uses PG 11; PG 12 is the oldest shared grammar level, not the Hologres product version.
        return PostgresVersion.POSTGRES_12;
    }

    @Override
    protected SplitAnalysisSpi newSplitAnalysisSpi(PostgresVersion version) {
        return new HgSplitAnalysisSpi(version);
    }

    @Override
    protected BehaviorAnalysisSpi newBehaviorAnalysisSpi(PostgresVersion version) {
        return new HgBehaviorAnalysisSpi(version);
    }

    @Override
    protected SecDomainResolveSpi newSecDomainResolveSpi(PostgresVersion version) {
        return new HgSecDomainResolveSpi(metaService, version);
    }
}

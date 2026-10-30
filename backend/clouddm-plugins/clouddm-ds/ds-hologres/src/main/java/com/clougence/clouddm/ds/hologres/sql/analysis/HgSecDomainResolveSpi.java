/* Copyright 2026 杭州开云集致科技有限公司. Licensed under the Apache License, Version 2.0. */
package com.clougence.clouddm.ds.hologres.sql.analysis;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.postgres.analysis.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.analysis.security.builder.PgBuilderFactory;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class HgSecDomainResolveSpi extends PgSecDomainResolveSpi {
    public HgSecDomainResolveSpi(MetaService metaService, PostgresVersion version){
        super(metaService, version);
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory builder, Parser parser) {
        return new HgSecDomainParserVisitor(builder, parser);
    }
}

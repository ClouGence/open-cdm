package com.clougence.clouddm.ds.hologres.analysis;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class HgSecDomainResolveSpi extends PgSecDomainResolveSpi {

    public HgSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory domainBuilder, Parser parser) {
        return new HgSQLParserVisitor(domainBuilder, parser);
    }
}

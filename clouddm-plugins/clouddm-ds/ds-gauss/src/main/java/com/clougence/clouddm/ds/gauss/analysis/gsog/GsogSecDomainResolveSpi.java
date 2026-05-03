package com.clougence.clouddm.ds.gauss.analysis.gsog;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.gauss.parser.GaussDslProvider;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.DslProvider;

public class GsogSecDomainResolveSpi extends PgSecDomainResolveSpi {

    public GsogSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory domainBuilder, Parser parser) {
        return new GsogSQLParserVisitor(domainBuilder, parser);
    }
}

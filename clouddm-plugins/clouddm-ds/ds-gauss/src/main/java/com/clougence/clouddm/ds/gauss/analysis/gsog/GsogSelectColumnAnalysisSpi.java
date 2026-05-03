package com.clougence.clouddm.ds.gauss.analysis.gsog;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.gauss.analysis.gs.GsSQLParserVisitor;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.gauss.parser.GaussDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class GsogSelectColumnAnalysisSpi extends PgSelectColumnAnalysisSpi {

    public GsogSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory domainBuilder, Parser parser) {
        return new GsSQLParserVisitor(domainBuilder, parser);
    }
}

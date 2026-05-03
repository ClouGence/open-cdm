package com.clougence.clouddm.ds.polardb.analysis.porx;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.polardb.parser.porx.PolarXDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class PorXSelectColumnAnalysisSpi extends MySelectColumnAnalysisSpi {

    public PorXSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return PolarXDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new PorXSqlParserVisitor(domainBuilder, parser);
    }
}

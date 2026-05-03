package com.clougence.clouddm.ds.ads.analysis.ads4my;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.ads.parser.ads4my.AdsMyDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class AdsMySelectColumnAnalysisSpi extends MySelectColumnAnalysisSpi {

    public AdsMySelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return AdsMyDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new AdsMyParserVisitor(domainBuilder, parser);
    }
}

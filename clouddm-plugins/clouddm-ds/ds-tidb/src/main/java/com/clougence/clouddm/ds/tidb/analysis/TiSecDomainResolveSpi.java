package com.clougence.clouddm.ds.tidb.analysis;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.tidb.parser.TiDBDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class TiSecDomainResolveSpi extends MySecDomainResolveSpi {

    public TiSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return TiDBDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new TiParserVisitor(domainBuilder, parser);
    }
}

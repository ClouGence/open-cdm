package com.clougence.clouddm.ds.oceanbase.analysis.obforora;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.builder.OraBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.oceanbase.parser.ob4ora.ObOraDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class ObForOraSecDomainResolveSpi extends OraSecDomainResolveSpi {

    public ObForOraSecDomainResolveSpi(MetaService metaService){

        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return ObOraDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(OraBuilderFactory domainBuilder, Parser parser) {
        return new ObForOracleSqlParserVisitor(domainBuilder, parser);
    }
}

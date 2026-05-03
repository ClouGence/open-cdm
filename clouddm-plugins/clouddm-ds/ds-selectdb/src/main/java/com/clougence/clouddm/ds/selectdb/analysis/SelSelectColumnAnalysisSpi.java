package com.clougence.clouddm.ds.selectdb.analysis;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.doris.analysis.DrSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSqlParserVisitor;
import com.clougence.clouddm.ds.doris.analysis.builder.DrBuilderFactory;
import com.clougence.clouddm.ds.doris.parser.DrDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.DslProvider;

public class SelSelectColumnAnalysisSpi extends DrSelectColumnAnalysisSpi {

    public SelSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return DrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(DrBuilderFactory domainBuilder, Parser parser) {
        return new DrSqlParserVisitor(domainBuilder, parser);
    }

}

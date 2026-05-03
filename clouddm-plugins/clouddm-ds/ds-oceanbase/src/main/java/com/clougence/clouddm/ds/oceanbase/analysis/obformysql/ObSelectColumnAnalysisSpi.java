package com.clougence.clouddm.ds.oceanbase.analysis.obformysql;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.ObMyDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class ObSelectColumnAnalysisSpi extends MySelectColumnAnalysisSpi {

    public ObSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return ObMyDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new ObMyParserVisitor(domainBuilder, parser);
    }
}

package com.clougence.clouddm.ds.oceanbase.analysis.obformysql;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.ObMyDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class ObSplitAnalysisSpi extends MySplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return ObMyDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return ObSplitVisitor.INSTANCE; }
}

package com.clougence.clouddm.ds.gauss.analysis.gs;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.gauss.parser.GaussDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class GsSplitAnalysisSpi extends PgSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return GsSplitVisitor.INSTANCE; }
}

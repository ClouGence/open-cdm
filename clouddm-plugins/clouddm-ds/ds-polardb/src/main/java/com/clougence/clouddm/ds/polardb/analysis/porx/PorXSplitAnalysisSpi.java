package com.clougence.clouddm.ds.polardb.analysis.porx;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.polardb.parser.porx.PolarXDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class PorXSplitAnalysisSpi extends MySplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return PolarXDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return PorXSplitVisitor.INSTANCE; }
}

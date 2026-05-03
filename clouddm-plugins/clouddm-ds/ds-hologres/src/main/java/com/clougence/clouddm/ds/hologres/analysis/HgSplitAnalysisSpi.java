package com.clougence.clouddm.ds.hologres.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class HgSplitAnalysisSpi extends PgSplitAnalysisSpi {

    @Override
    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return HgSplitVisitor.INSTANCE; }
}

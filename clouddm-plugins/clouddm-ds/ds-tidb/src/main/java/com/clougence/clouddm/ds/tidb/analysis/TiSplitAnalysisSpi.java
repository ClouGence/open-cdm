package com.clougence.clouddm.ds.tidb.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.tidb.parser.TiDBDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class TiSplitAnalysisSpi extends MySplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return TiDBDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return TiSplitVisitor.INSTANCE; }
}

package com.clougence.clouddm.ds.oceanbase.analysis.obforora;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.oceanbase.parser.ob4ora.ObOraDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class ObForOraSplitAnalysisSpi extends OraSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return ObOraDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return ObForOracleSplitVisitor.INSTANCE; }
}

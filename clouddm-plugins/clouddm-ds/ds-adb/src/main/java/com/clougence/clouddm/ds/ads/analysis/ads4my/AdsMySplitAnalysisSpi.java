package com.clougence.clouddm.ds.ads.analysis.ads4my;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.ads.parser.ads4my.AdsMyDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class AdsMySplitAnalysisSpi extends MySplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return AdsMyDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return AdsMySplitVisitor.INSTANCE; }
}

package com.clougence.clouddm.ds.gauss.analysis.gs;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class GsSplitVisitor extends GsAbstractSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new GsSplitVisitor();

}

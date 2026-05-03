package com.clougence.clouddm.ds.gauss.analysis.gsog;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class GsogSplitVisitor extends GsogAbstractSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new GsogSplitVisitor();

}

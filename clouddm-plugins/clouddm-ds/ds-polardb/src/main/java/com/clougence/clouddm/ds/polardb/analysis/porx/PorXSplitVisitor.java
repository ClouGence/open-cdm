package com.clougence.clouddm.ds.polardb.analysis.porx;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class PorXSplitVisitor extends AbstractPorXSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new PorXSplitVisitor();
}

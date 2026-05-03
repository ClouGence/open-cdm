package com.clougence.clouddm.ds.oceanbase.analysis.obforora;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class ObForOracleSplitVisitor extends AbstractObForOraSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new ObForOracleSplitVisitor();
}

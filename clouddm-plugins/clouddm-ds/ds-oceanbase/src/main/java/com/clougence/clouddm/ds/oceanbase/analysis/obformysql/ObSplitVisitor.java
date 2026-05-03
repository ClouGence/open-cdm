package com.clougence.clouddm.ds.oceanbase.analysis.obformysql;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class ObSplitVisitor extends AbstractObSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new ObSplitVisitor();
}

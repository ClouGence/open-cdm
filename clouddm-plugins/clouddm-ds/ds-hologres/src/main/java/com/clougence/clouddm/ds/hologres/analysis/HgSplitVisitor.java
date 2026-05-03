package com.clougence.clouddm.ds.hologres.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitVisitor;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class HgSplitVisitor extends PgSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new HgSplitVisitor();

}

package com.clougence.clouddm.ds.tidb.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.tidb.parser.antlr.TiDBParser;

public class TiSplitVisitor extends AbstractTiSplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new TiSplitVisitor();

    @Override
    public SecQueryType visitCreateSequence(TiDBParser.CreateSequenceContext ctx) {
        return SecQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SecQueryType visitDropSequence(TiDBParser.DropSequenceContext ctx) {
        return SecQueryType.DROP_SEQUENCE;
    }
}

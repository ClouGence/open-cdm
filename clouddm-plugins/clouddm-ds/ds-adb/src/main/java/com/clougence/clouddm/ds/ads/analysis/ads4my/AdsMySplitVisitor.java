package com.clougence.clouddm.ds.ads.analysis.ads4my;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.ads.parser.ads4my.antlr.AdsMyParser;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class AdsMySplitVisitor extends AbstractAdsMySplitVisitor {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new AdsMySplitVisitor();

    @Override
    public SecQueryType visitAnalyzeTable(AdsMyParser.AnalyzeTableContext ctx) {
        return SecQueryType.ANALYZE;
    }

    @Override
    public SecQueryType visitAdbExternalTable(AdsMyParser.AdbExternalTableContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitLoadDataStatement(AdsMyParser.LoadDataStatementContext ctx) {
        return SecQueryType.LOAD;
    }
}

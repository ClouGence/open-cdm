package com.clougence.clouddm.ds.mongodb.analysis;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.mongodb.parser.MongoDslProvider;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AstSplitScript;

public class MongoSplitAnalysisSpi implements SplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return MongoDslProvider.INSTANCE;
    }

    //    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return MongoSplitVisitor.INSTANCE; }

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseLine, int baseColumn) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase =//
                new com.clougence.dslpaser.ast.location.CodeLocation(baseLine, baseColumn);

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), script, dslBase);
        List<SplitScript> result = new ArrayList<>();
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            StatementSet statementSet = DslHelper.parserDsl(dslProvider(), s.getScript());
            for (Statement statement : statementSet.getStatements()) {
                AbstractMongoFunc mongoFunc = (AbstractMongoFunc) statement;

                SecQueryType type = MongoAnalysisHelper.convert(mongoFunc.getFuncType());
                ss.setType(type);
            }

            result.add(ss);
        }
        return result;
    }
}

package com.clougence.clouddm.ds.oracle.analysis;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.oracle.parser.OraDslProvider;
import com.clougence.clouddm.ds.oracle.parser.antlr.PlSqlParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AstSplitScript;

public class OraSplitAnalysisSpi implements SplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return OraDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return OraSplitVisitor.INSTANCE; }

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseLine, int baseColumn) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase = new com.clougence.dslpaser.ast.location.CodeLocation(baseLine, baseColumn);

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), script, dslBase);
        List<SplitScript> result = new ArrayList<>();
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            SecQueryType type = this.getSplitVisitor().visit(s.getAstTree());
            if (type == null) {
                ss.setType(SecQueryType.UNKNOWN);
            } else {
                ss.setType(type);
            }
            // begin ...  end
            if (s.getAstTree().getChild(0) instanceof PlSqlParser.Anonymous_blockContext) {
                ss.setScript(ss.getScript() + ";");
            }

            result.add(ss);
        }
        return result;
    }
}

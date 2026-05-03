package com.clougence.clouddm.ds.maxcompute.analysis;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.maxcompute.parser.McSqlDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;

public class McSplitAnalysisSpi implements SplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return McSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SecQueryType> getSplitVisitor() { return McSplitVisitor.INSTANCE; }

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseLine, int baseColumn) {
        CodeLocation dslBase = new CodeLocation(baseLine, baseColumn);

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
            result.add(ss);
        }
        return result;
    }
}

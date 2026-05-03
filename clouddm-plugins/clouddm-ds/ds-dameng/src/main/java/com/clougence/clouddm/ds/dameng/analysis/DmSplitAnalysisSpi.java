package com.clougence.clouddm.ds.dameng.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public class DmSplitAnalysisSpi implements SplitAnalysisSpi {

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {

        if (StringUtils.isBlank(script)) {
            return Collections.emptyList();
        } else {
            StringBuilder sb = fillQueryOffset(baseCodeLine, baseCodeColumn);
            String[] split = script.split(";");
            List<SplitScript> result = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isNotBlank(s)) {
                    SplitScript script1 = new SplitScript();
                    script1.setScript(sb.toString() + s);
                    script1.setType(SecQueryType.UNKNOWN);
                    result.add(script1);
                }
                fillBlankString(sb, s);
            }
            return result;

        }
    }

    private StringBuilder fillQueryOffset(int baseCodeLine, int baseCodeColumn) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < baseCodeLine; i++) {
            sb.append("\n");
        }
        for (int i = 1; i < baseCodeColumn; i++) {
            sb.append(" ");
        }
        return sb;
    }

    private void fillBlankString(StringBuilder sb, String s) {
        for (char c : s.toCharArray()) {
            if (c == '\n') {
                sb.append("\n");
            } else {
                sb.append(" ");
            }
        }
    }
}

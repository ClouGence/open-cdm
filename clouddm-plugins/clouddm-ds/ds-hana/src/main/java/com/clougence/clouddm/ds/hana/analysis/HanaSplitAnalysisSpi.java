package com.clougence.clouddm.ds.hana.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public class HanaSplitAnalysisSpi implements SplitAnalysisSpi {

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        if (StringUtils.isBlank(script)) {
            return Collections.emptyList();
        } else {
            return Arrays.stream(script.split(";")).filter(StringUtils::isNotBlank).map(s -> {
                SplitScript script1 = new SplitScript();
                script1.setScript(s);
                script1.setType(SecQueryType.UNKNOWN);
                return script1;
            }).collect(Collectors.toList());
        }
    }
}

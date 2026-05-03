package com.clougence.clouddm.sdk.analysis.split;

import java.util.List;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.QueryArg;

@FunctionalInterface
public interface SplitAnalysisSpi extends Spi {

    List<SplitScript> splitScript(String script, List<QueryArg> args, int baseCodeLine, int baseCodeColumn);
}

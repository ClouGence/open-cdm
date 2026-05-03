package com.clougence.clouddm.sdk.analysis.column;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;

public interface SelectColumnAnalysisSpi extends Spi {

    default List<SelectItem> parseSelectColumn(String script, ContextInfo info) {
        return Collections.emptyList();
    }

    boolean supportParseSelectColumn();
}

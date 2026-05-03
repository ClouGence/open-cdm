package com.clougence.clouddm.ds.sqlserver.analysis;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;

public class MsSelectColumnAnalysisSpi implements SelectColumnAnalysisSpi {

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        return Collections.emptyList();
    }

    @Override
    public boolean supportParseSelectColumn() {
        return false;
    }
}

package com.clougence.clouddm.ds.mongodb.analysis;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MongoSelectColumnAnalysisSpi implements SelectColumnAnalysisSpi {

    public MongoSelectColumnAnalysisSpi(MetaService metaService){
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo info) {
        return Collections.emptyList(); // TODO 需要根据具体的Redis命令，返回对应的列信息
    }

    @Override
    public boolean supportParseSelectColumn() {
        return true;
    }
}

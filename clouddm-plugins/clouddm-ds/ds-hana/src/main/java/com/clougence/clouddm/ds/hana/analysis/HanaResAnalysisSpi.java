package com.clougence.clouddm.ds.hana.analysis;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class HanaResAnalysisSpi implements ResAnalysisSpi {

    @Override
    public Map<RuleDomain, List<ResObject>> analysisResource(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo, Map<String, Object> ctx) {
        return Collections.emptyMap();
    }
}

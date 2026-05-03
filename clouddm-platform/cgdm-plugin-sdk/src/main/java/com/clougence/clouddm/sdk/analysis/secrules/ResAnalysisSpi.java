package com.clougence.clouddm.sdk.analysis.secrules;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

public interface ResAnalysisSpi extends Spi {

    Map<RuleDomain, List<ResObject>> analysisResource(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo, Map<String, Object> ctx);
}

package com.clougence.clouddm.console.web.service.analysis;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface QueryAnalysisService {

    List<SplitScript> analysisSplit(DataSourceType dsType, String queryString, List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn);

    Map<RuleDomain, List<ResObject>> analysisResourceV2(DataSourceConfig dataSourceConfig, String queryString, Map<String, Object> levels);
}

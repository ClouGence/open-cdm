/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.service.analysis;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
@Service
public class QueryAnalysisServiceImpl implements QueryAnalysisService {

    @Override
    public List<SplitScript> analysisSplit(DataSourceType dsType, String queryString, List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn) {
        SplitAnalysisSpi analysisSpi = PluginManager.findSplitAnalysisSpi(dsType);
        if (analysisSpi == null) {
            return Collections.singletonList(new SplitScript(SecQueryType.UNKNOWN, queryString, queryArgs));
        }

        List<SplitScript> scripts = analysisSpi.splitScript(queryString, queryArgs, baseCodeLine, baseCodeColumn);
        if (CollectionUtils.isEmpty(scripts)) {
            throw new IllegalStateException(dsType + " invoker SplitAnalysisSpi failed, result is empty.");
        } else {
            return scripts;
        }
    }

    @Override
    public Map<RuleDomain, List<ResObject>> analysisResourceV2(DataSourceConfig dataSourceConfig, String queryString, Map<String, Object> levels) {
        ResAnalysisSpi analysisSpi = PluginManager.findResourceAnalysisSpi(dataSourceConfig.getDataSourceType());
        if (analysisSpi == null) {
            return Collections.emptyMap();
        }

        CodeInfo codeInfo = CodeInfo.builder().baseLine(1).baseColumn(0).query(queryString).build();
        ContextInfo contextInfo = ContextInfo.builder().deepParser(false).dataSourceConfig(dataSourceConfig).build();
        Map<RuleDomain, List<ResObject>> scripts = analysisSpi.analysisResource(dataSourceConfig.getDataSourceType(), codeInfo, contextInfo, levels);
        if (CollectionUtils.isEmpty(scripts)) {
            return Collections.emptyMap();
        } else {
            return scripts;
        }
    }
}

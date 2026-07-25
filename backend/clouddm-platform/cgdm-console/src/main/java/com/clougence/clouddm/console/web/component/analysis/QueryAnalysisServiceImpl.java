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
package com.clougence.clouddm.console.web.component.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
@Service
public class QueryAnalysisServiceImpl implements QueryAnalysisService {

    @Resource
    private DmDsConfigService             dmDsConfigService;
    private final ResourceActionConverter converter = new ResourceActionConverter();

    @Override
    public List<SplitScript> analysisSplit(DataSourceConfig dsConfig, String queryString, List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn) {
        SqlEngineSpi sqlEngine = this.dmDsConfigService.fetchSqlEngineSpi(dsConfig);
        SqlParserParameters parameters = this.dmDsConfigService.fetchSqlParserParameters(dsConfig, Collections.emptyMap());
        SplitAnalysisSpi analysisSpi = sqlEngine.splitAnalysisSpi(parameters);

        List<SplitScript> scripts = analysisSpi.splitScript(queryString, queryArgs, baseCodeLine, baseCodeColumn);
        if (CollectionUtils.isEmpty(scripts)) {
            throw new IllegalStateException(dsConfig.getDataSourceType() + " invoker SplitAnalysisSpi failed, result is empty.");
        } else {
            return scripts;
        }
    }

    @Override
    public List<ResourceAction> analysisResource(DataSourceConfig dsConfig, String queryString, Map<UmiTypes, Object> levels, int baseCodeLine, int baseCodeColumn) {
        if (dsConfig == null) {
            throw new IllegalArgumentException("DataSourceConfig is required for resource analysis.");
        }

        Map<UmiTypes, Object> safeLevels = levels == null ? Collections.emptyMap() : levels;
        SqlEngineSpi sqlEngine = this.dmDsConfigService.fetchSqlEngineSpi(dsConfig);
        if (sqlEngine == null) {
            throw new IllegalStateException(dsConfig.getDataSourceType() + " has no SqlEngineSpi.");
        }

        SqlParserParameters parameters = this.dmDsConfigService.fetchSqlParserParameters(dsConfig, safeLevels);
        BehaviorAnalysisSpi analysisSpi = sqlEngine.behaviorAnalysisSpi(parameters);
        if (analysisSpi == null) {
            throw new IllegalStateException(dsConfig.getDataSourceType() + " does not support BehaviorAnalysisSpi.");
        }

        List<StatementBehavior> behaviors = analysisSpi.analysisBehavior(queryString, safeLevels, baseCodeLine, baseCodeColumn);
        return this.converter.convert(behaviors, currentResourcePath(safeLevels), instanceResourcePath(safeLevels));
    }

    private String currentResourcePath(Map<UmiTypes, Object> levels) {
        return resourcePath(levels, List.of(UmiTypes.Instance, UmiTypes.Catalog, UmiTypes.Schema));
    }

    private String instanceResourcePath(Map<UmiTypes, Object> levels) {
        return resourcePath(levels, List.of(UmiTypes.Instance));
    }

    private String resourcePath(Map<UmiTypes, Object> levels, List<UmiTypes> types) {
        List<String> nodes = new ArrayList<>();
        for (UmiTypes type : types) {
            Object value = levels.get(type);
            if (value == null) {
                continue;
            }
            for (String node : StringUtils.toString(value).split("/")) {
                if (StringUtils.isNotBlank(node)) {
                    nodes.add(node);
                }
            }
        }
        return nodes.isEmpty() ? "/" : "/" + String.join("/", nodes) + "/";
    }
}

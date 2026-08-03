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

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface QueryAnalysisService {
    Stream<SplitScript> analysisSplitStream(DataSourceConfig dsConfig, Reader reader, List<QueryArg> queryArgs,//
                                            int baseCodeLine, int baseCodeColumn);

    Stream<QueryRequest> analysisRequestsStream(DataSourceConfig dsConfig, Reader reader, List<QueryArg> queryArgs,//
                                                int baseCodeLine, int baseCodeColumn, AnalysisQueryOptions options);

    Stream<SecRulesCheckResult> analysisRulesStream(DataSourceConfig dsConfig, Reader reader, List<QueryArg> queryArgs,//
                                                    int baseCodeLine, int baseCodeColumn, AnalysisRuleOptions options);
}

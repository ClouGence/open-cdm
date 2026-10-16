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
package com.clougence.sql.es;

import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;

public class EsSecDomainResolveSpi implements SecDomainResolveSpi {
    private final SqlParserParameters parameters;

    public EsSecDomainResolveSpi(SqlParserParameters parameters){
        this.parameters = parameters;
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader reader, int baseLine, int baseColumn, ContextInfo context) {
        var scripts = new EsSplitAnalysisSpi(parameters).splitScriptStream(reader, List.of(), baseLine, baseColumn);
        return scripts.<RuleDomain> map(script -> resolve(dsType, script)).onClose(scripts::close);
    }

    private EsRequestDomain resolve(DataSourceType dsType, SplitScript script) {
        EsRequest request = new EsRequestParser(parameters).parse(script.getScript());
        EsRequestAnalysis analysis = EsRequestPolicy.analyze(request);
        EsRequestDomain domain = new EsRequestDomain();
        domain.setSplitScript(script);
        domain.setDsType(dsType);
        domain.setMethod(request.method());
        domain.setPath(request.path());
        domain.setIndex(analysis.index().name());
        domain.setIndices(analysis.indices());
        domain.setOperation(analysis.operation().name());

        Map<String, String> options = new LinkedHashMap<>();
        for (EsQueryParameter parameter : request.parameters()) {
            options.put(parameter.name(), parameter.value());
        }
        boolean hasQuery = options.containsKey("q");
        if (request.body() != null) {
            for (EsJsonMember member : request.body().members()) {
                if (member.name().value().equals("query")) {
                    hasQuery = true;
                    break;
                }
            }
        }
        domain.setHasQuery(hasQuery);
        domain.setOptions(options);
        RuleQueryType type = switch (analysis.operation()) {
            case SEARCH, COUNT, KNN_SEARCH, READ_DOCUMENT -> RuleQueryType.SELECT;
            case READ_INDEX -> RuleQueryType.READ;
            case INSERT_DOCUMENT -> RuleQueryType.INSERT;
            case REPLACE_DOCUMENT -> RuleQueryType.REPLACE;
            case UPDATE_DOCUMENT, UPDATE_BY_QUERY -> RuleQueryType.UPDATE;
            case DELETE_DOCUMENT, DELETE_BY_QUERY -> RuleQueryType.DELETE;
            case CREATE_INDEX -> RuleQueryType.CREATE_TABLE;
            case DROP_INDEX -> RuleQueryType.DROP_TABLE;
            case PUT_MAPPING, PUT_SETTINGS -> RuleQueryType.ALTER_TABLE;
        };
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }
}

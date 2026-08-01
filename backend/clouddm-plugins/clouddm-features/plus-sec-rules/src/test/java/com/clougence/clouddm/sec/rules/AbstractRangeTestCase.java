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
package com.clougence.clouddm.sec.rules;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.sql.mysql.analysis.security.MySecDomainResolveSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.utils.CollectionUtils;

public class AbstractRangeTestCase {

    protected MySecDomainResolveSpi     resolveSpi     = new MySecDomainResolveSpi(null, MySqlParserConfig.unknownSqlMode(null));
    protected DataSourceType            dataSourceType = DataSourceType.MySQL;
    protected final Map<String, Object> ctx            = CollectionUtils.asMap(//
            SessionSpi.PARAMS_DEFAULT_DB, "test_db",//
            SessionSpi.PARAMS_DEFAULT_SCHEMA, "test_schema");

    protected List<RuleDomain> resolveDomain(String sql) {
        CodeInfo codeInfo = CodeInfo.builder().baseLine(0).baseColumn(0).build();
        try (StringReader reader = new StringReader(sql)) {
            return this.resolveSpi.resolveDomain(dataSourceType, reader, codeInfo, ContextInfo.builder().build());
        }
    }

    protected List<RuleDomain> configDsAndEnv(long envId, long dsId, List<RuleDomain> domainList) {
        for (RuleDomain domain : domainList) {
            domain.setEnvId(envId);
            domain.setDsId(dsId);
        }
        return domainList;
    }
}

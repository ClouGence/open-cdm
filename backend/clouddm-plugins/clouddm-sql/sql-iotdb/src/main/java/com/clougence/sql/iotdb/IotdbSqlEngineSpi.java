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

package com.clougence.sql.iotdb;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.QueryResourceResolver;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

public class IotdbSqlEngineSpi implements SqlEngineSpi {

    public static final String NAME        = "IoTDB SQL";

    static final String        SQL_DIALECT = "sql_dialect";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        return new IotdbSplitAnalysisSpi(provider(SqlParserParameters.nullToEmpty(parameters).get(SQL_DIALECT)));
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        return new IotdbBehaviorAnalysisSpi(provider(SqlParserParameters.nullToEmpty(parameters).get(SQL_DIALECT)));
    }

    @Override
    public QueryResourceResolver queryResourceResolver() {
        return new IotdbQueryResourceResolver();
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return provider(SqlParserParameters.nullToEmpty(parameters).get(SQL_DIALECT));
    }

    static IotdbDslProvider provider(String model) {
        if (model == null || model.isBlank() || model.equalsIgnoreCase("TREE")) {
            return IotdbDslProvider.TREE;
        }
        if (model.equalsIgnoreCase("TABLE")) {
            return IotdbDslProvider.TABLE;
        }
        throw ThirdPartyApiException.as().with("Invalid IoTDB SQL model");
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        return null;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return null;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return null;
    }
}

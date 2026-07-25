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
package com.clougence.clouddm.ds.dameng.sql;

import com.clougence.clouddm.ds.dameng.sql.analysis.behavior.DmBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.dameng.sql.analysis.column.DmSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.dameng.sql.analysis.security.DmSecDomainResolveSpi;
import com.clougence.clouddm.ds.dameng.sql.parser.DmDslProvider;
import com.clougence.clouddm.ds.dameng.sql.parser.DmSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

public class DmSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "Dameng SQL";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final BehaviorAnalysisSpi     behaviorAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;

    public DmSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new DmSplitAnalysisSpi();
        this.secDomainResolveSpi = new DmSecDomainResolveSpi(metaService);
        this.behaviorAnalysisSpi = new DmBehaviorAnalysisSpi();
        this.selectColumnAnalysisSpi = new DmSelectColumnAnalysisSpi();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return DmDslProvider.INSTANCE;
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        return splitAnalysisSpi;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return secDomainResolveSpi;
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        return behaviorAnalysisSpi;
    }

    @Override
    public SelectColumnAnalysisSpi selectColumnAnalysisSpi(SqlParserParameters parameters) {
        return selectColumnAnalysisSpi;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return null;
    }
}

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
package com.clougence.clouddm.ds.greenplum.sql;

import com.clougence.clouddm.ds.greenplum.sql.analysis.behavior.GpBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.greenplum.sql.analysis.security.GpSecDomainResolveSpi;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpDslProvider;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.postgres.AbstractPgSqlEngineSpi;
import com.clougence.sql.postgres.editor.rewrite.PgRewriteSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** Greenplum uses its PostgreSQL kernel version, with Greenplum-owned built-ins. */
public class GpSqlEngineSpi extends AbstractPgSqlEngineSpi {

    public static final String NAME = "Greenplum SQL";
    private final MetaService  metaService;

    public GpSqlEngineSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected PostgresVersion resolveVersion(SqlParserParameters parameters) {
        return PostgresVersion.parse(parameters.version());
    }

    @Override
    protected DslProvider newDslProvider(PostgresVersion version) {
        return new GpDslProvider(version);
    }

    @Override
    protected SplitAnalysisSpi newSplitAnalysisSpi(PostgresVersion version) {
        return new GpSplitAnalysisSpi(version);
    }

    @Override
    protected SecDomainResolveSpi newSecDomainResolveSpi(PostgresVersion version) {
        return new GpSecDomainResolveSpi(metaService, version);
    }

    @Override
    protected BehaviorAnalysisSpi newBehaviorAnalysisSpi(PostgresVersion version) {
        return new GpBehaviorAnalysisSpi(version);
    }

    @Override
    protected RewriteSpi newRewriteSpi(PostgresVersion version) {
        return new PgRewriteSpi(new GpDslProvider(version));
    }
}

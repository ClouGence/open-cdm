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
package com.clougence.clouddm.ds.cloudberry.sql;

import com.clougence.clouddm.ds.cloudberry.sql.analysis.behavior.CbBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.cloudberry.sql.analysis.security.CbSecDomainResolveSpi;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbDslProvider;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSplitAnalysisSpi;
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

/** Cloudberry uses its PostgreSQL kernel version, with Cloudberry-owned built-ins. */
public class CbSqlEngineSpi extends AbstractPgSqlEngineSpi {

    public static final String NAME = "Cloudberry SQL";
    private final MetaService  metaService;

    public CbSqlEngineSpi(MetaService metaService){
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
        return new CbDslProvider(version);
    }

    @Override
    protected SplitAnalysisSpi newSplitAnalysisSpi(PostgresVersion version) {
        return new CbSplitAnalysisSpi(version);
    }

    @Override
    protected SecDomainResolveSpi newSecDomainResolveSpi(PostgresVersion version) {
        return new CbSecDomainResolveSpi(metaService, version);
    }

    @Override
    protected BehaviorAnalysisSpi newBehaviorAnalysisSpi(PostgresVersion version) {
        return new CbBehaviorAnalysisSpi(version);
    }

    @Override
    protected RewriteSpi newRewriteSpi(PostgresVersion version) {
        return new PgRewriteSpi(new CbDslProvider(version));
    }
}

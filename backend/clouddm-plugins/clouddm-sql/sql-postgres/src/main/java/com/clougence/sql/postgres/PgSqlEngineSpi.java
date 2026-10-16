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
package com.clougence.sql.postgres;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.analysis.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.editor.rewrite.PgRewriteSpi;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** @author mode */
public class PgSqlEngineSpi extends AbstractPgSqlEngineSpi {

    public static final String NAME = "PG SQL";

    private final MetaService  metaService;

    public PgSqlEngineSpi(MetaService metaService){
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
        return new PgDslProvider(version, NAME);
    }

    @Override
    protected SplitAnalysisSpi newSplitAnalysisSpi(PostgresVersion version) {
        return new PgSplitAnalysisSpi(version);
    }

    @Override
    protected SecDomainResolveSpi newSecDomainResolveSpi(PostgresVersion version) {
        return new PgSecDomainResolveSpi(metaService, version);
    }

    @Override
    protected BehaviorAnalysisSpi newBehaviorAnalysisSpi(PostgresVersion version) {
        return new PgBehaviorAnalysisSpi(version);
    }

    @Override
    protected RewriteSpi newRewriteSpi(PostgresVersion version) {
        return new PgRewriteSpi(version);
    }
}

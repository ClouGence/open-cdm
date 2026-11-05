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
package com.clougence.clouddm.ds.greenplum.sql.analysis.behavior;

import java.util.Map;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.greenplum.sql.analysis.reference.GpResourceRegistry;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpDslProvider;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpSplitAnalysisSpi;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpSplitVisitor;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpStatementParser;
import com.clougence.clouddm.ds.greenplum.sql.parser.antlr.GpSqlParser;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class GpBehaviorAnalysisSpi extends PgBehaviorAnalysisSpi {

    public GpBehaviorAnalysisSpi(PostgresVersion version){
        this(new GpDslProvider(version), version);
    }

    private GpBehaviorAnalysisSpi(GpDslProvider provider, PostgresVersion version){
        super(provider, new GpSplitAnalysisSpi(version), GpResourceRegistry.instance()::contains, GpResourceRegistry.instance()::pgFunctionBehavior, GpSplitVisitor::new);
    }

    @Override
    protected StatementBehavior analyzeExtensionStatement(Parser parser, PgSqlParser.ExtensionstmtContext extension, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        GpSqlParser.StatementContext statement = GpStatementParser.parse(parser.getTokenStream(), extension);
        GpStatementBehaviorVisitor visitor = new GpStatementBehaviorVisitor(GpSplitAnalysisSpi.classify(statement), levels, baseLine, baseColumn);
        visitor.visit(statement);
        return visitor.behavior();
    }
}

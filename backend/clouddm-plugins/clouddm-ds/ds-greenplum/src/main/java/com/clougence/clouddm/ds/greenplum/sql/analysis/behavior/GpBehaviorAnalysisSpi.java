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

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.greenplum.sql.parser.GpDslProvider;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpSplitAnalysisSpi;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpSplitVisitor;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpStatementParser;
import com.clougence.clouddm.ds.greenplum.sql.parser.antlr.GpSqlParser;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.analysis.reference.PgFunctionBehavior;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class GpBehaviorAnalysisSpi extends PgBehaviorAnalysisSpi {

    public GpBehaviorAnalysisSpi(PostgresVersion version){
        this(new GpDslProvider(version), version);
    }

    private GpBehaviorAnalysisSpi(GpDslProvider provider, PostgresVersion version){
        super(provider, new GpSplitAnalysisSpi(version), GpBehaviorAnalysisSpi::isGreenplumFunction, GpBehaviorAnalysisSpi::greenplumFunctionBehavior, GpSplitVisitor::new);
    }

    private static boolean isGreenplumFunction(String name) {
        return "gp_array_agg".equals(name) || "median".equals(name) || "pivot_sum".equals(name);
    }

    private static PgFunctionBehavior greenplumFunctionBehavior(List<String> names) {
        if (names.isEmpty() || names.size() > 2 || names.size() == 2 && !"pg_catalog".equals(names.get(0))) {
            return PgFunctionBehavior.DEFAULT;
        }
        String name = names.get(names.size() - 1);
        if ("gp_truncate_error_log".equals(name) || "gp_truncate_persistent_error_log".equals(name)) {
            return new PgFunctionBehavior(BehaviorAction.UNSAFE, null, null, false);
        }
        return PgFunctionBehavior.DEFAULT;
    }

    @Override
    protected StatementBehavior analyzeExtensionStatement(Parser parser, PgSqlParser.ExtensionstmtContext extension, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        GpSqlParser.StatementContext statement = GpStatementParser.parse(parser.getTokenStream(), extension);
        GpStatementBehaviorVisitor visitor = new GpStatementBehaviorVisitor(GpSplitAnalysisSpi.classify(statement), levels, baseLine, baseColumn);
        visitor.visit(statement);
        return visitor.behavior();
    }
}

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
package com.clougence.clouddm.ds.cloudberry.sql.analysis.behavior;

import java.util.Map;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.cloudberry.sql.parser.CbDslProvider;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSplitAnalysisSpi;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSplitVisitor;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbStatementParser;
import com.clougence.clouddm.ds.cloudberry.sql.parser.antlr.CbSqlParser;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class CbBehaviorAnalysisSpi extends PgBehaviorAnalysisSpi {

    public CbBehaviorAnalysisSpi(PostgresVersion version){
        this(new CbDslProvider(version), version);
    }

    private CbBehaviorAnalysisSpi(CbDslProvider provider, PostgresVersion version){
        super(provider, new CbSplitAnalysisSpi(version), name -> "median".equals(name) || "pivot_sum".equals(name), CbSplitVisitor::new);
    }

    @Override
    protected StatementBehavior analyzeExtensionStatement(Parser parser, PgSqlParser.ExtensionstmtContext extension, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        CbSqlParser.StatementContext statement = CbStatementParser.parse(parser.getTokenStream(), extension);
        CbStatementBehaviorVisitor visitor = new CbStatementBehaviorVisitor(CbSplitAnalysisSpi.classify(statement), levels, baseLine, baseColumn);
        visitor.visit(statement);
        return visitor.behavior();
    }
}

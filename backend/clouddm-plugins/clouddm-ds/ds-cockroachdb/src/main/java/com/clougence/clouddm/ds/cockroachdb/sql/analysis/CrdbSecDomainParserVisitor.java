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
package com.clougence.clouddm.ds.cockroachdb.sql.analysis;

import org.antlr.v4.runtime.Parser;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbStatementParser;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbResourceDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.sql.postgres.analysis.security.PgSqlParserVisitor;
import com.clougence.sql.postgres.analysis.security.builder.PgBuilderFactory;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

final class CrdbSecDomainParserVisitor extends PgSqlParserVisitor {
    private final PgBuilderFactory builder;
    private final Parser parser;
    CrdbSecDomainParserVisitor(PgBuilderFactory builder, Parser parser) {
        super(builder, parser);
        this.builder = builder;
        this.parser = parser;
    }
    @Override
    public Void visitExtensionstmt(PgSqlParser.ExtensionstmtContext extension) {
        var statement = CrdbStatementParser.parse(parser.getTokenStream(), extension);
        var domain = new RdbResourceDomain(RuleQueryType.METADATA, SecQueryKind.QUERY, true, TargetType.Instance);
        if (statement.table_name != null) {
            var names = BuilderUtil.parseTableName(CrdbStatementParser.names(statement.table_name));
            domain.setTarget(TargetType.Table);
            if (statement.VIEW() != null) {
                domain.setTarget(TargetType.View);
            } else if (statement.SEQUENCE() != null) {
                domain.setTarget(TargetType.Sequence);
            }
            domain.setCatalog(names.get(UmiTypes.Catalog));
            domain.setSchema(names.get(UmiTypes.Schema));
            domain.setName(names.get(UmiTypes.Table));
        } else if (statement.catalog_name != null) {
            domain.setTarget(TargetType.Catalog);
            domain.setCatalog(CrdbStatementParser.names(statement.catalog_name).get(0));
        } else if (statement.schema_name != null) {
            var names = CrdbStatementParser.names(statement.schema_name);
            if (names.size() == 2) {
                domain.setTarget(TargetType.Schema);
                domain.setCatalog(names.get(0));
                domain.setSchema(names.get(1));
            }
        } else if (statement.TABLES() != null || statement.SCHEMAS() != null || statement.SEQUENCES() != null || statement.TYPES_P() != null
                   || statement.list_keyword() != null && "ENUMS".equalsIgnoreCase(statement.list_keyword().getText())) {
            domain.setTarget(TargetType.Catalog);
        }
        builder.addDomain(domain);
        return null;
    }
    @Override
    public Void visitUpsert_keyword(PgSqlParser.Upsert_keywordContext ctx) {
        return null;
    }

    @Override
    public Void visitSnapshot_clause(PgSqlParser.Snapshot_clauseContext ctx) {
        // The timestamp is not a FROM object or projected column. Behavior analysis authorizes its function calls.
        return null;
    }
}

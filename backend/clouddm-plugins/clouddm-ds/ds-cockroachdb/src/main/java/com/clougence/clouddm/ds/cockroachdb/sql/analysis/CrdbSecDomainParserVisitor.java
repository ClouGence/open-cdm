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

import java.util.Locale;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbStatementParser;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.antlr.CrdbSqlParser;
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

    CrdbSecDomainParserVisitor(PgBuilderFactory builder, Parser parser){
        super(builder, parser);
        this.builder = builder;
    }

    @Override
    public Void visitReturning_clause(PgSqlParser.Returning_clauseContext ctx) {
        for (var item : ctx.target_list().target_el()) {
            if (item.a_expr() != null) {
                builder.handleOtherDomains(() -> item.a_expr().accept(this));
            }
        }
        return null;
    }

    @Override
    public Void visitInverted_index(PgSqlParser.Inverted_indexContext ctx) {
        return null;
    }

    @Override
    public Void visitInclude_(PgSqlParser.Include_Context ctx) {
        // Stored columns are payload, rather than index key expressions or separate resources.
        return null;
    }

    @Override
    public Void visitHash_sharding(PgSqlParser.Hash_shardingContext ctx) {
        if (ctx.a_expr() != null) {
            builder.handleOtherDomains(() -> ctx.a_expr().accept(this));
        }
        return null;
    }

    @Override
    public Void visitMutation_limit_clause(PgSqlParser.Mutation_limit_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndex_hint(PgSqlParser.Index_hintContext ctx) {
        // Index selection does not introduce another table or projected column.
        return null;
    }

    @Override
    public Void visitExtensionstmt(PgSqlParser.ExtensionstmtContext extension) {
        var statement = CrdbStatementParser.parse(extension);
        if (statement.privilege_stmt() != null || statement.default_privileges_stmt() != null) {
            CrdbSqlParser.Role_listContext grantees;
            RuleQueryType type = RuleQueryType.GRANT;
            if (statement.privilege_stmt() != null) {
                grantees = statement.privilege_stmt().grantees;
                if (statement.privilege_stmt().REVOKE() != null) {
                    type = RuleQueryType.REVOKE;
                }
            } else {
                grantees = statement.default_privileges_stmt().grantees;
                if (statement.default_privileges_stmt().REVOKE() != null) {
                    type = RuleQueryType.REVOKE;
                }
            }

            for (var grantee : grantees.role_name()) {
                addPrincipal(type, TargetType.UserOrRole, grantee.getText());
            }
            return null;
        }

        if (statement.role_stmt() != null) {
            visitRole(statement.role_stmt());
            return null;
        }

        if (statement.database_stmt() != null) {
            var database = statement.database_stmt();
            RuleQueryType type = RuleQueryType.DROP_CATALOG;
            SecQueryKind kind = SecQueryKind.DROP;
            if (database.CREATE() != null) {
                type = RuleQueryType.CREATE_CATALOG;
                kind = SecQueryKind.CREATE;
            }

            RdbResourceDomain catalog = new RdbResourceDomain(type, kind, true, TargetType.Catalog);
            catalog.setCatalog(CrdbStatementParser.name(database.database_name.getText()));
            builder.addDomain(catalog);
            return null;
        }

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

    private void visitRole(CrdbSqlParser.Role_stmtContext ctx) {
        TargetType target = TargetType.Role;
        if (ctx.role_kind().USER() != null) {
            target = TargetType.User;
        }

        RuleQueryType type = RuleQueryType.ALTER_USER;
        if (ctx.CREATE() != null) {
            type = RuleQueryType.CREATE_ROLE;
            if (target == TargetType.User) {
                type = RuleQueryType.CREATE_USER;
            }
        } else if (ctx.DROP() != null) {
            type = RuleQueryType.DROP_ROLE;
            if (target == TargetType.User) {
                type = RuleQueryType.DROP_USER;
            }
        }

        if (ctx.roles != null) {
            for (var role : ctx.roles.role_name()) {
                addPrincipal(type, target, role.getText());
            }
        } else if (ctx.principal != null) {
            addPrincipal(type, target, ctx.principal.getText());
        } else {
            // ALL denotes all roles, not a principal literally named "all".
            addPrincipal(type, target, null);
        }
    }

    private void addPrincipal(RuleQueryType type, TargetType target, String name) {
        SecQueryKind kind = SecQueryKind.ALTER;
        if (type == RuleQueryType.CREATE_ROLE || type == RuleQueryType.CREATE_USER) {
            kind = SecQueryKind.CREATE;
        } else if (type == RuleQueryType.DROP_ROLE || type == RuleQueryType.DROP_USER) {
            kind = SecQueryKind.DROP;
        }

        RdbResourceDomain domain = new RdbResourceDomain(type, kind, true, target);
        if (name != null && !name.equalsIgnoreCase("CURRENT_USER") && !name.equalsIgnoreCase("SESSION_USER")) {
            domain.setName(CrdbStatementParser.name(name).toLowerCase(Locale.ROOT));
        }

        builder.addDomain(domain);
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

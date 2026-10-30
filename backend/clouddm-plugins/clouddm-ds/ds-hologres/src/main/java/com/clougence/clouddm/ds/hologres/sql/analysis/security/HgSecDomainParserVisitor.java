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
package com.clougence.clouddm.ds.hologres.sql.analysis.security;

import java.util.Locale;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.hologres.sql.parser.HgPermissionCall;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbResourceDomain;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.postgres.analysis.security.PgSqlParserVisitor;
import com.clougence.sql.postgres.analysis.security.builder.PgBuilderFactory;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

final class HgSecDomainParserVisitor extends PgSqlParserVisitor {
    private final PgBuilderFactory builder;

    HgSecDomainParserVisitor(PgBuilderFactory builder, Parser parser){
        super(builder, parser);
        this.builder = builder;
    }

    @Override
    public Void visitGrantrolestmt(PgSqlParser.GrantrolestmtContext ctx) {
        for (var role : ctx.role_list().rolespec()) {
            addPrincipal(role.getText(), RuleQueryType.GRANT);
        }
        return null;
    }

    @Override
    public Void visitRevokerolestmt(PgSqlParser.RevokerolestmtContext ctx) {
        for (var role : ctx.role_list().rolespec()) {
            addPrincipal(role.getText(), RuleQueryType.REVOKE);
        }
        return null;
    }

    @Override
    public Void visitAlterdefaultprivilegesstmt(PgSqlParser.AlterdefaultprivilegesstmtContext ctx) {
        RuleQueryType type = RuleQueryType.GRANT;
        if (ctx.defaclaction().REVOKE() != null) {
            type = RuleQueryType.REVOKE;
        }

        for (var grantee : ctx.defaclaction().grantee_list().grantee()) {
            addPrincipal(grantee.rolespec().getText(), type);
        }
        return null;
    }

    @Override
    public Void visitAlterrolestmt(PgSqlParser.AlterrolestmtContext ctx) {
        addPrincipal(ctx.rolespec().getText(), RuleQueryType.ALTER_USER);
        return null;
    }

    @Override
    public Void visitAlterrolesetstmt(PgSqlParser.AlterrolesetstmtContext ctx) {
        addPrincipal(ctx.rolespec().getText(), RuleQueryType.ALTER_USER);
        return null;
    }

    private void addPrincipal(String name, RuleQueryType type) {
        if (name.startsWith("\"")) {
            name = name.substring(1, name.length() - 1).replace("\"\"", "\"");
        } else {
            name = name.toLowerCase(Locale.ROOT);
        }

        RdbResourceDomain domain = new RdbResourceDomain(type, SecQueryKind.ALTER, true, TargetType.UserOrRole);
        domain.setName(name);
        builder.addDomain(domain);
    }

    @Override
    public Void visitCallstmt(PgSqlParser.CallstmtContext call) {
        SplitQueryType type = HgPermissionCall.type(call);
        if (type == null) {
            return super.visitCallstmt(call);
        }

        var args = HgPermissionCall.arguments(call);
        boolean create = type == SplitQueryType.CREATE_USER;
        boolean membershipCall = create || type == SplitQueryType.GRANT || type == SplitQueryType.REVOKE;
        boolean literalArguments = !args.isEmpty() && args.size() <= 2 && (create || args.size() == 2);
        for (var arg : args) {
            literalArguments &= HgPermissionCall.literal(arg) != null;
        }

        if (!membershipCall || !literalArguments) {
            RdbResourceDomain domain = new RdbResourceDomain(RuleQueryType.UNSAFE, SecQueryKind.OTHER, false, TargetType.Instance);
            builder.addDomain(domain);
            return null;
        }

        int userIndex = 1;
        if (create) {
            userIndex = 0;
            RdbResourceDomain user = new RdbResourceDomain();
            user.setTarget(TargetType.User);
            user.setName(HgPermissionCall.literal(args.get(0)));
            user.setSqlType(RuleQueryType.CREATE_USER);
            user.setAuditKind(SecQueryKind.CREATE);
            user.setNeedSupply(true);
            builder.addDomain(user);
        }

        if (args.size() == 2) {
            RdbResourceDomain grantee = new RdbResourceDomain();
            grantee.setTarget(TargetType.UserOrRole);
            grantee.setName(HgPermissionCall.literal(args.get(userIndex)));
            grantee.setSqlType(RuleQueryType.GRANT);
            if (type == SplitQueryType.REVOKE) {
                grantee.setSqlType(RuleQueryType.REVOKE);
            }

            grantee.setAuditKind(SecQueryKind.ALTER);
            grantee.setNeedSupply(true);
            builder.addDomain(grantee);
        }
        return null;
    }
}

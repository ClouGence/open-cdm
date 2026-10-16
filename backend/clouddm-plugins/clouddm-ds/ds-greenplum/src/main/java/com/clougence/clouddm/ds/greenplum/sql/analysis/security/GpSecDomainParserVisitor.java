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
package com.clougence.clouddm.ds.greenplum.sql.analysis.security;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.greenplum.sql.parser.GpProtocolHandlerName;
import com.clougence.clouddm.ds.greenplum.sql.parser.GpStatementParser;
import com.clougence.clouddm.ds.greenplum.sql.parser.antlr.GpSqlParser;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbResourceDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.sql.postgres.analysis.security.PgSqlParserVisitor;
import com.clougence.sql.postgres.analysis.security.builder.PgBuilderFactory;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

final class GpSecDomainParserVisitor extends PgSqlParserVisitor {

    private final PgBuilderFactory builder;
    private final Parser           parser;

    GpSecDomainParserVisitor(PgBuilderFactory builder, Parser parser){
        super(builder, parser);
        this.builder = builder;
        this.parser = parser;
    }

    @Override
    public Void visitExtensionstmt(PgSqlParser.ExtensionstmtContext extension) {
        GpSqlParser.StatementContext statement = GpStatementParser.parse(parser.getTokenStream(), extension);
        if (statement.protocol_privilege_stmt() != null) {
            visitProtocolPrivilege(statement.protocol_privilege_stmt());
        } else if (statement.role_stmt() != null) {
            visitRole(statement.role_stmt());
        } else if (statement.protocol_stmt() != null) {
            visitProtocol(statement.protocol_stmt());
        } else if (statement.external_stmt() != null) {
            visitExternal(statement.external_stmt());
        } else if (statement.resource_stmt() != null) {
            visitResource(statement.resource_stmt());
        } else {
            RdbResourceDomain endpoint = domain(TargetType.Endpoint, name(statement.retrieve_stmt().identifier().getText()));
            endpoint.setSqlType(RuleQueryType.SELECT);
            endpoint.setAuditKind(SecQueryKind.QUERY);
            builder.addDomain(endpoint);
        }
        return null;
    }

    private void visitProtocolPrivilege(GpSqlParser.Protocol_privilege_stmtContext ctx) {
        RuleQueryType queryType = RuleQueryType.GRANT;
        if (ctx.REVOKE() != null) {
            queryType = RuleQueryType.REVOKE;
        }
        for (GpSqlParser.Role_nameContext role : ctx.role_name_list().role_name()) {
            RdbResourceDomain grantee = domain(TargetType.UserOrRole, name(role.getText()));
            grantee.setSqlType(queryType);
            grantee.setAuditKind(SecQueryKind.ALTER);
            builder.addDomain(grantee);
        }
    }

    private void visitRole(GpSqlParser.Role_stmtContext ctx) {
        boolean user = ctx.role_keyword().USER() != null;
        TargetType type = TargetType.Role;
        if (user) {
            type = TargetType.User;
        }
        RdbResourceDomain role = domain(type, name(ctx.role_name().getText()));
        if (ctx.CREATE() != null) {
            role.setSqlType(RuleQueryType.CREATE_ROLE);
            if (user) {
                role.setSqlType(RuleQueryType.CREATE_USER);
            }
        } else {
            role.setSqlType(RuleQueryType.ALTER_USER);
        }
        setAudit(role, ctx.CREATE() != null, ctx.ALTER() != null);
        builder.addDomain(role);
    }

    private void visitProtocol(GpSqlParser.Protocol_stmtContext ctx) {
        RdbResourceDomain protocol = domain(TargetType.ProgramObject, name(ctx.identifier(0).getText()));
        setAction(protocol, ctx.CREATE() != null, ctx.ALTER() != null, RuleQueryType.CREATE_PROG_OBJ, RuleQueryType.ALTER_PROG_OBJ, RuleQueryType.DROP_PROG_OBJ);
        builder.addDomain(protocol);
        for (GpSqlParser.Protocol_handlerContext handler : ctx.protocol_handler()) {
            List<String> parts = GpProtocolHandlerName.parse(handler.StringConstant().getText());
            RdbResourceDomain reference = domain(TargetType.Function, parts.get(parts.size() - 1));
            if (parts.size() == 2) {
                reference.setSchema(parts.get(0));
            } else if (parts.size() == 3) {
                reference.setCatalog(parts.get(0));
                reference.setSchema(parts.get(1));
            }
            reference.setSqlType(RuleQueryType.CALL_PROG_OBJ);
            reference.setAuditKind(SecQueryKind.CALL);
            builder.addDomain(reference);
        }
    }

    private void visitExternal(GpSqlParser.External_stmtContext ctx) {
        List<String> parts = ctx.qualified_name().identifier().stream().map(id -> name(id.getText())).toList();
        Map<UmiTypes, String> names = BuilderUtil.parseTableName(parts);
        RdbResourceDomain table = domain(TargetType.Table, names.get(UmiTypes.Table));
        table.setCatalog(names.get(UmiTypes.Catalog));
        table.setSchema(names.get(UmiTypes.Schema));
        RuleQueryType alterType = RuleQueryType.ALTER_TABLE;
        if (ctx.external_alter_action() != null) {
            if (ctx.external_alter_action().ADD_P() != null) {
                alterType = RuleQueryType.ADD_COLUMN;
            } else if (ctx.external_alter_action().DROP() != null) {
                alterType = RuleQueryType.DROP_COLUMN;
            } else if (ctx.external_alter_action().ALTER() != null) {
                alterType = RuleQueryType.ALTER_COLUMN;
            }
        }
        setAction(table, ctx.CREATE() != null, ctx.ALTER() != null, RuleQueryType.CREATE_TABLE, alterType, RuleQueryType.DROP_TABLE);
        builder.addDomain(table);
        if (ctx.external_alter_action() != null && ctx.external_alter_action().OWNER() != null) {
            RdbResourceDomain owner = domain(TargetType.UserOrRole, name(ctx.external_alter_action().identifier().getText()));
            owner.setSqlType(RuleQueryType.TRANSFER_PRIVILEGE);
            owner.setAuditKind(SecQueryKind.ALTER);
            builder.addDomain(owner);
        }
        if (ctx.external_columns() != null && ctx.external_columns().qualified_name() != null) {
            List<String> sourceParts = ctx.external_columns().qualified_name().identifier().stream().map(id -> name(id.getText())).toList();
            Map<UmiTypes, String> sourceNames = BuilderUtil.parseTableName(sourceParts);
            RdbResourceDomain source = domain(TargetType.Table, sourceNames.get(UmiTypes.Table));
            source.setCatalog(sourceNames.get(UmiTypes.Catalog));
            source.setSchema(sourceNames.get(UmiTypes.Schema));
            source.setSqlType(RuleQueryType.SELECT);
            source.setAuditKind(SecQueryKind.QUERY);
            builder.addDomain(source);
        }
        if (ctx.external_source() != null && ctx.external_source().EXECUTE() != null) {
            String command = ctx.external_source().StringConstant(0).getText();
            command = command.substring(1, command.length() - 1).replace("''", "'");
            RdbResourceDomain program = domain(TargetType.ProgramObject, command);
            program.setSqlType(RuleQueryType.UNSAFE);
            program.setAuditKind(SecQueryKind.OTHER);
            builder.addDomain(program);
        }
    }

    private void visitResource(GpSqlParser.Resource_stmtContext ctx) {
        TargetType type = TargetType.ResourceGroup;
        if (ctx.queue_keyword() != null || ctx.resource_kind() != null && ctx.resource_kind().queue_keyword() != null) {
            type = TargetType.Queue;
        }
        RdbResourceDomain resource = domain(type, name(ctx.identifier(0).getText()));
        resource.setSqlType(RuleQueryType.SYSTEM_SETTING_WRITE);
        setAudit(resource, ctx.CREATE() != null, ctx.ALTER() != null);
        builder.addDomain(resource);
    }

    private static RdbResourceDomain domain(TargetType type, String name) {
        RdbResourceDomain result = new RdbResourceDomain();
        result.setTarget(type);
        result.setName(name);
        result.setNeedSupply(true);
        return result;
    }

    private static void setAction(RdbResourceDomain domain, boolean create, boolean alter, RuleQueryType createType, RuleQueryType alterType, RuleQueryType dropType) {
        if (create) {
            domain.setSqlType(createType);
        } else if (alter) {
            domain.setSqlType(alterType);
        } else {
            domain.setSqlType(dropType);
        }
        setAudit(domain, create, alter);
    }

    private static void setAudit(RdbResourceDomain domain, boolean create, boolean alter) {
        if (create) {
            domain.setAuditKind(SecQueryKind.CREATE);
        } else if (alter) {
            domain.setAuditKind(SecQueryKind.ALTER);
        } else {
            domain.setAuditKind(SecQueryKind.DROP);
        }
    }

    private static String name(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toLowerCase(Locale.ROOT);
    }
}

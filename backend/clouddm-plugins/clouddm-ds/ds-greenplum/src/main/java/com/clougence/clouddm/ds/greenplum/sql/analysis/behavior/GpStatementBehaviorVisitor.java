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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.Trees;

import com.clougence.clouddm.ds.greenplum.sql.parser.GpProtocolHandlerName;
import com.clougence.clouddm.ds.greenplum.sql.parser.antlr.GpSqlParser;
import com.clougence.clouddm.ds.greenplum.sql.parser.antlr.GpSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class GpStatementBehaviorVisitor extends GpSqlParserBaseVisitor<Void> {

    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    GpStatementBehaviorVisitor(SplitQueryType statementType, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(statementType);
    }

    StatementBehavior behavior() {
        return this.behavior;
    }

    @Override
    public Void visitProtocol_privilege_stmt(GpSqlParser.Protocol_privilege_stmtContext ctx) {
        BehaviorAction action = BehaviorAction.GRANT;
        if (ctx.REVOKE() != null) {
            action = BehaviorAction.REVOKE;
        }
        List<BehaviorObject> grantees = new ArrayList<>();
        for (GpSqlParser.Role_nameContext role : ctx.role_name_list().role_name()) {
            grantees.add(instanceObject(TargetType.UserOrRole, role));
        }
        add(action, object(TargetType.ProgramObject, ctx.identifier()), grantees);
        return null;
    }

    @Override
    public Void visitRole_stmt(GpSqlParser.Role_stmtContext ctx) {
        TargetType type = TargetType.Role;
        if (ctx.role_keyword().USER() != null) {
            type = TargetType.User;
        }
        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.CREATE() != null) {
            action = BehaviorAction.CREATE;
        }
        BehaviorObject subject = instanceObject(type, ctx.role_name());

        List<BehaviorObject> resources = new ArrayList<>();
        for (var node : Trees.getDescendants(ctx)) {
            if (!(node instanceof GpSqlParser.Greenplum_role_optionContext option) || option.resource_kind() == null || option.role_name() == null) {
                continue;
            }
            TargetType resourceType = TargetType.ResourceGroup;
            if (option.resource_kind().queue_keyword() != null) {
                resourceType = TargetType.Queue;
            }
            resources.add(instanceObject(resourceType, option.role_name()));
        }
        add(action, subject, resources);

        if (ctx.CREATE() != null) {
            addCreateRoleMemberships(ctx, subject);
        }
        return null;
    }

    private void addCreateRoleMemberships(GpSqlParser.Role_stmtContext ctx, BehaviorObject createdRole) {
        List<BehaviorObject> grantees = new ArrayList<>();
        for (GpSqlParser.Create_role_optionContext option : ctx.create_role_option()) {
            GpSqlParser.Role_name_listContext names = option.role_name_list();
            if (names == null && option.alter_role_option() != null) {
                names = option.alter_role_option().role_name_list();
            }
            if (names == null) {
                continue;
            }
            if (option.IN_P() != null) {
                for (GpSqlParser.Role_nameContext role : names.role_name()) {
                    add(BehaviorAction.GRANT, instanceObject(TargetType.Role, role), List.of(createdRole));
                }
            } else {
                for (GpSqlParser.Role_nameContext role : names.role_name()) {
                    grantees.add(instanceObject(TargetType.UserOrRole, role));
                }
            }
        }
        if (!grantees.isEmpty()) {
            add(BehaviorAction.GRANT, createdRole, grantees);
        }
    }

    @Override
    public Void visitProtocol_stmt(GpSqlParser.Protocol_stmtContext ctx) {
        BehaviorObject protocol = object(TargetType.ProgramObject, ctx.identifier(0));
        if (ctx.CREATE() != null) {
            add(BehaviorAction.CREATE, protocol);
            for (GpSqlParser.Protocol_handlerContext handler : ctx.protocol_handler()) {
                add(BehaviorAction.CALL, this.objects
                    .object(TargetType.Function, handler.StringConstant().getSymbol(), GpProtocolHandlerName.parse(handler.StringConstant().getText())));
            }
        } else if (ctx.DROP() != null) {
            add(BehaviorAction.DROP, protocol);
        } else if (ctx.RENAME() != null) {
            add(BehaviorAction.RENAME, protocol, List.of(object(TargetType.ProgramObject, ctx.identifier(1))));
        } else {
            add(BehaviorAction.TRANSFER, protocol, List.of(instanceObject(TargetType.UserOrRole, ctx.identifier(1))));
        }
        return null;
    }

    @Override
    public Void visitExternal_stmt(GpSqlParser.External_stmtContext ctx) {
        if (ctx.external_alter_action() != null && ctx.external_alter_action().OWNER() != null) {
            add(BehaviorAction.TRANSFER, object(TargetType.Table, ctx.qualified_name()), List.of(instanceObject(TargetType.UserOrRole, ctx.external_alter_action().identifier())));
            return null;
        }
        BehaviorAction action = action(ctx.CREATE() != null, ctx.ALTER() != null);
        add(action, object(TargetType.Table, ctx.qualified_name()));
        if (ctx.external_columns() != null && ctx.external_columns().qualified_name() != null) {
            add(BehaviorAction.READ, object(TargetType.Table, ctx.external_columns().qualified_name()));
        }
        if (ctx.external_source() != null && ctx.external_source().EXECUTE() != null) {
            var command = ctx.external_source().StringConstant(0);
            String value = command.getText();
            value = value.substring(1, value.length() - 1).replace("''", "'");
            add(BehaviorAction.UNSAFE, this.objects.instanceObject(TargetType.ProgramObject, command.getSymbol(), value));
        }
        return null;
    }

    @Override
    public Void visitResource_stmt(GpSqlParser.Resource_stmtContext ctx) {
        TargetType type = TargetType.ResourceGroup;
        if (ctx.queue_keyword() != null || ctx.resource_kind() != null && ctx.resource_kind().queue_keyword() != null) {
            type = TargetType.Queue;
        }
        add(action(ctx.CREATE() != null, ctx.ALTER() != null), instanceObject(type, ctx.identifier(0)));
        return null;
    }

    @Override
    public Void visitRetrieve_stmt(GpSqlParser.Retrieve_stmtContext ctx) {
        add(BehaviorAction.READ, object(TargetType.Endpoint, ctx.identifier()));
        return null;
    }

    private BehaviorObject object(TargetType type, GpSqlParser.IdentifierContext identifier) {
        return this.objects.object(type, identifier, List.of(name(identifier.getText())));
    }

    private BehaviorObject object(TargetType type, GpSqlParser.Qualified_nameContext qualifiedName) {
        List<String> names = qualifiedName.identifier().stream().map(ParserRuleContext::getText).map(GpStatementBehaviorVisitor::name).toList();
        return this.objects.object(type, qualifiedName, names);
    }

    private BehaviorObject instanceObject(TargetType type, ParserRuleContext name) {
        return this.objects.instanceObject(type, name.getStart(), name.getStop(), name(name.getText()));
    }

    private void add(BehaviorAction action, BehaviorObject subject) {
        add(action, subject, List.of());
    }

    private void add(BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(subject);
        relation.getTarget().addAll(targets);
        this.behavior.getRelations().add(relation);
    }

    private static BehaviorAction action(boolean create, boolean alter) {
        if (create) {
            return BehaviorAction.CREATE;
        }
        if (alter) {
            return BehaviorAction.ALTER;
        }
        return BehaviorAction.DROP;
    }

    private static String name(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toLowerCase(Locale.ROOT);
    }
}

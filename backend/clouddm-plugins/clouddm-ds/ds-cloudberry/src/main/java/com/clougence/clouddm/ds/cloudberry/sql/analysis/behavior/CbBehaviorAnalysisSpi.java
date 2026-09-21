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

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.tree.Trees;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbDslProvider;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSplitAnalysisSpi;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSplitVisitor;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbStatementParser;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbProtocolHandlerName;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSyntax;
import com.clougence.clouddm.ds.cloudberry.sql.parser.antlr.CbSqlParser;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class CbBehaviorAnalysisSpi extends PgBehaviorAnalysisSpi {

    public CbBehaviorAnalysisSpi(PostgresVersion version) {
        super(new CbDslProvider(version), new CbSplitAnalysisSpi(version),
            name -> "median".equals(name) || "pivot_sum".equals(name), CbSplitVisitor::new);
    }

    @Override
    protected List<StatementBehavior> analyzeStatement(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        String sql = CbSyntax.readSql(reader);
        if (!CbSyntax.isExtensionStatement(sql)) {
            return super.analyzeStatement(new StringReader(sql), levels, baseLine, baseColumn);
        }
        CbSqlParser.StatementContext statement = CbStatementParser.parse(sql);
        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(CbSplitAnalysisSpi.classify(statement));
        RdbBehaviorObjectFactory objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        BehaviorAction action;
        BehaviorObject subject;
        if (statement.protocol_privilege_stmt() != null) {
            CbSqlParser.Protocol_privilege_stmtContext ctx = statement.protocol_privilege_stmt();
            action = BehaviorAction.GRANT;
            if (ctx.REVOKE() != null) {
                action = BehaviorAction.REVOKE;
            }
            List<BehaviorObject> grantees = new ArrayList<>();
            for (CbSqlParser.Role_nameContext role : ctx.role_name_list().role_name()) {
                grantees.add(objects.instanceObject(TargetType.UserOrRole, role.getStart(), role.getStop(),
                    name(role.getText())));
            }
            subject = objects.object(TargetType.ProgramObject, ctx.identifier(),
                List.of(name(ctx.identifier().getText())));
            add(behavior, action, subject, grantees);
            return List.of(behavior);
        }
        if (statement.role_stmt() != null) {
            CbSqlParser.Role_stmtContext ctx = statement.role_stmt();
            TargetType type = TargetType.Role;
            if (ctx.role_keyword().USER() != null) {
                type = TargetType.User;
            }
            action = BehaviorAction.ALTER;
            if (ctx.CREATE() != null) {
                action = BehaviorAction.CREATE;
            }
            List<BehaviorObject> targets = new ArrayList<>();
            for (var node : Trees.getDescendants(ctx)) {
                if (!(node instanceof CbSqlParser.Cloudberry_role_optionContext option)) {
                    continue;
                }
                if (option.resource_kind() == null || option.role_name() == null) {
                    continue;
                }
                TargetType resourceType = TargetType.ResourceGroup;
                if (option.resource_kind().queue_keyword() != null) {
                    resourceType = TargetType.Queue;
                }
                CbSqlParser.Role_nameContext resource = option.role_name();
                targets.add(objects.instanceObject(resourceType, resource.getStart(), resource.getStop(),
                    name(resource.getText())));
            }
            CbSqlParser.Role_nameContext role = ctx.role_name();
            subject = objects.instanceObject(type, role.getStart(), role.getStop(), name(role.getText()));
            add(behavior, action, subject, targets);
            return List.of(behavior);
        }
        if (statement.protocol_stmt() != null) {
            CbSqlParser.Protocol_stmtContext ctx = statement.protocol_stmt();
            action = action(ctx.CREATE() != null, ctx.ALTER() != null);
            subject = objects.object(TargetType.ProgramObject, ctx.identifier(0), List.of(name(ctx.identifier(0).getText())));
            add(behavior, action, subject);
            for (CbSqlParser.Protocol_handlerContext handler : ctx.protocol_handler()) {
                add(behavior, BehaviorAction.CALL,
                    objects.object(TargetType.Function, handler.StringConstant().getSymbol(),
                        CbProtocolHandlerName.parse(handler.StringConstant().getText())));
            }
            return List.of(behavior);
        }
        if (statement.external_stmt() != null) {
            CbSqlParser.External_stmtContext ctx = statement.external_stmt();
            action = action(ctx.CREATE() != null, ctx.ALTER() != null);
            subject = objects.object(TargetType.Table, ctx.qualified_name(),
                ctx.qualified_name().identifier().stream().map(id -> name(id.getText())).toList());
            add(behavior, action, subject);
            if (ctx.external_columns() != null && ctx.external_columns().qualified_name() != null) {
                CbSqlParser.Qualified_nameContext source = ctx.external_columns().qualified_name();
                add(behavior, BehaviorAction.READ, objects.object(TargetType.Table, source,
                    source.identifier().stream().map(id -> name(id.getText())).toList()));
            }
            if (ctx.external_source() != null && ctx.external_source().EXECUTE() != null) {
                var command = ctx.external_source().StringConstant(0);
                String value = command.getText();
                value = value.substring(1, value.length() - 1).replace("''", "'");
                add(behavior, BehaviorAction.UNSAFE,
                    objects.instanceObject(TargetType.ProgramObject, command.getSymbol(), value));
            }
            return List.of(behavior);
        }
        if (statement.resource_stmt() != null) {
            CbSqlParser.Resource_stmtContext ctx = statement.resource_stmt();
            action = action(ctx.CREATE() != null, ctx.ALTER() != null);
            TargetType type = TargetType.ResourceGroup;
            if (ctx.queue_keyword() != null || ctx.resource_kind() != null && ctx.resource_kind().queue_keyword() != null) {
                type = TargetType.Queue;
            }
            subject = objects.object(type, ctx.identifier(0), List.of(name(ctx.identifier(0).getText())));
            add(behavior, action, subject);
            return List.of(behavior);
        }
        CbSqlParser.Retrieve_stmtContext ctx = statement.retrieve_stmt();
        subject = objects.object(TargetType.Endpoint, ctx.identifier(), List.of(name(ctx.identifier().getText())));
        add(behavior, BehaviorAction.READ, subject);
        return List.of(behavior);
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

    private static void add(StatementBehavior behavior, BehaviorAction action, BehaviorObject subject) {
        add(behavior, action, subject, List.of());
    }

    private static void add(StatementBehavior behavior, BehaviorAction action, BehaviorObject subject,
                            List<BehaviorObject> targets) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(subject);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
    }

    private static String name(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toLowerCase(Locale.ROOT);
    }
}

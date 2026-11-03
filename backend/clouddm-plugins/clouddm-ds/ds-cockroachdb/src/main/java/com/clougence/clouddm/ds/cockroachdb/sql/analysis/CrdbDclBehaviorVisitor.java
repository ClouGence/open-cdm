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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbStatementParser;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.antlr.CrdbSqlParser;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.antlr.CrdbSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;

final class CrdbDclBehaviorVisitor extends CrdbSqlParserBaseVisitor<Void> {
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    CrdbDclBehaviorVisitor(SplitQueryType type, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(type);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitPrivilege_stmt(CrdbSqlParser.Privilege_stmtContext ctx) {
        BehaviorAction action = BehaviorAction.GRANT;
        if (ctx.REVOKE() != null) {
            action = BehaviorAction.REVOKE;
        }

        List<BehaviorObject> grantees = principals(ctx.grantees, TargetType.UserOrRole);
        if (ctx.membership != null) {
            for (BehaviorObject role : principals(ctx.membership, TargetType.Role)) {
                add(action, role, grantees);
            }
        } else if (ctx.SYSTEM_P() != null) {
            add(action, objects.instanceObject(TargetType.Instance, ctx.SYSTEM_P().getSymbol()), grantees);
        } else {
            grantTargets(ctx.privilege_target(), action, grantees);
        }
        return null;
    }

    private void grantTargets(CrdbSqlParser.Privilege_targetContext ctx, BehaviorAction action, List<BehaviorObject> grantees) {
        if (ctx.schemas != null) {
            for (var schema : ctx.schemas.schema_ref()) {
                List<String> names = schema.identifier().stream().map(id -> CrdbStatementParser.name(id.getText())).toList();
                // ALL ... IN SCHEMA authorizes the schema scope, rather than inventing a table named after it.
                add(action, objects.object(TargetType.Schema, schema, names), grantees);
            }
        } else if (ctx.database_names != null) {
            for (var name : ctx.database_names.identifier()) {
                add(action, objects.object(TargetType.Catalog, name, List.of(CrdbStatementParser.name(name.getText()))), grantees);
            }
        } else if (ctx.connections != null) {
            for (var name : ctx.connections.identifier()) {
                add(action, principal(TargetType.Resource, name), grantees);
            }
        } else if (ctx.type_names != null) {
            for (var name : ctx.type_names.qualified_name()) {
                add(action, object(TargetType.Type, name), grantees);
            }
        } else if (!ctx.routine_target().isEmpty()) {
            TargetType type = TargetType.Function;
            if (ctx.PROCEDURE() != null) {
                type = TargetType.Procedure;
            }

            for (var routine : ctx.routine_target()) {
                add(action, object(type, routine.qualified_name()), grantees);
            }
        } else {
            TargetType type = TargetType.Table;
            if (ctx.SEQUENCE() != null) {
                type = TargetType.Sequence;
            }

            for (var pattern : ctx.table_pattern()) {
                if (pattern.qualified_name() == null) {
                    add(action, objects.unnamedObject(TargetType.Schema, pattern, UmiTypes.Schema), grantees);
                } else if (pattern.STAR() != null) {
                    List<String> names = CrdbStatementParser.names(pattern.qualified_name());
                    TargetType scope = TargetType.Schema;
                    if (names.size() == 1) {
                        // In CockroachDB db.* is a database pattern, while db.schema.* identifies a schema.
                        scope = TargetType.Catalog;
                    }
                    add(action, objects.object(scope, pattern.qualified_name(), names), grantees);
                } else {
                    add(action, object(type, pattern.qualified_name()), grantees);
                }
            }
        }
    }

    @Override
    public Void visitDefault_privileges_stmt(CrdbSqlParser.Default_privileges_stmtContext ctx) {
        BehaviorAction action = BehaviorAction.GRANT;
        if (ctx.REVOKE() != null) {
            action = BehaviorAction.REVOKE;
        }

        List<BehaviorObject> targets = principals(ctx.grantees, TargetType.UserOrRole);
        if (ctx.creators != null) {
            targets.addAll(principals(ctx.creators, TargetType.Role));
        }

        if (ctx.schemas == null) {
            add(action, objects.unnamedObject(TargetType.Catalog, ctx.default_target(), UmiTypes.Catalog), targets);
        } else {
            for (var schema : ctx.schemas.schema_ref()) {
                List<String> names = schema.identifier().stream().map(id -> CrdbStatementParser.name(id.getText())).toList();
                add(action, objects.object(TargetType.Schema, schema, names), targets);
            }
        }
        return null;
    }

    @Override
    public Void visitRole_stmt(CrdbSqlParser.Role_stmtContext ctx) {
        TargetType type = TargetType.Role;
        if (ctx.role_kind().USER() != null) {
            type = TargetType.User;
        }

        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.CREATE() != null) {
            action = BehaviorAction.CREATE;
        } else if (ctx.DROP() != null) {
            action = BehaviorAction.DROP;
        } else if (ctx.role_setting() != null) {
            action = BehaviorAction.CONFIGURE;
        }

        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.role_setting() != null && ctx.role_setting().identifier() != null) {
            var database = ctx.role_setting().identifier();
            targets.add(objects.object(TargetType.Catalog, database, List.of(CrdbStatementParser.name(database.getText()))));
        }

        if (ctx.role_setting() != null && ctx.role_setting().qualified_name() != null) {
            var key = ctx.role_setting().qualified_name();
            targets.add(objects.instanceObject(TargetType.ConfigKey, key, key.getText().toLowerCase(Locale.ROOT)));
        }

        if (ctx.roles != null) {
            for (BehaviorObject role : principals(ctx.roles, type)) {
                add(action, role, targets);
            }
        } else if (ctx.principal != null) {
            add(action, principal(type, ctx.principal), targets);
        } else {
            add(action, objects.instanceObject(type, ctx.ALL().getSymbol()), targets);
        }
        return null;
    }

    private List<BehaviorObject> principals(CrdbSqlParser.Role_listContext roles, TargetType type) {
        List<BehaviorObject> result = new ArrayList<>();
        for (var role : roles.role_name()) {
            result.add(principal(type, role));
        }
        return result;
    }

    private BehaviorObject principal(TargetType type, ParserRuleContext name) {
        if (name.getText().equalsIgnoreCase("CURRENT_USER") || name.getText().equalsIgnoreCase("SESSION_USER")) {
            return objects.instanceObject(type, name);
        }

        String value = CrdbStatementParser.name(name.getText());
        if (type == TargetType.Role || type == TargetType.User || type == TargetType.UserOrRole) {
            // CockroachDB folds principal names even when SQL quotes preserve object-name case.
            value = value.toLowerCase(Locale.ROOT);
        }
        return objects.instanceObject(type, name, value);
    }

    private BehaviorObject object(TargetType type, CrdbSqlParser.Qualified_nameContext name) {
        return objects.object(type, name, CrdbStatementParser.names(name));
    }

    private void add(BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
    }
}

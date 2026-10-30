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
package com.clougence.clouddm.ds.hologres.sql.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

final class HgScopedPrivilegeBehavior {
    private HgScopedPrivilegeBehavior(){
    }

    static StatementBehavior analyze(PgSqlParser.Pg_stmtContext statement, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        PgSqlParser.Grantee_listContext grantees;
        List<PgSqlParser.NameContext> schemas = new ArrayList<>();
        TargetType target = TargetType.Table;
        BehaviorAction action = BehaviorAction.GRANT;
        ParserRuleContext scope;
        if (statement.alterdefaultprivilegesstmt() != null) {
            var defaults = statement.alterdefaultprivilegesstmt();
            scope = defaults;
            grantees = defaults.defaclaction().grantee_list();
            if (defaults.defaclaction().REVOKE() != null) {
                action = BehaviorAction.REVOKE;
            }

            var kind = defaults.defaclaction().defacl_privilege_target();
            if (kind.FUNCTIONS() != null || kind.ROUTINES() != null) {
                target = TargetType.Function;
            } else if (kind.SEQUENCES() != null) {
                target = TargetType.Sequence;
            } else if (kind.TYPES_P() != null) {
                target = TargetType.Type;
            } else if (kind.SCHEMAS() != null) {
                target = TargetType.Schema;
            }

            for (var option : defaults.defacloptionlist().defacloption()) {
                if (option.name_list() != null) {
                    schemas.addAll(option.name_list().name());
                }
            }
        } else {
            PgSqlParser.Privilege_targetContext resource;
            if (statement.grantstmt() != null) {
                resource = statement.grantstmt().privilege_target();
                grantees = statement.grantstmt().grantee_list();
            } else if (statement.revokestmt() != null) {
                resource = statement.revokestmt().privilege_target();
                grantees = statement.revokestmt().grantee_list();
                action = BehaviorAction.REVOKE;
            } else {
                return null;
            }

            if (resource.ALL() == null) {
                return null;
            }

            scope = resource;
            schemas.addAll(resource.name_list().name());
            if (resource.SEQUENCES() != null) {
                target = TargetType.Sequence;
            } else if (resource.FUNCTIONS() != null || resource.ROUTINES() != null) {
                target = TargetType.Function;
            } else if (resource.PROCEDURES() != null) {
                target = TargetType.Procedure;
            }
        }

        RdbBehaviorObjectFactory objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        List<BehaviorObject> recipients = new ArrayList<>();
        for (var grantee : grantees.grantee()) {
            recipients.add(objects.instanceObject(TargetType.UserOrRole, grantee.rolespec(), identifier(grantee.rolespec().getText())));
        }

        List<BehaviorObject> subjects = new ArrayList<>();
        for (var schema : schemas) {
            // The path denotes all objects of the requested type within this schema, not a table named after the schema.
            BehaviorObject subject = objects.object(TargetType.Schema, schema, List.of(identifier(schema.getText())));
            subject.setObjectType(target);
            subjects.add(subject);
        }

        if (schemas.isEmpty()) {
            subjects.add(objects.unnamedObject(target, scope, UmiTypes.Catalog));
        }

        StatementBehavior result = new StatementBehavior();
        result.setStatementType(SplitQueryType.GRANT);
        if (action == BehaviorAction.REVOKE) {
            result.setStatementType(SplitQueryType.REVOKE);
        }

        for (BehaviorObject subject : subjects) {
            BehaviorRelation relation = new BehaviorRelation();
            relation.setAction(action);
            relation.setSubject(subject);
            relation.getTarget().addAll(recipients);
            result.getRelations().add(relation);
        }
        return result;
    }

    private static String identifier(String text) {
        if (text.startsWith("\"")) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toLowerCase(Locale.ROOT);
    }
}

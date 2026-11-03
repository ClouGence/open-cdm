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
package com.clougence.clouddm.ds.hologres.sql.analysis.behavior;

import java.util.Map;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.hologres.sql.parser.HgPermissionCall;
import com.clougence.clouddm.ds.hologres.sql.parser.HgSplitAnalysisSpi;
import com.clougence.clouddm.ds.hologres.sql.parser.HgSplitVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class HgBehaviorAnalysisSpi extends PgBehaviorAnalysisSpi {
    public HgBehaviorAnalysisSpi(PostgresVersion version){
        super(new PgDslProvider(version), new HgSplitAnalysisSpi(version), name -> false, HgSplitVisitor::new);
    }

    @Override
    protected StatementBehavior analyzeDialectStatement(Parser parser, PgSqlParser.StmtContext statement, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        PgSqlParser.CallstmtContext call = null;
        if (statement.pg_stmt() != null) {
            StatementBehavior scoped = HgScopedPrivilegeBehavior.analyze(statement.pg_stmt(), levels, baseLine, baseColumn);
            if (scoped != null) {
                return scoped;
            }

            call = statement.pg_stmt().callstmt();
        }

        if (call == null || HgPermissionCall.type(call) == null) {
            return super.analyzeDialectStatement(parser, statement, levels, baseLine, baseColumn);
        }

        var args = HgPermissionCall.arguments(call);
        SplitQueryType type = HgPermissionCall.type(call);
        boolean create = type == SplitQueryType.CREATE_USER;
        boolean membershipCall = create || type == SplitQueryType.GRANT || type == SplitQueryType.REVOKE;
        boolean literalArguments = !args.isEmpty() && args.size() <= 2 && (create || args.size() == 2);
        for (var arg : args) {
            literalArguments &= HgPermissionCall.literal(arg) != null;
        }

        RdbBehaviorObjectFactory objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        StatementBehavior result = new StatementBehavior();
        result.setStatementType(type);
        if (!membershipCall || !literalArguments) {
            // Model migration/cleanup and runtime-computed principals cannot be reduced to one named grant.
            BehaviorRelation relation = new BehaviorRelation();
            relation.setAction(BehaviorAction.UNSAFE);
            relation.setSubject(objects.instanceObject(TargetType.Instance, call));
            result.getRelations().add(relation);
            return result;
        }

        if (create) {
            BehaviorObject user = objects.instanceObject(TargetType.User, args.get(0), HgPermissionCall.literal(args.get(0)));
            BehaviorRelation relation = new BehaviorRelation();
            relation.setAction(BehaviorAction.CREATE);
            relation.setSubject(user);
            result.getRelations().add(relation);
        }

        if (args.size() == 2) {
            int roleIndex = 0;
            int userIndex = 1;
            if (create) {
                roleIndex = 1;
                userIndex = 0;
            }

            BehaviorRelation membership = new BehaviorRelation();
            membership.setAction(BehaviorAction.GRANT);
            if (type == SplitQueryType.REVOKE) {
                membership.setAction(BehaviorAction.REVOKE);
            }

            membership.setSubject(objects.instanceObject(TargetType.Role, args.get(roleIndex), HgPermissionCall.literal(args.get(roleIndex))));
            membership.getTarget().add(objects.instanceObject(TargetType.UserOrRole, args.get(userIndex), HgPermissionCall.literal(args.get(userIndex))));
            result.getRelations().add(membership);
        }
        return result;
    }
}

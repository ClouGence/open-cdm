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

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbDslProvider;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbSplitAnalysisSpi;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbSplitVisitor;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbStatementParser;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class CrdbBehaviorAnalysisSpi extends PgBehaviorAnalysisSpi {
    public CrdbBehaviorAnalysisSpi(PostgresVersion version){
        super(new CrdbDslProvider(version), new CrdbSplitAnalysisSpi(version), CrdbSystemFunctions::contains, CrdbSplitVisitor::new);
    }

    @Override
    protected StatementBehavior analyzeExtensionStatement(Parser parser, PgSqlParser.ExtensionstmtContext extension, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var statement = CrdbStatementParser.parse(extension);
        if (statement.privilege_stmt() != null || statement.default_privileges_stmt() != null || statement.role_stmt() != null) {
            SplitQueryType type = CrdbSplitVisitor.classify(statement);
            CrdbDclBehaviorVisitor visitor = new CrdbDclBehaviorVisitor(type, levels, baseLine, baseColumn);
            visitor.visit(statement);
            return visitor.behavior();
        }

        var objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        SplitQueryType statementType = SplitQueryType.METADATA;
        BehaviorAction action = BehaviorAction.READ;
        BehaviorObject subject;
        if (statement.database_stmt() != null) {
            var database = statement.database_stmt();
            var name = database.database_name;
            subject = objects.object(TargetType.Catalog, name, List.of(CrdbStatementParser.name(name.getText())));
            statementType = CrdbSplitVisitor.classify(statement);
            action = BehaviorAction.DROP;
            if (database.CREATE() != null) {
                action = BehaviorAction.CREATE;
            }
        } else if (statement.table_name != null) {
            TargetType type = TargetType.Table;
            if (statement.VIEW() != null) {
                type = TargetType.View;
            } else if (statement.SEQUENCE() != null) {
                type = TargetType.Sequence;
            }
            subject = objects.object(type, statement.table_name, CrdbStatementParser.names(statement.table_name));
        } else if (statement.catalog_name != null) {
            subject = objects.object(TargetType.Catalog, statement.catalog_name, CrdbStatementParser.names(statement.catalog_name));
        } else if (statement.schema_name != null) {
            var names = CrdbStatementParser.names(statement.schema_name);
            if (names.size() == 2) {
                subject = objects.object(TargetType.Schema, statement.schema_name, names);
            } else {
                // One name resolves to a schema or database on the server; do not guess its scope.
                subject = objects.instanceObject(TargetType.Instance, statement);
            }
        } else if (statement.TABLES() != null || statement.SCHEMAS() != null || statement.SEQUENCES() != null || statement.TYPES_P() != null
                   || statement.list_keyword() != null && "ENUMS".equalsIgnoreCase(statement.list_keyword().getText())) {
            subject = objects.unnamedObject(TargetType.Catalog, statement, UmiTypes.Catalog);
        } else {
            subject = objects.instanceObject(TargetType.Instance, statement);
        }
        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(statementType);
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
        return behavior;
    }
}

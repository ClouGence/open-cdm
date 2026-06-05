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
package com.clougence.clouddm.dsfamily.db2.analysis.resolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateSchemaStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2DropSchemaStatement;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2SchemaDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class Db2SchemaResolveHelper extends Db2AbstractResolveHelper {

    private Db2TableResolveHelper tableHelper;

    public Db2SchemaResolveHelper(){
        this.tableHelper = new Db2TableResolveHelper();
    }

    public List<RuleDomain> fromCreateSchema(DB2CreateSchemaStatement statement) {
        Db2SchemaDomain domain = new Db2SchemaDomain();
        domain.setSqlType(SecQueryType.CREATE_SCHEMA);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractCatalogName(statement.getSchemaName()));
        domain.setSchema(extractName(statement.getSchemaName()));

        List<RuleDomain> result = new ArrayList<>();
        result.add(domain);

        if (statement.getCreateStatements() != null) {
            for (SQLCreateStatement stat : statement.getCreateStatements()) {
                if (stat instanceof SQLCreateTableStatement) {
                    ((SQLCreateTableStatement) stat).setSchema(domain.getSchema());
                    result.addAll(tableHelper.fromCreateTable((DB2CreateTableStatement) stat));
                } else if (stat instanceof SQLCreateViewStatement) {
                    throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
                } else if (stat instanceof SQLCreateSequenceStatement) {
                    throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
                } else {
                    throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
                }
            }

        }

        return result;
    }

    public List<RuleDomain> fromDropSchema(DB2DropSchemaStatement statement) {
        Db2SchemaDomain domain = new Db2SchemaDomain();
        domain.setSqlType(SecQueryType.DROP_SCHEMA);
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setOptions(Collections.emptyMap());

        domain.setSchema(extractName(statement.getName()));
        domain.setIfExists(statement.isIfExists());

        return Collections.singletonList(domain);
    }
}

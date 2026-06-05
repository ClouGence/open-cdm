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
package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerCreateSchemaStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerDropSchemaStatement;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSchemaDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsSchemaResolveHelper extends MsAbstractResolveHelper {

    private MsTableResolveHelper tableHelper;

    public MsSchemaResolveHelper(){
        this.tableHelper = new MsTableResolveHelper();
    }

    public List<RuleDomain> fromCreateSchema(SQLServerCreateSchemaStatement statement) {
        MsSchemaDomain domain = new MsSchemaDomain();
        domain.setSqlType(SecQueryType.CREATE_SCHEMA);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        if (statement.getSchemaName() == null) {
            String name = extractName(statement.getUserName());
            if (name.equalsIgnoreCase("current_user") || name.equalsIgnoreCase("current_role") || name.equalsIgnoreCase("session_user")) {
                // the UserName is dynamic
                throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
            } else {
                domain.setSchema(extractName(statement.getUserName()));
            }
        } else {
            domain.setCatalog(extractCatalogName(statement.getSchemaName()));
            domain.setSchema(extractName(statement.getSchemaName()));
        }

        if (statement.getUserName() != null) {
            domain.setOwner(extractName(statement.getUserName()));
        }

        List<RuleDomain> result = new ArrayList<>();
        result.add(domain);

        if (statement.getCreateStatements() != null) {
            for (SQLCreateStatement stat : statement.getCreateStatements()) {
                if (stat instanceof SQLCreateTableStatement) {
                    ((SQLCreateTableStatement) stat).setSchema(domain.getSchema());
                    result.addAll(tableHelper.fromCreateTable((SQLCreateTableStatement) stat));
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

    public List<RuleDomain> fromDropSchema(SQLServerDropSchemaStatement statement) {
        List<RuleDomain> result = new ArrayList<>();

        MsSchemaDomain domain = new MsSchemaDomain();
        domain.setSqlType(SecQueryType.DROP_SCHEMA);
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractSchemaName(statement.getSchemaName()));
        domain.setSchema(extractName(statement.getSchemaName()));

        if (statement.isIfExists()) {
            domain.setIfExists(true);
        }

        result.add(domain);

        return result;
    }
}

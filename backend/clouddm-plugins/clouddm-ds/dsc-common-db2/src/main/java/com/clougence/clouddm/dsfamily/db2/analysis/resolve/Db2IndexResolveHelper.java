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

import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;

public class Db2IndexResolveHelper extends Db2AbstractResolveHelper {

    public List<RuleDomain> fromCreateIndex(SQLCreateIndexStatement statement) {
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.setSqlType(SecQueryType.CREATE_INDEX);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        //tabDomain.setTableSchema(tabDomain.getSchema());
        domain.setTableName(extractName(((SQLExprTableSource) statement.getTable()).getName()));
        if (statement.getType() != null) {
            domain.setType(statement.getType());
        } else {
            domain.setType("index");
        }

        domain.setCatalog(null);
        //tabDomain.setSchema(tabDomain.getSchema());
        domain.setName(extractName(statement.getName()));
        domain.setComment(getString(statement.getComment()));

        domain.setColumns(new ArrayList<>());
        for (SQLSelectOrderByItem item : statement.getColumns()) {
            String columnName = getString(item.getExpr());
            domain.getColumns().add(columnName);
        }

        return Collections.singletonList(domain);
    }
//
//    public List<RuleDomain> fromDropIndex(SQLDropIndexStatement statement) {
//        RdbIndexDomain domain = new RdbIndexDomain();
//        domain.setSqlType(SecQueryType.DROP_INDEX);
//        domain.setSqlKind(SqlKind.DROP);
//        domain.setOptions(Collections.emptyMap());
//
//        //tabDomain.setTableSchema(tabDomain.getSchema());
//        domain.setTableName(extractName(statement.getTableName().getName()));
//        domain.setType("index");
//        //tabDomain.setSchema(tabDomain.getSchema());
//        domain.setName(extractName(statement.getIndexName()));
//
//        return Collections.singletonList(domain);
//    }

}

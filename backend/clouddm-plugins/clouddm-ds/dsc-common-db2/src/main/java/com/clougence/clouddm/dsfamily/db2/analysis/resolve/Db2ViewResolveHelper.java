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

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class Db2ViewResolveHelper extends Db2AbstractResolveHelper {

    public List<RuleDomain> fromAlterView(SQLAlterViewStatement statement) {
        RdbViewDomain domain = new RdbViewDomain();
        domain.setSqlType(SecQueryType.ALTER_VIEW);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getTableSource();
        SQLName tableName = tableSource.getName();
        domain.setView(extractName(tableName));

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromDropView(SQLDropViewStatement statement) {
        List<RuleDomain> dropList = new ArrayList<>();
        for (SQLExprTableSource item : statement.getTableSources()) {
            RdbViewDomain domain = new RdbViewDomain();
            domain.setSqlType(SecQueryType.DROP_VIEW);
            domain.setAuditKind(SecQueryKind.DROP);
            domain.setOptions(Collections.emptyMap());

            domain.setView(extractName(item.getName()));
            dropList.add(domain);
        }
        return dropList;
    }
}

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

import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsUseResolveHelper extends MsAbstractResolveHelper {

    public List<RuleDomain> fromSwitchCatalog(SQLUseStatement statement) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        domain.setSqlType(SecQueryType.SWITCH_CATALOG);
        domain.setAuditKind(SecQueryKind.OTHER);
        domain.setOptions(Collections.emptyMap());

        domain.setSchema(getString(statement.getDatabase()));

        return Collections.singletonList(domain);
    }
}

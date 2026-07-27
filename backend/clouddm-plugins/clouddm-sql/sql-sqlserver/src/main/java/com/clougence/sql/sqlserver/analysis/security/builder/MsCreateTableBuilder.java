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
package com.clougence.sql.sqlserver.analysis.security.builder;

import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.sql.common.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.sql.sqlserver.analysis.security.domain.MsTableDomain;

public class MsCreateTableBuilder extends CreateTableBuilder<MsTableDomain> {

    @Override
    protected MsTableDomain getTableDomain() {
        MsTableDomain domain = new MsTableDomain();
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setSqlType(RuleQueryType.CREATE_TABLE);
        return domain;
    }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.sql.common.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.sql.db2.analysis.security.domain.Db2TableDomain;

public class Db2CreateTableBuilder extends CreateTableBuilder<Db2TableDomain> {

    @Override
    protected Db2TableDomain getTableDomain() {
        Db2TableDomain domain = new Db2TableDomain();
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setSqlType(RuleQueryType.CREATE_TABLE);
        return domain;
    }
}

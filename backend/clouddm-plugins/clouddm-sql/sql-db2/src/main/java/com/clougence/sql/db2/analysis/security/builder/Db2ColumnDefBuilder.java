/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import com.clougence.sql.common.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.sql.db2.analysis.security.domain.Db2ColumnDomain;

public class Db2ColumnDefBuilder extends ColumnDefBuilder<Db2ColumnDomain> {

    @Override
    protected Db2ColumnDomain getColumnDomain() {
        Db2ColumnDomain domain = new Db2ColumnDomain();
        domain.setNullable(true);
        return domain;
    }
}

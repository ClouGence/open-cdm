/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import com.clougence.sql.common.analysis.secrules.builder.DropTableBuilder;
import com.clougence.sql.db2.analysis.security.domain.Db2TableDomain;

public class Db2DropTableBuilder extends DropTableBuilder<Db2TableDomain> {

    @Override
    protected Db2TableDomain getTableDomain() { return new Db2TableDomain(); }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import com.clougence.sql.common.analysis.secrules.builder.DropSchemaBuilder;
import com.clougence.sql.db2.analysis.security.domain.Db2SchemaDomain;

public class Db2DropSchemaBuilder extends DropSchemaBuilder<Db2SchemaDomain> {

    @Override
    protected Db2SchemaDomain getSchemaDomain() { return new Db2SchemaDomain(); }
}

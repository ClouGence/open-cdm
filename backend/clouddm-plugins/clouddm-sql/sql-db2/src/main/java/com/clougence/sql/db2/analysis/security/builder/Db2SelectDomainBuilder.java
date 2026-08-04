/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.sql.common.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.sql.common.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.sql.db2.analysis.security.domain.Db2SelectDomain;

public class Db2SelectDomainBuilder extends SelectDomainBuilder<Db2SelectDomain> {

    public Db2SelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected Db2SelectDomain getSelectDomain() { return new Db2SelectDomain(); }
}

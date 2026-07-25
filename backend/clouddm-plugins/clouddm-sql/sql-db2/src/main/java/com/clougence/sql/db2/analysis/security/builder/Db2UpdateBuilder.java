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

import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbUpdateDomain;
import com.clougence.sql.common.analysis.secrules.builder.UpdateBuilder;
import com.clougence.sql.common.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.sql.db2.analysis.security.domain.Db2UpdateDomain;

public class Db2UpdateBuilder extends UpdateBuilder {

    public Db2UpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() { return new Db2UpdateDomain(); }
}
